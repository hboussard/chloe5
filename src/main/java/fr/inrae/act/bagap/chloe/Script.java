package fr.inrae.act.bagap.chloe;

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

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class Script {

	public static void main(String[] args){
		scriptTestDistanceFunction();
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
		builder.setValues(sb.toString()); // valeurs classées
		
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile(path+"mnt_standard_deviation_5p.asc");
		builder.setAreaRasterFile(path+"parcellaire_toulouse_5m.asc");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile(path+"T01alti25m.tif");
		builder.setAreaRasterFile(path+"parcellaire_toulouse.asc");
		builder.addMetric("standard_deviation");
		builder.addAsciiGridFolderOutput(path+"mnt_standard_deviation/");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile(path+"MNT5mT01.tif");
		builder.setAreaRasterFile(path+"parcellaire_toulouse_5m.asc");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile(path+"occsol_sitesecofriche1.tif");
		builder.setAreaRasterFile(path+"unites_gestion_sites_ecofriche1.tif");
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); 
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.addMetric("pNV_6");
		builder.addAsciiGridFolderOutput(path+"prop/");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv_humide.tif");
		builder.setAreaRasterFile(path+"rpg/select/"+period+".tif");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv.tif");
		builder.setAreaRasterFile(path+"rpg/select/"+period+".tif");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile("F:/data/sig/CGTV/cgtv.tif");
		builder.setAreaRasterFile(path+"rpg/select/"+period+".tif");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		builder.setAreaRasterFile(path+"rpg/select/"+period+".tif");
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		builder.setAreaRasterFile(path+"rpg/select/"+period+".tif");
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
		builder.setValues(sb.toString()); // valeurs classées
		
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
		builder.setValues(sb.toString()); // valeurs classées
		
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
		builder.setAnalysisType(WindowAnalysisType.AREA);
		builder.setRasterFile(path+"za.asc");
		builder.setAreaRasterFile(path+"feature/feature_za.asc");
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
	
	private static void scriptGrainGers(){
		String path = "F:/gers/data/carto/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(path+"GRAIN_DIST_EUCLI.asc");
		builder.addMetric("MD");
		builder.setWindowSize(141);
		builder.addAsciiGridOutput("MD", path+"grain2.asc");
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
		builder.setValues("1, 2, 3, 4, 8, 9, 21, 22, 23, 31, 32"); // doivent être classées
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
		//builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent être classées
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
		builder.setValues(sb.toString()); // valeurs classées
		
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
	
	private static void test(){
		String path = "F:/chloe/chloe5/data/";
		//String raster = path+"za.asc";
		String raster = path+"za.tif";
		
		try {
			// coverage et infos associées
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
	}
	
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
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12"); // doivent être classées
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
		builder.setAnalysisType(WindowAnalysisType.SELECTED);
		builder.setRasterFile(path+"PF.asc");
		builder.setWindowSize(151);
		builder.setPointFilter(path+"points.csv");
		builder.addAscExportWindowOutput(path+"filters");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
	
}
