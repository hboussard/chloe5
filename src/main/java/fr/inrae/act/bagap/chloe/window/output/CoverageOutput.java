package fr.inrae.act.bagap.chloe.window.output;

import java.util.Set;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;

public class CoverageOutput implements CountingObserver{
	
	private Coverage coverage;
	
	private String metric;

	private float[] datas;
	
	private  EnteteRaster entete;
	
	private int ind;
	
	public CoverageOutput(String metric){
		this.metric = metric;
	}
	
	public void setEntete(EnteteRaster entete){
		this.entete = entete;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		ind = 0;
		this.datas = new float[entete.width()*entete.height()];
	}
	
	@Override
	public void prerun(Counting c) {
	}
	
	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		for(Metric m : metrics){
			if(m.getName().equalsIgnoreCase(metric)){
				datas[ind++] = Float.parseFloat(Util.format(m.value()));
			}
		}
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
		
		coverage = new TabCoverage(datas, entete);
		this.datas = null;
	}
	
	public Coverage getCoverage(){
		return coverage;
	}

}