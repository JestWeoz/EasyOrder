package com.example.easyoderba.Controller.PaymentController;

import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.Date;
import java.util.Map;


@Controller
public class Checkout {
    private final PayOS payOS;

    public Checkout(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-payment-qr")
    @ResponseBody
    public ResponseEntity<?> createPaymentQR(@RequestBody PaymentRequest paymentRequest) {
        try {
            final String productName = paymentRequest.getProductName() != null ?
                    paymentRequest.getProductName() : "Mì tôm hảo hảo ly";
            final String description = paymentRequest.getDescription() != null ?
                    paymentRequest.getDescription() : "Thanh toan don hang";
            final int price = paymentRequest.getPrice() > 0 ?
                    paymentRequest.getPrice() : 2000;

            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder()
                    .name(productName)
                    .quantity(1)
                    .price(price)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(price)
                    .cancelUrl("/cancel")
                    .returnUrl("/cancel")
                    .description(description)
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            // Tạo response chỉ chứa thông tin cần thiết
            PaymentQRResponse response = PaymentQRResponse.builder()
                    .orderCode(orderCode)
                    .qrCode(data.getQrCode())
                    .checkoutUrl(data.getCheckoutUrl())
                    .amount(price)
                    .description(description)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể tạo QR code thanh toán", "message", e.getMessage()));
        }
    }

    // DTO classes
    @Data
    @Builder
    public static class PaymentRequest {
        private String productName;
        private String description;
        private int price;
    }

    @Data
    @Builder
    public static class PaymentQRResponse {
        private long orderCode;
        private String qrCode;
        private String checkoutUrl;
        private int amount;
        private String description;
    }

    // API để check trạng thái thanh toán
    @RequestMapping(method = RequestMethod.GET, value = "/payment-status/{orderCode}")
    @ResponseBody
    public ResponseEntity<?> getPaymentStatus(@PathVariable long orderCode) {
        try {
            PaymentLinkData paymentInfo = payOS.getPaymentLinkInformation(orderCode);
            Map<String, Object> response = Map.of(
                    "orderCode", orderCode,
                    "status", paymentInfo.getStatus(),
                    "amount", paymentInfo.getAmount()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy thông tin thanh toán"));
        }
    }
}