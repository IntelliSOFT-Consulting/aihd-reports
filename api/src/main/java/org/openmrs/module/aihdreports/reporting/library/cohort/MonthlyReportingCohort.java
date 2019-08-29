package org.openmrs.module.aihdreports.reporting.library.cohort;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.module.aihdreports.reporting.calculation.ValueTextObsCalculation;
import org.openmrs.module.aihdreports.reporting.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.queries.Queries;
import org.openmrs.module.aihdreports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.NumericObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MonthlyReportingCohort {

    @Autowired
    private CommonCohortLibrary commonCohortLibrary;

    public CohortDefinition getEncounters(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        cd.setName("Encounters");
        cd.addParameter(new Parameter("location", "Facility", Location.class));
        cd.addParameter(new Parameter("onOrAfter", "Start Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End Date", Date.class));
        cd.setQuery("SELECT patient_id FROM encounter WHERE location_id=:location AND voided=0 AND encounter_datetime BETWEEN :onOrAfter AND :onOrBefore");
        return cd;
    }

    public CohortDefinition hasEncounterPerLocation(EncounterType type){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Has encounters per location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));
        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("encounter", ReportUtils.map(commonCohortLibrary.hasEncounter(type), ""));
        cd.setCompositionString("location AND encounter");
        return cd;
    }

    public CohortDefinition newDiagnosedCases(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        Concept diabeticQuestion = Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE);
        Concept hypertensionQuestion = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept newDmPatient = Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT);
        Concept newHTNPatient = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);

        cd.setName("New diagnosed cases");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        cd.addSearch("newDiabetic", ReportUtils.map(commonCohortLibrary.hasCodedObs(diabeticQuestion, newDmPatient), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("newHypertension", ReportUtils.map(commonCohortLibrary.hasCodedObs(hypertensionQuestion, newHTNPatient), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.setCompositionString("(newDiabetic OR newHypertension) AND location");

        return cd;
    }

    public CohortDefinition hasObsOnLocation(Concept q, Concept ... a){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Has obs in a location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("hasCodedObs", ReportUtils.map(commonCohortLibrary.hasCodedObs(q, a), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("location AND hasCodedObs");
        return cd;
    }

    public CohortDefinition onInsulinOnly(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("takes insulin in a location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        Concept question = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
        Concept ins7030 = Dictionary.getConcept(Dictionary.INSULIN_70_30);
        Concept solins = Dictionary.getConcept(Dictionary.INSULIN_SOLUBLE);
        Concept nphT1 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1);
        Concept nphT2 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_2);
        Concept otherIns = Dictionary.getConcept(Dictionary.INSULIN_OTHER_MEDICATION);

        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("hasCodedObsIns", ReportUtils.map(commonCohortLibrary.hasCodedObs(question, ins7030, solins, nphT1, nphT2), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("other", ReportUtils.map(valueTextCalculation(otherIns), "onDate=${onOrBefore}"));
        cd.setCompositionString("location AND (hasCodedObsIns OR other)");
        return cd;
    }

    public CohortDefinition onOglasOnly(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("takes oglas in a location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        Concept question = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
        Concept metrofim = Dictionary.getConcept(Dictionary.OGLAS_METFORMIN);
        Concept gilbersmine = Dictionary.getConcept(Dictionary.OGLAS_GILBERCLAMIDE);
        Concept otherOglas = Dictionary.getConcept(Dictionary.OGLAS_OTHER);

        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("hasCodedObsOglas", ReportUtils.map(commonCohortLibrary.hasCodedObs(question, metrofim, gilbersmine), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("other", ReportUtils.map(valueTextCalculation(otherOglas), "onDate=${onOrBefore}"));
        cd.setCompositionString("location AND (hasCodedObsOglas OR other)");
        return cd;
    }

    public CohortDefinition valueTextCalculation(Concept concept){
        CalculationCohortDefinition cd = new CalculationCohortDefinition("value", new ValueTextObsCalculation());
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        cd.addCalculationParameter("concept", concept);
        return cd;
    }

    public CohortDefinition havingValueNumericObsHbA1c(){
        NumericObsCohortDefinition cd = new NumericObsCohortDefinition();
        cd.setName("HbA1c");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.setQuestion(Dictionary.getConcept(Dictionary.HBA1C));
        cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.ANY);
        return cd;
    }

    public CohortDefinition havingValueNumericObsHbA1cWithLocation(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("HbA1c");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        cd.addSearch("location", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("hba1c", ReportUtils.map(havingValueNumericObsHbA1c(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("location AND hba1c");
        return cd;
    }

    public CohortDefinition havingValueNumericObsHbA1cLess7(){
        NumericObsCohortDefinition cd = new NumericObsCohortDefinition();
        cd.setName("HbA1c<7");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.setQuestion(Dictionary.getConcept(Dictionary.HBA1C));
        cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.ANY);
        cd.setOperator1(RangeComparator.LESS_THAN);
        cd.setValue1(7.0);
        return cd;
    }

    public CohortDefinition havingValueNumericObsHbA1cWithLocationLes7(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("HbA1cLess7");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        cd.addSearch("loc", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("less7", ReportUtils.map(havingValueNumericObsHbA1cLess7(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("loc AND less7");
        return cd;
    }

    public CohortDefinition havingValueNumericWithBoundaries(Concept q1, Double val){
        NumericObsCohortDefinition cd = new NumericObsCohortDefinition();
        cd.setName("");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.setQuestion(q1);
        cd.setTimeModifier(BaseObsCohortDefinition.TimeModifier.ANY);
        cd.setOperator1(RangeComparator.GREATER_EQUAL);
        if(val != null) {
            cd.setValue1(val);
        }
        return cd;
    }

    public CohortDefinition havingValueNumericObsPressure(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("pressure");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));

        cd.addSearch("loc", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("gt140", ReportUtils.map(havingValueNumericWithBoundaries(Dictionary.getConcept(Dictionary.SYSTOLIC_BLOOD_PRESSURE), 140.0), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("gt90", ReportUtils.map(havingValueNumericWithBoundaries(Dictionary.getConcept(Dictionary.DIASTOLIC_BLOOD_PRESSURE), 90.0), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("loc AND gt140 AND gt90");
        return cd;
    }

    public CohortDefinition footUlcers(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Foot Ulcers");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));
        cd.addSearch("loc", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("initial", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.PROBLEM_ADDED), Dictionary.getConcept(Dictionary.FOOT_ULCER)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("foot", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_ULCERS_FOOT), Dictionary.getConcept(Dictionary.UNDER_TREATMENT)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("loc AND (initial OR foot)");
        return cd;
    }

    //foot saved options
    private CohortDefinition peripheralNeuropathyDeformityFootUlcer(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("peripheralNeuropathyDeformityFootUlcer");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addSearch("peripheral", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_PHERIPHERAL), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("deformity", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.DEFORMITY), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("footUlcer", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_ULCERS_FOOT), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("peripheral OR deformity OR footUlcer");
        return cd;
    }
    private CohortDefinition amputationAcuteJointLossOfSensation(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("amputationAcuteJointLossOfSensation");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addSearch("amp", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_AMPUTATION), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("aj", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.ACUTE_JOINTS), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("los", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.LOSS_OF_SENSATION), Dictionary.getConcept(Dictionary.FOOT_SAVED)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("amp OR aj OR los");
        return cd;
    }

    public CohortDefinition combinedFootOptionsWithLocation(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("amputationAcuteJointLossOfSensation");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));
        cd.addSearch("loc", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("pndfu", ReportUtils.map(peripheralNeuropathyDeformityFootUlcer(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("aajlos", ReportUtils.map(amputationAcuteJointLossOfSensation(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("loc AND (pndfu OR aajlos)");
        return cd;
    }
    //
    //foot amputation options due to diabetic foot
    private CohortDefinition diabeticFootOptions(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("diabeticFootOptions");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addSearch("past", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_PAST_CONDITION), Dictionary.getConcept(Dictionary.FOOT_AMPUTATION)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("fa", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_ASSESSMENT), Dictionary.getConcept(Dictionary.FOOT_AMPUTATION)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("yes", ReportUtils.map(commonCohortLibrary.hasCodedObs(Dictionary.getConcept(Dictionary.FOOT_AMPUTATION), Dictionary.getConcept(Dictionary.YES)), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("past OR fa OR yes");
        return cd;
    }

    public CohortDefinition diabeticFootOptionsWithLocation(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("diabeticFootOptionsWithLocation");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("location", "Facility", Location.class));
        cd.addSearch("loc", ReportUtils.map(getEncounters(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},location=${location}"));
        cd.addSearch("dfo", ReportUtils.map(diabeticFootOptions(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("loc AND dfo");
        return cd;

    }

    /**
     * Get the patients who are dead
     * @return CohortDefinition
     */
    public CohortDefinition getDeadPatients(int conceptId) {
        SqlCohortDefinition sql = new SqlCohortDefinition();
        sql.setName("Dead Patients");
        sql.addParameter(new Parameter("location", "Location", Location.class));
        sql.addParameter(new Parameter("startDate", "Start Date", Date.class));
        sql.addParameter(new Parameter("endDate", "End Date", Date.class));
        sql.setQuery(Queries.getDeceasedPatientsWithBoundaries(conceptId));
        return sql;

    }


}