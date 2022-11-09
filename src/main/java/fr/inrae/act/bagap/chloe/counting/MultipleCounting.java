package fr.inrae.act.bagap.chloe.counting;

import java.util.Arrays;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class MultipleCounting extends Counting {
	
	private Counting[] countings;
	
	public MultipleCounting(int minRange, int maxRange, Counting[] countings) {
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
