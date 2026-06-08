package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.vector.GeoPackageTool;
import org.locationtech.jts.geom.Envelope;

public class ScriptCabart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		fusion();
	}

	private static void fusion() {

		String path = "C:/Data/temp/cabart/";
		String geoEnvelope = path+"Emprise_territoire_a_partir_de_5kmrayon.gpkg";
		Envelope env = GeoPackageTool.getEnvelope(geoEnvelope);
		
		Coverage cov22 = CoverageManager.getCoverage("D:/grain_bocager/data/22/2021/22_2021_grain_bocager_5m.tif");
		EnteteRaster entete22 = cov22.getEntete();
		float[] data = cov22.getData(EnteteRaster.getROI(entete22, env));
		cov22.dispose();
		/*
		Coverage cov35 = CoverageManager.getCoverage("D:/grain_bocager/data/35/2023/35_2023_grain_bocager_5m.tif");
		EnteteRaster entete35 = cov35.getEntete();
		float[] databis = cov35.getData(EnteteRaster.getROI(entete35, env));
		cov35.dispose();
		
		float v1, v2;
		for(int i=0; i<data.length; i++) {
			
			v2 = databis[i];

			if(v2 != entete22.noDataValue()) {
				
				v1 = data[i];
				
				if(v1 == entete22.noDataValue()) {
					
					data[i] = v2;
					
				}else if(v2 < v1) {
						
					data[i] = v2;
				}
			}
		}
		
		Coverage cov44 = CoverageManager.getCoverage("D:/grain_bocager/data/44/2022/44_2022_grain_bocager_5m.tif");
		EnteteRaster entete44 = cov44.getEntete();
		databis = cov44.getData(EnteteRaster.getROI(entete44, env));
		cov44.dispose();
		
		for(int i=0; i<data.length; i++) {

			v2 = databis[i];

			if(v2 != entete22.noDataValue()) {
				
				v1 = data[i];
				
				if(v1 == entete22.noDataValue()) {
					
					data[i] = v2;
					
				}else if(v2 < v1) {
						
					data[i] = v2;
				}
			}
		}
		
		Coverage cov56 = CoverageManager.getCoverage("D:/grain_bocager/data/56/2022/56_2022_grain_bocager_5m.tif");
		EnteteRaster entete56 = cov56.getEntete();
		databis = cov56.getData(EnteteRaster.getROI(entete56, env));
		cov56.dispose();
		
		for(int i=0; i<data.length; i++) {

			v2 = databis[i];

			if(v2 != entete22.noDataValue()) {
				
				v1 = data[i];
				
				if(v1 == entete22.noDataValue()) {
					
					data[i] = v2;
					
				}else if(v2 < v1) {
						
					data[i] = v2;
				}
			}
		}
		
		CoverageManager.write(path+"fusion_grain_bocager_5m.tif", data, EnteteRaster.getEntete(entete22, env));
		*/
	}

}
