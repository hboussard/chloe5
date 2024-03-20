package fr.inrae.act.bagap.chloe.window.analysis;

import java.io.IOException;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.entity.HugeEntityLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.entity.TinyEntityLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.grid.HugeGridLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.grid.TinyGridLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.map.HugeMapLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.map.TinyMapLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.selected.HugeSelectedLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.selected.TinySelectedLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.sliding.HugeSlidingLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.chloe.window.analysis.sliding.TinySlidingLandscapeMetricAnalysisFactory;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.TabCoverage;

public class LandscapeMetricAnalysisFactory {
	
 	public static LandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder) throws IOException {
		
		Coverage coverage;
		int inWidth;
		int inHeight;
		float inCellSize;
		
		if(builder.getCoverage() != null){
		
			coverage = builder.getCoverage();
			builder.setEntete(coverage.getEntete());
			inWidth = coverage.getEntete().width();
			inHeight = coverage.getEntete().height();
			inCellSize = coverage.getEntete().cellsize();
		
		}else if(builder.getRasterTile() != null){
			
			coverage = CoverageManager.getCoverage(builder.getRasterTile());
			builder.setEntete(coverage.getEntete());
			inWidth = coverage.getEntete().width();
			inHeight = coverage.getEntete().height();
			inCellSize = coverage.getEntete().cellsize();
			
		}else if(builder.getRasterFile() != null){
			
			coverage = CoverageManager.getCoverage(builder.getRasterFile());
			builder.setEntete(coverage.getEntete());
			inWidth = coverage.getEntete().width();
			inHeight = coverage.getEntete().height();
			inCellSize = coverage.getEntete().cellsize();
			
		}else if(builder.getRasterTab() != null){
			
			inWidth = builder.getEntete().width();
			inHeight = builder.getEntete().height();
			inCellSize = builder.getEntete().cellsize();
			
			coverage = new TabCoverage(builder.getRasterTab(), builder.getEntete());
			
		}else{
			throw new IllegalArgumentException("no raster declared");
		}
				
		int roiWidth = builder.getROIWidth();
		int roiHeight = builder.getROIHeight();
		
		if(builder.getAnalysisType() == ChloeAnalysisType.SLIDING){
				
			// windowSize
			int windowSize = -1;
			if(builder.getWindowSize() > 0){
				windowSize = builder.getWindowSize();
			}else if(builder.getWindowRadius() > 0){
				double v = (2 * builder.getWindowRadius() / inCellSize);
				windowSize = v%2==0?new Double(v+1).intValue():new Double(v).intValue();
			}else{
				throw new IllegalArgumentException("windowSize must be defined");
			}
			int midWindowSize = (int) (windowSize/2);
			
			int maxWidth = -1;
			int maxHeight = -1;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth + 2 * midWindowSize;
				maxHeight = roiHeight + 2 * midWindowSize;
			}
			
			//System.out.println((maxWidth/1000.0)+" "+(maxHeight/1000.0)+" "+((maxWidth/1000.0) * (maxHeight/1000.0))+" "+(LandscapeMetricAnalysis.maxTile()/1000000.0));
			
			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (LandscapeMetricAnalysis.maxTile()/1000000.0)){
				//System.out.println("tiny sliding");
				return new TinySlidingLandscapeMetricAnalysisFactory().create(builder, coverage);
			}else{
				//System.out.println("huge sliding");
				return new HugeSlidingLandscapeMetricAnalysisFactory().create(builder, coverage);
			}
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.SELECTED){
			
			// windowSize
			int windowSize = -1;
			if(builder.getWindowSize() > 0){
				windowSize = builder.getWindowSize();
			}else if(builder.getWindowRadius() > 0){
				double v = (2 * builder.getWindowRadius() / inCellSize);
				windowSize = v%2==0?new Double(v+1).intValue():new Double(v).intValue();
			}else{
				throw new IllegalArgumentException("windowSize must be defined");
			}
			int midWindowSize = (int) (windowSize/2);
			
			int maxWidth = -1;
			int maxHeight = -1;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth + 2 * midWindowSize;
				maxHeight = roiHeight + 2 * midWindowSize;
			}

			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (LandscapeMetricAnalysis.maxTile()/1000000.0)){
				
				return new TinySelectedLandscapeMetricAnalysisFactory().create(builder, coverage);
				//return new HugeSelectedLandscapeMetricAnalysisFactory().create(builder, coverage);
			}else{
				
				return new HugeSelectedLandscapeMetricAnalysisFactory().create(builder, coverage);
			}
		}else if(builder.getAnalysisType() == ChloeAnalysisType.ENTITY){
			
			int maxWidth = inWidth;
			int maxHeight = inHeight;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth;
				maxHeight = roiHeight;
			}
			
			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (LandscapeMetricAnalysis.maxTile()/1000000.0)){
				
				return new TinyEntityLandscapeMetricAnalysisFactory().create(builder, coverage);

			}else{
				
				return new HugeEntityLandscapeMetricAnalysisFactory().create(builder, coverage);
			}
			
		}else if(builder.getAnalysisType() == ChloeAnalysisType.GRID){
		
			int maxWidth = inWidth;
			int maxHeight = inHeight;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth;
				maxHeight = roiHeight;
			}
			
			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (LandscapeMetricAnalysis.maxTile()/1000000.0)){
				
				return new TinyGridLandscapeMetricAnalysisFactory().create(builder, coverage);
			}else{
				
				return new HugeGridLandscapeMetricAnalysisFactory().create(builder, coverage);
			}
		}else if(builder.getAnalysisType() == ChloeAnalysisType.MAP){
		
			int maxWidth = inWidth;
			int maxHeight = inHeight;
			if(roiWidth == -1){ // on analyse toute la carte
				maxWidth = inWidth;
				maxHeight = inHeight;
			}else{ // on analyse une partie de la carte
				maxWidth = roiWidth;
				maxHeight = roiHeight;
			}
			
			if(((maxWidth/1000.0) * (maxHeight/1000.0)) <= (LandscapeMetricAnalysis.maxTile()/1000000.0)){
				
				return new TinyMapLandscapeMetricAnalysisFactory().create(builder, coverage);
				
				//return new HugeMapLandscapeMetricAnalysisFactory().create(builder, coverage);
			}else{
				
				return new HugeMapLandscapeMetricAnalysisFactory().create(builder, coverage);
			}
		}
		
		throw new IllegalArgumentException(builder.getAnalysisType()+" is not a recognized analysis type");
	}

}
