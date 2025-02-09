package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.rupture;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.mapping.EPPMapping;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class EPPRupture extends EcoPaysageProcedure {

	public EPPRupture(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(String carto : manager().cartos().values()) {
			for(int k : manager().classes()) {
				if(!new File(manager().mapFile(carto, k)).exists()) {
					force = true;
					break;
				}
			}
		}
		
		if(force) {
			new EPPMapping(manager()).run();
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation de l'entete raster");
		
		EnteteRaster header = EcoPaysage.getHeader(manager().headerFile(), manager().noDataValue());
		
		System.out.println("generation des fichiers de distance thematique");
		
		for(int k : manager().classes()) {
			
			EcoPaysage.generateThematicDistanceFile(manager().infoFile(k), manager().thematicDistanceFile(k));	
		}
		
		System.out.println("analyse de fronts de rupture");
		
		EcoPaysage.analyseRuptures(manager().ruptureFile(), manager().mapFiles(), manager().thematicDistanceFiles(), header);
		
	}
	
}
