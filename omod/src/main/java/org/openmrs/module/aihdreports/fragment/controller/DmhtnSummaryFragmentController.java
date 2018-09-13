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
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DmhtnSummaryFragmentController {

    public void controller(FragmentModel model,
                           @FragmentParam(value = "requiredLocations", required = false) List<Integer> overral_location,
                           @FragmentParam(value = "allPatients", required = false) List<Patient> allPatients) {
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        //get a collection of all patients
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

        //declare concepts
        Concept dm = Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE);
        Concept dmNew = Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT);
        Concept htn = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept htnNew = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);

        //get the attribute to be displayed to the page
        model.addAttribute("dmM", getCount(dm, dmNew, male, context, overral_location));
        model.addAttribute("dmF", getCount(dm, dmNew, female, context, overral_location));
        model.addAttribute("htnM", getCount(htn, htnNew, male, context, overral_location));
        model.addAttribute("htnF", getCount(htn, htnNew, female, context, overral_location));
    }

    private Integer getCount(Concept q, Concept a, Set<Integer> cohort, PatientCalculationContext context, List<Integer> loc){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
            if(obs != null  && concept != null && loc.contains(obs.getLocation().getLocationId()) && concept.equals(a)){
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
