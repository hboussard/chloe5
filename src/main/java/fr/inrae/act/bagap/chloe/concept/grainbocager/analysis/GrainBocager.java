package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.TabCoverage;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class GrainBocager {

	// run sur la procedure de Grain Bocager
	
	public static Coverage run(String rasterHauteurBoisement){
		return run(rasterHauteurBoisement, 250, 5, true);
	}
	
	public static Coverage run(String rasterHauteurBoisement, double outputCellSize){
		return run(rasterHauteurBoisement, 250, outputCellSize, true);
	}
	
	public static Coverage run(String rasterHauteurBoisement, boolean modeFast){
		return run(rasterHauteurBoisement, 250, 5, modeFast);
	}
	
	public static Coverage run(String rasterHauteurBoisement, double outputCellSize, boolean modeFast){
		return run(rasterHauteurBoisement, 250, outputCellSize, modeFast);
	}
	
	public static Coverage run(String rasterHauteurBoisement, double windowRadius, double outputCellSize, boolean modeFast){
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		covHauteurBoisement.dispose();
		
		return run(dataHauteurBoisement, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage run(Coverage covHauteurBoisement){
		return run(covHauteurBoisement, 250, 5, true);
	}
	
	public static Coverage run(Coverage covHauteurBoisement, double outputCellSize){
		return run(covHauteurBoisement, 250, outputCellSize, true);
	}
	
	public static Coverage run(Coverage covHauteurBoisement, boolean modeFast){
		return run(covHauteurBoisement, 250, 5, modeFast);
	}
	
	public static Coverage run(Coverage covHauteurBoisement, double outputCellSize, boolean modeFast){
		return run(covHauteurBoisement, 250, outputCellSize, modeFast);
	}
	
	public static Coverage run(Coverage covHauteurBoisement, double windowRadius, double outputCellSize, boolean modeFast){
		
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		
		return run(dataHauteurBoisement, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage run(float[] dataHauteurBoisement, EnteteRaster entete){
		return run(dataHauteurBoisement, entete, 250, 5, true);
	}
	
	public static Coverage run(float[] dataHauteurBoisement, EnteteRaster entete, double outputCellSize){
		return run(dataHauteurBoisement, entete, 250, outputCellSize, true);
	}
	
	public static Coverage run(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast){
		return run(dataHauteurBoisement, entete, 250, 5, modeFast);
	}
	
	public static Coverage run(float[] dataHauteurBoisement, EnteteRaster entete, double outputCellSize, boolean modeFast){
		return run(dataHauteurBoisement, entete, 250, outputCellSize, modeFast);
	}
	
	public static Coverage run(float[] dataHauteurBoisement, EnteteRaster entete, double windowRadius, double outputCellSize, boolean modeFast){
		
		// detection des types de boisement
		Coverage covTypeBoisement = detectionTypeBoisement(dataHauteurBoisement, entete, modeFast);
		
		// calcul de distances ponderees
		Coverage covDistanceInfluence = calculDistancesInfluences(dataHauteurBoisement, covTypeBoisement.getData(), entete, modeFast);
		
		// moyenne globale du grain bocager
		return calculGrainBocager(covDistanceInfluence, windowRadius, outputCellSize, modeFast);
	}
	
	// recuperation des hauteurs de boisement
	
	public static Coverage recuperationHauteurBoisement(String bocage, EnteteRaster entete) {
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(bocage);
		EnteteRaster enteteMNHC = covHauteurBoisement.getEntete();
		float[] dataBoisement = covHauteurBoisement.getData(EnteteRaster.getROI(enteteMNHC, new Envelope(entete.minx(), entete.maxx(), entete.miny(), entete.maxy())));
		covHauteurBoisement.dispose();
		
		return new TabCoverage(dataBoisement, entete);
	}
	
	public static Coverage recuperationZoneArrachage(String arrachage, EnteteRaster entete) {
		
		Coverage covZoneArrachage = ShapeFile2CoverageConverter.getSurfaceCoverage(arrachage, entete, 0, entete.noDataValue());
		
		return covZoneArrachage;
	}
	
	public static Coverage recuperationHauteurPlantation(String plantation, String attributHauteurPlantation, EnteteRaster entete) {
		
		Coverage covHauteurReplantation = null;
		
		if (ShapeFile2CoverageConverter.getShapeType(plantation).isPolygonType()) {
			
			covHauteurReplantation = ShapeFile2CoverageConverter.getSurfaceCoverage(plantation, attributHauteurPlantation, entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), 0);
			
		} else if (ShapeFile2CoverageConverter.getShapeType(plantation).isLineType()) {
			
			covHauteurReplantation = ShapeFile2CoverageConverter.getLinearCoverage(plantation, attributHauteurPlantation, entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), 0, entete.cellsize());
			
		} /*else if (ShapeFile2CoverageConverter.getShapeType(plantation).isPointType()) {
			
		} */else {
			throw new IllegalArgumentException(plantation);
		}
		
		return covHauteurReplantation;
	}
	
	// ajoute hauteurs de boisement des plantations
	
	public static Coverage ajouteHauteurPlantation(Coverage bocage, Coverage plantation){
		
		EnteteRaster entete = bocage.getEntete();
		
		float[] dataHauteurBoisement = bocage.getData();
		float[] dataHauteurPlantation = plantation.getData();
		
		return ajouteHauteurPlantation(dataHauteurBoisement, dataHauteurPlantation, entete);
	}
	
	public static Coverage ajouteHauteurPlantation(float[] dataHauteurBoisement, float[] dataHauteurPlantation, EnteteRaster entete){
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataHauteurBoisement, dataHauteurPlantation){
			@Override
			protected float doTreat(float[] v) {
				return Math.max(v[0], v[1]);
			}
		};
		pptc.run();
		
		return new TabCoverage(data, entete);
	}
	
	public static Coverage supprimeZoneArrachage(float[] dataHauteurBoisement, float[] dataZoneArrachage, EnteteRaster entete){
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataHauteurBoisement, dataZoneArrachage){
			@Override
			protected float doTreat(float[] v) {
				float da = v[1];
				if(da != entete.noDataValue()){
					return da;
				}
				return v[0];
			}
		};
		pptc.run();
		
		return new TabCoverage(data, entete);
	}
	
	// detection des types de boisement 
	
	public static Coverage detectionTypeBoisement(String rasterHauteurBoisement, boolean modeFast) {
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		covHauteurBoisement.dispose();
		
		return detectionTypeBoisement(dataHauteurBoisement, entete, modeFast);
	}
	
	public static Coverage detectionTypeBoisement(Coverage covHauteurBoisement, boolean modeFast) {
	
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		
		return detectionTypeBoisement(dataHauteurBoisement, entete, modeFast);
	}
	
	public static Coverage detectionTypeBoisement(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast) {
		
		// detection des boisements phase 1
		Coverage covTypeBoisementPhase1 = detectionTypeBoisementPhase1(dataHauteurBoisement, entete, modeFast);
	
		
		// calcul de distance aux massifs boises
		Coverage covDistanceMassif = calculDistanceMassifsBoisesEuclidian(covTypeBoisementPhase1);
		
		
		// detection des boisements phase 2
		return detectionTypeBoisementPhase2(covTypeBoisementPhase1, covDistanceMassif);	
	}
	
	// detection des types de boisement phase 1
	
	public static Coverage detectionTypeBoisementPhase1(String rasterHauteurBoisement, boolean modeFast) {
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		covHauteurBoisement.dispose();
		
		return detectionTypeBoisementPhase1(dataHauteurBoisement, entete, modeFast);
	}
	
	public static Coverage detectionTypeBoisementPhase1(Coverage covHauteurBoisement, boolean modeFast) {
		
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		
		return detectionTypeBoisementPhase1(dataHauteurBoisement, entete, modeFast);
	}
	
	public static Coverage detectionTypeBoisementPhase1(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast) {
		
		float[] data = new float[entete.width()*entete.height()];
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		
		builder.setRasterTab(dataHauteurBoisement);
		builder.setEntete(entete);
		builder.setWindowSize(21);
		
		builder.addMetric("GBBocage");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
		return new TabCoverage(data, entete);
	}
	
	// calcul des distances euclidiennes aux massifs
	
	public static Coverage calculDistanceMassifsBoisesEuclidian(String rasterTypeBoisementPhase1){
		
		Coverage covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		float[] dataTypeBoisementPhase1 = covTypeBoisementPhase1.getData();
		EnteteRaster entete = covTypeBoisementPhase1.getEntete();
		covTypeBoisementPhase1.dispose();
		
		return calculDistanceMassifsBoisesEuclidian(dataTypeBoisementPhase1, entete);
	}
	
	public static Coverage calculDistanceMassifsBoisesEuclidian(Coverage covTypeBoisementPhase1){
		
		float[] dataTypeBoisementPhase1 = covTypeBoisementPhase1.getData();
		EnteteRaster entete = covTypeBoisementPhase1.getEntete();
		
		return calculDistanceMassifsBoisesEuclidian(dataTypeBoisementPhase1, entete);
	}
	
	public static Coverage calculDistanceMassifsBoisesEuclidian(float[] dataTypeBoisementPhase1, EnteteRaster entete){
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabChamferDistanceAnalysis da = new TabChamferDistanceAnalysis(data, dataTypeBoisementPhase1, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{5}, entete.noDataValue());
		da.allRun();
		
		return new TabCoverage(data, entete);
	}
	
	// detection des types de boisement phase 2
	
	public static Coverage detectionTypeBoisementPhase2(String rasterTypeBoisementPhase1, String rasterDistanceMassif) {	
		
		Coverage covTypeBoisementPhase1 = CoverageManager.getCoverage(rasterTypeBoisementPhase1);
		float[] dataTypeBoisementPhase1 = covTypeBoisementPhase1.getData();
		EnteteRaster entete = covTypeBoisementPhase1.getEntete();
		covTypeBoisementPhase1.dispose();
		
		Coverage covDistanceMassif = CoverageManager.getCoverage(rasterDistanceMassif);
		float[] dataDistanceMassif = covDistanceMassif.getData();
		covDistanceMassif.dispose();
		
		return detectionTypeBoisementPhase2(dataTypeBoisementPhase1, dataDistanceMassif, entete);
	}
	
	public static Coverage detectionTypeBoisementPhase2(Coverage covTypeBoisementPhase1, Coverage covDistanceMassif) {	
		
		float[] dataTypeBoisementPhase1 = covTypeBoisementPhase1.getData();
		EnteteRaster entete = covTypeBoisementPhase1.getEntete();
		
		float[] dataDistanceMassif = covDistanceMassif.getData();
		
		return detectionTypeBoisementPhase2(dataTypeBoisementPhase1, dataDistanceMassif, entete);
	}
	
	public static Coverage detectionTypeBoisementPhase2(float[] dataTypeBoisementPhase1, float[] dataDistanceMassif, EnteteRaster entete) {	
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(data, dataTypeBoisementPhase1, dataDistanceMassif){
			@Override
			protected float doTreat(float[] v) {
				float vdm = v[1];
				float vtb = v[0];
				if(vdm == entete.noDataValue()){
					return vtb;
				}
				if(vtb == entete.noDataValue()){
					return entete.noDataValue();
				}
				if(vdm <= 20){
					if(vtb == 10){
						return 5;
					}
				}
				return vtb;
			}
		};
		pptcc.run();
		
		return new TabCoverage(data, entete);
	}
	
	// calcul des distances d'influence de boisement
	
	public static Coverage calculDistancesInfluences(String rasterHauteurBoisement, String rasterTypeBoisement, boolean modeFast) {
		
		Coverage covHauteurBoisement = CoverageManager.getCoverage(rasterHauteurBoisement);
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		covHauteurBoisement.dispose();
		
		Coverage covTypeBoisement = CoverageManager.getCoverage(rasterTypeBoisement);
		float[] dataTypeBoisement = covTypeBoisement.getData();
		covTypeBoisement.dispose();
		
		return calculDistancesInfluences(dataHauteurBoisement, dataTypeBoisement, entete, modeFast);
	}
	
	public static Coverage calculDistancesInfluences(Coverage covHauteurBoisement, Coverage covTypeBoisement, boolean modeFast) {
		
		float[] dataHauteurBoisement = covHauteurBoisement.getData();
		EnteteRaster entete = covHauteurBoisement.getEntete();
		
		float[] dataTypeBoisement = covTypeBoisement.getData();
		
		return calculDistancesInfluences(dataHauteurBoisement, dataTypeBoisement, entete, modeFast);
	}
	
	public static Coverage calculDistancesInfluences(float[] dataHauteurBoisement, float[] dataTypeBoisement, EnteteRaster entete, boolean modeFast) {
				
		float[] data = new float[entete.width()*entete.height()];
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		}
		
		builder.setRasterTab(dataHauteurBoisement);
		builder.setRasterTab2(dataTypeBoisement);
		builder.setEntete(entete);
		
		builder.addMetric("GBDistance");
		builder.setWindowSize(121);
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return new TabCoverage(data, entete);
	}
	
	// calcul du grain bocager en fonction d'une taille de fen�tre et d'une taille de sortie
	
	public static Coverage calculGrainBocager(String rasterDistanceInfluence, double windowRadius, double outputCellSize, boolean modeFast) {
		
		Coverage covDistanceInfluence = CoverageManager.getCoverage(rasterDistanceInfluence);
		float[] dataDistanceInfluence = covDistanceInfluence.getData();
		EnteteRaster entete = covDistanceInfluence.getEntete();
		covDistanceInfluence.dispose();
		
		return calculGrainBocager(dataDistanceInfluence, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage calculGrainBocager(Coverage covDistanceInfluence, double windowRadius, double outputCellSize, boolean modeFast) {
		
		float[] dataDistanceInfluence = covDistanceInfluence.getData();
		EnteteRaster entete = covDistanceInfluence.getEntete();
		
		return calculGrainBocager(dataDistanceInfluence, entete, windowRadius, outputCellSize, modeFast);
		/*
		EnteteRaster entete = covDistanceInfluence.getEntete();
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		CoverageOutput covOutput = new CoverageOutput("average");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setCoverage(covDistanceInfluence);
		builder.setDisplacement(displacement); 
		builder.addMetric("average");
		builder.setWindowSize(windowSize);
		builder.addCoverageOutput(covOutput);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return covOutput.getCoverage();
		*/
	}
	
	public static Coverage calculGrainBocager(float[] dataDistanceInfluence, EnteteRaster entete, double windowRadius, double outputCellSize, boolean modeFast) {
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		CoverageOutput covOutput = new CoverageOutput("average");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}
		builder.setRasterTab(dataDistanceInfluence);
		builder.setEntete(entete);
		builder.setDisplacement(displacement); 
		builder.addMetric("average");
		if(modeFast){
			builder.setWindowSize((int) (windowSize*1.5));
		}else{
			builder.setWindowSize(windowSize);
		}
		
		builder.addCoverageOutput(covOutput);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return covOutput.getCoverage();
	}
	
	// classification en N classes definit par des seuils
	
	public static Coverage runClassificationNClasses(String rasterGrainBocager, float noDataValue, double... seuils) {	
		
		Coverage covGrainBocager = CoverageManager.getCoverage(rasterGrainBocager);
		float[] dataGrainBocager = covGrainBocager.getData();
		EnteteRaster entete = covGrainBocager.getEntete();
		covGrainBocager.dispose();
		
		return runClassificationNClasses(dataGrainBocager, entete, noDataValue, seuils);
	}

	public static Coverage runClassificationNClasses(Coverage covGrainBocager, float noDataValue, double... seuils) {	
		
		float[] dataGrainBocager = covGrainBocager.getData();
		EnteteRaster entete = covGrainBocager.getEntete();
		
		return runClassificationNClasses(dataGrainBocager, entete, noDataValue, seuils);
	}
	
	public static Coverage runClassificationNClasses(float[] dataGrainBocager, EnteteRaster entete, float noDataValue, double... seuils) {	
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(data, dataGrainBocager){
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
		
		return new TabCoverage(data, entete);
	}
	
	// classification fonctionnelle a partir d'un seuil
	
	public static Coverage runClassificationFonctionnelle(String rasterGrainBocager, double seuil) {	
		
		Coverage covGrainBocager = CoverageManager.getCoverage(rasterGrainBocager);
		float[] dataGrainBocager = covGrainBocager.getData();
		EnteteRaster entete = covGrainBocager.getEntete();
		covGrainBocager.dispose();
		
		return runClassificationFonctionnelle(dataGrainBocager, entete, seuil);
	}
	
	public static Coverage runClassificationFonctionnelle(Coverage covGrainBocager, double seuil) {	
		
		float[] dataGrainBocager = covGrainBocager.getData();
		EnteteRaster entete = covGrainBocager.getEntete();
		
		return runClassificationFonctionnelle(dataGrainBocager, entete, seuil);
	}

	public static Coverage runClassificationFonctionnelle(float[] dataGrainBocager, EnteteRaster entete, double seuil) {	
	
		float[] data = new float[entete.width()*entete.height()];
	
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(data, dataGrainBocager){
			@Override
			protected float doTreat(float[] v) {
				if(v[0] <= seuil){
					return 1;
				}
				return 0;
			}
		};
		pptcc.run();
	
		return new TabCoverage(data, entete);
	}
	
	// clusterisation du grain fonctionnel
	
	public static Coverage runClusterisationGrainFonctionnel(String rasterGrainBocagerFonctionnel){
		
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterGrainBocagerFonctionnel);
		float[] dataGrainBocagerFonctionnel = covGrainBocagerFonctionnel.getData();
		EnteteRaster entete = covGrainBocagerFonctionnel.getEntete();
		covGrainBocagerFonctionnel.dispose();
		
		return runClusterisationGrainFonctionnel(dataGrainBocagerFonctionnel, entete);
	}
	
	public static Coverage runClusterisationGrainFonctionnel(Coverage covGrainBocagerFonctionnel){
		
		float[] dataGrainBocagerFonctionnel = covGrainBocagerFonctionnel.getData();
		EnteteRaster entete = covGrainBocagerFonctionnel.getEntete();
		
		return runClusterisationGrainFonctionnel(dataGrainBocagerFonctionnel, entete);
	}
	
	public static Coverage runClusterisationGrainFonctionnel(float[] dataGrainBocagerFonctionnel, EnteteRaster entete){
		
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(dataGrainBocagerFonctionnel, entete.width(), entete.height(), new int[]{1}, entete.noDataValue());
		float[] tabCluster = (float[]) ca.allRun();
		
		return new TabCoverage(tabCluster, entete);
	}
	
	// SHDI sur les clusters fonctionnels en fonction d'une taille de fenetre
	
	public static Coverage runSHDIClusterGrainBocagerFonctionnel(String rasterClusterGrainFonctionnel, double windowRadius, double outputCellSize, boolean modeFast) {

		Coverage covClusterGrainFonctionnel = CoverageManager.getCoverage(rasterClusterGrainFonctionnel);
		float[] dataClusterGrainFonctionnel = covClusterGrainFonctionnel.getData();
		EnteteRaster entete = covClusterGrainFonctionnel.getEntete();
		covClusterGrainFonctionnel.dispose();
		
		return runSHDIClusterGrainBocagerFonctionnel(dataClusterGrainFonctionnel, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage runSHDIClusterGrainBocagerFonctionnel(Coverage covClusterGrainFonctionnel, double windowRadius, double outputCellSize, boolean modeFast) {

		float[] dataClusterGrainFonctionnel = covClusterGrainFonctionnel.getData();
		EnteteRaster entete = covClusterGrainFonctionnel.getEntete();
		
		return runSHDIClusterGrainBocagerFonctionnel(dataClusterGrainFonctionnel, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage runSHDIClusterGrainBocagerFonctionnel(float[] dataClusterGrainFonctionnel, EnteteRaster entete, double windowRadius, double outputCellSize, boolean modeFast) {

		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		CoverageOutput covOutput = new CoverageOutput("SHDI");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(dataClusterGrainFonctionnel);
		builder.setEntete(entete);
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		builder.addMetric("SHDI");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(displacement);
		builder.addCoverageOutput(covOutput);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return covOutput.getCoverage();
	}
	
	// proportion de grain fonctionnel en fonction d'une taille de fenetre
	
	public static Coverage runProportionGrainBocagerFonctionnel(String rasterGrainBocagerFonctionnel, double windowRadius, double outputCellSize, boolean modeFast) {
		
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(rasterGrainBocagerFonctionnel);
		float[] dataGrainBocagerFonctionnel = covGrainBocagerFonctionnel.getData();
		EnteteRaster entete = covGrainBocagerFonctionnel.getEntete();
		covGrainBocagerFonctionnel.dispose();
		
		return runProportionGrainBocagerFonctionnel(dataGrainBocagerFonctionnel, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage runProportionGrainBocagerFonctionnel(Coverage covGrainBocagerFonctionnel, double windowRadius, double outputCellSize, boolean modeFast) {
		
		float[] dataGrainBocagerFonctionnel = covGrainBocagerFonctionnel.getData();
		EnteteRaster entete = covGrainBocagerFonctionnel.getEntete();

		return runProportionGrainBocagerFonctionnel(dataGrainBocagerFonctionnel, entete, windowRadius, outputCellSize, modeFast);
	}
	
	public static Coverage runProportionGrainBocagerFonctionnel(float[] dataGrainBocagerFonctionnel, EnteteRaster entete, double windowRadius, double outputCellSize, boolean modeFast) {
		
		int windowSize = LandscapeMetricAnalysis.getWindowSize(entete.cellsize(), windowRadius);
		int displacement = LandscapeMetricAnalysis.getDisplacement(entete.cellsize(), outputCellSize);
		
		CoverageOutput covOutput = new CoverageOutput("pNV_1");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(dataGrainBocagerFonctionnel);
		builder.setEntete(entete);
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		builder.addMetric("pNV_1");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(displacement);
		builder.addCoverageOutput(covOutput);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return covOutput.getCoverage();
	}

}
