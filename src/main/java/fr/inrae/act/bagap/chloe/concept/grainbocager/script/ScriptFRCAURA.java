package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptFRCAURA {

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
	}

	private static void prepaMNH(String dpt) {
		
		String path = "E:/FRC_AURA/data/grain2d/"+dpt+"/";
		
		Envelope env = ShapeFile2CoverageConverter.getEnvelope(path+"dpt_"+dpt+".shp", 2500);
		EnteteRaster entete = EnteteRaster.getEntete(env, 5, -1);
		
		/*
		Coverage covDpt = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"dpt_"+dpt+".shp", entete, 1, -1);
		float[] dataDpt = covDpt.getData();
		covDpt.dispose();
		CoverageManager.write(path+"departement_"+dpt+".tif", dataDpt, entete);
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
		
		Coverage covWood = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"data/ZONE_DE_VEGETATION.shp", "NATURE", codes, entete, 0);
		float[] dataWood = covWood.getData();
		covWood.dispose();
		
		Coverage covHedge = ShapeFile2CoverageConverter.getLinearCoverage(path+"data/HAIE.shp", entete, 10, 0, 5);
		float[] dataHedge = covHedge.getData();
		covHedge.dispose();
		
		for(int i=0; i<entete.width()*entete.height(); i++) {
			dataWood[i] = Math.max(dataHedge[i], dataWood[i]);
		}
		CoverageManager.write(path+"data/MNH_"+dpt+".tif", dataWood, entete);
	}
	
	private static void calculGrainBocager5m(String dpt) {
		
		String path = "E:/FRC_AURA/data/grain2d/"+dpt+"/";
		String outputPath = path+"grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setBufferArea(0);
		gbManager.setSeuils(0.2, 0.33, 0.45);
		gbManager.setHauteurBoisement(path+"data/MNH_"+dpt+".tif");
		gbManager.setTypeBoisement(outputPath+dpt+"_type_boisement.tif");
		gbManager.setDistanceInfluenceBoisement(outputPath+dpt+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+dpt+"_grain_bocager_5m.tif");
		gbManager.setGrainBocager4Classes(outputPath+dpt+"_grain_bocager_5m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculGrainBocager50m(String dpt) {
		
		String path = "E:/FRC_AURA/data/grain2d/"+dpt+"/";
		String outputPath = path+"grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_grain_bocager");
		gbManager.setModeFast(true);
		gbManager.setBufferArea(0);
		gbManager.setSeuils(0.2, 0.33, 0.45);
		gbManager.setGrainCellSize(50.0);
		gbManager.setDistanceInfluenceBoisement(outputPath+dpt+"_distance_influence.tif");
		gbManager.setGrainBocager(outputPath+dpt+"_grain_bocager_50m.tif");
		gbManager.setGrainBocager4Classes(outputPath+dpt+"_grain_bocager_50m_4classes.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
	private static void calculZoneEnjeux(String dpt, int km) {
		
		int ewr = km * 1000;
		
		String path = "E:/FRC_AURA/data/grain2d/"+dpt+"/grain_bocager/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("calcul_enjeux_globaux");
		gbManager.setModeFast(true); 
		gbManager.setGrainBocager(path+dpt+"_grain_bocager_50m.tif");
		gbManager.setGrainBocagerFonctionnel(path+dpt+"_grain_bocager_fonctionnel_50m.tif");
		gbManager.setClusterGrainBocagerFonctionnel(path+dpt+"_grain_bocager_cluster_50m.tif");
		gbManager.setEnjeuxCellSize(200);
		gbManager.setEnjeuxWindowRadius(ewr);
		gbManager.setProportionGrainBocagerFonctionnel(path+dpt+"_proportion_grain_bocager_fonc_"+km+"km.tif");
		gbManager.setZoneFragmentationGrainBocagerFonctionnel(path+dpt+"_fragmentation_grain_bocager_fonc_"+km+"km.tif");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
		
	}
	
}
