package org.openmrs.module.aihdreports.reports;

import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class PermanentRegister extends AIHDDataExportManager {

    @Override
    public String getExcelDesignUuid() {
        return "59f454be-0271-11e8-baab-1b0fac14df0e";
    }

    @Override
    public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
        ReportDesign rd= createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "permanent.xls");
        Properties props = new Properties();
        props.put("repeatingSections", "sheet:1,row:7,dataset:P");
        props.put("sortWeight", "5000");
        rd.setProperties(props);
        return rd;
    }

    @Override
    public String getUuid() {
        return "713f9b56-0271-11e8-8377-3fa4a39ea76a";
    }

    @Override
    public String getName() {
        return "Permanent Register";
    }

    @Override
    public String getDescription() {
        return "Collect patient permanent information";
    }

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        List<ReportDesign> l = new ArrayList<ReportDesign>();
        l.add(buildReportDesign(reportDefinition));
        return l;
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public ReportDefinition constructReportDefinition() {
        ReportDefinition rd = new ReportDefinition();
        rd.setUuid(getUuid());
        rd.setName(getName());
        rd.setDescription(getDescription());
        rd.addParameters(getParameters());
        rd.addDataSetDefinition("P", Mapped.mapStraightThrough(dataSetDefinition()));
        return rd;
    }

    private DataSetDefinition dataSetDefinition() {
        PatientDataSetDefinition dsd = new PatientDataSetDefinition();
        SharedDataDefinition sdd= new SharedDataDefinition();
        return dsd;
    }

}
