package fr.inrae.act.bagap.chloe.window.counting;

public interface ValueCountingInterface {

	/**
	 * to get values of the map
	 * @return values of the map
	 */
	int[] values();
	
	/**
	 * the count of values
	 * a value is different to 0 and to Raster.noDataValue()
	 * @return the count of values
	 */
	float countValues();
	
	/**
	 * to count value of type v
	 * @param v : type of value
	 * @return the count of v
	 */
	float countValue(int v);
	
	/**
	 * to count the classes
	 * @return the count of classes
	 */
	int countClass();
	
}
