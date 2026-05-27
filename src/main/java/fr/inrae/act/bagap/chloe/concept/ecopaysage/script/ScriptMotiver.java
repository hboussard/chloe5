package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMotiver {

	public static void main(String [] args) {
		
		//copyMNHC();
		//selected();
		
		//ecolandscapeDynamics();
		
		//evoMembershipDynamics();
		//intensityMembershipDynamics();
		
		//globalEvoMembershipDynamics();
		//globalIntensityMembershipDynamics();
		
		//retileEuropeToFrance(2006);
		//cleanFrance(2000);
		//cleanFrance(2001);
		//cleanFrance(2002);
		//cleanFrance(2003);
		//cleanFrance(2004);
		//cleanFrance(2005);
		//cleanFrance(2006);
		analyseFranceDynamics();
		
	}
	
	private static void cleanFrance(int year) {
		
		String path = "E:/data/continental_europe/france/";
		
		Coverage cov = CoverageManager.getCoverage(path+"france_"+year+".tif");
		EnteteRaster entete = cov.getEntete();
		float[] data = cov.getData();
		cov.dispose();
		
		entete.setNoDataValue(-1);
		
		int index = 0;
		for(float d : data) {
			
			if(d == 0 || d == 30 || d == 31){
				
				data[index] = 28;
				
			}else if(d == 32){
				
				data[index] = 30;
			}
			
			index++;
		}
		
		CoverageManager.write(path+"france_clean_"+year+".tif", data, entete);
	}
	
	private static void retileEuropeToFrance(int year) {
		
		String path = "E:/data/continental_europe/";
		
		Coverage covOS = CoverageManager.getCoverage(path+"lcv_landcover.hcl_lucas.corine.rf_p_30m_0..0cm_"+year+"_eumap_epsg3035_v0.1.tif");
		EnteteRaster entete = covOS.getEntete();		
		Rectangle ROI = new Rectangle(77000, 77000, 34000, 34000);
		EnteteRaster localEntete = EnteteRaster.getEntete(entete, ROI);
		float[] dataOS = covOS.getData(ROI);
		covOS.dispose();
		
		CoverageManager.write(path+"france/france_"+year+".tif", dataOS, localEntete);
	}
	
	private static void analyseFranceDynamics() {

		long begin = System.currentTimeMillis();

		String path = "E:/data/continental_europe/france/";
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.setForce(true);
		epManager.addInputRaster(path+"france_clean_2000.tif");
		epManager.addInputRaster(path+"france_clean_2001.tif");
		epManager.addInputRaster(path+"france_clean_2002.tif");
		epManager.addInputRaster(path+"france_clean_2003.tif");
		epManager.addInputRaster(path+"france_clean_2004.tif");
		epManager.addInputRaster(path+"france_clean_2005.tif");
		epManager.addInputRaster(path+"france_clean_2006.tif");
		epManager.setCodes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
		epManager.setScales(new int[]{3000});
		epManager.setDisplacement(10); // tous les 300m
		epManager.setOutputFolder(path+"ecolandscapes_2000-2006/");
		epManager.setClasses(new int[]{25});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		epProcedure.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}

	private static void globalEvoMembershipDynamics() {
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecolandscape_dynamic/";
		
		int nbK = 7;
		Coverage cov = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2015_7classes_ecop1_1000m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Integer, Map<Integer, float[]>> datas = new TreeMap<Integer, Map<Integer, float[]>>(); // per K, per year
		for(int k=1; k<=nbK; k++) {
			
			datas.put(k, new TreeMap<Integer, float[]>());
			
			for(int year=2015; year<=2018; year++) {
				
				cov = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_"+year+"_7classes_ecop"+k+"_1000m.tif");
				datas.get(k).put(year, cov.getData());
				cov.dispose();
			}
		}
		
		float[] data = new float[entete.height()*entete.width()];
		Arrays.fill(data, -1);
		for(int i=0; i<data.length; i++) {
			
			float[] data2015 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2015[k-1] = datas.get(k).get(2015)[i];
			}
			
			float[] data2018 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2018[k-1] = datas.get(k).get(2018)[i];
			}
			
			data[i] = EcoPaysage.distance(data2018, data2015);
		}
			
		CoverageManager.write(path+"global_evo_membership_ecopaysages_os_za_7classes_1000m.tif", data, entete);
	}
	
	private static void globalIntensityMembershipDynamics() {
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecolandscape_dynamic/";
		
		int nbK = 7;
		Coverage cov = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2015_7classes_ecop1_1000m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		Map<Integer, Map<Integer, float[]>> datas = new TreeMap<Integer, Map<Integer, float[]>>(); // per K, per year
		for(int k=1; k<=nbK; k++) {
			
			datas.put(k, new TreeMap<Integer, float[]>());
			
			for(int year=2015; year<=2018; year++) {
				
				cov = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_"+year+"_7classes_ecop"+k+"_1000m.tif");
				datas.get(k).put(year, cov.getData());
				cov.dispose();
			}
		}
		
		float[] data = new float[entete.height()*entete.width()];
		Arrays.fill(data, -1);
		for(int i=0; i<data.length; i++) {
			
			float[] data2015 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2015[k-1] = datas.get(k).get(2015)[i];
			}
			
			float[] data2016 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2016[k-1] = datas.get(k).get(2016)[i];
			}
			
			float[] data2017 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2017[k-1] = datas.get(k).get(2017)[i];
			}
			
			float[] data2018 = new float[nbK];
			for(int k=1; k<=nbK; k++) {
				data2018[k-1] = datas.get(k).get(2018)[i];
			}
			
			data[i] = EcoPaysage.distance(data2018, data2017) + EcoPaysage.distance(data2017, data2016) + EcoPaysage.distance(data2016, data2015);
		}
			
		CoverageManager.write(path+"global_intensity_membership_ecopaysages_os_za_7classes_1000m.tif", data, entete);
	}
	
	private static void evoMembershipDynamics() {
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecolandscape_dynamic/";
		
		int nbK = 7;
		EnteteRaster entete = null; 
		
		for(int k=1; k<=nbK; k++) {
			
			Coverage cov1 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2015_7classes_ecop"+k+"_1000m.tif");
			float[] data1 = cov1.getData();
			if(entete == null) {
				entete = cov1.getEntete();
				entete.setNoDataValue(999);
			}
			cov1.dispose();
			
			Coverage cov4 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2018_7classes_ecop"+k+"_1000m.tif");
			float[] data4 = cov4.getData();
			cov4.dispose();
			
			float[] data = new float[entete.height()*entete.width()];
			Arrays.fill(data, -1);
			for(int i=0; i<data.length; i++) {
				
				data[i] = (data4[i]- data1[i]);
			}
			
			CoverageManager.write(path+"evo_membership_ecopaysages_os_za_7classes_ecop"+k+"_1000m.tif", data, entete);
		}
	}
	
	private static void intensityMembershipDynamics() {
		
		String path = "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecolandscape_dynamic/";
		
		int nbK = 7;
		EnteteRaster entete = null; 
		
		for(int k=1; k<=nbK; k++) {
			
			Coverage cov1 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2015_7classes_ecop"+k+"_1000m.tif");
			float[] data1 = cov1.getData();
			if(entete == null) {
				entete = cov1.getEntete();
				entete.setNoDataValue(999);
			}
			cov1.dispose();
			
			Coverage cov2 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2016_7classes_ecop"+k+"_1000m.tif");
			float[] data2 = cov2.getData();
			cov2.dispose();
			
			Coverage cov3 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2017_7classes_ecop"+k+"_1000m.tif");
			float[] data3 = cov3.getData();
			cov3.dispose();
			
			Coverage cov4 = CoverageManager.getCoverage(path+"membership_ecopaysages_os_za_2018_7classes_ecop"+k+"_1000m.tif");
			float[] data4 = cov4.getData();
			cov4.dispose();
			
			
			float[] data = new float[entete.height()*entete.width()];
			Arrays.fill(data, -1);
			for(int i=0; i<data.length; i++) {
				
				data[i] = Math.abs(data4[i]- data3[i]) + Math.abs(data3[i]- data2[i]) + Math.abs(data2[i]- data1[i]);
			}
			
			CoverageManager.write(path+"intensity_membership_ecopaysages_os_za_7classes_ecop"+k+"_1000m.tif", data, entete);
		}
	}
	
	private static void ecolandscapeDynamics() {
		
		long begin = System.currentTimeMillis();
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.setForce(true);
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2015.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2016.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2017.tif");
		epManager.addInputRaster("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/os_za_2018.tif");
		epManager.setScales(new int[]{1000});
		epManager.setOutputFolder("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/ecolandscape2/");
		epManager.setClasses(new int[]{7});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		epProcedure.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void selected() {
		
		long begin = System.currentTimeMillis();
		
		String path = "C:\\Data\\temp\\motiver/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.addRasterFile(path+"OS_site_0.tif");
		builder.addMetric("SHDI");
		
		// analyse avec plusieurs tailles de fen�tre
		builder.setWindowSizes(new int[]{31, 51});
		builder.setPointsFilter(path+"points_site_0.csv");
		builder.addCsvOutput(path+"selected_pixels.csv");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
	private static void copyMNHC() {
		
		String inputPath = "D:/grain_bocager/data/";
		File folder = new File(inputPath);
		String outputPath = "D:/grain_bocager/mnhc/data/";
		
		for(File depFolder : folder.listFiles()) {
			
			//System.out.println(depFolder.getName());
			for(File yearFolder : depFolder.listFiles()) {
				
				//System.out.println(yearFolder.getName());
				
				for(String file : yearFolder.list()) {
					
					if(file.contains("hauteur_boisement")){
						//System.out.println(file);
						//System.out.println(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file);
						System.out.println("--> "+outputPath+file);
						//Tool.copy(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file, outputPath+file);
						try {
							Files.copy(new File(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file).toPath(), 
									new File(outputPath+file).toPath(), 
									StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
