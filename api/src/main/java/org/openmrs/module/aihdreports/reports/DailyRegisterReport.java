package org.openmrs.module.aihdreports.reports;

import org.openmrs.Location;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdreports.data.converter.ObsDataConverter;
import org.openmrs.module.aihdreports.definition.dataset.definition.CalculationDataDefinition;
import org.openmrs.module.aihdreports.reporting.calculation.BmiCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.EncounterDateCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.FootCareOutcomeCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.InitialReturnVisitCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.TreatmentCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.ComplicationsCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.DiagnosisCalculation;
import org.openmrs.module.aihdreports.reporting.converter.*;
import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefinition;
import org.openmrs.module.aihdreports.reporting.library.cohort.CommonCohortLibrary;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class DailyRegisterReport extends AIHDDataExportManager {

	@Autowired
	SharedDataDefinition sdd;

	@Autowired
	CommonCohortLibrary cohortLibrary;

	@Override
	public String getExcelDesignUuid() {
		return "0ce3048c-0018-11e8-9821-a73b4f1d38df";
	}

	@Override
	public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
		ReportDesign rd= createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "daily.xls");
		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,row:7,dataset:D");
		props.put("sortWeight", "5000");
		rd.setProperties(props);
		return rd;
	}

	@Override
	public String getUuid() {
		return "fed89032-0017-11e8-9580-f731617cc228";
	}

	@Override
	public String getName() {
		return "Daily Register";
	}

	@Override
	public String getDescription() {
		return "Collect daily information";
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
		rd.addDataSetDefinition("D", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}

	private DataSetDefinition dataSetDefinition() {
		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.addParameters(getParameters());
		PatientIdentifierType patientId = CoreUtils.getPatientIdentifierType(Metadata.Identifier.PATIENT_ID);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientId), identifierFormatter);
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		dsd.addRowFilter(cohortLibrary.hasEncounter(CoreUtils.getEncounterType(Metadata.EncounterType.DM_FOLLOWUP), CoreUtils.getEncounterType(Metadata.EncounterType.DM_INITIAL)), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${location}");


		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Date", encounterDate(), "onDate=${endDate}", new CalculationResultConverter());
		dsd.addColumn("Patient No", identifierDef, "");
		dsd.addColumn("Names", nameDef, "");
		dsd.addColumn("Age", new AgeDataDefinition(), "", new AgeConverter());
		dsd.addColumn("Sex", new GenderDataDefinition(), "", new GenderConverter());
		dsd.addColumn("fvrv", firstOrRevisit(), "onDate=${endDate}", new CalculationResultConverter());
		dsd.addColumn("weight", sdd.obsDataDefinition("weight",  Dictionary.getConcept(Dictionary.WEIGHT)), "", new ObsDataConverter());
		dsd.addColumn("height", sdd.obsDataDefinition("height",  Dictionary.getConcept(Dictionary.HEIGHT)), "", new ObsDataConverter());
		dsd.addColumn("bmi", bmi(), "", new CalculationResultConverter());
		dsd.addColumn("wc", sdd.obsDataDefinition("wc",  Dictionary.getConcept(Dictionary.WAIST_CIRCUMFERENCE)), "", new ObsDataConverter());
		dsd.addColumn("systolic", sdd.obsDataDefinition("systolic",  Dictionary.getConcept(Dictionary.SYSTOLIC_BLOOD_PRESSURE)), "", new ObsDataConverter());
		dsd.addColumn("diastolic", sdd.obsDataDefinition("diastolic",  Dictionary.getConcept(Dictionary.DIASTOLIC_BLOOD_PRESSURE)), "", new ObsDataConverter());
		dsd.addColumn("htn", sdd.obsDataDefinition("htn",  Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE)), "", new HtnDataConverter());
		dsd.addColumn("newlyDiagnosed", sdd.obsDataDefinition("newlyDiagnosed",  Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE)), "", new NewlyDiagnosedConverter());
		dsd.addColumn("rbs", sdd.obsDataDefinition("rbs",  Dictionary.getConcept(Dictionary.RBS)), "", new ObsDataConverter());
		dsd.addColumn("fbs", sdd.obsDataDefinition("fbs",  Dictionary.getConcept(Dictionary.FBS)), "", new ObsDataConverter());
		dsd.addColumn("currentHbac", sdd.obsDataDefinition("currentHbac",  Dictionary.getConcept(Dictionary.HBA1C)), "", new ObsDataConverter());
		dsd.addColumn("diagnosis", diagnosis(), "", new CalculationResultConverter());
		dsd.addColumn("complications", complications(), "", new CalculationResultConverter());
		dsd.addColumn("treatment", treatment(), "", new CalculationResultConverter());
		dsd.addColumn("footclinic", sdd.obsDataDefinition("footclinic",  Dictionary.getConcept(Dictionary.VISIT_TYPE)), "", new ObsDataConverter());
		dsd.addColumn("dfoot", sdd.obsDataDefinition("dfoot",  Dictionary.getConcept(Dictionary.DIABETIC_FOOT)), "", new ObsDataConverter());
		dsd.addColumn("friskass", sdd.obsDataDefinition("friskass",  Dictionary.getConcept(Dictionary.FOOT_RISK_ASSESSMENT)), "", new ObsDataConverter());
		dsd.addColumn("fcare", footCareOutcomes(), "", new CalculationResultConverter());
		dsd.addColumn("tbscreen", sdd.obsDataDefinition("tbscreen",  Dictionary.getConcept(Dictionary.SCREENED_FOR_TB)), "", new ObsDataConverter());
		dsd.addColumn("tbstatus", sdd.obsDataDefinition("tbstatus",  Dictionary.getConcept(Dictionary.TB_STATUS)), "", new ObsDataConverter());
		dsd.addColumn("nhif", sdd.obsDataDefinition("nhif",  Dictionary.getConcept(Dictionary.NHIF_MEMBER)), "", new ObsDataConverter());

		return dsd;
	}

	private DataDefinition encounterDate(){
		CalculationDataDefinition cd = new CalculationDataDefinition("Date", new EncounterDateCalculation());
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}
	private DataDefinition firstOrRevisit(){
		CalculationDataDefinition cd = new CalculationDataDefinition("fvrv", new InitialReturnVisitCalculation());
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	private DataDefinition bmi(){
		CalculationDataDefinition cd = new CalculationDataDefinition("bmi", new BmiCalculation());
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

	private DataDefinition diagnosis(){
		CalculationDataDefinition cd = new CalculationDataDefinition("diagnosis", new DiagnosisCalculation());
		return cd;
	}
	private DataDefinition footCareOutcomes(){
		CalculationDataDefinition cd = new CalculationDataDefinition("fcare", new FootCareOutcomeCalculation());
		return cd;
	}

	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(
				new Parameter("startDate", "Start Date", Date.class),
				new Parameter("endDate", "End Date",Date.class),
				new Parameter("location", "Facility", Location.class)
		);
	}


}