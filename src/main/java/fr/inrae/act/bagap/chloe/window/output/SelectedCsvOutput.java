package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class SelectedCsvOutput implements CountingObserver{
	
	private final StringBuffer sb;

	private BufferedWriter bw;
	
	private String csv;
	
	private Set<Pixel> pixels;
	
	private EnteteRaster entete;
	
	public SelectedCsvOutput(String csv, Set<Pixel> pixels, EnteteRaster entete) {
		
		this.csv = csv;
		this.pixels = pixels;
		this.entete = entete;
		this.sb = new StringBuffer();
	}
	
	@Override
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
	
	@Override
	public void prerun(Counting c) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		
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
				
				if(pixel instanceof PixelWithID){
					sb.append((((PixelWithID) pixel).getId())+";"+(((PixelWithID) pixel).getX())+";"+(((PixelWithID) pixel).getY()));
				}else{
					sb.append(CoordinateManager.getProjectedX(entete, pixel.x())+";"+CoordinateManager.getProjectedY(entete, pixel.y()));
				}
				
				
				double v;
				for(Metric m : metrics){
					v = m.value();
					if(v == entete.noDataValue()){
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

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing
	}

}
