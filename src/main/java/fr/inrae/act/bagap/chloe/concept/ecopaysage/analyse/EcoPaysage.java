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
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.analysis.Stats;
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

	private static Stats stats = new Stats();
	
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
	
	public static void calculateCompoMetrics(String metricsFile, String inputRaster, int radius, int[] values, List<String> compoMetrics, WindowDistanceType distanceType, int displacement, int[] unfilters) {
		
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
		builder.addWindowSize(ws);
		builder.setDisplacement(displacement);
		builder.setUnfilters(unfilters);
		builder.addCsvOutput(metricsFile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
		cov.dispose();
	}
	
	public static void calculateConfigMetrics(String metricsFile, String inputRaster, int radius, int[] values, List<String> configMetrics, WindowDistanceType distanceType, int displacement, int[] unfilters) {
		
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		
		int ws = ((int) ((radius*2)/entete.cellsize()))+1;
		//System.out.println(ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(distanceType);
		builder.setCoverage(cov);
		builder.setValues(values);
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
	
	public static void calculateCompoConfigMetrics(String metricsFile, String inputRaster, int radius, int[] values, List<String> compoMetrics, List<String> configMetrics, WindowDistanceType distanceType, int displacement, int[] unfilters) {
		
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
	
	public static int splitCompoConfig(Set<String> dataFiles, String xyFile, String compoFile, String configFile, List<String> compoMetrics, List<String> configMetrics) {
		
		try {
			
			CsvWriter cwCompo = new CsvWriter(compoFile);
			cwCompo.setDelimiter(';');
			for(String cpm : compoMetrics) {
				cwCompo.write(cpm);
			}
			cwCompo.endRecord();
			
			/*
			CsvWriter cwConfig = new CsvWriter(configFile);
			cwConfig.setDelimiter(';');
			for(String cfm : configMetrics) {
				cwConfig.write(cfm);
			}
			cwConfig.endRecord();
			*/
			int count = 0;
			
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
							
							//if(cr.get(cpm).equalsIgnoreCase("")) {
							//	System.out.println(cpm+" vide");
							//}
							
							cwCompo.write(cr.get(cpm));
						}
						cwCompo.endRecord();
						
						/*
						for(String cfm : configMetrics) {
							
							//if(cr.get(cfm).equalsIgnoreCase("")) {
							//	System.out.println(cfm+" vide");
							//}
							
							cwConfig.write(cr.get(cfm));
						}
						cwConfig.endRecord();
						*/
						count++;
						
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
			//cwConfig.close();
			
			return count;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private static void standardize(String groupFile, List<String> groupMetrics, float[][] distances) {
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
			
			float divisor = divisor(dGroup, groupMetrics.size());
			System.out.println("divisor "+divisor);
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
					float nv = v/divisor;
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
	
	public static void standardizeExternal(String stdGroupFile, String groupFile, Map<String, String> groupMetrics, int times) {
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
				for(String gm : groupMetrics.keySet()) {
					group[index++] = Float.parseFloat(cr.get(gm));
				}
				dGroup.add(group);
			}
			cr.close();
			
			float divisor = divisor(dGroup, groupMetrics.size());
			System.out.println("divisor "+divisor);
			
			//float divisor = (float) (inertie * Math.sqrt(groupMetrics.size()));
			
			CsvWriter cwNormGroup = new CsvWriter(stdGroupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics.values()) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			for(int t=0; t<times; t++) {
				for(float[] d : dGroup) {
					for(index = 0; index<d.length; index++) {
						
						float v = d[index];
						//float nv = v;
						float nv = v/divisor;
						
						//test
						//d[index] = nv;
						
						cwNormGroup.write(nv+"");
					}
					cwNormGroup.endRecord();
				}
			}
			
			// test
			//System.out.println(inertia1(dGroup, groupMetrics.size())+" "+ecarttype(dGroup, groupMetrics.size()));
			
			cwNormGroup.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static float standardizeCompo(String groupFile, int count, List<String> groupMetrics, Map<String, Float> importances, int scale) {
		try {
			CsvReader cr = new CsvReader(groupFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			//List<float[]> dGroup = new ArrayList<float[]>();
			float[][] dGroup = new float[count][];
			
			float[] group;
			int index;
			int indexCount = 0;
			while(cr.readRecord()) {
				group = new float[groupMetrics.size()];
				index = 0;
				for(String gm : groupMetrics) {
					group[index++] = importances.get(gm+"_"+scale+"m") * Float.parseFloat(cr.get(gm));
				}
				
				dGroup[indexCount++] = group;
				//dGroup.add(group);
			}
			cr.close();
			
			float divisor = divisor(dGroup, groupMetrics.size());
			System.out.println("divisor "+divisor);
			
			CsvWriter cwNormGroup = new CsvWriter(groupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			for(float[] d : dGroup) {
				for(index = 0; index<d.length; index++) {
					
					float v = d[index];
					//float nv = v;
					float nv = v/divisor;
					//float nv = v/(inertia*(12/78.0f));
					//float nv = (v/inertia) + 10;
					
					//test
					//d[index] = nv;
					
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			// test
			//System.out.println(inertia1(dGroup, groupMetrics.size())+" "+ecarttype(dGroup, groupMetrics.size()));
			
			cwNormGroup.close();
			
			return divisor;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static float standardizeConfig(String groupFile, int count, List<String> groupMetrics, Map<String, Float> importances, float[][] distances, int scale) {
		try {
			CsvReader cr = new CsvReader(groupFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			//List<float[]> dGroup = new ArrayList<float[]>();
			float[][] dGroup = new float[count][groupMetrics.size()];
			
			float[] group;
			int index;
			int indexCount = 0;
			while(cr.readRecord()) {
				group = new float[groupMetrics.size()];
				index = 0;
				System.out.println(indexCount+" / "+count);
				for(String gm : groupMetrics) {
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					//group[index++] = coeff*Float.parseFloat(cr.get(gm));
					group[index++] = importances.get(gm+"_"+scale+"m") * Float.parseFloat(cr.get(gm));
				}
				//dGroup.add(group);
				dGroup[indexCount++] = group;
			}
			cr.close();
			
			float divisor = divisor(dGroup, groupMetrics.size());
			System.out.println("divisor "+divisor);
			//float divisor = (float) (inertie * Math.sqrt(groupMetrics.size()));
			
			CsvWriter cwNormGroup = new CsvWriter(groupFile);
			cwNormGroup.setDelimiter(';');
			for(String gm : groupMetrics) {
				cwNormGroup.write(gm);
			}
			cwNormGroup.endRecord();
			
			for(float[] d : dGroup) {
				for(index = 0; index<d.length; index++) {
					
					//float coeff = distances[Couple.getOneFromPNC(gm)][Couple.getOtherFromPNC(gm)];
					float v = d[index];
					//float nv = coeff*v/inertie;
					//float nv = v;
					float nv = v/divisor;
					//float nv = v/(inertia*(66/78.0f));
					
					//test
					//d[index] = nv;
					
					//float nv = (v/inertia) + 2;
					cwNormGroup.write(nv+"");
				}
				cwNormGroup.endRecord();
			}
			
			// test
			//System.out.println(inertia1(dGroup, groupMetrics.size())+" "+ecarttype(dGroup, groupMetrics.size()));
			
			cwNormGroup.close();
			
			return divisor;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static float divisor(float[][] data, int nbMetrics) {
		
		//analyseStats(data, nbMetrics);
		
		return inertia(data, nbMetrics);
		//return 1.0f/inertia(data, nbMetrics);
		//return moyenne(data, nbMetrics);
		//return moyenne(data, nbMetrics)/inertia(data, nbMetrics);
	}
	
	public static float divisor(List<float[]> data, int nbMetrics) {
		
		//analyseStats(data, nbMetrics);
		
		return inertia(data, nbMetrics);
		//return 1.0f/inertia(data, nbMetrics);
		//return moyenne(data, nbMetrics);
		//return moyenne(data, nbMetrics)/inertia(data, nbMetrics);
	}
	
	private static void analyseStats(List<float[]> data, int nbMetrics) {
		stats.reset();
		
		for(float[] d : data) {
			for(int i=0; i<nbMetrics; i++) {
				stats.add((float) d[i]);
			}
		}
		
		stats.calculate();
		
		System.out.println("stats");
		System.out.println("size "+stats.size());
		System.out.println("moyenne "+stats.getAverage());
		System.out.println("somme carre"+stats.getSquareSum());
		System.out.println("variance "+stats.getVariance());
		System.out.println("ecart-type "+stats.getStandardDeviation());
		System.out.println("coeff var "+stats.getVariationCoefficient());
		System.out.println("erreur std "+stats.getStandardError());
	}
	
	public static float inertia(float[][] data, int nbMetrics) {
		
		double[] means = new double[nbMetrics];
		for(float[] d : data) {
			for(int i=0; i<nbMetrics; i++) {
				means[i] += d[i];
			}
		}
		for(int i=0; i<nbMetrics; i++) {
			means[i] /= data.length;
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
		
		return (float) Math.sqrt(rowSum/data.length);
	}
	
	public static float inertia(List<float[]> data, int nbMetrics) {
		
		double[] means = new double[nbMetrics];
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
	
	public static float moyenne(List<float[]> data, int nbMetrics) {
		
		double sum = 0;
		for(float[] d : data) {
			for(int i=0; i<nbMetrics; i++) {
				sum += d[i];
			}
		}
		
		return (float) (sum / (data.size()*nbMetrics));
	}
	
	private static void compileStdCompoConfig(String stdFile, String compoStdFile, String configStdFile, List<String> compoMetrics, List<String> configMetrics, int scale) {
		
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
	
	public static void compileStdFiles(String stdFile, int scale, String... stdFiles) {
		
		try {
			
			CsvWriter cwStd = new CsvWriter(stdFile);
			cwStd.setDelimiter(';');
			
			CsvReader[] readers = new CsvReader[stdFiles.length];
			int index = 0;
			for(String stdf : stdFiles) {
			
				CsvReader crStdf = new CsvReader(stdf);
				crStdf.setDelimiter(';');
				crStdf.readHeaders();
				
				for(int h=0; h<crStdf.getHeaderCount(); h++) {
					
					cwStd.write(crStdf.getHeader(h)+"_"+scale+"m");
				}
				
				readers[index++] = crStdf;
			}
			cwStd.endRecord();
			
			boolean ok = true;
			while(ok) {
				
				for(CsvReader crStdf : readers) {
					
					if(crStdf.readRecord()) {
						
						for(int c=0; c<crStdf.getColumnCount(); c++) {
							
							cwStd.write(crStdf.get(c));
						}
						
					}else {
						ok = false;
						break;
					}
				}
				
				if(ok) {
					cwStd.endRecord();
				}
			}
			
			for(CsvReader crStdf : readers) {
				
				crStdf.close();
			}
			cwStd.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void compileStandardizedGroup(String stdFile, String compoStdFile, String configStdFile, List<String> compoMetrics, List<String> configMetrics, int scale) {
		
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
			kmeans.setSeed(1);
			//kmeans.setMaxIterations(100);
			kmeans.setMaxIterations(1000);
			//kmeans.setMaxIterations(10000);
			kmeans.setNumClusters(k);
			kmeans.setNumExecutionSlots(32);
			//kmeans.setPreserveInstancesOrder(false);
			//kmeans.setFastDistanceCalc(true);
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.RANDOM, SimpleKMeans.TAGS_SELECTION));
			//kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.KMEANS_PLUS_PLUS, SimpleKMeans.TAGS_SELECTION));
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
	
	public static void analyseGradient(String gradientCsv, String[][] dataXY, String kmeanFile, String normFile, int indexYear) {
		
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
			int index = -1;
			int indexY = 1;
			while(crN.readRecord()) {
				
				index++;

				if(index == dataXY.length) {
					index = 0;
					indexY++;
				}
				
				if(indexY < indexYear) {
					continue;
				}
				if(indexY > indexYear) {
					break;
				}
				
				String[] xy = dataXY[index];
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
	
	public static void analyseMembership(String membershipFile, String gradientFile, int classe) {
		
		try {
			
			CsvWriter cw = new CsvWriter(membershipFile);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			for(int ik=1; ik<=classe; ik++) {
				cw.write("ecop_"+ik);
			}
			cw.endRecord();
			
			CsvReader cr = new CsvReader(gradientFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			float[] gradients = new float[classe+1];
			while(cr.readRecord()) {
				
				cw.write(cr.get("X"));
				cw.write(cr.get("Y"));
				
				for(int ik=1; ik<=classe; ik++) {
					gradients[ik] = Float.parseFloat(cr.get("ecop_"+ik));
				}
				
				for(int ik=1; ik<=classe; ik++) {
					cw.write(generateMembership(gradients, ik)+"");
				}
				
				cw.endRecord();
			}
			
			cr.close();
			cw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static float generateMembership(float[] gradients, int ik) {
				
		float sum = 0;
		for(int iik=1; iik<gradients.length; iik++) {
		
			sum += Math.pow(gradients[ik]/gradients[iik], 2);
		}
		
		return 1f / sum;
	}
	
	public static void analyseMembershipStrict(String membershipFile, String gradientFile, String kmeanFile, int classe) {
		
		try {
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(kmeanFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
			}
			crI.close();
			
			Map<Integer, Map<Integer, Float>> kmeansDistances = new TreeMap<Integer, Map<Integer, Float>>();
			for(Entry<Integer, float[]> kmean1 : kmeans.entrySet()){
				kmeansDistances.put(kmean1.getKey(), new TreeMap<Integer, Float>());
			}
			for(Entry<Integer, float[]> kmean1 : kmeans.entrySet()){
				for(Entry<Integer, float[]> kmean2 : kmeans.entrySet()){
					
					//System.out.println(kmean1.getKey()+" "+kmean2.getKey());
					
					if(kmean1.getKey() > kmean2.getKey()) {
						// do nothing
					}else if(kmean1.getKey() == kmean2.getKey()) {
						kmeansDistances.get(kmean1.getKey()).put(kmean2.getKey(), 0f);
					}else {
						float distance = distance(kmean1.getValue(), kmean2.getValue());
						//System.out.println(distance);
						kmeansDistances.get(kmean1.getKey()).put(kmean2.getKey(), distance);
						kmeansDistances.get(kmean2.getKey()).put(kmean1.getKey(), distance);
					}
				}
			}
			
			/*
			for(Entry<Integer, Map<Integer, Float>> e1 : kmeansDistances.entrySet()) {
				
				for(Entry<Integer, Float> e2 : e1.getValue().entrySet()) {
					
					System.out.println(e1.getKey()+" "+e2.getKey()+" "+e2.getValue());
				}
			}
			*/
			
			CsvWriter cw = new CsvWriter(membershipFile);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			for(int ik=1; ik<=classe; ik++) {
				cw.write("ecop_"+ik);
			}
			cw.endRecord();
			
			CsvReader cr = new CsvReader(gradientFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			float[] gradients = new float[classe+1];
			while(cr.readRecord()) {
				
				cw.write(cr.get("X"));
				cw.write(cr.get("Y"));
				
				for(int ik=1; ik<=classe; ik++) {
					gradients[ik] = Float.parseFloat(cr.get("ecop_"+ik));
				}
				
				for(int ik=1; ik<=classe; ik++) {
					cw.write(generateMembershipStrict(gradients, ik, kmeansDistances)+"");
				}
				
				cw.endRecord();
			}
			
			cr.close();
			cw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static float generateMembershipStrict(float[] gradients, int ik, Map<Integer, Map<Integer, Float>> kmeansDistances) {
		
		int closestK = -1;
		float closestDistance = -1;

		for(int iik=1; iik<gradients.length; iik++) {
			
			if(iik == 1 || gradients[iik] < closestDistance) {
				
				closestK = iik;
				closestDistance = gradients[iik];
			}
		}
		
		boolean outside = true;
		for(int iik=1; iik<gradients.length; iik++) {
			
			//System.out.println(iik+" "+closestK);
			
			if(gradients[iik] < kmeansDistances.get(iik).get(closestK)) {
				
				outside = false;
				break;
			}
		}
		
		if(outside) {
			
			if(closestK == ik) {
				
				return 1;
				
			}else {
				
				return 0;
			}
		}
		
		float sum = 0;
		for(int iik=1; iik<gradients.length; iik++) {
		
			sum += Math.pow(gradients[ik]/gradients[iik], 2);
		}
		
		return 1f / sum;
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
	
	public static float distance(float[] v1, float[] v2) {
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
	
	public static void analyseEvoMembership(String evoMembershipFile, int classe, String[] membershipFiles) {
		
		try {
			
			int nbYear = membershipFiles.length;
			CsvReader[] crs = new CsvReader[nbYear];
			
			int index = 0;
			for(String membershipFile : membershipFiles) {
				
				CsvReader cr =  new CsvReader(membershipFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				crs[index++] = cr;
			}
			
			CsvWriter cw = new CsvWriter(evoMembershipFile);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			cw.write("global_evo");
			cw.write("global_intensity");
			for(int k=1; k<=classe; k++) {
				cw.write("evo_"+k);
			}
			for(int k=1; k<=classe; k++) {
				cw.write("intensity_"+k);
			}
			for(int k=1; k<=classe; k++) {
				cw.write("mean_"+k);
			}
			for(int k=1; k<=classe; k++) {
				cw.write("std_dev_"+k);
			}
			cw.endRecord();
			
			Stats stats = new Stats();
			
			while(crs[0].readRecord()) {
				
				for(int c=1; c<crs.length; c++) {
					
					crs[c].readRecord();
				}
				
				cw.write(crs[0].get("X"));
				cw.write(crs[0].get("Y"));
				
				float[][] datas = new float[nbYear][classe];
				for(int year=0; year<nbYear; year++) {
					for(int k=1; k<=classe; k++) {
					
						datas[year][k-1] = Float.parseFloat(crs[year].get("ecop_"+k));
					}
				}
				
				float evo = distance(datas[nbYear-1], datas[0]);
				cw.write(evo+"");
				
				float intensity = 0;
				for(int year=1; year<nbYear; year++) {
					
					intensity += distance(datas[year], datas[year-1]);
				}
				cw.write(intensity+"");
				
				for(int k=1; k<=classe; k++) {
					evo = datas[nbYear-1][k-1] - datas[0][k-1];
					cw.write(evo+"");
				}
				
				for(int k=1; k<=classe; k++) {
					intensity = 0;
					for(int year=1; year<nbYear; year++) {
						
						intensity += Math.abs(datas[year][k-1] - datas[year-1][k-1]);
					}
					cw.write(intensity+"");
				}
				
				for(int k=1; k<=classe; k++) {
					stats.reset();
					for(int year=0; year<nbYear; year++) {
						stats.add(datas[year][k-1]);
					}
					stats.calculate();
					cw.write(stats.getAverage()+"");
				}
				
				for(int k=1; k<=classe; k++) {
					stats.reset();
					for(int year=0; year<nbYear; year++) {
						stats.add(datas[year][k-1]);
					}
					stats.calculate();
					cw.write(stats.getStandardDeviation()+"");
				}
				
				cw.endRecord();
			}
			
			for(CsvReader cr : crs) {
				cr.close();
			}
			cw.close();
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
