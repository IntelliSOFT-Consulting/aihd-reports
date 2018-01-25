package org.openmrs.module.aihdreports.reporting.converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class EncounterDateConveter implements DataConverter {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public Object convert(Object original) {

        Encounter encounter = (Encounter) original;

        if (original == null || encounter == null) {
            return "";
        }
        else {
            return CoreUtils.formatDates(encounter.getEncounterDatetime());
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
