package fr.inrae.act.bagap.chloe.distance.output;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public abstract class TileRasterDistanceOutput {
	
	private String folder, name;
	
	private Tile tile;
	
	private int tileSize;
	
	private int maxTile;
	
	//private double minX, /*maxX, minY,*/ maxY;
	
	private double cellSize;
	
	private int noDataValue;
	
	private float[][] tabs;
	
	private boolean[] actives;
	
	private float[] datas;
	
	private int width, height;
	
	private int theY;
	
	private int tY;
	
	private int tabY;
	
	public TileRasterDistanceOutput(String folder, String name, Tile tile, int width, int height, int maxTile, double minX, double maxX, double minY, double maxY, double cellSize, int noDataValue){
		this.folder = folder;
		this.name = name;
		this.tile = Tile.getTile(tile, new Envelope(minX, maxX, minY, maxY));
		this.width = width;
		this.height = height;
		this.maxTile = maxTile;
		//this.minX = minX;
		//this.maxX = maxX;
		//this.minY = minY;
		//this.maxY = maxY;
		
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
	}
	
	public void init() {
		
		tileSize = new Double(tile.getTileLength()/cellSize).intValue();
		
		actives = new boolean[tile.getNcols()];
		tabs = new float[tile.getNcols()][new Double(Math.pow(tileSize, 2)).intValue()];
		
		datas = new float[maxTile*width];
		
		tabY = 0;
		tY = 0;
		theY = 0;
		
		resetTabs();
	}
	
	private void resetTabs(){
		for(int t=0; t<tile.getNcols(); t++){
			//tabs[t] = new float[new Double(Math.pow(tileSize, 2)).intValue()];
			actives[t] = false;
		}
		tabY = 0;
	}

	public void post(int x, int y, int roiWidth, int roiHeight, float[] values) {
		
		for(int j=0; j<roiHeight; j++){
			for(int i=0; i<roiWidth; i++){
				//System.out.println((j*width + (i+x))+" "+(j*roiWidth + i));
				datas[j*width + (i+x)] = values[j*roiWidth + i];
			}	
		}
		
	}
	
	public void operate(int y, int localHeight){
		if(y == 0){
			doOperate(0, localHeight);	
		}else{
			doOperate(1, localHeight);
		}
		
	}
	
	private void doOperate(int y, int localHeight){
		
		float v;
		while(tabY < tileSize && y < localHeight){
			for(int t=0; t<tile.getNcols(); t++){
				
				for(int x=0; x<tileSize; x++){
					v = datas[y*width + x + t*tileSize];
					if(v != noDataValue){
						actives[t] = true;
					}
					tabs[t][tabY*tileSize + x] = v;
				}
			}
			
			tabY++;
			y++;
			theY++;
		}
		
		if(tabY == tileSize){
			exportTabs();
			tY++;
			resetTabs();
			doOperate(y, localHeight);
		}else if(theY == height){
			while(tabY < tileSize){
				for(int t=0; t<tile.getNcols(); t++){
					for(int x=0; x<tileSize; x++){
						tabs[t][tabY*tileSize + x] = noDataValue;
					}
				}
				tabY++;
			}
			exportTabs();
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
		writeRaster(folder+name+"_"+((int) e.getMinX()/1000)+"_"+((int) e.getMaxY()/1000), tabs[tX], entete);
	}

	public void close() {
		datas = null;
		tabs = null;
		actives = null;
	}

	protected abstract void writeRaster(String file, float[] data, EnteteRaster entete);
	
}
