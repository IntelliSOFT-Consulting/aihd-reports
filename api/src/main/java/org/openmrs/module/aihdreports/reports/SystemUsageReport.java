package org.openmrs.module.aihdreports.reports;

import java.util.Properties;

import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

@Component
public class SystemUsageReport extends AIHDDataExportManager {

    @Override
    public String getUuid() {
        // TODO Auto-generated method stub
        return "3976c5f0-8492-11ea-bb57-e3371ce88bc1";
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "System Usage Report";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Collects a summary of system usage";
    }

    @Override
    public ReportDefinition constructReportDefinition() {
        // TODO Auto-generated method stub
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setUuid(getUuid());
        reportDefinition.setName(getName());
        reportDefinition.setDescription(getDescription());

        return reportDefinition;
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return "0.1";
    }

    @Override
    public String getExcelDesignUuid() {
        // TODO Auto-generated method stub
        return "545edb82-8492-11ea-bba6-d353472f76d7";
    }

    @Override
    public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
        ReportDesign reportDesign =createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "systemUsageReport.xls");
        Properties properties = new Properties();
        properties.put("repeatingSections", "sheet:1,row:7,dataset:SUR");
        properties.put("sortWeight", "5000");
        reportDesign.setProperties(properties);
        return reportDesign;
    }

    
}