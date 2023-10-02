package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;
import java.util.Map;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;

public class ChloeUtilAnalysisBuilder extends ChloeAnalysisBuilder {
	
	private Map<String, String> factors;
	
	private String raster;
	
	private String combination;
	
	@Override
	public void reset() {
		factors = null;
		raster = null;
		combination = null;
	}
	
	@Override
	public void setNamesAndRasters(Map<String, String> factors){
		this.factors = factors;
	}
	
	@Override
	public void addAsciiGridOutput(String ascii){
		addRasterOutput(ascii);
	}
	
	@Override
	public void addGeoTiffOutput(String geotiff){
		addRasterOutput(geotiff);
	}
	
	private void addRasterOutput(String raster){
		this.raster = raster;
	}
	
	@Override
	public void setCombination(String combination){
		this.combination = combination;
	}
	
	public Map<String, String> getNamesAndRasters(){
		return factors;
	}
	
	public String getOutputRaster(){
		return raster;
	}
	
	public String getCombination(){
		return combination;
	}
	
	@Override
	public ChloeAnalysis build() {
		ChloeUtilAnalysis analysis = null;
		try{
			
			analysis = ChloeUtilAnalysisFactory.create(this);

		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			reset();
		}
		return analysis;
	}


}
