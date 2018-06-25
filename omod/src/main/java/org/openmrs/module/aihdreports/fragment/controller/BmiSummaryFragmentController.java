package org.openmrs.module.aihdreports.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BmiSummaryFragmentController {

    public void controller(FragmentModel model) {
        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        //get a collection of all patients
        List<Patient> allPatients = Context.getPatientService().getAllPatients();
        List<Integer> cohort = new ArrayList<>();
        //loop through all and get their patient ids
        if(allPatients.size() > 0) {
            for (Patient patient : allPatients) {
                cohort.add(patient.getPatientId());
            }
        }
        //exclude dead patients
        Set<Integer> alivePatients = Filters.alive(cohort, context);
        Set<Integer> male = Filters.male(alivePatients, context);
        Set<Integer> female = Filters.female(alivePatients, context);
        //declare concepts
        Concept weight = Dictionary.getConcept(Dictionary.WEIGHT);
        Concept height = Dictionary.getConcept(Dictionary.HEIGHT);

        model.addAttribute("belowOrEqualTo18M", getBmi(weight, height, male, context, 0.0, 18.0));
        model.addAttribute("over18Below25M", getBmi(weight, height, male, context, 18.5, 24.9));
        model.addAttribute("twenty5Below30M", getBmi(weight, height, male, context, 25.0, 29.9));
        model.addAttribute("over30M", getBmi(weight, height, male, context, 30.0, 60.0));

        model.addAttribute("belowOrEqualTo18F", getBmi(weight, height, female, context, 0.0, 18.0));
        model.addAttribute("over18Below25F", getBmi(weight, height, female, context, 18.5, 24.9));
        model.addAttribute("twenty5Below30F", getBmi(weight, height, female, context, 25.0, 29.9));
        model.addAttribute("over30F", getBmi(weight, height, female, context, 30.0, 60.0));
    }
    private Integer getBmi(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context, double minBmi, double maxBmi){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap weightMap = Calculations.lastObs(q1, cohort, context);
        CalculationResultMap heightMap = Calculations.lastObs(q2, cohort, context);

        for(Integer pId: cohort){
            Double weightObs = EmrCalculationUtils.numericObsResultForPatient(weightMap, pId);
            Double heightObs = EmrCalculationUtils.numericObsResultForPatient(heightMap, pId);
            if(weightObs != null && heightObs !=null){
                //calculate the BMI here
                Double bmi = calculateBmi(weightObs, heightObs);
                if(bmi >= minBmi && bmi <= maxBmi) {
                    allSet.add(pId);
                }
            }

        }
        return allSet.size();
    }

    private Double calculateBmi(Double w, Double h){
        Double bmi = 0.0;

        if(w != null && h != null){
            double convertedHeihgt = h/100;
            double productHeight = convertedHeihgt * convertedHeihgt;
            bmi = w/productHeight;
        }

        return bmi;
    }
}
