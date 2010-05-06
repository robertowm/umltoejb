package core.exception;

public class XEOSException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XEOSException(String exception){
		super(exception);
	}
	
	public XEOSException(String exception, Throwable e){
		super(exception, e);
	}
	
	public XEOSException(Throwable e){
		super(e);
	}

}
