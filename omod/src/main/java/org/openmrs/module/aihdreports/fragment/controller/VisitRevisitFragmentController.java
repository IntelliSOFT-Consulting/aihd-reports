package org.openmrs.module.aihdreports.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitRevisitFragmentController {

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
        EncounterType initial = Context.getEncounterService().getEncounterTypeByUuid(Metadata.EncounterType.DM_INITIAL);
        EncounterType followup = Context.getEncounterService().getEncounterTypeByUuid(Metadata.EncounterType.DM_INITIAL);



        //get the attribute to be displayed to the page
        model.addAttribute("vM", getCountVisit(initial, followup, male, context, overral_location));
        model.addAttribute("vF", getCountVisit(initial, followup, female, context, overral_location));
        model.addAttribute("rvM", getCountRevisit(followup, male, context, overral_location));
        model.addAttribute("rvF", getCountRevisit(followup, female, context, overral_location));

    }

    private Integer getCountVisit(EncounterType q1, EncounterType q2, Set<Integer> cohort, PatientCalculationContext context, List<Integer> loc){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map1 = Calculations.lastEncounter(q1, cohort, context);
        CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
        for(Integer pId: cohort){
            Encounter encounter1 = EmrCalculationUtils.encounterResultForPatient(map1, pId);
            Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
             if(encounter1 != null && encounter2 == null && loc.contains(encounter1.getLocation().getLocationId())) {
                allSet.add(pId);
            }
        }
        return allSet.size();
    }

    private Integer getCountRevisit(EncounterType q2, Set<Integer> cohort, PatientCalculationContext context, List<Integer> loc){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
        for(Integer pId: cohort){
            Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
            if(encounter2 != null && loc.contains(encounter2.getLocation().getLocationId())) {
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
