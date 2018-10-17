package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.CalculationUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FootCareOutcomeCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap footUlcerMap = Calculations.lastObs(Dictionary.getConcept(Dictionary.DIABETIC_FOOT), cohort, context);
        CalculationResultMap footAmputated = Calculations.lastObs(Dictionary.getConcept(Dictionary.FOOT_AMPUTATION), cohort, context);
        Set<Integer> lostToFollow = CalculationUtils.patientsThatPass(calculate(new LostToFollowUpCalcultion(), cohort, context));
        for(Integer ptId:cohort){
            String value = "";
            Concept footUlcerValue = EmrCalculationUtils.codedObsResultForPatient(footUlcerMap, ptId);
            Concept footAmputatedValue = EmrCalculationUtils.codedObsResultForPatient(footAmputated, ptId);
            if(footUlcerValue != null && footUlcerValue.equals(Dictionary.getConcept(Dictionary.RESSOLVED))){
                value = "a";
            }

            if(footAmputatedValue != null && footAmputatedValue.equals(Dictionary.getConcept(Dictionary.UNDER_TREATMENT))){
                value = "c";
            }

            if(lostToFollow != null && lostToFollow.contains(ptId)){
                value = "d";
            }
            ret.put(ptId, new SimpleResult(value, this));

        }
        return ret;
    }
}