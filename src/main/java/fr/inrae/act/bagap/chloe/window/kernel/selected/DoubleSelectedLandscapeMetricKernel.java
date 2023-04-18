package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;

public abstract class DoubleSelectedLandscapeMetricKernel extends SelectedLandscapeMetricKernel {

	private float[] inDatas2;
	
	protected DoubleSelectedLandscapeMetricKernel(int windowSize, Set<Pixel> pixels, float[] coeff, int noDataValue){
		super(windowSize, pixels, coeff, noDataValue);
	}
	
	public void setInDatas2(float[] inDatas2){
		this.inDatas2 = inDatas2;
	}
	
	public float[] inDatas2(){
		return inDatas2;
	}
	
	@Override
	public void dispose(){
		super.dispose();
		inDatas2 = null;
	}
	
}
