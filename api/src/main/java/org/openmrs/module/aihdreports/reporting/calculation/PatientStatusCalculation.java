package org.openmrs.module.aihdreports.reporting.calculation;

import java.util.Collection;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

public class PatientStatusCalculation extends AbstractPatientCalculation{

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context){
        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap reasonsForDiscontinuation = Calculations.lastObs(Dictionary.getConcept(Dictionary.DISCONTINUE_REASON), cohort, context);

        for(Integer pid: cohort){
            String value = "";

            Concept codedValue = EmrCalculationUtils.codedObsResultForPatient(reasonsForDiscontinuation, pid);

            if (codedValue != null) {
                if(codedValue.equals(Dictionary.getConcept(Dictionary.TRANSFER_OUT))){
                    value = "Transfer Out";
                }
                else {
                    
                }
            } else {
                
            }

            ret.put(pid, new SimpleResult(value, this));
        }

        return ret;
    }

    
}