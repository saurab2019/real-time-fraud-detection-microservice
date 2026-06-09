package com.saurabh.frauddetection.exception;

import com.saurabh.frauddetection.logging.IRequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import com.saurabh.frauddetection.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final IRequestLogger requestLogger;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex)
    {
        FieldError fieldError =  ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();

        requestLogger.error(errorMessage, ex);
        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorResponse(
                                ErrorCode.VALIDATION_ERROR,
                                errorMessage,
                                LocalDateTime.now()
                        )
                );


    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFound(TransactionNotFoundException ex)
    {
        requestLogger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                ErrorCode.TRANSACTION_NOT_FOUND,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(FraudException.class)
    public ResponseEntity<ErrorResponse> handleFraud(FraudException ex)
    {
        requestLogger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                ErrorCode.BUSINESS_ERROR,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            DataIntegrityViolationException ex) {

        requestLogger.error("Database constraint violation", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ErrorResponse(
                                ErrorCode.DATA_ERROR,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException ex)
    {
        requestLogger.error("Database operation failed", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                ErrorCode.INTERNAL_ERROR,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex)
    {
        requestLogger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                ErrorCode.INTERNAL_ERROR,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

}
