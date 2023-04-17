package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class MeanPatchSizeMultipleIndex extends Metric implements PatchMetric {

	private short[] v;
	
	public MeanPatchSizeMultipleIndex(short[] v) {
		super();
		StringBuffer name = new StringBuffer("MPSm_");
		for(short vv : v){
			name.append(vv);
			name.append('&');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		double totalSurface = 0.0;
		int nbPatches = 0;
		for(short vv : v){
			totalSurface += co.getTotalSurface(vv);
			nbPatches += co.getNbPatches(vv);
		}
		if(nbPatches > 0){
			value = (double) totalSurface / nbPatches;
		}else{
			value = 0;
		}
	}

}
