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

public class BmiSummaryFragmentController {

    public void controller(FragmentModel model,
                            @FragmentParam(value = "location", required= false)
                            Location location,
                            @FragmentParam(value = "startDate", required= false) Date startDate,
                            @FragmentParam(value = "endDate", required= false) Date endDate,
                            @FragmentParam(value = "male", required = false) Set<Integer> male,
                            @FragmentParam(value = "female", required = false) Set<Integer> female,
                            @FragmentParam(value = "subcounty", required = false) List<Location> subCounty) throws ParseException

                            {
        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        //declare concepts
        Concept weight = Dictionary.getConcept(Dictionary.WEIGHT);
        Concept height = Dictionary.getConcept(Dictionary.HEIGHT);

        model.addAttribute("belowOrEqualTo18M", getBmi(weight, height, male, context, 0.0, 18.0, location, subCounty, startDate, endDate));
        model.addAttribute("over18Below25M", getBmi(weight, height, male, context, 18.5, 24.9, location, subCounty, startDate, endDate));
        model.addAttribute("twenty5Below30M", getBmi(weight, height, male, context, 25.0, 29.9, location, subCounty, startDate, endDate));
        model.addAttribute("over30M", getBmi(weight, height, male, context, 30.0, 60.0, location, subCounty, startDate, endDate));

        model.addAttribute("belowOrEqualTo18F", getBmi(weight, height, female, context, 0.0, 18.0, location, subCounty, startDate, endDate));
        model.addAttribute("over18Below25F", getBmi(weight, height, female, context, 18.5, 24.9, location, subCounty, startDate, endDate));
        model.addAttribute("twenty5Below30F", getBmi(weight, height, female, context, 25.0, 29.9, location, subCounty, startDate, endDate));
        model.addAttribute("over30F", getBmi(weight, height, female, context, 30.0, 60.0, location, subCounty, startDate, endDate));
    }
    private Integer getBmi(Concept q1, Concept q2, Set<Integer> cohort, PatientCalculationContext context, double minBmi, double maxBmi, Location location, List<Location> subcounty, Date startDate, Date endDate){
        Set<Integer> allSet = new HashSet<>();
        CalculationResultMap weightMap = Calculations.lastObs(q1, cohort, context);
        CalculationResultMap heightMap = Calculations.lastObs(q2, cohort, context);

        for(Integer pId: cohort){
            Double weightObs = EmrCalculationUtils.numericObsResultForPatient(weightMap, pId);
            Double heightObs = EmrCalculationUtils.numericObsResultForPatient(heightMap, pId);
            Obs wObs = EmrCalculationUtils.obsResultForPatient(weightMap, pId);
            if(wObs != null && (wObs.getObsDatetime().equals(startDate) || wObs.getObsDatetime().after(startDate) && (wObs.getObsDatetime().before(endDate) || wObs.getObsDatetime().equals(endDate)))) {
                if (weightObs != null && heightObs != null && wObs.getLocation().equals(location)) {
                    //calculate the BMI here
                    Double bmi = calculateBmi(weightObs, heightObs);
                    if (bmi >= minBmi && bmi <= maxBmi) {
                        allSet.add(pId);
                    }
                }
                else if (weightObs != null && heightObs != null && subcounty.contains(wObs.getLocation())) {
                    //calculate the BMI here
                    Double bmi = calculateBmi(weightObs, heightObs);
                    if (bmi >= minBmi && bmi <= maxBmi) {
                        allSet.add(pId);
                    }
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
