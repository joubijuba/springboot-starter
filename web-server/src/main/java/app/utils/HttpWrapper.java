package app.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import core.dtos.WrappedResponseDto;
import core.dtos.ResponseStatus;

/**
 * HTTP Response factory class to map wrappedResponse to HTTP response. Only to be used in controllers and GlobalExceptionHandler.java
 */
public class HttpWrapper {

    private HttpWrapper() {
    }

    public static HttpEntity<WrappedResponseDto<?>> fromWrToRes(WrappedResponseDto<?> wr) {
        switch (wr.getStatus()) { // getStatus is a class method thanks to @Builder annotator
        case ResponseStatus.OK:
            return ResponseEntity.status(HttpStatus.OK).body(wr);

        case ResponseStatus.INTERNAL_SERVER_ERROR:
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(wr);

        case ResponseStatus.NOT_FOUND:
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(wr);

        case ResponseStatus.UNAUTHORIZED:
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(wr);

        case ResponseStatus.BAD_REQUEST:
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(wr);

        case ResponseStatus.CONFLICT:
            return ResponseEntity.status(HttpStatus.CONFLICT).body(wr);

        default: // Will never happen but we are forced to set default case
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(wr);
        }
    }

    /**
     * HTTP Response with httpOnly cookie
     * @param rw wrapped response
     * @param cookie encoded JWT 
     * @return HTTP Response
     */
    public static HttpEntity<WrappedResponseDto<?>> fromWrToHttpWithCookie(WrappedResponseDto<?> rw, String cookie) {
        ResponseCookie responseCookie = ResponseCookie.from("auth_token", cookie).httpOnly(true).path("/").build();

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(rw);
    }
}
