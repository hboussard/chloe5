package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.HugeGrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence.HugeGBPCalculDistanceInfluenceBoisement;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class HugeGBPCalculGrainBocager extends GrainBocagerProcedure {

	public HugeGBPCalculGrainBocager(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covDistanceInfluenceBoisement;
		if(!new File(manager().distanceInfluenceBoisement()).exists()){
			covDistanceInfluenceBoisement = new HugeGBPCalculDistanceInfluenceBoisement(manager()).run();
		}else{
			covDistanceInfluenceBoisement = CoverageManager.getCoverage(manager().distanceInfluenceBoisement());
		}
		
		System.out.println("calcul du grain bocager à "+manager().grainCellSize()+"m dans une fenêtre de "+manager().grainWindowRadius()+"m");
		
		Coverage covGrainBocager = HugeGrainBocager.calculGrainBocager(manager().grainBocager(), covDistanceInfluenceBoisement, manager().entete(), manager().grainWindowRadius(), manager().grainCellSize(), manager().tile());
		
		if(!manager().grainBocager4Classes().equalsIgnoreCase("")){
			
			System.out.println("calcul des seuils du grain bocager");
			
			Coverage covGrainBocager4Classes = HugeGrainBocager.runClassificationNClasses(manager().grainBocager4Classes(), covGrainBocager, manager().entete().noDataValue(),  manager().seuils());
			
			covGrainBocager4Classes.dispose();
		}
		
		return covGrainBocager;
		
	}

}
