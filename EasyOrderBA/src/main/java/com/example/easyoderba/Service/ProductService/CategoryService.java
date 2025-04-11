package com.example.easyoderba.Service.ProductService;


import com.example.easyoderba.Model.DTO.request.ProductReq.CategoryReq;
import com.example.easyoderba.Model.Entity.ProductEntity.CategoryEntity;
import com.example.easyoderba.Repository.ProductRespository.CategoryRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;

    public String addCategory(CategoryReq categoryReq) {
        categoryRepository.save(CategoryEntity.builder()
                        .name(categoryReq.getName())
                        .build());
        return "oke";
    }
}
