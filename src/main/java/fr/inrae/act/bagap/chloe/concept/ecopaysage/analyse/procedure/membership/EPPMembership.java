package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.membership;

import java.io.File;

import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.gradient.EPPGradient;

public class EPPMembership extends EcoPaysageProcedure {

	public EPPMembership(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(String inputRaster : manager().inputRasters()) {
			String carto = manager().carto(inputRaster);
			for(int k : manager().classes()) {
				if(!new File(manager().gradientFile(carto, k)).exists()) {
					force = true;
					break;
				}
			}
		}
		
		if(force) {
			new EPPGradient(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		for(String inputRaster : manager().inputRasters()) {

			String carto = manager().carto(inputRaster);
		
			for(int k : manager().classes()) {
				
				System.out.println("analyse des 'membership' pour "+k+" classes");
				
				EcoPaysage.analyseMembership(manager().membershipFile(carto, k), manager().gradientFile(carto, k), k);
				//EcoPaysage.analyseMembershipStrict(manager().membershipFile(k), manager().gradientFile(k), manager().infoFile(k), k);
			}
			
			System.out.println("recuperation de l'entete raster");
			
			EnteteRaster header = EcoPaysage.getHeader(manager().headerFile(), manager().noDataValue());
			
			for(int k : manager().classes()) {
				
				for(int ik=1; ik<=k; ik++) {
					System.out.println("export cartographique vers "+manager().membershipMapFile(carto, k, ik));
					
					EcoPaysage.exportMap(manager().membershipMapFile(carto, k, ik), manager().membershipFile(carto, k), ik, header);
				}
			}
		}
	}

}
