package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SelectedCsvOutput implements CountingObserver{
	
	private final StringBuffer sb;

	private BufferedWriter bw;
	
	private String csv;
	
	private Set<Pixel> pixels;
	
	private int noDataValue;
	
	public SelectedCsvOutput(String csv, Set<Pixel> pixels, int noDataValue) {
		
		this.csv = csv;
		this.pixels = pixels;
		this.noDataValue = noDataValue;
		this.sb = new StringBuffer();
	}
	
	public void init(Counting c, Set<Metric> metrics) {
		
		try {
			bw = new BufferedWriter(new FileWriter(csv));
			if(pixels.iterator().next() instanceof PixelWithID){
				bw.write("id;");
			}
			bw.write("X;");
			bw.write("Y");
			for(Metric m : metrics) {
				bw.write(";"+m.getName());
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prerun(Counting c) {
		// do nothing
	}

	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		
		Pixel pixel = null;
		for(Pixel p : pixels){
			if(p.x() == i && p.y() == j){
				pixel = p;
				break;
			}
		}
		
		if(pixel != null){
			try {
				boolean export = true;
				sb.setLength(0);
				sb.append((((PixelWithID) pixel).getId())+";"+(((PixelWithID) pixel).getX())+";"+(((PixelWithID) pixel).getY()));
				
				for(double v : values.values()){
					if(v == noDataValue){
						export = false;
						break;
					}
					sb.append(';');
					sb.append(Util.format(v));
				}
				
				if(export){
					bw.write(sb.toString());
					bw.newLine();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close(Counting c, Set<Metric> metrics) {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

}
