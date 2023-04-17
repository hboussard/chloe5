package fr.inrae.act.bagap.chloe.analysis.entity;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class TinyEntityLandscapeMetricAnalysis extends EntityLandscapeMetricAnalysis {
	
	public TinyEntityLandscapeMetricAnalysis(Coverage coverage, Coverage entityCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, EntityLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, entityCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	protected void doInit() {
		
		kernel().setWidth(roiWidth());
		kernel().setHeight(roiHeight());
		
		// lecture de la carte area et détection des numéros
		// voir si on ne peut faire cette initialisation à la volée
				
		// recuperation des donnees depuis le coverage
		Rectangle roi = new Rectangle(roiX(), roiY(), roiWidth(), roiHeight());
	
		kernel().setInDatas(coverage().getDatas(roi));
		coverage().dispose();
		
		kernel().setEntityDatas(entityCoverage().getDatas(roi));
		
		Set<Integer> entityIds = new TreeSet<Integer>();
		for(float f : kernel().entityDatas()){
			if(f != 0 && f != Raster.getNoDataValue()){
				entityIds.add((int) f);
			}
		}
		//System.out.println("nombre de features "+areaNumbers.size());
		
		// gestion des sorties
		kernel().setOutDatas(new HashMap<Integer, double[]>());
		for(int an : entityIds){
			kernel().outDatas().put(an, new double[nbValues()]);
			kernel().outDatas().get(an)[0] = 1; // filtre ok
		}
		
		// initialisation du comptage
		counting().init();
		
		// initialisation du kernel
		kernel().init();
	}

	@Override
	protected void doRun() {
		kernel().applyEntityWindow();
		
		for(Entry<Integer, double[]> e : kernel().outDatas().entrySet()){
			counting().setCounts(e.getValue());
			counting().calculate();
			counting().export(e.getKey());
		}
	}

}
