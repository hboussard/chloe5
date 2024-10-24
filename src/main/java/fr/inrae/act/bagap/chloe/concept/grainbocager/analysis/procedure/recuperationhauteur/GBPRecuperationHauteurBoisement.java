package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur;

import java.io.IOException;

import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedureFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class GBPRecuperationHauteurBoisement extends GrainBocagerProcedure {

	public GBPRecuperationHauteurBoisement(GrainBocagerProcedureFactory factory, GrainBocagerManager manager) {
		super(factory, manager);
	}
	
	@Override
	public void doInit() {}

	@Override
	public void doRun() {
		
		Coverage covHauteurBoisement = GrainBocager.recuperationHauteurBoisement(manager().bocage(), manager().entete());
		
		if(manager().woodRemoval() != null){ // suppression des arrachages

			Coverage covZoneArrachage = GrainBocager.recuperationZoneArrachage(manager().woodRemoval(), manager().entete());
			
			float[] dataHauteurBoisement = covHauteurBoisement.getData();
			float[] dataZoneArrachage = covZoneArrachage.getData();
			
			covHauteurBoisement.dispose();
			covZoneArrachage.dispose();
			
			covHauteurBoisement = GrainBocager.supprimeZoneArrachage(dataHauteurBoisement, dataZoneArrachage, manager().entete());
			
		}
		
		if(manager().woodPlanting() != null){ // ajout des plantations

			Coverage covHauteurPlantation = null;
			
			if(manager().heightPlanting() != -1) { // hauteur de plantation uniforme
				
				covHauteurPlantation = GrainBocager.recuperationHauteurPlantation(manager().woodPlanting(), manager().heightPlanting(), manager().entete());
				
			} else { // hauteur de plantation declare par un attribut de la table attributaire
				
				covHauteurPlantation = GrainBocager.recuperationHauteurPlantation(manager().woodPlanting(), manager().heightPlantingAttribute(), manager().entete());
			}
			
			float[] dataHauteurBoisement = covHauteurBoisement.getData();
			float[] dataHauteurPlantation = covHauteurPlantation.getData();
			
			covHauteurBoisement.dispose();
			covHauteurPlantation.dispose();
			
			covHauteurBoisement = GrainBocager.ajouteHauteurPlantation(dataHauteurBoisement, dataHauteurPlantation, manager().entete());
			
		}
		
		CoverageManager.write(manager().woodHeight(), covHauteurBoisement.getData(), covHauteurBoisement.getEntete());
		covHauteurBoisement.dispose();
		
		try {
			Tool.copy(GrainBocagerManager.class.getResourceAsStream("style_hauteur_boisement.qml"), Tool.deleteExtension(manager().woodHeight())+".qml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
