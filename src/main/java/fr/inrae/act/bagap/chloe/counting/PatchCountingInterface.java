package fr.inrae.act.bagap.chloe.counting;

public interface PatchCountingInterface {

	/**
	 * the number of distinct patches according to 8 neigbourhood
	 * @return the number distinct of patches
	 */
	int getNbPatches();
	
	/**
	 * the total surface of patches in hectares
	 * @return the total surface of patches
	 */
	double getTotalSurface();
	
}
