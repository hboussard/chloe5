package fr.inrae.act.bagap.chloe.window.counting;

public class ErosionCounting extends Counting implements ErosionCountingInterface {

	private float emprise;
	
	private float intensity;
	
	public ErosionCounting(float theoreticalSize){
		super(theoreticalSize);
	}
	/*
	public ContinuityCounting(){
		this(0);
	}*/
	
	/**
	 * partie specifique :
	 * 4 : emprise
	 * 5 : intensity
	 */
	@Override
	public void doSetCounts(float[] counts){
	
		emprise = counts[4];
		intensity = counts[5];
	}
	
	@Override
	public float erosionEmprise(){
		return emprise;
	}
	
	@Override
	public float erosionIntensity(){
		return intensity;
	}
	
}
