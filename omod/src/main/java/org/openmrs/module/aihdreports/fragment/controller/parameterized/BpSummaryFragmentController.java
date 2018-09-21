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

public class BpSummaryFragmentController {

    public void controller(FragmentModel model,
                           @FragmentParam(value = "location", required= false) Location location,
                           @FragmentParam(value = "startDate", required= false) Date startDate,
                           @FragmentParam(value = "endDate", required= false) Date endDate,
                           @FragmentParam(value = "male", required = false) Set<Integer> male,
                           @FragmentParam(value = "female", required = false) Set<Integer> female,
                           @FragmentParam(value = "subcounty", required = false) List<Location> subCounty) throws ParseException {
        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        //declare concepts
        Concept systtollic = Dictionary.getConcept(Dictionary.SYSTOLIC_BLOOD_PRESSURE);
        Concept diastollic = Dictionary.getConcept(Dictionary.DIASTOLIC_BLOOD_PRESSURE);

        model.addAttribute("above14090M", getPressure(systtollic, diastollic, male, context, 140, 90, location, subCounty, startDate, endDate));
        model.addAttribute("above14090F", getPressure(systtollic, diastollic, female, context, 140, 90, location, subCounty, startDate, endDate));
    }

    private Integer getPressure(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context, double systollic, double diastollic, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap systo = Calculations.lastObs(q1, cohort, context);
        CalculationResultMap diasto = Calculations.lastObs(q2, cohort, context);

        for(Integer pId: cohort){
            Double sys = EmrCalculationUtils.numericObsResultForPatient(systo, pId);
            Double dia = EmrCalculationUtils.numericObsResultForPatient(diasto, pId);
            Obs wObs = EmrCalculationUtils.obsResultForPatient(systo, pId);
            if(wObs != null && (wObs.getObsDatetime().equals(startDate) || wObs.getObsDatetime().after(startDate) && (wObs.getObsDatetime().before(endDate) || wObs.getObsDatetime().equals(endDate)))) {
                if (sys != null && dia != null && wObs.getLocation().equals(location)) {
                    if (sys > systollic && dia > diastollic) {
                        allSet.add(pId);
                    }
                }
                else if (sys != null && dia != null && subcounty.contains(wObs.getLocation())) {
                    if (sys > systollic && dia > diastollic) {
                        allSet.add(pId);
                    }
                }
            }

        }
        return allSet.size();
    }
}
