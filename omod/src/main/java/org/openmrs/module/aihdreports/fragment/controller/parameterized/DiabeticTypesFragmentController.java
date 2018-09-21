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

public class DiabeticTypesFragmentController {

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
        Concept diabetic_type = Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC);
        Concept type1 =Dictionary.getConcept(Dictionary.TYPE_1_DIABETES);
        Concept type2 = Dictionary.getConcept(Dictionary.TYPE_2_DIABETES);
        Concept gdm = Dictionary.getConcept(Dictionary.GDM);

        //get the attribute to be displayed to the page
        model.addAttribute("t1M", getCount(diabetic_type, type1, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("t1F", getCount(diabetic_type, type1, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("t2M", getCount(diabetic_type, type2, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("t2F", getCount(diabetic_type, type2, female, context, location, subCounty, startDate, endDate));
        model.addAttribute("gdmM", getCount(diabetic_type, gdm, male, context, location, subCounty, startDate, endDate));
        model.addAttribute("gdmF", getCount(diabetic_type, gdm, female, context, location, subCounty, startDate, endDate));
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
                else if (subcounty.size() > 0 && concept != null && subcounty.contains(obs.getLocation()) && concept.equals(a)) {
                    allSet.add(pId);
                }
            }
        }
        return allSet.size();
    }
}
