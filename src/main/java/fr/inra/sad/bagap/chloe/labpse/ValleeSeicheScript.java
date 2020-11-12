package fr.inra.sad.bagap.chloe.labpse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.ArrayMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.JaiMatrixFactory;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;

public class ValleeSeicheScript {

	private static String path = "F:/Requete_SIG_LabPSE/vallee_de_la_seiche/data/";
	
	public static void main(String[] args) {
		
		//entete();
		retile1();
		retile2();
		retile3();
	}
	
	private static void entete(){
		try {
			String ascii2 = path+"pente/pente_clean2.asc";
			BufferedWriter writer = new BufferedWriter(new FileWriter(ascii2));
			
			writer.write("ncols 12640");
			writer.newLine();
			writer.write("nrows 6153");
			writer.newLine();
			writer.write("xllcorner 341809.68854193");
			writer.newLine();
			writer.write("yllcorner 6757921.69715393");
			writer.newLine();
			writer.write("cellsize 5.0");
			writer.newLine();
			writer.write("NODATA_value -1");
			writer.newLine();
			
			String ascii1 = path+"pente/pente_clean.asc";
			BufferedReader reader = new BufferedReader(new FileReader(ascii1));
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			
			while(reader.ready()){
				writer.write(reader.readLine());
				writer.newLine();
			}
			
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retile1(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"raster/carto_vallee_seiche_eau_dessus.asc", false);
			Matrix m2 = MatrixManager.retile(m, 2000, 2000, 1000, 1000);
			MatrixManager.exportAsciiGridAndVisualize(m2, path+"raster/test/carto_vallee_seiche_eau_dessus.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retile2(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"erosion/carte_friction_erosion4.asc", false);
			Matrix m2 = MatrixManager.retile(m, 2000, 2000, 1000, 1000);
			MatrixManager.exportAsciiGridAndVisualize(m2, path+"raster/test/carte_friction_erosion4.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void retile3(){
		try {
			Matrix m = JaiMatrixFactory.get().createWithAsciiGridOld(path+"pente/pente_clean2.asc", false);
			Matrix m2 = MatrixManager.retile(m, 2000, 2000, 1000, 1000);
			MatrixManager.exportAsciiGridAndVisualize(m2, path+"raster/test/pente_clean.asc");
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

}
