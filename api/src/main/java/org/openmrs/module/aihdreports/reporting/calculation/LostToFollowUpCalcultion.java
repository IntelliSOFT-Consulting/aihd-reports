package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.Filters;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils.daysSince;

public class LostToFollowUpCalcultion extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        Set<Integer> alive = Filters.alive(cohort, context);
        CalculationResultMap lastReturnDateObss = Calculations.lastObs(Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE), alive, context);
        for (Integer ptId : cohort) {
            boolean lost = false;
            Date lastScheduledReturnDate = EmrCalculationUtils.datetimeObsResultForPatient(lastReturnDateObss, ptId);
            if(lastScheduledReturnDate != null && daysSince(lastScheduledReturnDate, context) > 90){
                lost = true;
            }
            ret.put(ptId, new BooleanResult(lost, this, context));
        }
        return ret;
    }
}