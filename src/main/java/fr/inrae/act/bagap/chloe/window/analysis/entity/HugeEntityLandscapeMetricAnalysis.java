package fr.inrae.act.bagap.chloe.window.analysis.entity;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import fr.inrae.act.bagap.apiland.raster.Raster;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.kernel.entity.EntityLandscapeMetricKernel;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class HugeEntityLandscapeMetricAnalysis extends EntityLandscapeMetricAnalysis {

	public HugeEntityLandscapeMetricAnalysis(Coverage coverage, Coverage areaCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, EntityLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
	}

	@Override
	protected void doInit() {
		
		kernel().setWidth(roiWidth());
		
		// initialisation du kernel
		kernel().init();
		
		// initialisation du comptage
		counting().init();
	}

	@Override
	protected void doRun() {
		
		Set<Integer> entityIds = new HashSet<Integer>();
		
		// gestion des sorties
		kernel().setOutDatas(new HashMap<Integer, double[]>());
		
		int tYs;
		Rectangle roi;
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=LandscapeMetricAnalysis.tileYSize()){
			
			//System.out.println("pass "+localROIY);
			
			tYs = Math.min(LandscapeMetricAnalysis.tileYSize(), roiHeight() + roiY() - localROIY );
			
			kernel().setHeight(tYs);
			
			// recuperation des donnees depuis le coverage
			roi = new Rectangle(roiX(), localROIY, roiWidth(), tYs);
			
			// gestion des entrees
			kernel().setInDatas(coverage().getData(roi));
			
			kernel().setEntityDatas(entityCoverage().getData(roi));
			
			for(float f : kernel().entityDatas()){
				if(f != 0 && f != Raster.getNoDataValue()){
					entityIds.add((int) f);
				}
			}
			
			// gestion des sorties
			for(int aId : entityIds){
				if(!kernel().outDatas().containsKey(aId)){
					kernel().outDatas().put(aId, new double[nbValues()]);
					kernel().outDatas().get(aId)[0] = 1; // filtre ok
					
					//kernel().outDatas().get(aId)[6] = Float.MAX_VALUE; // init minimum
				}
			}
			
			// initialisation du kernel
			kernel().init();
			
			kernel().applyEntityWindow();	
		}
		
		for(Entry<Integer, double[]> e : kernel().outDatas().entrySet()){
			counting().setCounts(e.getValue());
			counting().calculate();
			counting().export(e.getKey());
		}
	}

	@Override
	protected void doClose() {
		super.doClose();
		coverage().dispose();
	}

}
