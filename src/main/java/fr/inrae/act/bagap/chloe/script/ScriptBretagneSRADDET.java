package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.Tile;
import fr.inrae.act.bagap.apiland.raster.TileCoverage;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.HugeRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.cluster.chess.TileQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TileDistanceClusteringAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.HugeChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptBretagneSRADDET {
	
	private static String bretagnePath = "E:/data/caphaie/occsol/raster/bretagne/";
	private static String path = "C:/Data/temp/sraddet/data1/";

	public static void main(String[] args) {
		
		// grassland
		
		//generatePermeabilityGrassland();
		//calculContinuity(250, path+"permeability_grassland/", path+"continuity_grassland/contintuity_grassland");
		//calculIndiceDeplacement(250, path+"continuity_grassland/contintuity_grassland", path+"continuity_grassland/indice_contintuity_grassland");
		//classificationHabitatGrassland(250, 0.5f);
		//calculDistanceAccessibiliteFromHabitatGrassland(5000);
		//clusteringHabitatGrassland(1000);
		//reduceClusteringGrassland(1000);
		//analyseFragmentationClusteringGrassland(1000, 5000);
		//calculMoyenneContinuityGrassland(250, 5000);
		
		
		// woody
		//generatePermeabilityWoody();
		//calculContinuity(50, path+"permeability_woody/", path+"continuity_woody/contintuity_woody");
		//calculIndiceDeplacement(50, path+"continuity_woody/contintuity_woody", path+"continuity_woody/indice_contintuity_woody");
		//classificationHabitatWoody(250, 0.5f);
		//calculDistanceAccessibiliteFromHabitatWoody(5000);
		//clusteringHabitatWoody(250);
		//reduceClusteringWoody(250);
		//analyseFragmentationClusteringWoody(250, 5000);
		//calculMoyenneContinuityWoody(250, 5000);
	}
	
	private static void calculMoyenneContinuityGrassland(int dMax, int scale) {
		
		int ws = ((scale*2)/50)+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"continuity_grassland/indice_contintuity_grassland_"+dMax+"m.tif");
		builder.setWindowSize(ws);
		builder.setDisplacement(4);
		builder.addMetric("average");
		builder.addGeoTiffOutput("average", path+"continuity_grassland/moyenne_continuity_grassland_"+dMax+"m_"+scale+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void calculMoyenneContinuityWoody(int dMax, int scale) {
		
		int ws = ((scale*2)/50)+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"continuity_woody/indice_contintuity_woody_"+dMax+"m.tif");
		builder.setWindowSize(ws);
		builder.setDisplacement(4);
		builder.addMetric("average");
		builder.addGeoTiffOutput("average", path+"continuity_woody/moyenne_continuity_woody_"+dMax+"m_"+scale+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void analyseFragmentationClusteringGrassland(int distanceMax, int scale) {
		
		int ws = ((scale*2)/50)+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"continuity_grassland/cluster_habitat_grassland_"+distanceMax+"m_50m.tif");
		builder.setWindowSize(ws);
		builder.setDisplacement(4);
		//builder.addMetric("pN-values");
		//builder.addMetric("N-valid");
		//builder.addMetric("N-values");
		builder.addMetric("SHDI-frag");
		//builder.addGeoTiffOutput("pN-values", path+"continuity_grassland/proportion_habitat_grassland_"+distanceMax+"m_"+scale+"m.tif");
		//builder.addGeoTiffOutput("N-valid", path+"continuity_grassland/valids_habitat_grassland_"+distanceMax+"m_"+scale+"m.tif");
		//builder.addGeoTiffOutput("N-values", path+"continuity_grassland/values_habitat_grassland_"+distanceMax+"m_"+scale+"m.tif");
		builder.addGeoTiffOutput("SHDI-frag", path+"continuity_grassland/fragmentation_habitat_grassland_"+distanceMax+"m_"+scale+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void analyseFragmentationClusteringWoody(int distanceMax, int scale) {
		
		int ws = ((scale*2)/50)+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"continuity_woody/cluster_habitat_woody_"+distanceMax+"m_50m.tif");
		builder.setWindowSize(ws);
		builder.setDisplacement(4);
		builder.addMetric("SHDI-frag");
		builder.addGeoTiffOutput("SHDI-frag", path+"continuity_woody/fragmentation_habitat_woody_"+distanceMax+"m_"+scale+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void reduceClusteringGrassland(int distanceMax) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		builder.setRasterFile(path+"cluster_distance_habitat_grassland/");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addMetric("Central");
		builder.addGeoTiffOutput("Central", path+"continuity_grassland/cluster_habitat_grassland_"+distanceMax+"m_50m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void reduceClusteringWoody(int distanceMax) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		builder.setRasterFile(path+"cluster_distance_habitat_woody/");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addMetric("Central");
		builder.addGeoTiffOutput("Central", path+"continuity_woody/cluster_habitat_woody_"+distanceMax+"m_50m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}

	private static void clusteringHabitatGrassland(int distanceMax) {
		
		Coverage covHabitat = CoverageManager.getCoverage(path+"habitat_grassland/");
		EnteteRaster entete = covHabitat.getEntete();
		
		Coverage covDistanceHabitat = CoverageManager.getCoverage(path+"distance_habitat_grassland/");
		
		//Util.createAccess(path+"cluster_distance_eucl_habitat_grassland/");
		
		int[] codes = new int[] {1};
		
		//TileQueenClusteringAnalysis analysis = new TileQueenClusteringAnalysis(path+"test2/cluster_habitat_grassland/", (TileCoverage) covHabitat, codes, entete.noDataValue());
		TileDistanceClusteringAnalysis analysis = new TileDistanceClusteringAnalysis(path+"cluster_distance_habitat_grassland/", (TileCoverage) covHabitat, (TileCoverage) covDistanceHabitat, codes, distanceMax, entete.noDataValue());
		
		analysis.allRun();
		
		covHabitat.dispose();
		covDistanceHabitat.dispose();
	}
	
	private static void clusteringHabitatWoody(int distanceMax) {
		
		Coverage covHabitat = CoverageManager.getCoverage(path+"habitat_woody/");
		EnteteRaster entete = covHabitat.getEntete();
		
		Coverage covDistanceHabitat = CoverageManager.getCoverage(path+"distance_habitat_woody/");
		
		//Util.createAccess(path+"cluster_distance_eucl_habitat_woody/");
		
		int[] codes = new int[] {1};
		
		//TileQueenClusteringAnalysis analysis = new TileQueenClusteringAnalysis(path+"test2/cluster_habitat_woody/", (TileCoverage) covHabitat, codes, entete.noDataValue());
		TileDistanceClusteringAnalysis analysis = new TileDistanceClusteringAnalysis(path+"cluster_distance_habitat_woody/", (TileCoverage) covHabitat, (TileCoverage) covDistanceHabitat, codes, distanceMax, entete.noDataValue());
		
		analysis.allRun();
		
		covHabitat.dispose();
		covDistanceHabitat.dispose();
	}
	
	private static void calculDistanceAccessibiliteFromHabitatGrassland(int distanceMax) {
		
		Coverage covHabitat = CoverageManager.getCoverage(path+"habitat_grassland/");
		EnteteRaster entete = covHabitat.getEntete();
		
		Tile tile = Tile.getTile(path+"habitat_grassland/");
		
		Coverage covPerm = CoverageManager.getCoverage(path+"permeability_grassland/");	
		
		int[] codes = new int[] {1};
		
		//HugeChamferDistanceAnalysis distance = new HugeChamferDistanceAnalysis(covHabitat, tile, path+"test2/distance_habitat_grassland_eucl/", "distance_habitat_grassland_eucl", codes, distanceMax) ;
		HugeRCMDistanceAnalysis distance = new HugeRCMDistanceAnalysis(covHabitat, covPerm, tile, path+"distance_habitat_grassland/", "distance_habitat_grassland", codes, distanceMax) ;
		distance.allRun();
		
		covHabitat.dispose();
		covPerm.dispose();	
	}
	
	private static void calculDistanceAccessibiliteFromHabitatWoody(int distanceMax) {
		
		Coverage covHabitat = CoverageManager.getCoverage(path+"habitat_woody/");
		EnteteRaster entete = covHabitat.getEntete();
		
		Tile tile = Tile.getTile(path+"habitat_woody/");
		
		Coverage covPerm = CoverageManager.getCoverage(path+"permeability_woody/");	
		
		int[] codes = new int[] {1};
		
		//HugeChamferDistanceAnalysis distance = new HugeChamferDistanceAnalysis(covHabitat, tile, path+"test2/distance_habitat_woody_eucl/", "distance_habitat_woody_eucl", codes, distanceMax) ;
		HugeRCMDistanceAnalysis distance = new HugeRCMDistanceAnalysis(covHabitat, covPerm, tile, path+"distance_habitat_woody/", "distance_habitat_woody", codes, distanceMax) ;
		distance.allRun();
		
		covHabitat.dispose();
		covPerm.dispose();	
	}

	private static void classificationHabitatGrassland(int dMax, float thresholdHabtitat) {
		
		Coverage covCont = CoverageManager.getCoverage(path+"continuity_grassland/indice_contintuity_grassland_"+dMax+"m.tif");
		float[] dataCont = covCont.getData();
		EnteteRaster enteteCont = covCont.getEntete();
		covCont.dispose();
		
		Util.createAccess(path+"habitat_grassland/");
		
		File folder = new File(bretagnePath);
		
		for(String f : folder.list()) {
			
			if(f.endsWith(".tif")) {
				
				String[] fs = f.replace(".tif", "").split("_");
				String minx = fs[1];
				String maxy = fs[2];
				
				Coverage covOccsol = CoverageManager.getCoverage(bretagnePath+f);
				float[] dataOccsol = covOccsol.getData();
				EnteteRaster entete = covOccsol.getEntete();
				covOccsol.dispose();
				
				float[] dataClassif = new float[entete.width()*entete.height()];
				Arrays.fill(dataClassif, -1);
				for(int j=0; j<entete.height(); j++) {
					for(int i=0; i<entete.width(); i++) {
						
						if(dataOccsol[j*entete.width()+i] != entete.noDataValue()) {
							
							if(dataOccsol[j*entete.width()+i] == 18) { // prairie permanente
								
								if(dataCont[CoordinateManager.getLocalY(enteteCont, CoordinateManager.getProjectedY(entete, j))*enteteCont.width()+CoordinateManager.getLocalX(enteteCont, CoordinateManager.getProjectedX(entete, i))] >= thresholdHabtitat) {
									
									dataClassif[j*entete.width()+i] = 1;
								}else {
									
									dataClassif[j*entete.width()+i] = 0;
								}
								
							}else {
								
								dataClassif[j*entete.width()+i] = 0;
							}
						}
					}	
				}
				
				CoverageManager.write(path+"habitat_grassland/habitat_grassland_"+minx+"_"+maxy+".tif", dataClassif, entete);
			}
		}
 	}
	
	private static void classificationHabitatWoody(int dMax, float thresholdHabtitat) {
		
		Coverage covCont = CoverageManager.getCoverage(path+"continuity_woody/indice_contintuity_woody_"+dMax+"m.tif");
		float[] dataCont = covCont.getData();
		EnteteRaster enteteCont = covCont.getEntete();
		covCont.dispose();
		
		Util.createAccess(path+"habitat_woody/");
		
		File folder = new File(bretagnePath);
		
		for(String f : folder.list()) {
			
			if(f.endsWith(".tif")) {
				
				String[] fs = f.replace(".tif", "").split("_");
				String minx = fs[1];
				String maxy = fs[2];
				
				Coverage covOccsol = CoverageManager.getCoverage(bretagnePath+f);
				float[] dataOccsol = covOccsol.getData();
				EnteteRaster entete = covOccsol.getEntete();
				covOccsol.dispose();
				
				float[] dataClassif = new float[entete.width()*entete.height()];
				Arrays.fill(dataClassif, -1);
				for(int j=0; j<entete.height(); j++) {
					for(int i=0; i<entete.width(); i++) {
						
						if(dataOccsol[j*entete.width()+i] != entete.noDataValue()) {
							
							if(dataOccsol[j*entete.width()+i] == 13) { // massif boise
								
								if(dataCont[CoordinateManager.getLocalY(enteteCont, CoordinateManager.getProjectedY(entete, j))*enteteCont.width()+CoordinateManager.getLocalX(enteteCont, CoordinateManager.getProjectedX(entete, i))] >= thresholdHabtitat) {
									
									dataClassif[j*entete.width()+i] = 1;
								}else {
									
									dataClassif[j*entete.width()+i] = 0;
								}
								
							}else {
								
								dataClassif[j*entete.width()+i] = 0;
							}
						}
					}	
				}
				
				CoverageManager.write(path+"habitat_woody/habitat_woody_"+minx+"_"+maxy+".tif", dataClassif, entete);
			}
		}
 	}
	
	private static void generatePermeabilityWoody() {
		
		File folder = new File(bretagnePath);
		
		Util.createAccess(path+"permeability_woody/");
		
		Map<Float, Float> mapPerm = Util.importData(path+"doc/map_permeabilite_woody.csv", "cover", "friction");
		
		for(String f : folder.list()) {
		
			if(f.endsWith(".tif")) {
				
				System.out.println(f);
				
				String[] fs = f.replace(".tif", "").split("_");
				String minx = fs[1];
				String maxy = fs[2];
				
				Coverage covOccsol = CoverageManager.getCoverage(bretagnePath+f);
				float[] dataOccsol = covOccsol.getData();
				EnteteRaster entete = covOccsol.getEntete();
				covOccsol.dispose();
				
				float[] dataPerm = new float[entete.width()*entete.height()];
				SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataPerm, dataOccsol, mapPerm);
				cal.run();
				
				CoverageManager.write(path+"permeability_woody/perm_wood_"+minx+"_"+maxy+".tif", dataPerm, entete);
			}
		}
	}
	
	private static void generatePermeabilityGrassland() {
		
		translatePermeabilityGrassland();
		
		calculLisiereBoisement();
		
		compilePearmebilityGrassland();
		
	}
	
	private static void compilePearmebilityGrassland() {
		
		File folder = new File(path+"permeability_grassland/");
		
		for(String f : folder.list()) {
			if(f.endsWith(".tif")) {
				System.out.println(f);
				
				String[] fs = f.replace(".tif", "").split("_");
				String minx = fs[2];
				String maxy = fs[3];
				
				Coverage covPerm = CoverageManager.getCoverage(folder+"/"+f);
				EnteteRaster entete = covPerm.getEntete();
				float[] dataPerm = covPerm.getData();
				covPerm.dispose();
				
				Coverage covLisere = CoverageManager.getCoverage(path+"lisiere_boisement/pNVm_10&12&13_"+minx+"_"+maxy+".tif");
				float[] dataLisiere = covLisere.getData();
				covLisere.dispose();
				
				float[] data = new float[entete.width()*entete.height()];
				
				Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataPerm, dataLisiere) {

					@Override
					protected float doTreat(float[] v) {
						
						float vPerm = v[0];
						float vLisiere = v[1];
						
						if(vPerm != entete.noDataValue()) {
							
							if(vPerm < 0.5 ) {
								
								return vPerm;
								
							}else if(vLisiere > 0) {
								
								return 0.5f;
								
							}else {
								
								return vPerm;
							}
						}
						
						return entete.noDataValue();
					}
				};
				pptc.run();
				
				CoverageManager.write(folder+"/"+f, data, entete);
			}
		}
	}

	private static void calculLisiereBoisement() {
		
		Tile tile = Tile.getTile(bretagnePath);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		builder.setRasterFile(bretagnePath);
		builder.setWindowSize(3);
		builder.setDisplacement(1);
		builder.addMetric("pNVm_10&12&13");
		builder.setUnfilters(new int[] {-1, 10, 12, 13, 29, 34});
		builder.addTileGeoTiffOutput("pNVm_10&12&13", path+"lisiere_boisement/", tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}

	private static void translatePermeabilityGrassland() {
		
		File folder = new File(bretagnePath);
		
		Util.createAccess(path+"permeability_grassland/");
		
		Map<Float, Float> mapPerm = Util.importData(path+"doc/map_permeabilite_grassland.csv", "cover", "friction");
		
		for(String f : folder.list()) {
			
			if(f.endsWith(".tif")) {
				
				String[] fs = f.replace(".tif", "").split("_");
				String minx = fs[1];
				String maxy = fs[2];
				
				Coverage covOccsol = CoverageManager.getCoverage(bretagnePath+f);
				float[] dataOccsol = covOccsol.getData();
				EnteteRaster entete = covOccsol.getEntete();
				covOccsol.dispose();
				
				float[] dataPerm = new float[entete.width()*entete.height()];
				SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataPerm, dataOccsol, mapPerm);
				cal.run();
				
				CoverageManager.write(path+"permeability_grassland/perm_grass_"+minx+"_"+maxy+".tif", dataPerm, entete);
			}
		}
	}

	private static void translateOccsolForPermeabilityGrassland() {
		
		Coverage covOccsol = CoverageManager.getCoverage(path+"occsol/occsol_D022_240_6820.tif");
		float[] dataOccsol = covOccsol.getData();
		EnteteRaster entete = covOccsol.getEntete();
		covOccsol.dispose();
		
		float[] dataPerm = new float[entete.width()*entete.height()];
		Map<Float, Float> mapPerm = Util.importData(path+"doc/map_permeabilite_grassland.csv", "cover", "friction");

		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(dataPerm, dataOccsol, mapPerm);
		cal.run();
		
		Util.createAccess(path+"permeability_grassland/");
		CoverageManager.write(path+"permeability_grassland/perm_grass_D022_240_6820.tif", dataPerm, entete);
		
	}
	
	private static void calculContinuity(int dMax, String permeabilityFile, String continuityFileBase){
		
		int ws = (((dMax*2)/5)*4)+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setRasterFile(bretagnePath);
		builder.setRasterFile2(permeabilityFile);
		
		builder.setWindowSize(ws);
		builder.setDisplacement(10);
		builder.addMetric("volume");
		//builder.addMetric("area_18");
		builder.setDMax(dMax);
		builder.addGeoTiffOutput("volume", continuityFileBase+"_"+dMax+"m.tif");
		//builder.addGeoTiffOutput("area_18", continuityFileBase+"_area_PP_"+dMax+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}

	private static void calculIndiceDeplacement(int dMax, String continuityFileBase, String indiceContinuityFileBase) {
		Coverage cov = CoverageManager.getCoverage(continuityFileBase+"_"+dMax+"m.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		System.out.println((((Math.pow(dMax, 2)*Math.PI))*dMax)/3);
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				
				if(v[0] != entete.noDataValue()) {
					return (float) (v[0] / (((Math.pow(dMax, 2)*Math.PI))*dMax)/3);	
				}
				
				return entete.noDataValue();
			}
		};
		cal.run();
		
		CoverageManager.write(indiceContinuityFileBase+"_"+dMax+"m.tif", outData, entete);
		
	}
	
}
