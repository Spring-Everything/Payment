package com.example.payment.Service;

import com.example.payment.DTO.JWTDTO;
import com.example.payment.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    boolean isUidDuplicate(String uid);
    boolean isNicknameDuplicate(String nickname);
    UserDTO createUser(UserDTO userDTO);
    JWTDTO login(String uid, String password);
    UserDTO updateUser(String uid, UserDTO userDTO, UserDetails userDetails);
    void deleteUser(String uid, UserDetails userDetails);
    JWTDTO loginWithOAuth2(String code);
}
