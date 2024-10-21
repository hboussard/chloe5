package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.gradient;

import java.io.File;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.clustering.EPPClustering;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class EPPGradient extends EcoPaysageProcedure {

	public EPPGradient(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(int k : manager().classes()) {
			if(!new File(manager().ecoFile(k)).exists()) {
				force = true;
				break;
			}
		}
		
		if(force) {
			new EPPClustering(manager()).run();
		}
	}

	@Override
	public void doRun() {
	
		System.out.println("recuperation du masque");
		
		String[][] dataXY = EcoPaysage.importXY(manager().xyFile());
		
		if(!new File(manager().normFile()).exists()) {
			
			if(manager().hasMultipleScales()) {
				
				EcoPaysage.compileFiles(manager().normFile(), dataXY.length, manager().normFiles());
				
			}else {
				
				manager().setNormFile(manager().normFile(manager().scale()));
			}
		}
		
		for(int k : manager().classes()) {
			
			System.out.println("analyse des distances aux clustering pour "+k+" classes");
			
			EcoPaysage.analyseGradient(manager().gradientFile(k), dataXY, manager().infoFile(k), manager().normFile());
			
		}
		
		System.out.println("recuperation de l'entete raster");
		
		EnteteRaster header = EcoPaysage.getHeader(manager().headerFile(), manager().noDataValue());
		
		for(int k : manager().classes()) {
			
			for(int ik=1; ik<=k; ik++) {
				System.out.println("export cartographique vers "+manager().gradientMapFile(k, ik));
				
				EcoPaysage.exportMap(manager().gradientMapFile(k, ik), manager().gradientFile(k), ik, header);
			}
		}
	}
	
}
