package org.openmrs.module.aihdreports.reporting.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.Map;

public class DiagnosisCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap diabeticPatients = calculate(new DiabetesTypesCalculation(), cohort, context);
        CalculationResultMap hypertensionPatients = calculate(new HypertensionTypeCalculation(), cohort, context);
        for(Integer ptId:cohort){
            String value = "";
            SimpleResult diabeticResults = (SimpleResult) diabeticPatients.get(ptId);
            SimpleResult hypertensionResults = (SimpleResult) hypertensionPatients.get(ptId);
            if(diabeticResults != null && hypertensionResults != null){
                if(StringUtils.isNotEmpty(diabeticResults.getValue().toString()) && StringUtils.isNotEmpty(hypertensionResults.getValue().toString())) {
                    value = diabeticResults.getValue() + "," + hypertensionResults.getValue();
                }
            }
            else if(diabeticResults != null && StringUtils.isNotEmpty(diabeticResults.getValue().toString())){
                value = diabeticResults.getValue().toString();
            }
            else if(hypertensionResults != null && StringUtils.isNotEmpty(hypertensionResults.getValue().toString())){
                value = hypertensionResults.getValue().toString();
            }
            ret.put(ptId, new SimpleResult(value, this));
        }
        return ret;
    }
}