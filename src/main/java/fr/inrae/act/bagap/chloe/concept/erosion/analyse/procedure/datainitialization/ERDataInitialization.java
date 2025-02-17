package fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.datainitialization;

import fr.inrae.act.bagap.chloe.concept.erosion.analyse.Erosion;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionManager;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedure;
import fr.inrae.act.bagap.chloe.concept.erosion.analyse.procedure.ErosionProcedureFactory;

public class ERDataInitialization extends ErosionProcedure {

	public ERDataInitialization(ErosionProcedureFactory factory, ErosionManager manager) {
		super(factory, manager);
	}

	@Override
	public void doInit() {
		
		// do nothing
	}

	@Override
	public void doRun() {
	
		System.out.println("rasteurisation du bassin versant");
		
		Erosion.bvRasterization(manager().territory(), manager().elevationFolders(), manager().territoryShape(), manager().territoryIDAttribute(), manager().territoryIDValues());
		
		System.out.println("recuperation de l'altitude (RGE_alti, IGN)");
		
		Erosion.elevationConstruction(manager().elevation(), manager().territory(), manager().elevationFolders());
		
		System.out.println("recuperation de l'occupation des sols (OSO, Theia)");
		
		Erosion.osRecovery(manager().os(), manager().territory(), manager().osSource(), 2);
		
		System.out.println("recuperation des boisements surfaciques (bd_topo, Zone de vegetation, IGN)");
		
		for(String sws : manager().surfaceWoodShapes()) {
			Erosion.surfaceWoodRasterization(manager().os(), sws, manager().surfaceWoodAttribute(), manager().surfaceWoodCodes());
		}
		
		System.out.println("recuperation des boisements lineaires (bd_topo, Haie, IGN)");
		
		for(String lws : manager().linearWoodShapes()) {
			Erosion.linearWoodRasterization(manager().os(), lws, manager().linearWoodCode());
		}
		
		System.out.println("recuperation des routes lineaires (bd_topo, troncon de route, IGN)");
		
		for(String lrs : manager().linearRoadShapes()) {
			Erosion.linearRoadRasterization(manager().os(), lrs, manager().linearRoadAttribute(), manager().linearRoadCodes());
		}
		
		System.out.println("recuperation des voies ferrees (bd_topo, troncon de voie ferree, IGN)");
		
		for(String lts : manager().linearTrainShapes()) {
			Erosion.linearTrainRasterization(manager().os(), lts, manager().linearTrainCode());
		}
		
		System.out.println("recuperation des surfaces hydrographiques (bd_topo, surface hydrographique, IGN)");
		
		for(String sws : manager().surfaceWaterShapes()) {
			Erosion.surfaceWaterRasterization(manager().os(), sws, manager().surfaceWaterCode());
		}
		
		System.out.println("recuperation des troncons hydrographiques (bd_topo, troncon hydrographique, IGN)");
		
		for(String lws : manager().linearWaterShapes()) {
			Erosion.linearWaterRasterization(manager().os(), lws, manager().linearWaterCode());
		}
		
	}
		

}