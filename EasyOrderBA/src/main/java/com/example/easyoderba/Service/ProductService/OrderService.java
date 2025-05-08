package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.OrderItemReq;
import com.example.easyoderba.Model.DTO.request.ProductReq.OrderReq;
import com.example.easyoderba.Model.DTO.response.ProductRes.OrderItemRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.OrderRes;
import com.example.easyoderba.Model.DTO.response.ProductRes.ProductRes;
import com.example.easyoderba.Model.Entity.ProductEntity.OrderEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.OrderItemEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.TableEntity;
import com.example.easyoderba.Repository.ProductRespository.OrderItemRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    ModelMapper modelMapper;
    TableRepository tableRepository;
    ProductRepository productRepository;

    OrderItemRepository orderItemRepository;

    

    public List<OrderRes> getAllOrders() {
        return orderItemRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderRes.class))
                .collect(Collectors.toList());
    }
    
    // Lấy đơn hàng theo ID

    public List<OrderRes> getOrderByTableId(Long tableId) {
        TableEntity tableEntity = tableRepository.findById(tableId).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));
        List<OrderEntity> orderEntities = orderRepository.findAllByTable(tableEntity);
        List<OrderRes> orderResList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            List<OrderItemEntity> orderItemEntities = orderEntity.getOrderItems();
            List<OrderItemRes> orderItemResList = new ArrayList<>();
            for (OrderItemEntity item : orderItemEntities) {
                OrderItemRes orderItemRes = modelMapper.map(item, OrderItemRes.class);
                orderItemRes.setProduct(modelMapper.map(item.getProduct(), ProductRes.class));
                orderItemResList.add(modelMapper.map(item, OrderItemRes.class));
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
        newOrder.setTable(tableRepository.findById(orderReq.getTableId()).orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND)));
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
    public void updateOrder(Long id, OrderEntity orderDetails) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isPresent()) {
            OrderEntity existingOrder = order.get();
            existingOrder.setTable(orderDetails.getTable());
            existingOrder.setOrderItems(orderDetails.getOrderItems());
            existingOrder.setTotalAmount(orderDetails.getTotalAmount());
            existingOrder.setStatus(orderDetails.getStatus());
            existingOrder.setNote(orderDetails.getNote());
            existingOrder.setPaymentMethod(orderDetails.getPaymentMethod());
            existingOrder.setIsPaid(orderDetails.getIsPaid());
            existingOrder.setCustomerName(orderDetails.getCustomerName());
            existingOrder.setCustomerPhone(orderDetails.getCustomerPhone());
            orderRepository.save(existingOrder);
        }
    }
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
