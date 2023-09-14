package fr.inrae.act.bagap.chloe.concept.grainbocager.script;


import fr.inra.sad.bagap.apiland.analysis.tab.Pixel2PixelTabCalculation;
//import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.GrainBocager;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.territory.GrainBocagerTerritoire;
import fr.inrae.act.bagap.chloe.concept.grainbocager.analysis.procedure.territory.GrainBocagerTerritoireBuilder;
import fr.inrae.act.bagap.raster.Coverage;
import fr.inrae.act.bagap.raster.CoverageManager;
import fr.inrae.act.bagap.raster.EnteteRaster;

public class ScriptTerritoire {

	public static void main(String[] args){
		
		scriptMartigneFerchaud();
		//scriptCUMAQuestembert();
		//scriptRSELeRheu();
		//scriptRennesMetropole();
		
	}
	
	private static void scriptRennesMetropole() {
		
		long begin = System.currentTimeMillis();
		
		GrainBocagerTerritoireBuilder builder = new GrainBocagerTerritoireBuilder();
		
		builder.setOutputPath("H:/rennes_metropole/grain_bocager/");
		builder.setBocage("H:/IGN/data/35_2020_5m/mean/");
		builder.setTerritoire("H:/rennes_metropole/data/communes_rennes_metropole_L93.shp");
		builder.setName("rm");
		builder.setBufferArea(5000);
		builder.setOutCellSize(50);
		
		GrainBocagerTerritoire gbTerritoire = builder.build();
		
		gbTerritoire.run();
		
		long end = System.currentTimeMillis();
		System.out.println("time computing : "+(end - begin));
		
		//Coverage cov4Classes5m = GrainBocager.runClassificationNClasses("H:/rennes_metropole/grain_bocager/rm_grain_bocager_5m.tif", 0.20, 0.33, 0.45);
		//CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_5m_4classes.tif", cov4Classes5m.getData(), cov4Classes5m.getEntete());
		//cov4Classes5m.dispose();
		
		/*
		Coverage cov4Classes50m = GrainBocager.runClassificationNClasses("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m.tif", 0.20, 0.33, 0.45);
		CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_4classes.tif", cov4Classes50m.getDatas(), cov4Classes50m.getEntete());
		cov4Classes50m.dispose();
		*/
		//Coverage covClassif = GrainBocager.runClassificationFonctionnelle("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m.tif", 0.33);
		//CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_fonctionnel.tif", covClassif.getData(), covClassif.getEntete());
		//covClassif.dispose();
		
		
		//Coverage covCluster = GrainBocager.runClusterisationGrainFonctionnel("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_fonctionnel.tif");
		//CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_cluster.tif", covCluster.getData(), covCluster.getEntete());
		//covCluster.dispose();
		
		/*
		Coverage covSHDICluster = GrainBocager.runSHDIClusterGrainFonctionnel("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_cluster.tif", 101);
		CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_shdi_cluster.tif", covSHDICluster.getDatas(), covSHDICluster.getEntete());
		covSHDICluster.dispose();
		*/
		/*
		Coverage covPorportionFonctionnelle = GrainBocager.runProportionGrainFonctionnel("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_fonctionnel.tif", 101);
		CoverageManager.writeGeotiff("H:/rennes_metropole/grain_bocager/rm_grain_bocager_50m_proportion_fonctionnelle.tif", covPorportionFonctionnelle.getDatas(), covPorportionFonctionnelle.getEntete());
		covPorportionFonctionnelle.dispose();
		*/
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
		float[] dataGrainBocager = covGrainBocager.getData();
		covGrainBocager.dispose();
		
		Coverage covDistanceBoisement = CoverageManager.getCoverage("H:/questembert/grain_bocager/distance_boisement_questembert.tif");
		float[] dataDistanceBoisement = covDistanceBoisement.getData();
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
		/*
		GrainBocagerTerritoireBuilder builder = new GrainBocagerTerritoireBuilder();
		
		builder.setOutputPath("H:/rafcom/grain_bocager/");
		builder.setBocage("H:/temp/tile_coverage/mean/");
		builder.setTerritoire("H:/rafcom/data/perimetre_RAF_2022_cadastre.shp");
		builder.setName("raf");
		builder.setReplantationBocagere("H:/rafcom/data/plantations_RAF_2008-2023_l93.shp");
		builder.setOutCellSize(50);
		builder.setBufferArea(700);
		GrainBocagerTerritoire gbTerritoire = builder.build();
		
		gbTerritoire.run();
		*/
		
		//String scenario = "";
		String scenario = "simule_";
		/*
		Coverage cov4Classes5m = GrainBocager.runClassificationNClasses("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_5m.tif", 0.20, 0.33, 0.45);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_5m_4classes.tif", cov4Classes5m.getDatas(), cov4Classes5m.getEntete());
		cov4Classes5m.dispose();
		
		Coverage cov4Classes50m = GrainBocager.runClassificationNClasses("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m.tif", 0.20, 0.33, 0.45);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_4classes.tif", cov4Classes50m.getDatas(), cov4Classes50m.getEntete());
		cov4Classes50m.dispose();
		
		Coverage covClassif = GrainBocager.runClassificationFonctionnelle("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m.tif", 0.33);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_fonctionnel.tif", covClassif.getDatas(), covClassif.getEntete());
		covClassif.dispose();
		
		Coverage covCluster = GrainBocager.runClusterisationGrainFonctionnel("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_fonctionnel.tif");
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_cluster.tif", covCluster.getDatas(), covCluster.getEntete());
		covCluster.dispose();
		
		Coverage covSHDICluster = GrainBocager.runSHDIClusterGrainFonctionnel("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_cluster.tif", 101);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_shdi_cluster.tif", covSHDICluster.getDatas(), covSHDICluster.getEntete());
		covSHDICluster.dispose();
		
		Coverage covPorportionFonctionnelle = GrainBocager.runProportionGrainFonctionnel("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_fonctionnel.tif", 101);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_proportion_fonctionnelle.tif", covPorportionFonctionnelle.getDatas(), covPorportionFonctionnelle.getEntete());
		covPorportionFonctionnelle.dispose();
		
		Coverage covSHDICluster = GrainBocager.runSHDIClusterGrainFonctionnel("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_cluster.tif", 41);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_shdi_cluster_1km.tif", covSHDICluster.getDatas(), covSHDICluster.getEntete());
		covSHDICluster.dispose();
		
		Coverage covPorportionFonctionnelle = GrainBocager.runProportionGrainFonctionnel("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_fonctionnel.tif", 41);
		CoverageManager.writeGeotiff("H:/rafcom/grain_bocager/raf_"+scenario+"grain_bocager_50m_proportion_fonctionnelle_1km.tif", covPorportionFonctionnelle.getDatas(), covPorportionFonctionnelle.getEntete());
		covPorportionFonctionnelle.dispose();
		*/
		
	}
	
	
	
}
