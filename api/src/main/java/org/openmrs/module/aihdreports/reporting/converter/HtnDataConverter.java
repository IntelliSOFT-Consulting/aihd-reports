package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class HtnDataConverter implements DataConverter {
 public HtnDataConverter() {}
    @Override
    public Object convert(Object o) {

        if(o == null)
            return "";
        Obs obs = (Obs) o;
        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT))){
            return "a";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT))){
            return "b";
        }
        return null;
    }

    @Override
    public Class<?> getInputDataType() {
        return Obs.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}
