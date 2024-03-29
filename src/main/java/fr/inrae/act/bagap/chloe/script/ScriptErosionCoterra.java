package fr.inrae.act.bagap.chloe.script;

import java.io.File;

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

public class ScriptErosionCoterra {

	private static final String path = "C:/Data/projet/coterra/essai_magdelaine/data/";
	
	private static final String bv = path+"bv_magdelaine.tif";
	private static final String bv_os_bre = path+"bv_magdelaine_OSO_bre.tif";
	private static final String bv_altitude = path+"bv_altitude.tif";
	private static final String bv_infiltration_map = path+"infiltration_map.txt";
	private static final String bv_versement_map = path+"versement_map.txt";
	private static final String bv_friction_maximale = path+"bv_friction_maximale.tif";
	
	// situation initiale	
	private static final String bv_os = path+"bv_magdelaine_OS.tif"; 
	private static final String output_path = path+"erosion2/";
	private static final String bv_intensity = output_path+"bv_intensity.tif";
	private static final String bv_direction = output_path+"bv_direction.tif";
	private static final String bv_infiltration = output_path+"bv_infiltration.tif";
	private static final String bv_versement = output_path+"bv_versement.tif";
	private static final String bv_intensite_versement = output_path+"bv_intensite_versement.tif";
	private static final String bv_source_erosion_emprise = output_path+"bv_source_erosion_emprise_pente.tif";
	private static final String bv_source_erosion_intensity = output_path+"bv_source_erosion_intensity_pente.tif";
	private static final String bv_degat_erosion_emprise = output_path+"bv_degat_erosion_emprise_pente.tif";
	private static final String bv_degat_erosion_intensity = output_path+"bv_degat_erosion_intensity_pente.tif";
	private static final String bv_norm_erosion_intensity = output_path+"bv_norm_erosion_intensity.tif";
	private static final String bv_norm_degat_erosion_intensity = output_path+"bv_norm_degat_erosion_intensity.tif";
	private static final String bv_norm_factor_erosion_intensity = output_path+"bv_norm_factor_erosion_intensity.tif";
	
	public static void main(String[] args) {
		
		Util.createAccess(output_path);
		
		// initialisation
		//convertRGE();
		//rasterizeBV();
		//recuperationAltitude();
		//recuperationOS();
		//detectionPente();
		
		// situation
		//generationCoeffInfiltration();
		//generationCoeffVersement();
		//generationIntensiteVersement();
		//generationFrictionMaximales();		
		
		//calculSourceErosion();
		
		//calculDegatErosionAltitude();
		calculDegatErosionPente();
				
		//normalize(bv_erosion_intensity, bv_norm_erosion_intensity, 100000000);
		//normalize(bv_degat_erosion_intensity, bv_norm_degat_erosion_intensity, 100000000);
		//factor(bv_norm_erosion_intensity, bv_norm_degat_erosion_intensity, bv_norm_factor_erosion_intensity);
		
	}
	
