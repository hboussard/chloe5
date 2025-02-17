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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
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
	
	public static void calculMetrics(String metricsFile, String inputRaster, int radius, int[] values, List<String> compoMetrics, List<String> configMetrics, WindowDistanceType distanceType, int displacement, int[] unfilters) {
		
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		
		int ws = ((int) ((radius*2)/entete.cellsize()))+1;
		//System.out.println(ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(distanceType);
		builder.setCoverage(cov);
		builder.setValues(values);
		for(String cpm : compoMetrics) {
			builder.addMetric(cpm);
		}
		for(String cfm : configMetrics) {
			builder.addMetric(cfm);
		}
		builder.addWindowSize(ws);
		builder.setDisplacement(displacement);
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
	
	public static void splitCompoConfig(Set<String> dataFiles, String xyFile, String compoFile, String configFile, List<String> compoMetrics, List<String> configMetrics) {
		
		try {
			
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
			
			for(String dataFile : dataFiles) {
				
				CsvReader cr = new CsvReader(dataFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				
				CsvReader crm = new CsvReader(xyFile);
				crm.setDelimiter(';');
				crm.readHeaders();
			
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
			}
			
			cwCompo.close();
			cwConfig.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void standardize(String groupFile, List<String> groupMetrics) {
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
	
	public static void standardize(String groupFile, List<String> groupMetrics, float[][] distances) {
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
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					//group[index++] = coeff*Float.parseFloat(cr.get(gm));
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
				index = 0;
				for(String gm : groupMetrics) {
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					float v = d[index++];
					//float nv = coeff*v/inertie;
					float nv = v/inertie;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			/*
			for(float[] d : dGroup) {
				for(float v : d) {
					//float nv = v/divisor;
					float nv = v/inertie;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			*/
			
			cwNormGroup.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static float standardize(String groupFile, List<String> groupMetrics, Map<String, Float> importances, int scale) {
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
					group[index++] = importances.get(gm+"_"+scale+"m") * Float.parseFloat(cr.get(gm));
				}
				dGroup.add(group);
			}
			cr.close();
			
			float inertia = inertia(dGroup, groupMetrics.size());
			System.out.println(inertia);
			
			CsvWriter cwNormGroup = new CsvWriter(groupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			for(float[] d : dGroup) {
				for(float v : d) {
					//float nv = v/divisor;
					float nv = v/inertia;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			cwNormGroup.close();
			
			return inertia;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static float standardize(String groupFile, List<String> groupMetrics, Map<String, Float> importances, float[][] distances, int scale) {
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
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					//group[index++] = coeff*Float.parseFloat(cr.get(gm));
					group[index++] = importances.get(gm+"_"+scale+"m") * Float.parseFloat(cr.get(gm));
				}
				dGroup.add(group);
			}
			cr.close();
			
			float inertia = inertia(dGroup, groupMetrics.size());
			System.out.println(inertia);
			//float divisor = (float) (inertie * Math.sqrt(groupMetrics.size()));
			
			CsvWriter cwNormGroup = new CsvWriter(groupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			
			for(float[] d : dGroup) {
				index = 0;
				for(String gm : groupMetrics) {
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					float v = d[index++];
					//float nv = coeff*v/inertie;
					float nv = v/inertia;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			/*
			for(float[] d : dGroup) {
				for(float v : d) {
					//float nv = v/divisor;
					float nv = v/inertie;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			*/
			
			cwNormGroup.close();
			
			return inertia;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
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
	
	public static void compileStdCompoConfig(String stdFile, String compoStdFile, String configStdFile, List<String> compoMetrics, List<String> configMetrics, int scale) {
		
		try {
			CsvReader crCompo = new CsvReader(compoStdFile);
			crCompo.setDelimiter(';');
			crCompo.readHeaders();
			
			CsvReader crConfig= new CsvReader(configStdFile);
			crConfig.setDelimiter(';');
			crConfig.readHeaders();
			
			CsvWriter cwStd = new CsvWriter(stdFile);
			cwStd.setDelimiter(';');
			for(String cpm : compoMetrics) {
				cwStd.write(cpm+"_"+scale+"m");
			}
			for(String cfm : configMetrics) {
				cwStd.write(cfm+"_"+scale+"m");
			}
			cwStd.endRecord();
			
			while(crCompo.readRecord() && crConfig.readRecord()) {
				for(String cpm : compoMetrics) {
					cwStd.write(crCompo.get(cpm));
				}
				for(String cfm : configMetrics) {
					cwStd.write(crConfig.get(cfm));
				}
				cwStd.endRecord();
			}
			
			crCompo.close();
			crConfig.close();
			cwStd.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void compileStandardizedGroup(String stdFile, String compoStdFile, String configStdFile, List<String> compoMetrics, List<String> configMetrics, int scale) {
		
		try {
			CsvReader crCompo = new CsvReader(compoStdFile);
			crCompo.setDelimiter(';');
			crCompo.readHeaders();
			
			CsvReader crConfig= new CsvReader(configStdFile);
			crConfig.setDelimiter(';');
			crConfig.readHeaders();
			
			CsvWriter cwStd = new CsvWriter(stdFile);
			cwStd.setDelimiter(';');
			for(String cpm : compoMetrics) {
				cwStd.write(cpm+"_"+scale+"m");
			}
			for(String cfm : configMetrics) {
				cwStd.write(cfm+"_"+scale+"m");
			}
			cwStd.endRecord();
			
			while(crCompo.readRecord() && crConfig.readRecord()) {
				for(String cpm : compoMetrics) {
					cwStd.write(crCompo.get(cpm));
				}
				for(String cfm : configMetrics) {
					cwStd.write(crConfig.get(cfm));
				}
				cwStd.endRecord();
			}
			
			crCompo.close();
			crConfig.close();
			cwStd.close();
			
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
					//System.out.println(index+" / "+size);
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
			//kmeans.setMaxIterations(100);
			kmeans.setMaxIterations(1000);
			kmeans.setNumClusters(k);
			kmeans.setNumExecutionSlots(32);
			//kmeans.setPreserveInstancesOrder(false);
			//kmeans.setFastDistanceCalc(true);
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.KMEANS_PLUS_PLUS, SimpleKMeans.TAGS_SELECTION));
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.RANDOM, SimpleKMeans.TAGS_SELECTION));
			kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.FARTHEST_FIRST, SimpleKMeans.TAGS_SELECTION));
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.CANOPY, SimpleKMeans.TAGS_SELECTION));
			
			EuclideanDistance distanceFunction = new EuclideanDistance();
			//ManhattanDistance distanceFunction = new ManhattanDistance();
			distanceFunction.setDontNormalize(true);
			kmeans.setDistanceFunction(distanceFunction);
			
			//((NormalizableDistance) kmeans.getDistanceFunction()).setDontNormalize(true);
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
	
	public static void exportCSV(String ecoFile, SimpleKMeans kmeans, int k, String[][] dataXY, Instances data, int index) {
		
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
				writer.write((1+kmeans.clusterInstance(data.get(i+((index-1)*dataXY.length))))+"");
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
				//System.out.println(i+"/"+dataXY.length);
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
	
	public static void exportCSV2(String ecoFile, SimpleKMeans kmeans, int k, String[][] dataXY, String dataFile, Instances data) {
		
		try {
			Map<Integer, float[]> mapKmeans = new TreeMap<Integer, float[]>();
			Instances clusterCentroids = kmeans.getClusterCentroids();
			float[] dataKmeans;
			for(int ki=1; ki<=k; ki++) {
				Instance centroid = clusterCentroids.instance(ki-1);
				dataKmeans = new float[centroid.numValues()];
				for(int j=0; j<centroid.numValues(); j++) {
					dataKmeans[j] = (float) centroid.value(j);
				}
				mapKmeans.put(ki, dataKmeans);
			}
			
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
			//Instance instance;
			//int closeKmean;
			for(int i=0; i<dataXY.length; i++){
				//System.out.println(i+"/"+dataXY.length);
				stringValues = reader.readLine().split(";");
				values = new double[stringValues.length];
				for(int j=0; j<stringValues.length; j++) {
					values[j] = Double.parseDouble(stringValues[j]);
				}
				
				//instance = new DenseInstance(1.0, values);
				//instance.setDataset(data);
				
				//closeKmean = getClosestKmean(mapKmeans, values);
				
				iXY = dataXY[i];
				writer.write(iXY[0]+";");
				writer.write(iXY[1]+";");
				//writer.write((1+kmeans.clusterInstance(instance))+"");
				writer.write((getClosestKmean(mapKmeans, values))+"");
				writer.newLine();
			}
			
			writer.close();
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int getClosestKmean(Map<Integer, float[]> mapKmeans, double[] values) {
		float minDistance = Float.MAX_VALUE;
		int closeKmean = -1;
		float distance;
		for(Entry<Integer, float[]> e : mapKmeans.entrySet()) {
			distance = distance(e.getValue(), values);
			if(distance < minDistance) {
				minDistance = distance;
				closeKmean = e.getKey();
			}
		}
		
		return closeKmean;
	}

	public static void exportInfo(String infoFile, SimpleKMeans kmeans, int k, Instances data, Map<String, Float> importances) {
		
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(infoFile));
			writer.write("classe");
			String[] metrics = new String[data.numAttributes()];
			int[] export = new int[data.numAttributes()];
			for(int ai=0; ai<data.numAttributes(); ai++) {
				metrics[ai] = data.attribute(ai).name();
				
				if(importances.get(metrics[ai]) > 0) {
					writer.write(";"+metrics[ai]);
					export[ai] = 1;
				}
			}
			writer.newLine();
			
			Instances clusterCentroids = kmeans.getClusterCentroids();
			for(int ki=1; ki<=k; ki++) {
				writer.write(ki+"");
				Instance centroid = clusterCentroids.instance(ki-1);
				for(int j=0; j<centroid.numValues(); j++) {
					if(export[j] == 1) {
						writer.write(";"+((float) centroid.value(j)));
					}
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
					//cw.write(cr.getHeader(h)+"_"+e.getKey()+"m");
					cw.write(cr.getHeader(h));
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
	
	public static void analyseRuptures(String outputRaster, String[] mapRasters, String[] thematicDistanceFiles, EnteteRaster entete) {
		
		float[] outData = new float[entete.width()*entete.height()];
		int size = mapRasters.length;
		int ind = 0;
		for(String mapRaster : mapRasters) {
			
			float[] localData = new float[entete.width()*entete.height()];
			
			LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
			builder.addRasterFile(mapRaster);
			//builder.addMetric("SHDI");
			builder.addMetric("RaoQ");
			builder.setThematicDistanceFile(thematicDistanceFiles[ind++]);
			//builder.addWindowSize(7);
			builder.addWindowSize(21);
			//builder.addWindowSize(51);
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
	
	public static void analyseGradient(String gradientCsv, String[][] dataXY, String kmeanFile, String normFile) {
		
		try {
			
			CsvWriter cw = new CsvWriter(gradientCsv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(kmeanFile);
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
	
	public static void generateThematicDistanceFile(String infoFile, String thematicDistanceFile) {
		
		try {	
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(infoFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int k;
			int max = -1;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				max = Math.max(max, k);
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
			}
			crI.close();
			
			float[][] distance = new float[max+1][max+1];
			for(Entry<Integer, float[]> e1 : kmeans.entrySet()) {
				for(Entry<Integer, float[]> e2 : kmeans.entrySet()) {
					if(e1.getKey() < e2.getKey()) {
						float d = distance(e1.getValue(), e2.getValue());
						distance[e1.getKey()][e2.getKey()] = d;
						distance[e2.getKey()][e1.getKey()] = d;
					}
				}	
			}
			
			CsvWriter cw = new CsvWriter(thematicDistanceFile);
			cw.setDelimiter(';');
			cw.write("distance");
			for(Integer ki : kmeans.keySet()) {
				cw.write(ki.toString());
			}
			cw.endRecord();
			for(Integer k1 : kmeans.keySet()) {
				cw.write(k1.toString());
				for(Integer k2 : kmeans.keySet()) {
					cw.write(distance[k1][k2]+"");	
				}
				cw.endRecord();
			}
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
	
	private static float distance(float[] v1, double[] v2) {
		double distance = 0;
		for(int i=0; i<v1.length; i++) {
			distance += Math.pow(v1[i]-v2[i], 2);
		}
		return (float) Math.sqrt(distance);		
	}
	
	public static Map<String, Float> initImportanceByMetric(String importanceFile){
		try {
			CsvReader cr = new CsvReader(importanceFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Float> mapImportances = new HashMap<String, Float>();
			while(cr.readRecord()){
				float importance = Float.parseFloat(cr.get("importance"));
				mapImportances.put(cr.get("metric"), importance);
			}
			cr.close();
			
			return mapImportances;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Float> initImportanceByCode(String importanceFile, boolean composition, boolean configuration, int[] scales) {
		
		if(new File(importanceFile).exists()) {
			try {
				CsvReader cr = new CsvReader(importanceFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				
				Map<Integer, Float> codeImportances = new TreeMap<Integer, Float>();
				while(cr.readRecord()){
					int code = Integer.parseInt(cr.get("code"));
					float importance = Float.parseFloat(cr.get("importance"));
					codeImportances.put(code, importance);
				}
				cr.close();
				
				Map<String, Float> mapImportances = new HashMap<String, Float>();
				for(int code1 : codeImportances.keySet()) {
					if(composition) {
						for(int scale : scales) {
							mapImportances.put("pNV_"+code1+"_"+scale+"m", codeImportances.get(code1));
						}
					}
					if(configuration) {
						for(int code2 : codeImportances.keySet()) {
							if(code1 < code2) {
								for(int scale : scales) {
									mapImportances.put("pNC_"+code1+"-"+code2+"_"+scale+"m", codeImportances.get(code1) * codeImportances.get(code2));
								}
							}
						}
					}
				}
				
				return mapImportances;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("WARNING: Thematic importance file "+importanceFile+" doesn't exists.");
		}
		
		return null;
	}

	public static void exportInertia(String inertiaFile, Map<String, Float> inerties) {
		
		try {
		
			File file = new File(inertiaFile);
		
			if(file.exists()) {
				CsvReader cr = new CsvReader(inertiaFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()) {
					inerties.put(cr.get("group"), Float.valueOf(cr.get("inertia")));
				}
				cr.close();
			}
		
			CsvWriter cw = new CsvWriter(inertiaFile);
			cw.setDelimiter(';');
			cw.write("group");
			cw.write("inertia");
			cw.endRecord();
			for(Entry<String, Float> e : inerties.entrySet()) {
				cw.write(e.getKey());
				cw.write(e.getValue()+"");
				cw.endRecord();
			}
			cw.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
