package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.geotools.data.shapefile.dbf.DbaseFileException;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.jumpmind.symmetric.csv.CsvWriter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inrae.act.bagap.apiland.core.composition.AttributeType;
import fr.inrae.act.bagap.apiland.core.element.DynamicFeature;
import fr.inrae.act.bagap.apiland.core.element.DynamicLayer;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicElementType;
import fr.inrae.act.bagap.apiland.core.space.Linear;
import fr.inrae.act.bagap.apiland.core.space.Local;
import fr.inrae.act.bagap.apiland.core.space.Surfacic;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationManager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.util.CompileMNHC;

public class ScriptSecteurParcellaire {

	//private static String parcellaire = "F:/data/sig/RPG/rpg-2020/surfaces_2020_toutes_parcelles_graphiques_constatees_exploitants_FRCB.shp";
	private static String parcellaire = "C:/Data/data/rpg/bretagne_2023/parcelles_geom_2023_r53.shp";
	private static String parcellaireSecteur = "C:/Data/data/rpg/bretagne_2023/parcelles_geom_2023_r53_secteurs";
	
	private static String eaCode = "pacage";
	
	private static int distanceMaximale = 1000; // en metres
	
	public static void main(String[] args) {
		
		//compileMNHC();
		
		//String eaCodeValue = "022068049";
		
		//detecteSecteursExploitation(eaCodeValue);
	
		//scriptExploitationsBretagne();
	
		//Coverage coverageBocage = CoverageManager.getCoverage("C:/Data/projet/grain_bocager/data/mnhc_france_2021-2023/");
		//coverageBocage.dispose();
		
		//generateParcellaireSecteur(parcellaire, eaCode, parcellaireSecteur);
		
		//jointure();
		
		//rasterize("si");
		//rasterize("txi_bois");
		//rasterize("txe_bois");
		//rasterize("delta_gb");
		//rasterize("d_s_gb");
		//rasterize("tx_d_s_gb");
		
		
	}

	private static void jointure() {
		
		ShapeFileTool.jointurePolygon("C:/Data/temp/grain_bocager/secteurs/bretagne_2023/shape/rpg_2023", parcellaire, eaCode, "C:/Data/temp/grain_bocager/secteurs/bretagne_2023/analyse_ea_bretagne_2023_5m.csv", "ea");
		
	}

	private static void rasterize(String attribute) {
		
		//String attribute = "tx_boiseme_1";
		String output = "C:/Data/temp/grain_bocager/secteurs/bretagne_2023/raster/"+attribute+"_2023_10m.tif";
		String input = "C:/Data/temp/grain_bocager/secteurs/bretagne_2023/shape/rpg_2023.shp";
		float cellSize = 10f;
		int noDataValue = -1;
		
		float fillValue = -1;
		
		double minx = 93992.1885533562162891;
		double maxx = 405692.1885533562162891;
		double miny = 6699174.1972564728930593;
		double maxy = 6890974.1972564728930593;
		
		ShapeFile2CoverageConverter.rasterize(output, input, attribute, cellSize, noDataValue, null, minx, maxx, miny, maxy, fillValue);
		
	}

	private static void scriptExploitationsBretagne() {
		
		//String[] codesExploitation = recuperationCodesExploitation();
		//String[] codesExploitation = new String[]{"022068049"};
		System.out.println("recuperation des exploitations");
		Map<String, Set<Polygon>> parcellesExploitation = ShapeFileTool.getSurfaceEntities(parcellaire, eaCode);
		
		System.out.println(parcellesExploitation.size()+" exploitations a analyser");
		
		String outputFolder = "C:/Data/temp/grain_bocager/secteurs/";
		
		boolean exportMap = false;
		
		CsvWriter writer = DiagnosticGrainBocagerExploitation.prepareIndices("C:/Data/temp/grain_bocager/secteurs/analyse_ea_bretagne_2023_5m.csv");
		CsvWriter writerSecteur = DiagnosticGrainBocagerExploitation.prepareIndices("C:/Data/temp/grain_bocager/secteurs/analyse_ea_bretagne_2023_secteur_5m.csv");
		
		System.out.println("recuperation du bocage");
		//Coverage coverageBocage = null;
		Coverage coverageBocage = CoverageManager.getCoverage("C:/Data/projet/grain_bocager/data/mnhc_france_2021-2023/");
		
		int nb = 1;
		for(Entry<String, Set<Polygon>> exploitation : parcellesExploitation.entrySet()){
			
			if(nb < 9067) {
				nb++;
				continue;
			}
			
			System.out.println(nb+" diagnostic sur exploitation "+exploitation.getKey());
			
			if(scriptExploitation(outputFolder, exploitation.getKey(), exploitation.getValue(), coverageBocage, 5, writer, writerSecteur, exportMap)){
				nb++;
			}
			//nb++;
			break;
			
		}
		nb--;
		
		coverageBocage.dispose();
		
		System.out.println("nombre d'EA analysees = "+nb+" / "+parcellesExploitation.size());
		
		DiagnosticGrainBocagerExploitation.fermeIndices(writer);
		DiagnosticGrainBocagerExploitation.fermeIndices(writerSecteur);
	
	}
	
