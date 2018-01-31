package org.openmrs.module.aihdreports.reporting.metadata;

/**
 * Metadata for reporting functionality
 */
public class Metadata {

    public static class Concept{
		public final static String WEIGHT = "5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		public final static String HEIGHT = "5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIASTOLIC_BLOOD_PRESSURE = "5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String SYSTOLIC_BLOOD_PRESSURE = "5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String WAIST_CIRCUMFERENCE = "163080AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String PULSE_RATE = "5087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String RBS = "887AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String FBS = "160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String HBA1C = "159644AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String RETURN_VISIT_DATE = "5096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        public final static String MEDICATION_HISTORY = "160741AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String MEDICATION_ORDERED = "1282AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String CURRENTLY_TAKING_MEDICATON = "159367AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIET_AND_PHYSICAL_ACTIVITY = "";//a
        public final static String OGLAS = "";//b
        public final static String INSULIN = "";//c
        public final static String ANT_HYPETENSIVE = "";//d
        public final static String HERBAL = "";//e
        public final static String OTHER = "";//f
        public final static String OGLAS_AND_INSULIN = "";//g




        public final static String NHIF_MEMBER = "1917AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        public final static String HTN = "117399AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String NEW = "";//1
        public final static String KNOWN = "";//2


        public final static String AGE_AT_DIAGNOSIS_YEARS = "160617AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String LEVEL_OF_EDUCATION = "1712AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String OCCUPATION = "1542AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String ADMITTED_REFERED = "";
        public final static String YES = "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String NO = "1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        public final static String SYMPTOM = "1728AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIABETES_SECONDARY_TO_OTHER_CAUSES = "";//d

        //complication options
        public final static String PROBLEM_ADDED = "6042AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String RETONOPATHY = "";//a
        public final static String NEUROPATHY = "";//b
        public final static String NEPTHOPATHY = "";//c
        public final static String CELEBRALVARSULAR_DISEASES = "";//d
        public final static String COLONARY_HEART_DISEASES = "";//e
        public final static String PERIPHERAL_VASCULAR_DISEASES = "";//f
        public final static String DIABETIC_FOOT = "";//g
        public final static String HEART_FAILURE = "";//h
        public final static String ERECTILE_DISFUNCTION = "";//i
        public final static String GASTROPATHY = "";//j
        public final static String CATARACTS = "";//k
        public final static String DENTAL_COMPLICATIONS = "";//l

        public final static String HYPERTENSION = "";
        public final static String DYSLIPIDEMIA = "";
        public final static String OBESITY = "";
        public final static String HIV = "";
        public final static String TB = "";

        //populating the monthly report indicators concepts here
        public final static String DIABETIC_VISIT_TYPE = "2d0d45ca-a92f-4fb2-a6af-c53a1c079bf3";
        public final static String NEW_DIABETIC_PATIENT = "78144858-1452-4b31-af12-fdfd303fc77a";
        public final static String KNOWN_DAIBETIC_PATIENT = "2e385fe5-2d51-4d86-862e-a7752470c508";
        public final static String HYPERTENSION_VISIT_TYPE = "02c2e1f7-7f0c-4bd1-b89e-1a1b37855a6a";
        public final static String NEW_HYPERTENSION_PATIENT = "d88104a5-9126-4b5a-9380-e31a8e7e9442";
        public final static String KNOWN_HYPERTENSION_PATIENT = "9b3d4986-ab44-41eb-afc8-504ab5bfe6e2";
        public final static String TYPE_OF_DIABETIC = "c73b9fa8-4ecb-4f34-8506-aa21c6bd5894";
        public final static String TYPE_1_DIABETES = "142474AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//a
        public final static String TYPE_2_DIABETES = "142473AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//b
        public final static String GDM = "1449AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//c


    }
    
    public static class Identifier{
		public final static String PATIENT_ID = "b9ba3418-7108-450c-bcff-7bc1ed5c42d1";
		public final static String PHONE_NUMBER = "d0929ad2-f87a-11e7-80ee-672bf941f754";
    }
    
    public static class EncounterType{

		public final static String DM_FOLLOWUP = "2da542a4-f87d-11e7-8eb4-37dc291c1b12";
		public final static String DM_INITIAL ="bf3f3108-f87c-11e7-913d-5f679b8fdacb";
    }
    
    public static class Program{

    }
}
