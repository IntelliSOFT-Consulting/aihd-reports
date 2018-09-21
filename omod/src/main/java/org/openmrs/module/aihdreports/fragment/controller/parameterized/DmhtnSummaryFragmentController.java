package org.openmrs.module.aihdreports.fragment.controller.parameterized;

import org.openmrs.Concept;
import org.openmrs.Location;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DmhtnSummaryFragmentController {

    public void controller(FragmentModel model,
                           @FragmentParam(value = "location", required= false) Location location,
                           @FragmentParam(value = "startDate", required= false) Date startDate,
                           @FragmentParam(value = "endDate", required= false) Date endDate,
                           @FragmentParam(value = "male", required = false) Set<Integer> male,
                           @FragmentParam(value = "female", required = false) Set<Integer> female,
                           @FragmentParam(value = "subcounty", required = false) List<Location> subCounty) throws ParseException {

        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        //declare concepts
        Concept dm = Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE);
        Concept dmNew = Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT);
        Concept htn = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept htnNew = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);

        //get the attribute to be displayed to the page
        model.addAttribute("dmM", getCount(dm, dmNew, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("dmF", getCount(dm, dmNew, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("htnM", getCount(htn, htnNew, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("htnF", getCount(htn, htnNew, female, context, location, subCounty, startDate, endDate));
    }

    private Integer getCount(Concept q, Concept a, Set<Integer> cohort, PatientCalculationContext context, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
            if(obs != null && (obs.getObsDatetime().equals(startDate) || obs.getObsDatetime().after(startDate) && (obs.getObsDatetime().before(endDate) || obs.getObsDatetime().equals(endDate)))) {
                if (concept != null && obs.getLocation().equals(location) && concept.equals(a)) {
                    allSet.add(pId);
                }
                else if (concept != null && subcounty.contains(obs.getLocation()) && concept.equals(a)) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }
}
