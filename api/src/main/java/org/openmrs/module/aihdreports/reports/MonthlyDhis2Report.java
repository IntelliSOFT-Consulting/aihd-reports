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

import org.openmrs.Location;
import org.openmrs.module.aihdreports.reporting.library.dimension.CommonDimension;
import org.openmrs.module.aihdreports.reporting.library.indicator.MonthlyReporting;
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


@Component
public class MonthlyDhis2Report extends AIHDDataExportManager {
	
	@Autowired
	private MonthlyReporting indicators;
	
	@Autowired
	private CommonDimension commonDimension;
	
	/**
	 * @return the uuid for the report design for exporting to Excel
	 */
	@Override
	public String getExcelDesignUuid() {
		return "631ebf7a-e000-4220-9038-e70079ceafca";
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
		return createExcelTemplateDesign(getExcelDesignUuid(), reportDefinition, "monthlyDhis2Reporting.xls");
	}
	
	@Override
	public String getUuid() {
		return "6cd0b441-d644-487c-8466-5820a73f9ce5";
	}
	
	@Override
	public String getName() {
		return "Monthly DHIS2 Reporting Form";
	}
	
	@Override
	public String getDescription() {
		return "Used to collect dhis2 information on a monthly basis";
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.addParameters(getParameters());
		rd.addDataSetDefinition("MD", Mapped.mapStraightThrough(dataSetDefinition()));
		return rd;
	}
	
	@Override
	public String getVersion() {
		return "0.1";
	}
	
	private DataSetDefinition dataSetDefinition() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setParameters(getParameters());
		dsd.setName("MD");

		String indParams = "startDate=${startDate},endDate=${endDate},location=${location}";
		//add dimensions to the dsd gender is needed
		dsd.addDimension("gender", ReportUtils.map(commonDimension.gender()));
					
		//build the column parameters here for gender
		ColumnParameters female = new ColumnParameters("Female", "Female", "gender=F");
		ColumnParameters male = new ColumnParameters("Male", "Male", "gender=M");
		ColumnParameters totals = new ColumnParameters("totals", "Totals", "");


		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumnsGender = Arrays.asList(female, male, totals);

		//build the design here, all patients in the system with different aggregation


		EmrReportingUtils.addRow(dsd, "NDP", "Number of diabetic patients", ReportUtils.map(indicators.numberOfDiabeticPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));
		EmrReportingUtils.addRow(dsd, "NHP", "Number of hypetension patients", ReportUtils.map(indicators.numberOfhypertensionPatients(), indParams), allColumnsGender, Arrays.asList("01","02", "03"));

		
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
