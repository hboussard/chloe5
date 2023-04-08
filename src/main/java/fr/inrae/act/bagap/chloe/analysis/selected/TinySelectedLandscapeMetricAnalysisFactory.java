package fr.inrae.act.bagap.chloe.analysis.selected;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.WindowDistanceType;
import fr.inrae.act.bagap.chloe.WindowShapeType;
import fr.inrae.act.bagap.chloe.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.counting.MultipleCounting;
import fr.inrae.act.bagap.chloe.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedCountCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedCountValueKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedQuantitativeKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.kernel.selected.SelectedPatchKernel;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.metric.MetricManager;
import fr.inrae.act.bagap.chloe.output.SelectedCsvOutput;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;

public class TinySelectedLandscapeMetricAnalysisFactory {

	public static SelectedLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
	
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		//double inMinX = coverage.minx();
		//double inMinY = coverage.miny();
		//double inMaxX = coverage.maxx();
		//double inMaxY = coverage.maxy();
		double inCellSize = coverage.cellsize();
			
		// ROI
		int roiX = builder.getROIX();
		int roiY = builder.getROIY();
		int roiWidth = builder.getROIWidth();
		if(roiWidth == -1){
			roiWidth = inWidth;
		}
		int roiHeight = builder.getROIHeight();
		if(roiHeight == -1){
			roiHeight = inHeight;
		}
			
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
			
		// buffer ROI			
		int bufferROIXMin = Math.min(roiX, midWindowSize);
		int bufferROIXMax = Math.min(inWidth-(roiX+roiWidth), midWindowSize);
		int bufferROIYMin = Math.min(roiY, midWindowSize);
		int bufferROIYMax = Math.min(inHeight-(roiY+roiHeight), midWindowSize);
		//System.out.println(bufferROIXMin+" "+bufferROIXMax+" "+bufferROIYMin+" "+bufferROIYMax);
			
		// window shape and distance
		short[] shape = new short[windowSize*windowSize];
		float[] coeffs = new float[windowSize*windowSize];
					
		int theoreticalSize = 0;
		int theoreticalCoupleSize = 0;
			
		double dMax = midWindowSize * inCellSize;
		DistanceFunction function = CombinationExpressionFactory.createDistanceFunction(builder.getWindowDistanceFunction(), dMax);
									
		for(int j=0; j<windowSize; j++){
			for(int i=0; i<windowSize; i++){
				// gestion en cercle
				if(Util.distance(midWindowSize, midWindowSize, i, j) <= midWindowSize){
					shape[(j * windowSize) + i] = 1;
					theoreticalSize++; 
					if(j>0 && (Util.distance(midWindowSize, midWindowSize, i, j-1) <= midWindowSize)) {
						theoreticalCoupleSize++;
					}
					if(i>0 && (Util.distance(midWindowSize, midWindowSize, i-1, j) <= midWindowSize)) {
						theoreticalCoupleSize++;
					}
				}else{
					shape[(j * windowSize) + i] = 0;
				}
							
				// gestion des distances pondÃ©rÃ©es (gaussienne centrÃ©e Ã  0)
				//exp(-pow(distance, 2)/pow(dmax/2, 2))
				//float d = (float) Math.exp(-1 * Math.pow(Util.distance(midWindowSize, midWindowSize, i, j), 2) / Math.pow(midWindowSize/2, 2));
				float d = (float) function.interprete(Util.distance(midWindowSize, midWindowSize, i, j)*inCellSize);
				coeffs[(j * windowSize) + i] = d;
			}
		}
				
		if(builder.getWindowShapeType() == WindowShapeType.SQUARE){
			for(int s=0; s<(windowSize*windowSize); s++){
				shape[s] = 1;
			}
		}
					 
		if(builder.getWindowDistanceType() == WindowDistanceType.THRESHOLD){
			for(int c=0; c<(windowSize*windowSize); c++){
				coeffs[c] = 1;
			}
		}
			
		// metriques
		Set<Metric> metrics = builder.getMetrics();
			
		// pixels
		Set<Pixel> pixels = null;
		if(builder.getRefPixels() != null){
			pixels = builder.getRefPixels();
		}else{
			pixels = CoordinateManager.initWithPoints(builder.getPointsFilter(), coverage.getEntete());
		}
			
		// observers
		Set<CountingObserver> observers = builder.getObservers();
		if(builder.getCsv() != null){
			SelectedCsvOutput csvOutput = new SelectedCsvOutput(builder.getCsv(), pixels, Raster.getNoDataValue());
			observers.add(csvOutput);
		}
		/*if(builder.getDatas() != null){
				System.out.println("ici");
				DataOutput dataOutput = new DataOutput(builder.getMetrics().iterator().next(), builder.getDatas());
				observers.add(dataOutput);
			}*/
			
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
				
