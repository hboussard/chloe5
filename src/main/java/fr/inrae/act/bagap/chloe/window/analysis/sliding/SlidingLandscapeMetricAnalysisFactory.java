package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.inrae.act.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inrae.act.bagap.apiland.analysis.distance.DistanceFunction;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.BasicCounting;
import fr.inrae.act.bagap.chloe.window.counting.ContinuityCounting;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.DegatErosionCounting;
import fr.inrae.act.bagap.chloe.window.counting.SourceErosionCounting;
import fr.inrae.act.bagap.chloe.window.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.SlopeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingBasicKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingPatchKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.erosion.SlidingMassCumulKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.erosion.SlidingSourceErosionKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.slope.SlidingSlopeKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian.FastGaussianWeightedCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian.FastGaussianWeightedCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian.FastGaussianWeightedCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian.FastGaussianWeightedGrainBocagerDetectionBocageKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.gaussian.FastGaussianWeightedQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square.FastSquareCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square.FastSquareCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square.FastSquareCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square.FastSquareGrainBocagerDistanceBocageKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.fast.square.FastSquareQuantitativeKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalBasicKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalContinuityKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager.GrainBocagerSlidingDetectionBocageKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager.GrainBocagerSlidingDistanceBocageKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.metric.ThematicDistanceMetric;
import fr.inrae.act.bagap.chloe.window.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.chloe.window.output.CsvOutput;
//import fr.inrae.act.bagap.chloe.window.output.CsvOutput2;
import fr.inrae.act.bagap.chloe.window.output.GeoTiffOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineGeoTiffOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearAsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearCsvOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearTabOutput;
import fr.inrae.act.bagap.chloe.window.output.TabOutput;
import fr.inrae.act.bagap.chloe.window.output.TileAsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.TileGeoTiffOutput;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;
import fr.inrae.act.bagap.apiland.raster.Tile;

public abstract class SlidingLandscapeMetricAnalysisFactory {

	public SlidingLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMinY = coverage.miny();
		double inMaxX = coverage.maxx();
		double inMaxY = coverage.maxy();
		float inCellSize = (float) coverage.cellsize();

		// displacement
		int displacement = builder.getDisplacement();

		// ROI
		int roiX = builder.getROIX();
		int roiY = builder.getROIY();
		int roiWidth = builder.getROIWidth();
		if (roiWidth == -1) {
			roiWidth = inWidth - roiX;
		}
		int roiHeight = builder.getROIHeight();
		if (roiHeight == -1) {
			roiHeight = inHeight - roiY;
		}

		// taille de sortie
		int outWidth = (int) (((roiWidth - 1) / displacement) + 1);
		int outHeight = (int) (((roiHeight - 1) / displacement) + 1);
		double outCellSize = inCellSize * displacement;
		double outMinX = inMinX + roiX * inCellSize + inCellSize / 2.0 - outCellSize / 2.0;
		double outMaxX = outMinX + outWidth * outCellSize;
		double outMaxY = inMaxY - roiY * inCellSize - inCellSize / 2.0 + outCellSize / 2.0;
		double outMinY = outMaxY - outHeight * outCellSize;

		// windowSize
		int windowSize = -1;
		if (builder.getWindowSize() > 0) {
			windowSize = builder.getWindowSize();
		} else if (builder.getWindowRadius() > 0) {
			double v = (2 * builder.getWindowRadius() / inCellSize);
			windowSize = v % 2 == 0 ? (int)(v + 1) : (int)(v);
		} else {
			throw new IllegalArgumentException("windowSize must be defined");
		}
		int midWindowSize = (int) (windowSize / 2);

		// buffer ROI
		int bufferROIXMin = Math.min(roiX, midWindowSize);
		int bufferROIXMax = Math.min(inWidth - (roiX + roiWidth), midWindowSize);
		int bufferROIYMin = Math.min(roiY, midWindowSize);
		int bufferROIYMax = Math.min(inHeight - (roiY + roiHeight), midWindowSize);

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
		
