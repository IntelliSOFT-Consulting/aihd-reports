package org.openmrs.module.aihdreports.fragment.controller.parameterized;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplicationsFragmentController {

    public void controller(FragmentModel model,
                           @FragmentParam(value = "location", required= false) Location location,
                           @FragmentParam(value = "startDate", required= false) Date startDate,
                           @FragmentParam(value = "endDate", required= false) Date endDate,
                           @FragmentParam(value = "male", required = false) Set<Integer> male,
                           @FragmentParam(value = "female", required = false) Set<Integer> female,
                           @FragmentParam(value = "subcounty", required = false) List<Location> subCounty) throws ParseException{

        //get a collection of all patients
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());


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
        model.addAttribute("sM", getCount(problem_added, stroke, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("isM", getCount(problem_added, ischemic_heart_disease, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("pM", getCount(problem_added, Peripheral_Vascular_disease, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("hM", getCount(problem_added, Heart_failure, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("nM", getCount(problem_added, Neuropathy, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("rM", getCount(problem_added, Retinopathy, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("dM", getCount(problem_added, Diabetic_foot, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("neM", getCount(problem_added, nephropathy, male, context, location, subCounty, startDate, endDate));

        model.addAttribute("sF", getCount(problem_added, stroke, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("isF", getCount(problem_added, ischemic_heart_disease, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("pF", getCount(problem_added, Peripheral_Vascular_disease, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("hF", getCount(problem_added, Heart_failure, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("nF", getCount(problem_added, Neuropathy, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("rF", getCount(problem_added, Retinopathy, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("dF", getCount(problem_added, Diabetic_foot, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("neF", getCount(problem_added, nephropathy, female, context, location, subCounty, startDate, endDate));

        }

    private Integer getCount(Concept q, Concept a1, Set<Integer> cohort, PatientCalculationContext context, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap problem_added = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Obs obs = EmrCalculationUtils.obsResultForPatient(problem_added, pId);
            if(obs != null && (obs.getObsDatetime().equals(startDate) || obs.getObsDatetime().after(startDate) && (obs.getObsDatetime().before(endDate) || obs.getObsDatetime().equals(endDate)))) {
                if (obs.getValueCoded().equals(a1) && obs.getLocation().equals(location)) {
                    allSet.add(pId);
                }
                else if (subcounty.size() > 0 && obs.getValueCoded().equals(a1) && subcounty.contains(obs.getLocation())) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }
}
