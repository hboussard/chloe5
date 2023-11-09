package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EntityCsvOutput implements CountingObserver{

	private BufferedWriter bw;
	
	private String csv;
	
	public EntityCsvOutput(String csv) {
		this.csv = csv;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		try {
			bw = new BufferedWriter(new FileWriter(csv));
			
			bw.write("id");
			for(Metric m : metrics) {
				bw.write((";"+m.getName()));
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
	public void postrun(Counting c, int x, int y, Set<Metric> metrics) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		try {
			bw.write((id+""));
			double v;
			for(Metric m : metrics){
				v = m.value();
				bw.write((";"+Util.format(v)));
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
