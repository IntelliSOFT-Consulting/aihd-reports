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
import java.util.List;
import java.util.Map;

public class TreatmentCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap map = Calculations.allObs(Dictionary.getConcept(Dictionary.MEDICATION_ORDERED), cohort, context);
        for(Integer ptId:cohort){
            String treatment = "";
            ListResult listResult = (ListResult) map.get(ptId);
            if(listResult != null) {
                List<Obs> loads = CalculationUtils.extractResultValues(listResult);
                if(loads.size() > 0){
                    for(Obs obs:loads){
                        if(Metadata.ListsofConcepts.dietAndExercise().contains(obs.getValueCoded())){
                            treatment = "a";
                        }

                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.oglas().contains(obs.getValueCoded())){
                            treatment += ",b";
                        }
                        else if(StringUtils.isEmpty(treatment) && Metadata.ListsofConcepts.oglas().contains(obs.getValueCoded())){
                            treatment = "b";
                        }

                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.insulin().contains(obs.getValueCoded())){
                            treatment += ",c";
                        }
                        else if(StringUtils.isEmpty(treatment) && Metadata.ListsofConcepts.insulin().contains(obs.getValueCoded())){
                            treatment = "c";
                        }

                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.hypertensive().contains(obs.getValueCoded())){
                            treatment += ",d";
                        }
                        else if(StringUtils.isEmpty(treatment) && Metadata.ListsofConcepts.hypertensive().contains(obs.getValueCoded())){
                            treatment = "d";
                        }

                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.herbal().contains(obs.getValueCoded())){
                            treatment += ",e";
                        }
                        else if(StringUtils.isEmpty(treatment) && Metadata.ListsofConcepts.herbal().contains(obs.getValueCoded())){
                            treatment = "e";
                        }

                        if(StringUtils.isNotEmpty(treatment) && Metadata.ListsofConcepts.other().contains(obs.getValueCoded())){
                            treatment += ",f";
                        }
                        else if(StringUtils.isEmpty(treatment) && Metadata.ListsofConcepts.other().contains(obs.getValueCoded())){
                            treatment += "f";
                        }
                    }
                }
            }
            ret.put(ptId, new SimpleResult(treatment, this));
        }

        return ret;
    }
}
