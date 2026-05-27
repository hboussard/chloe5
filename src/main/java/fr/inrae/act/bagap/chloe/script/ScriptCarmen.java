package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.cluster.chess.TabQueenClusteringAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptCarmen {

	public static void main(String[] args) {
		
		scriptCarmen6();
	}
	
	private static void scriptCarmen6(){
		
		String path = "C:/Data/temp/carmen/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowShapeType(WindowShapeType.CIRCLE);
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"data/buffer_cultures_BDG_2017.tif");
		//builder.addMetric("SHDI");
		//builder.addMetric("HET-frag");
		//builder.addMetric("SIDI");
		//builder.addMetric("pNC-hete");
		//builder.addMetric("EMS");
		builder.addMetric("RaoQ");
		builder.setWindowSize(101);
		builder.setThematicDistanceFile(path+"test6/essaie_distance.txt");
		builder.setUnfilters(new int[]{-1});
		//builder.addGeoTiffOutput("SHDI", path+"test6/shdi.tif");
		//builder.addGeoTiffOutput("HET-frag", path+"test6/het-frag.tif");
		//builder.addGeoTiffOutput("SIDI", path+"test6/sidi.tif");
		//builder.addGeoTiffOutput("pNC-hete", path+"test6/pNC-hete.tif");
		//builder.addGeoTiffOutput("EMS", path+"test6/ems.tif");
		builder.addGeoTiffOutput("RaoQ", path+"test6/raoq2.tif");
		
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptCarmen5(){
		
		String path = "C:/Data/temp/carmen/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowShapeType(WindowShapeType.CIRCLE);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addRasterFile(path+"data/buffer_cultures_BDG_2017.tif");
		builder.addMetric("MPS");
		builder.addMetric("MPS-class_8");
		builder.addMetric("NP");
		builder.addMetric("NP-class_8");
		builder.addMetric("LPI");
		builder.addMetric("LPI-class_8");
		builder.addMetric("EMS");
		builder.addMetric("EMS-class_8");
		builder.setWindowSize(51);
		builder.setUnfilters(new int[]{-1});
		/*
		builder.addGeoTiffOutput("MPS", path+"test3/mps_circle.tif");
		builder.addGeoTiffOutput("MPS-class_8", path+"test3/mps_8_circle.tif");
		builder.addGeoTiffOutput("NP", path+"test3/np_circle.tif");
		builder.addGeoTiffOutput("NP-class_8", path+"test3/np_8_circle.tif");
		builder.addGeoTiffOutput("LPI", path+"test3/lpi_circle.tif");
		builder.addGeoTiffOutput("LPI-class_8", path+"test3/lpi_8_circle.tif");
		builder.addGeoTiffOutput("EMS", path+"test3/ems_circle.tif");
		builder.addGeoTiffOutput("EMS-class_8", path+"test3/ems_8_circle.tif");
		*/
		/*
		builder.addGeoTiffOutput("MPS", path+"test3/mps_circle2.tif");
		builder.addGeoTiffOutput("MPS-class_8", path+"test3/mps_8_circle2.tif");
		builder.addGeoTiffOutput("NP", path+"test3/np_circle2.tif");
		builder.addGeoTiffOutput("NP-class_8", path+"test3/np_8_circle2.tif");
		builder.addGeoTiffOutput("LPI", path+"test3/lpi_circle2.tif");
		builder.addGeoTiffOutput("LPI-class_8", path+"test3/lpi_8_circle2.tif");
		builder.addGeoTiffOutput("EMS", path+"test3/ems_circle2.tif");
		builder.addGeoTiffOutput("EMS-class_8", path+"test3/ems_8_circle2.tif");
		*/
		
		builder.addGeoTiffOutput("MPS", path+"test3/mps_gaussian.tif");
		builder.addGeoTiffOutput("MPS-class_8", path+"test3/mps_8_gaussian.tif");
		builder.addGeoTiffOutput("NP", path+"test3/np_gaussian.tif");
		builder.addGeoTiffOutput("NP-class_8", path+"test3/np_8_gaussian.tif");
		builder.addGeoTiffOutput("LPI", path+"test3/lpi_gaussian.tif");
		builder.addGeoTiffOutput("LPI-class_8", path+"test3/lpi_8_gaussian.tif");
		builder.addGeoTiffOutput("EMS", path+"test3/ems_gaussian.tif");
		builder.addGeoTiffOutput("EMS-class_8", path+"test3/ems_8_gaussian.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptCarmen4(){
		
		String path = "C:/Data/temp/carmen/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowShapeType(WindowShapeType.CIRCLE);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(path+"cluster/cluster_distance_8.tif");
		builder.addMetric("SHDI");
		builder.addMetric("SHDI-frag");
		builder.setWindowSize(101);
		builder.setUnfilters(new int[]{-1});
		builder.addGeoTiffOutput("SHDI", path+"test5/shdi_cluster_8.tif");
		builder.addGeoTiffOutput("SHDI-frag", path+"test5/frag_cluster_8.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptCarmen3(){
	
		String path = "C:/Data/temp/carmen/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		//builder.setWindowShapeType(WindowShapeType.CIRCLE);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(path+"data/buffer_cultures_BDG_2017.tif");
		builder.addMetric("pInterface_8");
		builder.setWindowSize(151);
		builder.setUnfilters(new int[]{-1});
		builder.addGeoTiffOutput("pInterface_8", path+"test2/pinterface_8.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptCarmen2(){
	
		String path = "C:/Data/temp/carmen/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setWindowShapeType(WindowShapeType.CIRCLE);
		builder.addRasterFile(path+"cluster2/cluster_8.tif");
		builder.addMetric("Nclass");
		
		// analyse avec plusieurs tailles de fen�tre
		builder.setWindowSize(201);
		
		builder.setPointsFilter(path+"data/Centroides_cultures.csv");
		builder.addCsvOutput(path+"analyse/selected_pixels.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void scriptCarmen(){
		
		String path = "C:/Data/temp/carmen/";
		
		Coverage cov = CoverageManager.getCoverage(path+"data/buffer_cultures_BDG_2017.tif");
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		int[] codes =  new int[]{8};
		
		TabQueenClusteringAnalysis ca = new TabQueenClusteringAnalysis(data, entete.width(), entete.height(), codes, entete.noDataValue());
		float[] dataCluster = (float[]) ca.allRun();
		
		CoverageManager.writeGeotiff(path+"cluster2/cluster_8.tif", dataCluster, entete);
	}
}
