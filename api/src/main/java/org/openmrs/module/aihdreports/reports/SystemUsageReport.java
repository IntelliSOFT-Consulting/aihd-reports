package org.openmrs.module.aihdreports.reports;

import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

@Component
public class SystemUsageReport extends AIHDDataExportManager {

    @Override
    public String getUuid() {
        // TODO Auto-generated method stub
        return null;
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
        return null;
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return "0.1";
    }

    @Override
    public String getExcelDesignUuid() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
        // TODO Auto-generated method stub
        return null;
    }

    
}