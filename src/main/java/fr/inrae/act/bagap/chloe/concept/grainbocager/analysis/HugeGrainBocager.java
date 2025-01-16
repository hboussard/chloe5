package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis;

import fr.inrae.act.bagap.apiland.raster.Raster;
import fr.inrae.act.bagap.chloe.cluster.chess.TileQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.HugeChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.Tile;
import fr.inrae.act.bagap.apiland.raster.TileCoverage;
import fr.inrae.act.bagap.apiland.raster.analysis.Pixel2PixelTileCoverageCalculation;
import fr.inrae.act.bagap.apiland.util.Tool;

public class HugeGrainBocager {
	
	// detection des types de boisement 
	
	public static Coverage detectionTypeBoisement(String rasterTypeBoisement, String rasterTypeBoisementPhase1, String rasterDistanceBoisement, String rasterHauteurBoisement, Tile tile, boolean fastMode) {
		
		// recuperation du coverage
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		
		Coverage covTypeBoisement = detectionTypeBoisement(rasterTypeBoisement, rasterTypeBoisementPhase1, rasterDistanceBoisement, covHauteurBoisement, tile, fastMode);
		
		covHauteurBoisement.dispose();
		
		return covTypeBoisement;
	}
	
	public static Coverage detectionTypeBoisement(String rasterTypeBoisement, String rasterTypeBoisementPhase1, String rasterDistanceBoisement, Coverage covHauteurBoisement, Tile tile, boolean fastMode) {
		
		// detection des boisements phase 1
		Coverage covTypeBoisementPhase1 = detectionTypeBoisementPhase1(rasterTypeBoisementPhase1, covHauteurBoisement, tile, fastMode);
		//Coverage covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		
		// calcul de distance aux massifs boises
		//Coverage covTypeBoisementDistanceBoisement = calculDistanceMassifsBoisesEuclidian(rasterDistanceBoisement, covTypeBoisementPhase1, tile);
		Coverage covTypeBoisementDistanceBoisement = calculDistanceMassifsBoisesEuclidian_2(rasterDistanceBoisement, covTypeBoisementPhase1, tile);
		//Coverage covTypeBoisementDistanceBoisement = CoverageManager.getCoverage(rasterDistanceBoisement);
		
		
		covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		
		// detection des boisements phase 2
		//Coverage covTypeBoisement = detectionTypeBoisementPhase2(rasterTypeBoisement, covTypeBoisementPhase1, covTypeBoisementDistanceBoisement);
		Coverage covTypeBoisement = detectionTypeBoisementPhase2_2(rasterTypeBoisement, covTypeBoisementPhase1, covTypeBoisementDistanceBoisement);
		
		covTypeBoisementPhase1.dispose();
		covTypeBoisementDistanceBoisement.dispose();
		
		Tool.deleteFolder(rasterTypeBoisementPhase1);
		Tool.deleteFolder(rasterDistanceBoisement);
		
		return covTypeBoisement;
	}
	
	// detection des types de boisement phase 1
	
	public static Coverage detectionTypeBoisementPhase1(String rasterHauteurBoisement, String rasterTypeBoisementPhase1, Tile tile, boolean fastMode) {
		
		// recuperation du coverage
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		
		Coverage covTypeBoisementPhase1 = detectionTypeBoisementPhase1(rasterTypeBoisementPhase1, covHauteurBoisement, tile, fastMode);
		
		covHauteurBoisement.dispose();
		
		return covTypeBoisementPhase1;
	}
	
