package com.saurabh.frauddetection.controller;

import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.service.ITransactionService;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction API",
        description = "Fraud detection transaction endpoints")
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(
            summary = "Create transaction",
            description = "Evaluates transaction for fraud and returns decision"
    )
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request)
    {
        TransactionResponse response = transactionService.saveTransaction(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get transaction",
            description = "Returns valid transaction on the basis of the transactionId, provided"
    )
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Transaction> get(@PathVariable String transactionId)
    {
        Transaction response = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(response);
    }
}
