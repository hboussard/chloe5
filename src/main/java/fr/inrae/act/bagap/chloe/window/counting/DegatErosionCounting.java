package fr.inrae.act.bagap.chloe.window.counting;

public class DegatErosionCounting extends Counting implements DegatErosionCountingInterface {

	private float emprise;
	
	private float intensity;
	
	public DegatErosionCounting(float theoreticalSize){
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
	public float degatErosionEmprise(){
		return emprise;
	}
	
	@Override
	public float degatErosionIntensity(){
		return intensity;
	}
	
}
