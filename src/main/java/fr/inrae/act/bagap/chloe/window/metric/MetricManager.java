package fr.inrae.act.bagap.chloe.window.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.jumpmind.symmetric.csv.CsvReader;

import fr.inra.sad.bagap.apiland.analysis.process.metric.DistanceValueMetric;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.util.DistanceValueMatrix;
import fr.inrae.act.bagap.chloe.window.metric.basic.BasicMetric;
import fr.inrae.act.bagap.chloe.window.metric.continuity.ContinuityMetric;
import fr.inrae.act.bagap.chloe.window.metric.couple.CoupleMetric;
import fr.inrae.act.bagap.chloe.window.metric.erosion.DegatErosionMetric;
import fr.inrae.act.bagap.chloe.window.metric.erosion.SourceErosionMetric;
import fr.inrae.act.bagap.chloe.window.metric.patch.PatchMetric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.QuantitativeMetric;
import fr.inrae.act.bagap.chloe.window.metric.slope.SlopeMetric;
import fr.inrae.act.bagap.chloe.window.metric.value.ValueMetric;
import weka.gui.SysErrLog;

public class MetricManager {

	private static Map<String, String> metrics;
	
	private static Map<String, Integer> coherences;
	/*
	private static Set<String> valuesMetrics;
	
	private static Set<String> couplesMetrics;
	
	private static Set<String> patchMetrics;
	
	private static Set<String> connectivityMetrics;
	
	private static Set<String> diversityMetrics;
	
	private static Set<String> slopeMetrics;
	
	private static Set<String> grainMetrics;
	
	private static Set<String> quantitativeMetrics;
	*/
	private static Set<String> processValueMetrics;
	
	private static Set<String> processCoupleMetrics;
	
	static {
		metrics = new HashMap<String, String>();
		coherences = new HashMap<String, Integer>();
		/*
		valuesMetrics = new HashSet<String>();
		couplesMetrics = new HashSet<String>();
		patchMetrics = new HashSet<String>();
		connectivityMetrics = new HashSet<String>();
		diversityMetrics = new HashSet<String>();
		grainMetrics = new HashSet<String>();
		quantitativeMetrics = new TreeSet<String>();
		*/
		processValueMetrics = new HashSet<String>();
		processCoupleMetrics = new HashSet<String>();
		
		CsvReader cr = null;
		BufferedReader buf = null;
		try{
			buf = new BufferedReader(new InputStreamReader(MetricManager.class.getResourceAsStream("metrics.csv")));
			
			cr = new CsvReader(buf);
			cr.setDelimiter(';');
			cr.readHeaders();
			while(cr.readRecord()){
				if(!cr.get("name").startsWith("#")){
					//System.out.println(cr.get("name"));
					metrics.put(cr.get("name"), cr.get("class"));
					coherences.put(cr.get("name"), Integer.parseInt(cr.get("coherence")));
					
					/*
					switch(cr.get("type")){
					case "value" :
						valuesMetrics.add(cr.get("name"));
						break;
					case "couple" :
						couplesMetrics.add(cr.get("name"));
						break;
					case "patch" :
						patchMetrics.add(cr.get("name"));
						break;
					case "connectivity" :
						connectivityMetrics.add(cr.get("name"));
						break;
					case "diversity" :
						diversityMetrics.add(cr.get("name"));
						break;
					case "grain" :
						grainMetrics.add(cr.get("name"));
						break;
					case "quantitative" :
						quantitativeMetrics.add(cr.get("name"));
						break;
					}
					*/
					switch(cr.get("process")){
					case "value" : 
						processValueMetrics.add(cr.get("name"));
						break;
					case "couple" : 
						processCoupleMetrics.add(cr.get("name"));
						break;
						default : // do nothing
					}
					
				}
			}
			cr.close();
			buf.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}/*finally{
			cr.close();
		}*/
	}
	
	public static boolean hasMetric(String m){
		for(String metric : metrics.keySet()){
			if(m.startsWith(metric)){
				return true;
			}
		}
		return false;
	}
	
