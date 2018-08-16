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
		dsd.addParameters(getParameters());
		dsd.setName("M");
		
		String indParams = "startDate=${startDate},endDate=${endDate},locationList=${locationList}";
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

		//columns specific for type 1 diabetes
		//female
		ColumnParameters zeroTo5FemaleT1 = new ColumnParameters("0-5FT1", "0-5-F", "age=0-5|gender=F");
		ColumnParameters sixTo18FemaleT1 = new ColumnParameters("6-18FT1", "6-18-F", "age=6-18|gender=F");
		ColumnParameters thirty5PlusFemaleT1 = new ColumnParameters("35+FT1", "35+F", "age=35+|gender=F");
		//male
		ColumnParameters zeroTo5MaleT1 = new ColumnParameters("0-5MT1", "0-5-M", "age=0-5|gender=M");
		ColumnParameters sixTo18MaleT1 = new ColumnParameters("6-18MT1", "6-18-M", "age=6-18|gender=M");
		ColumnParameters thirty5PlusMaleT1 = new ColumnParameters("35+MT1", "35+M", "age=35+|gender=M");




		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumnsGender = Arrays.asList(male, female, totals);
		List<ColumnParameters> allColumnsT1 = Arrays.asList(zeroTo5MaleT1, sixTo18MaleT1, ninteenTo35MaleT1, thirty5PlusMaleT1, zeroTo5FemaleT1, sixTo18FemaleT1, ninteenTo35FemaleT1, thirty5PlusFemaleT1, totals);
		List<ColumnParameters> allColumnsT2 = Arrays.asList(zeroTo18MaleT2, ninteenTo35MaleT2, thirty6To60MaleT2, sixtyPlusMaleT2, zeroTo18FemaleT2, ninteenTo35FemaleT2, thirty6To60FemaleT2, sixtyPlusFemaleT2, totals);
		
		//build the design here, all patients in the system with different aggregation
		//number of patients diabetic patients
		Concept diabeticType = Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC);
		Concept diabeticT1 = Dictionary.getConcept(Dictionary.TYPE_1_DIABETES);
		Concept diabeticT2 = Dictionary.getConcept(Dictionary.TYPE_2_DIABETES);
		//concepts for CVD
		Concept stroke = Dictionary.getConcept(Dictionary.STROKE);
		Concept ischemic_heart_disease = Dictionary.getConcept(Dictionary.Ischemic_heart_disease);
		Concept peripheral_vascular_or_artery_disease = Dictionary.getConcept(Dictionary.Peripheral_Vascular_disease);
		Concept heart_failure = Dictionary.getConcept(Dictionary.Heart_failure);
		Concept problem_added = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);

		//
		Concept gdm = Dictionary.getConcept(Dictionary.GDM);
		Concept secondary = Dictionary.getConcept(Dictionary.DIABETES_SECONDARY_TO_OTHER_CAUSES);

		//
		Concept medication_ordered = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
		Concept exercise = Dictionary.getConcept(Dictionary.PHYSICAL_EXERCISE);
		Concept hypertension = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
		Concept newHypertension = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);
		Concept knownHypertension = Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT);



		EmrReportingUtils.addRow(dsd, "NDP", "Number of diabetic patients", ReportUtils.map(indicators.numberOfDiabeticPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "NDC", "New diagnosis cases", ReportUtils.map(indicators.numberOfNewDiagnosedPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "FV", "First Visit", ReportUtils.map(indicators.numberOfPatientsWithEncounter(Metadata.EncounterType.DM_INITIAL), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "RV", "Return Visit", ReportUtils.map(indicators.numberOfPatientsWithEncounter(Metadata.EncounterType.DM_FOLLOWUP), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "TNT1", "Total number with type 1", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType, diabeticT1), indParams), allColumnsT1, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(dsd, "TNT2", "Total number with type 2", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType, diabeticT2), indParams), allColumnsT2, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(dsd, "GDMD", "Total No. diagnosed for Gestational Diabetes Mellitus", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType,gdm), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "DSOC", "Total No. of Diabetes secondary to other causes", ReportUtils.map(indicators.numberOfPatientsPerDiabetiType(diabeticType, secondary), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "INS", "Number of patients on insulin", ReportUtils.map(indicators.numberOfPatientsOnInsulin(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "OGL", "No. of patients on OGLAs", ReportUtils.map(indicators.numberOfPatientsOnOglas(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "BOTH", "No. of patients on both", ReportUtils.map(indicators.numberOfPatientsOnInsAndOgl(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "DAEO", "No. of patients on diet and exercise only", ReportUtils.map(indicators.numberOfPatientsWithCodedValuesAndAnswer(medication_ordered, exercise ), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NPOH", "No. of patients done HbA1c", ReportUtils.map(indicators.havingValueNumericObsHbA1cWithLocation(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "PHT7", "% that met HbA1c target (< 7%)", ReportUtils.map(indicators.havingValueNumericObsHbA1cWithLocationLes7(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NOHPOC", "No. of hypertensive patients on care", ReportUtils.map(indicators.numberOfPatientsWithCodedValuesAndAnswer(hypertension, newHypertension, knownHypertension), indParams), allColumnsT2, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"));
		EmrReportingUtils.addRow(dsd, "NHTN", "Total Number with New HTN", ReportUtils.map(indicators.numberOfPatientsWithCodedValuesAndAnswer(hypertension, newHypertension), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		/*EmrReportingUtils.addRow(dsd, "NWHBP", "No. with high BP (>= 140/90)", ReportUtils.map(indicators.numberOfNewHtnPatients(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NPWCVDS", "No. of patients with CVD - Stroke", ReportUtils.map(indicators.numberOfPatientsPerQuestionAndSetOfAnswers(problem_added, stroke), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NPWCVDI", "No. of patients with CVD - Heart Disease", ReportUtils.map(indicators.numberOfPatientsPerQuestionAndSetOfAnswers(problem_added, ischemic_heart_disease), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NPWCVDP", "No. of patients with CVD - Peripheral Vascular", ReportUtils.map(indicators.numberOfPatientsPerQuestionAndSetOfAnswers(problem_added, peripheral_vascular_or_artery_disease), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NPWCVDH", "No. of patients with CVD - Heart failure", ReportUtils.map(indicators.numberOfPatientsPerQuestionAndSetOfAnswers(problem_added, heart_failure), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));

		EmrReportingUtils.addRow(dsd, "NPWN", "No. of Patients with neuropathies", ReportUtils.map(indicators.numberOfKnownHtnPatients(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NSFDF", "Total Number Screened For Diabetic Foot", ReportUtils.map(indicators.numberOfPatientsScreenedForDiabeticFoot(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NDFU", "Total Number with Diabetic Foot Ulcer", ReportUtils.map(indicators.numberOfPatientsWithDiabeticFootUlcer(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NFSTT", "No. of feet saved through treatment", ReportUtils.map(indicators.numberOfAmputationDueToDiabeticFoot(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NADF", "Total Number Amputated Due To Diabetic Foot ", ReportUtils.map(indicators.numberOfAmputationDueToDiabeticFoot(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NWKC", "No. with kidney complications", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NWDR", "No. with diabetic retinopathy", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NSFT", "No. Screened for Tuberculosis", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NSPFT", "No. Screened Positive for Tuberculosis", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NTA", "Total admitted", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NAWDKA", "No. admitted with DKA", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NAWH", "No. admitted with Hypoglycemia", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NDDC", "Total deaths due to diabetes complications", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NDHC", "Total deaths due to hypertension complications", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		EmrReportingUtils.addRow(dsd, "NEWNHIF", "No. enrolled with NHIF", ReportUtils.map(indicators.numberOfPatientsWithKidneyFailure(), indParams), allColumnsGender, Arrays.asList("01", "02", "03"));
		*/return dsd;
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
