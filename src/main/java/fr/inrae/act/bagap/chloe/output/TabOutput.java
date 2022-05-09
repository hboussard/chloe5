package fr.inrae.act.bagap.chloe.output;

import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;

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
	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		//System.out.println(i+" "+j);
		datas[(j/displacement)*width+(i/displacement)] = values.get(metric).floatValue();
	}

	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		// do nothing
	}

}
