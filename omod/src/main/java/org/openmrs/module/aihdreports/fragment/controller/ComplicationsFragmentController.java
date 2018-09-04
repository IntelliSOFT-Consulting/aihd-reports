package org.openmrs.module.aihdreports.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplicationsFragmentController {

    public void controller(FragmentModel model,
                           @SpringBean("locationService") LocationService locationService) {

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

        User user = Context.getAuthenticatedUser();
        PersonAttribute attribute = user.getPerson().getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid("8930b69a-8e7c-11e8-9599-337483600ed7"));
        Location loggedInLocation = locationService.getLocation(1);
        if(attribute != null && StringUtils.isNotEmpty(attribute.getValue())){
            loggedInLocation = locationService.getLocation(Integer.parseInt(attribute.getValue()));
        }
        //declare concepts here to passed
        Concept problem_added = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept stroke = Dictionary.getConcept(Dictionary.STROKE);
        Concept ischemic_heart_disease = Dictionary.getConcept(Dictionary.Ischemic_heart_disease);
        Concept Peripheral_Vascular_disease = Dictionary.getConcept(Dictionary.Peripheral_Vascular_disease);
        Concept Heart_failure = Dictionary.getConcept(Dictionary.Heart_failure);
        Concept Neuropathy = Dictionary.getConcept(Dictionary.Neuropathy);
        Concept Retinopathy = Dictionary.getConcept(Dictionary.Retinopathy);
        Concept Diabetic_foot = Dictionary.getConcept(Dictionary.Diabetic_foot);
        Concept nephropathy = Dictionary.getConcept(Dictionary.Nephropathy);

        //add maps
        model.addAttribute("sM", getCount(problem_added, stroke, male, context, loggedInLocation));
        model.addAttribute("isM", getCount(problem_added, ischemic_heart_disease, male, context, loggedInLocation));
        model.addAttribute("pM", getCount(problem_added, Peripheral_Vascular_disease, male, context, loggedInLocation));
        model.addAttribute("hM", getCount(problem_added, Heart_failure, male, context, loggedInLocation));
        model.addAttribute("nM", getCount(problem_added, Neuropathy, male, context, loggedInLocation));
        model.addAttribute("rM", getCount(problem_added, Retinopathy, male, context, loggedInLocation));
        model.addAttribute("dM", getCount(problem_added, Diabetic_foot, male, context, loggedInLocation));
        model.addAttribute("neM", getCount(problem_added, nephropathy, male, context, loggedInLocation));

        model.addAttribute("sF", getCount(problem_added, stroke, female, context, loggedInLocation));
        model.addAttribute("isF", getCount(problem_added, ischemic_heart_disease, female, context, loggedInLocation));
        model.addAttribute("pF", getCount(problem_added, Peripheral_Vascular_disease, female, context, loggedInLocation));
        model.addAttribute("hF", getCount(problem_added, Heart_failure, female, context, loggedInLocation));
        model.addAttribute("nF", getCount(problem_added, Neuropathy, female, context, loggedInLocation));
        model.addAttribute("rF", getCount(problem_added, Retinopathy, female, context, loggedInLocation));
        model.addAttribute("dF", getCount(problem_added, Diabetic_foot, female, context, loggedInLocation));
        model.addAttribute("neF", getCount(problem_added, nephropathy, female, context, loggedInLocation));

        }

    private Integer getCount(Concept q, Concept a1, Set<Integer> cohort, PatientCalculationContext context, Location location){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap problem_added = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Obs obs = EmrCalculationUtils.obsResultForPatient(problem_added, pId);
            if(obs != null && (obs.getValueCoded().equals(a1)) && obs.getLocation().equals(location)){
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
