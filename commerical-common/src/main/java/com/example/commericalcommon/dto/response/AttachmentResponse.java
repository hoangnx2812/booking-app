package com.example.commericalcommon.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentResponse {
    Long id;
    String name;
    String thumbnail;
    String pathSmall;
    String pathLarge;
    String pathOriginal;
    String size;
    String type;
}
