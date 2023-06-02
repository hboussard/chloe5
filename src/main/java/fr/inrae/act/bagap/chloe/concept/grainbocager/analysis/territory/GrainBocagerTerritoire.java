package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.territory;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class GrainBocagerTerritoire {


	private static int buffer = 350; 	// buffer d'influence en metres 
										// 100m pour le calcul des distances
										// 250m pour la fenetre glissante 
	
	private String outputPath; // dossier de génération des sorties
	
	private String bocage; // tuiles MNHC
	
	private double seuil; // seuil de fonctionnalite du grain bocager
	
	private String territoire; // territoire d'analyse
	
	private String name; // nom identifiant du territoire
	
	private String replantationBocagere; // lineaire bocagere imagine
	
	private boolean modeFast; 				// mode FAST (mais faux)
	
	private double outputCellSize;			// du pixel de sortie
	
	private EnteteRaster refEntete;
	
	public GrainBocagerTerritoire(String outputPath, String territoire, String name, String bocage, double seuil, String replantationBocagere, boolean modeFast, double outputCellSize){
		this.outputPath = outputPath;
		this.territoire = territoire;
		this.name = name;
		this.bocage = bocage;
		this.seuil = seuil;
		this.replantationBocagere = replantationBocagere;
		this.modeFast = modeFast;
		this.outputCellSize = outputCellSize;
	}
	
	public void run(){

		// creation du repertoire de sortie
		Util.createAccess(outputPath);
		
		// récupération de l'enveloppe totale de travail
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(territoire, 2*buffer);
				
		// récupération de l'entete
		refEntete = EnteteRaster.getEntete(envelope, 5, -1);
		
		// lancement du grain bocager
		runGrainBocager(outputPath, name, territoire, bocage, seuil, refEntete);
		
		if(replantationBocagere != null && !replantationBocagere.equalsIgnoreCase("")){
			// lancement du grain bocager alternatif
			runGrainBocagerAlternatif(outputPath, name, territoire, bocage, replantationBocagere, seuil, refEntete);
		}
	}
	
	private void runGrainBocagerAlternatif(String path, String name, String zone, String bocage, String replantation, double seuil, EnteteRaster entete) {
		
		// recuperation des elements boises
		float[] dataHauteurBoisementTotal = getElementsBoises(bocage, entete);
		//CoverageManager.writeGeotiff(path+name+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
		
		Coverage covReplantation = ShapeFile2CoverageConverter.getLinearCoverage(replantation, "code", entete.cellsize(), entete.noDataValue(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), 0, entete.cellsize());
		float[] dataReplantation = covReplantation.getDatas();
		covReplantation.dispose();
		//CoverageManager.writeGeotiff(path+name+"_replantation.tif", dataReplantation, entete);
		
		Pixel2PixelTabCalculation pptc = new Pixel2PixelTabCalculation(dataHauteurBoisementTotal, dataHauteurBoisementTotal, dataReplantation){
			@Override
			protected float doTreat(float[] v) {
				return Math.max(v[0], v[1]);
			}
		};
		pptc.run();
		CoverageManager.writeGeotiff(path+name+"_hauteur_boisement_simule.tif", dataHauteurBoisementTotal, entete);
		
		// calcul du grain bocager total
		Coverage covGrainBocagerTotal = GrainBocager.calculate(dataHauteurBoisementTotal, entete, modeFast, outputPath+name, outputCellSize);
		CoverageManager.writeGeotiff(path+name+"_grain_bocager_simule.tif", covGrainBocagerTotal.getDatas(), covGrainBocagerTotal.getEntete());
		covGrainBocagerTotal.dispose();
		
	}
	
	private void runGrainBocager(String path, String name, String zone, String bocage, double seuil, EnteteRaster entete) {
		
		// recuperation des elements boises
		float[] dataHauteurBoisementTotal = getElementsBoises(bocage, entete);
		CoverageManager.writeGeotiff(path+name+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
		
		// calcul du grain bocager total
		Coverage covGrainBocagerTotal = GrainBocager.calculate(dataHauteurBoisementTotal, entete, modeFast, outputPath+name, outputCellSize);
		CoverageManager.writeGeotiff(path+name+"_grain_bocager.tif", covGrainBocagerTotal.getDatas(), covGrainBocagerTotal.getEntete());
		covGrainBocagerTotal.dispose();
		
	}
	
	private float[] getElementsBoises(String bocage, EnteteRaster entete) {
		
		System.out.println("récupération des éléments boisés");
		
		Coverage covBoisement = CoverageManager.getCoverage(bocage);
		EnteteRaster enteteMNHC = covBoisement.getEntete();
		float[] dataBoisement = covBoisement.getDatas(EnteteRaster.getROI(enteteMNHC, new Envelope(entete.minx(), entete.maxx(), entete.miny(), entete.maxy())));
		covBoisement.dispose();
		
		return dataBoisement;
	}

}
