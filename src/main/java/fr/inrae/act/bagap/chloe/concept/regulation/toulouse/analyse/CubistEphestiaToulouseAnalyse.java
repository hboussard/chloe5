package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.analyse;

import fr.inrae.act.bagap.apiland.raster.CoverageManager;

public class CubistEphestiaToulouseAnalyse {
	
	private CubistEphestiaToulouseManager manager;

	public CubistEphestiaToulouseAnalyse(CubistEphestiaToulouseManager manager) {
		this.manager = manager;
	}
	
	public void run() {
		
		float[] dataModel = CubistEphestiaToulouse.calculate(
				manager.cubistModel(),
				manager.dataCover(),
				manager.dataSystem(),
				manager.enteteInput(),
				manager.enteteOutput(),
				manager.delta(),
				manager.cultures(),
				manager.snh_surf(),
				manager.snh_lin(),
				manager.mapIFT(),
				manager.meteo());
		
		CoverageManager.write(manager.modelOutput(), dataModel, manager.enteteOutput());
	}
	
}
