package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class DiagnosisDataConverter implements DataConverter {

    @Override
    public Object convert(Object o) {
        if(o == null)
            return "";

        Obs obss = (Obs) o;
        if(obss.getValueCoded().equals(Dictionary.getConcept(Dictionary.TYPE_1_DIABETES))) {
            return "a";

        }
        else if(obss.getValueCoded().equals(Dictionary.getConcept(Dictionary.TYPE_2_DIABETES))) {
            return "b";

        }
        else if(obss.getValueCoded().equals(Dictionary.getConcept(Dictionary.GDM))) {
            return "c";

        }
        else if(obss.getValueCoded().equals(Dictionary.getConcept(Dictionary.DIABETES_SECONDARY_TO_OTHER_CAUSES))) {
            return "d";

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
