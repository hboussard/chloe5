package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;
import java.util.StringTokenizer;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;
import fr.inrae.act.bagap.raster.analysis.Pixel2PixelTileCoverageCalculation;

public class ScriptPaysDeLaLoire {

	public static void main(String[] args){
		
		//convertAsciiToTif();
		
		//scriptRecuperationHauteurBoisement();
		//scriptDetectionTypeBoisement();
		//scriptCalculDistanceInfluenceBoisement();
		//scriptCalculGrainBocager();
		//scriptClusterisationZoneGrainBocagerFonctionnel();
		//scriptCalculEnjeuxGlobaux();
		//scriptEnsembleCalculGrainBocager();
		
		//scriptPrepareAnalyseDensiteBoisement();
		//scriptDensiteBoisement();
	}

	
	private static void convertAsciiToTif() {
		
		//String path = "H:/IGN/analyse_pays_de_la_loire/";
		
		/*
		Coverage cov = CoverageManager.getCoverage(path+"prop_grain_fin_pays_de_la_loire_51p.asc");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		CoverageManager.write(path+"proportion_grain_bocager_fonctionnel_pays_de_la_loire_1-25km.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage(path+"shdi_grain_fin_pays_de_la_loire_51p.asc");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		CoverageManager.write(path+"fragmentation_grain_bocager_fonctionnel_pays_de_la_loire_1-25km.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage(path+"prop_grain_fin_pays_de_la_loire_201p.asc");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		CoverageManager.write(path+"proportion_grain_bocager_fonctionnel_pays_de_la_loire_5km.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage(path+"shdi_grain_fin_pays_de_la_loire_201p.asc");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		CoverageManager.write(path+"fragmentation_grain_bocager_fonctionnel_pays_de_la_loire_5km.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/44_2020_5m/bocage_distance_massif/");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		System.out.println(entete.width()+" "+entete.height());
		CoverageManager.write("H:/IGN/analyse_pays_de_la_loire/type_boisement_dpt44.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/49_2020_5m/bocage_distance_massif/");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		System.out.println(entete.width()+" "+entete.height());
		CoverageManager.write("H:/IGN/analyse_pays_de_la_loire/type_boisement_dpt49.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/53_2019_5m/bocage_distance_massif/");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		System.out.println(entete.width()+" "+entete.height());
		CoverageManager.write("H:/IGN/analyse_pays_de_la_loire/type_boisement_dpt53.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/72_2019_5m/bocage_distance_massif/");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		System.out.println(entete.width()+" "+entete.height());
		CoverageManager.write("H:/IGN/analyse_pays_de_la_loire/type_boisement_dpt72.tif", data, entete);
		*/
		/*
		Coverage cov = CoverageManager.getCoverage("H:/IGN/data/85_2019_5m/bocage_distance_massif/");
		float[] data = cov.getDatas();
		EnteteRaster entete = cov.getEntete();
		System.out.println(entete.width()+" "+entete.height());
		CoverageManager.write("H:/IGN/analyse_pays_de_la_loire/type_boisement_dpt85.tif", data, entete);
		*/
		
	}


	private static void scriptDetectionTypeBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("detection_type_boisement");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setHauteurBoisement("G:/FRC_Pays_de_la_Loire/data/mnhc/");
		gbManager.setOutputPath("G:/FRC_Pays_de_la_Loire/data/grain_bocager/");
		gbManager.setTypeBoisement("G:/FRC_Pays_de_la_Loire/data/grain_bocager/TypeBoisement/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptCalculDistanceInfluenceBoisement() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_distance_influence_boisement");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setHauteurBoisement("G:/FRC_Pays_de_la_Loire/data/mnhc/");
		gbManager.setTypeBoisement("G:/FRC_Pays_de_la_Loire/data/grain_bocager/TypeBoisement/");
		gbManager.setDistanceInfluenceBoisement("G:/FRC_Pays_de_la_Loire/data/grain_bocager/DistanceInfluenceBoisement/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptCalculGrainBocager() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setDistanceInfluenceBoisement("G:/FRC_Pays_de_la_Loire/data/grain_bocager/DistanceInfluenceBoisement/");
		gbManager.setGrainCellSize(50.0);
		gbManager.setGrainBocager("G:/FRC_Pays_de_la_Loire/data/grain_bocager/GrainBocager/");
		gbManager.setGrainBocager4Classes("G:/FRC_Pays_de_la_Loire/data/grain_bocager/GrainBocager4Classes/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptClusterisationZoneGrainBocagerFonctionnel() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("clusterisation_fonctionnalite");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setGrainBocager("G:/FRC_Pays_de_la_Loire/data/grain_bocager/GrainBocager/");
		gbManager.setGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/GrainBocagerFonctionnel/");
		gbManager.setClusterGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/ClusterisationGrainBocagerFonctionnel/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptCalculEnjeuxGlobaux() {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true);
		gbManager.setTile(Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/"));
		gbManager.setGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/GrainBocagerFonctionnel/");
		gbManager.setClusterGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/ClusterisationGrainBocagerFonctionnel/");
		gbManager.setEnjeuxWindowRadius(1000);
		gbManager.setEnjeuxCellSize(200);
		gbManager.setProportionGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/ProportionGrainBocagerFonctionnel/");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel("G:/FRC_Pays_de_la_Loire/data/grain_bocager/ZoneFragmentationGrainBocagerFonctionnel/");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		Coverage cov = gbProcedure.run();
		cov.dispose();
		
	}
	
	private static void scriptPrepareAnalyseDensiteBoisement() {
		
		String rasterPresenceBoisement = "G:/FRC_Pays_de_la_Loire/data/densite_boisement/PresenceBoisement/";
		Util.createAccess(rasterPresenceBoisement);
		
		Coverage covBocage = CoverageManager.getCoverage("G:/FRC_Pays_de_la_Loire/data/mnhc/");
		
		Pixel2PixelTileCoverageCalculation pptcc = new Pixel2PixelTileCoverageCalculation(rasterPresenceBoisement, covBocage){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] == -1){
					return -1;
				}
				if(v[0] > 0){
					return 1;
				}
				return 0;
			}
		};
		pptcc.run();
		
		covBocage.dispose();
		
	}
	
	private static void scriptDensiteBoisement() {
		
		String rasterDensiteBoisement = "G:/FRC_Pays_de_la_Loire/data/densite_boisement/DensiteBoisement/";
		Util.createAccess(rasterDensiteBoisement);
		
		Tile tile = Tile.getTile("G:/FRC_Pays_de_la_Loire/data/mnhc/");
		
		String rasterPresenceBoisement = "G:/FRC_Pays_de_la_Loire/data/densite_boisement/PresenceBoisement/";
		Coverage covPresenceBoisement = CoverageManager.getCoverage(rasterPresenceBoisement);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(covPresenceBoisement);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addMetric("pNV_1");
		builder.setWindowSize(401);
		builder.setDisplacement(40);
		builder.addTileGeoTiffOutput("pNV_1", rasterDensiteBoisement, tile);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		covPresenceBoisement.dispose();
	}
	
}
