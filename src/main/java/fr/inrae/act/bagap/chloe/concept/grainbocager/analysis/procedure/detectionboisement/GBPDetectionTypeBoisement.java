package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement;

import java.io.File;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur.GBPRecuperationHauteurBoisement;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPDetectionTypeBoisement extends GrainBocagerProcedure {

	public GBPDetectionTypeBoisement(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covHauteurBoisement;
		if(manager().force() || !new File(manager().hauteurBoisement()).exists()){
			covHauteurBoisement = new GBPRecuperationHauteurBoisement(manager()).run();
		}else{
			covHauteurBoisement = CoverageManager.getCoverage(manager().hauteurBoisement());
		}
		
		System.out.println("détection des types de boisements");
		
		Coverage covTypeBoisement = GrainBocager.detectionTypeBoisement(covHauteurBoisement, manager().modeFast());
		
		covHauteurBoisement.dispose();
		
		if(!manager().typeBoisement().equalsIgnoreCase("")){
			CoverageManager.write(manager().typeBoisement(), covTypeBoisement.getData(), covTypeBoisement.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_type_boisement.qml"), Tool.deleteExtension(manager().typeBoisement())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}/*else{
			CoverageManager.write(manager().outputPath()+"type_boisement.tif", covTypeBoisement.getDatas(), covTypeBoisement.getEntete());
		}*/
		
		//covTypeBoisement.dispose();
		return covTypeBoisement;
	}

}