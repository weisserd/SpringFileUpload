package newbienewsletter.exceptions;

public class PatternNotFoundException extends DetailsServiceException {

    public PatternNotFoundException() {
        super("Pattern for Regex not found.");
    }

    public PatternNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
