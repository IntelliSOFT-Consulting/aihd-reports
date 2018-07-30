package org.openmrs.module.aihdreports.fragment.controller;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreatmentFragmentController {

    public void controller(FragmentModel model) {

        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        //get a collection of all patients
        List<Patient> allPatients = Context.getPatientService().getAllPatients();
        List<Integer> cohort = new ArrayList<>();
        //loop through all and get their patient ids
        if (allPatients.size() > 0) {
            for (Patient patient : allPatients) {
                cohort.add(patient.getPatientId());
            }
        }
        //exclude dead patients
        Set<Integer> alivePatients = Filters.alive(cohort, context);
        Set<Integer> male = Filters.male(alivePatients, context);
        Set<Integer> female = Filters.female(alivePatients, context);

        //declare concepts here to passed
        Concept medicaiton = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);

        //map the models and keys to be used on the UI
        model.addAttribute("dpM", getCount(medicaiton, Metadata.ListsofConcepts.dietAndExercise(), male, context));
        model.addAttribute("ogM", getCount(medicaiton, Metadata.ListsofConcepts.oglas(), male, context));
        model.addAttribute("insM", getCount(medicaiton, Metadata.ListsofConcepts.insulin(), male, context));
        model.addAttribute("hM", getCount(medicaiton, Metadata.ListsofConcepts.herbal(), male, context));
        model.addAttribute("otM", getCount(medicaiton, Metadata.ListsofConcepts.other(), male, context));
        model.addAttribute("antHm", getCount(medicaiton, Metadata.ListsofConcepts.hypertensive(), male, context));

        model.addAttribute("dpF", getCount(medicaiton, Metadata.ListsofConcepts.dietAndExercise(), female, context));
        model.addAttribute("ogF", getCount(medicaiton, Metadata.ListsofConcepts.oglas(), female, context));
        model.addAttribute("insF", getCount(medicaiton, Metadata.ListsofConcepts.insulin(), female, context));
        model.addAttribute("hF", getCount(medicaiton, Metadata.ListsofConcepts.herbal(), female, context));
        model.addAttribute("otF", getCount(medicaiton, Metadata.ListsofConcepts.other(), female, context));
        model.addAttribute("antHf", getCount(medicaiton, Metadata.ListsofConcepts.hypertensive(), female, context));

        
    }

    private Integer getCount(Concept q, List<Concept> options, Set<Integer> cohort, PatientCalculationContext context){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap problem_added = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Obs obs = EmrCalculationUtils.obsResultForPatient(problem_added, pId);
            if(obs != null && (options.contains(obs.getValueCoded()))){
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
