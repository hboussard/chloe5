package fr.inrae.act.bagap.chloe.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
	
}
