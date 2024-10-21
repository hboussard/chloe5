package fr.inrae.act.bagap.chloe.window.analysis.selected;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inrae.act.bagap.apiland.analysis.distance.DistanceFunction;
import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.BasicCounting;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedBasicKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedPatchKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.SelectedQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.functional.SelectedFunctionalCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.functional.SelectedFunctionalCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.functional.SelectedFunctionalCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.selected.grainbocager.GrainBocagerSelectedDistanceBocageKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.SelectedCsvOutput;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;

public abstract class SelectedLandscapeMetricAnalysisFactory {

	public SelectedLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inCellSize = coverage.cellsize();
			
		// ROI
		int roiX = builder.getROIX();
		int roiY = builder.getROIY();
		int roiWidth = builder.getROIWidth();
		if(roiWidth == -1){
			roiWidth = inWidth - roiX;
		}
		int roiHeight = builder.getROIHeight();
		if(roiHeight == -1){
			roiHeight = inHeight - roiY;
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
			
		// window coeff and distance
		float[] coeffs = new float[windowSize * windowSize];

		double dMax = builder.getDMax();
		if(dMax == -1){
			dMax = midWindowSize * inCellSize;
		}
		
		DistanceFunction function = null;
		if (builder.getWindowDistanceType() == WindowDistanceType.WEIGHTED) {
			function = CombinationExpressionFactory.createDistanceFunction(builder.getWindowDistanceFunction(), dMax);
		}
		
		float theoreticalSize = 0;
		int theoreticalCoupleSize = 0;
		if(builder.getWindowShapeType() == WindowShapeType.SQUARE) {
			theoreticalSize = windowSize * windowSize;
			theoreticalCoupleSize = (windowSize-1) * windowSize * 2;
			for (int s = 0; s < (windowSize * windowSize); s++) {
				coeffs[s] = 1;
			}
		}else if(builder.getWindowShapeType() == WindowShapeType.CIRCLE) {
			float d;
			for (int j = 0; j < windowSize; j++) {
				for (int i = 0; i < windowSize; i++) {
					// gestion en cercle
					if (Util.distance(midWindowSize, midWindowSize, i, j) <= midWindowSize) {
						
						if (j > 0 && (Util.distance(midWindowSize, midWindowSize, i, j - 1) <= midWindowSize)) {
							theoreticalCoupleSize++;
						}
						if (i > 0 && (Util.distance(midWindowSize, midWindowSize, i - 1, j) <= midWindowSize)) {
							theoreticalCoupleSize++;
						}
						if (builder.getWindowDistanceType() == WindowDistanceType.WEIGHTED) {
							d = (float) function.interprete(Util.distance(midWindowSize, midWindowSize, i, j) * inCellSize);
							theoreticalSize += d;
							coeffs[(j * windowSize) + i] = d;
									
						}else if(builder.getWindowDistanceType() == WindowDistanceType.THRESHOLD) {
							theoreticalSize++;
							coeffs[(j * windowSize) + i] = 1;
						}
					}
				}
			}
		}
			
		// metriques
		Set<Metric> metrics = builder.getMetrics();
			
		// pixels
		Set<Pixel> pixels = null;
		if(builder.getRefPixels() != null){
			pixels = builder.getRefPixels();
		}else if(builder.getPixelsFilter() != null){
			pixels = CoordinateManager.initWithPixels(builder.getPixelsFilter());
			builder.setPixelsFilter(pixels);
		}else if(builder.getRefPoints() != null){
			throw new IllegalArgumentException();
		}else{
			pixels = CoordinateManager.initWithPoints(builder.getPointsFilter(), coverage.getEntete());
			builder.setPixelsFilter(pixels);
		}
			
		// observers
		//Set<CountingObserver> observers = builder.getObservers();
		Set<CountingObserver> observers = new HashSet<CountingObserver>();
		
		if(builder.getCsv() != null){
			
			SelectedCsvOutput csvOutput = new SelectedCsvOutput(builder.getCsv(), pixels, coverage.getEntete());
			observers.add(csvOutput);
		}else if (builder.getCsvFolder() != null){
			
			String name = builder.getCsvFolder()+new File(builder.getRasterFile()).getName().replace(".tif", "").replace(".asc", "").toString()+".csv";
			SelectedCsvOutput csvOutput = new SelectedCsvOutput(name, pixels, coverage.getEntete());
			observers.add(csvOutput);
		}
		
		/*if(builder.getDatas() != null){
				System.out.println("ici");
				DataOutput dataOutput = new DataOutput(builder.getMetrics().iterator().next(), builder.getDatas());
				observers.add(dataOutput);
			}*/
		
		// kernel et counting
		SelectedLandscapeMetricKernel kernel = null;
		Counting counting = null;
		int nbValues = -1;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyBasicMetric(metrics)){
			
			nbValues = 4;
			
			kernel = new SelectedBasicKernel(windowSize, pixels, coeffs, coverage.getEntete(), builder.getWindowsPath());
			
			counting = new BasicCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}

