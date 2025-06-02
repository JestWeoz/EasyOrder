package com.example.easyoderba.Service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.easyoderba.Model.DTO.response.ProductRes.ImageResponse;
import com.example.easyoderba.Model.Entity.ProductEntity.ImageEntity;
import com.example.easyoderba.Repository.ProductRespository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.easyoderba.Model.DTO.response.ProductRes.CategoryRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.MenuRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.ProductRes;
import com.example.easyoderba.Model.Entity.ProductEntity.CategoryEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;
import com.example.easyoderba.Repository.ProductRespository.CategoryRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ImageRepository imageRepository;
    ModelMapper modelMapper;

    public MenuRes getMenu() throws IOException {
        List<CategoryRes> categoryResList = new ArrayList<>();
        List<CategoryEntity> categories = categoryRepository.findAll();
        
        for (CategoryEntity category : categories) {
            List<ProductEntity> products = productRepository.findByCategory(category);
            List<ProductRes> productResList = new ArrayList<>();
            
            for (ProductEntity product : products) {
                List<ImageEntity> images = imageRepository.findByProductId(product.getId());
                List<ImageResponse> imageResponses = new ArrayList<>();
                for (ImageEntity image : images) {
                    imageResponses.add(modelMapper.map(image, ImageResponse.class));
                }
                productResList.add(ProductRes.builder()
                                .id(product.getId())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .images(imageResponses)
                                .name(product.getName())
                                .status(product.getStatus())
                        .build());
            }
            
            CategoryRes categoryRes = CategoryRes.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .products(productResList)
                    .build();
                    
            categoryResList.add(categoryRes);
        }
        
        return MenuRes.builder().categories(categoryResList).build();
    }
}
