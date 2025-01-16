package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.mapping;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.clustering.EPPClustering;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class EPPMapping extends EcoPaysageProcedure {

	public EPPMapping(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(String inputRaster : manager().inputRasters()) {
			String carto = manager().carto(inputRaster);
			for(int k : manager().classes()) {
				if(!new File(manager().ecoFile(carto, k)).exists()) {
					force = true;
					break;
				}
			}
		}
		
		if(force) {
			new EPPClustering(manager()).run();
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation de l'entete raster");
		
		EnteteRaster header = EcoPaysage.getHeader(manager().headerFile(), manager().noDataValue());
		
		for(String inputRaster : manager().inputRasters()) {

			String carto = manager().carto(inputRaster);
			
			for(int k : manager().classes()) {
				
				System.out.println("export cartographique vers "+manager().mapFile(carto, k));
				
				EcoPaysage.exportMap(manager().mapFile(carto, k), manager().ecoFile(carto, k), k, header);
			}	
		}
		
	}
	
}
