package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.repository.IFraudResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FraudResultServiceTest {

    @Mock
    private IFraudResultRepository fraudResultRepository;

    @InjectMocks
    private FraudResultService fraudResultService;

    @Test
    void shouldSaveFraudResult() {

        FraudResult savedResult = new FraudResult();
        when(fraudResultRepository.save(any(FraudResult.class))).thenReturn(
                savedResult
        );

        fraudResultService.saveResult(
                "TXN-001",
                40,
                Decision.OTP_REQUIRED);

        ArgumentCaptor<FraudResult>  captor = ArgumentCaptor.forClass(FraudResult.class);

        verify(fraudResultRepository).save(captor.capture());

        FraudResult fraudResult = captor.getValue();
        assertEquals("TXN-001", fraudResult.getTransactionId());
        assertEquals(40, fraudResult.getFraudScore());
        assertEquals(Decision.OTP_REQUIRED, fraudResult.getDecision());
    }

}
