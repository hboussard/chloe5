package fr.inra.sad.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.counting.CountingObserver;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class AsciiGridOutput implements CountingObserver, MetricObserver{

	private BufferedWriter writer;
	
	private short width;
	
	private short currentWidth = 0;
	
	private StringBuffer sb;
	
	public AsciiGridOutput(String file, short width, short height, double xllCorner, double yllCorner, double cellSize, short noDataValue){
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
		this.width = width;
	}
	
	public void notify(Metric m, String metric, float value) {
		
		sb.append(value+" ");
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
	
	
	public void init() {
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
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
