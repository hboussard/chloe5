package fr.inrae.act.bagap.chloe.window.counting;

public class SourceErosionCounting extends Counting implements SourceErosionCountingInterface {

	private double emprise;
	
	private double intensity;
	
	public SourceErosionCounting(double theoreticalSize){
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
	public double sourceErosionEmprise(){
		return emprise;
	}
	
	@Override
	public double sourceErosionIntensity(){
		return intensity;
	}
	
}
