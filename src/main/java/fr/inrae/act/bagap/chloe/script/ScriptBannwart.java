package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptBannwart {

	private static String path = "C:/Data/projet/bannwart/data/test3/";
	
	public static void main(String[] args) {
		
		
		double p = 1000 * 0.893 * 0.893 * 0.893;
		//double p = 1000 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893 * 0.893;
		System.out.println(p);
		
		//superposition();
		//generationPourcentageRecouvrement();
		//generationCandidats();
		//generationRugosite();
		//tileZone();
		
		/*
		//Coverage covOS = CoverageManager.getCoverage(path+"raster_Casys_1m.tif");
		Coverage covOS = CoverageManager.getCoverage(path+"OS.tif");
		EnteteRaster entete = covOS.getEntete();
		float[] dataOS = covOS.getData();
		covOS.dispose();
		//float[] dataEffectifs = getEffectifsInitial(dataOS, 100, entete);
		//float[] dataEffectifs = getEffectifsInitial(dataOS, entete);
		float[] dataEffectifs = getEffectifsInitialTest(1000, entete);
		
		//CoverageManager.write(path+"effectif_initial.tif", dataEffectifs, entete);
		
		
		//String period = "sortie_hiver";
		String period = "fin_saison_culturale";
		//float[] dataRecouvrement = generatePourcentageRecouvrementSortieHiver(dataOS, entete);
		float[] dataRecouvrement = generatePourcentageRecouvrementFinSaisonCulturale(dataOS, entete);
		
		//float[] dataHauteurVegetation = generatePourcentageHauteurVegetationSortieHiver(dataOS, entete);
		float[] dataHauteurVegetation = generatePourcentageHauteurVegetationFinSaisonCulturale(dataOS, entete);
		
		//CoverageManager.write(path+"recouvrement_fin_saison_culturale.tif", dataRecouvrement, entete);
		
		//dispersion(dataOS, dataRecouvrement, dataEffectifs, entete, period, 1);
		
		
		for(int i=1; i<=14; i++) {
			dataEffectifs = dispersion(dataOS, dataRecouvrement, dataHauteurVegetation, dataEffectifs, entete, period, i);
			analysePoints(dataEffectifs, entete, period, i);
		}
		*/
		
		//compilationAnalysesPoints();
		
 	}

	private static void compilationAnalysesPoints() {
		
		try {
			
			float[][] data = new float[3][14];
			
			for(int i=1; i<=14; i++) {
				
				CsvReader cr = new CsvReader(path+"analyse_points/analyse_"+i+".csv");
				cr.setDelimiter(';');
				cr.readHeaders();
				
				while(cr.readRecord()) {
					
					String id = cr.get("id");
					float value = Float.parseFloat(cr.get("Central")); 
							
					for(int j=i; j<=14; j++) {
							
						if(id.startsWith("A")) {
							
							data[0][j-1] += value;	
						}
						if(id.startsWith("B")) {
							
							data[1][j-1] += value;
						}
						if(id.startsWith("C")) {
		
							data[2][j-1] += value;
						}
					}		
				}
				cr.close();
			}
			
			CsvWriter cw = new CsvWriter(path+"analyse_points/analyse_cumul.csv");
			cw.setDelimiter(';');
			cw.write("zone");
			for(int i=1; i<=14; i++) {
				cw.write("cumul_"+i);	
			}
			cw.endRecord();
			
			cw.write("A");
			for(int i=1; i<=14; i++) {
				cw.write(data[0][i-1]+"");
			}
			cw.endRecord();
			
			cw.write("B");
			for(int i=1; i<=14; i++) {
				cw.write(data[1][i-1]+"");
			}
			cw.endRecord();
			
			cw.write("C");
			for(int i=1; i<=14; i++) {
				cw.write(data[2][i-1]+"");
			}
			cw.endRecord();
			
			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void analysePoints(float[] dataEffectifs, EnteteRaster entete, String period, int index) {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"test1_effectifs_"+period+"_"+index+".tif");
		//builder.setRasterTab(dataEffectifs);
		//builder.setEntete(entete);
		builder.setPointsFilter(path+"points_analyse.txt");
		builder.setWindowSize(3);
		builder.addMetric("Central");
		builder.addCsvOutput(path+"analyse_points/analyse_"+index+".csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}

	public static float[] dispersion(float[] dataOS, float[] dataRecouvrement, float[] dataHauteurVegetation, float[] dataEffectifs, EnteteRaster entete, String period, int index) {
		
		float[] dataCandidats = generateCandidats(dataEffectifs, 100, entete);
		float[] dataRugosite = generateRugosite(dataOS, dataRecouvrement, entete, period);
		float[] dataQualiteHabitat = generateQualiteHabitat(dataHauteurVegetation, entete, period);
		float[] dataCapaciteAccueil = getCapaciteAccueil(dataEffectifs);
		
		if(index == 1) {
			//CoverageManager.write(path+"candidat_"+index+".tif", dataCandidats, entete);
			//CoverageManager.write(path+"rugosite_"+period+"__"+index+".tif", dataRugosite, entete);
			//CoverageManager.write(path+"qualite_"+period+"_"+index+".tif", dataQualiteHabitat, entete);
			//CoverageManager.write(path+"accueil_"+index+".tif", dataCapaciteAccueil, entete);
		}
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTabs(dataCandidats, dataRugosite, dataQualiteHabitat, dataCapaciteAccueil);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.FUNCTIONAL);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("dispersion");
		builder.setWindowSize(45);
		builder.setDMax(45);
		builder.addGeoTiffOutput("effectif", path+"test1_effectifs_"+period+"_"+index+".tif");
		//builder.addGeoTiffOutput("dispersion", path+"dispersion_"+period+"_"+index+".tif");
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		Coverage covEffectif = CoverageManager.getCoverage(path+"test1_effectifs_"+period+"_"+index+".tif");
		float[] dataEffectifsN1 = covEffectif.getData();
		covEffectif.dispose();
		
		return dataEffectifsN1;
		
	}
	
	private static void tileZone() {
		
		Coverage covOS = CoverageManager.getCoverage(path+"test2/raster_Casys_1m.tif");
		EnteteRaster entete = covOS.getEntete();
		//float[] dataOS = covOS.getData();
				
		
		Rectangle ROI = new Rectangle(850, 100, 101, 51);
		EnteteRaster localEntete = EnteteRaster.getEntete(entete, ROI);
		float[] dataOS = covOS.getData(ROI);
		covOS.dispose();
		
		CoverageManager.write(path+"test3/OS.tif", dataOS, localEntete);
	}
	
	private static float[] generateCandidats(float[] dataEffectifs, float pourcentageCandidat, EnteteRaster entete) {
		
		float[] data = new float[dataEffectifs.length];  
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataEffectifs) {
			@Override
			protected float doTreat(float[] v) {
				float vE = v[0];
				if(vE != entete.noDataValue()) {
					return vE * pourcentageCandidat / (float) 100.0;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
		//CoverageManager.write(path+"raster_rugosite_"+period+"_"+index+".tif", dataRugosite, entete);
		
		return data;
	}

	private static float[] generateRugosite(float[] dataOS, float[] dataRecouvrement, EnteteRaster entete, String period) {

		float[] dataRugosite = new float[entete.width() * entete.height()];  
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(dataRugosite, dataOS, dataRecouvrement) {
			@Override
			protected float doTreat(float[] v) {
				float vOS = v[0];
				float vR = v[1];
				if(vOS != entete.noDataValue()) {
					if(vOS == 7 || vOS == 8) {
						return 10;
					}
					return vR * 2 + 1;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
		//CoverageManager.write(path+"raster_rugosite_"+period+".tif", dataRugosite, entete);
		
		return dataRugosite;
		
	}
	
	private static float[] generateQualiteHabitat(float[] dataHauteurVegetation, EnteteRaster entete, String period) {
		
		float[] dataQualiteHabitat = new float[entete.width() * entete.height()];  
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(dataQualiteHabitat, dataHauteurVegetation) {
			@Override
			protected float doTreat(float[] v) {
				float vR = v[0];
				if(vR != entete.noDataValue()) {
					return vR;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
		//CoverageManager.write(path+"raster_qualite_habitat_"+period+".tif", dataQualiteHabitat, entete);
		
		return dataQualiteHabitat;
	}
	
	private static float[] getEffectifsInitial(float[] dataOS, EnteteRaster entete) {

		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 100f); 
		sarMap.put(3f, 0f);
		sarMap.put(4f, 0f); 
		sarMap.put(5f, 100f); 
		sarMap.put(6f, 100f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 0f);
		Pixel2PixelTabCalculation cal;
		
		float[] data = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(data, dataOS, sarMap);
		cal.run();
		
		return data;
	}
	
	private static float[] getEffectifsInitialTest(int effectifIntitial, EnteteRaster entete) {

		float[] data = new float[entete.width()*entete.height()];
		data[(entete.width()*entete.height())/2] = effectifIntitial;
		
		return data;
	}
	
	private static float[] getEffectifsInitial(float[] dataOS, int effectifInitial, EnteteRaster entete) {
		
		float[] data = new float[entete.width() * entete.height()];  
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataOS) {
			@Override
			protected float doTreat(float[] v) {
				float vOS = v[0];
				if(vOS != entete.noDataValue()) {
					return effectifInitial;
				}
				return entete.noDataValue();
			}
		};
		cal.run();
		
		return data;
	}
	
	private static float[] getRugosite() {
		
		Coverage covR = CoverageManager.getCoverage(path+"raster_rugosite_1m.tif");
		//Coverage covR = CoverageManager.getCoverage(path+"raster_rugosite_sortie_hiver_1m.tif");
		float[] dataR = covR.getData();
		covR.dispose();
		
		return dataR;
	}

	private static float[] getCapaciteAccueil(float[] dataEffectifs) {
		
		float min = 0.1f;
		float mean = 10;
		float max = 100;
		
		float b = (max - mean*min) / (max - mean);
		float a = (1 - b) / mean;
		
		float[] data = new float[dataEffectifs.length];  
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataEffectifs) {
			@Override
			protected float doTreat(float[] v) {
				float vE = v[0];
				if(vE != -1) {
					return 1;
					/*
					if(vE <= mean) {
						return 1;
					}
					if(vE > max) {
						return min;
					}
					*/
					/*
					return Math.max(min, (-vE/max) + 1);
					return (-vE/max) + 1;
					*/
					//return a * vE + b;
				}
				return -1;
			}
		};
		cal.run();
		
		return data;
	}

	private static void generationRugosite() {
		
		Coverage covOS = CoverageManager.getCoverage(path+"raster_Casys_1m.tif");
		EnteteRaster entete = covOS.getEntete();
		float[] dataOS = covOS.getData();
		covOS.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 2.9f); 
		sarMap.put(2f, 2.9f); 
		sarMap.put(3f, 2f);
		sarMap.put(4f, 1.6f); 
		sarMap.put(5f, 2.9f); 
		sarMap.put(6f, 2.9f);
		sarMap.put(7f, 10f);
		sarMap.put(8f, 10f);
		sarMap.put(10f, 1.5f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataRugosite = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataRugosite, dataOS, sarMap);
		cal.run();
		
		CoverageManager.write(path+"raster_rugosite_sortie_hiver_1m.tif", dataRugosite, entete);
	}
	
	private static void generationCandidats() {
		
		Coverage covOS = CoverageManager.getCoverage(path+"raster_Casys_1m.tif");
		EnteteRaster entete = covOS.getEntete();
		float[] dataOS = covOS.getData();
		covOS.dispose();
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 100f); 
		sarMap.put(2f, 100f); 
		sarMap.put(3f, 0f);
		sarMap.put(4f, 0f); 
		sarMap.put(5f, 0f); 
		sarMap.put(6f, 100f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 100f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataCandidat = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataCandidat, dataOS, sarMap);
		cal.run();
		
		CoverageManager.write(path+"raster_candidat_1m.tif", dataCandidat, entete);
	}
	
	private static float[] generatePourcentageHauteurVegetationSortieHiver(float[] dataOS, EnteteRaster entete) {
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0.5f); 
		sarMap.put(2f, 0.5f); 
		sarMap.put(3f, 0.5f);
		sarMap.put(4f, 0.1f); 
		sarMap.put(5f, 0.95f); 
		sarMap.put(6f, 0.65f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 0.35f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataRecouvrement = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataRecouvrement, dataOS, sarMap);
		cal.run();
		
		//CoverageManager.write(path+"raster_hauteur_vegetation_sortie_hiver.tif", dataRecouvrement, entete);
		
		return dataRecouvrement;
	}
	
	private static float[] generatePourcentageHauteurVegetationFinSaisonCulturale(float[] dataOS, EnteteRaster entete) {
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0.4f); 
		sarMap.put(2f, 0.4f); 
		//sarMap.put(1f, 0.7f); 
		//sarMap.put(2f, 0.7f); 
		sarMap.put(3f, 0.5f);
		sarMap.put(4f, 0.1f); 
		sarMap.put(5f, 0.95f); 
		sarMap.put(6f, 0.5f);
		//sarMap.put(6f, 0.65f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 0.9f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataRecouvrement = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataRecouvrement, dataOS, sarMap);
		cal.run();
		
		//CoverageManager.write(path+"raster_hauteur_vegetation_fin_saison_culturale.tif", dataRecouvrement, entete);
		
		return dataRecouvrement;
	}
	
	
	private static float[] generatePourcentageRecouvrementSortieHiver(float[] dataOS, EnteteRaster entete) {
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0.65f); 
		sarMap.put(2f, 0.65f); 
		sarMap.put(3f, 0.5f);
		sarMap.put(4f, 0.3f); 
		sarMap.put(5f, 0.95f); 
		sarMap.put(6f, 0.65f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 0.35f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataRecouvrement = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataRecouvrement, dataOS, sarMap);
		cal.run();
		
		//CoverageManager.write(path+"raster_recouvrement_sortie_hiver.tif", dataRecouvrement, entete);
		
		return dataRecouvrement;
	}
	
	private static float[] generatePourcentageRecouvrementFinSaisonCulturale(float[] dataOS, EnteteRaster entete) {
		
		Map<Float, Float> sarMap = new TreeMap<Float, Float>();
		sarMap.put(1f, 0.95f); 
		sarMap.put(2f, 0.95f); 
		sarMap.put(3f, 0.5f);
		sarMap.put(4f, 0.3f); 
		sarMap.put(5f, 0.95f); 
		sarMap.put(6f, 0.95f);
		sarMap.put(7f, 0f);
		sarMap.put(8f, 0f);
		sarMap.put(10f, 0.75f);
		Pixel2PixelTabCalculation cal;
		
		float[] dataRecouvrement = new float[entete.width()*entete.height()];
		cal = new SearchAndReplacePixel2PixelTabCalculation(dataRecouvrement, dataOS, sarMap);
		cal.run();
		
		//CoverageManager.write(path+"raster_recouvrement_fin_saison_culturale.tif", dataRecouvrement, entete);
		
		return dataRecouvrement;
	}
	
	private static void superposition() {
		
		Coverage covIAE = CoverageManager.getCoverage(path+"raster_iae_1m.tif");
		EnteteRaster entete = covIAE.getEntete();
		float[] dataIAE = covIAE.getData();
		covIAE.dispose();
		
		Coverage covParcelle = CoverageManager.getCoverage(path+"raster_parcelle_1m.tif");
		float[] dataParcelle = covParcelle.getData();
		covParcelle.dispose();
		
		float[] dataOS = new float[entete.width()*entete.height()];
		for(int i=0; i<dataOS.length; i++) {
			float vIAE = dataIAE[i];
			float vParcelle = dataParcelle[i];
			if(vIAE != entete.noDataValue()) {
				dataOS[i] = vIAE;
			} else if(vParcelle != entete.noDataValue()) {
				dataOS[i] = vParcelle;
			} else {
				dataOS[i] = entete.noDataValue();
			}
		}
		
		CoverageManager.write(path+"raster_Casys_1m.tif", dataOS, entete);
 		
	}

}
