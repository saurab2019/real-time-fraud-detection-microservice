package com.saurabh.frauddetection.controller;

import com.saurabh.frauddetection.service.ITransactionService;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Object> create(@Valid @RequestBody TransactionRequest request)
    {
        TransactionResponse response = transactionService.saveTransaction(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
