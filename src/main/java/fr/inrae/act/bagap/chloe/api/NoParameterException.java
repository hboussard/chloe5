package fr.inrae.act.bagap.chloe.api;

public class NoParameterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoParameterException(String parameter){
		super(parameter);
	}
	
}
