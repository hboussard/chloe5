package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.territory;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
//import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class GrainBocagerTerritoire {


	/*private static int buffer = 350; 	// buffer d'influence en metres 
										// 100m pour le calcul des distances
										// 250m pour la fenetre glissante 
	*/
	private String outputPath; // dossier de génération des sorties
	
	private String bocage; // tuiles MNHC
	
	private double seuil; // seuil de fonctionnalite du grain bocager
	
	private String territoire; // territoire d'analyse
	
	private String name; // nom identifiant du territoire
	
	private String replantationBocagere; // lineaire bocagere imagine
	
	private boolean modeFast; 				// mode FAST (mais imprecie)
	
	private double outputCellSize;			// du pixel de sortie
	
	private int bufferArea; 			// buffer d'influence en metres autour du territoire d'analyse
	
	private EnteteRaster entete;
	
	public GrainBocagerTerritoire(String outputPath, String territoire, String name, String bocage, double seuil, String replantationBocagere, boolean modeFast, double outputCellSize, int bufferArea){
		this.outputPath = outputPath;
		this.territoire = territoire;
		this.name = name;
		this.bocage = bocage;
		this.seuil = seuil;
		this.replantationBocagere = replantationBocagere;
		this.modeFast = modeFast;
		this.outputCellSize = outputCellSize;
		this.bufferArea = bufferArea;
	}
	
	public void run(){

		// creation du repertoire de sortie
		//Util.createAccess(outputPath);
		
		// récupération de l'entete du bocage
		Coverage covBocage = CoverageManager.getCoverage(bocage);
		entete = covBocage.getEntete();
		covBocage.dispose();
		
		// récupération de l'enveloppe totale de travail
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(territoire, bufferArea);
		
		// récupération de l'entete
		entete = EnteteRaster.getEntete(entete, envelope);
		
		// lancement du grain bocager
		runGrainBocager();
		
		if(replantationBocagere != null && !replantationBocagere.equalsIgnoreCase("")){
			// lancement du grain bocager alternatif
			runGrainBocagerAlternatif("simule");
		}
	}
	
	private void runGrainBocager() {
		
		// recuperation des elements boises
		float[] dataHauteurBoisementTotal = getElementsBoises(bocage, entete);
		CoverageManager.writeGeotiff(outputPath+name+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
		
		// calcul du grain bocager total
		String size = ((int) outputCellSize)+"";
		//Coverage covGrainBocagerTotal = GrainBocager.allRun(dataHauteurBoisementTotal, entete, modeFast, outputPath+name, outputCellSize);
		//CoverageManager.writeGeotiff(outputPath+name+"_grain_bocager_"+size+"m.tif", covGrainBocagerTotal.getData(), covGrainBocagerTotal.getEntete());
		//covGrainBocagerTotal.dispose();
		
	}
	
	private void runGrainBocagerAlternatif(String scenario) {
		
		// recuperation des elements boises
		float[] dataHauteurBoisementTotal = getElementsBoises(bocage, entete);
		//CoverageManager.writeGeotiff(outputPath+name+"_"+scenario+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
		
		Coverage covReplantation = ShapeFile2CoverageConverter.getLinearCoverage(replantationBocagere, "code", entete.cellsize(), entete.noDataValue(), entete.crs(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), 0, entete.cellsize());
		float[] dataReplantation = covReplantation.getData();
		covReplantation.dispose();
		CoverageManager.writeGeotiff(outputPath+name+"_"+scenario+"_replantation.tif", dataReplantation, entete);
		
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(dataHauteurBoisementTotal, dataHauteurBoisementTotal, dataReplantation){
			@Override
			protected float doTreat(float[] v) {
				return Math.max(v[0], v[1]);
			}
		};
		pptc.run();
		CoverageManager.writeGeotiff(outputPath+name+"_"+scenario+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
		
		// calcul du grain bocager total
		String size = ((int) outputCellSize)+"";
		//Coverage covGrainBocagerTotal = GrainBocager.allRun(dataHauteurBoisementTotal, entete, modeFast, outputPath+name+"_"+scenario, outputCellSize);
		//CoverageManager.writeGeotiff(outputPath+name+"_"+scenario+"_grain_bocager_"+size+"m.tif", covGrainBocagerTotal.getData(), covGrainBocagerTotal.getEntete());
		//covGrainBocagerTotal.dispose();
		
	}
	
	private float[] getElementsBoises(String bocage, EnteteRaster entete) {
		
		//System.out.println("récupération des éléments boisés");
		
		Coverage covBoisement = CoverageManager.getCoverage(bocage);
		EnteteRaster enteteMNHC = covBoisement.getEntete();
		float[] dataBoisement = covBoisement.getData(EnteteRaster.getROI(enteteMNHC, new Envelope(entete.minx(), entete.maxx(), entete.miny(), entete.maxy())));
		covBoisement.dispose();
		
		return dataBoisement;
	}

}
