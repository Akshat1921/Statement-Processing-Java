package com.StatementProcessing.model;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class MapData {
    private Map<String, StatementResponse> dataMap;
    private AdditonalStatementResponse additonalStatementResponse;
}
