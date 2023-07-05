package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure;

import java.util.ArrayList;
import java.util.List;

public class GrainBocagerManagerOld {

	private List<String> scenarios;			// les noms des scenarios
	
	private String outputPath; 				// dossier de generation des sorties
	
	private String name;					// nom identifiant du territoire
	
	private String territoire;				// shapefile du territoire
	
	private String zoneBocage;				// shapefile des zones bocageres appartenant à l'acteur du territoire
	
	private String bocage;					// tuiles MNHC ou autre hauteur de canopee
	
	private String plantationBocagere;		// ajout de lineaires bocagers ou surfaces de bocage imagine
	
	private String suppressionBocagere;		// suppression de surfaces de bocage imagine
	
	private boolean modeFast; 				// mode FAST (imprecis mais rapide)
	
	private double outCellSize;				// taille du pixel de sortie
	
	private int bufferArea; 				// buffer autour du territoire d'analyse
	
	private double seuil; 					// seuil de fonctionnalite du grain bocager
	
	private double[] seuils; 				// seuil d'observation du grain bocager
	
	public GrainBocagerManager(){
		reset();
	}

	private void reset(){
		
		// attributs avec valeur par défaut
		seuil = 0.33;
		seuils = new double[]{0.2, 0.33, 0.45};
		modeFast = true;
		outCellSize = 5;
		bufferArea = 350;
		scenarios = new ArrayList<String>();
		
		// attributs à redéfinir obligatoirement
		outputPath = "";
		territoire = "";
		name = "";
		bocage = "";
		
		// attributs facultatifs
		plantationBocagere = "";
		suppressionBocagere = "";
	}

	public void addScenario(String scenario){
		scenarios.add(scenario);
	}
	
	public void setOutCellSize(double outputCellSize){
		this.outCellSize = outputCellSize;
	}
	
	public void setSeuil(double seuil) {
		this.seuil = seuil;
	}

	public void setTerritoire(String territoire) {
		this.territoire = territoire;
	}
	
	public void setBufferArea(int bufferArea){
		this.bufferArea = bufferArea;
	}
	
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setBocage(String bocage) {
		this.bocage = bocage;
	}

	public void setPlantationBocagere(String plantationBocagere) {
		this.plantationBocagere = plantationBocagere;
	}
	
	public void setSuppressionBocagere(String suppressionBocagere) {
		this.suppressionBocagere = suppressionBocagere;
	}
	
	public void setModeFast(boolean modeFast){
		this.modeFast = modeFast;
	}
	
	public List<String> scenarios(){
		return scenarios;
	}
	
	public double seuil() {
		return seuil;
	}
	
	public double[] seuils() {
		return seuils;
	}

	public String outputPath() {
		return outputPath;
	}

	public String territoire() {
		return territoire;
	}

	public String name() {
		return name;
	}

	public String bocage() {
		return bocage;
	}

	public String plantationBocagere() {
		return plantationBocagere;
	}
	
	public String suppressionBocagere() {
		return suppressionBocagere;
	}

	public boolean modeFast() {
		return modeFast;
	}

	public double outCellSize() {
		return outCellSize;
	}

	public int bufferArea() {
		return bufferArea;
	}

	private boolean check(){
		boolean ok = true;
		if(scenarios.size() == 0){
			scenarios.add("");
		}
		if(seuil<0 || seuil>1){
			System.out.println("threshold parameter 'seuil' is unconsistant "+seuil);
			ok = false;
		}
		if(outCellSize < 5 && outCellSize%5!=0){
			System.out.println("output cellsize parameter 'outputCellSize' is unconsistant "+outCellSize);
			ok = false;
		}
		if(bufferArea < 0){
			System.out.println("buffer area parameter 'bufferArea' is unconsistant "+bufferArea);
			ok = false;
		}
		if(outputPath.equalsIgnoreCase("")){
			System.out.println("output folder 'outpath' is missing");
			ok = false;
		}
		if(territoire.equalsIgnoreCase("")){
			System.out.println("input shapefile for 'territoire' is missing");
			ok = false;
		}
		if(name.equalsIgnoreCase("")){
			System.out.println("input shapefile for 'name' is missing");
			ok = false;
		}
		if(bocage.equalsIgnoreCase("")){
			System.out.println("input MNHC folder for 'bocage' is missing");
			ok = false;
		}
		return ok;
	}
	
	public GrainBocagerDiagnostic build(){
		
		if(check()){
			GrainBocagerDiagnostic gbProcedure = new GrainBocagerDiagnostic(this);
			return gbProcedure;
		}
		
		throw new IllegalArgumentException("parameters are unconsistant");
	}
	
}