		double theoreticalSize = 0;
		double theoreticalCoupleSize = 0;
		if(builder.getWindowShapeType() == WindowShapeType.SQUARE || builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
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

		// observers
		//Set<CountingObserver> observers = builder.getObservers();
		Set<CountingObserver> observers = new HashSet<CountingObserver>();
		
		if(builder.getCoverageOutputs() != null){
			for(CoverageOutput coverageOutput : builder.getCoverageOutputs()) {
				coverageOutput.setEntete(new EnteteRaster(outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, (float) outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs()));
				observers.add(coverageOutput);
			}
		}
		
		if (builder.getCsv() != null) {
			if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
				//CsvOutput2 csvOutput = new CsvOutput2(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight,
				 //outCellSize, Raster.getNoDataValue());
				CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth,	outHeight, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
				observers.add(csvOutput);
			} else {
				InterpolateSplineLinearCsvOutput csvOutput = new InterpolateSplineLinearCsvOutput(builder.getCsv(),
						inMinX + (roiX * inCellSize),
						inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize),
						inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize),
						inMaxY - (roiY * inCellSize), roiWidth, roiHeight, inCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs(),
						displacement);
				observers.add(csvOutput);
			}
		}else if (builder.getCsvFolder() != null){
			String name = builder.getCsvFolder()+new File(builder.getRasterFile()).getName().replace(".tif", "").replace(".asc", "").toString()+".csv";
			if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
				CsvOutput csvOutput = new CsvOutput(name, outMinX, outMaxX, outMinY, outMaxY, outWidth,	outHeight, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
				observers.add(csvOutput);
			} else {
				InterpolateSplineLinearCsvOutput csvOutput = new InterpolateSplineLinearCsvOutput(name,
						inMinX + (roiX * inCellSize),
						inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize),
						inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize),
						inMaxY - (roiY * inCellSize), roiWidth, roiHeight, inCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs(),
						displacement);
				observers.add(csvOutput);
			}
		}
		
		if(builder.getTileAsciiGridOutputs() != null){
			for (Entry<Tile, Map<String, String>> entry : builder.getTileAsciiGridOutputs().entrySet()) {
				for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
					Metric metric = null;
					for (Metric m : metrics) {
						if (m.getName().equalsIgnoreCase(entry2.getKey())) {
							metric = m;
							break;
						}
					}
					if(metric != null){
						TileAsciiGridOutput tileAsciiOutput = new TileAsciiGridOutput(entry2.getValue(), metric, entry.getKey(), outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
						observers.add(tileAsciiOutput);
					}
				}
			}
		}
		
		if(builder.getTileGeoTiffOutputs() != null){
			for (Entry<Tile, Map<String, String>> entry : builder.getTileGeoTiffOutputs().entrySet()) {
				for (Entry<String, String> entry2 : entry.getValue().entrySet()) {
					Metric metric = null;
					for (Metric m : metrics) {
						if (m.getName().equalsIgnoreCase(entry2.getKey())) {
							metric = m;
							break;
						}
					}
					if(metric != null){
						TileGeoTiffOutput tileGeoTiffOutput = new TileGeoTiffOutput(entry2.getValue(), metric, entry.getKey(), outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
						observers.add(tileGeoTiffOutput);
					}
				}
			}
		}
		
		if(builder.getAsciiOutputs(windowSize) != null){
			for (Entry<String, String> entry : builder.getAsciiOutputs(windowSize).entrySet()) {
				Metric metric = null;
				for (Metric m : metrics) {
					if (m.getName().equalsIgnoreCase(entry.getKey())) {
						metric = m;
						break;
					}
				}
				if(metric != null){
					if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
						AsciiGridOutput asciiOutput = new AsciiGridOutput(entry.getValue(), metric, outWidth, outHeight,
								outMinX, outMinY, outCellSize, coverage.getEntete().noDataValue());
						observers.add(asciiOutput);
					} else {
						InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(
								entry.getValue(), metric, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
								inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize), inCellSize,
								coverage.getEntete().noDataValue(), displacement);
						observers.add(asciiOutput);
					}
				}
			}
		}
		
		if(builder.getAsciiGridFolder() != null){
			
			String asciiFolder = builder.getAsciiGridFolder();
			if(!(asciiFolder.endsWith("/") || asciiFolder.endsWith("\\"))){
				asciiFolder += "/";
			}
			String asciiFile;
			for (Metric m : metrics) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(new File(builder.getRasterFile()).getName().replace(".asc", "").replace(".tif", "")); // file name, assume it exists
				sb.append("_"+m.getName());
				sb.append("_"+builder.getWindowSize());
				sb.append(".asc");
				
				asciiFile = asciiFolder+sb.toString();
				
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					AsciiGridOutput asciiOutput = new AsciiGridOutput(asciiFile, m, outWidth, outHeight,
							outMinX, outMinY, outCellSize, coverage.getEntete().noDataValue());
					observers.add(asciiOutput);
				} else {
					InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(
							asciiFile, m, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
							inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize), inCellSize,
							coverage.getEntete().noDataValue(), displacement);
					observers.add(asciiOutput);
				}
			}
		}
		
		if(builder.getGeoTiffOutputs(windowSize) != null){
			for (Entry<String, String> entry : builder.getGeoTiffOutputs(windowSize).entrySet()) {
				Metric metric = null;
				for (Metric m : metrics) {
					if (m.getName().equalsIgnoreCase(entry.getKey())) {
						metric = m;
						break;
					}
				}
				if(metric != null){
					//System.out.println("tiff for "+metric);
					if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
						GeoTiffOutput geotiffOutput = new GeoTiffOutput(entry.getValue(), metric, outWidth, outHeight, outMinX,
								outMaxX, outMinY, outMaxY, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
						observers.add(geotiffOutput);
					} else {
						InterpolateSplineGeoTiffOutput geotiffOutput = new InterpolateSplineGeoTiffOutput(entry.getValue(), metric, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
								inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize),
								inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize),
								inMaxY - (roiY * inCellSize), inCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs(), displacement);
						observers.add(geotiffOutput);
					}
				}
			}
		}
		
		if(builder.getGeoTiffFolder() != null){
			
			String tifFolder = builder.getGeoTiffFolder();
			if(!(tifFolder.endsWith("/") || tifFolder.endsWith("\\"))){
				tifFolder += "/";
			}
			String tifFile;
			for (Metric m : metrics) {
				
				StringBuffer sb = new StringBuffer();
				sb.append(new File(builder.getRasterFile()).getName().replace(".asc", "").replace(".tif", "")); // file name, assume it exists
				sb.append("_"+m.getName());
				sb.append("_"+builder.getWindowSize());
				sb.append(".tif");
				
				tifFile = tifFolder+sb.toString();
				
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					GeoTiffOutput geotiffOutput = new GeoTiffOutput(tifFile, m, outWidth, outHeight, outMinX,
							outMaxX, outMinY, outMaxY, outCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs());
					observers.add(geotiffOutput);
				} else {
					InterpolateSplineGeoTiffOutput geotiffOutput = new InterpolateSplineGeoTiffOutput(tifFile, m, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
							inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize),
							inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize),
							inMaxY - (roiY * inCellSize), inCellSize, coverage.getEntete().noDataValue(), coverage.getEntete().crs(), displacement);
					observers.add(geotiffOutput);
				}
			}
		}
		
		if(builder.getTabOutputs(windowSize) != null){
			for (Entry<String, float[]> entry : builder.getTabOutputs(windowSize).entrySet()) {
				Metric metric = null;
				for (Metric m : metrics) {
					if (m.getName().equalsIgnoreCase(entry.getKey())) {
						metric = m;
						break;
					}
				}
				if(metric != null){
					if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
						TabOutput tabOutput = new TabOutput(entry.getValue(), metric, outWidth, displacement);
						observers.add(tabOutput);
					} else {
						InterpolateSplineLinearTabOutput tabOutput = new InterpolateSplineLinearTabOutput(entry.getValue(), metric, roiWidth, displacement, coverage.getEntete().noDataValue());
						observers.add(tabOutput);
					}
				}
			}
		}

		// les non-filtres
		int[] unfilters = builder.getUnfilters();
		
		// kernel et counting
		SlidingLandscapeMetricKernel kernel = null;
		Counting counting = null;
		int nbValues = -1;
		
		// gestion specifiques des analyses quantitatives ou qualitatives
		if(MetricManager.hasOnlyBasicMetric(metrics)){
			
			System.out.println("comptage basique");
			
			nbValues = 5;
			
			counting = new BasicCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}
			
			if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {

				kernel = new SlidingFunctionalBasicKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, inCellSize, function, dMax);

				Coverage coverageFriction = null;
				if (builder.getRasterFile2() != null) {
					coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
				} else if (builder.getRasterTab2() != null) {
					coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
				} else if (builder.getCoverage2() != null) {
					coverageFriction = builder.getCoverage2();
				} else if (builder.getRasterTabs() != null) {
					coverageFriction = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
				} else {
					throw new IllegalArgumentException("no raster2 declared");
				}

				return createMultiple(new Coverage[] {coverage, coverageFriction}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
				//return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);

			} else {
				
				kernel = new SlidingBasicKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), unfilters);
				
				// analysis
				return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			}
			
		}else if (MetricManager.hasOnlySlopeMetric(metrics)) { // slope
			
			nbValues = 6;
			
			kernel = new SlidingSlopeKernel(coverage.getEntete().noDataValue(), inCellSize);
			
			counting = new SlopeCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}

			// analysis
			return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			
		}else if (MetricManager.hasOnlyQuantitativeMetric(metrics)) { // quantitative

			nbValues = 8;
			
			if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("MD")) {
				
				kernel = new SlidingQuantitativeKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), 100, unfilters);
				
			} else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBDistance")) {

				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE){
					
					kernel = new FastSquareGrainBocagerDistanceBocageKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, 5, 3, 30);
					
				}else{
					
					kernel = new GrainBocagerSlidingDistanceBocageKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), unfilters, 5, 3, 30);
				}

				Coverage coverageBocage = null;
				if (builder.getRasterFile2() != null) {
					coverageBocage = CoverageManager.getCoverage(builder.getRasterFile2());
				} else if (builder.getRasterTab2() != null) {
					coverageBocage = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
				} else if (builder.getCoverage2() != null) {
					coverageBocage = builder.getCoverage2();
				} else if (builder.getRasterTabs() != null) {
					coverageBocage = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
				} else {
					throw new IllegalArgumentException("no raster 'type de boisement' declared");
				}

				counting = new QuantitativeCounting(inCellSize, theoreticalSize);

				// add metrics to counting
				for (Metric m : metrics) {
					counting.addMetric(m);
				}

				// observers
				for (CountingObserver co : observers) {
					counting.addObserver(co);
				}

				// analysis
				return createMultiple(new Coverage[] {coverage, coverageBocage}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
				//return createDouble(coverage, coverageBocage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
				

			} else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBBocage")) {
				
				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN){
					
					kernel = new FastGaussianWeightedGrainBocagerDetectionBocageKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, 3);
				}else{
					
					kernel = new GrainBocagerSlidingDetectionBocageKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), unfilters, 3);
				}
				
			} else {
				
				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
					
					kernel = new FastGaussianWeightedQuantitativeKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters);
				
				} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
					
					kernel = new FastSquareQuantitativeKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters);
				
				} else{
					
					kernel = new SlidingQuantitativeKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), unfilters);
				}
			}
			
			counting = new QuantitativeCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}

			// analysis
			return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);

		}else if (MetricManager.hasOnlyContinuityMetric(metrics) && builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) { // continuity 
		
			nbValues = 6;
			
			kernel = new SlidingFunctionalContinuityKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, inCellSize, function, dMax);
			
			counting = new ContinuityCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}

			Coverage coverageFriction = null;
			if (builder.getRasterFile2() != null) {
				coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
			} else if (builder.getRasterTab2() != null) {
				coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
			} else if (builder.getCoverage2() != null) {
				coverageFriction = builder.getCoverage2();
			} else if (builder.getRasterTabs() != null) {
				coverageFriction = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster2 declared");
			}

			return createMultiple(new Coverage[] {coverage, coverageFriction}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			//return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			
		}else if (MetricManager.hasOnlySourceErosionMetric(metrics)) { // erosion
		
			nbValues = 6;
			
			String outputDegatIntensity = null;
			String outputDepotIntensity = null;
			if(builder.getGeoTiffOutputs(windowSize) != null) {
				outputDegatIntensity = builder.getGeoTiffOutputs(windowSize).get("degat-erosion-intensity");
				outputDepotIntensity = builder.getGeoTiffOutputs(windowSize).get("depot-erosion-intensity");
			}
			
			EnteteRaster outEntete = new EnteteRaster(outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, (float) outCellSize, coverage.getEntete().noDataValue());
			
			kernel = new SlidingSourceErosionKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, coverage.getEntete(), outEntete, builder.getInterpolation(), outputDegatIntensity, outputDepotIntensity);
			
			counting = new SourceErosionCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}

			Coverage coverageAltitude = null;
			if (builder.getRasterFile2() != null) {
				coverageAltitude = CoverageManager.getCoverage(builder.getRasterFile2());
			} else if (builder.getRasterTab2() != null) {
				coverageAltitude = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
			} else if (builder.getCoverage2() != null) {
				coverageAltitude = builder.getCoverage2();
			} else if (builder.getRasterTabs() != null) {
				coverageAltitude = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster2 declared");
			}
			
			Coverage coverageInfiltration = null;
			if (builder.getRasterFile3() != null) {
				coverageInfiltration = CoverageManager.getCoverage(builder.getRasterFile3());
			} else if (builder.getRasterTab3() != null) {
				coverageInfiltration = new TabCoverage(builder.getRasterTab3(), coverage.getEntete());
			} else if (builder.getCoverage3() != null) {
				coverageInfiltration = builder.getCoverage3();
			} else if (builder.getRasterTabs() != null) {
				coverageInfiltration = new TabCoverage(builder.getRasterTabs()[2], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster3 declared");
			}
			
			Coverage coverageSlopeIntensity = null;
			if (builder.getRasterTabs() != null) {
				coverageSlopeIntensity = new TabCoverage(builder.getRasterTabs()[3], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster4 declared");
			}

			return createMultiple(new Coverage[] {coverage, coverageAltitude, coverageInfiltration, coverageSlopeIntensity}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			//return createTriple(coverage, coverageAltitude, coverageInfiltration, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			
		}else if (MetricManager.hasOnlyDegatErosionMetric(metrics)) { // degat erosion
		
			nbValues = 6;
			
			String outputDegatIntensity = null,  outputDepotIntensity = null;
			if(builder.getGeoTiffOutputs(windowSize) != null) {
				outputDegatIntensity = builder.getGeoTiffOutputs(windowSize).get("degat-mass-cumul");
				outputDepotIntensity = builder.getGeoTiffOutputs(windowSize).get("depot-mass-cumul");
			}
			
			counting = new DegatErosionCounting(inCellSize, theoreticalSize);

			// add metrics to counting
			for (Metric m : metrics) {
				counting.addMetric(m);
			}

			// observers
			for (CountingObserver co : observers) {
				counting.addObserver(co);
			}
			
			EnteteRaster outEntete = new EnteteRaster(outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, (float) outCellSize, coverage.getEntete().noDataValue());
			
			// version avec altitude 
			kernel = new SlidingMassCumulKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, coverage.getEntete(), outEntete, outputDegatIntensity/*, outputDepotIntensity*/);
			
			Coverage coverageAltitude = null;
			if (builder.getRasterTabs() != null) {
				coverageAltitude = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster2 declared");
			}
			
			Coverage coverageInfiltration = null;
			if (builder.getRasterTabs() != null) {
				coverageInfiltration = new TabCoverage(builder.getRasterTabs()[2], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster3 declared");
			}
			
			Coverage coverageSlopeIntensity = null;
			if (builder.getRasterTabs() != null) {
				coverageSlopeIntensity = new TabCoverage(builder.getRasterTabs()[3], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster4 declared");
			}
			
			return createMultiple(new Coverage[] {coverage, coverageAltitude, coverageInfiltration, coverageSlopeIntensity}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			//return createMultiple(new Coverage[] {coverage, coverageAltitude, coverageInfiltration}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			
			
			// version avec pente
			/*
			kernel = new SlidingDegatErosionPenteKernel(windowSize, displacement, coverage.getEntete().noDataValue(), unfilters, inCellSize, coverage.getEntete(), (int) dMax);
			
			Coverage coverageSlopeIntensity = null;
			if (builder.getRasterFile2() != null) {
				coverageSlopeIntensity = CoverageManager.getCoverage(builder.getRasterFile2());
			} else if (builder.getRasterTab2() != null) {
				coverageSlopeIntensity = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
			} else if (builder.getCoverage2() != null) {
				coverageSlopeIntensity = builder.getCoverage2();
			} else if (builder.getRasterTabs() != null) {
				coverageSlopeIntensity = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster2 declared");
			}
			
			Coverage coverageSlopeDirection = null;
			if (builder.getRasterFile2() != null) {
				coverageSlopeDirection = CoverageManager.getCoverage(builder.getRasterFile3());
			} else if (builder.getRasterTab2() != null) {
				coverageSlopeDirection = new TabCoverage(builder.getRasterTab3(), coverage.getEntete());
			} else if (builder.getCoverage2() != null) {
				coverageSlopeDirection = builder.getCoverage3();
			} else if (builder.getRasterTabs() != null) {
				coverageSlopeDirection = new TabCoverage(builder.getRasterTabs()[2], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster3 declared");
			}
			
			Coverage coverageInfiltration = null;
			if (builder.getRasterTabs() != null) {
				coverageInfiltration = new TabCoverage(builder.getRasterTabs()[3], coverage.getEntete());
			} else {
				throw new IllegalArgumentException("no raster4 declared");
			}
			
			return createMultiple(new Coverage[] {coverage, coverageSlopeIntensity, coverageSlopeDirection, coverageInfiltration}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			*/
			
		}else{ // qualitative or patch
			
			// recuperation des valeurs
			int[] values = builder.getValues();
			if (values == null) {
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight), coverage.getEntete().noDataValue());
			}
			
			// les non-filtres
			int[] filters = builder.getFilters();
			if(filters != null){
				
				Set<Integer> inverseFilters = new TreeSet<Integer>();
				
				if(unfilters != null){
					for(int uf : unfilters){
						inverseFilters.add(uf);
					}
				}
				
				for(int v : values){
					boolean ok = false;
					for(int f : filters){
						if(v == f){
							ok = true;
							break;
						}
					}
					if(!ok){
						inverseFilters.add(v);
					}
				}
				
				boolean ok = false;
				for(int f : filters){
					if(coverage.getEntete().noDataValue() == f){
						ok = true;
						break;
					}
					if(!ok){
						inverseFilters.add(coverage.getEntete().noDataValue());
					}
				}
				
				unfilters = new int[inverseFilters.size()];
				Iterator<Integer> ite = inverseFilters.iterator();
				for(int i=0; i<inverseFilters.size(); i++){
					unfilters[i] = ite.next();
				}
			}
			
			
			
			if (MetricManager.hasOnlyQualitativeMetric(metrics)) { // qualitative
				
				// initialisation des metriques de distance thematique
				for(Metric m : metrics) {
					if(m instanceof ThematicDistanceMetric) {
						MetricManager.initThematicDistanceMetric(builder.getThematicDistanceFile(), (ThematicDistanceMetric) m, values);
					}
				}
				
				// recuperation des couples
				float[] couples = null;
				if (MetricManager.hasCoupleMetric(metrics)) {
					couples = new float[(((values.length * values.length) - values.length) / 2) + values.length];
					int index = 0;
					for (int s1 : values) {
						couples[index++] = Couple.getCouple(s1, s1);
					}
					for (int s1 : values) {
						for (int s2 : values) {
							if (s1 < s2) {
								couples[index++] = Couple.getCouple(s1, s2);
							}
						}
					}
				}

				if (MetricManager.hasOnlyValueMetric(metrics)) {

					System.out.println("comptage des valeurs");

					nbValues = 5 + values.length;
					
					counting = new ValueCounting(inCellSize, values, theoreticalSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}

					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {

						kernel = new SlidingFunctionalCountValueKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else if (builder.getRasterTabs() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						//return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);

					} else {
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN){
							
							kernel = new FastGaussianWeightedCountValueKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE){
							
							kernel = new FastSquareCountValueKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountValueKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), values, unfilters);
						}

						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
					}

				} else if (MetricManager.hasOnlyCoupleMetric(metrics)) {

					System.out.println("comptage des couples");

					nbValues = 7 + couples.length;
					
					counting = new CoupleCounting(inCellSize, values.length, couples, theoreticalSize, theoreticalCoupleSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
						
						kernel = new SlidingFunctionalCountCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else if (builder.getRasterTabs() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						//return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						
					}else{
						
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
							
							kernel = new FastGaussianWeightedCountCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
							
							kernel = new FastSquareCountCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountCoupleKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), values, unfilters);
						}
						
						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
					}

				} else {

					System.out.println("comptage des valeurs et des couples");

					nbValues = 5 + values.length + 3 + couples.length;
					
					counting = new ValueAndCoupleCounting(inCellSize, values, couples, theoreticalSize, theoreticalCoupleSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
					
						kernel = new SlidingFunctionalCountValueAndCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else if (builder.getRasterTabs() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTabs()[1], coverage.getEntete());
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createMultiple(new Coverage[] {coverage, coverageFriction}, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						//return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						
					}else{
						
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
							
							kernel = new FastGaussianWeightedCountValueAndCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
							
							kernel = new FastSquareCountValueAndCoupleKernel(windowSize, displacement, coverage.getEntete().noDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountValueAndCoupleKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), values, unfilters);
						}

						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);					
					}
				}

			} else if (MetricManager.hasOnlyPatchMetric(metrics)) { // patch
				

				System.out.println("comptage des patchs");
				
				nbValues = 8 + 4 * values.length;

				kernel = new SlidingPatchKernel(windowSize, displacement, coeffs, coverage.getEntete().noDataValue(), values, inCellSize, unfilters);
				
				counting = new PatchCounting(inCellSize, values, theoreticalSize);

				// add metrics to counting
				for (Metric m : metrics) {
					counting.addMetric(m);
				}

				// observers
				for (CountingObserver co : observers) {
					counting.addObserver(co);
				}

				// analysis
				return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin,	bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
			
			}
		}
		
		return null;
		
	}
	
	protected abstract int[] readValues(Coverage coverage, Rectangle roi, int noDataValue);
	
	
	
	protected abstract SlidingLandscapeMetricAnalysis createSingle(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting);
	
	protected abstract SlidingLandscapeMetricAnalysis createMultiple(Coverage[] coverages, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting);
	

}
