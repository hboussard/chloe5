package fr.inrae.act.bagap.chloe.window.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class EntityRasterOutput implements CountingObserver{
	
	private final String file;
	
	private final Metric metric;
	
	private final Coverage entityCoverage;
	
	private final int noDataValue;
	
	private Map<Integer, Float> datas;
	
	public EntityRasterOutput(String file, Metric metric, Coverage entityCoverage, int noDataValue){
		this.file = file;
		this.metric = metric;
		this.entityCoverage = entityCoverage;
		this.noDataValue = noDataValue;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		Util.createAccess(file);
		datas = new HashMap<Integer, Float>();
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int x, int y, Set<Metric> metrics) {
		// do nothing
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// stockage des valeurs
		datas.put(id, Float.parseFloat(Util.format(metric.value())));
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		
		float[] inData = entityCoverage.getData();
		EnteteRaster entete = entityCoverage.getEntete();
			
		float[] outData = new float[inData.length]; 
			
		int index = 0;
		for(float d : inData){
			
			if(d == noDataValue || d == 0){
				outData[index] = d;
			}else{
				outData[index] = datas.get((int) d);
			}
				
			index++;
		}
			
		//System.out.println(file+" "+outData+" "+entete);
		//System.out.println(outData.length+" "+(entete.width()*entete.height()));
		CoverageManager.write(file, outData, entete);
		
	}
	
}
