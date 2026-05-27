package fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.evolution.membership;

import java.io.File;

import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.EcoPaysageProcedure;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.procedure.membership.EPPMembership;

public class EPPMembershipEvolution extends EcoPaysageProcedure {

	public EPPMembershipEvolution(EcoPaysageManager manager) {
		super(manager);
	}

	@Override
	public void doInit() {
		
		boolean force = manager().force();
		for(String inputRaster : manager().inputRasters()) {
			String carto = manager().carto(inputRaster);
			for(int k : manager().classes()) {
				if(!new File(manager().membershipFile(carto, k)).exists()) {
					force = true;
					break;
				}
			}
		}
		
		if(force) {
			new EPPMembership(manager()).run();
		}
	}

	@Override
	public void doRun() {
		
		for(int k : manager().classes()) {
			
			String[] membershipFiles = new String[manager().inputRasters().size()];
			int index = 0;
			for(String inputRaster : manager().inputRasters()) {

				String carto = manager().carto(inputRaster);
			
				membershipFiles[index++] = manager().membershipFile(carto, k);
			}
			
			EcoPaysage.analyseEvoMembership(manager().evoMembershipFile(k), k, membershipFiles);
		}
		
		
	}
		

}
