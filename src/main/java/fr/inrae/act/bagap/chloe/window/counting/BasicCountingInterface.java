package fr.inrae.act.bagap.chloe.window.counting;

public interface BasicCountingInterface {

	/**
	 * the counting is valid when the central pixel is filtered
	 * make sense for SLIDING only, in other cases, it's always valid
	 * note : the window may not be computed according to the "minimum valid value" parameter
	 * @return the counting is valid
	 */
	boolean validCounting();
	
	/**
	 * the theoretical size of the window if there were no border
	 * @return the theoretical size
	 */
	double theoreticalSize();
	
	/**
	 * the total count of values
	 * @return the total count of values
	 */
	double totalValues();
	
	/**
	 * the total count of valid values
	 * a valid value is different to Raster.noDataValue()
	 * @return the total count of valid values
	 */
	double validValues();
	
	/**
	 * the central value of the window
	 * only available for sliding et selected windows
	 * @return the central value
	 */
	double centralValue();

}
