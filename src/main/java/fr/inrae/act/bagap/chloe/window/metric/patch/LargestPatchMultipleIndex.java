package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

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
			value = Math.max(value, co.maxSurface(vv));
		}
	}
	
}
