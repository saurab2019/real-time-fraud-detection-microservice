package com.saurabh.frauddetection.rules;

import com.saurabh.frauddetection.config.properties.GeoAnomalyProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.redis.GeoLocationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoAnomalyRule implements IFraudRule{

    private final GeoLocationService geoLocationService;
    private final GeoAnomalyProperties properties;

    @Override
    public RuleResult evaluate(Transaction transaction) {
        //get last country from redis
        String lastCountry = geoLocationService.getLastCountry(transaction.getUserId());
        String currentCountry = transaction.getCountry();
        boolean anomaly =
                lastCountry != null &&
                        !lastCountry.equalsIgnoreCase(currentCountry);

        geoLocationService.updateCountry(transaction.getUserId(), currentCountry);

        if (!anomaly) {
            return new RuleResult(
                    false,
                    0,
                    null,
                    "GeoAnomalyRule");
        }
        else
        {
            return new RuleResult(
                    true,
                    properties.score(),
                    String.format(
                            "Country changed from %s to %s",
                            lastCountry,
                            currentCountry),
                    "GeoAnomalyRule");
        }
    }
}
