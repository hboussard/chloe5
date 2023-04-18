package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class RateCentralValue extends Metric implements ValueMetric {

	public RateCentralValue() {
		super("pCentral");
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.centralValue() == Raster.getNoDataValue() || co.centralValue() == 0){
			value = Raster.getNoDataValue();
		}else if(co.validValues() > 0){
			value = co.countValue((int) co.centralValue()) / co.validValues();
		}
	}

}
