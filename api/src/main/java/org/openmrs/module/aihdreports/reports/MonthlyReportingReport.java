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

import org.openmrs.module.aihdreports.reporting.library.dimension.CommonDimension;
import org.openmrs.module.aihdreports.reporting.library.indicator.MonthlyReporting;
import org.openmrs.module.aihdreports.reporting.utils.ColumnParameters;
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
		
		String indParams = "startDate=${startDate},endDate=${endDate}";
		//add dimensions to the dsd gender is needed
		dsd.addDimension("gender", ReportUtils.map(commonDimension.gender()));
		
		//bulid the column parameters here
		ColumnParameters female = new ColumnParameters("female", "Female", "gender=F");
		ColumnParameters male = new ColumnParameters("Male", "Male", "gender=M");
		ColumnParameters total = new ColumnParameters("Total", "Total", "");
		
		//form columns as list to be used in the dsd
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		
		//build the design here, all patients in the system with different aggregation
		
		return dsd;
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class));
	}
}
