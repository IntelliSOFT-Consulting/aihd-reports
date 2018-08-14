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
        return cohortIndicator("Diabetic Patients", ReportUtils.map(cohorts.hasObsOnLocation(diabeticQuestion, newDmPatient, knownDmPatient), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * Number of patients who have encounters
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsWithEncounter(String uuid){
        EncounterType type = CoreUtils.getEncounterType(uuid);
        return cohortIndicator("Patients with encounter", ReportUtils.map(common.hasEncounter(type), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * Number of new diagnosed patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfNewDiagnosedPatients(){
        return cohortIndicator("New diagnosed patients", ReportUtils.map(cohorts.newDiagnosedCases(), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * Type of diabetic
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsPerDiabetiType(Concept question, Concept answer) {
       return cohortIndicator("Per diabetic", ReportUtils.map(cohorts.hasObsOnLocation(question, answer), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * The number of GDM
     * @return
     */
    public CohortIndicator numberOfGdm() {
        Concept question = Dictionary.getConcept(Dictionary.TYPE_OF_DIABETIC);
        Concept ans = Dictionary.getConcept(Dictionary.GDM);
        return cohortIndicator("numGdm", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * numberOfPatientsOnInsulin()
     */
    public CohortIndicator numberOfPatientsOnInsulin() {
        Concept question = Dictionary.getConcept("f5b23ca2-ee78-4fa8-915d-8c81c8c9d8bd");
        Concept ans = Dictionary.getConcept("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return cohortIndicator("onInsulin", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * numberOfPatientsOnOglas
     */
    public CohortIndicator numberOfPatientsOnOglas() {

        Concept question = Dictionary.getConcept("2bac6fca-19d0-4e4b-99a7-4bb30f40470b");
        Concept ans = Dictionary.getConcept("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return cohortIndicator("onoglas", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * numberOfPatientsOnInsAndOgl
     */
    public CohortIndicator numberOfPatientsOnInsAndOgl(){
        Concept question = Dictionary.getConcept("6b90a512-39a4-4eac-a6dc-12b54932f536");
        Concept ans = Dictionary.getConcept("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return cohortIndicator("onInsulin+Oglas", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfNewHtnPatients
     */
    public CohortIndicator numberOfNewHtnPatients(){
        Concept question = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept ans = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);
        return cohortIndicator("newHtn", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfKnownHtnPatients
     */
    public CohortIndicator numberOfKnownHtnPatients(){
        Concept question = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept ans = Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT);
        return cohortIndicator("knownHtn", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }
    /**
     * numberOfPatientsScreenedForDiabeticFoot
     */
    public CohortIndicator numberOfPatientsScreenedForDiabeticFoot(){
        Concept question = Dictionary.getConcept(Dictionary.PHYSICAL_EXAM);
        Concept ans = Dictionary.getConcept(Dictionary.FOOT_EXAM);
        return cohortIndicator("diabeticFootScreened", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfAmputationDueToDiabeticFoot
     */
    public CohortIndicator numberOfAmputationDueToDiabeticFoot(){
        Concept question = Dictionary.getConcept(Dictionary.FOOT_AMPUTATION);
        Concept ans = Dictionary.getConcept(Dictionary.YES);
        return cohortIndicator("diabeticFootAmputation", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfPatientsWithDiabeticFootUlcer
     */
    public CohortIndicator numberOfPatientsWithDiabeticFootUlcer(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.FOOT_ULCER);
        return cohortIndicator("diabeticFootUlcer", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfPatientsWithKidneyFailure
     */
    public CohortIndicator numberOfPatientsWithKidneyFailure(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.KIDNEY_FAILURE);
        return cohortIndicator("withKidneyFailure", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfPatientsWithVisualImpairment
     */
    public CohortIndicator numberOfPatientsWithVisualImpairment(){
        Concept question = Dictionary.getConcept(Dictionary.PROBLEM_ADDED);
        Concept ans = Dictionary.getConcept(Dictionary.VISUAL_IMPAIRMENT);
        return cohortIndicator("withEyeComplications", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * numberOfPatientsEducatedOnDiabetes
     */
    public CohortIndicator numberOfPatientsEducatedOnDiabetes(){
        Concept question = Dictionary.getConcept(Dictionary.EDUCATION_COUNSELING_ORDERS);
        Concept ans = Dictionary.getConcept(Dictionary.DIABETES);
        return cohortIndicator("educatedOnDiabetes", ReportUtils.map(cohorts.hasObsOnLocation(question, ans), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }

    /**
     * Number of Hypertension patients
     * @return CohortIndicator
     */
    public CohortIndicator numberOfhypertensionPatients(){
        Concept htnQuestion = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept newHtnPatient = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);
        Concept knownHtnPatient = Dictionary.getConcept(Dictionary.KNOWN_HYPERTENSION_PATIENT);
        return cohortIndicator("HTN Patients", ReportUtils.map(cohorts.hasObsOnLocation(htnQuestion, newHtnPatient, knownHtnPatient), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));
    }

    /**
     * Number of patients that match a given question and number of possible answers
     * @return CohortIndicator
     */
    public CohortIndicator numberOfPatientsPerQuestionAndSetOfAnswers(Concept q, Concept ... a){

        return cohortIndicator("Question "+q.getName().getName(), ReportUtils.map(cohorts.hasObsOnLocation(q, a), "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${locationList}"));

    }
}
