package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class SimpsonDiversityMultipleIndex extends Metric implements ValueMetric {

	private short[][] v;
	
	public SimpsonDiversityMultipleIndex(String alias, short[]... v) {
		this(v);
		setAlias(alias);
	}
	
	public SimpsonDiversityMultipleIndex(short[]... v) {
		super();
		StringBuffer name = new StringBuffer("SIDIm_");
		for(short[] vv : v){
			for(short vvv : vv){
				name.append(vvv);
				name.append('&');
			}
			name.deleteCharAt(name.length()-1);
			name.append('-');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}

	@Override
	protected void doCalculate(Counting co) {
		
		if(co.countValues() > 0){
			value = 0;
			double p;
			
			double countV;
			for(short[] vv : v) {
				
				countV = 0;
				
				for(short vvv : vv) {
					
					countV += co.countValue(vvv);
				}

				p = countV / co.validValues();
				value += p*p;
			}
			
			value = 1 - value;
		}
	}

}
