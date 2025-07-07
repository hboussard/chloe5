package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;

public class CubistModelFactory {
	
	public static CubistModel create(String cubistFile){
		
		
		try {
			CsvReader cr = new CsvReader(cubistFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			Map<Integer, Map<Integer, CubistRule>> data = new TreeMap<Integer, Map<Integer, CubistRule>>();
			int committee, id;
			CubistCondition condition;
			CubistLinearModel linearModel;
			while(cr.readRecord()){
				committee = Integer.parseInt(cr.get("committee"));
				id = Integer.parseInt(cr.get("id"));
				condition = createCondition(cr.get("LHS"));
				linearModel = createLinearModel(cr.get("RHS"));
				if(!data.containsKey(committee)){
					data.put(committee, new TreeMap<Integer, CubistRule>());
				}
				data.get(committee).put(id, new CubistRule(condition, linearModel));
			}
			cr.close();
			
			CubistModel model = new CubistModel();
			
			for(Entry<Integer, Map<Integer, CubistRule>> entry : data.entrySet()){
				model.addCubistUnitModel(entry.getKey(), new CubistUnitModel(entry.getValue()));
			}	
			
			return model;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
	private static CubistCondition createCondition(String lhs) {
		//System.out.println(lhs);
		
		if(lhs.equalsIgnoreCase("NA")){
			return new CubistNACondition();
		}
		
		Set<CubistVarCondition> vconditions = new HashSet<CubistVarCondition>();
		String[] expression = lhs.replaceAll(" ", "").split("&");
		CubistVarCondition condition;
		String[] cond;
		for(String exp : expression){
			//System.out.println(exp);
			
			if(exp.contains("<=")){
				cond = exp.split("<=");
				condition = new CubistVarCondition(cond[0], "<=", Double.parseDouble(cond[1]));
			}else if(exp.contains(">=")){
				cond = exp.split(">=");
				condition = new CubistVarCondition(cond[0], ">=", Double.parseDouble(cond[1]));
			}else if(exp.contains("==")){
				cond = exp.split("==");
				if(cond[1].startsWith("'")){
					condition = new CubistCouvertCondition(cond[1].replaceAll("'", ""));
				}else{
					condition = new CubistVarCondition(cond[0], "==", Double.parseDouble(cond[1]));
				}
			}else if(exp.contains("<")){
				cond = exp.split("<");
				condition = new CubistVarCondition(cond[0], "<", Double.parseDouble(cond[1]));
			}else if(exp.contains(">")){
				cond = exp.split(">");
				condition = new CubistVarCondition(cond[0], ">", Double.parseDouble(cond[1]));
			}else if(exp.contains("%in%")){
				cond = exp.replaceAll("'", "").replace("Couvert%in%c(","").replace(")", "").split(",");
				condition = new CubistCouvertCondition(cond);
			}else{
				throw new IllegalArgumentException();
			}
			
			vconditions.add(condition);
		}
		
		return new CubistCondition(vconditions);
	}

	private static CubistLinearModel createLinearModel(String rhs){
		
		rhs = rhs.replace("(", "").replace(")", "").replace(" - ", " +- ");
		//System.out.println(rhs);
		String[] expressions = rhs.split(" \\+");
		
		Map<String, Double> locals = new HashMap<String, Double>();
		locals.put("intercept", 0.0);
		locals.put("Ps", 0.0);
		locals.put("Vm", 0.0);
		locals.put("Tx", 0.0);
		locals.put("An_Tn", 0.0);
		locals.put("An_Tm", 0.0);
		locals.put("An_Tx", 0.0);
		locals.put("An_Ps", 0.0);
		locals.put("An_Gel_succ", 0.0);
		locals.put("OSSnh_surf_1000m", 0.0);
		locals.put("OSSnh_lin_1000m", 0.0);
		locals.put("OS_cult_hote_1000m", 0.0);
		locals.put("Shannon_Cult_1000m", 0.0);
		locals.put("Shannon_Comb_1000m", 0.0);
		locals.put("nb_Cult_1000m", 0.0);
		locals.put("mean_parea_Culture_1000", 0.0);
		locals.put("int_cult_snh_1000m", 0.0);
		locals.put("OS_Bio2018_1000m", 0.0);
		locals.put("IFT_Paysager_2018_1000m", 0.0);
		locals.put("IFT_Paysager_2018_snh_inclus1000m", 0.0);
		locals.put("IFT_Xtot", 0.0);
		double value;
		String var;
		for(String expression : expressions){
			//System.out.println(expression);
			String[] exp = expression.replace(" ", "").split("\\*");
			if(exp.length == 1){
				// intercept
				value = Double.parseDouble(exp[0]);
				var = "intercept";
			}else{
				value = Double.parseDouble(exp[0]);
				var = exp[1];
			}
			locals.put(var, value);
		}
		
		return new CubistLinearModel(
				locals.get("intercept"),
				locals.get("Ps"),
				locals.get("Vm"),
				locals.get("Tx"),
				locals.get("An_Tn"),
				locals.get("An_Tm"),
				locals.get("An_Tx"),
				locals.get("An_Ps"),
				locals.get("An_Gel_succ"),
				locals.get("OSSnh_surf_1000m"),
				locals.get("OSSnh_lin_1000m"),
				locals.get("OS_cult_hote_1000m"),
				locals.get("Shannon_Cult_1000m"),
				locals.get("Shannon_Comb_1000m"),
				locals.get("nb_Cult_1000m"),
				locals.get("mean_parea_Culture_1000"),
				locals.get("int_cult_snh_1000m"),
				locals.get("OS_Bio2018_1000m"),
				locals.get("IFT_Paysager_2018_1000m"),
				locals.get("IFT_Paysager_2018_snh_inclus1000m"),
				locals.get("IFT_Xtot")
				);
	}
	
	public static Map<Integer, Map<String, Double>> getData(String dataFile){
		
		Map<Integer, Map<String, Double>> datas = new TreeMap<Integer, Map<String, Double>>();
		
		try {
			CsvReader cr = new CsvReader(dataFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			int index = 1;
			Map<String, Double> data;
			while(cr.readRecord()){
				data = new HashMap<String, Double>();
				//data.put(key, value)
				data.put("Ps", convert(Double.parseDouble(cr.get("Ps"))));
				data.put("Vm", convert(Double.parseDouble(cr.get("Vm"))));
				data.put("Tx", convert(Double.parseDouble(cr.get("Tx"))));
				data.put("An_Tn", convert(Double.parseDouble(cr.get("An_Tn"))));
				data.put("An_Tm", convert(Double.parseDouble(cr.get("An_Tm"))));
				data.put("An_Tx", convert(Double.parseDouble(cr.get("An_Tx"))));
				data.put("An_Ps", convert(Double.parseDouble(cr.get("An_Ps"))));
				data.put("An_Gel_succ", convert(Double.parseDouble(cr.get("An_Gel_succ"))));
				data.put("OSSnh_surf_1000m", convert(Double.parseDouble(cr.get("OSSnh_surf_1000m"))));
				data.put("OSSnh_lin_1000m", convert(Double.parseDouble(cr.get("OSSnh_lin_1000m"))));
				data.put("OS_cult_hote_1000m", convert(Double.parseDouble(cr.get("OS_cult_hote_1000m"))));
				data.put("Shannon_Cult_1000m", convert(Double.parseDouble(cr.get("Shannon_Cult_1000m"))));
				data.put("Shannon_Comb_1000m", convert(Double.parseDouble(cr.get("Shannon_Comb_1000m"))));
				data.put("nb_Cult_1000m", convert(Double.parseDouble(cr.get("nb_Cult_1000m"))));
				data.put("mean_parea_Culture_1000", convert(Double.parseDouble(cr.get("mean_parea_Culture_1000"))));
				data.put("int_cult_snh_1000m", convert(Double.parseDouble(cr.get("int_cult_snh_1000m"))));
				data.put("OS_Bio2018_1000m", convert(Double.parseDouble(cr.get("OS_Bio2018_1000m"))));
				data.put("IFT_Paysager_2018_1000m", convert(Double.parseDouble(cr.get("IFT_Paysager_2018_1000m"))));
				data.put("IFT_Paysager_2018_snh_inclus1000m", convert(Double.parseDouble(cr.get("IFT_Paysager_2018_snh_inclus1000m"))));
				
				String sIFT_Xtot = cr.get("IFT_Xtot");
				if(sIFT_Xtot.equalsIgnoreCase("NA")){
					data.put("IFT_Xtot", 4.3528);
				}else{
					data.put("IFT_Xtot", convert(Double.parseDouble(sIFT_Xtot)));
				}
				
				data.put("Couvert", CubistModel.couvertMap.get(cr.get("Couvert")));
				
				datas.put(index++, data);
			}
			cr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return datas;
	}
	
	private static double convert(double v){
		return new Double(v*1000000).intValue()/1000000.0;
		//return v;
	}
	
}

