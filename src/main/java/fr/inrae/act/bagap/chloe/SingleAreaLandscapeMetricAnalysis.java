package fr.inrae.act.bagap.chloe;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.kernel.AreaLandscapeMetricKernel;
import fr.inrae.act.bagap.raster.Coverage;

public class SingleAreaLandscapeMetricAnalysis extends AreaLandscapeMetricAnalysis {
	
	public SingleAreaLandscapeMetricAnalysis(Coverage coverage, Coverage areaCoverage, int roiX, int roiY, int roiWidth, int roiHeight, short[] values, int nbValues, AreaLandscapeMetricKernel kernel, Counting counting) {	
		super(coverage, areaCoverage, roiX, roiY, roiWidth, roiHeight, /*values,*/ nbValues, kernel, counting);
	}

	@Override
	protected void doInit() {
		
		kernel().setRoiWidth(roiWidth());
		kernel().setRoiHeight(roiHeight());
		
		// lecture de la carte area et dÈtection des numÈros
		// voir si on ne peut faire cette initialisation ‡ la volÈe
				
		// recuperation des donnees depuis le coverage
		// attention bug de la r√©cup√©ration des donn√©es dans le coverage2D si le Y d√©passe une certaine valeur
		// bizarement ce bug influence les donn√©es en X
		// ce bug n'est effectif que sur les coverage issus de fichiers AsciiGrid
		// pas de probl√®me sur fichier TIF
		Rectangle roi = new Rectangle(roiX(), roiY(), roiWidth(), roiHeight());
		//setInDatas(new float[roi.width * roi.height]);
		//setInDatas(coverage().getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas()));
		setInDatas(coverage().getDatas(roi));
		kernel().setInDatas(inDatas());
		
		//setInAreaDatas(new float[roi.width * roi.height]);
		//setInAreaDatas(areaCoverage().getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inAreaDatas()));
		setInAreaDatas(areaCoverage().getDatas(roi));
		kernel().setInAreaDatas(inAreaDatas());
		
		Set<Integer> areaNumbers = new TreeSet<Integer>();
		for(float f : inAreaDatas()){
			if(f != 0 && f != Raster.getNoDataValue()){
				areaNumbers.add((int) f);
			}
		}
		//System.out.println("nombre de features "+areaNumbers.size());
		
		// gestion des sorties
		setOutDatas(new HashMap<Integer, double[]>());
		for(int an : areaNumbers){
			outDatas().put(an, new double[nbValues()]);
		}
		kernel().setOutDatas(outDatas());
		
		// initialisation du comptage
		counting().init();
		
	}

	@Override
	protected void doRun() {
		kernel().applyAreaWindow();
		
		for(Entry<Integer, double[]> e : outDatas().entrySet()){
			counting().setCounts(e.getValue());
			counting().calculate();
			counting().export(e.getKey());
		}
		
	}

	@Override
	protected void doClose() {
		counting().close();
	}

}
