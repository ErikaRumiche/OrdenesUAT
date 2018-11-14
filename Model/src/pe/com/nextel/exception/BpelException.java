package pe.com.nextel.exception;

public class BpelException extends RuntimeException {

	public BpelException() {
		super();
	}
	
	public BpelException(String strMessage) {
		super(strMessage);
	}
	
	public BpelException(String strCodError, String strErrMsg) {
		StringBuffer sbCadena = new StringBuffer();
		sbCadena.append("[CodError]: ").append(strCodError).append("\t").append("[ErrMsg]: ").append(strErrMsg);
		new BpelException(sbCadena.toString());
	}
	
	public BpelException(String strMessage, Throwable thwCause) {
		super(strMessage, thwCause);
	}

	
}