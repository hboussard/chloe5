package fr.inrae.act.bagap.chloe.script;

import java.io.File;

import org.locationtech.jts.geom.Envelope;

import fr.inrae.act.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.analysis.ChloeAnalysisType;
import fr.inrae.act.bagap.chloe.distance.analysis.functional.TabRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabInverseAltitudeRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabInverseAltitudeRCMDistanceAnalysis2;
import fr.inrae.act.bagap.chloe.distance.analysis.slope.TabInverseSlopeDirectionRCMDistanceAnalysis;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.raster.Tile;
import fr.inrae.act.bagap.apiland.raster.converter.ShapeFile2CoverageConverter;

public class ScriptErosionRuissellement {

	private static final String path = "E:/temp/slope/test2/data/";
	private static final String bv = path+"bv.tif"; 
	private static final String bv_troncon_hydrographique = path+"bv_troncon_hydrographique.tif";
	private static final String bv_surface_hydrographique = path+"bv_surface_hydrographique.tif";
	private static final String bv_reseau_hydrographique = path+"bv_reseau_hydrographique_plat.tif";
	private static final String bv_altitude = path+"bv_altitude.tif";
	private static final String bv_altitude_lisse = path+"bv_altitude_lisse.tif";
	private static final String bv_intensity = path+"bv_intensity_brute.tif";
	private static final String bv_direction = path+"bv_direction_brute.tif";
	private static final String bv_slope = path+"bv_slope.tif";
	private static final String bv_creux = path+"bv_creux.tif";
	
	// situation initiale
	private static final String bv_os = path+"bv_os.tif";
	private static final String bv_abattement = path+"bv_abattement.tif";
	private static final String bv_distance_eau = path+"bv_distance_eau.tif";
	private static final String bv_distance_eau_altitude =  path+"bv_distance_eau_altitude_bis.tif";
	private static final String bv_distance_eau_direction = path+"bv_distance_eau_direction_brute_reseau_plat.tif";
	private static final String bv_coef_distance_eau = path+"bv_coef_distance_eau_reseau_plat.tif";
	private static final String bv_coeff_versement = path+"bv_coeff_versement.tif";
	private static final String bv_risque_erosif_local = path+"bv_risque_erosif_local_brute.tif";
	private static final String bv_risque_erosif = path+"bv_risque_erosif_reseau_plat.tif";
	/*
	// situation amenagement
	private static final String bv_os = path+"bv_os_amenagement.tif";
	private static final String bv_abattement = path+"bv_abattement_amenagement.tif";
	private static final String bv_coeff_versement = path+"bv_coeff_versement_amenagement.tif";
	private static final String bv_distance_eau = path+"bv_distance_eau_amenagement.tif";
	private static final String bv_distance_eau_altitude =  path+"bv_distance_eau_altitude_amenagement.tif";
	private static final String bv_distance_eau_direction = path+"bv_distance_eau_direction_amenagement.tif";
	private static final String bv_coef_distance_eau = path+"bv_coef_distance_eau_amenagement.tif";
	private static final String bv_risque_erosif_local = path+"bv_risque_erosif_local_amenagement.tif";
	private static final String bv_risque_erosif = path+"bv_risque_erosif_amenagement.tif";
	*/
	public static void main(String[] args) {
		
		// initialisation
		convertRGE();
		recuperationBV();
		recuperationAltitude();
		//cleanAltitude();
		//detectionPente();
		//detectioncreux();
		//cleanIntensite();
		recuperationTronconHydrographique();
		//recuperationOccupationSol();
		//recuperationSurfaceHydrographique();
		recuperationReseauHydrographique();
		
		// situation
		generationFrictionAbattement();
		//generationCoeffVersement();
		//calculDistanceFromEau();
		//calculDistanceFromEauWithAltitude();
		//calculDistanceFromEauWithDirection();
		//calculCoeffDistanceEau();
		//calculRisqueErosifLocal();
		//calculRisqueErosif();
		
		// amenagement
		recuperationOccupationSolAmenagement();
		//generationFrictionAbattement();
		//generationCoeffVersement();
		//calculDistanceFromEau();
		//calculDistanceFromEauWithAltitude();
		//calculDistanceFromEauWithDirection();
		//calculCoeffDistanceEau();
		//calculRisqueErosifLocal();
		//calculRisqueErosif();
		
		//test();
	}
	
