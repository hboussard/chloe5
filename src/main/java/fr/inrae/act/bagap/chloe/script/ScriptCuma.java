package fr.inrae.act.bagap.chloe.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.converter.GeoPackage2CoverageConverter;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;

public class ScriptCuma {

	public static void main(String[] args) {

		/*
		//String outputShapeFile = "";
		//String shapeCommune = "D:/sig/commune/communes-20220101_L93.shp";
		String fichierInsee = "C:/Data/temp/cuma/v_commune_2026.csv";
		String typeCommuneCode = "TYPECOM"; 
		String inseeCode = "COM";
		String nomCommuneCode = "NCC";
		String parentCode = "COMPARENT";
		String cumaFile = "C:/Data/temp/cuma/Cuma_utf8_no_accents.csv";
		String cumaInseeCode = "Insee";
		String cumaPostalCode = "Code_Postal";
		String cumaNomCommuneCode = "Commune";
		*/
		//analyseCumaAvecFichieInsee(fichierInsee, typeCommuneCode, inseeCode, nomCommuneCode, parentCode, cumaFile, cumaInseeCode, cumaPostalCode, cumaNomCommuneCode);
		
		//convertFile();
		/*
		String rawString = "Entwickeln Sie mit Vergnügen"; 
		byte[] bytes = StringUtils.toEncodedString(null, null).getBytesUtf8(rawString); 
		String utf8EncodedString = StringUtils.newStringUtf8(bytes);
		String str = "abcdef";   
		CharsetEncoder encoder = Charset.forName("iso-8859-1").newEncoder();
		CharsetEncoder encoder2 = Charset.forName("utf-8").newEncoder();
		System.out.println(encoder.canEncode(str));
		System.out.println(encoder2.canEncode(str));
		
		str = "1000€";    
		encoder = Charset.forName("iso-8859-1").newEncoder();
		System.out.println(encoder.canEncode(str));
		
		String rawString = "Chemazé";
		ByteBuffer buffer = StandardCharsets.ISO_8859_1.encode(rawString); 

		String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
		
		System.out.println(rawString+" "+utf8EncodedString);
		*/
		
		//Charset charset = Charset.forName("windows-1252").newEncoder().charset();
		
		//rasterizeMyCumaLink();
		//rasterizeFNCuma();
		//rasterizeRPG();
		//rasterizeRA();
		//analyseRA();
		rasterizeRAFromCsv();
	}
	
