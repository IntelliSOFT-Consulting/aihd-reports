package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class NewlyDiagnosedConverter implements DataConverter {

    @Override
    public Object convert(Object o) {
        if(o == null)
            return "";

        Obs obs = (Obs) o;
        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DIAGNOSIS_TYPE_NEW))){
            return "Y";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DIAGNOSIS_TYPE_NOT_NEW))){
            return "N";
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
