package fr.inrae.act.bagap.chloe.metric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.chloe.metric.couple.CoupleMetric;
import fr.inrae.act.bagap.chloe.metric.patch.PatchMetric;
import fr.inrae.act.bagap.chloe.metric.quantitative.QuantitativeMetric;
import fr.inrae.act.bagap.chloe.metric.value.ValueMetric;


public class MetricManager {

	private static Map<String, String> metrics;
	
	private static Set<String> valuesMetrics;
	
	private static Set<String> couplesMetrics;
	
	private static Set<String> patchMetrics;
	
	private static Set<String> connectivityMetrics;
	
	private static Set<String> diversityMetrics;
	
	private static Set<String> grainMetrics;
	
	private static Set<String> quantitativeMetrics;
	
	private static Set<String> processValueMetrics;
	
	private static Set<String> processCoupleMetrics;
	
	static {
		metrics = new HashMap<String, String>();
		valuesMetrics = new HashSet<String>();
		couplesMetrics = new HashSet<String>();
		patchMetrics = new HashSet<String>();
		connectivityMetrics = new HashSet<String>();
		diversityMetrics = new HashSet<String>();
		grainMetrics = new HashSet<String>();
		quantitativeMetrics = new TreeSet<String>();
		
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
					
					metrics.put(cr.get("name"), cr.get("class"));
					
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
			buf.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			cr.close();
		}
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
				short c1 = new Short(st.nextToken());
				short c2 = new Short(st.nextToken());
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
						v[i++] = new Short(ss);
					}
					return (Metric) c.getConstructor(short[].class).newInstance(v);
				}else{
					short v = new Short(metric);
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
	
	public static boolean isValueMetric(String m){
		return valuesMetrics.contains(m) || isProcessValueMetric(m);
	}
	
	public static boolean isCoupleMetric(String m){
		return couplesMetrics.contains(m) || isProcessCoupleMetric(m);
	}
	
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
			if(!(m instanceof ValueMetric)){
				return false;
			}
		}
		return true;
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
			if(!(m instanceof CoupleMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasQuantitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(m instanceof QuantitativeMetric){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasOnlyQuantitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof QuantitativeMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyQualitativeMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof QualitativeMetric)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasOnlyPatchMetric(Set<Metric> metrics) {
		for(Metric m : metrics){
			if(!(m instanceof PatchMetric)){
				return false;
			}
		}
		return true;
	}

	
}
