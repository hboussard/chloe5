package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.util.Friction;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptErosionRuissellement2 {

	private static final String path = "E:/temp/slope/test2/data/";
	
	private static final String bv_altitude = path+"bv_altitude.tif";
	private static final String bv_intensity = path+"bv_intensity.tif";
	private static final String bv_direction = path+"bv_direction.tif";
	private static final String bv_infiltration_map = path+"infiltration_map.txt";
	private static final String bv_versement_map = path+"versement_map.txt";
	/*
	// situation initiale
	private static final String bv_os = path+"bv_os.tif";
	private static final String output_path = path+"erosion/";
	private static final String bv_infiltration = output_path+"bv_infiltration.tif";
	private static final String bv_versement = output_path+"bv_versement.tif";
	private static final String bv_intensite_versement = output_path+"bv_intensite_versement.tif";
	private static final String bv_erosion_emprise = output_path+"bv_erosion_emprise.tif";
	private static final String bv_erosion_intensity = output_path+"bv_erosion_intensity.tif";
	private static final String bv_degat_erosion_emprise = output_path+"bv_degat_erosion_emprise.tif";
	private static final String bv_degat_erosion_intensity = output_path+"bv_degat_erosion_intensity.tif";
	private static final String bv_norm_erosion_intensity = output_path+"bv_norm_erosion_intensity.tif";
	private static final String bv_norm_degat_erosion_intensity = output_path+"bv_norm_degat_erosion_intensity.tif";
	private static final String bv_norm_factor_erosion_intensity = output_path+"bv_norm_factor_erosion_intensity.tif";
	*/
	
	// sitation amenagement
	private static final String bv_os = path+"bv_os_amenagement.tif";
	private static final String output_path = path+"alt_erosion/";
	private static final String bv_infiltration = output_path+"bv_alt_infiltration.tif";
	private static final String bv_versement = output_path+"bv_alt_versement.tif";
	private static final String bv_intensite_versement = output_path+"bv_alt_intensite_versement.tif";
	private static final String bv_erosion_emprise = output_path+"bv_alt_erosion_emprise_5m_201p.tif";
	private static final String bv_erosion_intensity = output_path+"bv_alt_erosion_intensity_5m_201p.tif";
	private static final String bv_degat_erosion_emprise = output_path+"bv_alt_degat_erosion_emprise.tif";
	private static final String bv_degat_erosion_intensity = output_path+"bv_alt_degat_erosion_intensity.tif";
	private static final String bv_norm_erosion_intensity = output_path+"bv_alt_norm_erosion_intensity.tif";
	private static final String bv_norm_degat_erosion_intensity = output_path+"bv_alt_norm_degat_erosion_intensity.tif";
	private static final String bv_norm_factor_erosion_intensity = output_path+"bv_alt_norm_factor_erosion_intensity.tif";
	
	public static void main(String[] args) {
	
		detectionPente();
		
		//generationOccupationSolAmenagement();
		
		// situation
		generationCoeffInfiltration();
		generationCoeffVersement();
		generationIntensiteVersement();
		
		calculErosion();
		calculDegatErosion();
		
		normalize(bv_erosion_intensity, bv_norm_erosion_intensity, 30000000);
		normalize(bv_degat_erosion_intensity, bv_norm_degat_erosion_intensity, 30000000);
		factor(bv_norm_erosion_intensity, bv_norm_degat_erosion_intensity, bv_norm_factor_erosion_intensity);
		
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
	
	private static void generationOccupationSolAmenagement(){
		Coverage cov = CoverageManager.getCoverage(path+"bv_os.tif");
		EnteteRaster entete = cov.getEntete();
		float[] dataOS = cov.getData();
		cov.dispose();
		
		Coverage covAmenagement = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"amenagement_bocager.shp", entete, 4, 0);
		float[] dataAm = covAmenagement.getData();
		covAmenagement.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataOS, dataAm){
			@Override
			protected float doTreat(float[] v) {
				float vam = v[1];
				if(vam == 4){
					return vam;
				}
				return v[0];
			}
		};
		cal.run();
		
		CoverageManager.write(path+"bv_os_amenagement.tif", data, entete);
	}
	
	private static void calculErosion(){
		
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
		builder.addMetric("erosionemprise");
		builder.addMetric("erosionintensity");
		builder.setWindowSize(201);
		//builder.setDisplacement(2);
		builder.addGeoTiffOutput("erosionemprise", bv_erosion_emprise);
		builder.addGeoTiffOutput("erosionintensity", bv_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDegatErosion(){
		
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
		builder.addMetric("degaterosionemprise");
		builder.addMetric("degaterosionintensity");
		builder.setWindowSize(201);
		//builder.setDisplacement(2);
		builder.setDMax(500.0);
		builder.addGeoTiffOutput("degaterosionemprise", bv_degat_erosion_emprise);
		builder.addGeoTiffOutput("degaterosionintensity", bv_degat_erosion_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
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
		builder.addMetric("slopedirection");
		builder.addMetric("slopeintensity");
		builder.setWindowSize(3);
		builder.addGeoTiffOutput("slopedirection", bv_direction);
		builder.addGeoTiffOutput("slopeintensity", bv_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
}
