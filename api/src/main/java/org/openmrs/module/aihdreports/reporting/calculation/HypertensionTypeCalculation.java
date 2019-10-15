package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class HypertensionTypeCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap hypetensionTypes = Calculations.lastObs(Dictionary.getConcept(Dictionary.HYPETENSION_TYPE), cohort, context);
        CalculationResultMap HTN = Calculations.lastObs(Dictionary.getConcept(Dictionary.HTN), cohort, context);

        Concept mild = Dictionary.getConcept(Dictionary.MILD_HYPERTENSION);
        Concept moderate = Dictionary.getConcept(Dictionary.MODERATE_HYPERTENSION);
        Concept severe = Dictionary.getConcept(Dictionary.SEVERE_HYPERTENSION);
        Concept preeclampsia = Dictionary.getConcept(Dictionary.PREECLAMSIA);
        for(Integer ptId:cohort){
            String value = "";
            Concept result = EmrCalculationUtils.codedObsResultForPatient(hypetensionTypes, ptId);
            Concept resultHTN = EmrCalculationUtils.codedObsResultForPatient(HTN, ptId);
            if(result != null){
                if(result.equals(mild) || result.equals(moderate) || result.equals(severe)){
                    value = "e";
                }
                else if(result.equals(preeclampsia)){
                    value = "f";
                }
            }
            else if (resultHTN != null) {
                value = "e";
            }
            ret.put(ptId, new SimpleResult(value, this));
        }
        return ret;
    }
}