package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;
import java.util.Map.Entry;

public class TestCubist {

	public static void main(String[] args) {
		
		//CubistModel model = CubistModelFactory.create("G:/PREPARE/sylvain/Cubist_SP/rules_Dijon_Ephestia.csv");
		CubistModel model = CubistModelFactory.create("G:/PREPARE/sylvain/Cubist_SP/CUB_rules_Rennes_Graines.csv");
		
		System.out.println(model);
		
		Map<Integer, Map<String, Double>> datas = CubistModelFactory.getData("G:/PREPARE/sylvain/Cubist_SP/data_Dijon_Ephestia.csv");
		
		int index = 1, indexMin = 1, indexMax = 5;
		for(Entry<Integer, Map<String, Double>> data : datas.entrySet()){
			/*
			if(index>=indexMin && index<=indexMax){
				for(Entry<String, Double> e : data.getValue().entrySet()){
					System.out.print(e.getKey()+" "+e.getValue()+" , ");
				}
				//System.out.println();
				System.out.println(data.getKey()+" : "+model.predict(data.getValue()));
			}
			index++;
			*/
			System.out.println(model.predict(data.getValue()));
		}
		
	}

}
