package org.easyoder.Service;

import org.easyoder.Model.DTO.UserCreationReq;
import org.easyoder.Model.Entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(UserCreationReq userCreationReq);
}
