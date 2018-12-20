package newbienewsletter.exceptions;

public class DetailsServiceException extends RuntimeException {

    public DetailsServiceException(String message) {
        super(message);
    }

    public DetailsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
