package theater;

@SuppressWarnings({"checkstyle:WriteTag", "checkstyle:SuppressWarnings"})
public class UnknownPlayTypeException extends RuntimeException {

    public UnknownPlayTypeException(String message) {
        super(message);
    }
}
