package fr.inrae.act.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class SelectedCsvOutput implements CountingObserver{
	
	private final DecimalFormat format;

	private BufferedWriter bw;
	
	private String csv;
	
	private Set<Pixel> pixels;
	
	private int noDataValue;
	
	public SelectedCsvOutput(String csv, Set<Pixel> pixels, int noDataValue) {
		
		this.csv = csv;
		this.pixels = pixels;
		this.noDataValue = noDataValue;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
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
		//System.out.println(i+" "+j);
		
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
				StringBuffer sb = new StringBuffer();
				if(pixel instanceof PixelWithID){
					sb.append((((PixelWithID) pixel).getId())+";"+(((PixelWithID) pixel).getX())+";"+(((PixelWithID) pixel).getY()));
				}else{
					//TODO
				}
				
				for(double v : values.values()){
					if(v == Raster.getNoDataValue()){
						export = false;
						break;
					}
					sb.append(';');
					sb.append(format(v));
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
	
	protected String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}

}
