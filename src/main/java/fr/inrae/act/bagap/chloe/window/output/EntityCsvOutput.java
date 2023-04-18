package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class EntityCsvOutput implements CountingObserver{

	private final DecimalFormat format;
	
	private BufferedWriter bw;
	
	private String csv;
	
	public EntityCsvOutput(String csv) {
		this.csv = csv;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
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
	public void postrun(Counting c, int x, int y, Map<Metric, Double> values) {
		// do nothing
	}

	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		try {
			bw.write((id+""));
			for(double v : values.values()){
				bw.write((";"+format.format(v)));
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
