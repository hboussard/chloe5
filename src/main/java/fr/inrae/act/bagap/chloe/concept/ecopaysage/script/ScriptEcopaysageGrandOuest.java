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

public class ScriptEcopaysageGrandOuest {

	public static void main(String[] args) {
		
		//retile();
		//clean();
		
		
		//int[] scales = new int[] {2500}; // m
		//ecolandscape(scales);
		
		int[] echelles = new int[] {500, 3000};
		//int[] echelles = new int[] {3000};
		//ecolandscapeRennesMetropole(echelles);
		//ecolandscapeBretagne(echelles);
		ecolandscapeGrandOuest(echelles);
		
		/*
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
		}
		*/
		
		//sumRuptures("D:/data/sig/bretagne/ecopaysage/sum_rupture.tif", "D:/data/sig/bretagne/ecopaysage/rupture/");
	}
	
	private static void clean() {
		Coverage covGO = CoverageManager.getCoverage("D:/data/sig/bretagne/bretagne_2021_ebr.tif");
		EnteteRaster entete = covGO.getEntete();
		float[] data = covGO.getData();
		covGO.dispose();
		
		entete.setNoDataValue(-1);
		
		float[] outData = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, data){

			@Override
			protected float doTreat(float[] v) {
				float value = v[0];
				if(value == 255) {
					return -1;
				}
				return value;
			}
		};
		cal.run();
		
		CoverageManager.write("D:/data/sig/bretagne/bretagne_2021_ebr_clean.tif", outData, entete);
	}

	private static void retile() {
		
		Coverage covB = CoverageManager.getCoverage("D:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		EnteteRaster enteteRef = covB.getEntete();
		covB.dispose();
		
		Coverage covGO = CoverageManager.getCoverage("D:/data/sig/grand_ouest/GO_2021_ebr.tif");
		EnteteRaster entete = covGO.getEntete();
		float[] data = covGO.getData(EnteteRaster.getROI(entete, enteteRef.getEnvelope()));
		covGO.dispose();
		
		CoverageManager.write("D:/data/sig/bretagne/bretagne_2021_ebr.tif", data, enteteRef);
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
	
	private static void ecolandscapeRennesMetropole(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setInputRaster("E:/rennes_metropole/data/rm_os_bre.tif");
		epManager.setScales(scales);
		//epManager.setFactor(3);
		epManager.setOutputFolder("E:/rennes_metropole/ecopaysage/essaie7/");
		epManager.setClasses(new int[]{8});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}

	private static void ecolandscapeBretagne(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("mapping");
		epManager.setInputRaster("D:/data/sig/bretagne/bretagne_2021_ebr_clean.tif");
		epManager.setScales(scales);
		epManager.setFactor(2);
		epManager.setOutputFolder("D:/data/sig/bretagne/ecopaysage_2021/");
		epManager.setClasses(new int[]{15});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
	
	private static void ecolandscapeGrandOuest(int[] scales) {
		
		EcoPaysageManager epManager = new EcoPaysageManager("rupture");
		epManager.setInputRaster("D:/data/sig/grand_ouest/GO_2021_ebr_clean.tif");
		epManager.setScales(scales);
		epManager.setFactor(100);
		epManager.setOutputFolder("D:/data/sig/grand_ouest/ecopaysage/");
		epManager.setClasses(new int[]{10, 15, 20, 25, 30});
		
		EcoPaysageProcedure epProcedure = epManager.build();
		
		epProcedure.run();
	}
}
