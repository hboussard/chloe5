package fr.inrae.act.bagap.chloe.window.output;

import java.util.Arrays;
import java.util.Set;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class InterpolateSplineLinearTabOutput implements CountingObserver{

	private final float[] datas;
	
	private final Metric metric;
	
	private final int width, delta;
	
	private int maxI;
	
	private float[] line;
	
	private float[] oldLine;
	
	private int noDataValue;
	
	public InterpolateSplineLinearTabOutput(float[] datas, Metric metric, int width, int displacement, int noDataValue){
		this.datas = datas;
		this.metric = metric;
		this.width = width;
		this.delta = displacement;
		this.noDataValue = noDataValue;
	}
	
	public Metric getMetric(){
		return metric;
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
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		
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
		// do nothing
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing
	}
	
	private double droite(double v, double v_delta, double x){
		if(v == noDataValue || v_delta == noDataValue){
			return noDataValue;
		}
		return x*(v_delta-v)/delta + v;
	}

}
