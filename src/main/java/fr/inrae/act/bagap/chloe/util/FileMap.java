package fr.inrae.act.bagap.chloe.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;

public class FileMap {

	private Map<Float, Float> map;
	
	public FileMap(String file, String from, String to){
		map = new TreeMap<Float, Float>();
		read(file, from, to);
	}
	
	private void read(String file, String from, String to){
		try{
			CsvReader cr = new CsvReader(file,';');
			cr.readHeaders();
			while(cr.readRecord()){
				map.put(Float.parseFloat(cr.get(from)), Float.parseFloat(cr.get(to)));
			}
			cr.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public float get(float value){
		return map.get(value);
	}
	
	public Map<Float, Float> getMap(){
		return map;
	}
	
	public void change(float source, float target) {
		map.put(source, target);
	}
	
	public void display(){
		for(Entry<Float, Float> e : map.entrySet()){
			System.out.println(e.getKey()+" --> "+e.getValue());
		}
	}
	
}
