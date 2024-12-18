package com.StatementProcessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import com.StatementProcessing.dataProcessing.CashFlowProcessing;
import com.StatementProcessing.model.AdditionalIndicators;
import com.StatementProcessing.model.AdditonalStatementResponse;
import com.StatementProcessing.model.MapData;
import com.StatementProcessing.model.StatementResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class StatementProcessingApplication implements CommandLineRunner{

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MapData mapData;

	@Autowired
	private CashFlowProcessing cashFlowProcessing;

	@Autowired
	private AdditonalStatementResponse additonalStatementResponse;

	public static void main(String[] args) {
		SpringApplication.run(StatementProcessingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		String url = "http://localhost:8080/data";
		String url1 = "http://localhost:8080/data/other";
		ResponseEntity<Map<String, StatementResponse>> responseEntity = restTemplate.exchange(
			url,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Map<String, StatementResponse>>() {}
    	);

		ResponseEntity<Map<String, Object>> responseEntity1 = restTemplate.exchange(
			url1,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Map<String, Object>>() {}
		);
		Map<String, Object> res1 = responseEntity1.getBody();
		ObjectMapper mapper = new ObjectMapper();
		AdditionalIndicators additionalIndicatorsData = mapper.convertValue(res1.get("additionalData"), AdditionalIndicators.class);
		
		additonalStatementResponse.setAdditionalIndicators(additionalIndicatorsData);
		
		Map<String, StatementResponse> response = responseEntity.getBody();

		mapData.setDataMap(response);
		
	}

}
