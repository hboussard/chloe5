package fr.inrae.act.bagap.chloe.distance.analysis.functional;

import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.util.ImageUtilities;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inrae.act.bagap.chloe.distance.output.TileGeoTiffDistanceOutput;
import fr.inrae.act.bagap.chloe.distance.output.TileRasterDistanceOutput;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;

public class HugeRCMDistanceAnalysis extends Analysis {

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
	
	private Coverage inCoverage, frictionCoverage;
	
	private Tile tile;
	
	/*
	public static void main(String[] args){
		
		//String input = "F:/chloe/chloe5/data/za.tif";
		//String inputFriction = "F:/chloe/chloe5/data/distance/permeabilite_pf.tif";
		//String output = "F:/chloe/chloe5/data/distance/continuites_pf.asc";
		//Set<Integer> c = new HashSet<Integer>();
		//c.add(4);
		//c.add(5);
		
		//String input = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/habitat_prairial.tif";
		//String inputFriction = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/permeabilite_prairiale.tif";
		//String output = "F:/coterra/data/Coterra_2019_DNSB_erb/continuites_prairie/continuites_prairiales.asc";
		//Set<Integer> c = new HashSet<Integer>();
		//c.add(1);
		
		String input = "F:/chloe/debug/mallet/Habitat_et_permeabilite/Habitat_haie_sup_0_mas_clean.asc";
		String inputFriction = "F:/chloe/debug/mallet/Habitat_et_permeabilite/Permeabilite_grand_rhino_bis.asc";
		String output = "F:/chloe/debug/mallet/Habitat_et_permeabilite/continuites_bis.asc";
		Set<Integer> c = new HashSet<Integer>();
		c.add(1);
		
		
		Raster.setNoDataValue(-1);
		new HugeRCMDistanceAnalysis(input, inputFriction, output, c).allRun();
	}*/
	
	public HugeRCMDistanceAnalysis(Coverage inCoverage, Coverage frictionCoverage, Tile tile, String output, String name, Collection<Integer> codes, float threshold) {
		this.inCoverage = inCoverage;
		this.frictionCoverage = frictionCoverage;
		this.folder = output;
		this.name = name;
		
		this.tile = tile;
		
		this.temp = output+"euclidian/";
		Util.createAccess(temp);
		this.temp += "name";
		
		
		
		this.codes = new int[codes.size()];
		int index = 0;
		for(int i : codes) {
			this.codes[index++] = i;
		}
		this.threshold = threshold;
	}
	
