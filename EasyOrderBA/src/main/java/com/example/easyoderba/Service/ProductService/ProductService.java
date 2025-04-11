package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.ProductReq;
import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;
import com.example.easyoderba.Repository.ProductRespository.CategoryRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public String addProduct(ProductReq productReq) {
        productRepository.save(ProductEntity.builder()
                        .price(productReq.getPrice())
                        .category(categoryRepository.findById(productReq.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                        .description(productReq.getDescription())
                        .name(productReq.getName())
                .build());
        return "oke";
    }

}
