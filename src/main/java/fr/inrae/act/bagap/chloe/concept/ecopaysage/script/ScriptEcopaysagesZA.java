package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptEcopaysagesZA {

	public static void main(String[] args) {
		//cleanOccsol();
		
		ecolandscape("2016", "inv_inertia", "", 25);
		//ecolandscape("2016", "inv_inertia", "", 50);
		ecolandscape("2016", "inv_inertia", "", 100);
		//ecolandscapeDynamics();
		
		//prepaRgeAlti();
		//detectionPentes();
		/*
		//100m
		calulateMoyennePentes(1000, 5, 20);
		calulateMoyenneAltitudes(1000, 5, 20);
		//50m
		calulateMoyennePentes(1000, 5, 10);
		calulateMoyenneAltitudes(1000, 5, 10);
		//25m
		calulateMoyennePentes(1000, 5, 5);
		calulateMoyenneAltitudes(1000, 5, 5);
		*/
		//testInertia();
		
		//testEcopaysages();
		//testEcopaysagesDynamiques();
	}
	
	private static void ecolandscape(String year, String method, String info, int size) {
		
		int cellSize = 5;
		int dep = size/cellSize;
		//System.out.println(size+" "+dep);
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setForce(true);
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_"+year+".tif");
		epManager.setScales(new int[]{1000});
		epManager.setOutputFolder("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecopaysages_"+year+"_"+method+"_"+info+"_"+size+"m/");
		epManager.setClasses(new int[]{10});
		//epManager.setDisplacement(5); // 25m	
		//epManager.setDisplacement(10); // 50m	
		//epManager.setDisplacement(20); // 100m
		epManager.setDisplacement(dep);
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}

	private static void testInertia() {
			
		int nbMetrics = 3;
		List<float[]> data1 = new ArrayList<float[]>();
		data1.add(new float[] {10,100,1000});
		data1.add(new float[] {11,101,1000});
		data1.add(new float[] {10,100,1005});
		
		List<float[]> data2 = new ArrayList<float[]>();
		data2.add(new float[] {100,100,100});
		data2.add(new float[] {101,101,100});
		data2.add(new float[] {100,100,105});
		
		float inertia1 = EcoPaysage.inertia(data1, nbMetrics);
		//float inertia2 = EcoPaysage.inertia2(data1, nbMetrics);
		float moyenne = EcoPaysage.moyenne(data1, nbMetrics);
		System.out.println(inertia1+" "+moyenne);
		
		inertia1 = EcoPaysage.inertia(data2, nbMetrics);
		moyenne = EcoPaysage.moyenne(data2, nbMetrics);
		System.out.println(inertia1+" "+moyenne);
		
	}
	
	private static void testEcopaysagesDynamiques() {
		
		//ecolandscapeDynamics("inertia1", "100m");
		//ecolandscapeDynamics("inertia2", "100m");
		//ecolandscapeDynamics("moyenne", "100m");
		
		//ecolandscapeDynamics("inertia1", "pente");
		//ecolandscapeDynamics("inertia2", "pente");
		//ecolandscapeDynamics("moyenne", "pente");
		
		//ecolandscapeDynamics("inertia1", "altitude");
		//ecolandscapeDynamics("inertia2", "altitude");
		//ecolandscapeDynamics("moyenne", "altitude");
		
		//ecolandscapeDynamics("inertia1", "pente_altitude");
		//ecolandscapeDynamics("inertia2", "pente_altitude");
		//ecolandscapeDynamics("moyenne", "pente_altitude");
		
		//ecolandscapeDynamics("inertia1", "25m");
		//ecolandscapeDynamics("inertia2", "25m");
		//ecolandscapeDynamics("moyenne", "25m");
	}
	
	private static void ecolandscapeDynamics(String method, String resolution) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setForce(true);
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2015.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2016.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2017.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2018.tif");
		epManager.setScales(new int[]{1000});
		epManager.setOutputFolder("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecopaysages_2015-2018_"+method+"_"+resolution+"/");
		epManager.setClasses(new int[]{7});
		if(resolution.equalsIgnoreCase("25m")){
			epManager.setDisplacement(5); // 25m	
		}
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void testEcopaysages() {
		
		//ecolandscape("2016", "inertia1", "compo");
		//ecolandscape("2016", "inertia1", "config");
		//ecolandscape("2016", "moyenne", "compo");
		//ecolandscape("2016", "moyenne", "config");
		
		/*
		ecolandscape("2015", "inertia1", "100m");
		ecolandscape("2016", "inertia1", "100m");
		ecolandscape("2017", "inertia1", "100m");
		ecolandscape("2018", "inertia1", "100m");
		
		ecolandscape("2015", "inertia1", "25m");
		ecolandscape("2016", "inertia1", "25m");
		ecolandscape("2017", "inertia1", "25m");
		ecolandscape("2018", "inertia1", "25m");
		*/
		/*
		ecolandscape("2015", "inertia2", "100m");
		ecolandscape("2016", "inertia2", "100m");
		ecolandscape("2017", "inertia2", "100m");
		ecolandscape("2018", "inertia2", "100m");
		
		ecolandscape("2015", "inertia2", "25m");
		ecolandscape("2016", "inertia2", "25m");
		ecolandscape("2017", "inertia2", "25m");
		ecolandscape("2018", "inertia2", "25m");
		*/
		/*
		ecolandscape("2015", "moyenne", "100m");
		ecolandscape("2016", "moyenne", "100m");
		ecolandscape("2017", "moyenne", "100m");
		ecolandscape("2018", "moyenne", "100m");
		
		ecolandscape("2015", "moyenne", "25m");
		ecolandscape("2016", "moyenne", "25m");
		ecolandscape("2017", "moyenne", "25m");
		ecolandscape("2018", "moyenne", "25m");
		*/
		//ecolandscape("2016", "inertia1", "pente");
		//ecolandscape("2016", "inertia2", "pente");
		//ecolandscape("2016", "moyenne", "pente");
		
		//ecolandscape("2016", "inertia1", "altitude");
		//ecolandscape("2016", "inertia2", "altitude");
		//ecolandscape("2016", "moyenne", "altitude");
		
		//ecolandscape("2016", "inertia1", "pente_altitude");
		//ecolandscape("2016", "inertia2", "pente_altitude");
		//ecolandscape("2016", "moyenne", "pente_altitude");
		
	}
	
	private static void cleanOccsol() {
		Coverage cov2016 = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2016.tif");
		EnteteRaster entete = cov2016.getEntete();
		float[] data2016 = cov2016.getData();
		cov2016.dispose();
		
		Coverage cov2015 = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2015.tif");
		float[] data2015 = cov2015.getData();
		cov2015.dispose();
		
		Coverage cov2017 = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2017.tif");
		float[] data2017 = cov2017.getData();
		cov2017.dispose();
		
		Coverage cov2018 = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2018.tif");
		float[] data2018 = cov2018.getData();
		cov2018.dispose();
		
		Coverage covSlope = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope.tif");
		float[] dataSlope = covSlope.getData();
		covSlope.dispose();
		
		Coverage covElevation = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation.tif");
		float[] dataElevation = covElevation.getData();
		covElevation.dispose();
		
		for(int i=0; i<(entete.width()*entete.height()); i++){
			if(data2015[i] == entete.noDataValue()
				|| data2016[i] == entete.noDataValue()
				|| data2017[i] == entete.noDataValue()
				|| data2018[i] == entete.noDataValue()
				|| dataSlope[i] == entete.noDataValue()
				|| dataElevation[i] == entete.noDataValue()
			) {
				data2015[i] = entete.noDataValue();
				data2016[i] = entete.noDataValue();
				data2017[i] = entete.noDataValue();
				data2018[i] = entete.noDataValue();
				dataSlope[i] = entete.noDataValue();
				dataElevation[i] = entete.noDataValue();
			}
		}
		
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2015.tif", data2015, entete);
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2016.tif", data2016, entete);
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2017.tif", data2017, entete);
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2018.tif", data2018, entete);
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope.tif", dataSlope, entete);
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation.tif", dataElevation, entete);
	}
	
	private static void calulateMoyenneAltitudes(int radius, int cellSize, int displacement) {
		
		int ws = ((int) ((radius*2)/cellSize))+1;
		int dep = displacement * cellSize;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation.tif");
		builder.addMetric("average");
		builder.addWindowSize(ws);
		builder.setDisplacement(displacement);
		builder.setUnfilters(new int[] {-1});
		builder.addCsvOutput("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation_average_"+radius+"m_"+dep+"m.csv");
		builder.addGeoTiffOutput("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation_average_"+radius+"m_"+dep+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
	}
	
	private static void calulateMoyennePentes(int radius, int cellSize, int displacement) {
		
		int ws = ((int) ((radius*2)/cellSize))+1;
		int dep = displacement * cellSize;
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope.tif");
		builder.addMetric("average");
		builder.addWindowSize(ws);
		builder.setDisplacement(displacement);
		builder.setUnfilters(new int[] {-1});
		builder.addCsvOutput("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope_average_"+radius+"m_"+dep+"m.csv");
		builder.addGeoTiffOutput("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope_average_"+radius+"m_"+dep+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
	}
	
	private static void detectionPentes() {

		Erosion.slopeDetection("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope.tif", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation.tif");		
	}

	private static void prepaRgeAlti() {

		Coverage osCov = CoverageManager.getCoverage("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2015.tif");
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		System.out.println(osEntete);
		
		Envelope osEnvelope = osEntete.getEnvelope();
		System.out.println(osEnvelope);
	
		Coverage altCov = CoverageManager.getCoverage("F:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2022-11-15/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2022-11-15/RGEALTI/1_DONNEES_LIVRAISON_2023-01-00223/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035/");
		//Coverage altCov = CoverageManager.getCoverage("F:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035/");
		EnteteRaster altEntete = altCov.getEntete();
		
		System.out.println(EnteteRaster.getROI(altEntete, osEnvelope));
		
		float[] altData = altCov.getData(EnteteRaster.getROI(altEntete, osEnvelope));
		altCov.dispose();
		
		System.out.println(altEntete);
		
		for(int ind=0; ind<osData.length; ind++){
			if(osData[ind] == osEntete.noDataValue() || altData[ind] == altEntete.noDataValue()){
				altData[ind] = osEntete.noDataValue();
			}
		}
		
		CoverageManager.write("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation.tif", altData, osEntete);
	}

}
