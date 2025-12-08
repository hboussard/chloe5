package fr.inrae.act.bagap.chloe.script;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

public class ScriptVero {

	public static void main(String[] args) {

		String outputShapeFile = "";
		String shapeCommune = "D:/sig/commune/communes-20220101_L93.shp";
		String inseeCode = "insee";
		String nomCommuneCode = "nom";
		String csvFile = "C:/Data/temp/cuma/Cuma.csv";
		String csvInseeCode = "Insee";
		String csvNomCommuneCode = "Commune";
		
		analyseCuma(outputShapeFile, shapeCommune, inseeCode, nomCommuneCode, csvFile, csvInseeCode, csvNomCommuneCode);
	}

	public static void analyseCuma(String outputShapeFile, String shapeCommune, String inseeCode, String nomCommuneCode, String csvFile, String csvInseeCode, String csvNomCommuneCode) {
		

		try {
			ShpFiles sf = new ShpFiles(shapeCommune);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			
			DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
			int posInseeCode = -1;
			int posCommuneCode = -1;
			for (int f=0; f<dfh.getNumFields(); f++) {
				if (dfh.getFieldName(f).equalsIgnoreCase(inseeCode)) {
					posInseeCode = f;
				}
				else if (dfh.getFieldName(f).equalsIgnoreCase(nomCommuneCode)) {
					posCommuneCode = f;
				}
			}
			
			Map<String, String> inseeByCommune = new TreeMap<String, String>();
			Map<String, Polygon> polygonByInsee = new TreeMap<String, Polygon>();
			Map<String, Integer> nbCumaByInsee = new TreeMap<String, Integer>();
			
			Geometry the_geom;
			Polygon the_poly;
			Object[] entry;
			String codeInsee;
			String nomCommune;
			while(sfr.hasNext()){
				
				entry = dfr.readEntry();
				codeInsee = entry[posInseeCode].toString().toLowerCase();
				nomCommune = entry[posCommuneCode].toString().toLowerCase();
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				if(the_geom != null) {
					
					inseeByCommune.put(nomCommune, codeInsee);
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
						System.out.println(the_geom);
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
			
			posInseeCode = -1;
			posCommuneCode = -1;
			for (int h=0; h<cr.getHeaderCount(); h++) {
				if (cr.getHeader(h).equalsIgnoreCase(csvInseeCode)) {
					posInseeCode = h;
				}
				else if (cr.getHeader(h).equalsIgnoreCase(csvNomCommuneCode)) {
					posCommuneCode = h;
				}
			}
			
			while(cr.readRecord()) {
			
				codeInsee = cr.get(posInseeCode).toLowerCase();
				
				if(cr.get(posCommuneCode) == null) {
					continue;
				}
				nomCommune = cr.get(posCommuneCode).toLowerCase();
				
				System.out.println(codeInsee+" "+nomCommune);
				
				if(polygonByInsee.containsKey(codeInsee)) {
					
					nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					
				}else if(inseeByCommune.containsKey(nomCommune)) {
					
					codeInsee = inseeByCommune.get(nomCommune);
					
					if(polygonByInsee.containsKey(codeInsee)) {
						
						nbCumaByInsee.put(codeInsee, nbCumaByInsee.get(codeInsee)+1);
					}
				}
			}
			
			cr.close();
	
			CsvWriter cw = new CsvWriter("C:/Data/temp/cuma/test.csv");
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
}
