package com.StatementProcessing.dataProcessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StatementProcessing.model.AdditonalStatementResponse;
import com.StatementProcessing.model.MapData;
import com.StatementProcessing.model.StatementResponse;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.*;

@Service
@Data
public class CashFlowProcessing {

    @Autowired
    private MapData mapData;

    @Autowired
    private AdditonalStatementResponse additonalStatementResponse;

    @Autowired
    private MillionConvert millionConvert;

    public Map<String, Double> getCAGRData(){
        Set<String> dataMapKeySet = mapData.getDataMap().keySet();
        Map<String, Double> CagrMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(dataMapKeySet);

        List<String> finCagr = new ArrayList<>(List.of("Revenue","Income","Cfo","Assets","Liability","Ebit"));
        Method[] statementMethods = StatementResponse.class.getDeclaredMethods();

        for(Method method: statementMethods){
            String statementValue = method.getName();
            try{
                if(statementValue.contains("get")){
                    boolean matchesFinCagr = finCagr.stream().anyMatch(statementValue::contains);
                        if(matchesFinCagr){
                            Object lastYearObject = map.get(dataMapKey.get(0));
                            Object firstYearObject = map.get(dataMapKey.get(dataMapKeySet.size() - 1));

                            Method currenMethod = firstYearObject.getClass().getMethod(statementValue);

                            String firstYearValue = (String)currenMethod.invoke(firstYearObject);
                            String lastYearValue = (String)currenMethod.invoke(lastYearObject);
                            statementValue = statementValue.replace("get", "") + " CAGR";
                            getCompundedStatements(dataMapKeySet.size(), firstYearValue, lastYearValue, statementValue, CagrMap);
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();  // Handle possible reflection exceptions
            }
        }
        return CagrMap;
    }

    public Map<String, Double> CfoToNPData(){
        Set<String> yearMap = mapData.getDataMap().keySet();
        Map<String, Double> CfoToNPMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(yearMap);

        for(String year: dataMapKey){
            Double income = Double.parseDouble(map.get(year).getIncome());
            Double cfo = Double.parseDouble(map.get(year).getCfo());
            Double CfoNPRatio = cfo/income;
            CfoToNPMap.put(year, CfoNPRatio);
        }

        return CfoToNPMap;
    }

    private void getCompundedStatements(int size, String firstYearValue, String lastYearValue, String statementValue, Map<String, Double> StatemnetDataMap){
        Double firstYear = millionConvert.numberToMillion(firstYearValue);
        Double lastYear = millionConvert.numberToMillion(lastYearValue);
        Double cagr;

        if(firstYear>0 && lastYear>0){
            cagr = Math.pow(lastYear/firstYear,1.0/size)-1;
        }else if(firstYear<0 && lastYear<0){
            cagr = Math.pow(Math.abs(lastYear)/Math.abs(firstYear),1.0/size)-1;
        }else if(firstYear>0 && lastYear<0){
            cagr = -1*(Math.pow(Math.abs(lastYear)+2*firstYear/firstYear,1.0/size)-1);
        }else{
            cagr = .0;
        }
        StatemnetDataMap.put(statementValue, cagr*100);
    }

    public Map<String, Double> currentEVEbitda(){
        Map<String, Double> evEbitdaMap = new HashMap<>();
        Double ebitda = millionConvert.numberToMillion(additonalStatementResponse.getAdditionalIndicators().getEbitda());
        Double ev = millionConvert.numberToMillion(additonalStatementResponse.getAdditionalIndicators().getEnterpriseValue());
        evEbitdaMap.put("currentEVEbitda", ev/ebitda);
        return evEbitdaMap;
    }

    public Map<String, Double> interestCoverageRatio(){
        Set<String> yearMap = mapData.getDataMap().keySet();
        Map<String, Double> interestCoverageMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(yearMap);

        for(String year: dataMapKey){
            Double ebitda = Double.parseDouble(map.get(year).getEbitda());
            Double interest = Double.parseDouble(map.get(year).getInterest());
            Double interestCoverageRatio = ebitda/interest;
            interestCoverageMap.put(year, interestCoverageRatio);
        }

        return interestCoverageMap;
    }

    public Map<String, Double> returnOnAsset(){
        Set<String> yearMap = mapData.getDataMap().keySet();
        Map<String, Double> returnOnAssetMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(yearMap);

        for(String year: dataMapKey){
            Double netIncome = Double.parseDouble(map.get(year).getIncome());
            Double totalAssets = Double.parseDouble(map.get(year).getAssets());
            Double ROA = netIncome/totalAssets;
            returnOnAssetMap.put(year, ROA);
        }

        return returnOnAssetMap;
    }

    public Map<String, Double> returnOnEquity(){
        Set<String> yearMap = mapData.getDataMap().keySet();
        Map<String, Double> returnOnEquityMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(yearMap);

        for(String year: dataMapKey){
            Double netIncome = Double.parseDouble(map.get(year).getIncome());
            Double totalAssets = Double.parseDouble(map.get(year).getAssets());
            Double totalLiability = Double.parseDouble(map.get(year).getLiability());
            Double shareHolderEquity = totalAssets - totalLiability;
            Double ROE = netIncome/shareHolderEquity;
            returnOnEquityMap.put(year, ROE);
        }

        return returnOnEquityMap;
    }

    public Map<String, Double> returnOnCapitalEmployed(){
        Set<String> yearMap = mapData.getDataMap().keySet();
        Map<String, Double> returnOnCapitalEmployedMap = new HashMap<>();

        Map<String, StatementResponse> map = mapData.getDataMap();
        List<String> dataMapKey = new ArrayList<String>(yearMap);

        for(String year: dataMapKey){
            Double ebit = Double.parseDouble(map.get(year).getEbit());
            Double totalAssets = Double.parseDouble(map.get(year).getAssets());
            Double totalLiability = Double.parseDouble(map.get(year).getLiability());
            Double shareHolderEquity = totalAssets - totalLiability;
            Double ROCE = ebit/shareHolderEquity;
            returnOnCapitalEmployedMap.put(year, ROCE);
        }

        return returnOnCapitalEmployedMap;
    }

}
