package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class AreaMetric extends Metric implements ValueMetric {

	private short v;
	
	private float surface;
	
	public AreaMetric(short v) {
		super("area_"+v);
		this.v = v;
		this.surface = (float) Math.pow(Raster.getCellSize(), 2);
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.validValues() > 0){
			value = (co.countValue(v) * surface) / 10000.0;
		}
	}

}