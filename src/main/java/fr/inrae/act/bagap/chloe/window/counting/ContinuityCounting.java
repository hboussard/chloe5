package fr.inrae.act.bagap.chloe.window.counting;

public class ContinuityCounting extends Counting implements ContinuityCountingInterface {

	private float surface;
	
	private float volume;
	
	public ContinuityCounting(float theoreticalSize){
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
	public void doSetCounts(float[] counts){
	
		surface = counts[4];
		volume = counts[5];
	}
	
	@Override
	public float surface(){
		return surface;
	}
	
	@Override
	public float volume(){
		return volume;
	}
	
}
