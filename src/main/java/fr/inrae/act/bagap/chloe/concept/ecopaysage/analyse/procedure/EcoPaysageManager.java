package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.calculmetrics.EPPCalculMetricsFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.clustering.EPPClusteringFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.gradient.EPPGradientFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.mapping.EPPMappingFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.rupture.EPPRuptureFactory;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.standardization.EPPStandardizationFactory;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class EcoPaysageManager {

	private EcoPaysageProcedureFactory factory; // constructeur de procedure

	private boolean force;
	
	//private String metricsFile; // csv des metriques
	
	//private Map<Integer, String> metricsFiles; // csv des metriques par echelle
	
	private Map<String, Map<Integer, String>> metricsFiles; // csv des metriques par carto d'entree et par echelle
	
	private String xyFile; // csv des XY
	
	private String carto; // nom de la carto
	private Map<String, String> cartos; // nom de la carto
	
	//private String inputRaster; // raster d'entree
	
	private Set<String> inputRasters; // raster d'entree
	
	private String outputFolder; // dossier de sortie
	
	private Map<Integer, String> compoFiles, configFiles, stdFiles;
	
	private String stdFile, ruptureFile, inertiaFile;
	
	//private int scale; // echelle d'analyse unique
	
	private int[] scales; // echelle d'analyse multiple
	
	private List<String> compoMetrics, configMetrics; // les metriques de compo et de config
	
	private int[] codes;
	
	private boolean initMetrics;
	
	private int[] classes; // les nombres de classes à générer
	
	private int factor; // facteur de clusterisation
	
	private Map<String, Map<Integer, String>> ecoFiles, mapFiles;
	
	private Map<Integer, String> /*ecoFiles, mapFiles,*/ infoFiles, gradientFiles, thematicDistanceFiles;
	
	private Map<Integer, Map<Integer, String>> gradientMapFiles;
	
	private String headerFile; // le fichier d'entete raster
	
	private int noDataValue;
	
	private int[] unfilters;
	
	private EnteteRaster inEntete;
	
	private int displacement;
	
	private WindowDistanceType distanceType;
	
	private String codeImportanceFile, metricImportanceFile;
	
	private Map<String, Float> importances;
	
	private Map<String, Float> inerties;
	
	
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
		//metricsFiles = new TreeMap<Integer, String>();
		metricsFiles = new TreeMap<String, Map<Integer, String>>();
		xyFile = null;
		//compoFile = null;
		//configFile = null;
		stdFile = null;
		inertiaFile = null;
		compoFiles = new TreeMap<Integer, String>();
		configFiles = new TreeMap<Integer, String>();
		stdFiles = new TreeMap<Integer, String>();
		ruptureFile = null;
		//inputRaster = null;
		inputRasters = new TreeSet<String>();
		carto = "essai";
		cartos = new TreeMap<String, String>();
		//scale = -1;
		scales = null;
		unfilters = null;
		codes = null;
		compoMetrics = null;
		configMetrics = null;
		outputFolder = null;
		classes = null;
		ecoFiles = new TreeMap<String, Map<Integer, String>>();
		mapFiles = new TreeMap<String, Map<Integer, String>>();
		infoFiles = new TreeMap<Integer, String>();
		gradientFiles = new TreeMap<Integer, String>();
		thematicDistanceFiles = new TreeMap<Integer, String>();
		gradientMapFiles = new TreeMap<Integer, Map<Integer, String>>();
		headerFile = null;
		factor = 1;
		noDataValue = -1;
		displacement = 20;
		distanceType = WindowDistanceType.FAST_GAUSSIAN;
		importances = null;
		codeImportanceFile = null;
		metricImportanceFile = null;
		inerties = new TreeMap<String, Float>();
	}
	
	public EcoPaysageProcedure build(){
		
		if(factory.check(this)){
			
			// initialisation of importances if needed
			if(codeImportanceFile != null) {
				importances = EcoPaysage.initImportanceByCode(codeImportanceFile, true, true, scales);
			}else if(metricImportanceFile != null){
				importances = EcoPaysage.initImportanceByMetric(metricImportanceFile);
			}else {
				initImportances(codes, scales);
			}
			
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
	
	/*
	public void addMetricsFile(int scale, String metricsFile) {
		Util.createAccess(metricsFile);
		this.metricsFiles.put(scale, metricsFile);
	}
	*/
	
	public void addMetricsFile(String carto, int scale, String metricsFile) {
		Util.createAccess(metricsFile);
		if(!this.metricsFiles.containsKey(carto)) {
			this.metricsFiles.put(carto, new TreeMap<Integer, String>());
		}
		this.metricsFiles.get(carto).put(scale, metricsFile);
	}
	
	public void setXYFile(String xyFile) {
		Util.createAccess(xyFile);
		this.xyFile = xyFile;
	}
	
	public void setInertiaFile(String inertiaFile) {
		Util.createAccess(inertiaFile);
		this.inertiaFile = inertiaFile;
	}
	
	public void addCompoFile(int scale, String compoFile) {
		Util.createAccess(compoFile);
		this.compoFiles.put(scale, compoFile);
	}
	
	public void addConfigFile(int scale, String configFile) {
		Util.createAccess(configFile);
		this.configFiles.put(scale, configFile);
	}
	
	public void setStandardizedFile(String stdFile) {
		Util.createAccess(stdFile);
		this.stdFile = stdFile;
	}
	
	public void setRuptureFile(String ruptureFile) {
		Util.createAccess(ruptureFile);
		this.ruptureFile = ruptureFile;
	}
	
	public void addStandardizedFile(int scale, String stdFile) {
		Util.createAccess(stdFile);
		this.stdFiles.put(scale, stdFile);
	}
	
	/*
	public void setInputRaster(String inputRaster) {
		this.inputRaster = inputRaster;
		Coverage cov = CoverageManager.getCoverage(inputRaster);
		this.inEntete = cov.getEntete();
		cov.dispose();
		this.carto = new File(inputRaster).getName().replace(".tif", "").replace(".asc", "");
	}*/
	
	public void addInputRaster(String inputRaster) {
		this.inputRasters.add(inputRaster);
		if(this.inEntete == null) {
			Coverage cov = CoverageManager.getCoverage(inputRaster);
			this.inEntete = cov.getEntete();
			cov.dispose();
		}
		this.cartos.put(inputRaster, new File(inputRaster).getName().replace(".tif", "").replace(".asc", ""));
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
		this.codes = codes;
		setCompositionMetrics(codes);
		setConfigurationMetrics(codes);
		setInitMetrics(true);
		//initImportances(codes);
	}
	
	private void initImportances(int[] codes, int[] scales) {
		importances = new TreeMap<String, Float>();
		for(int scale : scales) {
			for(int code1 : codes) {
				importances.put("pNV_"+code1+"_"+scale+"m", 1f);
				for(int code2 : codes) {
					if(code1 < code2) {
						importances.put("pNC_"+code1+"-"+code2+"_"+scale+"m", 1f);
					}
				}
			}
		}
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
	
	public void setCodeImportanceFile(String codeImportanceFile) {
		this.codeImportanceFile = codeImportanceFile;
	}
	
	public void setMetricImportanceFile(String metricImportanceFile) {
		this.metricImportanceFile = metricImportanceFile;
	}
	
	public void addEcoFile(String carto, int k, String ecoFile) {
		Util.createAccess(ecoFile);
		if(!this.ecoFiles.containsKey(carto)) {
			this.ecoFiles.put(carto, new TreeMap<Integer, String>());
		}
		ecoFiles.get(carto).put(k, ecoFile);
	}
	
	public void addMapFile(String carto, int k, String mapFile) {
		Util.createAccess(mapFile);
		if(!this.mapFiles.containsKey(carto)) {
			this.mapFiles.put(carto, new TreeMap<Integer, String>());
		}
		mapFiles.get(carto).put(k, mapFile);
	}
	
	public void setInfoFile(int k, String infoFile) {
		Util.createAccess(infoFile);
		infoFiles.put(k, infoFile);
	}
	
	public void setThematicDistanceFile(int k, String thematicDistanceFile) {
		Util.createAccess(thematicDistanceFile);
		thematicDistanceFiles.put(k, thematicDistanceFile);
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
	
	public void setInertia(String group, float inertia) {
		this.inerties.put(group, inertia);
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
		return metricsFile(cartos.get(inputRasters.iterator().next()), scales[0]);
	}
	
	/*
	public String metricsFile(int scale) {
		if(!metricsFiles.containsKey(scale)) {
			metricsFiles.put(scale, outputFolder()+"metrics_"+carto+"_"+scale+"m.csv");
		}
		return metricsFiles.get(scale);
	}
	*/
	
	public String metricsFile(int scale) {
		String carto = cartos.get(inputRasters.iterator().next());
		return metricsFile(carto, scale);
	}
	
	public Set<String> metricsFiles(int scale) {
		
		Set<String> files = new TreeSet<String>();
		for(String carto : cartos.values()) {
			files.add(metricsFile(carto, scale));
		}
		
		return files;
	}
	
	public String metricsFile(String carto, int scale) {
		if(!metricsFiles.containsKey(carto) || !metricsFiles.get(carto).containsKey(scale)) {
			addMetricsFile(carto, scale, outputFolder()+"metrics_"+carto+"_"+scale+"m.csv");
		}
		return metricsFiles.get(carto).get(scale);
	}
	
	public String ecoFile(String carto, int k) {
		if(!ecoFiles.containsKey(carto) || !ecoFiles.get(carto).containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			addEcoFile(carto, k, sb.toString());
		}
		return ecoFiles.get(carto).get(k);
	}
	
	public String mapFile(String carto, int k) {
		if(!mapFiles.containsKey(carto) || !mapFiles.get(carto).containsKey(k)) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"ecopaysages_"+carto+"_"+k+"classes");
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".tif");
			addMapFile(carto, k, sb.toString());
		}
		return mapFiles.get(carto).get(k);
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
			compoFiles.put(scale, outputFolder()+"composition_metrics_"+carto+"_"+scale+"m.csv");
		}
		return compoFiles.get(scale);
	}
	/*
	public String configFile() {
		return configFile(scales[0]);
	}*/
	
	public String configFile(int scale) {
		if(!configFiles.containsKey(scale)) {
			configFiles.put(scale, outputFolder()+"configuration_metrics_"+carto+"_"+scale+"m.csv");
		}
		return configFiles.get(scale);
	}
	
	public String standardizedFile() {
		if(stdFile == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"standardized_metrics_"+carto);
			for(int scale : scales) {
				sb.append("_"+scale+"m");
			}
			sb.append(".csv");
			setStandardizedFile(sb.toString());
		}
		return stdFile;
	}
	
	public String inertiaFile() {
		if(inertiaFile == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(outputFolder()+"inertia_"+carto);
			sb.append(".csv");
			setInertiaFile(sb.toString());
		}
		return inertiaFile;
	}
	
	public String standardizedFile(int scale) {
		if(!stdFiles.containsKey(scale)) {
			stdFiles.put(scale, outputFolder()+"standardized_metrics_"+carto+"_"+scale+"m.csv");
		}
		return stdFiles.get(scale);
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

	/*
	public String inputRaster() {
		return inputRaster;
	}
	*/
	
	public Set<String> inputRasters() {
		return inputRasters;
	}
	
	public String carto() {
		return carto;
	}
	
	public Map<String, String> cartos() {
		return cartos;
	}
	
	public String carto(String inputRaster) {
		return cartos.get(inputRaster);
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

	public int[] codes() {
		return codes;
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
			for(String carto : cartos.values()) {
				for(int k : classes()) {
					mapFile(carto, k);
				}	
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
	
	public Map<Integer, String> standardizedFiles(){
		return this.stdFiles;
	}
	
	public Map<String, Float> importances(){
		return importances;
	}
	
	public Map<String, Float> inerties(){
		return inerties;
	}
	
}
