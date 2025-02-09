package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class HugeGBPCalculGrainBocager extends GrainBocagerProcedure {

	public HugeGBPCalculGrainBocager(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() 
				|| new File(manager().influenceDistance()).list().length == 0){
			
			factory().parentFactory().create(manager()).run();
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation des distances d'infuence");
		
		Coverage covDistanceInfluenceBoisement = CoverageManager.getCoverage(manager().influenceDistance());
		
		System.out.println("calcul du grain bocager a "+manager().grainBocagerCellSize()+"m dans une fenetre de "+manager().grainBocagerWindowRadius()+"m");
		
		Coverage covGrainBocager = HugeGrainBocager.calculGrainBocager(manager().grainBocager(), covDistanceInfluenceBoisement, manager().entete(), manager().grainBocagerWindowRadius(), manager().grainBocagerCellSize(), manager().tile(), manager().fastMode());
		//Coverage covGrainBocager = CoverageManager.getCoverage(manager().grainBocager());
		
		System.out.println("calcul des seuils du grain bocager");
			
		Coverage covGrainBocager4Classes = HugeGrainBocager.runClassificationNClasses(manager().grainBocager4Classes(), covGrainBocager, manager().entete().noDataValue(),  manager().thresholds());
		//Coverage covGrainBocager4Classes = GrainBocager.runClassificationNClasses(manager().grainBocager(), manager().entete().noDataValue(),  manager().thresholds());
		
		//float[] data = covGrainBocager4Classes.getData();
		//EnteteRaster entete = covGrainBocager4Classes.getEntete();
		//covGrainBocager4Classes.dispose();
		//CoverageManager.write(manager().grainBocager4Classes(), data, entete);
		
		covGrainBocager4Classes.dispose();
		
	}

}
