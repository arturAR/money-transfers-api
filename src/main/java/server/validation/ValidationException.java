package server.validation;

public class ValidationException extends RuntimeException {

    ValidationException(String message) {
        super(message);
    }
}
