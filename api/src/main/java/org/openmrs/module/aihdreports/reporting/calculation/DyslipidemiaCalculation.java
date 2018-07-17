package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class DyslipidemiaCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap hdlMap = Calculations.lastObs(Dictionary.getConcept(Dictionary.HDL), cohort, context);
        CalculationResultMap ldlMap = Calculations.lastObs(Dictionary.getConcept(Dictionary.LDL), cohort, context);
        CalculationResultMap tgMap = Calculations.lastObs(Dictionary.getConcept(Dictionary.TG), cohort, context);
        for(Integer ptId: cohort){
            boolean isDyslipidemia = false;
            Double hdlValue = EmrCalculationUtils.numericObsResultForPatient(hdlMap, ptId);
            Double ldlValue = EmrCalculationUtils.numericObsResultForPatient(ldlMap, ptId);
            Double tgValue = EmrCalculationUtils.numericObsResultForPatient(tgMap, ptId);
            if(hdlValue != null && (hdlValue < 0.7 || hdlValue > 1.9)){
                isDyslipidemia = true;
            }

            if(ldlValue != null && ldlValue > 3.4){
                isDyslipidemia = true;
            }
            if(tgValue != null && (tgValue < 0.0 || tgValue > 5.7)){
                isDyslipidemia = true;
            }

            ret.put(ptId, new BooleanResult(isDyslipidemia, this));
        }

        return ret;
    }
}
