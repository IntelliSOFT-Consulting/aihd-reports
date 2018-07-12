package org.openmrs.module.aihdreports.reporting.calculation.followup;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.utils.CalculationUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class PatientStatusCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        Set<Integer> transferredOutPatients = CalculationUtils.patientsThatPass(calculate(new TransferOutCalculation(), cohort, context));
        Set<Integer> ltfuPatients = CalculationUtils.patientsThatPass(calculate(new LostToFollowUpCalcultion(), cohort, context));
        Set<Integer> deadPatients = CalculationUtils.patientsThatPass(calculate(new DeceasedCalculation(), cohort, context));
        for(Integer ptId: cohort){
            String value = "";
            if(deadPatients != null && deadPatients.contains(ptId)){
                value = "c";
            }
            else if(transferredOutPatients != null && transferredOutPatients.contains(ptId)){
                value = "a";
            }
            else if(ltfuPatients != null && ltfuPatients.contains(ptId)){
                value = "b";
            }
            ret.put(ptId, new SimpleResult(value, this));
        }
        return ret;
    }
}
