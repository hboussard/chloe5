package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.calculgrainbocager.GBPCalculGrainBocagerFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.clusterisationfonctionnalite.GBPClusterisationFonctionnaliteFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.detectionboisement.GBPDetectionTypeBoisementFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.diagnosticexploitation.GBPDiagnosticExploitationFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.diagnosticterritoire.GBPDiagnosticTerritoireFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.distanceinfluence.GBPCalculDistanceInfluenceBoisementFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.enjeuxglobaux.GBPCalculEnjeuxGlobauxFactory;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.recuperationhauteur.GBPRecuperationHauteurBoisementFactory;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.Tile;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class GrainBocagerManager {

	private GrainBocagerProcedureFactory factory; // constructeur de procedure
	
	private EnteteRaster entete; 		// entete de travail
	
	private Tile tile;					// tuilage pour grandes analyses
	
	private List<String> scenarios;		// liste des scenarios
	
	private List<String> codesEA;		// liste des codes exploitations
	
	private String outputPath; 		// dossier de generation des sorties
	
	private String name;				// nom identifiant du territoire
	
	private String territoire;			// shapefile du territoire pour definir l'enveloppe
	
	private String enveloppe;			// envelopppe d'analyse
	
	private double bufferArea; 			// buffer autour du territoire d'analyse
	
	private String zoneBocage;			// shapefile des zones bocageres appartenant a l'acteur du territoire
	
	private String bocage;				// tuiles MNHC ou autre donnees de boisement
	
	private String hauteurBoisement;	// les hauteur de boisement
	
	private String typeBoisement;		// les types de boisement (masif, haie ou arbre isole)
	
	private String distanceInfluenceBoisement; // les distances d'influence des boisement (en fonctin de leur type et de leur hauteur)
	
	private String grainBocager;		// le grain bocager continue (de 0 a 1)
	
	private String grainBocager4Classes;	// le grain bocager classifie en 4 classes
	
	private String grainBocagerFonctionnel;	// le grain bocager fonctionnel
	
	private String clusterGrainBocagerFonctionnel;	// le grain bocager fonctionnel clusterise
	
	private String proportionGrainBocagerFonctionnel;	// les proportions de grain bocager fonctionnel (a une certaine echelle)
	
	private String zoneFragmentationGrainBocagerFonctionnel;	// les zones de fragmentation du grain bocager fonctionnel (a une certaine echelle)
	
	private String plantation;			// ajout de lineaires bocagers ou surfaces de bocage
	
	private String suppression;			// suppression de surfaces de bocage 
	
	private boolean modeFast; 			// mode FAST (imprecis mais rapide)
	
	private boolean force; 				// forcage des recalculations
	
	private double grainCellSize;			// taille du pixel du grain bocager
	
	private double grainWindowRadius;		// taille de la fen�tre d'analyse pour le grain bocager
	
	private double enjeuxCellSize;		// taille du pixel des enjeux
	
	private double enjeuxWindowRadius;	// taille de la fen�tre d'analyse pour les enjeux bocagers
	
	private double[] seuils; 			// seuil d'observation du grain bocager, le seuil de fonctionnalite est le deuxi�me
	
	private String attributCodeEA;		// attribut de code exploitation
	
	private String attributSecteur;		// attribut des secteurs
	
	private String attributHauteurPlantation;		// attribut des valeurs de hauteur dans la couche de plantation
	
	public GrainBocagerManager(String treatment){
		setTreatment(treatment);
		init();
	}
	
	private void setTreatment(String treatment){
		switch(treatment){
		case "diagnostique_exploitation" : 
			factory = new GBPDiagnosticExploitationFactory();
			break;
		case "diagnostique_territoire" : 
			factory = new GBPDiagnosticTerritoireFactory();
			break;
		case "recuperation_hauteur_boisement" : 
			factory = new GBPRecuperationHauteurBoisementFactory();
			break;
		case "detection_type_boisement" : 
			factory = new GBPDetectionTypeBoisementFactory();
			break;
		case "calcul_distance_influence_boisement" :
			factory = new GBPCalculDistanceInfluenceBoisementFactory();
			break;
		case "calcul_grain_bocager" : 
			factory = new GBPCalculGrainBocagerFactory();
			break;
		case "clusterisation_fonctionnalite" : 
			factory = new GBPClusterisationFonctionnaliteFactory();
			break;
		case "calcul_enjeux_globaux" : 
			factory = new GBPCalculEnjeuxGlobauxFactory();
			break;
		default : throw new IllegalArgumentException("treatment '"+treatment+"' do not exists");
		}
	}
	
	private void init(){
		
		// attributs avec valeur par defaut
		tile = null;
		seuils = new double[]{0.2, 0.33, 0.45};
		modeFast = true;
		force = false;
		grainCellSize = 5;
		grainWindowRadius = 250.0;
		enjeuxCellSize = 50;
		enjeuxWindowRadius = 1000.0;
		bufferArea = 0.0;
		scenarios = new ArrayList<String>();
		codesEA = new ArrayList<String>();
		attributCodeEA = "id_ea";
		attributSecteur = "secteur";
		attributHauteurPlantation = "hauteur";
		
		// attributs a redefinir 
		bocage = "";
		territoire = "";
		enveloppe = "";
		outputPath = "";
		name = "";
		plantation = "";
		suppression = "";
		hauteurBoisement = "";
		typeBoisement = "";
		distanceInfluenceBoisement = "";
		grainBocager = "";
		grainBocager4Classes = "";
		zoneBocage = "";
		grainBocagerFonctionnel = "";
		clusterGrainBocagerFonctionnel = "";
		proportionGrainBocagerFonctionnel = "";
		zoneFragmentationGrainBocagerFonctionnel = "";
	}
	
	private boolean initEntete(){
		
		if(!bocage.equalsIgnoreCase("")){
			
			// recuperation de l'entete du bocage
			Coverage covBocage = CoverageManager.getCoverage(bocage);
			entete = covBocage.getEntete();
			covBocage.dispose();
			
			if(!enveloppe.equalsIgnoreCase("") && bufferArea >= 0){
				
				String[] bornes = enveloppe.replace("{", "").replace("}", "").split(";");
				
				// recuperation de l'enveloppe totale de travail
				Envelope envelope = new Envelope(
						Double.parseDouble(bornes[0])-bufferArea, 
						Double.parseDouble(bornes[1])+bufferArea, 
						Double.parseDouble(bornes[2])-bufferArea, 
						Double.parseDouble(bornes[3])+bufferArea);
							
				// recuperation de l'entete
				entete = EnteteRaster.getEntete(entete, envelope);
				
			} else if(!territoire.equalsIgnoreCase("") && bufferArea >= 0){
				
				// recuperation de l'enveloppe totale de travail
				Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(territoire, bufferArea);
							
				// recuperation de l'entete
				entete = EnteteRaster.getEntete(entete, envelope);
			}
			
			return true;
		}
		
		if(!hauteurBoisement.equalsIgnoreCase("") && new File(hauteurBoisement).exists()){
			
			// recuperation de l'entete
			Coverage covHauteurBoisement = CoverageManager.getCoverage(hauteurBoisement);
			entete = covHauteurBoisement.getEntete();
			covHauteurBoisement.dispose();
			
			return true;
		}
		
		if(!distanceInfluenceBoisement.equalsIgnoreCase("") && new File(distanceInfluenceBoisement).exists()){
			
			// recuperation de l'entete
			Coverage covDistanceInfluence = CoverageManager.getCoverage(distanceInfluenceBoisement);
			entete = covDistanceInfluence.getEntete();
			covDistanceInfluence.dispose();
			
			return true;
		}
		
		if(!grainBocager.equalsIgnoreCase("") && new File(grainBocager).exists()){
			
			// recuperation de l'entete
			Coverage covGrainBocager = CoverageManager.getCoverage(grainBocager);
			entete = covGrainBocager.getEntete();
			covGrainBocager.dispose();
			
			return true;
		}
		
		if(!clusterGrainBocagerFonctionnel.equalsIgnoreCase("") && new File(clusterGrainBocagerFonctionnel).exists()){
			
			// recuperation de l'entete
			Coverage covClusterGrainBocager = CoverageManager.getCoverage(clusterGrainBocagerFonctionnel);
			entete = covClusterGrainBocager.getEntete();
			covClusterGrainBocager.dispose();
			
			return true;
		}
		
		return false;
	}
	
	public GrainBocagerProcedure build(){
		if(factory.check(this)){
			if(initEntete()){
				GrainBocagerProcedure gbProcedure = factory.create(this);
				return gbProcedure;
			}
		}
		
		throw new IllegalArgumentException("parameters are unconsistant");
	}
	
	public void setTile(Tile tile){
		this.tile = tile;
	}

	public void setBocage(String bocage) {
		this.bocage = bocage;
	}
	
	public void setHauteurBoisement(String hauteurBoisement) {
		this.hauteurBoisement = hauteurBoisement;
	}
	
	public void setTypeBoisement(String typeBoisement) {
		Util.createAccess(typeBoisement);
		this.typeBoisement = typeBoisement;
	}
	
	public void setDistanceInfluenceBoisement(String distanceInfluenceBoisement) {
		Util.createAccess(distanceInfluenceBoisement);
		this.distanceInfluenceBoisement = distanceInfluenceBoisement;
	}
	
	public void setGrainBocager(String grainBocager){
		Util.createAccess(grainBocager);
		this.grainBocager = grainBocager;
	}
	
	public void setGrainBocager4Classes(String grainBocager4Classes){
		Util.createAccess(grainBocager4Classes);
		this.grainBocager4Classes = grainBocager4Classes;
	}
	
	public void setGrainBocagerFonctionnel(String grainBocagerFonctionnel){
		Util.createAccess(grainBocagerFonctionnel);
		this.grainBocagerFonctionnel = grainBocagerFonctionnel;
	}
	
	public void setClusterGrainBocagerFonctionnel(String clusterGrainBocagerFonctionnel){
		Util.createAccess(clusterGrainBocagerFonctionnel);
		this.clusterGrainBocagerFonctionnel = clusterGrainBocagerFonctionnel;
	}
	
	public void setProportionGrainBocagerFonctionnel(String proportionGrainBocagerFonctionnel){
		Util.createAccess(proportionGrainBocagerFonctionnel);
		this.proportionGrainBocagerFonctionnel = proportionGrainBocagerFonctionnel;
	}
	
	public void setZoneFragmentationGrainBocagerFonctionnel(String zoneFragmentationGrainBocagerFonctionnel){
		Util.createAccess(zoneFragmentationGrainBocagerFonctionnel);
		this.zoneFragmentationGrainBocagerFonctionnel = zoneFragmentationGrainBocagerFonctionnel;
	}
	
	public void addScenario(String scenario){
		scenarios.add(scenario);
	}
	
	public void addCodeEA(String codeEA){
		codesEA.add(codeEA);
	}
	
	public void setGrainCellSize(double grainCellSize){
		this.grainCellSize = grainCellSize;
	}
	
	public void setGrainWindowRadius(double grainWindowRadius){
		this.grainWindowRadius = grainWindowRadius;
	}
	
	public void setEnjeuxCellSize(double enjeuxCellSize){
		this.enjeuxCellSize = enjeuxCellSize;
	}
	
	public void setEnjeuxWindowRadius(double enjeuxWindowRadius){
		this.enjeuxWindowRadius = enjeuxWindowRadius;
	}
	
	public void setSeuils(double seuil1, double seuil2, double seuil3) {
		this.seuils[0] = seuil1;
		this.seuils[1] = seuil2;
		this.seuils[2] = seuil3;
	}
	
	public void setSeuil(double seuil) {
		this.seuils[1] = seuil;
	}

	public void setTerritoire(String territoire) {
		this.territoire = territoire;
	}
	
	public void setEnveloppe(String enveloppe) {
		this.enveloppe = enveloppe;
	}
	
	public void setBufferArea(double bufferArea){
		this.bufferArea = bufferArea;
	}
	
	public void setZoneBocage(String zoneBocage) {
		this.zoneBocage = zoneBocage;
	}
	
	public void setOutputPath(String outputPath) {
		Util.createAccess(outputPath);
		this.outputPath = outputPath;
	}
	/*
	public void setOutputFile(String outputFile) {
		Util.createAccess(outputFile);
		this.outputFile = outputFile;
	}*/
	
	public void setName(String name) {
		this.name = name;
	}

	public void setPlantation(String plantation) {
		this.plantation = plantation;
	}
	
	public void setSuppression(String suppression) {
		this.suppression = suppression;
	}
	
	public void setModeFast(boolean modeFast){
		this.modeFast = modeFast;
	}
	
	public void setForce(boolean force){
		this.force = force;
	}
	
	public void setAttributCodeEA(String attributeCodeEA) {
		this.attributCodeEA = attributeCodeEA;
	}

	public void setAttributSecteur(String attributeSecteur) {
		this.attributSecteur = attributeSecteur;
	}
	
	public void setAttributHauteurPlantation(String attributHauteurPlantation) {
		this.attributHauteurPlantation = attributHauteurPlantation;
	}
	
	public List<String> scenarios(){
		return scenarios;
	}
	
	public Tile tile(){
		return tile;
	}

	public double seuil() {
		return seuils[1];
	}
	
	public double[] seuils() {
		return seuils;
	}

	public String outputPath() {
		return outputPath;
	}
	/*
	public String outputFile() {
		return outputFile;
	}*/

	/*
	public String territoire() {
		return territoire;
	}*/
	
	public String zoneBocage() {
		return zoneBocage;
	}

	public String name() {
		return name;
	}

	public String bocage() {
		return bocage;
	}
	
	public String hauteurBoisement() {
		return hauteurBoisement;
	}
	
	public String typeBoisement() {
		return typeBoisement;
	}
	
	public String distanceInfluenceBoisement() {
		return distanceInfluenceBoisement;
	}
	
	public String grainBocager() {
		return grainBocager;
	}
	
	public String grainBocager4Classes() {
		return grainBocager4Classes;
	}
	
	public String grainBocagerFonctionnel() {
		return grainBocagerFonctionnel;
	}
	
	public String clusterGrainBocagerFonctionnel() {
		return clusterGrainBocagerFonctionnel;
	}
	
	public String proportionGrainBocagerFonctionnel() {
		return proportionGrainBocagerFonctionnel;
	}
	
	public String zoneFragmentationGrainBocagerFonctionnel() {
		return zoneFragmentationGrainBocagerFonctionnel;
	}

	public String plantation() {
		return plantation;
	}
	
	public String suppression() {
		return suppression;
	}

	public boolean modeFast() {
		return modeFast;
	}
	
	public boolean force() {
		return force;
	}

	public double grainCellSize() {
		return grainCellSize;
	}

	public double grainWindowRadius(){
		return grainWindowRadius;
	}
	
	public double enjeuxCellSize() {
		return enjeuxCellSize;
	}

	public double enjeuxWindowRadius(){
		return enjeuxWindowRadius;
	}
	/*
	public double bufferArea() {
		return bufferArea;
	}
	*/
	public String attributCodeEA(){
		return attributCodeEA;
	}
	
	public String attributSecteur(){
		return attributSecteur;
	}
	
	public String attributHauteurPlantation(){
		return attributHauteurPlantation;
	}
	
	public EnteteRaster entete(){
		return entete;
	}
	
}
