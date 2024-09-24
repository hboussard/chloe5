package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.calculmetrics;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;

public class EPPCalculMetrics extends EcoPaysageProcedure {

	private int scale;
	
	public EPPCalculMetrics(EcoPaysageManager manager) {
		super(manager);
	}
	
	public EPPCalculMetrics(EcoPaysageManager manager, int specificScale) {
		this(manager);
		this.scale = specificScale;
	}
	
	@Override
	public void doInit() {
		
		if(scale == 0) {
			scale = manager().scale();
		}
		
		if(!manager().initMetrics()) {
			
			System.out.println("lecture des codes de la carte "+manager().inputRaster());
			
			manager().setCodes(EcoPaysage.getCodes(manager().inputRaster()));
		}
		
	}

	@Override
	public void doRun() {
		
		System.out.println("calcul des metriques à l'echelle "+scale+"m");
		
		EcoPaysage.calculMetrics(manager().metricsFile(scale), manager().inputRaster(), scale, manager().compoMetrics(), manager().configMetrics(), manager().displacement(), manager().unfilters());
		
	}

}
