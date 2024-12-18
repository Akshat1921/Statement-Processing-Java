package com.StatementProcessing.controller;

import org.springframework.web.bind.annotation.RestController;

import com.StatementProcessing.dataProcessing.CashFlowProcessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;


@RestController
public class Controller {

    @Autowired
    private CashFlowProcessing cashFlowProcessing;

    @GetMapping("/processed-data")
    ResponseEntity <Map<String, Map<String, Double>>> getProcessedData(){
        Map<String, Map<String, Double>> responseMap = new HashMap<>();
        responseMap.put("EVEbitda", cashFlowProcessing.currentEVEbitda());
        responseMap.put("CfoNp", cashFlowProcessing.CfoToNPData());
        responseMap.put("CAGR", cashFlowProcessing.getCAGRData());
        responseMap.put("InterestCoverageData", cashFlowProcessing.interestCoverageRatio());
        responseMap.put("ROA", cashFlowProcessing.returnOnAsset());
        responseMap.put("ROE", cashFlowProcessing.returnOnEquity());
        responseMap.put("ROCE", cashFlowProcessing.returnOnCapitalEmployed());
        return ResponseEntity.ok(responseMap);
    }
}
