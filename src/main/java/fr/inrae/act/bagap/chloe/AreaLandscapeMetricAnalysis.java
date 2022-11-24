package fr.inrae.act.bagap.chloe;

import java.util.Map;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.AreaLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class AreaLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	private final Coverage coverage;
	
	private final Coverage areaCoverage;
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int nbValues;
	
	private final AreaLandscapeMetricKernel kernel;
	
	private final Counting counting;
	
	private float[] inDatas, inAreaDatas;
	
	private Map<Integer, double[]> outDatas;
	
	public AreaLandscapeMetricAnalysis(Coverage coverage, Coverage areaCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int nbValues, AreaLandscapeMetricKernel kernel, Counting counting) {
		this.coverage = coverage;
		this.areaCoverage = areaCoverage;
		this.roiX = roiX;
		this.roiY = roiY;
		this.roiWidth = roiWidth;
		this.roiHeight = roiHeight;
		this.nbValues = nbValues;
		this.kernel = kernel;
		this.counting = counting;
	}

	public Coverage coverage() {
		return coverage;
	}

	public Coverage areaCoverage() {
		return areaCoverage;
	}

	public int roiX() {
		return roiX;
	}

	public int roiY() {
		return roiY;
	}

	public int roiWidth() {
		return roiWidth;
	}

	public int roiHeight() {
		return roiHeight;
	}

	public int nbValues() {
		return nbValues;
	}

	public AreaLandscapeMetricKernel kernel() {
		return kernel;
	}

	@Override
	public Counting counting() {
		return counting;
	}

	public float[] inDatas() {
		return inDatas;
	}

	public float[] inAreaDatas() {
		return inAreaDatas;
	}

	public Map<Integer, double[]> outDatas() {
		return outDatas;
	}
	
	public void setInDatas(float[] inDatas){
		this.inDatas = inDatas;
	}
	
	public void setInAreaDatas(float[] inAreaDatas){
		this.inAreaDatas = inAreaDatas;
	}
	
	public void setOutDatas(Map<Integer, double[]> outDatas){
		this.outDatas = outDatas;
	}
	
}
