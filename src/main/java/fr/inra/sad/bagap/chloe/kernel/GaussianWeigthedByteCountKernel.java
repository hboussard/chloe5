package fr.inra.sad.bagap.chloe.kernel;

import com.aparapi.Kernel;

public class GaussianWeigthedByteCountKernel extends Kernel {

	private final int width, height;
	
	private final int dep;

	private final byte imageIn[];
	
	private final float imageOut[][];
	
	private final byte noCalc[];
	
	private final int rayon;
	
	private final int nValues;
	
	private int theY;

	private final float[][] buf;
	private final float[] gauss;
	
	private final int xmin,xmax,ymin,ymax; // in pixels
	
	public GaussianWeigthedByteCountKernel(int nValues, int windowSize, int width, int height, int dep, byte[] imageIn, float[][] imageOut, int noDataValue){
		this(nValues, windowSize, width, height, dep, imageIn, imageOut, noDataValue, 0);
	}
		
	
	public GaussianWeigthedByteCountKernel(int nValues, int windowSize, int width, int height, int dep, byte[] imageIn, float[][] imageOut, int noDataValue, int enveloppeInterne){
		this(nValues, windowSize, width, height, dep, imageIn, imageOut, noDataValue, enveloppeInterne, width - enveloppeInterne, enveloppeInterne, height - enveloppeInterne, null);		
	}
	public GaussianWeigthedByteCountKernel(int nValues, int windowSize, int width, int height, int dep, byte[] imageIn, float[][] imageOut, int noDataValue, int xmin,int xmax, int ymin, int ymax, byte[] noCalc ){
		this.setExplicit(true);
		this.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.JTP);
		this.nValues = nValues;
		this.rayon = windowSize/2 + 1;
		this.width = width;
		this.height = height;
		this.dep = dep;
		this.imageIn = imageIn;
		this.imageOut = imageOut;
		this.noCalc = noCalc;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.buf = new float[width][nValues];
		this.gauss = new float[rayon];
		
		float r=(rayon-1)/2.f;
		for(int i=0;i<rayon;i++) {
			float d = i/r; // distance en nombre de rayons
			gauss[i]=(float) Math.exp(-d*d);
			//gauss[i]=1;
		}
	}

	public void processVerticalPixel(int x,int y) {
		// parcours vertical centré sur (x,y) dans imageIn et stocké dans buf
		for(int i=0;i<nValues;i++)
			buf[x][i]=0;
		
		for (int dy = max(-rayon+1,-y); dy < min(rayon,height-y); dy++) {
				int v = (int)imageIn[(y + dy) * width + x];
				buf[x][v]++;	
		}
	}

	public void processHorizontalPixel(int x_out,int y_out) {
		// parcours horizontal de buf: position dans imageOut : (x_in/dep,y_out)
		int x_in = x_out*dep;
		int ind = y_out*((width-1)/dep+1) + x_out;
		
		for(int value=0; value<nValues;value++) {
			float val=0;
			for(int i=max(x_in-rayon+1,0);i<min(x_in+rayon,width);i++)
				val+=buf[i][value];
			imageOut[ind][value] = val;
		}
	}


	public void applySlidingWindow(int theY, short buffer) {
		this.theY = theY; // ligne dans 
		int nlines = (buffer - ((dep-theY%dep)%dep) )/dep;
		execute(width,2*nlines);
	}

	@Override
	public void run() {
		int x = getGlobalId(0); // x dans imageIn
		int pass = getPassId();
		int vertical = pass%2;
		int line= (dep-theY%dep)%dep + pass/2; // ligne dans imageOut
		int y = theY + line*dep; // y dans imageIn
		if(y<ymin || y>=ymax)
			return;
		if(vertical == 0)
			processVerticalPixel(x,y);
		else {
			if(x>=xmin/dep && x < (xmax-1)/dep+1 ) {
				byte val = imageIn[y+x*width];
				if(noCalc!=null)
					for(byte v:noCalc)
						if(v==val)
							return;
				processHorizontalPixel(x,line);
			}
		}
	}
}
