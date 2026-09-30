package fr.inrae.act.bagap.chloe.window.metric.value;

import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class ShannonDiversityMultipleIndex extends Metric implements ValueMetric {
	
	private short[][] v;
	
	public ShannonDiversityMultipleIndex(String alias, short[]... v) {
		this(v);
		setAlias(alias);
	}
	
	public ShannonDiversityMultipleIndex(short[]... v) {
		super();
		StringBuffer name = new StringBuffer("SHDIm_");
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
		if(co.validValues() > 0){
			value = 0;
			double p; 
			double countV;
			for(short[] vv : v) {
				
				countV = 0;
				
				for(short vvv : vv) {
					
					countV += co.countValue(vvv);
				}
				
				p = countV / co.validValues();
				if(p != 0){
					if(p > 1){
						p = 1;
					}
					value += p*Math.log(p);
				}
			}
			
			if(value != 0){
				value *= -1;
			}
			//System.out.println(value);
		}
	}

}
