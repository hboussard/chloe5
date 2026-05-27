package fr.inrae.act.bagap.chloe.script;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;

import fr.inrae.act.bagap.apiland.raster.SpacePreference;

public class ScriptCRS {

	public static void main(String[] args) {
		
		//System.out.println("ici");
		
		for(int i =0; i<10000; i++) {
		
			System.out.println(SpacePreference.getLocal());
		}
		
		
		//SpacePreference.setLocal("default");
		/*
		try {
			
			System.out.println(CRS.decode("EPSG:2154").toString());
			System.out.println();
			SpacePreference.setLocal("france");
			System.out.println(SpacePreference.getCRS());
			System.out.println();
			System.out.println(CRS.toSRS(SpacePreference.getCRS()));
			System.out.println();
			System.out.println(SpacePreference.getEPSG());
			
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
