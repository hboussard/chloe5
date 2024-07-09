package fr.inrae.act.bagap.chloe.window.counting;

public class BasicCounting extends Counting implements BasicCountingInterface {

	public BasicCounting(double resolution, double theoreticalSize) {
		super(resolution, theoreticalSize);
	}

	@Override
	public void doSetCounts(double[] counts) {
		// do nothing special
	}

}
