package fr.inrae.act.bagap.chloe.concept.regulation.toulouse.cubist;

import java.util.Map;

public class CubistLinearModel {

	private double intercept, Ps, Vm, Tx, An_Tn, An_Tm, An_Tx, An_Ps, An_Gel_succ, OSSnh_surf_1000m, OSSnh_lin_1000m, OS_cult_hote_1000m, Shannon_Cult_1000m, Shannon_Comb_1000m, nb_Cult_1000m, mean_parea_Culture_1000, int_cult_snh_1000m, OS_Bio2018_1000m, IFT_Paysager_2018_1000m, IFT_Paysager_2018_snh_inclus1000m, IFT_Xtot;
	
	public CubistLinearModel(double intercept, double Ps, double Vm, double Tx, double An_Tn, double An_Tm, double An_Tx, double An_Ps, double An_Gel_succ, 
			double OSSnh_surf_1000m, double OSSnh_lin_1000m, double OS_cult_hote_1000m, double Shannon_Cult_1000m, double Shannon_Comb_1000m, 
			double nb_Cult_1000m, double mean_parea_Culture_1000, double int_cult_snh_1000m, double OS_Bio2018_1000m, double IFT_Paysager_2018_1000m, 
			double IFT_Paysager_2018_snh_inclus1000m, double IFT_Xtot){
		this.intercept = intercept;
		this.Ps = Ps;
		this.Vm = Vm;
		this.Tx = Tx;
		this.An_Tn = An_Tn;
		this.An_Tm = An_Tm;
		this.An_Tx = An_Tx;
		this.An_Ps = An_Ps;
		this.An_Gel_succ = An_Gel_succ;
		this.OSSnh_surf_1000m = OSSnh_surf_1000m;
		this.OSSnh_lin_1000m = OSSnh_lin_1000m;
		this.OS_cult_hote_1000m = OS_cult_hote_1000m;
		this.Shannon_Cult_1000m = Shannon_Cult_1000m;
		this.Shannon_Comb_1000m = Shannon_Comb_1000m;
		this.nb_Cult_1000m = nb_Cult_1000m;
		this.mean_parea_Culture_1000 = mean_parea_Culture_1000;
		this.int_cult_snh_1000m = int_cult_snh_1000m;
		this.OS_Bio2018_1000m = OS_Bio2018_1000m;
		this.IFT_Paysager_2018_1000m = IFT_Paysager_2018_1000m;
		this.IFT_Paysager_2018_snh_inclus1000m = IFT_Paysager_2018_snh_inclus1000m;
		this.IFT_Xtot = IFT_Xtot;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		if(intercept != 0){
			sb.append(intercept);
		}
		if(Ps != 0){
			if(Ps > 0){
				sb.append("+");
			}
			sb.append(Ps+"*Ps");
		}
		if(Vm != 0){
			if(Vm > 0){
				sb.append("+");
			}
			sb.append(Vm+"*Vm");
		}
		if(Tx != 0){
			if(Tx > 0){
				sb.append("+");
			}
			sb.append(Tx+"*Tx");
		}
		if(An_Tn != 0){
			if(An_Tn > 0){
				sb.append("+");
			}
			sb.append(An_Tn+"*An_Tn");
		}
		if(An_Tm != 0){
			if(An_Tm > 0){
				sb.append("+");
			}
			sb.append(An_Tm+"*An_Tm");
		}
		if(An_Tx != 0){
			if(An_Tx > 0){
				sb.append("+");
			}
			sb.append(An_Tx+"*An_Tx");
		}
		if(An_Ps != 0){
			if(An_Ps > 0){
				sb.append("+");
			}
			sb.append(An_Ps+"*An_Ps");
		}
		if(An_Gel_succ != 0){
			if(An_Gel_succ > 0){
				sb.append("+");
			}
			sb.append(An_Gel_succ+"*An_Gel_succ");
		}
		if(OSSnh_surf_1000m != 0){
			if(OSSnh_surf_1000m > 0){
				sb.append("+");
			}
			sb.append(OSSnh_surf_1000m+"*OSSnh_surf_1000m");
		}
		if(OSSnh_lin_1000m != 0){
			if(OSSnh_lin_1000m > 0){
				sb.append("+");
			}
			sb.append(OSSnh_lin_1000m+"*OSSnh_lin_1000m");
		}
		if(OS_cult_hote_1000m != 0){
			if(OS_cult_hote_1000m > 0){
				sb.append("+");
			}
			sb.append(OS_cult_hote_1000m+"*OS_cult_hote_1000m");
		}
		if(Shannon_Cult_1000m != 0){
			if(Shannon_Cult_1000m > 0){
				sb.append("+");
			}
			sb.append(Shannon_Cult_1000m+"*Shannon_Cult_1000m");
		}
		if(Shannon_Comb_1000m != 0){
			if(Shannon_Comb_1000m > 0){
				sb.append("+");
			}
			sb.append(Shannon_Comb_1000m+"*Shannon_Comb_1000m");
		}
		if(nb_Cult_1000m != 0){
			if(nb_Cult_1000m > 0){
				sb.append("+");
			}
			sb.append(nb_Cult_1000m+"*nb_Cult_1000m");
		}
		if(mean_parea_Culture_1000 != 0){
			if(mean_parea_Culture_1000 > 0){
				sb.append("+");
			}
			sb.append(mean_parea_Culture_1000+"*mean_parea_Culture_1000");
		}
		if(int_cult_snh_1000m != 0){
			if(int_cult_snh_1000m > 0){
				sb.append("+");
			}
			sb.append(int_cult_snh_1000m+"*int_cult_snh_1000m");
		}
		if(OS_Bio2018_1000m != 0){
			if(OS_Bio2018_1000m > 0){
				sb.append("+");
			}
			sb.append(OS_Bio2018_1000m+"*OS_Bio2018_1000m");
		}
		if(IFT_Paysager_2018_1000m != 0){
			if(IFT_Paysager_2018_1000m > 0){
				sb.append("+");
			}
			sb.append(IFT_Paysager_2018_1000m+"*IFT_Paysager_2018_1000m");
		}
		if(IFT_Paysager_2018_snh_inclus1000m != 0){
			if(IFT_Paysager_2018_snh_inclus1000m > 0){
				sb.append("+");
			}
			sb.append(IFT_Paysager_2018_snh_inclus1000m+"*IFT_Paysager_2018_snh_inclus1000m");
		}
		if(IFT_Xtot != 0){
			if(IFT_Xtot > 0){
				sb.append("+");
			}
			sb.append(IFT_Xtot+"*IFT_Xtot");
		}
		
		return sb.toString();
	}

