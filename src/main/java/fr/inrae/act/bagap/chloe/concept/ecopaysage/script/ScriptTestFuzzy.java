package fr.inrae.act.bagap.chloe.concept.ecopaysage.script;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.apiland.raster.Coverage;
import fr.inrae.act.bagap.apiland.raster.CoverageManager;
import fr.inrae.act.bagap.apiland.raster.EnteteRaster;
import fr.inrae.act.bagap.apiland.util.SpatialCsvManager;
import fr.inrae.act.bagap.chloe.concept.ecopaysage.analyse.EcoPaysage;
import weka.core.Instance;
import weka.core.Instances;

public class ScriptTestFuzzy {

	private static String path = "C:/Data/temp/gradient/"; 
	
	private static Map<Integer, float[]> covs = new TreeMap<Integer, float[]>();
	
	private static EnteteRaster entete;
	
	public static void main(String[] args) {

		fuzzyExtremeIndividual();
		
	}
	
	private static void fuzzyExtremeIndividual() {
	
		//exportExtremeInfo(path+"extremes.csv", path+"info_ecopaysages_essai_5classes_500m.csv", path+"standardized_metrics_essai_500m.csv");
		
		//analyseGradientExtremes(path+"fuzzy_extreme.csv", path+"metrics_essai_XY.csv", path+"extremes.csv", path+"standardized_metrics_essai_500m.csv");
		
		/*
		EnteteRaster entete = EnteteRaster.read(path+"metrics_OCS_2021_ebr_5m_500m_header.txt");
		SpatialCsvManager.exportRaster(path+"fuzzy_extreme.csv", path+"fuzzy_extreme_ecop_1.tif", "ecop_"+1, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_extreme.csv", path+"fuzzy_extreme_ecop_2.tif", "ecop_"+2, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_extreme.csv", path+"fuzzy_extreme_ecop_3.tif", "ecop_"+3, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_extreme.csv", path+"fuzzy_extreme_ecop_4.tif", "ecop_"+4, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_extreme.csv", path+"fuzzy_extreme_ecop_5.tif", "ecop_"+5, entete);
		*/
		
		
		//exportMedianInfo(path+"medians.csv", path+"info_ecopaysages_essai_5classes_500m.csv", path+"standardized_metrics_essai_500m.csv");
		
		//analyseGradientMedians(path+"fuzzy_medians.csv", path+"metrics_essai_XY.csv", path+"medians.csv", path+"standardized_metrics_essai_500m.csv");
		/*
		EnteteRaster entete = EnteteRaster.read(path+"metrics_OCS_2021_ebr_5m_500m_header.txt");
		SpatialCsvManager.exportRaster(path+"fuzzy_medians.csv", path+"fuzzy_median_ecop_1.tif", "ecop_"+1, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_medians.csv", path+"fuzzy_median_ecop_2.tif", "ecop_"+2, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_medians.csv", path+"fuzzy_median_ecop_3.tif", "ecop_"+3, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_medians.csv", path+"fuzzy_median_ecop_4.tif", "ecop_"+4, entete);
		SpatialCsvManager.exportRaster(path+"fuzzy_medians.csv", path+"fuzzy_median_ecop_5.tif", "ecop_"+5, entete);
		*/
	}
	
	private static void exportMedianInfo(String medianFile, String kmeanFile, String normFile) {
		
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(medianFile));
			writer.write("classe");
			CsvReader crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			int num = crN.getHeaderCount();
			String[] metrics = new String[num];
			for(int ai=0; ai<num; ai++) {
				metrics[ai] = crN.getHeader(ai);
				writer.write(";"+metrics[ai]);
			}
			crN.close();
			writer.newLine();
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(kmeanFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int nbk = 0, k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
				nbk = Math.max(k, nbk);			
			}
			crI.close();
			
			

			Map<Integer, Set<float[]>> individuals = new TreeMap<Integer, Set<float[]>>();
			
			crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			while(crN.readRecord()) {
				float[] data = new float[num];
				for(int i=0; i<num; i++) {
					data[i] = Float.parseFloat(crN.get(i));
				}
				float minDistance = Float.MAX_VALUE;
				int classe = -1;
				for(int ki=1; ki<=nbk; ki++) {
					float[] kmean = kmeans.get(ki);
					float d = EcoPaysage.distance(kmean, data);
					if(d < minDistance) {
						minDistance = d;
						classe = ki;
					}
				}
				
				if(!individuals.containsKey(classe)) {
					individuals.put(classe, new HashSet<float[]>());
				}
				individuals.get(classe).add(data);
			}
			
			Map<Integer, float[]> kMedians = new TreeMap<Integer, float[]>();
			for(int ki=1; ki<=nbk; ki++) {
				float[]	median = getMedian(individuals.get(ki), num);
				kMedians.put(ki, median);
			}
			
