package fr.inrae.act.bagap.chloe.window.counting;

public class ContinuityCounting extends Counting implements ContinuityCountingInterface {

	private double surface;
	
	private double volume;
	
	public ContinuityCounting(double theoreticalSize){
		super(theoreticalSize);
	}
	/*
	public ContinuityCounting(){
		this(0);
	}*/
	
	/**
	 * partie specifique :
	 * 4 : surface
	 * 5 : volume
	 */
	@Override
	public void doSetCounts(double[] counts){
	
		surface = counts[4];
		volume = counts[5];
	}
	
	@Override
	public double surface(){
		return surface;
	}
	
	@Override
	public double volume(){
		return volume;
	}
	
}
