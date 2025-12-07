package com.example.commericalcommon.dto.object;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicesDTO {
    Long id;
    String name;
    Double price;
    Long userId;
    Long storeId;
    String time;
}
