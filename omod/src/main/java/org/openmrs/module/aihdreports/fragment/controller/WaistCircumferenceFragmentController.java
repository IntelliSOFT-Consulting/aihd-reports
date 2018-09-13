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

public class WaistCircumferenceFragmentController {

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
        //declare concepts
        Concept waist_circumference = Dictionary.getConcept(Dictionary.WAIST_CIRCUMFERENCE);

        model.addAttribute("over102", getWC(waist_circumference, male, context, 102.0, overral_location));
        model.addAttribute("over88", getWC(waist_circumference, male, context, 88.0, overral_location));
    }

    private Integer getWC(Concept q1, Set<Integer> cohort, PatientCalculationContext context, double minValue, List<Integer> loc){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap waistCircumMap = Calculations.lastObs(q1, cohort, context);
        for(Integer pId: cohort){
            Double cirm = EmrCalculationUtils.numericObsResultForPatient(waistCircumMap, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(waistCircumMap, pId);
            if(cirm != null && obs != null && loc.contains(obs.getLocation().getLocationId())){
                if(cirm > minValue) {
                    allSet.add(pId);
                }
            }

        }
        return allSet.size();
    }
}
