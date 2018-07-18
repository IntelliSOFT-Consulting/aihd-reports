package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.Map;

public class ObesityCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap bmi = calculate(new BmiCalculation(),cohort, context);
        for(Integer ptId: cohort){
            boolean isObese = false;
            SimpleResult bmiResults = (SimpleResult) bmi.get(ptId);
            if(bmiResults != null && bmiResults.getValue() != null){
                Double result = (Double) bmiResults.getValue();
                if(result != null && result > 29.0){
                    isObese = true;
                }
            }
            ret.put(ptId, new BooleanResult(isObese, this));
        }
        return ret;
    }
}
