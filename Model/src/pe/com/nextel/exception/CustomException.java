package pe.com.nextel.exception;

public class CustomException extends Exception {
    public CustomException() {
        super();
    }
    
    public CustomException(String strMessage) {
                  super(strMessage);
          }
          
    public CustomException(String strMessage, Throwable thwCause) {
          super(strMessage, thwCause);
    }
}
