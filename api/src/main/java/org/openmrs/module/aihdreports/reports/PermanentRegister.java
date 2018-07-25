package org.openmrs.module.aihdreports.reports;

import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdreports.reporting.calculation.TreatmentCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.address.AddressCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.address.PersonAttributeCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.diagnosis.ComplicationsCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.diagnosis.DiagnosisCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.diagnosis.YearOfDiagnosisCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.followup.PatientStatusCalculation;
import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefinition;
import org.openmrs.module.aihdreports.reporting.library.cohort.CommonCohortLibrary;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.aihdreports.reporting.converter.CalculationResultConverter;
import org.openmrs.module.aihdreports.reporting.calculation.EncounterDateCalculation;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.aihdreports.reporting.converter.GenderConverter;
import org.openmrs.module.aihdreports.data.converter.ObsDataConverter;
import org.openmrs.module.aihdreports.definition.dataset.definition.CalculationDataDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class PermanentRegister extends AIHDDataExportManager {

    @Autowired
    SharedDataDefinition sdd;

    @Autowired
    CommonCohortLibrary cohortLibrary;

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
        dsd.addParameters(getParameters());
        PatientIdentifierType patientId = CoreUtils.getPatientIdentifierType(Metadata.Identifier.PATIENT_ID);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientId), identifierFormatter);

        PatientIdentifierType patientPhoneNumber = CoreUtils.getPatientIdentifierType(Metadata.Identifier.PHONE_NUMBER);
		DataDefinition phoneNumberDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientPhoneNumber), identifierFormatter);

		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
        EncounterType initial = Context.getEncounterService().getEncounterTypeByUuid("2da542a4-f87d-11e7-8eb4-37dc291c1b12");
        EncounterType followUp = Context.getEncounterService().getEncounterTypeByUuid("bf3f3108-f87c-11e7-913d-5f679b8fdacb");
		dsd.addRowFilter(cohortLibrary.hasEncounter(initial, followUp), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}");


		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Date", encounterDate(), "", new CalculationResultConverter());
		dsd.addColumn("Patient No", identifierDef, "");
		dsd.addColumn("Names", nameDef, "");
		dsd.addColumn("dob", new BirthdateDataDefinition(), "", new BirthdateConverter());
        dsd.addColumn("Sex", new GenderDataDefinition(), "", new GenderConverter());
        dsd.addColumn("occupation", sdd.obsDataDefinition("occupation",  Dictionary.getConcept(Dictionary.OCCUPATION)), "", new ObsDataConverter());
        dsd.addColumn("telephone", phoneNumberDef, "");
        dsd.addColumn("subcounty", address("subcounty"), "", new CalculationResultConverter());
        dsd.addColumn("village", address("village"), "", new CalculationResultConverter());
        dsd.addColumn("landmark", address("landmark"), "", new CalculationResultConverter());
        dsd.addColumn("tsn", personAttributes("14d07597-d618-4f58-baab-d921e43f0a4c"), "", new CalculationResultConverter());
        dsd.addColumn("cts", personAttributes("9fe7f9c2-877c-4209-83f1-abeba41b80a7"), "", new CalculationResultConverter());
        dsd.addColumn("diagnosis", diagnosis(), "", new CalculationResultConverter());
        dsd.addColumn("diagnosis_year", diagnosis_year(), "", new CalculationResultConverter());
        dsd.addColumn("complications", complications(), "", new CalculationResultConverter());
        dsd.addColumn("treatment", treatment(), "", new CalculationResultConverter());
        dsd.addColumn("tsn", personAttributes("14d07597-d618-4f58-baab-d921e43f0a4c"), "", new CalculationResultConverter());
        dsd.addColumn("cts", personAttributes("9fe7f9c2-877c-4209-83f1-abeba41b80a7"), "", new CalculationResultConverter());
        dsd.addColumn("nhif", sdd.obsDataDefinition("nhif",  Dictionary.getConcept(Dictionary.NHIF_MEMBER)), "", new ObsDataConverter());
        dsd.addColumn("status", patientStatus(), "", new CalculationResultConverter());
        return dsd;
    }
    
	private DataDefinition encounterDate(){
		CalculationDataDefinition cd = new CalculationDataDefinition("Date", new EncounterDateCalculation());
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		return cd;
	}

	private DataDefinition address(String which){
        CalculationDataDefinition cd = new CalculationDataDefinition("address"+which, new AddressCalculation());
        cd.addCalculationParameter("which", which);
        return cd;
    }

    private DataDefinition personAttributes(String uuid){
        CalculationDataDefinition cd = new CalculationDataDefinition("attributes"+uuid, new PersonAttributeCalculation());
        cd.addCalculationParameter("uuid", uuid);
        return cd;
    }

    private DataDefinition diagnosis(){
        CalculationDataDefinition cd = new CalculationDataDefinition("diagnosis", new DiagnosisCalculation());
        return cd;
    }
    private DataDefinition diagnosis_year(){
        CalculationDataDefinition cd = new CalculationDataDefinition("diagnosis_year", new YearOfDiagnosisCalculation());
        return cd;
    }

    private DataDefinition diagnosis_year(){
        CalculationDataDefinition cd = new CalculationDataDefinition("diagnosis_year", new YearOfDiagnosisCalculation());
        return cd;
    }
    private DataDefinition patientStatus(){
        CalculationDataDefinition cd = new CalculationDataDefinition("status", new PatientStatusCalculation());
        return cd;
    }
    private DataDefinition treatment(){
        CalculationDataDefinition cd = new CalculationDataDefinition("treatment", new TreatmentCalculation());
        return cd;
    }
    private DataDefinition complications(){
        CalculationDataDefinition cd = new CalculationDataDefinition("complications", new ComplicationsCalculation());
        return cd;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date",Date.class),
                new Parameter("locationList", "Facility", Location.class)
        );
    }
}
