package fr.inrae.act.bagap.chloe.window.counting;

public interface ValueCountingInterface {

	/**
	 * to get values of the map
	 * @return values of the map
	 */
	int[] values();
	
	/**
	 * to count value of type v
	 * @param v : type of value
	 * @return the count of v
	 */
	double countValue(int v);
	
	/**
	 * to count the classes
	 * @return the count of classes
	 */
	int countClass();
	
	
	/**
	 * the central value of the window
	 * only available for sliding et selected windows
	 * @return the central value
	 */
	float centralValue();
	
}
