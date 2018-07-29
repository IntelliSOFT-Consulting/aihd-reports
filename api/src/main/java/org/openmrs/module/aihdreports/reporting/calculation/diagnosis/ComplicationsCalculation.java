package org.openmrs.module.aihdreports.reporting.calculation.diagnosis;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.DyslipidemiaCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.calculation.ObesityCalculation;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CalculationUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComplicationsCalculation  extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap hiv = Calculations.lastObs(Dictionary.getConcept(Dictionary.HIV_STATUS), cohort, context);
        CalculationResultMap tb = Calculations.lastObs(Dictionary.getConcept(Dictionary.TB_STATUS), cohort, context);
        CalculationResultMap complications = Calculations.allObs(Dictionary.getConcept(Dictionary.PROBLEM_ADDED), cohort, context);
        Set<Integer> obese = CalculationUtils.patientsThatPass(calculate(new ObesityCalculation(), cohort, context));
        Set<Integer> dyslipidemia = CalculationUtils.patientsThatPass(calculate(new DyslipidemiaCalculation(), cohort, context));
        for(Integer ptId: cohort){
            Set<String> value = new HashSet<>();
            StringBuilder computedValue = new StringBuilder();
            Concept hivStatus = EmrCalculationUtils.codedObsResultForPatient(hiv, ptId);
            Concept tbStatus = EmrCalculationUtils.codedObsResultForPatient(tb, ptId);
            ListResult listResult = (ListResult) complications.get(ptId);
            if(listResult != null) {
                List<Obs> loads = CalculationUtils.extractResultValues(listResult);
                if(loads.size() > 0){
                    for(Obs obs:loads){
                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.VISUAL_IMPAIRMENT))){
                            value.add("a");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Neuropathy))){
                            value.add("b");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.KIDNEY_FAILURE))){
                            value.add("c");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.STROKE))){
                            value.add("d");
                        }


                        if(Metadata.ListsofConcepts.heartDisease().contains(obs.getValueCoded())){
                            value.add("f");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Diabetic_foot))){
                            value.add("h");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ERECTILE_DISFUNCTION))){
                            value.add("i");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.GASTROPATHY))){
                            value.add("j");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.CATARACTS))){
                            value.add("k");
                        }

                        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DENTAL_COMPLICATIONS))){
                            value.add("l");
                        }

                        if(dyslipidemia != null && dyslipidemia.contains(ptId)){
                            value.add("m");
                        }

                        if(obese != null && obese.contains(ptId)){
                            value.add("n");
                        }

                    }
                }
            }
            if(hivStatus != null && hivStatus.equals(Dictionary.getConcept(Dictionary.HIV_POSTIVE))){
                value.add("o");
            }

            if(tbStatus != null && tbStatus.equals(Dictionary.getConcept(Dictionary.ON_TREATMENT))){
                value.add("p");
            }
            if(value.size() > 0){
                for(String s: value){
                    computedValue.append(s);
                }
            }

            ret.put(ptId, new SimpleResult(computedValue.toString().replaceAll(".(?!$)", "$0 "), this));

        }

        return ret;
    }
}