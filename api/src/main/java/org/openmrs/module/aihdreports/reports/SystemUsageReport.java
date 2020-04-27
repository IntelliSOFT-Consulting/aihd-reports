package org.openmrs.module.aihdreports.reports;

import java.util.Properties;

import org.openmrs.module.aihdreports.reporting.queries.Queries;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

@Component
public class SystemUsageReport extends AIHDDataExportManager {

    @Override
    public String getUuid() {
        return "3976c5f0-8492-11ea-bb57-e3371ce88bc1";
    }

    @Override
    public String getName() {
        return "System Usage Report";
    }

    @Override
    public String getDescription() {
        return "Collects a summary of system usage";
    }

    @Override
    public ReportDefinition constructReportDefinition() {
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setUuid(getUuid());
        reportDefinition.setName(getName());
        reportDefinition.setDescription(getDescription());

        reportDefinition.addDataSetDefinition("SUR", Mapped.mapStraightThrough(constructSystemUsageDataset()));

        return reportDefinition;
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getExcelDesignUuid() {
        return "545edb82-8492-11ea-bba6-d353472f76d7";
    }

    @Override
    public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
        ReportDesign reportDesign =createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "systemUsageReport.xls");
        Properties properties = new Properties();
        properties.put("repeatingSections", "sheet:1,row:5,dataset:SUR");
        properties.put("sortWeight", "5000");
        reportDesign.setProperties(properties);
        return reportDesign;
    }

    private DataSetDefinition constructSystemUsageDataset(){
        SqlDataSetDefinition su = new SqlDataSetDefinition();
        su.setName("SUR");
        su.setSqlQuery(Queries.getPatientsListForSystemUsageReport());

        return su;
    }

    
}