package com.example.easyoderba.Model.DTO.response.ProductRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jfr.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuRes {
    List<CategoryRes> categories;
}
