package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.domain.Domain;
import fr.inra.sad.bagap.apiland.domain.DomainFactory;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysis;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisBuilder;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.util.analysis.ChloeUtilAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptLea {
	
	private static final String path = "E:/temp/lea/";

	private static final String rasterBio = path+"bio.tif";
	
	private static final String centralBio = path+"central_bio.tif";
	
	private static final String rasterOccSol = path+"occsol.tif";
	
	private static final String rasterHaie = path+"haie.tif";
	
	private static final String propCulture = path+"prop_culture.tif";
	
	private static final String propBois = path+"prop_bois.tif";
	
	private static final String propImpermeabilise = path+"prop_impermeabilise.tif";
	
	private static final String propEau = path+"prop_eau.tif";
	
	private static final String propHaie = path+"prop_haie.tif";
	
	private static final String classifHaie = path+"classification_haie_5classes.tif";
	
	private static final String rasterDiversite = path+"diversite.tif";
	
	private static final String centralDiversite = path+"central_diversite.tif";
	
	private static final String rasterParcelle = path+"parcelle.tif";
	
	private static final String centralParcelle = path+"central_parcelle.tif";
	
	private static final String decision = path+"decision.tif";
	
	private static final String analyseParcelleParCategorie = path+"analyse_parcelle_par_categorie.csv";
	
	public static void main(String[] args) {
		
		//analyseBio();
		
		//analyseOccSol();
		
		//analyseHaie();
		
		//analyseDiversite();
		
		//decision();
		
		analyseParcelle();
		
	}

	private static void decision() {
		
		Coverage covBio = CoverageManager.getCoverage(centralBio);
		EnteteRaster entete = covBio.getEntete();
		float[] dataBio = covBio.getData();
		covBio.dispose();
		
		Coverage covCulture = CoverageManager.getCoverage(propCulture);
		float[] dataCulture = covCulture.getData();
		covCulture.dispose();
		
		Coverage covBois = CoverageManager.getCoverage(propBois);
		float[] dataBois = covBois.getData();
		covBois.dispose();
		
		Coverage covImpermeabilise = CoverageManager.getCoverage(propImpermeabilise);
		float[] dataImpermeabilise = covImpermeabilise.getData();
		covImpermeabilise.dispose();
		
		Coverage covEau = CoverageManager.getCoverage(propEau);
		float[] dataEau = covEau.getData();
		covEau.dispose();
		
		Coverage covHaie = CoverageManager.getCoverage(classifHaie);
		float[] dataHaie = covHaie.getData();
		covHaie.dispose();
		
		Coverage covDiversite = CoverageManager.getCoverage(centralDiversite);
		float[] dataDiversite = covDiversite.getData();
		covDiversite.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataBio, dataCulture, dataBois, dataImpermeabilise, dataEau, dataHaie, dataDiversite){
			@Override
			protected float doTreat(float[] v) {
				float vBio = v[0];
				if(vBio == -1){
					return -1;
				}
				
				if(v[1] > 0.8 && v[2] < 0.1 && v[3] < 0.1 && v[4] < 0.1) {
					
					float vHaie = v[5];
					float vDiversite = v[6];
					if(vDiversite > 0) {
						if(vBio == 1) { // BIO
							
							switch((int) vHaie) {
							case 1 : return 1+((vDiversite-1)*10); 
							case 2 : return 2+((vDiversite-1)*10); 
							case 3 : return 3+((vDiversite-1)*10); 
							case 4 : return 4+((vDiversite-1)*10);  
							case 5 : return 5+((vDiversite-1)*10); 
							default : break;
							}
						
						}else if(vBio == 2){ // pas BIO
							
							switch((int) vHaie) {
							case 1 : return 6+((vDiversite-1)*10); 
							case 2 : return 7+((vDiversite-1)*10);  
							case 3 : return 8+((vDiversite-1)*10);  
							case 4 : return 9+((vDiversite-1)*10);  
							case 5 : return 10+((vDiversite-1)*10); 
							default : break;
							}
						}
					}
				}
				
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write(decision, data, entete);
	}
	
	private static void analyseParcelle() {
		
		//rasterisationParcelle(path+"sequence_zaa_bio_diversity_2015_2022.shp");
		//analyseLocaleParcelle();
		analyseNbParcelleParCategorie();
		
	}
	
	private static void rasterisationParcelle(String parcelleShapeFile){
		
		ShapeFile2CoverageConverter.rasterize(rasterParcelle, parcelleShapeFile, "code", 5, -1, null);
		
	}
	
	private static void analyseLocaleParcelle(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(rasterParcelle);
		builder.addMetric("Central");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("Central", centralParcelle);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseNbParcelleParCategorie() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(centralParcelle);
		builder.setEntityRasterFile(decision);
		builder.addMetric("Nclass");
		builder.addCsvOutput(analyseParcelleParCategorie);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseDiversite(){
		
		rasterisationDiversite(path+"sequence_zaa_bio_diversity_2015_2022.shp");
		analyseLocaleDiversite();
	}

	private static void rasterisationDiversite(String diversiteShapeFile){
		
		ShapeFile2CoverageConverter.rasterize(rasterDiversite, diversiteShapeFile, "Diversity_", 5, -1, null);
	}
	
	private static void analyseLocaleDiversite(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(rasterDiversite);
		builder.addMetric("Central");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("Central", centralDiversite);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseBio(){
		
		rasterisationBio(path+"sequence_zaa_bio_2015_2022.shp");
		analyseLocaleBio();
	}
	
	private static void rasterisationBio(String bioShapeFile){
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("BIO", 1);
		codes.put("", 2);
		
		ShapeFile2CoverageConverter.rasterize(rasterBio, bioShapeFile, "bio_zaa", codes, 5, -1, 0, null);
	}
	
	private static void analyseLocaleBio(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(rasterBio);
		builder.addMetric("Central");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("Central", centralBio);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseOccSol(){
		
		rasterisationOccSol(path+"zaa_sud.shp");
		cleanOccSol();
		analyseLandscapeOccSol();
	}
	
	private static void rasterisationOccSol(String occsolShapeFile){
		
		Coverage cov = CoverageManager.getCoverage(rasterBio);
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		ShapeFile2CoverageConverter.rasterize(rasterOccSol, occsolShapeFile, "OS", -1, entete);
	}
	
	private static void cleanOccSol() {
		
		Map<Float, Float> changes = new HashMap<Float, Float>();
		changes.put(23f, -1f);
		
		ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SEARCHANDREPLACE);
		builder.setRasterFile(rasterOccSol);
		builder.setChanges(changes);
		builder.setNoDataValue(-1);
		builder.addGeoTiffOutput(rasterOccSol);
		
		ChloeAnalysis analysis = builder.build();
		analysis.allRun();
	}
	
	private static void analyseLandscapeOccSol(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(rasterOccSol);
		builder.addMetric("pNVm_28&3000");
		builder.addMetric("pNV_6");
		builder.addMetric("pNVm_3&8&16&42");
		builder.addMetric("pNV_11");
		//builder.setWindowSize(401);
		builder.setWindowSize(601);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("pNVm_28&3000", propCulture);
		builder.addGeoTiffOutput("pNV_6", propBois);
		builder.addGeoTiffOutput("pNVm_3&8&16&42", propImpermeabilise);
		builder.addGeoTiffOutput("pNV_11", propEau);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}

	private static void analyseHaie(){
		
		recuperationHaie();
		analyseLandscapeHaie();
		classificationHaie();
		
	}

	private static void recuperationHaie(){
		
		Coverage cov = CoverageManager.getCoverage(rasterBio);
		EnteteRaster refEntete = cov.getEntete();
		cov.dispose();
		
		Coverage haieCov = CoverageManager.getCoverage("E:/grain_bocager/data/35/2020/35_2020_type_boisement.tif");
		EnteteRaster entete = haieCov.getEntete();
		float[] data = haieCov.getData(EnteteRaster.getROI(entete, refEntete.getEnvelope()));
		haieCov.dispose();
		
		
		CoverageManager.write(rasterHaie, data, refEntete);
	}
	
	private static void analyseLandscapeHaie() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(rasterHaie);
		builder.addMetric("pNVm_1&10");
		//builder.setWindowSize(401);
		builder.setWindowSize(601);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("pNVm_1&10", propHaie);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void classificationHaie() {

		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		//domains.put(DomainFactory.getFloatDomain("[0,0]"), 0);
		domains.put(DomainFactory.getFloatDomain("[0,0.05]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.05,0.07]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.07,0.09]"), 3);
		domains.put(DomainFactory.getFloatDomain("]0.09,0.11]"), 4);
		domains.put(DomainFactory.getFloatDomain("]0.11,]"), 5);
		
		
		ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.CLASSIFICATION);
		builder.setRasterFile(propHaie);
		builder.setDomains(domains);
		builder.addGeoTiffOutput(classifHaie);
		ChloeAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	
}
