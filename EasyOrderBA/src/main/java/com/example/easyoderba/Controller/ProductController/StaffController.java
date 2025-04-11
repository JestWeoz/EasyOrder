package com.example.easyoderba.Controller.ProductController;

import com.example.easyoderba.Model.DTO.MessageDTO;
import com.example.easyoderba.Model.DTO.response.AuthRes.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    @MessageMapping("/call-staff")
    @SendTo("/topic/staff")
    public ApiResponse<MessageDTO> callStaff(@RequestBody MessageDTO message) {
        return ApiResponse.<MessageDTO>builder()
                .result(message)
                .build();
    }
}
