package com.example.commericalcommon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageRequest implements Serializable {
    @NotNull(message = "Page number must not be null")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    Integer page;
    @NotNull(message = "Size number must not be null")
    @Min(value = 1, message = "Size number must be greater than or equal to 1")
    Integer size;
}
