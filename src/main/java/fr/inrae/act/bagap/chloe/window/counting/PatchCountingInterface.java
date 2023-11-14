package fr.inrae.act.bagap.chloe.window.counting;

public interface PatchCountingInterface {

	/**
	 * the number of distinct patches according to 8 neigbourhood
	 * @return the number distinct of patches
	 */
	int nbPatches();
	
	/**
	 * the total surface of patches in hectares
	 * @return the total surface of patches
	 */
	float totalSurface();
	
	/**
	 * the max surface of largest patch in hectares
	 * @return the maximum surface of largest patch
	 */
	float maxSurface();
	
	/**
	 * the number of distinct patches according to 8 neigbourhood
	 * @return the number distinct of patches of type v
	 */
	int nbPatches(int v);
	
	/**
	 * the total surface of patches in hectares
	 * @return the total surface of patches of type v
	 */
	float totalSurface(int v);
	
	/**
	 * the max surface of largest patch in hectares
	 * @return the maximum surface of largest patch of type v
	 */
	float maxSurface(int v);
	
}
