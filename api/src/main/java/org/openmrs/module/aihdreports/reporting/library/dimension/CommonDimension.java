package org.openmrs.module.aihdreports.reporting.library.dimension;

import org.openmrs.module.aihdreports.reporting.library.cohort.CommonCohortLibrary;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.aihdreports.reporting.utils.ReportUtils.map;

/**
 * Created by Nicholas Ingosi on 6/20/17.
 */
@Component
public class CommonDimension {
	
	@Autowired
	private CommonCohortLibrary commonLibrary;
	
	/**
	 * Gender dimension
	 * 
	 * @return the dimension
	 */
	public CohortDefinitionDimension gender() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("gender");
		dim.addCohortDefinition("M", map(commonLibrary.males()));
		dim.addCohortDefinition("F", map(commonLibrary.females()));
		return dim;
	}
	
	/**
	 * Dimension of age for patients on ARV of different age group in years
	 * 
	 * @return CohortDefinitionDimension
	 */
	public CohortDefinitionDimension arvAgeBandsInYears() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("age group (0-18,19-35, >=36, 36-60, >60)");
		dim.addParameter(new Parameter("onDate", "Date", Date.class));
		dim.addCohortDefinition("0-18", map(commonLibrary.agedAtLeastAgedAtMost(0, 18), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("19-35", map(commonLibrary.agedAtLeastAgedAtMost(19, 35), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("36+", map(commonLibrary.agedAtLeast(35), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("36-60", map(commonLibrary.agedAtLeastAgedAtMost(36, 60), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("61+", map(commonLibrary.agedAtLeast(61), "effectiveDate=${onDate}"));
		return dim;
	}
	
}