	public double evaluate(double Ps, double Vm, double Tx, double An_Tn, double An_Tm, double An_Tx, double An_Ps, double An_Gel_succ, 
			double OSSnh_surf_1000m, double OSSnh_lin_1000m, double OS_cult_hote_1000m, double Shannon_Cult_1000m, double Shannon_Comb_1000m, 
			double nb_Cult_1000m, double mean_parea_Culture_1000, double int_cult_snh_1000m, double OS_Bio2018_1000m, double IFT_Paysager_2018_1000m, 
			double IFT_Paysager_2018_snh_inclus1000m, double IFT_Xtot){
		return intercept 
				+ this.Ps*Ps 
				+ this.Vm*Vm 
				+ this.Tx*Tx 
				+ this.An_Tn*An_Tn 
				+ this.An_Tm*An_Tm 
				+ this.An_Tx*An_Tx 
				+ this.An_Ps*An_Ps 
				+ this.An_Gel_succ*An_Gel_succ 
				+ this.OSSnh_surf_1000m*OSSnh_surf_1000m 
				+ this.OSSnh_lin_1000m*OSSnh_lin_1000m 
				+ this.OS_cult_hote_1000m*OS_cult_hote_1000m 
				+ this.Shannon_Cult_1000m*Shannon_Cult_1000m 
				+ this.Shannon_Comb_1000m*Shannon_Comb_1000m 
				+ this.nb_Cult_1000m*nb_Cult_1000m 
				+ this.mean_parea_Culture_1000*mean_parea_Culture_1000 
				+ this.int_cult_snh_1000m*int_cult_snh_1000m 
				+ this.OS_Bio2018_1000m*OS_Bio2018_1000m 
				+ this.IFT_Paysager_2018_1000m*IFT_Paysager_2018_1000m 
				+ this.IFT_Paysager_2018_snh_inclus1000m*IFT_Paysager_2018_snh_inclus1000m
				+ this.IFT_Xtot*IFT_Xtot;
	}
	
	public double evaluate(double[] data){
		return intercept 
				+ this.Ps*data[0] 
				+ this.Vm*data[1] 
				+ this.Tx*data[2] 
				+ this.An_Tn*data[3] 
				+ this.An_Tm*data[4] 
				+ this.An_Tx*data[5] 
				+ this.An_Ps*data[6]
				+ this.An_Gel_succ*data[7]
				+ this.OSSnh_surf_1000m*data[8] 
				+ this.OSSnh_lin_1000m*data[9] 
				+ this.OS_cult_hote_1000m*data[10] 
				+ this.Shannon_Cult_1000m*data[11] 
				+ this.Shannon_Comb_1000m*data[12] 
				+ this.nb_Cult_1000m*data[13] 
				+ this.mean_parea_Culture_1000*data[14] 
				+ this.int_cult_snh_1000m*data[15] 
				+ this.OS_Bio2018_1000m*data[16] 
				+ this.IFT_Paysager_2018_1000m*data[17] 
				+ this.IFT_Paysager_2018_snh_inclus1000m*data[18]
				+ this.IFT_Xtot*data[19];
	}
	
	public double evaluate(Map<String, Double> data){
		return intercept 
				+ this.Ps*data.get("Ps") 
				+ this.Vm*data.get("Vm") 
				+ this.Tx*data.get("Tx")
				+ this.An_Tn*data.get("An_Tn")
				+ this.An_Tm*data.get("An_Tm")
				+ this.An_Tx*data.get("An_Tx")
				+ this.An_Ps*data.get("An_Ps")
				+ this.An_Gel_succ*data.get("An_Gel_succ")
				+ this.OSSnh_surf_1000m*data.get("OSSnh_surf_1000m")
				+ this.OSSnh_lin_1000m*data.get("OSSnh_lin_1000m")
				+ this.OS_cult_hote_1000m*data.get("OS_cult_hote_1000m")
				+ this.Shannon_Cult_1000m*data.get("Shannon_Cult_1000m")
				+ this.Shannon_Comb_1000m*data.get("Shannon_Comb_1000m")
				+ this.nb_Cult_1000m*data.get("nb_Cult_1000m")
				+ this.mean_parea_Culture_1000*data.get("mean_parea_Culture_1000")
				+ this.int_cult_snh_1000m*data.get("int_cult_snh_1000m")
				+ this.OS_Bio2018_1000m*data.get("OS_Bio2018_1000m")
				+ this.IFT_Paysager_2018_1000m*data.get("IFT_Paysager_2018_1000m")
				+ this.IFT_Paysager_2018_snh_inclus1000m*data.get("IFT_Paysager_2018_snh_inclus1000m")
				+ this.IFT_Xtot*data.get("IFT_Xtot");
	}
	
}
