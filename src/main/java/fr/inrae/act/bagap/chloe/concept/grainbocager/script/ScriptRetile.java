package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptRetile {
	
	public static void main(String[] args) {
		
		//retile();
	}

	private static void retile() {
		/*
		Envelope env = getEnvelope();
		
		prepaMNH("38", env);
		prepaMNH("42", env);
		prepaMNH("69", env);
		*/
		//compileMNH();
	}
	
	private static void compileMNH() {
	
		String path = "E:/FRC_AURA/data/grain2d/CVB/";
		
		Coverage cov38 = CoverageManager.getCoverage(path+"MNH_CVB_38.tif");
		float[] data38 = cov38.getData();
		EnteteRaster entete = cov38.getEntete();
		cov38.dispose();
		
		Coverage cov42 = CoverageManager.getCoverage(path+"MNH_CVB_42.tif");
		float[] data42 = cov42.getData();
		cov42.dispose();
		
		Coverage cov69 = CoverageManager.getCoverage(path+"MNH_CVB_69.tif");
		float[] data69 = cov69.getData();
		cov69.dispose();
		
		float[] data = new float[entete.width()* entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data38, data42, data69){

			@Override
			protected float doTreat(float[] v) {
				
				float v0 = v[0];
				float v1 = v[1];
				float v2 = v[2];
				
				if(v0 == -1 || v1 == -1 || v2 == -1) { 
					return -1;
				}
				if(v0 == 10 || v1 == 10 || v2 == 10) { 
					return 10;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write(path+"MNH_CVB.tif", data, entete);

				
	}
	
	private static Envelope getEnvelope() {
		
		String path = "E:/FRC_AURA/data/grain2d/CVB/";
		
		Envelope envIsere = ShapeFile2CoverageConverter.getEnvelope(path+"CVB_Isere.shp", 1000);
		Envelope envLoire = ShapeFile2CoverageConverter.getEnvelope(path+"CVB_Loire.shp", 1000);
		Envelope envRhone = ShapeFile2CoverageConverter.getEnvelope(path+"CVB_Rhone.shp", 1000);
		
		double minx = Math.min(Math.min(envIsere.getMinX(), envLoire.getMinX()), envRhone.getMinX());
		double maxx = Math.max(Math.max(envIsere.getMaxX(), envLoire.getMaxX()), envRhone.getMaxX());
		double miny = Math.min(Math.min(envIsere.getMinY(), envLoire.getMinY()), envRhone.getMinY());
		double maxy = Math.max(Math.max(envIsere.getMaxY(), envLoire.getMaxY()), envRhone.getMaxY());
		
		Envelope env = new Envelope(minx, maxx, miny, maxy);
		
		System.out.println(envIsere);
		System.out.println(envLoire);
		System.out.println(envRhone);
		System.out.println(env);
		
		return env;
	}
	
	private static void prepaMNH(String dpt, Envelope env) {
		
		String path = "E:/FRC_AURA/data/grain2d/";
		
		String localPath = path+dpt+"/";
		
		EnteteRaster entete = EnteteRaster.getEntete(env, 5, -1);
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("Bois", 10);
		codes.put("Forêt fermée de feuillus", 10);
		codes.put("Forêt fermée de conifères", 10);
		codes.put("Forêt fermée mixte", 10);
		codes.put("Forêt ouverte", 10);
		codes.put("Haie", 10);
		codes.put("Lande ligneuse", 0);
		codes.put("Peupleraie", 10);
		codes.put("Verger", 0);
		codes.put("Vigne", 0);
		
		Coverage covWood = ShapeFile2CoverageConverter.getSurfaceCoverage(localPath+"data/ZONE_DE_VEGETATION.shp", "NATURE", codes, entete, 0);
		float[] dataWood = covWood.getData();
		covWood.dispose();
		
		Coverage covHedge = ShapeFile2CoverageConverter.getLinearCoverage(localPath+"data/HAIE.shp", entete, 10, 0, 5);
		float[] dataHedge = covHedge.getData();
		covHedge.dispose();
		
		for(int i=0; i<entete.width()*entete.height(); i++) {
			dataWood[i] = Math.max(dataHedge[i], dataWood[i]);
		}
		
		String outputPath = "E:/FRC_AURA/data/grain2d/CVB/";
		
		CoverageManager.write(outputPath+"MNH_CVB_"+dpt+".tif", dataWood, entete);
	}

}
