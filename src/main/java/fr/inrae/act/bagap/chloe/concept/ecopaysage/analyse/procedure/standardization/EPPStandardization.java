package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization;

import java.io.File;

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
		
		EcoPaysage.splitCompoConfig(manager().metricsFile(scale), manager().xyFile(), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		
		System.out.println("standardisation des donnees de composition");
		
		EcoPaysage.normalize(manager().compoFile(scale), manager().compoMetrics());
		
		System.out.println("standardisation des donnees de configuration");
		
		EcoPaysage.normalize(manager().configFile(scale), manager().configMetrics());
		
		System.out.println("compilation des donnees standardisees de composition et de configuration");
		
		EcoPaysage.compileNormCompoConfig(manager().normFile(scale), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		
	}
	
	
}