			SelectedLandscapeMetricKernel kernel = null;
			if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("MD")){
				kernel = new SelectedQuantitativeKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue(), 100);
			}else if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("distance")){
				// TODO
				/*
					kernel = new EmpriseBocageKernel3(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), unfilters);
					
					Coverage coverage2 = null;
					
					if(builder.getRasterFile2() != null){
						GridCoverage2DReader reader;
						if(builder.getRasterFile2().endsWith(".asc")){
							File file = new File(builder.getRasterFile2());
							reader = new ArcGridReader(file);
						}else if(builder.getRasterFile2().endsWith(".tif")){
							File file = new File(builder.getRasterFile2());
							reader = new GeoTiffReader(file);
						}else{
							throw new IllegalArgumentException(builder.getRasterFile()+" is not a recognize raster");
						}
						GridCoverage2D coverage2D = (GridCoverage2D) reader.read(null);
						reader.dispose(); // a  tester, ca va peut-etre bloquer la lecture des donnees
						
						coverage2 = new FileCoverage(coverage2D, coverage.getEntete());
						
					}else if(builder.getRasterTab2() != null){
						
						coverage2 = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
					}else{
						throw new IllegalArgumentException("no raster2 declared");
					}
					
					
					Counting counting = new QuantitativeCounting(0, 7, theoreticalSize);
					
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
					
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
					// analysis
					return new MultipleLandscapeMetricAnalysis(coverage, coverage2, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, 7, displacement, kernel, counting);
					*/
				/*}else if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("bocage")){
					kernel = new LocalBocageKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), unfilters);
				}else if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("prop")){
					kernel = new ProportionKernel(windowSize, displacement, shape, coeffs, Raster.getNoDataValue(), unfilters);
				*/
			}else{
				kernel = new SelectedQuantitativeKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue());
			}
			Counting counting = new QuantitativeCounting(0, 7, theoreticalSize);
				
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
				
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
				
			// analysis
			return new TinySelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, 7, kernel, counting);
				
		}else if(MetricManager.hasOnlyQualitativeMetric(metrics)){ // qualitative
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
				Set<Float> inValues = new HashSet<Float>();
				for(float d : datas){
					if(d != 0 && d != Raster.getNoDataValue()){
						inValues.add(d);
					}
				}
				int index = 0;
				values = new int[inValues.size()];
				for(float d : inValues){
					values[index++] = (int) d;
				}
			}
				
			// recuperation des couples
			float[] couples = null;
			if(MetricManager.hasCoupleMetric(metrics)){
				couples = new float[(((values.length*values.length)-values.length)/2) + values.length];
				int index = 0;
				for(int s1 : values){
					couples[index++] = Couple.getCouple(s1, s1);
				}
				for(int s1 : values){
					for(int s2 : values){
						if(s1 < s2) {
							couples[index++] = Couple.getCouple(s1, s2);
						}
					}
				}
			}
				
			// kernel et counting
			SelectedLandscapeMetricKernel kernel = null;
			Counting counting = null;
				
			int nbValues = 2;
			if(MetricManager.hasOnlyValueMetric(metrics)){
				nbValues += 1 + values.length;
					
				kernel = new SelectedCountValueKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue(), values);
				counting = new ValueCounting(0, nbValues, values, theoreticalSize);
					
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
			}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
					
				nbValues += couples.length;
				kernel = new SelectedCountCoupleKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue(), values);
				counting = new CoupleCounting(0, nbValues, values.length, couples, theoreticalCoupleSize);
					
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
			}else{
					
				nbValues += 1 + values.length + 2 + couples.length;
					
				Counting[] countings = new Counting[2];
					
				kernel = new SelectedCountValueAndCoupleKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue(), values);
				ValueCounting vCounting = new ValueCounting(0, values.length+3, values, theoreticalSize);
				// add metrics to counting
				for(Metric m : metrics){
					if(MetricManager.isValueMetric(m.getName())){
						vCounting.addMetric(m);
					}
				}
				countings[0] = vCounting;
				
				CoupleCounting cCounting = new CoupleCounting(values.length+3, nbValues, values.length, couples, theoreticalCoupleSize);
				// add metrics to counting
				for(Metric m : metrics){
					if(MetricManager.isCoupleMetric(m.getName())){
						cCounting.addMetric(m);
					}
				}
				countings[1] = cCounting;
					
				counting = new MultipleCounting(0, nbValues, countings); 
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
			}
				
			// analysis
			return new TinySelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
			
		}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
				
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				float[] datas = coverage.getDatas(new Rectangle(roiX, roiY, roiWidth, roiHeight));
				Set<Float> inValues = new HashSet<Float>();
				for(float d : datas){
					if(d != 0){
						inValues.add(d);
					}
				}
				int index = 0;
				values = new int[inValues.size()];
				for(float d : inValues){
					values[index++] = (int) d;
				}
			}
				
			int nbValues = 2;
			
			SelectedLandscapeMetricKernel kernel = new SelectedPatchKernel(windowSize, pixels, shape, coeffs, Raster.getNoDataValue(), values, inCellSize);
			Counting counting =  new PatchCounting(values);
				
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
				
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
				
			// analysis
			return new TinySelectedLandscapeMetricAnalysis(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
		}else{
			throw new IllegalArgumentException("problème de métriques non consistantes");
		}
		
	}
	
}
