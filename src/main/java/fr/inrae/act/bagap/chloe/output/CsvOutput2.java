package fr.inrae.act.bagap.chloe.output;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class CsvOutput2 implements CountingObserver{

	//private FileOutputStream fos;
	
	private BufferedOutputStream bout;
	
	private String csv;
	
	private double x, y, initX;
	
	private final double minX, maxX, minY, maxY, cellSize;
	
	private final int width, height, noDataValue;
	
	public CsvOutput2(String csv, double minX, double maxX, double minY, double maxY, int width, int height, double cellSize, int noDataValue) {
		
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
	
	public void init(Counting c, Set<Metric> metrics) {

		this.x = minX + cellSize/2.0;
		this.initX = x;
		this.y = maxY - cellSize/2.0;
		
		try {
			//fos = new FileOutputStream(csv);
			bout = new BufferedOutputStream(new FileOutputStream(csv));
			
			
			bout.write("X;Y".getBytes());
			for(Metric m : metrics) {
				bout.write((";"+m.getName()).getBytes());
			}
			bout.write("\n".getBytes());
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
	
	public void prerun(Counting c) {
		try {
			bout.write((x+";"+y).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		try {
			for(double v : values.values()){
				bout.write((";"+v).getBytes());
			}
			bout.write("\n".getBytes());
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

	public void close(Counting c) {
		try {
			bout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
