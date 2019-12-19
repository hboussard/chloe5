package fr.inra.sad.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import fr.inra.sad.bagap.chloe.counting.Counting;
import fr.inra.sad.bagap.chloe.counting.CountingObserver;
import fr.inra.sad.bagap.chloe.metric.Metric;

public class CsvOutput implements CountingObserver, MetricObserver{

	private Set<Metric> metrics;
	
	private BufferedWriter bw;
	
	private double x;
	
	private double y;
	
	private double cellSize;
	
	private double initX;
	
	private double maxX;
	
	public CsvOutput(String csv, double minX, double maxX, double minY, double maxY, short width, short height, double cellSize, short noDataValue, Set<Metric> metrics) {
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
		try {
			bw = new BufferedWriter(new FileWriter(csv));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.x = minX;
		this.y = maxY;
		this.cellSize = cellSize;
		this.maxX = maxX;
		
		this.metrics = metrics;
		for(Metric m : metrics) {
			m.addObserver(this);
		}
	}
	
	public void init(Counting c) {

		x = x + cellSize/2.0;
		initX = x;
		y = y - cellSize/2.0;
		
		try {
			bw.write("X;Y");
			for(Metric m : metrics) {
				bw.write(";"+m.getName());
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prerun(Counting c) {
		try {
			bw.write(x+";"+y);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notify(Metric m, String metric, float value) {
		//System.out.println(m+" "+metric+" "+value);
		try {
			bw.write(";"+value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void postrun(Counting c) {
		try {
			bw.newLine();
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
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
