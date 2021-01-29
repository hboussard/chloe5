package fr.inrae.act.bagap.chloe.util;

public class Util {

	public static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
}
