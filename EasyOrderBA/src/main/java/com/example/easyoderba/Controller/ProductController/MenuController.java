package com.example.easyoderba.Controller.ProductController;


import com.example.easyoderba.Model.DTO.request.ProductReq.CategoryReq;
import com.example.easyoderba.Model.DTO.request.ProductReq.ProductReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import com.example.easyoderba.Model.DTO.response.ProductRes.MenuRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.ProductRes;
import com.example.easyoderba.Service.ProductService.CategoryService;
import com.example.easyoderba.Service.ProductService.MenuService;
import com.example.easyoderba.Service.ProductService.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuController {
    MenuService menuService;
    CategoryService categoryService;
    ProductService productService;

    @GetMapping
    public ApiResponse<MenuRes> getMenu() throws IOException {
        return ApiResponse.<MenuRes>builder()
                .result(menuService.getMenu())
                .build();
    }
    @GetMapping("/product")
    public ApiResponse<List<ProductRes>> getProducts(@RequestParam String keyword) throws IOException {
        return ApiResponse.<List<ProductRes>>builder()
                .result(productService.findByParam(keyword))
                .build();
    }
    @PostMapping("/category")
    public ApiResponse<?> addCategory(@RequestBody CategoryReq categoryReq) {
        return ApiResponse.<String>builder()
                .result(categoryService.addCategory(categoryReq))
                .build();
    }
    @PostMapping("/product")
    public ApiResponse<?> addProduct(ProductReq productReq) throws IOException {
        return ApiResponse.builder()
                .result(productService.addProduct(productReq))
                .build();
    }
    @DeleteMapping("/product")
    public ApiResponse<String> deleteProduct(@RequestParam Long id) throws IOException {
        return ApiResponse.<String>builder()
                .result(productService.DeleteProduct(id))
                .build();
    }
    @PutMapping("/product")
    public ApiResponse<String> updateProduct(ProductReq productReq) throws IOException {
        return ApiResponse.<String>builder()
                .result(productService.updateProduct(productReq))
                .build();
    }

}
