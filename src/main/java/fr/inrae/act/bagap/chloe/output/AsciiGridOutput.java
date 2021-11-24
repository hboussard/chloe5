package fr.inrae.act.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class AsciiGridOutput implements CountingObserver{

	private final String file;
	
	private final Metric metric;
	
	private final int width, height, noDataValue;
	
	private final double xllCorner, yllCorner, cellSize;
	
	private BufferedWriter writer;
	
	private int currentWidth = 0;
	
	private StringBuffer sb;
	
	public AsciiGridOutput(String file, Metric metric, int width, int height, double xllCorner, double yllCorner, double cellSize, int noDataValue){
		this.file = file;
		this.metric = metric;
		this.width = width;
		this.height = height;
		this.xllCorner = xllCorner;
		this.yllCorner = yllCorner;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		try {
			this.writer = new BufferedWriter(new FileWriter(file));
			this.sb = new StringBuffer();
			writer.write("ncols "+width);
			writer.newLine();
			writer.write("nrows "+height);
			writer.newLine();
			writer.write("xllcorner "+xllCorner);
			writer.newLine();
			writer.write("yllcorner "+yllCorner);
			writer.newLine();
			writer.write("cellsize "+cellSize);
			writer.newLine();
			writer.write("NODATA_value "+noDataValue);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prerun(Counting c) {
	}

	@Override
	public void postrun(Counting c, int x, int y, Map<Metric, Double> values) {
		//System.out.println(metric+" "+values.get(metric));
		//sb.append(values.get(metric)+" ");
		sb.append(values.get(metric));
		sb.append(' ');
		currentWidth++;
		
		if(currentWidth == width){
			try {
				writer.write(sb.toString());
				writer.newLine();
				sb.setLength(0);
				currentWidth = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream(MatrixManager.epsg()), file.replace(".asc", "")+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

}
