package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;

public class EcoPaysage {

	public static int[] getCodes(String raster) {
		
		Coverage cov = CoverageManager.getCoverage(raster);
		EnteteRaster entete = cov.getEntete();
		int[] codes = Util.readValuesHugeRoi(cov, new Rectangle(0, 0, entete.width(), entete.height()), entete.noDataValue());
		cov.dispose();
		
		return codes;
	}
	
	public static void calculMetrics(String metricsFile, String inputRaster, int radius, List<String> compoMetrics, List<String> configMetrics) {
		
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		
		int ws = ((int) ((radius*2)/entete.cellsize()))+1;
		//System.out.println(ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setCoverage(cov);
		for(String cpm : compoMetrics) {
			builder.addMetric(cpm);
		}
		for(String cfm : configMetrics) {
			builder.addMetric(cfm);
		}
		builder.addWindowSize(ws);
		builder.setDisplacement(20);
		builder.setUnfilters(new int[]{-1});
		builder.addCsvOutput(metricsFile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
		cov.dispose();
	}
	
	public static void generateMask(String metricsFile, String xyFile) {
		try {
			CsvReader cr = new CsvReader(metricsFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			CsvWriter cwXY = new CsvWriter(xyFile);
			cwXY.setDelimiter(';');
			cwXY.write("X");
			cwXY.write("Y");
			cwXY.endRecord();
			
			while(cr.readRecord()) {
				cwXY.write(cr.get("X"));
				cwXY.write(cr.get("Y"));
				cwXY.endRecord();
			}
			
			cr.close();
			cwXY.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void splitCompoConfig(String dataFile, String xyFile, String compoFile, String configFile, List<String> compoMetrics, List<String> configMetrics) {
		
		try {
			CsvReader cr = new CsvReader(dataFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			CsvReader crm = new CsvReader(xyFile);
			crm.setDelimiter(';');
			crm.readHeaders();
			
			CsvWriter cwCompo = new CsvWriter(compoFile);
			cwCompo.setDelimiter(';');
			for(String cpm : compoMetrics) {
				cwCompo.write(cpm);
			}
			cwCompo.endRecord();
			
			CsvWriter cwConfig = new CsvWriter(configFile);
			cwConfig.setDelimiter(';');
			for(String cfm : configMetrics) {
				cwConfig.write(cfm);
			}
			cwConfig.endRecord();
			
			crm.readRecord();
			double mX = Double.parseDouble(crm.get("X"));
			double mY = Double.parseDouble(crm.get("Y"));
			double tolerance = 30;
			while(cr.readRecord()) {
				double X = Double.parseDouble(cr.get("X"));
				double Y = Double.parseDouble(cr.get("Y"));
				
				//System.out.println(X+" "+mX);
				
				if(Math.abs(X-mX) < tolerance && Math.abs(Y-mY) < tolerance) {
					for(String cpm : compoMetrics) {
						if(cr.get(cpm).equalsIgnoreCase("")) {
							System.out.println(cpm+" vide");
						}
						cwCompo.write(cr.get(cpm));
					}
					cwCompo.endRecord();
					for(String cfm : configMetrics) {
						if(cr.get(cfm).equalsIgnoreCase("")) {
							System.out.println(cfm+" vide");
						}
						cwConfig.write(cr.get(cfm));
					}
					cwConfig.endRecord();
					
					if(crm.readRecord()) {
						mX = Double.parseDouble(crm.get("X"));
						mY = Double.parseDouble(crm.get("Y"));
					}else {
						break;
					}
				}
			}
			cr.close();
			crm.close();
			cwCompo.close();
			cwConfig.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void normalize(String groupFile, List<String> groupMetrics) {
		try {
			CsvReader cr = new CsvReader(groupFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			List<float[]> dGroup = new ArrayList<float[]>();
			float[] group;
			int index;
			while(cr.readRecord()) {
				group = new float[groupMetrics.size()];
				index = 0;
				for(String gm : groupMetrics) {
					group[index++] = Float.parseFloat(cr.get(gm));
				}
				dGroup.add(group);
			}
			cr.close();
			
			float inertie = inertia(dGroup, groupMetrics.size());
			System.out.println(inertie);
			
			//float divisor = (float) (inertie * Math.sqrt(groupMetrics.size()));
			
			CsvWriter cwNormGroup = new CsvWriter(groupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			for(float[] d : dGroup) {
				for(float v : d) {
					//float nv = v/divisor;
					float nv = v/inertie;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			cwNormGroup.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static float inertia(List<float[]> data, int nbMetrics) {
		
		float[] means = new float[nbMetrics];
		for(float[] d : data) {
			for(int i=0; i<nbMetrics; i++) {
				means[i] += d[i];
			}
		}
		for(int i=0; i<nbMetrics; i++) {
			means[i] /= data.size();
		}
		
		double sumdistcarre;
		float v ;
		double rowSum = 0;
		for(float[] d : data) {
			sumdistcarre = 0;
			for(int i=0; i<nbMetrics; i++) {
				v = d[i];
				sumdistcarre += Math.pow(means[i]-v, 2);
			}
			rowSum += sumdistcarre;
		}
		
		return (float) Math.sqrt(rowSum/data.size());	
	}
	
	public static void compileNormCompoConfig(String normFile, String compoNormFile, String configNormFile, List<String> compoMetrics, List<String> configMetrics) {
		
		try {
			CsvReader crCompo = new CsvReader(compoNormFile);
			crCompo.setDelimiter(';');
			crCompo.readHeaders();
			
			CsvReader crConfig= new CsvReader(configNormFile);
			crConfig.setDelimiter(';');
			crConfig.readHeaders();
			
			CsvWriter cwNorm = new CsvWriter(normFile);
			cwNorm.setDelimiter(';');
			for(String cpm : compoMetrics) {
				cwNorm.write(cpm);
			}
			for(String cfm : configMetrics) {
				cwNorm.write(cfm);
			}
			cwNorm.endRecord();
			
			while(crCompo.readRecord() && crConfig.readRecord()) {
				for(String cpm : compoMetrics) {
					cwNorm.write(crCompo.get(cpm));
				}
				for(String cfm : configMetrics) {
					cwNorm.write(crConfig.get(cfm));
				}
				cwNorm.endRecord();
			}
			
			crCompo.close();
			crConfig.close();
			cwNorm.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[][] importXY(String masqueFile) {
		try {
			CsvReader cr = new CsvReader(masqueFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			List<String[]> dXY = new ArrayList<String[]>();
			String[] xy;
			while(cr.readRecord()) {
				xy = new String[2];
				xy[0] = cr.get("X");
				xy[1] = cr.get("Y");
				dXY.add(xy);
			}
			
			cr.close();
			
			return dXY.toArray(new String[dXY.size()][]);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Instances readData(String dataFile) {
	    try {
	    	CSVLoader loader = new CSVLoader();
			loader.setSource(new File(dataFile));
			loader.setFieldSeparator(";");
		    //loader.setMissingValue("-1"); // not generic
		    Instances instances = loader.getDataSet();
		   
		    return instances;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public static SimpleKMeans kmeans(Instances data, int k){	
		
		try {
			SimpleKMeans kmeans = new SimpleKMeans();
			//kmeans.setSeed(1);
			kmeans.setMaxIterations(100);
			kmeans.setNumClusters(k);
			kmeans.setNumExecutionSlots(32);
			//kmeans.setPreserveInstancesOrder(false);
			//kmeans.setFastDistanceCalc(true);
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.KMEANS_PLUS_PLUS, SimpleKMeans.TAGS_SELECTION));
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.RANDOM, SimpleKMeans.TAGS_SELECTION));
			kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.FARTHEST_FIRST, SimpleKMeans.TAGS_SELECTION));
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.CANOPY, SimpleKMeans.TAGS_SELECTION));
			kmeans.buildClusterer(data);
		
			return kmeans;
			
			//System.out.println(kmeans);
			//System.out.println(nbClasses+" classes, squared error : "+kmeans.getSquaredError());
			
			/*
			writer = new BufferedWriter(new FileWriter(csvOutput.replace(".csv", "_eval.txt")));
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(kmeans);
			eval.evaluateClusterer(new Instances(data));
			writer.append(eval.clusterResultsToString());
			writer.newLine();
			writer.close();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}	
	
	public static void exportCSV(String ecoFile, SimpleKMeans kmeans, int k, String[][] dataXY, Instances data) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ecoFile));
			writer.write("X;");
			writer.write("Y;");
			writer.write("ecop_"+k);
			writer.newLine();
			
			String[] iXY;
			for(int i=0; i<dataXY.length; i++){
				iXY = dataXY[i];
				writer.write(iXY[0]+";");
				writer.write(iXY[1]+";");
				writer.write((1+kmeans.clusterInstance(data.get(i)))+"");
				writer.newLine();
			}
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportInfo(String infoFile, SimpleKMeans kmeans, int k, Instances data) {
		
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(infoFile));
			writer.write("classe");
			String[] metrics = new String[data.numAttributes()];
			for(int ai=0; ai<data.numAttributes(); ai++) {
				metrics[ai] = data.attribute(ai).name();
				writer.write(";"+metrics[ai]);
			}
			writer.newLine();
			
			Instances clusterCentroids = kmeans.getClusterCentroids();
			for(int ki=1; ki<=k; ki++) {
				writer.write(ki+"");
				Instance centroid = clusterCentroids.instance(ki-1);
				for(int j=0; j<centroid.numValues(); j++) {
					writer.write(";"+((float) centroid.value(j)));
				}
				writer.newLine();
			}
											
			writer.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static EnteteRaster getHeader(String headerFile) {
	
		return EnteteRaster.read(headerFile);
	}
	
	public static void exportMap(String outputRaster, String ecoFile, int k, EnteteRaster header) {
		
		SpatialCsvManager.exportRaster(ecoFile, outputRaster, "ecop_"+k, header);
	}

	public static void compileFiles(String normFile, int size, Map<Integer, String> normFiles) {

		try {
			
			CsvWriter cw = new CsvWriter(normFile);
			cw.setDelimiter(';');
			Map<Integer, CsvReader> readers = new TreeMap<Integer, CsvReader>();
			for(Entry<Integer, String> e : normFiles.entrySet()) {
				CsvReader cr = new CsvReader(e.getValue());
				cr.setDelimiter(';');
				cr.readHeaders();
				for(int h=0; h<cr.getHeaderCount(); h++) {
					cw.write(cr.getHeader(h)+"_"+e.getKey()+"m");
				}
				readers.put(e.getKey(), cr);
			}
			cw.endRecord();
			
			for(int i=0; i<size; i++) {
				for(CsvReader cr : readers.values()) {
					cr.readRecord();
					for(int j=0; j<cr.getColumnCount(); j++) {
						cw.write(cr.get(j));
					}
				}
				cw.endRecord();
			}
			
			for(CsvReader cr : readers.values()) {
				cr.close();
			}
		
			cw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
