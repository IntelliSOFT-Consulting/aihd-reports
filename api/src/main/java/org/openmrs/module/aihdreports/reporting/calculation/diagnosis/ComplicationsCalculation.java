package org.openmrs.module.aihdreports.reporting.calculation.diagnosis;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CalculationUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ComplicationsCalculation  extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap hiv = Calculations.lastObs(Dictionary.getConcept(Dictionary.HIV_STATUS), cohort, context);
        CalculationResultMap tb = Calculations.lastObs(Dictionary.getConcept(Dictionary.TB_STATUS), cohort, context);
        CalculationResultMap complications = Calculations.allObs(Dictionary.getConcept(Dictionary.PROBLEM_ADDED), cohort, context);
        for(Integer ptId: cohort){
            String value = "";
            Concept hivStatus = EmrCalculationUtils.codedObsResultForPatient(hiv, ptId);
            Concept tbStatus = EmrCalculationUtils.codedObsResultForPatient(tb, ptId);
            ListResult listResult = (ListResult) complications.get(ptId);
            if(listResult != null) {
                List<Obs> loads = CalculationUtils.extractResultValues(listResult);
                if(loads.size() > 0){
                    for(Obs obs:loads){
                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.VISUAL_IMPAIRMENT))){
                            value = "a";
                        }

                        if(StringUtils.isNotEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Neuropathy))){
                            value += ",b";
                        }
                        else if(StringUtils.isEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Neuropathy))){
                            value = "b";
                        }

                        if(StringUtils.isNotEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.KIDNEY_FAILURE))){
                            value += ",c";
                        }
                        else if(StringUtils.isEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.KIDNEY_FAILURE))){
                            value = "c";
                        }

                        if(StringUtils.isNotEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.STROKE))){
                            value += ",d";
                        }
                        else if(StringUtils.isEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.STROKE))){
                            value = "d";
                        }

                        if(StringUtils.isNotEmpty(value) && Metadata.ListsofConcepts.heartDisease().contains(obs.getValueCoded())){
                            value += ",f";
                        }
                        else if(StringUtils.isEmpty(value) && Metadata.ListsofConcepts.heartDisease().contains(obs.getValueCoded())){
                            value = "f";
                        }

                        if(StringUtils.isNotEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Diabetic_foot))){
                            value += ",h";
                        }
                        else if(StringUtils.isEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Diabetic_foot))){
                            value = "h";
                        }

                        if(StringUtils.isNotEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ERECTILE_DISFUNCTION))){
                            value += ",i";
                        }
                        else if(StringUtils.isEmpty(value) && obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ERECTILE_DISFUNCTION))){
                            value = "i";
                        }
                    }
                }
            }
            if(StringUtils.isNotEmpty(value) && hivStatus.equals(Dictionary.getConcept(Dictionary.HIV_POSTIVE))){
                value += ",o";
            }
            else if(StringUtils.isEmpty(value) && hivStatus.equals(Dictionary.getConcept(Dictionary.HIV_POSTIVE))){
                value = "o";
            }

            if(StringUtils.isNotEmpty(value) && tbStatus.equals(Dictionary.getConcept(Dictionary.ON_TREATMENT))){
                value += ",o";
            }
            else if(StringUtils.isEmpty(value) && tbStatus.equals(Dictionary.getConcept(Dictionary.ON_TREATMENT))){
                value = "o";
            }

            ret.put(ptId, new SimpleResult(value, this));

        }

        return ret;
    }
}