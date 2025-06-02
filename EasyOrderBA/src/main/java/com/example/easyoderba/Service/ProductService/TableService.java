package com.example.easyoderba.Service.ProductService;


import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.TableReq;
import com.example.easyoderba.Model.DTO.response.ProductRes.TableRes;
import com.example.easyoderba.Model.Entity.ProductEntity.TableEntity;
import com.example.easyoderba.Repository.ProductRespository.TableRepository;
import com.example.easyoderba.constant.TableCreateQR;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableService {
    TableRepository tableRepository;
    ModelMapper modelMapper;

    public String addTable(TableReq tableReq) {
        TableEntity tableEntity = TableEntity.builder()
                .name(tableReq.getName())
                .capacity(tableReq.getCapacity())
                .status(1L)
                .description(tableReq.getDescription())
                .build();
        tableRepository.save(tableEntity);
        tableEntity.setUrl(TableCreateQR.QR_URL_HEADER + tableEntity.getId());
        tableRepository.save(tableEntity);
        return "oke";
    }
    public TableRes getTableById(Long id) throws AppException {
        tableRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
        return modelMapper.map(tableRepository.findById(id), TableRes.class);
    }

    public List<TableRes> getTables() {
        List<TableRes> tableResList = new ArrayList<>();
        for (TableEntity tableEntity : tableRepository.findAll()) {
            TableRes tableRes = new TableRes();
            tableResList.add(modelMapper.map(tableEntity, TableRes.class));
        }
        return tableResList;
    }
    public void updateTable(TableReq tableReq) {
        TableEntity tableEntity = tableRepository.findById(tableReq.getId()).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
        String url = tableEntity.getUrl();
        tableEntity = modelMapper.map(tableReq, TableEntity.class);
        tableEntity.setUrl(url);
        tableRepository.save(tableEntity);
    }
    public void deleteTable(long id) {
        tableRepository.deleteById(id);
    }
}
