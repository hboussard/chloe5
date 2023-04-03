package fr.inrae.act.bagap.chloe.output;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.raster.Coverage;

public class AreaMultipleAsciiGridOutput implements CountingObserver{

	private final String folder;
	
	private final Coverage areaCoverage;
	
	private final int width, height, noDataValue;
	
	private final double xllCorner, yllCorner, cellSize;
	
	private Map<Integer, Map<Metric, Double>> datas;
	
	public AreaMultipleAsciiGridOutput(String folder, Coverage areaCoverage, int width, int height, double xllCorner, double yllCorner, double cellSize, int noDataValue){
		this.folder = folder;
		this.areaCoverage = areaCoverage;
		this.width = width;
		this.height = height;
		this.xllCorner = xllCorner;
		this.yllCorner = yllCorner;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		new File(folder).mkdirs();
		datas = new HashMap<Integer, Map<Metric, Double>>();
	}

	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int x, int y, Map<Metric, Double> values) {
		// do nothing
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// stockage des valeurs
		Map<Metric, Double> v = new HashMap<Metric, Double>();
		for(Metric m : values.keySet()){
			v.put(m, values.get(m));
		}
		datas.put(id, v);
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		try {
			
			Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
			Map<String, StringBuffer> stringBuffers = new HashMap<String, StringBuffer>();
			for(Metric m : metrics){
				writers.put(m.getName(), new BufferedWriter(new FileWriter(folder+m.getName()+".asc")));
				stringBuffers.put(m.getName(), new StringBuffer());
				writers.get(m.getName()).write("ncols "+width);
				writers.get(m.getName()).newLine();
				writers.get(m.getName()).write("nrows "+height);
				writers.get(m.getName()).newLine();
				writers.get(m.getName()).write("xllcorner "+xllCorner);
				writers.get(m.getName()).newLine();
				writers.get(m.getName()).write("yllcorner "+yllCorner);
				writers.get(m.getName()).newLine();
				writers.get(m.getName()).write("cellsize "+cellSize);
				writers.get(m.getName()).newLine();
				writers.get(m.getName()).write("NODATA_value "+noDataValue);
				writers.get(m.getName()).newLine();
			}
			
			Rectangle roi = new Rectangle(0, 0, width, height);
			
			float[] inDatas = areaCoverage.getDatas(roi);
			// areaCoverage.dispose();
			
			int index = 0;
			for(float d : inDatas){
				//System.out.println(index+" "+width+" "+height+" "+d+" "+noDataValue);
				index++;
				if(index%width == 0){
					//System.out.println(index);
					index = 0;
					for(Metric m : metrics){
						writers.get(m.getName()).newLine();
					}
				}
				if(d == noDataValue || d == 0){
					for(Metric m : metrics){
						writers.get(m.getName()).write(d+" ");
					}
				}else{
					for(Metric m : metrics){
						//System.out.println(d);
						if(!datas.containsKey((int) d)){
							writers.get(m.getName()).write("0 ");
						}else{
							writers.get(m.getName()).write(datas.get((int) d).get(m)+" ");
						}
					}
				}
			}
			
			for(Entry<String, BufferedWriter> ew : writers.entrySet()){
				ew.getValue().close();
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream(MatrixManager.epsg()), folder+ew.getKey()+".prj");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
