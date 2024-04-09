package fr.inrae.act.bagap.chloe.script;

import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;
import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inra.sad.bagap.apiland.analysis.tab.SearchAndReplacePixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.util.FileMap;
import fr.inrae.act.bagap.chloe.util.Util;
import fr.inrae.act.bagap.chloe.window.WindowShapeType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;
import fr.inrae.act.bagap.raster.converter.ShapeFile2CoverageConverter;

public class ScriptErosionMagdelaine {

	private static final String path = "C:/Data/projet/coterra/essai_petit_magdelaine/data/";
	
	private static final String bv = path+"bv_magdelaine.tif";
	private static final String bv_os_bre = path+"bv_magdelaine_OSO_bre.tif";
	private static final String bv_os = path+"bv_magdelaine_OS.tif"; 
	private static final String bv_os_prairie = path+"bv_magdelaine_OS_prairie.tif"; 
	private static final String bv_altitude = path+"bv_altitude.tif";
	private static final String bv_infiltration_map = path+"infiltration_map.txt";
	private static final String bv_versement_map = path+"versement_map.txt";
	
	private static final String output_path = path+"erosion/";
	private static final String bv_intensity = output_path+"bv_intensity.tif";
	private static final String bv_direction = output_path+"bv_direction.tif";
	private static final String bv_infiltration = output_path+"bv_infiltration.tif";
	private static final String bv_versement = output_path+"bv_versement.tif";
	private static final String bv_intensite_versement = output_path+"bv_intensite_versement.tif";
	
	public static void main(String[] args) {
		
		Util.createAccess(output_path);
		
		//rasterizeBV();
		//recuperationOS();
		//recuperationAltitude();
		//recuperePrairieFromRPG();
		//detectionPente();
		
		// situation
		generationCoeffInfiltration();
		generationCoeffVersement();
		generationIntensiteVersement();
	
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
		builder.addMetric("slope-direction");
		builder.addMetric("slope-intensity");
		builder.setWindowSize(3);
		builder.addGeoTiffOutput("slope-direction", bv_direction);
		builder.addGeoTiffOutput("slope-intensity", bv_intensity);
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
	}
	
	private static void generationCoeffInfiltration() {
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_infiltration_map, "cover", "infiltration") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_infiltration, data, osEntete);
	}
	
	private static void generationCoeffVersement() {
		Coverage osCov = CoverageManager.getCoverage(bv_os_prairie);
		EnteteRaster osEntete = osCov.getEntete();
		float[] osData = osCov.getData();
		osCov.dispose();
		
		FileMap fMap = new FileMap(bv_versement_map, "cover", "versement") ;
		
		float[] data = new float[osData.length];
		SearchAndReplacePixel2PixelTabCalculation cal = new SearchAndReplacePixel2PixelTabCalculation(data, osData, fMap.getMap());
		cal.run();
		
		CoverageManager.write(bv_versement, data, osEntete);
	}
	
	private static void generationIntensiteVersement() {
		
		Coverage cov1 = CoverageManager.getCoverage(bv_intensity);
		EnteteRaster entete = cov1.getEntete();
		float[] data1 = cov1.getData();
		cov1.dispose();
		
		Coverage cov2 = CoverageManager.getCoverage(bv_versement);
		float[] data2 = cov2.getData();
		cov2.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, data1, data2){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 == -1){
					return -1;
				}
				v1 = Math.abs(90-v1);
				float v2 = v[1];
				return v1*v2*100.0f;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_intensite_versement, data, entete);
	}

	private static void recuperePrairieFromRPG() {
		
		Coverage bvCov = CoverageManager.getCoverage(bv_os);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Map<String, Integer> codes = new HashMap<String, Integer>();
		codes.put("PPH", 24);
		codes.put("PRL", 24);
		codes.put("SPL", 24);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"rpg/parcelle_rpg_2020.shp", "CODE_CULTU", codes, bvEntete, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		float[] outData = new float[bvEntete.width()*bvEntete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(outData, bvData, data){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 != -1){
					float v2 = v[1];
					if(v1 == 25 || v1 == 23 || v1 == 27) {
						return v1;
					}
					if(v2 > 0) {
						return v2;
					}
					if(v2 == -1 && v1 == 13) {
						return 24;
					}
					return v1;
				}
				return -1;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_os_prairie, outData, bvEntete);
	}

	private static void rasterizeBV() {
		
		Envelope envelope = ShapeFile2CoverageConverter.getEnvelope(path+"Couche SIG BV Magdelaine/BV_Magdelaine.shp", 0);
		Coverage refCov = CoverageManager.getCoverage(bv_os_bre);
		EnteteRaster refEntete = refCov.getEntete();
		refCov.dispose();
		refEntete.setNoDataValue(-1);

		EnteteRaster entete = EnteteRaster.getEntete(refEntete, envelope);
		
		Coverage cov = ShapeFile2CoverageConverter.getSurfaceCoverage(path+"Couche SIG BV Magdelaine/BV_Magdelaine.shp", entete, 1, -1);
		float[] data = cov.getData();
		cov.dispose();
		
		CoverageManager.write(bv, data, entete);
	}
	
	private static void recuperationOS() {
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster entete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		Coverage osCov = CoverageManager.getCoverage(bv_os_bre);
		EnteteRaster enteteCov = osCov.getEntete();
		float[] osData = osCov.getData(EnteteRaster.getROI(enteteCov, entete.getEnvelope()));
		osCov.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		Pixel2PixelTabCalculation cal = new Pixel2PixelTabCalculation(data, bvData, osData){
			@Override
			protected float doTreat(float[] v) {
				float v1 = v[0];
				if(v1 != -1){
					return v[1];
				}
				return -1;
			}
		};
		cal.run();
		
		CoverageManager.write(bv_os, data, entete);
	}
	
	private static void recuperationAltitude(){
		
		Coverage bvCov = CoverageManager.getCoverage(bv);
		EnteteRaster bvEntete = bvCov.getEntete();
		float[] bvData = bvCov.getData();
		bvCov.dispose();
		
		String path = "D:/data/sig/bd_alti/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI_2-0_5M_ASC_LAMB93-IGN69_D031_2021-05-12/RGEALTI/1_DONNEES_LIVRAISON_2021-10-00009/";
		String alti = path+"RGEALTI_MNT_5M_GEOTIF_LAMB93_IGN69_D031/";
		Coverage altCov = CoverageManager.getCoverage(alti);
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
}
