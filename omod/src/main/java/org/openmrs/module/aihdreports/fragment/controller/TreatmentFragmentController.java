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
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Arrays;
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
        Concept diet = Dictionary.getConcept(Dictionary.DIET);
        Concept physical_exercise = Dictionary.getConcept(Dictionary.PHYSICAL_EXERCISE);
        Concept oglas_metformin = Dictionary.getConcept(Dictionary.OGLAS_METFORMIN);
        Concept oglas_gilberclamide = Dictionary.getConcept(Dictionary.OGLAS_GILBERCLAMIDE);
        Concept oglas_other = Dictionary.getConcept(Dictionary.OGLAS_OTHER);
        Concept insulins_70_30 = Dictionary.getConcept(Dictionary.INSULIN_70_30);
        Concept insulins_soluble = Dictionary.getConcept(Dictionary.INSULIN_SOLUBLE);
        Concept insulins_nph_type_1 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1);
        Concept insulins_nph_type_2 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_2);
        Concept insulins_other_medication = Dictionary.getConcept(Dictionary.INSULIN_OTHER_MEDICATION);
        Concept herbal = Dictionary.getConcept(Dictionary.HERBAL);
        Concept other_non_coded = Dictionary.getConcept(Dictionary.OTHER_NON_CODED);

        //create lists for possible answers per category
        List<Concept> diet_and_physical_activities = Arrays.asList(diet, physical_exercise);
        List<Concept> oglas = Arrays.asList(oglas_metformin, oglas_gilberclamide, oglas_other);
        List<Concept> insulin = Arrays.asList(insulins_70_30, insulins_soluble, insulins_nph_type_1, insulins_nph_type_2, insulins_other_medication);
        List<Concept> herbals = Arrays.asList(herbal);
        List<Concept> other = Arrays.asList(other_non_coded);

        //map the models and keys to be used on the UI
        model.addAttribute("dpM", getCount(medicaiton, diet_and_physical_activities, male, context));
        model.addAttribute("ogM", getCount(medicaiton, oglas, male, context));
        model.addAttribute("insM", getCount(medicaiton, insulin, male, context));
        model.addAttribute("hM", getCount(medicaiton, herbals, male, context));
        model.addAttribute("otM", getCount(medicaiton, other, male, context));

        model.addAttribute("dpF", getCount(medicaiton, diet_and_physical_activities, female, context));
        model.addAttribute("ogF", getCount(medicaiton, oglas, female, context));
        model.addAttribute("insF", getCount(medicaiton, insulin, female, context));
        model.addAttribute("hF", getCount(medicaiton, herbals, female, context));
        model.addAttribute("otF", getCount(medicaiton, other, male, context));

        
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
