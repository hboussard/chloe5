package fr.inra.sad.bagap.chloe.counting;

public interface CountingObserver {

	void init(Counting c);
	
	void prerun(Counting c);
	
	void postrun(Counting c);
	
	void close(Counting c);
	
}
