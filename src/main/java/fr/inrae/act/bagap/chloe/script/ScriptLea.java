package fr.inrae.act.bagap.chloe.script;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedPoint;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.capfarm.CfmUtil;
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
	
	//private static final String centralBio = path+"central_bio.tif";
	
	private static final String rasterOccSol = path+"occsol.tif";
	
	private static final String rasterHaie = path+"haie.tif";
	
	private static final String propCulture = path+"prop_culture_5m.tif";
	
	private static final String propBois = path+"prop_bois_5m.tif";
	
	private static final String propImpermeabilise = path+"prop_impermeabilise_5m.tif";
	
	private static final String propEau = path+"prop_eau_5m.tif";
	
	private static final String propHaie = path+"prop_haie_5m.tif";
	
	private static final String propValidite = path+"prop_validite_5m.tif";
	
	private static final String classifHaie = path+"classification_haie_3classes_4_5m.tif";
	
	private static final String rasterDiversite = path+"diversite3.tif";
	
	//private static final String centralDiversite = path+"central_diversite3.tif";
	
	private static final String rasterParcelle = path+"parcelle.tif";
	
	//private static final String centralParcelle = path+"central_parcelle.tif";
	
	private static final String decision = path+"decision_c65_b10_p14.tif";
	
	private static final String analyseParcelleParCategorie = path+"analyse_parcelle_par_categorie_c65_b10_p14.csv";
	
	private static final String analyseCategorieParParcelle = path+"analyse_categorie_par_parcelle_c65_b10_p14.csv";
	
	public static void main(String[] args) {
		
		//analyseBio();
		
		//analyseOccSol();
		
		//analyseHaie();
		
		//analyseDiversite();
		
		decision(0.14f);
		
		analyseParcelle();
		
		//appelConnu();
	}

	private static void decision(float prop) {
		
		Coverage covBio = CoverageManager.getCoverage(rasterBio);
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
		
		Coverage covDiversite = CoverageManager.getCoverage(rasterDiversite);
		float[] dataDiversite = covDiversite.getData();
		covDiversite.dispose();
		
		Coverage covValidite = CoverageManager.getCoverage(propValidite);
		float[] dataValidite = covValidite.getData();
		covValidite.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataBio, dataCulture, dataBois, dataImpermeabilise, dataEau, dataHaie, dataDiversite, dataValidite){
			@Override
			protected float doTreat(float[] v) {
				float vBio = v[0];
				float validite = v[7];
				if(vBio == -1 || validite < 1){
					return -1;
				}
				
				if(v[1] > 0.65 && v[2] < 0.1 && v[3] < prop && v[4] < prop) {
					
					float vHaie = v[5];
					float vDiversite = v[6];
					if(vDiversite > 0) {
						if(vBio == 1) { // BIO
							
							switch((int) vHaie) {
							case 1 : return 1+((vDiversite-1)*6); 
							case 2 : return 2+((vDiversite-1)*6); 
							case 3 : return 3+((vDiversite-1)*6); 
							
							default : break;
							}
						
						}else if(vBio == 2){ // pas BIO
							
							switch((int) vHaie) {
							case 1 : return 4+((vDiversite-1)*6); 
							case 2 : return 5+((vDiversite-1)*6);  
							case 3 : return 6+((vDiversite-1)*6);  
							
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
		analyseCategorieParParcelle();
		
	}
	
	private static void rasterisationParcelle(String parcelleShapeFile){
		
		ShapeFile2CoverageConverter.rasterize(rasterParcelle, parcelleShapeFile, "code", 5, -1, null);
		
	}
	
	/*
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
	*/
	
	private static void analyseNbParcelleParCategorie() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(rasterParcelle);
		builder.setEntityRasterFile(decision);
		builder.addMetric("Nclass");
		builder.addCsvOutput(analyseParcelleParCategorie);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	

	private static void analyseCategorieParParcelle() {
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.ENTITY);
		builder.setRasterFile(decision);
		builder.setEntityRasterFile(rasterParcelle);
		builder.addMetric("Majority");
		builder.addCsvOutput(analyseCategorieParParcelle);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseDiversite(){
		
		rasterisationDiversite(path+"sequence_zaa_bio_diversity3_2015_2022.shp");
		//analyseLocaleDiversite();
	}

	private static void rasterisationDiversite(String diversiteShapeFile){
		
		ShapeFile2CoverageConverter.rasterize(rasterDiversite, diversiteShapeFile, "Diversity3", 5, -1, null);
	}
	
	/*
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
	*/
	
	private static void analyseBio(){
		
		rasterisationBio(path+"sequence_zaa_bio_2015_2022.shp");
		//analyseLocaleBio();
	}
	
	private static void rasterisationBio(String bioShapeFile){
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("BIO", 1);
		codes.put("", 2);
		
		ShapeFile2CoverageConverter.rasterize(rasterBio, bioShapeFile, "bio_zaa", codes, 5, -1, 0, null);
	}
	/*
	private static void analyseLocaleBio(){
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterFile(rasterBio);
		builder.addMetric("Central");
		builder.setWindowSize(3);
		builder.setDisplacement(10);
		builder.addGeoTiffOutput("Central", centralBio);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}*/
	
	private static void analyseOccSol(){
		
		//rasterisationOccSol(path+"zaa_sud.shp");
		//cleanOccSol();
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
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(rasterOccSol);
		builder.addMetric("pNVm_28&3000");
		builder.addMetric("pNV_6");
		builder.addMetric("pNVm_3&8&16&42");
		builder.addMetric("pNV_11");
		builder.addMetric("pN-valid");
		builder.setWindowSize(401);
		//builder.setWindowSize(601);
		//builder.setDisplacement(10);
		builder.addGeoTiffOutput("pNVm_28&3000", propCulture);
		builder.addGeoTiffOutput("pNV_6", propBois);
		builder.addGeoTiffOutput("pNVm_3&8&16&42", propImpermeabilise);
		builder.addGeoTiffOutput("pNV_11", propEau);
		builder.addGeoTiffOutput("pN-valid", propValidite);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}


	private static void analyseHaie(){
		
		//recuperationHaie();
		//analyseLandscapeHaie();
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
		//builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		builder.setRasterFile(rasterHaie);
		builder.addMetric("pNVm_1&10");
		builder.setWindowSize(401);
		//builder.setWindowSize(601);
		//builder.setDisplacement(10);
		builder.addGeoTiffOutput("pNVm_1&10", propHaie);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void classificationHaie() {

		Map<Domain<Float, Float>, Integer> domains = new HashMap<Domain<Float, Float>, Integer>();
		//domains.put(DomainFactory.getFloatDomain("[0,0]"), 0);
		/*
		domains.put(DomainFactory.getFloatDomain("[0,0.05]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.05,0.07]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.07,0.09]"), 3);
		domains.put(DomainFactory.getFloatDomain("]0.09,0.11]"), 4);
		domains.put(DomainFactory.getFloatDomain("]0.11,]"), 5);
		*/
		/*
		domains.put(DomainFactory.getFloatDomain("[0,0.07]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.07,0.09]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.09,]"), 3);
		*/
		domains.put(DomainFactory.getFloatDomain("[0,0.065]"), 1);
		domains.put(DomainFactory.getFloatDomain("]0.065,0.095]"), 2);
		domains.put(DomainFactory.getFloatDomain("]0.095,]"), 3);
		
		
		ChloeAnalysisBuilder builder = new ChloeUtilAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.CLASSIFICATION);
		builder.setRasterFile(propHaie);
		builder.setDomains(domains);
		builder.addGeoTiffOutput(classifHaie);
		ChloeAnalysis analysis = builder.build();
		
		analysis.allRun();
		
	}
	
	private static void analyseLea(){
		
		String path = "E:/temp/lea/";
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SELECTED);
		builder.setRasterFile(path+"haie_clean.tif");
		builder.setPointsFilter(path+"point_parcelle.csv");
		builder.setWindowSizes(new int[]{401});
		builder.addMetric("pNV_10");	
		builder.addCsvOutput(path+"analyse_test1.csv");
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	public static void appelConnu() {
		String path = "E:/temp/lea/transfer_7227431_files_3b840578/";
		generateShapefileConnues(path+"sequence_categories", path+"Territoires_EA_ZA_sud", path+"sequence_categories_connues");
	}
	
	public static void generateShapefileConnues(String inputSequence, String inputCoonue, String outputSequenceConnue){
		
		
		try(FileOutputStream fos = new FileOutputStream(outputSequenceConnue+".dbf");
				FileOutputStream shp = new FileOutputStream(outputSequenceConnue+".shp");
				FileOutputStream shx = new FileOutputStream(outputSequenceConnue+".shx");){
			
			ShpFiles sfConnue = new ShpFiles(inputCoonue+".shp");
			ShapefileReader sfrConnue = new ShapefileReader(sfConnue, true, false, new org.locationtech.jts.geom.GeometryFactory());
			
			Object[] data;
			Geometry g;
			Set<Geometry> parcellesConnues = new HashSet<Geometry>();
			while(sfrConnue.hasNext()){
				g = (Geometry) sfrConnue.nextRecord().shape();
				parcellesConnues.add(g);
			}
			sfrConnue.close();
			
			ShpFiles sfSequence = new ShpFiles(inputSequence+".shp");
			ShapefileReader sfrSequence = new ShapefileReader(sfSequence, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfrSequence = new DbaseFileReader(sfSequence, true, Charset.defaultCharset());
			DbaseFileHeader headerSequence = dfrSequence.getHeader();
			
			// gestion du header de sortie
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(dfrSequence.getHeader().getNumRecords());
			
			for(int i=0; i<headerSequence.getNumFields(); i++){
				header.addColumn(headerSequence.getFieldName(i), headerSequence.getFieldType(i), headerSequence.getFieldLength(i), headerSequence.getFieldDecimalCount(i));
			}
			header.addColumn("connue", 'c', 3, 0);
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(
					new Envelope(sfrSequence.getHeader().minX(), sfrSequence.getHeader().maxX(), sfrSequence.getHeader().minY(), sfrSequence.getHeader().maxY()), 
					ShapeType.POLYGON, headerSequence.getNumRecords(), 1000000);
			
			Object[] entry = new Object[header.getNumFields()];
			PreparedPoint pp;
			boolean isConnue;
			while(sfrSequence.hasNext()){
				
				data = dfrSequence.readEntry();
				
				for(int i=0; i<headerSequence.getNumFields(); i++){
					entry[i] = data[i];
				}
				
				g = (Geometry) sfrSequence.nextRecord().shape();
				
				pp = new PreparedPoint(g.getInteriorPoint());
				isConnue = false;
				for(Geometry pConnue : parcellesConnues) {
					if(pp.intersects(pConnue)) {
						isConnue = true;
						break;
					}
				}
				if(isConnue) {
					entry[headerSequence.getNumFields()] = "oui";
				}else {
					entry[headerSequence.getNumFields()] = "non";
				}
				
				sfw.writeGeometry(g);
				dfw.write(entry);
			}
			
			sfrSequence.close();
			dfrSequence.close();
			dfw.close();
			sfw.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			CfmUtil.copy(inputSequence+".prj", outputSequenceConnue+".prj");
		}
	}
	
	public static void appel(String[] args) {
		String path = "E:/temp/lea/";
		generateShapefile(path+"sequence_zaa_2015_2022", path+"bio_zaa_2022", "bio_zaa", "BIO", path+"sequence_zaa_bio_2015_2022");
	}
	
	public static void generateShapefile(String inputSequence, String inputBio, String bioColumn, String bioValue, String outputSequenceBio){
		
		
		try(FileOutputStream fos = new FileOutputStream(outputSequenceBio+".dbf");
				FileOutputStream shp = new FileOutputStream(outputSequenceBio+".shp");
				FileOutputStream shx = new FileOutputStream(outputSequenceBio+".shx");){
			
			ShpFiles sfBio = new ShpFiles(inputBio+".shp");
			ShapefileReader sfrBio = new ShapefileReader(sfBio, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfrBio = new DbaseFileReader(sfBio, true, Charset.defaultCharset());
			DbaseFileHeader headerBio = dfrBio.getHeader();
			int indexBio = -1;
			for(int i=0; i<headerBio.getNumFields(); i++){
				if(headerBio.getFieldName(i).equalsIgnoreCase(bioColumn)) {
					indexBio = i;
					break;
				}
			}
			Object[] data;
			Geometry g;
			Set<Geometry> parcellesBio = new HashSet<Geometry>();
			while(sfrBio.hasNext()){
				g = (Geometry) sfrBio.nextRecord().shape();
				data = dfrBio.readEntry();
				if(data[indexBio].toString().equalsIgnoreCase(bioValue)) {
					parcellesBio.add(g);
				}
			}
			sfrBio.close();
			dfrBio.close();
			
			ShpFiles sfSequence = new ShpFiles(inputSequence+".shp");
			ShapefileReader sfrSequence = new ShapefileReader(sfSequence, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfrSequence = new DbaseFileReader(sfSequence, true, Charset.defaultCharset());
			DbaseFileHeader headerSequence = dfrSequence.getHeader();
			
			// gestion du header de sortie
			DbaseFileHeader header = new DbaseFileHeader();
			header.setNumRecords(dfrSequence.getHeader().getNumRecords());
			
			for(int i=0; i<headerSequence.getNumFields(); i++){
				header.addColumn(headerSequence.getFieldName(i), headerSequence.getFieldType(i), headerSequence.getFieldLength(i), headerSequence.getFieldDecimalCount(i));
			}
			header.addColumn(bioColumn, 'c', 3, 0);
			
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(
					new Envelope(sfrSequence.getHeader().minX(), sfrSequence.getHeader().maxX(), sfrSequence.getHeader().minY(), sfrSequence.getHeader().maxY()), 
					ShapeType.POLYGON, headerSequence.getNumRecords(), 1000000);
			
			Object[] entry = new Object[header.getNumFields()];
			PreparedPoint pp;
			boolean isBio;
			while(sfrSequence.hasNext()){
				
				data = dfrSequence.readEntry();
				
				for(int i=0; i<headerSequence.getNumFields(); i++){
					entry[i] = data[i];
				}
				
				g = (Geometry) sfrSequence.nextRecord().shape();
				
				pp = new PreparedPoint(g.getInteriorPoint());
				isBio = false;
				for(Geometry pBio : parcellesBio) {
					if(pp.intersects(pBio)) {
						isBio = true;
						break;
					}
				}
				if(isBio) {
					entry[headerSequence.getNumFields()] = bioValue;
				}else {
					entry[headerSequence.getNumFields()] = "";
				}
				
				sfw.writeGeometry(g);
				dfw.write(entry);
			}
			
			sfrSequence.close();
			dfrSequence.close();
			dfw.close();
			sfw.close();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			CfmUtil.copy(inputSequence+".prj", outputSequenceBio+".prj");
		}
	}
	
	
}
