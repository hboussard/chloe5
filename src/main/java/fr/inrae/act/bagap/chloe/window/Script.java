package fr.inrae.act.bagap.chloe.window;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.image.util.ImageUtilities;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;
import fr.inrae.act.bagap.raster.TileCoverage;

public class Script {

	public static void main(String[] args){

		scriptMultipleSelected();
	}
	
	private static void scriptMultipleSelected(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"pf_2018_10m.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		//builder.addMetric("NP");
		//builder.addMetric("LPI");
		//builder.addMetric("MPS");
		
		// analyse avec une seule taille de fenêtre
		builder.setWindowSize(31);
		
		// 1
		//builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf.csv");
		//builder.addCsvOutput(path+"selected/output/selected_pixels.csv");
		// les points sont sortis 
		// mais :
		// pb : ce ne sont pas les pixels
		
		// 2
		//builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf_ID.csv");
		//builder.addCsvOutput(path+"selected/output/selected_pixels_ID.csv");
		// les points sont exportés 
		// mais :
		// pb : ce ne sont pas les pixels
		// pb : les identifiants sont manquants
		
		// 3
		builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf.csv");
		builder.addCsvOutput(path+"selected/output/selected_points.csv");
		// les points demandés sont exportés 
		// mais :
		// pb : avec des identifiants non demandés
		
		// 4
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_decal.csv");
		//builder.addCsvOutput(path+"selected/output/selected_points_decal.csv");
		// les points décalés demandés sont exportés 
		// mais,
		// pb : avec des identifiants non demandés
		
		// 5
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID.csv");
		//builder.addCsvOutput(path+"selected/output/selected_points_ID.csv");
		// les points demandés sont exportés avec les identifiants demandés
		
		// 6
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID_decal.csv");
		//builder.addCsvOutput(path+"selected/output/selected_points_ID_decal.csv");
		// les points décalés demandés sont exportés avec les identifiants demandés
		
		// analyse avec plusieurs tailles de fenêtre
		//builder.setWindowSizes(new int[]{31,51});
		
		// 1
		//builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_pixels.csv");
			
		// 2
		//builder.setPixelsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/pixels_pf_ID.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_pixels_ID.csv");
			
		// 3
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_points.csv");
		
		// 4
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_decal.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_points_decal.csv");
		
		// 5
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_points_ID.csv");
			
		// 6
		//builder.setPointsFilter("C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/selected/points_pf_ID_decal.csv");
		//builder.addCsvOutput(path+"selected/output/combines/selected_points_ID_decal.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptMultipleSliding(){
		
		long begin = System.currentTimeMillis();
		
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"pf_2018_10m.tif");
		builder.addMetric("SHDI");
		builder.addMetric("HET");
		builder.addMetric("NP");
		builder.addMetric("LPI");
		builder.addMetric("MPS");
		//builder.setWindowSize(41);
		builder.setWindowSizes(new int[]{31,51});
		builder.setUnfilters(new int[]{-1});
		builder.setDisplacement(4);
		//builder.setInterpolation(true);
		builder.addGeoTiffOutput(31, "SHDI", path+"sliding_c5/shdi_31p.tif");
		builder.addGeoTiffOutput(31, "NP", path+"sliding_c5/np_31p.tif");
		builder.addGeoTiffOutput(51, "SHDI", path+"sliding_c5/shdi_51p.tif");
		builder.addGeoTiffOutput(51, "NP", path+"sliding_c5/np_51p.tif");
		builder.addCsvOutput(path+"sliding_c5/sliding_pf_2018.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptSelected(){
		
		String path = "G:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"za.tif");
		builder.setPointsFilter(path+"points_id.csv");
		builder.setWindowSize(21);
		builder.addMetric("SHDI");
		builder.addCsvOutput(path+"selected2/analyse.csv");
		builder.setWindowsPath(path+"selected2/filters/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptSlope(){
		
		long begin = System.currentTimeMillis();
		
		Tile tile = Tile.getTile("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		
		//String path = "H:/temp/slope/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.addMetric("slopedirection");
		builder.addMetric("slopeintensity");
		builder.setWindowSize(3);
		//builder.addGeoTiffOutput("slopedirection", path);
		builder.addTileGeoTiffOutput("slopedirection", "H:/temp/slope/direction/", tile);
		builder.addTileGeoTiffOutput("slopeintensity", "H:/temp/slope/intensity/", tile);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestErwan(){

		String path = "G:/chloe/winterschool/data/start/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setAnalysisType(WindowAnalysisType.SLIDING);
		builder.setAnalysisType(ChloeAnalysisType.GRID);
		builder.setRasterFile(path+"za.tif");
		builder.setWindowSize(201);
		//builder.setDisplacement(20);
		builder.addMetric("pNV_3");
		
		builder.addGeoTiffOutput("pNV_3", path+"erwan/prop_prairie_grid_500m.tif"); 
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
	
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestSliding(){

		String path = "G:/chloe/winterschool/data/start/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setAnalysisType(WindowAnalysisType.SLIDING);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		builder.setRasterFile(path+"za.tif");
		builder.setWindowSize(201);
		builder.setDisplacement(20);
		
		//builder.setROIX(100);
		//builder.setROIY(100);
		//builder.setROIWidth(1000);
		//builder.setROIHeight(1000);
		
		//builder.addMetric("SHDI");
		/*
		for(int i=1; i<12; i++){
			builder.addMetric("pNV_"+i);
			builder.addAsciiGridOutput("pNV_"+i, path+"nais/pnv_"+i+".asc"); 
			for(int j=1; j<12; j++){
				if(i<=j){
					builder.addMetric("pNC_"+i+"-"+j);
					builder.addAsciiGridOutput("pNC_"+i+"-"+j, path+"nais/pnv_"+i+"-"+j+".asc");
				}
			}
		}
		*/
		//builder.addMetric("NV_5");
		//builder.addMetric("NC_4-5");
		//builder.addMetric("pNC_4-5");
		
		//builder.addAsciiGridOutput("NV_5", path+"sliding/nv_5.asc"); 
		
		//builder.addMetric("SHDI");
		//builder.addAsciiGridOutput("SHDI", path+"fast/sshdi.asc"); 
		
		builder.addMetric("average");
		builder.addAsciiGridOutput("average", path+"fast/fsaverage.asc"); 
		
		//builder.addMetric("HET-frag");
		//builder.addAsciiGridOutput("HET-frag", path+"sliding/mhet_201p.asc"); 
		
		//builder.addMetric("Central");
		//builder.addAsciiGridOutput("Central", path+"fast/fcentral_201p_dep20.asc"); 
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
	
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestTileCoverageZA(){
		
		String path = "H:/temp/tile_coverage/";
		
		//Coverage coverage = CoverageManager.getCoverage(path+"mean/");
		//Coverage coverage = CoverageManager.getCoverage(path+"little/");
		Coverage coverage = CoverageManager.getCoverage(path+"very_little/");
		Tile tile = Tile.getTile((TileCoverage) coverage);
		System.out.println(tile);
	
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setRasterFile("G:/chloe/winterschool/data/start/za.tif");
		builder.setRasterFile("G:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		builder.setWindowSize(501);
		builder.setDisplacement(40);
		
		//builder.setUnfilters(new int[]{-1});
		
		/*
		builder.setROIX(1000);
		builder.setROIY(1000);
		builder.setROIWidth(5000);
		builder.setROIHeight(5000);
		*/
		
		builder.addMetric("Majority");
		builder.addTileGeoTiffOutput("Majority", path+"majority_501p/", tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
	
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestTileCoverage(){
		
		String path = "H:/temp/tile_coverage/";
		
		Coverage coverage = CoverageManager.getCoverage(path+"mean/");
		//Coverage coverage = CoverageManager.getCoverage(path+"little/");
		Tile tile = Tile.getTile((TileCoverage) coverage);
		System.out.println(tile);
	
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setCoverage(coverage);
		builder.setWindowSize(21);
		//builder.setDisplacement(20);
		
		builder.setUnfilters(new int[]{-1});
		
		/*
		builder.setROIX(1000);
		builder.setROIY(1000);
		builder.setROIWidth(5000);
		builder.setROIHeight(5000);
		*/
		
		builder.addMetric("average");
		//builder.addAsciiGridOutput("average", path+"zone_average_201p_d20.asc"); 
		builder.addTileAsciiGridOutput("average", path+"average/", tile);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		long begin = System.currentTimeMillis();
	
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestGenerateTileCoverage(){
		
		System.out.println("importation des tuiles");
		Coverage cov = CoverageManager.getCoverage("H:/temp/tile_coverage/little/");
		
		//System.out.println("recuperation des donnees");
		//float[] datas = cov.getDatas();
		
		//System.out.println("recuperation de l'entete");
		//EnteteRaster entete = cov.getEntete();
		
		//System.out.println("exportation du raster");
		//CoverageManager.writeGeotiff("H:/temp/tile_coverage/test_35.tif", datas, entete);
		
		/*
		Rectangle localRoi = new Rectangle(500, 2000, 2000, 2000);
		
		System.out.println("recuperation des donnees locales");
		float[] roiDatas = cov.getDatas(localRoi);
		
		System.out.println("recuperation de l'entete locale");
		EnteteRaster roiEntete = EnteteRaster.getEntete(cov.getEntete(), localRoi);
		
		System.out.println("exportation du raster local");
		CoverageManager.writeGeotiff("H:/temp/tile_coverage/test_35_local.tif", roiDatas, roiEntete);
		*/
		
		Rectangle localRoi = new Rectangle(500, 1500, 2000, 2000);
		
		System.out.println("recuperation des donnees locales");
		float[] roiDatas = cov.getData(localRoi);
		
		System.out.println("recuperation de l'entete locale");
		EnteteRaster roiEntete = EnteteRaster.getEntete(cov.getEntete(), localRoi);
		
		System.out.println("exportation du raster local");
		CoverageManager.writeGeotiff("H:/temp/tile_coverage/test_35_local2.tif", roiDatas, roiEntete);
	}
	
	private static void scriptTestEntity(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"za.tif");
		builder.setEntityRasterFile(path+"communes_za.asc");
		
		builder.addMetric("SHDI");
		//builder.addMetric("NV_5");
		//builder.addMetric("NC_4-5");
		//builder.addMetric("pNC_4-5");
		
		//builder.addAsciiGridOutput("pNV_1", path+"area/pnv1.asc"); // marche pas...
		
		builder.setAsciiGridFolderOutput(path+"communes/");
		//builder.addAsciiGridFolderOutput(path+"communes/double/");
		//builder.addCsvOutput(path+"area/analyse_communes_za.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestMinimumValidValue(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za_2018.asc");
		builder.setWindowSize(51);
		builder.setDisplacement(5);
		builder.setMinRate(100);
		builder.addMetric("SHDI");
		builder.addAsciiGridOutput("SHDI", path+"valid/shdi_3.asc"); 
	
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestOutput(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.setWindowSize(201);
		builder.setDisplacement(20);
		
		for(int i=1; i<=12; i++){
			builder.addMetric("pNV_"+i);
			for(int j=1; j<=12; j++){
				if(i<j){
					builder.addMetric("pNC_"+i+"-"+j);
				}
			}
		}
		//builder.addCsvOutput(path+"output/analyse_za_5km.csv");
		//builder.addAsciiGridFolderOutput(path+"output2");
		builder.setGeoTiffFolderOutput(path+"output4/");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestEcopaysage(){

		String path = "G:/chloe/winterschool/data/start/";
		//String path = "G:/data/sig/bretagne/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setRasterFile(path+"za.tif");
		//builder.setRasterFile(path+"Bretagne_2019_dispositif_bocage_ebr.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.setWindowSize(201);
		builder.setDisplacement(20);
		
		//builder.setUnfilters(new int[]{7}); // pas la mer
		
		for(int i=1; i<=12; i++){
			builder.addMetric("pNV_"+i);
			for(int j=1; j<=12; j++){
				if(i<j){
					builder.addMetric("pNC_"+i+"-"+j);
				}
			}
		}
		builder.addCsvOutput(path+"ecopaysage/analyse_za_5km.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestQuantitative(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setAnalysisType(WindowAnalysisType.SLIDING);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowShapeType(WindowShapeType.SQUARE);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		builder.setRasterFile(path+"fast/cshdi_201p_dep20.asc");
		builder.setWindowSize(21);
		/*
		builder.setROIX(100);
		builder.setROIY(100);
		builder.setROIWidth(1000);
		builder.setROIHeight(1000);
		*/
		
		builder.addMetric("average");
		builder.addAsciiGridOutput("average", path+"fast/fgaverage_21p.asc"); 
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestSelected(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setPointsFilter("G:/chloe/winterschool/data/start/selected/points.csv");
		builder.setRasterFile(path+"za.tif");
		builder.setWindowSize(101);
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.addMetric("SHDI");
		builder.addCsvOutput("G:/chloe/winterschool/data/start/selected/new/test5.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestWithoutShape(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setRasterFile(path+"za.tif");
		builder.setWindowSize(21);
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
	
		builder.addMetric("SHDI");
		
		builder.addAsciiGridOutput("SHDI", path+"without_shape/without_shdi.asc"); 
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestNais(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setRasterFile(path+"za.tif");
		builder.setWindowSize(201);
		builder.setWindowSize(401);
		builder.setDisplacement(20);
		
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.addMetric("SHEI");

		builder.addAsciiGridOutput(201, "SHEI", path+"diversity/shei_201.asc");
		builder.addAsciiGridOutput(401, "SHEI", path+"diversity/shei_401.asc");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestMap(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.setROIX(0);
		builder.setROIY(0);
		builder.setROIWidth(21);
		builder.setROIHeight(21);
		
		//builder.addMetric("SHDI");
		builder.addMetric("NV_5");
		//builder.addMetric("pNV_5");
		//builder.addMetric("NC_4-5");
		//builder.addMetric("pNC_4-5");
		//builder.addMetric("average");
		//builder.addMetric("NP");
		//builder.addMetric("MPS");
		//builder.addMetric("LPI");
		
		builder.addCsvOutput(path+"map/analyse.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestGrid(){

		String path = "G:/chloe/winterschool/data/start/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		
		builder.setAnalysisType(ChloeAnalysisType.GRID);
		builder.setWindowSize(10);
		/*
		builder.setAnalysisType(WindowAnalysisType.SLIDING);
		builder.setWindowSize(11);
		*/
		builder.setROIX(100);
		builder.setROIY(100);
		builder.setROIWidth(1000);
		builder.setROIHeight(1000);
		
		builder.addMetric("NV_1");
		builder.addAsciiGridOutput("NV_1", path+"grid/huge_roi_nv1_grid_10p.asc");
		//builder.addAsciiGridOutput("NV_1", path+"grid/huge_roi_nv1_sliding_11p.asc");
		
		//builder.addMetric("SHDI");
		//builder.addAsciiGridOutput("SHDI", path+"grid/roi_shdi_10p.asc");
		
		//builder.addMetric("HET-frag");
		//builder.addAsciiGridOutput("HET-frag", path+"grid/roi_het_grid_10p.asc");
		//builder.addAsciiGridOutput("SHDI", path+"grid/shdi_sliding_201p.asc");
		
		//builder.addMetric("NP");
		//builder.addAsciiGridOutput("NP", path+"grid/np_grid_11p.asc");
		
		//builder.addMetric("average");
		//builder.addAsciiGridOutput("average", path+"grid/average_grid_11p.asc");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestComptageValueAndCouple(){
		
		long begin = System.currentTimeMillis();
		
		String path = "G:/chloe/winterschool/data/start/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.setWindowSize(101);
		builder.setDisplacement(20);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		
		//builder.addMetric("pNV_1");
		//builder.addAsciiGridOutput("pNV_1", path+"test3/pNV_1_21p_dep20.asc");
		
		builder.addMetric("HET-frag");
		builder.addAsciiGridOutput("HET-frag", path+"test2/HET-frag_201p_dep20.asc");
		
		//builder.addMetric("AI");
		//builder.addAsciiGridOutput("AI", path+"test3/ai_201p_dep204.asc");
		
		//builder.setRasterFile2(path+"test3/friction2.asc");
		//builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
	
		
		//builder.addAsciiGridOutput("pNV_1", path+"test3/pNV_1_201p_dep20_functional2.asc");
	
		//builder.addMetric("N-valid");
		//builder.addAsciiGridOutput("N-valid", path+"test3/N-valid_201p_dep20_circle.asc");
		//builder.addAsciiGridOutput("N-valid", path+"test3/N-valid_201p_dep20_functional.asc");
		//builder.addAsciiGridOutput("N-valid", path+"test3/N-valid_101p_dep10_functional_gaussian2.asc");
		
		/*
		builder.addMetric("AI");
		builder.addAsciiGridOutput("AI", path+"test2/ai_201p_dep20.asc");
		
		builder.addMetric("HET-frag");
		builder.addAsciiGridOutput("HET-frag", path+"test2/HET-frag_201p_dep20.asc");
		*/
		/*
		builder.addMetric("LPI");
		builder.addAsciiGridOutput("LPI", path+"test2/lpi_201p_dep20.asc");
		
		builder.addMetric("MPS");
		builder.addAsciiGridOutput("MPS", path+"test2/mps_201p_dep20.asc");
		*/
		/*
		 * builder.addMetric("pNV_2");
		builder.addAsciiGridOutput("pNV_2", path+"test2/pNV_2_201p_dep20.asc");
		builder.addMetric("pNV_3");
		builder.addAsciiGridOutput("pNV_3", path+"test2/pNV_3_201p_dep20.asc");
		builder.addMetric("pNV_4");
		builder.addAsciiGridOutput("pNV_4", path+"test2/pNV_4_201p_dep20.asc");
		builder.addMetric("pNV_5");
		builder.addAsciiGridOutput("pNV_5", path+"test2/pNV_5_201p_dep20.asc");
		builder.addMetric("pNV_6");
		builder.addAsciiGridOutput("pNV_6", path+"test2/pNV_6_201p_dep20.asc");
		builder.addMetric("pNV_7");
		builder.addAsciiGridOutput("pNV_7", path+"test2/pNV_7_201p_dep20.asc");
		builder.addMetric("pNV_8");
		builder.addAsciiGridOutput("pNV_8", path+"test2/pNV_8_201p_dep20.asc");
		builder.addMetric("pNV_9");
		builder.addAsciiGridOutput("pNV_9", path+"test2/pNV_9_201p_dep20.asc");
		builder.addMetric("pNV_10");
		builder.addAsciiGridOutput("pNV_10", path+"test2/pNV_10_201p_dep20.asc");
		builder.addMetric("pNV_11");
		builder.addAsciiGridOutput("pNV_11", path+"test2/pNV_11_201p_dep20.asc");
		builder.addMetric("pNV_12");
		builder.addAsciiGridOutput("pNV_12", path+"test2/pNV_12_201p_dep20.asc");
		 */
		
		//builder.addMetric("SHDI");
		//builder.addAsciiGridOutput("SHDI", path+"test/shdi_201p_dep20.asc");
		//builder.addMetric("HET");
		//builder.addAsciiGridOutput("HET", path+"test/het2.asc");
		//builder.addMetric("AI_4");
		//builder.addAsciiGridOutput("AI_4", path+"test/ai_4.asc");
		//builder.addMetric("AI-class_2");
		//builder.addAsciiGridOutput("AI-class_2", path+"test/ai_2_dep20.asc");
		//builder.addMetric("AIm_1&2");
		//builder.addAsciiGridOutput("AIm_1&2", path+"test/ai_culture_201p_dep20.asc");
		//builder.addMetric("AI2m_1&2");
		//builder.addAsciiGridOutput("AI2m_1&2", path+"test/ai2_culture_201p_dep20.asc");
		//builder.addMetric("pNVm_1&2");
		//builder.addAsciiGridOutput("pNVm_1&2", path+"test/pNV_culture_201p_dep20.asc");
		//builder.addMetric("AIm_4&5&6");
		//builder.addAsciiGridOutput("AIm_4&5&6", path+"test/ai_boisement_201p_dep20.asc");
		//builder.addMetric("AI2m_4&5&6");
		//builder.addAsciiGridOutput("AI2m_4&5&6", path+"test/ai2_boisement_201p_dep20.asc");
		//builder.addMetric("SHDI");
		//builder.addAsciiGridOutput("SHDI", path+"test/shdi_dep20.asc");
		//builder.addMetric("LPI");
		//builder.addAsciiGridOutput("LPI", path+"test/lpi_201p_dep20.asc");
		//builder.addMetric("MPS");
		//builder.addAsciiGridOutput("MPS", path+"test/mps_201p_dep20.asc");
		//builder.addMetric("NP");
		//builder.addAsciiGridOutput("np", path+"test/np_201p_dep20.asc");
		//builder.addMetric("MPS-class_4");
		//builder.addAsciiGridOutput("MPS-class_4", path+"test/mps_haie_201p_dep20.asc");
		//builder.addMetric("NP-class_4");
		//builder.addAsciiGridOutput("NP-class_4", path+"test/np_haie_201p_dep20.asc");
		//builder.addMetric("LPI-class_4");
		//builder.addAsciiGridOutput("LPI-class_4", path+"test/lpi_haie_201p_dep20.asc");
		//builder.addMetric("EMS");
		//builder.addAsciiGridOutput("EMS", path+"test/ems2_201p_dep20.asc");
		//builder.addMetric("EMS-class_4");
		//builder.addAsciiGridOutput("EMS-class_4", path+"test/ems_haie_201p_dep20.asc");
		//builder.addMetric("pNV_4");
		//builder.addAsciiGridOutput("pNV_4", path+"test/prop2_haie_201p_dep20.asc");
		//builder.addMetric("AI");
		//builder.addAsciiGridOutput("AI", path+"test/ai_201p_dep20.asc");
		//builder.setWindowSize(3);
		//builder.addMetric("minimum");
		//builder.addAsciiGridOutput("minimum", path+"test/minimum_3p.asc");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEcopaysageAmazonie(){
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(9);
		codes.add(11);
		codes.add(12);
		codes.add(15);
		codes.add(20);
		codes.add(21);
		codes.add(23);
		codes.add(24);
		codes.add(25);
		codes.add(30);
		codes.add(32);
		codes.add(33);
		codes.add(39);
		codes.add(41);
		codes.add(48);
		codes.add(63);
		
		//scriptEcopaysageAmazonie("composition", "5km", codes);
		//scriptEcopaysageAmazonie("configuration", "5km", codes);
		
		//scriptEcopaysageAmazonie("composition", "10km", codes);
		//scriptEcopaysageAmazonie("configuration", "10km", codes);
		
		scriptEcopaysageAmazonie("composition", "25km", codes);
		//scriptEcopaysageAmazonie("configuration", "25km", codes);
		
		//scriptEcopaysageAmazonie("composition", "50km", codes);
		//scriptEcopaysageAmazonie("configuration", "50km", codes);
	}
	
	private static void scriptEcopaysageAmazonie(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "H:/amazonie/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"data/mapbiomas-brazil-collection-70-amazonia-2019.tif");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classees
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		//builder.addMetric("HET-frag");
		
		if(scale.equalsIgnoreCase("5km")){
			builder.setWindowSize(333); // 5km, 333 = diametre en pixels, doit etre impaire
			builder.setDisplacement(33); // 10% de la taille de la fenetre
		}
		if(scale.equalsIgnoreCase("10km")){
			builder.setWindowSize(667); // 10km
			builder.setDisplacement(66);
		}
		if(scale.equalsIgnoreCase("25km")){
			builder.setWindowSize(1667); // 25km
			builder.setDisplacement(160);
		}
		if(scale.equalsIgnoreCase("50km")){
			builder.setWindowSize(3333); // 50km
			builder.setDisplacement(330);
		}
		
		builder.setUnfilters(new int[]{0}); // on filtre pour ignorer le calcul sur les fenetres dont le pixel central est "0"
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED); // fenetre circulaire gaussienne
		builder.addCsvOutput(path+"analyse/test_amazonie_"+compo_config+"_"+scale+".csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptAmazonieTest(){
		long begin = System.currentTimeMillis();
	
		String path = "H:/amazonie/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"mapbiomas-brazil-collection-70-amazonia-2019.tif");
		builder.setValues("3, 4, 5, 9, 11, 12, 15, 20, 21, 23, 24 ,25, 30, 32, 33, 39, 41, 48, 63"); // doivent etre classees
		builder.addMetric("SHDI");
		builder.setWindowSize(333);
		builder.setDisplacement(30);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addAsciiGridOutput("SHDI", path+"amazonie_shdi.asc");
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestMPS(){
		
		long begin = System.currentTimeMillis();
		
		String path = "G:/PREPARE/toulouse/model/test_MPS/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"s1_cover_2020.asc");
		builder.addMetric("Central");
		builder.addMetric("pCentral");
		builder.addMetric("pNV_6");
		builder.addMetric("N-valid");
		builder.addMetric("pNC_6-24");
		builder.setDisplacement(10);
		builder.setWindowSize(401);
		builder.addAsciiGridOutput("Central", path+"chloe5/data.asc");
		builder.addAsciiGridOutput("pCentral", path+"chloe5/data1.asc");
		builder.addAsciiGridOutput("pNV_6", path+"chloe5/data2.asc");
		builder.addAsciiGridOutput("N-valid", path+"chloe5/data3.asc");
		builder.addAsciiGridOutput("pNC_6-24", path+"chloe5/data4.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		MatrixManager.visualize(path+"chloe5/data.asc");
		MatrixManager.visualize(path+"chloe5/data1.asc");
		MatrixManager.visualize(path+"chloe5/data2.asc");
		MatrixManager.visualize(path+"chloe5/data3.asc");
		MatrixManager.visualize(path+"chloe5/data4.asc");
	}

	private static void test2(){
		
		GridCoverage2D cov = CoverageManager.get("F:/IGN/35-2020-0320-6785-LA93-5M-MNHC/35-2020-0320-6785-LA93-5M-MNHC.tif");
		float[] tileDatas = CoverageManager.getData(cov, 0, 0, 1000, 1000);
		cov.dispose(true);
		ImageUtilities.disposePlanarImageChain((PlanarImage) cov.getRenderedImage()); // relache les ressources
		
		/*
		String path = "F:/data/sig/IGN/35_2020/";
		String prefix = "prepaGrain";
		String code = "20FD3525"; 
		int tileSize = 1000;
		GridCoverage2D cov;
		float[] tileDatas;
		
		File deptFolder = new File(path);
		int it, jt;
		int iMin = Integer.MAX_VALUE;
		int iMax = 0;
		int jMin = Integer.MAX_VALUE;
		int jMax = 0;
		for(String file : deptFolder.list()){
			if(file.startsWith(prefix+"_"+code) && file.endsWith(".tif")){
				String[] f = file.replace(prefix+"_"+code+"_", "").replace(".tif", "").split("_");
				it = Integer.parseInt(f[0]);
				jt = Integer.parseInt(f[1]);
				System.out.println(it+" "+jt);
				
				// mise a jur emprise
				iMin = Math.min(iMin, it);
				iMax = Math.max(iMax, it);
				jMin = Math.min(jMin, jt);
				jMax = Math.max(jMax, jt);
				
				//float[] data = getData(it, jt);	
				
				//test
				cov = CoverageManager.get(path+file);
				tileDatas = CoverageManager.getData(cov, 0, 0, tileSize, tileSize);
				cov.dispose(true);
				ImageUtilities.disposePlanarImageChain((PlanarImage) cov.getRenderedImage()); // relache les ressources
			}
		}
		System.out.println(iMin+" "+iMax+" "+jMin+" "+jMax);
		*/
	}
	
 	private static void propClassifGrainPaysdelaLoire(){
		long begin = System.currentTimeMillis();
		
		String path = "F:/FRC_Pays_de_la_Loire/data/BocagePdlL_V3/test2/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"grain_PdlL_classif.asc");
		builder.addMetric("pNV_1");
		builder.setWindowSize(401);
		builder.setDisplacement(40);
		builder.addAsciiGridOutput("pNV_1", path+"prop_grain_10km.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptSHDIClusterGrainPaysdelaLoire(){
		long begin = System.currentTimeMillis();
		
		String path = "F:/FRC_Pays_de_la_Loire/data/BocagePdlL_V3/test2/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"cluster_grain.asc");
		builder.addMetric("SHDI");
		builder.setWindowSize(401);
		builder.setDisplacement(40);
		builder.addAsciiGridOutput("SHDI", path+"shdi_10km.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainPaysdelaLoire(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/FRC_Pays_de_la_Loire/data/BocagePdlL_V3/test2/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"distance_bois_2021_2.tif");
		builder.addMetric("MD");
		builder.setWindowSize(101); 
		builder.setDisplacement(10);
		builder.addAsciiGridOutput("MD", path+"grain_pays_loire_101p_dep10_2.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainGers(){
		String path = "F:/gers/data/wetransfer_overlay_dist-1-2-asc_2022-03-03_1538/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"overlay_dist-[1, 2].asc");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", path+"grain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void boisementBaieLancieux(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F://dreal/ophelie/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"cgtv_baie_lancieux.asc");
		builder.addMetric("pNMV_12-13-14-17-18-19");
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setWindowSize(41);
		builder.addAsciiGridOutput("pNMV_12-13-14-17-18-19", path+"boisement.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void luminanceBaieLancieux(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F://dreal/ophelie/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"cgtv_baie_lancieux.asc");
		//builder.addMetric("pNV_25");
		//builder.addMetric("pNV_26");
		builder.addMetric("pNMV_25-26");
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setWindowSize(41);
		//builder.addAsciiGridOutput("pNV_25", path+"eprop_bati_100m.asc");
		//builder.addAsciiGridOutput("pNV_26", path+"eprop_route_100m.asc");
		builder.addAsciiGridOutput("pNMV_25-26", path+"elumi_100m.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}

	private static void retileBaieLancieux(){
		// selection site
	
		double minx = 306091.0;
		double maxx = 328852.0;
		double miny = 6831235.0;
		double maxy = 6853026.0;
				
		String fileCGTV = "F://data/sig/CGTV/cgtv.tif";
		String fileBaie = "F://dreal/ophelie/cgtv_baie_lancieux.asc";
		
		CoverageManager.retile(fileCGTV, fileBaie, minx, maxx, miny, maxy);
	}
	
 	private static void scriptEcopaysageBretagneCGTV(){
		
		Raster.setNoDataValue(255);
		
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(6);
		codes.add(7);
		codes.add(8);
		codes.add(9);
		codes.add(10);
		codes.add(11);
		codes.add(12);
		codes.add(13);
		codes.add(14);
		codes.add(15);
		codes.add(16);
		codes.add(17);
		codes.add(18);
		codes.add(19);
		codes.add(20);
		codes.add(21);
		codes.add(22);
		codes.add(23);
		codes.add(24);
		codes.add(25);
		codes.add(26);
		codes.add(27);
		
		//scriptEcopaysageBretagneCGTV("composition", "500m", codes);
		//scriptEcopaysageBretagneCGTV("configuration", "500m", codes);
		scriptEcopaysageBretagneCGTV("composition", "3km", codes);
		scriptEcopaysageBretagneCGTV("configuration", "3km", codes);
	}
	
	private static void scriptEcopaysageBretagneCGTV(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "F:/dreal/ecopaysage/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("F:/data/sig/CGTV/cgtv.tif");
		//builder.setRasterFile("F:/dreal/ophelie/cgtv_baie_lancieux.asc");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classÃ©es
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(401); // 500m
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(2401); // 3km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setUnfilters(new int[]{23,255});
		builder.addCsvOutput(path+"analyse2/bretagne_cgtv_"+compo_config+"_"+scale+".csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}

	private static void scriptGrainEloise(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/Eloise/data/analyse/distance/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"haie_large_clean_dist-[99].asc");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", path+"grain_101.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestDistanceIGN5(){
		String path = "F:/IGN/35-2020-0320-6785-LA93-5M-MNHC/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"35-2020-0320-6785-LA93-5M-MNHC.tif");
		builder.setRasterFile2(path+"local_wood.asc");
		builder.addMetric("distance");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("distance", path+"test_ign4.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestDistanceIGN4(){
		String path = "F:/IGN/35-2020-0320-6785-LA93-5M-MNHC/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"35-2020-0320-6785-LA93-5M-MNHC.tif");
		builder.addMetric("standard_deviation");
		builder.setWindowSize(7);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addAsciiGridOutput("standard_deviation", path+"standard_deviation.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		//MatrixManager.visualize(path+"local_wood.asc");
	}
	
	private static void scriptTestDistanceIGN3(){
		String path = "F:/IGN/35-2020-0320-6785-LA93-5M-MNHC/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"35-2020-0320-6785-LA93-5M-MNHC.tif");
		builder.addMetric("bocage");
		builder.setWindowSize(13);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addAsciiGridOutput("bocage", path+"local_wood.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		//MatrixManager.visualize(path+"local_wood.asc");
	}
	
	private static void scriptTestDistanceIGN2(){
		String path = "F:/IGN/35-2020-0320-6785-LA93-5M-MNHC/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"35-2020-0320-6785-LA93-5M-MNHC_max.tif");
		builder.addMetric("distance");
		builder.setWindowSize(41);
		builder.addAsciiGridOutput("distance", path+"test_ign2.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		//MatrixManager.visualize(path+"test_ign2.asc");
	}
	
	private static void scriptTestDistanceIGN(){
		String path = "C:/Hugues/data/ascii/aparapi/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRasterFile(path+"data_test_aparapi.asc");
		builder.setRasterFile(path+"wood.asc");
		builder.addMetric("distance");
		builder.setWindowSize(51);
		builder.addAsciiGridOutput("distance", path+"result_test_aparapi_2.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		MatrixManager.visualize(path+"result_test_aparapi_2.asc");
	}
	
	private static void scriptTestDistanceFunction(){
		String path = "C:/Hugues/data/ascii/qualitative-no-data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"raster2007.asc");
		builder.setValues("1, 2, 3"); // doivent etre classees
		builder.addMetric("pNV_1");
		builder.setWindowSize(51);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.setWindowDistanceFunction("1");
		//builder.setWindowDistanceFunction("exp(-pow(distance, 2)/pow(dmax/2, 2))");
		//builder.setWindowDistanceFunction("distance*(-1/dmax)+1");
		//builder.setWindowDistanceFunction("if(distance<(dmax/2)){0.0+(exp(-pow((distance-(0))/(dmax/4.0), 2)))*(-1.0-0.0)}else{0.0+(exp(-pow((distance-((3*dmax)/4))/(dmax/4.0), 2)))*(1.0-0.0)}");
		builder.addAsciiGridOutput("pNV_1", path+"distance/pNV_1_dyn-4.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	
		MatrixManager.visualize(path+"distance/pNV_1_dyn-1.asc");
		MatrixManager.visualize(path+"distance/pNV_1_dyn-2.asc");
		MatrixManager.visualize(path+"distance/pNV_1_dyn-3.asc");
		MatrixManager.visualize(path+"distance/pNV_1_dyn-4.asc");
	}
	
	private static void scriptGrainBasLeon(){
		String path = "F:/bas_leon/data2/grain/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"bas_leon_distance_bois.asc");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", path+"bas_leon_grain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEcopaysageDordogneRPG2017(){
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(6);
		codes.add(7);
		codes.add(8);
		codes.add(11);
		codes.add(18);
		codes.add(19);
		codes.add(21);
		codes.add(28);
		codes.add(30);
		codes.add(33);
		
		scriptEcopaysageDordogneRPG2017("composition", "275m", codes);
		scriptEcopaysageDordogneRPG2017("configuration", "275m", codes);
		scriptEcopaysageDordogneRPG2017("composition", "1-5km", codes);
		scriptEcopaysageDordogneRPG2017("configuration", "1-5km", codes);
	}
	
	private static void scriptEcopaysageDordogneRPG2017(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "F:/aquitaine/dordogne/socio_ecosysteme_spatial/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"carto/dordogne_carto_2017.asc");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classÃ©es
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("275m")){
			builder.setWindowSize(221); // 275m
		}
		if(scale.equalsIgnoreCase("1-5km")){
			builder.setWindowSize(1201); // 1.5km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(path+"analyse/dordogne_rpg_2017_"+compo_config+"_"+scale+"_dep40.csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptLocalWoodOFBZA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/OFB/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"ocsol_terrain.asc");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowSize(3);
		builder.addAsciiGridOutput("pNV_4", path+"local_haie_3p.asc");
		builder.addAsciiGridOutput("pNV_5", path+"local_bois_3p.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptPropPrairieLocalOFBZA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/OFB/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"cgtv_terrain.asc");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 255"); 
		builder.addMetric("pNV_5");
		builder.addMetric("pNV_6");
		builder.setWindowSize(5);
		builder.addAsciiGridOutput("pNV_5", path+"prop_pp_5p.asc");
		builder.addAsciiGridOutput("pNV_6", path+"prop_pph_5p.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptPropPrairieOFBZA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/OFB/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"ocsol_terrain.asc");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNV_3");
		builder.setWindowSize(201);
		builder.addAsciiGridOutput("pNV_3", path+"prop_prairie_201.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptPropBoisOFBZA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/OFB/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"ocsol_terrain.asc");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.setWindowSize(21);
		builder.addAsciiGridOutput("pNV_4", path+"prop_haie_21.asc");
		builder.addAsciiGridOutput("pNV_5", path+"prop_bois_21.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainOFBZA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/OFB/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"distance_elt_bois.asc");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", path+"grain_101.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptPropPrairieTerrainEcofriche2(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/ecofriche2/continuites/terrain/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"ocsol_terrain.tif");
		builder.addMetric("pNV_3");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.setWindowSize(201);
		builder.addAsciiGridOutput("pNV_3", path+"prop_prairie_201p_terrain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainTerrainEcofriche2(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/ecofriche2/continuites/terrain/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"distance_elts_boises.asc");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", path+"grain_terrain_101.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptStandardDeviationMNTPREPAREToulouse(){
		String path = "F:/PREPARE/toulouse/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"mnt_standard_deviation_5p.asc");
		builder.setEntityRasterFile(path+"parcellaire_toulouse_5m.asc");
		//builder.addMetric("average");
		builder.addMetric("maximum");
		//builder.addAsciiGridFolderOutput(path+"mnt_standard_deviation/");
		//builder.addCsvOutput(path+"mnt_standard_deviation/parcelles_standard_deviation_mnt.csv");
		builder.addCsvOutput(path+"mnt_standard_deviation/parcelles_max_slope_mnt.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptStandardDeviationMNTPREPAREToulouseBis(){
		String path = "F:/PREPARE/toulouse/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"T01alti25m.tif");
		builder.setEntityRasterFile(path+"parcellaire_toulouse.asc");
		builder.addMetric("standard_deviation");
		builder.setAsciiGridFolderOutput(path+"mnt_standard_deviation/");
		builder.addCsvOutput(path+"mnt_standard_deviation/parcelles_standard_deviation_mnt_bis.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptAverageMNTPREPAREToulouse(){
		String path = "F:/PREPARE/toulouse/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"MNT5mT01.tif");
		builder.setEntityRasterFile(path+"parcellaire_toulouse_5m.asc");
		builder.addMetric("average");
		//builder.addAsciiGridFolderOutput(path+"mnt_5m_average/");
		builder.addCsvOutput(path+"mnt_5m_average/parcelles_average_mnt.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptMNTPREPAREToulouse(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/PREPARE/toulouse/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"MNT5mT01.asc");
		builder.addMetric("standard_deviation");
		//builder.addMetric("average");
		builder.setWindowSize(7);
		builder.addAsciiGridOutput("standard_deviation", path+"mnt_standard_deviation_7p.asc");
		//builder.addAsciiGridOutput("average", path+"mnt_average_7p.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofriche(){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"occsol_sitesecofriche1.tif");
		builder.setEntityRasterFile(path+"unites_gestion_sites_ecofriche1.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); 
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.addMetric("pNV_6");
		builder.setAsciiGridFolderOutput(path+"prop/");
		builder.addCsvOutput(path+"entites_composition_bis.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofricheRPG(){
		//scriptEntitesGestionEcofricheRPGComposition("0719");
		//scriptEntitesGestionEcofricheRPGComposition("1419");
		//scriptEntitesGestionEcofricheRPGConfiguration("0719");
		//scriptEntitesGestionEcofricheRPGConfiguration("1419");
		
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("0719");
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("1419");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("0719");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("1419");
		
		//scriptEntitesGestionEcofricheRPGComposition("rpg2009R");
		//scriptEntitesGestionEcofricheRPGConfiguration("rpg2009R");
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("rpg2009R");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("rpg2009R");
		scriptEntitesGestionEcofricheRPGCompositionBaseCGTVZoneHumide("rpg2009R");
		
		//scriptEntitesGestionEcofricheRPGComposition("rpg2009buffR");
		//scriptEntitesGestionEcofricheRPGConfiguration("rpg2009buffR");
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("rpg2009buffR");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("rpg2009buffR");
		scriptEntitesGestionEcofricheRPGCompositionBaseCGTVZoneHumide("rpg2009buffR");
		
		//scriptEntitesGestionEcofricheRPGComposition("rpg2014R");
		//scriptEntitesGestionEcofricheRPGConfiguration("rpg2014R");
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("rpg2014R");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("rpg2014R");
		scriptEntitesGestionEcofricheRPGCompositionBaseCGTVZoneHumide("rpg2014R");
		
		//scriptEntitesGestionEcofricheRPGComposition("rpg2014buffR");
		//scriptEntitesGestionEcofricheRPGConfiguration("rpg2014buffR");
		//scriptEntitesGestionEcofricheRPGCompositionBaseCGTV("rpg2014buffR");
		//scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV("rpg2014buffR");
		scriptEntitesGestionEcofricheRPGCompositionBaseCGTVZoneHumide("rpg2014buffR");

	}
	
	private static void scriptEntitesGestionEcofricheRPGCompositionBaseCGTVZoneHumide(String period){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv_humide.tif");
		builder.setEntityRasterFile(path+"rpg/select/"+period+".tif");
		builder.setValues("1, 2"); 
		builder.addMetric("pNV_1");
		builder.addMetric("N-valid");
		//builder.addAsciiGridFolderOutput(path+"prop/");
		builder.addCsvOutput(path+"entites_"+period+"_composition_cgtv_humide.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofricheRPGCompositionBaseCGTV(String period){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv.tif");
		builder.setEntityRasterFile(path+"rpg/select/"+period+".tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27"); 
		for(int v : builder.getValues()){
			builder.addMetric("pNV_"+v);
			//builder.addMetric("NV_"+v);
		}
		builder.addMetric("N-valid");
		//builder.addAsciiGridFolderOutput(path+"prop/");
		builder.addCsvOutput(path+"entites_"+period+"_composition_cgtv.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofricheRPGConfigurationBaseCGTV(String period){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv.tif");
		builder.setEntityRasterFile(path+"rpg/select/"+period+".tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27"); 
		
		for(int v1 : builder.getValues()){
			for(int v2 : builder.getValues()){
				if(v1<=v2){
					builder.addMetric("pNC_"+v1+"-"+v2);
					//builder.addMetric("NC_"+v1+"-"+v2);
				}
			}
		}
		builder.addMetric("NC-valid");
		builder.addCsvOutput(path+"entites_"+period+"_configuration_cgtv.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofricheRPGComposition(String period){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		builder.setEntityRasterFile(path+"rpg/select/"+period+".tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); 
		for(int v : builder.getValues()){
			builder.addMetric("pNV_"+v);
			//builder.addMetric("NV_"+v);
		}
		builder.addMetric("N-valid");
		//builder.addAsciiGridFolderOutput(path+"prop/");
		builder.addCsvOutput(path+"entites_"+period+"_composition_occsol.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEntitesGestionEcofricheRPGConfiguration(String period){
		String path = "F:/ecofriche2/ecopaysage/entite_gestion/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		builder.setEntityRasterFile(path+"rpg/select/"+period+".tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); 
		
		for(int v1 : builder.getValues()){
			for(int v2 : builder.getValues()){
				if(v1<=v2){
					builder.addMetric("pNC_"+v1+"-"+v2);
					//builder.addMetric("NC_"+v1+"-"+v2);
				}
			}
		}
		builder.addMetric("NC-valid");
		builder.addCsvOutput(path+"entites_"+period+"_configuration_occsol.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainBretagne(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/FDCCA/bretagne/grain_10m/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"distance_bois_bretagne.tif");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.setDisplacement(2);
		builder.addAsciiGridOutput("MD", path+"grain_bretagne_101p_dep2.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEcopaysageNouvelleAquitaine(){
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(6);
		codes.add(7);
		codes.add(8);
		codes.add(9);
		codes.add(10);
		
		scriptEcopaysageNouvelleAquitaine("composition", "500m", codes);
		scriptEcopaysageNouvelleAquitaine("configuration", "500m", codes);
		scriptEcopaysageNouvelleAquitaine("composition", "3km", codes);
		scriptEcopaysageNouvelleAquitaine("configuration", "3km", codes);
	}
	
	private static void scriptEcopaysageNouvelleAquitaine(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "F:/aquitaine/nouvelle_aquitaine/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"ocs_15_na_ecopaysage_dnsb.tif");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classÃ©es
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(401); // 500m
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(2401); // 3km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(path+"analyse/nouvelle_aquitaine_"+compo_config+"_"+scale+"_dep40.csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptLocalWoodCoterra(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/coterra/data/Coterra_2019_DNSB_erb/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"Coterra_2019_DNSB_erb_alternative.asc");
		builder.addMetric("pNV_4");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.setWindowSize(3);
		builder.addAsciiGridOutput("pNV_4", path+"analyse/Coterra_local_wood_3p.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptPropPrairieCoterra(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/coterra/data/Coterra_2019_DNSB_erb/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"Coterra_2019_DNSB_erb.tif");
		builder.addMetric("pNV_3");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.setWindowSize(201);
		builder.setDisplacement(20);
		builder.setInterpolation(true);
		builder.addAsciiGridOutput("pNV_3", path+"analyse/Coterra_prop_prairie_201p.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptEcopaysageCoterra(){
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(6);
		codes.add(7);
		codes.add(8);
		codes.add(9);
		codes.add(10);
		codes.add(11);
		codes.add(12);
		
		scriptEcopaysageCoterra("composition", "500m", codes);
		scriptEcopaysageCoterra("configuration", "500m", codes);
		scriptEcopaysageCoterra("composition", "3km", codes);
		scriptEcopaysageCoterra("configuration", "3km", codes);
	}
	
	private static void scriptEcopaysageCoterra(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "F:/coterra/data/Coterra_2019_DNSB_erb/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"Coterra_2019_DNSB_erb.tif");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classÃ©es
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(401); // 500m
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(2401); // 3km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(path+"analyse/coterra_"+compo_config+"_"+scale+"_dep40.csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainCoterra(){
		String path = "F:/coterra/data/Coterra_2019_DNSB_erb/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"Coterra_2019_DNSB_erb_distance_elt_boises.asc");
		builder.addMetric("MD");
		builder.setWindowSize(141);
		builder.addAsciiGridOutput("MD", path+"Coterra_2019_DNSB_erb_grain_bocager.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestValuesAndCouples(){
		/*
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		
		builder.setRaster(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNV_1");
		builder.setWindowSize(21);
		//builder.addCsvOutput(path+"test/test1_value3.csv");
		//builder.addAsciiGridOutput("pNV_1", path+"test/test.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("NC_1-2");
		//builder.addMetric("NC-valid");
		builder.setWindowSize(21);
		//builder.addCsvOutput(path+"test/test1_couple3.csv");
		//builder.addAsciiGridOutput("NC_1-2", path+"test/nc_1-2_v.asc");
		//builder.addAsciiGridOutput("NC-valid", path+"test/nvalid_couple.asc");
		analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		*/
		/*
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNC_1-2");
		builder.setWindowSize(21);
		//builder.addCsvOutput(path+"focus/shdi.csv");
		builder.addAsciiGridOutput("pNC_1-2", path+"test/test_nc_1-2.asc");
		//builder.addAsciiGridOutput("NC-valid", path+"test/nvalid_couple.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		*/
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent etre classees
		builder.addMetric("pNV_1");
		builder.addMetric("pNC_1-2");
		builder.setWindowSize(21);
		//builder.addCsvOutput(path+"focus/shdi.csv");
		//builder.addAsciiGridOutput("pNV_1", path+"test/pnv_1-bis.asc");
		//builder.addAsciiGridOutput("pNC_1-2", path+"test/pnc_1-2-bis.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptFeature(){
		String path = "F:/chloe/chloe5/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(path+"za.asc");
		builder.setEntityRasterFile(path+"feature/feature_za.asc");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); 
		builder.addMetric("pNV_3");
		builder.addMetric("NV_3");
		builder.addCsvOutput(path+"feature/feature_za2.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTest(String singleOrHuge){
		String path = "";
		if(singleOrHuge.equalsIgnoreCase("single")){
			path = "F:/chloe/chloe5/data/";
		}else{
			path = "F:/data/sig/bretagne/";
		}
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(singleOrHuge.equalsIgnoreCase("single")){
			builder.setRasterFile(path+"za.tif");
		}else{
			builder.setRasterFile(path+"Bretagne_2018_dispositif_bocage_reb_4.tif");
		}
		
		builder.addMetric("SHDI");
		builder.setWindowSize(21);
		builder.addAsciiGridOutput("SHDI", path+"SHDI.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptCouesnon(){
		//scriptGrainCouesnon("BAU_2050");
		//scriptGrainCouesnon("Biomass_2050");
		//scriptGrainCouesnon("DesertCereals_2050");
		//scriptGrainCouesnon("DoublePerfs_2050");
		//scriptGrainCouesnon("OS_2018");
		//scriptGrainCouesnon("Utopia_BGIN_2050");
		
		//scriptPropPrairieCouesnon("BAU_2050");
		//scriptPropPrairieCouesnon("Biomass_2050");
		//scriptPropPrairieCouesnon("DesertCereals_2050");
		scriptPropPrairieCouesnon("DoublePerf_2050");
		scriptPropPrairieCouesnon("OS_2018");
		scriptPropPrairieCouesnon("UtopiaBGIN_2050");
	}
	
	private static void scriptPropPrairieCouesnon(String name){
		String path = "F:/Couesnon/analyse2/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"OS_Couesnon_bocage/cover_"+name+".tif");
		builder.setValues("1, 2, 3, 4, 8, 9, 21, 22, 23, 31, 32"); // doivent Ãªtre classÃ©es
		builder.addMetric("pNV_31");
		builder.addMetric("pNV_32");
		builder.setWindowSize(101);
		builder.setDisplacement(10);
		builder.setInterpolation(true);
		builder.addCsvOutput(path+"prairial/prop_prairie_"+name+".csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptGrainCouesnon(String name){
		String path = "F:/Couesnon/analyse2/data/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"distance_bois/"+name+"_distance_bois.asc");
		builder.addMetric("MD");
		builder.setWindowSize(71);
		builder.addAsciiGridOutput("MD", path+"grain/"+name+"_grain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptTestFocus(){

		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za.tif");
		//builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent Ãªtre classÃ©es
		builder.addMetric("sum");
		builder.setWindowSize(101);
		//builder.setDisplacement(40);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"focus/shdi.csv");
		//builder.addAsciiGridOutput("sum", path+"focus/sum.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void scriptEcopaysageBretagne(){
		Set<Integer> codes = new TreeSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		codes.add(6);
		codes.add(7);
		codes.add(8);
		codes.add(9);
		codes.add(10);
		codes.add(11);
		codes.add(12);
		
		scriptEcopaysageBretagne("composition", "500m", codes);
		scriptEcopaysageBretagne("configuration", "500m", codes);
		scriptEcopaysageBretagne("composition", "3km", codes);
		scriptEcopaysageBretagne("configuration", "3km", codes);
	}
	
	private static void scriptEcopaysageBretagne(String compo_config, String scale, Set<Integer> codes){
		long begin = System.currentTimeMillis();
		
		String path = "F:/ecofriche2/ecopaysage/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		
		StringBuilder sb = new StringBuilder();
		for(int c : codes){
			sb.append(c+",");
		}
		sb.deleteCharAt(sb.length()-1);
		builder.setValues(sb.toString()); // valeurs classÃ©es
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(401); // 500m
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(2401); // 3km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(path+"analyse/bretagne_"+compo_config+"_"+scale+"_dep40.csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	/*
	private static void test(){
		String path = "F:/chloe/chloe5/data/";
		//String raster = path+"za.asc";
		String raster = path+"za.tif";
		
		try {
			// coverage et infos associÃ©es
			GridCoverage2DReader reader;
			if(raster.endsWith(".asc")){
				File file = new File(raster);
				reader = new ArcGridReader(file);
			}else if(raster.endsWith(".tif")){
				File file = new File(raster);
				reader = new GeoTiffReader(file);
			}else{
				throw new IllegalArgumentException(raster+" is not a recognize raster");
			}
			GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			reader.dispose();
			reader = null;
			
			int inWidth = (Integer) coverage.getProperty("image_width");
			int inHeight = (Integer) coverage.getProperty("image_height");
			double inMinX = coverage.getEnvelope().getMinimum(0);
			double inMinY = coverage.getEnvelope().getMinimum(1);
			double inMaxX = coverage.getEnvelope().getMaximum(0);
			double inMaxY = coverage.getEnvelope().getMaximum(1);
			double inCellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			System.out.println(inWidth+" "+inHeight+" "+inMinX+" "+inMaxX+" "+inMinY+" "+inMaxY+" "+inCellSize);
			
			Rectangle roi = new Rectangle(22, 23, 100, 100);
			float[] inDatas = new float[roi.width * roi.height];
			System.out.println(roi.width*roi.height+" "+inDatas.length);
			System.out.println(roi.x+" "+roi.y+" "+roi.width+" "+roi.height);
			inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
			System.out.println(inDatas[0]+" "+inDatas[1]+" "+inDatas[2]+" "+inDatas[3]);
			System.out.println(inDatas[0+roi.width]+" "+inDatas[1+roi.width]+" "+inDatas[2+roi.width]+" "+inDatas[3+roi.width]);
			System.out.println(inDatas[0+2*roi.width]+" "+inDatas[1+2*roi.width]+" "+inDatas[2+2*roi.width]+" "+inDatas[3+2*roi.width]);
			
			
			PlanarImage planarImage = (PlanarImage) coverage.getRenderedImage();
			ImageUtilities.disposePlanarImageChain(planarImage);
			coverage.dispose(true);
			coverage = null;
			planarImage = null;
			
		} catch (DataSourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		float[] inDatas2 = CoverageManager.readData(raster, 22, 23, 100, 100);
		System.out.println(inDatas2[0]+" "+inDatas2[1]+" "+inDatas2[2]+" "+inDatas2[3]);
		System.out.println(inDatas2[100]+" "+inDatas2[101]+" "+inDatas2[102]+" "+inDatas2[103]);
		System.out.println(inDatas2[200]+" "+inDatas2[201]+" "+inDatas2[202]+" "+inDatas2[203]);
	}*/
	
	private static void scriptLea(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/temp/lea/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRaster(path+"fav_cheveche5m_c.asc");
		builder.setRasterFile(path+"fav_cheveche5m.tif");
		builder.addMetric("average");
		builder.setWindowSize(121);
		//builder.setDisplacement(12);
		//builder.setInterpolation(true);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"average_121p.csv");
		builder.addAsciiGridOutput("average", path+"average_121p_c.asc");
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void script1(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent Ãªtre classÃ©es
		builder.addMetric("SHDI");
		builder.setWindowSize(23);
		
		/*
		builder.setROIX(200);
		builder.setROIY(200);
		builder.setROIWidth(1000);
		builder.setROIHeight(1000);
		*/
		//builder.setDisplacement(7);
		//builder.setInterpolation(true);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(path+"analyse/shdi_23_writer2.csv");
		//builder.addAsciiGridOutput("SHDI", path+"analyse/shdi_23_200_200_huge_dep_7.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptHuge(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"za.asc");
		builder.addMetric("SHDI");
		builder.setWindowSize(21);
		//builder.setBufferROIXMin(0);
		//builder.setBufferROIXMax(200);
		//builder.setBufferROIYMin(100);
		//builder.setBufferROIYMax(300);
		builder.setDisplacement(2);
		builder.setInterpolation(true);
		//builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"analyse/testi.csv");
		builder.addAsciiGridOutput("SHDI", path+"analyse/shdi_ex100_300_bis.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptACE(){
		
		Set<Integer> codes = new HashSet<Integer>();
		codes.add(1);
		codes.add(2);
		codes.add(3);
		codes.add(4);
		codes.add(5);
		//codes.add(6); // eau
		codes.add(7);
		
		
		//scriptACE("2000", "composition", "300m", codes);
		//scriptACE("2009", "composition", "300m", codes);
		//scriptACE("2015", "composition", "300m", codes);
		//scriptACE("2000", "configuration", "300m", codes);
		//scriptACE("2009", "configuration", "300m", codes);
		//scriptACE("2015", "configuration", "300m", codes);
		//scriptACE("2000", "composition", "500m", codes);
		//scriptACE("2009", "composition", "500m", codes);
		//scriptACE("2015", "composition", "500m", codes);
		//scriptACE("2000", "configuration", "500m", codes);
		//scriptACE("2009", "configuration", "500m", codes);
		//scriptACE("2015", "configuration", "500m", codes);
		//scriptACE("2000", "composition", "700m", codes);
		//scriptACE("2009", "composition", "500m", codes);
		//scriptACE("2015", "composition", "500m", codes);
		//scriptACE("2000", "configuration", "700m", codes);
		//scriptACE("2009", "configuration", "500m", codes);
		//scriptACE("2015", "configuration", "500m", codes);
		//scriptACE("2000", "composition", "2km", codes);
		//scriptACE("2009", "composition", "2km", codes);
		//scriptACE("2015", "composition", "2km", codes);
		//scriptACE("2000", "configuration", "2km", codes);
		//scriptACE("2009", "configuration", "2km", codes);
		//scriptACE("2015", "configuration", "2km", codes);
		//scriptACE("2000", "composition", "3km", codes);
		//scriptACE("2009", "composition", "3km", codes);
		//scriptACE("2015", "composition", "3km", codes);
		//scriptACE("2000", "configuration", "3km", codes);
		//scriptACE("2009", "configuration", "3km", codes);
		//scriptACE("2015", "configuration", "3km", codes);
		//scriptACE("2000", "composition", "3-5km", codes);
		//scriptACE("2009", "composition", "3-5km", codes);
		//scriptACE("2015", "composition", "3-5km", codes);
		//scriptACE("2000", "configuration", "3-5km", codes);
		//scriptACE("2009", "configuration", "3-5km", codes);
		//scriptACE("2015", "configuration", "3-5km", codes);
	}
	
	private static void scriptACE(String year, String compo_config, String scale, Set<Integer> codes){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/aquitaine/ocs/OCS_Dordogne_00_09_15_raster/eau_divers/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setDisplacement(20);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.setRasterFile(path+"carto/OCS_Dordogne_"+year+".tif");
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : codes){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : codes){
				for(int j : codes){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("300m")){
			builder.setWindowSize(121); // 300m
		}
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(201); // 500m
		}
		if(scale.equalsIgnoreCase("700m")){
			builder.setWindowSize(281); // 700m
		}
		if(scale.equalsIgnoreCase("2km")){
			builder.setWindowSize(801); // 2km
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(1201); // 3km
		}
		if(scale.equalsIgnoreCase("3-5km")){
			builder.setWindowSize(1401); // 3km
		}
		
		builder.addCsvOutput(path+"analyse/ace_"+compo_config+"_"+year+"_"+scale+".csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptFDCCA(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/FDCCA/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRaster(path+"secteur1/carto/secteur1_distance.asc");
		//builder.setRaster(path+"secteur2/carto/secteur2_distance.asc");
		//builder.setRaster(path+"secteur3/carto/secteur3_distance.asc");
		//builder.setRaster(path+"secteur4/carto/secteur4_distance.asc");
		builder.setRasterFile(path+"secteur4bis/carto/secteur4bis_distance.asc");
		//builder.setRaster(path+"secteur5/carto/secteur5_distance.asc");
		builder.addMetric("MD");
		builder.setWindowSize(141);
		//builder.addAsciiGridOutput("MD", path+"secteur1/carto/secteur1_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur2/carto/secteur2_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur3/carto/secteur3_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur4/carto/secteur4_grain.asc");
		builder.addAsciiGridOutput("MD", path+"secteur4/carto/secteur4bis_grain.asc");
		//builder.addAsciiGridOutput("MD", path+"secteur5/carto/secteur5_grain.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptBasleonEcopaysageFonctionnel(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/bas_leon/data/continuite/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setRaster(path+"boise/norm_continuite_bois.asc");
		//builder.setRaster(path+"prairiale/norm_continuites_prairiales_talus_edge_wood.asc");
		builder.setRasterFile(path+"zh/norm_continuites_zones_humides.asc");
		builder.addMetric("sum");
		//builder.setWindowSize(401); // 500m gaussian
		builder.setWindowSize(2401); // 3km gaussian
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_boises_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_boises_3km.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_prairiales_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_prairiales_3km.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_zones_humides_500m.csv");
		//builder.addCsvOutput(path+"ecopaysage_functional/sum_continuites_zones_humides_3km.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptClemence(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/agent/clemence_brosse/Limousin/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"Limousin.tif");
		//builder.addMetric("NV_1");
		//builder.setWindowRadius(500);
		builder.setWindowSize(3);
		//builder.addCsvOutput(path+"Limousin_prop_eau.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptMathilde(){
		String path = "F:/woodnet/mathilde/Continuity/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"PF.asc");
		builder.setWindowSize(151);
		builder.setPointsFilter(path+"points.csv");
		//builder.addAscExportWindowOutput(path+"filters");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
	
}
