package fr.inrae.act.bagap.chloe.util;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.apiland.util.CoordinateManager;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.metric.ThematicDistanceMetric;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;

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
		
		if(f.endsWith("/") || f.endsWith("\\")){ // folder
			new File(f).mkdirs();
		}else{ // file
			new File(f).getParentFile().mkdirs();
		}

	}
	
	public static double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}
	
	public static double distance(int x1, int y1, int x2, int y2, double cellSize){
		return Math.sqrt(Math.pow(cellSize*(x1-x2), 2) + Math.pow(cellSize*(y1-y2), 2)); 
	}
	
	public static int[] readValuesTinyRoi(Coverage coverage, Rectangle roi, int noDataValue) {
		float[] datas = coverage.getData(roi);
		Set<Float> inValues = new TreeSet<Float>();
		for (float d : datas) {
			if (d != 0 && d != noDataValue) {
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
	
	public static int[] readValuesHugeRoi(Coverage coverage, Rectangle roi, int noDataValue) {
		
		float[] datas;
		Set<Float> inValues = new TreeSet<Float>();
		for(int j=0; j<roi.height; j+=LandscapeMetricAnalysis.tileYSize()){
			datas = coverage.getData(new Rectangle(roi.x, roi.y+j, roi.width, Math.min(LandscapeMetricAnalysis.tileYSize(), roi.height-j)));
			for (float d : datas) {
				if (d != 0 && d != noDataValue) {
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
	
	public static int[] readValues(Coverage coverage, int width, int height, int noDataValue) {
		
		float[] datas;
		Set<Float> inValues = new TreeSet<Float>();
		for(int j=0; j<height; j+=LandscapeMetricAnalysis.tileYSize()){
			datas = coverage.getData(new Rectangle(0, j, width, Math.min(LandscapeMetricAnalysis.tileYSize(), height-j)));
			for (float d : datas) {
				if (d != 0 && d != noDataValue) {
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
	
	public static Map<Float, Float> importData(String dataFile, String code, String value){
		
		try {
			CsvReader cr = new CsvReader(dataFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<Float, Float> sarMap = new HashMap<Float, Float>();
			while(cr.readRecord()) {
				sarMap.put(Float.parseFloat(cr.get(code)), Float.parseFloat(cr.get(value)));
			}
			
			cr.close();
			
			return sarMap;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
	
	public static Coverage reduce(Coverage coverage, int displacement) {
		
		EnteteRaster inEntete = coverage.getEntete();
		
		int inWidth = inEntete.width();
		int inHeight = inEntete.height();
		double inMinX = inEntete.minx();
		double inMinY = inEntete.miny();
		double inMaxX = inEntete.maxx();
		double inMaxY = inEntete.maxy();
		float inCellSize = inEntete.cellsize();
		
		// taille de sortie
		int outWidth = (int) (((inWidth - 1) / displacement) + 1);
		int outHeight = (int) (((inHeight - 1) / displacement) + 1);
		float outCellSize = inCellSize * displacement;
		double outMinX = inMinX + inCellSize / 2.0 - outCellSize / 2.0;
		double outMaxX = outMinX + outWidth * outCellSize;
		double outMaxY = inMaxY - inCellSize / 2.0 + outCellSize / 2.0;
		double outMinY = outMaxY - outHeight * outCellSize;
		
		EnteteRaster outEntete = new EnteteRaster(outWidth, outHeight, outMinX, outMaxX, outMinY, outMaxY, outCellSize, inEntete.noDataValue());
		
		float[] inData = coverage.getData();
		float[] outData = new float[outWidth*outHeight];
		for(int j=0, y=0; j<inHeight; j+=displacement, y++) {
			for(int i=0, x=0; i<inWidth; i+=displacement, x++) {
				outData[y*outWidth + x] = inData[j*inWidth + i];
			}
		}
		
		return new TabCoverage(outData, outEntete);
	}
	
	public static float[] extend(float[] inData, EnteteRaster inEntete, EnteteRaster outEntete, int displacement) {
		
		float[] outData = new float[outEntete.width()*outEntete.height()];
		Arrays.fill(outData, outEntete.noDataValue());
		
		for(int j=0, y=0; j<inEntete.height(); j++, y+=displacement) {
			for(int i=0, x=0; i<inEntete.width(); i++, x+=displacement) {
				outData[y*outEntete.width()+x] = inData[j*inEntete.width()+i];
			}
		}
		
		return outData;
	}
	
	public static Coverage extendAndFill(Coverage inCoverage, Coverage refCoverage, int displacement) {
		
		EnteteRaster outEntete = refCoverage.getEntete();
		float[] refData = refCoverage.getData();
		int outWidth = outEntete.width();
		int outHeight = outEntete.height();
		
		EnteteRaster inEntete = inCoverage.getEntete();
		float[] inData = inCoverage.getData(EnteteRaster.getROI(inEntete, outEntete.getEnvelope()));
		inEntete = EnteteRaster.getEntete(inEntete, outEntete.getEnvelope());
		int inWidth = inEntete.width();
		int inHeight = inEntete.height();
		
		float[] outData = new float[outWidth*outHeight];
		
		for(int j=0; j<outHeight; j++) {
			int y = CoordinateManager.getLocalY(inEntete, CoordinateManager.getProjectedY(outEntete, j));
			for(int i=0; i<outWidth; i++) {
				int x = CoordinateManager.getLocalX(inEntete, CoordinateManager.getProjectedX(outEntete, i));
				
				outData[j*outWidth + i] = inData[y*inWidth + x];
			}	
		}
		
		float value;
		for(int j=0; j<inHeight; j++) {
			for(int i=0; i<inWidth; i++) {
				value = inData[j*inWidth + i];
				for(int y=(j*displacement); y<((j*displacement)+displacement) && y<outHeight; y++) {
					for(int x=(i*displacement); x<((i*displacement)+displacement) && x<outWidth; x++) {
						if(refData[y*outWidth + x] != outEntete.noDataValue()) {
							outData[y*outWidth + x] = value;	
						}else {
							outData[y*outWidth + x] = outEntete.noDataValue();
						}
					}
				}
			}
		}
		
		return new TabCoverage(outData, outEntete);
	}
	
	public static Coverage extendAndFill(Coverage inCoverage, EnteteRaster outEntete, int displacement) {
		
		int outWidth = outEntete.width();
		int outHeight = outEntete.height();
		
		EnteteRaster inEntete = inCoverage.getEntete();
		float[] inData = inCoverage.getData(EnteteRaster.getROI(inEntete, outEntete.getEnvelope()));
		inEntete = EnteteRaster.getEntete(inEntete, outEntete.getEnvelope());
		int inWidth = inEntete.width();
		int inHeight = inEntete.height();
		
		float[] outData = new float[outWidth*outHeight];
		Arrays.fill(outData, outEntete.noDataValue());
		
		for(int j=0; j<outHeight; j++) {
			int y = CoordinateManager.getLocalY(inEntete, CoordinateManager.getProjectedY(outEntete, j));
			for(int i=0; i<outWidth; i++) {
				int x = CoordinateManager.getLocalX(inEntete, CoordinateManager.getProjectedX(outEntete, i));
				
				outData[j*outWidth + i] = inData[y*inWidth + x];
			}	
		}
		
		float value;
		for(int j=0; j<inHeight; j++) {
			for(int i=0; i<inWidth; i++) {
				value = inData[j*inWidth + i];
				for(int y=(j*displacement); y<((j*displacement)+displacement) && y<outHeight; y++) {
					for(int x=(i*displacement); x<((i*displacement)+displacement) && x<outWidth; x++) {
						outData[y*outWidth + x] = value;
					}
				}
			}
		}
		
		return new TabCoverage(outData, outEntete);
	}
	
	public static float[][] initThematicDistanceMap(String distanceFile) {
		
		if(new File(distanceFile).exists()) {
			try {
				CsvReader cr = new CsvReader(distanceFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				int max = -1;
				Set<Integer> values = new TreeSet<Integer>();
				int value;
				for(int h=1; h<cr.getHeaderCount(); h++) {
					value = Integer.parseInt(cr.getHeader(h));
					values.add(value);
					max = Math.max(max, value);
				}
				
				float[][] distances = new float[max+1][max+1];
				
				while(cr.readRecord()){
					int v1 = Integer.parseInt(cr.get("distance"));
					for(int v2 : values){
						if(v2 <= v1) {
							float d = Float.parseFloat(cr.get(v2+""));
							distances[v1][v2] = d;
							distances[v2][v1] = d;
						}
					}
				}
				cr.close();
			
				return distances;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("WARNING: Thematic distance file "+distanceFile+" doesn't exists.");
		}
		
		return null;
	}
	
	/*
	public static Map<String, Float> initThematicImportanceMap(String importanceFile) {
		
		if(new File(importanceFile).exists()) {
			try {
				CsvReader cr = new CsvReader(importanceFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				
				Map<String, Float> mapImportances = new HashMap<String, Float>();
				while(cr.readRecord()){
					float importance = Float.parseFloat(cr.get("importance"));
					mapImportances.put(cr.get("metric"), importance);
					
				}
				cr.close();
				
				return mapImportances;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("WARNING: Thematic importance file "+importanceFile+" doesn't exists.");
		}
		
		return null;
	}*/
	
	/*
	public static Map<String, Float> initThematicImportanceMap(String importanceFile) {
		
		if(new File(importanceFile).exists()) {
			try {
				CsvReader cr = new CsvReader(importanceFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				
				Map<Integer, Float> codeImportances = new TreeMap<Integer, Float>();
				while(cr.readRecord()){
					int code = Integer.parseInt(cr.get("code"));
					float importance = Float.parseFloat(cr.get("importance"));
					codeImportances.put(code, importance);
				}
				cr.close();
				
				Map<String, Float> mapImportances = new HashMap<String, Float>();
				for(int code1 : codeImportances.keySet()) {
					mapImportances.put("pNV_"+code1, codeImportances.get(code1));
					for(int code2 : codeImportances.keySet()) {
						if(code1 < code2) {
							//mapImportances.put("pNC_"+code1+"-"+code2, Math.max(codeImportances.get(code1), codeImportances.get(code2)));
							mapImportances.put("pNC_"+code1+"-"+code2, codeImportances.get(code1) * codeImportances.get(code2));
						}
					}
				}
				
				return mapImportances;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("WARNING: Thematic importance file "+importanceFile+" doesn't exists.");
		}
		
		return null;
	}*/
	
}
