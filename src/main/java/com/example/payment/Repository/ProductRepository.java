package com.example.payment.Repository;

import com.example.payment.DTO.ProductDTO;
import com.example.payment.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
