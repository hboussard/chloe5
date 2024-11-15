package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.ErosionEventType;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization.ERDataInitializationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilitygeneration.ERErodibilityGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erodibilityintensitygeneration.ERErodibilityIntensityGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.erosioncalculation.ERErosionCalculationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.infiltrationgeneration.ERInfiltrationGenerationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.slopedetection.ERSlopeDetectionFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermasscumulation.ERWaterMassCumulationFactory;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.watermassinitialization.ERWaterMassInitializationFactory;
import fr.inrae.act.bagap.chloe.util.Util;

public class ErosionManager {
	
	private ErosionProcedureFactory factory; // constructeur de procedure
	
	private boolean force; 			// forcage des recalculations

	private String outputFolder; 	// dossier de generation des sorties
	
	private String outputPrefix;	// nom identifiant du territoire
	
	private String territory; 		// la carte du terrioire du bassin versant
	
	private String territoryShape; 	// le shapefile des entites territoriales d'interet (typiquement le bassin versant)
	
	private String territoryIDAttribute; // le champs identifiant de territoire de la table attributaire  
	
	private String[] territoryIDValues; // les valeurs possibles du champs identifiant de territoire
	
	private String os; 				// la carte d'occupation des sols
	
	private String osSource; 		// la carte source d'occupation des sols (typiquement OSO-Thiea France entiere à 10m)
	
	private Set<String> surfaceWoodShapes; // les shapefiles des boisements de la bd_topo 
	
	private String surfaceWoodAttribute; // l'attribut a rasteriser des boisements de la bd_topo
	
	private Map<String, Integer> surfaceWoodCodes; // les codes de rasteurisation des boisements de la bd_topo
	
	private Set<String> linearWoodShapes; // les shapefiles des boisements lineaires de la bd_topo 
	
	private int linearWoodCode; 	// code de rasteurisation des elements lineaires
	
	private Set<String> linearRoadShapes; // les shapefiles des routes de la bd_topo
	
	private String linearRoadAttribute; // l'attribut à rasteuriser des routes de la bd_topo
	
	private Map<String, Integer> linearRoadCodes; // les codes de rasteurisation des routes de la bd_topo
	
	private Set<String> linearTrainShapes; // les shapefiles des voies ferrees lineaires de la bd_topo 
	
	private int linearTrainCode; 	// code de rasteurisation des elements voies ferrees 
	
	private Set<String> surfaceWaterShapes; // les shapefiles des surfaces hydrographiques de la bd_topo 
	
	private int surfaceWaterCode; 	// code de rasteurisation des surfaces hydrographiques
	
	private Set<String> linearWaterShapes; // les shapefiles des lineaires hydrographiques de la bd_topo 
	
	private int linearWaterCode; 	// code de rasteurisation des lineaires hydrographiques
	
	private String elevation; 		// la carte d'altitude
	
	private Set<String> elevationFolders; // le dossier source des tuiles d'altitude
	
	private String slopeIntensity; // la carte des intensites de pentes
	
	private String normSlopeIntensity; // la carte des intensites de pentes normees
	
	private String infiltrationMapFile; // le fichier des codes d'infiltration des occupations des sols
	
	private String infiltration; 	// la carte d'infiltration
	
	private String erodibilityMapFile; // le fichier des codes d'erodibilite des occupations des sols
	
	private String erodibility; 	// la carte d'erodibilité
	
	//private int waterQuantity; 		// quantite d'eau inititale in mm ; 5, 10 ou 20
	
	private ErosionEventType eventType; // type d'evenement climatique
	
	private String initialWaterMass; // la carte des masses d'eau initiales
	
	private String cumulWaterMass; // la carte des masses d'eau cumulees
	
	private String erodibilityIntensity; // la carte des intensites d'erodibilite
	
	private String sourceErosionIntensity; // la carte des sources d'erosion
	
	private String depositionErosionIntensity; // la carte des depots d'erosion
	
	private String bocageAmenagement; // amenagement bocager
	
	private int displacement; 		// deplacement pour l'analyse
	
	public ErosionManager(String treatment){
		setTreatment(treatment);
		init();
	}
	
