package org.openmrs.module.aihdreports.data.converter;

import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.List;

public class DiabeticFootComplainsDataConverter implements DataConverter {

    @Override
    public Object convert(Object obj) {
        if (obj == null) {
            return "";
        }
        String results = "";
        Obs obs = ((Obs) obj);
        if (obs.getValueCoded() != null) {
            if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.LEG_SWEALING))){
                results = "Swelling";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.OTHER_NON_CODED))){
                List<Obs> obsList = Context.getObsService().getObservationsByPersonAndConcept(obs.getPerson(), Dictionary.getConcept(Dictionary.OTHER_CHIEF_COMPLAINT_TEXT));
                if(obsList.size() > 0){
                    Obs wanted = obsList.get(0);
                    if(wanted != null) {
                        results += ","+wanted.getValueText();
                    }
                }

            }
        }
        return results;
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