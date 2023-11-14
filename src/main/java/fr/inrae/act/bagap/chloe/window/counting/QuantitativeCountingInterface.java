package fr.inrae.act.bagap.chloe.window.counting;

public interface QuantitativeCountingInterface {
	
	/**
	 * to get the average value
	 * @return the average of values
	 */
	float average();
	
	/**
	 * to get the sum
	 * @return the sum of values
	 */
	float sum();
	
	/**
	 * to get the squaresum
	 * @return the square sum of values
	 */
	float squareSum();
	
	/**
	 * to get the standard deviation
	 * @return the standard deviation of values
	 */
	float standardDeviation();
	
	/**
	 * to get the minimum
	 * @return the minimum of values
	 */
	float minimum();
	
	/**
	 * to get the maximum
	 * @return the maximum of values
	 */
	float maximum();

}
