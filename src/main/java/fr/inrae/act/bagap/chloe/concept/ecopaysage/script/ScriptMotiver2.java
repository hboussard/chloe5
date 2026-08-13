package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;
import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.GeoPackage2CoverageConverter;
import fr.inrae.act.bagap.apiland.vector.GeoPackageTool;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMotiver2 {

	private static String pathOccsol = "E:/data/caphaie/occsol/raster/france/";
	
	private static String pathMotiver = "E:/projet/motiver/Sites_motiver/";
	
	private static String pathRPG = "E:/data/rpg/data/france/rpg_explorer/";
	
	public static void main(String[] args) {

		//recuperationOccSol();
		//calculateEcolandscape("mapping");
		//calculateEcolandscape("membership");
		
		//exportValues();
		//calculateMetrics();
		//compileMetrics();
		
		//rasterizeRPG(0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		//rasterizeRPG(1, 2018, 2019, 2020, 2021, 2022, 2023);
		//rasterizeRPG(2, 2017);
		//rasterizeRPG(3, 2015, 2016, 2017, 2018);
		//rasterizeRPG(4, 2015, 2016, 2017, 2018);
		//rasterizeRPG(5, 2015, 2016, 2017, 2018);
		
		//compileRPG(0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		//compileRPG(1, 2018, 2019, 2020, 2021, 2022, 2023);
		//compileRPG(2, 2017);
		//compileRPG(3, 2015, 2016, 2017, 2018);
		//compileRPG(4, 2015, 2016, 2017, 2018);
		//compileRPG(5, 2015, 2016, 2017, 2018);
		
		//compileOSO(0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		//compileOSO(1, 2018, 2019, 2020, 2021, 2022, 2023);
		//compileOSO(2, 2017);
		//compileOSO(3, 2015, 2016, 2017, 2018);
		//compileOSO(4, 2015, 2016, 2017, 2018);
		//compileOSO(5, 2015, 2016, 2017, 2018);
		/*
		int[] sizes = new int[] {1000};
		ecolandscape("mapping", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("mapping", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("mapping", sizes, 2, 2017);
		ecolandscape("mapping", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 5, 2015, 2016, 2017, 2018);
		
		int[] sizes = new int[] {5000};
		ecolandscape("mapping", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("mapping", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("mapping", sizes, 2, 2017);
		ecolandscape("mapping", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 5, 2015, 2016, 2017, 2018);
		
		int[] sizes = new int[] {1000, 5000};
		ecolandscape("mapping", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("mapping", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("mapping", sizes, 2, 2017);
		ecolandscape("mapping", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("mapping", sizes, 5, 2015, 2016, 2017, 2018);
		*/
		/*
		int[] sizes = new int[] {1000};
		ecolandscape("membership", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("membership", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("membership", sizes, 2, 2017);
		ecolandscape("membership", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 5, 2015, 2016, 2017, 2018);
		
		sizes = new int[] {5000};
		ecolandscape("membership", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("membership", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("membership", sizes, 2, 2017);
		ecolandscape("membership", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 5, 2015, 2016, 2017, 2018);
		
		sizes = new int[] {1000, 5000};
		ecolandscape("membership", sizes, 0, 2016, 2017, 2018, 2019, 2020, 2021, 2022);
		ecolandscape("membership", sizes, 1, 2018, 2019, 2020, 2021, 2022, 2023);
		ecolandscape("membership", sizes, 2, 2017);
		ecolandscape("membership", sizes, 3, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 4, 2015, 2016, 2017, 2018);
		ecolandscape("membership", sizes, 5, 2015, 2016, 2017, 2018);
		*/
	}
	
	private static void ecolandscape(String param, int[] sizes, int site, int... years) {
		
		int[] ks = new int[] {5, 6, 7, 8 , 9, 10};
		
		EcoPaysageManager epManager = new EcoPaysageManager(param);
		for(int year : years) {
			epManager.addInputRaster(pathMotiver+"occsol/oso/site_"+site+"/OSO_"+year+"_site_"+site+".tif");
		}
		epManager.setScales(sizes);
		epManager.setOutputFolder(pathMotiver+"ecolandscapes/ecolandscapes_site"+site+"/");
		epManager.setClasses(ks);
		epManager.setUnfilters(new int[] {-1});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38});
		epManager.setCompositionMetrics();
		epManager.setConfigurationMetrics();
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void compileOSO(int site, int... years) {
		
		Util.createAccess("E:/projet/motiver/Sites_motiver/occsol/oso/site_"+site+"/");
		
		float[] data, rpgData;
		EnteteRaster entete; 
		Coverage cov;
		
		cov = CoverageManager.getCoverage("E:/projet/motiver/Sites_motiver/occsol/ref/OSO_2024_site_"+site+".tif");
		entete = cov.getEntete();
		data = cov.getData();
		cov.dispose();
		
		for(int year : years) {
			
			cov = CoverageManager.getCoverage("E:/projet/motiver/Sites_motiver/occsol/rpg/site_"+site+"/RPG_"+year+"_site_"+site+".tif");
			rpgData = cov.getData();
			cov.dispose();
			
			int index = 0;
			for(float d : rpgData) {
				
				if(d != entete.noDataValue()) {
					
					data[index] = d;
				}
				index++;
			}
			
			CoverageManager.write("E:/projet/motiver/Sites_motiver/occsol/oso/site_"+site+"/OSO_"+year+"_site_"+site+".tif", data, entete);
		}
	}
	
	private static void compileRPG(int site, int... years) {
		
		float[] data, depData;
		EnteteRaster entete; 
		Coverage cov;
		for(int year : years) {
			
			File folder = new File(pathMotiver+"occsol/rpg/site_"+site+"/"+year+"/");
			
			data = null;
			entete = null;
			
			for(String file : folder.list()) {
				
				if(file.endsWith(".tif")) {
					
					System.out.println(folder.getAbsolutePath()+"/"+file);
					
					if(entete == null) {
						
						cov = CoverageManager.getCoverage(folder.getAbsolutePath()+"/"+file);
						entete = cov.getEntete();
						data = cov.getData();
						cov.dispose();
					
					}else {
						
						cov = CoverageManager.getCoverage(folder.getAbsolutePath()+"/"+file);
						depData = cov.getData();
						cov.dispose();
						
						int index = 0;
						for(float d : depData) {
							
							if(d != entete.noDataValue()) {
								
								data[index] = d;
							}
							
							index++;
						}
					}
				}
			}
			
			CoverageManager.write(pathMotiver+"occsol/rpg/site_"+site+"/RPG_"+year+"_site_"+site+".tif", data, entete);
		}
	}
	
	private static void rasterizeRPG(int site, int... years) {
		
		System.out.println("rasteurize site "+site);
		
		String[] deps = new String[] {
			"01", "02", "03", "04",	"05", "06",	"07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"2A", "2B", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
			"60", "61", "62", "63", "64", "65", "66", "67", "68", "69",
			"70", "71", "72", "73", "74", "76", "77", "78", "79",
			"80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
			"90", "91", "93", "94", "95"
		};
		
		Coverage cov = CoverageManager.getCoverage(pathMotiver+"occsol/OSO_2024_site_"+site+".tif");
		EnteteRaster entete = cov.getEntete();
		Envelope envelope = entete.getEnvelope();
		cov.dispose();
		
		Map<String, Integer> codes = Util.importDataString2Int("E:/data/rpg/data/france/rpg_sequence_traduction.txt", "code_culture", "code_groupe");
		
		Envelope env;
		String vectorFile;
		String outputFile;
		for(String dep : deps) {
			
			vectorFile = pathRPG+"seq1524_d"+dep+".gpkg";
			
			env = GeoPackageTool.getEnvelope(vectorFile);
			
			if(envelope.intersects(env)) {
				
				for(int year : years) {
					
					System.out.println(dep+" "+year);
					
					outputFile = pathMotiver+"occsol/rpg/RPG_"+year+"_site_"+site+"_dep_"+dep+".tif";
					
					GeoPackage2CoverageConverter.rasterize(outputFile, vectorFile, "crop"+(year-2000), codes, entete.noDataValue(), entete);
				}	
			}
		}
	}

	private static void compileMetrics() {
		
		int[] ks = new int[] {5, 6, 7, 8 , 9, 10};
		String[] sizes = new String[] {"1000m", "5000m", "1000m_5000m"};
		int[] years = new int[] {2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023}; 
		Map<String, Map<String, String>> data = new LinkedHashMap<String, Map<String, String>>();
		
		try {
			CsvReader cr = new CsvReader(pathMotiver+"site_motiver_point.csv");
			cr.setDelimiter(';');
			cr.readHeaders();
			
			String id, x, y;
			while(cr.readRecord()) {
				
				id = cr.get("ID");
				x = cr.get("X");
				y = cr.get("Y");
				
				data.put(id, new LinkedHashMap<String, String>());
				data.get(id).put("X", x);
				data.get(id).put("Y", y);
			}
			
			cr.close();
			
			String value;
			for(int year : years) {
				
				cr = new CsvReader(pathMotiver+"analyse/metrics_"+year+".csv");
				cr.setDelimiter(';');
				cr.readHeaders();
				
				List<String> vars = new LinkedList<String>();
				for(int v=0; v<cr.getHeaderCount(); v++) {
					String var = cr.getHeader(v);
					if(!var.equalsIgnoreCase("ID") && !var.equalsIgnoreCase("X") && !var.equalsIgnoreCase("Y")) {
						vars.add(var);
					}
				}
				
				while(cr.readRecord()) {
					
					id = cr.get("ID");
					
					if(year == 2023) {
						data.get(id).put("RASTER", "OSO_2023_site_1.tif");
					}
					
					for(String var : vars) {
						
						value = cr.get(var);
						data.get(id).put(var, value);
					}
				}
				
				cr.close();
			}
			
			for(int year : years) {
				
				System.out.println(year);
				
				for(int k : ks) {
					
					for(String size : sizes) {
						
						cr = new CsvReader(pathMotiver+"analyse/ecopaysages_OSO_"+year+"_site_"+k+"classes_"+size+".csv");
						cr.setDelimiter(';');
						cr.readHeaders();
						
						while(cr.readRecord()) {
							if(year < 2023) {
								id = cr.get("ID");
								value = cr.get("Central_3");
								data.get(id).put("EL_k"+k+"_"+size, value);
							}else {
								id = cr.get("id");
								value = cr.get("Central");
								data.get(id).put("EL_k"+k+"_"+size, value);
							}
						}
						
						cr.close();
						
						for(int k2=1; k2<=k; k2++) {
							
							cr = new CsvReader(pathMotiver+"analyse/membership_ecopaysages_OSO_"+year+"_site_"+k+"classes_ecop"+k2+"_"+size+".csv");
							cr.setDelimiter(';');
							cr.readHeaders();
							
							while(cr.readRecord()) {
								if(year < 2023) {
									id = cr.get("ID");
									value = cr.get("Central_3");
									data.get(id).put("EL"+k2+"_k"+k+"_"+size, value);
								}else {
									id = cr.get("id");
									value = cr.get("Central");
									data.get(id).put("EL"+k2+"_k"+k+"_"+size, value);
								}
							}
							
							cr.close();
						}
					}
				}
			}
			
			CsvWriter cw = new CsvWriter(pathMotiver+"analyse_metrics_temporal.csv");
			cw.setDelimiter(';');
			
			cw.write("ID");
			for(String var : data.values().iterator().next().keySet()) {
				
				cw.write(var);
			}
			cw.endRecord();
			
			for(Entry<String, Map<String, String>> entry : data.entrySet()) {
				
				cw.write(entry.getKey());
				
				for(Entry<String, String> entry2 : entry.getValue().entrySet()) {
					
					cw.write(entry2.getValue());
				}
				cw.endRecord();
			}
			
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private static void calculateMetrics() {
		
		int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
		
		int[] years = new int[] {2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023}; 
		
		for(int year : years) {
			
			System.out.println(year);
		
			LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
			builder.setAnalysisType(ChloeAnalysisType.SELECTED);
					
			builder.setWindowSizes(new int[]{81, 401, 2001});
			builder.setPointsFilter(pathMotiver+"site_motiver_point_"+year+".csv");
			
			if(new File(pathMotiver+"occsol/oso/site_0/OSO_"+year+"_site_0.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_0/OSO_"+year+"_site_0.tif");
			}
			if(new File(pathMotiver+"occsol/oso/site_1/OSO_"+year+"_site_1.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_1/OSO_"+year+"_site_1.tif");
			}
			if(new File(pathMotiver+"occsol/oso/site_2/OSO_"+year+"_site_2.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_2/OSO_"+year+"_site_2.tif");
			}
			if(new File(pathMotiver+"occsol/oso/site_3/OSO_"+year+"_site_3.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_3/OSO_"+year+"_site_3.tif");
			}
			if(new File(pathMotiver+"occsol/oso/site_4/OSO_"+year+"_site_4.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_4/OSO_"+year+"_site_4.tif");
			}
			if(new File(pathMotiver+"occsol/oso/site_5/OSO_"+year+"_site_5.tif").exists()) {
				builder.addRasterFile(pathMotiver+"occsol/oso/site_5/OSO_"+year+"_site_5.tif");
			}
			
			builder.setValues(values);
			
			builder.addMetric("SHDI");
			for(int v : values) {
				builder.addMetric("pNV_"+v);
			}
			
			builder.addCsvOutput(pathMotiver+"analyse/metrics_"+year+".csv");
			
			LandscapeMetricAnalysis analysis = builder.build();
			
			analysis.allRun();
			
		}
		
		
	}
	
	private static void exportValues() {
	
		int[] years = new int[] {2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023}; 
		
		int[] ks = new int[] {5, 6, 7, 8, 9, 10};
		
		for(int year : years) {
			
			System.out.println(year);
			
			for(int k : ks) {
				
				exportValues("ecopaysages_OSO_"+year+"_site", year, k, -1, 1000);
				exportValues("ecopaysages_OSO_"+year+"_site", year, k, -1, 5000);
				exportValues("ecopaysages_OSO_"+year+"_site", year, k, -1, 1000, 5000);
				
				for(int k2=1; k2<=k; k2++) {
					
					exportValues("membership_ecopaysages_OSO_"+year+"_site", year, k, k2, 1000);
					exportValues("membership_ecopaysages_OSO_"+year+"_site", year, k, k2, 5000);
					exportValues("membership_ecopaysages_OSO_"+year+"_site", year, k, k2, 1000, 5000);
				}
			}
		}
	}	
	
	private static void exportValues(String name, int year, int k, int k2, int... sizes) {

		String size = "";
		for(int s : sizes) {
			size += s+"m_";
		}
		size = size.substring(0, size.length()-1);
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.addMetric("Central");		
		builder.setWindowSizes(new int[]{3});
		builder.setPointsFilter(pathMotiver+"site_motiver_point_"+year+".csv");
		
		String rasterName;
		if(k2 == -1) {
			
			for(int site=0; site<=5; site++) {
				
				rasterName = pathMotiver+"ecolandscapes/ecolandscapes_site"+site+"/"+name+"_"+site+"_"+k+"classes_"+size+".tif";
				
				if(new File(rasterName).exists()) {
					
					System.out.println(rasterName);
			
					builder.addRasterFile(rasterName);
				}
			}
			builder.addCsvOutput(pathMotiver+"analyse/"+name+"_"+k+"classes_"+size+".csv");
			
		}else {
			
			for(int site=0; site<=5; site++) {
				
				rasterName = pathMotiver+"ecolandscapes/ecolandscapes_site"+site+"/"+name+"_"+site+"_"+k+"classes_ecop"+k2+"_"+size+".tif";
				
				if(new File(rasterName).exists()) {
					
					System.out.println(rasterName);
					
					builder.addRasterFile(rasterName);
				}
			}
			builder.addCsvOutput(pathMotiver+"analyse/"+name+"_"+k+"classes_ecop"+k2+"_"+size+".csv");
		}
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	private static void calculateEcolandscape(String param) {

		int[] ks = new int[] {5, 6, 7, 8 , 9, 10};
		
		for(int site=0; site<=5; site++) {
			
			String occsol = pathMotiver+"OSO_site_"+site+".tif";
			
			ecolandscape(param, occsol, ks, new int[] {1000});
				
			ecolandscape(param, occsol, ks, new int[] {5000});
					
			ecolandscape(param, occsol, ks, new int[] {1000, 5000});
		}
		
	}
	
	private static void ecolandscape(String param, String occsol, int[] ks, int... sizes) {
		
		System.out.println(occsol+" "+sizes[0]+" "+sizes.length);
		
		String completeName = new File(occsol).getName().replace(".tif", "");
		
		EcoPaysageManager epManager = new EcoPaysageManager(param);
		epManager.addInputRaster(occsol);
		epManager.setScales(sizes);
		epManager.setOutputFolder(pathMotiver+"ecolandscape_"+completeName+"/");
		epManager.setClasses(ks);
		epManager.setUnfilters(new int[] {-1});
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38});
		epManager.setCompositionMetrics();
		epManager.setConfigurationMetrics();
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}

	private static void recuperationOccSol() {
		
		for(int index=0; index<6; index++) {
			
			Coverage cov = CoverageManager.getCoverage(pathMotiver+"OS_site_"+index+".tif");
			EnteteRaster entete = cov.getEntete();
			cov.dispose();
			
			Coverage covOccsol = CoverageManager.getCoverage(pathOccsol);
			EnteteRaster enteteOcsol = covOccsol.getEntete();
			float[] dataOccsol = covOccsol.getData(EnteteRaster.getROI(enteteOcsol, entete.getEnvelope()));
			covOccsol.dispose();
			
			CoverageManager.write(pathMotiver+"OSO_site_"+index+".tif", dataOccsol, EnteteRaster.getEntete(enteteOcsol, entete.getEnvelope()));
		}
	}
	
}
