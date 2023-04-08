package fr.inrae.act.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.Set;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.util.Util;

public class CsvOutput implements CountingObserver{

	private BufferedWriter bw;
	
	private String csv;
	
	private double x, y, initX;
	
	private final double minX, maxX, minY, maxY, cellSize;
	
	private final int width, height, noDataValue;
	
	public CsvOutput(String csv, double minX, double maxX, double minY, double maxY, int width, int height, double cellSize, int noDataValue) {
		
		this.csv = csv;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {

		this.x = minX + cellSize/2.0;
		this.initX = x;
		this.y = maxY - cellSize/2.0;
		
		try {
			bw = new BufferedWriter(new FileWriter(csv));
			bw.write("X;Y");
			for(Metric m : metrics) {
				bw.write(";"+m.getName());
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeHeader();
	}
	
	private void writeHeader(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(csv.replace(".csv", "_header.txt")));
			writer.write("ncols "+width);
			writer.newLine();
			writer.write("nrows "+height);
			writer.newLine();
			writer.write("xllcorner "+minX);
			writer.newLine();
			writer.write("yllcorner "+minY);
			writer.newLine();
			writer.write("cellsize "+cellSize);
			writer.newLine();
			writer.write("NODATA_value "+noDataValue);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void prerun(Counting c) {
		/*
		try {
			bw.write(x+";"+y);
			//bw.write(new char[]{'x',';','y'});
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		
		try {
			boolean export = true;
			StringBuffer sb = new StringBuffer(x+";"+y);
			for(double v : values.values()){
				if(v == Raster.getNoDataValue()){
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
			
			
			/*
			for(double v : values.values()){
				if(v == Raster.getNoDataValue()){
					System.out.println(x+" "+y);
				}
				bw.write(";"+v);
			}
			bw.newLine();
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(x+cellSize > maxX) {
			x = initX;
			y -= cellSize;
		}else {
			x += cellSize;
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
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

}
