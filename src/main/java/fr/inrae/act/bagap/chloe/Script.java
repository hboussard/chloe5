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
import fr.inrae.act.bagap.chloe.counting.Counting;

public class Script {

	public static void main(String[] args){
	
		
	}
	
	private static void scriptGrainGers(){
		String path = "F:/gers/data/carto/";
		
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"distance_elts_boises.asc");
		builder.addMetric("MD");
		builder.setWindowSize(71);
		builder.addAsciiGridOutput("MD", path+"grain.asc");
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
		builder.setRaster(path+"OS_Couesnon_bocage/cover_"+name+".tif");
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
		builder.setRaster(path+"distance_bois/"+name+"_distance_bois.asc");
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
		builder.setRaster(path+"za.tif");
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
		builder.setRaster("F:/data/sig/bretagne/Bretagne_2018_dispositif_bocage_reb_4.tif");
		
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
		builder.setRaster(path+"fav_cheveche5m.tif");
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
		builder.setRaster(path+"za.tif");
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
	
	private static void scriptGrainBretagne(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/FDCCA/bretagne/grain2/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"distance_bois_bretagne.tif");
		builder.addMetric("MD");
		builder.setWindowSize(101);
		/*
		builder.setROIX(20000);
		builder.setROIY(10000);
		builder.setROIWidth(1000);
		builder.setROIHeight(1000);
		*/
		builder.setDisplacement(10);
		builder.addAsciiGridOutput("MD", path+"grain_bretagne_101p_dep10.asc");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void scriptHuge(){
		
		long begin = System.currentTimeMillis();
		
		String path = "F:/chloe/chloe5/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRaster(path+"za.asc");
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
		builder.setRaster(path+"carto/OCS_Dordogne_"+year+".tif");
		
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
		builder.setRaster(path+"secteur4bis/carto/secteur4bis_distance.asc");
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
		builder.setRaster(path+"zh/norm_continuites_zones_humides.asc");
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
		builder.setRaster(path+"Limousin.tif");
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
		builder.setRaster(path+"PF.asc");
		builder.setWindowSize(151);
		builder.setPointFilter(path+"points.csv");
		builder.addAscExportWindowOutput(path+"filters");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
	
}
