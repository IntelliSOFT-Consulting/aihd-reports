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

public class WaistCircumferenceFragmentController {

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
        Concept waist_circumference = Dictionary.getConcept(Dictionary.WAIST_CIRCUMFERENCE);

        model.addAttribute("over102", getWC(waist_circumference, male, context, 102.0, location, subCounty, startDate, endDate));
        model.addAttribute("over88", getWC(waist_circumference, male, context, 88.0, location, subCounty, startDate, endDate));
    }

    private Integer getWC(Concept q1, Set<Integer> cohort, PatientCalculationContext context, double minValue, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap waistCircumMap = Calculations.lastObs(q1, cohort, context);
        for(Integer pId: cohort){
            Double cirm = EmrCalculationUtils.numericObsResultForPatient(waistCircumMap, pId);
            Obs obs = EmrCalculationUtils.obsResultForPatient(waistCircumMap, pId);
            if(obs != null && (obs.getObsDatetime().equals(startDate) || obs.getObsDatetime().after(startDate) && (obs.getObsDatetime().before(endDate) || obs.getObsDatetime().equals(endDate)))) {
                if (cirm != null && obs.getLocation().equals(location)) {
                    if (cirm > minValue) {
                        allSet.add(pId);
                    }
                }
                else if (subcounty.size() > 0 && cirm != null && subcounty.contains(obs.getLocation())) {
                    if (cirm > minValue) {
                        allSet.add(pId);
                    }
                }
            }

        }
        return allSet.size();
    }
}
