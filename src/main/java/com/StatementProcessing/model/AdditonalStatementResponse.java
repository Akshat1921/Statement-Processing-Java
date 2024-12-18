package com.StatementProcessing.model;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.*;

@Getter
@Setter
@Service
public class AdditonalStatementResponse {
    private AdditionalIndicators additionalIndicators;
    private DividendData dividendData;
    private PriceData priceData;
    private RiskData riskData;
    private ShareData shareData;
    private VolumeData volumeData;
}
