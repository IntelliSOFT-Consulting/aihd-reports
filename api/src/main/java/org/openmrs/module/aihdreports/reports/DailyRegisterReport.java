package org.openmrs.module.aihdreports.reports;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.aihdreports.data.converter.ObsDataConverter;
import org.openmrs.module.aihdreports.definition.dataset.definition.CalculationDataDefinition;
import org.openmrs.module.aihdreports.reporting.calculation.BmiCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.EncounterDateCalculation;
import org.openmrs.module.aihdreports.reporting.calculation.InitialReturnVisitCalculation;
import org.openmrs.module.aihdreports.reporting.converter.*;
import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefinition;
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
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class DailyRegisterReport extends AIHDDataExportManager {

	@Autowired
	SharedDataDefinition sdd;
	
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
		PatientIdentifierType patientId = CoreUtils.getPatientIdentifierType(Metadata.Identifier.PATIENT_ID);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientId), identifierFormatter);
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Date", encounterDate(), "", new CalculationResultConverter());
		dsd.addColumn("Patient No", identifierDef, "");
		dsd.addColumn("Names", nameDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", new GenderConverter());
		dsd.addColumn("Age", new AgeDataDefinition(), "", new AgeConverter());
		dsd.addColumn("fvrv", firstOrRevisit(), "", new CalculationResultConverter());
		dsd.addColumn("newlyDiagnosed", sdd.obsDataDefinition("newlyDiagnosed",  Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE)), "", new NewlyDiagnosedConverter());
		dsd.addColumn("systolic", sdd.obsDataDefinition("systolic",  Dictionary.getConcept(Dictionary.SYSTOLIC_BLOOD_PRESSURE)), "", new ObsDataConverter());
		dsd.addColumn("diastolic", sdd.obsDataDefinition("diastolic",  Dictionary.getConcept(Dictionary.DIASTOLIC_BLOOD_PRESSURE)), "", new ObsDataConverter());
		dsd.addColumn("htn", sdd.obsDataDefinition("htn",  Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE)), "", new HtnDataConverter());
		dsd.addColumn("wc", sdd.obsDataDefinition("wc",  Dictionary.getConcept(Dictionary.WAIST_CIRCUMFERENCE)), "", new ObsDataConverter());
		dsd.addColumn("weight", sdd.obsDataDefinition("weight",  Dictionary.getConcept(Dictionary.WEIGHT)), "", new ObsDataConverter());
		dsd.addColumn("height", sdd.obsDataDefinition("height",  Dictionary.getConcept(Dictionary.HEIGHT)), "", new ObsDataConverter());
		dsd.addColumn("bmi", bmi(), "", new CalculationResultConverter());
		dsd.addColumn("rbs", sdd.obsDataDefinition("rbs",  Dictionary.getConcept(Dictionary.RBS)), "", new ObsDataConverter());
		dsd.addColumn("fbs", sdd.obsDataDefinition("rbs",  Dictionary.getConcept(Dictionary.FBS)), "", new ObsDataConverter());
		dsd.addColumn("currentHbac", sdd.obsDataDefinition("currentHbac",  Dictionary.getConcept(Dictionary.HBA1C)), "", new ObsDataConverter());
		dsd.addColumn("diagnosis", sdd.obsDataDefinition("diagnosis",  Dictionary.getConcept(Dictionary.SYMPTOM)), "", new DiagnosisDataConverter());
		dsd.addColumn("complications", sdd.obsDataDefinition("complications",  Dictionary.getConcept(Dictionary.PROBLEM_ADDED)), "", new ComplicationsDataConverter());
		dsd.addColumn("treatment", sdd.obsDataDefinition("treatment",  Dictionary.getConcept(Dictionary.CURRENTLY_TAKING_MEDICATON)), "", new TreatmentDataConveter());
		dsd.addColumn("nhif", sdd.obsDataDefinition("nhif",  Dictionary.getConcept(Dictionary.NHIF_MEMBER)), "", new NhifDataConverter());
		dsd.addColumn("admitted_referred", sdd.obsDataDefinition("admitted_referred",  Dictionary.getConcept(Dictionary.ADMITTED_REFERED)), "", new ObsDataConverter());
		dsd.addColumn("next_appointment", sdd.obsDataDefinition("next_appointment",  Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE)), "", new ObsDataConverter());

		return dsd;
	}

	private DataDefinition encounterDate(){
		CalculationDataDefinition cd = new CalculationDataDefinition("Date", new EncounterDateCalculation());
		return cd;
	}
	private DataDefinition firstOrRevisit(){
		CalculationDataDefinition cd = new CalculationDataDefinition("fvrv", new InitialReturnVisitCalculation());
		return cd;
	}

	private DataDefinition bmi(){
		CalculationDataDefinition cd = new CalculationDataDefinition("bmi", new BmiCalculation());
		return cd;
	}

}