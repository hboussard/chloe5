package fr.inrae.act.bagap.chloe.concept.grainbocager.analysis;

import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.distance.analysis.euclidian.TabChamferDistanceAnalysis;
import fr.inrae.act.bagap.chloe.window.WindowDistanceType;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysis;
import fr.inrae.act.bagap.chloe.window.analysis.LandscapeMetricAnalysisBuilder;
import fr.inrae.act.bagap.chloe.window.output.CoverageOutput;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class GrainBocager {

	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete){
		return calculate(dataHauteurBoisement, entete, true, null, 5);
	}
	
	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete, double outputCellSize){
		return calculate(dataHauteurBoisement, entete, true, null, outputCellSize);
	}
	
	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast){
		return calculate(dataHauteurBoisement, entete, modeFast, null, 5);
	}
	
	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast, double outputCellSize){
		return calculate(dataHauteurBoisement, entete, modeFast, null, outputCellSize);
	}
	
	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete, String outputName){
		return calculate(dataHauteurBoisement, entete, true, outputName, 5);
	}
	
	public static Coverage calculate(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast, String outputName, double outputCellSize){
		
		// detection des types de boisement
		float[] dataTypeBoisement = detectionTypeBoisement(dataHauteurBoisement, entete, modeFast, outputName);
		if(outputName != null){
			CoverageManager.write(outputName+"_type_boisement.tif", dataTypeBoisement, entete);
		}
		
		// test grain 2D
		//float[] dataTypeBoisement = dataHauteurBoisement;
		
		// calcul de distances ponderees
		float[] dataDistancePonderee = calculInfluenceBoisement(dataHauteurBoisement, dataTypeBoisement, entete, modeFast);
		//if(outputName != null){
		//	CoverageManager.write(outputName+"_influence_boisement.tif", dataDistancePonderee, entete);
		//}
		
		// moyenne globale du grain bocager
		return calculGrainBocager(dataDistancePonderee, entete, outputName, outputCellSize);
	}
	
	private static float[] detectionTypeBoisement(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast, String outputName) {
		
		// detection des boisements phase 1
		float[] dataTypeBoisementPhase1 = detectionTypeBoisementPhase1(dataHauteurBoisement, entete, modeFast);
		//if(outputName != null){
		//	CoverageManager.write(outputName+"_type_boisement_phase1.tif", dataTypeBoisementPhase1, entete);
		//}
		
		// calcul de distance aux massifs boises
		float[] dataDistanceMassif = calculDistanceMassifsBoisesEuclidian(dataTypeBoisementPhase1, entete);
		//if(outputName != null){
		//	CoverageManager.write(outputName+"_distance_massif.tif", dataDistanceMassif, entete);
		//}
		
		// detection des boisements phase 2
		return detectionTypeBoisementPhase2(dataTypeBoisementPhase1, dataDistanceMassif, entete);	
	}
	
	private static float[] detectionTypeBoisementPhase1(float[] dataHauteurBoisement, EnteteRaster entete, boolean modeFast) {
		
		//System.out.println("detection du type de bocage phase 1");
		
		float[] data = new float[entete.width()*entete.height()];
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_GAUSSIAN);
		}else{
			builder.setWindowDistanceType(WindowDistanceType.WEIGHTED);
		}
		
		builder.setRasterTab(dataHauteurBoisement);
		builder.setEntete(entete);
		builder.setWindowSize(21);
		
		builder.addMetric("GBBocage");
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
	
		analysis.allRun();
		
		return data;
	}
	
	private static float[] calculDistanceMassifsBoisesEuclidian(float[] dataTypeBoisementPhase1, EnteteRaster entete){
		
		//System.out.println("calcul de distance aux massifs boises");
		
		float[] data = new float[entete.width()*entete.height()];
		
		TabChamferDistanceAnalysis da = new TabChamferDistanceAnalysis(data, dataTypeBoisementPhase1, entete.width(), entete.height(), entete.cellsize(), entete.noDataValue(), new int[]{5});
		da.allRun();
		
		return data;
	}
	
	private static float[] detectionTypeBoisementPhase2(float[] dataTypeBoisementPhase1, float[] dataDistanceMassif, EnteteRaster entete) {	
		
		//System.out.println("mise à jour du type de bocage phase 2");
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(data, dataTypeBoisementPhase1, dataDistanceMassif){
			@Override
			protected float doTreat(float[] v) {
				float vdm = v[1];
				float vtb = v[0];
				if(vdm == entete.noDataValue()){
					return vtb;
				}
				if(vtb == entete.noDataValue()){
					return entete.noDataValue();
				}
				if(vdm <= 20){
					if(vtb == 10){
						return 5;
					}
				}
				return vtb;
			}
		};
		pptcc.run();
		
		return data;
	}
	
	private static float[] calculInfluenceBoisement(float[] dataHauteurBoisement, float[] dataTypeBoisement, EnteteRaster entete, boolean modeFast) {
				
		//System.out.println("calcul des distances ponderees");
		
		float[] data = new float[entete.width()*entete.height()];
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		if(modeFast){
			builder.setWindowDistanceType(WindowDistanceType.FAST_SQUARE);
		}
		
		builder.setRasterTab(dataHauteurBoisement);
		builder.setRasterTab2(dataTypeBoisement);
		builder.setEntete(entete);
		
		builder.addMetric("GBDistance");
		builder.setWindowSize(121);
		builder.addTabOutput(data);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		return data;
	}
	
	private static Coverage calculGrainBocager(float[] dataDistancePonderee, EnteteRaster entete, String outputName, double outputCellSize) {
				
		//System.out.println("calcul du grain bocager");
		
		int displacement = 1;
		if(entete.cellsize() != outputCellSize){
			displacement = (int) (outputCellSize/entete.cellsize());
		}else{
		}
		
		CoverageOutput covOutput = new CoverageOutput("average");
		
		LandscapeMetricAnalysisBuilder builder = new LandscapeMetricAnalysisBuilder();
		builder.setRasterTab(dataDistancePonderee);
		builder.setEntete(entete);
		
		builder.setDisplacement(displacement); 
		
		builder.addMetric("average");
		builder.setWindowSize(101);
		//builder.addGeoTiffOutput(outputName+"_grain_bocager.tif");
		builder.addCoverageOutput(covOutput);
		
		LandscapeMetricAnalysis analysis = builder.build();
		
		analysis.allRun();
		
		//return data;
		return covOutput.getCoverage();
	}

}
