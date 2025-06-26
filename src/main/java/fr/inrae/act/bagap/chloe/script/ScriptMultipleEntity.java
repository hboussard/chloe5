package fr.inrae.act.bagap.chloe.script;

import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptMultipleEntity {

	public static void main(String[] args) {
		//analyseEntity();
		
		
		//rasterize();
		analyseEntityMaxence();
	}
	
	private static void rasterize(){
		
		Coverage cov = CoverageManager.getCoverage("E:/rennes_metropole/data/rm_os_bre.tif");	
		EnteteRaster entete1 = cov.getEntete();
		cov.dispose();
		System.out.println(entete1.crs());
		System.out.println(CRS.toSRS(entete1.crs()));
		System.out.println();
		
		String intputShapefile = "E:/rennes_metropole/data/MNIE22/MNIE22/MNIE-SIG/MNIE_HAB_PAYS_2022_L93-2.shp";
		CoordinateReferenceSystem crs = ShapeFile2CoverageConverter.getCoordinateReferenceSystem(intputShapefile);
		
		System.out.println(CRS.toSRS(crs));
		
		System.out.println(crs);
		EnteteRaster entete = EnteteRaster.getEntete(new Envelope(entete1.minx(), entete1.maxx(), entete1.miny(), entete1.maxy()), entete1.cellsize(), entete1.noDataValue(), crs);
		
		
		String outputRaster = "C:/Data/temp/entity/data/mnie2.tif";
		
		String attribute = "codeMNIE";
		int fillValue = -1;
		
		ShapeFile2CoverageConverter.rasterize(outputRaster, intputShapefile, attribute, fillValue, entete);
	}
	
	private static void analyseEntityMaxence(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.addRasterFile("E:/rennes_metropole/data/rm_os_bre.tif");
		builder.setEntityRasterFile("C:/Data/temp/entity/data/mnie.tif");
		builder.addMetric("N-valid");
		builder.addMetric("Nclass");
		builder.addMetric("pNV_1");
		builder.addMetric("pNV_2");
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.addMetric("pNV_5");
		builder.addMetric("pNV_6");
		builder.addMetric("pNV_7");
		builder.addMetric("pNV_8");
		builder.addMetric("SHDI");
		//builder.addMetric("NP");
		//builder.addMetric("MPS");
		builder.setGeoTiffOutputFolder("C:/Data/temp/entity/data/raster2/");
		builder.setCsvOutputFolder("C:/Data/temp/entity/data/csv2/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		//analysis.allRun();
	}
	
	private static void analyseEntity(){
		
		String path = "E:/FRC_AURA/data/grain2d/CVB/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.addRasterFile(path+"avant/avant_grain_bocager_5m_4classes.tif");
		builder.setEntityRasterFile(path+"communes.tif");
		builder.addMetric("Nclass");
		builder.addMetric("Majority");	
		builder.addMetric("NV_1");
		builder.addMetric("NV_2");
		builder.addMetric("NV_3");
		builder.addMetric("NV_4");
		builder.addMetric("pNV_1");
		builder.addMetric("pNV_2");
		builder.addMetric("pNV_3");
		builder.addMetric("pNV_4");
		builder.setGeoTiffOutputFolder(path+"avant/communes2/");
		builder.setCsvOutputFolder(path+"avant/communes2/");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
}
