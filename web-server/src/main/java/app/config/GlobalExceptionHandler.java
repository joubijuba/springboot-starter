package app.config;

import org.springframework.http.HttpEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.exceptions.*;
import app.utils.HttpWrapper;
import core.utils.ResponseWrapper;
import core.dtos.WrappedResponseDto;

/**
 * This controller advice is handling all errors thrown in the application that aren't caugth.
 * Returns relevant HTTP response relative to the error (MethodArgumentNotValidException <=> BAD_REQUEST)
 */
@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Catch les erreurs de ce type
    public HttpEntity<WrappedResponseDto<?>> handleValidationException() {
        return HttpWrapper.fromWrToRes(ResponseWrapper.BAD_REQUEST("Incorrect inputs"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public HttpEntity<WrappedResponseDto<?>> handleMismatchException() {
        return HttpWrapper.fromWrToRes(ResponseWrapper.BAD_REQUEST("Incorrect path parameters"));
    }

    @ExceptionHandler(NotFoundException.class)
    public HttpEntity<WrappedResponseDto<?>> handleNotFoundException(NotFoundException ex) {
        return HttpWrapper.fromWrToRes(ResponseWrapper.NOT_FOUND(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public HttpEntity<WrappedResponseDto<?>> handleUnauthorizedException(UnauthorizedException ex) {
        return HttpWrapper.fromWrToRes(ResponseWrapper.UNAUTHORIZED(ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public HttpEntity<WrappedResponseDto<?>> handleConflictException(ConflictException ex) {
        return HttpWrapper.fromWrToRes(ResponseWrapper.CONFLICT(ex.getMessage()));
    }

    @ExceptionHandler(InternalServerError.class)
    public HttpEntity<WrappedResponseDto<?>> handleInternalServerError(InternalServerError ex) {
        return HttpWrapper.fromWrToRes(ResponseWrapper.ERROR(ex.getMessage()));
    }

    /**
     * Safety net for uncaught errors 
     * @param ex exception
     * @return ERROR HTTP response
     */
    @ExceptionHandler(Exception.class)
    public HttpEntity<WrappedResponseDto<?>> handleException(Exception ex) {
        System.out.println(ex); // TO DO : replace with proper logger
        return HttpWrapper.fromWrToRes(ResponseWrapper.ERROR("Unknown error"));
    }
}