package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Encounter;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;

import java.util.Collection;
import java.util.Map;

public class InitialReturnVisitCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap lastFollowUpEncounter = Calculations.lastEncounter(CoreUtils.getEncounterType(Metadata.EncounterType.DM_FOLLOWUP), cohort, context);
        CalculationResultMap lastInitialEncounter = Calculations.lastEncounter(CoreUtils.getEncounterType(Metadata.EncounterType.DM_INITIAL), cohort, context);
        for(Integer ptId: cohort){
            String firstOrRevisit = "";
            Encounter initialEncounter = EmrCalculationUtils.encounterResultForPatient(lastInitialEncounter, ptId);
            Encounter followUpEncounter = EmrCalculationUtils.encounterResultForPatient(lastFollowUpEncounter, ptId);

            if(followUpEncounter != null){
                firstOrRevisit = "R";
            }
            else if(initialEncounter != null) {

                firstOrRevisit = "F";
            }

            ret.put(ptId, new SimpleResult(firstOrRevisit, this));
        }
        return ret;
    }
}
