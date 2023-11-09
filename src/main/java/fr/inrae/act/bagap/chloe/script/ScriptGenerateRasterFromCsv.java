package fr.inrae.act.bagap.chloe.script;

import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptGenerateRasterFromCsv {

	public static void main(String[] args) {
		scriptGenerateRasterFromCsv();
	}
	
	private static void scriptGenerateRasterFromCsv(){
		String path = "C:/Hugues/data/data_ZA/PF_OS_L93/PF_2018/";

		String csv = path+"sliding_c5/sliding_pf_2018.csv";
		
		String variable = "HET_31";
		
		EnteteRaster entete = EnteteRaster.readFromAsciiGridHeader(path+"sliding_c5/sliding_pf_2018_header.txt");
		
		float[] data = new float[entete.width()*entete.height()];
		
		SpatialCsvManager.exportTab(data, csv, variable, entete);
		
		CoverageManager.write(path+"csv/csv_HET_31_2018_10m.asc", data, entete);	
	}
}
