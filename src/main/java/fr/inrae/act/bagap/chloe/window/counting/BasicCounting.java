package fr.inrae.act.bagap.chloe.window.counting;

public class BasicCounting extends Counting implements BasicCountingInterface {

	public BasicCounting(float theoreticalSize) {
		super(theoreticalSize);
	}

	@Override
	public void doSetCounts(double[] counts) {
		// do nothing special
	}

}
