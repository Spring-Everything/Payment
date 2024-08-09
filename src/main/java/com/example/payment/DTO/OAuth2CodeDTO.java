package com.example.payment.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OAuth2CodeDTO {
    private String code;
}
