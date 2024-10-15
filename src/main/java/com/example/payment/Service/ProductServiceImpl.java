package com.example.payment.Service;

import com.example.payment.DTO.ProductDTO;
import com.example.payment.Entity.ProductEntity;
import com.example.payment.Entity.UserEntity;
import com.example.payment.Repository.ProductRepository;
import com.example.payment.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 상품 생성
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        UserEntity userEntity = userRepository.findById(productDTO.getUserId()).orElseThrow();
        ProductEntity productEntity = productDTO.dtoToEntity(userEntity);
        ProductEntity savedProduct = productRepository.save(productEntity);
        logger.info("상품 등록 완료!");
        return ProductDTO.entityToDto(savedProduct);
    }

    // 상품 조회
    @Override
    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow();
        logger.info("아이디 " + id + "번 상품 조회 성공!");
        return ProductDTO.entityToDto(productEntity);
    }
}

