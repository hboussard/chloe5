package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.geom.prep.PreparedPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.union.UnaryUnionOp;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.farm.DiagnosticGrainBocagerExploitationManager;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.TileCoverage;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.TabCoverage;
import fr.inrae.act.bagap.apiland.raster.Tile;
import fr.inrae.act.bagap.apiland.vector.ShapeFileTool;

import org.locationtech.jts.geom.Envelope;

public class ScriptDiagEABretagne {

	public static void main(String[] args) {
		
		//cleanMNHC();
		//scriptExploitationsFrance();
		//integrateRegionDepartementEpciCommune();
		//closestRegionDepartementCommune();
		//scriptExploitationsBretagne();
		//scriptExploitation("056046330", 5, null, true);
		//recuperationEnveloppesExploitations();
		//tuilageTypeBoisement();
		//analyseBoisementSecteur();
		//compileSecteursExploitation();
		/*
		Coverage covBoisement = CoverageManager.getCoverage("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/");
		EnteteRaster enteteBoisement = covBoisement.getEntete();
		covBoisement.dispose();
		*/
		/*
		File folder = new File("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/");
		Coverage cov;
		int ind=0;
		
		//Util.createAccess("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/");
		for(File f : folder.listFiles()) {
			
			for(String file : f.list()) {
			
				System.out.println((++ind)+" "+f+"/"+file);
			
				//int xnum = Integer.parseInt(file.split("_")[3])/100;
			
				//Util.createAccess("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/type_boisement_"+xnum+"/");
			
				//Tool.copy(folder+"/"+file, "C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement/"+file);
			
				cov = CoverageManager.getCoverage(f+"/"+file);
				cov.dispose();
			}
		}
			*/
		/*
		Coverage cov;
		float[] data;
		EnteteRaster entete, entete2;
		File folder = new File("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/");
		int ind=0;
		for(File f : folder.listFiles()) {
			
			System.out.println(f.getAbsolutePath());
			Util.createAccess("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement/"+f.getName()+"/");
			
			for(String file : f.list()) {
			
				System.out.println((++ind)+" "+f+"/"+file);
			
				//int xnum = Integer.parseInt(file.split("_")[3])/100;
			
				//Util.createAccess("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement_2/type_boisement_"+xnum+"/");
			
				//Tool.copy(folder+"/"+file, "C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement/"+file);
			
				cov = CoverageManager.getCoverage(f+"/"+file);
				data = cov.getData();
				entete = cov.getEntete();
				cov.dispose();
				
				entete2 = new EnteteRaster(entete.width(), entete.height(), Math.round(entete.minx()), Math.round(entete.maxx()), Math.round(entete.miny()), Math.round(entete.maxy()), entete.cellsize(),  entete.noDataValue());
		
				CoverageManager.writeGeotiff("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement/"+f.getName()+"/"+file, data, entete2);
					
			}
		}
	*/
	}
	
