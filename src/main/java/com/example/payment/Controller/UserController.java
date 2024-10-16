package com.example.payment.Controller;

import com.example.payment.DTO.JWTDTO;
import com.example.payment.DTO.UserDTO;
import com.example.payment.DTO.OAuth2CodeDTO;
import com.example.payment.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO.getUid(), userDTO.getPassword()));
    }

    // 유저 전체 조회
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUsers(userDetails));
    }

    // id로 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 회원 정보 수정
    @PutMapping("/{uid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.updateUser(uid, userDTO, userDetails));
    }

    // 회원 탈퇴
    @DeleteMapping("/{uid}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String uid, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.deleteUser(uid, userDetails));
    }

    // 카카오 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok(userService.loginWithOAuth2(code));
    }

    // 카카오 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        return ResponseEntity.ok(userService.loginWithOAuth2(codeDTO.getCode()));
    }
}
