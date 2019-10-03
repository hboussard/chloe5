package fr.inra.sad.bagap.aparapi;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class Test {

	public static void main(String[] args) {
		System.out.println("instantiation");
		/*
		int[] inA = new int[999999999];
		int[] inB = new int[999999999];
		int[] result = new int[999999999];
		*/
		int size = 99999999;
		int[] inA = new int[size];
		int[] inB = new int[size];
		int[] result = new int[size];
		System.out.println("fin instantiation");
		
		System.out.println("initialisation");
		for(int i=0; i<inA.length; i++){
			inA[i] = 1;
			inB[i] = 2;
		}
		System.out.println("fin initialisation");
		
		System.out.println("début test classique");
		testClassic(inA, inB, result);
		System.out.println("fin test classique");
		System.out.println("début test classique");
		testClassic(inA, inB, result);
		System.out.println("fin test classique");
		
		Kernel kernel = new Kernel() {
			@Override
			public void run() {
				int i = getGlobalId();
				result[i] = inA[i] + inB[i];
			}
		};
		Range range = Range.create(size);
		
		System.out.println("début test aparapi");
		testAparapi(kernel, range, inA, inB, result);
		System.out.println("fin test aparapi");
		System.out.println("début test aparapi");
		testAparapi(kernel, range, inA, inB, result);
		System.out.println("fin test aparapi");
		System.out.println("début test aparapi");
		testAparapi(kernel, range, inA, inB, result);
		System.out.println("fin test aparapi");
		
	}

	private static void testClassic(int[] inA, int[] inB, int[] result){
		for (int i = 0; i < result.length; i++) {
		    result[i] = inA[i] + inB[i];
		}
	}
	
	private static void testAparapi(Kernel kernel, Range range, int[] inA, int[] inB, int[] result){
		kernel.execute(range);
		kernel.dispose();
	}
	
	

	
}
