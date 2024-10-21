package fr.inrae.act.bagap.chloe.window.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import fr.inrae.act.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.Pixel;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class InterpolateSplineLinearAsciiGridOutput implements CountingObserver{

	private String file;
	
	private Metric metric;
	
	private final int width, height, noDataValue, delta, maxWidth, maxHeight;
	
	private final double xllCorner, yllCorner, cellSize;
	
	private BufferedWriter writer;
	
	private int yGlobal;
	
	private double[] values_d;
	
	private Map<Integer, double[]> values;
	
	public InterpolateSplineLinearAsciiGridOutput(String file, Metric metric, int width, int height, double xllCorner, double yllCorner, double cellSize, int noDataValue, int displacement){
		this.file = file;
		this.metric = metric;
		this.width = width;
		this.height = height;
		this.xllCorner = xllCorner;
		this.yllCorner = yllCorner;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.delta = displacement;
		this.maxWidth = width - ((width-1)%delta) - 1;
		this.maxHeight = height - ((height-1)%delta) - 1;
		this.yGlobal = -1;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		try {
			this.writer = new BufferedWriter(new FileWriter(file));
			writer.write("ncols "+width);
			writer.newLine();
			writer.write("nrows "+height);
			writer.newLine();
			writer.write("xllcorner "+xllCorner);
			writer.newLine();
			writer.write("yllcorner "+yllCorner);
			writer.newLine();
			writer.write("cellsize "+cellSize);
			writer.newLine();
			writer.write("NODATA_value "+noDataValue);
			writer.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		values = new TreeMap<Integer, double[]>();
		values_d = new double[width];
	}

	@Override
	public void prerun(Counting c) {
	}

	@Override
	public void postrun(Counting c, int x, int y, Set<Metric> metrics) {
		double v = metric.value();
		
		//System.out.println(x+" "+y+" "+v);
		if(y == 0){ // on est sur la premiere ligne
			if(x == 0){ // on est sur la premiÃ¨re valeur de la ligne
				values.put(0, new double[width]);
				// j'affecte la nouvelle valeur
				values.get(0)[0] = v;
				// les autres valeurs sont vides
				for(int i=1; i<width; i++){
					values.get(0)[i] = (double) noDataValue;
				}
			}else{ // on n'est pas sur la premiere valeur de la ligne
				// j'affecte la nouvelle valeur
				values.get(0)[x] = v;
				// j'affecte les valeurs horizontales
				for(int xv=1; xv<delta; xv++){
					values.get(0)[x-delta+xv] = droite(values.get(0)[x-delta], v, xv);
				}
			}
		}else{ // on n'est pas sur la premiere ligne
			if(x == 0){ // on est sur la premiere valeur de la ligne
				
				if(y > delta){
					// ecriture des valeurs precedentes
					for(int yv=1; yv<=delta; yv++){
						//System.out.println(index++);
						for(double d : values.get(y-2*delta+yv)){
							write(d, y-2*delta+yv);
						}
					}
				}else{
					// ecriture des valeurs precedentes
					//System.out.println(index++);
					for(double d : values.get(0)){
						write(d, 0);
					}
				}
				
				// affectation des anciennes valeurs
				for(int i=0; i<width; i++){
					values_d[i] = values.get(y-delta)[i];
				}
				// remise a zero
				values.clear();
				for(int yv=1; yv<=delta; yv++){
					values.put(y-delta+yv, new double[width]);
					for(int i=0; i<width; i++){
						values.get(y-delta+yv)[i] = (double) noDataValue;
					}
				}
				// j'affecte la nouvelle valeur
				values.get(y)[0] = v;
				// j'affecte les valeurs verticales 
				for(int yv=1; yv<delta; yv++){
					values.get(y-delta+yv)[0] = droite(values_d[0], v, yv);
				}
			}else{ // on n'est pas sur la premiere valeur de la ligne
				// j'affecte la nouvelle valeur
				values.get(y)[x] = v;
				// j'affecte les valeurs horizontales
				for(int xv=1; xv<delta; xv++){
					values.get(y)[x-delta+xv] = droite(values.get(y)[x-delta], v, xv);
				}
				double vh;
				// j'affecte les valeurs verticales 
				for(int yv=1; yv<delta; yv++){
					vh = droite(values_d[x], v, yv);
					values.get(y-delta+yv)[x] = vh;
					// j'affecte les valeurs intermediaires
					for(int xv=1; xv<delta; xv++){
						values.get(y-delta+yv)[x-delta+xv] = droite(values.get(y-delta+yv)[x-delta], vh, xv);
					}
				}
				if(y == maxHeight && x == maxWidth){
					// ecriture des valeurs precedentes
					for(int yv=1; yv<=delta; yv++){
						//System.out.println(index++);
						for(double d : values.get(y-delta+yv)){
							write(d, y-delta+yv);
						}
					}
					// ecriture des valeurs suivantes
					for(int j=maxHeight+1; j<height; j++){
						for(int i=0; i<width; i++){
							write(noDataValue, j);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void close(Counting c, Set<Metric> metrics){
		try {
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				Tool.copy(DynamicLayerFactory.class.getResourceAsStream(CoverageManager.epsg()), file.replace(".asc", "")+".prj");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private double droite(double v_delta, double v, double yv){
		if(v == noDataValue || v_delta == noDataValue){
			return noDataValue;
		}
		return yv*(v-v_delta)/delta + v_delta;
	}
	
	private void write(double v, int y){
		try {
			if(yGlobal == -1){
				writer.write(Util.format(v));
				yGlobal = 0;
			}else if(y != yGlobal){
				writer.newLine();
				writer.write(Util.format(v));
				yGlobal = y;
			}else{
				writer.write(" "+Util.format(v));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing
	}
	
	@Override
	public void postrun(Counting c, Pixel p, Set<Metric> metrics) {
		// do nothing;
	}

}
