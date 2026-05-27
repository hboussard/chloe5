package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;

public class ScriptCleanMNHCCosia {

	public static void main(String[] args) {
	
		//compileCosiaFrance();
		//cleanTuilesMNHC();
		//calculGrainBocager();
		
		String depPath = "E:/data/grain_bocager/v26/data2/35/2023/";
		//calculGrainBocagerWoodTypeDetection(depPath, 35, 2023);
		procedureGrainBocager(depPath, 35, 2023);
	}
	
	private static void calculGrainBocagerWoodTypeDetection(String depPath, int numDep, int annee) {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("wood_type_detection");
		gbManager.setWoodHeight(depPath+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(depPath+numDep+"_"+annee+"_type_boisement.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void calculGrainBocager() {
		
		String gbv25path = "D:/grain_bocager/data/";
		String gbv26path = "E:/data/grain_bocager/v26/data/";
		
		Map<Integer, Integer> departements = new TreeMap<Integer, Integer>();
		departements.put(35, 2023);
		
		String pathMNHC = "E:/data/grain_bocager/v26/tuiles/france/";
		Coverage covMNHC = CoverageManager.getCoverage(pathMNHC);
		EnteteRaster enteteMNHC = covMNHC.getEntete();
		
		Coverage covGB25;
		EnteteRaster entete;
		Envelope envelope;
		float[] dataMNHC;
		String depPath;
		for(Entry<Integer, Integer> departement : departements.entrySet()) {
			
			covGB25 = CoverageManager.getCoverage(gbv25path+departement.getKey()+"/"+departement.getValue()+"/"+departement.getKey()+"_"+departement.getValue()+"_hauteur_boisement.tif");
			entete = covGB25.getEntete();
			covGB25.dispose();
			
			envelope = entete.getEnvelope();
			dataMNHC = covMNHC.getData(EnteteRaster.getROI(enteteMNHC, envelope));
			
			depPath = gbv26path+departement.getKey()+"/"+departement.getValue()+"/";
			
			Util.createAccess(depPath);
			
			CoverageManager.write(depPath+departement.getKey()+"_"+departement.getValue()+"_hauteur_boisement.tif", dataMNHC, entete);
			
			procedureGrainBocager(depPath, departement.getKey(), departement.getValue());
		}
		
		covMNHC.dispose();
	}
	
	private static void procedureGrainBocager(String depPath, int numDep, int annee) {
		
		calculGrainBocager5m(depPath, numDep, annee);
		calculGrainBocager50m(depPath, numDep, annee);
		calculZoneEnjeux(depPath, numDep, annee, 1);
		calculZoneEnjeux(depPath, numDep, annee, 5);
		
	}
	
	private static void calculGrainBocager5m(String depPath, int numDep, int annee) {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setWoodHeight(depPath+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(depPath+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(depPath+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(depPath+numDep+"_"+annee+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(depPath+numDep+"_"+annee+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}

	private static void calculGrainBocager50m(String depPath, int numDep, int annee) {
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setWoodHeight(depPath+numDep+"_"+annee+"_hauteur_boisement.tif");
		gbManager.setWoodType(depPath+numDep+"_"+annee+"_type_boisement.tif");
		gbManager.setInfluenceDistance(depPath+numDep+"_"+annee+"_distance_influence.tif");
		gbManager.setGrainBocager(depPath+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(depPath+numDep+"_"+annee+"_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void calculZoneEnjeux(String depPath, int numDep, int annee, int km) {
		
		int ewr = km * 1000;
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
		gbManager.setGrainBocager(depPath+numDep+"_"+annee+"_grain_bocager_50m.tif");
		gbManager.setFunctionalGrainBocager(depPath+numDep+"_"+annee+"_grain_bocager_fonctionnel_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(depPath+numDep+"_"+annee+"_grain_bocager_cluster_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(depPath+numDep+"_"+annee+"_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(depPath+numDep+"_"+annee+"_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void compileCosiaFrance() {
		
		String pathCosia = "E:/data/cosia/raster/";
		File folderCosia = new File(pathCosia+"departement/");
		
		File deptFolder;
		int it, jt;
		String outFile;
		String[] f;
		Coverage covOutFile, covInFile;
		float[] dataOutFile, dataInFile;
		EnteteRaster entete;
		for(String input : folderCosia.list()){
			deptFolder = new File(pathCosia+"departement/"+input+"/");
			
			System.out.println(pathCosia+input+"/");
			
			for(String file : deptFolder.list()){
				if(file.endsWith(".tif")){
					f = file.replace(".tif", "").split("_");
					
					it = Integer.parseInt(f[3]);
					jt = Integer.parseInt(f[4]);
					//System.out.println(it+" "+jt);
					
					outFile = pathCosia+"france_2021_2024/cosia_"+it+"_"+jt+".tif";
					//System.out.println(outFile);
					
					if(new File(outFile).exists()){
						
						//System.out.println("comparaison pour : "+outFile);
						
						//compare and update
						covOutFile = CoverageManager.getCoverage(outFile);
						dataOutFile = covOutFile.getData();
						entete = covOutFile.getEntete();
						covOutFile.dispose();
						
						covInFile = CoverageManager.getCoverage(deptFolder.getAbsolutePath()+"/"+file);
						dataInFile = covInFile.getData();
						covInFile.dispose();
						
						Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(dataOutFile, dataOutFile, dataInFile){
							@Override
							protected float doTreat(float[] v) {
								return Math.max(v[0], v[1]);
							}
						};
						pptc.run();
						CoverageManager.writeGeotiff(outFile, dataOutFile, entete);
						
					}else{
						Tool.copy(deptFolder.getAbsolutePath()+"/"+file, outFile);
					}
				}
			}
		}
		
	}
	
	private static void cleanTuilesMNHC() {
		
		String pathMNHC = "C:/Data/projet/grain_bocager/data_mnhc/mnhc_france_2021-2023/";
		File folderMNHC = new File(pathMNHC);
		
		String pathCosia = "E:/data/cosia/raster/france_2021_2024/";
		Coverage covCosia = CoverageManager.getCoverage(pathCosia);
		EnteteRaster enteteCosia = covCosia.getEntete();
		
		String outputPath = "E:/data/grain_bocager/v26/tuiles/france/";
		Util.createAccess(outputPath);
		
		int index = 0;
		Coverage covMNHC;
		float[] dataMNHC, dataCosia;
		EnteteRaster entete;
		Envelope envelope;
		for(String tuileMNHC : folderMNHC.list()) {
			
			if(tuileMNHC.endsWith(".tif")) {
			
				System.out.println((++index)+" "+pathMNHC+tuileMNHC);
				
				covMNHC = CoverageManager.getCoverage(pathMNHC+tuileMNHC);
				dataMNHC = covMNHC.getData();
				entete = covMNHC.getEntete();
				covMNHC.dispose();
				
				envelope = entete.getEnvelope();
				
				dataCosia = covCosia.getData(EnteteRaster.getROI(enteteCosia, envelope));
				
				float[] data = new float[entete.width()*entete.height()];
				
				Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(data, dataCosia, dataMNHC) {
					@Override
					protected float doTreat(float[] v) {
						
						if(v[1] == -1) {
							
							return -1;
						}
						if(v[0] == 8 || v[0] == 9 || v[0] == 10) { // coniferes, feuillus ou brousailles
							
							if(v[1] > 0) {
								
								return v[1];
							}
							
							return 3;
						}
						
						return 0;
					}
				};
				pptc.run();
				
				CoverageManager.write(outputPath+tuileMNHC, data, entete);
			}
		}
		
		covCosia.dispose();
	}

}
