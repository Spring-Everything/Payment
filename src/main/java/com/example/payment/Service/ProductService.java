package com.example.payment.Service;

import com.example.payment.DTO.ProductDTO;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProductById(Long id);
}
