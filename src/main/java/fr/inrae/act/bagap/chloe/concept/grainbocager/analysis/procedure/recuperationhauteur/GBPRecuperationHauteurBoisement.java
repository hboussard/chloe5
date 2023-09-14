package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur;

import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;

public class GBPRecuperationHauteurBoisement extends GrainBocagerProcedure {

	public GBPRecuperationHauteurBoisement(GrainBocagerManager manager) {
		super(manager);
	}

	@Override
	public Coverage run() {
		
		Coverage covHauteurBoisement = GrainBocager.recuperationHauteurBoisement(manager().bocage(), manager().entete());
		
		if(!manager().suppression().equalsIgnoreCase("")){ // suppression des arrachages

			Coverage covZoneArrachage = GrainBocager.recuperationZoneArrachage(manager().suppression(), manager().entete());
			
			float[] dataHauteurBoisement = covHauteurBoisement.getData();
			float[] dataZoneArrachage = covZoneArrachage.getData();
			
			covHauteurBoisement.dispose();
			covZoneArrachage.dispose();
			
			covHauteurBoisement = GrainBocager.supprimeZoneArrachage(dataHauteurBoisement, dataZoneArrachage, manager().entete());
			
		}
		
		if(!manager().plantation().equalsIgnoreCase("")){ // ajout des plantations

			Coverage covHauteurPlantation = GrainBocager.recuperationHauteurPlantation(manager().plantation(), manager().attributHauteurPlantation(), manager().entete());
			
			float[] dataHauteurBoisement = covHauteurBoisement.getData();
			float[] dataHauteurPlantation = covHauteurPlantation.getData();
			
			covHauteurBoisement.dispose();
			covHauteurPlantation.dispose();
			
			covHauteurBoisement = GrainBocager.ajouteHauteurPlantation(dataHauteurBoisement, dataHauteurPlantation, manager().entete());
			
		}
		
		if(!manager().hauteurBoisement().equalsIgnoreCase("")){
			
			CoverageManager.write(manager().hauteurBoisement(), covHauteurBoisement.getData(), covHauteurBoisement.getEntete());
			
			try {
				Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_hauteur_boisement.qml"), Tool.deleteExtension(manager().hauteurBoisement())+".qml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}/*else{
			CoverageManager.write(manager().outputPath()+"boisement.tif", covHauteurBoisement.getDatas(), covHauteurBoisement.getEntete());
		}*/
		
		//covHauteurBoisement.dispose();
		return covHauteurBoisement;
	}

}
