package fr.inrae.act.bagap.chloe.window.counting;

public class DegatErosionCounting extends Counting implements DegatErosionCountingInterface {

	private double emprise;
	
	private double intensity;
	
	public DegatErosionCounting(double theoreticalSize){
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
	public void doSetCounts(double[] counts){
	
		emprise = counts[4];
		intensity = counts[5];
	}
	
	@Override
	public double degatErosionEmprise(){
		return emprise;
	}
	
	@Override
	public double degatErosionIntensity(){
		return intensity;
	}
	
}
