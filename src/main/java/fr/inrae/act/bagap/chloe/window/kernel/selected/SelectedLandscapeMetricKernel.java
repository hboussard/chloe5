package fr.inrae.act.bagap.chloe.window.kernel.selected;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.aparapi.Kernel;

import fr.inra.sad.bagap.apiland.core.space.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.PixelWithID;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.window.kernel.LandscapeMetricKernel;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public abstract class SelectedLandscapeMetricKernel extends Kernel implements LandscapeMetricKernel {

	private final int windowSize;
	
	private float[] coeff;
	
	//private final int noDataValue;
	
	private final EnteteRaster entete;
	
	private float[][] inDatas;
	
	private Map<Pixel, double[]> outDatas;
	
	private int localROIY;
	
	private int width, height;
	
	private int bufferROIXMin, bufferROIXMax, bufferROIYMin, bufferROIYMax; // in pixels
	
	private Set<Pixel> pixels;
	
	private String windowsPath;
	
	@SuppressWarnings("deprecation")
	protected SelectedLandscapeMetricKernel(int windowSize, Set<Pixel> pixels, float[] coeff, EnteteRaster entete, String windowsPath){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.windowSize = windowSize;
		this.pixels = pixels;
		this.coeff = coeff;
		this.entete = entete;
		this.windowsPath = windowsPath;
	}
	
	public void applySelectedWindow(int buffer, int localROIY) {
		this.localROIY = localROIY;
		execute((width - bufferROIXMin - bufferROIXMax) * buffer);
	}
	
	@Override
	public void run() {
		final int x = bufferROIXMin() + (getGlobalId(0) % (width() - bufferROIXMin() - bufferROIXMax()));
		final int y = bufferROIYMin() + (getGlobalId(0) / (width() - bufferROIXMin() - bufferROIXMax()));
		
		Pixel p = new Pixel(getGlobalId(0) % width(), (localROIY+(getGlobalId(0) / width())));
		if(pixels().contains(p)){
			processPixel(p, x, y);
			exportFilters(p, x, y); // export des filtres si demande
		}
	}
	
	protected void processPixel(Pixel p, int x, int y){
		// do nothing
	}

	public int windowSize(){
		return this.windowSize;
	}
	
	protected void setCoeff(float[] coeff){
		this.coeff = coeff;
	}
	
	protected float[] coeff(){
		return this.coeff;
	}
	
	protected int noDataValue(){
		return entete.noDataValue();
	}
	
	protected double cellSize(){
		return entete.cellsize();
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setBufferROIXMin(int bufferROIXMin) {
		this.bufferROIXMin = bufferROIXMin;
	}

	public void setBufferROIXMax(int bufferROIXMax) {
		this.bufferROIXMax = bufferROIXMax;
	}

	public void setBufferROIYMin(int bufferROIYMin) {
		this.bufferROIYMin = bufferROIYMin;
	}

	public void setBufferROIYMax(int bufferROIYMax) {
		this.bufferROIYMax = bufferROIYMax;
	}

	public void setInDatas(float[][] inDatas){
		this.inDatas = inDatas;
	}
	
	protected float[] inDatas(){
		return inDatas[0];
	}
	
	protected float[] inDatas(int index){
		return inDatas[index-1];
	}
	
	public void setOutDatas(Map<Pixel, double[]> outDatas){
		this.outDatas = outDatas;
	}
	
	public Map<Pixel, double[]> outDatas(){
		return outDatas;
	}
	
	protected int width(){
		return this.width;
	}
	
	protected int height(){
		return this.height;
	}
	
	public int bufferROIXMin(){
		return this.bufferROIXMin;
	}
	
	public int bufferROIXMax(){
		return this.bufferROIXMax;
	}
	
	public int bufferROIYMin(){
		return this.bufferROIYMin;
	}
	
	public int bufferROIYMax(){
		return this.bufferROIYMax;
	}

	public Set<Pixel> pixels(){
		return pixels;
	}
	
	public String windowsPath(){
		return windowsPath;
	}
	
	private void exportFilters(Pixel p, int x, int y){
		if(windowsPath != null){
			int mid = windowSize() / 2;
			float[] image = new float[windowSize()*windowSize()];
			Arrays.fill(image, Raster.getNoDataValue());
			int ic;
			for (int dy = -mid; dy <= mid; dy += 1) {
				if(((y + dy) >= 0) && ((y + dy) < height())){
					for (int dx = -mid; dx <= mid; dx += 1) {
						if(((x + dx) >= 0) && ((x + dx) < width())){
							ic = ((dy+mid) * windowSize()) + (dx+mid);
							float coeff = coeff()[ic];
							if(coeff > 0){
								image[ic] = inDatas()[((y + dy) * width()) + (x + dx)];
							}
						}
					}
				}
			}

			double X, Y;
			if(p instanceof PixelWithID){
				X = ((PixelWithID) p).getX();
				Y = ((PixelWithID) p).getY();
				
				//System.out.println(X+" "+CoordinateManager.getProjectedX(entete, x)+" "+Y+" "+CoordinateManager.getProjectedY(entete, y));
			}else{
				X = CoordinateManager.getProjectedX(entete, x);
				Y = CoordinateManager.getProjectedY(entete, y);
			}
			
			double minx = CoordinateManager.getProjectedX(entete, x-mid)-(entete.cellsize()/2);
			double maxx = CoordinateManager.getProjectedX(entete, x+mid)+(entete.cellsize()/2);
			double miny = CoordinateManager.getProjectedY(entete, y-mid)+(entete.cellsize()/2);
			double maxy = CoordinateManager.getProjectedY(entete, y+mid)-(entete.cellsize()/2);

			EnteteRaster localEntete = new EnteteRaster(windowSize(), windowSize(), minx, maxx, miny, maxy, entete.cellsize(), entete.noDataValue());
			CoverageManager.write(windowsPath+"window_"+X+"-"+Y+".tif", image, localEntete);
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		coeff = null;
		inDatas = null;
		outDatas = null;
		pixels = null;
	}
	
}
