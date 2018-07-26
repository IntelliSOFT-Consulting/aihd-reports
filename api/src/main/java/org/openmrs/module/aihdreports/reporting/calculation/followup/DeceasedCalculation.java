package org.openmrs.module.aihdreports.reporting.calculation.followup;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.BooleanResult;
import org.openmrs.module.aihdreports.reporting.utils.Filters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DeceasedCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
        Set<Integer> alive = Filters.alive(cohort, context);
        CalculationResultMap ret = new CalculationResultMap();
        for (Integer ptId : cohort) {
            boolean dead = false;
            if(!(alive.contains(ptId))){
                dead = true;
            }
            ret.put(ptId, new BooleanResult(dead, this, context));
        }
        return ret;
    }
}
