package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class NewlyDiagnosedConverter implements DataConverter {
public NewlyDiagnosedConverter() {

}
    @Override
    public Object convert(Object o) {
        if(o == null)
            return "";

        Obs obs = (Obs) o;
        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT))){
            return "Y";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.KNOWN_DAIBETIC_PATIENT))){
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
