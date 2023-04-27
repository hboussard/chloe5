package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MeanPatchSizeClassIndex extends Metric implements PatchMetric {

	private int classMetric;
	
	public MeanPatchSizeClassIndex(short cm) {
		super("MPS-class_"+cm);
		classMetric = cm;
	}

	@Override
	protected void doCalculate(Counting co) {
		int nbPatch = co.nbPatches(classMetric);
		if(nbPatch > 0){
			value = (double) co.totalSurface(classMetric)/nbPatch;
		}else{
			value = 0;
		}
		
	}

}
