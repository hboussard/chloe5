package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.OverlayPixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.cluster.TabClusteringOutput;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.cluster.distance.TabDistanceClusteringAnalysis;
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
		
		//calculClusterFonctionnel();
		
		//prepaFenetresFonctionnelles();
		//calculFenetresFonctionnelles();
		//calculContinuity();
		//testIndiceContinuite();
		//calculContinuityBoise();
		//calculContinuityHumide();
		//testIndiceContinuite2();
		//classifFonctionnelBoise();
		//clusterFonctionnelBoise();
		fragmentationFonctionnelBoise();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void fragmentationFonctionnelBoise(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("H:/rennes_metropole/test/cluster_volume_deplacement_boise_9_2.tif");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(201);
		builder.setDisplacement(10);
		builder.addMetric("SHDI");
		builder.addGeoTiffOutput("SHDI", "H:/rennes_metropole/test/fragmentation_volume_deplacement_boise_9_2.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
	}
	
	private static void clusterFonctionnelBoise(){
		
		Coverage cov = CoverageManager.getCoverage("H:/rennes_metropole/test/classif_volume_deplacement_boise_9_2.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();

		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), new int[]{1}, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		CoverageManager.writeGeotiff("H:/rennes_metropole/test/cluster_volume_deplacement_boise_9_2.tif", dataCluster, entete);
		
	}
	
	private static void classifFonctionnelBoise(){
		
		
		Coverage covVolume = CoverageManager.getCoverage("H:/rennes_metropole/test/volume_deplacement_boise_9.tif");
		float[] dataVolume = covVolume.getData();
		EnteteRaster entete = covVolume.getEntete();
		covVolume.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataVolume){

			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value > 20000000){
					return 1;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/test/classif_volume_deplacement_boise_9_2.tif", outData, entete);
		
	}
	
	private static void testIndiceContinuite(){
		
		Coverage covSurface = CoverageManager.getCoverage("H:/rennes_metropole/test/surface_3.tif");
		float[] dataSurface = covSurface.getData();
		EnteteRaster entete = covSurface.getEntete();
		covSurface.dispose();
		
		Coverage covVolume = CoverageManager.getCoverage("H:/rennes_metropole/test/volume_3.tif");
		float[] dataVolume = covVolume.getData();
		covVolume.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, dataSurface, dataVolume){

			@Override
			protected float doTreat(float[] v) {
				//return v[1] / v[0];
				return v[0] / v[1];
			}
		};
		cal.run();
		
		CoverageManager.write("H:/rennes_metropole/test/rapport_inverse_3.tif", outData, entete);
		
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
		builder.addMetric("SHDI"); // bug à corriger
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
		
		String path = "G:/AUDIAR/Données2Hugues/Données2Hugues/ZonesHumides/";
		Coverage cov2;
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, path+"izh_sage_vilaine/izh_sage_vilaine.shp", "code", entete);
		cov2.dispose();
		
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, path+"zone_humide_sage_couesnon/zone_humide_sage_couesnon.shp", "code", entete);
		cov2.dispose();
		
		cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, path+"zonehumidesagerfbb/zonehumidesagerfbb.shp", "code", entete);
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
		
		// OSbre --> total = 6 couches à compiler
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
		
		sarMap.put(1f, 3f); // 1 -  --> 3 - voie fréquentée
		sarMap.put(2f, 3f); // 2 -  --> 3 - voie fréquentée
		sarMap.put(3f, 3f); // 3 -  --> 3 - voie fréquentée
		sarMap.put(4f, 4f); // 4 - --> 4 - voie peu fréquentée
		sarMap.put(5f, 4f); // 5 -  --> 4 - voie peu fréquentée 
		sarMap.put(6f, 0f); // 6 -  --> ignoré
		
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
		
		sarMap.put(1f, 10f); // 1 - arbre isolé --> 10 - haie
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
		sarMap.put(3f, 3f); // 3 - voie fréquentée --> 3 - voie fréquentée
		sarMap.put(4f, 4f); // 4 - voie peu fréquentée --> 4 - voie peu fréquentée
		sarMap.put(5f, 5f); // 5 - colza --> 5 - culture
		sarMap.put(6f, 5f); // 6 - céréales à paille --> 5 - culture
		sarMap.put(7f, 5f); // 7 - protéagineux --> 5 - culture
		sarMap.put(9f, 5f); // 9 - tournesol --> 5 - culture
		sarMap.put(10f, 5f); // 10 - maïs --> 5 - culture
		sarMap.put(12f, 5f); // 12 - tubercules --> 5 - culture
		sarMap.put(13f, 6f); // 13 - prairie --> 6 - prairie
		sarMap.put(14f, 7f); // 14 - verger --> 7 - verger
		sarMap.put(15f, 8f); // 15 - vigne --> 8 - vignes
		sarMap.put(16f, 9f); // 16 - forêt --> 9 boisement
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
		
		float[] data = cov.getData(EnteteRaster.getROI(entete, new Envelope(enteteRef.minx(), enteteRef.maxx(), enteteRef.miny(), enteteRef.maxy())));
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
				Coverage cov2 = ShapeFile2CoverageConverter.getSurfaceCoverage(data, path+"CoSIA_D035_2020/"+file, "numero", entete);
				data = cov2.getData();
				cov2.dispose();
			}
		}
		
		CoverageManager.write(path+"cosia_2020.tif", data, entete);
		
		
	}
	
}