	private static boolean scriptExploitation(String outputFolder, String exploitation, Set<Polygon> parcelles, Coverage coverageBocage, int outCellSize, CsvWriter writer, CsvWriter writerSecteur, boolean exportMap) {
		
		DiagnosticGrainBocagerExploitationManager manager = new DiagnosticGrainBocagerExploitationManager();
		
		manager.setOutputFolder(outputFolder);
		if(writer != null) {
			manager.setWriter(writer);
		}
		if(writerSecteur != null) {
			manager.setWriterSecteur(writerSecteur);
		}
	
		// definition du territoire d'exploiattion
		//manager.setParcellaire(parcellaire);
		//manager.setAttributCodeEA(eaCode); 
		manager.setCodeEA(exploitation);
		manager.setParcelles(parcelles);
		
		// definition du bocage
		//manager.setBocage("D:/grain_bocager/data/22/2021/22_2021_hauteur_boisement.tif"); // bocage="chemin/vers/bocage.tif"
		//manager.setBocage("C:/Data/projet/grain_bocager/data/mnhc_france_2021-2023/");// bocage="chemin/vers/bocage.tif"
		manager.setCoverageBocage(coverageBocage);
		
		manager.setScenario("initial");
		
		manager.setGrainBocagerCellSize(outCellSize);
		manager.setExportMap(exportMap);
		DiagnosticGrainBocagerExploitation diagEA = manager.build();
		
		return diagEA.run();
		
	}
	
	private static String[] recuperationCodesExploitation(){
		
		try{
			ShpFiles sf = new ShpFiles(parcellaire);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			DbaseFileHeader header = dfr.getHeader();
			int ideaNumber = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(eaCode)){
					ideaNumber = i;
				}
			}
			if(ideaNumber == -1){
				throw new IllegalArgumentException("l'attribut '"+eaCode+"' n'existe pas.");
			}
		
			Set<String> codesExploitation = new TreeSet<String>();
			
