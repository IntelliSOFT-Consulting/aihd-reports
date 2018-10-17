/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.aihdreports.reports;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.module.aihdreports.reporting.library.dimension.CommonDimension;
import org.openmrs.module.aihdreports.reporting.library.indicator.MonthlyReporting;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.metadata.Metadata;
import org.openmrs.module.aihdreports.reporting.utils.ColumnParameters;
import org.openmrs.module.aihdreports.reporting.utils.EmrReportingUtils;
import org.openmrs.module.aihdreports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
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

/**
 * Created by Nicholas Ingosi on 7/27/17.
 */
@Component
public class MonthlyReportingReport extends AIHDDataExportManager {
	
	@Autowired
	private MonthlyReporting indicators;
	
	@Autowired
	private CommonDimension commonDimension;
	
	/**
	 * @return the uuid for the report design for exporting to Excel
	 */
	@Override
	public String getExcelDesignUuid() {
		return "771ba924-0015-11e8-973d-eb520478643a";
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> l = new ArrayList<ReportDesign>();
		l.add(buildReportDesign(reportDefinition));
		return l;
	}
	
	/**
	 * Build the report design for the specified report, this allows a user to override the report
	 * design by adding properties and other metadata to the report design
	 * 
	 * @param reportDefinition
	 * @return The report design
	 */
	@Override
	public ReportDesign buildReportDesign(ReportDefinition reportDefinition) {
		return createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "monthlyReporting.xls");
	}
	
	@Override
	public String getUuid() {
		return "94ead1d2-0015-11e8-87c2-ebf60d915687";
	}
	
	@Override
	public String getName() {
		return "Monthly Reporting Form";
	}
	
	@Override
	public String getDescription() {
		return "Used to collect information on a monthly basis";
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.addParameters(getParameters());
		rd.addDataSetDefinition("M", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	private DataSetDefinition dataSetDefinition() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setParameters(getParameters());
		dsd.setName("M");
		
		String indParams = "startDate=${startDate},endDate=${endDate},location=${location}";
		//add dimensions to the dsd gender is needed
		dsd.addDimension("gender", ReportUtils.map(commonDimension.gender()));
		//add dimension for the age bands
		dsd.addDimension("age", ReportUtils.map(commonDimension.arvAgeBandsInYears()));
		
		//build the column parameters here for gender
		ColumnParameters female = new ColumnParameters("Female", "Female", "gender=F");
		ColumnParameters male = new ColumnParameters("Male", "Male", "gender=M");
		ColumnParameters totals = new ColumnParameters("totals", "Totals", "");

		//build the columns parameters here for type 1 for female
		ColumnParameters zeroTo18FemaleT1 = new ColumnParameters("0-18FT1", "0-18-F", "age=0-18|gender=F");
		ColumnParameters ninteenTo35FemaleT1 = new ColumnParameters("19-35FT1", "19-35-F", "age=19-35|gender=F");
		ColumnParameters thirty6PlusFemaleT1 = new ColumnParameters("36+FT1", "36+F", "age=36+|gender=F");

		//build the columns parameters here for type 1 for male
		ColumnParameters zeroTo18MaleT1 = new ColumnParameters("0-18MT1", "0-18-M", "age=0-18|gender=M");
		ColumnParameters ninteenTo35MaleT1 = new ColumnParameters("19-35MT1", "19-35-M", "age=19-35|gender=M");
		ColumnParameters thirty6PlusMaleT1 = new ColumnParameters("36+MT1", "36+M", "age=36+|gender=M");

		//build the columns parameters for type2 for female
		ColumnParameters zeroTo18FemaleT2 = new ColumnParameters("0-18FT2", "0-18-F", "age=0-18|gender=F");
		ColumnParameters ninteenTo35FemaleT2 = new ColumnParameters("19-35FT2", "19-35-F", "age=19-35|gender=F");
		ColumnParameters thirty6To60FemaleT2 = new ColumnParameters("36-60FT2", "36-60-F", "age=36-60|gender=F");
		ColumnParameters sixtyPlusFemaleT2 = new ColumnParameters("60+FT2", "60+F", "age=60+|gender=F");
		//build the columns parameters for type2 for male
		ColumnParameters zeroTo18MaleT2 = new ColumnParameters("0-18MT2", "0-18-M", "age=0-18|gender=M");
		ColumnParameters ninteenTo35MaleT2 = new ColumnParameters("19-35MT2", "19-35-M", "age=19-35|gender=M");
		ColumnParameters thirty6To60MaleT2 = new ColumnParameters("36-60MT2", "36-60-M", "age=36-60|gender=M");
		ColumnParameters sixtyPlusMaleT2 = new ColumnParameters("60+MT2", "60+M", "age=60+|gender=M");


		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumnsGender = Arrays.asList(female, male, totals);
		List<ColumnParameters> allColumnsT1 = Arrays.asList(zeroTo18FemaleT1, ninteenTo35FemaleT1, thirty6PlusFemaleT1, zeroTo18MaleT1, ninteenTo35MaleT1, thirty6PlusMaleT1, totals);
		List<ColumnParameters> allColumnsT2 = Arrays.asList(zeroTo18FemaleT2, ninteenTo35FemaleT2, thirty6To60FemaleT2, zeroTo18MaleT2, ninteenTo35MaleT2, thirty6To60MaleT2, sixtyPlusFemaleT2, sixtyPlusMaleT2, totals);
		
		//build the design here, all patients in the system with different aggregation
		//number of patients diabetic patients
		Concept diabeticType = Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC);
		Concept diabeticT1 = Dictionary.getConcept(Dictionary.TYPE_1_DIABETES);
		Concept diabeticT2 = Dictionary.getConcept(Dictionary.TYPE_2_DIABETES);

		EmrReportingUtils.addRow(dsd, "NDP", "Number of diabetic patients", ReportUtils.map(indicators.numberOfDiabeticPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "FV", "First Visit", ReportUtils.map(indicators.numberOfPatientsWithEncounter(Metadata.EncounterType.DM_INITIAL), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "RV", "Return Visit", ReportUtils.map(indicators.numberOfPatientsWithEncounter(Metadata.EncounterType.DM_FOLLOWUP), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "NDC", "New diagnosis cases", ReportUtils.map(indicators.numberOfNewDiagnosedPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "TNT1", "Total number with type 1", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType, diabeticT1), indParams), allColumnsT1, Arrays.asList("01", "02", "03", "04", "05", "06", "07"));
		EmrReportingUtils.addRow(dsd, "TNT2", "Total number with type 2", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType, diabeticT2), indParams), allColumnsT2, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(dsd, "GDM", "GESTATIONAL DIABETES MELLITUS", ReportUtils.map(indicators.numberOfGdm(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		//a line will be added here to calculate those patients with diabetes caused by other factors
		EmrReportingUtils.addRow(dsd, "INS", "Number of patients on insulin", ReportUtils.map(indicators.numberOfPatientsOnInsulin(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "OGL", "Number of patients on insulin", ReportUtils.map(indicators.numberOfPatientsOnOglas(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "BOTH", "Number of patients on insulin", ReportUtils.map(indicators.numberOfPatientsOnInsAndOgl(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		
		EmrReportingUtils.addRow(dsd, "NHTN", "Total Number with New HTN ", ReportUtils.map(indicators.numberOfNewHtnPatients(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "KHTN", "Total Number with Known HTN ", ReportUtils.map(indicators.numberOfKnownHtnPatients(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "SDF", "Total Number Screened For Diabetic Foot ", ReportUtils.map(indicators.numberOfPatientsScreenedForDiabeticFoot(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "ADF", "Total Number Amputated Due To Diabetic Foot ", ReportUtils.map(indicators.numberOfAmputationDueToDiabeticFoot(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "DFU", "Total Number with Diabetic Foot Ulcer ", ReportUtils.map(indicators.numberOfPatientsWithDiabeticFootUlcer(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "KF", "Total Number with Kidney Failure ", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "EC", "Total Number with Eye Complications", ReportUtils.map(indicators.numberOfPatientsWithVisualImpairment(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "ED", "Total Number Educated On Diabetes ", ReportUtils.map(indicators.numberOfPatientsEducatedOnDiabetes(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));

		
		return dsd;
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
