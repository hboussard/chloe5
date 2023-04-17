package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class MeanPatchSizeIndex extends Metric implements PatchMetric {

	public MeanPatchSizeIndex() {
		super("MPS");
	}

	@Override
	protected void doCalculate(Counting co) {
		int nbPatch = co.getNbPatches();
		if(nbPatch > 0){
			value = (double) co.getTotalSurface()/nbPatch;
		}else{
			value = 0;
		}
	}

}
