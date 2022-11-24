package fr.inrae.act.bagap.chloe.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.counting.Counting;
import fr.inrae.act.bagap.chloe.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.metric.Metric;

public class InterpolateSplineLinearCsvOutput implements CountingObserver{

	private BufferedWriter bw;
	
	private final String csv;
	
	private double x, y;
	
	private final double minX, maxX, minY, maxY, cellSize;
	
	private final int width, height, noDataValue, delta, maxWidth, maxHeight;
	
	private Map<String, double[]> values_d;
	
	private Map<String, Map<Integer, double[]>> values;
	
	public InterpolateSplineLinearCsvOutput(String csv, double minX, double maxX, double minY, double maxY, int width, int height, double cellSize, int noDataValue, int displacement) {
		this.csv = csv;
		this.x = minX;
		this.y = maxY;
		this.cellSize = cellSize;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.width = width;
		this.height = height;
		this.delta = displacement;
		this.noDataValue = noDataValue;
		this.maxWidth = width - ((width-1)%delta) - 1;
		this.maxHeight = height - ((height-1)%delta) - 1;
	}
	
	@Override
	public void init(Counting c, Set<Metric> metrics) {
		
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
		
		values = new TreeMap<String, Map<Integer, double[]>>();
		values_d = new TreeMap<String, double[]>();
		
		
		for(Metric wm : metrics){
			values.put(wm.getName(), new TreeMap<Integer, double[]>());
			values.get(wm.getName()).put(0, new double[width]);
			for(int i=0; i<width; i++){
				values.get(wm.getName()).get(0)[i] = (double) Raster.getNoDataValue();
			}
			values_d.put(wm.getName(), new double[width]);
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
		// do nothing
	}

	@Override
	public void postrun(Counting c, int i, int j, Map<Metric, Double> metrics) {
		for(Entry<Metric, Double> entry : metrics.entrySet()){
			double v = entry.getValue();
			String metric = entry.getKey().getName();
			
			if(!values.get(metric).containsKey(j)){ 
				// affectation des anciennes valeurs
				for(int ii=0; ii<width; ii++){
					values_d.get(metric)[ii] = values.get(metric).get(j-delta)[ii];
				}
				// initialisation des nouvelles lignes
				for(int yv=1; yv<=delta; yv++){
					values.get(metric).put(j-delta+yv, new double[width]);
					for(int ii=0; ii<width; ii++){
						values.get(metric).get(j-delta+yv)[ii] = (double) Raster.getNoDataValue();
					}
				}
			}
			
			// j'affecte la nouvelle valeur
			values.get(metric).get(j)[i] = v;
		}
		
		
		double v;
		int yy, im;
		if(j != 0 && i == 0){ // ecriture des valeurs precedentes
			double[] vv = new double[values.keySet().size()];
			if(j > delta){
				for(int yv=1; yv<=delta; yv++){
					yy = j-2*delta+yv;
					for(int ii=0; ii<width; ii++){
						im = 0;
						for(String m : values.keySet()){
							vv[im++] = values.get(m).get(yy)[ii]; 
						}
						write(vv, ii, yy);
					}
					for(String m : values.keySet()){
						values.get(m).remove(yy);
					}
				}		
			}else{
				for(int ii=0; ii<width; ii++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = values.get(m).get(0)[ii];
					}
					write(vv, ii, 0);
				}
			}		
		}
		
		for(String m : values.keySet()){ // pour chaque metrique
			//System.out.println("y "+y);
			if(j == 0){ // on est sur la premiere ligne
				if(i == 0){ // on est sur la premiÃ¨re valeur de la ligne
					// les autres valeurs sont vides
					for(int ii=1; ii<width; ii++){
						values.get(m).get(0)[ii] = (double) Raster.getNoDataValue();
					}
				}else{ // on n'est pas sur la premiere valeur de la ligne
					v = values.get(m).get(0)[i];
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(m).get(0)[i-delta+xv] = droite(values.get(m).get(0)[i-delta], v, xv);
					}
				}
			}else{ // on n'est pas sur la premiere ligne
				if(i == 0){ // on est sur la premiÃ¨re valeur de la ligne
					v = values.get(m).get(j)[0];
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						values.get(m).get(j-delta+yv)[0] = droite(values_d.get(m)[0], v, yv);
					}
				}else{ // on n'est pas sur la premiere valeur de la ligne
					v = values.get(m).get(j)[i];
					// j'affecte les valeurs horizontales
					for(int xv=1; xv<delta; xv++){
						values.get(m).get(j)[i-delta+xv] = droite(values.get(m).get(j)[i-delta], v, xv);
					}
					double vh;
					// j'affecte les valeurs verticales 
					for(int yv=1; yv<delta; yv++){
						vh = droite(values_d.get(m)[i], v, yv);
						values.get(m).get(j-delta+yv)[i] = vh;
						// j'affecte les valeurs intermediaires
						for(int xv=1; xv<delta; xv++){
							values.get(m).get(j-delta+yv)[i-delta+xv] = droite(values.get(m).get(j-delta+yv)[i-delta], vh, xv);
						}
					}
				}
			}
		}
		
		if(j == maxHeight && i == maxWidth){
			double[] vv = new double[values.keySet().size()];
			// ecriture des valeurs precedentes
			for(int yv=1; yv<=delta; yv++){
				yy = j-delta+yv;
				for(int ii=0; ii<width; ii++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = values.get(m).get(yy)[ii]; 
					}
					write(vv, ii, yy);
				}
			}
			// ecriture des valeurs suivantes
			for(int jj=maxHeight+1; jj<height; jj++){
				for(int ii=0; ii<width; ii++){
					im = 0;
					for(String m : values.keySet()){
						vv[im++] = Raster.getNoDataValue();
					}
					write(vv, ii, jj);
				}
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
	
	private double droite(double v_delta, double v, double yv){
		if(v == Raster.getNoDataValue() || v_delta == Raster.getNoDataValue()){
			return Raster.getNoDataValue();
		}
		return yv*(v-v_delta)/delta + v_delta;
	}
	
	private void write(double[] vv, int i, int j){
		try {
			bw.write(CoordinateManager.getProjectedX(minX, cellSize, i)+";");
			bw.write(CoordinateManager.getProjectedY(minY, cellSize, height, j)+"");
			
			for(double v : vv){
				bw.write(";"+v);
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void postrun(Counting c, int id, Map<Metric, Double> values) {
		// do nothing
	}

}
