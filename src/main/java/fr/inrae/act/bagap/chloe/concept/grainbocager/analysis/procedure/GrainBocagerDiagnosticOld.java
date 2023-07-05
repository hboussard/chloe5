package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

import org.locationtech.jts.geom.Envelope;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.TabCoverage;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class GrainBocagerDiagnosticOld {

	private GrainBocagerManager manager;
	
	public GrainBocagerDiagnostic(GrainBocagerManager manager){
		this.manager = manager;
	}
	
	public void run(){
		
		// récupération de l'entete
		EnteteRaster entete = recuperationEntete();
		
		for(String scenario : manager.scenarios()){
			
			// récupération des éléments boisés
			Coverage covHauteurBoisement = recuperationElementsBoises(entete);
			
			// détection des types de boisements
			detectionTypesBoisements(covHauteurBoisement);
			
			// calcul des distances d'influences
			calculDistancesInfluences();
			
			// calcul du grain bocager 
			calculGrainBocager();
			
			// seuillage de fonctionnalite
			seuillageFonctionnalite();
			
			// calcul des zones à enjeux();
			calculZonesAEnjeux();
		}
	
	}

	private EnteteRaster recuperationEntete() {
		// récupération de l'entete du bocage
		Coverage covBocage = CoverageManager.getCoverage(manager.bocage());
		EnteteRaster entete = covBocage.getEntete();
		covBocage.dispose();
				
		// récupération de l'enveloppe totale de travail
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(manager.territoire(), manager.bufferArea());
				
		// récupération de l'entete
		entete = EnteteRaster.getEntete(entete, envelope);
		
		return entete;
	}

	private Coverage recuperationElementsBoises(EnteteRaster entete) {
		// recuperation des elements boises
		float[] dataHauteurBoisementTotal = getElementsBoises(manager.bocage(), entete);
		//CoverageManager.writeGeotiff(path+name+"_hauteur_boisement.tif", dataHauteurBoisementTotal, entete);
				
		Coverage covReplantation = ShapeFile2CoverageConverter.getLinearCoverage(manager.replantationBocagere(), "code", entete.cellsize(), entete.noDataValue(), entete.minx(), entete.maxx(), entete.miny(), entete.maxy(), 0, entete.cellsize());
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
		CoverageManager.writeGeotiff(manager.outputPath()+manager.name()+"_hauteur_boisement_simule.tif", dataHauteurBoisementTotal, entete);
		
		return new TabCoverage(dataHauteurBoisementTotal, entete);
	}

	private Coverage detectionTypesBoisements(Coverage covHauteurBoisement) {
		return GrainBocager.detectionTypeBoisement(covHauteurBoisement, manager.modeFast(), manager.outputName());
	}

	private void calculDistancesInfluences() {
		GrainBocager.calculDistancesInfluences(null, null, null, false);
	}

	private void calculGrainBocager() {
		GrainBocager.calculGrainBocager(dataDistancePonderee, entete, outputName, outputCellSize);
	}

	private void seuillageFonctionnalite() {
		GrainBocager.runClassificationNClasses(, manager.seuils());
		
	}

	private void calculZonesAEnjeux() {
		// TODO Auto-generated method stub
		
	}

}
