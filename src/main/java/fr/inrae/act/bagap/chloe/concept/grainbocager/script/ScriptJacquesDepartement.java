package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.util.CompileMNHC;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;

public class ScriptJacquesDepartement {

	public static void main(String[] args){
		
		/*
		String inputPath = "E:/IGN/data/";
		String outputPath = "E:/temp/jacques/boisement/";
		scriptmasqueBoisementDepartement(outputPath, inputPath, 22, 2018);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 29, 2018);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 35, 2020);
		scriptmasqueBoisementDepartement(outputPath, inputPath, 56, 2019);
		*/
		
		//String input = "D:/temp/jacques/PFboisements.asc";
		//String output = "D:/temp/jacques/PFboisements2.tif";
		//scriptConversion(input, output);
		//scriptSliding(input, output);
		
		//String path = "E:\\grain_bocager\\data/";
		//getEmpriseBoisement(path+"emprise/emprise_22_2018_ouverte.tif", path+"22/2018/22_2018_type_boisement.tif", path+"22/2018/22_2018_grain_bocager_5m.tif", 0.33, 1, 200);
		//getEmpriseBoisement(path+"emprise/emprise_22_2018_semi_ouverte.tif", path+"22/2018/22_2018_type_boisement.tif", path+"22/2018/22_2018_grain_bocager_5m.tif", 0.25, 0.33, 200);
		
		//getEmpriseBoisement(path+"emprise/emprise_22_2018_ouverte_100m.tif", path+"22/2018/22_2018_type_boisement.tif", path+"22/2018/22_2018_grain_bocager_5m.tif", 0.33, 1, 100);
		//getEmpriseBoisement(path+"emprise/emprise_22_2018_semi_ouverte_100m.tif", path+"22/2018/22_2018_type_boisement.tif", path+"22/2018/22_2018_grain_bocager_5m.tif", 0.25, 0.33, 100);
		
		//analyseMap(path+"emprise/emprise_22_2018_ouverte_100m.tif");
		//analyseMap(path+"emprise/emprise_22_2018_semi_ouverte_100m.tif");
		/*
		String path = "E:/temp/jacques/boisement/densite/";
		enjeux(path+"zone_densite_fonctionnel_22_2018.tif", path+"cluster_fonctionnel_22_2018.tif", path+"zone_proportion_fonctionnel_22_2018_1km.tif", path+"zone_fragmentation_fonctionnel_22_2018_1km.tif", path+"densite_22_2018_350m.tif", 0.2, 1);
		enjeux(path+"zone_densite_fonctionnel_29_2018.tif", path+"cluster_fonctionnel_29_2018.tif", path+"zone_proportion_fonctionnel_29_2018_1km.tif", path+"zone_fragmentation_fonctionnel_29_2018_1km.tif", path+"densite_29_2018_350m.tif", 0.2, 1);
		enjeux(path+"zone_densite_fonctionnel_35_2020.tif", path+"cluster_fonctionnel_35_2020.tif", path+"zone_proportion_fonctionnel_35_2020_1km.tif", path+"zone_fragmentation_fonctionnel_35_2020_1km.tif", path+"densite_35_2020_350m.tif", 0.2, 1);
		enjeux(path+"zone_densite_fonctionnel_56_2019.tif", path+"cluster_fonctionnel_56_2019.tif", path+"zone_proportion_fonctionnel_56_2019_1km.tif", path+"zone_fragmentation_fonctionnel_56_2019_1km.tif", path+"densite_56_2019_350m.tif", 0.2, 1);
		
		enjeux(path+"zone_densite_fonctionnel_22_2018.tif", path+"cluster_fonctionnel_22_2018.tif", path+"zone_proportion_fonctionnel_22_2018_5km.tif", path+"zone_fragmentation_fonctionnel_22_2018_5km.tif", path+"densite_22_2018_350m.tif", 0.2, 5);
		enjeux(path+"zone_densite_fonctionnel_29_2018.tif", path+"cluster_fonctionnel_29_2018.tif", path+"zone_proportion_fonctionnel_29_2018_5km.tif", path+"zone_fragmentation_fonctionnel_29_2018_5km.tif", path+"densite_29_2018_350m.tif", 0.2, 5);
		enjeux(path+"zone_densite_fonctionnel_35_2020.tif", path+"cluster_fonctionnel_35_2020.tif", path+"zone_proportion_fonctionnel_35_2020_5km.tif", path+"zone_fragmentation_fonctionnel_35_2020_5km.tif", path+"densite_35_2020_350m.tif", 0.2, 5);
		enjeux(path+"zone_densite_fonctionnel_56_2019.tif", path+"cluster_fonctionnel_56_2019.tif", path+"zone_proportion_fonctionnel_56_2019_5km.tif", path+"zone_fragmentation_fonctionnel_56_2019_5km.tif", path+"densite_56_2019_350m.tif", 0.2, 5);
		*/
		
		//analyseDepartement(35, 2020);
		
		//CompileMNHC.compile("E:/IGN/data_bretagne/mean/", "bretagne", new String[] {"E:/IGN/data/22_2018_5m/mean/", "E:/IGN/data/29_2018_5m/mean/", "E:/IGN/data/35_2020_5m/mean/", "E:/IGN/data/56_2019_5m/mean/"});
		analyseBretagne();
		
		
	}
	
	
	