			for(int ki=1; ki<=nbk; ki++) {
				writer.write(ki+"");
				
				for(float fi : kMedians.get(ki)) {
					writer.write(";"+fi);
				}
				writer.newLine();
			}											
			writer.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void analyseGradientMedians(String gradientCsv, String masqueFile, String medianFile, String normFile) {
		
		try {
			
			CsvWriter cw = new CsvWriter(gradientCsv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(medianFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				cw.write("ecop_"+crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
			}
			cw.endRecord();
			crI.close();
			
			CsvReader crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			
			CsvReader crXY = new CsvReader(masqueFile);
			crXY.setDelimiter(';');
			crXY.readHeaders();
			
			int index = 0;
			while(crN.readRecord() && crXY.readRecord()) {
				
				cw.write(crXY.get("X"));
				cw.write(crXY.get("Y"));
				float[] data = new float[crN.getColumnCount()];
				for(int i=0; i<crN.getColumnCount(); i++) {
					data[i] = Float.parseFloat(crN.get(i));
				}
				for(k=1; k<kmeans.size()+1; k++) {
					float d = EcoPaysage.distance(kmeans.get(k), data);
					cw.write(d+"");
				}
				cw.endRecord();
			}
			crN.close();
			crXY.close();
						
			cw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static float[] getMedian(Set<float[]> individuals, int num) {
		
		float[] medians = new float[num];
		
		for(int m=0; m<num; m++) {
			
			float[] data = new float[individuals.size()];
			
			int i=0;
			for(float[] ind : individuals) {
				
				data[i] = ind[m];
				
				i++;
			}
			
			Arrays.sort(data);
			float median;
			if (data.length % 2 == 0) {
			    median = (data[data.length/2] + data[data.length/2 - 1])/2;
			} else {
			    median = data[data.length/2];
			}
			
			medians[m] = median;
			
		}
		
		return medians;
	}
	
	public static void analyseGradientExtremes(String gradientCsv, String masqueFile, String extremeFile, String normFile) {
		
		try {
			
			CsvWriter cw = new CsvWriter(gradientCsv);
			cw.setDelimiter(';');
			cw.write("X");
			cw.write("Y");
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(extremeFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				cw.write("ecop_"+crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
			}
			cw.endRecord();
			crI.close();
			
			CsvReader crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			
			CsvReader crXY = new CsvReader(masqueFile);
			crXY.setDelimiter(';');
			crXY.readHeaders();
			
			int index = 0;
			while(crN.readRecord() && crXY.readRecord()) {
				
				cw.write(crXY.get("X"));
				cw.write(crXY.get("Y"));
				float[] data = new float[crN.getColumnCount()];
				for(int i=0; i<crN.getColumnCount(); i++) {
					data[i] = Float.parseFloat(crN.get(i));
				}
				for(k=1; k<kmeans.size()+1; k++) {
					float d = EcoPaysage.distance(kmeans.get(k), data);
					cw.write(d+"");
				}
				cw.endRecord();
			}
			crN.close();
			crXY.close();
						
			cw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void exportExtremeInfo(String extremeFile, String kmeanFile, String normFile) {
		
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(extremeFile));
			writer.write("classe");
			CsvReader crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			int num = crN.getHeaderCount();
			String[] metrics = new String[num];
			for(int ai=0; ai<num; ai++) {
				metrics[ai] = crN.getHeader(ai);
				writer.write(";"+metrics[ai]);
			}
			crN.close();
			writer.newLine();
			
			Map<Integer, float[]> kmeans = new TreeMap<Integer, float[]>();
			CsvReader crI = new CsvReader(kmeanFile);
			crI.setDelimiter(';');
			crI.readHeaders();
			int nbk = 0, k;
			while(crI.readRecord()) {
				k = Integer.parseInt(crI.get("classe"));
				float[] infos = new float[crI.getColumnCount()-1];
				for(int i=1; i<crI.getColumnCount(); i++) {
					infos[i-1] = Float.parseFloat(crI.get(i));
				}
				kmeans.put(k, infos);
				nbk = Math.max(k, nbk);			
			}
			crI.close();
			
			
			Map<Integer, float[]> kExtremes = new TreeMap<Integer, float[]>();
			float[] maxDistances = new float[nbk+1];
			
			crN = new CsvReader(normFile);
			crN.setDelimiter(';');
			crN.readHeaders();
			while(crN.readRecord()) {
				float[] data = new float[num];
				for(int i=0; i<num; i++) {
					data[i] = Float.parseFloat(crN.get(i));
				}
				float minDistance = Float.MAX_VALUE;
				int classe = -1;
				for(int ki=1; ki<=nbk; ki++) {
					float[] kmean = kmeans.get(ki);
					float d = EcoPaysage.distance(kmean, data);
					if(d < minDistance) {
						minDistance = d;
						classe = ki;
					}
				}
				
				minDistance = Float.MAX_VALUE;			
				for(int li=1; li<=nbk; li++) {
					if(classe != li) {
						float[] lmean = kmeans.get(li);
						float d = EcoPaysage.distance(lmean, data);
						minDistance = Math.min(d, minDistance);
					}
				}
				if(minDistance > maxDistances[classe]) {
					maxDistances[classe] = minDistance;
					kExtremes.put(classe, data);
				}
			}
			
			
			for(int ki=1; ki<=nbk; ki++) {
				writer.write(ki+"");
				
				for(float fi : kExtremes.get(ki)) {
					writer.write(";"+fi);
				}
				writer.newLine();
			}											
			writer.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void fuzzyPaul() {
		
		Coverage cov;
		for(int k=1; k<=5; k++) {
			cov = CoverageManager.getCoverage(path+"gradient_ecopaysages_essai_5classes_ecop"+k+"_500m.tif");
			entete = cov.getEntete();
			covs.put(k, cov.getData());
			cov.dispose();
		}

		for(int k=1; k<=5; k++) {
			generateFuzzyGradient(k);
		}
	}

	private static void generateFuzzyGradient(int k) {
		
		float[] data = new float[entete.width()*entete.height()];
		
		for(int ind=0; ind<data.length; ind++) {
			
			float gk = covs.get(k)[ind];
			
			float sum = 0;
			
			for(int l=1; l<=5; l++) {
				
				if(l != k) {
					
					float gl = covs.get(l)[ind];
					
					sum += Math.pow((gk/gl), 2);
				}
			}
			
			data[ind] = 1/sum;
		}
		
		CoverageManager.write(path+"fuzzy_"+k+".tif", data, entete);
	}

}
