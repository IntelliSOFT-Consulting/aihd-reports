package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class BmiCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap height = Calculations.lastObs(Dictionary.getConcept(Dictionary.HEIGHT), cohort, context);
        CalculationResultMap weight = Calculations.lastObs(Dictionary.getConcept(Dictionary.WEIGHT), cohort, context);
        for(Integer ptId: cohort){
            Double bmi = null;
            Double heightValue = EmrCalculationUtils.numericObsResultForPatient(height, ptId);
            Double weightValue = EmrCalculationUtils.numericObsResultForPatient(weight, ptId);

            if(heightValue != null && weightValue != null){
                //convert height into meters
                double heightInMeters = heightValue/100;
                bmi = weightValue/(heightInMeters * heightInMeters);
            }
            ret.put(ptId, new SimpleResult(bmi, this));
        }
        return ret;
    }
}
