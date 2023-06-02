package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.territory;

public class GrainBocagerTerritoireBuilder {
	
	private double seuil; 					// seuil de fonctionnalite du grain bocager
	
	private String outputPath; 				// dossier de génération des sorties
	
	private String territoire;				// shapefile du territoire
	
	private String name;					// nom identifiant du territoire
	
	private String bocage;					// tuiles MNHC
	
	private String replantationBocagere;	// lineaire bocagere imagine
	
	private boolean modeFast; 				// mode FAST (mais faux)
	
	private double outCellSize;			// du pixel de sortie
	
	public GrainBocagerTerritoireBuilder(){
		reset();
	}

	private void reset(){
		
		// attributs avec valeur par défaut
		seuil = 0.33;
		modeFast = true;
		outCellSize = 5;
		
		// attributs à redéfinir obligatoirement
		outputPath = "";
		territoire = "";
		name = "";
		bocage = "";
		
		// attributs facultatifs
		replantationBocagere = "";
	}
	
	public void setOutCellSize(double outputCellSize){
		this.outCellSize = outputCellSize;
	}
	
	public void setSeuil(double seuil) {
		this.seuil = seuil;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void setTerritoire(String territoire) {
		this.territoire = territoire;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setBocage(String bocage) {
		this.bocage = bocage;
	}

	public void setReplantationBocagere(String replantationBocagere) {
		this.replantationBocagere = replantationBocagere;
	}
	
	public void setModeFast(boolean modeFast){
		this.modeFast = modeFast;
	}
	
	private boolean check(){
		boolean ok = true;
		if(seuil<0 || seuil>1){
			System.out.println("threshold parameter 'seuil' is unconsistant "+seuil);
			ok = false;
		}
		if(outCellSize < 5 && outCellSize%5!=0){
			System.out.println("output cellsize parameter 'outputCellSize' is unconsistant "+outCellSize);
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
	
	public GrainBocagerTerritoire build(){
		
		if(check()){
			GrainBocagerTerritoire gbTerritoire = new GrainBocagerTerritoire(outputPath, territoire, name, bocage, seuil, replantationBocagere, modeFast, outCellSize);
			reset();
			return gbTerritoire;
		}
		throw new IllegalArgumentException("parameters are unconsistant");
	}
	
}
