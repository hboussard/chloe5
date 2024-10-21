package fr.inrae.act.bagap.chloe.window.analysis.entity;

import fr.inrae.act.bagap.chloe.window.analysis.SingleLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public abstract class EntityLandscapeMetricAnalysis extends SingleLandscapeMetricAnalysis {

	private final Coverage entityCoverage;
	
	public EntityLandscapeMetricAnalysis(Coverage coverage, Coverage entityCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, EntityLandscapeMetricKernel kernel, Counting counting) {
		super(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		this.entityCoverage = entityCoverage;
	}

	public Coverage entityCoverage() {
		return entityCoverage;
	}

	@Override
	public EntityLandscapeMetricKernel kernel() {
		return (EntityLandscapeMetricKernel) super.kernel();
	}
	
	@Override
	protected void doClose() {
		super.doClose();
		entityCoverage().dispose();
	}

}
