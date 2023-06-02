package fr.inrae.act.bagap.chloe.concept.grainbocager.script;


import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.territory.GrainBocagerTerritoire;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.territory.GrainBocagerTerritoireBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptTerritoire {

	public static void main(String[] args){
		
		//scriptMartigneFerchaud();
		//scriptCUMAQuestembert();
		scriptRSELeRheu();
		
	}
	
	private static void scriptRSELeRheu() {
		
		long begin = System.currentTimeMillis();
		
		GrainBocagerTerritoireBuilder builder = new GrainBocagerTerritoireBuilder();
		
		builder.setOutputPath("H:/rse/grain_bocager_50m/");
		builder.setBocage("H:/IGN/data/35_2020_5m/mean/");
		builder.setTerritoire("H:/rse/data/DonnéesRHEU_Hugues/rse_lerheu.shp");
		builder.setName("rse_lerheu_50m");
		builder.setOutCellSize(50);
		
		GrainBocagerTerritoire gbTerritoire = builder.build();
		
		gbTerritoire.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
	}
	
	private static void test() {
		
		Coverage covGrainBocager = CoverageManager.getCoverage("H:/questembert/grain_bocager/grain_bocager.tif");
		EnteteRaster entete = covGrainBocager.getEntete();
		float[] dataGrainBocager = covGrainBocager.getDatas();
		covGrainBocager.dispose();
		
		Coverage covDistanceBoisement = CoverageManager.getCoverage("H:/questembert/grain_bocager/distance_boisement_questembert.tif");
		float[] dataDistanceBoisement = covDistanceBoisement.getDatas();
		covDistanceBoisement.dispose();
		
		float[] data = new float[entete.width()*entete.height()];
		
		Pixel2PixelTabCalculation pptcc = new Pixel2PixelTabCalculation(data, dataGrainBocager, dataDistanceBoisement){
			@Override
			protected float doTreat(float[] v) {
				return 1-((1-v[0])*(1-v[1]));
			}
		};
		pptcc.run();
		
		CoverageManager.write("H:/questembert/grain_bocager/test.tif", data, entete);
	}
	
	private static void scriptCUMAQuestembert() {
		
		GrainBocagerTerritoireBuilder builder = new GrainBocagerTerritoireBuilder();
		
		builder.setOutputPath("H:/questembert/grain_bocager/");
		builder.setBocage("H:/IGN/data/56_2019_5m/mean/");
		builder.setTerritoire("H:/questembert/data/communes_CUMA_questembert.shp");
		builder.setName("questembert");
		
		GrainBocagerTerritoire gbTerritoire = builder.build();
		
		gbTerritoire.run();
		
	}

	private static void scriptMartigneFerchaud() {
		
		GrainBocagerTerritoireBuilder builder = new GrainBocagerTerritoireBuilder();
		
		builder.setOutputPath("H:/rafcom/grain_bocager/");
		builder.setBocage("H:/temp/tile_coverage/mean/");
		builder.setTerritoire("H:/rafcom/data/perimetre_RAF_2022_cadastre.shp");
		builder.setName("raf");
		builder.setReplantationBocagere("H:/rafcom/data/plantations_RAF_2008-2023_l93.shp");
		
		GrainBocagerTerritoire gbTerritoire = builder.build();
		
		gbTerritoire.run();
		
	}
	
	
	
}
