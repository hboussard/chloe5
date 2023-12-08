package fr.inrae.act.bagap.chloe.distance.analysis.euclidian;

import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;
import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inrae.act.bagap.chloe.distance.output.TileGeoTiffDistanceOutput;
import fr.inrae.act.bagap.chloe.distance.output.TileRasterDistanceOutput;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class HugeChamferDistanceAnalysis extends Analysis {

	private String folder, name, temp;
	
	private int[] codes;
	
	private float threshold;
	
	private int maxTile = 5000;
	
	private int width, height;
	
	private double imageMinX, imageMinY, imageMaxX, imageMaxY;
	
	private float cellSize;
	
	private int noDataValue;
	
	private int dWidth, dHeight;
	
	private Map<Pixel, Map<String, float[]>> bords;
	
	private short[] calculTab;
	
	private short[] majTab;
	
	private Coverage inCoverage;
	
	private Tile tile;
	
	public HugeChamferDistanceAnalysis(Coverage inCoverage, Tile tile, String output, String name, int[] codes, float threshold) {
		this.inCoverage = inCoverage;
		this.folder = output;
		this.name = name;
		
		this.tile = tile;
		
		this.temp = output+"euclidian/";
		Util.createAccess(temp);
		this.temp += "name";
		
		this.codes = codes;
		this.threshold = threshold;
	}
	
	@Override
	protected void doInit() {
		
		EnteteRaster entete = inCoverage.getEntete();
		
		width = entete.width();
		height = entete.height();
		imageMinX = entete.minx();
		imageMinY = entete.miny();
		imageMaxX = entete.maxx();
		imageMaxY = entete.maxy();
		cellSize = entete.cellsize();
		noDataValue = entete.noDataValue();
		
		dWidth = 0;
		dHeight = 0;
		bords = new HashMap<Pixel, Map<String, float[]>>();
		
		// tuilage à priori
		int dx = 0, dy = 0;
		Pixel p;
		int roiWidth, roiHeight;
		float[] bord;
		for(int y=0; y<height; dy++, y+=maxTile-1){
			dx = 0;
			dWidth = 0;
			for(int x=0; x<width; dx++, x+=maxTile-1){
				dWidth++;
				p = new Pixel(dx, dy);
				bords.put(p, new HashMap<String, float[]>());
				
				roiWidth = Math.min(maxTile, width - x);
				roiHeight = Math.min(maxTile, height - y);
				
				if(dx > 0){
					bord = new float[roiHeight];
					Arrays.fill(bord, -1);
					bords.get(p).put("left", bord);
					bords.get(new Pixel(dx-1, dy)).put("right", bord);
				}
				if(dy > 0){
					bord = new float[roiWidth];
					Arrays.fill(bord, -1);
					bords.get(p).put("north", bord);
					bords.get(new Pixel(dx, dy-1)).put("south", bord);
				}
				
			}
			dHeight++;
		}
		
		calculTab = new short[dWidth*dHeight];
		Arrays.fill(calculTab, (short) -1);
		
		majTab = new short[dWidth*dHeight];
		Arrays.fill(majTab, (short) 0);
	}
	
	@Override
	protected void doRun() {	
		
		boolean finish = false, ok, isFinish;
		int pass = 1;
		float[] inDatas, outDatas, bord;
		int roiWidth, roiHeight;
		double roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY;
		float v;
		TabChamferDistanceAnalysis ccd;
		GridCoverage2D cov;
		Pixel p;
		PlanarImage planarImage;
		while(!finish){
			finish = true;
			//System.out.println("pass "+pass);
			if(pass == 1){
				
				int dx = 0;
				int dy = 0;
				for(int y=0; y<height-1; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width-1; dx++, x+=maxTile-1){
						
						//System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						roiWidth = Math.min(maxTile, width - x);
						roiHeight = Math.min(maxTile, height - y);
						roiPosMinX = imageMinX + x*cellSize;
						roiPosMaxX = roiPosMinX + roiWidth * cellSize;
						roiPosMinY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
						roiPosMaxY = roiPosMinY + roiHeight * cellSize;
						
						// recuperation des valeurs initiales
						inDatas = inCoverage.getData(new Rectangle(x, y, roiWidth, roiHeight));
						
						// donnees de sortie
						outDatas = new float[roiHeight*roiWidth];
						
						// analyse de distance
						ccd = new TabChamferDistanceAnalysis(outDatas, inDatas, roiWidth, roiHeight, cellSize, noDataValue, codes, threshold);
						
						ccd.init();
						inDatas = null;
						
						if(ccd.hasValue()){
							ccd.run();
							outDatas = (float[]) ccd.getResult();
							
							// maj des bords
							isFinish = bordUpdateFromData(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
							if(!isFinish){
								finish = false;
							}
							
							calculTab[dy*dWidth+dx] = 1;
						}
						
						// stockage en mémoire fichier de l'état de la tuile
						CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
						
					}	
				}
				//finish = true;
			}else{
				
				if(inCoverage != null){
					inCoverage.dispose();
					inCoverage = null;
				}
				
				int dx = 0, dy = 0;
				for(int y=0; y<height-1; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width-1; dx++, x+=maxTile-1){
						
						//System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
						
						if(majTab[dy*dWidth + dx] == 1){ // la tuile doit être mise à jour
							
							//System.out.println("traitement de la tuile "+dx+" "+dy+" / "+dWidth+" "+dHeight);
							
							roiWidth = Math.min(maxTile, width - x);
							roiHeight = Math.min(maxTile, height - y);
							roiPosMinX = imageMinX + x*cellSize;
							roiPosMaxX = roiPosMinX + roiWidth * cellSize;
							roiPosMinY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
							roiPosMaxY = roiPosMinY + roiHeight * cellSize;
							
							cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
							
							outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
							cov.dispose(true);
							ImageUtilities.disposePlanarImageChain((PlanarImage) cov.getRenderedImage());
							cov = null;
							
							// vérification des mises à jour à faire
							p = new Pixel(dx, dy);
							if(calculTab[dy*dWidth+dx] == -1){ // la tuile n'a jamais été calculée
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx-1)] >= 0){ // si la tuile à gauche a été calculée
										bord = bords.get(p).get("left");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth+0] = bord[j]; 
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									if(calculTab[(dy-1)*dWidth+dx] >= 0){ // si la tuile au nord a été calculée
										bord = bords.get(p).get("north");
										for(int i=0; i<roiWidth; i++){
											outDatas[i] = bord[i];
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx+1)] >= 0){ // si la tuile à droite a été calculée
										bord = bords.get(p).get("right");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									if(calculTab[(dy+1)*dWidth+dx] >= 0){ // si la tuile au sud a été calculée
										bord = bords.get(p).get("south");
										for(int i=0; i<roiWidth; i++){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
										}
									}
								}
							}else{ // tuile a déjà été calculé
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									bord = bords.get(p).get("left");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth+0]){
											outDatas[j*roiWidth+0] = bord[j]; 
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									bord = bords.get(p).get("north");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[i]){
											outDatas[i] = bord[i];
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									bord = bords.get(p).get("right");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									bord = bords.get(p).get("south");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
										}
									}
								}
							}
								
							ccd = new TabChamferDistanceAnalysis(outDatas, null, roiWidth, roiHeight, cellSize, noDataValue, null, threshold);
							ccd.run();
							outDatas = (float[]) ccd.getResult();
								
							// maj des bords
							isFinish = bordUpdateFromData(dx, dy, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY, outDatas);
							if(!isFinish){
								finish = false;
							}
								
							// stockage en mémoire fichier de l'état de la tuile
							CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY);
								
							majTab[dy*dWidth+dx] = 0;
							calculTab[dy*dWidth+dx] = 1;
							
						}else{ // la tuile ne doit pas être mise à jour
							if(calculTab[dy*dWidth+dx] >= 0){
								calculTab[dy*dWidth+dx] = 0;
							}
						}
					}	
				}
			}
			pass++;
			
		}
		
		// normalisation
		normalisation();
		
		// compilation
		exportRaster();
	}

	/**
	 * 3 cas
	 * 1. la tuile n'a jamais été mise à jour, peut-être que les bords vont l'y obliger...
	 * 2. la tuile n'a pas été mis à jour (cette fois), la mise à jour des bords se fera sous condition de l'état des tuiles voisines
	 * 3. la tuile vient d'être mise à jour, attention au bords jamais mis à jour
	 */
	private boolean bordUpdateFromData(int dx, int dy, int roiWidth, int roiHeight, double roiPosMinX, double roiPosMaxX, double roiPosMinY, double roiPosMaxY, float[] outDatas){
		
		boolean finish = true;
		Pixel p = new Pixel(dx, dy);
		
		//System.out.println("traitement de la tuile "+x+" "+y);
		if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("left");
			for(int j=0; j<roiHeight; j++){
				if(bord[j] != outDatas[j * roiWidth + 0]){
					bord[j] = outDatas[j * roiWidth + 0];
					majTab[dy*dWidth+(dx-1)] = 1;
					finish = false;
				}
			}
		}
		
		if(dy > 0){ // le bord nord de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("north");
			for(int i=0; i<roiWidth; i++){
				if(bord[i] != outDatas[i]){
					bord[i] = outDatas[i];
					majTab[(dy-1)*dWidth+dx] = 1;
					finish = false;
				}
			}
		}
		
		if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("right");
			for(int j=0; j<roiHeight; j++){
				if(bord[j] != outDatas[j * roiWidth + (roiWidth-1)]){
					bord[j] = outDatas[j * roiWidth + (roiWidth-1)];
					majTab[dy*dWidth+(dx+1)] = 1;
					finish = false;
				}
			}
		}
		
		if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
			float[] bord = bords.get(p).get("south");
			for(int i=0; i<roiWidth; i++){
				if(bord[i] != outDatas[(roiHeight-1)*roiWidth + i]){
					bord[i] = outDatas[(roiHeight-1)*roiWidth + i];
					majTab[(dy+1)*dWidth+dx] = 1;
					finish = false;
				}
			}
		}
			
		return finish;
	}
	
	private void normalisation(){
		
		//System.out.println("normalisation");
		
		// normalisation
		int dx = 0, dy = 0;
		for(int y=0; y<height-1; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width-1; dx++, x+=maxTile-1){
						
				//System.out.println("traitement de la tuile "+dx+" "+dy);
						
				int roiWidth = Math.min(maxTile, width - x);
				int roiHeight = Math.min(maxTile, height - y);
				double roiPosX = imageMinX + x*cellSize;
				double roiPosMaxX = roiPosX + roiWidth * cellSize;
				double roiPosY = Math.max(imageMinY, imageMaxY - (y+roiHeight)*cellSize);
				double roiPosMaxY = roiPosY + roiHeight * cellSize;
						
				GridCoverage2D cov = CoverageManager.get(temp+"_"+dx+"-"+dy+".tif");
				float[] outDatas = CoverageManager.getData(cov, 0, 0, roiWidth, roiHeight);
				cov.dispose(true);
				PlanarImage planarImage = (PlanarImage) cov.getRenderedImage();
				ImageUtilities.disposePlanarImageChain(planarImage);
				cov = null;
						
				TabChamferDistanceAnalysis ccd = new TabChamferDistanceAnalysis(outDatas, null, roiWidth, roiHeight, cellSize, noDataValue, null, threshold);
				outDatas = ccd.normalize();
						
				// stockage en mémoire fichier de l'état de la tuile
				CoverageManager.writeGeotiff(new File(temp+"_"+dx+"-"+dy+".tif"), outDatas, roiWidth, roiHeight, roiPosX, roiPosMaxX, roiPosY, roiPosMaxY);
			}
		}
	}
	
	private void exportRaster() {
		
		//System.out.println("export raster");
		
		TileRasterDistanceOutput output = new TileGeoTiffDistanceOutput(folder, name, tile, width, height, maxTile, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize, noDataValue);
		
		output.init();
		
		Coverage localCoverage;
		float[] datas;
		EnteteRaster entete;
		int dx = 0, dy = 0;
		int localHeight = -1;
		for(int y=0; y<height-1; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width-1; dx++, x+=maxTile-1){
						
				//System.out.println("traitement de la tuile "+dx+" "+dy);
						
				localCoverage = CoverageManager.getCoverage(temp+"_"+dx+"-"+dy+".tif");
				datas = localCoverage.getData();
				entete = localCoverage.getEntete();
				localCoverage.dispose();
				
				localHeight = entete.height();
			
				output.post(x, y, entete.width(), entete.height(), datas);		
			}
			
			output.operate(y, localHeight);
			
		}
		
		output.close();
	}
	
	private void deleteTempFolder(){
		int dx = 0, dy = 0;
		for(int y=0; y<height-1; dy++, y+=maxTile-1){
			dx = 0;
			for(int x=0; x<width-1; dx++, x+=maxTile-1){
						
				new File(temp+"_"+dx+"-"+dy+".tif").delete();
					
			}
		}
	}
	
	@Override
	protected void doClose() {
		
		deleteTempFolder();
		
		if(inCoverage != null){
			//inCoverage.dispose();
			inCoverage = null;
		}
		
		bords.clear();
		bords = null;
		codes = null;
		calculTab = null;
		majTab = null;
	}

}