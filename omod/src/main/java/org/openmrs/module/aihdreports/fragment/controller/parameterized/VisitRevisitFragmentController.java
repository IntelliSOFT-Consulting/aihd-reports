package org.openmrs.module.aihdreports.fragment.controller.parameterized;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.Calculations;
import org.openmrs.module.aihdreports.reporting.calculation.EmrCalculationUtils;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitRevisitFragmentController {

    public void controller(FragmentModel model,
                           @FragmentParam(value = "location", required= false) Location location,
                           @FragmentParam(value = "startDate", required= false) Date startDate,
                           @FragmentParam(value = "endDate", required= false) Date endDate,
                           @FragmentParam(value = "male", required = false) Set<Integer> male,
                           @FragmentParam(value = "female", required = false) Set<Integer> female,
                           @FragmentParam(value = "subcounty", required = false) List<Location> subCounty) throws ParseException

    {

        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        //declare concepts
        EncounterType initial = Context.getEncounterService().getEncounterTypeByUuid(Metadata.EncounterType.DM_INITIAL);
        EncounterType followup = Context.getEncounterService().getEncounterTypeByUuid(Metadata.EncounterType.DM_FOLLOWUP);



        //get the attribute to be displayed to the page
        model.addAttribute("vM", getCountVisit(initial, followup, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("vF", getCountVisit(initial, followup, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("rvM", getCountRevisit(followup, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("rvF", getCountRevisit(followup, female, context, location, subCounty, startDate, endDate));

    }

    private Integer getCountVisit(EncounterType q1, EncounterType q2, Set<Integer> cohort, PatientCalculationContext context, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map1 = Calculations.lastEncounter(q1, cohort, context);
        CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
        for(Integer pId: cohort){
            Encounter encounter1 = EmrCalculationUtils.encounterResultForPatient(map1, pId);
            Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
            if(encounter1 != null && (encounter1.getEncounterDatetime().equals(startDate) || encounter1.getEncounterDatetime().after(startDate) && (encounter1.getEncounterDatetime().before(endDate) || encounter1.getEncounterDatetime().equals(endDate)))) {
                if (encounter2 == null && encounter1.getLocation().equals(location)) {
                    allSet.add(pId);
                }
                else if (subcounty.size() > 0 && encounter2 == null && subcounty.contains(encounter1.getLocation())) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }

    private Integer getCountRevisit(EncounterType q2, Set<Integer> cohort, PatientCalculationContext context, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();

        CalculationResultMap map2 = Calculations.lastEncounter(q2, cohort, context);
        for(Integer pId: cohort){
            Encounter encounter2 = EmrCalculationUtils.encounterResultForPatient(map2, pId);
            if(encounter2 != null && (encounter2.getEncounterDatetime().equals(startDate) || encounter2.getEncounterDatetime().after(startDate) && (encounter2.getEncounterDatetime().before(endDate) || encounter2.getEncounterDatetime().equals(endDate)))) {
                if (encounter2.getLocation().equals(location)) {
                    allSet.add(pId);
                }
                else if (subcounty.size() > 0 && subcounty.contains(encounter2.getLocation())) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }
}
