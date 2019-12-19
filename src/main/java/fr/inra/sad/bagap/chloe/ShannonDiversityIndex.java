package fr.inra.sad.bagap.chloe;

import fr.inra.sad.bagap.apiland.analysis.VariableManager;

public class ShannonDiversityIndex extends Metric {

	public ShannonDiversityIndex() {
		super(VariableManager.get("SHDI"));
	}

	@Override
	protected void doCalculate(Counting co) {
		if(co.countValues() > 0){
			value = 0;
			float p; 
			for(short v : co.values()){
				p = co.countValue(v) / co.validValues();
				if(p != 0){
					value += p*Math.log(p);
				}
			}
			if(value != 0){
				value *= -1;
			}
		}
	}

}
