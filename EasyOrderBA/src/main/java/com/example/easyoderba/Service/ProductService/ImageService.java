package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Model.DTO.response.ProductRes.ImageResponse;
import com.example.easyoderba.Model.Entity.ProductEntity.ImageEntity;
import com.example.easyoderba.Repository.ProductRespository.ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {

    ImageRepository imageRepository;
    ModelMapper modelMapper;

    public void save(Map result) {
        ImageEntity imageEntity = ImageEntity.builder()
                .url((String) result.get("url"))
                .build();
        imageRepository.save(imageEntity);
    }

    public List<ImageResponse> findByProductId(Long productId) {
        List<ImageEntity> imageEntities = imageRepository.findByProductId(productId);
        List<ImageResponse> imageResponses = new ArrayList<>();
        for (ImageEntity imageEntity : imageEntities) {
            ImageResponse imageResponse = modelMapper.map(imageEntity, ImageResponse.class);
            imageResponses.add(imageResponse);
        }
        return imageResponses;
    }
    public void DeleteById(Long id) {
        imageRepository.deleteById(id);
    }
    public ImageResponse findById(Long id) {
        ImageEntity imageEntity = imageRepository.findById(id).orElse(null);
        return modelMapper.map(imageEntity, ImageResponse.class);
    }


}
