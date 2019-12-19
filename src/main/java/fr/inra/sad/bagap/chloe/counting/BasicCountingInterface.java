package fr.inra.sad.bagap.chloe.counting;

public interface BasicCountingInterface {

	/**
	 * the theoretical size of the window if there were no border
	 * @return the theoretical size
	 */
	int theoreticalSize();
	
	/**
	 * the total count of values
	 * @return the total count of values
	 */
	float totalValues();
	
	/**
	 * the total count of valid values
	 * a valid value is different to Raster.noDataValue()
	 * @return the total count of valid values
	 */
	float validValues();
	
	/**
	 * the count of values
	 * a value is different to 0 and to Raster.noDataValue()
	 * @return the count of values
	 */
	float countValues();
	
}
