package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.calculmetrics.EPPCalculMetrics;

public class EPPStandardization extends EcoPaysageProcedure {

	private int scale;
	
	public EPPStandardization(EcoPaysageManager manager) {
		super(manager);
	}
	
	public EPPStandardization(EcoPaysageManager manager, int specificScale) {
		this(manager);
		this.scale = specificScale;
	}
	
	@Override
	public void doInit() {
		
		if(scale == 0) {
			scale = manager().scale();
		}
		
		if(manager().force() || !new File(manager().metricsFile(scale)).exists()){
			
			new EPPCalculMetrics(manager(), scale).run();
		}
		
		if(!new File(manager().xyFile()).exists()){
			
			System.out.println("generation du fichier de masque XY");
			
			EcoPaysage.generateMask(manager().metricsFile(), manager().xyFile());
		}
	}

	@Override
	public void doRun() {

		System.out.println("split des donnees de composition et de configuration");
		
		int count = EcoPaysage.splitGroups(manager().metricsFiles(scale), manager().xyFile(), manager().groupFiles(scale), manager().groupMetrics());
		
		float divisor;
		for(String group : manager().groupMetrics().keySet()) {
			
			System.out.println("standardisation des donnees du groupe "+group);
			
			divisor = EcoPaysage.standardize(manager().groupFiles(scale).get(group), count, manager().groupMetrics().get(group), manager().importances(), scale);
			
			manager().setDivisor(group+"_"+scale+"m", divisor);
		}
		
		EcoPaysage.exportDivisor(manager().divisorFile(), manager().divisors());
		
		System.out.println("compilation des donnees standardisees");
		
		Set<String> setSdtFiles = new LinkedHashSet<String>();
		
		for(Entry<String, String> groupFile : manager().groupFiles(scale).entrySet()) {	
			setSdtFiles.add(groupFile.getValue());
		}
		
		String[] sdtFiles = setSdtFiles.toArray(new String[setSdtFiles.size()]);
		
		EcoPaysage.compileStdFiles(manager().standardizedFile(scale), scale, sdtFiles);
		
	}
	
}
