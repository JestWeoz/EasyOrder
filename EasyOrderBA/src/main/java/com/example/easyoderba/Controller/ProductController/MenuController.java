package com.example.easyoderba.Controller.ProductController;


import com.example.easyoderba.Model.DTO.request.ProductReq.CategoryReq;
import com.example.easyoderba.Model.DTO.request.ProductReq.ProductReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import com.example.easyoderba.Model.DTO.response.ProductRes.MenuRes;
import com.example.easyoderba.Service.ProductService.CategoryService;
import com.example.easyoderba.Service.ProductService.MenuService;
import com.example.easyoderba.Service.ProductService.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuController {
    MenuService menuService;
    CategoryService categoryService;
    ProductService productService;

    @GetMapping
    public ApiResponse<MenuRes> getMenu() {
        return ApiResponse.<MenuRes>builder()
                .result(menuService.getMenu())
                .build();
    }
    @PostMapping("/category")
    public ApiResponse<?> addCategory(@RequestBody CategoryReq categoryReq) {
        return ApiResponse.<String>builder()
                .result(categoryService.addCategory(categoryReq))
                .build();
    }
    @PostMapping("/product")
    public ApiResponse<?> addProduct(@RequestBody ProductReq productReq) {
        return ApiResponse.builder()
                .result(productService.addProduct(productReq))
                .build();
    }

}
