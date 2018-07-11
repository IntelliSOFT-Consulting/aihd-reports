package org.openmrs.module.aihdreports.reporting.calculation.diagnosis;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.AIHDReportUtil;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class YearOfDiagnosisCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap diabeticYearOfDiagnosis = Calculations.lastObs(Dictionary.getConcept(Dictionary.DIABETIC_YEAR_OF_DIAGNOSIS), cohort, context);
        CalculationResultMap hypertensionYearOfDiagnosis = Calculations.lastObs(Dictionary.getConcept(Dictionary.HYPERTENSION_YEAR_OF_DIAGNOSIS), cohort, context);
        for(Integer ptId:cohort){
            String dateValue = "";
            Date diabeticDate = EmrCalculationUtils.datetimeObsResultForPatient(diabeticYearOfDiagnosis, ptId);
            Date hypertensionDate = EmrCalculationUtils.datetimeObsResultForPatient(hypertensionYearOfDiagnosis, ptId);
            if(diabeticDate != null && hypertensionDate != null){
                dateValue = AIHDReportUtil.formatDates(diabeticDate)+","+AIHDReportUtil.formatDates(hypertensionDate);
            }
            else if(diabeticDate != null){
                dateValue = AIHDReportUtil.formatDates(diabeticDate);
            }
            else if(hypertensionDate != null){
                dateValue = AIHDReportUtil.formatDates(hypertensionDate);
            }
            ret.put(ptId, new SimpleResult(dateValue, this));
        }

        return ret;
    }
}
