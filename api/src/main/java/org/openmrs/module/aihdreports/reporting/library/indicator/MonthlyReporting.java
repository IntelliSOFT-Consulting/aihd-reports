package org.openmrs.module.aihdreports.reporting.library.indicator;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
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
        return cohortIndicator("Diabetic Patients", ReportUtils.map(cohorts.hasObsOnLocation(diabeticQuestion, newDmPatient, knownDmPatient), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

   /* *//**
     * Number of patients who have encounters
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsWithEncounter(String uuid){
        EncounterType type = CoreUtils.getEncounterType(uuid);
        return cohortIndicator("Patients with encounter", ReportUtils.map(cohorts.hasEncounterPerLocation(type), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

    /**
     * Number of new diagnosed patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfNewDiagnosedPatients(){
        return cohortIndicator("New diagnosed patients", ReportUtils.map(cohorts.newDiagnosedCases(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

    /**
     * Type of diabetic
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsPerDiabetiType(Concept question, Concept answer) {
        return cohortIndicator("Per diabetic", ReportUtils.map(cohorts.hasObsOnLocation(question, answer), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

    /**
     * numberOfPatientsOnInsulin()
     */
    public CohortIndicator numberOfPatientsOnInsulin() {
        return cohortIndicator("onInsulin", ReportUtils.map(cohorts.onInsulinOnly(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

    /**
     * numberOfPatientsOnOglas
     */
    public CohortIndicator numberOfPatientsOnOglas() {
        return cohortIndicator("onoglas", ReportUtils.map(cohorts.onOglasOnly(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }

    /**
     * numberOfPatientsOnInsAndOgl
     */
    public CohortIndicator numberOfPatientsOnInsAndOgl(){
        Concept question = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
        Concept both = Dictionary.getConcept(Dictionary.BOTH_OGLAS_INSULIN);
        return cohortIndicator("onInsulin+Oglas", ReportUtils.map(cohorts.hasObsOnLocation(question, both), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfNewHtnPatients
     */
    public CohortIndicator numberOfNewHtnPatients(){
        Concept question = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept ans = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);
        return cohortIndicator("newHtn", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfKnownHtnPatients
     */
    public CohortIndicator numberOfKnownHtnPatients(){
        Concept question = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept ans = Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT);
        return cohortIndicator("knownHtn", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }
    /**
     * numberOfPatientsScreenedForDiabeticFoot
     */
    public CohortIndicator numberOfPatientsScreenedForDiabeticFoot(){
        Concept question = Dictionary.getConcept(Dictionary.PHYSICAL_EXAM);
        Concept ans = Dictionary.getConcept(Dictionary.FOOT_EXAM);
        return cohortIndicator("diabeticFootScreened", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfAmputationDueToDiabeticFoot
     */
    public CohortIndicator numberOfAmputationDueToDiabeticFoot(){
        return cohortIndicator("diabeticFootAmputation", ReportUtils.map(cohorts.diabeticFootOptionsWithLocation(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfPatientsWithDiabeticFootUlcer
     */
    public CohortIndicator numberOfPatientsWithDiabeticFootUlcer(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.FOOT_ULCER);
        return cohortIndicator("diabeticFootUlcer", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfPatientsWithKidneyFailure
     */
    public CohortIndicator numberOfPatientsWithKidneyFailure(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.KIDNEY_FAILURE);
        return cohortIndicator("withKidneyFailure", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfPatientsWithVisualImpairment
     */
    public CohortIndicator numberOfPatientsWithVisualImpairment(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.VISUAL_IMPAIRMENT);
        return cohortIndicator("withEyeComplications", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfPatientsEducatedOnDiabetes
     */
    public CohortIndicator numberOfPatientsEducatedOnDiabetes(){
        Concept question = Dictionary.getConcept(Dictionary.EDUCATION_COUNSELING_ORDERS);
        Concept ans = Dictionary.getConcept(Dictionary.DIABETES);
        return cohortIndicator("educatedOnDiabetes", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * Number of Hypertension patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfhypertensionPatients(){
        Concept htnQuestion = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept newHtnPatient = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);
        Concept knownHtnPatient = Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT);
        return cohortIndicator("HTN Patients", ReportUtils.map(cohorts.hasObsOnLocation(htnQuestion, newHtnPatient, knownHtnPatient), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    }



    /**
     * Number of patients that match a given question and number of possible answers
     * @return CohortIndicator
     */

    public CohortIndicator numberOfPatientsPerQuestionAndSetOfAnswers(Concept q, Concept ... a){

        return cohortIndicator("Question "+q.getName().getName(), ReportUtils.map(cohorts.hasObsOnLocation(q, a), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }

    /**
     * numberOfPatientsWithCodedValuesAndAnswer
     */

    public CohortIndicator numberOfPatientsWithCodedValuesAndAnswer(Concept question, Concept ... ans){
        return cohortIndicator("", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * havingValueNumericObsHbA1cWithLocation
     */

    public CohortIndicator havingValueNumericObsHbA1cWithLocation(){
        return cohortIndicator("HbA1c", ReportUtils.map(cohorts.havingValueNumericObsHbA1cWithLocation(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * havingValueNumericObsHbA1cWithLocationLes7
     */

    public CohortIndicator havingValueNumericObsHbA1cWithLocationLes7(){
        return cohortIndicator("HbA1cLess7", ReportUtils.map(cohorts.havingValueNumericObsHbA1cWithLocationLes7(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * havingValueNumericObsPressure
     */

    public CohortIndicator havingValueNumericObsPressure(){
        return cohortIndicator("highPressure", ReportUtils.map(cohorts.havingValueNumericObsPressure(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * footUlcers
     */

    public CohortIndicator footUlcers(){
        return cohortIndicator("footUlcers", ReportUtils.map(cohorts.footUlcers(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * foot options
     */

    public CohortIndicator combinedFootOptionsWithLocation(){
        return cohortIndicator("combinedFootOptionsWithLocation", ReportUtils.map(cohorts.combinedFootOptionsWithLocation(), "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));

    }


    /**
     * cause of death for a patient
     * @param concept
     */

    public CohortIndicator CauseOfDeath(Concept concept){
        return cohortIndicator("causeOfDeath", ReportUtils.map(cohorts.getDeadPatients(concept.getConceptId()), "startDate=${startDate},endDate=${endDate},location=${location}"));

    }


}