	private static void compileSecteursExploitation() {
		
		try {
		
			CsvReader reader = new CsvReader("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement_admin2.csv");
			reader.setDelimiter(';');
			reader.readHeaders();
			
			CsvWriter writer = new CsvWriter("E:/data/rpg/secteur/exploitations_rpg20224.csv");
			writer.setDelimiter(';');
			
			writer.write("pacage");
			writer.write("nb_secteurs");
			writer.write("nb_parcelles");
			writer.write("surf_int");
			writer.write("surf_ext");
			writer.write("x");
			writer.write("y");
			writer.write("xmin");
			writer.write("xmax");
			writer.write("ymin");
			writer.write("ymax");
			writer.write("surf_bois_int");
			writer.write("surf_massif_int");
			writer.write("surf_haie_int");
			writer.write("surf_arbre_int");
			writer.write("surf_bois_ext");
			writer.write("surf_massif_ext");
			writer.write("surf_haie_ext");
			writer.write("surf_arbre_ext");
			writer.write("tx_bois_int");
			writer.write("tx_bois_ext");
			writer.write("insee");
			writer.write("commune");
			writer.write("code_epci");
			writer.write("epci");
			writer.write("code_dep");
			writer.write("departement");
			writer.write("code_reg");
			writer.write("region");
			
			writer.endRecord();
			
			String exploitation = null;
			int nb_secteurs = 0, nb_parcelles = 0;
			double surf, surf_int = 0, surf_ext = 0, maxSurf = 0, surf_bois_int = 0, surf_massif_int = 0, surf_haie_int = 0, surf_arbre_int = 0, surf_bois_ext = 0, surf_massif_ext = 0, surf_haie_ext = 0, surf_arbre_ext = 0;
			double x = 0, y = 0, xmin = 0, xmax = 0, ymin = 0, ymax = 0;
			String insee = null, commune = null, code_epci = null, epci = null, code_dep = null, departement = null, code_reg = null, region = null;
			while(reader.readRecord()) {
					
				if(exploitation == null) { // premier passage
					
					exploitation = reader.get("pacage");
					nb_secteurs = 1;
					nb_parcelles = Integer.parseInt(reader.get("nb_parcelles"));
					surf_int = Double.parseDouble(reader.get("surf_int"));
					maxSurf = surf_int;
					surf_ext = Double.parseDouble(reader.get("surf_ext"));
					x = Double.parseDouble(reader.get("x"));
					y = Double.parseDouble(reader.get("y"));
					xmin = Double.parseDouble(reader.get("xmin"));
					xmax = Double.parseDouble(reader.get("xmax"));
					ymin = Double.parseDouble(reader.get("ymin"));
					ymax = Double.parseDouble(reader.get("ymax"));
					surf_bois_int = Double.parseDouble(reader.get("surf_bois_int")); 
					surf_massif_int = Double.parseDouble(reader.get("surf_massif_int"));
					surf_haie_int = Double.parseDouble(reader.get("surf_haie_int"));
					surf_arbre_int = Double.parseDouble(reader.get("surf_arbre_int"));
					surf_bois_ext = Double.parseDouble(reader.get("surf_bois_ext"));
					surf_massif_ext = Double.parseDouble(reader.get("surf_massif_ext"));
					surf_haie_ext = Double.parseDouble(reader.get("surf_haie_ext"));
					surf_arbre_ext = Double.parseDouble(reader.get("surf_arbre_ext"));
					insee = reader.get("insee");
					commune = reader.get("commune");
					code_epci = reader.get("code_epci");
					epci = reader.get("epci");
					code_dep = reader.get("code_dep");
					departement = reader.get("departement");
					code_reg = reader.get("code_reg");
					region = reader.get("region");
					
				}else if(reader.get("pacage").equalsIgnoreCase(exploitation)) { // exploitation deja commencee pour traitement
					
					nb_secteurs++;
					nb_parcelles += Integer.parseInt(reader.get("nb_parcelles"));
					surf = Double.parseDouble(reader.get("surf_int"));
					surf_int += surf;
					surf_ext += Double.parseDouble(reader.get("surf_ext"));
					surf_bois_int += Double.parseDouble(reader.get("surf_bois_int")); 
					surf_massif_int += Double.parseDouble(reader.get("surf_massif_int"));
					surf_haie_int += Double.parseDouble(reader.get("surf_haie_int"));
					surf_arbre_int += Double.parseDouble(reader.get("surf_arbre_int"));
					surf_bois_ext += Double.parseDouble(reader.get("surf_bois_ext"));
					surf_massif_ext += Double.parseDouble(reader.get("surf_massif_ext"));
					surf_haie_ext += Double.parseDouble(reader.get("surf_haie_ext"));
					surf_arbre_ext += Double.parseDouble(reader.get("surf_arbre_ext"));
					
					if(surf > maxSurf) {
						
						maxSurf = surf;
						x = Double.parseDouble(reader.get("x"));
						y = Double.parseDouble(reader.get("y"));
						xmin = Double.parseDouble(reader.get("xmin"));
						xmax = Double.parseDouble(reader.get("xmax"));
						ymin = Double.parseDouble(reader.get("ymin"));
						ymax = Double.parseDouble(reader.get("ymax"));
						insee = reader.get("insee");
						commune = reader.get("commune");
						code_epci = reader.get("code_epci");
						epci = reader.get("epci");
						code_dep = reader.get("code_dep");
						departement = reader.get("departement");
						code_reg = reader.get("code_reg");
						region = reader.get("region");
					}
				}else { // exploitation differente
					
					// enregistrer l'exploitation en cours
					writer.write(exploitation);
					writer.write(nb_secteurs+"");
					writer.write(nb_parcelles+"");
					writer.write(Util.format(surf_int));
					writer.write(Util.format(surf_ext));
					writer.write(Util.format(x));
					writer.write(Util.format(y));
					writer.write(Util.format(xmin));
					writer.write(Util.format(xmax));
					writer.write(Util.format(ymin));
					writer.write(Util.format(ymax));
					writer.write(Util.format(surf_bois_int));
					writer.write(Util.format(surf_massif_int));
					writer.write(Util.format(surf_haie_int));
					writer.write(Util.format(surf_arbre_int));
					writer.write(Util.format(surf_bois_ext));
					writer.write(Util.format(surf_massif_ext));
					writer.write(Util.format(surf_haie_ext));
					writer.write(Util.format(surf_arbre_ext));
					double tx_bois_int = surf_bois_int*100.0 / surf_int;
					writer.write(Util.format(tx_bois_int));
					double tx_bois_ext = surf_bois_ext*100.0 / surf_ext;
					writer.write(Util.format(tx_bois_ext));
					writer.write(insee);
					writer.write(commune);
					writer.write(code_epci);
					writer.write(epci);
					writer.write(code_dep);
					writer.write(departement);
					writer.write(code_reg);
					writer.write(region);
					writer.endRecord();
					
					// integrer la nouvelle
					exploitation = reader.get("pacage");
					nb_secteurs = 1;
					nb_parcelles = Integer.parseInt(reader.get("nb_parcelles"));
					surf_int = Double.parseDouble(reader.get("surf_int"));
					maxSurf = surf_int;
					surf_ext = Double.parseDouble(reader.get("surf_ext"));
					x = Double.parseDouble(reader.get("x"));
					y = Double.parseDouble(reader.get("y"));
					xmin = Double.parseDouble(reader.get("xmin"));
					xmax = Double.parseDouble(reader.get("xmax"));
					ymin = Double.parseDouble(reader.get("ymin"));
					ymax = Double.parseDouble(reader.get("ymax"));
					surf_bois_int = Double.parseDouble(reader.get("surf_bois_int")); 
					surf_massif_int = Double.parseDouble(reader.get("surf_massif_int"));
					surf_haie_int = Double.parseDouble(reader.get("surf_haie_int"));
					surf_arbre_int = Double.parseDouble(reader.get("surf_arbre_int"));
					surf_bois_ext = Double.parseDouble(reader.get("surf_bois_ext"));
					surf_massif_ext = Double.parseDouble(reader.get("surf_massif_ext"));
					surf_haie_ext = Double.parseDouble(reader.get("surf_haie_ext"));
					surf_arbre_ext = Double.parseDouble(reader.get("surf_arbre_ext"));
					insee = reader.get("insee");
					commune = reader.get("commune");
					code_epci = reader.get("code_epci");
					epci = reader.get("epci");
					code_dep = reader.get("code_dep");
					departement = reader.get("departement");
					code_reg = reader.get("code_reg");
					region = reader.get("region");
					
				}
			}
			
			// enregistrer la derniere exploitation
			writer.write(exploitation);
			writer.write(nb_secteurs+"");
			writer.write(nb_parcelles+"");
			writer.write(Util.format(surf_int));
			writer.write(Util.format(surf_ext));
			writer.write(Util.format(x));
			writer.write(Util.format(y));
			writer.write(Util.format(xmin));
			writer.write(Util.format(xmax));
			writer.write(Util.format(ymin));
			writer.write(Util.format(ymax));
			writer.write(Util.format(surf_bois_int));
			writer.write(Util.format(surf_massif_int));
			writer.write(Util.format(surf_haie_int));
			writer.write(Util.format(surf_arbre_int));
			writer.write(Util.format(surf_bois_ext));
			writer.write(Util.format(surf_massif_ext));
			writer.write(Util.format(surf_haie_ext));
			writer.write(Util.format(surf_arbre_ext));
			double tx_bois_int = surf_bois_int*100.0 / surf_int;
			writer.write(Util.format(tx_bois_int));
			double tx_bois_ext = surf_bois_ext*100.0 / surf_ext;
			writer.write(Util.format(tx_bois_ext));
			writer.write(insee);
			writer.write(commune);
			writer.write(code_epci);
			writer.write(epci);
			writer.write(code_dep);
			writer.write(departement);
			writer.write(code_reg);
			writer.write(region);
			writer.endRecord();
			
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analyseBoisementSecteur() {
		
		try {
			
			//Coverage covBoisement = CoverageManager.getCoverage("E:/data/grain_bocager/tuile/2021-2023/type_boisement/");
			//Coverage covBoisement = CoverageManager.getCoverage("E:/data/grain_bocager/tuile/2021-2023/type_boisement_test/");
			Coverage covBoisement = CoverageManager.getCoverage("C:/Data/projet/grain_bocager/data/tuile/2021-2023/type_boisement/");
			EnteteRaster enteteBoisement = covBoisement.getEntete();
			
			String path = "E:/data/rpg/data/france/francetransfert-1339765012/";
			
			Set<String> setInputs = new HashSet<String>();
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
			String[] parcellaires = setInputs.toArray(new String[setInputs.size()]);
			
			String attributeCodeEA = "pacage";
			
			Map<String, Set<Polygon>> exploitations = new TreeMap<String, Set<Polygon>>();
			ShapeFileTool.getSurfaceEntities(exploitations, parcellaires, attributeCodeEA);
			
			System.out.println("nombre d'explotations = "+exploitations.size());
			
			CsvWriter writer = new CsvWriter("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement_part3.csv");
			writer.setDelimiter(';');
			
			writer.write("code");
			writer.write("pacage");
			writer.write("numero");
			writer.write("nb_parcelles");
			writer.write("surf_int");
			writer.write("surf_ext");
			writer.write("x");
			writer.write("y");
			writer.write("xmin");
			writer.write("xmax");
			writer.write("ymin");
			writer.write("ymax");
			writer.write("surf_bois_int");
			writer.write("surf_massif_int");
			writer.write("surf_haie_int");
			writer.write("surf_arbre_int");
			writer.write("surf_bois_ext");
			writer.write("surf_massif_ext");
			writer.write("surf_haie_ext");
			writer.write("surf_arbre_ext");
			writer.write("tx_bois_int");
			writer.write("tx_bois_ext");
			writer.endRecord();
			
			int nbExploitations = 0;
			GeometryFactory gf = new GeometryFactory();
			int num;
			Envelope env;
			float[] data, distData, boisData;
			EnteteRaster entete;
			GeometryCollection parcellesDuSecteur;
			int[] codes = new int[]{1};
			TabChamferDistanceAnalysis distance;
			int buffer = 500;
			Point p;
			for(Entry<String, Set<Polygon>> exploitation : exploitations.entrySet()){
					
				if(exploitation.getKey().compareTo("069167137") > 0) {
					
					num = 1;
					
					Map<Integer, Set<Polygon>> secteursExploitation = DiagnosticGrainBocagerExploitation.detecteSecteursExploitation(exploitation.getValue(), exploitation.getKey(), 2000);
						
					System.out.println((++nbExploitations)+" : exploitation "+exploitation.getKey()+" avec "+exploitation.getValue().size()+" parcelles regroupees en "+secteursExploitation.size()+" secteurs");
						
					for(Entry<Integer, Set<Polygon>> secteur : secteursExploitation.entrySet()){
						
						parcellesDuSecteur = gf.createMultiPolygon(secteur.getValue().toArray(new Polygon[secteur.getValue().size()]));
						
						p = parcellesDuSecteur.getInteriorPoint();
						
						env = parcellesDuSecteur.getEnvelopeInternal();
						env = new Envelope(env.getMinX()-buffer, env.getMaxX()+buffer, env.getMinY()-buffer, env.getMaxY()+buffer);
						entete = EnteteRaster.getEntete(enteteBoisement, env);
					
						data = ShapeFile2CoverageConverter.getSurfaceData(secteur.getValue(), entete, 1f, 0f);
						
						distData = new float[data.length];
						
						distance = new TabChamferDistanceAnalysis(distData, data, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), codes, 2*buffer);
						distance.allRun();
						
						int nbIn = 0;
						int nbExt = 0;
						
						for(int i=0; i<data.length; i++) {
							
							float v = distData[i];
							
							if(v==0) {
								nbIn++;
							}
							if(v>0 && v<=buffer) {
								data[i] = 2;
								nbExt++;
							}
						}
						
						double surfaceIn = (nbIn * 25)/10000.0;
						double surfaceExt = (nbExt * 25)/10000.0;
						
						//CoverageManager.write("E:/data/rpg/secteur/test/"+exploitation.getKey()+"_"+secteur.getKey()+".tif", data, entete);
						
						boisData = covBoisement.getData(EnteteRaster.getROI(enteteBoisement, env));
						
						int nbBoisIn = 0;
						int nbMassifIn = 0;
						int nbHaieIn = 0;
						int nbArbreIn = 0;
						
						int nbBoisExt = 0;
						int nbMassifExt = 0;
						int nbHaieExt = 0;
						int nbArbreExt = 0;
						
						for(int i=0; i<data.length; i++) {
							int bd = (int) boisData[i];
							if(bd > 0) {
								int d = (int) data[i];
								if(d == 1) {
									nbBoisIn++;
									if(bd == 1) {
										nbArbreIn++;
									}else if(bd == 5) {
										nbMassifIn++;
									}else {
										nbHaieIn++;
									}
								}else if(d == 2) {
									nbBoisExt++;
									if(bd == 1) {
										nbArbreExt++;
									}else if(bd == 5) {
										nbMassifExt++;
									}else {
										nbHaieExt++;
									}
								}
							}
						}
						
						double surfaceBoisIn = (nbBoisIn * 25)/10000.0;
						double surfaceMassifIn = (nbMassifIn * 25)/10000.0;
						double surfaceHaieIn = (nbHaieIn * 25)/10000.0;
						double surfaceArbreIn = (nbArbreIn * 25)/10000.0;
						
						double surfaceBoisExt = (nbBoisExt * 25)/10000.0;
						double surfaceMassifExt = (nbMassifExt * 25)/10000.0;
						double surfaceHaieExt = (nbHaieExt * 25)/10000.0;
						double surfaceArbreExt = (nbArbreExt * 25)/10000.0;
						
						double txBoisIn = surfaceBoisIn * 100.0 / surfaceIn;
						double txBoisExt = surfaceBoisExt * 100.0 / surfaceExt;
						
						//System.out.println(txBoisIn+" "+txBoisExt);
						
						writer.write(exploitation.getKey()+"_"+num);
						writer.write(exploitation.getKey());
						writer.write(num+"");
						writer.write(secteur.getValue().size()+"");
						writer.write(Util.format(surfaceIn));
						writer.write(Util.format(surfaceExt));
						writer.write(Util.format(p.getX()));
						writer.write(Util.format(p.getY()));
						writer.write(Util.format(env.getMinX()));
						writer.write(Util.format(env.getMaxX()));
						writer.write(Util.format(env.getMinY()));
						writer.write(Util.format(env.getMaxY()));
						
						writer.write(Util.format(surfaceBoisIn));
						writer.write(Util.format(surfaceMassifIn));
						writer.write(Util.format(surfaceHaieIn));
						writer.write(Util.format(surfaceArbreIn));
						writer.write(Util.format(surfaceBoisExt));
						writer.write(Util.format(surfaceMassifExt));
						writer.write(Util.format(surfaceHaieExt));
						writer.write(Util.format(surfaceArbreExt));
						writer.write(Util.format(txBoisIn));
						writer.write(Util.format(txBoisExt));
						
						writer.endRecord();
						
						num++;
					}
					
				}	
			}
			
			writer.close();
			covBoisement.dispose();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void tuilageTypeBoisement() {
		
		
		Map<String, Integer> yearByDep = new TreeMap<String, Integer>();
		Map<String, Envelope> envByDep = new TreeMap<String, Envelope>();
		Map<String, Set<Envelope>> tilesByDep = new TreeMap<String, Set<Envelope>>();
		
		String pathGB = "D:/grain_bocager/data/";
		File folderGB = new File(pathGB);
		Coverage covTB;
		EnteteRaster enteteTB;
		Envelope envTB;
		String dep;
		int lastYear;
		int year;
		for(File fileDep : folderGB.listFiles()) {
			
			dep = fileDep.getName();
			System.out.println(dep);
			
			lastYear = 0;
			for(File fileYear : fileDep.listFiles()) {
				
				year = Integer.parseInt(fileYear.getName());
				
				lastYear = Math.max(lastYear, year);
			}
			
			yearByDep.put(dep, lastYear);
			tilesByDep.put(dep, new HashSet<Envelope>());
			
			covTB = CoverageManager.getCoverage(pathGB+dep+"/"+lastYear+"/"+dep+"_"+lastYear+"_type_boisement.tif");
			enteteTB = covTB.getEntete();
			envTB = enteteTB.getEnvelope();
			covTB.dispose();
			
			envByDep.put(dep, envTB);
		}
		
		String pathTile = "E:/data/grain_bocager/tuile/2021-2023/hauteur_boisement/";
		File folderTile = new File(pathTile);
		
		int ind = 1;
		float[] data;
		Coverage covTile;
		EnteteRaster enteteTile;
		Envelope envTile;
		for(File fileTile : folderTile.listFiles()) {
			
			if(fileTile.getName().endsWith(".tif")) {
				
				System.out.println("tuile "+(ind++));
				
				covTile = CoverageManager.getCoverage(fileTile.getPath());
				enteteTile = covTile.getEntete();
				envTile = covTile.getEntete().getEnvelope();
				covTile.dispose();
				
				data = new float[enteteTile.width()*enteteTile.height()];
				
				for(Entry<String, Envelope> envDep : envByDep.entrySet()) {
					
					if(envDep.getValue().intersects(envTile)) {
						
						tilesByDep.get(envDep.getKey()).add(envTile);
						
						//System.out.println(fileTile.getName()+" "+((int) (envTile.getMinX()/1000.0))+" "+((int)(envTile.getMaxY()/1000.0)));
						
						/*
						String dep = envDep.getKey();
						int year = yearByDep.get(dep);
						
						System.out.println(dep+" "+fileTile.getName());
						
						Coverage covTB = CoverageManager.getCoverage(pathGB+dep+"/"+year+"/"+dep+"_"+year+"_type_boisement.tif");
						EnteteRaster enteteTB = covTB.getEntete();
						float[] dataTB = covTB.getData(EnteteRaster.getROI(enteteTB, envTile));
						covTB.dispose();
						
						for(int i=0; i<dataTB.length; i++) {
							
							data[i] = Math.max(data[i], dataTB[i]);
						}
						*/
					}
				}
				
				//CoverageManager.write("E:/data/grain_bocager/tuile/2021-2023/type_boisement/"+fileTile.getName().replace("hauteur", "type"), data, enteteTile);
			}	
		}
		
		int minX;
		int maxY;
		String outFile;
		float[] dataTB;
		Coverage cov;
		EnteteRaster entete;
		for(Entry<String, Integer> yd : yearByDep.entrySet()) {
			
			dep = yd.getKey();
			year = yd.getValue();
			
			System.out.println(dep);
			
			covTB = CoverageManager.getCoverage(pathGB+dep+"/"+year+"/"+dep+"_"+year+"_type_boisement.tif");
			enteteTB = covTB.getEntete();
			
			for(Envelope env : tilesByDep.get(dep)) {
				
				minX = (int)(env.getMinX()/1000.0);
				maxY = (int)(env.getMaxY()/1000.0);
				
				outFile = "E:/data/grain_bocager/tuile/2021-2023/type_boisement/type_boisement_2021-2023_"+minX+"_"+maxY+".tif";
				
				dataTB = covTB.getData(EnteteRaster.getROI(enteteTB, env));
				
				if(new File(outFile).exists()){
					
					cov = CoverageManager.getCoverage(outFile);
					data = cov.getData();
					entete = cov.getEntete();
					cov.dispose();
					
					for(int i=0; i<dataTB.length; i++) {
						
						data[i] = Math.max(data[i], dataTB[i]);
					}
					
					CoverageManager.writeGeotiff(outFile, data, entete);
					
				}else{
					
					CoverageManager.writeGeotiff(outFile, dataTB, new EnteteRaster(1000, 1000, env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY(), 5, -1) );
				}
			}
		
			covTB.dispose();
		}
	
	}
	
	private static void closestRegionDepartementCommune() {
		
		String regionFile = "D://sig/region/RegionsL93.shp";
		String departementFile = "D://sig/departement/DepartementsL93.shp";
		String epciFile = "D://sig/epci/EPCIL93.shp";
		String communeFile = "D://sig/commune/CommunesL93.shp";
		
		try{
			// integration des regions
			ShpFiles sf = new ShpFiles(regionFile);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
		
			Map<String, Geometry> regions = new TreeMap<String, Geometry>();
			Map<String, String> regionNames = new TreeMap<String, String>();
			
			Geometry the_geom;
			String code = null, nom;
			Object[] entry;
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
				
				regions.put(code, the_geom);
				regionNames.put(code, nom);
			}
			
			sfr.close();
			dfr.close();
			
			// integration des departements
			sf = new ShpFiles(departementFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
		
			Map<String, Geometry> departements = new TreeMap<String, Geometry>();
			Map<String, String> departementNames = new TreeMap<String, String>();
			
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
				
				//System.out.println(the_geom+" "+code+" "+nom);
				
				if(the_geom != null) {
					
					departements.put(code, the_geom);
					departementNames.put(code, nom);
				}
			}
			
			sfr.close();
			dfr.close();
			
			// integration des EPCIs
			sf = new ShpFiles(epciFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
								
			Map<String, Geometry> epcis = new TreeMap<String, Geometry>();
			Map<String, String> epciNames = new TreeMap<String, String>();
									
			while(sfr.hasNext()){
							
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
										
				//System.out.println(the_geom+" "+code+" "+nom);
										
				if(the_geom != null) {
								
					epcis.put(code, the_geom);
					epciNames.put(code, nom);
				}
			}
			
			// integration des communes
			sf = new ShpFiles(communeFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
					
			Map<String, Geometry> communes = new TreeMap<String, Geometry>();
			Map<String, String> communeNames = new TreeMap<String, String>();
						
			while(sfr.hasNext()){
							
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[2];
						
				if(the_geom != null) {
					
					communes.put(code, the_geom);
					communeNames.put(code, nom);	
				}
			}
						
			sfr.close();
			dfr.close();
			
			CsvReader reader = new CsvReader("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement_admin.csv");
			reader.setDelimiter(';');
			reader.readHeaders();
			
			CsvWriter writer = new CsvWriter("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement_admin2.csv");
			writer.setDelimiter(';');
			for(int i=0; i<reader.getHeaderCount(); i++) {
				
				writer.write(reader.getHeader(i));
			}
			
			writer.endRecord();
			
			double x, y;
			Point pSecteur = null;
			WKTReader wkt = new WKTReader();
			double distMin, distance;
			int ind = 1;
			while(reader.readRecord()) {
			
				if(reader.get("insee").equalsIgnoreCase("") || reader.get("code_epci").equalsIgnoreCase("") || reader.get("code_dep").equalsIgnoreCase("") || reader.get("code_reg").equalsIgnoreCase("")) {
			
					System.out.println((ind++)+" "+reader.get("code"));
					
					for(int i=0; i<=21; i++) {
						
						writer.write(reader.get(i));
						
					}
					
					x = Double.parseDouble(reader.get("x"));
					y = Double.parseDouble(reader.get("y"));
					
					pSecteur = (Point) wkt.read("POINT ("+x+" "+y+")");
					
					distMin = Double.MAX_VALUE;
					code = null;
					for(Entry<String, Geometry> commune : communes.entrySet()) {
						
						distance = commune.getValue().distance(pSecteur);
						
						if(distance < distMin) {
							
							distMin = distance;
							code = commune.getKey();
						}
					}
					writer.write(code);
					writer.write(communeNames.get(code));
					
					distMin = Double.MAX_VALUE;
					code = null;
					for(Entry<String, Geometry> epci : epcis.entrySet()) {
						
						distance = epci.getValue().distance(pSecteur);
						
						if(distance < distMin) {
							
							distMin = distance;
							code = epci.getKey();
						}
					}
					writer.write(code);
					writer.write(epciNames.get(code));
						
					distMin = Double.MAX_VALUE;
					code = null;
					for(Entry<String, Geometry> departement : departements.entrySet()) {
						
						distance = departement.getValue().distance(pSecteur);
						
						if(distance < distMin) {
							
							distMin = distance;
							code = departement.getKey();
						}
					}
					writer.write(code);
					writer.write(departementNames.get(code));

					distMin = Double.MAX_VALUE;
					code = null;
					for(Entry<String, Geometry> region : regions.entrySet()) {
						
						distance = region.getValue().distance(pSecteur);
						
						if(distance < distMin) {
							
							distMin = distance;
							code = region.getKey();
						}
					}
					writer.write(code);
					writer.write(regionNames.get(code));
					
				}else {
					
					for(int i=0; i<reader.getColumnCount(); i++) {
						
						writer.write(reader.get(i));
					}
				}
				
				writer.endRecord();
			}
			
			reader.close();
			writer.close();
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void integrateRegionDepartementEpciCommune() {
		
		String regionFile = "D://sig/region/RegionsL93.shp";
		String departementFile = "D://sig/departement/DepartementsL93.shp";
		String epciFile = "D://sig/epci/EPCIL93.shp";
		String communeFile = "D://sig/commune/CommunesL93.shp";
		
		try{
			// integration des regions
			ShpFiles sf = new ShpFiles(regionFile);
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
		
			Map<String, PreparedPolygon> regions = new TreeMap<String, PreparedPolygon>();
			Map<String, String> regionNames = new TreeMap<String, String>();
			
			Geometry the_geom;
			String code, nom;
			Object[] entry;
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
				
				regions.put(code, new PreparedPolygon((Polygonal) the_geom));
				regionNames.put(code, nom);
			}
			
			sfr.close();
			dfr.close();
			
			// integration des departements
			sf = new ShpFiles(departementFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
		
			Map<String, PreparedPolygon> departements = new TreeMap<String, PreparedPolygon>();
			Map<String, String> departementNames = new TreeMap<String, String>();
			
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
				
				//System.out.println(the_geom+" "+code+" "+nom);
				
				if(the_geom != null) {
					
					departements.put(code, new PreparedPolygon((Polygonal) the_geom));
					departementNames.put(code, nom);
				}
			}
			
			sfr.close();
			dfr.close();
			
			// integration des EPCIs
			sf = new ShpFiles(epciFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
					
			Map<String, PreparedPolygon> epcis = new TreeMap<String, PreparedPolygon>();
			Map<String, String> epciNames = new TreeMap<String, String>();
						
			while(sfr.hasNext()){
				
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[1];
							
				//System.out.println(the_geom+" "+code+" "+nom);
							
				if(the_geom != null) {
					
					epcis.put(code, new PreparedPolygon((Polygonal) the_geom));
					epciNames.put(code, nom);
				}
			}
						
			sfr.close();
			dfr.close();
			
			// integration des communes
			sf = new ShpFiles(communeFile);
			sfr = new ShapefileReader(sf, true, false, new GeometryFactory());
			dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
					
			Map<String, PreparedPolygon> communes = new TreeMap<String, PreparedPolygon>();
			Map<String, String> communeNames = new TreeMap<String, String>();
						
			while(sfr.hasNext()){
							
				the_geom = (Geometry) sfr.nextRecord().shape();
				entry = dfr.readEntry();
				code = (String) entry[0];
				nom = (String) entry[2];
						
				if(the_geom != null) {
					
					communes.put(code, new PreparedPolygon((Polygonal) the_geom));
					communeNames.put(code, nom);	
				}
			}
						
			sfr.close();
			dfr.close();
			
			CsvReader reader = new CsvReader("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement.csv");
			reader.setDelimiter(';');
			reader.readHeaders();
			
			CsvWriter writer = new CsvWriter("E:/data/rpg/secteur/secteurs_rpg20224_2000m_boisement_admin.csv");
			writer.setDelimiter(';');
			for(int i=0; i<reader.getHeaderCount(); i++) {
				
				writer.write(reader.getHeader(i));
			}
			writer.write("insee");
			writer.write("commune");
			writer.write("code_epci");
			writer.write("epci");
			writer.write("code_dep");
			writer.write("departement");
			writer.write("code_reg");
			writer.write("region");
			
			writer.endRecord();
			
			double x, y;
			Point pSecteur;
			WKTReader wkt = new WKTReader();
			while(reader.readRecord()) {
			
				for(int i=0; i<reader.getColumnCount(); i++) {
					
					writer.write(reader.get(i));
				}
				
				x = Double.parseDouble(reader.get("x"));
				y = Double.parseDouble(reader.get("y"));
				
				pSecteur = (Point) wkt.read("POINT ("+x+" "+y+")");
				
				for(Entry<String, PreparedPolygon> commune : communes.entrySet()) {
					
					if(commune.getValue().contains(pSecteur)) {
						
						code = commune.getKey();
						writer.write(code);
						writer.write(communeNames.get(code));
						break;
					}
				}
				
				for(Entry<String, PreparedPolygon> epci : epcis.entrySet()) {
					
					if(epci.getValue().contains(pSecteur)) {
						
						code = epci.getKey();
						writer.write(code);
						writer.write(epciNames.get(code));
						break;
					}
				}
				
				for(Entry<String, PreparedPolygon> departement : departements.entrySet()) {
					
					if(departement.getValue().contains(pSecteur)) {
						
						code = departement.getKey();
						writer.write(code);
						writer.write(departementNames.get(code));
						break;
					}
				}

				for(Entry<String, PreparedPolygon> region : regions.entrySet()) {
	
					if(region.getValue().contains(pSecteur)) {
		
						code = region.getKey();
						writer.write(code);
						writer.write(regionNames.get(code));
						break;
					}
				}
				
				writer.endRecord();
				
			}
			
			reader.close();
			writer.close();
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void scriptExploitationsFrance() {
		
		String path = "E:/data/rpg/data/france/francetransfert-1339765012/";
		
		Set<String> setInputs = new HashSet<String>();
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
		String[] parcellaires = setInputs.toArray(new String[setInputs.size()]);
		
		String attributeCodeEA = "pacage";
		
		//String[] codesExploitation = recuperationCodesExploitation(parcellaires, attributeCodeEA);
		
		Map<String, Set<Polygon>> exploitations = new TreeMap<String, Set<Polygon>>();
		ShapeFileTool.getSurfaceEntities(exploitations, parcellaires, attributeCodeEA);
		
		System.out.println("nombre d'explotations = "+exploitations.size());
		
		try {
			
			CsvWriter writer = new CsvWriter("E:/data/rpg/secteur/secteurs_rpg20224_2000m.csv");
			writer.setDelimiter(';');
			
			writer.write("code");
			writer.write("pacage");
			writer.write("numero");
			//writer.write("insee");
			writer.write("nb_parcelles");
			writer.write("surface");
			writer.write("x");
			writer.write("y");
			writer.write("xmin");
			writer.write("xmax");
			writer.write("ymin");
			writer.write("ymax");
			writer.endRecord();
			
			//Map<String, Map<Integer, Set<Polygon>>> secteurs = new HashMap<String, Map<Integer, Set<Polygon>>>();
			//Map<String, Map<Integer, Envelope>> enveloppeSecteurs = new HashMap<String, Map<Integer, Envelope>>();
			int nbExploitations = 0;
			GeometryFactory gf = new GeometryFactory();
			Point p;
			Envelope env;
			int num;
			double surface;
			for(Entry<String, Set<Polygon>> exploitation : exploitations.entrySet()){
				
				num = 1;
				
				Map<Integer, Set<Polygon>> secteursExploitation = DiagnosticGrainBocagerExploitation.detecteSecteursExploitation(exploitation.getValue(), exploitation.getKey(), 2000);
				
				System.out.println((++nbExploitations)+" : exploitation "+exploitation.getKey()+" avec "+exploitation.getValue().size()+" parcelles regroupees en "+secteursExploitation.size()+" secteurs");
				
				for(Entry<Integer, Set<Polygon>> secteur : secteursExploitation.entrySet()){
					
					GeometryCollection parcellesDuSecteur = gf.createMultiPolygon(secteur.getValue().toArray(new Polygon[secteur.getValue().size()]));
					
					p = parcellesDuSecteur.getInteriorPoint();
					
					env = parcellesDuSecteur.getEnvelopeInternal();
					
					surface = parcellesDuSecteur.getArea()/10000.0;
					
					writer.write(exploitation.getKey()+"_"+num);
					writer.write(exploitation.getKey());
					writer.write(num+"");
					//writer.write("insee");
					writer.write(secteur.getValue().size()+"");
					writer.write(Util.format(surface));
					writer.write(Util.format(p.getX()));
					writer.write(Util.format(p.getY()));
					writer.write(Util.format(env.getMinX()));
					writer.write(Util.format(env.getMaxX()));
					writer.write(Util.format(env.getMinY()));
					writer.write(Util.format(env.getMaxY()));
					writer.endRecord();
					
					num++;
				}
				
				//secteurs.put(exploitation.getKey(), secteursExploitation);
				
				//System.out.println("nombre de secteurs de l'exploitation "+exploitation.getKey()+" = "+secteursExploitation.size());
				
				/*
				enveloppeSecteurs.put(exploitation.getKey(), new TreeMap<Integer, Envelope>());
				
				for(Entry<Integer, Set<Polygon>> secteur : secteursExploitation.entrySet()){
					
					//System.out.println("secteur "+secteur.getKey()+" avec "+secteur.getValue().size()+" parcelles");
					
					enveloppeSecteurs.get(exploitation.getKey()).put(secteur.getKey(), ShapeFileTool.getEnvelope(secteur.getValue(), 10));
				}
				*/
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void scriptExploitationsBretagne() {
		
		//String parcellaire = "C:/Data/data/rpg/bretagne_2023/test_rpg.shp";
		//String parcellaire = "C:/Data/data/rpg/bretagne_2023/test2_rpg.shp";
		String parcellaire = "C:/Data/data/rpg/bretagne_2023/parcelles_geom_2023_r53.shp";
		String attributeCodeEA = "pacage";
		
		String[] codesExploitation = recuperationCodesExploitation(parcellaire, attributeCodeEA);
		//String[] codesExploitation = new String[]{"035177652", "035179895"};
		//String[] codesExploitation = new String[]{"056047987"};
		
		Map<String, Set<Polygon>> exploitations = ShapeFileTool.getSurfaceEntities(parcellaire, attributeCodeEA);
		
		Map<String, Map<Integer, Set<Polygon>>> secteurs = new HashMap<String, Map<Integer, Set<Polygon>>>();
		
		Map<String, Map<Integer, Envelope>> enveloppeSecteurs = new HashMap<String, Map<Integer, Envelope>>();

		for(Entry<String, Set<Polygon>> exploitation : exploitations.entrySet()){
			
			//System.out.println("exploitation "+exploitation.getKey()+" avec "+exploitation.getValue().size()+" parcelles");
			
			Map<Integer, Set<Polygon>> secteursExploitation = DiagnosticGrainBocagerExploitation.detecteSecteursExploitation(exploitation.getValue(), exploitation.getKey(), 2000);
			
			secteurs.put(exploitation.getKey(), secteursExploitation);
			
			enveloppeSecteurs.put(exploitation.getKey(), new TreeMap<Integer, Envelope>());
			
			//System.out.println("nombre de secteurs de l'exploitation "+exploitation.getKey()+" = "+secteursExploitation.size());
			
			for(Entry<Integer, Set<Polygon>> secteur : secteursExploitation.entrySet()){
				
				//System.out.println("secteur "+secteur.getKey()+" avec "+secteur.getValue().size()+" parcelles");
				
				enveloppeSecteurs.get(exploitation.getKey()).put(secteur.getKey(), getEnvelope(secteur.getValue()));
			}
		}
		//System.out.println("nombre d'exploitations = "+exploitations.size());
	
		// construction de la structure de données	

		Map<String, Set<Geometry>> buffersIntermediaires = new HashMap<String, Set<Geometry>>();
		
		GeometryFactory gf = new GeometryFactory();
		for(Entry<String, Set<Polygon>> exploitation : exploitations.entrySet()){
			
			System.out.println("exploitation "+exploitation.getKey());
			
			buffersIntermediaires.put(exploitation.getKey(), new HashSet<Geometry>());
			
			Map<Integer, Set<Polygon>> secteursExploitation = secteurs.get(exploitation.getKey());
			
			for(Entry<Integer, Set<Polygon>> secteur : secteursExploitation.entrySet()){
		        
		        GeometryCollection parcellesDuSecteur = gf.createMultiPolygon(secteur.getValue().toArray(new Polygon[secteur.getValue().size()]));
		        Geometry dilationErosionDesParcelles = parcellesDuSecteur.buffer(10).buffer(-5);
		        
		        if(dilationErosionDesParcelles instanceof GeometryCollection) {
		        	
		        	Set<Polygon> newParcelles = new HashSet<Polygon>();		        	
		        	for(int i=0; i<((GeometryCollection) dilationErosionDesParcelles).getNumGeometries(); i++) {
		        		
		        		Geometry newParcelle = ((GeometryCollection) dilationErosionDesParcelles).getGeometryN(i);
		        		
		        		if(newParcelle instanceof Polygon) {
		        			newParcelles.add((Polygon) newParcelle);
		        		}
		        	}
		        	dilationErosionDesParcelles = gf.createMultiPolygon(newParcelles.toArray(new Polygon[newParcelles.size()]));
		        }
		        
		        Geometry parcellesDuSecteur2 = null;
		        
		        parcellesDuSecteur2 = parcellesDuSecteur.buffer(0);
		        
		        if (!parcellesDuSecteur.isValid()) {
		        	//System.out.println("pass");
		        	parcellesDuSecteur2 = parcellesDuSecteur.buffer(0);
		        }else {
		        	parcellesDuSecteur2 = parcellesDuSecteur;
		        }
		        if (!dilationErosionDesParcelles.isValid()) dilationErosionDesParcelles = dilationErosionDesParcelles.buffer(0);
		        
		        //System.out.println(parcellesDuSecteur2.getClass()+" "+dilationErosionDesParcelles.getClass());
		        
		        Geometry unionA = UnaryUnionOp.union(parcellesDuSecteur2);
		        Geometry unionB = UnaryUnionOp.union(dilationErosionDesParcelles);
		        
		        //System.out.println(unionA.getClass()+" "+unionB.getClass());
		        
		        Geometry result = unionA.difference(unionB);
		        
		        /*
		        
		        if(dilationErosionDesParcelles instanceof GeometryCollection) {
		        	
		        	Set<Polygon> newParcelles = new HashSet<Polygon>();		        	
		        	for(int i=0; i<((GeometryCollection) dilationErosionDesParcelles).getNumGeometries(); i++) {
		        		
		        		Geometry newParcelle = ((GeometryCollection) dilationErosionDesParcelles).getGeometryN(i);
		        		
		        		if(newParcelle instanceof Polygon) {
		        			newParcelles.add((Polygon) newParcelle);
		        		}
		        	}
		        	
		        	dilationErosionDesParcelles = gf.createGeometryCollection(newParcelles.toArray(new Polygon[newParcelles.size()]));
		        }
		        
		      //  Geometry buffersDesParcelles = dilationErosionDesParcelles.difference(parcellesDuSecteur);
				
		        
		        Geometry buffersDesParcelles = dilationErosionDesParcelles;
	        	
	        	for(Polygon parcelle : secteur.getValue()) {
				
	        		System.out.println(buffersDesParcelles.getClass());
	        		
	        		buffersDesParcelles = buffersDesParcelles.difference(parcelle);
	        		
	        		UnaryUnionOp.union(null);
				}
				*/
		        
				//
				
				//buffersIntermediaires.get(exploitation.getKey()).add(buffersDesParcelles);
			}
		}
		
		
		/*
		for(Entry<String, Set<Geometry>> bufferIntermediaire : buffersIntermediaires.entrySet()) {
			
			double totalArea = 0.0;
			for(Geometry buffer : bufferIntermediaire.getValue()) {
				
				totalArea += buffer.getArea();
			}
			totalArea /= 10000.0;
			
			System.out.println("exploitation "+bufferIntermediaire.getKey()+", surface de buffer externe "+totalArea+" hectares");
			
		}
		*/
		/*
		Map<String, Map<Integer, Set<Polygon>>> buffersParExploitations; // pour chaque code exploitation (String), pour chaque "n" nombre d'exploitation à partager le buffer, la liste des buffers
		
		for(explitation1 : exploitations) {
			for(explitation2 : exploitations) {
			}		
		}
		
		buffersParExploitations = new HashMap<String, Map<Integer, Set<Polygon>>>();
		
			Map<Integer, Set<Polygon>> buffersParNombre = new TreeMap<Integer, Set<Polygon>>();
			
			int n = 0; //nombre d'exploitatin partageant le buffer
			
			Set<Polygon> buffers = new HashSet<Polygon>();
			
			Polygon parcelle = null;
			
			buffers.add(parcelle);
			
			buffersParNombre.put(n, null);
			
			buffersParExploitations.put(exploitation.getKey(), null);
		}
		*/
		
		/*
		// utlisation de cette strcuture pour générer une sortie
		try {
			CsvWriter cw = new CsvWriter("chemin/vers/fichier.csv");
			cw.setDelimiter(';');
			
			cw.write("exploitation");
			cw.write("scenario");
			cw.write("secteur");
			cw.endRecord();
			
			while(condition()) {
				
				cw.write("exploitation1");
				cw.write("scenario2");
				cw.write("secteur3");
				cw.endRecord();
			}
			
			cw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		/*
		String outputFolder = "F:/FDCCA/diag_ea/data/analyse_bretagne/";
		
		boolean exportMap = true;
		
		CsvWriter writer = DiagnosticGrainBocagerExploitation.prepareIndices("F:/FDCCA/diag_ea/data/analyse_bretagne/analyse_bretagne_ea_test.csv");
		
		int nb = 0;
		for(String codeExploitation : codesExploitation){
			System.out.println("diagnostique sur exploitation "+codeExploitation);
			if(scriptExploitation(outputFolder, codeExploitation, exploitations.get(codeExploitation), 5, writer, exportMap)){
				nb++;
			}
		}
		System.out.println("nombre d'EA analysees = "+nb+" / "+codesExploitation.length);
		DiagnosticGrainBocagerExploitation.fermeIndices(writer);
		*/
	}
	
	private static Envelope getEnvelope(Set<Polygon> parcelles) {
		
		double minx = Double.MAX_VALUE;
		double maxx = 0.0;
		double miny = Double.MAX_VALUE;
		double maxy = 0.0;
		
		Envelope e;
		for(Polygon p : parcelles) {
			
			e = p.getEnvelopeInternal();
			
			minx = Math.min(minx, e.getMinX());
			maxx = Math.max(maxx, e.getMaxX());
			miny = Math.min(miny, e.getMinY());
			maxy = Math.min(maxy, e.getMaxY());
		}
		
		return new Envelope(minx, maxx, miny, maxy);
	}

	private static boolean scriptExploitation(String outputFolder, String exploitation, Set<Polygon> parcelles, int outCellSize, CsvWriter writer, boolean exportMap) {
		
		DiagnosticGrainBocagerExploitationManager manager = new DiagnosticGrainBocagerExploitationManager();
		
		manager.setOutputFolder(outputFolder);
		if(writer != null) {
			manager.setWriter(writer);
		}
		
		manager.setAttributCodeEA("pacage"); // attribut_code_ea="pacage"
		manager.setCodeEA(exploitation); // code_ea="05376853"
	
		// definition du territoire d'exploiattion
		//manager.setParcellaire("F:/data/sig/RPG/rpg-2020/surfaces_2020_toutes_parcelles_graphiques_constatees_exploitants_FRCB.shp");
		//manager.setParcellaire(outputFolder+"056046330/056046330.shp"); // parcellaire="chemin/vers/shape.shp"
		
		manager.setParcelles(parcelles);
		
		// definition du bocage
		manager.setBocage("F:/FDCCA/diag_ea/data/mnhc/"); // bocage="chemin/vers/bocage.tif"
		
		//manager.setZoneBocageExploitation("F:/data/sig/RPG/rpg-2020/surfaces_2020_toutes_parcelles_graphiques_constatees_exploitants_FRCB.shp");
		//manager.setBufferZoneBocageExploitation(10);
		
		
		manager.setScenario("initial");
		//manager.setAmenagement("F:/FDCCA/diag_ea/data/analyse_bretagne/056047987/amenagement_056047987.shp");
		//manager.setAmenagement(outputFolder+"056046330/amenagement.shp"); // managment="chemin/vers/shape.shp"
		//manager.setScenario("scenario0");
		//manager.setScenario("scenario1");
		//manager.setScenario("scenario2");
		//manager.setScenario("scenario3");
		//manager.setScenario("hauteur"); // scenario_managment=""scenario1"
		
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
	
	private static String[] recuperationCodesExploitation(String[] parcellaires, String attributeCodeEA){
		
		Set<String> codesExploitation = new TreeSet<String>();
		
		try{
			
			for(String parcellaire : parcellaires) {
			
				System.out.println(parcellaire);
				
				ShpFiles sf = new ShpFiles(parcellaire);
				DbaseFileReader dfr = new DbaseFileReader(sf, false, Charset.defaultCharset());
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
			
				String code;
				Object[] entry;
				while(dfr.hasNext()){
					entry = dfr.readEntry();
					code = (String) entry[ideaNumber];
					codesExploitation.add(code);
				}
				
				dfr.close();
			}
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return codesExploitation.toArray(new String[codesExploitation.size()]);
	}
	
 	private static String[] recuperationCodesExploitation(String parcellaire, String attributeCodeEA){
		//String parcellaire = "F:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp";
		//String parcellaire = "C:/Data/data/rpg/bretagne_2023/parcelles_geom_2023_r53.shp";
		//String attributeCodeEA = "pacage";
		
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
 	
 	private static void recuperationEnveloppesExploitations(){
 		
		//String parcellaire = "F:/data/sig/RPG/rpg-2020/surfaces_2020_parcelles_graphiques_constatees_FRCB.shp";
		String parcellaire = "C:/Data/data/rpg/bretagne_2023/parcelles_geom_2023_r53.shp";
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
		
			Map<String, Double> minXEnveloppesExploitation = new TreeMap<String, Double>();
			Map<String, Double> maxXEnveloppesExploitation = new TreeMap<String, Double>();
			Map<String, Double> minYEnveloppesExploitation = new TreeMap<String, Double>();
			Map<String, Double> maxYEnveloppesExploitation = new TreeMap<String, Double>();
			
			Geometry the_geom;
			String code;
			Object[] entry;
			while(sfr.hasNext()){
				the_geom = (Geometry) sfr.nextRecord().shape();
				
				entry = dfr.readEntry();
				code = (String) entry[ideaNumber];
				
				if(!minXEnveloppesExploitation.containsKey(code)) {
					
					minXEnveloppesExploitation.put(code, the_geom.getEnvelopeInternal().getMinX());
					maxXEnveloppesExploitation.put(code, the_geom.getEnvelopeInternal().getMaxX());
					minYEnveloppesExploitation.put(code, the_geom.getEnvelopeInternal().getMinY());
					maxYEnveloppesExploitation.put(code, the_geom.getEnvelopeInternal().getMaxY());
			
				}else {
					
					minXEnveloppesExploitation.put(code, Math.min(minXEnveloppesExploitation.get(code), the_geom.getEnvelopeInternal().getMinX()));
					maxXEnveloppesExploitation.put(code, Math.max(maxXEnveloppesExploitation.get(code), the_geom.getEnvelopeInternal().getMaxX()));
					minYEnveloppesExploitation.put(code, Math.min(minYEnveloppesExploitation.get(code), the_geom.getEnvelopeInternal().getMinY()));
					maxYEnveloppesExploitation.put(code, Math.max(maxYEnveloppesExploitation.get(code), the_geom.getEnvelopeInternal().getMaxY()));
				}
				
			}
			
			sfr.close();
			dfr.close();
			
			Map<String, Envelope> enveloppesExploitation = new TreeMap<String, Envelope>();
			double max = 0;
			String eaMax = "";
			for(String ea : minXEnveloppesExploitation.keySet()) {
				Envelope env = new Envelope(minXEnveloppesExploitation.get(ea), maxXEnveloppesExploitation.get(ea), minYEnveloppesExploitation.get(ea), maxYEnveloppesExploitation.get(ea));
				enveloppesExploitation.put(ea, env);
				System.out.println("ea "+ea+" : "+env.getArea()/10000.0+" ha");
				if(env.getArea()/10000.0 > max) {
					max = env.getArea()/10000.0;
					eaMax = ea;
				}
			}
			
			System.out.println("max "+eaMax+" : "+max+" ha");
			
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
