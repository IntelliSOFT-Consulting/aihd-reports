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

public class TbSummaryFragmentController {

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

        //declare concepts
        Concept screened_for_tb = Dictionary.getConcept(Dictionary.SCREENED_FOR_TB);
        Concept tb_ststus =Dictionary.getConcept(Dictionary.TB_STATUS);
        Concept on_treatment = Dictionary.getConcept(Dictionary.ON_TREATMENT);
        Concept yes = Dictionary.getConcept(Dictionary.YES);

        //get the attribute to be displayed to the page
        model.addAttribute("srnM", getCount(screened_for_tb, yes, male, context));
        model.addAttribute("scrF", getCount(screened_for_tb, yes, female, context));
        model.addAttribute("ptvM", getCount(tb_ststus, on_treatment, male, context));
        model.addAttribute("ptvF", getCount(tb_ststus, on_treatment, female, context));
    }

    private Integer getCount(Concept q, Concept a, Set<Integer> cohort, PatientCalculationContext context){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
            if(concept != null && concept.equals(a)){
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
