package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
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
	
	/*
	public static void calculMetrics(String metricsFile, String inputRaster, int radius, List<String> compoMetrics, List<String> configMetrics) {
		calculMetrics(metricsFile, inputRaster, radius, compoMetrics, configMetrics, new int[]{-1});
	}
	*/
	
	public static void calculMetrics(String metricsFile, String inputRaster, int radius, List<String> compoMetrics, List<String> configMetrics, int[] unfilters) {
		
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
		builder.setUnfilters(unfilters);
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
	
	/*
	public static Instances readData(String dataFile, int factor, int size) {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String[] attributs = reader.readLine().split(";");
			
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			for(int i=0; i<attributs.length; i++) {
				attributes.add(new Attribute(attributs[i]));
			}
			
			Instances data = new Instances("metrics", attributes, size/factor);
			String line;
			String[] stringValues;
			double[] values;
			Instance instance;
			int index = 0;
			while((line = reader.readLine()) != null) {
				if(index++ % factor == 0) {
					//System.out.println(index+" / "+size);
					stringValues = line.split(";");
					values = new double[stringValues.length];
					for(int i=0; i<stringValues.length; i++) {
						values[i] = Double.parseDouble(stringValues[i]);
					}
					instance = new DenseInstance(1.0, values);
					data.add(instance);
				}
			}
			
			reader.close();
			
			return data;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	*/
	
	public static Instances readData(String dataFile, String[][] dataXY, int factor, EnteteRaster entete) {
		
		try {
			
			int size = 0;
			int x, y;
			for(String[] dXY : dataXY) {
				x = CoordinateManager.getLocalX(entete, Double.parseDouble(dXY[0]));
				y = CoordinateManager.getLocalY(entete, Double.parseDouble(dXY[1]));
				if(x%factor == 0 && y%factor == 0) {
					size++;
				}
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String[] attributs = reader.readLine().split(";");
			
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			for(int i=0; i<attributs.length; i++) {
				attributes.add(new Attribute(attributs[i]));
			}
			
			Instances data = new Instances("metrics", attributes, size);
			String line;
			String[] stringValues;
			double[] values;
			Instance instance;
			int index = 0;
			while((line = reader.readLine()) != null) {
				x = CoordinateManager.getLocalX(entete, Double.parseDouble(dataXY[index][0]));
				y = CoordinateManager.getLocalY(entete, Double.parseDouble(dataXY[index][1]));
				if(x%factor == 0 && y%factor == 0) {
					System.out.println(index+" / "+size);
					stringValues = line.split(";");
					values = new double[stringValues.length];
					for(int i=0; i<stringValues.length; i++) {
						values[i] = Double.parseDouble(stringValues[i]);
					}
					instance = new DenseInstance(1.0, values);
					data.add(instance);
				}
				index++;
			}
			
			reader.close();
			
			return data;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
	
	public static void exportCSV(String ecoFile, SimpleKMeans kmeans, int k, String[][] dataXY, String dataFile, Instances data) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ecoFile));
			writer.write("X;");
			writer.write("Y;");
			writer.write("ecop_"+k);
			writer.newLine();
			
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			reader.readLine(); // 1ere ligne d'attributs
			
			String[] iXY;
			String[] stringValues;
			double[] values;
			Instance instance;
			for(int i=0; i<dataXY.length; i++){
				
				stringValues = reader.readLine().split(";");
				values = new double[stringValues.length];
				for(int j=0; j<stringValues.length; j++) {
					values[j] = Double.parseDouble(stringValues[j]);
				}
				instance = new DenseInstance(1.0, values);
				instance.setDataset(data);
				
				iXY = dataXY[i];
				writer.write(iXY[0]+";");
				writer.write(iXY[1]+";");
				writer.write((1+kmeans.clusterInstance(instance))+"");
				writer.newLine();
			}
			
			writer.close();
			reader.close();
			
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
	
	
	public static EnteteRaster getHeader(String headerFile, int noDataValue) {
	
		EnteteRaster entete = EnteteRaster.read(headerFile);
		
		entete.setNoDataValue(noDataValue);
		
		return entete;
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
	
	public static void analyseRuptures(String outputRaster, String[] mapRasters, EnteteRaster entete) {
		
		float[] outData = new float[entete.width()*entete.height()];
		int size = mapRasters.length;
		for(String mapRaster : mapRasters) {
			
			float[] localData = new float[entete.width()*entete.height()];
			
			LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
			builder.addRasterFile(mapRaster);
			builder.addMetric("SHDI");
			builder.addWindowSize(7);
			builder.setUnfilters(new int[]{-1});
			builder.addTabOutput(localData);
			LandscapeMetricAnalysis analysis = builder.build();
			analysis.allRun();
			
			for(int i=0; i<outData.length; i++) {
				outData[i] += localData[i]/size;
			}
		}
		
		CoverageManager.write(outputRaster, outData, entete);
		
	}
	
	public static void analyseGradient(String gradientCsv, String[][] dataXY, String infoFile, String normFile) {
		
		try {
			
			CsvWriter cw = new CsvWriter(gradientCsv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(infoFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				cw.write("ecop_"+crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
			}
			cw.endRecord();
			crI.close();
			
			CsvReader crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			int index = 0;
			while(crN.readRecord()) {
				String[] xy = dataXY[index++];
				cw.write(xy[0]);
				cw.write(xy[1]);
				float[] data = new float[crN.getColumnCount()];
				for(int i=0; i<crN.getColumnCount(); i++) {
					data[i] = Float.parseFloat(crN.get(i));
				}
				for(k=1; k<kmeans.size()+1; k++) {
					float d = distance(kmeans.get(k), data);
					cw.write(d+"");
				}
				cw.endRecord();
			}
			crN.close();			
						
			cw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	private static float distance(float[] v1, float[] v2) {
		double distance = 0;
		for(int i=0; i<v1.length; i++) {
			distance += Math.pow(v1[i]-v2[i], 2);
		}
		return (float) Math.sqrt(distance);		
	}
	
}
