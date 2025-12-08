package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.calculmetrics.EPPCalculMetrics;

public class EPPStandardization extends EcoPaysageProcedure {

	private int scale;
	
	public EPPStandardization(EcoPaysageManager manager) {
		super(manager);
	}
	
	public EPPStandardization(EcoPaysageManager manager, int specificScale) {
		this(manager);
		this.scale = specificScale;
	}
	
	@Override
	public void doInit() {
		
		if(scale == 0) {
			scale = manager().scale();
		}
		
		if(manager().force() || !new File(manager().metricsFile(scale)).exists()){
			
			new EPPCalculMetrics(manager(), scale).run();
		}
		
		if(!new File(manager().xyFile()).exists()){
			
			System.out.println("generation du fichier de masque XY");
			
			EcoPaysage.generateMask(manager().metricsFile(), manager().xyFile());
		}
	}

	@Override
	public void doRun() {

		System.out.println("split des donnees de composition et de configuration");
		
		//EcoPaysage.splitCompoConfig(manager().metricsFile(scale), manager().xyFile(), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		EcoPaysage.splitCompoConfig(manager().metricsFiles(scale), manager().xyFile(), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics());
		
		System.out.println("standardisation des donnees de composition");
		
		float inertia;
		
		inertia = EcoPaysage.standardizeCompo(manager().compoFile(scale), manager().compoMetrics(), manager().importances(), scale);
		
		manager().setInertia("composition_"+scale+"m", inertia);
		
		System.out.println("standardisation des donnees de configuration");
		
		//EcoPaysage.standardize(manager().configFile(scale), manager().configMetrics());
		
		float[][] distances = null;
		//float[][] distances = Util.initThematicDistanceMap("E:/rennes_metropole/ecopaysage/distance/distance_neutre.txt");
		
		inertia = EcoPaysage.standardizeConfig(manager().configFile(scale), manager().configMetrics(), manager().importances(), distances, scale);
		
		manager().setInertia("configuration_"+scale+"m", inertia);
		/*
		System.out.println("standardisation test");
		Map<String, String> complementaryMetrics;
		
		complementaryMetrics = new HashMap<String, String>();
		complementaryMetrics.put("average", "slope_average");
		//EcoPaysage.standardizeExternal("F:/coterra/data/Coterra_2019_DNSB_erb/std_slope_1000m.csv", "F:/coterra/data/Coterra_2019_DNSB_erb/slope_1000m.csv", complementaryMetrics, 1);
		*/
		/*
		//EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_slope_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope_average_1000m_100m.csv", complementaryMetrics, 1);
		//EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_slope_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope_average_1000m_50m.csv", complementaryMetrics, 1);
		//EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_slope_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/slope_average_1000m_25m.csv", complementaryMetrics, 1);
		*/
		/*
		complementaryMetrics = new HashMap<String, String>();
		complementaryMetrics.put("average", "elevation_average");
		EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_elevation_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation_average_1000m_100m.csv", complementaryMetrics, 1);
		EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_elevation_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation_average_1000m_50m.csv", complementaryMetrics, 1);
		EcoPaysage.standardizeExternal("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_elevation_average_1000m.csv", "D:/data/sig/data_ZA/PF_OS_L93/raster_5m/elevation_average_1000m_25m.csv", complementaryMetrics, 1);
		*/
		
		System.out.println("compilation des donnees standardisees");
		
		Set<String> setSdtFiles = new LinkedHashSet<String>();
		setSdtFiles.add(manager().compoFile(scale));
		setSdtFiles.add(manager().configFile(scale));
		//setSdtFiles.add("F:/coterra/data/Coterra_2019_DNSB_erb/std_slope_1000m.csv"); // pente
		//setSdtFiles.add("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_slope_average_1000m.csv"); // pente
		//setSdtFiles.add("D:/data/sig/data_ZA/PF_OS_L93/raster_5m/std_elevation_average_1000m.csv"); // altitude
		String[] sdtFiles = setSdtFiles.toArray(new String[setSdtFiles.size()]);
		
		
		//EcoPaysage.compileStdCompoConfig(manager().standardizedFile(scale), manager().compoFile(scale), manager().configFile(scale), manager().compoMetrics(), manager().configMetrics(), scale);
		EcoPaysage.compileStdFiles(manager().standardizedFile(scale), scale, sdtFiles);
		
		Tool.deleteFile(manager().compoFile(scale));
		Tool.deleteFile(manager().configFile(scale));
		
		EcoPaysage.exportInertia(manager().inertiaFile(), manager().inerties());
	}
	
}
