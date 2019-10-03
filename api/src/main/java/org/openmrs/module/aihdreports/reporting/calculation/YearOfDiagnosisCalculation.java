package org.openmrs.module.aihdreports.reporting.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;

import java.util.Collection;
import java.util.Map;

public class YearOfDiagnosisCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context){
        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap diabeticYearOfDiagnosis = Calculations.lastObs(Dictionary.getConcept(Dictionary.DIABETIC_YEAR_OF_DIAGNOSIS), cohort, context);
        CalculationResultMap hypertensionYearOfDiagnosis = Calculations.lastObs(Dictionary.getConcept(Dictionary.HYPERTENSION_YEAR_OF_DIAGNOSIS), cohort, context);

        for(Integer pid: cohort){
            String value = "";

            Obs yearOfDiagnosisDiabeticObs = EmrCalculationUtils.obsResultForPatient(diabeticYearOfDiagnosis, pid);
            Obs yearOfDiagnosisHypertensionObs = EmrCalculationUtils.obsResultForPatient(hypertensionYearOfDiagnosis, pid);

            if(yearOfDiagnosisDiabeticObs != null && yearOfDiagnosisHypertensionObs != null && !yearOfDiagnosisDiabeticObs.getValueText().isEmpty() && !yearOfDiagnosisHypertensionObs.getValueText().isEmpty()){
               
                value = yearOfDiagnosisDiabeticObs.getValueText() + "," + yearOfDiagnosisHypertensionObs.getValueText();

            }
            else if (yearOfDiagnosisDiabeticObs != null  && !yearOfDiagnosisDiabeticObs.getValueText().isEmpty()){

                value = yearOfDiagnosisDiabeticObs.getValueText();

            }
            else if (yearOfDiagnosisHypertensionObs != null  && !yearOfDiagnosisHypertensionObs.getValueText().isEmpty()){

                value = yearOfDiagnosisHypertensionObs.getValueText();

            }
            else{

            }

            ret.put(pid, new SimpleResult(value, this));

        }

        return ret;

    }

    
}