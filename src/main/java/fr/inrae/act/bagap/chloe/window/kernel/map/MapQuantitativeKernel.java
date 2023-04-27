package fr.inrae.act.bagap.chloe.window.kernel.map;

public class MapQuantitativeKernel extends MapLandscapeMetricKernel {

	public MapQuantitativeKernel(int noDataValue) {
		super(noDataValue);
	}

	@Override
	public void applyMapWindow(int theY) {
			
		short v;
		float nb_nodata = 0;
		float nb = 0;
		float sum = 0;
		double square_sum = 0;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;			
		for(int y=0; y<height(); y++) {
			for(int x=0; x<width(); x++) {
					
				v = (short) inDatas()[((theY+y)*width()) + x];	
				nb += 1;	
				if(v == noDataValue()) {
					nb_nodata += 1;
				}else{
					sum += v;
					square_sum += v * v;
					min = Math.min(min, v);
					max = Math.max(max, v);
				}
			}
		}
		
		outDatas()[2] += nb;
		outDatas()[3] += nb_nodata;
		outDatas()[4] += sum;
		outDatas()[5] += square_sum;
		outDatas()[6] += min;
		outDatas()[7] += max;
	}
	
	

}
