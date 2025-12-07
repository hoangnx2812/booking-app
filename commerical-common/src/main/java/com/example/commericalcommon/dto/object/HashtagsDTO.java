package com.example.commericalcommon.dto.object;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashtagsDTO {
    Long id;
    String name;
    String objectType;
    Long objectId;
}