	public static Coverage detectionTypeBoisementPhase1(String rasterTypeBoisementPhase1, Coverage covHauteurBoisement, Tile tile, boolean fastMode) {
		
		//System.out.println("detection phase 1");
		
		Util.createAccess(rasterTypeBoisementPhase1);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		builder.setCoverage(covHauteurBoisement);
		builder.setWindowSize(21);
		
		builder.addMetric("GBBocage");
		builder.addTileGeoTiffOutput("GBBocage", rasterTypeBoisementPhase1, tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
		return CoverageManager.getCoverage(rasterTypeBoisementPhase1);
	}
	
	// calcul des distances euclidiennes aux massifs
	
	public static Coverage calculDistanceMassifsBoisesEuclidian(String rasterDistanceBoisement, String rasterTypeBoisementPhase1, Tile tile){
		
		// recuperation du coverage
		Coverage covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		
		Coverage covDistanceMassifsBoisesEuclidian = calculDistanceMassifsBoisesEuclidian(rasterDistanceBoisement, covTypeBoisementPhase1, tile);
	
		covTypeBoisementPhase1.dispose();
		
		return covDistanceMassifsBoisesEuclidian;
	}
	
	public static Coverage calculDistanceMassifsBoisesEuclidian(String rasterDistanceBoisement, Coverage covTypeBoisementPhase1, Tile tile){
		
		Util.createAccess(rasterDistanceBoisement);
	
		HugeChamferDistanceAnalysis da = new HugeChamferDistanceAnalysis(covTypeBoisementPhase1, tile, rasterDistanceBoisement, "massif", new int[]{5}, covTypeBoisementPhase1.getEntete().noDataValue());
		
		da.allRun();
		
		Coverage covDistanceBoisement = CoverageManager.getCoverage(rasterDistanceBoisement);
		
		return covDistanceBoisement;
	}
	
	public static Coverage calculDistanceMassifsBoisesEuclidian_2(String rasterDistanceBoisement, Coverage covTypeBoisementPhase1, Tile tile){
				
		//System.out.println("detection phase 2");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(covTypeBoisementPhase1);
		builder.addMetric("pNV_5");
		builder.setWindowSize(9);
		builder.addTileGeoTiffOutput("pNV_5", rasterDistanceBoisement, tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
				
		analysis.allRun();
		
		Coverage covDistanceBoisement = CoverageManager.getCoverage(rasterDistanceBoisement);
		
		return covDistanceBoisement;
	}
	
	// detection des types de boisement phase 2
	
	public static Coverage detectionTypeBoisementPhase2(String rasterTypeBoisement, String rasterTypeBoisementPhase1, String rasterTypeBoisementDistanceBoisement) {
		
		// recuperation des types de boisement (dossier de tuiles) phase1 
		Coverage covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		
		// recuperation des distances aux boisements (dossier de tuiles)
		Coverage covDistanceBoisement = CoverageManager.getCoverage(rasterTypeBoisementDistanceBoisement);
		
		Coverage covTypeBoisement = detectionTypeBoisementPhase2(rasterTypeBoisement, covTypeBoisementPhase1, covDistanceBoisement);
		
		covTypeBoisementPhase1.dispose();
		covDistanceBoisement.dispose();
		
		return covTypeBoisement;
	}
	
	public static Coverage detectionTypeBoisementPhase2(String rasterTypeBoisement, Coverage covTypeBoisementPhase1, Coverage covDistanceBoisement) {
		
		Util.createAccess(rasterTypeBoisement);
		
		Pixel2PixelTileCoverageCalculation pptcc = new Pixel2PixelTileCoverageCalculation(rasterTypeBoisement, covTypeBoisementPhase1, covDistanceBoisement){
			@Override
			protected float doTreat(float[] v) {
				float vtb = v[0];
				if(vtb == Raster.getNoDataValue()){
					return Raster.getNoDataValue();
				}
				if(v[1] <= 20){
					if(v[0] == 10){
						return 5;
					}
				}
				return vtb;
			}
		};
		pptcc.run();
		
		Coverage covTypeBoisement = CoverageManager.getCoverage(rasterTypeBoisement);
		
		return covTypeBoisement;
	}
	
	public static Coverage detectionTypeBoisementPhase2_2(String rasterTypeBoisement, Coverage covTypeBoisementPhase1, Coverage covDistanceBoisement) {
		
		System.out.println("detection phase 3");
		
		Util.createAccess(rasterTypeBoisement);
		
		Pixel2PixelTileCoverageCalculation pptcc = new Pixel2PixelTileCoverageCalculation(rasterTypeBoisement, covTypeBoisementPhase1, covDistanceBoisement){
			@Override
			protected float doTreat(float[] v) {
				float vtb = v[0];
				if(vtb == Raster.getNoDataValue()){
					return Raster.getNoDataValue();
				}
				if(v[1] > 0 && v[0] == 10){
					return 5;
				}
				return vtb;
			}
		};
		pptcc.run();
		
		Coverage covTypeBoisement = CoverageManager.getCoverage(rasterTypeBoisement);
		
		return covTypeBoisement;
	}
	
	// calcul des distances d'influence de boisement

	public static Coverage calculDistancesInfluences(String rasterDistanceInfluenceBoisement, Coverage covHauteurBoisement, Coverage covTypeBoisement, Tile tile, boolean fastMode) {

		Util.createAccess(rasterDistanceInfluenceBoisement);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		}
		builder.setCoverage(covHauteurBoisement);
		builder.setCoverage2(covTypeBoisement);
		builder.addMetric("GBDistance");
		builder.setWindowSize(121);
		builder.addTileGeoTiffOutput("GBDistance", rasterDistanceInfluenceBoisement, tile);
		
		LandscapeMetricAnalysis analysis = builder.build();

		analysis.allRun();
		
		return CoverageManager.getCoverage(rasterDistanceInfluenceBoisement);
		
	}
	
	// calcul du grain bocager en fonction d'une taille de fen�tre et d'une taille de sortie
	
	public static Coverage calculGrainBocager(String rasterGrainBocager, Coverage covDistanceInfluence, EnteteRaster entete, double windowRadius, double outputCellSize, Tile tile, boolean fastMode) {
		
		Util.createAccess(rasterGrainBocager);
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}
		builder.setCoverage(covDistanceInfluence);
		builder.setDisplacement(displacement); 
		builder.addMetric("average");
		if(fastMode){
			builder.setWindowSize((int) (windowSize*1.5));
		}else{
			builder.setWindowSize(windowSize);
		}
		builder.addTileGeoTiffOutput("average", rasterGrainBocager, tile);
		//builder.addGeoTiffOutput("average", rasterGrainBocager);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return CoverageManager.getCoverage(rasterGrainBocager);
		
	}
	
