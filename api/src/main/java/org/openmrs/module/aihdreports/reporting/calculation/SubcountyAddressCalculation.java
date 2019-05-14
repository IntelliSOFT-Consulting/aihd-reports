package org.openmrs.module.aihdreports.reporting.calculation;

import org.openmrs.PersonAddress;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.Map;

public class SubcountyAddressCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
        CalculationResultMap ret= new CalculationResultMap();
        PersonService personService = Context.getPersonService();

        for (Integer pid:cohort){
            String value="";
            PersonAddress personAddress = personService.getPerson(pid).getPersonAddress();
            if(personAddress != null && personAddress.getAddress1() != null){
                value= personAddress.getAddress1();
            }
            ret.put(pid, new SimpleResult(value, this));
        }
        return ret;
    }
}