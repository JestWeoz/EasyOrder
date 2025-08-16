package com.example.easyoderba.Service.ProductService;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.ProductReq.InvoiceReq;
import com.example.easyoderba.Model.DTO.response.ProductRes.PaymentResponse;
import com.example.easyoderba.Model.Entity.ProductEntity.OrderEntity;
import com.example.easyoderba.Repository.ProductRespository.OrderRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PayOS payOS;
    OrderRepository orderRepository;

    public PaymentResponse createPaymentQR(InvoiceReq invoiceReq) {
        try {
            // Lấy thông tin đơn hàng
            List<OrderEntity> orders = orderRepository.findAllById(invoiceReq.getOrderIDs());
            if (orders.isEmpty()) {
                throw new AppException(ErrorCode.ORDER_NOT_FOUND);
            }

            // Tính tổng tiền
            long totalAmount = orders.stream()
                    .mapToLong(order -> order.getTotalAmount().longValue())
                    .sum();

            // Tạo mã đơn hàng
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            // Tạo item cho PayOS
            ItemData item = ItemData.builder()
                    .name("Thanh toán đơn hàng #" + orderCode)
                    .price((int) totalAmount)
                    .quantity(1)
                    .build();

            // Tạo payment data
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description("Thanh toán đơn hàng #" + orderCode)
                    .amount((int) totalAmount)
                    .item(item)
                    .build();

            // Tạo payment link
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            // Tạo QR code
            String qrBase64 = generateQRCodeBase64(data.getCheckoutUrl());

            return PaymentResponse.builder()
                    .qrCodeBase64(qrBase64)
                    .checkoutUrl(data.getCheckoutUrl())
                    .orderCode(orderCode)
                    .status("success")
                    .message("Tạo QR code thanh toán thành công")
                    .build();

        } catch (Exception e) {
            return PaymentResponse.builder()
                    .status("error")
                    .message("Lỗi khi tạo QR code: " + e.getMessage())
                    .build();
        }
    }

    private String generateQRCodeBase64(String url) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        byte[] bytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
} 