	private void setTreatment(String treatment){
		switch(treatment){
		case "data_initialization" : 
			factory = new ERDataInitializationFactory();
			break;
		case "infiltration_generation" : 
			factory = new ERInfiltrationGenerationFactory();
			break;
		case "erodibility_generation" : 
			factory = new ERErodibilityGenerationFactory();
			break;
		case "slope_detection" : 
			factory = new ERSlopeDetectionFactory();
			break;
		case "water_mass_initialization" : 
			factory = new ERWaterMassInitializationFactory();
			break;
		case "water_mass_cumulation" : 
			factory = new ERWaterMassCumulationFactory();
			break;
		case "erodibility_intensity_generation" : 
			factory = new ERErodibilityIntensityGenerationFactory();
			break;
		case "erosion_calculation" : 
			factory = new ERErosionCalculationFactory();
			break;
		default : throw new IllegalArgumentException("treatment '"+treatment+"' do not exists");
		}
	}
	
	private void init(){
		
		// attributs avec valeur par defaut
		force = false;
		//waterQuantity = 10;
		eventType = ErosionEventType.MEDIUM;
		displacement = 1;
		outputFolder = new File(System.getProperty("java.io.tmpdir")).toString().replace("\\", "/")+"/erosion/";
		Util.createAccess(outputFolder);
		outputPrefix = "";
		
		// attributs a redefinir 
		territory = null;
		territoryShape = null;
		territoryIDAttribute = null;
		territoryIDValues = null;
		os = null; 
		osSource = null;
		surfaceWoodShapes = new HashSet<String>();
		surfaceWoodAttribute = null;
		surfaceWoodCodes = new HashMap<String, Integer>();
		linearWoodShapes = new HashSet<String>();
		linearWoodCode = -1;
		linearRoadShapes = new HashSet<String>();
		linearRoadAttribute = null;
		linearRoadCodes = new HashMap<String, Integer>();
		linearTrainShapes = new HashSet<String>();
		linearTrainCode = -1;
		surfaceWaterShapes = new HashSet<String>();
		surfaceWaterCode = -1;
		linearWaterShapes = new HashSet<String>();
		linearWaterCode = -1;
		elevation = null;
		elevationFolders = new HashSet<String>();
		slopeIntensity = null; 
		normSlopeIntensity = null;
		infiltrationMapFile = null; 
		infiltration = null;
		erodibilityMapFile = null;
		erodibility = null;
		initialWaterMass = null;
		cumulWaterMass = null; 
		erodibilityIntensity = null; 
		sourceErosionIntensity = null;
		depositionErosionIntensity = null;
		bocageAmenagement = null;
	}
	
	public ErosionProcedure build(){
		if(factory.check(this)){
			ErosionProcedure erProcedure = factory.create(this);
			return erProcedure;
		}
		
		throw new IllegalArgumentException("parameters are unconsistant");
	}
	
	public void setForce(boolean force){
		this.force = force;
	}
	
	public void setTerritory(String territory) {
		Util.createAccess(territory);
		this.territory = territory;
	}
	
	public void setTerritoryShape(String territoryShape) {
		this.territoryShape = territoryShape;
	}
	
	public void setTerritoryIDAttribute(String territoryIDAttribute) {
		this.territoryIDAttribute = territoryIDAttribute;
	}
	
	public void setTerritoryIDValues(String... territoryIDValues) {
		this.territoryIDValues = territoryIDValues;
	}

	public void setOutputFolder(String outputFolder) {
		Util.createAccess(outputFolder);
		this.outputFolder = outputFolder;
	}

	public void setOutputPrefix(String outputPrefix) {
		this.outputPrefix = outputPrefix;
	}

	public void setOs(String os) {
		Util.createAccess(os);
		this.os = os;
	}
	
	public void setOsSource(String osSource) {
		this.osSource = osSource;
	}
	
	public void addSurfaceWoodShape(String surfaceWoodShape){
		this.surfaceWoodShapes.add(surfaceWoodShape);
	}
	
	public void setSurfaceWoodAttribute(String surfaceWoodAttribute) {
		this.surfaceWoodAttribute = surfaceWoodAttribute;
	}
	
	public void addSurfaceWoodCode(String attributeValue, int codeValue) {
		surfaceWoodCodes.put(attributeValue, codeValue);
	}
	
