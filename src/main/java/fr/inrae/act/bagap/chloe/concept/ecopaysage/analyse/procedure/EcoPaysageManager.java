package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.calculmetrics.EPPCalculMetricsFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.clustering.EPPClusteringFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.gradient.EPPGradientFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.mapping.EPPMappingFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.rupture.EPPRuptureFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization.EPPStandardizationFactory;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class EcoPaysageManager {

	private EcoPaysageProcedureFactory factory; // constructeur de procedure

	private boolean force;
	
	//private String metricsFile; // csv des metriques
	
	private Map<Integer, String> metricsFiles; // csv des metriques par echelle
	
	private String xyFile; // csv des XY
	
	private String carto; // nom de la carto
	
	private String inputRaster; // raster d'entree
	
	private String outputFolder; // dossier de sortie
	
	private Map<Integer, String> compoFiles, configFiles, normFiles;
	
	private String normFile, ruptureFile;
	
	//private int scale; // echelle d'analyse unique
	
	private int[] scales; // echelle d'analyse multiple
	
	private List<String> compoMetrics, configMetrics; // les metriques de compo et de config
	
	private boolean initMetrics;
	
	private int[] classes; // les nombres de classes à générer
	
	private int factor; // facteur de clusterisation
	
	private Map<Integer, String> ecoFiles, mapFiles, infoFiles, gradientFiles, thematicDistanceFiles;
	
	private Map<Integer, Map<Integer, String>> gradientMapFiles;
	
	private String headerFile; // le fichier d'entete raster
	
	private int noDataValue;
	
	private int[] unfilters;
	
	private EnteteRaster inEntete;
	
	private int displacement;
	
	private WindowDistanceType distanceType;
	
	public EcoPaysageManager(String treatment){
		setTreatment(treatment);
		init();
	}
	
	private void setTreatment(String treatment){
		
		switch(treatment){
		case "calcul_metrics" : 
			factory = new EPPCalculMetricsFactory();
			break;
		case "standardization" :
			factory = new EPPStandardizationFactory();
			break;
		case "clustering" :
			factory = new EPPClusteringFactory();
			break;
		case "gradient" :
			factory = new EPPGradientFactory();
			break;
		case "mapping" :
			factory = new EPPMappingFactory();
			break;
		case "rupture" :
			factory = new EPPRuptureFactory();
			break;
		default : throw new IllegalArgumentException("treatment '"+treatment+"' do not exists");
		}
	}
	
	private void init(){
		
		force = false;
		//metricsFile = null;
		metricsFiles = new TreeMap<Integer, String>();
		xyFile = null;
		//compoFile = null;
		//configFile = null;
		normFile = null;
		compoFiles = new TreeMap<Integer, String>();
		configFiles = new TreeMap<Integer, String>();
		normFiles = new TreeMap<Integer, String>();
		ruptureFile = null;
		inputRaster = null;
		//scale = -1;
		scales = null;
		unfilters = null;
		compoMetrics = null;
		configMetrics = null;
		outputFolder = null;
		classes = null;
		ecoFiles = new TreeMap<Integer, String>();
		mapFiles = new TreeMap<Integer, String>();
		infoFiles = new TreeMap<Integer, String>();
		gradientFiles = new TreeMap<Integer, String>();
		thematicDistanceFiles = new TreeMap<Integer, String>();
		gradientMapFiles = new TreeMap<Integer, Map<Integer, String>>();
		headerFile = null;
		factor = 1;
		noDataValue = -1;
		displacement = 20;
		distanceType = WindowDistanceType.FAST_GAUSSIAN;
	}
	
	public EcoPaysageProcedure build(){
		
		if(factory.check(this)){
			EcoPaysageProcedure gbProcedure = factory.create(this);
			return gbProcedure;
		}
		
		throw new IllegalArgumentException("parameters are unconsistant");
	}
	
	// setters

	public void setForce(boolean force) {
		this.force = force;
	}
	
	/*
	public void setMetricsFile(String metricsFile) {
		Util.createAccess(metricsFile);
		this.metricsFile = metricsFile;
	}
	*/
	/*
	public void setMetricsFile(int scale, String metricsFile) {
		Util.createAccess(metricsFile);
		//this.setScale(scale);
		this.metricsFiles.put(scale, metricsFile);
	}*/
	
	public void addMetricsFile(int scale, String metricsFile) {
		Util.createAccess(metricsFile);
		this.metricsFiles.put(scale, metricsFile);
	}
	
	public void setXYFile(String xyFile) {
		Util.createAccess(xyFile);
		this.xyFile = xyFile;
	}
	
	public void addCompoFile(int scale, String compoFile) {
		Util.createAccess(compoFile);
		this.compoFiles.put(scale, compoFile);
	}
	
	public void addConfigFile(int scale, String configFile) {
		Util.createAccess(configFile);
		this.configFiles.put(scale, configFile);
	}
	
	public void setNormFile(String normFile) {
		Util.createAccess(normFile);
		this.normFile = normFile;
	}
	
	public void setRuptureFile(String ruptureFile) {
		Util.createAccess(ruptureFile);
		this.ruptureFile = ruptureFile;
	}
	
	public void addNormFile(int scale, String normFile) {
		Util.createAccess(normFile);
		this.normFiles.put(scale, normFile);
	}
	
	public void setInputRaster(String inputRaster) {
		this.inputRaster = inputRaster;
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		this.inEntete = cov.getEntete();
		cov.dispose();
		this.carto = new File(inputRaster).getName().replace(".tif", "").replace(".asc", "");
	}
	
	public void setOutputFolder(String outputFolder) {
		Util.createAccess(outputFolder);
		this.outputFolder = outputFolder;
	}
	
	public void setScale(int scale) {
		//this.scale = scale;
		this.scales = new int[] {scale};
	}
	
	public void setScales(int[] scales) {
		this.scales = scales;
	}
	
	public void setUnfilters(int[] unfilters) {
		this.unfilters = unfilters;
	}

	public void setCodes(int[] codes){
		setCompositionMetrics(codes);
		setConfigurationMetrics(codes);
		setInitMetrics(true);
	}
	
	public void setNoDataValue(int noDataValue) {
		this.noDataValue = noDataValue;
	}
	
	private void setInitMetrics(boolean initMetrics) {
		this.initMetrics = initMetrics;
	}
	
	public boolean initMetrics() {
		return this.initMetrics;
	}

	public void setClasses(int[] classes){
		this.classes = classes;
	}
	
	public void setFactor(int factor) {
		this.factor = factor;
	}
	
	public void addCompositionMetric(String metric) {
		if(compoMetrics == null) {
			compoMetrics = new ArrayList<String>();
		}
		compoMetrics.add(metric);
	}
	
	public void addConfigurationMetric(String metric) {
		if(configMetrics == null) {
			configMetrics = new ArrayList<String>();
		}
		configMetrics.add(metric);
	}
	
	public void removeCompositionMetric(String metric) {
		if(compoMetrics != null) {
			compoMetrics.remove(metric);
		}
	}
	
	public void removeConfigurationMetric(String metric) {
		if(configMetrics != null) {
			configMetrics.remove(metric);
		}
	}
	
	private void setCompositionMetrics(int[] codes){
		compoMetrics = new ArrayList<String>();
		for(int c : codes) {
			compoMetrics.add("pNV_"+c);
		}
	}
	
	private void setConfigurationMetrics(int[] codes){
		configMetrics = new ArrayList<String>();
		for(int c1 : codes) {
			for(int c2 : codes) {
				if(c1 < c2) {
					configMetrics.add("pNC_"+c1+"-"+c2);
				}
			}
		}
	}
	
	public void setEcoFile(int k, String ecoFile) {
		Util.createAccess(ecoFile);
		ecoFiles.put(k, ecoFile);
	}
	
	public void setInfoFile(int k, String infoFile) {
		Util.createAccess(infoFile);
		infoFiles.put(k, infoFile);
	}
	
	public void setThematicDistanceFile(int k, String thematicDistanceFile) {
		Util.createAccess(thematicDistanceFile);
		thematicDistanceFiles.put(k, thematicDistanceFile);
	}
	
	public void setMapFile(int k, String mapFile) {
		Util.createAccess(mapFile);
		mapFiles.put(k, mapFile);
	}
	
	public void setGradientFile(int k, String gradientFile) {
		Util.createAccess(gradientFile);
		gradientFiles.put(k, gradientFile);
	}
	
	public void setGradientMapFile(int k, int ik, String gradientMapFile) {
		Util.createAccess(gradientMapFile);
		if(!gradientMapFiles.containsKey(k)) {
			gradientMapFiles.put(k, new TreeMap<Integer, String>());
		}
		gradientMapFiles.get(k).put(ik, gradientMapFile);
	}
	
	public void setHeaderFile(String headerFile) {
		this.headerFile = headerFile;
	}
	
	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}
	
	public void setWindowDistanceType(WindowDistanceType distanceType) {
		this.distanceType = distanceType;
	}
	
	// getters
	
	public boolean force() {
		return force;
	}
	
	/*
	public boolean metricsFilesExist() {
		boolean ok = true;
		for(int scale : scales) {
			if(!new File(metricsFile(scale)).exists()) {
				return false;
			}
		}
	}*/
	
	public String metricsFile() {
		return metricsFile(scales[0]);
	}
	
	public String metricsFile(int scale) {
		if(!metricsFiles.containsKey(scale)) {
			metricsFiles.put(scale, outputFolder()+"metrics_"+carto+"_scale_"+scale+"m.csv");
		}
		return metricsFiles.get(scale);
	}
	
	public String xyFile() {
		if(xyFile == null) {
			setXYFile(outputFolder()+"metrics_"+carto+"_XY.csv");
		}
		return xyFile;
	}
	/*
	public String compoFile() {
		return compoFile(scales[0]);
	}*/
	
	public String compoFile(int scale) {
		if(!compoFiles.containsKey(scale)) {
			String name = new File(metricsFile(scale)).getName().replace(".txt", "").replace(".csv", "");
			compoFiles.put(scale, outputFolder()+name+"_"+carto+"_compo.csv");
		}
		return compoFiles.get(scale);
	}
	/*
	public String configFile() {
		return configFile(scales[0]);
	}*/
	
	public String configFile(int scale) {
		if(!configFiles.containsKey(scale)) {
			String name = new File(metricsFile(scale)).getName().replace(".txt", "").replace(".csv", "");
			configFiles.put(scale, outputFolder()+name+"_"+carto+"_config.csv");
		}
		return configFiles.get(scale);
	}
	
	public String normFile() {
		if(normFile == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"norm_"+carto);
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			setNormFile(sb.toString());
		}
		return normFile;
	}
	
	public String normFile(int scale) {
		if(!normFiles.containsKey(scale)) {
			String name = new File(metricsFile(scale)).getName().replace(".txt", "").replace(".csv", "");
			normFiles.put(scale, outputFolder()+name+"_"+carto+"_norm.csv");
		}
		return normFiles.get(scale);
	}
	
	public String ruptureFile() {
		if(ruptureFile == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"rupture_"+carto);
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".tif");
			setRuptureFile(sb.toString());
		}
		return ruptureFile;
	}

	public String inputRaster() {
		return inputRaster;
	}
	
	public String carto() {
		return carto;
	}
	
	public String outputFolder() {
		return outputFolder;
	}
	
	public int scale() {
		return scales[0];
	}
	
	public int[] scales() {
		return scales;
	}
	
	public int[] unfilters() {
		if(unfilters == null) {
			unfilters = new int[] {noDataValue()};
		}
		return unfilters;
	}
	
	public int noDataValue() {
		return noDataValue;
	}
	
	public boolean hasMultipleScales() {
		return scales.length > 1;
	}

	public List<String> compoMetrics() {
		return compoMetrics;
	}

	public List<String> configMetrics() {
		return configMetrics;
	}
	
	public int[] classes() {
		return classes;
	}
	
	public int factor() {
		return factor;
	}
	
	public EnteteRaster entete() {
		return inEntete;
	}
	
	public int displacement() {
		return displacement;
	}
	
	public WindowDistanceType windowDistanceType() {
		return distanceType;
	}
	
	public String ecoFile(int k) {
		if(!ecoFiles.containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			setEcoFile(k, sb.toString());
		}
		return ecoFiles.get(k);
	}
	
	public String infoFile(int k) {
		if(!infoFiles.containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"info_ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			setInfoFile(k, sb.toString());
		}
		return infoFiles.get(k);
	}
	
	public String thematicDistanceFile(int k) {
		if(!thematicDistanceFiles.containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"distance_ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".txt");
			setThematicDistanceFile(k, sb.toString());
		}
		return thematicDistanceFiles.get(k);
	}
	
	public String mapFile(int k) {
		if(!mapFiles.containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".tif");
			setMapFile(k, sb.toString());
		}
		return mapFiles.get(k);
	}
	
	public String gradientFile(int k) {
		if(!gradientFiles.containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"gradient_ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			setGradientFile(k, sb.toString());
		}
		return gradientFiles.get(k);
	}
	
	public String gradientMapFile(int k, int ik) {
		if(!gradientMapFiles.containsKey(k) || !gradientMapFiles.get(k).containsKey(ik)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"gradient_ecopaysages_"+carto+"_"+k+"classes_ecop"+ik);
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".tif");
			setGradientMapFile(k, ik, sb.toString());
		}
		return gradientMapFiles.get(k).get(ik);
	}
	
	public String[] thematicDistanceFiles() {
		if(thematicDistanceFiles.size() == 0) {
			for(int k : classes()) {
				thematicDistanceFile(k);
			}
		}
		return thematicDistanceFiles.values().toArray(new String[thematicDistanceFiles.size()]);
	}
	
	public String[] mapFiles() {
		if(mapFiles.size() == 0) {
			for(int k : classes()) {
				mapFile(k);
			}
		}
		return mapFiles.values().toArray(new String[mapFiles.size()]);
	}
	
	public String headerFile() {
		if(headerFile == null) {
			File f = new File(metricsFile());
			String name = f.getName().replace(".txt", "").replace(".csv", "");
			setHeaderFile(f.getParent()+"/"+name+"_header.txt");
		}
		return headerFile;
	}
	
	public Map<Integer, String> normFiles(){
		return this.normFiles;
	}
	
}
