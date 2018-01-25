package org.openmrs.module.aihdreports.reports;

import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdreports.reporting.converter.EncounterDateConveter;
import org.openmrs.module.aihdreports.reporting.converter.GenderConverter;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;
import org.openmrs.module.aihdreports.data.converter.ObsDataConverter;
import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefintion;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Component
public class DailyRegisterReport extends AIHDDataExportManager {
	
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
		PatientIdentifierType patientId = Context.getPatientService().getPatientIdentifierTypeByUuid("b9ba3418-7108-450c-bcff-7bc1ed5c42d1");
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientId), identifierFormatter);

		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
		SharedDataDefintion sdd= new SharedDataDefintion();

		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Date", encounterDateDataDefinition(), "", new EncounterDateConveter());
		dsd.addColumn("Patient No", identifierDef, "");
		dsd.addColumn("Names", nameDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", new GenderConverter());
		dsd.addColumn("Age", new AgeDataDefinition(), "", new AgeConverter());
		dsd.addColumn("Weight", sdd.definition("Weight",  Dictionary.getConcept("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")), "", new ObsDataConverter());
		dsd.addColumn("Height", sdd.definition("Height",  Dictionary.getConcept("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")), "", new ObsDataConverter());

		return dsd;
	}

	private DataDefinition encounterDateDataDefinition(){
		EncountersForPatientDataDefinition encounterDataDefinition = new EncountersForPatientDataDefinition();
		encounterDataDefinition.setWhich(TimeQualifier.LAST);
		encounterDataDefinition.addType(Context.getEncounterService().getEncounterTypeByUuid("2da542a4-f87d-11e7-8eb4-37dc291c1b12"));
		return encounterDataDefinition;
	}
}