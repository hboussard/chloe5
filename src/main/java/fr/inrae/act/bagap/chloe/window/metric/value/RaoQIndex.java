package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.ThematicDistanceMetric;

public class RaoQIndex extends Metric implements ValueMetric, ThematicDistanceMetric {

	private float[][] distance;
	
	public RaoQIndex() {
		super("RaoQ");
	}

	@Override
	protected void doCalculate(Counting co) {
		
		if(co.validValues() > 0){
			value = 0;
			double p1, p2; 
		
			for(int v1 : co.values()){
				p1 = co.countValue(v1) / co.validValues();
				for(int v2 : co.values()){
					if(v1 < v2) {
						p2 = co.countValue(v2) / co.validValues();
						value += distance[v1][v2] * p1 * p2;	
					}
				}
			}
		}
	}

	@Override
	public void setThematicDistance(float[][] distance) {
		this.distance = distance;
	}

}
