package fr.inrae.act.bagap.chloe.analysis;

import java.util.Map;
import java.util.Set;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.RefPoint;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.chloe.window.output.DataOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public abstract class ChloeAnalysisBuilder {

	private ChloeAnalysisType analysisType;
	
	public ChloeAnalysisBuilder(){
		reset();
	}
	
	public abstract ChloeAnalysis build();
	
	public abstract void reset();
	
	public void setAnalysisType(ChloeAnalysisType analysisType){
		this.analysisType = analysisType;
	}
	
	public ChloeAnalysisType getAnalysisType() {
		return analysisType;
	}
	
	// setters
	
	public void addObserver(CountingObserver observer){
		throw new UnsupportedOperationException();
	}
	
	public void setObservers(Set<CountingObserver> observers){
		throw new UnsupportedOperationException();
	}
	
	
	public void setWindowShapeType(WindowShapeType shapeType){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowDistanceType(WindowDistanceType distanceType){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowDistanceFunction(String function){
		throw new UnsupportedOperationException();
	}
	
	public void setCoverage(Coverage coverage){
		throw new UnsupportedOperationException();
	}
	
	public void setCoverage2(Coverage coverage2){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTile(String rasterTile){
		throw new UnsupportedOperationException();
	}

	public void setRasterFile(String rasterFile){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterFile2(String rasterFile2){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTab(float[] inputDatas){
		throw new UnsupportedOperationException();
	}
	
	public void setRasterTab2(float[] inputDatas){
		throw new UnsupportedOperationException();
	}
	
	public void setEntete(EnteteRaster entete){
		throw new UnsupportedOperationException();
	}
	
	public void setEntityRasterFile(String entityRasterFile){
		throw new UnsupportedOperationException();
	}
	
	public void setEntityRasterTab(float[] entityRasterTab){
		throw new UnsupportedOperationException();
	}

	public void setValues(String sValues){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowSize(int windowSize){
		throw new UnsupportedOperationException();
	}

	public void setWindowRadius(double radius){
		throw new UnsupportedOperationException();
	}
	
	public void addWindowSize(int windowSize){
		throw new UnsupportedOperationException();
	}
	
	public void addWindowRadius(double radius){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowSizes(int[] windowSizes){
		throw new UnsupportedOperationException();
	}
	
	public void setWindowRadius(double[] radius){
		throw new UnsupportedOperationException();
	}
	
	public void setDisplacement(int displacement){
		throw new UnsupportedOperationException();
	}
	
	public void setInterpolation(boolean interpolation){
		throw new UnsupportedOperationException();
	}

	public void setUnfilters(int[] unfilters){
		throw new UnsupportedOperationException();
	}
	
	public void setFilters(int[] filters){
		throw new UnsupportedOperationException();
	}
	
	public void addMetric(Metric metric){
		throw new UnsupportedOperationException();
	}
	
	public void addMetric(String metric){
		throw new UnsupportedOperationException();
	}
	
	public void setMetrics(Set<Metric> metrics){
		throw new UnsupportedOperationException();
	}
	
	public void addCsvOutput(String csv){
		throw new UnsupportedOperationException();
	}
	
	public void addCoverageOutput(CoverageOutput coverageOutput){
		throw new UnsupportedOperationException();
	}
	
	public void addAsciiGridFolderOutput(String asciiGridFolder){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffFolderOutput(String geoTiffFolder){
		throw new UnsupportedOperationException();
	}
	
	public void addAsciiGridOutput(String ascii){
		throw new UnsupportedOperationException();
	}
	
	public void addAsciiGridOutput(String metric, String ascii){
		throw new UnsupportedOperationException();
	}

	public void addAsciiGridOutput(int size, String metric, String ascii){
		throw new UnsupportedOperationException();
	}
	
	public void addTabOutput(float[] tab){
		throw new UnsupportedOperationException();
	}

	public void addTabOutput(String metric, float[] tab){
		throw new UnsupportedOperationException();
	}
	
	public void addTabOutput(int size, String metric, float[] tab){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(String metric, String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addGeoTiffOutput(int size, String metric, String geotiff){
		throw new UnsupportedOperationException();
	}
	
	public void addDataOutput(String metric, Map<RefPoint, Float> datas){
		throw new UnsupportedOperationException();
	}
	
	public void addDataOutput(DataOutput dout){
		throw new UnsupportedOperationException();
	}
	
	public void addTileAsciiGridOutput(String metric, String pathTile, Tile tile){
		throw new UnsupportedOperationException();
	}
	
	public void addTileGeoTiffOutput(String metric, String pathTile, Tile tile){
		throw new UnsupportedOperationException();
	}
	
	public void setPointsFilter(String points){
		throw new UnsupportedOperationException();
	}
	
	public void setPointsFilter(Set<RefPoint> points){
		throw new UnsupportedOperationException();
	}
	
	public void setPixelsFilter(String pixels){
		throw new UnsupportedOperationException();
	}
	
	public void setPixelsFilter(Set<? extends Pixel> pixels){
		throw new UnsupportedOperationException();
	}

	public void setWindowsPath(String windowsPath){
		throw new UnsupportedOperationException();
	}
	
	public void setBufferSize(int buffSize){
		throw new UnsupportedOperationException();
	}
	 
	public void setROIX(int roiX){
		throw new UnsupportedOperationException();
	}
	
	public void setROIY(int roiY){
		throw new UnsupportedOperationException();
	}
	
	public void setROIWidth(int roiWidth){
		throw new UnsupportedOperationException();
	}
	
	public void setROIHeight(int roiHeight){
		throw new UnsupportedOperationException();
	}
	
	public void setMinRate(double min){
		throw new UnsupportedOperationException();
	}
	
	public void setNamesAndRasters(Map<String, String> factors){
		throw new UnsupportedOperationException();
	}
	
	public void setCombination(String combination){
		throw new UnsupportedOperationException();
	}
	
	public void setChanges(Map<Float, Float> changes) {
		throw new UnsupportedOperationException();
	}

	public void setNoDataValue(int noDataValue) {
		throw new UnsupportedOperationException();
	}
	
	// getters
	
	public WindowShapeType getWindowShapeType() {
		throw new UnsupportedOperationException();
	}
	
	public int getWindowSize(){
		throw new UnsupportedOperationException();
	}
	
	public int getDisplacement() {
		throw new UnsupportedOperationException();
	}

	public String getRasterFile() {
		throw new UnsupportedOperationException();
	}
}
