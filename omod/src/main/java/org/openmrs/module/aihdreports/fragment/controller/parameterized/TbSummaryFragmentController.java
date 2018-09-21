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

public class TbSummaryFragmentController {

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
        Concept screened_for_tb = Dictionary.getConcept(Dictionary.SCREENED_FOR_TB);
        Concept tb_ststus =Dictionary.getConcept(Dictionary.TB_STATUS);
        Concept on_treatment = Dictionary.getConcept(Dictionary.ON_TREATMENT);
        Concept yes = Dictionary.getConcept(Dictionary.YES);

        //get the attribute to be displayed to the page
        model.addAttribute("srnM", getCount(screened_for_tb, yes, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("scrF", getCount(screened_for_tb, yes, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("ptvM", getCount(tb_ststus, on_treatment, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("ptvF", getCount(tb_ststus, on_treatment, female, context, location, subCounty, startDate, endDate));
    }

    private Integer getCount(Concept q, Concept a, Set<Integer> cohort, PatientCalculationContext context, Location facility, List<Location> subcounties, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map = Calculations.lastObs(q, cohort, context);
        for(Integer pId: cohort){
            Concept concept = EmrCalculationUtils.codedObsResultForPatient(map, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(map, pId);
            if(obs != null && (obs.getObsDatetime().equals(startDate) || obs.getObsDatetime().after(startDate) && (obs.getObsDatetime().before(endDate) || obs.getObsDatetime().equals(endDate)))) {
                if (facility != null && concept != null && obs.getLocation().equals(facility) && concept.equals(a)) {
                    allSet.add(pId);
                }
                else if (subcounties.size() > 0 && concept != null && subcounties.contains(obs.getLocation()) && concept.equals(a)) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }
}
