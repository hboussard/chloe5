package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import fr.inrae.act.bagap.apiland.util.Tool;

public class ScriptMotiver {

	public static void main(String [] args) {
		
		//copyMNHC();
	}
	
	private static void copyMNHC() {
		
		String inputPath = "D:/grain_bocager/data/";
		File folder = new File(inputPath);
		String outputPath = "D:/grain_bocager/mnhc/data/";
		
		for(File depFolder : folder.listFiles()) {
			
			//System.out.println(depFolder.getName());
			for(File yearFolder : depFolder.listFiles()) {
				
				//System.out.println(yearFolder.getName());
				
				for(String file : yearFolder.list()) {
					
					if(file.contains("hauteur_boisement")){
						//System.out.println(file);
						//System.out.println(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file);
						System.out.println("--> "+outputPath+file);
						//Tool.copy(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file, outputPath+file);
						try {
							Files.copy(new File(inputPath+depFolder.getName()+"/"+yearFolder.getName()+"/"+file).toPath(), 
									new File(outputPath+file).toPath(), 
									StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