	private static void analyseDepartement(int numDep, int annee) {
		
		//calculGrainBocager5m(numDep, annee);
		calculGrainBocager50m(numDep, annee);
		calculZoneEnjeux(numDep, annee, 1);
		calculZoneEnjeux(numDep, annee, 5);
	}
	
	private static void analyseBretagne() {
		
		// Grain Bocager
		//calculBretagneDistanceInfluence5m();
		//calculBretagneGrainBocager50m();
		//calculBretagneZoneEnjeux(1);
		//calculBretagneZoneEnjeux(5);
		
		// Densite Boisement
		//scriptMasqueBoisement();
		scriptProportionBoisement();
	}
	
	private static void scriptMasqueBoisement() {
		
		String dataPath = "E:/IGN/data_bretagne/";
		
		Util.createAccess(dataPath+"boisement/");
		
		File folder = new File(dataPath+"mean/");
		for(String file : folder.list()) {
			System.out.println(dataPath+"mean/"+file);
			
			Coverage cov = CoverageManager.getCoverage(dataPath+"mean/"+file);
			EnteteRaster entete = cov.getEntete();
			float[] inData = cov.getData();
			cov.dispose();
			
			float[] data = new float[entete.width()*entete.height()];
			Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, inData){
				@Override
				protected float doTreat(float[] v) {
					float v1 = v[0];
					if(v1 == -1){
						return -1;
					}
					if(v1 > 0) {
						return 1;
					}
					return 0;
				}
			};
			cal.run();
			
			CoverageManager.write(dataPath+"boisement/"+file, data, entete);
		}
	}
	
	private static void scriptProportionBoisement(){
		
		String dataPath = "E:/IGN/data_bretagne/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(dataPath+"boisement/");
		builder.addMetric("pNV_1");
		builder.setDisplacement(10);
		builder.addWindowSize(181);
		builder.addGeoTiffOutput("pNV_1", dataPath+"proportion_boisement_bretagne_450m_50m_threshold.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculBretagneDistanceInfluence5m() {
		
		String dataPath = "E:/IGN/data_bretagne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("influence_distance_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setTile(dataPath+"mean/");
		gbManager.setWoodHeight(dataPath+"mean/");
		gbManager.setWoodType(dataPath+"type_boisement/");
		gbManager.setInfluenceDistance(dataPath+"distance_influence/");
		gbManager.setOutputFolder(dataPath+"temp/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculBretagneGrainBocager50m() {
		
		String outputPath = "E:/IGN/data_bretagne/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setThresholds(0.2, 0.3, 0.45);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setGrainBocagerWindowRadius(450);
		gbManager.setInfluenceDistance(outputPath+"distance_influence/");
		gbManager.setGrainBocager(outputPath+"grain_bocager_bretagne_450m_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+"grain_bocager_bretagne_450m_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	
	private static void calculGrainBocager5m(int numDep, int annee) {
		
		String dataPath = "E:/IGN/data/";
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.3, 0.45);
		gbManager.setGrainBocagerWindowRadius(450);
		gbManager.setBocage(dataPath+numDep+"_"+annee+"_5m/mean/");
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_450m_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_450m_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculGrainBocager50m(int numDep, int annee) {
		
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.3, 0.45);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setGrainBocagerWindowRadius(450);
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_450m_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_450m_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculZoneEnjeux(int numDep, int annee, int km) {
		
		int ewr = km * 1000;
		
		String outputPath = "E:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
		gbManager.setFastMode(true); 
		gbManager.setThresholds(0.2, 0.3, 0.45);
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_450m_50m.tif");
		gbManager.setFunctionalGrainBocager(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_fonctionnel_450m_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_grain_bocager_cluster_450m_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_proportion_grain_bocager_fonc_450m_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(outputPath+numDep+"/"+annee+"/450m/"+numDep+"_"+annee+"_fragmentation_grain_bocager_fonc_450m_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void enjeux(String outputZoneFonctionnel, String outputClusterFonctionnel, String outputProportionFonctionnel, String outputFragmentationFonctionnel, String inputRaster, double seuil, int echelle) {
		
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		float[] dataDensiteFonctionnel = new float[entete.width()*entete.height()];
		
		System.out.println("detection des zones fonctionnelles");
		
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(dataDensiteFonctionnel, data){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] >= seuil){
					return 1;
				}
				return 0;
			}
		};
		pptcc.run();
		
		CoverageManager.write(outputZoneFonctionnel, dataDensiteFonctionnel, entete);
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), echelle*1000);
		System.out.println("proportion de zones fonctionnelles");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(outputZoneFonctionnel);
		builder.setEntete(entete);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addMetric("pNV_1");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(4);
		builder.addGeoTiffOutput(outputProportionFonctionnel);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		System.out.println("clusterisation fonctionnelles");
		
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(dataDensiteFonctionnel, entete.width(), entete.height(), new int[]{1}, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		
		CoverageManager.write(outputClusterFonctionnel, dataCluster, entete);
		
		dataDensiteFonctionnel = null;
		
		System.out.println("fragmentation fonctionnelles");
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(outputClusterFonctionnel);
		builder.setEntete(entete);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addMetric("SHDI");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(4);
		builder.addGeoTiffOutput(outputFragmentationFonctionnel);
		analysis = builder.build();
		
		analysis.allRun();
	}

	private static void analyseMap(String input){
		

		Coverage cov = CoverageManager.getCoverage(input);
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		int sum = 0;
		for(float v : data) {
			if(v >0) {
				sum++;
			}
		}
		
		System.out.println(sum);
	}
	
	private static void getEmpriseBoisement(String outputRaster, String typeBoisement, String grainBocager, double seuilMin, double seuilMax, double distanceMax) {
		
		Coverage covTB = CoverageManager.getCoverage(typeBoisement);
		EnteteRaster entete = covTB.getEntete();
		float[] dataTB = covTB.getData();
		covTB.dispose();
		
		Coverage covGB = CoverageManager.getCoverage(grainBocager);
		float[] dataGB = covGB.getData();
		covTB.dispose();
		
		System.out.println("récuapration des boisements");
		float[] dataBoisement = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(dataBoisement, dataTB, dataGB){
			@Override
			protected float doTreat(float[] v) {
				float vTB = v[0];
				float vGB = v[1];
				if(vGB == -1) {
					return -1;
				}
				if(vGB >= seuilMin && vGB <= seuilMax){
					if(vTB > 0) {
						return 1;
					}
				}
				return 0;
			}
		};
		cal.run();
		
		int[] codes =  new int[]{1};
		
		System.out.println("distance aux boisements");
		
		float[] dataDistance = new float[entete.width() * entete.height()];
		TabChamferDistanceAnalysis da = new TabChamferDistanceAnalysis(dataDistance, dataBoisement, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), codes, entete.noDataValue());
		da.allRun();
		
		dataBoisement = null;
		
		System.out.println("emprise");
		
		float[] dataEmprise = new float[entete.width() * entete.height()];
		cal = new Pixel2PixelTabCalculation(dataEmprise, dataDistance){
			@Override
			protected float doTreat(float[] v) {
				float vD = v[0];
				if(vD == -1) {
					return -1;
				}
				if(vD <= distanceMax){
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		dataDistance = null;
		
		CoverageManager.write(outputRaster, dataEmprise, entete);
	}
	
	
	
	private static void scriptConversion(String input, String output) {
		
		Coverage cov = CoverageManager.getCoverage(input);
		EnteteRaster entete = cov.getEntete();
		float[] inData = cov.getData();
		cov.dispose();
	
		
		CoverageManager.write(output, inData, entete);
	}
	
	private static void scriptSliding(String input, String output){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(input);
		builder.addMetric("pNV_1");
		builder.addWindowSize(201);
		builder.addGeoTiffOutput("pNV_1", output);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptmasqueBoisementDepartement(String outputPath, String inputPath, int numDep, int annee) {
		
		Coverage cov = CoverageManager.getCoverage(inputPath+numDep+"_"+annee+"_5m/mean/");
		EnteteRaster entete = cov.getEntete();
		float[] inData = cov.getData();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, inData){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				if(v1 > 0) {
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write(outputPath+"boisement_"+numDep+"_"+annee+".tif", data, entete);
	}
}