	private static void detectionPente(){
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		EnteteRaster entete = altCov.getEntete();
		float[] altData = altCov.getData();
		altCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(altData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("slope-direction");
		builder.addMetric("slope-intensity");
		builder.setWindowSize(3);
		builder.addGeoTiffOutput("slope-direction", bv_direction);
		builder.addGeoTiffOutput("slope-intensity", bv_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculSourceErosion(){
		
		Coverage intVersCov = CoverageManager.getCoverage(bv_intensite_versement);
		EnteteRaster entete = intVersCov.getEntete();
		float[] intVersData = intVersCov.getData();
		intVersCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(intVersData);
		builder.setRasterTab2(altData);
		builder.setRasterTab3(infilData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("source-erosion-emprise");
		builder.addMetric("source-erosion-intensity");
		builder.setWindowSize(201);
		//builder.setDisplacement(100);
		builder.setDMax(500.0);
		builder.addGeoTiffOutput("source-erosion-emprise", bv_source_erosion_emprise);
		builder.addGeoTiffOutput("source-erosion-intensity", bv_source_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDegatErosionAltitude(){
		
		Coverage intVersCov = CoverageManager.getCoverage(bv_intensite_versement);
		EnteteRaster entete = intVersCov.getEntete();
		float[] intVersData = intVersCov.getData();
		intVersCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(intVersData, altData, infilData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("degat-erosion-emprise");
		builder.addMetric("degat-erosion-intensity");
		builder.setWindowSize(201);
		//builder.setDisplacement(100);
		builder.setDMax(500.0);
		builder.addGeoTiffOutput("degat-erosion-emprise", bv_degat_erosion_emprise);
		builder.addGeoTiffOutput("degat-erosion-intensity", bv_degat_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDegatErosionPente(){
		
		Coverage intVersCov = CoverageManager.getCoverage(bv_intensite_versement);
		EnteteRaster entete = intVersCov.getEntete();
		float[] intVersData = intVersCov.getData();
		intVersCov.dispose();
		
		Coverage slopeIntCov = CoverageManager.getCoverage(bv_intensity);
		float[] slopeIntData = slopeIntCov.getData();
		slopeIntCov.dispose();
		
		Coverage slopeDirCov = CoverageManager.getCoverage(bv_direction);
		float[] slopeDirData = slopeDirCov.getData();
		slopeDirCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(intVersData, slopeIntData, slopeDirData, infilData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("degat-erosion-emprise");
		builder.addMetric("degat-erosion-intensity");
		builder.setWindowSize(201);
		//builder.setDisplacement(100);
		builder.setDMax(500.0);
		builder.addGeoTiffOutput("degat-erosion-emprise", bv_degat_erosion_emprise);
		builder.addGeoTiffOutput("degat-erosion-intensity", bv_degat_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void generationCoeffInfiltration() {
		Coverage osCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_infiltration_map, "cover", "infiltration") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_infiltration, data, osEntete);
	}
	
	private static void generationCoeffVersement() {
		Coverage osCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_versement_map, "cover", "versement") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_versement, data, osEntete);
	}
	
	private static void generationFrictionMaximales() {
		
		Coverage cov1 = CoverageManager.getCoverage(bv_intensity);
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(bv_infiltration);
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data1, data2){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				
				return friction(getSlopeIntensity(v1), v[1]);
			}
		};
		cal.run();
		
		CoverageManager.write(bv_friction_maximale, data, entete);
	}

	private static void generationIntensiteVersement() {
		
		Coverage cov1 = CoverageManager.getCoverage(bv_intensity);
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(bv_versement);
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data1, data2){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				v1 = Math.abs(90-v1);
				float v2 = v[1];
				return v1*v2*100.0f;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_intensite_versement, data, entete);
	}
	
	private static void recuperationOS() {
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster entete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_bre);
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, bvData, osData){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 != -1){
					return v[1];
				}
				return -1;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_os, data, entete);
	}
	
	private static void rasterizeBV() {
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_bre);
		EnteteRaster entete = osCov.getEntete();
		entete.setNoDataValue(-1);
		osCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"bv_shape/bv_magdelaine.shp", entete, 1, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(bv, data, entete);
	}
	
	private static void recuperationAltitude(){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		String path = "D:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/";
		String alti = path+"RGEALTI_MNT_5M_GEOTIF_LAMB93_IGN69_D031/";
		Coverage altCov = CoverageManager.getCoverage(alti);
		EnteteRaster entete = altCov.getEntete();
		float[] data = altCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		altCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write(bv_altitude, data, bvEntete);
	}
	
	// ex : 0530_6250
	private static void convertRGE(){
		String path = "D:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/";
		String inputPath = "RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D031/";
		String outputPath = "RGEALTI_MNT_5M_GEOTIF_LAMB93_IGN69_D031/";
		
		for(String file : new File(path+inputPath).list()){ 
			if(file.endsWith(".asc")){
				convert(path, inputPath, outputPath, file);
			}
		}
	}
	
	private static void convert(String globalPath, String inputPath, String outputPath, String file) {
		
		Util.createAccess(globalPath+outputPath);
		
		Coverage cov = CoverageManager.getCoverage(globalPath+inputPath+file);
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		entete.setNoDataValue(-1);
		cov.dispose();
		
		CoverageManager.write(globalPath+outputPath+file.replace(".asc", ".tif"), data, entete);
		
	}
	
	private static void factor(String input1, String input2, String output){
		Coverage cov1 = CoverageManager.getCoverage(input1);
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(input2);
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] outData = new float[entete.width() * entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data1, data2){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				return v1*v[1];
			}
		};
		cal.run();
		
