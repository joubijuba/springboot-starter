package app.dtos;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * id is annoted with @Nullable because this field comes empty in add-product route
 * TO DO : 2 records (ProductRequestDto and ProductResponseDto) for separation of concerns
 */
public record ProductDto(
    @Nullable UUID id,
    @NotNull @Size(min = 3) String name
) {}