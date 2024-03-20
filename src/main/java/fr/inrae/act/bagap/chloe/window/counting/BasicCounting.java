package fr.inrae.act.bagap.chloe.window.counting;

public class BasicCounting extends Counting implements BasicCountingInterface {

	public BasicCounting(double theoreticalSize) {
		super(theoreticalSize);
	}

	@Override
	public void doSetCounts(double[] counts) {
		// do nothing special
	}

}
