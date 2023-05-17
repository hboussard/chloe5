package fr.inrae.act.bagap.chloe.window.analysis.sliding;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;

import fr.inra.sad.bagap.apiland.analysis.combination.CombinationExpressionFactory;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.distance.DistanceFunction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.counting.CoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.PatchCounting;
import fr.inrae.act.bagap.chloe.window.counting.QuantitativeCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueAndCoupleCounting;
import fr.inrae.act.bagap.chloe.window.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingLandscapeMetricKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingPatchKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.SlidingQuantitativeKernel;
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
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountValueAndCoupleKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.functional.SlidingFunctionalCountValueKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager.GrainBocagerSlidingDetectionBocageKernel;
import fr.inrae.act.bagap.chloe.window.kernel.sliding.grainbocager.GrainBocagerSlidingDistanceBocageKernel;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.MetricManager;
import fr.inrae.act.bagap.chloe.window.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.CsvOutput;
import fr.inrae.act.bagap.chloe.window.output.GeoTiffOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearAsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearCsvOutput;
import fr.inrae.act.bagap.chloe.window.output.InterpolateSplineLinearTabOutput;
import fr.inrae.act.bagap.chloe.window.output.TabOutput;
import fr.inrae.act.bagap.chloe.window.output.TileAsciiGridOutput;
import fr.inrae.act.bagap.chloe.window.output.TileGeoTiffOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.TabCoverage;
import fr.inrae.act.bagap.raster.Tile;

public abstract class SlidingLandscapeMetricAnalysisFactory {

	public SlidingLandscapeMetricAnalysis create(LandscapeMetricAnalysisBuilder builder, Coverage coverage) throws IOException {
		
		int inWidth = coverage.width();
		int inHeight = coverage.height();
		double inMinX = coverage.minx();
		double inMinY = coverage.miny();
		double inMaxX = coverage.maxx();
		double inMaxY = coverage.maxy();
		double inCellSize = coverage.cellsize();

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
			windowSize = v % 2 == 0 ? new Double(v + 1).intValue() : new Double(v).intValue();
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

		double dMax = midWindowSize * inCellSize;
		DistanceFunction function = CombinationExpressionFactory.createDistanceFunction(builder.getWindowDistanceFunction(), dMax);
		
		double theoreticalSize = 0;
		int theoreticalCoupleSize = 0;
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
		Set<CountingObserver> observers = builder.getObservers();
		if (builder.getCsv() != null) {
			if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
				// CsvOutput2 csvOutput = new CsvOutput2(builder.getCsv(),
				// outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight,
				// outCellSize, Raster.getNoDataValue());
				CsvOutput csvOutput = new CsvOutput(builder.getCsv(), outMinX, outMaxX, outMinY, outMaxY, outWidth,
						outHeight, outCellSize, Raster.getNoDataValue());
				observers.add(csvOutput);
			} else {
				InterpolateSplineLinearCsvOutput csvOutput = new InterpolateSplineLinearCsvOutput(builder.getCsv(),
						inMinX + (roiX * inCellSize),
						inMaxX - ((inWidth - roiX) * inCellSize) + (roiWidth * inCellSize),
						inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize),
						inMaxY - (roiY * inCellSize), roiWidth, roiHeight, inCellSize, Raster.getNoDataValue(),
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
					TileAsciiGridOutput tileAsciiOutput = new TileAsciiGridOutput(entry2.getValue(), metric, entry.getKey(), outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, Raster.getNoDataValue());
					observers.add(tileAsciiOutput);
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
					TileGeoTiffOutput tileGeoTiffOutput = new TileGeoTiffOutput(entry2.getValue(), metric, entry.getKey(), outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, Raster.getNoDataValue());
					observers.add(tileGeoTiffOutput);
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
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					AsciiGridOutput asciiOutput = new AsciiGridOutput(entry.getValue(), metric, outWidth, outHeight,
							outMinX, outMinY, outCellSize, Raster.getNoDataValue());
					observers.add(asciiOutput);
				} else {
					InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(
							entry.getValue(), metric, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
							inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize), inCellSize,
							Raster.getNoDataValue(), displacement);
					observers.add(asciiOutput);
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
				if(builder.getWindowShapeType() == WindowShapeType.CIRCLE){
					sb.append("_cr");
				}else if(builder.getWindowShapeType() == WindowShapeType.SQUARE){
					sb.append("_sq");
				}else if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
					sb.append("_fu");
				}
				sb.append("_w"+builder.getWindowSize());
				sb.append("_"+m.getName());
				sb.append("_d_"+builder.getDisplacement());
				sb.append(".asc");
				
