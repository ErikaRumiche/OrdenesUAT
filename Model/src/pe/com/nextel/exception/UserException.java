package pe.com.nextel.exception;

public class UserException extends RuntimeException {
  public UserException(){
    super();
  }

  public UserException(String strMessage) {
		super(strMessage);
	}
	
	public UserException(String strMessage, Throwable thwCause) {
		super(strMessage, thwCause);
	}
}