package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class ValueTextObsCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        Concept concept = (Concept) params.get("concept");
        CalculationResultMap map = Calculations.lastObs(Dictionary.getConcept(Dictionary.MEDICATION_ORDERED), cohort, context);
        for(Integer pId:cohort){
            boolean hasValue = false;
            Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
            if(obs != null && obs.getValueText() != null && concept != null && obs.getConcept().equals(concept)){
                hasValue = true;
            }
            ret.put(pId, new BooleanResult(hasValue, this));
        }
        return ret;

    }
}
