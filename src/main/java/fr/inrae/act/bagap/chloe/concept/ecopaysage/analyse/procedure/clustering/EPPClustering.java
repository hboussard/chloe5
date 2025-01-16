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
			if(manager().force() || !new File(manager().standardizedFile(scale)).exists()) {
				
				new EPPStandardization(manager(), scale).run();
			}
		}
	}
	
	@Override
	public void doRun() {
		
		System.out.println("recuperation du masque");
		
		String[][] dataXY = EcoPaysage.importXY(manager().xyFile());
		
		if(!new File(manager().standardizedFile()).exists()) {
			
			if(manager().hasMultipleScales()) {
				
				EcoPaysage.compileFiles(manager().standardizedFile(), dataXY.length, manager().standardizedFiles());
				
			}else {
				
				manager().setStandardizedFile(manager().standardizedFile(manager().scale()));
			}
		}
		
		System.out.println("integration des donnees");
		
		Instances data = null;
		if(manager().factor() == 1) {
			data = EcoPaysage.readData(manager().standardizedFile());
		}else {
			//data = EcoPaysage.readData(manager().standardizedFile(), manager().factor(), dataXY.length);
			data = EcoPaysage.readData(manager().standardizedFile(), dataXY, manager().factor(), manager().entete());
		}
		
		SimpleKMeans kmeans;
		for(int k : manager().classes()) {
			
			System.out.println("clusterisation a "+k+" classes");
			
			kmeans = EcoPaysage.kmeans(data, k);
			
			int index = 1;
			
			for(String inputRaster : manager().inputRasters()) {
				
				String carto = manager().carto(inputRaster);
				
				System.out.println("export en csv vers "+manager().ecoFile(carto, k));
				
				if(manager().factor() == 1) {
					EcoPaysage.exportCSV(manager().ecoFile(carto, k), kmeans, k, dataXY, data, index);
				}else {
					EcoPaysage.exportCSV(manager().ecoFile(carto, k), kmeans, k, dataXY, manager().standardizedFile(), data);
				}
				
				index++;
			}
			
			System.out.println("export des infos vers "+manager().infoFile(k));
			
			EcoPaysage.exportInfo(manager().infoFile(k), kmeans, k, data, manager().importances());
		}
	
	}
	
}
