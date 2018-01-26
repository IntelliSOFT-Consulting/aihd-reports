package org.openmrs.module.aihdreports.reports;

import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdreports.reporting.converter.EncounterDateConveter;
import org.openmrs.module.aihdreports.reporting.converter.GenderConverter;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.AgeConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
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
import org.openmrs.module.aihdreports.reporting.dataset.definition.SharedDataDefinition;
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
		SharedDataDefinition sdd= new SharedDataDefinition();
		String followUpEncounter = Metadata.EncounterType.DM_FOLLOWUP;
		PatientIdentifierType patientId = CoreUtils.getPatientIdentifierType(Metadata.Identifier.PATIENT_ID);
		DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
		DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(patientId.getName(), patientId), identifierFormatter);

		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


		dsd.addColumn("id", new PersonIdDataDefinition(), "");
		dsd.addColumn("Date", sdd.encounterDefinition("Date", followUpEncounter), "", new EncounterDateConveter());
		dsd.addColumn("Patient No", identifierDef, "");
		dsd.addColumn("Names", nameDef, "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "", new GenderConverter());
		dsd.addColumn("Age", new AgeDataDefinition(), "", new AgeConverter());
		dsd.addColumn("Weight", sdd.obsDdefinition("Weight",  Dictionary.getConcept(Dictionary.WEIGHT)), "", new ObsDataConverter());
		dsd.addColumn("Height", sdd.obsDdefinition("Height",  Dictionary.getConcept(Dictionary.HEIGHT)), "", new ObsDataConverter());

		return dsd;
	}

}