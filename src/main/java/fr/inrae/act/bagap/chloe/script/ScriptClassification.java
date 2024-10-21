package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.apiland.analysis.tab.ClassificationPixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.domain.Domain;
import fr.inrae.act.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptClassification {

	public static void main(String[] args) {
		scriptClassification();
	}
	
	private static void scriptClassification(){
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";

		Coverage cov1 = CoverageManager.getCoverage(path+"pf_2018_10m.tif");
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		float[] data = new float[data1.length];
		
		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		domains.put(DomainFactory.getFloatDomain("[2,3]"), 1);
		domains.put(DomainFactory.getFloatDomain("[4,9]"), 2);
		domains.put(DomainFactory.getFloatDomain("]9,]"), 3);
		
		ClassificationPixel2PixelTabCalculation cal = new ClassificationPixel2PixelTabCalculation(data, data1, entete.noDataValue(), domains);
		cal.run();
		
		CoverageManager.write(path+"classif_1/classif_2018_10m_1.tif", data, entete);	
	}
	
}
