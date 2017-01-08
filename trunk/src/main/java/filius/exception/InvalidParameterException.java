package filius.exception;

@SuppressWarnings("serial")
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String msg) {
        super(msg);
    }

}
