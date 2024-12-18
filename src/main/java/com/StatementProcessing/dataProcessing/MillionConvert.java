package com.StatementProcessing.dataProcessing;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class MillionConvert {
    double numberToMillion(String num){
        double inMills = Double.parseDouble(num);
        return inMills/1000000;
    }
}
