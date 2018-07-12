package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Obs;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class StatusDataConverter implements DataConverter {

    @Override
    public Object convert(Object obj) {

        if (obj == null) {
            return "";
        }

        Obs obs = ((Obs) obj);


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
