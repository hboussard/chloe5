package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

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

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.GrainBocagerProcedure;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationBuilder;

public class ScriptDiagEABretagne {

	public static void main(String[] args) {
		
		test();
		//script();
		
	}
	
	private static void test() {

		String inputPath = "F:/FDCCA/diag_ea/data/analyse_bretagne/";
		String outputPath = "F:/FDCCA/diag_ea/data/analyse_bretagne/test/";
		
		GrainBocagerManager gbManager = new GrainBocagerManager("grain_bocager_calculation");
		gbManager.setBocage(inputPath+"hauteur_boisement_total_014033171_existant.tif");
		gbManager.setOutputFolder(outputPath);
		gbManager.setOuputPrefix("test");
		
		GrainBocagerProcedure gbProcedure = gbManager.build();
		
		gbProcedure.run();
	}
	
	private static void script() {

		//int max = 23738;
		int max = 100;
		
		//String[] codesExploitation = recuperationCodesExploitation();
		String[] codesExploitation = new String[]{"014033171"};
		System.out.println(codesExploitation.length);
		
		CsvWriter writer = DiagnosticGrainBocagerExploitation.prepareIndices("F:/FDCCA/diag_ea/data/analyse_bretagne/analyse_bretagne_test.csv");
		
		int index = 0;
		for(String codeExploitation : codesExploitation){
			System.out.println("diag EA sur exploitation "+codeExploitation);
			scriptExploitation(codeExploitation, 5, writer);
			if(++index >= max){
				break;
			}
		}
		
		DiagnosticGrainBocagerExploitation.fermeIndices(writer);
	}
	
 	private static String[] recuperationCodesExploitation(){
		String parcellaire = "G:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp";
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

	private static void scriptExploitation(String exploitation, int outCellSize, CsvWriter writer) {
		
		DiagnosticGrainBocagerExploitationBuilder builder = new DiagnosticGrainBocagerExploitationBuilder();
		
		builder.setOutputPath("F:/FDCCA/diag_ea/data/analyse_bretagne/");
		builder.setCsvWriter(writer);
		builder.setBocage("F:/FDCCA/diag_ea/data/mnhc/");
		builder.setParcellaire("F:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp");
		builder.setAttributCodeEA("pacage");
		builder.setZoneBocageExploitation("F:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp");
		builder.setBufferZoneBocageExploitation(10);
		builder.setSeuil(0.33);
		builder.setCodeEA(exploitation);
		builder.addScenario("existant");
		builder.setOutCellSize(outCellSize);
		DiagnosticGrainBocagerExploitation diagEA = builder.build();
		
		diagEA.run();
		
	}

}
