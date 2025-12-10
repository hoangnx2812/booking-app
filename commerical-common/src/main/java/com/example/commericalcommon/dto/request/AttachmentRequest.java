package com.example.commericalcommon.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentRequest {
    @NotBlank(message = "File name must not be blank")
    String name;

    @NotBlank(message = "File data must not be blank")
    String data;

    String description;

    String type;
}
