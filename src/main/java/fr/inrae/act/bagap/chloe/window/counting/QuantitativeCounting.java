package fr.inrae.act.bagap.chloe.window.counting;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {
	
	private double sum, squareSum, minimum, maximum;
	
	public QuantitativeCounting(double resolution, double theoriticalSize) {
		super(resolution, theoriticalSize);
	}
	
	public QuantitativeCounting(double theoriticalSize) {
		this(1, theoriticalSize);
	}
	
	/**
	 * partie specifique :
	 * 4 : somme des valeurs
	 * 5 : somme des carres des valeurs
	 * 6 : minimum des valeurs
	 * 7 : maximum des valeurs
	 */
	@Override
	public void doSetCounts(double[] counts){
		
		sum = counts[4];
		squareSum = counts[5];
		minimum = counts[6];
		maximum = counts[7];
		
	}

	@Override
	public double average() {
		return (float) (sum / validValues());
	}

	@Override
	public double sum() {
		return sum;
	}
	
	@Override
	public double squareSum() {
		return squareSum;
	}
	
	@Override
	public double standardDeviation() {
		if(((squareSum / validValues()) - Math.pow(sum / validValues(), 2)) < 0){
			return 0;
		}
		return (float) Math.sqrt((squareSum / validValues()) - Math.pow(sum / validValues(), 2));
	}
	
	@Override
	public double minimum(){
		return minimum;
	}
	
	@Override
	public double maximum(){
		return maximum;
	}

}
