package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager;

import java.io.File;
import java.io.IOException;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence.GBPCalculDistanceInfluenceBoisement;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPCalculGrainBocager extends GrainBocagerProcedure {

	public GBPCalculGrainBocager(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covDistanceInfluenceBoisement;
		if(!new File(manager().distanceInfluenceBoisement()).exists()){
			covDistanceInfluenceBoisement = new GBPCalculDistanceInfluenceBoisement(manager()).run();
		}else{
			covDistanceInfluenceBoisement = CoverageManager.getCoverage(manager().distanceInfluenceBoisement());
		}
		
		System.out.println("calcul du grain bocager à "+manager().cellSize()+"m dans une fenêtre de "+manager().windowRadius()+"m");
		
		Coverage covGrainBocager = GrainBocager.calculGrainBocager(covDistanceInfluenceBoisement, manager().windowRadius(), manager().cellSize());
		
		if(!manager().grainBocager().equalsIgnoreCase("")){
			CoverageManager.write(manager().grainBocager(), covGrainBocager.getDatas(), covGrainBocager.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager.qml"), Tool.deleteExtension(manager().grainBocager())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}/*else{
			CoverageManager.write(manager().outputPath()+"grain_bocager.tif", covGrainBocager.getDatas(), covGrainBocager.getEntete());
		}*/
		
		if(!manager().grainBocager4Classes().equalsIgnoreCase("")){
			
			Coverage covGrainBocager4Classes = GrainBocager.runClassificationNClasses(covGrainBocager, manager().seuils());
			
			CoverageManager.write(manager().grainBocager4Classes(), covGrainBocager4Classes.getDatas(), covGrainBocager4Classes.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_grain_bocager_4classes.qml"), Tool.deleteExtension(manager().grainBocager4Classes())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			covGrainBocager4Classes.dispose();
			
		}
		
		//covGrainBocager.dispose();
		return covGrainBocager;
		
	}

}