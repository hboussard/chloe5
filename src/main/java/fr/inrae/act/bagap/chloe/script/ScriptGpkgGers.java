package fr.inrae.act.bagap.chloe.script;

import java.io.File;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.GeoPackage2CoverageConverter;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptGpkgGers {

	public static void main(String[] args) {
		
		//convert();
		//compile();
		//cleanMNHC();
		//procedureGrainBocager("32", "2022");
		differenceHauteurBoisement();
	}
	
	private static void differenceHauteurBoisement() {
		
		Coverage cov2019 = CoverageManager.getCoverage("D:/grain_bocager/data/32/2019/32_2019_hauteur_boisement.tif");
		EnteteRaster entete2019 = cov2019.getEntete();
		float[] data2019 = cov2019.getData();
		cov2019.dispose();
		
		Coverage cov2022 = CoverageManager.getCoverage("D:/grain_bocager/data/32/2022/32_2022_hauteur_boisement.tif");
		EnteteRaster entete2022 = cov2022.getEntete();
		float[] data2022 = cov2022.getData(EnteteRaster.getROI(entete2022, entete2019.getEnvelope()));
		cov2022.dispose();
		
		entete2019.setNoDataValue(-9999);
		
		float[] dataDifference = new float[data2019.length]; 
		
		for(int i=0; i<data2019.length; i++) {
			float v2019 = data2019[i];
			float v2022 = data2022[i];
			if(v2019 != -1 && v2022 != -1) {
				dataDifference[i] = v2022 - v2019;
			}else {
				dataDifference[i] = -9999;
			}
		}
		
		CoverageManager.write("D:/grain_bocager/data/32/test/32_2022_hauteur_boisement.tif", dataDifference, entete2019);
	}
	
	private static void procedureGrainBocager(String numDep, String annee) {
		
		calculGrainBocager5m(numDep, annee);
		calculGrainBocager50m(numDep, annee);
		calculZoneEnjeux(numDep, annee, 1);
		calculZoneEnjeux(numDep, annee, 5);
		
	}
	
	private static void calculGrainBocager5m(String numDep, String annee) {
		

		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}

	private static void calculGrainBocager50m(String numDep, String annee) {
		
		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setWoodHeight(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculZoneEnjeux(String numDep, String annee, int km) {
		
		int ewr = km * 1000;
		
		String outputPath = "D:/grain_bocager/data/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
		gbManager.setFastMode(true); 
		gbManager.setGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setFunctionalGrainBocager(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_fonctionnel_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_grain_bocager_cluster_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(outputPath+numDep+"/"+annee+"/"+numDep+"_"+annee+"_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void convert() {
		String path = "D:/sig/cosia/CoSIA_D032_2022/CoSIA_D032_2022/";
		File folder = new File(path);
		Util.createAccess("D:/sig/cosia/raster/CoSIA_D032_2022/");
		String name;
		for(String f : folder.list()) {
			if(f.endsWith(".gpkg")) {
				System.out.println(path+f);
				name = f.replace(".gpkg", ".tif");
				GeoPackage2CoverageConverter.rasterize("D:/sig/cosia/raster/CoSIA_D032_2022/"+name, path+f, "numero", -1, 5, -1, null);
			}
		}
	}
	
	private static void compile() {
		
		String path = "D:/sig/cosia/raster/";
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setRasterTile(path+"CoSIA_D032_2022/");
		builder.setWindowSize(3);
		builder.addMetric("Central");		
		builder.addGeoTiffOutput(path+"CoSIA_D032_2022_5m.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void cleanMNHC() {
		
		Coverage covMNHC = CoverageManager.getCoverage("D:/grain_bocager/data/32/2022/32_2022_hauteur_boisement.tif");
		EnteteRaster entete = covMNHC.getEntete();
		float[] dataMNHC = covMNHC.getData();
		covMNHC.dispose();
		
		Coverage covCosia = CoverageManager.getCoverage("D:/sig/cosia/raster/CoSIA_D032_2022_5m.tif");
		float[] dataCosia = covCosia.getData();
		covCosia.dispose();
		
		for(int i=0; i<dataMNHC.length; i++) {
			float vMNHC = dataMNHC[i];
			if(vMNHC != entete.noDataValue()) {
				float vCosia = dataCosia[i];
				if(vCosia == 11) { // vigne
					dataMNHC[i] = 0;
				}
			}
		}
		
		Util.createAccess("D:/grain_bocager/data/32/2022_clean/");
		CoverageManager.write("D:/grain_bocager/data/32/2022_clean/32_2022_hauteur_boisement.tif", dataMNHC, entete);
		
	}
	
	
	/*
	private static void test() {
		String path = "D:/sig/cosia/CoSIA_D032_2022/CoSIA_D032_2022/";
		try {
			GeoPackage gp = new GeoPackage(new File(path+"D032_2022_550_6290_vecto.gpkg"));
			//GeoPackage gp = new GeoPackage(new File(path));
			
			//GeoPackageReader reader = new GeoPackageReader(path+"D032_2022_550_6290_vecto.gpkg", null);
			
			System.out.println(gp);
			
			//FeatureEntry fe = new FeatureEntry();
			//SimpleFeatureReader reader = gp.reader(fe, null, null);
			
			FeatureEntry fe = gp.features().get(0);
			//SimpleFeatureReader sfr = gp.reader(fe, filter, new DefaultTransaction());
			SimpleFeatureReader sfr = gp.reader(fe, null, null);

			int index = 0;
			while(sfr.hasNext()) {
			    SimpleFeature sf = sfr.next();
			    // Do things...
			    System.out.println(index++);
			    
			    //for(int at=0; at<sf.getAttributeCount(); at++) {
			    //	System.out.print(sf.getAttribute(at)+" ");
			   // }
			    System.out.println(sf.getDefaultGeometry().getClass());
			    System.out.println(sf.getAttribute("numero"));
			   // System.out.println();
			}
			sfr.close();
			gp.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
