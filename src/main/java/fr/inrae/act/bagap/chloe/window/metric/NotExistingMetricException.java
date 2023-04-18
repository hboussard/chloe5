package fr.inrae.act.bagap.chloe.window.metric;

public class NotExistingMetricException extends RuntimeException {
  
    /**
     * Constructs an <code>IllegalArgumentException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public NotExistingMetricException(String s) {
        super(s);
    }

	private static final long serialVersionUID = 1L;

}
