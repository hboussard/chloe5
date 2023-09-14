package fr.inrae.act.bagap.chloe.script;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptEcopaysagePaysDeLaLoire {

	public static void main(String[] args){
		
		rasterizeDepartement();
		
	}
	
	private static void rasterizeDepartement() {
		
		Coverage cov = CoverageManager.getCoverage("G:/data/sig/grand_ouest/GO_2021_ebr.tif");
		EnteteRaster refEntete = cov.getEntete();
		cov.dispose();
		
		System.out.println(refEntete.width()+" "+refEntete.height()+" "+(refEntete.width()*refEntete.height()));
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope("G:/FRC_Pays_de_la_Loire/data/departements/departements_pdl.shp", 5000);
		
		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		System.out.println(entete.width()+" "+entete.height()+" "+(entete.width()*entete.height()));
		
		Coverage covCommune = ShapeFile2CoverageConverter.getSurfaceCoverage("G:/data/sig/departements-20180101-shp/departements-20180101_L93.shp", "code_insee", entete, 1);
		
		//CoverageManager.writeGeotiff("G:/FRC_Pays_de_la_Loire/data/ecopaysage/departement.tif", covCommune.getDatas(), entete);
		
		//covCommune.dispose();
		
	}
	
}
