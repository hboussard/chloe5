package fr.inra.sad.bagap.chloe.dreal;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.chloe.metric.Metric;
import fr.inrae.act.bagap.chloe.counting.ValueCounting;
import fr.inrae.act.bagap.chloe.kernel.DistanceWeigthedCountValueKernel;
import fr.inrae.act.bagap.chloe.metric.value.CountValueMetric;
import fr.inrae.act.bagap.chloe.output.AsciiGridOutput;
import fr.inrae.act.bagap.chloe.output.CsvOutput;

public class Bretagne {

	public static void main(final String[] args) {
		try {
			System.out.println("sliding window");
			
			short windowSize = 1201;
			short mid = (short) (windowSize/2);
			int initRoiWidth = 12001;
			int initRoiHeight = 12001;
			System.out.println(initRoiWidth*initRoiHeight);
			int roiWidth;
			int roiHeight;
			int roiX;
			int roiY;
			short dep = 200;
			short buffer = 500;
			
			buffer = (short) Math.max(dep, buffer);
			
			File file = new File("C:/Users/hboussard/data/bretagne.tif");
			GeoTiffReader reader = new GeoTiffReader(file);
			//System.out.println(reader.getCoordinateReferenceSystem());
			GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
			
			int imageWidth = (Integer) coverage.getProperty("image_width");
			int imageHeight = (Integer) coverage.getProperty("image_height");
			double imageMinX = coverage.getEnvelope().getMinimum(0);
			double imageMinY = coverage.getEnvelope().getMinimum(1);
			double imageMaxY = coverage.getEnvelope().getMaximum(1);
			double cellSize = ((java.awt.geom.AffineTransform) coverage.getGridGeometry().getGridToCRS2D()).getScaleX();
			
			System.out.println("cellsize : "+cellSize);
			System.out.println(coverage.getEnvelope());
			System.out.println(coverage.getEnvelope().getMinimum(0)+" "+coverage.getEnvelope().getMaximum(0)+" "+coverage.getEnvelope().getMinimum(1)+" "+coverage.getEnvelope().getMaximum(1));
			System.out.println(coverage.getProperties());
			
			/*
			int numTile = 1;
			for(roiY=0; roiY<imageHeight; roiY+=initRoiHeight-windowSize) {
				roiHeight = Math.min(initRoiHeight, imageHeight-roiY);
				for(roiX=0; roiX<imageWidth; roiX+=initRoiWidth-windowSize) {
					
					roiWidth = Math.min(initRoiWidth, imageWidth-roiX);
					System.out.println(roiX+" "+roiY+" "+roiWidth+" "+roiHeight);
					
					short outWidth = (short) (((roiWidth-1)/dep)+1);
					short outHeight = (short) (((roiHeight-1)/dep)+1);
					double outCellSize = cellSize * dep;
					double outMinX = imageMinX + roiX*cellSize;
					double outMaxX = outMinX + outWidth*cellSize;
					double outMinY = imageMinY + (imageHeight-roiY)*cellSize - outHeight*outCellSize;
					double outMaxY = imageMaxY - roiY*cellSize;
					
					Rectangle roi = new Rectangle(roiX, roiY, roiWidth, roiHeight);
					
					float[] inDatas = new float[roiWidth*roiHeight];
					inDatas = coverage.getRenderedImage().getData(roi).getSamples(roi.x, roi.y, roi.width, roi.height, 0, inDatas);
					//System.out.println(pi.getMinX()+" "+pi.getMaxX()+" "+pi.getMinTileX()+" "+pi.getMaxTileX());
						
					
					Set<Short> inValues = new TreeSet<Short>();
					for(float s : inDatas){
						if(s!=Raster.getNoDataValue() && s!=0 && !inValues.contains((short) s)){
							inValues.add((short) s);
						}
					}
					System.out.println(inValues);
					short[] values = new short[inValues.size()];
					int index = 0;
					for(Short s : inValues){
						values[index++] = (short) s;
					}
					
					float[][] outDatas = new float[((((roiWidth-1)/dep)+1)*(((buffer-1)/dep)+1))][values.length+2];
								
					short[] shape = new short[windowSize*windowSize];
					float[] coeffs = new float[windowSize*windowSize];
					
					int theoriticalSize = 0;
					
					for(short j=0; j<windowSize; j++){
						for(short i=0; i<windowSize; i++){
							
							// gestion en cercle
							if(distance(mid, mid, i, j) <= mid){
								shape[(j * windowSize) + i] = 1;
								theoriticalSize++; 
								//System.out.print(1+" ");
							}else{
								shape[(j * windowSize) + i] = 0;
								//System.out.print(0+" ");
							}
							
							// gestion des distances pondérées (décroissantes)
							float d = mid - distance(mid, mid, i, j);
							if(d < 0){
								d = 0;
							}
							coeffs[(j * windowSize) + i] = (float) d / mid;
							//System.out.println((float) d / mid);
						}
						//System.out.println();
					}
					
					DistanceWeigthedCountValueKernel cv = new DistanceWeigthedCountValueKernel(values, windowSize, shape, coeffs, roiWidth, roiHeight, dep, inDatas, outDatas);
					
					ValueCounting vc = new ValueCounting(values, theoriticalSize);
					
					Metric metric;
					for(short v : values){
						metric = new CountValueMetric(v);
						vc.addMetric(metric);
					}
					
					CsvOutput csvOut = new CsvOutput("C:/Users/hboussard/modelisation/chloe/chloe5/data/output/bretagne_3km_"+(numTile++)+".csv", outMinX, outMaxX, outMinY, outMaxY, outWidth, outHeight, cellSize, (short) Raster.getNoDataValue(), vc.metrics());
					vc.addObserver(csvOut);
					
					vc.init();
					
					int nextJ = 0;
					long begin = System.currentTimeMillis();
					for(int b=0; b<roiHeight; b+=buffer){
						System.out.println(b);
						cv.applySlidingWindow(b, (short) Math.min(buffer, (roiHeight-b)));
						cv.get(outDatas);
						
						index = 0;
						for(int j=nextJ%buffer; j<Math.min(buffer, roiHeight-b); j+=dep){
							
							nextJ += dep;
							for(int i=0; i<roiWidth; i+=dep){
								//System.out.println(i+" "+j);
								vc.setCounts(outDatas[index]);
								vc.calculate();
								vc.export();
								index++;
							}
						}	
					}
					
					long end = System.currentTimeMillis();
					System.out.println("time computing : "+(end - begin));
					
					cv.dispose();
					vc.close();
					
				}
			}
			*/
			
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static float distance(int x1, int y1, int x2, int y2){
		return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); 
	}

}
