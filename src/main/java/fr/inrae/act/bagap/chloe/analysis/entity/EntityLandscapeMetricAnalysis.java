package fr.inrae.act.bagap.chloe.analysis.entity;

import fr.inrae.act.bagap.chloe.analysis.UnitLandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public abstract class EntityLandscapeMetricAnalysis extends UnitLandscapeMetricAnalysis {

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

}
