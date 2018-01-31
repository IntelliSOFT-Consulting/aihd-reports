package org.openmrs.module.aihdreports.reporting.library.indicator;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.module.aihdreports.reporting.library.cohort.CommonCohortLibrary;
import org.openmrs.module.aihdreports.reporting.library.cohort.MonthlyReportingCohort;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.aihdreports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.aihdreports.reporting.utils.EmrReportingUtils.cohortIndicator;

@Component
public class MonthlyReporting {

    @Autowired
    private MonthlyReportingCohort cohorts;

    @Autowired
    private CommonCohortLibrary common;

    /**
     * Number of diabetic patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfDiabeticPatients(){
        Concept diabeticQuestion = Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE);
        Concept newDmPatient = Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT);
        Concept knownDmPatient = Dictionary.getConcept(Dictionary.KNOWN_DAIBETIC_PATIENT);
        return cohortIndicator("Diabetic Patients", ReportUtils.map(common.hasObs(diabeticQuestion, newDmPatient, knownDmPatient), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }

    /**
     * Number of patients who have encounters
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsWithEncounter(String uuid){
        EncounterType type = CoreUtils.getEncounterType(uuid);
        return cohortIndicator("Patients with encounter", ReportUtils.map(common.hasEncounter(type), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }

    /**
     * Number of new diagnosed patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfNewDiagnosedPatients(){
        return cohortIndicator("New diagnosed patients", ReportUtils.map(cohorts.newDiagnosedCases(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }

    /**
     * Type of diabetic
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsPerDiabetiType(Concept question, Concept answer) {
       return cohortIndicator("Per diabetic", ReportUtils.map(common.hasObs(question, answer), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
}