	private static void rasterizeRAFromCsv() {
		
		Coverage cov = CoverageManager.getCoverage("C:/Data/temp/cuma/ademe/Geo_Contours_Communes_2026/fncuma_100m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		System.out.println(entete);
		
		float[] data = new float[entete.width()*entete.height()];
		
		//String csv = "C:/Data/temp/cuma/ra/RA_SAU-par-commune_centro.csv";
		String csv = "C:/Data/temp/cuma/ra/RA_SAUxNB-EA_centro.csv";
		
		//SpatialCsvManager.exportTab(data, csv, "nb_ea_int", entete);
		
		//CoverageManager.write("C:/Data/temp/cuma/ra/ra_nb_ea_100m.tif", data, entete);
	}
	
	private static void analyseRA() {
		
		String path = "C:/Data/temp/cuma/ra/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.MAP);
		builder.addRasterFile(path+"RA_SAU-par-commune_centro4.tif");
		builder.addMetric("N-values");		
		builder.addCsvOutput(path+"ana_nv.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	
	private static void rasterizeRA() {
		
		Coverage cov = CoverageManager.getCoverage("C:/Data/temp/cuma/ademe/Geo_Contours_Communes_2026/fncuma_100m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		String shape = "C:/Data/temp/cuma/ra/RA_SAU-par-commune_centro3.shp";
		
		ShapeFile2CoverageConverter.rasterize("C:/Data/temp/cuma/ra/RA_SAU-par-commune_centro4.tif", shape, "SAU-2020_i", 0, entete);
	}
	
	private static void rasterizeRPG() {
		
		Coverage cov = CoverageManager.getCoverage("C:/Data/temp/cuma/ademe/Geo_Contours_Communes_2026/fncuma_100m.tif");
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		String gpkg = "E:/data/rpg/data/france/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01.7z/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01/RPG/1_DONNEES_LIVRAISON_2024/RPG_3-0__GPKG_LAMB93_FXX_2024-01-01/RPG_Parcelles.gpkg";
		
		GeoPackage2CoverageConverter.rasterize("C:/Data/temp/cuma/rpg_france_2024_100m.tif", gpkg, "code_group", 0, entete);
	}
	
	private static void rasterizeFNCuma() {
	
		String path ="C:/Data/temp/cuma/ademe/Geo_Contours_Communes_2026/";
		
		Envelope envelope = ShapeFileTool.getEnvelope("C:/Data/temp/cuma/commune_cuma_corse.shp", 0);
		
		System.out.println(envelope);
		
		EnteteRaster entete = EnteteRaster.getEntete(envelope, 100, -1);
		
		ShapeFile2CoverageConverter.rasterize(path+"fncuma_100m.tif", path+"Communes_fncuma.shp", "ncuma", 0, entete);
		
	}
	
	private static void rasterizeMyCumaLink() {
		
		String path ="C:/Data/temp/cuma/mycumalink/";
		
		Envelope envelope = ShapeFileTool.getEnvelope("C:/Data/temp/cuma/commune_cuma_corse.shp", 0);
		
		System.out.println(envelope);
		
		EnteteRaster entete = EnteteRaster.getEntete(envelope, 100, -1);
		
		//ShapeFile2CoverageConverter.rasterize(path+"mycumalink_100m.tif", path+"MyCumaLinkJson_corrige.shp", "code", 0, entete);
		ShapeFile2CoverageConverter.rasterize(path+"mycumalink_salarie_100m.tif", path+"MyCumaLinkJson_corrige.shp", "salaries", 0, entete);
		
	}
	
	public static void convertFile() {
		
		String csvFile = "C:/Data/temp/cuma/Cuma.csv";
		String csvFile2 = "C:/Data/temp/cuma/Cuma3.csv";
		
		File input = new File(csvFile);
		File output = new File(csvFile2);
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), Charset.forName("windows-1252")));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(removeAccents(line));
				writer.newLine();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void analyseCumaAvecFichieInsee (String fichierInsee, String typeCommuneCode, String inseeCode, String nomCommuneCode, String parentCode, String cumaFile, String cumaInseeCode, String cumaPostalCode, String cumaNomCommuneCode) {
		
		try {
			CsvReader cr = new CsvReader(fichierInsee);
			cr.setDelimiter(',');
			cr.readHeaders();
			
			Map<String, String> communeByInsee = new TreeMap<String, String>();
			
			Map<String, Set<String>> nomsByInsee = new TreeMap<String, Set<String>>();
			
			String codeInsee, communeInsee, typeCommune, codeParent;
			while(cr.readRecord()) {
				
				typeCommune = cr.get(typeCommuneCode);
				
				codeInsee = cr.get(inseeCode).toLowerCase();
				if(codeInsee.length() == 4) {
					
					codeInsee = '0'+codeInsee;
				}
				
				codeParent = cr.get(parentCode).toLowerCase();
				if(codeParent.length() == 4) {
					
					codeParent = '0'+codeParent;
				}
				
				communeInsee = cr.get(nomCommuneCode).toLowerCase();
				if(communeInsee.startsWith("l ")) {
					
					communeInsee = communeInsee.substring(2);
				}
				if(communeInsee.startsWith("le ")) {
					
					communeInsee = communeInsee.substring(3);
				}
				if(communeInsee.startsWith("la ")) {
					
					communeInsee = communeInsee.substring(3);
				}
				if(communeInsee.startsWith("les ")) {
	
					communeInsee = communeInsee.substring(4);
				}
				
				if(typeCommune.equalsIgnoreCase("COM")) {
					
					communeByInsee.put(codeInsee, communeInsee);
				
				}else {
					/*
					if(!communeByInsee.containsKey(codeInsee)) {
						
						communeByInsee.put(codeInsee, communeInsee);					
					}
					*/
					
					if(!nomsByInsee.containsKey(codeParent)) {
						
						nomsByInsee.put(codeParent, new TreeSet<String>());
					}
					nomsByInsee.get(codeParent).add(communeInsee);	
				}
			}
			
			cr.close();

			cr = new CsvReader(cumaFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			int index = 0;
			
			Map<String, Integer> nbCumaByInsee = new TreeMap<String, Integer>();
			
			String codePostal, nomCommune, inseeFinal;
			while(cr.readRecord()) {
				
				//System.out.println((++index)+" "+cr.get(cumaInseeCode));
				
				codeInsee = cr.get(cumaInseeCode).toLowerCase();
				
				if(codeInsee.length() == 4) {
					
					codeInsee = '0'+codeInsee;
				}
				
				codePostal = cr.get(cumaPostalCode);
				if(codePostal != null) {
					codePostal = codePostal.toLowerCase();
				}else {
					codePostal = "";
				}
				
				if(codePostal.length() == 4) {
					
					codePostal = '0'+codePostal;
				}
				
				nomCommune = cr.get(cumaNomCommuneCode);
				if(nomCommune == null || nomCommune.equalsIgnoreCase("") || nomCommune.equalsIgnoreCase("-")) {
					continue;
				}
				nomCommune = removeAccents(nomCommune)
						.toLowerCase()
						.replace("-", " ")
						.replace("'", " ")
						.replace("p/", "pres ")
						.replace("/", " sur ")
						.replace("   ", " ")
						.replace("  ", " ")
						.replace(".", "")
						;
				
				if(nomCommune.startsWith(" ")) {
					
					nomCommune = nomCommune.substring(1);
				}
				if(nomCommune.startsWith("l ")) {
					
					nomCommune = nomCommune.substring(2);
				}
				if(nomCommune.startsWith("le ")) {
					
					nomCommune = nomCommune.substring(3);
				}
				if(nomCommune.startsWith("la ")) {
					
					nomCommune = nomCommune.substring(3);
				}
				if(nomCommune.startsWith("les ")) {
	
					nomCommune = nomCommune.substring(4);
				}
				if(nomCommune.endsWith(" (l )")) {
					
					nomCommune = nomCommune.replace(" (l )", "");
				}
				if(nomCommune.endsWith(" (le)")) {
					
					nomCommune = nomCommune.replace(" (le)", "");
				}
				if(nomCommune.endsWith(" (la)")) {
					
					nomCommune = nomCommune.replace(" (la)", "");
				}
				if(nomCommune.endsWith(" (les)")) {
	
					nomCommune = nomCommune.replace(" (les)", "");
				}
				if(nomCommune.startsWith("st ")) {
	
					nomCommune = "saint "+nomCommune.substring(3);
				}
				if(nomCommune.startsWith("ste ")) {
					
					nomCommune = "sainte "+nomCommune.substring(4);
				}
				if(nomCommune.endsWith(" cedex")) {
					
					nomCommune = nomCommune.replace(" cedex", "");
				}
				if(nomCommune.endsWith(" cedex 01")) {
					
					nomCommune = nomCommune.replace(" cedex 01", "");
				}
				if(nomCommune.endsWith(" cedex 1")) {
					
					nomCommune = nomCommune.replace(" cedex 1", "");
				}
				
				nomCommune = nomCommune
						.replace(" st ", " saint ")
						.replace(" ste ", " sainte ");
				
				//System.out.println(codeInsee);
				
				if(communeByInsee.containsKey(codeInsee) && nomCommune.equalsIgnoreCase(communeByInsee.get(codeInsee))) {
					
					addCuma(nbCumaByInsee, codeInsee);
					
					//System.out.println((++index)+";"+codeInsee+";"+nomCommune+" --> "+communeByInsee.get(codeInsee));
					
				}else if(communeByInsee.containsKey(codeInsee) && nomAlternatifContenu(nomsByInsee, codeInsee, nomCommune)) {
					
					for(String nomAlternatif : nomsByInsee.get(codeInsee)) {
						
						if(nomCommune.equalsIgnoreCase(nomAlternatif)) {
						
							addCuma(nbCumaByInsee, codeInsee);
							
							//System.out.println((++index)+";"+codeInsee+";"+nomCommune+" --> "+communeByInsee.get(codeInsee)+" --> "+nomAlternatif);
							break;
						}
					}
				}else if((inseeFinal = getInseeByCommune(communeByInsee, nomsByInsee, nomCommune, codeInsee, codePostal)) != null) { // nom de la commune présent dans le fichier des communes
					
					addCuma(nbCumaByInsee, inseeFinal);
					
					//System.out.println((++index)+";"+inseeFinal+";"+nomCommune);
					
					//System.out.println(codeInsee);
					
				}else if((inseeFinal = getInseeByCommuneContained(communeByInsee, nomsByInsee, nomCommune, codeInsee, codePostal)) != null) { // nom de la commune contenu dans le fichier des communes
					
					addCuma(nbCumaByInsee, inseeFinal);
					
					//System.out.println((++index)+";"+inseeFinal+";"+nomCommune);
					
					//System.out.println(codeInsee);
					
				}else {
					
					//System.out.println((++index)+";"+codeInsee+";"+codePostal+";"+nomCommune);	
					
				}
			}
			
			cr.close();
			
			//System.out.println("nombre de communes avec cuma : "+nbCumaByInsee.size());
			
			//CsvWriter cw = new CsvWriter("C:/Data/temp/cuma/nb_cuma.csv");
			//cw.setDelimiter(';');
			
			//cw.write("insee");
			//cw.write("nb_cuma");
			//cw.endRecord();
			
			int nb = 0;
			
			for(Entry<String, Integer> e : nbCumaByInsee.entrySet()) {
				
				//cw.write(e.getKey().toUpperCase());
				//cw.write(e.getValue()+"");
				//cw.endRecord();
				
				nb += e.getValue();
			}
			
			//cw.close();
			
			System.out.println("nombre de communes avec cuma : "+nbCumaByInsee.size());
			System.out.println("nombre de cumas : "+nb);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addCuma(Map<String, Integer> cumaByInsee, String insee) {
		
		if(!cumaByInsee.containsKey(insee)) {
			
			cumaByInsee.put(insee, 1);
			
		}else {
			
			cumaByInsee.put(insee, cumaByInsee.get(insee)+1);
		}
	}
	
	private static boolean nomAlternatifContenu(Map<String, Set<String>> nomsByInsee, String codeInsee, String nomCommune) {
		
		boolean ok = false;
		
		if(nomsByInsee.containsKey(codeInsee)) {
			
			//System.out.println((++index)+";"+codeInsee+";"+nomCommune+" --> "+communeByInsee.get(codeInsee));
			
			for(String nomAlternatif : nomsByInsee.get(codeInsee)) {
				
				if(nomCommune.equalsIgnoreCase(nomAlternatif)) {
				
					ok = true;
					//System.out.println((++index)+";"+codeInsee+";"+nomCommune+" --> "+communeByInsee.get(codeInsee));
					break;
				}
			}
		}
		
		return ok;
	}
		
	/*
	public static void analyseCumaAvecShapefileCommunal (String outputShapeFile, String shapeCommune, String inseeCode, String nomCommuneCode, String csvFile, String csvInseeCode, String csvNomCommuneCode, String csvPostalCode) {
		

		try {
			ShpFiles sf = new ShpFiles(shapeCommune);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	StandardCharsets.ISO_8859_1);
			DbaseFileHeader dfh = dfr.getHeader();
			int posInseeCode = -1;
			int posPostalCode = -1;
			int posCommuneCode = -1;
			for (int f=0; f<dfh.getNumFields(); f++) {
				if (dfh.getFieldName(f).equalsIgnoreCase(inseeCode)) {
					posInseeCode = f;
				}
				else if (dfh.getFieldName(f).equalsIgnoreCase(nomCommuneCode)) {
					posCommuneCode = f;
				}
			}
			
			//Map<String, String> inseeByCommune = new TreeMap<String, String>();
			Map<String, String> communeByInsee = new TreeMap<String, String>();
			Map<String, Polygon> polygonByInsee = new TreeMap<String, Polygon>();
			Map<String, Integer> nbCumaByInsee = new TreeMap<String, Integer>();
			
			Geometry the_geom;
			Polygon the_poly;
			Object[] entry;
			String codeInsee;
			String codePostal;
			String nomCommune;
			while(sfr.hasNext()){
				
				entry = dfr.readEntry();
				codeInsee = entry[posInseeCode].toString().toLowerCase();
				nomCommune = removeAccents(entry[posCommuneCode].toString())
						.toLowerCase()
						.replace("-", " ")
						.replace("'", " ")
						.replace("  ", " ")
						.replace(".", "")
						;
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				if(the_geom != null) {
					
					//inseeByCommune.put(nomCommune, codeInsee);
					communeByInsee.put(codeInsee, nomCommune);
					nbCumaByInsee.put(codeInsee, 0);
					
					if(the_geom instanceof Polygon){
						
						the_poly = (Polygon) the_geom;
						the_poly.setUserData(entry);
							
						polygonByInsee.put(codeInsee, the_poly);
						
					}else if(the_geom instanceof MultiPolygon){
						
						for(int i=0; i<the_geom.getNumGeometries(); i++){
							
							the_poly = (Polygon) ((MultiPolygon) the_geom).getGeometryN(i);
							the_poly.setUserData(entry);
							
							polygonByInsee.put(codeInsee, the_poly);
						}
						
					}else{
						//System.out.println(the_geom);
						//throw new IllegalArgumentException("probleme geometrique");
					}			
				}
			}
			
			sfr.close();
			dfr.close();
			sf.dispose();
			
			CsvReader cr = new CsvReader(csvFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			posPostalCode = -1;
			posInseeCode = -1;
			posCommuneCode = -1;
			for (int h=0; h<cr.getHeaderCount(); h++) {
				if (cr.getHeader(h).equalsIgnoreCase(csvInseeCode)) {
					posInseeCode = h;
				}
				else if (cr.getHeader(h).equalsIgnoreCase(csvNomCommuneCode)) {
					posCommuneCode = h;
				}
				else if (cr.getHeader(h).equalsIgnoreCase(csvPostalCode)) {
					posPostalCode = h;
				}
			}
			
			int index = 0;
			
			while(cr.readRecord()) {
			
				codeInsee = cr.get(posInseeCode).toLowerCase();
				
				if(codeInsee.length() == 4) {
					
					codeInsee = '0'+codeInsee;
				}
				
				codePostal = cr.get(posPostalCode);
				if(codePostal != null) {
					codePostal = cr.get(posPostalCode).toLowerCase();
				}else {
					codePostal = "";
				}
				
				if(codePostal.length() == 4) {
					
					codePostal = '0'+codePostal;
				}
				
				if(cr.get(posCommuneCode) == null || cr.get(posCommuneCode).equalsIgnoreCase("") || cr.get(posCommuneCode).equalsIgnoreCase("-")) {
					continue;
				}
				nomCommune = removeAccents(cr.get(posCommuneCode))
						.toLowerCase()
						.replace("-", " ")
						.replace("'", " ")
						.replace("p/", "pres ")
						.replace("/", " sur ")
						.replace("  ", " ")
						.replace(".", "")
						.replace("?", "a")
						;
				
				if(nomCommune.endsWith(" (le)")) {
					
					nomCommune = "le "+nomCommune.replace(" (le)", "");
				}
				if(nomCommune.endsWith(" (la)")) {
					
					nomCommune = "la "+nomCommune.replace(" (la)", "");
				}
				if(nomCommune.endsWith(" (les)")) {
	
					nomCommune = "les "+nomCommune.replace(" (les)", "");
				}
				if(nomCommune.startsWith("st ")) {
	
					nomCommune = "saint "+nomCommune.replace("st ", "");
				}
				if(nomCommune.startsWith("ste ")) {
					
					nomCommune = "sainte "+nomCommune.replace("ste ", "");
				}
				
				nomCommune = nomCommune
						.replace(" st ", " saint ")
						.replace(" ste ", " sainte ");
				
				//.replace("/", " sur ")
				
				//System.out.println(codeInsee+" "+nomCommune);
				
				if(polygonByInsee.containsKey(codeInsee)) { // code ISNEE ok
					
					//System.out.println((++index)+";"+codeInsee+";"+nomCommune);
					
					nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					
				}else if((codeInsee = getInseeByCommune(communeByInsee, nomCommune, codeInsee, codePostal)) != null) { // nom de la commune présent dans le fichier des communes
					
					//System.out.println((++index)+";"+codeInsee+";"+nomCommune);
					
					//System.out.println(codeInsee);
					
					nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					
				}else if((codeInsee = getInseeByCommuneContained(communeByInsee, nomCommune)) != null) { // rapatriement
					
					//System.out.println((++index)+";"+codeInsee+";"+nomCommune);
					
					//System.out.println(codeInsee);
					
					nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					
				}else {
					
					//System.out.println((++index)+";"+nomCommune);
					
				}
				
				else if(inseeByCommune.containsKey(nomCommune)) {
					
					//System.out.println(nomCommune);
					
					codeInsee = inseeByCommune.get(nomCommune);
					
					if(polygonByInsee.containsKey(codeInsee)) {
						
						nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					}
				}
				
				
			}
			
			cr.close();
			
			CsvWriter cw = new CsvWriter("C:/Data/temp/cuma/test2.csv");
			cw.setDelimiter(';');
			
			cw.write("insee");
			cw.write("nb_cuma");
			cw.endRecord();
			
			for(Entry<String, Integer> insee : nbCumaByInsee.entrySet()) {
				
				System.out.println(insee.getKey()+" "+insee.getValue());
				
				if(insee.getKey().length() == 4) {
					
					cw.write('0'+insee.getKey());
				
				}else {
					
					cw.write(insee.getKey());
				}
				
				cw.write(insee.getValue()+"");
				cw.endRecord();
			}
			
			cw.close();
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	*/
	
	private static String removeAccents(String input) {
	    //return normalize(input).replaceAll("\\p{M}", "");
	    return (input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD)).replaceAll("\\p{M}", "");
	}
	
	private static String normalize(String input) {
	    return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
	}
	
	private static String getInseeByCommune(Map<String, String> communeByInsee, Map<String, Set<String>> nomsByInsee, String commune, String codeInsee, String codePostal) {
		
		//int nb = 0;
		
		//Map<String, String> reponses = new HashMap<String, String>();
		
		for(Entry<String, String> cbi : communeByInsee.entrySet()) {
			
			String startInsee = cbi.getKey().substring(0, 2);
			
			if(cbi.getValue().equalsIgnoreCase(commune)) {
				
				if(codeInsee.length() == 5) {
					
					String startCodeInsee = codeInsee.substring(0, 2);
					
					if(startCodeInsee.equalsIgnoreCase(startInsee)) {
						
						//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbi.getValue());
						
						return cbi.getKey();
					}
				}
				if(codePostal.length() == 5) {
					
					String startCodePostal = codePostal.substring(0, 2);
					
					if(startCodePostal.equalsIgnoreCase(startInsee)) {
						
						//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbi.getValue());
						
						return cbi.getKey();
					}
				}
				
				//reponses.put(cbi., codePostal)
				//nb++;
				//return cbi.getKey();
			}
		}
		for(Entry<String, Set<String>> cbi : nomsByInsee.entrySet()) {
			
			String startInsee = cbi.getKey().substring(0, 2);
			
			for(String cbiName : cbi.getValue()) {
				
				if(cbiName.equalsIgnoreCase(commune)) {
					
					if(codeInsee.length() == 5) {
						
						String startCodeInsee = codeInsee.substring(0, 2);
						
						if(startCodeInsee.equalsIgnoreCase(startInsee)) {
							
							//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbiName);
							
							return cbi.getKey();
						}
					}
					if(codePostal.length() == 5) {
						
						String startCodePostal = codePostal.substring(0, 2);
						
						if(startCodePostal.equalsIgnoreCase(startInsee)) {
							
							//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbiName);
							
							return cbi.getKey();
						}
					}
					
					//reponses.put(cbi., codePostal)
					//nb++;
					//return cbi.getKey();
				}
			}
		}
		
		return null;
	}
	
	private static String getInseeByCommuneContained(Map<String, String> communeByInsee, Map<String, Set<String>> nomsByInsee, String commune, String codeInsee, String codePostal) {
		
		//int nb = 0;
		
		for(Entry<String, String> cbi : communeByInsee.entrySet()) {
			
			String startInsee = cbi.getKey().substring(0, 2);
			
			if(cbi.getValue().contains(commune)) {
				
				if(codeInsee.length() == 5) {
					
					String startCodeInsee = codeInsee.substring(0, 2);
					
					if(startCodeInsee.equalsIgnoreCase(startInsee)) {
						
						//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbi.getValue());
						//System.out.println(commune+";"+codeInsee+";"+codePostal+";"+cbi.getValue()+";"+cbi.getKey());
						
						return cbi.getKey();
					}
				}
				if(codePostal.length() == 5) {
					
					String startCodePostal = codePostal.substring(0, 2);
					
					if(startCodePostal.equalsIgnoreCase(startInsee)) {
						
						//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbi.getValue());
						//System.out.println(commune+";"+codeInsee+";"+codePostal+";"+cbi.getValue()+";"+cbi.getKey());
						
						return cbi.getKey();
					}
				}
				
				//reponses.put(cbi., codePostal)
				//nb++;
				//return cbi.getKey();
				
				//System.out.println(commune+" --> "+cbi.getValue());
				
				//return cbi.getKey();
			}
		}
		
		for(Entry<String, Set<String>> cbi : nomsByInsee.entrySet()) {
			
			String startInsee = cbi.getKey().substring(0, 2);
			
			for(String cbiName : cbi.getValue()) {
				
				if(cbiName.contains(commune)) {
					
					if(codeInsee.length() == 5) {
						
						String startCodeInsee = codeInsee.substring(0, 2);
						
						if(startCodeInsee.equalsIgnoreCase(startInsee)) {
							
							//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbiName);
							//System.out.println(commune+";"+codeInsee+";"+codePostal+";"+cbi.getValue()+";"+cbi.getKey());
							
							return cbi.getKey();
						}
					}
					if(codePostal.length() == 5) {
						
						String startCodePostal = codePostal.substring(0, 2);
						
						if(startCodePostal.equalsIgnoreCase(startInsee)) {
							
							//System.out.println("rapatriement "+commune+" "+codeInsee+" "+codePostal+" dans "+cbi.getKey()+" "+cbiName);
							//System.out.println(commune+";"+codeInsee+";"+codePostal+";"+cbi.getValue()+";"+cbi.getKey());
							
							return cbi.getKey();
						}
					}
					
					//reponses.put(cbi., codePostal)
					//nb++;
					//return cbi.getKey();
				}
			}
		}
		
		return null;
	}
	
}