				asciiFile = asciiFolder+sb.toString();
				
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					AsciiGridOutput asciiOutput = new AsciiGridOutput(asciiFile, m, outWidth, outHeight,
							outMinX, outMinY, outCellSize, Raster.getNoDataValue());
					observers.add(asciiOutput);
				} else {
					InterpolateSplineLinearAsciiGridOutput asciiOutput = new InterpolateSplineLinearAsciiGridOutput(
							asciiFile, m, roiWidth, roiHeight, inMinX + (roiX * inCellSize),
							inMinY + ((inHeight - roiY) * inCellSize) - (roiHeight * inCellSize), inCellSize,
							Raster.getNoDataValue(), displacement);
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
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					GeoTiffOutput geotiffOutput = new GeoTiffOutput(entry.getValue(), metric, outWidth, outHeight, outMinX,
							outMaxX, outMinY, outMaxY, outCellSize, Raster.getNoDataValue());
					observers.add(geotiffOutput);
				} else {
					// TODO
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
				if(builder.getWindowShapeType() == WindowShapeType.CIRCLE){
					sb.append("_cr");
				}else if(builder.getWindowShapeType() == WindowShapeType.SQUARE){
					sb.append("_sq");
				}else if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
					sb.append("_fu");
				}
				sb.append("_w"+builder.getWindowSize());
				sb.append("_"+m.getName());
				sb.append("_d_"+builder.getDisplacement());
				sb.append(".tif");
				
				tifFile = tifFolder+sb.toString();
				
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					GeoTiffOutput geotiffOutput = new GeoTiffOutput(tifFile, m, outWidth, outHeight, outMinX,
							outMaxX, outMinY, outMaxY, outCellSize, Raster.getNoDataValue());
					observers.add(geotiffOutput);
				} else {
					// TODO
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
				if (builder.getDisplacement() == 1 || builder.getInterpolation() == false) {
					TabOutput tabOutput = new TabOutput(entry.getValue(), metric, outWidth, displacement);
					observers.add(tabOutput);
				} else {
					InterpolateSplineLinearTabOutput tabOutput = new InterpolateSplineLinearTabOutput(entry.getValue(),
							metric, roiWidth, displacement);
					observers.add(tabOutput);
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
		if (MetricManager.hasOnlyQuantitativeMetric(metrics)) { // quantitative

			nbValues = 8;
			
			if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("MD")) {
				
				kernel = new SlidingQuantitativeKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), 100, unfilters);
				
			} else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBDistance")) {

				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE){
					
					kernel = new FastSquareGrainBocagerDistanceBocageKernel(windowSize, displacement, Raster.getNoDataValue(), unfilters, 5, 3, 30);
					
				}else{
					
					kernel = new GrainBocagerSlidingDistanceBocageKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), unfilters, 5, 3, 30);
				}

				Coverage coverageBocage = null;
				if (builder.getRasterFile2() != null) {
					coverageBocage = CoverageManager.getCoverage(builder.getRasterFile2());
				} else if (builder.getRasterTab2() != null) {
					coverageBocage = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
				} else if (builder.getCoverage2() != null) {
					coverageBocage = builder.getCoverage2();
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
				return createDouble(coverage, coverageBocage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
				

			} else if (metrics.size() == 1 && metrics.iterator().next().getName().equalsIgnoreCase("GBBocage")) {
				
				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN){
					
					kernel = new FastGaussianWeightedGrainBocagerDetectionBocageKernel(windowSize, displacement, Raster.getNoDataValue(), unfilters, 3);
				}else{
					
					kernel = new GrainBocagerSlidingDetectionBocageKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), unfilters, 3);
				}
				
			} else {
				if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
					
					kernel = new FastGaussianWeightedQuantitativeKernel(windowSize, displacement, Raster.getNoDataValue(), unfilters);
				
				} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
					
					kernel = new FastSquareQuantitativeKernel(windowSize, displacement, Raster.getNoDataValue(), unfilters);
				
				} else{
					
					kernel = new SlidingQuantitativeKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), unfilters);
					
				}
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
			return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);

		} else{
			// recuperation des valeurs
			int[] values = builder.getValues();
			if (values == null) {
				values = readValues(coverage, new Rectangle(roiX, roiY, roiWidth, roiHeight));
			}
			
			if (MetricManager.hasOnlyQualitativeMetric(metrics)) { // qualitative
				
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
					
					counting = new ValueCounting(values, theoreticalSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}

					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {

						kernel = new SlidingFunctionalCountValueKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);

					} else {
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN){
							
							kernel = new FastGaussianWeightedCountValueKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE){
							
							kernel = new FastSquareCountValueKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountValueKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), values, unfilters);
						}

						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
					}

				} else if (MetricManager.hasOnlyCoupleMetric(metrics)) {

					System.out.println("comptage des couples");

					nbValues = 7 + couples.length;
					
					counting = new CoupleCounting(values.length, couples, theoreticalSize, theoreticalCoupleSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
						
						kernel = new SlidingFunctionalCountCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						
					}else{
						
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
							
							kernel = new FastGaussianWeightedCountCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
							
							kernel = new FastSquareCountCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountCoupleKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), values, unfilters);
						}
						
						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
					}

				} else {

					System.out.println("comptage des valeurs et des couples");

					nbValues = 5 + values.length + 3 + couples.length;
					
					counting = new ValueAndCoupleCounting(values, couples, theoreticalSize, theoreticalCoupleSize);

					// add metrics to counting
					for (Metric m : metrics) {
						counting.addMetric(m);
					}

					// observers
					for (CountingObserver co : observers) {
						counting.addObserver(co);
					}
					
					if (builder.getWindowShapeType() == WindowShapeType.FUNCTIONAL) {
					
						kernel = new SlidingFunctionalCountValueAndCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters, inCellSize, function, dMax);

						Coverage coverageFriction = null;
						if (builder.getRasterFile2() != null) {
							coverageFriction = CoverageManager.getCoverage(builder.getRasterFile2());
						} else if (builder.getRasterTab2() != null) {
							coverageFriction = new TabCoverage(builder.getRasterTab2(), coverage.getEntete());
						} else if (builder.getCoverage2() != null) {
							coverageFriction = builder.getCoverage2();
						} else {
							throw new IllegalArgumentException("no raster2 declared");
						}

						return createDouble(coverage, coverageFriction, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);
						
					}else{
						
						if (builder.getWindowDistanceType() == WindowDistanceType.FAST_GAUSSIAN) {
							
							kernel = new FastGaussianWeightedCountValueAndCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else if (builder.getWindowDistanceType() == WindowDistanceType.FAST_SQUARE) {
							
							kernel = new FastSquareCountValueAndCoupleKernel(windowSize, displacement, Raster.getNoDataValue(), values, unfilters);
						
						} else {
							
							kernel = new SlidingCountValueAndCoupleKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), values, unfilters);
						}

						// analysis
						return createSingle(coverage, roiX, roiY, roiWidth, roiHeight, bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax, nbValues, displacement, kernel, counting);					
					}
				}

			} else if (MetricManager.hasOnlyPatchMetric(metrics)) { // patch
				

				nbValues = 7 + 3 * values.length;

				kernel = new SlidingPatchKernel(windowSize, displacement, coeffs, Raster.getNoDataValue(), values, inCellSize, unfilters);
				
				counting = new PatchCounting(values, theoreticalSize);

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
	
	protected abstract int[] readValues(Coverage coverage, Rectangle roi);
	
	protected abstract SlidingLandscapeMetricAnalysis createSingle(Coverage coverage, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting);
	
	protected abstract SlidingLandscapeMetricAnalysis createDouble(Coverage coverage, Coverage coverage2, int roiX, int roiY, int roiWidth, int roiHeight, 
			int bufferROIXMin, int bufferROIXMax, int bufferROIYMin, int bufferROIYMax, int nb, int displacement, SlidingLandscapeMetricKernel kernel, Counting counting);
	

}