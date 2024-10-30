package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

import java.awt.Rectangle;
import java.util.Set;
import java.util.TreeSet;

import fr.inrae.act.bagap.apiland.raster.Coverage;

public class ScriptPaul {

	public static void main(String[] args) {
		/*
		Coverage cov = CoverageManager.getCoverage("E:/Projets/BiodivExpe/OS/");
		System.out.println(cov.getEntete());
		//cov.getData(new Rectangle(100000,100000,1000,1000));
		//cov.getData(new Rectangle(0, 0, 1000, 1000));
		cov.dispose();
		*/
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterTile("D:/temp/OS2/");
		//builder.setRasterTile("D:/temp/OS2/OS_fr_120_6730.tif");
		builder.addMetric("SHDI");
		builder.addWindowSize(201);
		builder.setUnfilters(new int[]{255});
		builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23"); 
		builder.setDisplacement(200);
		builder.addGeoTiffOutput("SHDI", "D:/temp/SHDI2.tif");
		LandscapeMetricAnalysis analysis = builder.build();
		analysis.allRun();
		
		
		/*
		String prop = "{1 ; 5; 14; 12}";
		prop = prop.replace("{", "").replace("}", "").replace(" ", "");
		String[] vs = prop.split(";");
		Set<Integer> setValues = new TreeSet<Integer>();
		for(String v : vs){
			setValues.add(Integer.parseInt(v));
		}
		
		StringBuilder sb = new StringBuilder();
		for(int sv : setValues) {
			sb.append(sv+",");
		}
		sb.deleteCharAt(sb.length()-1);
		
		System.out.println(sb.toString());
		//builder.setValues("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12");
		 
		 */
	}

}
