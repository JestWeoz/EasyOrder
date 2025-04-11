package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Model.DTO.response.ProductRes.CategoryRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.MenuRes;
import com.example.easyoderba.Repository.ProductRespository.CategoryRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ModelMapper modelMapper;

    public MenuRes getMenu() {
        List<CategoryRes> categoryResList = new ArrayList<>();
        categoryRepository.findAll().forEach(category -> {
            categoryResList.add(modelMapper.map(category, CategoryRes.class));
        });
        return MenuRes.builder().categories(categoryResList).build();
    }


}
