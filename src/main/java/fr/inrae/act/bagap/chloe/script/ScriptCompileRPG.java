package fr.inrae.act.bagap.chloe.script;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureSource;

public class ScriptCompileRPG {

	public static void main(String[] args) {
		
		compileRPG();
		//compileRPG2();
	}
	
	private static void compileRPG2() {

		try {
			
			 System.setProperty("org.geotools.shapefile.useMemoryMapped", "false");

			    String path = "E:/data/rpg/data/france/francetransfert-1339765012/";

			    String[] inputs = new String[15];
				inputs[0] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R11_siege_2024/RPG_n2_inrae_R11_siege_2024.shp";
				inputs[1] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R24_siege_2024/RPG_n2_inrae_R24_siege_2024.shp";
				inputs[2] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R27_siege_2024/RPG_n2_inrae_R27_siege_2024.shp";
				inputs[3] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R28_siege_2024/RPG_n2_inrae_R28_siege_2024.shp";
				inputs[4] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R32_siege_2024/RPG_n2_inrae_R32_siege_2024.shp";
				inputs[5] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R44_siege_2024/RPG_n2_inrae_R44_siege_2024.shp";
				inputs[6] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R52_siege_2024/RPG_n2_inrae_R52_siege_2024.shp";
				inputs[7] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R53_siege_2024/RPG_n2_inrae_R53_siege_2024.shp";
				inputs[8] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R75A_siege_2024/RPG_n2_inrae_R75A_siege_2024.shp";
				inputs[9] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R75B_siege_2024/RPG_n2_inrae_R75B_siege_2024.shp";
				inputs[10] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R76A_siege_2024/RPG_n2_inrae_R76A_siege_2024.shp";
				inputs[11] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R76B_siege_2024/RPG_n2_inrae_R76B_siege_2024.shp";
				inputs[12] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R84_siege_2024/RPG_n2_inrae_R84_siege_2024.shp";
				inputs[13] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R93_siege_2024/RPG_n2_inrae_R93_siege_2024.shp";
				inputs[14] = path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R94_siege_2024/RPG_n2_inrae_R94_siege_2024.shp";

			    File newFile = new File(path + "RPG_niv2_france_2024.shp");

			    // ---------- 1. Création du DataStore de sortie ----------
			    ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
			    Map<String, Serializable> params = new HashMap<>();
			    params.put("url", newFile.toURI().toURL());
			    params.put("create spatial index", Boolean.TRUE);
			    ShapefileDataStore newDataStore = (ShapefileDataStore) factory.createNewDataStore(params);

			    // ---------- 2. Définition du schema ----------
			    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
			    builder.setName("RPG");

			    builder.add("the_geom", Polygon.class);
			    builder.add("pacage", String.class);

			    final SimpleFeatureType TYPE = builder.buildFeatureType();

			    newDataStore.createSchema(TYPE);

			    // ---------- 3. Writer ----------
			    FeatureWriter<SimpleFeatureType, SimpleFeature> writer = newDataStore.getFeatureWriterAppend(Transaction.AUTO_COMMIT);

			    // ---------- 4. Merge ----------
			    for (String input : inputs) {

			    	System.out.println(input);
			    	
			        File file = new File(input);

			        ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());

			        store.setCharset(Charset.defaultCharset());

			        SimpleFeatureSource source = store.getFeatureSource();

			        try (SimpleFeatureIterator it = source.getFeatures().features()) {

			            while (it.hasNext()) {

			                SimpleFeature f = it.next();

			                SimpleFeature newFeature = writer.next();

			                newFeature.setAttribute("the_geom", f.getDefaultGeometry());
			                newFeature.setAttribute("pacage", f.getAttribute(0));

			                writer.write();
			            }
			        }

			        store.dispose();
			    }

			    writer.close();
			    newDataStore.dispose();

			    System.out.println("Merge terminé !");
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void compileRPG() {
		
		System.setProperty("org.geotools.shapefile.useMemoryMapped", "false");
		
		String path = "E:/data/rpg/data/france/francetransfert-1339765012/";
		
		Set<String> setInputs = new HashSet<String>();
		/*
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R11_siege_2024/RPG_n2_inrae_R11_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R24_siege_2024/RPG_n2_inrae_R24_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R27_siege_2024/RPG_n2_inrae_R27_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R28_siege_2024/RPG_n2_inrae_R28_siege_2024.shp");
		
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R32_siege_2024/RPG_n2_inrae_R32_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R44_siege_2024/RPG_n2_inrae_R44_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R52_siege_2024/RPG_n2_inrae_R52_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R53_siege_2024/RPG_n2_inrae_R53_siege_2024.shp");
		
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R75A_siege_2024/RPG_n2_inrae_R75A_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R75B_siege_2024/RPG_n2_inrae_R75B_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R76A_siege_2024/RPG_n2_inrae_R76A_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R76B_siege_2024/RPG_n2_inrae_R76B_siege_2024.shp");
		
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R84_siege_2024/RPG_n2_inrae_R84_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R93_siege_2024/RPG_n2_inrae_R93_siege_2024.shp");
		setInputs.add(path+"RPG_n2_inrae_METRO_siege_2024/RPG_n2_inrae_R94_siege_2024/RPG_n2_inrae_R94_siege_2024.shp");
		*/
		/*
		setInputs.add(path+"RPG_niveau2_france_2024_p1.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p2.shp");
		
		setInputs.add(path+"RPG_niveau2_france_2024_p3.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p4.shp");
		*/
		/*
		setInputs.add(path+"RPG_niveau2_france_2024_p1.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p3.shp");
		
		setInputs.add(path+"RPG_niveau2_france_2024_p2.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p4.shp");
		*/
		/*
		setInputs.add(path+"RPG_niveau2_france_2024_p1.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p4.shp");
		
		setInputs.add(path+"RPG_niveau2_france_2024_p2.shp");
		setInputs.add(path+"RPG_niveau2_france_2024_p3.shp");
		*/
		
		String[] inputs = setInputs.toArray(new String[setInputs.size()]);
		
		int nbParcelles = getNbEntities(inputs);
		
		System.out.println("total = "+nbParcelles);
		
		String output = path+"RPG_niveau2_france_2024_part23";
		
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output+".shp");
				FileOutputStream shx = new FileOutputStream(output+".shx");){
			
			ShpFiles sf = new ShpFiles(inputs[0]);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			ShapefileHeader sfh = sfr.getHeader();
			DbaseFileReader dfr = new DbaseFileReader(sf, false, Charset.defaultCharset());
			DbaseFileHeader dfh = dfr.getHeader();
		
			// gestion du header de sortie
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(nbParcelles);
					
			header.addColumn("pacage", 'c', 20, 0);
			/*
			for(int i=0; i<dfh.getNumFields(); i++){
				header.addColumn(dfh.getFieldName(i), dfh.getFieldType(i), dfh.getFieldLength(i), dfh.getFieldDecimalCount(i));
			}
			*/
			sfr.close();
			dfr.close();
			sf.dispose();
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(
					new Envelope(sfh.minX(), sfh.maxX(), sfh.minY(), sfh.maxY()), 
					ShapeType.POLYGON, nbParcelles, 8000000);
			
			Geometry the_geom;
			String pacage;
			Object[] data;
			for(String input : inputs) {
				
				System.out.println(input);
				
				sf = new ShpFiles(input);
				sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
				dfr = new DbaseFileReader(sf, false, Charset.defaultCharset());
				
				while(sfr.hasNext()){
					
					the_geom = (Geometry) sfr.nextRecord().shape();
					
					pacage = (String) dfr.readEntry()[0]; 
					
					data = new String[]{pacage};
					
					sfw.writeGeometry(the_geom);
					dfw.write(data);
				}
				
				sfr.close();
				dfr.close();
				sf.dispose();				
			}
			
			dfw.close();
			sfw.close();
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.setProperty("org.geotools.shapefile.useMemoryMapped", "true");
	}

	private static int getNbEntities(String[] inputs) {
		
		int nbEntities = 0;

		try{
			
			for(String input : inputs) {
				
				ShpFiles sf = new ShpFiles(input);
				DbaseFileReader dfr = new DbaseFileReader(sf, true,	Charset.defaultCharset());
				DbaseFileHeader dfh = dfr.getHeader();
			
				System.out.println(input+" --> "+dfh.getNumRecords());
				
				nbEntities += dfh.getNumRecords();
				
				dfr.close();
				sf.dispose();
			}
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return nbEntities;
	}

}
