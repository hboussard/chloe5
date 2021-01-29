package fr.inra.sad.bagap.chloe.output;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.codecimpl.util.RasterFactory;

import fr.inra.sad.bagap.apiland.analysis.matrix.CoverageManager;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.MetricObserver;

public class GeoTiffOutput implements CountingObserver, MetricObserver{

	private String file;
	
	//private WritableRaster raster;
	
	private float[][] datas;
	
	private short width;
	
	private short height;
	
	private double outMinX, outMaxX, outMinY, outMaxY;
	
	private double cellSize;
	
	private short X;
	
	private short Y;
	
	public GeoTiffOutput(String file, short width, short height, double outMinX, double outMaxX, double outMinY, double outMaxY, double cellSize){
		this.file = file;
		this.width = width;
		this.height = height;
		this.outMinX = outMinX;
		this.outMaxX = outMaxX;
		this.outMinY = outMinY;
		this.outMaxY = outMaxY;
		this.cellSize = cellSize;
		this.datas = new float[height][width];
	}
	
	public void notify(Metric m, String metric, float value) {
		
		//System.out.println(value);
		//raster.setSample(X, Y, 0, value);
		
		datas[Y][X] = value;
		
		X++;
		if(X == width){
			X = 0;
			Y++;
		}
	}
	
	public void init() {
		X = 0;
		Y = 0;
	}
	
	public void close() {
		
		//raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
		//GridCoverage2D outC = CoverageManager.getCoverageUsingRaster(raster, width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
		
		//GridCoverage2D outC = CoverageManager.getEmptyCoverage(width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
		//((WritableRenderedImage) outC.getRenderedImage()).setData(raster);
		
		GridCoverage2D outC = CoverageManager.getCoverageFromData2D(datas, width, height, outMinX, outMinY, cellSize);
		
		System.out.println("écriture sur fichier");
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

	public void init(Counting c) {
	}

	public void prerun(Counting c) {
	}

	public void postrun(Counting c) {
	}
	
	public void close(Counting c){
	}

}
