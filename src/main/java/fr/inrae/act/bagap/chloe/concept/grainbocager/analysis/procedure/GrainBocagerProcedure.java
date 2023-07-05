package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

import fr.inrae.act.bagap.raster.Coverage;

public abstract class GrainBocagerProcedure {

	private GrainBocagerManager manager;
	
	public GrainBocagerProcedure(GrainBocagerManager manager){
		this.manager = manager;
	}
	
	protected GrainBocagerManager manager(){
		return manager;
	}
	
	public abstract Coverage run();
	
}
