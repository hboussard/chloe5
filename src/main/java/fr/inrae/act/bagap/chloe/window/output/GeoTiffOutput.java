package fr.inrae.act.bagap.chloe.window.output;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class GeoTiffOutput implements CountingObserver{

	private final DecimalFormat format;
	
	private final String file;
	
	private final Metric metric;

	private final float[] datas;
	
	private final int width, height, noDataValue;
	
	private final double outMinX, outMaxX, outMinY, outMaxY, cellSize;
	
	private int ind;
	
	public GeoTiffOutput(String file, Metric metric, int width, int height, double outMinX, double outMaxX, double outMinY, double outMaxY, double cellSize, int noDataValue){
		this.file = file;
		this.metric = metric;
		this.width = width;
		this.height = height;
		this.outMinX = outMinX;
		this.outMaxX = outMaxX;
		this.outMinY = outMinY;
		this.outMaxY = outMaxY;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.datas = new float[height*width];
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		ind = 0;
	}
	
	@Override
	public void prerun(Counting c) {
	}
	
	@Override
	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		datas[ind++] = Float.parseFloat(format(values.get(metric)));
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics) {
		//raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
		//GridCoverage2D outC = CoverageManager.getCoverageUsingRaster(raster, width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
				
		//GridCoverage2D outC = CoverageManager.getEmptyCoverage(width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
		//((WritableRenderedImage) outC.getRenderedImage()).setData(raster);
				
		GridCoverage2D outC = CoverageManager.getCoverageFromData(datas, width, height, outMinX, outMinY, cellSize);
				
		//System.out.println("ecriture sur fichier");
		try {
			GeoTiffWriteParams wp = new GeoTiffWriteParams();
			wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
			//wp.setCompressionType("LZW");
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
			GeoTiffWriter writer = new GeoTiffWriter(new File(file));
			writer.write(outC, params.values().toArray(new GeneralParameterValue[1]));
			writer.dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}

}
