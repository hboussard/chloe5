package fr.inrae.act.bagap.chloe.concept.grainbocager.script;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.util.MultiVolume7z;
import fr.inrae.act.bagap.apiland.util.Tool;
import fr.inrae.act.bagap.apiland.raster.Coverage;

public class ScriptCarteGouvFR {

	public static void main(String[] args) {
		
		//generateMetadata();
		//maxNbCluster();
		//copyAndRenameData();
		
		//copyAndRenameData("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D030_2021-01-01/", "D:/grain_bocager/data/30/2021/", "30", 2021);
		try {
			
			MultiVolume7z.create("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D030_2021-01-01/", "C:/Data/projet/grain_bocager/archive/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D030_2021-01-01", "4g");
		
			//Tool.deleteFolder("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D030_2021-01-01/");
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		//ZipUtil.zip("C:/Data/projet/grain_bocager/archive/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D001_2021-01-01.zip", "C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D001_2021-01-01/");
		//Tool.deleteFolder("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D001_2021-01-01/");
			
	}
	
	private static void generateMetadata() {
		
		String path = "D:/grain_bocager/data/";
		File folder = new File(path);
		
		for(File dptFolder : folder.listFiles()) {
			
			String dpt = dptFolder.getName();
			
			for(File yearFolder : dptFolder.listFiles()) {
				
				int year = Integer.parseInt(yearFolder.getName());
				
				if(year > 2020) {
					
					System.out.println("metadata "+dpt+" "+year);
					
					generateMetadata(dpt, year, 1, 0);
					
				}
			}
		}
		
	}
	
	private static void generateMetadata(String dpt, int year, int version, int rc) {

        try {
            // create a FileWriter object with the file name
            FileWriter writer = new FileWriter("D:/grain_bocager/data/"+dpt+"/"+year+"/Grain_Bocager_metadata_"+dpt+"_"+year+"_"+version+"-"+rc+".txt");

            // write the string to the file
            writer.write("Voici les métadonnées du Grain Bocager sur le département '"+dpt+"' à l'année "+year+", version "+version+"."+rc+".\n");
            writer.write("\n");
            writer.write("Résumé : Le produit Grain Bocager est un ensemble de couches nationales de référence pour la gestion des bocages en lien avec la biodiversité forestière, en France métropolitaine. Ces couches sont issues d’une procédure de calculs de métriques paysagères développée par INRAE (UMR BAGAP) en collaboration avec IGN et déployée dans le cadre du projet Dispositif de Suivi des Bocages (DSB), OFB/IGN initié en 2017. La fourniture des données cartographiques au format GEOTIFF (et de leurs styles QGIS) suit une logique par département et par année.\n");
            writer.write("\n");
            writer.write("Les auteurs du Grain Bocager sont l'UMR INRAE-BAGAP, l'IGN, la Fédération départementale des Chasseurs 22 et l'OFB.\n");
            writer.write("\n");
            writer.write("Vous pouvez citer les données Grain Bocager de la manière suivante : \n");
            writer.write("Boussard, H., Commagnac, L., Meurice, P., Rolland, D., Baudry, J. Grain Bocager : un indice paysager pour évaluer la qualité d'un réseau bocager pour la biodiversité forestière. 2025, BAGAP INRAE. version 3D, source de données, MNHC IGN.\n");
            writer.write("\n");
            writer.write("Les données présentent dans ce package constituent l'ensemble des données du Grain Bocager établies sur le département n°"+dpt+" pour l'année "+year+", produites à partir de données MNHC (IGN), elles-mêmes constituées à partir des données MNS (IGN) produites à l'année "+year+".\n");
            writer.write("\n");
            writer.write("Le numéro de version '1.0' indique que ces données ont été produites lors de la phase de calcul qui comprend l'ensemble des départements de France métropolitaine entre 2021 et 2023.\n");
            writer.write("\n");
            writer.write("Ces données ont été produites en 2025 dans le cadre du projet Dispositif de Suivi des Bocages (OFB/IGN).\n");
            writer.write("\n");
            writer.write("Ces données sont au format RASTER (GEOTIFF) à des résolutions variables et projetées en Lambert93 (EPSG:2154)\n");
            writer.write("\n");
            writer.write("Ces données sont libres de droit. Seule la citation des auteurs est obligatoire.\n");
            writer.write("\n");
            writer.write("Pour plus d'information, merci de vous référer au site de l'outil 'CHLOE - métriques paysagères' à l'adresse suivante : https://chloe.inrae.fr\n");
            writer.write("\n");
            writer.write("Pour toute question à la bonne utilisation des données du Grain Bocager ou toute demande de formation, merci d'envoyer un mail à chloe@inrae.fr\n");
            
            // close the writer
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	private static void copyAndRenameData() {
		
		String path = "D:/grain_bocager/data/";
		File folder = new File(path);
		
		for(File dptFolder : folder.listFiles()) {
			
			String dpt = dptFolder.getName();
			
			for(File yearFolder : dptFolder.listFiles()) {
				
				int year = Integer.parseInt(yearFolder.getName());
				
				if(year > 2020) {
					
					if(! new File("C:/Data/projet/grain_bocager/archive/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01.zip").exists()) {
						
						System.out.println(yearFolder.getAbsolutePath());
						
						copyAndRenameData("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01/", yearFolder.getAbsolutePath()+"/", dpt, year);
						
						//ZipUtil.zip("C:/Data/projet/grain_bocager/archive/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01.zip", );
						try {
							
							MultiVolume7z.create("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01/", "C:/Data/projet/grain_bocager/archive/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01", "4g");
						
							Tool.deleteFolder("C:/Data/projet/grain_bocager/data/GRAIN-BOCAGER_1-0__TIFF_LAMB93_D0"+dpt+"_"+year+"-01-01/");
							
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private static void copyAndRenameData(String outputFolder, String inputFolder, String dpt, int year) {

		String ressourcesFolder = "C:/Data/projet/grain_bocager/ressources/";
		
		Tool.createAccess(outputFolder);
		
		Tool.copy(ressourcesFolder+"Grain_Bocager_descriptif.pdf", outputFolder+"Grain_Bocager_descriptif.pdf");
		Tool.copy(ressourcesFolder+"Grain_Bocager_memo.pdf", outputFolder+"Grain_Bocager_memo.pdf");
		Tool.copy(inputFolder+"Grain_Bocager_metadata_"+dpt+"_"+year+"_1-0.txt", outputFolder+"Grain_Bocager_metadata_"+dpt+"_"+year+"_1-0.txt");

		Tool.copy(inputFolder+dpt+"_"+year+"_"+"hauteur_boisement.tif", outputFolder+"hauteur_boisement_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/hauteur_boisement.qml", outputFolder+"hauteur_boisement_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"type_boisement.tif", outputFolder+"type_boisement_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/type_boisement.qml", outputFolder+"type_boisement_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"distance_influence.tif", outputFolder+"distance_influence_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/distance_influence.qml", outputFolder+"distance_influence_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"grain_bocager_5m.tif", outputFolder+"grain_bocager_5m_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/grain_bocager_5m.qml", outputFolder+"grain_bocager_5m_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"grain_bocager_5m_4classes.tif", outputFolder+"grain_bocager_5m_4classes_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/grain_bocager_5m_4classes.qml", outputFolder+"grain_bocager_5m_4classes_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"grain_bocager_cluster_50m.tif", outputFolder+"cluster_grain_bocager_50m_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/cluster_grain_bocager_50m.qml", outputFolder+"cluster_grain_bocager_50m_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"proportion_grain_bocager_fonc_1km.tif", outputFolder+"proportion_grain_bocager_fonc_1km_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/proportion_grain_bocager_fonc_1km.qml", outputFolder+"proportion_grain_bocager_fonc_1km_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"fragmentation_grain_bocager_fonc_1km.tif", outputFolder+"fragmentation_grain_bocager_fonc_1km_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/fragmentation_grain_bocager_fonc_1km.qml", outputFolder+"fragmentation_grain_bocager_fonc_1km_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"proportion_grain_bocager_fonc_5km.tif", outputFolder+"proportion_grain_bocager_fonc_5km_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/proportion_grain_bocager_fonc_5km.qml", outputFolder+"proportion_grain_bocager_fonc_5km_"+dpt+"_"+year+".qml");
		
		Tool.copy(inputFolder+dpt+"_"+year+"_"+"fragmentation_grain_bocager_fonc_5km.tif", outputFolder+"fragmentation_grain_bocager_fonc_5km_"+dpt+"_"+year+".tif");
		Tool.copy(ressourcesFolder+"style/fragmentation_grain_bocager_fonc_5km.qml", outputFolder+"fragmentation_grain_bocager_fonc_5km_"+dpt+"_"+year+".qml");
		
	}

	private static void maxNbCluster() {
		
		String path = "D:/grain_bocager/data/";
		File folder = new File(path);
		
		int maxTotal = 0; 
		
		for(File dptFolder : folder.listFiles()) {
			
			String dpt = dptFolder.getName();
			
			for(File yearFolder : dptFolder.listFiles()) {
				
				int max = 0; 
				
				int year = Integer.parseInt(yearFolder.getName());
				
				if(year > 2020) {
					
					Coverage cov = CoverageManager.getCoverage(yearFolder.getAbsolutePath()+"/"+dpt+"_"+year+"_grain_bocager_cluster_50m.tif");
					float[] data = cov.getData();
					cov.dispose();
					
					for(float d : data) {
						
						max = Math.max(max, (int) d);
					}
					
					System.out.println(yearFolder.getAbsolutePath()+" --> "+max);
					
					maxTotal = Math.max(max, maxTotal);
				}
			}
		}
		
		System.out.println("max total --> "+maxTotal);
	}

}
