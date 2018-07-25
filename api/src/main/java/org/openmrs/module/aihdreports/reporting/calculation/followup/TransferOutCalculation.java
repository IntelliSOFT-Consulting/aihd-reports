package org.openmrs.module.aihdreports.reporting.calculation.followup;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.BooleanResult;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.Filters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TransferOutCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
                                         PatientCalculationContext context) {
        Concept discontinueQuestion = Dictionary.getConcept(Dictionary.DISCONTINUE_REASON);
        Concept transferOut = Dictionary.getConcept(Dictionary.TRANSFER_OUT);
        Set<Integer> alive = Filters.alive(cohort, context);

        CalculationResultMap discontinueObs = Calculations.lastObs(discontinueQuestion, alive, context);

        CalculationResultMap ret = new CalculationResultMap();
        for(int ptId : cohort){
            boolean isTransferredOut = false;
            Obs discoReason = EmrCalculationUtils.obsResultForPatient(discontinueObs, ptId);
            if(discoReason != null && discoReason.getValueCoded().equals(transferOut)){
                isTransferredOut = true;
            }
            ret.put(ptId, new BooleanResult(isTransferredOut, this));

        }
        return ret;
    }
}
