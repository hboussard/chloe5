package fr.inra.sad.bagap.chloe.output;

import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.MetricObserver;

public class TextImageOutput implements CountingObserver, MetricObserver{

	private FileWriter writer;
	
	private short width;
	
	private short currentWidth = 0;
	
	private StringBuffer sb;
	
	public TextImageOutput(String file, short width){
		try {
			this.writer = new FileWriter(file);
			this.sb = new StringBuffer();
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
				sb.append('\n');
				writer.write(sb.toString());
				sb.setLength(0);
				currentWidth = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(Counting c){
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void init(Counting c) {
		// TODO Auto-generated method stub
		
	}

	public void prerun(Counting c) {
		// TODO Auto-generated method stub
		
	}

	public void postrun(Counting c) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
