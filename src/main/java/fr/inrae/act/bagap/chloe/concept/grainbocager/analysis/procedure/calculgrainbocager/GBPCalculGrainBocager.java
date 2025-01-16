package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;
import java.io.IOException;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class GBPCalculGrainBocager extends GrainBocagerProcedure {
	
	public GBPCalculGrainBocager(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		if(manager().force() || !new File(manager().influenceDistance()).exists()){
			
			factory().parentFactory().create(manager()).run();
			
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation des distances d'infuence");
		
		Coverage covDistanceInfluenceBoisement = CoverageManager.getCoverage(manager().influenceDistance());
		
		System.out.println("calcul du grain bocager a "+manager().grainBocagerCellSize()+"m dans une fenetre de "+manager().grainBocagerWindowRadius()+"m");
		
		Coverage covGrainBocager = GrainBocager.calculGrainBocager(covDistanceInfluenceBoisement, manager().grainBocagerWindowRadius(), manager().grainBocagerCellSize(), manager().fastMode());
		
		CoverageManager.write(manager().grainBocager(), covGrainBocager.getData(), covGrainBocager.getEntete());
			
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager.qml"), Tool.deleteExtension(manager().grainBocager())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("calcul des seuils du grain bocager");
			
		Coverage covGrainBocager4Classes = GrainBocager.runClassificationNClasses(covGrainBocager, manager().entete().noDataValue(), manager().thresholds());
			
		covGrainBocager.dispose();
		
		CoverageManager.write(manager().grainBocager4Classes(), covGrainBocager4Classes.getData(), covGrainBocager4Classes.getEntete());
			
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager_4classes.qml"), Tool.deleteExtension(manager().grainBocager4Classes())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		covGrainBocager4Classes.dispose();
			
	}

}