	private static void recuperationReseauHydrographique() {
		Coverage covS = CoverageManager.getCoverage(bv_surface_hydrographique);
		EnteteRaster entete = covS.getEntete();
		float[] dataS = covS.getData();
		covS.dispose();
		
		Coverage covC = CoverageManager.getCoverage(bv_creux);
		float[] dataC = covC.getData();
		covC.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataS, dataC){
			@Override
			protected float doTreat(float[] v) {
				float vS = v[0];
				if(vS == -1){
					return -1;
				}
				float vC = v[1];
				if(vC == 2 || vC == 1){
					return 1;
				}
				return vS;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_reseau_hydrographique, data, entete);
	}

	private static void detectioncreux() {
		Coverage cov = CoverageManager.getCoverage(bv_intensity);
		EnteteRaster entete = cov.getEntete();
		float[] dataInt = cov.getData();
		cov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataInt){
			@Override
			protected float doTreat(float[] v) {
				float vInt = v[0];
				if(vInt == -1){
					return -1;
				}
				if(vInt == 90f){
					return 1;
				}
				if(vInt > 90f){
					return 2;
				}
				return 0;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_creux, data, entete);
	}

	private static void test(){
		
		double vc = 60;
		double v = 58;
		
		double diffHauteur = vc - v;
		double diffDistance = 5.0;
		
		double tangente = diffDistance/diffHauteur;
		
		double slopeIntensity = ((90 + (90 + Math.toDegrees(Math.atan(tangente))))%180.0);
		
		
		System.out.println(tangente+" "+slopeIntensity);
		
		v = 62;
		
		diffHauteur = vc - v;
		diffDistance = 5.0;
		
		tangente = diffDistance/diffHauteur;
		slopeIntensity = ((90 + (90 + Math.toDegrees(Math.atan(tangente))))%180.0);
		
		System.out.println(tangente+" "+slopeIntensity);
	}

	
	private static void calculRisqueErosif(){
		Coverage covCDE = CoverageManager.getCoverage(bv_coef_distance_eau);
		EnteteRaster entete = covCDE.getEntete();
		float[] dataCDE = covCDE.getData();
		covCDE.dispose();
		
		Coverage covREL = CoverageManager.getCoverage(bv_risque_erosif_local);
		float[] dataREL = covREL.getData();
		covREL.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataCDE, dataREL){
			@Override
			protected float doTreat(float[] v) {
				float vcde = v[0];
				if(vcde == -1){
					return -1;
				}
				return vcde * v[1];
			}
		};
		cal.run();
		
