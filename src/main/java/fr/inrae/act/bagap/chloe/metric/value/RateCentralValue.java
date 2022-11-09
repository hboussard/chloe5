package fr.inrae.act.bagap.chloe.metric.value;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.metric.Metric;

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
