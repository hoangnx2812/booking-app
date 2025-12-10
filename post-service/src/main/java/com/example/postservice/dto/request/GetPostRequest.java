package com.example.postservice.dto.request;

import com.example.commericalcommon.dto.PageRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetPostRequest extends PageRequest {
    String keyword;
    Double priceFrom;
    Double priceTo;
}
