package fr.inrae.act.bagap.chloe.script;


import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptCortege {

	public static void main(String[] args) {
		
		//rasterizeOccsolTest();
		retileSousZone("occsolTest", 3);
		retileSousZone("occsolTest", 5);
		retileSousZone("occsol_2019", 3);
		retileSousZone("occsol_2019", 5);
		retileSousZone("occsol_2020", 3);
		retileSousZone("occsol_2020", 5);
		retileSousZone("occsol_2021", 3);
		retileSousZone("occsol_2021", 5);
		
		//analyseMetrics();
		
	}

	private static void retileSousZone(String name, int size) {
		
		String path = "C:/Data/projet/cortege/atelier_03-06-26/data/dataParametersModele/";
		
		Envelope env = null;
		if(size == 3) {
			env = new Envelope(634175.0, 637175.0, 6795850.0, 6798850.0); // 3x3 km
		}else {
			env = new Envelope(634175.0, 639175.0, 6793850.0, 6798850.0); // 5x5 km
		}
		
		Coverage cov = CoverageManager.getCoverage(path+name+".tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData(EnteteRaster.getROI(entete, env));
		cov.dispose();
		
		System.out.println(entete);
		
		if(size == 3) {
			CoverageManager.write(path+name+"_3x3km.tif", data, EnteteRaster.getEntete(entete, env));
		}else {
			CoverageManager.write(path+name+"_5x5km.tif", data, EnteteRaster.getEntete(entete, env));
		}
	}

	private static void rasterizeOccsolTest() {

		Envelope env = ShapeFileTool.getEnvelope("C:/Data/projet/cortege/atelier_03-06-26/data/enveloppe1.shp");
		
		Coverage cov = CoverageManager.getCoverage("E:/data/caphaie/occsol/raster/departement/OCCSOL_D045_cosia_2023_rpg_2024_gb_2023_theia_2023/");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData(EnteteRaster.getROI(entete, env));
		cov.dispose();
		
		CoverageManager.write("C:/Data/projet/cortege/atelier_03-06-26/data/occsolTest.tif", data, EnteteRaster.getEntete(entete, env));
	}
	
	private static void analyseMetrics() {

		// 2. maïs
		// 5. colza
		// 18. prairie permanente
		// 19. prairie temporaire
		// 24. betterave (autres cultures industrielles)
		// 25. legumes et fleurs
		
		String path = "C:/Data/projet/cortege/atelier_03-06-26/data/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.addRasterFile(path+"occsolTest.tif");
		builder.setWindowSizes(new int[]{201}); // 500m
		builder.addMetric("pNV_2");
		builder.addMetric("pNV_5");
		builder.addMetric("pNVm_18&19");
		builder.addMetric("pNV_24");
		builder.addMetric("pNV_25");	
		builder.addGeoTiffOutput("pNV_2", path+"pMais_500m.tif");
		builder.addGeoTiffOutput("pNV_5", path+"pColza_500m.tif");
		builder.addGeoTiffOutput("pNVm_18&19", path+"pPrairie_500m.tif");
		builder.addGeoTiffOutput("pNV_24", path+"pBetterave_500m.tif");
		builder.addGeoTiffOutput("pNV_25", path+"pLegume_500m.tif");
		//builder.setDisplacement(20);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		

	}

}
