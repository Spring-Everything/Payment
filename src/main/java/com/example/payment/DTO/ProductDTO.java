package com.example.payment.DTO;

import com.example.payment.Entity.ProductEntity;
import com.example.payment.Entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private int price;
    private Long userId;
    private UserDTO user;

    public static ProductDTO entityToDto(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getUser().getId(),
                UserDTO.entityToDto(productEntity.getUser())
        );
    }

    public ProductEntity dtoToEntity(UserEntity user){
        return new ProductEntity(id, name, price, user);
    }
}
