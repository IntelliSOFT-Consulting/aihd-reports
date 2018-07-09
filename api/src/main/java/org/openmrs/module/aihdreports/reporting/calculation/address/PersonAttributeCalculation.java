package org.openmrs.module.aihdreports.reporting.calculation.address;

import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.calculation.AbstractPatientCalculation;

import java.util.Collection;
import java.util.Map;

public class PersonAttributeCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        String uuid = (String) parameterValues.get("uuid");
        CalculationResultMap ret = new CalculationResultMap();
        for(Integer ptId: cohort){
            String value = "";
            if(uuid != null) {
                PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(uuid);
                Person person = Context.getPersonService().getPerson(ptId);
                PersonAttribute attribute = person.getAttribute(personAttributeType);
                if(attribute != null){
                    value = attribute.getValue();
                }
            }
            ret.put(ptId, new SimpleResult(value, this));
        }
        return ret;
    }
}
