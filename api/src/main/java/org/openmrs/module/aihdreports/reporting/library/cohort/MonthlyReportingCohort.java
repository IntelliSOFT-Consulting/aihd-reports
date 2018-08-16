package org.openmrs.module.aihdreports.reporting.library.cohort;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.module.aihdreports.reporting.calculation.ValueTextObsCalculation;
import org.openmrs.module.aihdreports.reporting.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.NumericObsCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MonthlyReportingCohort {

    @Autowired
    private CommonCohortLibrary commonCohortLibrary;

    public CohortDefinition newDiagnosedCases(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        Concept diabeticQuestion = Dictionary.getConcept(Dictionary.DIABETIC_VISIT_TYPE);
        Concept hypertensionQuestion = Dictionary.getConcept(Dictionary.HYPERTENSION_VISIT_TYPE);
        Concept newDmPatient = Dictionary.getConcept(Dictionary.NEW_DIABETIC_PATIENT);
        Concept newHTNPatient = Dictionary.getConcept(Dictionary.NEW_HYPERTENSION_PATIENT);

        cd.setName("New diagnosed cases");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("locationList", "Facility", Location.class));

        cd.addSearch("newDiabetic", ReportUtils.map(commonCohortLibrary.hasCodedObs(diabeticQuestion, newDmPatient), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("newHypertension", ReportUtils.map(commonCohortLibrary.hasCodedObs(hypertensionQuestion, newHTNPatient), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("location", ReportUtils.map(commonCohortLibrary.hasEncounter(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},locationList=${locationList}"));
        cd.setCompositionString("newDiabetic AND newHypertension AND location");

        return cd;
    }

    public CohortDefinition hasObsOnLocation(Concept q, Concept ... a){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Has obs in a location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("locationList", "Facility", Location.class));

        cd.addSearch("location", ReportUtils.map(commonCohortLibrary.hasEncounter(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},locationList=${locationList}"));
        cd.addSearch("hasCodedObs", ReportUtils.map(commonCohortLibrary.hasCodedObs(q, a), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("location AND hasCodedObs");
        return cd;
    }

    public CohortDefinition onInsulinOnly(){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("takes insulin in a location");
        cd.addParameter(new Parameter("onOrAfter", "Start date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "End date", Date.class));
        cd.addParameter(new Parameter("locationList", "Facility", Location.class));

        Concept question = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
        Concept ins7030 = Dictionary.getConcept(Dictionary.INSULIN_70_30);
        Concept solins = Dictionary.getConcept(Dictionary.INSULIN_SOLUBLE);
        Concept nphT1 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1);
        Concept nphT2 = Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_2);
        Concept otherIns = Dictionary.getConcept(Dictionary.INSULIN_OTHER_MEDICATION);

        cd.addSearch("location", ReportUtils.map(commonCohortLibrary.hasEncounter(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},locationList=${locationList}"));
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
        cd.addParameter(new Parameter("locationList", "Facility", Location.class));

        Concept question = Dictionary.getConcept(Dictionary.MEDICATION_ORDERED);
        Concept metrofim = Dictionary.getConcept(Dictionary.OGLAS_METFORMIN);
        Concept gilbersmine = Dictionary.getConcept(Dictionary.OGLAS_GILBERCLAMIDE);
        Concept otherOglas = Dictionary.getConcept(Dictionary.OGLAS_OTHER);

        cd.addSearch("location", ReportUtils.map(commonCohortLibrary.hasEncounter(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},locationList=${locationList}"));
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
        cd.addParameter(new Parameter("locationList", "Facility", Location.class));

        cd.addSearch("location", ReportUtils.map(commonCohortLibrary.hasEncounter(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore},locationList=${locationList}"));
        cd.addSearch("hba1c", ReportUtils.map(havingValueNumericObsHbA1c(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("location AND hba1c");
        return cd;
    }

}
