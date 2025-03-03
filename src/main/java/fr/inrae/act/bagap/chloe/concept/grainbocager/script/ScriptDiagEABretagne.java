package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.jumpmind.symmetric.csv.CsvWriter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationManager;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;

public class ScriptDiagEABretagne {

	public static void main(String[] args) {
		//cleanMNHC();
		//scriptExploitationsBretagne();
		scriptExploitation("056046330", 5, null, true);
	}
	
	private static void scriptExploitationsBretagne() {
		
		//String[] codesExploitation = recuperationCodesExploitation();
		//String[] codesExploitation = new String[]{"035177652", "035179895"};
		String[] codesExploitation = new String[]{"056047987"};

		boolean exportMap = true;
		
		CsvWriter writer = DiagnosticGrainBocagerExploitation.prepareIndices("F:/FDCCA/diag_ea/data/analyse_bretagne/analyse_bretagne_ea.csv");
		
		int nb = 0;
		for(String codeExploitation : codesExploitation){
			System.out.println("diagnostique sur exploitation "+codeExploitation);
			if(scriptExploitation(codeExploitation, 5, writer, exportMap)){
				nb++;
			}
		}
		System.out.println("nombre d'EA analysees = "+nb+" / "+codesExploitation.length);
		DiagnosticGrainBocagerExploitation.fermeIndices(writer);
	}
	
	private static boolean scriptExploitation(String exploitation, int outCellSize, CsvWriter writer, boolean exportMap) {
		
		DiagnosticGrainBocagerExploitationManager manager = new DiagnosticGrainBocagerExploitationManager();
		
		manager.setOutputFolder("F:/FDCCA/diag_ea/data/analyse_bretagne/");
		if(writer != null) {
			manager.setWriter(writer);
		}
	
		// definition du territoire d'exploiattion
		//manager.setParcellaire("F:/data/sig/RPG/rpg-2020/surfaces_2020_toutes_parcelles_graphiques_constatees_exploitants_FRCB.shp");
		manager.setParcellaire("F:/FDCCA/diag_ea/data/analyse_bretagne/056046330/056046330.shp"); // parcellaire="chemin/vers/shape.shp"
		manager.setAttributCodeEA("pacage"); // attribut_code_ea="pacage"
		manager.setCodeEA(exploitation); // code_ea="05376853"
		
		// definition du bocage
		manager.setBocage("F:/FDCCA/diag_ea/data/mnhc/"); // bocage="chemin/vers/bocage.tif"
		
		//manager.setZoneBocageExploitation("F:/data/sig/RPG/rpg-2020/surfaces_2020_toutes_parcelles_graphiques_constatees_exploitants_FRCB.shp");
		//manager.setBufferZoneBocageExploitation(10);
		
		
		//manager.addScenario("initial");
		//manager.setAmenagement("F:/FDCCA/diag_ea/data/analyse_bretagne/056047987/amenagement_056047987.shp");
		manager.setAmenagement("F:/FDCCA/diag_ea/data/analyse_bretagne/056046330/amenagement.shp"); // managment="chemin/vers/shape.shp"
		//manager.addScenario("scenario0");
		//manager.addScenario("scenario1");
		//manager.addScenario("scenario2");
		//manager.addScenario("scenario3");
		manager.setScenario("hauteur"); // scenario_managment=""scenario1"
		
		manager.setGrainBocagerCellSize(outCellSize);
		manager.setExportMap(exportMap);
		DiagnosticGrainBocagerExploitation diagEA = manager.build();
		
		return diagEA.run();
		
	}
	
	private static void cleanMNHC() {
		
		File f = new File("F:/FDCCA/diag_ea/data/mnhc/");
		Coverage cov;
		float[] data;
		EnteteRaster entete;
		for(File file : f.listFiles()) {
			
			cov = CoverageManager.getCoverage(file.getAbsolutePath());
			data = cov.getData();
			entete = cov.getEntete();
			cov.dispose();
			
			entete = new EnteteRaster(entete.width(), entete.height(), Math.round(entete.minx()), Math.round(entete.maxx()), Math.round(entete.miny()), Math.round(entete.maxy()), entete.cellsize(), entete.noDataValue(), entete.crs());
			
			CoverageManager.write(file.getAbsolutePath(), data, entete);
		}
	}
	
 	private static String[] recuperationCodesExploitation(){
		String parcellaire = "F:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp";
		String attributeCodeEA = "pacage";
		
		try{
			ShpFiles sf = new ShpFiles(parcellaire);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader header = dfr.getHeader();
			int ideaNumber = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributeCodeEA)){
					ideaNumber = i;
				}
			}
			if(ideaNumber == -1){
				throw new IllegalArgumentException("l'attribut '"+attributeCodeEA+"' n'existe pas.");
			}
		
			Set<String> codesExploitation = new TreeSet<String>();
			
			Geometry the_geom;
			String code;
			Object[] entry;
			while(sfr.hasNext()){
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				entry = dfr.readEntry();
				code = (String) entry[ideaNumber];
				codesExploitation.add(code);
			}
			
			sfr.close();
			dfr.close();
			
			return codesExploitation.toArray(new String[codesExploitation.size()]);
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
