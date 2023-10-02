package fr.inrae.act.bagap.chloe.script;

import java.io.File;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabInverseAltitudeRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabInverseSlopeDirectionRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptRGEAlti {

	public static void main(String[] args) {
		
		//convertRGE();
		//recuperationBV();
		//recuperationAltitude();
		//cleanAltitude();
		//detectionPente();
		//cleanIntensite();
		//recuperationTronconHydrographique();
		//recuperationOccupationSol();
		//recuperationSurfaceHydrographique();
		//generationFrictionAbattement();
		//generationCoeffVersement();
		//calculDistanceFromEau();
		calculDistanceFromEauWithAltitude();
			
	}
	
	private static void cleanIntensite(){
		
		Coverage intCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_intensity.tif");
		EnteteRaster entete = intCov.getEntete();
		float[] intData = intCov.getData();
		intCov.dispose();
		
		float[] data = new float[intData.length];
		
		for(int ind=0; ind<intData.length; ind++){
			double v = intData[ind];
			if(v != entete.noDataValue()){
				if(v <= 45){
					data[ind] = 1;
				}else{
					data[ind] = (float) ((90-v)/45.0);
				}
			}else{
				data[ind] = entete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_slope.tif", data, entete);
	}
	
	private static void detectionPente(){
		
		Coverage altCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_altitude_lisse.tif");
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
		builder.addGeoTiffOutput("slopedirection", "H:/temp/slope/test/data/bv_direction.tif");
		builder.addGeoTiffOutput("slopeintensity", "H:/temp/slope/test/data/bv_intensity.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDistanceFromEauWithAltitude(){
		
		Coverage eauCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_surface_hydrographique.tif");
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_abattement.tif");
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_altitude_lisse.tif");
		float[] altData = altCov.getData();
		altCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabInverseAltitudeRCMDistanceAnalysis analysis = new TabInverseAltitudeRCMDistanceAnalysis(data, eauData, abbData, altData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write("H:/temp/slope/test/data/bv_distance_eau_altitude.tif", data, entete);
	}
	
	
	
	private static void calculDistanceFromEau(){
		
		Coverage eauCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_surface_hydrographique.tif");
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_abattement.tif");
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, eauData, abbData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write("H:/temp/slope/test/data/bv_distance_eau.tif", data, entete);
	}
	
	private static void generationFrictionAbattement(){
		Coverage osCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_os.tif");
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[osData.length];
		for(int ind=0; ind<osData.length; ind++){
			int vos = (int) osData[ind];
			switch(vos){
			case -1 : 
				data[ind] = osEntete.noDataValue();
				break;
			case 1:
				data[ind] = 1;
				break;
			case 2:
				data[ind] = 2;
				break;
			case 3:
				data[ind] = 5;
				break;
			case 4:
				data[ind] = 10;
				break;
			case 5:
				data[ind] = 10;
				break;
			case 6:
				data[ind] = 10;
				break;
			case 7:
				data[ind] = 0;
				break;
			case 8:
				data[ind] = 0;
				break;
			case 9:
				data[ind] = 0;
				break;
			case 10:
				data[ind] = 0;
				break;
			case 11:
				data[ind] = 0;
				break;
			case 12:
				data[ind] = 0;
				break;
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_abattement.tif", data, osEntete);
	}
	
	private static void generationCoeffVersement(){
		Coverage osCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_os.tif");
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[osData.length];
		for(int ind=0; ind<osData.length; ind++){
			int vos = (int) osData[ind];
			switch(vos){
			case -1 : 
				data[ind] = osEntete.noDataValue();
				break;
			case 1:
				data[ind] = 1;
				break;
			case 2:
				data[ind] = 1.5f;
				break;
			case 3:
				data[ind] = 0.1f;
				break;
			case 4:
				data[ind] = 0;
				break;
			case 5:
				data[ind] = 0;
				break;
			case 6:
				data[ind] = 0;
				break;
			case 7:
				data[ind] = 0;
				break;
			case 8:
				data[ind] = 0;
				break;
			case 9:
				data[ind] = 0;
				break;
			case 10:
				data[ind] = 0;
				break;
			case 11:
				data[ind] = 0;
				break;
			case 12:
				data[ind] = 0;
				break;
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_coeff_versement.tif", data, osEntete);
	}
	
	private static void recuperationOccupationSol(){
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage("G:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData(EnteteRaster.getROI(osEntete, bvEntete.getEnvelope()));
		osCov.dispose();
		
		for(int ind=0; ind<osData.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				osData[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_os.tif", osData, bvEntete);
	}
	
	private static void recuperationSurfaceHydrographique(){
		
		Coverage thCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_troncon_hydrographique.tif");
		EnteteRaster thEntete = thCov.getEntete();
		float[] thData = thCov.getData();
		thCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_os.tif");
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[thData.length];
		for(int ind=0; ind<osData.length; ind++){
			if(thData[ind] == thEntete.noDataValue()){
				data[ind] = thEntete.noDataValue();
			}else if(thData[ind] == 1 || osData[ind] == 7){
				data[ind] = 1;
			}else{
				data[ind] = 0;
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_surface_hydrographique.tif", data, thEntete);
	}
	
	private static void recuperationTronconHydrographique(){
		
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage("G:/data/sig/bd_topo/archive/2019/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D035_2019-06-20/BDTOPO/1_DONNEES_LIVRAISON_2019-06-00361/BDT_3-0_SHP_LAMB93_D035-ED2019-06-20/HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp", bvEntete, 1, 0);
		float[] data = cov.getData();
		cov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_troncon_hydrographique.tif", data, bvEntete);
	}
	
	private static void cleanAltitude(){
		Coverage altCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_altitude.tif");
		EnteteRaster entete = altCov.getEntete();
		float[] altData = altCov.getData();
		altCov.dispose();
		
		float[] data = new float[altData.length];

		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		//builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowSize(15);
		builder.setUnfilters(new int[]{-1});
		builder.setRasterTab(altData);
		builder.setEntete(entete);
		builder.addMetric("average");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		CoverageManager.write("H:/temp/slope/test/data/bv_altitude_lisse.tif", data, entete);
	}
	
	private static void recuperationAltitude(){
		
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		EnteteRaster entete = altCov.getEntete();
		float[] data = altCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		altCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_altitude.tif", data, bvEntete);
	}
	
	private static void recuperationBV(){
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope("H:/temp/slope/test/data/bv.shp", 0);
		Coverage refCov = CoverageManager.getCoverage("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		EnteteRaster refEntete = refCov.getEntete();
		refCov.dispose();
		refEntete.setNoDataValue(-1);

		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/temp/slope/test/data/bv.shp", entete, 1, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write("H:/temp/slope/test/data/bv.tif", data, entete);
		
	}
	
	private static void convertRGE(){
		String path = "G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/";
		String inputPath = "RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035/";
		String outputPath = "RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/";
		
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
		cov.dispose();
		
		CoverageManager.write(globalPath+outputPath+file.replace(".asc", ".tif"), data, entete);
		
	}
	
	// pour memoire
	private static void cleanDirection(){
		Coverage dirCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_direction.tif");
		EnteteRaster entete = dirCov.getEntete();
		float[] dirData = dirCov.getData();
		dirCov.dispose();
		
		for(int ind=0; ind<dirData.length; ind++){
			int vd = (int) dirData[ind];
			if(vd == 0){
				dirData[ind] = 9;
			}
		}
		
		float[] data = new float[dirData.length];

		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowSize(3);
		builder.setRasterTab(dirData);
		builder.setEntete(entete);
		builder.addMetric("Majority");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		for(int ind=0; ind<data.length; ind++){
			int vd = (int) data[ind];
			if(vd == 9){
				data[ind] = 0;
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_direction_clean_sq_3.tif", data, entete);
	}
	
	private static void recuperationDirection(){
		
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage dirCov = CoverageManager.getCoverage("H:/temp/slope/direction/");
		EnteteRaster entete = dirCov.getEntete();
		float[] data = dirCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		dirCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_direction.tif", data, bvEntete);
	}
	
	private static void recuperationIntensity(){
		
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage intCov = CoverageManager.getCoverage("H:/temp/slope/intensity/");
		EnteteRaster entete = intCov.getEntete();
		float[] data = intCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		intCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_intensity.tif", data, bvEntete);
	}
	
	private static void calculDistanceFromEauWithDirection(){
		
		Coverage eauCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_troncon_hydrographique.tif");
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_abattement.tif");
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		//Coverage dirCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_direction.tif");
		Coverage dirCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_direction_5.tif");
		float[] dirData = dirCov.getData();
		dirCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabInverseSlopeDirectionRCMDistanceAnalysis analysis = new TabInverseSlopeDirectionRCMDistanceAnalysis(data, eauData, abbData, dirData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write("H:/temp/slope/test/data/bv_distance_eau_direction_5.tif", data, entete);
	}

}
