package fr.inrae.act.bagap.chloe.analysis.entity;

import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class EntityLandscapeMetricAnalysis extends LandscapeMetricAnalysis {

	private final Coverage coverage;
	
	private final Coverage entityCoverage;
	
	private final int roiX, roiY;
	
	private final int roiWidth, roiHeight;
	
	private final int nbValues;
	
	private final EntityLandscapeMetricKernel kernel;
	
	private final Counting counting;
	
	public EntityLandscapeMetricAnalysis(Coverage coverage, Coverage entityCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int nbValues, EntityLandscapeMetricKernel kernel, Counting counting) {
		this.coverage = coverage;
		this.entityCoverage = entityCoverage;
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

	public Coverage entityCoverage() {
		return entityCoverage;
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

	public EntityLandscapeMetricKernel kernel() {
		return kernel;
	}

	@Override
	public Counting counting() {
		return counting;
	}

}
