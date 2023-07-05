package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite.GBPClusterisationFonctionnalite;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPCalculEnjeuxGlobaux extends GrainBocagerProcedure {

	public GBPCalculEnjeuxGlobaux(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {

		Coverage covClusterFonctionnel;
		if(!new File(manager().clusterGrainBocagerFonctionnel()).exists()){
			covClusterFonctionnel = new GBPClusterisationFonctionnalite(manager()).run();
		}else{
			covClusterFonctionnel = CoverageManager.getCoverage(manager().clusterGrainBocagerFonctionnel());
		}
		
		System.out.println("calcul des zones de fragmentation de grain bocager fonctionnel à "+manager().enjeuxCellSize()+"m en utilisant une fenêtre de "+manager().enjeuxWindowRadius()+"m");
		
		Coverage covFragmentationGrainBocagerFonctionnel = GrainBocager.runSHDIClusterGrainBocagerFonctionnel(covClusterFonctionnel, manager().enjeuxWindowRadius(), manager().enjeuxCellSize(), manager().modeFast());
		
		covClusterFonctionnel.dispose();
		
		if(!manager().zoneFragmentationGrainBocagerFonctionnel().equalsIgnoreCase("")){
			
			CoverageManager.write(manager().zoneFragmentationGrainBocagerFonctionnel(), covFragmentationGrainBocagerFonctionnel.getDatas(), covFragmentationGrainBocagerFonctionnel.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_fragmentation_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().zoneFragmentationGrainBocagerFonctionnel())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		covFragmentationGrainBocagerFonctionnel.dispose();
		
		Coverage covGrainBocagerFonctionnel = CoverageManager.getCoverage(manager().grainBocagerFonctionnel());
		
		System.out.println("calcul des proportions de grain bocager fonctionnel dans une fenêtre de "+manager().enjeuxWindowRadius()+"m avec une resolution de "+manager().enjeuxCellSize()+"m");
		
		Coverage covProportionGrainBocagerFonctionnel = GrainBocager.runProportionGrainBocagerFonctionnel(covGrainBocagerFonctionnel, manager().enjeuxWindowRadius(), manager().enjeuxCellSize(), manager().modeFast());
		
		covGrainBocagerFonctionnel.dispose();
		
		if(!manager().proportionGrainBocagerFonctionnel().equalsIgnoreCase("")){
			
			CoverageManager.write(manager().proportionGrainBocagerFonctionnel(), covProportionGrainBocagerFonctionnel.getDatas(), covProportionGrainBocagerFonctionnel.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_proportion_grain_bocager_fonctionnel.qml"), Tool.deleteExtension(manager().proportionGrainBocagerFonctionnel())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return covProportionGrainBocagerFonctionnel;
		
	}

}
