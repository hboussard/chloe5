package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MeanPatchSizeIndex extends Metric implements PatchMetric {

	public MeanPatchSizeIndex() {
		super("MPS");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			int nbPatch = co.nbPatches();
			if(nbPatch > 0){
				value = (double) co.totalSurface()/nbPatch;
			}else{
				value = 0;
			}
		}
	}

}
