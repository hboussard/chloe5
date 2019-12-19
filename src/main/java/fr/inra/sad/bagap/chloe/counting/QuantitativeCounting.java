package fr.inra.sad.bagap.chloe.counting;

public class QuantitativeCounting extends Counting implements QuantitativeCountingInterface {

	private final int theoriticalSize;
	
	private float totalValues;
	
	private float validValues;
	
	private double sum;
	
	public QuantitativeCounting(int theoriticalSize) {
		this.theoriticalSize = theoriticalSize;
	}
	
	public void setCounts(double[] counts){
		totalValues = 0;
		validValues = 0;
		sum = 0;
		
		totalValues += counts[0];
		
		totalValues += counts[1];
		validValues += counts[1];
		
		sum = counts[2];
	}
	
	public int theoreticalSize() {
		return theoriticalSize;
	}

	public float totalValues() {
		return totalValues;
	}

	public float validValues() {
		return validValues;
	}

	public float average() {
		return (float) (sum / validValues);
	}

	public double sum() {
		return sum;
	}

}
