package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import fr.inrae.act.bagap.apiland.raster.Tile;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptFrance {

	public static void main(String[] args) {
		
		calculGrainBocager5m();
	}

	private static void calculGrainBocager5m() {
		
		String path = "E:/data/grain_bocager/tuile/2021-2023/";
		Tile tile = Tile.getTile("E:/projet/caphaie/occsol/raster/france/");
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setTile(tile);
		gbManager.setWoodHeight(path+"hauteur_boisement/");
		gbManager.setWoodType(path+"type_boisement/");
		gbManager.setInfluenceDistance(path+"distance_influence/");
		gbManager.setGrainBocager(path+"grain_bocager_5m/");
		gbManager.setExportGrainBocager4Classes(false);
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
}
