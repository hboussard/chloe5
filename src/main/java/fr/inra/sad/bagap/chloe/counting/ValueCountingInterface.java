package fr.inra.sad.bagap.chloe.counting;

public interface ValueCountingInterface {

	/**
	 * to get values of the map
	 * @return values of the map
	 */
	short[] values();
	
	/**
	 * to count value of type v
	 * @param v : type of value
	 * @return the count of v
	 */
	float countValue(short v);
	
	/**
	 * to count the classes
	 * @return the count of classes
	 */
	short countClass();
	
}
