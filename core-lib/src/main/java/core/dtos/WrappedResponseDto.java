package core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Builder // to have multiple constructors (constructor overloading)
@JsonInclude(JsonInclude.Include.NON_NULL) // remove null values from object
@Data // make it serializable / deserializable
public class WrappedResponseDto<DATA> {
    ResponseStatus status;
    String message;
    DATA data;
    Object error;
}