			// analysis
			return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
			
		}else if(MetricManager.hasOnlyQuantitativeMetric(metrics)){ // quantitative
			
			nbValues = 8;
			
			if(metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("MD")){
				
				kernel = new SelectedQuantitativeKernel(windowSize, pixels, coeffs, coverage.getEntete(), builder.getWindowsPath(), 100);
				
			}else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBDistance")) {

				kernel = new GrainBocagerSelectedDistanceBocageKernel(windowSize, pixels, coeffs, coverage.getEntete(), builder.getWindowsPath(), 3, 30);

				Coverage coverageBocage = null;
				if (builder.getRasterFile2() != null) {
					coverageBocage = CoverageManager.getCoverage(builder.getRasterFile2());
				} else if (builder.getRasterTab2() != null) {
					coverageBocage = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
				} else {
					throw new IllegalArgumentException("no raster 'type de boisement' declared");
				}

				counting = new QuantitativeCounting(theoreticalSize);

				// add metrics to counting
				for (Metric m : metrics) {
					counting.addMetric(m);
				}

				// observers
				for (CountingObserver co : observers) {
					counting.addObserver(co);
				}

				// analysis
				return createMultiple(new Coverage[] {coverage, coverageBocage}, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
				

			} /*else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBBocage")) {
				// a priori jamais utilise
				kernel = new GrainBocagerSelectedDetectionBocageKernel(windowSize, pixels, coeffs, Raster.getNoDataValue());
				
			}*/ else{
				kernel = new SelectedQuantitativeKernel(windowSize, pixels, coeffs, coverage.getEntete(), builder.getWindowsPath());
			}
			
			counting = new QuantitativeCounting(theoreticalSize);
				
			// add metrics to counting
			for(Metric m : metrics){
				counting.addMetric(m);
			}
				
			//observers
			for(CountingObserver co : observers){
				counting.addObserver(co);
			}
				
			// analysis
			return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
				
		}else{
			
			// recuperation des valeurs
			int[] values = builder.getValues();
			if(values == null){
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight), coverage.getEntete().noDataValue());
			}
			
			if(MetricManager.hasOnlyQualitativeMetric(metrics)){ // qualitative
				
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
					
		
				if(MetricManager.hasOnlyValueMetric(metrics)){
					
					System.out.println("comptage des valeurs");

					nbValues = 5 + values.length;
					
					counting = new ValueCounting(inCellSize, values, theoreticalSize);
						
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
						
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {

						kernel = new SelectedFunctionalCountValueKernel(windowSize, pixels, coverage.getEntete(), values, function, dMax, builder.getWindowsPath());

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);

					} else {
						
						kernel = new SelectedCountValueKernel(windowSize, pixels, coeffs, coverage.getEntete(), values, builder.getWindowsPath());
						
						// analysis
						return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
						
					}
						
				}else if(MetricManager.hasOnlyCoupleMetric(metrics)){
						
					System.out.println("comptage des couples");

					nbValues = 7 + couples.length;
					
					counting = new CoupleCounting(inCellSize, values.length, couples, theoreticalSize, theoreticalCoupleSize);
						
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
						
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
						
						kernel = new SelectedFunctionalCountCoupleKernel(windowSize, pixels, coverage.getEntete(), values, function, dMax, builder.getWindowsPath());

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
						
					}else{
						
						kernel = new SelectedCountCoupleKernel(windowSize, pixels, coeffs, coverage.getEntete(), values, builder.getWindowsPath());
						
						// analysis
						return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
					}
						
				}else{
					
					System.out.println("comptage des valeurs et des couples");
					
					nbValues = 5 + values.length + 3 + couples.length;
					
					counting = new ValueAndCoupleCounting(inCellSize, values, couples, theoreticalSize, theoreticalCoupleSize);
					
					// add metrics to counting
					for(Metric m : metrics){
						counting.addMetric(m);
					}
					
					//observers
					for(CountingObserver co : observers){
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
						
						kernel = new SelectedFunctionalCountValueAndCoupleKernel(windowSize, pixels, coverage.getEntete(), values, function, dMax, builder.getWindowsPath());

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
						
					}else{
						
						kernel = new SelectedCountValueAndCoupleKernel(windowSize, pixels, coeffs, coverage.getEntete(), values, builder.getWindowsPath());

						// analysis
						return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
					}
					
				}
					
			}else if(MetricManager.hasOnlyPatchMetric(metrics)){ // patch
					
				nbValues = 8 + 4 * values.length;
				
				kernel = new SelectedPatchKernel(windowSize, pixels, coeffs, coverage.getEntete(), values, builder.getWindowsPath());
				
				counting =  new PatchCounting(inCellSize, values, theoreticalSize);
					
				// add metrics to counting
				for(Metric m : metrics){
					counting.addMetric(m);
				}
					
				//observers
				for(CountingObserver co : observers){
					counting.addObserver(co);
				}
					
				// analysis
				return createSingle(coverage, pixels, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, kernel, counting);
				
			}
		}
		return null;
	}
	
	protected abstract int[] readValues(Coverage coverage, Rectangle roi, int noDataValue);
	
	protected abstract SelectedLandscapeMetricAnalysis createSingle(Coverage coverage, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting);
	
	protected abstract SelectedLandscapeMetricAnalysis createMultiple(Coverage[] coverages, Set<Pixel> pixels, int roiX, int roiY, int roiWidth, int roiHeight, int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nbValues, SelectedLandscapeMetricKernel kernel, Counting counting);
	

	
}
