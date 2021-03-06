package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class DiabetesTypesCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap diabeticTypes = Calculations.lastObs(Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC), cohort, context);
        CalculationResultMap diabetes = Calculations.lastObs(Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE), cohort, context);

        for(Integer ptId:cohort){
            String value = "";
            Concept result = EmrCalculationUtils.codedObsResultForPatient(diabeticTypes, ptId);
            Concept resultDiabetes = EmrCalculationUtils.codedObsResultForPatient(diabetes, ptId);
            if(result != null){
                if(result.equals(Dictionary.getConcept(Dictionary.TYPE_1_DIABETES))){
                    value = "a";
                }
                else if(result.equals(Dictionary.getConcept(Dictionary.TYPE_2_DIABETES))){
                    value = "b";
                }
                else if(result.equals(Dictionary.getConcept(Dictionary.GDM))){
                    value = "c";
                }
                else if(result.equals(Dictionary.getConcept(Dictionary.DIABETES_SECONDARY_TO_OTHER_CAUSES))){
                    value = "d";
                }
            }
            else if (resultDiabetes !=null) {
                value = "g";
            }
            ret.put(ptId, new SimpleResult(value, this));
        }
        return ret;
    }
}