package vn.edu.hcmaf.apigamestore.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.ErrorResponse;

import java.util.List;
import java.util.NoSuchElementException;
/** * Global exception handler for the API Game Store.
 * This class handles various exceptions that may occur during the execution of the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles validation exceptions that occur when method arguments are not valid.
     *
     * @param ex the MethodArgumentNotValidException
     * @return a ResponseEntity containing an ErrorResponse with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err ->
                        err.getField() + ": " + err.getDefaultMessage()
                )
                .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(
                "400", "Validation error",
                errors.toString()
        ));
    }
    /**
     * Handles DataAccessException, which is thrown when there is an issue with database access.
     *
     * @param ex the DataAccessException
     * @return a ResponseEntity containing an ErrorResponse with database error details
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "500",
                        "Database error",
                        ex.getMessage()
                ));
    }

    /**
     * Handles AuthenticationException, which is thrown when there is an authentication failure.
     *
     * @param ex the AuthenticationException
     * @return a ResponseEntity containing an ErrorResponse with authentication error details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        "401",
                        "Unauthorized",
                        ex.getMessage()
                ));
    }
    /**
     * Handles NoSuchElementException, which is thrown when an element is not found.
     *
     * @param ex the NoSuchElementException
     * @return a ResponseEntity containing an ErrorResponse with not found error details
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("404", "Not found", ex.getMessage()));
    }

    /**
     * Handles IllegalArgumentException, which is thrown when an illegal argument is passed.
     *
     * @param ex the IllegalArgumentException
     * @return a ResponseEntity containing an ErrorResponse with illegal argument error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                "400",
                "IllegalArgument",
                ex.getMessage()
        ));
    }

    /**
     * Handles general exceptions that are not specifically handled by other methods.
     *
     * @param ex the Exception
     * @return a ResponseEntity containing an ErrorResponse with internal server error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "500",
                "Internal Server Error",
                 ex.getMessage()
        ));
    }

}