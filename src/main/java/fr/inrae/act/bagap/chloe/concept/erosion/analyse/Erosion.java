package fr.inrae.act.bagap.chloe.concept.erosion.analyse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class Erosion {

	public static void bvRasterization(String bvRaster, Set<String> elevationFolders, String bvShape, String attributeID, String... values) {
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(bvShape, 0, attributeID, values);
		
		EnteteRaster refEntete = null;
		Coverage refCov;
		for(String ef : elevationFolders) {
			refCov = CoverageManager.getCoverage(ef);
			if(refEntete == null) {
				refEntete = refCov.getEntete();
			}else{
				refEntete = EnteteRaster.sum(refEntete, refCov.getEntete());
			}
			refCov.dispose();
		}
		refEntete.setNoDataValue(-1);

		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		for(String v : values) {
			codes.put(v, 1);	
		}
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(bvShape, attributeID, codes, entete, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(bvRaster, data, entete);
	}
	
	public static void elevationConstruction(String elevation, String bv, Set<String> elevationFolders){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		Envelope bvEnvelope = bvEntete.getEnvelope();
		bvCov.dispose();
		
		float[] outData = new float[bvEntete.width()*bvEntete.height()];
		
		Coverage altCov;
		EnteteRaster altEntete;
		float[] altData;
		for(String ef : elevationFolders) {
			
			altCov = CoverageManager.getCoverage(ef);
			altEntete = altCov.getEntete();
			altData = altCov.getData(EnteteRaster.getROI(altEntete, bvEnvelope));
			altCov.dispose();
			
			for(int ind=0; ind<outData.length; ind++) {
				outData[ind] = Math.max(outData[ind], altData[ind]);
			}
		}
		
		for(int ind=0; ind<outData.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				outData[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write(elevation, outData, bvEntete);
	}
	
	public static void osRecovery(String os, String territory, String osSource, int divisor) {
		
		Coverage ossCov = CoverageManager.getCoverage(osSource);
		
		Coverage bvCov = CoverageManager.getCoverage(territory);
				
		Coverage cov = Util.extendAndFill(ossCov, bvCov, divisor);
		
		ossCov.dispose();
		bvCov.dispose();
		
		CoverageManager.write(os, cov.getData(), cov.getEntete());
		
		cov.dispose();
	}
	
	public static void osRecovery(String os, EnteteRaster entete, String osSource, int divisor) {
		
		Coverage ossCov = CoverageManager.getCoverage(osSource);
				
		Coverage cov = Util.extendAndFill(ossCov, entete, divisor);
		
		ossCov.dispose();
		
		CoverageManager.write(os, cov.getData(), cov.getEntete());
		
		cov.dispose();
	}
	
	public static void surfaceWoodRasterization(String os, String surfaceWoodShape, String surfaceWoodAttribute, Map<String, Integer> surfaceWoodCodes){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(osData, osEntete, surfaceWoodShape, surfaceWoodAttribute, surfaceWoodCodes);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearWoodRasterization(String os, String linearWoodShape, int linearWoodCode){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearWoodShape, linearWoodCode, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearRoadRasterization(String os, String linearRoadShape, String linearRoadAttribute, Map<String, Integer> linearRoadCodes){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearRoadShape, linearRoadAttribute, linearRoadCodes, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void linearTrainRasterization(String os, String linearTrainShape, int linearTrainCode){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearTrainShape, linearTrainCode, 2.5);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void surfaceWaterRasterization(String os, String surfaceWaterShape, int surfaceWaterCode){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(osData, osEntete, surfaceWaterShape, surfaceWaterCode);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}

	public static void linearWaterRasterization(String os, String linearWaterShape, int linearWaterCode){
	
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
	
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, linearWaterShape, linearWaterCode, 2.5);
		cov.dispose();
	
		CoverageManager.write(os, osData, osEntete);
	}
	
	public static void sectionHydroRasterization(String os, String sectionHydroShape){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage(osData, osEntete, sectionHydroShape, 1, 0);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(os, data, osEntete);
	}
	/*
	public static void surfaceHydroRasterization(String os, String surfaceHydroShape){
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(osData, osEntete, surfaceHydroShape, 1, 0);
		cov.dispose();
		
		CoverageManager.write(os, osData, osEntete);
	}
	*/
	
	
	public static void slopeDetection(String slopeIntensity, String altitude) {
		
		Coverage altCov = CoverageManager.getCoverage(altitude);
		EnteteRaster entete = altCov.getEntete();
		float[] altData = altCov.getData();
		altCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(altData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setUnfilters(new int[]{-1});
		//builder.addMetric("slope-direction");
		builder.addMetric("slope-intensity");
		builder.setWindowSize(3);
		//builder.addGeoTiffOutput("slope-direction", outputPath+bv_slope_direction);
		builder.addGeoTiffOutput("slope-intensity", slopeIntensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	public static void normalizeSlopeIntensity(String normSlopeIntensity, String slopeIntensity) {
		
		Coverage intCov = CoverageManager.getCoverage(slopeIntensity);
		float[] intData = intCov.getData();
		EnteteRaster entete = intCov.getEntete();
		intCov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, intData){
			@Override
			protected float doTreat(float[] v) {
				float vi = v[0];
				if(vi == -1){
					return -1;
				}
				return getSlopeIntensity(vi);
			}
		};
		cal.run();
		
		CoverageManager.write(normSlopeIntensity, data, entete);
	}
	
	private static float getSlopeIntensity(float slopIntMax) {
		
		float v =  slopIntMax;
		
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	public static void waterMassInitialization(String initialWaterMass, String os, int waterQuantity) {
		
		Coverage cov = CoverageManager.getCoverage(os);
		EnteteRaster entete = cov.getEntete();
		float[] bvData = cov.getData();
		cov.dispose();
		
		float masseInitiale = (float) (waterQuantity * Math.pow(entete.cellsize(), 2));
		
		float[] data = new float[entete.width()*entete.height()];
		for(int i=0; i<data.length; i++) {
			if(bvData[i] == -1) {
				data[i] = -1;
			}else {
				data[i] = masseInitiale;
			}
		}
		
		CoverageManager.write(initialWaterMass, data, entete);
	}
	
	public static void infiltrationGeneration(String infiltration, String os, String infiltrationMap) {
	
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		
		FileMap fMap = new FileMap(infiltrationMap, "cover", "infiltration");
		/*
		fMap.change(5, infiltration);
		fMap.change(6, infiltration);
		fMap.change(7, infiltration);
		fMap.change(9, infiltration);
		fMap.change(10, infiltration);
		fMap.change(13, infiltration);
		
		fMap.display();
		*/
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(infiltration, data, osEntete);
	}
	
	public static void erodibilityGeneration(String erodibility, String os, String erodibilityMap) {
		
		Coverage osCov = CoverageManager.getCoverage(os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(erodibilityMap, "cover", "erodibility") ;
		/*
		fMap.change(5, versement);
		fMap.change(6, versement);
		fMap.change(7, versement);
		fMap.change(9, versement);
		fMap.change(10, versement);
		fMap.change(13, versement);
		
		fMap.display();
		*/
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(erodibility, data, osEntete);
	}
	
	public static void waterMassCumulation(String cumulWaterMass, String initialWaterMass, String altitude, String infltration, String slopeIntensity, int displacement, int windowSize) {
		
		Coverage massInitCov = CoverageManager.getCoverage(initialWaterMass);
		EnteteRaster entete = massInitCov.getEntete();
		float[] massInitData = massInitCov.getData();
		massInitCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(infltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		Coverage slopIntCov = CoverageManager.getCoverage(slopeIntensity);
		float[] slopIntData = slopIntCov.getData();
		slopIntCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(massInitData, altData, infilData, slopIntData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("mass-cumul");
		builder.setWindowSize(windowSize);
		//builder.setDMax(masseEauInitiale*Math.pow(entete.cellsize(), 2));
		builder.setDisplacement(displacement);
		builder.addGeoTiffOutput("degat-mass-cumul", cumulWaterMass);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	public static void erodibilityIntensityGeneration(String erodibilityIntensity, String erodibility, String cumulWaterMass, String normSlopeIntensity, int displacement) {
		
		//Coverage versCov = Util.reduce(CoverageManager.getCoverage(bv_versement), 10);
		Coverage versCov = CoverageManager.getCoverage(erodibility);
		EnteteRaster outEntete = versCov.getEntete();
		float[] versData = versCov.getData();
		versCov.dispose();
		
		Coverage degatCov = CoverageManager.getCoverage(cumulWaterMass);
		EnteteRaster inEntete = degatCov.getEntete();
		//float[] degatData = degatCov.getData();
		float[] degatData = Util.extend(degatCov.getData(), inEntete, outEntete, displacement);
		degatCov.dispose();
		
		//Coverage intCov = Util.reduce(CoverageManager.getCoverage(bv_norm_slope_intensity), 10);
		Coverage intCov = CoverageManager.getCoverage(normSlopeIntensity);
		float[] intData = intCov.getData();
		intCov.dispose();
		
		float[] data = new float[outEntete.width()*outEntete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, versData, degatData, intData){
			@Override
			protected float doTreat(float[] v) {
				float vv = v[0];
				if(vv == -1){
					return -1;
				}
				float vd = v[1];
				float vi = v[2];
				if(vi > 0 && vd != -1) {
					//return (vv * vd * vi) / 10;
					return (vv * vd * vi);
				}else {
					return 0;
				}
			}
		};
		cal.run();
		
		CoverageManager.write(erodibilityIntensity, data, outEntete);
	}
	
	public static void erosionCalculation(String sourceErosionIntensity, String depositionErosionIntensity, String elevation, String infiltration, String slopeIntensity, String erodibilityIntensity, int displacement, int windowSize){
		
		Coverage intVersCov = CoverageManager.getCoverage(erodibilityIntensity);
		EnteteRaster entete = intVersCov.getEntete();
		float[] intVersData = intVersCov.getData();
		intVersCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(elevation);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		Coverage slopIntCov = CoverageManager.getCoverage(slopeIntensity);
		float[] slopIntData = slopIntCov.getData();
		slopIntCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(intVersData, altData, infilData, slopIntData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		//builder.addMetric("source-erosion-intensity");
		builder.addMetric("mass-cumul");
		builder.setWindowSize(windowSize);
		builder.setDisplacement(displacement);
		builder.addGeoTiffOutput("mass-cumul", sourceErosionIntensity);
		builder.addGeoTiffOutput("degat-mass-cumul", depositionErosionIntensity);
		//builder.addGeoTiffOutput("depot-mass-cumul", outputPath+bv_depot_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

}
