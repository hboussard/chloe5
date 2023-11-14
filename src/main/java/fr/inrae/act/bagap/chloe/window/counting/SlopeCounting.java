package fr.inrae.act.bagap.chloe.window.counting;

public class SlopeCounting extends Counting implements QuantitativeCountingInterface {
	
	private float slopeDirection;
	
	private float slopeIntensity;
	
	public SlopeCounting(float theoreticalSize) {
		super(theoreticalSize);
	}
	
	/**
	 * partie specifique :
	 * 4 : direction de la pente
	 * 5 : intensite de la pente
	 */
	@Override
	public void doSetCounts(float[] counts){
		slopeDirection = counts[4];
		slopeIntensity = counts[5];
	}

	@Override
	public float slopeDirection(){
		return slopeDirection;
	}
	
	@Override
	public float slopeIntensity(){
		return slopeIntensity;
	}

}
