package fr.inrae.act.bagap.chloe.util.analysis;

import java.io.IOException;
import java.util.Map;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;

public class ChloeUtilAnalysisBuilder extends ChloeAnalysisBuilder {
	
	private Map<String, String> factors;
	
	private String outputRaster;
	
	private String inputRaster;
	
	private String combination;
	
	private Map<Float, Float> changes;
	
	private int noDataValue;
	
	@Override
	public void reset() {
		factors = null;
		outputRaster = null;
		inputRaster = null;
		combination = null;
		changes = null;
		noDataValue = Raster.getNoDataValue();
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
	
	private void addRasterOutput(String outputRaster){
		this.outputRaster = outputRaster;
	}
	
	@Override
	public void setRasterFile(String rasterFile){
		this.inputRaster = rasterFile;
	}
	
	@Override
	public void setCombination(String combination){
		this.combination = combination;
	}
	
	@Override
	public void setChanges(Map<Float, Float> changes) {
		this.changes = changes;
	}
	
	@Override
	public void setNoDataValue(int noDataValue) {
		this.noDataValue = noDataValue;
	}
	
	public Map<String, String> getNamesAndRasters(){
		return factors;
	}
	
	public String getOutputRaster(){
		return outputRaster;
	}
	
	@Override
	public String getRasterFile() {
		return inputRaster;
	}
	
	public String getCombination(){
		return combination;
	}
	
	public Map<Float, Float> getChanges(){
		return changes;
	}
	
	public int getNoDataValue(){
		return noDataValue;
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
