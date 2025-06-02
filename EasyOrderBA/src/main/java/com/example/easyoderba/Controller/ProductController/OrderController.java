package com.example.easyoderba.Controller.ProductController;


import com.example.easyoderba.Model.DTO.request.ProductReq.OrderReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import com.example.easyoderba.Model.DTO.response.ProductRes.OrderRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.TableRes;
import com.example.easyoderba.Service.ProductService.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    public void addOrder(@RequestBody OrderReq order) {
        orderService.createOrder(order);
    }
    @DeleteMapping
    public void deleteOrder(@RequestParam Long id) {
        orderService.deleteOrder(id);
    }
    @GetMapping
    public ApiResponse<List<OrderRes>> getOrderByTable(@RequestParam Long tableId) {
        return ApiResponse.<List<OrderRes>>builder()
                .result(orderService.getOrderByTableId(tableId))
                .build();
    }
    @PutMapping("/updateOrder")
    public void updateOrderStatus(@RequestBody OrderReq orderReq) {
        orderService.updateOrder(orderReq);
    }
    @GetMapping("/getAll")
    public ApiResponse<List<OrderRes>> getAllOrder() {
        return ApiResponse.<List<OrderRes>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    
}
