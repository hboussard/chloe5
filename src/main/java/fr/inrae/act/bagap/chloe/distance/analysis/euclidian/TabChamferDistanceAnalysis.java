package fr.inrae.act.bagap.chloe.distance.analysis.euclidian;

import fr.inra.sad.bagap.apiland.analysis.Analysis;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;

public class TabChamferDistanceAnalysis extends Analysis {

	private final static int[][] chamfer13 = new int[][] { 
		new int[] { 1, 0, 68 }, 
		new int[] { 1, 1, 96 },
		new int[] { 2, 1, 152 }, 
		new int[] { 3, 1, 215 }, 
		new int[] { 3, 2, 245 }, 
		new int[] { 4, 1, 280 },
		new int[] { 4, 3, 340 }, 
		new int[] { 5, 1, 346 }, 
		new int[] { 6, 1, 413 } };

	private int[][] chamfer = null;

	private int normalizer = 0;

	private int width = 0, height = 0;

	private float[] outDatas, inDatas;
	
	private int[] codes;
	
	private float cellSize;
	
	private int noDataValue;
	
	private boolean hasValue;

	public TabChamferDistanceAnalysis(float[] outDatas, float[] inDatas, int width, int height, float cellSize, int noDataValue, int[] codes) {
		this.chamfer = TabChamferDistanceAnalysis.chamfer13;
		this.normalizer = this.chamfer[0][2];
		this.outDatas = outDatas;
		this.inDatas = inDatas;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.noDataValue = noDataValue;
		this.codes = codes;
	}
	
	public boolean hasValue(){
		return hasValue;
	}

	@Override
	protected void doRun() {
		setResult(compute());
	}

	private void testAndSet(int x, int y, float newvalue) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		double v = outDatas[y * width + x];
		if (v == Raster.getNoDataValue() || (v >= 0 && v < newvalue)) {
			return;
		}
		outDatas[y * width + x] = newvalue;
	}
	
	private float[] compute() {
		
		// forward
		for (int y = 0; y <= height - 1; y++) {
			for (int x = 0; x <= width - 1; x++) {
				float v = outDatas[y * width + x];
				if (v < 0) {
					continue;
				}
				for (int k = 0; k < chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];

					testAndSet(x + dx, y + dy, v + dt);
					if (dy != 0) {
						testAndSet(x - dx, y + dy, v + dt);
					}
					if (dx != dy) {
						testAndSet(x + dy, y + dx, v + dt);
						if (dy != 0) {
							testAndSet(x - dy, y + dx, v + dt);
						}
					}
				}
			}
		}

		// backward
		for (int y = height - 1; y >= 0; y--) {
			for (int x = width - 1; x >= 0; x--) {
				float v = outDatas[y * width + x];
				if (v < 0) {
					continue;
				}
				for (int k = 0; k < chamfer.length; k++) {
					int dx = chamfer[k][0];
					int dy = chamfer[k][1];
					int dt = chamfer[k][2];

					testAndSet(x - dx, y - dy, v + dt);
					if (dy != 0) {
						testAndSet(x + dx, y - dy, v + dt);
					}
					if (dx != dy) {
						testAndSet(x - dy, y - dx, v + dt);
						if (dy != 0)
							testAndSet(x + dy, y - dx, v + dt);
					}
				}
			}
		}
		
		return outDatas;
	}

	public float[] normalize(){
		float v;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				v = outDatas[y * width + x];
				if(v != Raster.getNoDataValue()){
					outDatas[y * width + x] = (v / normalizer) * cellSize;
				}
			}
		}
		return outDatas;
	}
	
	@Override
	protected void doInit() {
		hasValue = false;
		boolean ok;
		float v;
		for (int yt = 0; yt < height; yt++) {
			for (int xt = 0; xt < width; xt++) {
				v = inDatas[yt*width+xt];
				ok = false;
				if (v != Raster.getNoDataValue()) {
					for (int c : codes) {
						if (c == v) {
							ok = true;
							hasValue = true;
							break;
						}
					}
					if (ok) {
						outDatas[yt * width + xt] = 0; // inside the object -> distance=0
					} else {
						outDatas[yt * width + xt] = -2; // outside the object -> to be computed
					}
				}else{
					outDatas[yt * width + xt] = noDataValue; // nodata_value -> to be not computed
				}
				
			}
		}
		inDatas = null;
	}

	@Override
	protected void doClose() {
		setResult(normalize());
		//outDatas = null;
		inDatas = null;
	}
	
}
