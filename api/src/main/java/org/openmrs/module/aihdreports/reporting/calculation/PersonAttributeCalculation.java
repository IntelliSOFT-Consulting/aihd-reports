package org.openmrs.module.aihdreports.reporting.calculation;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.Map;

public class PersonAttributeCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
        String uuid= (String) parameterValues.get("uuid");
        PersonService personService= Context.getPersonService();

        CalculationResultMap calculationResultMap=new CalculationResultMap();
        for (Integer pid : cohort){
            String value="";
            Person person=personService.getPerson(pid);
            PersonAttribute personAttribute=person.getAttribute(personService.getPersonAttributeTypeByUuid(uuid));
            if (personAttribute != null && StringUtils.isNotEmpty(personAttribute.getValue())){
                value=personAttribute.getValue();
            }
            calculationResultMap.put(pid, new SimpleResult(value, this));
        }
        return calculationResultMap;
    }
}
