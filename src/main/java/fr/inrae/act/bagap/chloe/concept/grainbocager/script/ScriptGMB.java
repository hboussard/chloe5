package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.analysis.Stats;
import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
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
		//classificationContinuite(path+"indice_continuite_boisee_0-10_150m.tif");
		
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 800);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_mars_0-10_150m.tif", path+"Couts-0_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag_clean.tif", 1, 800);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 3300);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_800m.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 9, 3300);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m_essaie1.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 2, 800);
		//clusteringContinuite(path+"classif_indice_continuite_boisee_0-10_150m_essaie2.tif", path+"Couts_0-10_MusAve2025_SansSeuillage_modelANN_13var_clean.tif", 3, 800);
		
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
		//meanDistance(path+"classif_indice_continuite_boisee_0-10_150m_essaie1.tif", 2, 800);
		meanDistance(path+"classif_indice_continuite_boisee_0-10_150m_essaie2.tif", 3, 800);
		
		//distanceFromHabitat(800);
		//clusterFromHabitat(800);
		
		// classification des distributions GMB de Muscardin
		// sur quelle carte ? DistributionPotentielleMuscardin2025_ClassifBiotope-Raster10m_BzhHisto_L93
		// quels seuils ? les 5 seuils de la couche
		// combien de classes ? 5
		// calcul de distances fonctionnelles à ces seuils
		//calculDistanceSeuilsMuscardin();
		// moyennage des seuils
		//meanDistanceMuscardin();
		// calcul des clusters pour visualisation
		//clusterFromHabitatMuscardin(800);	

		//rasterizeSampling();
		//rasterizeSamplingMAJ();
		//analyseSampling();
		//analyseSamplingSdm();
		//analyseSamplingInrae();
		//analyseSamplingInraeEssaie1();
		analyseSamplingInraeEssaie2();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void analyseSamplingInraeEssaie2() {
		
		Coverage covMuscardin = CoverageManager.getCoverage(path+"muscardin/data_muscardin_maj.tif");
		float[] dataMuscardin = covMuscardin.getData();
		EnteteRaster entete = covMuscardin.getEntete();
		covMuscardin.dispose();
		
	
		Coverage covDist1 = CoverageManager.getCoverage(path+"distance_1_classif_indice_continuite_boisee_0-10_150m_essaie2_800m.tif");
		float[] dataDist1 = covDist1.getData();
		covDist1.dispose();
		
		Coverage covDist2 = CoverageManager.getCoverage(path+"distance_2_classif_indice_continuite_boisee_0-10_150m_essaie2_800m.tif");
		float[] dataDist2 = covDist2.getData();
		covDist2.dispose();
		
		Coverage covDist3 = CoverageManager.getCoverage(path+"distance_3_classif_indice_continuite_boisee_0-10_150m_essaie2_800m.tif");
		float[] dataDist3 = covDist3.getData();
		covDist3.dispose();
	
		try {
			CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie2_presence_inrae.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie2_presence_pseudoabs_inrae.csv");
			cw.setDelimiter(';');
			cw.write("observation");
			cw.write("presence");
			cw.write("dist_inrae_1");
			cw.write("dist_inrae_2");
			cw.write("dist_inrae_3");
			
			cw.endRecord();
			
			boolean ok;
			
			for(int ind=0; ind<entete.width()*entete.height(); ind++) {
		
				float vMuscardin = dataMuscardin[ind];
				if(vMuscardin > 0) {
				
					ok = false;
					
					switch((int) vMuscardin) {
					case 1 : 
						cw.write("presence");
						cw.write("1");
						ok = true;
						break;
					case 2 : 
						cw.write("abscence");
						cw.write("0");
						ok = true;
						break;
						/*
					case 3 : 
						cw.write("pseudo_abs");
						cw.write("0");
						ok = true;
						break;*/
					}
						
					if(ok) {
						cw.write(dataDist1[ind]+"");
						cw.write(dataDist2[ind]+"");
						cw.write(dataDist3[ind]+"");
						cw.endRecord();
					}
				}
			}

			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void analyseSamplingInraeEssaie1() {
		
		Coverage covMuscardin = CoverageManager.getCoverage(path+"muscardin/data_muscardin_maj.tif");
		float[] dataMuscardin = covMuscardin.getData();
		EnteteRaster entete = covMuscardin.getEntete();
		covMuscardin.dispose();
		
	
		Coverage covDist1 = CoverageManager.getCoverage(path+"distance_1_classif_indice_continuite_boisee_0-10_150m_essaie1_800m.tif");
		float[] dataDist1 = covDist1.getData();
		covDist1.dispose();
		
		Coverage covDist2 = CoverageManager.getCoverage(path+"distance_2_classif_indice_continuite_boisee_0-10_150m_essaie1_800m.tif");
		float[] dataDist2 = covDist2.getData();
		covDist2.dispose();
	
		try {
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_inrae.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_pseudoabs_inrae.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_inrae.csv");
			CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_pseudoabs_inrae.csv");
			cw.setDelimiter(';');
			cw.write("observation");
			cw.write("presence");
			cw.write("dist_inrae_1");
			cw.write("dist_inrae_2");
			
			cw.endRecord();
			
			boolean ok;
			
			for(int ind=0; ind<entete.width()*entete.height(); ind++) {
		
				float vMuscardin = dataMuscardin[ind];
				if(vMuscardin > 0) {
				
					ok = false;
					
					switch((int) vMuscardin) {
					case 1 : 
						cw.write("presence");
						cw.write("1");
						ok = true;
						break;
					/*case 2 : 
						cw.write("abscence");
						cw.write("0");
						ok = true;
						break;
						*/
					case 3 : 
						cw.write("pseudo_abs");
						cw.write("0");
						ok = true;
						break;
					}
						
					if(ok) {
						cw.write(dataDist1[ind]+"");
						cw.write(dataDist2[ind]+"");
						cw.endRecord();
					}
				}
			}

			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void analyseSamplingInrae() {
		
		Coverage covMuscardin = CoverageManager.getCoverage(path+"muscardin/data_muscardin_maj.tif");
		float[] dataMuscardin = covMuscardin.getData();
		EnteteRaster entete = covMuscardin.getEntete();
		covMuscardin.dispose();
		
	
		Coverage covDist1 = CoverageManager.getCoverage(path+"distance_1_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist1 = covDist1.getData();
		covDist1.dispose();
		
		Coverage covDist2 = CoverageManager.getCoverage(path+"distance_2_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist2 = covDist2.getData();
		covDist2.dispose();
		
		Coverage covDist3 = CoverageManager.getCoverage(path+"distance_3_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist3 = covDist3.getData();
		covDist3.dispose();
		
		Coverage covDist4 = CoverageManager.getCoverage(path+"distance_4_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist4 = covDist4.getData();
		covDist4.dispose();
		
		Coverage covDist5 = CoverageManager.getCoverage(path+"distance_5_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist5 = covDist5.getData();
		covDist5.dispose();
		
		Coverage covDist6 = CoverageManager.getCoverage(path+"distance_6_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist6 = covDist6.getData();
		covDist6.dispose();
		
		Coverage covDist7 = CoverageManager.getCoverage(path+"distance_7_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist7 = covDist7.getData();
		covDist7.dispose();
		
		Coverage covDist8 = CoverageManager.getCoverage(path+"distance_8_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist8 = covDist8.getData();
		covDist8.dispose();
		
		Coverage covDist9 = CoverageManager.getCoverage(path+"distance_9_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDist9 = covDist9.getData();
		covDist9.dispose();
	
		try {
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_inrae.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_pseudoabs_inrae.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_inrae.csv");
			CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_pseudoabs_inrae.csv");
			cw.setDelimiter(';');
			cw.write("observation");
			cw.write("presence");
			cw.write("dist_inrae_1");
			cw.write("dist_inrae_2");
			cw.write("dist_inrae_3");
			cw.write("dist_inrae_4");
			cw.write("dist_inrae_5");
			cw.write("dist_inrae_6");
			cw.write("dist_inrae_7");
			cw.write("dist_inrae_8");
			cw.write("dist_inrae_9");
			
			cw.endRecord();
			
			boolean ok;
			
			for(int ind=0; ind<entete.width()*entete.height(); ind++) {
		
				float vMuscardin = dataMuscardin[ind];
				if(vMuscardin > 0) {
				
					ok = false;
					
					switch((int) vMuscardin) {
					case 1 : 
						cw.write("presence");
						cw.write("1");
						ok = true;
						break;
					/*case 2 : 
						cw.write("abscence");
						cw.write("0");
						ok = true;
						break;*/
						
					case 3 : 
						cw.write("pseudo_abs");
						cw.write("0");
						ok = true;
						break;
					}
						
					if(ok) {
						cw.write(dataDist1[ind]+"");
						cw.write(dataDist2[ind]+"");
						cw.write(dataDist3[ind]+"");
						cw.write(dataDist4[ind]+"");
						cw.write(dataDist5[ind]+"");
						cw.write(dataDist6[ind]+"");
						cw.write(dataDist7[ind]+"");
						cw.write(dataDist8[ind]+"");
						cw.write(dataDist9[ind]+"");
						cw.endRecord();
					}
				}
			}

			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void analyseSamplingSdm() {
		
		Coverage covMuscardin = CoverageManager.getCoverage(path+"muscardin/data_muscardin_maj.tif");
		float[] dataMuscardin = covMuscardin.getData();
		EnteteRaster entete = covMuscardin.getEntete();
		covMuscardin.dispose();
		
	
		Coverage covDist1 = CoverageManager.getCoverage(path+"distance_habitat_muscardin_1-2-3-4-5.tif");
		float[] dataDist1 = covDist1.getData();
		covDist1.dispose();
		
		Coverage covDist2 = CoverageManager.getCoverage(path+"distance_habitat_muscardin_2-3-4-5.tif");
		float[] dataDist2 = covDist2.getData();
		covDist2.dispose();
		
		Coverage covDist3 = CoverageManager.getCoverage(path+"distance_habitat_muscardin_3-4-5.tif");
		float[] dataDist3 = covDist3.getData();
		covDist3.dispose();
		
		Coverage covDist4 = CoverageManager.getCoverage(path+"distance_habitat_muscardin_4-5.tif");
		float[] dataDist4 = covDist4.getData();
		covDist4.dispose();
		
		Coverage covDist5 = CoverageManager.getCoverage(path+"distance_habitat_muscardin_5.tif");
		float[] dataDist5 = covDist5.getData();
		covDist5.dispose();
	
		try {
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_sdm.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_pseudoabs_sdm.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_sdm.csv");
			CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_pseudoabs_sdm.csv");
			cw.setDelimiter(';');
			cw.write("observation");
			cw.write("presence");
			cw.write("dist_sdm_1");
			cw.write("dist_sdm_2");
			cw.write("dist_sdm_3");
			cw.write("dist_sdm_4");
			cw.write("dist_sdm_5");
			
			cw.endRecord();
			
			boolean ok;
			
			for(int ind=0; ind<entete.width()*entete.height(); ind++) {
		
				float vMuscardin = dataMuscardin[ind];
				if(vMuscardin > 0) {
				
					ok = false;
					
					switch((int) vMuscardin) {
					case 1 : 
						cw.write("presence");
						cw.write("1");
						ok = true;
						break;
					/*case 2 : 
						cw.write("abscence");
						cw.write("0");
						ok = true;
						break;
						*/
					case 3 : 
						cw.write("pseudo_abs");
						cw.write("0");
						ok = true;
						break;
					}
						
					if(ok) {
						cw.write(dataDist1[ind]+"");
						cw.write(dataDist2[ind]+"");
						cw.write(dataDist3[ind]+"");
						cw.write(dataDist4[ind]+"");
						cw.write(dataDist5[ind]+"");
						cw.endRecord();
					}
				}
			}

			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analyseSampling() {
		
		Coverage covMuscardin = CoverageManager.getCoverage(path+"muscardin/data_muscardin_maj.tif");
		float[] dataMuscardin = covMuscardin.getData();
		EnteteRaster entete = covMuscardin.getEntete();
		covMuscardin.dispose();
		
		Coverage covDist = CoverageManager.getCoverage(path+"DistributionPotentielleMuscardin2025_Modelisation13var-Raster10m_BzhHisto_L93.tif");
		float[] dataDist = covDist.getData();
		covDist.dispose();
		
		Coverage covIndCont = CoverageManager.getCoverage(path+"indice_continuite_boisee_0-10_150m.tif");
		float[] dataIndCont = covIndCont.getData();
		covIndCont.dispose();
		
		Coverage covDistMean = CoverageManager.getCoverage(path+"distance_mean_classif_indice_continuite_boisee_0-10_150m_3300m.tif");
		float[] dataDistMean = covDistMean.getData();
		covDistMean.dispose();
		
		Coverage covDistHabitatMean = CoverageManager.getCoverage(path+"distance_mean_habitat_muscardin.tif");
		float[] dataDistHabitatMean = covDistHabitatMean.getData();
		covDistHabitatMean.dispose();
		
		try {
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_maj_presence_pseudoabs.csv");
			//CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence.csv");
			CsvWriter cw = new CsvWriter(path+"muscardin/analyse_muscardin_essaie1_presence_pseudoabs.csv");
			cw.setDelimiter(';');
			cw.write("observation");
			cw.write("presence");
			cw.write("sdm");
			cw.write("inrae");
			cw.write("dist_sdm");
			cw.write("dist_inrae");
			
			cw.endRecord();
			
			boolean ok;
			
			for(int ind=0; ind<entete.width()*entete.height(); ind++) {
		
				float vMuscardin = dataMuscardin[ind];
				if(vMuscardin > 0) {
				
					ok = false;
					
					switch((int) vMuscardin) {
					case 1 : 
						cw.write("presence");
						cw.write("1");
						ok = true;
						break;
					/*case 2 : 
						cw.write("abscence");
						cw.write("0");
						ok = true;
						break;*/
					
					case 3 : 
						cw.write("pseudo_abs");
						cw.write("0");
						ok = true;
						break;
					}
						
					if(ok) {
						cw.write(dataDist[ind]+"");
						cw.write(dataIndCont[ind]+"");
						cw.write(dataDistHabitatMean[ind]+"");
						cw.write(dataDistMean[ind]+"");
						cw.endRecord();
					}
				}
			}

			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void calculDistanceSeuilsMuscardin() {
		
		String permeabilite = "C:/Data/projet/gmb/data/Couts-0_10-MusAve2025_SansSeuil_ModelMARS12varSansFrag_clean.tif";
		Coverage covPermeabilite = CoverageManager.getCoverage(permeabilite);
		float[] dataPermeabilite = covPermeabilite.getData();
		EnteteRaster entete = covPermeabilite.getEntete();
		covPermeabilite.dispose();
		
		String habitat = "C:/Data/projet/gmb/data/DistributionPotentielleMuscardin2025_ClassifBiotope-Raster10m_BzhHisto_L93.tif";
		Coverage covHabitat = CoverageManager.getCoverage(habitat);
		float[] dataHabitat = covHabitat.getData();
		covHabitat.dispose();
		
		float[] dataDistance;
		TabRCMDistanceAnalysis da;
		
		dataDistance = new float[entete.width()*entete.height()];
		da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {5});
		da.allRun();
		CoverageManager.write(path+"distance_habitat_muscardin_5.tif", dataDistance, entete);
		
		dataDistance = new float[entete.width()*entete.height()];
		da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {4, 5});
		da.allRun();
		CoverageManager.write(path+"distance_habitat_muscardin_4-5.tif", dataDistance, entete);
		
		dataDistance = new float[entete.width()*entete.height()];
		da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {3, 4, 5});
		da.allRun();
		CoverageManager.write(path+"distance_habitat_muscardin_3-4-5.tif", dataDistance, entete);
		
		dataDistance = new float[entete.width()*entete.height()];
		da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {2, 3, 4, 5});
		da.allRun();
		CoverageManager.write(path+"distance_habitat_muscardin_2-3-4-5.tif", dataDistance, entete);
		
		dataDistance = new float[entete.width()*entete.height()];
		da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {1, 2, 3, 4, 5});
		da.allRun();
		CoverageManager.write(path+"distance_habitat_muscardin_1-2-3-4-5.tif", dataDistance, entete);
		
	}
	
	private static void rasterizeSamplingMAJ() {
		
		Coverage covRef = CoverageManager.getCoverage(path+"indice_continuite_boisee_0-10_150m.tif");
		//float[] dataRef = covRef.getData();
		EnteteRaster enteteRef = covRef.getEntete();
		covRef.dispose();
		
		float[] outData = new float[enteteRef.width()*enteteRef.height()];
		
		SpatialCsvManager.exportTab(outData, path+"muscardin/Donnees_Pres_Abs_PseudoAbs-MusAve-2016_2025-MAJ-2.csv", "presence", enteteRef);
		
		CoverageManager.writeGeotiff(path+"muscardin/data_muscardin_maj.tif", outData, enteteRef);
	}
	
	private static void rasterizeSampling() {
		
		Coverage covRef = CoverageManager.getCoverage(path+"indice_continuite_boisee_0-10_150m.tif");
		//float[] dataRef = covRef.getData();
		EnteteRaster enteteRef = covRef.getEntete();
		covRef.dispose();
	
		ShapeFile2CoverageConverter.rasterize(path+"muscardin/data_muscardin.tif", path+"muscardin/DataMusAve-2010_2024-PresAbsPeudoAbs-L93.shp", "presence", 0, enteteRef);
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
		
		Coverage covK = CoverageManager.getCoverage(path+"distance_2_"+name+"_"+dMax+"m.tif");
		float[] data = covK.getData();
		EnteteRaster entete = covK.getEntete();
		covK.dispose();
			
		for(int k=3; k<=nbDomains; k++) {
			
			covK = CoverageManager.getCoverage(path+"distance_"+k+"_"+name+"_"+dMax+"m.tif");
			float[] dataK = covK.getData();
			covK.dispose();
			
			for(int i=0; i<data.length; i++) {
				float v = data[i];
				if(v != entete.noDataValue()) {
					data[i] = (v*(k-2) + dataK[i]) / (k-1);
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
	
	private static void classificationContinuite(String indice_deplacement) {
		
		File f = new File(indice_deplacement);
		String name = f.getName().replaceAll(".tif", "");
		
		Coverage cov = CoverageManager.getCoverage(indice_deplacement);
		float[] inData = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] dataClassif = new float[entete.width()*entete.height()];
		
		int domain = 0;
		Map<Domain<Float, Float>, Integer> domains = new LinkedHashMap<Domain<Float, Float>, Integer>();	
		//domains.put(DomainFactory.getFloatDomain("[0, 0.0025]"), domain++);
		//domains.put(DomainFactory.getFloatDomain("]0.0025, 0.065]"), domain++);
		//domains.put(DomainFactory.getFloatDomain("]0.065, ]"), domain++);
		
		domains.put(DomainFactory.getFloatDomain("[0, 0.002]"), domain++);
		domains.put(DomainFactory.getFloatDomain("]0.002, 0.04]"), domain++);
		domains.put(DomainFactory.getFloatDomain("]0.04, 0.33]"), domain++);
		domains.put(DomainFactory.getFloatDomain("]0.33, ]"), domain++);
		
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(dataClassif, inData, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"classif_"+name+"_essaie2.tif", dataClassif, entete);
		
	}
	
	private static void meanDistanceMuscardin() {
		
		int k;
		float[] dataK;
		
		Coverage covK = CoverageManager.getCoverage(path+"distance_habitat_muscardin_5.tif");
		float[] data = covK.getData();
		EnteteRaster entete = covK.getEntete();
		covK.dispose();
		
		covK = CoverageManager.getCoverage(path+"distance_habitat_muscardin_4-5.tif");
		dataK = covK.getData();
		covK.dispose();
		k = 2;
		for(int i=0; i<data.length; i++) {
			float v = data[i];
			if(v != entete.noDataValue()) {
				data[i] = (v*(k-1) + dataK[i]) / k;
			}
		}
		
		covK = CoverageManager.getCoverage(path+"distance_habitat_muscardin_3-4-5.tif");
		dataK = covK.getData();
		covK.dispose();
		k = 3;
		for(int i=0; i<data.length; i++) {
			float v = data[i];
			if(v != entete.noDataValue()) {
				data[i] = (v*(k-1) + dataK[i]) / k;
			}
		}
		
		covK = CoverageManager.getCoverage(path+"distance_habitat_muscardin_2-3-4-5.tif");
		dataK = covK.getData();
		covK.dispose();
		k = 4;
		for(int i=0; i<data.length; i++) {
			float v = data[i];
			if(v != entete.noDataValue()) {
				data[i] = (v*(k-1) + dataK[i]) / k;
			}
		}
		
		covK = CoverageManager.getCoverage(path+"distance_habitat_muscardin_1-2-3-4-5.tif");
		dataK = covK.getData();
		covK.dispose();
		k = 5;
		for(int i=0; i<data.length; i++) {
			float v = data[i];
			if(v != entete.noDataValue()) {
				data[i] = (v*(k-1) + dataK[i]) / k;
			}
		}
		
		CoverageManager.writeGeotiff(path+"distance_mean_habitat_muscardin.tif", data, entete);
	}
	
	
	
	private static void clusterFromHabitatMuscardin(int dMax) {

		String habitat = "C:/Data/projet/gmb/data/DistributionPotentielleMuscardin2025_ClassifBiotope-Raster10m_BzhHisto_L93.tif";
		Coverage covHabitat = CoverageManager.getCoverage(habitat);
		float[] dataHabitat = covHabitat.getData();
		EnteteRaster entete = covHabitat.getEntete();
		covHabitat.dispose();
		
		Coverage covDistance;
		float[] dataDistance;
		float[] dataCluster;
		TabDistanceClusteringAnalysis ca;
		
		covDistance = CoverageManager.getCoverage(path+"distance_habitat_muscardin_5.tif");
		dataDistance = covDistance.getData();
		covDistance.dispose();
		dataCluster = new float[entete.width()*entete.height()];
		ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {5}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		CoverageManager.write(path+"cluster_habitat_muscardin_5_"+dMax+"m.tif", dataCluster, entete);
		
		covDistance = CoverageManager.getCoverage(path+"distance_habitat_muscardin_4-5.tif");
		dataDistance = covDistance.getData();
		covDistance.dispose();
		dataCluster = new float[entete.width()*entete.height()];
		ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {4, 5}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		CoverageManager.write(path+"cluster_habitat_muscardin_4-5_"+dMax+"m.tif", dataCluster, entete);
		
		covDistance = CoverageManager.getCoverage(path+"distance_habitat_muscardin_3-4-5.tif");
		dataDistance = covDistance.getData();
		covDistance.dispose();
		dataCluster = new float[entete.width()*entete.height()];
		ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {3, 4, 5}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		CoverageManager.write(path+"cluster_habitat_muscardin_3-4-5_"+dMax+"m.tif", dataCluster, entete);
		
		covDistance = CoverageManager.getCoverage(path+"distance_habitat_muscardin_2-3-4-5.tif");
		dataDistance = covDistance.getData();
		covDistance.dispose();
		dataCluster = new float[entete.width()*entete.height()];
		ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {2, 3, 4, 5}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		CoverageManager.write(path+"cluster_habitat_muscardin_2-3-4-5_"+dMax+"m.tif", dataCluster, entete);
		
		covDistance = CoverageManager.getCoverage(path+"distance_habitat_muscardin_1-2-3-4-5.tif");
		dataDistance = covDistance.getData();
		covDistance.dispose();
		dataCluster = new float[entete.width()*entete.height()];
		ca = new TabDistanceClusteringAnalysis(dataHabitat, dataDistance, entete.width(), entete.height(), new int[] {1, 2, 3, 4, 5}, (double) dMax, entete.noDataValue());
		dataCluster = (float[]) ca.allRun();
		CoverageManager.write(path+"cluster_habitat_muscardin_1-2-3-4-5_"+dMax+"m.tif", dataCluster, entete);
		
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

	private static void distanceFromHabitat(String distance, String habitat, String permeabilite, int dMax) {

		Coverage covHabitat = CoverageManager.getCoverage(habitat);
		float[] dataHabitat = covHabitat.getData();
		EnteteRaster entete = covHabitat.getEntete();
		covHabitat.dispose();
		
		Coverage covPermeabilite = CoverageManager.getCoverage(permeabilite);
		float[] dataPermeabilite = covPermeabilite.getData();
		covPermeabilite.dispose();
		
		float[] dataDistance = new float[entete.width()*entete.height()];
		
		TabRCMDistanceAnalysis da = new TabRCMDistanceAnalysis(dataDistance, dataHabitat, dataPermeabilite, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {1}, dMax);
		da.allRun();
		
		CoverageManager.write(distance, dataDistance, entete);
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
