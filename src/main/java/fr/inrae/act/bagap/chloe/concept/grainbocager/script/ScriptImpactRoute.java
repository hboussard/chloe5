package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.IOException;
import java.util.Arrays;

import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.RasterPolygon;
import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;

public class ScriptImpactRoute {

	public static void main(String[] args) {
		
		//testRasterPolygon();
		impactRoute();
	}
	
	private static void testRasterPolygon() {
		
		Coverage cov = CoverageManager.getCoverage("C:/Data/projet/grain_bocager/formation/2026-06-26/exo2/test_grain_bocager_5m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		try{
			
			ShpFiles sf = new ShpFiles("C:/Data/projet/grain_bocager/formation/2026-06-26/couche_tampon_louise/tampon_route_30_test.shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			
			Geometry the_geom;
			Polygon the_poly;
			RasterPolygon rp;
			int index = 0;
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				if(the_geom != null) {
					
					if(entete.getEnvelope().intersects(the_geom.getEnvelopeInternal())){
						if(the_geom instanceof Polygon){
							the_poly = (Polygon) the_geom;
							
							rp = RasterPolygon.getRasterPolygon(the_poly, entete);
							
							if(rp != null) {
								
								float[] datas = rp.getDatas();
								
								if(datas.length == entete.size()) {
									CoverageManager.write("C:/Data/projet/grain_bocager/formation/2026-06-26/effet_route/test_"+(index++)+".tif", datas, entete);
								}
								
							}
							
						}else if(the_geom instanceof MultiPolygon){
							
							for(int i=0; i<the_geom.getNumGeometries(); i++){
								the_poly = (Polygon) ((MultiPolygon) the_geom).getGeometryN(i);
								
								rp = RasterPolygon.getRasterPolygon(the_poly, entete);
								
								if(rp != null) {
									
									float[] datas = rp.getDatas();
									
									if(datas.length == entete.size()) {
										CoverageManager.write("C:/Data/projet/grain_bocager/formation/2026-06-26/effet_route/test_"+(index++)+".tif", datas, entete);
									}
									
								}
							}
							
						}else{
							System.out.println(the_geom);
							//throw new IllegalArgumentException("probleme geometrique");
						}
					}
				}
			}
			
			sfr.close();
			sf.dispose();
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private static void impactRoute() {
		
		Envelope envelope = ShapeFileTool.getEnvelope("D:/grain_bocager/formation/2006-06-26/ressources/ressources/RAFC/limites_RAFC.shp");
		String env = "{"+envelope.getMinX()+";"+envelope.getMaxX()+";"+envelope.getMinY()+";"+envelope.getMaxY()+"}";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setBocage("D:/grain_bocager/formation/2006-06-26/ressources/ressources/grain_bocager/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D035_2023-01-01/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D035_2023-01-01/hauteur_boisement_35_2023.tif");

		gbManager.setEnvelope(env);
		gbManager.setWoodRemoval("C:/Data/projet/grain_bocager/formation/2026-06-26/couche_tampon_louise/tampon_route_30.shp");
		gbManager.setOutputFolder("C:/Data/projet/grain_bocager/formation/2026-06-26/effet_route/");
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}

}
