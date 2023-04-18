package fr.inrae.act.bagap.chloe.window.counting;

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
	
	/**
	 * the max surface of largest patch in hectares
	 * @return the maximum surface of largest patch
	 */
	double getMaxSurface();
	
	/**
	 * the number of distinct patches according to 8 neigbourhood
	 * @return the number distinct of patches of type v
	 */
	int getNbPatches(int v);
	
	/**
	 * the total surface of patches in hectares
	 * @return the total surface of patches of type v
	 */
	double getTotalSurface(int v);
	
	/**
	 * the max surface of largest patch in hectares
	 * @return the maximum surface of largest patch of type v
	 */
	double getMaxSurface(int v);
	
}
