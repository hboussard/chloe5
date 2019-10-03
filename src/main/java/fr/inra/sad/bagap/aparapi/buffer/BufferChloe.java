package fr.inra.sad.bagap.aparapi.buffer;

import java.io.IOException;

import fr.inra.sad.bagap.apiland.analysis.matrix.output.AsciiGridOutput;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.WindowMatrixProcessType;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value.CountValueMetric;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.qualitative.value.LandscapeGrain4Index;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysis;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.WindowMatrixAnalysisBuilder;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.CircleWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.SquareWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.CenteredWindow;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.type.Window;
import fr.inra.sad.bagap.apiland.analysis.window.WindowAnalysisType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class BufferChloe {

	private static String path = "C:/Hugues/temp/"; 
	
	public static void main(String[] args){
		//retile();
		calcul(101);
	}
	
	private static void retile(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld("C:/Hugues/modelisation/dreal/emprise_bretagne/data/bretagne.asc", false);
			Matrix m2 = MatrixManager.retile(m, 10000, 10000, 1000, 1000);
			MatrixManager.exportAsciiGrid(m2, path+"test_bretagne.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void calcul(int size) {
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"test_bretagne.asc", false);
			WindowMatrixProcessType pt = new WindowMatrixProcessType(m); 
			pt.addMetric(new CountValueMetric(11));
			Window w = new CenteredWindow(new SquareWindow(size));
			WindowMatrixAnalysisBuilder builder = new WindowMatrixAnalysisBuilder(WindowAnalysisType.SLIDING);
			builder.addMatrix(m);
			builder.setWindow(w);
			builder.setProcessType(pt);
			//builder.setDisplacement(10);
			builder.addObserver(new AsciiGridOutput("NV_11", path+"NV_11.asc", 1));
			WindowMatrixAnalysis wa = builder.build();
			
			long begin = System.currentTimeMillis();
			wa.allRun();
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	
}
