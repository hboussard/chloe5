package fr.inrae.act.bagap.chloe.analysis.area;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.area.AreaLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class HugeAreaLandscapeMetricAnalysis extends AreaLandscapeMetricAnalysis {

	public HugeAreaLandscapeMetricAnalysis(Coverage coverage, Coverage areaCoverage, int roiX, int roiY, int roiWidth, int roiHeight, int nbValues, AreaLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, nbValues, kernel, counting);
	}

	@Override
	protected void doInit() {
		
		kernel().setRoiWidth(roiWidth());
		
		// initialisation du comptage
		counting().init();
	}

	@Override
	protected void doRun() {
		
		Set<Integer> areaNumbers = new TreeSet<Integer>();
		
		// gestion des sorties
		setOutDatas(new HashMap<Integer, double[]>());
		
		int tYs;
		Rectangle roi;
		for(int localROIY=roiY(); localROIY<roiY()+roiHeight(); localROIY+=LandscapeMetricAnalysis.tileYSize()){
			
			tYs = Math.min(LandscapeMetricAnalysis.tileYSize(), roiHeight() + roiY() - localROIY );
			
			kernel().setRoiHeight(tYs);
			
			roi = new Rectangle(roiX(), localROIY, roiWidth(), tYs);
			setInDatas(coverage().getDatas(roi));
			kernel().setInDatas(inDatas());
			
			setInAreaDatas(areaCoverage().getDatas(roi));
			kernel().setInAreaDatas(inAreaDatas());
			
			for(float f : inAreaDatas()){
				if(f != 0 && f != Raster.getNoDataValue()){
					areaNumbers.add((int) f);
				}
			}
			
			// gestion des sorties
			for(int an : areaNumbers){
				if(!outDatas().containsKey(an)){
					outDatas().put(an, new double[nbValues()]);
				}
			}
			kernel().setOutDatas(outDatas());
			
			kernel().applyAreaWindow();
		}
		
		
		for(Entry<Integer, double[]> e : outDatas().entrySet()){
			counting().setCounts(e.getValue());
			counting().calculate();
			counting().export(e.getKey());
		}
		
	}

	@Override
	protected void doClose() {
		counting().close();
		coverage().dispose();
		areaCoverage().dispose();
	}

}