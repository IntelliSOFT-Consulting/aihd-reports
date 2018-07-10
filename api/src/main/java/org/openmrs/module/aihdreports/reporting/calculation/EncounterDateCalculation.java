package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Encounter;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class EncounterDateCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap lastFollowUpEncounter = Calculations.lastEncounter(CoreUtils.getEncounterType(Metadata.EncounterType.DM_FOLLOWUP), cohort, context);
        CalculationResultMap lastInitialEncounter = Calculations.lastEncounter(CoreUtils.getEncounterType(Metadata.EncounterType.DM_INITIAL), cohort, context);
        for(Integer ptId: cohort){
            Date encounterDate = null;
            Encounter initialEncounter = EmrCalculationUtils.encounterResultForPatient(lastInitialEncounter, ptId);
            Encounter followUpEncounter = EmrCalculationUtils.encounterResultForPatient(lastFollowUpEncounter, ptId);

            if(followUpEncounter != null){
                encounterDate = followUpEncounter.getEncounterDatetime();
            }
            else if(initialEncounter != null) {

                encounterDate = initialEncounter.getEncounterDatetime();
            }

            ret.put(ptId, new SimpleResult(encounterDate, this));
        }
        return ret;
    }
}
