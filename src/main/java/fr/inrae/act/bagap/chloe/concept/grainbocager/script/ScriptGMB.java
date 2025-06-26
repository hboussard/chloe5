package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptGMB {

	private static String path = "C:/Data/projet/gmb/data/";
	
	public static void main(String[] args) {
		
		long begin = System.currentTimeMillis();
		
		//retile();
		//retile2();
		//convertPermeabilite();
		//convertHabitat();
		//generatePermeabilite();
		//generatePermeabiliteTest();
		//generatePermeabiliteDistribution();
		//cleanPermeabilite(path+"Couts-0_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag_clean.tif", path+"Couts-0.1_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag.tif");
		
		//continuitesBoisees(path+"continuite_boisee_mars_0-10_150m.tif", path+"Couts-0_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag_clean.tif", 150, 61);
		//calculIndiceContinuite(path+"indice_continuite_boisee_mars_0-10_150m.tif", path+"continuite_boisee_mars_0-10_150m.tif", 150);
		
		//continuitesBoisees(path+"continuite_boisee_0-10_3300m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var.tif", 3300, 1321);
		//calculIndiceContinuite(path+"indice_continuite_boisee_0-10_3300m.tif", path+"continuite_boisee_0-10_3300m.tif", 3300);
		
		//classificationContinuite(path+"indice_continuite_boisee_0-10_150m.tif", 0.1, 0.5, 0.05);
		//classificationContinuite(path+"indice_continuite_boisee_0-10_800m.tif");
		//classificationContinuite(path+"indice_continuite_boisee_mars_0-10_150m.tif", 0.01, 0.02, 0.01);
		
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 800);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_mars_0-10_150m.tif", path+"Couts-0_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag_clean.tif", 1, 800);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 3300);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_800m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 3300);
		
		//analyseClustersHabitat(path+"cluster_1_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_2_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_3_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_4_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_5_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_6_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_7_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_8_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		//analyseClustersHabitat(path+"cluster_9_classif_indice_continuite_boisee_0-10_150m_800m.tif", 3300);
		
		//meanDistance(path+"classif_indice_continuite_boisee_0-10_150m.tif", 9, 3300);
		
		//distanceFromHabitat(800);
		//clusterFromHabitat(800);
		
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void analyseClustersHabitat(String cluster_continuite, int echelle) {

		File f = new File(cluster_continuite);
		String name = f.getName().replaceAll(".tif", "");
		
		int windowSize = (echelle * 2)/10 + 1;
		System.out.println(windowSize);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(cluster_continuite);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(windowSize);
		builder.setDisplacement(20);
		builder.addMetric("pN-values");
		builder.addMetric("SHDI-frag");
		builder.addGeoTiffOutput("pN-values", path+"proportion_"+name+".tif");
		builder.addGeoTiffOutput("SHDI-frag", path+"fragmentation_"+name+".tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}

	private static void meanDistance(String classification_continuite, int nbDomains, int dMax) {
		
		File f = new File(classification_continuite);
		String name = f.getName().replaceAll(".tif", "");
		
		Coverage covK = CoverageManager.getCoverage(path+"distance_1_"+name+"_"+dMax+"m.tif");
		float[] data = covK.getData();
		EnteteRaster entete = covK.getEntete();
		covK.dispose();
		
		for(int k=2; k<nbDomains; k++) {
			
			covK = CoverageManager.getCoverage(path+"distance_"+k+"_"+name+"_"+dMax+"m.tif");
			float[] dataK = covK.getData();
			covK.dispose();
			
			for(int i=0; i<data.length; i++) {
				float v = data[i];
				if(v != entete.noDataValue()) {
					data[i] = (v*(k-1) + dataK[i]) / k;
				}
			}
		}
		
		CoverageManager.writeGeotiff(path+"distance_mean_"+name+"_"+dMax+"m.tif", data, entete);
	}

	private static void clusteringContinuite(String classification_continuite, String permeabilite, int nbDomains, int dMax) {

		File f = new File(classification_continuite);
		String name = f.getName().replaceAll(".tif", "");
		
		Coverage covClassif = CoverageManager.getCoverage(classification_continuite);
		float[] dataClassif = covClassif.getData();
		EnteteRaster entete = covClassif.getEntete();
		covClassif.dispose();
		
		Coverage covPermeabilite = CoverageManager.getCoverage(permeabilite);
		float[] dataPermeabilite = covPermeabilite.getData();
		covPermeabilite.dispose();
		
		TabRCMDistanceAnalysis da;
		TabDistanceClusteringAnalysis ca;
		float[] dataDistance, dataCluster;
		int[] values;
		
		for(int k=nbDomains; k>0; k--) {
			
			System.out.println(k);
			
			values = new int[nbDomains-k+1];
			for(int v=k, i=0; v<=nbDomains; v++, i++) {
				values[i] = v;
			}
			
			dataDistance = new float[entete.width()*entete.height()];
			
			da = new TabRCMDistanceAnalysis(dataDistance, dataClassif, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), values, entete.noDataValue());
			da.allRun();
			
			CoverageManager.writeGeotiff(path+"distance_"+k+"_"+name+"_"+dMax+"m.tif", dataDistance, entete);
		
			ca = new TabDistanceClusteringAnalysis(dataClassif, dataDistance, entete.width(), entete.height(), values, dMax, entete.noDataValue());
			dataCluster = (float[]) ca.allRun();
			
			CoverageManager.writeGeotiff(path+"cluster_"+k+"_"+name+"_"+dMax+"m.tif", dataCluster, entete);
			
		}
	}

	private static void classificationContinuite(String indice_deplacement, double min, double max, double step) {
		
		File f = new File(indice_deplacement);
		String name = f.getName().replaceAll(".tif", "");
		
		Coverage cov = CoverageManager.getCoverage(indice_deplacement);
		float[] inData = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] dataClassif = new float[entete.width()*entete.height()];
		
		double dmin = min * 10.0;
		double dstep = step * 10.0;
		double dmax = max * 10.0;
		
		int domain = 0;
		Map<Domain<Float, Float>, Integer> domains = new LinkedHashMap<Domain<Float, Float>, Integer>();	
		domains.put(DomainFactory.getFloatDomain("[0,"+dmin/10.0+"]"), domain++);
		
		for(double d=dmin+dstep; d<=dmax; d+=dstep) {
			
			//System.out.println(dmin+" "+d+" "+dmax+" "+domain);
			System.out.println((dmin/10.0)+" "+(d/10.0)+" "+(dmax/10.0)+" "+domain);
			
			if(d<dmax) {
				
				domains.put(DomainFactory.getFloatDomain("]"+dmin/10.0+","+d/10.0+"]"), domain++);
				
			}else {
				
				domains.put(DomainFactory.getFloatDomain("]"+dmin/10.0+",]"), domain++);
			}
			
			dmin=d;
		}
		
		for(Domain<Float, Float> d : domains.keySet()) {
			System.out.println(d);
		}
		
		
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(dataClassif, inData, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"classif_"+name+".tif", dataClassif, entete);
		
	}

	private static void distanceFromHabitat(int dMax) {

		Coverage covHabitat = CoverageManager.getCoverage(path+"habitats.tif");
		float[] dataHabitat = covHabitat.getData();
		EnteteRaster entete = covHabitat.getEntete();
		covHabitat.dispose();
		
		Coverage covPermeabilite = CoverageManager.getCoverage(path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var.tif");
		float[] dataPermeabilite = covPermeabilite.getData();
		covPermeabilite.dispose();
		
		float[] dataDistance = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {1}, dMax);
		da.allRun();
		
		CoverageManager.write(path+"distance_habitat_"+dMax+"m.tif", dataDistance, entete);
	}
	
	private static void clusterFromHabitat(int dMax) {

		Coverage covHabitat = CoverageManager.getCoverage(path+"habitats_clean.tif");
		float[] dataHabitat = covHabitat.getData();
		EnteteRaster entete = covHabitat.getEntete();
		covHabitat.dispose();
		
		Coverage covDistance = CoverageManager.getCoverage(path+"distance_habitat_clean_"+dMax+"m.tif");
		float[] dataDistance = covDistance.getData();
		covDistance.dispose();
		
		float[] dataCluster = new float[entete.width()*entete.height()];
		
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {1}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		
		CoverageManager.write(path+"cluster_habitat_clean_"+dMax+"m.tif", dataCluster, entete);
	}
	
	private static void clusteringContinuity(String indice_deplacement) {
		
		File f = new File(indice_deplacement);
		String name = f.getName().replaceAll(".tif", "");
		
		Coverage cov = CoverageManager.getCoverage(indice_deplacement);
		float[] inData = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] dataClassif = new float[entete.width()*entete.height()];
		
		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		
		float dmin = 0;
		float dstep = 0.5f;
		float dmax = 5;
		int domain = 1;
		for(float d=dmin+dstep; d<=dmax; dmin=d, d+=dstep, domain++) {
			
			if(domain == 0) {
				
				domains.put(DomainFactory.getFloatDomain("["+dmin+","+d+"]"), domain);
			}
			if(d<dmax) {
			
				domains.put(DomainFactory.getFloatDomain("]"+dmin+","+d+"]"), domain);
			}else {
			
				domains.put(DomainFactory.getFloatDomain("]"+dmin+",]"), domain);
			}
		}
		
		/*
		 * domains.put(DomainFactory.getFloatDomain("[0,0.5]"), 0);
		 * domains.put(DomainFactory.getFloatDomain("]1,2]"), 1);
		domains.put(DomainFactory.getFloatDomain("]2,3]"), 2);
		domains.put(DomainFactory.getFloatDomain("]3,4]"), 3);
		domains.put(DomainFactory.getFloatDomain("]4,5]"), 4);
		domains.put(DomainFactory.getFloatDomain("]5,6]"), 5);
		domains.put(DomainFactory.getFloatDomain("]6,7]"), 6);
		domains.put(DomainFactory.getFloatDomain("]7,8]"), 7);
		domains.put(DomainFactory.getFloatDomain("]8,9]"), 8);
		domains.put(DomainFactory.getFloatDomain("]9,10]"), 9);
		domains.put(DomainFactory.getFloatDomain("]10,11]"), 10);
		domains.put(DomainFactory.getFloatDomain("]11,12]"), 11);
		domains.put(DomainFactory.getFloatDomain("]12,13]"), 12);
		domains.put(DomainFactory.getFloatDomain("]13,14]"), 13);
		domains.put(DomainFactory.getFloatDomain("]14,]"), 14);
		domains.put(DomainFactory.getFloatDomain("]1,]"), 1);
		*/
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(dataClassif, inData, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"classif_"+name+".tif", dataClassif, entete);
		
		
		
		TabChamferDistanceAnalysis da;
		TabQueenClusteringAnalysis ca;
		float[] dataDistance, dataCluster;
		int[] values;
		int nbDomains = domains.size()-1;
		for(int k=nbDomains; k>0; k--) {
			
			values = new int[nbDomains-k+1];
			for(int v=k, i=0; v<=nbDomains; v++, i++) {
				values[i] = v;
			}
			
			/*
			dataDistance = new float[entete.width()*entete.height()];
			
			da = new TabChamferDistanceAnalysis(dataDistance, dataClassif, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), values, 15);
			da.allRun();
			
			ca = new TabDistanceClusteringAnalysis(dataClassif, dataDistance, entete.width(), entete.height(), values, 10, entete.noDataValue());
			dataCluster = (float[]) ca.allRun();
			*/
			
			ca = new TabQueenClusteringAnalysis(dataClassif, entete.width(), entete.height(), values, entete.noDataValue());
			dataCluster = (float[]) ca.allRun();
			
			CoverageManager.writeGeotiff(path+"cluster_"+k+"_"+name+".tif", dataCluster, entete);
		}
		
		/*
		LandscapeMetricAnalysisBuilder builder;
		LandscapeMetricAnalysis analysis;
		for(int k=10; k>0; k--) {
			
			builder = new LandscapeMetricAnalysisBuilder();
			builder.setRasterFile(path+"cluster_indice_deplacement_250m_"+k+".tif");
			//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
			builder.setWindowSize(401);
			builder.setDisplacement(10);
			builder.addMetric("pN-values");
			builder.addGeoTiffOutput("pN-values", path+"pnvalues_cluster_indice_deplacement_250m_"+k+".tif");
			builder.addMetric("SHDI2");
			builder.addGeoTiffOutput("SHDI2", path+"shdi2_cluster_indice_deplacement_250m_"+k+".tif");
			analysis = builder.build();
			
			analysis.allRun();
		}
		*/
		/*
		float[][] datas = new float[10][];
 		for(int k=10; k>0; k--) {
 			cov = CoverageManager.getCoverage(path+"shdi2_cluster_indice_deplacement_250m_"+k+".tif");
 			datas[k-1] = cov.getData();
 			entete = cov.getEntete();
 			cov.dispose();
		}
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, datas){
			@Override
			protected float doTreat(float[] v) {
				return (v[0]+v[1]+v[2]+v[3]+v[4]+v[5]+v[6]+v[7]+v[8]+v[9]) / 10.0f;
			}
		};
		cal.run();
		
		CoverageManager.write(path+"sum_shdi2_cluster_indice_deplacement_250m.tif", outData, entete);
		*/
		/*
		float[][] datas = new float[10][];
 		for(int k=10; k>0; k--) {
 			cov = CoverageManager.getCoverage(path+"pnvalues_cluster_indice_deplacement_250m_"+k+".tif");
 			datas[k-1] = cov.getData();
 			entete = cov.getEntete();
 			cov.dispose();
		}
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, datas){
			@Override
			protected float doTreat(float[] v) {
				return (v[0]+v[1]+v[2]+v[3]+v[4]+v[5]+v[6]+v[7]+v[8]+v[9]) / 10.0f;
			}
		};
		cal.run();
		
		CoverageManager.write(path+"sum_pnvalues_cluster_indice_deplacement_250m.tif", outData, entete);
		*/
	}

	private static void calculIndiceContinuite(String output, String input, int dMax) {
		
		Coverage cov = CoverageManager.getCoverage(input);
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				return (float) (v[0] / (((Math.pow(dMax, 2)*Math.PI))*dMax)/3);
			}
		};
		cal.run();
		
		CoverageManager.write(output, outData, entete);
	}
	
	private static void continuitesBoisees(String continuites, String permeabilite, int dMax, int windowSize) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile(permeabilite);
		builder.setRasterFile2(permeabilite);
		builder.setWindowSize(windowSize);
		builder.addMetric("volume");
		builder.setDMax(dMax);
		builder.addGeoTiffOutput("volume", continuites);
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void cleanPermeabilite(String output, String input) {
		
		Coverage cov = CoverageManager.getCoverage(input);
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		int newNoDataValue = -1;
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data) {
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD < 0) {
					return newNoDataValue;
				}
				return vD;
			}
		};
		cal.run();
		
		entete.setNoDataValue(newNoDataValue);
		
		CoverageManager.write(output, data, entete);	
	}
	
	private static void convertPermeabilite() {
		
		Coverage covDist = CoverageManager.getCoverage(path+"Couts_0-100_MusAve2025_SansSeuillage_modelANN_13var.tif");
		EnteteRaster entete = covDist.getEntete();
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataDist) {
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD != entete.noDataValue()) {
					vD /= 10;
					if(vD == 0) {
						return 0.1f;
					}
					return vD;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
	
		CoverageManager.write(path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var.tif", data, entete);
	}
	
	private static void convertHabitat() {
		
		Coverage covDist = CoverageManager.getCoverage(path+"Couts_MusAve2025_Seuil0.188_modelANN_13var.tif");
		EnteteRaster entete = covDist.getEntete();
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataDist) {
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD != entete.noDataValue()) {
					if(vD != 1) {
						return 2f;
					}
					return 1;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
	
		CoverageManager.write(path+"habitats.tif", data, entete);
	}
	
	private static void generatePermeabiliteDistribution() {
		
		Coverage covDist = CoverageManager.getCoverage(path+"DistributionPotentielleMuscardin2025_Modelisation13var-Raster10m_BzhHisto_L93.tif");
		EnteteRaster entete = covDist.getEntete();
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataDist) {
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD != entete.noDataValue()) {
					return (float) (-0.1 * vD + 10);
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
	
		CoverageManager.write(path+"permeabilite_distribution.tif", data, entete);
	}
	
	private static void generatePermeabiliteTest() {
		
		Coverage covDist = CoverageManager.getCoverage(path+"test.tif");
		EnteteRaster entete = covDist.getEntete();
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataDist) {
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD != entete.noDataValue()) {
					return (float) (-0.1 * vD + 10);
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
	
		CoverageManager.write(path+"test_permeabilite.tif", data, entete);
	}
	
	private static void generatePermeabilite() {
		
		Coverage covOS = CoverageManager.getCoverage(path+"sousOS.tif");
		EnteteRaster entete = covOS.getEntete();
		float[] dataOS = covOS.getData();
		covOS.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 100f); 
		sarMap.put(3f, 100f);
		sarMap.put(4f, 1f); 
		sarMap.put(5f, 5f); 
		sarMap.put(6f, 5f);
		sarMap.put(7f, 5f);
		sarMap.put(9f, 5f);
		sarMap.put(10f, 5f);
		sarMap.put(12f, 5f);
		sarMap.put(13f, 2f);
		sarMap.put(14f, 2f);
		sarMap.put(15f, 5f);
		sarMap.put(16f, 10f);
		sarMap.put(17f, 0.1f);
		sarMap.put(18f, 0.1f);
		sarMap.put(19f, 2f);
		sarMap.put(20f, 0.1f);
		sarMap.put(23f, 100f);
		Pixel2PixelTabCalculation cal;
		
		float[] data = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(data, dataOS, sarMap);
		cal.run();
		
		float[] distanceData = new float[entete.width()*entete.height()];
		TabChamferDistanceAnalysis distance = new TabChamferDistanceAnalysis(distanceData, dataOS, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {16}, 10);
		distance.allRun();
		
		for(int i=0; i<data.length; i++) {
			float dd = distanceData[i];
			if(dd > 0 && dd < 10) {
				data[i] = 0.1f;
			}
		}
		
		CoverageManager.write(path+"sous_permeabilite.tif", data, entete);
	}
	
	private static void retile2() {
		
		Coverage covOS = CoverageManager.getCoverage("F:/data/sig/bretagne/bretagne_2021_ebr_clean.tif");
		EnteteRaster entete = covOS.getEntete();
		
		Rectangle ROI = new Rectangle(26000, 11800, 2000, 2000);
		EnteteRaster localEntete = EnteteRaster.getEntete(entete, ROI);
		float[] dataOS = covOS.getData(ROI);
		covOS.dispose();
		
		CoverageManager.write(path+"sousOS.tif", dataOS, localEntete);
	}

	private static void retile() {
		
		Coverage covOS = CoverageManager.getCoverage(path+"DistributionPotentielleMuscardin2025_Modelisation13var-Raster10m_BzhHisto_L93.tif");
		EnteteRaster entete = covOS.getEntete();
		//float[] dataOS = covOS.getData();
				
		
		Rectangle ROI = new Rectangle(10000, 5000, 1000, 1000);
		EnteteRaster localEntete = EnteteRaster.getEntete(entete, ROI);
		float[] dataOS = covOS.getData(ROI);
		covOS.dispose();
		
		CoverageManager.write(path+"test.tif", dataOS, localEntete);
	}
}
