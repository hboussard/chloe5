package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist.CubistModel;
import fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist.CubistModelFactory;
import fr.inrae.act.bagap.chloe.util.Util;

public class CubistEphestiaToulouseManager {

	private CubistModel model;
	
	private float[] dataCover, dataSystem;
	
	private String farms;
	
	private EnteteRaster enteteInput, enteteOutput;
	
	private int delta;
	
	private String site;
	
	private String systemFile, iftFile, meteoFile;
	
	private Set<String> cultures, snh_surf, snh_lin;
	
	private Map<Integer, Map<Float, Float>> mapIFT;
	
	private Map<Float, String> mapTypeCulture;
	
	private Map<String, Double> meteo;
	
	private String modelOutput;
	
	public CubistEphestiaToulouseManager() {
		init();
	}
	
	private void init() {
	
		site = "Toulouse";
		delta = 10; // 10 pixels de deplacement --> 50m
	}
	
	public void setCubistModel(String cubistModelName) {
		
		model = CubistModelFactory.create(cubistModelName);
	}

	public void setDataCover(String raster) {
		
		Coverage covCover = CoverageManager.getCoverage(raster);
		dataCover = covCover.getData();
		enteteInput = covCover.getEntete();
		covCover.dispose();
	}
	
	public void setDataFarm(String farms) {
		
		this.farms = farms;
	}
	
	public void setSystemFile(String systemFile) {
		
		this.systemFile = systemFile;
	}
	
	public void setIFTFile(String iftFile) {
		
		this.iftFile = iftFile;
	}
	
	public void setMeteoFile(String meteoFile) {
		
		this.meteoFile = meteoFile;
	}
	
	public void setModelOutput(String modelOutput) {
		
		this.modelOutput = modelOutput;
	}
	
	public CubistModel cubistModel() {
		
		return model;
	}

	public float[] dataCover() {
		
		return dataCover;
	}
	
	public float[] dataSystem() {
		
		return dataSystem;
	}
	
	public EnteteRaster enteteInput() {
		
		return enteteInput;
	}
	
	public EnteteRaster enteteOutput() {
		
		return enteteOutput;
	}
	
	public int delta() {
		
		return delta;
	}
	
	public Set<String> cultures(){
		
		return cultures;
	}
	
	public Set<String> snh_surf(){
		
		return snh_surf;
	}
	
	public Set<String> snh_lin(){
		
		return snh_lin;
	}
	
	public Map<Integer, Map<Float, Float>> mapIFT(){
		
		return mapIFT;
	}
	
	public Map<Float, String> mapTypeCulture(){
		
		return mapTypeCulture;
	}
	
	public Map<String, Double> meteo(){
		
		return meteo;
	}
	
	public String modelOutput() {
		
		return modelOutput;
	}
	
	private void initIFT() {
		
		cultures = new HashSet<String>();
		snh_surf = new HashSet<String>();
		snh_lin = new HashSet<String>();
		mapIFT = new HashMap<Integer, Map<Float, Float>>();
		mapIFT.put(1, new HashMap<Float, Float>()); // ac
		mapIFT.put(2, new HashMap<Float, Float>()); // ad
		mapIFT.put(3, new HashMap<Float, Float>()); // ab
		mapTypeCulture = new HashMap<Float, String>();
		try {
			CsvReader cr = new CsvReader(iftFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			String code;
			int type;
			while(cr.readRecord()){
				code = cr.get("code");
				type = Integer.parseInt(cr.get("type"));
				switch(type){
				case 0 : break;
				case 1 : // les cultures
					cultures.add(code); 
					mapIFT.get(1).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcs")));
					mapIFT.get(2).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcd")));
					mapIFT.get(3).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftab")));
					mapTypeCulture.put(Float.parseFloat(code), cr.get("typeculture"));
					break;
				case 2 : // les snh surfaciques
					snh_surf.add(code);
					mapIFT.get(1).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcs")));
					mapIFT.get(2).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcd")));
					mapIFT.get(3).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftab")));
					break;
				case 3 : // les snh lineaires
					snh_lin.add(code);
					mapIFT.get(1).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcs")));
					mapIFT.get(2).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftcd")));
					mapIFT.get(3).put(Float.parseFloat(code), Float.parseFloat(cr.get("iftab")));
					break;
				}
				
			}
			cr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initMeteo() {
		
		meteo = new HashMap<String, Double>();
		try {
			CsvReader cr = new CsvReader(meteoFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			while(cr.readRecord()){
				if(cr.get("Site").equalsIgnoreCase(site)){
					
					meteo.put("Mean_Ps", Double.parseDouble(cr.get("Mean_Ps")));
					meteo.put("Mean_Vm", Double.parseDouble(cr.get("Mean_Vm")));
					meteo.put("Mean_Tx", Double.parseDouble(cr.get("Mean_Tx")));
					meteo.put("Mean_An_Tn", Double.parseDouble(cr.get("Mean_An_Tn")));
					meteo.put("Mean_An_Tm", Double.parseDouble(cr.get("Mean_An_Tm")));
					meteo.put("Mean_An_Tx", Double.parseDouble(cr.get("Mean_An_Tx")));
					meteo.put("Mean_An_Ps", Double.parseDouble(cr.get("Mean_An_Ps")));
					meteo.put("Mean_An_Gel_succ", Double.parseDouble(cr.get("Mean_An_Gel_succ")));
							
					break;
				}
			}
			cr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initEnteteOutput() {
		
		double minx = enteteInput.minx();
		double maxx = enteteInput.maxx();
		double miny = enteteInput.miny();
		double maxy = enteteInput.maxy();
		float inCellSize = enteteInput.cellsize();
		int noDataValue = enteteInput.noDataValue();
		
		int outWidth = new Double(Math.floor((maxx - minx) / (inCellSize*delta))).intValue();
		int outHeight = new Double(Math.floor((maxy - miny) / (inCellSize*delta))).intValue();
		enteteOutput = new EnteteRaster(outWidth, outHeight, 
				minx+(inCellSize/2.0)-(inCellSize*delta)/2.0, 
				maxx-(inCellSize/2.0)+(inCellSize*delta)/2.0, 
				miny+(inCellSize/2.0)-(inCellSize*delta)/2.0 + (inCellSize*delta), 
				maxy-(inCellSize/2.0)+(inCellSize*delta)/2.0 + (inCellSize*delta), 
				(float) (inCellSize*delta), noDataValue);
	}
	
	private void initDataSystem() {
		
		Map<Float, Float> systems = Util.importData(systemFile, "farm", "system");
		
		Coverage covFarm = CoverageManager.getCoverage(farms);
		float[] dataFarm = covFarm.getData();
		covFarm.dispose();
		
		dataSystem = new float[dataFarm.length]; 
		SearchAndReplacePixel2PixelTabCalculation srpptc = new SearchAndReplacePixel2PixelTabCalculation(dataSystem, dataFarm, systems);
		srpptc.run();
	}
	
	private boolean check() {
	
		initIFT();
		
		initMeteo();
		
		initEnteteOutput();
		
		initDataSystem();
		
		return true;
	}

	public CubistEphestiaToulouseAnalyse build() {
		
		if(check()) {
			
			return new CubistEphestiaToulouseAnalyse(this);
		}
		
		throw new IllegalArgumentException();
	}
	
}