	public HugeRCMDistanceAnalysis(Coverage inCoverage, Coverage frictionCoverage, Tile tile, String output, String name, Collection<Integer> codes) {
		this(inCoverage, frictionCoverage, tile, output, name, codes, Raster.getNoDataValue());
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
		
		boolean finish = false, hasValue, ok, isFinish, maj;
		int pass = 1;
		float[] inDatas, frictionDatas, outDatas, bord;
		int roiWidth, roiHeight;
		double roiPosMinX, roiPosMaxX, roiPosMinY, roiPosMaxY;
		float v;
		TabRCMDistanceAnalysis rcm;
		GridCoverage2D cov;
		Pixel p;
		while(!finish){
			finish = true;
			//System.out.println("pass "+pass);
			if(pass == 1){ // premiere passe
				
				int dx = 0;
				int dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
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
						
						// recuperation des valeurs de friction
						frictionDatas = frictionCoverage.getData(new Rectangle(x, y, roiWidth, roiHeight));
						
						// analyse de distance
						rcm = new TabRCMDistanceAnalysis(outDatas, inDatas, frictionDatas, roiWidth, roiHeight, cellSize, noDataValue, codes);
						
						rcm.init();
						inDatas = null;
						
						if(rcm.hasValue()){
							
							rcm.run();
							outDatas = (float[]) rcm.getResult();
							
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
				// finish = true;
			}else{
				
				if(inCoverage != null){
					inCoverage.dispose();
					inCoverage = null;
				}
				
				int dx = 0, dy = 0;
				for(int y=0; y<height; dy++, y+=maxTile-1){
					dx = 0;
					for(int x=0; x<width; dx++, x+=maxTile-1){
						
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
							
							// récupération des valeurs de friction
							frictionDatas = frictionCoverage.getData(new Rectangle(x, y, roiWidth, roiHeight));
							
							rcm = new TabRCMDistanceAnalysis(outDatas, null, frictionDatas, roiWidth, roiHeight, cellSize, noDataValue, codes);
							
							// vérification des mises à jour à faire
							p = new Pixel(dx, dy);
							if(calculTab[dy*dWidth+dx] == -1){ // la tuile n'a jamais été calculée
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx-1)] >= 0){ // si la tuile à gauche a été calculée
										bord = bords.get(p).get("left");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth+0] = bord[j]; 
											rcm.setPixelAndValue(j*roiWidth+0, bord[j]);
											//rcm.setPixelAndValue(new Pixel(0, j), bord[j]);
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									if(calculTab[(dy-1)*dWidth+dx] >= 0){ // si la tuile au nord a été calculée
										bord = bords.get(p).get("north");
										for(int i=0; i<roiWidth; i++){
											outDatas[i] = bord[i];
											rcm.setPixelAndValue(i, bord[i]);
											//rcm.setPixelAndValue(new Pixel(i, 0), bord[i]);
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									if(calculTab[dy*dWidth+(dx+1)] >= 0){ // si la tuile à droite a été calculée
										bord = bords.get(p).get("right");
										for(int j=0; j<roiHeight; j++){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
											rcm.setPixelAndValue(j*roiWidth + (roiWidth-1), bord[j]);
											//rcm.setPixelAndValue(new Pixel(roiWidth-1, j), bord[j]);
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									if(calculTab[(dy+1)*dWidth+dx] >= 0){ // si la tuile au sud a été calculée
										bord = bords.get(p).get("south");
										for(int i=0; i<roiWidth; i++){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
											rcm.setPixelAndValue((roiHeight-1)*roiWidth + i, bord[i]);
											//rcm.setPixelAndValue(new Pixel(i, roiHeight-1), bord[i]);
										}
									}
								}
							}else{ // tuile a déjà été calculé
								
								if(dx > 0){ // le bord gauche de la tuile est inclu et partagé
									bord = bords.get(p).get("left");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth+0]){
											outDatas[j*roiWidth+0] = bord[j]; 
											rcm.setPixelAndValue(j*roiWidth+0, bord[j]);
											//rcm.setPixelAndValue(new Pixel(0, j), bord[j]);
										}
									}
								}
								if(dy > 0){ // le bord nord de la tuile est inclu et partagé
									bord = bords.get(p).get("north");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[i]){
											outDatas[i] = bord[i];
											rcm.setPixelAndValue(i, bord[i]);
											//rcm.setPixelAndValue(new Pixel(i, 0), bord[i]);
										}
									}
								}
								if(dx < (dWidth-1)){ // le bord droite de la tuile est inclu et partagé
									bord = bords.get(p).get("right");
									for(int j=0; j<roiHeight; j++){
										if(bord[j] < outDatas[j*roiWidth + (roiWidth-1)]){
											outDatas[j*roiWidth + (roiWidth-1)] = bord[j];
											rcm.setPixelAndValue(j*roiWidth + (roiWidth-1), bord[j]);
											//rcm.setPixelAndValue(new Pixel(roiWidth-1, j), bord[j]);
										}
									}
								}
								if(dy < (dHeight-1)){ // le bord sud de la tuile est inclu et partagé
									bord = bords.get(p).get("south");
									for(int i=0; i<roiWidth; i++){
										if(bord[i] < outDatas[(roiHeight-1)*roiWidth + i]){
											outDatas[(roiHeight-1)*roiWidth + i] = bord[i];
											rcm.setPixelAndValue((roiHeight-1)*roiWidth + i, bord[i]);
											//rcm.setPixelAndValue(new Pixel(i, roiHeight-1), bord[i]);
										}
									}
								}
							}
								
							rcm.run();
							outDatas = (float[]) rcm.getResult();
								
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
		
		// compilation
		exportRaster();
	}
	
	/*
	private void setPixelAndValue(Map<Float, Set<Pixel>> waits, Pixel pixel, float value) {
		if (!waits.containsKey(value)) {
			waits.put(value, new HashSet<Pixel>());
		}
		waits.get(value).add(pixel);
	}*/

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
	
	private void exportRaster() {
		
		System.out.println("export raster");
		
		TileRasterDistanceOutput output = new TileGeoTiffDistanceOutput(folder, name, tile, width, height, maxTile, imageMinX, imageMaxX, imageMinY, imageMaxY, cellSize, Raster.getNoDataValue());
		
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
		if(frictionCoverage != null){
			//frictionCoverage.dispose();
			frictionCoverage = null;
		}
		
		bords.clear();
		bords = null;
		codes = null;
		calculTab = null;
		majTab = null;
	}

	
}
