package fr.inrae.act.bagap.chloe.counting;

public interface QuantitativeCountingInterface {
	
	/**
	 * to get the average value
	 * @return the average of values
	 */
	double average();
	
	/**
	 * to get the sum
	 * @return the sum of values
	 */
	double sum();
	
	/**
	 * to get the standard deviation
	 * @return the standard deviation of values
	 */
	double standardDeviation();
	
	/**
	 * to get the minimum
	 * @return the minimum of values
	 */
	double minimum();
	
	/**
	 * to get the maximum
	 * @return the maximum of values
	 */
	double maximum();
}
