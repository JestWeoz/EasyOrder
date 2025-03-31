package com.example.easyoderba.Service.Implements;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.AuthReq.CreateUserReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.UpdateUserReq;
import com.example.easyoderba.Model.DTO.response.AuthRes.UserResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import com.example.easyoderba.Model.Entity.AuthEntity.User;
import com.example.easyoderba.Repository.RoleRepository;
import com.example.easyoderba.Repository.UserRepository;
import com.example.easyoderba.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    ModelMapper modelMapper = new ModelMapper();
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    public String CreateUser(CreateUserReq createUserReq) {

        if(userRepository.existsByUsername(createUserReq.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = modelMapper.map(createUserReq, User.class);
        HashSet<Role> roles = new HashSet<>();
        Role role = roleRepository.findById(1L).orElseThrow(() -> new AppException(ErrorCode.ROLE_INVALID));
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Register Success";
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userEntities = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        userEntities.forEach(entity -> {
            userResponses.add(modelMapper.map(entity, UserResponse.class));
        });
        return userResponses;
    }


    @Override
    public UserResponse GetUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepository.findById(id).orElse(null);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public String updateUser(UpdateUserReq updateUserReq) {
        User user = userRepository.findByUsername(updateUserReq.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (updateUserReq.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserReq.getPassword()));
        }
        if (updateUserReq.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            updateUserReq.getRoles().forEach(role -> {
                roles.add(roleRepository.findById(role).orElseThrow(() -> new AppException(ErrorCode.ROLE_INVALID)));
            });
            user.setRoles(roles);
        }
        userRepository.save(user);
        return "Update Success";
    }
}
