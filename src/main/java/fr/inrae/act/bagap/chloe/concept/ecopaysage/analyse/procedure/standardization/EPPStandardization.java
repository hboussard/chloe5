package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization;

import java.io.File;
import fr.inrae.act.bagap.apiland.util.Tool;
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
		
		//EcoPaysage.splitCompoConfig(manager().metricsFile(scale), manager().xyFile(), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		EcoPaysage.splitCompoConfig(manager().metricsFiles(scale), manager().xyFile(), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		
		System.out.println("standardisation des donnees de composition");
		
		float inertia;
		
		inertia = EcoPaysage.standardize(manager().compoFile(scale), manager().compoMetrics(), manager().importances(), scale);
		
		manager().setInertia("composition_"+scale+"m", inertia);
		
		System.out.println("standardisation des donnees de configuration");
		
		//EcoPaysage.standardize(manager().configFile(scale), manager().configMetrics());
		
		float[][] distances = null;
		//float[][] distances = Util.initThematicDistanceMap("E:/rennes_metropole/ecopaysage/distance/distance_neutre.txt");
		
		inertia = EcoPaysage.standardize(manager().configFile(scale), manager().configMetrics(), manager().importances(), distances, scale);
		
		manager().setInertia("configuration_"+scale+"m", inertia);
		
		System.out.println("compilation des donnees standardisees de composition et de configuration");
		
		EcoPaysage.compileStdCompoConfig(manager().standardizedFile(scale), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics(), scale);
		
		Tool.deleteFile(manager().compoFile(scale));
		Tool.deleteFile(manager().configFile(scale));
		
		EcoPaysage.exportInertia(manager().inertiaFile(), manager().inerties());
	}
	
	
}
