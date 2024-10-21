package fr.inrae.act.bagap.chloe.window.output;

import java.util.Arrays;
import java.util.Set;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class InterpolateSplineGeoTiffOutput implements CountingObserver{
	
	private final String file;
	
	private final Metric metric;

	private final float[] datas;
	
	private final int width, height, noDataValue, delta;
	
	private final double outMinX, outMaxX, outMinY, outMaxY, cellSize;
	
	private final CoordinateReferenceSystem crs;
	
	private int maxI;
	
	private float[] line;
	
	private float[] oldLine;
	
	public InterpolateSplineGeoTiffOutput(String file, Metric metric, int width, int height, double outMinX, double outMaxX, double outMinY, double outMaxY, double cellSize, int noDataValue, CoordinateReferenceSystem crs, int displacement){
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
		this.crs = crs;
		this.delta = displacement; 
		this.datas = new float[height*width];
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		
		Arrays.fill(datas, noDataValue);
		
		line = new float[width];
		Arrays.fill(line, noDataValue);
		
		maxI = getMaxI(width, delta);
		
	}
	
	private int getMaxI(int w, int d) {
		return d*((w-1)/d);
	}
	
	@Override
	public void prerun(Counting c) {
	}
	
	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		//System.out.println(metric+" "+format(metric.value()));
		line[i] = (float) metric.value();
		if(i > 0){
			for(int x=1; x<delta; x++){
				line[i-delta+x] = Float.parseFloat(Util.format(droite(line[i-delta], line[i], x)));
			}
			
			if(i == maxI){
				if(j != 0){
					for(int y=1; y<delta; y++){			
						for(int x=0; x<width; x++){
							datas[(j-delta+y)*width+x] = Float.parseFloat(Util.format(droite(oldLine[x], line[x], y)));
						}
					}
				}
				for(int x=0; x<width; x++){
					datas[j*width+x] = line[x];	
				}
				
				oldLine = line.clone(); // stockage
				Arrays.fill(line, noDataValue); // nettoyage
			}
		}
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing;
	}
	
	@Override
	public void postrun(Counting c, Pixel p, Set<Metric> metrics) {
		// do nothing;
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics) {
		//raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
		//GridCoverage2D outC = CoverageManager.getCoverageUsingRaster(raster, width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
				
		//GridCoverage2D outC = CoverageManager.getEmptyCoverage(width, height, outMinX, outMaxX, outMinY, outMaxY, cellSize);
		//((WritableRenderedImage) outC.getRenderedImage()).setData(raster);
		/*	
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
		*/
		
		CoverageManager.writeGeotiff(file, datas, new EnteteRaster(width, height, outMinX, outMaxX, outMinY, outMaxY, (float) cellSize, noDataValue, crs));
	}
	
	private double droite(double v, double v_delta, double x){
		if(v == noDataValue || v_delta == noDataValue){
			return noDataValue;
		}
		return x*(v_delta-v)/delta + v;
	}

}
