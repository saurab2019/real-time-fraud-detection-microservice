package com.saurabh.frauddetection.rules;

import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;

public interface IFraudRule {

    RuleResult evaluate(Transaction transaction);

}
