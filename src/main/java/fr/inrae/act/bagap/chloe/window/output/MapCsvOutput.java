package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class MapCsvOutput implements CountingObserver{

	private BufferedWriter bw;
	
	private String csv;
	
	private String name;
	
	public MapCsvOutput(String csv, String name){
		this.csv = csv;
		this.name = name;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		try {
			bw = new BufferedWriter(new FileWriter(csv));
			bw.write("name");
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
	public void postrun(Counting c, int i, int j, Map<Metric, Double> values) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		try {
			bw.write(name);
			for(double v : values.values()) {
				bw.write(";"+Util.format(v));
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
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
	
}