		CoverageManager.write(bv_risque_erosif, data, entete);
	}
	
	private static void calculCoeffDistanceEau(){
		Coverage covED = CoverageManager.getCoverage(bv_distance_eau_direction);
		EnteteRaster entete = covED.getEntete();
		float[] dataED = covED.getData();
		covED.dispose();
		/*
		Coverage covEA = CoverageManager.getCoverage(bv_distance_eau_altitude);
		float[] dataEA = covEA.getData();
		covEA.dispose();
		
		Coverage covE = CoverageManager.getCoverage(bv_distance_eau);
		float[] dataE = covE.getData();
		covE.dispose();
		*/
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataED){
			@Override
			protected float doTreat(float[] v) {
				float vED = v[0];
				if(vED == -1){
					return -1;
				}
				if(vED > 5000){
					return 0;
				}
				return (5000 - vED)/5000;
			}
		};
		/*Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataED, dataEA, dataE){
			@Override
			protected float doTreat(float[] v) {
				float vED = v[0];
				float d = 1;
				if(vED < 0){
					float vEA = v[1];
					if(vEA < 0){
						float vE = v[2];
						if(vE < 0){
							return -1;
						}else{
							d = vE;
						}
					}else{
						d = vEA;
					}
				}else{
					d = vED;
				}
				if(d > 5000){
					return 0;
				}
				return (5000 - d)/5000;
			}
		};*/
		cal.run();
		
		CoverageManager.write(bv_coef_distance_eau, data, entete);
	}

	private static void calculRisqueErosifLocal(){
		Coverage osSlope = CoverageManager.getCoverage(bv_slope);
		EnteteRaster entete = osSlope.getEntete();
		float[] dataSlope = osSlope.getData();
		osSlope.dispose();
		
		Coverage osVers = CoverageManager.getCoverage(bv_coeff_versement);
		float[] dataVers = osVers.getData();
		osVers.dispose();
		
		float[] dataSlopeVers = new float[dataSlope.length];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(dataSlopeVers, dataSlope, dataVers){
			@Override
			protected float doTreat(float[] v) {
				float vs = v[0];
				if(vs != -1){
					return vs * v[1];
				}
				return -1;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_risque_erosif_local, dataSlopeVers, entete);
	}
	
	private static void cleanIntensite(){
		
		Coverage intCov = CoverageManager.getCoverage(bv_intensity);
		EnteteRaster entete = intCov.getEntete();
		float[] intData = intCov.getData();
		intCov.dispose();
		
		float[] data = new float[intData.length];
		
		for(int ind=0; ind<intData.length; ind++){
			double v = intData[ind];
			if(v != entete.noDataValue()){
				if(v <= 45){
					data[ind] = 1;
				}else{
					data[ind] = (float) ((90-v)/45.0);
				}
			}else{
				data[ind] = entete.noDataValue();
			}
		}
		
		CoverageManager.write(bv_slope, data, entete);
	}
	
	private static void detectionPente(){
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		EnteteRaster entete = altCov.getEntete();
		float[] altData = altCov.getData();
		altCov.dispose();
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(altData);
		builder.setEntete(entete);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setUnfilters(new int[]{-1});
		builder.addMetric("slopedirection");
		builder.addMetric("slopeintensity");
		builder.setWindowSize(3);
		builder.addGeoTiffOutput("slopedirection", bv_direction);
		builder.addGeoTiffOutput("slopeintensity", bv_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void calculDistanceFromEauWithDirection(){
		
		Coverage eauCov = CoverageManager.getCoverage(bv_reseau_hydrographique);
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage(bv_abattement);
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		Coverage dirCov = CoverageManager.getCoverage(bv_direction);
		float[] dirData = dirCov.getData();
		dirCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabInverseSlopeDirectionRCMDistanceAnalysis analysis = new TabInverseSlopeDirectionRCMDistanceAnalysis(data, eauData, abbData, dirData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write(bv_distance_eau_direction, data, entete);
	}

	
	private static void calculDistanceFromEauWithAltitude(){
		
		Coverage eauCov = CoverageManager.getCoverage(bv_surface_hydrographique);
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage(bv_abattement);
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage(bv_altitude_lisse);
		float[] altData = altCov.getData();
		altCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabInverseAltitudeRCMDistanceAnalysis analysis = new TabInverseAltitudeRCMDistanceAnalysis(data, eauData, abbData, altData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1},1000);
		//TabInverseAltitudeRCMDistanceAnalysis2 analysis = new TabInverseAltitudeRCMDistanceAnalysis2(data, eauData, abbData, altData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write(bv_distance_eau_altitude, data, entete);
	}
	
	private static void calculDistanceFromEau(){
		
		Coverage eauCov = CoverageManager.getCoverage(bv_surface_hydrographique);
		EnteteRaster entete = eauCov.getEntete();
		float[] eauData = eauCov.getData();
		eauCov.dispose();
		
		Coverage abbCov = CoverageManager.getCoverage(bv_abattement);
		float[] abbData = abbCov.getData();
		abbCov.dispose();
		
		float[] data = new float[eauData.length];
		
		TabRCMDistanceAnalysis analysis = new TabRCMDistanceAnalysis(data, eauData, abbData, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{1});
		analysis.allRun();
		
		CoverageManager.write(bv_distance_eau, data, entete);
	}
	
	private static void generationFrictionAbattement(){
		Coverage osCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[osData.length];
		for(int ind=0; ind<osData.length; ind++){
			int vos = (int) osData[ind];
			switch(vos){
			case -1 : 
				data[ind] = osEntete.noDataValue();
				break;
			case 1:
				data[ind] = 1;
				break;
			case 2:
				data[ind] = 2;
				break;
			case 3:
				data[ind] = 5;
				break;
			case 4:
				data[ind] = 100;
				break;
			case 5:
				data[ind] = 100;
				break;
			case 6:
				data[ind] = 10;
				break;
			case 7:
				data[ind] = 0;
				break;
			case 8:
				data[ind] = 0;
				break;
			case 9:
				data[ind] = 0;
				break;
			case 10:
				data[ind] = 0;
				break;
			case 11:
				data[ind] = 0;
				break;
			case 12:
				data[ind] = 0;
				break;
			}
		}
		
		CoverageManager.write(bv_abattement, data, osEntete);
	}
	
	private static void generationCoeffVersement(){
		Coverage osCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[osData.length];
		for(int ind=0; ind<osData.length; ind++){
			int vos = (int) osData[ind];
			switch(vos){
			case -1 : 
				data[ind] = osEntete.noDataValue();
				break;
			case 1:
				data[ind] = 1f;
				break;
			case 2:
				data[ind] = 0.25f;
				break;
			case 3:
				data[ind] = 0f;
				break;
			case 4:
				data[ind] = 0;
				break;
			case 5:
				data[ind] = 0;
				break;
			case 6:
				data[ind] = 0;
				break;
			case 7:
				data[ind] = 0;
				break;
			case 8:
				data[ind] = 0;
				break;
			case 9:
				data[ind] = 0;
				break;
			case 10:
				data[ind] = 0;
				break;
			case 11:
				data[ind] = 0;
				break;
			case 12:
				data[ind] = 0;
				break;
			}
		}
		
		CoverageManager.write(bv_coeff_versement, data, osEntete);
	}
	
	private static void recuperationOccupationSol(){
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage("G:/data/sig/bretagne/Bretagne_2019_dispositif_bocage_ebr.tif");
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData(EnteteRaster.getROI(osEntete, bvEntete.getEnvelope()));
		osCov.dispose();
		
		for(int ind=0; ind<osData.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				osData[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_os.tif", osData, bvEntete);
	}
	
	private static void recuperationOccupationSolAmenagement(){
		Coverage cov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv_os.tif");
		EnteteRaster entete = cov.getEntete();
		float[] dataOS = cov.getData();
		cov.dispose();
		
		Coverage covAmenagement = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/temp/slope/test/data/amenagement_bocager.shp", entete, 4, 0);
		float[] dataAm = covAmenagement.getData();
		covAmenagement.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, dataOS, dataAm){
			@Override
			protected float doTreat(float[] v) {
				float vam = v[1];
				if(vam == 4){
					return vam;
				}
				return v[0];
			}
		};
		cal.run();
		
		CoverageManager.write("H:/temp/slope/test/data/bv_os_amenagement.tif", data, entete);
	}
	
	
	private static void recuperationSurfaceHydrographique(){
		
		Coverage thCov = CoverageManager.getCoverage(bv_troncon_hydrographique);
		EnteteRaster thEntete = thCov.getEntete();
		float[] thData = thCov.getData();
		thCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage(bv_os);
		float[] osData = osCov.getData();
		osCov.dispose();
		
		float[] data = new float[thData.length];
		for(int ind=0; ind<osData.length; ind++){
			if(thData[ind] == thEntete.noDataValue()){
				data[ind] = thEntete.noDataValue();
			}else if(thData[ind] == 1 || osData[ind] == 7){
				data[ind] = 1;
			}else{
				data[ind] = 0;
			}
		}
		
		CoverageManager.write(bv_surface_hydrographique, data, thEntete);
	}
	
	private static void recuperationTronconHydrographique(){
		
		Coverage bvCov = CoverageManager.getCoverage("H:/temp/slope/test/data/bv.tif");
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage cov = ShapeFile2CoverageConverter.getLinearCoverage("G:/data/sig/bd_topo/archive/2019/BDTOPO_3-0_TOUSTHEMES_SHP_LAMB93_D035_2019-06-20/BDTOPO/1_DONNEES_LIVRAISON_2019-06-00361/BDT_3-0_SHP_LAMB93_D035-ED2019-06-20/HYDROGRAPHIE/TRONCON_HYDROGRAPHIQUE.shp", bvEntete, 1, 0);
		float[] data = cov.getData();
		cov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_troncon_hydrographique.tif", data, bvEntete);
	}
	
	private static void cleanAltitude(){
		Coverage altCov = CoverageManager.getCoverage(bv_altitude);
		EnteteRaster entete = altCov.getEntete();
		float[] altData = altCov.getData();
		altCov.dispose();
		
		float[] data = new float[altData.length];

		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		//builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowSize(15);
		builder.setUnfilters(new int[]{-1});
		builder.setRasterTab(altData);
		builder.setEntete(entete);
		builder.addMetric("average");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		CoverageManager.write(bv_altitude_lisse, data, entete);
	}
	
	private static void recuperationAltitude(){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage altCov = CoverageManager.getCoverage("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		EnteteRaster entete = altCov.getEntete();
		float[] data = altCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		altCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write(bv_altitude, data, bvEntete);
	}
	
	private static void recuperationBV(){
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope("H:/temp/slope/test/data/bv.shp", 0);
		Coverage refCov = CoverageManager.getCoverage("G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/");
		EnteteRaster refEntete = refCov.getEntete();
		refCov.dispose();
		refEntete.setNoDataValue(-1);

		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage("H:/temp/slope/test/data/bv.shp", entete, 1, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(bv, data, entete);
		
	}
	
	private static void convertRGE(){
		String path = "G:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D035_2020-01-27/RGEALTI/1_DONNEES_LIVRAISON_2020-04-00197/";
		String inputPath = "RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035/";
		String outputPath = "RGEALTI_MNT_5M_ASC_LAMB93_IGN69_D035_geotiff/";
		
		for(String file : new File(path+inputPath).list()){ 
			if(file.endsWith(".asc")){
				convert(path, inputPath, outputPath, file);
			}
		}
	}
	
	private static void convert(String globalPath, String inputPath, String outputPath, String file) {
		
		Util.createAccess(globalPath+outputPath);
		
		Coverage cov = CoverageManager.getCoverage(globalPath+inputPath+file);
		float[] data = cov.getData();
		EnteteRaster entete = cov.getEntete();
		cov.dispose();
		
		CoverageManager.write(globalPath+outputPath+file.replace(".asc", ".tif"), data, entete);
		
	}
	
	// pour memoire
	private static void cleanDirection(){
		Coverage dirCov = CoverageManager.getCoverage(bv_direction);
		EnteteRaster entete = dirCov.getEntete();
		float[] dirData = dirCov.getData();
		dirCov.dispose();
		
		for(int ind=0; ind<dirData.length; ind++){
			int vd = (int) dirData[ind];
			if(vd == 0){
				dirData[ind] = 9;
			}
		}
		
		float[] data = new float[dirData.length];

		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setAnalysisType(ChloeAnalysisType.SLIDING);
		builder.setWindowShapeType(WindowShapeType.SQUARE);
		builder.setWindowSize(3);
		builder.setRasterTab(dirData);
		builder.setEntete(entete);
		builder.addMetric("Majority");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		for(int ind=0; ind<data.length; ind++){
			int vd = (int) data[ind];
			if(vd == 9){
				data[ind] = 0;
			}
		}
		
		CoverageManager.write("H:/temp/slope/test/data/bv_direction_clean_sq_3.tif", data, entete);
	}
	
	private static void recuperationDirection(){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage dirCov = CoverageManager.getCoverage("H:/temp/slope/direction/");
		EnteteRaster entete = dirCov.getEntete();
		float[] data = dirCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		dirCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write(bv_direction, data, bvEntete);
	}
	
	private static void recuperationIntensity(){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage intCov = CoverageManager.getCoverage("H:/temp/slope/intensity/");
		EnteteRaster entete = intCov.getEntete();
		float[] data = intCov.getData(EnteteRaster.getROI(entete, bvEntete.getEnvelope()));
		intCov.dispose();
		
		for(int ind=0; ind<data.length; ind++){
			if(bvData[ind] == bvEntete.noDataValue()){
				data[ind] = bvEntete.noDataValue();
			}
		}
		
		CoverageManager.write(bv_intensity, data, bvEntete);
	}
	
	
	
	
}
