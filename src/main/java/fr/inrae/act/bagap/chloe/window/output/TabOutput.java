package fr.inrae.act.bagap.chloe.window.output;

import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class TabOutput implements CountingObserver{
	
	private final float[] datas;
	
	private final Metric metric;
	
	private final int width;
	
	private final int displacement;
	
	public TabOutput(float[] datas, Metric metric, int width, int displacement){
		this.datas = datas;
		this.metric = metric;
		this.width = width;
		this.displacement = displacement;
	}
	
	/*public float[] getDatas(){
		return datas;
	}*/
	
	public Metric getMetric(){
		return metric;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		datas[(j/displacement)*width+(i/displacement)] = Float.parseFloat(Util.format(metric.value()));
	}

	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, Pixel p, Set<Metric> metrics) {
		// do nothing;
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing
	}

}
