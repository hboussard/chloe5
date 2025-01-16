package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptEcopaysagesBretagne {

	public static void main(String[] args) {
		
		int[] scales = new int[] {3000}; // m
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
		
		int[] codes = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 17, 18 ,19, 20, 22, 23};
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		//epManager.addInputRaster("F:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		epManager.addInputRaster("F:/data/sig/bretagne/bretagne_2021_ebr_clean.tif");
		epManager.setScales(scales);
		epManager.setCodes(codes);
		epManager.setOutputFolder("F:/data/sig/bretagne/ecopaysages2/");
		epManager.setClasses(new int[]{15});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
