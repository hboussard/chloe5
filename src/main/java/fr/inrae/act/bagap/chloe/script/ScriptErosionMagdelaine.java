package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;
import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptErosionMagdelaine {

	private static final String path = "C:/Data/projet/coterra/essai_petit_magdelaine/data/";
	
	private static final String bv = path+"bv_magdelaine.tif";
	private static final String bv_os_bre = path+"bv_magdelaine_OSO_bre.tif";
	private static final String bv_os = path+"bv_magdelaine_OS.tif"; 
	private static final String bv_altitude = path+"bv_altitude.tif";
	private static final String bv_infiltration_map = path+"infiltration_map.txt";
	private static final String bv_versement_map = path+"versement_map.txt";
	private static final String bocageAmenagement = path+"bocage_amenagement.shp";
	private static final String bv_os_prairie = path+"bv_magdelaine_OS_prairie.tif";
	//private static final String bv_os_prairie = path+"bv_magdelaine_OS_amenagement.tif";
	
	// initial
	private static final int quantitetEAU = 10; // mm
	private static final int displacement = 1;
	
	private static final String bv_slope_intensity = "bv_slope_intensity.tif";
	private static final String bv_infiltration = "bv_infiltration.tif";
	private static final String bv_versement = "bv_versement.tif";
	private static final String bv_masse_eau_initial = "bv_masse_eau_initial_"+quantitetEAU+"mm.tif";
	private static final String bv_masse_eau_cumul = "bv_masse_eau_cumul_"+quantitetEAU+"mm.tif";
	private static final String bv_intensite_versement = "bv_intensite_versement_"+quantitetEAU+"mm.tif";
	private static final String bv_source_erosion_intensity = "bv_source_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_depot_erosion_intensity = "bv_depot_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_degat_erosion_intensity = "bv_degat_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_norm_slope_intensity = "bv_norm_slope_intensity.tif";
	private static final String bv_norm_source_erosion_intensity = "bv_norm_source_erosion_intensity_"+quantitetEAU+"mm.tif";
	
	/*
	private static final String bv_slope_intensity = output_path+"bv_slope_intensity.tif";
	private static final String bv_infiltration = output_path+"bv_infiltration.tif";
	private static final String bv_versement = output_path+"bv_versement.tif";
	private static final String bv_masse_eau_initial = output_path+"bv_masse_eau_initial_"+quantitetEAU+"mm.tif";
	private static final String bv_masse_eau_cumul = output_path+"bv_masse_eau_cumul_"+quantitetEAU+"mm.tif";
	private static final String bv_intensite_versement = output_path+"bv_intensite_versement_"+quantitetEAU+"mm.tif";
	private static final String bv_source_erosion_intensity = output_path+"bv_source_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_depot_erosion_intensity = output_path+"bv_depot_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_degat_erosion_intensity = output_path+"bv_degat_erosion_intensity_"+quantitetEAU+"mm.tif";
	private static final String bv_norm_slope_intensity = output_path+"bv_norm_slope_intensity.tif";
	*/
	
	public static void main(String[] args) {
		
		// analyse
		analyseErosion();
		
		// remote sensing
		//remoteSensing();
	}
	
	private static void analyseErosion() {
		
		String outputPath = path+"test2_"+quantitetEAU+"mm/";
		
		Util.createAccess(outputPath);
		
		//recuperationOccupationSolAmenagement();
		//rasterizeBV();
		//recuperationOS();
		//recuperationAltitude();
		//recuperePrairieFromRPG();
		detectionPente(outputPath);
		normalizeSlopeIntensity(outputPath);
		generationMasseEau(outputPath);
		
		// situation
		generationInfiltrationMap(outputPath, 0.25f);
		generationVersementMap(outputPath, 1f);
	
		generationCumulMasseEau(outputPath, bv_masse_eau_cumul, displacement, 201);
		
		generationIntensiteVersement(outputPath);
		
		calculErosion(outputPath, displacement, 201);
		
	}

	private static void remoteSensing() {
		
		//float inf = 0.5f;
		//float ver = 0.5f;
		
		for(int inf=25; inf<=50; inf+=5) {
			for(int ver=50; ver<=100; ver+=10) {
				
				String outputPath = path+"remote_sensing3/erosion_f"+inf+"_v"+ver+"/";
				/*
				System.out.println(outputPath);
				
				Util.createAccess(outputPath);
				detectionPente(outputPath);
				normalizeSlopeIntensity(outputPath);
				generationMasseEau(outputPath);
				generationInfiltrationMap(outputPath, inf/100.0f);
				generationVersementMap(outputPath, ver/100.0f);
				generationCumulMasseEau(outputPath, bv_masse_eau_cumul, displacement, 201);
				generationIntensiteVersement(outputPath);
				calculErosion(outputPath, displacement, 201);
				normalize(outputPath, bv_source_erosion_intensity, bv_norm_source_erosion_intensity, 100000);
				*/
				//System.out.println("infiltration = "+inf/100.0f+", versement = "+ver/100.0f);
				noteSourceErosion(outputPath);
			}
		}
	}

	private static void noteSourceErosion(String outputPath) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile(outputPath+bv_norm_source_erosion_intensity);
		builder.addMetric("average");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	private static void generationCumulMasseEau(String outputPath, String masseEauCumul, int displacement, int windowSize) {
		
		Coverage massInitCov = CoverageManager.getCoverage(outputPath+bv_masse_eau_initial);
		EnteteRaster entete = massInitCov.getEntete();
		float[] massInitData = massInitCov.getData();
		massInitCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(outputPath+bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		Coverage slopIntCov = CoverageManager.getCoverage(outputPath+bv_slope_intensity);
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
		builder.addGeoTiffOutput("degat-mass-cumul", outputPath+masseEauCumul);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	private static void calculErosion(String outputPath, int displacement, int windowSize){
		
		Coverage intVersCov = CoverageManager.getCoverage(outputPath+bv_intensite_versement);
		EnteteRaster entete = intVersCov.getEntete();
		float[] intVersData = intVersCov.getData();
		intVersCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(outputPath+bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		Coverage slopIntCov = CoverageManager.getCoverage(outputPath+bv_slope_intensity);
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
		builder.addGeoTiffOutput("mass-cumul", outputPath+bv_source_erosion_intensity);
		builder.addGeoTiffOutput("degat-mass-cumul", outputPath+bv_degat_erosion_intensity);
		//builder.addGeoTiffOutput("depot-mass-cumul", outputPath+bv_depot_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDegatErosion(int displacement, boolean interpolation, int windowSize){
		
		Coverage massInitCov = CoverageManager.getCoverage(bv_intensite_versement);
		EnteteRaster entete = massInitCov.getEntete();
		float[] massInitData = massInitCov.getData();
		massInitCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		Coverage infilCov = CoverageManager.getCoverage(bv_infiltration);
		float[] infilData = infilCov.getData();
		infilCov.dispose();
		
		Coverage slopIntCov = CoverageManager.getCoverage(bv_slope_intensity);
		float[] slopIntData = slopIntCov.getData();
		slopIntCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(massInitData, altData, infilData, slopIntData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("degat-erosion-intensity");
		builder.setWindowSize(windowSize);
		builder.setDMax(2500);
		builder.setDisplacement(displacement);
		builder.setInterpolation(interpolation);
		builder.addGeoTiffOutput("degat-erosion-intensity", bv_degat_erosion_intensity);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void normalizeSlopeIntensity(String outputPath) {
		
		Coverage intCov = CoverageManager.getCoverage(outputPath+bv_slope_intensity);
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
		
		CoverageManager.write(outputPath+bv_norm_slope_intensity, data, entete);
	}
	
	private static void generationIntensiteVersement(String outputPath) {
		
		//Coverage versCov = Util.reduce(CoverageManager.getCoverage(bv_versement), 10);
		Coverage versCov = CoverageManager.getCoverage(outputPath+bv_versement);
		EnteteRaster outEntete = versCov.getEntete();
		float[] versData = versCov.getData();
		versCov.dispose();
		
		Coverage degatCov = CoverageManager.getCoverage(outputPath+bv_masse_eau_cumul);
		EnteteRaster inEntete = degatCov.getEntete();
		//float[] degatData = degatCov.getData();
		float[] degatData = Util.extend(degatCov.getData(), inEntete, outEntete, displacement);
		degatCov.dispose();
		
		//Coverage intCov = Util.reduce(CoverageManager.getCoverage(bv_norm_slope_intensity), 10);
		Coverage intCov = CoverageManager.getCoverage(outputPath+bv_norm_slope_intensity);
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
		
		CoverageManager.write(outputPath+bv_intensite_versement, data, outEntete);
	}
	
	private static void generationMasseEau(String outputPath) {
		Coverage cov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster entete = cov.getEntete();
		float[] bvData = cov.getData();
		cov.dispose();
		
		float masseInitiale = (float) (quantitetEAU * Math.pow(entete.cellsize(), 2));
		
		float[] data = new float[entete.width()*entete.height()];
		for(int i=0; i<data.length; i++) {
			if(bvData[i] == -1) {
				data[i] = -1;
			}else {
				data[i] = masseInitiale;
			}
		}
		
		CoverageManager.write(outputPath+bv_masse_eau_initial, data, entete);
	}

	private static void normalize(String outputPath, String input, String output, int max){
		Coverage cov = CoverageManager.getCoverage(outputPath+input);
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
				/*
				if(value >= max) {
					return 1;
				}*/
				return (float) (value/max);
			}
		};
		cal.run();
		
		CoverageManager.write(outputPath+output, outData, entete);
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
	
	private static void recuperationOccupationSolAmenagement(){
		Coverage cov = CoverageManager.getCoverage(path+"bv_magdelaine_OS_prairie.tif");
		EnteteRaster entete = cov.getEntete();
		float[] dataOS = cov.getData();
		cov.dispose();
		
		Coverage covAmenagement = ShapeFile2CoverageConverter.getSurfaceCoverage(bocageAmenagement, entete, 25, 0);
		float[] dataAm = covAmenagement.getData();
		covAmenagement.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataOS, dataAm){
			@Override
			protected float doTreat(float[] v) {
				float vam = v[1];
				if(vam == 25){
					return vam;
				}
				return v[0];
			}
		};
		cal.run();
		
		CoverageManager.write(path+"bv_magdelaine_OS_amenagement.tif" , data, entete);
	}
	
	private static float getSlopeIntensity(float alt, float nalt, float cote_adjacent) {
		
		if(alt == nalt) {
			return 0;
		}
		
		float cote_oppose = alt - nalt;
		float tangente = cote_oppose/cote_adjacent;
		double arctangente = Math.atan(tangente);
		double angle = Math.toDegrees(arctangente);
		
		//System.out.println(cote_oppose+" "+cote_adjacent+" "+tangente+" "+arctangente+" "+angle);
		
		float v =  (float) ((90 - angle)%180.0);
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		//}else if(v >= 90){
			//return  0;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private static float getSlopeIntensity(float slopIntMax) {
		
		float v =  slopIntMax;
		
		if(v <= 45){
			return  1;
		}else if(v >= 135){
			return  -1;
		//}else if(v >= 90){
		//	return  0;
		}else{
			return (float) ((90-v)/45.0);
		}
	}
	
	private static float friction(float slopeIntensity, float infiltration) {
		//float friction = 2 + 9*infiltration - slopeIntensity;
		/*if(friction >= 9) {
			friction *= 10;
		}*/
		//float friction = (9 - 9*slopeIntensity + 27*infiltration) / 9.0f;
		//float friction = 9 - 9*slopeIntensity + ((float) (27*Math.pow(infiltration, 2)));
		//float friction = (9 - 9*slopeIntensity + ((float) (27*Math.pow(infiltration, 2)))) / 9.0f;
		//friction = (float) Math.pow(friction, 2);
		
		//float friction = 1 - slopeIntensity + ((float) (30*Math.pow(infiltration, 2)));
		//float friction = 9 - 9*slopeIntensity + ((float) (27*Math.pow(infiltration, 2)));
		
		//float friction = 1 - slopeIntensity + ((float) (27*Math.pow(infiltration, 2)));
		
		//float friction = ((float) Math.pow((1 - slopeIntensity), 5)) + ((float) (27*Math.pow(infiltration, 2)));
		float friction = ((float) Math.pow((1 - slopeIntensity), 5)) + ((float) (100*Math.pow(infiltration, 2)));
		
		System.out.println(slopeIntensity+" "+infiltration+" "+friction);
		return friction;
	}
	
	private static void calculTestCompression(){
		
		Coverage cov;
		EnteteRaster entete;
		float[] data;
		
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/bv_altitude.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_init.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_RLE.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_T_4.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_T_6.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_LZW.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_JPEG.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_ZLib.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_PackBits.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_Deflate.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_EXIF_JPEG.tif", data, entete);
		//CoverageManager.write("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_ZSTD.tif", data, entete);
		
		 
		/*
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_RLE.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		*/
		/*
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_T_4.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		*/
		/*
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_CCITT_T_6.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		*/
		
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_LZW.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		/*
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_JPEG.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		*/
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_ZLib.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_PackBits.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_Deflate.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		/*
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_EXIF_JPEG.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		*/
		cov = CoverageManager.getCoverage("C:/Data/projet/coterra/essai_petit_magdelaine/data/test_compression/altitude_ZSTD.tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
	}
	
	private static void detectionPente(String outputPath){
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
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
		builder.addGeoTiffOutput("slope-intensity", outputPath+bv_slope_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void generationInfiltrationMap() {
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_infiltration_map, "cover", "infiltration") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_infiltration, data, osEntete);
	}
	
	private static void generationVersementMap() {
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_versement_map, "cover", "versement") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_versement, data, osEntete);
	}
	
	private static void generationInfiltrationMap(String outputPath, float infiltration) {
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_infiltration_map, "cover", "infiltration");
		fMap.change(5, infiltration);
		fMap.change(6, infiltration);
		fMap.change(7, infiltration);
		fMap.change(9, infiltration);
		fMap.change(10, infiltration);
		fMap.change(13, infiltration);
		
		//fMap.display();
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(outputPath+bv_infiltration, data, osEntete);
	}
	
	private static void generationVersementMap(String outputPath, float versement) {
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_versement_map, "cover", "versement") ;
		fMap.change(5, versement);
		fMap.change(6, versement);
		fMap.change(7, versement);
		fMap.change(9, versement);
		fMap.change(10, versement);
		fMap.change(13, versement);
		
		//fMap.display();
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(outputPath+bv_versement, data, osEntete);
	}
	
	private static void generationIntensiteVersementMap() {
		
		Coverage cov1 = CoverageManager.getCoverage(bv_slope_intensity);
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
				float vInt = v[0];
				if(vInt == -1){
					return -1;
				}
				//v1 = Math.abs(90-v1);
				vInt = 90-Math.min(vInt, 90);
				float vVers = v[1];
				return vInt*vVers*100.0f;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_intensite_versement, data, entete);
	}

	private static void recuperePrairieFromRPG() {
		
		Coverage bvCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("PPH", 24);
		codes.put("PRL", 24);
		codes.put("SPH", 24);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"rpg/parcelle_rpg_2020.shp", "CODE_CULTU", codes, bvEntete, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		float[] outData = new float[bvEntete.width()*bvEntete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, bvData, data){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 != -1){
					float v2 = v[1];
					if(v1 == 25 || v1 == 23 || v1 == 27) {
						return v1;
					}
					if(v2 > 0) {
						return v2;
					}
					if(v2 == -1 && v1 == 13) {
						return 24;
					}
					return v1;
				}
				return -1;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_os_prairie, outData, bvEntete);
	}

	private static void rasterizeBV() {
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(path+"Couche SIG BV Magdelaine/BV_Magdelaine.shp", 0);
		Coverage refCov = CoverageManager.getCoverage(bv_os_bre);
		EnteteRaster refEntete = refCov.getEntete();
		refCov.dispose();
		refEntete.setNoDataValue(-1);

		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"Couche SIG BV Magdelaine/BV_Magdelaine.shp", entete, 1, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(bv, data, entete);
	}
	
	private static void recuperationOS() {
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster entete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_bre);
		EnteteRaster enteteCov = osCov.getEntete();
		float[] osData = osCov.getData(EnteteRaster.getROI(enteteCov, entete.getEnvelope()));
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
}
