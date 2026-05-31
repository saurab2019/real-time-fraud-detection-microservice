package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;

public interface IDecisionService {
    public Decision determineDecision(int score);
}
