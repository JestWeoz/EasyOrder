package org.easyoder.Service.Implement;

import org.easyoder.Model.DTO.UserCreationReq;
import org.easyoder.Model.Entity.User;
import org.easyoder.Responsitory.UserRepository;
import org.easyoder.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ResponseEntity<?> createUser(UserCreationReq userCreationReq) {
        User user = modelMapper.map(userCreationReq, User.class);
        return ResponseEntity.ok(userRepository.save(user));
    }
}
