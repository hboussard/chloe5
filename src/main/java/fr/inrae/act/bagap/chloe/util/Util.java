package fr.inrae.act.bagap.chloe.util;

import java.awt.Rectangle;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.raster.Coverage;

public class Util {

	private static final DecimalFormat format;
	
	static{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00000", symbols);
	}
	
	public static String format(double v){
		int f = new Double(Math.floor(v)).intValue();
		if(v == f){
			return f+"";
		}
		return format.format(v);
	}
	
	public static void createAccess(String f){
		if(f.contains(".")){ // file
			new File(f).getParentFile().mkdirs();
		}else{ // folder
			new File(f).mkdirs();
		}
	}
	
	public static double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
	public static double distance(int x1, int y1, int x2, int y2, double cellSize){
		return Math.sqrt(Math.pow(cellSize*(x1-x2), 2) + Math.pow(cellSize*(y1-y2), 2)); 
	}
	
	public static int[] readValuesTinyRoi(Coverage coverage, Rectangle roi) {
		float[] datas = coverage.getData(roi);
		Set<Float> inValues = new TreeSet<Float>();
		for (float d : datas) {
			if (d != 0 && d != Raster.getNoDataValue()) {
				inValues.add(d);
			}
		}
		int index = 0;
		int[] values = new int[inValues.size()];
		for (float d : inValues) {
			values[index++] = (int) d;
		}
		return values;
	}
	
	public static int[] readValuesHugeRoi(Coverage coverage, Rectangle roi) {
		
		float[] datas;
		Set<Float> inValues = new TreeSet<Float>();
		for(int j=0; j<roi.height; j+=LandscapeMetricAnalysis.tileYSize()){
			datas = coverage.getData(new Rectangle(roi.x, roi.y+j, roi.width, Math.min(LandscapeMetricAnalysis.tileYSize(), roi.height-j)));
			for (float d : datas) {
				if (d != 0 && d != Raster.getNoDataValue()) {
					inValues.add(d);
				}
			}
		}
		int index = 0;
		int[] values = new int[inValues.size()];
		for (float d : inValues) {
			values[index++] = (int) d;
		}
		return values;
	}
	
}
