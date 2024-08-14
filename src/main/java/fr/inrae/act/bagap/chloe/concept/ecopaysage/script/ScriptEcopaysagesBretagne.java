package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptEcopaysagesBretagne {

	public static void main(String[] args) {
		
		int[] scales = new int[] {500, 3000}; // m
		ecolandscape(scales);
		
		/*
		int[] echelles = new int[] {500,1000,1500,2000,2500,3000};
		int[] scales;
		for(int e1 : echelles) {
			scales = new int[] {e1};
			ecolandscape(scales);
			for(int e2 : echelles) {
				if(e1 < e2) {
					scales = new int[] {e1, e2};
					ecolandscape(scales);
				}
			}	
		}*/
		
		//sumRuptures("D:/data/sig/bretagne/ecopaysage/sum_rupture.tif", "D:/data/sig/bretagne/ecopaysage/rupture/");
	}
	
	private static void sumRuptures(String ruptureRaster, String folder) {
		
		EnteteRaster entete = null;
		Set<float[]> sets = new HashSet<float[]>();
		for(String file : new File(folder).list()) {
			System.out.println(file);
			Coverage cov = CoverageManager.getCoverage(folder+file);
			entete = cov.getEntete();
			sets.add(cov.getData());
			cov.dispose();
		}
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, sets.toArray(new float[sets.size()][])){

			@Override
			protected float doTreat(float[] v) {
				float value = 0;
				for(float vv : v) {
					value += Math.min(1, vv)/v.length;
				}
				return value;
			}
		};
		cal.run();
		
		CoverageManager.write(ruptureRaster, outData, entete);
		
	}
	
	private static void ecolandscape(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("F:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		epManager.setScales(scales);
		epManager.setOutputFolder("F:/data/sig/bretagne/ecopaysage/");
		epManager.setClasses(new int[]{10,11,12,13,14,15});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
