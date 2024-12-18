package com.StatementProcessing.model;

import org.springframework.stereotype.Service;

import lombok.*;

@Getter
@Setter
@Service
public class StatementResponse {
    private String income;
    private String cashFlow;
    private String revenue;
    private String capex;
    private String interest;
    private String ebitda;
    private String cfo;
    private String ebit;
    private String assets;
    private String liability;
}
