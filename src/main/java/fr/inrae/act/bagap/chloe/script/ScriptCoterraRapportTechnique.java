package fr.inrae.act.bagap.chloe.script;

import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptCoterraRapportTechnique {

	public static void main(String[] args) {
		
		recuperationAltitude();
		//compileAltitute();
		//detectionPentes();
		//calulateMoyennePentes(1000);
		
		//ecolandscape("occsol", 6, 3000);
		//ecolandscape("pente", 10, 3000);
	}
	
	private static void ecolandscape(String info, int classes, int scale) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.setForce(true);
		epManager.addInputRaster("F:/coterra/data/Coterra_2019_DNSB_erb/Coterra_2019_DNSB_erb.tif");
		epManager.setScales(new int[]{scale});
		epManager.setClasses(new int[]{classes});
		epManager.setOutputFolder("F:/coterra/data/Coterra_2019_DNSB_erb/ecopaysages_"+info+"_"+classes+"_"+scale+"m/");
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void calulateMoyennePentes(int scale) {
		
		int ws = ((int) ((scale*2)/5.0))+1;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile("F:/coterra/data/Coterra_2019_DNSB_erb/slope.tif");
		builder.addMetric("average");
		builder.addWindowSize(ws);
		builder.setDisplacement(20);
		builder.setUnfilters(new int[] {-1});
		builder.addCsvOutput("F:/coterra/data/Coterra_2019_DNSB_erb/slope_"+scale+"m.csv");
		builder.addGeoTiffOutput("F:/coterra/data/Coterra_2019_DNSB_erb/slope_"+scale+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
	}
	
	private static void detectionPentes() {

		Erosion.slopeDetection("F:/coterra/data/Coterra_2019_DNSB_erb/slope.tif", "F:/coterra/data/Coterra_2019_DNSB_erb/elevation.tif");		
	}


	private static void compileAltitute(){
		
		Coverage cov1 = CoverageManager.getCoverage("F:/coterra/data/Coterra_2019_DNSB_erb/elevation1.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage("F:/coterra/data/Coterra_2019_DNSB_erb/elevation2.tif");
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		for(int i=0; i<entete.width()*entete.height(); i++) {
		
			data1[i] = Math.max(data1[i], data2[i]);
		}
		
		CoverageManager.write("F:/coterra/data/Coterra_2019_DNSB_erb/elevation.tif", data1, entete);
		
	}
	
	private static void recuperationAltitude(){
	
		String elevation = "F:/coterra/data/Coterra_2019_DNSB_erb/elevation_test.tif";
		String territoire ="F:/coterra/data/Coterra_2019_DNSB_erb/Coterra_2019_DNSB_erb.tif";
		Set<String> elevationFolders = new HashSet<String>();
		elevationFolders.add("D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D009_2023-10-04/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D009_2023-10-04/RGEALTI/1_DONNEES_LIVRAISON_2023-10-00126/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D009/");
		elevationFolders.add("D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D031/");
		elevationFolders.add("D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D032_2019-11-21/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D032_2019-11-21/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D032/");
		elevationFolders.add("D:/sig/rge_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D065_2020-02-11/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D065_2020-02-11/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D065/");
		
		Erosion.elevationConstruction(elevation, territoire, elevationFolders);
	}
	
	
}
