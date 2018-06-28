package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class TreatmentDataConveter implements DataConverter {
    @Override
    public Object convert(Object o) {
        if(o == null)
            return "";
        Obs obs = (Obs) o;
        if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.DIET))){
            return "a";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.OGLAS_METFORMIN))){
            return "b";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1))){
            return "c";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ANT_HYPETENSIVE))){
            return "d";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.HERBAL))){
            return "e";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.OTHER_NON_CODED))){
            return "f";
        }
        else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.OGLAS_AND_INSULIN))){
            return "g";
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
