package org.openmrs.module.aihdreports.reporting.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CalculationUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreatmentCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap map = Calculations.allObs(Dictionary.getConcept(Dictionary.MEDICATION_ORDERED), cohort, context);
        for(Integer ptId:cohort){
            Set<String> treat = new HashSet<>();
            StringBuilder treatment = new StringBuilder();
            ListResult listResult = (ListResult) map.get(ptId);
            if(listResult != null) {
                List<Obs> loads = CalculationUtils.extractResultValues(listResult);
                if(loads.size() > 0){
                    for(Obs obs:loads){
                        if(Metadata.ListsofConcepts.dietAndExercise().contains(obs.getValueCoded())){
                            treat.add("a");
                        }

                        if(Metadata.ListsofConcepts.oglas().contains(obs.getValueCoded())){
                            treat.add("b");
                        }

                        if(Metadata.ListsofConcepts.insulin().contains(obs.getValueCoded())){
                            treat.add("c");
                        }

                        if(Metadata.ListsofConcepts.hypertensive().contains(obs.getValueCoded())){
                            treat.add("d");
                        }


                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.herbal().contains(obs.getValueCoded())){
                            treat.add("e");
                        }

                        if(Metadata.ListsofConcepts.other().contains(obs.getValueCoded())){
                            treat.add("f");
                        }

                    }
                }
                if(treat.size() > 0){
                    for (String s:treat){
                        treatment.append(s);
                    }
                }
            }
            ret.put(ptId, new SimpleResult(treatment.toString().replaceAll(".(?!$)", "$0 "), this));
        }

        return ret;
    }
}