package core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import core.dtos.ResponseStatus;
import core.dtos.WrappedResponseDto;

/**
 * Factory class for WrappedResponseDto, for every status code we use in the app. Used by controllers and exception handlers to normalize HTTP
 * responses
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

    private ResponseWrapper() {
    }

    public static <DATA> WrappedResponseDto<DATA> OK(String message, DATA data) {
        return WrappedResponseDto.<DATA>builder().status(ResponseStatus.OK).message(message).data(data).build();
    }

    public static WrappedResponseDto<?> OK(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.OK).message(message).build();
    }

    public static WrappedResponseDto<?> NOT_FOUND(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.NOT_FOUND).message(message).build();
    }

    public static WrappedResponseDto<?> BAD_REQUEST(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.BAD_REQUEST).message(message).build();
    }

    public static WrappedResponseDto<?> UNAUTHORIZED(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.UNAUTHORIZED).message(message).build();
    }

    public static WrappedResponseDto<?> ERROR(String message, Object error) {
        return WrappedResponseDto.builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).message(message).error(error).build();
    }

    public static WrappedResponseDto<?> ERROR(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.INTERNAL_SERVER_ERROR).message(message).build();
    }

    public static WrappedResponseDto<?> CONFLICT(String message) {
        return WrappedResponseDto.builder().status(ResponseStatus.CONFLICT).message(message).build();
    }
}
