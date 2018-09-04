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

public class DiabeticTypesFragmentController {

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
        Concept diabetic_type = Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC);
        Concept type1 =Dictionary.getConcept(Dictionary.TYPE_1_DIABETES);
        Concept type2 = Dictionary.getConcept(Dictionary.TYPE_2_DIABETES);
        Concept gdm = Dictionary.getConcept(Dictionary.GDM);

        //get the attribute to be displayed to the page
        model.addAttribute("t1M", getCount(diabetic_type, type1, male, context, loggedInLocation));
        model.addAttribute("t1F", getCount(diabetic_type, type1, female, context, loggedInLocation));
        model.addAttribute("t2M", getCount(diabetic_type, type2, male, context, loggedInLocation));
        model.addAttribute("t2F", getCount(diabetic_type, type2, female, context, loggedInLocation));
        model.addAttribute("gdmM", getCount(diabetic_type, gdm, male, context, loggedInLocation));
        model.addAttribute("gdmF", getCount(diabetic_type, gdm, female, context, loggedInLocation));
    }

    private Integer getCount(Concept q, Concept a, Set<Integer> cohort, PatientCalculationContext context, Location location){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
            if(obs != null  && concept != null && obs.getLocation().equals(location) && concept.equals(a)){
                allSet.add(pId);
            }
        }
        return allSet.size();
    }
}