			String code;
			Object[] entry;
			while(sfr.hasNext()){
				sfr.nextRecord();
				
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
	
	private static void compileMNHC() {
		
		String outputPath = "C:/Data/projet/grain_bocager/data/mnhc_france_2021-2023/";
		String outputName = "hauteur_boisement_2021-2023";
		
		String inputPath = "C:/Data/projet/grain_bocager/data/mnhc/";
		File inputFolder = new File(inputPath);
		String[] inputs = new String[inputFolder.list().length];
		int index = 0;
		for(String f : inputFolder.list()) {
			inputs[index++] = inputPath+f+"/";
		}
		
		CompileMNHC.compile(outputPath, outputName, inputs);
	}
	
	private static void generateParcellaireSecteur(String parcellaire, String eaCode, String secteurParcellaire) {
		
		Map<String, Set<Polygon>> parcellesExploitation = ShapeFileTool.getSurfaceEntities(parcellaire, eaCode);
		int countParcelles = 0;
		for(Entry<String, Set<Polygon>> exploitation : parcellesExploitation.entrySet()){
			countParcelles += exploitation.getValue().size();
		}
		System.out.println("nombre de parcelles : "+countParcelles);
	
		try {
			ShpFiles sf = new ShpFiles(parcellaire);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			/*
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			int pos = -1;
			for (int f=0; f<dfh.getNumFields(); f++) {
				if (dfh.getFieldName(f).equalsIgnoreCase(eaCode)) {
					pos = f;
				}
			}
			dfr.close();
			*/
			WritableByteChannel out = new FileOutputStream(secteurParcellaire + ".dbf").getChannel(); 
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(countParcelles);
			header.addColumn(eaCode, 'C', 12, 0);
			header.addColumn("code_secteur", 'C', 12, 0);
			header.addColumn("num_secteur", 'C', 2, 0);
			DbaseFileWriter dbfW = new DbaseFileWriter(header, out);
			FileOutputStream shp = new FileOutputStream(secteurParcellaire + ".shp");
			FileOutputStream shx = new FileOutputStream(secteurParcellaire + ".shx");
			ShapefileWriter shapeW = new ShapefileWriter(shp.getChannel(), shx.getChannel());
				
			shapeW.writeHeaders(new Envelope(sfh.minX(), sfh.maxX(), sfh.minY(), sfh.maxY()), ShapeType.POLYGON, countParcelles, 1000000);
			sfr.close();
			sf.dispose();

			//exportElement(element, time, dbfW, shapeW, setId, setAtt, types);

			Object[] entry;
			for(Entry<String, Set<Polygon>> exploitation : parcellesExploitation.entrySet()){
				
				System.out.println("analyse de l'exploitation "+exploitation.getKey());
				
				Map<Integer, Set<Polygon>> secteurs = DiagnosticGrainBocagerExploitation.detecteSecteursExploitation(exploitation.getValue(), eaCode, 2000);
				
				for(Entry<Integer, Set<Polygon>> secteur : secteurs.entrySet()) {
					
					for(Polygon parcelle : secteur.getValue()) {
						
						entry = new Object[3];
						entry[0] = exploitation.getKey();
						entry[1] = exploitation.getKey()+"_"+secteur.getKey();
						entry[2] = secteur.getKey()+"";
						
						dbfW.write(entry);
						shapeW.writeGeometry(parcelle);
					}
				}
			}
				
			out.close();
			dbfW.close();
			shp.close();
			shx.close();
			shapeW.close();			
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	/*
	private static void detecteSecteursExploitation(String eaCodeValue) {
		
		Set<Polygon> parcelles = ShapeFileTool.getSurfaceEntities(parcellaire, eaCode, eaCodeValue);
		
		Polygon[] tabParcelles = parcelles.toArray(new Polygon[parcelles.size()]);
		
		int[] gestionParcelles = new int[parcelles.size()];

		Map<Integer, Set<Polygon>> secteurs = new HashMap<Integer, Set<Polygon>>();
		int indexSecteur = 1;
		
		while(resteParcelleEnAttente(gestionParcelles) != -1) {
			
			int numParcelle = resteParcelleEnAttente(gestionParcelles);
			
			gestionParcelles[numParcelle] = 1;
			
			secteurs.put(indexSecteur, new HashSet<Polygon>());
			
			while(resteParcelleAGerer(gestionParcelles) != -1) {
				
				numParcelle = resteParcelleAGerer(gestionParcelles);
				
				secteurs.get(indexSecteur).add(tabParcelles[numParcelle]);
				
				for(int numParcelle2=0; numParcelle2<gestionParcelles.length; numParcelle2++) {
					
					if(gestionParcelles[numParcelle2] == 0 
							&& tabParcelles[numParcelle].distance(tabParcelles[numParcelle2]) <= distanceMaximale) {
						
						gestionParcelles[numParcelle2] = 1;
					}
				}
				
				gestionParcelles[numParcelle] = 2;
				
			}
			
			indexSecteur++;
		}
		
		System.out.println(eaCodeValue+" : "+secteurs.size()+" secteurs");
		
		for(int secteur : secteurs.keySet()) {
			
			System.out.println("secteur "+secteur+" avec "+secteurs.get(secteur).size()+" parcelles");
			
		}	
	}
	
	// reste-t-il des parcelles avec indice "1" ? 
	private static int resteParcelleAGerer(int[] gestionParcelles) {
		
		for(int i=0; i<gestionParcelles.length; i++) {
				
			int gestion = gestionParcelles[i];
				
			if(gestion == 1) {
					
				return i;
			}
		}
			
		return -1;
	}
		
	// reste-t-il des parcelles avec indice "0" ?
	private static int resteParcelleEnAttente(int[] gestionParcelles) {

		for(int i=0; i<gestionParcelles.length; i++) {
				
			int gestion = gestionParcelles[i];
				
			if(gestion == 0) {
					
				return i;
			}
		}
			
		return -1;
	}
	*/
	
}
