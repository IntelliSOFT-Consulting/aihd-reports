package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.Set;

public class ComplicationsDataConverter implements DataConverter {

    @Override
    public Object convert(Object o) {
        if(o == null)
            return "";
        String value = "";

        Obs obs = (Obs) o;

                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.RETONOPATHY))) {
                    value = "a";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEUROPATHY))) {
                    value = value + " b";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEPTHOPATHY))) {
                    value = value + " c";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.CELEBRALVARSULAR_DISEASES))) {
                    value = value + " d";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.COLONARY_HEART_DISEASES))) {
                    value = value + " e";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.PERIPHERAL_VASCULAR_DISEASES))) {
                    value = value + " f";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DIABETIC_FOOT))) {
                    value = value + " g";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.HEART_FAILURE))) {
                    value = value + " h";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ERECTILE_DISFUNCTION))) {
                    value = value + " i";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.GASTROPATHY))) {
                    value = value + " j";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.CATARACTS))) {
                    value = value + " k";
                }
                if (obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DENTAL_COMPLICATIONS))) {
                    value = value + " l";
                }

        return value;
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
