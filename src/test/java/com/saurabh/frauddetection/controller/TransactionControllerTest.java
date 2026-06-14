package com.saurabh.frauddetection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.logging.IRequestLogger;
import com.saurabh.frauddetection.service.ITransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRequestLogger requestLogger;

    @MockitoBean
    private ITransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateTransaction() throws Exception
    {
        TransactionRequest request =
                TransactionRequest.builder()
                        .userId(123L)
                        .merchantId("M001")
                        .country("India")
                        .amount(new BigDecimal("5000"))
                        .build();

        TransactionResponse response =
                TransactionResponse.builder()
                        .transactionId("TXN-001")
                        .score(30)
                        .decision(Decision.APPROVED)
                        .build();

        when(transactionService.saveTransaction(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId")
                        .value("TXN-001"))
                .andExpect(jsonPath("$.score")
                        .value(30))
                .andExpect(jsonPath("$.decision")
                        .value("APPROVED"));

    }

    @Test
    void shouldReturnTransaction() throws Exception {
        Transaction transaction =
                Transaction.builder()
                        .transactionId("TXN-001")
                        .userId(123L)
                        .country("India")
                        .merchantId("M001")
                        .amount(new BigDecimal("5000"))
                        .build();

        when(transactionService.getTransaction("TXN-001"))
                .thenReturn(transaction);

        mockMvc.perform(
                get("/transactions/TXN-001")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId")
                        .value("TXN-001"));
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {

        TransactionRequest request = new TransactionRequest();

        mockMvc.perform(
                        post("/transactions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }
}
