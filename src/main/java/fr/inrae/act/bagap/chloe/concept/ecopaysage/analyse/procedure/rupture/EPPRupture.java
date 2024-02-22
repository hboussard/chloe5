package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.rupture;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.mapping.EPPMapping;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class EPPRupture extends EcoPaysageProcedure {

	public EPPRupture(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(int k : manager().classes()) {
			if(!new File(manager().mapFile(k)).exists()) {
				force = true;
				break;
			}
		}
		
		if(force) {
			new EPPMapping(manager()).run();
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation de l'entete raster");
		
		EnteteRaster header = EcoPaysage.getHeader(manager().headerFile());
		
		System.out.println("analyse de fronts de rupture");
		
		EcoPaysage.analyseRuptures(manager().ruptureFile(), manager().mapFiles(), header);
		
	}
	
}
