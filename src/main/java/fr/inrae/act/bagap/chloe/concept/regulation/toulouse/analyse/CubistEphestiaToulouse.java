package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist.CubistModel;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.metric.basic.CentralValue;
import fr.inrae.act.bagap.chloe.window.metric.couple.RateCoupleMetric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.AverageMetric;
import fr.inrae.act.bagap.chloe.window.metric.quantitative.VCentralValue;
import fr.inrae.act.bagap.chloe.window.metric.value.CountClassMetric;
import fr.inrae.act.bagap.chloe.window.metric.value.RateCentralValue;
import fr.inrae.act.bagap.chloe.window.metric.value.RateValueMetric;
import fr.inrae.act.bagap.chloe.window.metric.value.ShannonDiversityIndex;

public class CubistEphestiaToulouse {

	public static float[] calculate(CubistModel model, 
			float[] dataCover, float[] dataSystem, 
			EnteteRaster enteteInput, EnteteRaster enteteOutput, 
			int delta, Set<String> cultures, Set<String> snh_surf, Set<String> snh_lin,
			Map<Integer, Map<Float, Float>> mapIFT,
			Map<String, Double> meteo) {
		
		int inWidth = enteteInput.width();
		int inHeight = enteteInput.height();
		
		int outWidth = enteteOutput.width();
		int outHeight = enteteOutput.height();
		
		float[] tab;
		Map<String, float[]> tabs = new HashMap<String, float[]>();
		
		LandscapeMetricAnalysisBuilder builder;
		LandscapeMetricAnalysis analysis;
			
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(dataCover);
		builder.setEntete(enteteInput);
		builder.setDisplacement(delta);
		builder.setWindowSize(401);
		
		for(String v : cultures){
				
			for(String v2 : snh_surf){
				tab = new float[outWidth*outHeight];
				Metric metric = new RateCoupleMetric(Short.parseShort(v), Short.parseShort(v2));
				builder.addMetric(metric);
				builder.addTabOutput(metric.getName(), tab);
				tabs.put(metric.getName(), tab);
			}
				
			for(String v2 : snh_lin){
				tab = new float[outWidth*outHeight];
				Metric metric = new RateCoupleMetric(Short.parseShort(v), Short.parseShort(v2));
				builder.addMetric(metric);
				builder.addTabOutput(metric.getName(), tab);
				tabs.put(metric.getName(), tab);
			}
		}
		for(String v : snh_surf){
			tab = new float[outWidth*outHeight];
			builder.addMetric(new RateValueMetric(Short.parseShort(v)));
			builder.addTabOutput("pNV_"+v, tab);
			tabs.put("pNV_"+v, tab);
		}
		for(String v : snh_lin){
			tab = new float[outWidth*outHeight];
			builder.addMetric(new RateValueMetric(Short.parseShort(v)));
			builder.addTabOutput("pNV_"+v, tab);
			tabs.put("pNV_"+v, tab);
		}
		// SHDI comb
		tab = new float[outWidth*outHeight];
		builder.addMetric(new ShannonDiversityIndex());
		builder.addTabOutput("SHDI", tab);
		tabs.put("shannon_comb_1000m", tab);
			
		// proportion culture centrale
		tab = new float[outWidth*outHeight];
		builder.addMetric(new RateCentralValue());
		builder.addTabOutput("pCentral", tab);
		tabs.put("os_cult_hote_1000m", tab);
			
		// culture centrale
		tab = new float[outWidth*outHeight];
		builder.addMetric(new CentralValue());
		builder.addTabOutput("Central", tab);
		tabs.put("Central", tab);
			
		analysis = builder.build();
		analysis.allRun();
		
		Pixel2PixelTabCalculation pptc;
		
		// calcul des proportions d'elements semi-naturels surfaciques
		int ind = 0;
		tab = new float[outWidth*outHeight];
		float[][] tab_multiple = new float[snh_surf.size()][];
		for(String v : snh_surf){
			tab_multiple[ind++] = tabs.get("pNV_"+v);
		}
		pptc = new Pixel2PixelTabCalculation(tab, tab_multiple){
			@Override
			protected float doTreat(float[] v) {
				float value = 0;
				for(int i=0; i<snh_surf.size(); i++){
					value += v[i];
				}
				return value;
			}
		};
		pptc.run();
		tabs.put("ossnh_surf_1000m", tab);
			
		// calcul des proportions d'elements semi-naturels lineaires
		ind = 0;
		tab = new float[outWidth*outHeight];
		tab_multiple = new float[snh_lin.size()][];
		for(String v : snh_lin){
			tab_multiple[ind++] = tabs.get("pNV_"+v);
		}
		pptc = new Pixel2PixelTabCalculation(tab, tab_multiple){
			@Override
			protected float doTreat(float[] v) {
				float value = 0;
				for(int i=0; i<snh_lin.size(); i++){
					value += v[i];
				}
				return value;
			}
		};
		pptc.run();
		tabs.put("ossnh_lin_1000m", tab);
			
		// calcul des proportions d'interface cultures elements semi_naturels
		ind = 0;
		tab = new float[outWidth*outHeight];
		tab_multiple = new float[(cultures.size()*snh_surf.size())+(cultures.size()*snh_lin.size())][];
		for(String v : cultures){
			for(String v2 : snh_surf){
				if(Short.parseShort(v) < Short.parseShort(v2)) {
					tab_multiple[ind++] = tabs.get("pNC_"+v+"-"+v2);
				}else{
					tab_multiple[ind++] = tabs.get("pNC_"+v2+"-"+v);
				}
					
			}
			for(String v2 : snh_lin){
				if(Short.parseShort(v) < Short.parseShort(v2)) {
					tab_multiple[ind++] = tabs.get("pNC_"+v+"-"+v2);
				}else{
					tab_multiple[ind++] = tabs.get("pNC_"+v2+"-"+v);
				}	
			}
		}
		pptc = new Pixel2PixelTabCalculation(tab, tab_multiple){
			@Override
			protected float doTreat(float[] v) {
				float value = 0;
				for(int i=0; i<(cultures.size()*snh_surf.size())+(cultures.size()*snh_lin.size()); i++){
					value += v[i];
				}
				
				// calcul en metre / hectare
				// nombre de couples dans une fen�tre 401*401 --> 250456
				// conversion de la proportion de couples en nombre equivalent dans une fenetre "pleine"
				//value *= 250456.0; 
				//return (value*5)/314.159f;
				
				// calcul en nb de pixels
				value *= 250456.0;
				return value;
			}
		};
		pptc.run();
		tabs.put("int_cult_snh_1000m", tab);
		
		// simplification de la couche d'occ_sol
		Map<Float, Float> mapReplace = new HashMap<Float, Float>();
		//mapReplace.put(0f, -1f);
		for(String c : snh_surf){
			mapReplace.put(Float.parseFloat(c), 0f);
		}
		for(String c : snh_lin){
			mapReplace.put(Float.parseFloat(c), 0f);
		}
		float[] tabCulture = new float[inWidth*inHeight];
		SearchAndReplacePixel2PixelTabCalculation srptc = new SearchAndReplacePixel2PixelTabCalculation(tabCulture, dataCover, mapReplace);
		srptc.run();
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(tabCulture);
		builder.setEntete(enteteInput);
		builder.setDisplacement(delta);
		builder.setWindowSize(401);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new ShannonDiversityIndex());
		builder.addTabOutput("SHDI", tab);
		tabs.put("shannon_cult_1000m", tab);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new CountClassMetric());
		builder.addTabOutput("Nclass", tab);
		tabs.put("nb_cult_1000m", tab);
		
		analysis = builder.build();
		analysis.allRun();
	
		// calcul des IFT Totaux
		float[] tabIFTTotaux = new float[inWidth*inHeight];
		pptc = new Pixel2PixelTabCalculation(tabIFTTotaux, dataSystem, dataCover){
			@Override
			protected float doTreat(float[] v) {
				
				if(cultures.contains(((int) v[1])+"") 
						|| snh_surf.contains(((int) v[1])+"")
						|| snh_lin.contains(((int) v[1])+"")){
					if(mapIFT.containsKey((int) v[0])){
						return mapIFT.get((int) v[0]).get(v[1]);	
					}
					return 0;
				}
				return -1;
			}
		};
		pptc.run();
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(tabIFTTotaux);
		builder.setEntete(enteteInput);
		builder.setDisplacement(delta);
		builder.setWindowSize(401);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new AverageMetric());
		builder.addTabOutput("average", tab);
		tabs.put("ift_paysager_2018_snh_inclus1000m", tab);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new VCentralValue());
		builder.addTabOutput("VCentral", tab);
		tabs.put("ift_xtot", tab);
		
		analysis = builder.build();
		analysis.allRun();
		
		// calcul des IFT cultures
		float[] tabIFTCultures = new float[inWidth*inHeight];
		pptc = new Pixel2PixelTabCalculation(tabIFTCultures, dataSystem, dataCover){
			@Override
			protected float doTreat(float[] v) {
				
				if(cultures.contains(((int) v[1])+"")){
					if(mapIFT.containsKey((int) v[0])){
						return mapIFT.get((int) v[0]).get(v[1]);	
					}
					return 0;
				}
				return -1;
			}
		};
		pptc.run();		
		
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(tabIFTCultures);
		builder.setEntete(enteteInput);
		builder.setDisplacement(delta);
		builder.setWindowSize(401);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new AverageMetric());
		builder.addTabOutput("average", tab);
		tabs.put("ift_paysager_2018_1000m", tab);
		
		analysis = builder.build();
		analysis.allRun();
		
		// calcul du pourcentage de Bio
		builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(dataSystem);
		builder.setEntete(enteteInput);
		builder.setValues("1, 2, 3");
		builder.setDisplacement(delta);
		builder.setWindowSize(401);
		
		tab = new float[outWidth*outHeight];
		builder.addMetric(new RateValueMetric(Short.parseShort("3")));
		builder.addTabOutput("pNV_3", tab);
		tabs.put("os_bio2018_1000m", tab);
		
		analysis = builder.build();
		analysis.allRun();
		
		// nettoyage
		Iterator<Entry<String, float[]>> ite = tabs.entrySet().iterator();
		Entry<String, float[]> entry;
		while(ite.hasNext()){
			entry = ite.next();
			if(entry.getKey().startsWith("pNC") || entry.getKey().startsWith("pNV")){
				ite.remove();
			}
		}
		
		// model cubist
		Map<String, Double> data;
		double prediction;
		float[] tabModel = new float[outWidth*outHeight];
		for(int j=0; j<outHeight; j++){
			for(int i=0; i<outWidth; i++){
				//if(tabEA[j*outWidth + i] > 0){
					data = new HashMap<String, Double>();
					data.put("Ps", convert(meteo.get("Mean_Ps")));
					data.put("Vm", convert(meteo.get("Mean_Vm")));
					data.put("Tx", convert(meteo.get("Mean_Tx")));
					data.put("An_Tn", convert(meteo.get("Mean_An_Tn")));
					data.put("An_Tm", convert(meteo.get("Mean_An_Tm")));
					data.put("An_Tx", convert(meteo.get("Mean_An_Tx")));
					data.put("An_Ps", convert(meteo.get("Mean_An_Ps")));
					data.put("An_Gel_succ", convert(meteo.get("Mean_An_Gel_succ")));
					data.put("OSSnh_surf_1000m", convert(tabs.get("ossnh_surf_1000m")[j*outWidth + i]));
					data.put("OSSnh_lin_1000m", convert(tabs.get("ossnh_lin_1000m")[j*outWidth + i]));
					data.put("OS_cult_hote_1000m", convert(tabs.get("os_cult_hote_1000m")[j*outWidth + i]));
					data.put("Shannon_Cult_1000m", convert(tabs.get("shannon_cult_1000m")[j*outWidth + i]));
					data.put("Shannon_Comb_1000m", convert(tabs.get("shannon_comb_1000m")[j*outWidth + i]));
					data.put("nb_Cult_1000m", convert(tabs.get("nb_cult_1000m")[j*outWidth + i]));
					data.put("mean_parea_Culture_1000", 0.0);
					data.put("int_cult_snh_1000m", convert(tabs.get("int_cult_snh_1000m")[j*outWidth + i]));
					data.put("OS_Bio2018_1000m", convert(tabs.get("os_bio2018_1000m")[j*outWidth + i]));
					data.put("IFT_Paysager_2018_1000m", convert(tabs.get("ift_paysager_2018_1000m")[j*outWidth + i]));
					data.put("IFT_Paysager_2018_snh_inclus1000m", convert(tabs.get("ift_paysager_2018_snh_inclus1000m")[j*outWidth + i]));
					data.put("IFT_Xtot", convert(tabs.get("ift_xtot")[j*outWidth + i]));
					
					
					prediction = model.predict(data);
					
					tabModel[j*outWidth + i] = (float) prediction;
					
				/*}else{
					tabModel[j*outWidth + i] = enteteInput.noDataValue();
				}*/
			}	
		}
		
		return tabModel;
	}
	
	private static double convert(double v){
		return ((int) v*1000000)/1000000.0;
	}
	
}
