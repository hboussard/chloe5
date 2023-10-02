package fr.inrae.act.bagap.chloe.window.counting;

public class SlopeCounting extends Counting implements QuantitativeCountingInterface {
	
	private double slopeDirection;
	
	private double slopeIntensity;
	
	public SlopeCounting(double theoreticalSize) {
		super(theoreticalSize);
	}
	
	/**
	 * partie specifique :
	 * 4 : direction de la pente
	 * 5 : intensite de la pente
	 */
	@Override
	public void doSetCounts(double[] counts){
		slopeDirection = counts[4];
		slopeIntensity = counts[5];
	}

	@Override
	public double slopeDirection(){
		return slopeDirection;
	}
	
	@Override
	public double slopeIntensity(){
		return slopeIntensity;
	}

}
