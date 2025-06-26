package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;

public class ScriptDIRO {

	private static String path = "C:/Data/projet/DIRO/data/";
	
	private static int dMax = 250; 
	
	public static void main(String[] args) {
		
		/*
		//scriptContinuitesStructurelles(500);
		//scriptContinuitesStructurelles(3000);
		//scriptContinuitesStructurelles(5000);
		 */
		
		/*
		scriptGenerationPaysages();
		scriptGenerationPaysagesNettoyage();
		
		scriptGenerationPermeabilitesPrairiales(path+"permeabilites_prairiales/", path+"paysages_nettoyage/");
		scriptContinuitesPrairiales(path+"continuites_prairiales/", path+"paysages_nettoyage/", path+"permeabilites_prairiales/");
		calculIndicesContinuites(path+"indices_continuites_prairiales/", path+"continuites_prairiales/", dMax);
		
		scriptGenerationPermeabilitesBoisees(path+"permeabilites_boisees/", path+"paysages_nettoyage/");
		scriptContinuitesBoisees(path+"continuites_boisees/", path+"paysages_nettoyage/", path+"permeabilites_boisees/");
		calculIndicesContinuites(path+"indices_continuites_boisees/", path+"continuites_boisees/", dMax);
		*/
		
		/*scriptGenerationPaysagesScenario();
		
		scriptGenerationPermeabilitesPrairiales(path+"permeabilites_prairiales_scenario/", path+"paysages_scenario/");
		scriptContinuitesPrairiales(path+"continuites_prairiales_scenario/", path+"paysages_scenario/", path+"permeabilites_prairiales_scenario/");
		calculIndicesContinuites(path+"indices_continuites_prairiales_scenario/", path+"continuites_prairiales_scenario/", dMax);
		
		scriptGenerationPermeabilitesBoisees(path+"permeabilites_boisees_scenario/", path+"paysages_scenario/");
		scriptContinuitesBoisees(path+"continuites_boisees_scenario/", path+"paysages_scenario/", path+"permeabilites_boisees_scenario/");
		calculIndicesContinuites(path+"indices_continuites_boisees_scenario/", path+"continuites_boisees_scenario/", dMax);
		*/	
		
		//rasteurisation_sites();
		//rasteurisation_environement_sites();
		//analyseSurfaceSites();
		
		//analyseMoyennePrairiales();
		//analyseMoyenneBoisees();
		//analyseMoyennePrairialesScenario();
		//analyseMoyenneBoiseesScenario();
		
		//compileAnalyse();
	}
	
