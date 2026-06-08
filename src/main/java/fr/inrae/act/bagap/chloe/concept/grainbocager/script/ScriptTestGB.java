package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptTestGB {

	public static void main(String[] args) {
		
		String path = "D:/chloe/chloe5/data/";
		
		Envelope env = ShapeFileTool.getEnvelope(path+"rpg.shp");
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		
		gbManager.setBocage(path+"35_2023_hauteur_boisement.tif");
		gbManager.setOutputFolder(path+"test_territoire/");
		gbManager.setEnvelope("{"+env.getMinX()+";"+env.getMaxX()+";"+env.getMinY()+";"+env.getMaxY()+"}");
		gbManager.setOuputPrefix("essai1_");
		gbManager.setForce(false);
	
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}

}