	public void addLinearWoodShape(String linearWoodShape){
		this.linearWoodShapes.add(linearWoodShape);
	}
	
	public void setLinearWoodCode(int linearWoodCode) {
		this.linearWoodCode = linearWoodCode;
	}
	
	public void addLinearRoadShape(String linearRoadShape){
		this.linearRoadShapes.add(linearRoadShape);
	}
	
	public void setLinearRoadAttribute(String linearRoadAttribute) {
		this.linearRoadAttribute = linearRoadAttribute;
	}
	
	public void addLinearRoadCode(String attributeValue, int codeValue) {
		linearRoadCodes.put(attributeValue, codeValue);
	}
	
	public void addLinearTrainShape(String linearTrainShape){
		this.linearTrainShapes.add(linearTrainShape);
	}
	
	public void setLinearTrainCode(int linearTrainCode) {
		this.linearTrainCode = linearTrainCode;
	}
	
	public void addSurfaceWaterShape(String surfaceWaterShape){
		this.surfaceWaterShapes.add(surfaceWaterShape);
	}
	
	public void setSurfaceWaterCode(int surfaceWaterCode) {
		this.surfaceWaterCode = surfaceWaterCode;
	}
	
	public void addLinearWaterShape(String linearWaterShape){
		this.linearWaterShapes.add(linearWaterShape);
	}
	
	public void setLinearWaterCode(int linearWaterCode) {
		this.linearWaterCode = linearWaterCode;
	}
	
	public void setElevation(String elevation) {
		Util.createAccess(elevation);
		this.elevation = elevation;
	}
	
	public void addElevationFolder(String elevationFolder) {
		this.elevationFolders.add(elevationFolder);
	}

	public void setSlopeIntensity(String slopeIntensity) {
		Util.createAccess(slopeIntensity);
		this.slopeIntensity = slopeIntensity;
	}

	public void setNormSlopeIntensity(String normSlopeIntensity) {
		Util.createAccess(normSlopeIntensity);
		this.normSlopeIntensity = normSlopeIntensity;
	}
	
	public void setInfiltrationMapFile(String infiltrationMapFile) {
		this.infiltrationMapFile = infiltrationMapFile;
	}

	public void setInfiltration(String infiltration) {
		Util.createAccess(infiltration);
		this.infiltration = infiltration;
	}

	public void setErodibilityMapFile(String erodibilityMapFile) {
		this.erodibilityMapFile = erodibilityMapFile;
	}

	public void setErodibility(String erodibility) {
		Util.createAccess(erodibility);
		this.erodibility = erodibility;
	}

	/*
	public void setWaterQuantity(int waterQuantity) {
		this.waterQuantity = waterQuantity;
	}
	*/
	
	public void setEventType(String eventType) {
		this.eventType = ErosionEventType.valueOf(eventType);
	}

	public void setInitialWaterMass(String initialWaterMass) {
		Util.createAccess(initialWaterMass);
		this.initialWaterMass = initialWaterMass;
	}

	public void setCumulWaterMass(String cumulWaterMass) {
		Util.createAccess(cumulWaterMass);
		this.cumulWaterMass = cumulWaterMass;
	}

	public void setErodibilityIntensity(String erodibilityIntensity) {
		Util.createAccess(erodibilityIntensity);
		this.erodibilityIntensity = erodibilityIntensity;
	}

	public void setSourceErosionIntensity(String sourceErosionIntensity) {
		Util.createAccess(sourceErosionIntensity);
		this.sourceErosionIntensity = sourceErosionIntensity;
	}

	public void setDepositionErosionIntensity(String depositionErosionIntensity) {
		Util.createAccess(depositionErosionIntensity);
		this.depositionErosionIntensity = depositionErosionIntensity;
	}

