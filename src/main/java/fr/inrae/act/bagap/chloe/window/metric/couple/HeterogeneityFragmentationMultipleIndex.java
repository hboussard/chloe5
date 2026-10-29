package fr.inrae.act.bagap.chloe.window.metric.couple;

import fr.inrae.act.bagap.chloe.util.Couple;
import fr.inrae.act.bagap.chloe.window.counting.Counting;
import fr.inrae.act.bagap.chloe.window.metric.Metric;

public class HeterogeneityFragmentationMultipleIndex extends Metric implements CoupleMetric {

	private short[][] v;
	
	public HeterogeneityFragmentationMultipleIndex(String name, String alias, short[]... v) {
		super(name, alias);
		this.v = v;
	}
	
	/*
	public HeterogeneityFragmentationMultipleIndex(short[]... v) {
		super();
		StringBuffer name = new StringBuffer("HETm-frag_");
		for(short[] vv : v){
			for(short vvv : vv){
				name.append(vvv);
				name.append('&');
			}
			name.deleteCharAt(name.length()-1);
			name.append(',');
		}
		name.deleteCharAt(name.length()-1);
		setName(name.toString());
		this.v = v;
	}
	*/

	@Override
	protected void doCalculate(Counting co) {
		if(co.countCouples() > 0){
			value = 0;
			double p; 
			double countC;
			
			for(int ig1=0; ig1<v.length; ig1++) {
				
				for(int ig2=ig1+1; ig2<v.length; ig2++) {
				
					countC = 0;
					
					for(int iv1=0; iv1<v[ig1].length; iv1++) {	
					
						for(int iv2=0; iv2<v[ig2].length; iv2++) {
							
							short v1 = v[ig1][iv1];
							short v2 = v[ig2][iv2];
							
							float c12 = Couple.getCouple(v1, v2);
							
							countC += co.countCouple(c12);
						}	
					}
					
					p = countC / co.validCouples();
					if(p != 0){
						value += p*Math.log(p);
					}
				}	
			}
			if(value != 0){
				value *= -1;
			}
			//System.out.println(value);
		}
	}

}
