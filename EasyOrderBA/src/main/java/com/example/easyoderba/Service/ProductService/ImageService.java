package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Model.Entity.ProductEntity.FileEntity;
import com.example.easyoderba.Repository.ProductRespository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    FileRepository fileRepository;

    public void save(Map result) {
        FileEntity fileEntity = FileEntity.builder()
                .URL((String) result.get("url"))
                .build();
        fileRepository.save(fileEntity);
    }


}
