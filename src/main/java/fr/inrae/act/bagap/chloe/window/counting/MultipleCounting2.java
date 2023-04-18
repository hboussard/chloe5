package fr.inrae.act.bagap.chloe.window.counting;

import java.util.Arrays;

import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MultipleCounting2 extends Counting {
	
	private Counting[] countings;
	
	public MultipleCounting2(int minRange, int maxRange, Counting[] countings) {
		super(minRange, maxRange);
		this.countings = countings;
		for(Counting counting : countings){
			for(Metric metric : counting.metrics()){
				addMetric(metric);
			}
		}
		//System.out.println("multiple counting");
	}

	@Override
	public void setCounts(double[] counts) {
		for(Counting counting : countings){
			counting.setCounts(Arrays.copyOfRange(counts, counting.minRange(), counting.maxRange()));
		}
	}

	@Override
	protected void doCalculate() {
		for(Counting counting : countings){
			counting.doCalculate();
		}
	}

}
