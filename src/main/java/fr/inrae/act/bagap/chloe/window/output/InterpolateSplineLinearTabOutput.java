package fr.inrae.act.bagap.chloe.window.output;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class InterpolateSplineLinearTabOutput implements CountingObserver{

	private final float[] datas;
	
	private final Metric metric;
	
	private final int width, delta;
	
	private int maxI;
	
	private double[] line;
	
	private double[] oldLine;
	
	public InterpolateSplineLinearTabOutput(float[] datas, Metric metric, int width, int displacement){
		this.datas = datas;
		this.metric = metric;
		this.width = width;
		this.delta = displacement;
	}
	
	public Metric getMetric(){
		return metric;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		
		Arrays.fill(datas, Raster.getNoDataValue());
		
		line = new double[width];
		Arrays.fill(line, Raster.getNoDataValue());
		
		maxI = getMaxI(width, delta);
	}

	private int getMaxI(int w, int d) {
		return d*((w-1)/d);
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Map<Metric, Double> metrics) {
		
		line[i] = metrics.get(metric);
		if(i > 0){
			for(int x=1; x<delta; x++){
				line[i-delta+x] = splineValue(line[i-delta], line[i], x);
			}
			
			if(i == maxI){
				if(j != 0){
					for(int y=1; y<delta; y++){			
						for(int x=0; x<width; x++){
							datas[(j-delta+y)*width+x] = (float) splineValue(oldLine[x], line[x], y);
						}
					}
				}
				for(int x=0; x<width; x++){
					datas[j*width+x] = (float) line[x];	
				}
				
				oldLine = line.clone(); // stockage
				Arrays.fill(line, Raster.getNoDataValue()); // nettoyage
			}
		}
	}

	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing
	}
	
	private double splineValue(double v, double v_delta, double x){
		if(v == Raster.getNoDataValue() || v_delta == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		return x*(v_delta-v)/delta + v;
	}

}
