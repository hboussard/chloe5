package fr.inrae.act.bagap.chloe.concept.erosion.analyse;

public enum ErosionEventType {

	SOFT(5), MEDIUM(10), HARD(15);
	
	private final int waterQuantity;

	private ErosionEventType(int waterQuantity) {
		this.waterQuantity = waterQuantity;
	}

	public int getWaterQuantity() {
		return this.waterQuantity;
	}
	
}