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
			
			//System.out.println("lecture des codes de la carte "+manager().inputRaster());
			
			//manager().setCodes(EcoPaysage.getCodes(manager().inputRaster()));
		}
		
	}

	@Override
	public void doRun() {
		
		System.out.println("calcul des metriques à l'echelle "+scale+"m");
		
		for(String inputRaster : manager().inputRasters()) {
			
			System.out.println("analyse de la carto "+inputRaster);
			
			EcoPaysage.calculMetrics(manager().metricsFile(manager().carto(inputRaster), scale), inputRaster, scale, manager().codes(), manager().compoMetrics(), manager().configMetrics(), manager().windowDistanceType(), manager().displacement(), manager().unfilters());
		}
		
	}

}
