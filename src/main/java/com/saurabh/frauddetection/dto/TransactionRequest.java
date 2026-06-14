package com.saurabh.frauddetection.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionRequest {

    @Schema(example = "123")
    @NotNull(message = "userId is required")
    private Long userId;

    @Schema(example = "75000")
    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be > 0")
    private BigDecimal amount;

    @Schema(example = "IN")
    @NotBlank(message = "country is required")
    private String country;

    @Schema(example = "AMAZON")
    @NotBlank(message = "merchantId is required")
    private String merchantId;
}
