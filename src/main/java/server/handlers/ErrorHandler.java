package server.handlers;

import model.dto.ErrorMessage;
import org.eclipse.jetty.http.HttpStatus;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.transformer.JsonTransformer;
import server.validation.ValidationException;
import spark.ExceptionHandler;

public class ErrorHandler {

    private final static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    private final JsonTransformer json = new JsonTransformer();

    public ExceptionHandler<? super Exception> getErrorHandler() {
        return (e, request, response) -> {
            ErrorMessage error;
            if (e instanceof ValidationException) {
                response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);

                error = new ErrorMessage("Validation error", e.getMessage());
            } else if (e instanceof NoDataFoundException) {
                response.status(HttpStatus.NOT_FOUND_404);

                error = new ErrorMessage("Requested entity not found");
            } else if (e instanceof DataAccessException) {
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);

                error = new ErrorMessage("Data access error");
            } else {
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);

                error = new ErrorMessage("Internal server error");
            }

            logger.error(error.msg + " - " + e.getMessage());

            response.body(json.render(error));
        };
    }
}
