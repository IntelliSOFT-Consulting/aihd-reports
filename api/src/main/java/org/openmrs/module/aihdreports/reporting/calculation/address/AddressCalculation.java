package org.openmrs.module.aihdreports.reporting.calculation.address;

import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;

import java.util.Collection;
import java.util.Map;

public class AddressCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        String which = (String) parameterValues.get("which");
        CalculationResultMap ret = new CalculationResultMap();

        PersonService personService = Context.getPersonService();

        for(Integer ptId : cohort) {
            Person person = personService.getPerson(ptId);
            String value = "";
            if(which != null && person.getPersonAddress() != null) {
               if(which.equals("subcounty")){
                   value = person.getPersonAddress().getAddress1();
               }
               else if(which.equals("village")){
                    value = person.getPersonAddress().getCityVillage();
                }
               else if(which.equals("landmark")){
                   value = person.getPersonAddress().getAddress2();
               }
            }
            ret.put(ptId, new SimpleResult(value, this));
        }

        return ret;
    }
}
