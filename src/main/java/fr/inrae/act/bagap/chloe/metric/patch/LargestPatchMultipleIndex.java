package fr.inrae.act.bagap.chloe.metric.patch;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class LargestPatchMultipleIndex extends Metric implements PatchMetric {

	private short[] v;
	
	public LargestPatchMultipleIndex(short[] v){
		super();
		StringBuffer name = new StringBuffer("LPIm_");
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
		for(short vv : v){
			value = Math.max(value, co.getMaxSurface(vv));
		}
	}
	
}
