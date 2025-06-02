package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.ProductReq;
import com.example.easyoderba.Model.DTO.response.ProductRes.ImageResponse;
import com.example.easyoderba.Model.DTO.response.ProductRes.ProductRes;
import com.example.easyoderba.Model.Entity.ProductEntity.ImageEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;
import com.example.easyoderba.Repository.ProductRespository.CategoryRepository;
import com.example.easyoderba.Repository.ProductRespository.ImageRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;
    ImageRepository imageRepository;
    ModelMapper modelMapper;

    public String addProduct(ProductReq productReq) throws IOException {
        List<MultipartFile> images = productReq.getImages();
        ProductEntity product = productRepository.save(ProductEntity.builder()
                .price(productReq.getPrice())
                .status(1)
                .category(categoryRepository.findById(productReq.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                .description(productReq.getDescription())
                .name(productReq.getName())
                .build());
        for (MultipartFile image : images) {
            Map result = cloudinaryService.uploadFile(image);
            ImageEntity imageEntity = ImageEntity.builder()
                    .url(result.get("url").toString())
                    .product(product)
                    .build();
            imageRepository.save(imageEntity);
        }
        return "success";
    }
    public String updateProduct(ProductReq productReq) throws IOException {
        ProductEntity product = productRepository.findById(productReq.getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setPrice(productReq.getPrice());
        product.setCategory(categoryRepository.findById(productReq.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
        product.setDescription(productReq.getDescription());
        product.setStatus(productReq.getStatus());
        product.setName(productReq.getName());
        if (productReq.getImages() != null) {
            List<MultipartFile> images = productReq.getImages();
            for (MultipartFile image : images) {
                Map result = cloudinaryService.uploadFile(image);
                ImageEntity imageEntity = ImageEntity.builder()
                        .url(result.get("url").toString())
                        .product(product)
                        .build();
                imageRepository.save(imageEntity);
            }
        }
        productRepository.save(product);
        return "Success";
    }


    public List<ProductRes> findByParam(String keyword) throws IOException {
        List<ProductRes> productResList = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findByCustom(keyword);
        for (ProductEntity productEntity : productEntities) {
            List<ImageEntity> imageEntities = imageRepository.findByProductId(productEntity.getId());
            List<ImageResponse> imageResponses = new ArrayList<>();
            for (ImageEntity image : imageEntities) {
                imageResponses.add(modelMapper.map(image, ImageResponse.class));
            }
            ProductRes productRes = ProductRes.builder()
                    .id(productEntity.getId())
                    .name(productEntity.getName())
                    .price(productEntity.getPrice())
                    .images(imageResponses)
                    .description(productEntity.getDescription())
                    .build();
            productResList.add(productRes);
        }
        return productResList;
    }
    public String DeleteProduct(Long id) throws IOException {
        productRepository.deleteById(id);
        return "Success";
    }


}
