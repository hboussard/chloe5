package fr.inrae.act.bagap.chloe.script;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class TestPreference {

	private static Preferences prefs;
	
	public static void main(String[] args){
		
		TestPreference testP = new TestPreference();
		//testP.initPreference();
		//testP.readPreference();
		//testP.clear();
		
	}
	
	private void clear(){
		try {
			Preferences.userRoot().node(this.getClass().getName()).clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void initPreference(){
	
		// This will define a node in which the preferences can be stored
	    prefs = Preferences.userRoot().node(this.getClass().getName());
	    String langue = "langue";
	    String crs = "crs";
	    
	    //prefs.put(langue, "deutch");
	    
	    // First we will get the values
	    // Define a boolean value
	    System.out.println(prefs.get(langue, "français"));
	    // Define a string with default "Hello World
	    System.out.println(prefs.get(crs, "epsg:2154"));
	    
	}
	
	private void writePreference(){
		
		 // now set the values
	    //prefs.put(langue, "anglais");
	}
	
	private void brouillon(){
		
		// This will define a node in which the preferences can be stored
	    prefs = Preferences.userRoot().node(this.getClass().getName());
	    String ID1 = "Test1";
	    String ID2 = "Test2";
	    String ID3 = "Test3";

	    // First we will get the values
	    // Define a boolean value
	    System.out.println(prefs.getBoolean(ID1, true));
	    // Define a string with default "Hello World
	    System.out.println(prefs.get(ID2, "Hello World"));
	    // Define a integer with default 50
	    System.out.println(prefs.getInt(ID3, 50));

	    // now set the values
	    prefs.putBoolean(ID1, false);
	    prefs.put(ID2, "Hello Europa");
	    prefs.putInt(ID3, 45);

	    // Delete the preference settings for the first value
	    prefs.remove(ID1);
	}
	
	private void readPreference(){
		
		try {
			System.out.println("ici");
			for(String key : prefs.keys()) {
				
				System.out.println(key);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	
}
