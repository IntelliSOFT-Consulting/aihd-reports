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
public class DiabeticFootRegisterReport extends AIHDDataExportManager{
    @Override
    public String getExcelDesignUuid() {
        return "4bc35404-0270-11e8-9058-5b0d24f9ec33";
    }

    @Override
    public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
        ReportDesign rd= createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "diabetic_foot.xls");
        Properties props = new Properties();
        props.put("repeatingSections", "sheet:1,row:7,dataset:Df");
        props.put("sortWeight", "5000");
        rd.setProperties(props);
        return rd;
    }

    @Override
    public String getUuid() {
        return "59e83c20-0270-11e8-8691-e3f98920b873";
    }

    @Override
    public String getName() {
        return "Diabetic Foot Register";
    }

    @Override
    public String getDescription() {
        return "Collect diabetic foot information";
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
        rd.addDataSetDefinition("Df", Mapped.mapStraightThrough(dataSetDefinition()));
        return rd;
    }

    private DataSetDefinition dataSetDefinition() {
        PatientDataSetDefinition dsd = new PatientDataSetDefinition();
        SharedDataDefinition sdd= new SharedDataDefinition();
        return dsd;
    }

}