	// classification en N classes definit par des seuils
	
	public static Coverage runClassificationNClasses(String rasterGrainBocager4Classes, Coverage covGrainBocager, float noDataValue, double... seuils) {	
		
		Util.createAccess(rasterGrainBocager4Classes);
		
		Pixel2PixelTileCoverageCalculation pptcc = new Pixel2PixelTileCoverageCalculation(rasterGrainBocager4Classes, covGrainBocager){
			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value == noDataValue){
					return noDataValue;
				}
				int classe = 1;
				for(double s : seuils){
					if(value <= s){
						return classe;
					}
					classe++;
				}
				return classe;
			}
		};
		pptcc.run();
		
		Coverage covGrainBocager4Classes = CoverageManager.getCoverage(rasterGrainBocager4Classes);
		
		return covGrainBocager4Classes;
	}
	
	// classification fonctionnelle a partir d'un seuil
	
	public static Coverage runClassificationFonctionnelle(String rasterGrainBocagerFonctionnel, Coverage covGrainBocager, double seuil) {	
		
		Util.createAccess(rasterGrainBocagerFonctionnel);
		
		Pixel2PixelTileCoverageCalculation pptcc = new Pixel2PixelTileCoverageCalculation(rasterGrainBocagerFonctionnel, covGrainBocager){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] <= seuil){
					return 1;
				}
				return 0;
			}
		};
		pptcc.run();
	
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterGrainBocagerFonctionnel);
		
		return covGrainBocagerFonctionnel;
	}
	
	// clusterisation du grain fonctionnel
	
	public static Coverage runClusterisationGrainFonctionnel(String rasterClusterisationGrainBocagerFonctionnel, Coverage covGrainBocagerFonctionnel, int noDataValue){
		
		TileQueenClusteringAnalysis clustering = new TileQueenClusteringAnalysis(rasterClusterisationGrainBocagerFonctionnel, (TileCoverage) covGrainBocagerFonctionnel, new int[]{1}, noDataValue);
		clustering.allRun();
		
		Coverage covClusterisationGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterClusterisationGrainBocagerFonctionnel);
		
		return covClusterisationGrainBocagerFonctionnel;
	}
	
	// SHDI sur les clusters fonctionnels en fonction d'une taille de fenetre
	
	public static Coverage runSHDIClusterGrainBocagerFonctionnel(String rasterZoneFragmentationGrainBocagerFonctionnel, Coverage covClusterGrainFonctionnel, EnteteRaster entete, double windowRadius, double outputCellSize, Tile tile, boolean fastMode) {

		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		//System.out.println(entete.width()+" "+entete.height()+" "+windowSize+" "+displacement);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(covClusterGrainFonctionnel);
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		builder.addMetric("SHDI");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(displacement);
		builder.addTileGeoTiffOutput("SHDI", rasterZoneFragmentationGrainBocagerFonctionnel, tile);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		Coverage covZoneFragmentationGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterZoneFragmentationGrainBocagerFonctionnel);
		
		return covZoneFragmentationGrainBocagerFonctionnel;
	}
	
	// proportion de grain fonctionnel en fonction d'une taille de fenetre
	
	public static Coverage runProportionGrainBocagerFonctionnel(String rasterProportionGrainBocagerFonctionnel, Coverage covGrainBocagerFonctionnel, EnteteRaster entete, double windowRadius, double outputCellSize, Tile tile, boolean fastMode) {
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		System.out.println(entete.width()+" "+entete.height()+" "+windowSize+" "+displacement);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(covGrainBocagerFonctionnel);
		if(fastMode){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		builder.addMetric("pNV_1");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(displacement);
		builder.addTileGeoTiffOutput("pNV_1", rasterProportionGrainBocagerFonctionnel, tile);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		Coverage covProportionGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterProportionGrainBocagerFonctionnel);
		
		return covProportionGrainBocagerFonctionnel;
	}

	/*
	public static void scriptGrainBocager(String rasterHauteurBoisement, String folderGrainBocager, boolean fastMode){
		
		long begin = System.currentTimeMillis();
		
		Util.createAccess(folderGrainBocager); // creation du dossier de sortie
		
		Tile tile = Tile.getTile(rasterHauteurBoisement); // recuperation de la tuile gobale
		
		// detection des types de boisement
		String rasterTypeBoisementPhase1 = folderGrainBocager+"type_boisement_phase1/";
		String rasterTypeBoisement = folderGrainBocager+"type_boisement/";
		String rasterDistanceMassifs = folderGrainBocager+"distance_massif/";
		detectionTypeBoisement(rasterTypeBoisement, rasterTypeBoisementPhase1, rasterDistanceMassifs, rasterHauteurBoisement, tile, fastMode);
		
		// calcul de distances ponderees
		String rasterDistancePonderee = folderGrainBocager+"distance_boisement/";
		calculDistancesPonderees(rasterDistancePonderee, rasterHauteurBoisement, rasterTypeBoisement);
		
		// moyenne globale du grain bocager
		String rasterGrainBocager = folderGrainBocager+"grain_bocager_50m/";
		calculMoyenneGlobale(rasterGrainBocager, rasterDistancePonderee, tile);
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	public static void calculMoyenneGlobale(String rasterGrainBocager, String rasterDistancePonderee, Tile tile){
				
		// recuperation du coverage des hauteurs
		Coverage distancePonderee = CoverageManager.getCoverage(rasterDistancePonderee);
				
		System.out.println("calcul du grain bocager");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(distancePonderee);
		
		builder.setDisplacement(10); // 50m de resolution
		
		builder.addMetric("average");
		builder.setWindowSize(101);
		//builder.addGeoTiffOutput("average", rasterGrainBocager); 
		builder.addTileGeoTiffOutput("average", rasterGrainBocager, tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
		
		analysis.allRun();
				
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));	
	}

	public static void calculDistancesPonderees(String rasterDistancePonderee, String rasterHauteurBoisement, String rasterTypeBoisement){
					
		// recuperation du coverage des hauteurs
		Coverage coverageHauteur = CoverageManager.getCoverage(rasterHauteurBoisement);
		
		// recuperation du coverage des types de boisement hauteurs
		Coverage coverageTypeBoisement = CoverageManager.getCoverage(rasterTypeBoisement);
		
		// recuperation du tuilage
		Tile tile = Tile.getTile((TileCoverage) coverageHauteur);
				
		System.out.println("calcul des distances ponderees");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		
		builder.setCoverage(coverageHauteur);
		builder.setCoverage2(coverageTypeBoisement);
		
		builder.addMetric("GBDistance");
		builder.setWindowSize(121);
		builder.addTileGeoTiffOutput("GBDistance", rasterDistancePonderee, tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
		
		analysis.allRun();
					
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}*/
}
