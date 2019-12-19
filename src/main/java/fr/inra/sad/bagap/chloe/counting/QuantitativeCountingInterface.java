package fr.inra.sad.bagap.chloe.counting;

public interface QuantitativeCountingInterface {

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
	 * to get the average value
	 * @return the average of values
	 */
	float average();
	
	/**
	 * to get the sum
	 * @return the sum of values
	 */
	double sum();
}
