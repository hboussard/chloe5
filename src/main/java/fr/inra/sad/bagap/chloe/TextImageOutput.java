package fr.inra.sad.bagap.chloe;

import java.io.FileWriter;
import java.io.IOException;

public class TextImageOutput implements MetricObserver{

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
	
	@Override
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
	
	@Override
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
