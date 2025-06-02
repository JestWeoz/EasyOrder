package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Config.ModelMapperConfig;
import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.OrderItemReq;
import com.example.easyoderba.Model.DTO.request.ProductReq.OrderReq;
import com.example.easyoderba.Model.DTO.response.ProductRes.*;
import com.example.easyoderba.Model.Entity.ProductEntity.*;
import com.example.easyoderba.Repository.ProductRespository.OrderRepository;
import com.example.easyoderba.Repository.ProductRespository.ProductRepository;
import com.example.easyoderba.Repository.ProductRespository.TableRepository;
import com.example.easyoderba.Utils.Enums.OrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    ModelMapperConfig modelMapperConfig;
    ModelMapper modelMapper;
    TableRepository tableRepository;
    ProductRepository productRepository;

    

    public List<OrderRes> getAllOrders() {
       List<OrderEntity> orderEntities = orderRepository.findAll();
       List<OrderRes> orderResList = new ArrayList<>();

       for (OrderEntity orderEntity : orderEntities) {
           TableRes tableRes = modelMapper.map(orderEntity.getTable(), TableRes.class);
           List<OrderItemRes> orderItemResList = new ArrayList<>();
           orderEntity.getOrderItems().forEach(orderItemEntity -> {
               OrderItemRes orderItemRes = modelMapper.map(orderItemEntity, OrderItemRes.class);
               orderItemResList.add(orderItemRes);
           });
           OrderRes orderRes = modelMapper.map(orderEntity, OrderRes.class);
           orderRes.setTable(tableRes);
           orderRes.setCreated_at(String.valueOf(orderEntity.getCreatedAt()));
           orderRes.setOrderItems(orderItemResList);
           orderResList.add(orderRes);
       }
       return orderResList;
    }
    
    // Lấy đơn hàng theo ID

    public List<OrderRes> getOrderByTableId(Long tableId) {
        TableEntity tableEntity = tableRepository.findById(tableId).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
        List<OrderEntity> orderEntities = orderRepository.findAllByTable(tableEntity);
        List<OrderRes> orderResList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            if (orderEntity.getUpdatedAt() != null) {
                long diffMillis = new Date().getTime() - orderEntity.getUpdatedAt().getTime();
                long diffHours = diffMillis / (1000 * 60 * 60);
                if (diffHours > 1) {
                    continue;
                }
            }
            if (orderEntity.getIsPaid()) {
                continue;
            }
            List<OrderItemEntity> orderItemEntities = orderEntity.getOrderItems();
            List<OrderItemRes> orderItemResList = new ArrayList<>();
            for (OrderItemEntity item : orderItemEntities) {
                OrderItemRes orderItemRes = modelMapper.map(item, OrderItemRes.class);
                ProductEntity productEntity = item.getProduct();
                List<ImageEntity> images = productEntity.getImageEntities();
                List<ImageResponse> imageResponses = new ArrayList<>();
                for (ImageEntity image : images) {
                    imageResponses.add(modelMapper.map(image, ImageResponse.class));
                }
                orderItemRes.getProduct().setImages(imageResponses);
                orderItemResList.add(orderItemRes);
            }
            OrderRes orderRes = modelMapper.map(orderEntity, OrderRes.class);
            orderRes.setOrderItems(orderItemResList);
            orderResList.add(orderRes);
        }
        return orderResList;
    }

    // Tạo đơn hàng mới
    public void createOrder(OrderReq orderReq) {
        OrderEntity newOrder = modelMapper.map(orderReq, OrderEntity.class);
        TableEntity tableEntity = tableRepository.findById(orderReq.getTableId()).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
        newOrder.setTable(tableEntity);
        tableEntity.setStatus(0L);
        tableRepository.save(tableEntity);
        List<OrderItemEntity> orderItemEntity = getOrderItemEntities(orderReq, newOrder);
        newOrder.setOrderItems(orderItemEntity);
        newOrder.setStatus(OrderStatus.PENDING);
        orderRepository.save(newOrder);
    }

    private List<OrderItemEntity> getOrderItemEntities(OrderReq orderReq, OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntity = new java.util.ArrayList<>(List.of());
        List<OrderItemReq> orderItemReqs = orderReq.getOrderItems();
        orderItemReqs.forEach(orderItemReq -> {
            orderItemEntity.add(OrderItemEntity.builder()
                            .product(productRepository.findById(orderItemReq.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                    .quantity(orderItemReq.getQuantity())
                            .order(orderEntity)
                    .build());
        });
        return orderItemEntity;
    }

    // Cập nhật đơn hàng
    public void updateOrder(OrderReq orderReq) {
        OrderEntity orderEntity = orderRepository.findById(orderReq.getOrderId()).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (orderReq.getStatus() != null) {
            orderEntity.setStatus(orderReq.getStatus());
            if (orderReq.getStatus() == OrderStatus.COMPLETED) {
                orderEntity.getTable().setStatus(1L);
                tableRepository.save(orderEntity.getTable());
            }
        }
        if (orderReq.getIsPaid() != null) {
            orderEntity.setIsPaid(orderReq.getIsPaid());
        }
        orderRepository.save(orderEntity);
    }
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
