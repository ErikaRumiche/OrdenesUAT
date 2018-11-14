package pe.com.nextel.exception;

public class SessionException extends RuntimeException {

	public SessionException() {
		super();
	}
	
	public SessionException(String strMessage) {
		super(strMessage);
	}
	
	public SessionException(String strMessage, Throwable thwCause) {
		super(strMessage, thwCause);
	}

	
}