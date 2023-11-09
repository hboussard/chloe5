package fr.inrae.act.bagap.chloe.script;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.inra.sad.bagap.apiland.analysis.matrix.ChamferDistanceTabAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptCreuse {

	private static final String path = "G:/creuse/data/"; // chemin du répertoire de travail. 
																			// pas obligatoire mais pratique
																			// Attention, il finit par un slash "/" !
	
	// paramètres utiles au calcul du Grain Bocager
	private static final String osBois = path+"za.asc"; 	// le chemin vers le fichier d'occupation du sol contenant les boisements
															// ça peut être un ascii (.asc) ou un tif (.tif)
															// ça peut être le même fichier que celui d'occupation du sol pour les ecopaysges mais pas forcément.
	
	private static final int[] codesBois = new int[]{4,5};	// le(s) code(s) de boisements dans la couche "osBois"
															// si il y en a plusieurs, les séparer par une virgule
	
	private static final String rasterGrain = path+"grain.asc"; // le chemin du raster de sortie de l'analyse
																// il est forcément en ascii (.asc), le tif n'est pas encore pris en charge
																// si le dossier dans lequel il sera rangé n'existe pas, il sera créé automatiquement
																// si un fichier existe déjà à ce nom, il sera écrasé
	
	// paramètres utiles au calcul des métriques pour les ecopaysages
	private static final String os = path+"Creuse_OCS_tampon_6km_raster_V3.tif";			// le chemin vers le fichier d'occupation du sol
															// ça peut être un ascii (.asc) ou un tif (.tif)
															// ça peut être le même fichier que celui des boisements pour le grain mais pas forcément.
	
	private static final String outputFolderEcopaysage = path+"analyse/";   // chemin du répertoire de sortie des analyses pour les ecopaysages
																			// s'il n'existe pas, il sera créé
																			// Attention, il finit par un slash "/" !
	
	private static final String prefix = "gers"; 	// prefix de nommage pour les fichiers d'analyse
													// peut être vide = ""
	
	public static void main(String[] args){
		//scriptGrain();
		//scriptMetricsForEcopaysages();
		test();
	}
	
	private static void test(){
		
		Coverage covOS = CoverageManager.getCoverage(os);
		EnteteRaster entete = covOS.getEntete();
		float[] datasOS = covOS.getData();
		covOS.dispose();
		
	}
	
	private static void scriptGrain(){
		
		new File(new File(rasterGrain).getParent()).mkdirs();
		
		Coverage covOS = CoverageManager.getCoverage(osBois);
		EnteteRaster entete = covOS.getEntete();
		Rectangle roi = new Rectangle(0, 0, entete.width(), entete.height());
		float[] datasOS = covOS.getData(roi);
		covOS.dispose();
		
		float[] datasDistanceBois =  calculDistanceForGrain(datasOS, codesBois, entete);
		calculAverageForGrain(datasDistanceBois, rasterGrain, entete);
		
	}
	
	private static float[] calculDistanceForGrain(float[] datasOS, int[] codeBois, EnteteRaster entete){

		float[] datas = new float[datasOS.length];
		
		ChamferDistanceTabAnalysis analysis = new ChamferDistanceTabAnalysis(datas, datasOS, entete.width(), entete.height(), entete.cellsize(), codeBois);
		analysis.allRun();
		
		return datas;
	}
	
	private static void calculAverageForGrain(float[] datasDistanceBois, String outputRaster, EnteteRaster entete){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(datasDistanceBois);
		builder.setEntete(entete);
		builder.addMetric("MD");
		builder.setWindowSize(101);
		builder.addAsciiGridOutput("MD", outputRaster);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void scriptMetricsForEcopaysages(){
		
		new File(outputFolderEcopaysage).mkdirs();
		
		Coverage covOS = CoverageManager.getCoverage(os);
		EnteteRaster entete = covOS.getEntete();
		float[] datasOS = covOS.getData();
		covOS.dispose();
		
		Set<Float> inValues = new HashSet<Float>();
		for(float d : datasOS){
			inValues.add(d);
		}
		int index = 0;
		int[] values =  new int[inValues.size()];
		for(float d : inValues){
			values[index++] = (int) d;
		}
		
		scriptEcopaysage(os, "composition", "500m", values, outputFolderEcopaysage, prefix);
		scriptEcopaysage(os, "configuration", "500m", values, outputFolderEcopaysage, prefix);
		scriptEcopaysage(os, "composition", "3km", values, outputFolderEcopaysage, prefix);
		scriptEcopaysage(os, "configuration", "3km", values, outputFolderEcopaysage, prefix);
	}
	
	private static void scriptEcopaysage(String os, String compo_config, String scale, int[] values, String outputFolderEcopaysage, String prefix){
		long begin = System.currentTimeMillis();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(os);
		
		if(compo_config.equalsIgnoreCase("composition")){
			for(int i : values){
				builder.addMetric("pNV_"+i);
			}
		}
		if(compo_config.equalsIgnoreCase("configuration")){
			for(int i : values){
				for(int j : values){
					if(j>i){
						builder.addMetric("pNC_"+i+"-"+j);
					}
				}
			}
		}
		
		if(scale.equalsIgnoreCase("500m")){
			builder.setWindowSize(401); // 500m
		}
		if(scale.equalsIgnoreCase("3km")){
			builder.setWindowSize(2401); // 3km
		}
		
		builder.setDisplacement(40);
		builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		builder.addCsvOutput(outputFolderEcopaysage+prefix+"_"+compo_config+"_"+scale+".csv");
		
		LandscapeMetricAnalysis analysis = builder.build(); 
		
		analysis.allRun();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
	}
	
}
