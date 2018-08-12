package org.openmrs.module.aihdreports.reporting.converter;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.Date;
import java.util.Set;

public class CalculationResultConverter implements DataConverter {

    @Override
    public Object convert(Object obj) {

        if (obj == null) {
            return "";
        }

        Object value = ((CalculationResult) obj).getValue();

        if (value instanceof Boolean) {
            return (Boolean) value ? "Y" : "N";
        }
        else if (value instanceof Date) {
            return CoreUtils.formatDates((Date) value);
        }
        else if (value instanceof Concept) {

            return ((Concept) value).getName();
        }
        else if (value instanceof String) {
            return value.toString();
        }
        else if (value instanceof Double) {
            return Math.round(((Double) value) * 100) / 100;
        }
        else if (value instanceof Integer){
            return ((Integer) value);
        }
        else if (value instanceof Location){
            return ((Location) value).getName();
        }
        else if (value instanceof SimpleResult) {
            return ((SimpleResult) value).getValue();
        }


        return null;
    }

    @Override
    public Class<?> getInputDataType() {
        return CalculationResult.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}