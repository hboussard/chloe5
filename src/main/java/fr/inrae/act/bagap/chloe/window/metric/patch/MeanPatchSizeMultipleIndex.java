package fr.inrae.act.bagap.chloe.window.metric.patch;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MeanPatchSizeMultipleIndex extends Metric implements PatchMetric {

	private short[][] v;
	
	public MeanPatchSizeMultipleIndex(String name, String alias, short[]... v) {
		super(name, alias);
		this.v = v;
	}
	
	/*
	public MeanPatchSizeMultipleIndex(short[]... v) {
		super();
		StringBuffer name = new StringBuffer("MPSm_");
		for(short[] vv : v){
			for(short vvv : vv){
				name.append(vvv);
				name.append('&');
			}
			name.deleteCharAt(name.length()-1);
			name.append(',');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}
	*/

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			double totalSurface = 0.0;
			double nbPatches = 0;
			for(short[] vv : v){
				for(short vvv : vv){
					totalSurface += co.totalSurface(vvv);
					nbPatches += co.nbPatches(vvv);
				}
			}
			if(nbPatches > 0){
				value = totalSurface / nbPatches;
			}else{
				value = 0;
			}
		}
	}

}
