package org.openmrs.module.aihdreports.reporting.calculation.diagnosis;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;

import java.util.Collection;
import java.util.Map;

public class ComplicationsCalculation  extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();

        return ret;
    }
}