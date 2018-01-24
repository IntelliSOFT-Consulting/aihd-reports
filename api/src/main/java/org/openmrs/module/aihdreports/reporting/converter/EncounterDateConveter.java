package org.openmrs.module.aihdreports.reporting.converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.List;

public class EncounterDateConveter implements DataConverter {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public Object convert(Object original) {

        List<Encounter> encounters = (List<Encounter>) original;

        if (encounters.size() == 0)
            return "";

        Encounter lastEncounter = encounters.get(0);

        if (lastEncounter != null){
            return lastEncounter.getEncounterDatetime();
        } else {
            return "";
        }
    }

    @Override
    public Class<?> getInputDataType() {
        return Encounter.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}
