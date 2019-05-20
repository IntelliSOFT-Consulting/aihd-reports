package org.openmrs.module.aihdreports.reporting.utils;

import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

import java.util.List;


public class FacilityAddress {
    public static DataSetDefinition facilityAddress(List<Parameter> getParameters){
        SqlDataSetDefinition dsd = new SqlDataSetDefinition();
        dsd.setName("countyAndSubcounty");
        dsd.setParameters(getParameters);
        dsd.setSqlQuery("SELECT address14 as subcounty,address15 as county FROM location WHERE location_id=:location");
        return dsd;
    }

}
