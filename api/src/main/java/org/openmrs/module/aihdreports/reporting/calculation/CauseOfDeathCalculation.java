package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.utils.Filters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CauseOfDeathCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        Concept concept = (Concept) params.get("concept");
        Set<Integer> alive = Filters.alive(cohort, context);
        //loop through all the patients who are dead and check the reason for death
        for(Integer ptId: cohort){
            boolean isDeadWithRequiredReason = false;
            if(alive != null && !(alive.contains(ptId))){
                Person person = Context.getPersonService().getPerson(ptId);
                if(person != null && person.getCauseOfDeath() != null && person.getCauseOfDeath().equals(concept)){
                    isDeadWithRequiredReason = true;
                }
            }
            ret.put(ptId, new BooleanResult(isDeadWithRequiredReason, this));
        }
        return ret;
    }
}
