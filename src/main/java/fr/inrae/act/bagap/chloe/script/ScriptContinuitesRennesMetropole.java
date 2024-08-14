package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.OverlayPixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptContinuitesRennesMetropole {

	public static void main(String[] args){
		
		long begin = System.currentTimeMillis();
		
		//convert();
		
		//prepaOS();
		//rasterizeZoneHumide();
		//prepaHabitats();
		//prepaPermeabilite();
		//calculDistanceFonctionnelle();
		
		//rasterizeCGTV();
		//prepaPermeabilitePrairial();
		//cleanPermeabilitePrairial();
		
		//calculClusterFonctionnel();
		
		//prepaFenetresFonctionnelles();
		//calculFenetresFonctionnelles();
		//calculContinuity();
		
		//calculContinuityBoise();
		//calculContinuityHumide();
		//calculContinuity("prairial", 125);
		//calculContinuity("prairial", 250);
		//calculContinuity("prairial", 500);
		
		//int volumeMin = 30000000;
		//classifFonctionnelleBoise(volumeMin);
		//clusterFonctionnelBoise(volumeMin);
		//proportionFonctionnelleBoise(volumeMin);
		//fragmentationFonctionnelleBoise(volumeMin);
		
		//calculIndiceAccessibilite("boise", 125);
		//calculIndiceAccessibilite("humide", 125);
		//calculIndiceAccessibilite("boise", 250);
		//calculIndiceAccessibilite("humide", 250);
		//calculIndiceAccessibilite("boise", 500);
		//calculIndiceAccessibilite("humide", 500);
		
		//proportionContinuites("boise", 125);
		//proportionContinuites("humide", 125);
		//proportionContinuites("boise", 250);
		//proportionContinuites("humide", 250);
		//proportionContinuites("boise", 500);
		//proportionContinuites("humide", 500);
		
		//sumProportionContinuites(125);
		//sumProportionContinuites(250);
		//sumProportionContinuites(500);
		
		//calculIndiceDeplacement("boise", 125);
		//calculIndiceDeplacement("humide", 125);
		//calculIndiceDeplacement("boise", 250);
		//calculIndiceDeplacement("humide", 250);
		//calculIndiceDeplacement("boise", 500);
		//calculIndiceDeplacement("humide", 500);
		//calculIndiceDeplacement("prairial", 125);
		//calculIndiceDeplacement("prairial", 250);
		
		//calculIndiceContinuite("boise", 125);
		//calculIndiceContinuite("humide", 125);
		//calculIndiceContinuite("boise", 250);
		//calculIndiceContinuite("humide", 250);
		//calculIndiceContinuite("boise", 500);
		//calculIndiceContinuite("humide", 500);
		
		//classifFonctionnelle("boise", 125, "continuite", 1);
		//classifFonctionnelle("humide", 125, "continuite", 1);
		//classifFonctionnelle("boise", 250, "continuite", 1);
		//classifFonctionnelle("humide", 250, "continuite", 1);
		//classifFonctionnelle("boise", 500, "continuite", 1);
		//classifFonctionnelle("humide", 500, "continuite", 1);
		
		//clusterFonctionnel("boise", 125, "continuite", 1);
		//clusterFonctionnel("humide", 125, "continuite", 1);
		//clusterFonctionnel("boise", 250, "continuite", 1);
		//clusterFonctionnel("humide", 250, "continuite", 1);
		//clusterFonctionnel("boise", 500, "continuite", 1);
		//clusterFonctionnel("humide", 500, "continuite", 1);
		
		//proportionFonctionnelle("boise", 125, "continuite", 1, 2500);
		//proportionFonctionnelle("humide", 125, "continuite", 1, 2500);
		//proportionFonctionnelle("boise", 250, "continuite", 1, 2500);
		//proportionFonctionnelle("humide", 250, "continuite", 1, 2500);
		//proportionFonctionnelle("boise", 500, "continuite", 1, 2500);
		//proportionFonctionnelle("humide", 500, "continuite", 1, 2500);
		
		//fragmentationFonctionnelle("boise", 125, "continuite", 1, 2500);
		//fragmentationFonctionnelle("humide", 125, "continuite", 1, 2500);
		//fragmentationFonctionnelle("boise", 250, "continuite", 1, 2500);
		//fragmentationFonctionnelle("humide", 250, "continuite", 1, 2500);
		//fragmentationFonctionnelle("boise", 500, "continuite", 1, 2500);
		//fragmentationFonctionnelle("humide", 500, "continuite", 1, 2500);
		
		//rasterizeCodeMNIE();		
		//noteEntity("mnie", "boise", 125, "continuite", "maximum");
		//noteEntity("mnie", "humide", 125, "continuite", "maximum");
		//noteEntity("mnie", "boise", 250, "continuite", "maximum");
		//noteEntity("mnie", "humide", 250, "continuite", "maximum");
		//noteEntity("mnie", "boise", 500, "continuite", "maximum");
		//noteEntity("mnie", "humide", 500, "continuite", "maximum");
		/*
		noteEntity("mnie", "boise", 125, "deplacement", "average");
		noteEntity("mnie", "humide", 125, "deplacement", "average");
		noteEntity("mnie", "boise", 250, "deplacement", "average");
		noteEntity("mnie", "humide", 250, "deplacement", "average");
		noteEntity("mnie", "boise", 500, "deplacement", "average");
		noteEntity("mnie", "humide", 500, "deplacement", "average");
		*/
		/*
		//rasterizeCodeCommune();		
		noteEntity("commune", "boise", 125, "deplacement", "average");
		noteEntity("commune", "humide", 125, "deplacement", "average");
		noteEntity("commune", "boise", 250, "deplacement", "average");
		noteEntity("commune", "humide", 250, "deplacement", "average");
		noteEntity("commune", "boise", 500, "deplacement", "average");
		noteEntity("commune", "humide", 500, "deplacement", "average");
		
		noteEntity("commune", "boise", 125, "deplacement", "sum");
		noteEntity("commune", "humide", 125, "deplacement", "sum");
		noteEntity("commune", "boise", 250, "deplacement", "sum");
		noteEntity("commune", "humide", 250, "deplacement", "sum");
		noteEntity("commune", "boise", 500, "deplacement", "sum");
		noteEntity("commune", "humide", 500, "deplacement", "sum");
		*/
		/*
		//rasterizeCodeSecteur();	
		noteEntity("secteur", "boise", 125, "deplacement", "average");
		noteEntity("secteur", "humide", 125, "deplacement", "average");
		noteEntity("secteur", "boise", 250, "deplacement", "average");
		noteEntity("secteur", "humide", 250, "deplacement", "average");
		noteEntity("secteur", "boise", 500, "deplacement", "average");
		noteEntity("secteur", "humide", 500, "deplacement", "average");
		
		noteEntity("secteur", "boise", 125, "deplacement", "sum");
		noteEntity("secteur", "humide", 125, "deplacement", "sum");
		noteEntity("secteur", "boise", 250, "deplacement", "sum");
		noteEntity("secteur", "humide", 250, "deplacement", "sum");
		noteEntity("secteur", "boise", 500, "deplacement", "sum");
		noteEntity("secteur", "humide", 500, "deplacement", "sum");
		*/
		//testIndiceContinuite("boise", 125);
		
		//classifFonctionnelle("boise", 250, "deplacement", 0.5f);
		//clusterFonctionnel("boise", 250, "deplacement", 0.5f);		
		//proportionFonctionnelle("boise", 250, "deplacement", 0.5f, 1500);		
		//fragmentationFonctionnelle("boise", 250, "deplacement", 0.5f, 1500);
		/*
		classifFonctionnelle("boise", 125, "deplacement", 0.5f);
		clusterFonctionnel("boise", 125, "deplacement", 0.5f);		
		proportionFonctionnelle("boise", 125, "deplacement", 0.5f, 1500);		
		fragmentationFonctionnelle("boise", 125, "deplacement", 0.5f, 1500);
		*/
		
		//classification("boise", 250, "deplacement", 0, 1, 0.1f);
		//classification("humide", 250, "deplacement", 0, 1, 0.1f);
		
		//sum(250, "deplacement", 33);
		//sum(250, "deplacement", 26);
		
		//cleanClassif(250, "deplacement", 33);
		//cleanClassif(250, "deplacement", 26);
		/*
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_boise_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_boise_250m.csv");
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_humide_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_humide_250m.csv");
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-33_deplacement_250m.csv");
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_boise_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_boise_250m.csv");
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_humide_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_humide_250m.csv");
		analyseGlobale("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_250m.tif", "E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-26_deplacement_250m.csv");
		//System.out.println(Float.MAX_VALUE);
		*/
		//System.out.println(Long.MAX_VALUE);
		//System.out.println(Float.MAX_VALUE);
		//System.out.println(Double.MAX_VALUE);
	/*
		double f=0;
		for(int i=0; i<Integer.MAX_VALUE; i+=100) {
			f += 100;
			System.out.println(f);
		}
		*/
		//double d = Math.pow(2, 24);
		//System.out.println(d);
		
		clusteringContinuity();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void clusteringContinuity() {
		
		String path = "E:/rennes_metropole/continuite_ecologique/clustering2/";
		
		Coverage cov = CoverageManager.getCoverage(path+"indice_deplacement_250m.tif");
		float[] inData = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		/*
		float[] dataClassif = new float[entete.width()*entete.height()];
		
		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		domains.put(DomainFactory.getFloatDomain("[0,0.1]"), 0);
		domains.put(DomainFactory.getFloatDomain("]0.1,0.2]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.2,0.3]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.3,0.4]"), 3);
		domains.put(DomainFactory.getFloatDomain("]0.4,0.5]"), 4);
		domains.put(DomainFactory.getFloatDomain("]0.5,0.6]"), 5);
		domains.put(DomainFactory.getFloatDomain("]0.6,0.7]"), 6);
		domains.put(DomainFactory.getFloatDomain("]0.7,0.8]"), 7);
		domains.put(DomainFactory.getFloatDomain("]0.8,0.9]"), 8);
		domains.put(DomainFactory.getFloatDomain("]0.9,1]"), 9);
		domains.put(DomainFactory.getFloatDomain("]1,]"), 10);
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(dataClassif, inData, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"classif_indice_deplacement_250m.tif", dataClassif, entete);
		
		TabChamferDistanceAnalysis da;
		TabDistanceClusteringAnalysis ca;
		float[] dataDistance, dataCluster;
		int[] values;
		for(int k=10; k>0; k--) {
			
			values = new int[10-k+1];
			for(int v=k, i=0; v<=10; v++, i++) {
				values[i] = v;
			}
			
			dataDistance = new float[entete.width()*entete.height()];
			
			da = new TabChamferDistanceAnalysis(dataDistance, dataClassif, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), values, 15);
			da.allRun();
			
			ca = new TabDistanceClusteringAnalysis(dataClassif, dataDistance, entete.width(), entete.height(), values, 10, entete.noDataValue());
			dataCluster = (float[]) ca.allRun();
					
			CoverageManager.writeGeotiff(path+"cluster_indice_deplacement_250m_"+k+".tif", dataCluster, entete);
		}
		*/
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
		
	}

	private static void analyseGlobale(String inputRaster, String outputCsv) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.setRasterFile(inputRaster);
		builder.addMetric("pNV_1");
		builder.addCsvOutput(outputCsv);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void cleanClassif(int dMax, String continuite, int seuil) {
		
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/data/commune_5m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage cov1 = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_boise_"+dMax+"m.tif");
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data, data1){

			@Override
			protected float doTreat(float[] v) {
				float v0 = v[0];
				float v1 = v[1];
				if(v0 == -1) { 
					return -1;
				}
				if(v1 == 1) { 
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-"+seuil+"_"+continuite+"_boise_"+dMax+"m.tif", outData, entete);
		
		cov1 = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_humide_"+dMax+"m.tif");
		data1 = cov1.getData();
		cov1.dispose();
		
		outData = new float[entete.width()*entete.height()];
		
		cal = new Pixel2PixelTabCalculation(outData, data, data1){

			@Override
			protected float doTreat(float[] v) {
				float v0 = v[0];
				float v1 = v[1];
				if(v0 == -1) { 
					return -1;
				}
				if(v1 == 1) { 
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-"+seuil+"_"+continuite+"_humide_"+dMax+"m.tif", outData, entete);
		
		cov1 = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_"+dMax+"m.tif");
		data1 = cov1.getData();
		cov1.dispose();
		
		outData = new float[entete.width()*entete.height()];
		
		cal = new Pixel2PixelTabCalculation(outData, data, data1){

			@Override
			protected float doTreat(float[] v) {
				float v0 = v[0];
				float v1 = v[1];
				if(v0 == -1) { 
					return -1;
				}
				if(v1 == 1) { 
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/classification/rm_classif_0-"+seuil+"_"+continuite+"_"+dMax+"m.tif", outData, entete);

	}
	
	private static void sum(int dMax, String continuite, int seuil) {
		
		Coverage covB = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_boise_"+dMax+"m.tif");
		float[] dataB = covB.getData();
		EnteteRaster entete = covB.getEntete();
		covB.dispose();
		
		Coverage covH = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_humide_"+dMax+"m.tif");
		float[] dataH = covH.getData();
		covH.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataB, dataH){

			@Override
			protected float doTreat(float[] v) {
				float vB = v[0];
				float vH = v[1];
				if(vB == 1 || vH == 1) { 
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/classification/classif_0-"+seuil+"_"+continuite+"_"+dMax+"m.tif", outData, entete);
	}

	private static void classification(String type, int dMax, String continuite, int min, int max, float step) {

		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/indice_"+continuite+"_"+type+"_"+dMax+"m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		/*
		domains.put(DomainFactory.getFloatDomain("[0, 0.1]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.1, 0.2]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.2, 0.3]"), 3);
		domains.put(DomainFactory.getFloatDomain("]0.3, 0.4]"), 4);
		domains.put(DomainFactory.getFloatDomain("]0.4, 0.5]"), 5);
		domains.put(DomainFactory.getFloatDomain("]0.5, 0.6]"), 6);
		domains.put(DomainFactory.getFloatDomain("]0.6, 0.7]"), 7);
		domains.put(DomainFactory.getFloatDomain("]0.7, 0.8]"), 8);
		domains.put(DomainFactory.getFloatDomain("]0.8, 0.9]"), 9);
		domains.put(DomainFactory.getFloatDomain("]0.9, ]"), 10);
		*/
		domains.put(DomainFactory.getFloatDomain("[0, 0.26]"), -1);
		domains.put(DomainFactory.getFloatDomain("]0.26, ]"), 1);
		
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(outData, data, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/classification/classif_0-26_"+continuite+"_"+type+"_"+dMax+"m.tif", outData, entete); // export sur fichier
		
	}

	private static void cleanPermeabilitePrairial() {
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/permeabilite_prairial.tif");
		float[] data1 = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique/diversity.tif");
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		Coverage cov3 = CoverageManager.getCoverage("E:/rennes_metropole/data/cgtv.tif");
		float[] data3 = cov3.getData();
		cov3.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data1, data2, data3){

			@Override
			protected float doTreat(float[] v) {
				float v3 = v[2];
				if(v3 == 5 || v3 == 6) { // CGTV en prairie permanente
					return 0.1f;
				}
				float v2 = v[1];
				if(v2>0) { // diversite locale de couverts vegetaux
					return 1f;
				}
				return v[0];
			}
		};
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/permeabilite_prairial.tif", outData, entete);
		
	}

	private static void prepaPermeabilitePrairial(){
		
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/continuite_ecologique_old/rm_os_bre.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 10f); 
		sarMap.put(3f, 100f);
		sarMap.put(4f, 10f); 
		sarMap.put(5f, 2f); 
		sarMap.put(6f, 2f);
		sarMap.put(7f, 2f);
		sarMap.put(8f, 2f);
		sarMap.put(9f, 10f);
		sarMap.put(10f, 10f);
		sarMap.put(11f, 2f);
		sarMap.put(12f, 10f);
		sarMap.put(13f, 2f);
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/permeabilite_prairial.tif", dataSR, entete);
	}
	
	private static void rasterizeCGTV(){
		
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/grain_bocager/rm_grain_bocager_5m_4classes.tif");
		EnteteRaster enteteRef = cov.getEntete();
		cov.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage("D:/data/sig/CGTV/cgtv.tif");
		EnteteRaster entete = cov2.getEntete();
		float[] data = cov2.getData(EnteteRaster.getROI(entete, new Envelope(enteteRef.minx(), enteteRef.maxx(), enteteRef.miny(), enteteRef.maxy())));
		cov2.dispose();
		
		CoverageManager.write("E:/rennes_metropole/data/cgtv.tif", data, enteteRef);
	}

	private static void calculIndiceContinuite(String type, int dMax) {
		Coverage cov1 = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/indice_accessibilite_"+type+"_"+dMax+"m.tif");
		float[] data1 = cov1.getData();
		EnteteRaster entete = cov1.getEntete();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/indice_deplacement_"+type+"_"+dMax+"m.tif");
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data1, data2){

			@Override
			protected float doTreat(float[] v) {
				return v[0] * v[1];
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/indice_continuite_"+type+"_"+dMax+"m.tif", outData, entete);
		
	}
	
	private static void calculIndiceDeplacement(String type, int dMax) {
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/volume_deplacement/volume_deplacement_"+type+"_"+dMax+".tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		//System.out.println((((Math.pow(dMax, 2)*Math.PI))*dMax)/3);
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				//System.out.println(v[0]+" "+((((Math.pow(dMax, 2)*Math.PI))*dMax)/3)+" "+(v[0] / (((Math.pow(dMax, 2)*Math.PI))*dMax)/3));
				//return (float) (v[0] / (((Math.pow(125, 2)*Math.PI))*125)/3);
				return (float) (v[0] / (((Math.pow(dMax, 2)*Math.PI))*dMax)/3);
			}
		};
		cal.run();
		
		//CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/indice_deplacement_"+type+"_"+dMax+"m_rapport125m.tif", outData, entete);
		CoverageManager.write("E:/rennes_metropole/continuite_ecologique/indice_deplacement_"+type+"_"+dMax+"m.tif", outData, entete);
		
	}

	private static void sumProportionContinuites(int dMax){
		
		
		Coverage covB = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/average_500m_indice_accessibilite_boise_"+dMax+"m.tif");
		float[] dataB = covB.getData();
		EnteteRaster entete = covB.getEntete();
		covB.dispose();
		
		Coverage covH = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/average_500m_indice_accessibilite_humide_"+dMax+"m.tif");
		float[] dataH = covH.getData();
		covH.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataB, dataH){

			@Override
			protected float doTreat(float[] v) {
				return v[0] + v[1];
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/sum_average_continuites_"+dMax+"m.tif", outData, entete);
		
	}
	
	private static void calculIndiceAccessibilite(String type, int dMax){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/surface_accessibilite_"+type+"_"+dMax+".tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				return (float) (v[0] / (Math.pow(dMax, 2)*Math.PI));
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/indice_accessibilite_"+type+"_"+dMax+"m.tif", outData, entete);
		
	}
	
	private static void proportionContinuites(String type, int dMax){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/volume_deplacement/test/indice_accessibilite_"+type+"_"+dMax+"m.tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(201);
		builder.setDisplacement(10);
		builder.addMetric("average");
		builder.addGeoTiffOutput("average", "H:/rennes_metropole/volume_deplacement/test/average_500m_indice_accessibilite_"+type+"_"+dMax+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void noteEntity(String territoire, String type, int echelle, String continuite, String metric) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
	
		builder.setRasterFile("H:/rennes_metropole/continuite_ecologique/indice_"+continuite+"_"+type+"_"+echelle+"m.tif");
		builder.setEntityRasterFile("H:/rennes_metropole/continuite_ecologique/"+territoire+"_5m.tif");
		builder.addMetric(metric);
		builder.addGeoTiffOutput(metric, "H:/rennes_metropole/continuite_ecologique/"+territoire+"/"+territoire+"_"+metric+"_"+continuite+"_"+type+"_"+echelle+"m.tif");
		builder.addCsvOutput("H:/rennes_metropole/continuite_ecologique/"+territoire+"/"+territoire+"_"+metric+"_"+continuite+"_"+type+"_"+echelle+"m.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
	}
	
	private static void rasterizeCodeSecteur() {
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/continuite_ecologique/communes_rennes_metropole_L93.shp", "id_secteur", entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), entete.noDataValue());
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/secteur_5m.tif", dataN, entete);
	}
	
	private static void rasterizeCodeCommune() {
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/continuite_ecologique/communes_rennes_metropole_L93.shp", "insee", entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), entete.noDataValue());
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/commune_5m.tif", dataN, entete);
	}

	private static void rasterizeCodeMNIE() {
	
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/data/MNIE22/MNIE22/MNIE-SIG/MNIE_HAB_PAYS_2022.shp", "codeMNIE", entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), entete.noDataValue());
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/mnie_5m.tif", dataN, entete);
	}

	private static void fragmentationFonctionnelle(String type, int echelle, String continuite, float min, int echelle2){
		
		int ws = (echelle2*2/5)+1;
		String minV = ((int) (min * 100.0))+"";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/volume_deplacement/test/cluster_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+".tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(ws);
		builder.setDisplacement(10);
		builder.addMetric("SHDI");
		builder.addGeoTiffOutput("SHDI", "H:/rennes_metropole/volume_deplacement/test/fragmentation_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+"_"+echelle2+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void proportionFonctionnelle(String type, int echelle, String continuite, float min, int echelle2){
		
		int ws = (echelle2*2/5)+1;
		String minV = ((int) (min * 100.0))+"";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/volume_deplacement/test/classif_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+".tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(ws);
		builder.setDisplacement(10);
		builder.addMetric("pNV_1");
		builder.addGeoTiffOutput("pNV_1", "H:/rennes_metropole/volume_deplacement/test/proportion_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+"_"+echelle2+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void clusterFonctionnel(String type, int echelle, String continuite, float min){
		
		String minV = ((int) (min * 100.0))+"";
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/classif_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+".tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();

		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), new int[]{1}, entete.noDataValue());
		float[] outData = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/volume_deplacement/test/cluster_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+".tif", outData, entete);
		
	}
	
	private static void classifFonctionnelle(String type, int echelle, String continuite, float min){
		
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/indice_"+continuite+"_"+type+"_"+echelle+"m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value == -1){
					return -1;
				}
				if(value > min){
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		String minV = ((int) (min * 100.0))+"";
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/classif_indice_"+continuite+"_"+type+"_"+echelle+"m_min"+minV+".tif", outData, entete);
		
	}
	
	private static void testIndiceContinuiteHumide(){
		
		Coverage covSurface = CoverageManager.getCoverage("H:/rennes_metropole/test/surface_accessibilite_humide_2.tif");
		float[] dataSurface = covSurface.getData();
		EnteteRaster entete = covSurface.getEntete();
		covSurface.dispose();
		
		Coverage covVolume = CoverageManager.getCoverage("H:/rennes_metropole/test/volume_deplacement_humide_2.tif");
		float[] dataVolume = covVolume.getData();
		covVolume.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataSurface, dataVolume){

			@Override
			protected float doTreat(float[] v) {
				if(v[1] > 10000000){
					return v[1] / v[0];
				}
				return 0;
				//return v[0] / v[1];
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/test/rapport_humide_2_2.tif", outData, entete);
		
	}
		
	private static void fragmentationFonctionnelleBoise(int volumeMin){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/test/cluster_volume_deplacement_boise_9_"+volumeMin+".tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(201);
		builder.setDisplacement(10);
		builder.addMetric("SHDI");
		builder.addGeoTiffOutput("SHDI", "H:/rennes_metropole/test/fragmentation_volume_deplacement_boise_9_"+volumeMin+".tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void proportionFonctionnelleBoise(int volumeMin){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/test/classif_volume_deplacement_boise_9_"+volumeMin+".tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(201);
		builder.setDisplacement(10);
		builder.addMetric("pNV_1");
		builder.addGeoTiffOutput("pNV_1", "H:/rennes_metropole/test/proportion_volume_deplacement_boise_9_"+volumeMin+".tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void clusterFonctionnelBoise(int volumeMin){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/test/classif_volume_deplacement_boise_9_"+volumeMin+".tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();

		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), new int[]{1}, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/test/cluster_volume_deplacement_boise_9_"+volumeMin+".tif", dataCluster, entete);
		
	}
	
	private static void classifFonctionnelleBoise(int volumeMin){
		
		
		Coverage covVolume = CoverageManager.getCoverage("H:/rennes_metropole/test/volume_deplacement_boise_9.tif");
		float[] dataVolume = covVolume.getData();
		EnteteRaster entete = covVolume.getEntete();
		covVolume.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataVolume){

			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value > volumeMin){
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/test/classif_volume_deplacement_boise_9_"+volumeMin+".tif", outData, entete);
		
	}
	
	private static void testIndiceContinuite(String type, int dMax){
		
		Coverage covSurface = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/indice_accessibilite_"+type+"_"+dMax+"m.tif");
		float[] dataSurface = covSurface.getData();
		EnteteRaster entete = covSurface.getEntete();
		covSurface.dispose();
		
		Coverage covVolume = CoverageManager.getCoverage("H:/rennes_metropole/volume_deplacement/test/indice_deplacement_"+type+"_"+dMax+"m.tif");
		float[] dataVolume = covVolume.getData();
		covVolume.dispose();
		
		Coverage covPerm = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_"+type+".tif");
		float[] dataPerm = covPerm.getData();
		covPerm.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataSurface, dataVolume, dataPerm){

			@Override
			protected float doTreat(float[] v) {
				//return v[1] / (v[0]*v[2]);
				//return v[0] / (v[1]*v[2]);
				//return (v[0] / v[1]) * (1 / v[2]);
				return v[1] * (1/v[2]);
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/volume_deplacement/test/rapport_deplacement_sur_permeabilite_"+type+"_"+dMax+"m.tif", outData, entete);
		
	}
	
	
	
	private static void testIndiceContinuite2(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/test/volume_deplacement_boise_9.tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(401);
		builder.setDisplacement(10);
		builder.addMetric("average");
		builder.addGeoTiffOutput("average", "H:/rennes_metropole/test/volume_deplacement_boise_10_average.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void calculContinuity(String type, int dMax){
		
		int ws = (((dMax*2)/5)*4)+1;
		System.out.println(type+" "+dMax+" "+ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile("E:/rennes_metropole/continuite_ecologique_old/rm_os_bre.tif");
		builder.setRasterFile2("E:/rennes_metropole/continuite_ecologique/permeabilite_"+type+".tif");
		builder.setROIX(4000);
		builder.setROIY(4000);
		builder.setROIWidth(1000);
		builder.setROIHeight(1000);
		builder.setWindowSize(ws);
		builder.setDisplacement(1);
		builder.addMetric("surface");
		builder.addMetric("volume");
		builder.setDMax(dMax);
		builder.addGeoTiffOutput("surface", "E:/rennes_metropole/volume_deplacement/surface_accessibilite_"+type+"_"+dMax+".tif");
		builder.addGeoTiffOutput("volume", "E:/rennes_metropole/volume_deplacement/volume_deplacement_"+type+"_"+dMax+".tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void calculContinuityHumide(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_humide.tif");
		builder.setWindowSize(201);
		builder.setDisplacement(4);
		builder.addMetric("surface");
		builder.addMetric("volume");
		builder.setDMax(125.0);
		builder.addGeoTiffOutput("surface", "H:/rennes_metropole/test/surface_accessibilite_humide_2.tif");
		builder.addGeoTiffOutput("volume", "H:/rennes_metropole/test/volume_deplacement_humide_2.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void calculContinuityBoise(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_boise.tif");
		//builder.setROIX(4000);
		//builder.setROIY(4000);
		//builder.setROIWidth(1000);
		//builder.setROIHeight(1000);
		builder.setWindowSize(201);
		builder.setDisplacement(4);
		//builder.setInterpolation(true);
		builder.addMetric("surface");
		builder.addMetric("volume");
		builder.setDMax(250.0);
		builder.addGeoTiffOutput("surface", "H:/rennes_metropole/test/surface_accessibilite_boise_9.tif");
		builder.addGeoTiffOutput("volume", "H:/rennes_metropole/test/volume_deplacement_boise_9.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void calculContinuityGlobal(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		//builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_nvalid.tif");
		builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif");
		builder.setWindowSize(151);
		builder.setDisplacement(20);
		builder.addMetric("surface");
		builder.addMetric("volume");
		builder.setDMax(125.0);
		builder.addGeoTiffOutput("surface", "H:/rennes_metropole/test/surface_3.tif");
		builder.addGeoTiffOutput("volume", "H:/rennes_metropole/test/volume_3.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void calculFenetresFonctionnelles(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		//builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_nvalid.tif");
		builder.setRasterFile2("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif");
		builder.setWindowSize(501);
		builder.setDisplacement(20);
		builder.addMetric("N-valid");
		builder.addMetric("SHDI"); // bug � corriger
		builder.setDMax(125.0);
		//builder.addMetric("sum");
		//builder.addGeoTiffOutput("SHDI", "H:/rennes_metropole/continuite_ecologique/shdi_500m.tif");
		builder.addGeoTiffOutput("N-valid", "H:/rennes_metropole/test/nvalid_39.tif");
		//builder.addGeoTiffOutput("sum", "H:/rennes_metropole/test/sum_1.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void prepaFenetresFonctionnelles(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[data.length];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value != -1 && value > 0 && value < 1){
					return 1;
				}
				return value;
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/permeabilite_nvalid.tif", outData, entete);
		
	}
	
	private static void convert(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.asc", data, entete);
	}
	
	private static void calculClusterFonctionnel(){
		
		//calculClusterFonctionnelBoise(1000);
		//calculClusterFonctionnelHumide();
		
		for(int dc=0; dc<1000; dc+=50){
			calculClusterFonctionnelGlobal(dc);
		}
		
		
	}
	
	private static void calculClusterFonctionnelBoise(int dMax){
		
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_boise.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covDist = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/distance_boise.tif");
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(dataHab, dataDist, entete.width(), entete.height(), new int[]{1}, dMax, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/continuite_ecologique/cluster_boise_"+dMax+".tif", dataCluster, entete);
		
	}
	
	private static void calculClusterFonctionnelHumide(int dMax){
		
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_humide.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covDist = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/distance_humide.tif");
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(dataHab, dataDist, entete.width(), entete.height(), new int[]{1}, dMax, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/continuite_ecologique/cluster_humide_"+dMax+".tif", dataCluster, entete);
		
	}

	private static void calculClusterFonctionnelGlobal(int dMax){
	
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_global.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covDist = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/distance_globale.tif");
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		TabDistanceClusteringAnalysis ca = new TabDistanceClusteringAnalysis(dataHab, dataDist, entete.width(), entete.height(), new int[]{1}, dMax, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/continuite_ecologique/cluster_global_"+dMax+".tif", dataCluster, entete);
		
		TabClusteringOutput out = new TabClusteringOutput(dataCluster, dataHab, new int[]{1}, entete.cellsize(), entete.noDataValue());
		out.allRun();
		System.out.println(dMax+";"+out.getNbPatch());
	}
	
	private static void calculDistanceFonctionnelle(){
		
		//calculDistanceFonctionnelleBoise();
		//calculDistanceFonctionnelleHumide();
		calculDistanceFonctionnelleGlobale();
		
	}
	
	private static void calculDistanceFonctionnelleBoise(){
		
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_boise.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covPerm = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_boise.tif");
		float[] dataPerm = covPerm.getData();
		covPerm.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, dataHab, dataPerm, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.writeGeotiff("H:/rennes_metropole/continuite_ecologique/distance_boise.tif", data, entete);
		
	}
	
	private static void calculDistanceFonctionnelleHumide(){
		
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_humide.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covPerm = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_humide.tif");
		float[] dataPerm = covPerm.getData();
		covPerm.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, dataHab, dataPerm, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.writeGeotiff("H:/rennes_metropole/continuite_ecologique/distance_humide.tif", data, entete);
		
	}

	private static void calculDistanceFonctionnelleGlobale(){
	
		Coverage covHab = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/habitat_global.tif");
		float[] dataHab = covHab.getData();
		EnteteRaster entete = covHab.getEntete();
		covHab.dispose();
		
		Coverage covPerm = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif");
		float[] dataPerm = covPerm.getData();
		covPerm.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, dataHab, dataPerm, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.writeGeotiff("H:/rennes_metropole/test/distance_globale_4.tif", data, entete);
	}
	
	private static void prepaPermeabilite(){
		prepaPermeabiliteBoise();
		prepaPermeabiliteHumide();
		prepaPermeabiliteGlobale();
	}
	
	private static void prepaPermeabiliteBoise(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 10f); 
		sarMap.put(3f, 100f);
		sarMap.put(4f, 10f); 
		sarMap.put(5f, 5f); 
		sarMap.put(6f, 2f);
		sarMap.put(7f, 1f);
		sarMap.put(8f, 1f);
		sarMap.put(9f, 0.1f);
		sarMap.put(10f, 0.1f);
		sarMap.put(11f, 1f);
		sarMap.put(12f, 1f);
		sarMap.put(13f, 100f);
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/permeabilite_boise.tif", dataSR, entete);
	}
	
	private static void prepaPermeabiliteHumide(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 10f); 
		sarMap.put(3f, 100f);
		sarMap.put(4f, 10f); 
		sarMap.put(5f, 5f); 
		sarMap.put(6f, 1f);
		sarMap.put(7f, 5f);
		sarMap.put(8f, 5f);
		sarMap.put(9f, 10f);
		sarMap.put(10f, 10f);
		sarMap.put(11f, 1f);
		sarMap.put(12f, 10f);
		sarMap.put(13f, 0.1f);
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		Coverage covZH = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/zone_humide.tif");
		float[] dataZH = covZH.getData();
		covZH.dispose();
		
		for(int i=0; i<dataSR.length; i++){
			if(dataZH[i] == 1){
				dataSR[i] /= 10; 
			}
		}
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/permeabilite_humide.tif", dataSR, entete);
	}
	
	private static void prepaPermeabiliteGlobale(){
		
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 10f); 
		sarMap.put(3f, 100f);
		sarMap.put(4f, 10f); 
		sarMap.put(5f, 5f); 
		sarMap.put(6f, 1f);
		sarMap.put(7f, 1f);
		sarMap.put(8f, 1f);
		sarMap.put(9f, 0.1f);
		sarMap.put(10f, 0.1f);
		sarMap.put(11f, 1f);
		sarMap.put(12f, 1f);
		sarMap.put(13f, 0.1f);
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		Coverage covZH = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/zone_humide.tif");
		float[] dataZH = covZH.getData();
		covZH.dispose();
		
		for(int i=0; i<dataSR.length; i++){
			if(dataZH[i] == 1){
				dataSR[i] /= 10; 
			}
		}
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/permeabilite_globale.tif", dataSR, entete);
	}
	
	private static void rasterizeZoneHumide(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Arrays.fill(data, 0);
		
		String path = "G:/AUDIAR/Donn�es2Hugues/Donn�es2Hugues/ZonesHumides/";
		Coverage cov2;
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, entete, path+"izh_sage_vilaine/izh_sage_vilaine.shp", "code");
		cov2.dispose();
		
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, entete, path+"zone_humide_sage_couesnon/zone_humide_sage_couesnon.shp", "code");
		cov2.dispose();
		
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, entete, path+"zonehumidesagerfbb/zonehumidesagerfbb.shp", "code");
		cov2.dispose();
		
		CoverageManager.write(path+"zone_humide.tif", data, entete);
		
	}
	
	private static void prepaOS(){
		recuperationOS2021ebr();
		nettoyageOS2021ebr();
		nettoyageTypeBoisement();
		rasterizeVoies();
		rasterizeEau();
		compileOS();
	}
	
	private static void prepaHabitats(){
		rasterizeMNIEBoise();
		rasterizeMNIEHumide();
		rasterizeMNIEGlobaux();
	}
	
	private static void rasterizeMNIEGlobaux(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/data/MNIE22/MNIE22/MNIE-SIG/MNIE_HAB_PAYS_2022.shp", "code_globa", entete, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/habitat_global.tif", dataN, entete);
		
	}
	
	private static void rasterizeMNIEBoise(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/data/MNIE22/MNIE22/MNIE-SIG/MNIE_HAB_PAYS_2022.shp", "code_bois", entete, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/habitat_boise.tif", dataN, entete);
		
	}
	
	private static void rasterizeMNIEHumide(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/data/MNIE22/MNIE22/MNIE-SIG/MNIE_HAB_PAYS_2022.shp", "code_humid", entete, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/habitat_humide.tif", dataN, entete);
		
	}
	
	private static void compileOS(){
		
		// OSbre --> total = 6 couches � compiler
		// 1. reseau hydro --> 2 couches (surface et troncon)
		// 2. Route et autres voies --> 2 couches (route et voie ferre)
		// 3. Boisement --> 1 couche
		// 4. OS --> 1 couche
		float[][] datas = new float[6][];
		
		Coverage cov;
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/surface_eau.tif");
		EnteteRaster entete = cov.getEntete();
		datas[0] = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/troncon_eau.tif");
		datas[1] = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/route_clean.tif");
		datas[2] = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/voie_ferre.tif");
		datas[3] = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/type_boisement_clean.tif");
		datas[4] = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/os_2021_ebr_clean.tif");
		datas[5] = cov.getData();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		OverlayPixel2PixelTabCalculation cal = new OverlayPixel2PixelTabCalculation(data, entete.noDataValue(), datas);
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/rm_os_bre.tif", data, entete);
		
	}
	
	private static void rasterizeEau(){
		rasterizeSurfacesEau();
		rasterizeTronceauEau();
	}
	
	private static void rasterizeSurfacesEau(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/rennes_metropole/continuite_ecologique/SURFACE_HYDROGRAPHIQUE.shp", entete, 13, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/surface_eau.tif", dataN, entete);
		
	}
	
	private static void rasterizeTronceauEau(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getLinearCoverage("H:/rennes_metropole/continuite_ecologique/TRONCON_HYDROGRAPHIQUE.shp", entete, 13, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/troncon_eau.tif", dataN, entete);
		
	}
	
	private static void rasterizeVoies(){
		rasterizeRoutes();
		cleanRoutes();
		rasterizeVoiesFerres();
	}
	
	private static void rasterizeVoiesFerres(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getLinearCoverage("H:/rennes_metropole/continuite_ecologique/TRONCON_DE_VOIE_FERREE.shp", "code", entete, 0, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/voie_ferre.tif", dataN, entete);
		
	}
	
	private static void rasterizeRoutes(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covN = ShapeFile2CoverageConverter.getLinearCoverage("H:/rennes_metropole/continuite_ecologique/TRONCON_DE_ROUTE.shp", "importance", entete, 0, 0);
		float[] dataN = covN.getData();
		covN.dispose();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/route.tif", dataN, entete);
	}
	
	private static void cleanRoutes(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/route.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 3f); // 1 -  --> 3 - voie fr�quent�e
		sarMap.put(2f, 3f); // 2 -  --> 3 - voie fr�quent�e
		sarMap.put(3f, 3f); // 3 -  --> 3 - voie fr�quent�e
		sarMap.put(4f, 4f); // 4 - --> 4 - voie peu fr�quent�e
		sarMap.put(5f, 4f); // 5 -  --> 4 - voie peu fr�quent�e 
		sarMap.put(6f, 0f); // 6 -  --> ignor�
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/route_clean.tif", dataSR, entete);
		
	}
	
	private static void nettoyageTypeBoisement(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		
		sarMap.put(1f, 10f); // 1 - arbre isol� --> 10 - haie
		sarMap.put(5f, 9f); // 2 - massif --> 9 boisement
		sarMap.put(10f, 10f); // 3 - haie --> 10 - haie
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/type_boisement_clean.tif", dataSR, entete);
	}
	
	private static void nettoyageOS2021ebr(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/continuite_ecologique/os_2021_ebr.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(0f, 0f); // 0 - autre --> 0 - autre
		sarMap.put(1f, 1f); // 1 - bati --> 1 - bati
		sarMap.put(2f, 2f); // 2 - surface minerale, sable --> 2 - surface minerale
		sarMap.put(3f, 3f); // 3 - voie fr�quent�e --> 3 - voie fr�quent�e
		sarMap.put(4f, 4f); // 4 - voie peu fr�quent�e --> 4 - voie peu fr�quent�e
		sarMap.put(5f, 5f); // 5 - colza --> 5 - culture
		sarMap.put(6f, 5f); // 6 - c�r�ales � paille --> 5 - culture
		sarMap.put(7f, 5f); // 7 - prot�agineux --> 5 - culture
		sarMap.put(9f, 5f); // 9 - tournesol --> 5 - culture
		sarMap.put(10f, 5f); // 10 - ma�s --> 5 - culture
		sarMap.put(12f, 5f); // 12 - tubercules --> 5 - culture
		sarMap.put(13f, 6f); // 13 - prairie --> 6 - prairie
		sarMap.put(14f, 7f); // 14 - verger --> 7 - verger
		sarMap.put(15f, 8f); // 15 - vigne --> 8 - vignes
		sarMap.put(16f, 9f); // 16 - for�t --> 9 boisement
		sarMap.put(17f, 9f); // 17 - boisement --> 9 boisement
		sarMap.put(18f, 10f); // 18 - haie --> 10 - haie
		sarMap.put(19f, 11f); // 19 - lande ligneuse --> 11 - lande
		sarMap.put(20f, 12f); // 20 - peupleraie --> 12 - peupleraie
		sarMap.put(22f, 0f); // 22 - mer --> 0 - autre
		sarMap.put(23f, 13f); // 23 - eau --> 13 --> eau
		
		float[] dataSR = new float[data.length];
		
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataSR, data, sarMap);
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/os_2021_ebr_clean.tif", dataSR, entete);
	}
	
	private static void recuperationOS2021ebr(){
	
		Coverage cov1 = CoverageManager.getCoverage("H:/rennes_metropole/grain_bocager/rm_type_boisement.tif");
		EnteteRaster enteteRef = cov1.getEntete();
		cov1.dispose();
		
		Coverage cov = CoverageManager.getCoverage("G:/data/sig/grand_ouest/GO_2021_ebr.tif");
		EnteteRaster entete = cov.getEntete();
		
		float[] data = cov.getData(EnteteRaster.getROI(entete, enteteRef.getEnvelope()));
		cov.dispose();
		
		//System.out.println(enteteRef);
		//System.out.println(data.length);
		/*for(float v : data){
			System.out.println(v);
		}*/
		
		
		CoverageManager.write("H:/rennes_metropole/continuite_ecologique/os_2021_ebr.tif", data, enteteRef);
	}
	
	private static void rasterizeCosia(){
		
		String path = "H:/rennes_metropole/data/";
		Coverage cov = CoverageManager.getCoverage(path+"grain_rm_classif_5m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Arrays.fill(data, entete.noDataValue());
		
		for(String file : new File(path+"CoSIA_D035_2020/").list()){
			if(file.endsWith(".shp")){
				System.out.println(file);
				Coverage cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, entete, path+"CoSIA_D035_2020/"+file, "numero");
				data = cov2.getData();
				cov2.dispose();
			}
		}
		
		CoverageManager.write(path+"cosia_2020.tif", data, entete);
		
		
	}
	
}
