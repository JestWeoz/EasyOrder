package com.example.easyoderba.Service.Implements;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.LoginReq;
import com.example.easyoderba.Model.DTO.response.UserResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.UserEntity;
import com.example.easyoderba.Repository.UserRepository;
import com.example.easyoderba.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    ModelMapper modelMapper = new ModelMapper();
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public String CreateUser(CreateUserReq createUserReq) {

        if(userRepository.existsByUsername(createUserReq.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserEntity userEntity = modelMapper.map(createUserReq, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return "Register Success";
    }

    @Override
    public ResponseEntity<?> Login(LoginReq loginReq) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(loginReq.getUsername());
        if (userEntity == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            if (!passwordEncoder.matches(loginReq.getPassword(), userEntity.getPassword())) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.ok(userEntity);
            }
        }
    }

    @Override
    public UserResponse GetUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return modelMapper.map(userEntity, UserResponse.class);
    }
}
