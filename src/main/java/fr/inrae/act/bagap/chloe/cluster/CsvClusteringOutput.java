package fr.inrae.act.bagap.chloe.cluster;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.analysis.Analysis;

public class CsvClusteringOutput extends Analysis {

	private String csvOutput;
	
	private float[] dataCluster;
	
	private float[] dataCover;
	
	private int[] initValues;
	
	private double cellSize;
	
	private int noDataValue;
	
	private Map<Integer, int[]> values;
	
	private int maxInitValues;
	
	public CsvClusteringOutput(String csvOutput, float[] dataCluster, float[] dataCover, int[] initValues, double cellSize, int noDataValue){
		this.csvOutput = csvOutput;
		this.dataCluster = dataCluster;
		this.dataCover = dataCover;
		this.initValues = initValues;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	@Override
	protected void doInit() {
		values = new TreeMap<Integer, int[]>();
		maxInitValues = 0;
		for(int iv : initValues){
			maxInitValues = Math.max(maxInitValues, iv);
		}
	}

	@Override
	protected void doRun() {
		int index = 0;
		for(float vC : dataCluster){
			if(vC != noDataValue && vC != 0){
				if(!values.containsKey((int) vC)){
					values.put((int) vC, new int[maxInitValues+1]);
				}
				values.get((int) vC)[(int) dataCover[index]]++;
			}
			index++;
		}
		
		setResult(true);
	}

	@Override
	protected void doClose() {
		try {
			CsvWriter cw = new CsvWriter(csvOutput);
			cw.setDelimiter(';');
			cw.write("id");
			cw.write("surface");
			for(int iv : initValues){
				cw.write("surf_"+iv);
			}
			cw.endRecord();
			
			double sum;
			double v;
			int index;
			double[] lv = new double[initValues.length];
			//StringBuffer bf = new StringBuffer();
			for(Entry<Integer, int[]> e : values.entrySet()){
				cw.write(e.getKey()+"");
				sum = 0;
				//bf.setLength(0);
				index = 0;
				for(int iv : initValues){
					v = e.getValue()[iv]*Math.pow(cellSize, 2)/1000.0;
					sum += v;
					lv[index++] = v;
					//bf.append(";"+v);
				}
				cw.write(sum+"");
				for(double lvv : lv){
					cw.write(lvv+"");
				}
				cw.endRecord();
			}
			
			
			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
