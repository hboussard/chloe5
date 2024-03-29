package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
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
	
	//private boolean hugeMode; 			// mode grand territoire ?
	
	private EnteteRaster entete; 		// entete de travail
	
	private Tile tile;					// tuilage pour grandes analyses
	
	//private List<String> scenarios;		// liste des scenarios
	
	//private List<String> codesEA;		// liste des codes exploitations
	
	private String outputFolder; 		// dossier de generation des sorties
	
	private String outputPrefix;				// nom identifiant du territoire
	
	private String territory;			// shapefile du territoire pour definir l'enveloppe
	
	private String envelope;			// enveloppe d'analyse
	
	private double bufferArea; 			// buffer autour du territoire d'analyse
	
	//private String zoneBocage;			// shapefile des zones bocageres appartenant a l'acteur du territoire
	
	private String bocage;				// tuiles MNHC ou autre donnees de boisement
	
	private String woodHeight;			// les hauteur de boisement
	
	private String woodType;		// les types de boisement (masif, haie ou arbre isole)
	
	private String influenceDistance; // les distances d'influence des boisement (en fonctin de leur type et de leur hauteur)
	
	private String grainBocager;		// le grain bocager continue (de 0 a 1)
	
	private String grainBocager4Classes;	// le grain bocager classifie en 4 classes
	
	private String functionalGrainBocager;	// le grain bocager fonctionnel
	
	private String functionalGrainBocagerClustering;	// le grain bocager fonctionnel clusterise
	
	private String functionalGrainBocagerProportion;	// les proportions de grain bocager fonctionnel (a une certaine echelle)
	
	private String functionalGrainBocagerFragmentation;	// les zones de fragmentation du grain bocager fonctionnel (a une certaine echelle)
	
	private String woodPlanting;			// ajout de lineaires bocagers ou surfaces de bocage
	
	private String heightPlantingAttribute;		// attribut des valeurs de hauteur dans la couche de plantation
	
	private String woodRemoval;			// suppression de surfaces de bocage 
	
	private boolean fastMode; 			// mode FAST (imprecis mais rapide)
	
	private boolean force; 				// forcage des recalculations
	
	private double grainBocagerCellSize;			// taille du pixel du grain bocager
	
	private double grainBocagerWindowRadius;		// taille de la fen�tre d'analyse pour le grain bocager
	
	private double issuesCellSize;		// taille du pixel des enjeux
	
	private double issuesWindowRadius;	// taille de la fen�tre d'analyse pour les enjeux bocagers
	
	private double[] thresholds; 			// seuil d'observation du grain bocager, le seuil de fonctionnalite est le deuxi�me
	
	//private String attributCodeEA;		// attribut de code exploitation
	
	//private String attributSecteur;		// attribut des secteurs
	
	public GrainBocagerManager(String treatment){
		setTreatment(treatment);
		init();
	}
	
	private void setTreatment(String treatment){
		switch(treatment){
		//case "farm_diagnostic" : 
		//	factory = new GBPDiagnosticExploitationFactory();
		//	break;
		//case "territory_diagnostic" : 
		//	factory = new GBPDiagnosticTerritoireFactory();
		//	break;
		case "wood_height_recovery" : 
			factory = new GBPRecuperationHauteurBoisementFactory();
			break;
		case "wood_type_detection" : 
			factory = new GBPDetectionTypeBoisementFactory();
			break;
		case "influence_distance_calculation" :
			factory = new GBPCalculDistanceInfluenceBoisementFactory();
			break;
		case "grain_bocager_calculation" : 
			factory = new GBPCalculGrainBocagerFactory();
			break;
		case "functional_clustering" : 
			factory = new GBPClusterisationFonctionnaliteFactory();
			break;
		case "global_issues_calculation" : 
			factory = new GBPCalculEnjeuxGlobauxFactory();
			break;
		default : throw new IllegalArgumentException("treatment '"+treatment+"' do not exists");
		}
	}
	
	private void init(){
		
		// attributs avec valeur par defaut
		//hugeMode = false;
		tile = null;
		thresholds = new double[]{0.2, 0.33, 0.45};
		fastMode = true;
		force = false;
		grainBocagerCellSize = 5;
		grainBocagerWindowRadius = 250.0;
		issuesCellSize = 50;
		issuesWindowRadius = 1000.0;
		bufferArea = 0.0;
		//scenarios = new ArrayList<String>();
		//codesEA = new ArrayList<String>();
		//attributCodeEA = "id_ea";
		//attributSecteur = "secteur";
		heightPlantingAttribute = "hauteur";
		outputFolder = new File(System.getProperty("java.io.tmpdir")).toString().replace("\\", "/")+"/grain_bocager/";
		Util.createAccess(outputFolder);
		/*File tmpDir = new File(outputFolder);
		for(File f : tmpDir.listFiles()) {
			f.deleteOnExit();
		}*/
		outputPrefix = "";
		
		// attributs a redefinir 
		bocage = null;
		territory = null;
		envelope = null;
		
		woodPlanting = null;
		woodRemoval = null;
		woodHeight = null;
		woodType = null;
		influenceDistance = null;
		grainBocager = null;
		grainBocager4Classes = null;
		functionalGrainBocager = null;
		functionalGrainBocagerClustering = null;
		functionalGrainBocagerProportion = null;
		functionalGrainBocagerFragmentation = null;
	}
	
	private boolean initEntete(){
		
		if(bocage() != null){
			
			// recuperation de l'entete du bocage
			Coverage covBocage = CoverageManager.getCoverage(bocage());
			entete = covBocage.getEntete();
			covBocage.dispose();
			
			if(envelope != null){
				
				String[] bornes = envelope.replace("{", "").replace("}", "").split(";");
				
				// recuperation de l'enveloppe totale de travail
				Envelope envelope = new Envelope(
						Double.parseDouble(bornes[0])-bufferArea, 
						Double.parseDouble(bornes[1])+bufferArea, 
						Double.parseDouble(bornes[2])-bufferArea, 
						Double.parseDouble(bornes[3])+bufferArea);
							
				// recuperation de l'entete
				entete = EnteteRaster.getEntete(entete, envelope);
				
			} else if(territory != null){
				
				// recuperation de l'enveloppe totale de travail
				Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(territory, bufferArea);
							
				// recuperation de l'entete
				entete = EnteteRaster.getEntete(entete, envelope);
			}
			
			return true;
		}
		
		if(new File(woodHeight()).exists()){
			
			// recuperation de l'entete
			Coverage covHauteurBoisement = CoverageManager.getCoverage(woodHeight());
			entete = covHauteurBoisement.getEntete();
			covHauteurBoisement.dispose();
			
			return true;
		}
		
		if(new File(influenceDistance()).exists()){
			
			// recuperation de l'entete
			Coverage covDistanceInfluence = CoverageManager.getCoverage(influenceDistance());
			entete = covDistanceInfluence.getEntete();
			covDistanceInfluence.dispose();
			
			return true;
		}
		
		if(new File(grainBocager()).exists()){
			
			// recuperation de l'entete
			Coverage covGrainBocager = CoverageManager.getCoverage(grainBocager());
			entete = covGrainBocager.getEntete();
			covGrainBocager.dispose();
			
			return true;
		}
		
		if(new File(functionalGrainBocagerClustering()).exists()){
			
			// recuperation de l'entete
			Coverage covClusterGrainBocager = CoverageManager.getCoverage(functionalGrainBocagerClustering());
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
	
	public void setTile(String tileDir){
		this.tile = Tile.getTile(tileDir);
	}

	public void setBocage(String bocage) {
		this.bocage = bocage;
	}
	
	public void setWoodHeight(String woodHeight) {
		this.woodHeight = woodHeight;
	}
	
	public void setWoodType(String woodType) {
		Util.createAccess(woodType);
		this.woodType = woodType;
	}
	
	public void setInfluenceDistance(String influenceDistance) {
		Util.createAccess(influenceDistance);
		this.influenceDistance = influenceDistance;
	}
	
	public void setGrainBocager(String grainBocager){
		Util.createAccess(grainBocager);
		this.grainBocager = grainBocager;
	}
	
	public void setGrainBocager4Classes(String grainBocager4Classes){
		Util.createAccess(grainBocager4Classes);
		this.grainBocager4Classes = grainBocager4Classes;
	}
	
	public void setFunctionalGrainBocager(String functionalGrainBocager){
		Util.createAccess(functionalGrainBocager);
		this.functionalGrainBocager = functionalGrainBocager;
	}
	
	public void setFunctionalGrainBocagerClustering(String functionalGrainBocagerClustering){
		Util.createAccess(functionalGrainBocagerClustering);
		this.functionalGrainBocagerClustering = functionalGrainBocagerClustering;
	}
	
	public void setFunctionalGrainBocagerProportion(String functionalGrainBocagerProportion){
		Util.createAccess(functionalGrainBocagerProportion);
		this.functionalGrainBocagerProportion = functionalGrainBocagerProportion;
	}
	
	public void setFunctionalGrainBocagerFragmentation(String functionalGrainBocagerFragmentation){
		Util.createAccess(functionalGrainBocagerFragmentation);
		this.functionalGrainBocagerFragmentation = functionalGrainBocagerFragmentation;
	}
	
	/*
	public void addScenario(String scenario){
		scenarios.add(scenario);
	}
	
	public void addCodeEA(String codeEA){
		codesEA.add(codeEA);
	}
	*/
	
	public void setGrainBocagerCellSize(double grainCellSize){
		this.grainBocagerCellSize = grainCellSize;
	}
	
	public void setGrainBocagerWindowRadius(double grainWindowRadius){
		this.grainBocagerWindowRadius = grainWindowRadius;
	}
	
	public void setIssuesCellSize(double issuesCellSize){
		this.issuesCellSize = issuesCellSize;
	}
	
	public void setIssuesWindowRadius(double issuesWindowRadius){
		this.issuesWindowRadius = issuesWindowRadius;
	}
	
	public void setThresholds(double threshold1, double threshold2, double threshold3) {
		this.thresholds[0] = threshold1;
		this.thresholds[1] = threshold2;
		this.thresholds[2] = threshold3;
	}
	
	public void setThreshold(double threshold) {
		this.thresholds[1] = threshold;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}
	
	public void setEnvelope(String envelope) {
		this.envelope = envelope;
	}
	
	public void setBufferArea(double bufferArea){
		this.bufferArea = bufferArea;
	}
	/*
	public void setZoneBocage(String zoneBocage) {
		this.zoneBocage = zoneBocage;
	}
	*/
	public void setOutputFolder(String outputFolder) {
		Util.createAccess(outputFolder);
		this.outputFolder = outputFolder;
	}
	
	public void setOuputPrefix(String outputPrefix) {
		this.outputPrefix = outputPrefix;
	}

	public void setWoodPlanting(String woodPlanting) {
		this.woodPlanting = woodPlanting;
	}
	
	public void setHeightPlantingAttribute(String heightPlantingAttribute) {
		this.heightPlantingAttribute = heightPlantingAttribute;
	}
	
	public void setWoodRemoval(String woodRemoval) {
		this.woodRemoval = woodRemoval;
	}
	
	public void setFastMode(boolean fastMode){
		this.fastMode = fastMode;
	}
	
	public void setForce(boolean force){
		this.force = force;
	}
	
	/*
	public void setAttributCodeEA(String attributeCodeEA) {
		this.attributCodeEA = attributeCodeEA;
	}

	public void setAttributSecteur(String attributeSecteur) {
		this.attributSecteur = attributeSecteur;
	}
	*/
		
	/*
	public List<String> scenarios(){
		return scenarios;
	}
	*/
	
	public Tile tile(){
		return tile;
	}
	
	public boolean hugeMode() {
		return tile != null;
	}

	public double threshold() {
		return thresholds[1];
	}
	
	public double[] thresholds() {
		return thresholds;
	}

	public String outputFolder() {
		return outputFolder;
	}
	
	/*
	public String zoneBocage() {
		return zoneBocage;
	}
	*/
	
	public String outputPrefix() {
		return outputPrefix;
	}

	public String bocage() {
		return bocage;
	}
	
	public String woodHeight() {
		if(woodHeight == null) {
			if(tile == null) {
				setWoodHeight(outputFolder()+outputPrefix()+"hauteur_boisement.tif");
			}else {
				setWoodHeight(outputFolder()+outputPrefix()+"hauteur_boisement/");
			}
		}
		return woodHeight;
	}
	
	public String woodType() {
		if(woodType == null) {
			if(tile == null) {
				setWoodType(outputFolder()+outputPrefix()+"type_boisement.tif");
			}else {
				setWoodType(outputFolder()+outputPrefix()+"type_boisement/");
			}
		}
		return woodType;
	}
	
	public String influenceDistance() {
		if(influenceDistance == null) {
			if(tile == null) {
				setInfluenceDistance(outputFolder()+outputPrefix()+"distance_influence.tif");
			}else {
				setInfluenceDistance(outputFolder()+outputPrefix()+"distance_influence/");
			}
		}
		return influenceDistance;
	}
	
	public String grainBocager() {
		if(grainBocager == null) {
			if(tile == null) {
				setGrainBocager(outputFolder()+outputPrefix()+"grain_bocager_"+(int)grainBocagerCellSize()+"m.tif");
			}else {
				setGrainBocager(outputFolder()+outputPrefix()+"grain_bocager_"+(int)grainBocagerCellSize()+"m/");
			}
		}
		return grainBocager;
	}
	
	public String grainBocager4Classes() {
		if(grainBocager4Classes == null) {
			if(tile == null) {
				setGrainBocager4Classes(outputFolder()+outputPrefix()+"grain_bocager_4classes_"+(int)grainBocagerCellSize()+"m.tif");
			}else {
				setGrainBocager4Classes(outputFolder()+outputPrefix()+"grain_bocager_4classes_"+(int)grainBocagerCellSize()+"m/");
			}
		}
		return grainBocager4Classes;
	}
	
	public String functionalGrainBocager() {
		if(functionalGrainBocager == null) {
			if(tile == null) {
				setFunctionalGrainBocager(outputFolder()+outputPrefix()+"grain_bocager_fonctionnel_"+(int)grainBocagerCellSize()+"m.tif");
			}else {
				setFunctionalGrainBocager(outputFolder()+outputPrefix()+"grain_bocager_fonctionnel_"+(int)grainBocagerCellSize()+"m/");
			}
		}
		return functionalGrainBocager;
	}
	
	public String functionalGrainBocagerClustering() {
		if(functionalGrainBocagerClustering == null) {
			if(tile == null) {
				setFunctionalGrainBocagerClustering(outputFolder()+outputPrefix()+"cluster_grain_bocager_fonctionnel_"+(int)grainBocagerCellSize()+"m.tif");
			}else {
				setFunctionalGrainBocagerClustering(outputFolder()+outputPrefix()+"cluster_grain_bocager_fonctionnel_"+(int)grainBocagerCellSize()+"m/");
			}
		}
		return functionalGrainBocagerClustering;
	}
	
	public String functionalGrainBocagerProportion() {
		if(functionalGrainBocagerProportion == null) {
			if(tile == null) {
				setFunctionalGrainBocagerProportion(outputFolder()+outputPrefix()+"proportion_grain_bocager_fonctionnel_"+(int)issuesWindowRadius()+"m.tif");
			}else {
				setFunctionalGrainBocagerProportion(outputFolder()+outputPrefix()+"proportion_grain_bocager_fonctionnel_"+(int)issuesWindowRadius()+"m/");
			}
		}
		return functionalGrainBocagerProportion;
	}
	
	public String functionalGrainBocagerFragmentation() {
		if(functionalGrainBocagerFragmentation == null) {
			if(tile == null) {
				setFunctionalGrainBocagerFragmentation(outputFolder()+outputPrefix()+"fragmentation_grain_bocager_fonctionnel_"+(int)issuesWindowRadius()+"m.tif");
			}else {
				setFunctionalGrainBocagerFragmentation(outputFolder()+outputPrefix()+"fragmentation_grain_bocager_fonctionnel_"+(int)issuesWindowRadius()+"m/");
			}
		}
		return functionalGrainBocagerFragmentation;
	}

	public String woodPlanting() {
		return woodPlanting;
	}
	
	public String woodRemoval() {
		return woodRemoval;
	}

	public boolean fastMode() {
		return fastMode;
	}
	
	public boolean force() {
		return force;
	}

	public double grainBocagerCellSize() {
		return grainBocagerCellSize;
	}

	public double grainBocagerWindowRadius(){
		return grainBocagerWindowRadius;
	}
	
	public double issuesCellSize() {
		return issuesCellSize;
	}

	public double issuesWindowRadius(){
		return issuesWindowRadius;
	}
	/*
	public double bufferArea() {
		return bufferArea;
	}
	*/
	/*
	public String attributCodeEA(){
		return attributCodeEA;
	}
	
	public String attributSecteur(){
		return attributSecteur;
	}
	*/
	
	public String heightPlantingAttribute(){
		return heightPlantingAttribute;
	}
	
	public EnteteRaster entete(){
		return entete;
	}
	
}
