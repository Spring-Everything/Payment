package com.example.payment.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
