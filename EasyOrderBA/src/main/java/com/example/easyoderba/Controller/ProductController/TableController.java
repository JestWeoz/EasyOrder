package com.example.easyoderba.Controller.ProductController;


import com.example.easyoderba.Model.DTO.request.ProductReq.TableReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import com.example.easyoderba.Model.DTO.response.ProductRes.TableRes;
import com.example.easyoderba.Service.ProductService.TableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableController {
    TableService tableService;

    @PostMapping
    public ApiResponse<String> addTable(@RequestBody TableReq tableReq) {
        return ApiResponse.<String>builder()
                .result(tableService.addTable(tableReq))
                .build();
    }
    @GetMapping("/getAll")
    public ApiResponse<List<TableRes>> getTables() {
        return ApiResponse.<List<TableRes>>builder()
                .result(tableService.getTables())
                .build();
    }
    @GetMapping
    public ApiResponse<TableRes> getTable(@RequestParam long tableId) {
        return ApiResponse.<TableRes>builder()
                .result(tableService.getTableById(tableId))
                .build();
    }
    @PutMapping
    public void updateTable( TableReq tableReq) {
        tableService.updateTable(tableReq);
    }
    @DeleteMapping
    public void deleteTable(@RequestParam Long id) {
        tableService.deleteTable(id);
    }


}
