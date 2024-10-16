package com.example.payment.Controller;

import com.example.payment.DTO.ProductDTO;
import com.example.payment.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 생성
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    // 상품 전체 조회
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts(); // 상품 목록 반환
    }

    // id로 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 결제 처리
    @PostMapping("/pay/{productId}")
    public ResponseEntity<String> processPayment(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.processPayment(productId));
    }
}
