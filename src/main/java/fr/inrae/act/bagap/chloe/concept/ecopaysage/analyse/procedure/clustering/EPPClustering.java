package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.clustering;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization.EPPStandardization;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class EPPClustering extends EcoPaysageProcedure {

	public EPPClustering(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		for(int scale : manager().scales()) {
			if(manager().force() || !new File(manager().normFile(scale)).exists()) {
				
				new EPPStandardization(manager(), scale).run();
			}
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
		
		System.out.println("integration des donnees");
		
		Instances data = null;
		if(manager().factor() == 1) {
			data = EcoPaysage.readData(manager().normFile());
		}else {
			data = EcoPaysage.readData(manager().normFile(), manager().factor(), dataXY.length);
		}
		
		SimpleKMeans kmeans;
		for(int k : manager().classes()) {
			
			System.out.println("clusterisation a "+k+" classes");
			
			kmeans = EcoPaysage.kmeans(data, k);
			
			System.out.println("export en csv vers "+manager().ecoFile(k));
			
			if(manager().factor() == 1) {
				EcoPaysage.exportCSV(manager().ecoFile(k), kmeans, k, dataXY, data);
			}else {
				EcoPaysage.exportCSV(manager().ecoFile(k), kmeans, k, dataXY, manager().normFile(), data);
			}
			
			System.out.println("export des infos vers "+manager().infoFile(k));
			
			EcoPaysage.exportInfo(manager().infoFile(k), kmeans, k, data);
		}
	
	}
	
}
