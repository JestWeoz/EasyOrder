package com.example.easyoderba.Config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT) // Chỉ ánh xạ field có tên trùng khớp
                .setPropertyCondition(Conditions.isNotNull()); // Chỉ ánh xạ nếu giá trị không null
        return modelMapper;
    }
}