	public static Metric get(String m) throws NotExistingMetricException {
		try {
			
			if(isProcessCoupleMetric(m)){
				
				String metric = getProcessCoupleMetric(m);
				Class<?> c = Class.forName(metrics.get(metric));
				metric = m.replace(metric+"_", "");
				StringTokenizer st = new StringTokenizer(metric, "-");
				short c1 = Short.parseShort(st.nextToken());
				short c2 = Short.parseShort(st.nextToken());
				return (Metric) c.getConstructor(short.class, short.class).newInstance(c1, c2);
				
			}else if(isProcessValueMetric(m)){
				
				String metric = getProcessValueMetric(m);
				
				//System.out.println(metric+" "+m);
				
				Class<?> c = Class.forName(metrics.get(metric));
				metric = m.replace(metric+"_", "");
				
				if(metric.contains("-")){
					String[] s = metric.split("-");
					short[] v = new short[s.length];
					int i=0;
					for(String ss : s){
						v[i++] = Short.parseShort(ss);
					}
					return (Metric) c.getConstructor(short[].class).newInstance(v);
				}if(metric.contains("&")){
					String[] s = metric.split("&");
					short[] v = new short[s.length];
					int i=0;
					for(String ss : s){
						v[i++] = Short.parseShort(ss);
					}
					return (Metric) c.getConstructor(short[].class).newInstance(v);
				}else{
					short v = Short.parseShort(metric);
					return (Metric) c.getConstructor(short.class).newInstance(v);
				}
				
			}else{
				
				Class<?> c = Class.forName(metrics.get(m));
				return (Metric) c.getConstructor().newInstance();
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	
	private static boolean isProcessCoupleMetric(String m){
		for(String metric : processCoupleMetrics){
			if(m.startsWith(metric+"_")){
				return true;
			}
		}
		return false;
	}
	
	private static String getProcessCoupleMetric(String m){
		for(String metric : processCoupleMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static boolean isProcessValueMetric(String m){
		for(String metric : processValueMetrics){
			if(m.startsWith(metric+"_")){
				return true;
			}
		}
		return false;
	}
	
	private static String getProcessValueMetric(String m){
		for(String metric : processValueMetrics){
			if(m.startsWith(metric)){
				return metric;
			}
		}
		throw new IllegalArgumentException();
	}
	/*
	public static boolean isValueMetric(String m){
		return valuesMetrics.contains(m) || isProcessValueMetric(m);
	}
	
	public static boolean isCoupleMetric(String m){
		return couplesMetrics.contains(m) || isProcessCoupleMetric(m);
	}*/
	
	public static boolean hasValueMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(m instanceof ValueMetric){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasMetric(Set<Metric> metrics, String metric) {
		return metrics.contains(get(metric));
	}
	
	public static boolean hasOnlyValueMetric(Set<Metric> metrics){
		for(Metric m : metrics){
			if(!(isOnlyValueMetric(m)) && !(m instanceof BasicMetric)){
				return false;
			}
			//if(!(m instanceof ValueMetric)){
			//	return false;
			//}
		}
		return true;
	}
	
	private static boolean isOnlyValueMetric(Metric m) {
		return (m instanceof ValueMetric) && !(m instanceof CoupleMetric);
	}

	public static boolean hasCoupleMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(m instanceof CoupleMetric){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasOnlyCoupleMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(isOnlyCoupleMetric(m)) && !(m instanceof BasicMetric)){
				return false;
			}
			//if(!(m instanceof CoupleMetric)){
			//	return false;
			//}
		}
		return true;
	}
	
	private static boolean isOnlyCoupleMetric(Metric m) {
		return (m instanceof CoupleMetric) && !(m instanceof ValueMetric);
	}
	
	public static boolean hasQuantitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(m instanceof QuantitativeMetric){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasOnlyBasicMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyQuantitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof QuantitativeMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlySlopeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof SlopeMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyQualitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof QualitativeMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyPatchMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof PatchMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyContinuityMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof ContinuityMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlySourceErosionMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof SourceErosionMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyDegatErosionMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof DegatErosionMetric) && !(m instanceof BasicMetric)){
				return false;
			}
		}
		return true;
	}

	public static boolean hasCoherence(Set<Metric> metrics){
		int ic = 0;
		int c;
		for(Metric m : metrics){
			c = coherences.get(m.getName().split("_")[0]);
			if(ic != c && ic != 0 && c != 0){
				return false;
			}else{
				ic = c;
			}
		}
		return true;
	}
	
	public static Set<Integer> getCoherences(Set<Metric> metrics){
		Set<Integer> cIndex = new TreeSet<Integer>();
		for(Metric m : metrics){
			cIndex.add(coherences.get(m.getName().split("_")[0]));
		}
		return cIndex;
	}
	
	public static Set<Metric> getMetricsByCoherence(Set<Metric> metrics, int coherence){
		Set<Metric> cMetrics = new HashSet<Metric>();
		for(Metric m : metrics){
			if(coherences.get(m.getName().split("_")[0]) == coherence){
				cMetrics.add(m);
			}
		}
		return cMetrics;
	}
	
	public static void initThematicDistanceMetric(String distanceFile, ThematicDistanceMetric metric, int[] values) {
		
		if(new File(distanceFile).exists()) {
			try {
				CsvReader cr = new CsvReader(distanceFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				int max = -1;
				for(int h=1; h<cr.getHeaderCount(); h++) {
					max = Math.max(max, Integer.parseInt(cr.getHeader(h)));
				}
				
				float[][] distances = new float[max+1][max+1];
				
				while(cr.readRecord()){
					for(int v1 : values){
						int v2 = Integer.parseInt(cr.get("distance"));
						float d = Float.parseFloat(cr.get(v1+""));
						distances[v1][v2] = d;
						distances[v2][v1] = d;
					}
				}
				cr.close();
			
				metric.setThematicDistance(distances);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("WARNING: Thematic distance file "+distanceFile+" doesn't exists.");
		}
	}
	
}
