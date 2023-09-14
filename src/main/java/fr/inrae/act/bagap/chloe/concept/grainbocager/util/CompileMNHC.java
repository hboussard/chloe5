package fr.inrae.act.bagap.chloe.concept.grainbocager.util;

import java.io.File;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.core.element.manager.Tool;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class CompileMNHC {

	public static void compile(String outputPath, String outputName, String... inputPath){
		
		Util.createAccess(outputPath);
		
		File deptFolder;
		int it, jt;
		String outFile;
		String[] f;
		Coverage covOutFile, covInFile;
		float[] dataOutFile, dataInFile;
		EnteteRaster entete;
		for(String input : inputPath){
			deptFolder = new File(input);
			for(String file : deptFolder.list()){
				if(file.endsWith(".tif")){
					f = file.replace(".tif", "").split("_");
					
					it = Integer.parseInt(f[3]);
					jt = Integer.parseInt(f[4]);
					//System.out.println(it+" "+jt);
					
					outFile = outputPath+outputName+"_"+it+"_"+jt+".tif";
					//System.out.println(outFile);
					
					if(new File(outFile).exists()){
						
						//System.out.println("comparaison pour : "+outFile);
						
						//compare and update
						covOutFile = CoverageManager.getCoverage(outFile);
						dataOutFile = covOutFile.getData();
						entete = covOutFile.getEntete();
						covOutFile.dispose();
						
						covInFile = CoverageManager.getCoverage(deptFolder.getAbsolutePath()+"/"+file);
						dataInFile = covInFile.getData();
						covInFile.dispose();
						
						Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(dataOutFile, dataOutFile, dataInFile){
							@Override
							protected float doTreat(float[] v) {
								return Math.max(v[0], v[1]);
							}
						};
						pptc.run();
						CoverageManager.writeGeotiff(outFile, dataOutFile, entete);
						
					}else{
						Tool.copy(deptFolder.getAbsolutePath()+"/"+file, outFile);
					}
				}
			}
		}
		
	}
	
}