	private static void compileAnalyse() {
		try {
			
			CsvWriter cw = new CsvWriter(path+"analyse_diro.csv");
			cw.setDelimiter(';');
			cw.write("site");
			cw.write("area_site");
			cw.write("area_env");
			cw.write("moy_site_prairiale");
			cw.write("moy_site_boisee");
			cw.write("moy_env_prairiale");
			cw.write("moy_env_boisee");
			cw.write("moy_sc_env_prairiale");
			cw.write("moy_sc_env_boisee");
			cw.write("delta_moy_env_prairiale");
			cw.write("delta_moy_env_boisee");
			cw.endRecord();
			
			CsvReader cr = new CsvReader(path+"analyse_surfaces.csv");
			cr.setDelimiter(';');
			cr.readHeaders();
			
			CsvReader cr1;
			
			while(cr.readRecord()) {
				
				int code = Integer.parseInt(cr.get("raster").replace("environnement_site_clean_window_", "").replace(".tif", ""));
				System.out.println(code);
				
				float area_1 = Float.parseFloat(cr.get("area_1"));
				float area_2 = Float.parseFloat(cr.get("area_2"));
				
				cr1 = new CsvReader(path+"analyse_moyenne/analyse_moyenne_prairiale_"+code+".csv");
				cr1.setDelimiter(';');
				cr1.readHeaders();
				
				// site
				cr1.readRecord();
				float moy_p_1 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				
				// env
				cr1.readRecord();
				float moy_p_2 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				cr1.close();
				
				cr1 = new CsvReader(path+"analyse_moyenne/analyse_moyenne_boisee_"+code+".csv");
				cr1.setDelimiter(';');
				cr1.readHeaders();
				
				// site
				cr1.readRecord();
				float moy_b_1 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				// env
				cr1.readRecord();
				float moy_b_2 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				cr1.close();
				
				cr1 = new CsvReader(path+"analyse_moyenne_scenario/analyse_moyenne_prairiale_scenario_"+code+".csv");
				cr1.setDelimiter(';');
				cr1.readHeaders();
				
				// env
				cr1.readRecord();
				cr1.readRecord();
				float moy_sc_p_2 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				cr1.close();
				
				cr1 = new CsvReader(path+"analyse_moyenne_scenario/analyse_moyenne_boisee_scenario_"+code+".csv");
				cr1.setDelimiter(';');
				cr1.readHeaders();
				
				// env
				cr1.readRecord();
				cr1.readRecord();
				float moy_sc_b_2 = Float.parseFloat(cr1.get("average")) * 100.0f;
				
				cr1.close();
				
				float delta_moy_p_2 = (moy_p_2 - moy_sc_p_2) * 100.0f;
				float delta_moy_b_2 = (moy_b_2 - moy_sc_b_2) * 100.0f;
				
				cw.write(code+"");
				cw.write(area_1+"");
				cw.write(area_2+"");
				cw.write(moy_p_1+"");
				cw.write(moy_b_1+"");
				cw.write(moy_p_2+"");
				cw.write(moy_b_2+"");
				cw.write(moy_sc_p_2+"");
				cw.write(moy_sc_b_2+"");
				cw.write(delta_moy_p_2+"");
				cw.write(delta_moy_b_2+"");
				cw.endRecord();
			}
			
			cr.close();
			cw.close();
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

	private static void analyseMoyennePrairiales() {
		
		File folder = new File(path+"indices_continuites_prairiales/");
		
		for(String file : folder.list()) {
		
			if(file.endsWith(".tif")) {
		
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[5]);
				
				System.out.println(file+" "+code );
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setAnalysisType(ChloeAnalysisType.ENTITY);
				builder.setRasterFile(path+"indices_continuites_prairiales/"+file);
				builder.setEntityRasterFile(path+"environnement_sites/environnement_site_clean_window_"+code+".tif");
				builder.addMetric("average");
				builder.addCsvOutput(path+"analyse_moyenne/analyse_moyenne_prairiale_"+code+".csv");
				LandscapeMetricAnalysis analysis = builder.build();
				
				analysis.allRun();
				
			}
		}
	}
	
	private static void analyseMoyenneBoisees() {
		
		File folder = new File(path+"indices_continuites_boisees/");
		
		for(String file : folder.list()) {
		
			if(file.endsWith(".tif")) {
		
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[5]);
				
				System.out.println(file+" "+code );
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setAnalysisType(ChloeAnalysisType.ENTITY);
				builder.setRasterFile(path+"indices_continuites_boisees/"+file);
				builder.setEntityRasterFile(path+"environnement_sites/environnement_site_clean_window_"+code+".tif");
				builder.addMetric("average");
				builder.addCsvOutput(path+"analyse_moyenne/analyse_moyenne_boisee_"+code+".csv");
				LandscapeMetricAnalysis analysis = builder.build();
				
				analysis.allRun();
				
			}
		}
	}
	