	public void setBocageAmenagement(String bocageAmenagement) {
		this.bocageAmenagement = bocageAmenagement;
	}

	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}

	public boolean force() {
		return force;
	}
	
	public String outputFolder() {
		return outputFolder;
	}

	public String outputPrefix() {
		return outputPrefix;
	}

	public String territory() {
		if(territory == null) {
			setTerritory(outputFolder()+outputPrefix()+"_territory.tif");
		}
		return territory;
	}
	
	public String territoryShape() {
		return territoryShape;
	}
	
	public String territoryIDAttribute() {
		return territoryIDAttribute;
	}

	public String[] territoryIDValues() {
		return territoryIDValues;
	}
	
	public String os() {
		if(os == null) {
			setOs(outputFolder()+outputPrefix()+"_os.tif");
		}
		return os;
	}
	
	public String osSource() {
		return osSource;
	}
	
	public Set<String> surfaceWoodShapes(){
		return surfaceWoodShapes;
	}
	
	public String surfaceWoodAttribute() {
		return surfaceWoodAttribute;
	}
	
	public Map<String, Integer> surfaceWoodCodes(){
		return surfaceWoodCodes;
	}
	
	public Set<String> linearWoodShapes(){
		return linearWoodShapes;
	}

	public int linearWoodCode() {
		return linearWoodCode;
	}
	
	public Set<String> linearRoadShapes(){
		return linearRoadShapes;
	}
	
	public String linearRoadAttribute() {
		return linearRoadAttribute;
	}
	
	public Map<String, Integer> linearRoadCodes(){
		return linearRoadCodes;
	}
	
	public Set<String> linearTrainShapes(){
		return linearTrainShapes;
	}

	public int linearTrainCode() {
		return linearTrainCode;
	}
	
	public Set<String> surfaceWaterShapes(){
		return surfaceWaterShapes;
	}

	public int surfaceWaterCode() {
		return surfaceWaterCode;
	}
	
	public Set<String> linearWaterShapes(){
		return linearWaterShapes;
	}

	public int linearWaterCode() {
		return linearWaterCode;
	}
	
	public String elevation() {
		if(elevation == null) {
			setElevation(outputFolder()+outputPrefix()+"_elevation.tif");
		}
		return elevation;
	}
	
	public Set<String> elevationFolders() {
		return elevationFolders;
	}

	public String slopeIntensity() {
		if(slopeIntensity == null) {
			setSlopeIntensity(outputFolder()+outputPrefix()+"_slope_intensity.tif");
		}
		return slopeIntensity;
	}
	
	public String normSlopeIntensity() {
		if(normSlopeIntensity == null) {
			setNormSlopeIntensity(outputFolder()+outputPrefix()+"_norm_slope_intensity.tif");
		}
		return normSlopeIntensity;
	}

	public String infiltrationMapFile() {
		return infiltrationMapFile;
	}

	public String infiltration() {
		if(infiltration == null) {
			setInfiltration(outputFolder()+outputPrefix()+"_infiltration.tif");
		}
		return infiltration;
	}

	public String erodibilityMapFile() {
		return erodibilityMapFile;
	}

	public String erodibility() {
		if(erodibility == null) {
			setErodibility(outputFolder()+outputPrefix()+"_erodibility.tif");
		}
		return erodibility;
	}

	public int waterQuantity() {
		return eventType.getWaterQuantity();
	}

	public String initialWaterMass() {
		if(initialWaterMass == null) {
			setInitialWaterMass(outputFolder()+outputPrefix()+"_"+eventType.name()+"_initial_water_mass.tif");
		}
		return initialWaterMass;
	}

	public String cumulWaterMass() {
		if(cumulWaterMass == null) {
			setCumulWaterMass(outputFolder()+outputPrefix()+"_"+eventType.name()+"_cumul_water_mass.tif");
		}
		return cumulWaterMass;
	}

	public String erodibilityIntensity() {
		if(erodibilityIntensity == null) {
			setErodibilityIntensity(outputFolder()+outputPrefix()+"_"+eventType.name()+"_erodibility_intensity.tif");
		}
		return erodibilityIntensity;
	}

	public String sourceErosionIntensity() {
		if(sourceErosionIntensity == null) {
			setSourceErosionIntensity(outputFolder()+outputPrefix()+"_"+eventType.name()+"_source_erosion_intensity.tif");
		}
		return sourceErosionIntensity;
	}

	public String depositionErosionIntensity() {
		if(depositionErosionIntensity == null) {
			setDepositionErosionIntensity(outputFolder()+outputPrefix()+"_"+eventType.name()+"_deposition_erosion_intensity.tif");
		}
		return depositionErosionIntensity;
	}

	public String bocageAmenagement() {
		return bocageAmenagement;
	}

	public int displacement() {
		return displacement;
	}
	
	
}
