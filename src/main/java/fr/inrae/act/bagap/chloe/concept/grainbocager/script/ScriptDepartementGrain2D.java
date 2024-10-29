package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptDepartementGrain2D{

	//private static final String path = "E:/FRC_AURA/data/grain2d/";
	//private static final String path = "E:/CERFE/grain2d/";
	//private static final String path = "E:/FRC_OCCITANIE/data/grain2d/";
	private static final String path = "D:/grain_bocager/grain2d/";
	
	public static void main(String[] args) {
		
		/*
		prepaMNH("01");
		prepaMNH("03");
		prepaMNH("26");
		prepaMNH("38");
		prepaMNH("42");
		*/
		
		
		/*
		calculGrainBocager5m("01");
		calculGrainBocager5m("03");
		calculGrainBocager5m("26");
		calculGrainBocager5m("38");
		calculGrainBocager5m("42");
		*/
		/*
		calculGrainBocager50m("01");
		calculGrainBocager50m("03");
		calculGrainBocager50m("26");
		calculGrainBocager50m("38");
		calculGrainBocager50m("42");
		*/
		/*
		calculZoneEnjeux("01", 1);
		calculZoneEnjeux("03", 1);
		calculZoneEnjeux("26", 1);
		calculZoneEnjeux("38", 1);
		calculZoneEnjeux("42", 1);
		*/
		/*
		calculZoneEnjeux("01", 5);
		calculZoneEnjeux("03", 5);
		calculZoneEnjeux("26", 5);
		calculZoneEnjeux("38", 5);
		calculZoneEnjeux("42", 5);
		*/
		/*
		prepaMNH("08");
		calculGrainBocager5m("08");
		calculGrainBocager50m("08");
		calculZoneEnjeux("08", 1);
		calculZoneEnjeux("08", 5);
		*/
		
		//analyseDepartement("31");
		//analyseDepartement("34");
		//analyseDepartement("81");
		//analyseDepartement("43");
		//analyseDepartement("69");
		analyseDepartement("47");
	}
	
	private static void analyseDepartement(String codeDpt) {
		prepaMNH(codeDpt);
		calculGrainBocager5m(codeDpt);
		calculGrainBocager50m(codeDpt);
		calculZoneEnjeux(codeDpt, 1);
		calculZoneEnjeux(codeDpt, 5);
	}

	private static void prepaMNH(String dpt) {
		
		String localPath = path+dpt+"/";
		Util.createAccess(localPath+"data/");
		
		Envelope env = ShapeFile2CoverageConverter.getEnvelope(localPath+"data/dpt_"+dpt+".shp", 2500);
		EnteteRaster entete = EnteteRaster.getEntete(env, 5, -1);
		
		/*
		Coverage covDpt = ShapeFile2CoverageConverter.getSurfaceCoverage(localPath+"data/dpt_"+dpt+".shp", entete, 1, -1);
		float[] dataDpt = covDpt.getData();
		covDpt.dispose();
		CoverageManager.write(localPath+"departement_"+dpt+".tif", dataDpt, entete);
		dataDpt = null;
		*/
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("Bois", 10);
		codes.put("Forêt fermée de feuillus", 10);
		codes.put("Forêt fermée de conifères", 10);
		codes.put("Forêt fermée mixte", 10);
		codes.put("Forêt ouverte", 10);
		codes.put("Haie", 10);
		codes.put("Lande ligneuse", 0);
		codes.put("Peupleraie", 10);
		codes.put("Verger", 0);
		codes.put("Vigne", 0);
		
		Coverage covWood = ShapeFile2CoverageConverter.getSurfaceCoverage(localPath+"data/ZONE_DE_VEGETATION.shp", "NATURE", codes, entete, 0);
		float[] dataWood = covWood.getData();
		covWood.dispose();
		
		Coverage covHedge = ShapeFile2CoverageConverter.getLinearCoverage(localPath+"data/HAIE.shp", entete, 10, 0, 5);
		float[] dataHedge = covHedge.getData();
		covHedge.dispose();
		
		for(int i=0; i<entete.width()*entete.height(); i++) {
			dataWood[i] = Math.max(dataHedge[i], dataWood[i]);
		}
		CoverageManager.write(localPath+"data/MNH_"+dpt+".tif", dataWood, entete);
	}
	
	private static void calculGrainBocager5m(String dpt) {
		
		String localPath = path+dpt+"/";
		String outputPath = localPath+"grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setWoodHeight(localPath+"data/MNH_"+dpt+".tif");
		gbManager.setWoodType(outputPath+dpt+"_type_boisement.tif");
		gbManager.setInfluenceDistance(outputPath+dpt+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+dpt+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+dpt+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculGrainBocager50m(String dpt) {
		
		String localPath = path+dpt+"/";
		String outputPath = localPath+"grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setFastMode(true);
		gbManager.setBufferArea(0);
		gbManager.setThresholds(0.2, 0.33, 0.45);
		gbManager.setGrainBocagerCellSize(50.0);
		gbManager.setInfluenceDistance(outputPath+dpt+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+dpt+"_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+dpt+"_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculZoneEnjeux(String dpt, int km) {
		
		int ewr = km * 1000;
		
		String localPath = path+dpt+"/grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("global_issues_calculation");
		gbManager.setFastMode(true); 
		gbManager.setGrainBocager(localPath+dpt+"_grain_bocager_50m.tif");
		gbManager.setFunctionalGrainBocager(localPath+dpt+"_grain_bocager_fonctionnel_50m.tif");
		gbManager.setFunctionalGrainBocagerClustering(localPath+dpt+"_grain_bocager_cluster_50m.tif");
		gbManager.setIssuesCellSize(200);
		gbManager.setIssuesWindowRadius(ewr);
		gbManager.setFunctionalGrainBocagerProportion(localPath+dpt+"_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setFunctionalGrainBocagerFragmentation(localPath+dpt+"_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
