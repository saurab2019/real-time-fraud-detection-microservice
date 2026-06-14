package com.saurabh.frauddetection.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionRequest {
    @NotNull(message = "userId is required")
    private Long userId;
    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be > 0")
    private BigDecimal amount;
    @NotBlank(message = "country is required")
    private String country;
    @NotBlank(message = "merchantId is required")
    private String merchantId;
}
