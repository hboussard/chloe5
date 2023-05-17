package fr.inrae.act.bagap.chloe.window.output;

import java.util.Set;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.counting.CountingObserver;
import fr.inrae.act.bagap.chloe.window.metric.Metric;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public abstract class TileRasterOutput implements CountingObserver {

	private String folder;
	
	private Metric metric;
	
	private Tile tile;
	
	private int tileSize;
	
	private double minX, maxX, minY, maxY;
	
	private double cellSize;
	
	private int noDataValue;
	
	private float[][] tabs;
	
	private boolean[] actives;
	
	private int width, height;
	
	private int initMinX, initMinY/*, initMaxX, initMaxY*/;
	
	private int theX, theY;
	
	private int tY;
	
	public TileRasterOutput(String folder, Metric metric, Tile tile, int width, int height, double minX, double maxX, double minY, double maxY, double cellSize, int noDataValue){
		this.folder = folder;
		this.metric = metric;
		
		//this.tile = Tile.getTile(tile, new Envelope(minX, maxX, minY, maxY));
		this.tile = new Tile(new Envelope(minX, maxX, minY, maxY), tile.getNcols(), tile.getNrows(), tile.getTileLength());
		
		this.width = width;
		this.height = height;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}

	@Override
	public void init(Counting c, Set<Metric> metrics) {
		
		tileSize = new Double(tile.getTileLength()/cellSize).intValue();
		
		actives = new boolean[tile.getNcols()];
		tabs = new float[tile.getNcols()][new Double(Math.pow(tileSize, 2)).intValue()];
		initMinX = new Double((minX - tile.getMinX())/cellSize).intValue();
		initMinY = new Double((tile.getMaxY() - maxY)/cellSize).intValue();
		
		tY = 0;
		theX = initMinX;
		theY = initMinY;
		
		resetTabs();
		
		for(int y=0; y<initMinY; y++){
			for(int t=0; t<tile.getNcols(); t++){
				for(int x=0; x<tileSize; x++){
					tabs[t][y*tileSize + x] = noDataValue;
				}
			}
		}
		
	}

	@Override
	public void postrun(Counting c, int i, int j, Set<Metric> metrics) {
		
		if(theX == initMinX){
			for(int x=0; x<theX; x++){
				tabs[0][theY*tileSize + x] = noDataValue;
			}
		}
		
		//System.out.println(metric);
		double v = metric.value();
		//System.out.println(theX+" "+tileSize+" "+(theX/tileSize)+" "+(theX%tileSize));
		int t = theX/tileSize;
		int x = theX%tileSize;
		if(!actives[t] && v != noDataValue){
			actives[t] = true;
		}
		tabs[t][theY*tileSize + x] = (float) v;
		theX++;
		if((theX-initMinX) >= width){
			for(x++; x<tileSize; x++){
				tabs[t][theY*tileSize + x] = noDataValue;
			}
			theY++;
			theX = initMinX;
			
			boolean export = false;
			if((tY+1) == tile.getNrows() && tY*tileSize+theY == initMinY+height){
				for(int y=theY; y<tileSize; y++){
					for(t=0; t<tile.getNcols(); t++){
						for(x=0; x<tileSize; x++){
							tabs[t][y*tileSize + x] = noDataValue;
						}
					}
				}
				export = true;
			}
			
			if(export || theY == tileSize){
				exportTabs();
				resetTabs();
				theY = 0;
				tY++;
			}
		}
		
	}

	private void resetTabs(){
		for(int t=0; t<tile.getNcols(); t++){
			actives[t] = false;
			//Arrays.fill(tabs[t], noDataValue);
		}
	}
	
	private void exportTabs() {
		for(int t=0; t<tile.getNcols(); t++){
			if(actives[t]){
				exportTab(t);
			}
		}
	}
	
	private void exportTab(int tX) {
		Envelope e = tile.getEnvelope(tX, tY);
		EnteteRaster entete = new EnteteRaster(tileSize, tileSize, e.getMinX(), e.getMaxX(), e.getMinY(), e.getMaxY(), (float) cellSize, noDataValue);
		writeRaster(folder+metric.getName()+"_"+((int) e.getMinX()/1000)+"_"+((int) e.getMaxY()/1000), tabs[tX], entete);
	}

	@Override
	public void close(Counting c, Set<Metric> metrics) {
		
		tabs = null;
	}
	
	@Override
	public void prerun(Counting c) {
		// do nothing;
	}

	@Override
	public void postrun(Counting c, int id, Set<Metric> metrics) {
		// do nothing
	}

	protected abstract void writeRaster(String file, float[] data, EnteteRaster entete);
	
}
