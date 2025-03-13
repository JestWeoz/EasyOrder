package com.example.easyoderba.Service.Implements;

import com.example.easyoderba.Model.DTO.request.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.LoginReq;
import com.example.easyoderba.Model.DTO.response.UserResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.UserEntity;
import com.example.easyoderba.Repository.UserRepository;
import com.example.easyoderba.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity CreateUser(CreateUserReq createUserReq) {
        if(userRepository.existsByUsername(createUserReq.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        UserEntity userEntity = modelMapper.map(createUserReq, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
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
            throw new RuntimeException("User not found");
        }
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        return userResponse;
    }
}
