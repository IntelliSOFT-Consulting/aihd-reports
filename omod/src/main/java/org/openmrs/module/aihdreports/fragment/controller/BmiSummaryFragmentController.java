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

public class BmiSummaryFragmentController {

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
        if(allPatients.size() > 0) {
            for (Patient patient : allPatients) {
                cohort.add(patient.getPatientId());
            }
        }

        //get the location logged in
        User user = Context.getAuthenticatedUser();
        PersonAttribute attribute = user.getPerson().getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid("8930b69a-8e7c-11e8-9599-337483600ed7"));
        Location loggedInLocation = locationService.getLocation(1);
        if(attribute != null && StringUtils.isNotEmpty(attribute.getValue())){
            loggedInLocation = locationService.getLocation(Integer.parseInt(attribute.getValue()));
        }
        //exclude dead patients
        Set<Integer> alivePatients = Filters.alive(cohort, context);
        Set<Integer> male = Filters.male(alivePatients, context);
        Set<Integer> female = Filters.female(alivePatients, context);
        //declare concepts
        Concept weight = Dictionary.getConcept(Dictionary.WEIGHT);
        Concept height = Dictionary.getConcept(Dictionary.HEIGHT);

        model.addAttribute("belowOrEqualTo18M", getBmi(weight, height, male, context, 0.0, 18.0, loggedInLocation));
        model.addAttribute("over18Below25M", getBmi(weight, height, male, context, 18.5, 24.9, loggedInLocation));
        model.addAttribute("twenty5Below30M", getBmi(weight, height, male, context, 25.0, 29.9, loggedInLocation));
        model.addAttribute("over30M", getBmi(weight, height, male, context, 30.0, 60.0, loggedInLocation));

        model.addAttribute("belowOrEqualTo18F", getBmi(weight, height, female, context, 0.0, 18.0, loggedInLocation));
        model.addAttribute("over18Below25F", getBmi(weight, height, female, context, 18.5, 24.9, loggedInLocation));
        model.addAttribute("twenty5Below30F", getBmi(weight, height, female, context, 25.0, 29.9, loggedInLocation));
        model.addAttribute("over30F", getBmi(weight, height, female, context, 30.0, 60.0, loggedInLocation));
    }
    private Integer getBmi(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context, double minBmi, double maxBmi, Location location){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap weightMap = Calculations.lastObs(q1, cohort, context);
        CalculationResultMap heightMap = Calculations.lastObs(q2, cohort, context);

        for(Integer pId: cohort){
            Double weightObs = EmrCalculationUtils.numericObsResultForPatient(weightMap, pId);
            Double heightObs = EmrCalculationUtils.numericObsResultForPatient(heightMap, pId);
            Obs wObs = EmrCalculationUtils.obsResultForPatient(weightMap, pId);
            if(weightObs != null && heightObs !=null && wObs != null && wObs.getLocation().equals(location) ){
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
