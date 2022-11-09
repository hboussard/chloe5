package fr.inrae.act.bagap.chloe.counting;

import fr.inrae.act.bagap.chloe.metric.Metric;

public class PatchCounting extends Counting implements PatchCountingInterface {

	private double totalSurface;
	
	private int nbPatches;
	
	public PatchCounting() {
		super();
	}

	@Override
	public void setCounts(double[] counts) {
		nbPatches = (int) counts[0];
		totalSurface = counts[1];
	}

	@Override
	protected void doCalculate() {
		for(Metric m : metrics()){
			m.calculate(this, "");
		}
	}

	@Override
	public double getTotalSurface() {
		return totalSurface;
	}

	@Override
	public int getNbPatches() {
		return nbPatches;
	}
	
}