		CoverageManager.write(output, outData, entete);
	}
	
	private static void normalize(String input, String output, int max){
		Coverage cov = CoverageManager.getCoverage(input);
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		float[] outData = new float[entete.width() * entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){
			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value == -1){
					return -1;
				}
				if(value >= max) {
					return 1;
				}
				return (float) (value/max);
			}
		};
		cal.run();
		
		CoverageManager.write(output, outData, entete);
	}
	
	private static void test2() {
		
		float cote_oppose = 5;
		float cote_adjacent = 10;
		float tangente = cote_oppose/cote_adjacent;
		double arctangente = Math.atan(tangente);
		double angle = Math.toDegrees(arctangente);
		
		System.out.println(tangente+" "+arctangente+" "+angle);
		System.out.println(Math.toDegrees(Math.atan(0.8)));
		
	}
	
	private static void test() {
		float si, inf, fr;
		/*
		si = getSlopeIntensity(100, 100, 10);
		inf = 0;
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 90, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 95, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 110, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 105, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 120, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		
		si = getSlopeIntensity(100, 80, 10);
		fr = friction(si, inf);
		System.out.println(si+" "+inf+" "+fr);
		System.out.println();
		*/
		
		si = getSlopeIntensity(100, 100, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 90, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 95, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 110, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 105, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 120, 10);
		System.out.println(si);
		System.out.println();
		
		si = getSlopeIntensity(100, 80, 10);
		System.out.println(si);
		System.out.println();
		
		
		/*
		System.out.println(getSlopeIntensity(90));
		System.out.println(getSlopeIntensity(100));
		System.out.println(getSlopeIntensity(80));
		System.out.println(getSlopeIntensity(135));
		System.out.println(getSlopeIntensity(45));
		System.out.println(getSlopeIntensity(145));
		System.out.println(getSlopeIntensity(35));
		*/
	}
	
	private static float getSlopeIntensity(float alt, float nalt, float dist) {
		/*
		if(alt == nalt) {
			return 0;
		}*/
		
		float cote_oppose = alt - nalt;
		float cote_adjacent = dist;
		float tangente = cote_oppose/cote_adjacent;
		double arctangente = Math.atan(tangente);
		double angle = Math.toDegrees(arctangente);
		
		System.out.println(cote_oppose+" "+cote_adjacent+" "+tangente+" "+arctangente+" "+angle);
		
		//float tangente2 = dist/(alt - nalt);
		//float tangente2 = (alt - nalt)/dist;
		//System.out.println("ici "+((180 + Math.toDegrees(Math.atan(tangente2)))%180.0));
		
		//float v =  (float) ((90 + (90 + Math.toDegrees(Math.atan(tangente))))%180.0);
		//float v =  (float) (Math.toDegrees(Math.atan(tangente))%180.0);
		
		float v =  (float) ((90 - angle)%180.0);
		System.out.println(v);
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private static float getSlopeIntensity(float angle) {
		
		//float v =  (float) ((180 + angle)%180.0);
		//float v =  (float) (angle%180.0);
		float v = angle;
		
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private static float friction(float slopeIntensity, float infiltration) {
		float friction = 1 + 9*infiltration - slopeIntensity;
		/*if(friction >= 9) {
			friction *= 10;
		}*/
		return friction;
	}
	
}
