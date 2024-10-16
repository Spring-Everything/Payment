package com.example.payment.Service;

import com.example.payment.DTO.ProductDTO;
import com.example.payment.Entity.ProductEntity;
import com.example.payment.Entity.UserEntity;
import com.example.payment.Repository.ProductRepository;
import com.example.payment.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${payment.iamport.api_key}")
    private String apiKey;

    @Value("${payment.iamport.api_secret}")
    private String apiSecret;

    @Value("${payment.iamport.base_url}")
    private String baseUrl;

    // 상품 생성
    public ProductDTO createProduct(ProductDTO productDTO) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId()).orElseThrow();
        ProductEntity productEntity = productDTO.dtoToEntity(userEntity);
        ProductEntity savedProduct = productRepository.save(productEntity);
        logger.info("상품 등록 및 결제 완료!");
        return ProductDTO.entityToDto(savedProduct);
    }

    // 상품 전체 조회
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        logger.info("상품 전체 조회 성공! ");
        return productEntities.stream()
                .map(ProductDTO::entityToDto)
                .collect(Collectors.toList());
    }

    // id로 상품 조회
    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow();
        logger.info("아이디 " + id + "번 상품 조회 성공!");
        return ProductDTO.entityToDto(productEntity);
    }

    // 상품 결제
    public String processPayment(Long productId) {
        // 상품 조회
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));
        int amount = productEntity.getPrice();  // 상품 가격을 결제 요청에 사용

        // 1. Access Token 발급
        String accessToken = getAccessToken();

        // 2. 결제 API 호출
        return callPaymentAPI(productId, amount, accessToken);
    }

    // 결제 토큰 발급 메서드
    private String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // IAMPORT 토큰 발급 요청 데이터
        String body = String.format("{\"imp_key\":\"%s\", \"imp_secret\":\"%s\"}", apiKey, apiSecret);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Access Token 요청
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/users/getToken", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("response").path("access_token").asText();
            } catch (JsonProcessingException e) {
                logger.error("토큰 파싱 오류", e);
                throw new RuntimeException("Access Token 발급 실패");
            }
        } else {
            logger.error("Access Token 발급 실패");
            throw new RuntimeException("Access Token 발급 실패");
        }
    }

    // 결제 API 호출 메서드
    private String callPaymentAPI(Long productId, int amount, String accessToken) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        // 결제 요청 데이터
        String body = String.format(
                "{\"merchant_uid\":\"order_%d\", \"amount\":%d, \"name\":\"%s\", \"buyer_name\":\"홍길동\", \"buyer_tel\":\"010-1234-5678\", \"buyer_email\":\"buyer@example.com\"}",
                productId, amount, productEntity.getName());  // 상품 이름을 실제 상품 정보로 변경

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // 결제 API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/payments/prepare", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("결제 성공! 상품 ID: " + productId + ", 결제 금액: " + amount);
            return "결제 성공!";
        } else {
            logger.error("결제 실패! 상품 ID: " + productId);
            throw new RuntimeException("결제 실패!");
        }
    }
}