	private static void analyseMoyennePrairialesScenario() {
		
		File folder = new File(path+"indices_continuites_prairiales_scenario/");
		
		for(String file : folder.list()) {
		
			if(file.endsWith(".tif")) {
		
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[6]);
				
				System.out.println(file+" "+code );
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setAnalysisType(ChloeAnalysisType.ENTITY);
				builder.setRasterFile(path+"indices_continuites_prairiales_scenario/"+file);
				builder.setEntityRasterFile(path+"environnement_sites/environnement_site_clean_window_"+code+".tif");
				builder.addMetric("average");
				builder.addCsvOutput(path+"analyse_moyenne_scenario/analyse_moyenne_prairiale_scenario_"+code+".csv");
				LandscapeMetricAnalysis analysis = builder.build();
				
				analysis.allRun();
				
			}
		}
	}
	
	private static void analyseMoyenneBoiseesScenario() {
		
		File folder = new File(path+"indices_continuites_boisees_scenario/");
		
		for(String file : folder.list()) {
		
			if(file.endsWith(".tif")) {
		
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[6]);
				
				System.out.println(file+" "+code );
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setAnalysisType(ChloeAnalysisType.ENTITY);
				builder.setRasterFile(path+"indices_continuites_boisees_scenario/"+file);
				builder.setEntityRasterFile(path+"environnement_sites/environnement_site_clean_window_"+code+".tif");
				builder.addMetric("average");
				builder.addCsvOutput(path+"analyse_moyenne_scenario/analyse_moyenne_boisee_scenario_"+code+".csv");
				LandscapeMetricAnalysis analysis = builder.build();
				
				analysis.allRun();
				
			}
		}
	}

	private static void analyseSurfaceSites() {
		
		File folder = new File(path+"environnement_sites/");
		
		Raster.setCellSize(5);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		
		for(String file : folder.list()) {
		
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				builder.addRasterFile(path+"environnement_sites/"+file);
				
			}
		}
		
		builder.addMetric("area_1");
		builder.addMetric("area_2");
		builder.addCsvOutput(path+"analyse_surfaces.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	private static void rasteurisation_environement_sites() {
		
		Util.createAccess(path+"environnement_sites/");
		
		File folder = new File(path+"sites/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[3]);
				
				System.out.println(file+" "+code);
				
				Coverage cov = CoverageManager.getCoverage(path+"sites/"+file);
				float[] data = cov.getData();
				EnteteRaster entete = cov.getEntete();
				cov.dispose();
				
				float[] distanceData = new float[data.length];
				
				TabChamferDistanceAnalysis analysis = new TabChamferDistanceAnalysis(distanceData, data, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[] {code}, entete.noDataValue());
				analysis.allRun();
				
				//CoverageManager.write(path+"environnement_sites/distance_"+file, distanceData, entete);
				
				for(int i=0; i<distanceData.length; i++) {
					
					if(distanceData[i] == 0) {
						distanceData[i] = 1;
					}else if(distanceData[i] <= dMax) {
						distanceData[i] = 2;
					}else {
						distanceData[i] = 0;
					}
				}
				
				CoverageManager.write(path+"environnement_sites/environnement_"+file, distanceData, entete);
			}
		}
	}
	
	private static void rasteurisation_sites() {
		
		Util.createAccess(path+"sites/");
		
		File folder = new File(path+"paysages_nettoyage/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
			
				String code = file.replace(".tif", "").split("_")[2];
				System.out.println(path+"paysages_nettoyage/"+file+" "+code);
				
				Map<String, Integer> codes = new HashMap<String, Integer>();
				codes.put(code, Integer.parseInt(code));
				
				Coverage cov = CoverageManager.getCoverage(path+"paysages_nettoyage/"+file);
				EnteteRaster entete = cov.getEntete();
				cov.dispose();
				
				ShapeFile2CoverageConverter.rasterize(path+"sites/site_"+file, path+"Sites_DIRO.shp", "code", codes, 0, entete);			
			}
		}
	}

	private static void calculIndicesContinuites(String output, String input, int dMax) {
		
		Util.createAccess(output);
		
		File folder = new File(input);
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				calculIndiceContinuite(output+"indice_"+file, input+file, dMax);
			}
		}
	}
	
	private static void calculIndiceContinuite(String output, String input, int dMax) {
		
		Coverage cov = CoverageManager.getCoverage(input);
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				return (float) (v[0] / (((Math.pow(dMax, 2)*Math.PI))*dMax)/3);
			}
		};
		cal.run();
		
		CoverageManager.write(output, outData, entete);
	}
	
	private static void scriptContinuitesStructurelles(int size) {
		
		int ws = ((int) (size * 2 / 5)) + 1;
		System.out.println(ws);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		
		builder.addRasterFile("F:/data/sig/grand_ouest/GO_2021_ebr.tif");
		builder.setValues("1,2,3,4,5,6,7,9,10,12,13,14,15,16,17,18,19,20,22,23");
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setWindowSize(ws);
		builder.setDisplacement(20);
		builder.addMetric("pNVm_13&14&19");
		builder.addGeoTiffOutput("pNVm_13&14&19", path+"structure/proportion_habitats_prairiaux_"+size+"m.tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
	}
	
	private static void scriptContinuitesPrairiales(String output, String inputOS, String inputPermeabilite) {
		
		File folder = new File(inputOS);
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
				builder.setRasterFile(inputOS+file);
				builder.setRasterFile2(inputPermeabilite+"permeabilite_prairiale_"+file);
				builder.setWindowSize(101);
				builder.addMetric("volume");
				builder.setDMax(dMax);
				builder.addGeoTiffOutput("volume", output+"continuite_prairiale_"+file);
				
				LandscapeMetricAnalysis analysis = builder.build();
			
				analysis.allRun();
			}
		}
	}
	
	private static void scriptContinuitesBoisees(String output, String inputOS, String inputPermeabilite) {
		
		File folder = new File(inputOS);
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
				builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
				builder.setRasterFile(inputOS+file);
				builder.setRasterFile2(inputPermeabilite+"permeabilite_boisee_"+file);
				builder.setWindowSize(101);
				builder.addMetric("volume");
				builder.setDMax(dMax);
				builder.addGeoTiffOutput("volume", output+"continuite_boisee_"+file);
				
				LandscapeMetricAnalysis analysis = builder.build();
			
				analysis.allRun();
			}
		}
	}
	
	private static void scriptGenerationPermeabilitesPrairiales(String output, String input) {
		
		Util.createAccess(output);
		
		File folder = new File(input);
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				Coverage covOS = CoverageManager.getCoverage(input+file);
				EnteteRaster entete = covOS.getEntete();
				float[] dataOS = covOS.getData();
				covOS.dispose();
				
				Map<Float, Float> sarMap = new TreeMap<Float, Float>();
				sarMap.put(0f, 100f); 
				sarMap.put(1f, 100f); 
				sarMap.put(2f, 5f);
				sarMap.put(3f, 100f); 
				sarMap.put(4f, 10f); 
				sarMap.put(5f, 2.5f);
				sarMap.put(6f, 2.5f);
				sarMap.put(7f, 2.5f);
				sarMap.put(9f, 2.5f);
				sarMap.put(10f, 2.5f);
				sarMap.put(12f, 2.5f);
				sarMap.put(13f, 1f);
				sarMap.put(14f, 1f);
				sarMap.put(15f, 5f);
				sarMap.put(16f, 1.8f);
				sarMap.put(17f, 1.8f);
				sarMap.put(18f, 1.3f);
				sarMap.put(19f, 1f);
				sarMap.put(20f, 1.8f);
				sarMap.put(22f, 100f);
				sarMap.put(23f, 100f);
				Pixel2PixelTabCalculation cal;
				
				float[] dataPermeabilite = new float[entete.width()*entete.height()];
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataPermeabilite, dataOS, sarMap);
				cal.run();
				
				CoverageManager.write(output+"permeabilite_prairiale_"+file, dataPermeabilite, entete);
			}
		}
	}
	
	private static void scriptGenerationPermeabilitesBoisees(String output, String input) {
		
		Util.createAccess(output);
		
		File folder = new File(input);
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				Coverage covOS = CoverageManager.getCoverage(input+file);
				EnteteRaster entete = covOS.getEntete();
				float[] dataOS = covOS.getData();
				covOS.dispose();
				
				Map<Float, Float> sarMap = new TreeMap<Float, Float>();
				sarMap.put(0f, 100f); 
				sarMap.put(1f, 100f); 
				sarMap.put(2f, 10f);
				sarMap.put(3f, 100f); 
				sarMap.put(4f, 10f); 
				sarMap.put(5f, 2.5f);
				sarMap.put(6f, 2.5f);
				sarMap.put(7f, 2.5f);
				sarMap.put(9f, 2.5f);
				sarMap.put(10f, 2.5f);
				sarMap.put(12f, 2.5f);
				sarMap.put(13f, 2.5f);
				sarMap.put(14f, 1.5f);
				sarMap.put(15f, 2.5f);
				sarMap.put(16f, 1f);
				sarMap.put(17f, 1f);
				sarMap.put(18f, 1f);
				sarMap.put(19f, 2f);
				sarMap.put(20f, 1f);
				sarMap.put(22f, 100f);
				sarMap.put(23f, 100f);
				Pixel2PixelTabCalculation cal;
				
				float[] dataPermeabilite = new float[entete.width()*entete.height()];
				cal = new SearchAndReplacePixel2PixelTabCalculation(dataPermeabilite, dataOS, sarMap);
				cal.run();
				
				CoverageManager.write(output+"permeabilite_boisee_"+file, dataPermeabilite, entete);
			}
		}
	}

	private static void scriptGenerationPaysages(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.addRasterFile("F:/data/sig/grand_ouest/GO_2021_ebr.tif");
		builder.setValues("1,2,3,4,5,6,7,9,10,12,13,14,15,16,17,18,19,20,22,23");
		builder.addMetric("N-valid");
		builder.setWindowSize(1001); // 2.5km autour des points
		builder.setPointsFilter(path+"points_potentiels.txt");
		builder.addCsvOutput(path+"analyse.csv");
		builder.setWindowsPath(path+"paysages/");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptGenerationPaysagesNettoyage(){
		
		Util.createAccess(path+"paysages_nettoyage/");
		
		File folder = new File(path+"paysages/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				System.out.println(file);
				
				Coverage covOS = CoverageManager.getCoverage(path+"paysages/"+file);
				EnteteRaster entete = covOS.getEntete();
				float[] dataOS = covOS.getData();
				covOS.dispose();
		
				Coverage covMajOS = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"maj_sites.shp", "code", entete, 0);
				float[] dataMajOS = covMajOS.getData();
				covMajOS.dispose();
				
				for(int i=0; i<dataOS.length; i++) {
					if(dataOS[i] != -1 && dataMajOS[i] > 0) {
						dataOS[i] = dataMajOS[i];
					}
				}
				
				CoverageManager.write(path+"paysages_nettoyage/clean_"+file, dataOS, entete);
			}
		}
	}
	
	private static void scriptGenerationPaysagesScenario(){
		
		Util.createAccess(path+"paysages_scenario/");
		
		File folder = new File(path+"paysages_nettoyage/");
		
		for(String file : folder.list()) {
			
			if(file.endsWith(".tif")) {
				
				int code = Integer.parseInt(file.replace(".tif", "").split("_")[2]);
				
				System.out.println(file+" "+code);
				
				Coverage covOS = CoverageManager.getCoverage(path+"paysages_nettoyage/"+file);
				EnteteRaster entete = covOS.getEntete();
				float[] dataOS = covOS.getData();
				covOS.dispose();
		
				Map<String, Integer> codes = new HashMap<String, Integer>();
				codes.put(code+"", 1);
				
				Coverage covMajOS = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"Sites_DIRO.shp", "code", codes, entete, 0);
				//Coverage covMajOS = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"Sites_DIRO.shp", entete, 1, 0);
				float[] dataMajOS = covMajOS.getData();
				covMajOS.dispose();
				
				for(int i=0; i<dataOS.length; i++) {
					if(dataOS[i] != -1 && dataMajOS[i] > 0) {
						dataOS[i] = dataMajOS[i];
					}
				}
				
				CoverageManager.write(path+"paysages_scenario/scenario_"+file, dataOS, entete);
				
			}
		}
	}
	
}
