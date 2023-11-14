package fr.inrae.act.bagap.chloe.window.counting;

public interface CoupleCountingInterface {
	
	/**
	 * the theoretical size of the window if there were no border
	 * @return the theoretical size
	 */
	int theoreticalCoupleSize();
	
	/**
	 * the total count of couples
	 * @return the total count of couples
	 */
	float totalCouples();
	
	/**
	 * the total count of valid couples
	 * a couple value is different to Raster.noDataValue()
	 * @return the total count of valid couples
	 */
	float validCouples();
	
	/**
	 * the count of couples
	 * a couple is different to 0 and to Raster.noDataValue()
	 * @return the count of couples
	 */
	float countCouples();
	
	/**
	 * the count of couples
	 * a couple is different to 0 and to Raster.noDataValue()
	 * when both values are identical
	 * @return the count of couples
	 */
	float countHomogeneousCouples();
	
	/**
	 * the count of couples
	 * a couple is different to 0 and to Raster.noDataValue()
	 * when values are different
	 * @return the count of couples
	 */
	float countHeterogenousCouples();
	
	/**
	 * to get couples of the map
	 * @return couples of the map
	 */
	float[] couples();
	
	/**
	 * to count couple of type c
	 * @param c : type of couple
	 * @return the count of c
	 */
	float countCouple(float c);
	
	/**
	 * to count couple of type v1/v2
	 * @param v1 : value 1
	 * @param v2 : value 2
	 * @return the count of v1/v2
	 */
	float countCouple(short v1, short v2);
	
	/**
	 * to count the classes
	 * @return the count of classes
	 */
	short countCoupleClass();

}
