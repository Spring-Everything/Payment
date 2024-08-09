package com.example.payment.Controller;

import com.example.payment.DTO.JWTDTO;
import com.example.payment.DTO.UserDTO;
import com.example.payment.DTO.OAuth2CodeDTO;
import com.example.payment.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody UserDTO userDTO) {
        JWTDTO login = userService.login(userDTO.getUid(), userDTO.getPassword());
        return ResponseEntity.ok(login);
    }

    //회원 정보 수정
    @PutMapping("/{uid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO updatedUser = userService.updateUser(uid, userDTO, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //회원 탈퇴
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(uid, userDetails);
        return ResponseEntity.noContent().build();
    }

    //카카오 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //카카오 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }
}
