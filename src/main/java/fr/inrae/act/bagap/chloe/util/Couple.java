package fr.inrae.act.bagap.chloe.util;

public class Couple {
	
	public static float getCouple(short v1, short v2) {
		if(v1 == -1 || v2 == -1) {
			return -1;
		}
		if(v1 == 0 || v2 == 0) {
			return 0;
		}
		if(v1 < v2) {
			return (float) (v2 * 0.0001 + v1);
		}else {
			return (float) (v1 * 0.0001 + v2);
		}
	}
	
	public static float getCouple(int v1, int v2) {
		if(v1 == -1 || v2 == -1) {
			return -1;
		}
		if(v1 == 0 || v2 == 0) {
			return 0;
		}
		if(v1 < v2) {
			return (float) (v2 * 0.0001 + v1);
		}else {
			return (float) (v1 * 0.0001 + v2);
		}
	}
	
	public static short getOne(float c) {
		return new Double(Math.floor(c)).shortValue();
	}
	
	public static short getOther(float c) {
		return new Double(Math.round((c - Couple.getOne(c)) * 10000)).shortValue();
	}

	public static boolean isHomogeneous(float c) {
		double min = Math.floor(c);
		return c == getCouple((short) min, (short) min);
	}
	
}
