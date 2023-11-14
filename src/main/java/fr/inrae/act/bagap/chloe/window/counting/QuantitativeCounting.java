package fr.inrae.act.bagap.chloe.window.counting;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {
	
	private float sum, squareSum, minimum, maximum;
	
	public QuantitativeCounting(float theoriticalSize) {
		super(theoriticalSize);
	}
	
	public QuantitativeCounting() {
		this(0);
	}
	
	/**
	 * partie specifique :
	 * 4 : somme des valeurs
	 * 5 : somme des carrés des valeurs
	 * 6 : minimum des valeurs
	 * 7 : maximum des valeurs
	 */
	@Override
	public void doSetCounts(float[] counts){
		sum = counts[4];
		squareSum = counts[5];
		minimum = counts[6];
		maximum = counts[7];
	}

	@Override
	public float average() {
		return (float) (sum / validValues());
	}

	@Override
	public float sum() {
		return sum;
	}
	
	@Override
	public float squareSum() {
		return squareSum;
	}
	
	@Override
	public float standardDeviation() {
		if(((squareSum / validValues()) - Math.pow(sum / validValues(), 2)) < 0){
			return 0;
		}
		return (float) Math.sqrt((squareSum / validValues()) - Math.pow(sum / validValues(), 2));
	}
	
	@Override
	public float minimum(){
		return minimum;
	}
	
	@Override
	public float maximum(){
		return maximum;
	}

}
