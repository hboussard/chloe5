package fr.inrae.act.bagap.chloe.util;

import java.io.File;

public class Util {

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
