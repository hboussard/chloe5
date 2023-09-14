package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.util.CompileMNHC;
import fr.inrae.act.bagap.chloe.window.WindowAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptLeandre {

	public static void main(String[] args){
		
		//compilationMNHC();
		
		//HugeGrainBocager.scriptGrainBocager("H:/leandre/grain_bocager/mnhc/", "H:/leandre/grain_bocager/", true);
		
		//scriptDetectionTypeBoisement();
		//scriptCalculDistanceInfluenceBoisement();
		//scriptCalculGrainBocager();
		
		//rasterizeUrbain();
		
		//cleanGrainBocager();
		
		//rasterizeCommune();
		
		//analyseGrainCommune();
	}
	
	private static void analyseGrainCommune() {
		
		//analyseGrainContinueCommune();
		analyseGrainContinueCleanCommune();
		//analyseGrain4ClassesCommune();
		analyseGrain4ClassesCleanCommune();
		
	}
	
	private static void analyseGrain4ClassesCommune() {
		
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(WindowAnalysisType.ENTITY);
		builder.setRasterFile("H:/leandre/grain_bocager/grain_bocager_4Classes_50m.tif");
		builder.setEntityRasterFile("H:/leandre/data/communes.tif");
		
		builder.addMetric("pN-valid");
		builder.addMetric("pNVm_1&2");
		
		builder.addCsvOutput("H:/leandre/grain_bocager/analyse/analyse_communes_grain_bocager_4Classes.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseGrain4ClassesCleanCommune() {
		
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(WindowAnalysisType.ENTITY);
		builder.setRasterFile("H:/leandre/grain_bocager/grain_bocager_4Classes_50m_clean.tif");
		builder.setEntityRasterFile("H:/leandre/data/communes.tif");
		
		//builder.addMetric("pN-valid");
		builder.addMetric("pNVm_1&2");
		
		builder.addCsvOutput("H:/leandre/grain_bocager/analyse/analyse_communes_grain_bocager_4Classes_clean.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	
	private static void analyseGrainContinueCommune() {
		
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(WindowAnalysisType.ENTITY);
		builder.setRasterFile("H:/leandre/grain_bocager/grain_bocager_50m.tif");
		builder.setEntityRasterFile("H:/leandre/data/communes.tif");
		
		builder.addMetric("pN-valid");
		//builder.addMetric("average");
		
		builder.addCsvOutput("H:/leandre/grain_bocager/analyse/analyse_communes_grain_bocager_continue.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseGrainContinueCleanCommune() {
		
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(WindowAnalysisType.ENTITY);
		builder.setRasterFile("H:/leandre/grain_bocager/grain_bocager_50m_clean.tif");
		builder.setEntityRasterFile("H:/leandre/data/communes.tif");
		
		builder.addMetric("pN-valid");
		builder.addMetric("average");
		
		builder.addCsvOutput("H:/leandre/grain_bocager/analyse/analyse_communes_grain_bocager_clean_continue.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void rasterizeCommune() {
		
		Coverage cov = CoverageManager.getCoverage("H:/leandre/grain_bocager/grain_bocager_50m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covCommune = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/leandre/data/communes.shp", "insee", entete, -1);
		
		CoverageManager.writeGeotiff("H:/leandre/data/communes.tif", covCommune.getData(), entete);
		
		covCommune.dispose();
		
	}
	
	private static void cleanGrainBocager() {
		
		Coverage covUrbain = CoverageManager.getCoverage("H:/leandre/grain_bocager/urbain.tif");
		EnteteRaster entete = covUrbain.getEntete();
		float[] dataUrbain = covUrbain.getData();
		covUrbain.dispose();
		
		Coverage covGC = CoverageManager.getCoverage("H:/leandre/grain_bocager/grain_bocager_50m.tif");
		float[] dataGC = covGC.getData();
		covGC.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataGC, dataUrbain){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] == -1){
					return -1;
				}
				if(v[1] == 1){
					return -1;
				}
				return v[0];
			}
		};
		pptc.run();
		CoverageManager.writeGeotiff("H:/leandre/grain_bocager/grain_bocager_50m_clean.tif", data, entete);
		
		Coverage covG4 = CoverageManager.getCoverage("H:/leandre/grain_bocager/grain_bocager_4Classes_50m.tif");
		float[] dataG4 = covG4.getData();
		covG4.dispose();
		
		data = new float[entete.width()*entete.height()];
		pptc = new Pixel2PixelTabCalculation(data, dataG4, dataUrbain){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] == -1){
					return -1;
				}
				if(v[1] == 1){
					return -1;
				}
				return v[0];
			}
		};
		pptc.run();
		CoverageManager.writeGeotiff("H:/leandre/grain_bocager/grain_bocager_4Classes_50m_clean.tif", data, entete);
		
	}
	
	private static void rasterizeUrbain() {
		
		Coverage cov = CoverageManager.getCoverage("H:/leandre/grain_bocager/grain_bocager_50m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Coverage covUrbain22 = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/bd_topo/zones_habitations/ZONE_D_HABITATION_22_2022.shp", entete, 1, 0);
		Coverage covUrbain29 = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/bd_topo/zones_habitations/ZONE_D_HABITATION_29_2021.shp", entete, 1, 0);
		Coverage covUrbain35 = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/bd_topo/zones_habitations/ZONE_D_HABITATION_35_2022.shp", entete, 1, 0);
		Coverage covUrbain44 = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/bd_topo/zones_habitations/ZONE_D_HABITATION_44_2022.shp", entete, 1, 0);
		Coverage covUrbain56 = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/bd_topo/zones_habitations/ZONE_D_HABITATION_56_2021.shp", entete, 1, 0);
		
		float[] dataUrbain = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(dataUrbain, covUrbain22.getData(), covUrbain29.getData(), covUrbain35.getData(), covUrbain44.getData(), covUrbain56.getData()){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] == 1 || v[1] == 1 || v[2] == 1 || v[3] == 1 || v[4] == 1){
					return 1;
				}
				return 0;
			}
		};
		pptc.run();
		
		covUrbain22.dispose();
		covUrbain29.dispose();
		covUrbain35.dispose();
		covUrbain44.dispose();
		covUrbain56.dispose();
		
		CoverageManager.writeGeotiff("H:/leandre/grain_bocager/urbain.tif", dataUrbain, entete);
	}
	
	private static void scriptDetectionTypeBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("H:/leandre/grain_bocager/mnhc/"));
		gbManager.setHauteurBoisement("H:/leandre/grain_bocager/mnhc/");
		gbManager.setOutputPath("H:/leandre/grain_bocager/");
		gbManager.setTypeBoisement("H:/leandre/grain_bocager/type_boisement/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptCalculDistanceInfluenceBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_distance_influence_boisement");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("H:/leandre/grain_bocager/mnhc/"));
		gbManager.setHauteurBoisement("H:/leandre/grain_bocager/mnhc/");
		gbManager.setTypeBoisement("H:/leandre/grain_bocager/type_boisement/");
		gbManager.setDistanceInfluenceBoisement("H:/leandre/grain_bocager/distance_influence_boisement/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptCalculGrainBocager() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setDistanceInfluenceBoisement("H:/leandre/grain_bocager/distance_boisement/");
		gbManager.setGrainCellSize(50.0);
		gbManager.setGrainBocager("H:/leandre/grain_bocager/grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes("H:/leandre/grain_bocager/grain_bocager_4Classes_50m.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private void compilationMNHC(){
		String outputPath = "H:/leandre/grain_bocager/mnhc/";
		String outputName = "mnhc";
		String[] inputPath = new String[]{
				"H:/IGN/data/22_2018_5m/mean/",
				"H:/IGN/data/29_2018_5m/mean/",
				"H:/IGN/data/35_2020_5m/mean/",
				"H:/IGN/data/44_2020_5m/mean/",
				"H:/IGN/data/56_2019_5m/mean/"
		};
		
		CompileMNHC.compile(outputPath, outputName, inputPath);
	}
	
}
