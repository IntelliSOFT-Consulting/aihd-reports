package org.openmrs.module.aihdreports.reporting.metadata;

import org.openmrs.Concept;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata for reporting functionality
 */
public class Metadata {

    public static class Concepts {
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
        public final static String SCREENED_FOR_TB = "164800AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String TB_STATUS = "1659AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String ON_TREATMENT = "1662AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String DISCONTINUE_REASON = "161555AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String TRANSFER_OUT = "159492AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";


        public final static String MEDICATION_HISTORY = "160741AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String MEDICATION_ORDERED = "1282AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String CURRENTLY_TAKING_MEDICATON = "159367AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIET= "185c3281-058c-4f07-89cd-04417e18b3c7";//a
        public final static String PHYSICAL_EXERCISE= "159364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//a
        public final static String OGLAS_METFORMIN = "79651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//b
        public final static String OGLAS_GILBERCLAMIDE = "77071AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//b
        public final static String OGLAS_OTHER = "3c6865e4-a473-4fe6-8467-838d52a2c27a";//b
        public final static String INSULIN_70_30 = "159459AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//c
        public final static String INSULIN_SOLUBLE = "282AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String INSULIN_NPH_TYPE_1 = "e4f96288-5a75-4259-a610-fa380d469f1f";
        public final static String INSULIN_NPH_TYPE_2 = "ee5647b4-3ebf-48ae-9428-2558118de260";
        public final static String INSULIN_OTHER_MEDICATION = "ee5647b4-3ebf-48ae-9428-2558118de260";
        public final static String ANT_HYPETENSIVE = "";//d
        public final static String HERBAL = "c2769cb4-6f2a-476b-b8a1-8a7a7cb7d62e";//e
        public final static String OTHER_NON_CODED = "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//f




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
        //public final static String DIABETES_SECONDARY_TO_OTHER_CAUSES = Diabetes_Secondary_to_other_causes;//d

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
        public final static String FOOT_EXAM = "f8e9419e-b319-48c0-a57e-10720710f278";
        public final static String PHYSICAL_EXAM = "1391AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String FOOT_AMPUTATION = "99290a23-7b79-460d-b982-ea9f32486259";
        public final static String FOOT_ULCER = "163411AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String KIDNEY_FAILURE = "113338AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String VISUAL_IMPAIRMENT = "159298AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String EDUCATION_COUNSELING_ORDERS = "1379AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIABETES = "2ea479b2-7324-4f03-8e91-d8933a2fa51e";
        public final static String STROKE = "111103AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Ischemic_heart_disease = "139069AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Peripheral_Vascular_disease = "139069AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Heart_failure = "139069AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Neuropathy = "118983AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Retinopathy = "113257AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Nephropathy = "113338AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String Diabetic_foot = "163411AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public final static String DIABETES_SECONDARY_TO_OTHER_CAUSES = "44a5bd2a-e0c9-43f5-b96b-de00fad98bf0";
        public final static String HYPETENSION_TYPE = "d4678d10-c650-4a2b-87ad-6e3d3283c06d";
        public final static String MILD_HYPERTENSION = "c447006f-9923-4840-8a56-6b7ad8cb55a1";
        public final static String MODERATE_HYPERTENSION ="51879821-770d-4ba1-8cfe-e358c9209dab";
        public final static String SEVERE_HYPERTENSION ="fa3b33e0-bbd6-4d1a-b74b-26d411a928da";
        public final static String PREECLAMSIA ="c9614f94-cf50-4ed4-b0cd-58e06c6266d6";
        public final static String DIABETIC_YEAR_OF_DIAGNOSIS = "95d4aa36-05ee-44cb-823b-35a62f07ce49";
        public final static String HYPERTENSION_YEAR_OF_DIAGNOSIS = "b863e568-1491-42d0-b881-04df389ad0de";

        //concepts uuids for anti hypertensive drugs
        //ACE  inhibitor
        public static final String Captopril = "72806AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Enalapril = "75633AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Lisinopril = "78945AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Perindopril = "81822AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Ramipril = "83067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String other_ace = "b77a9f38-ef4a-4638-849c-4227be4ec8ef";
        //ARB
        public static final String Candesartan = "72758AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Irbesartan = "78210AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Losartan = "79074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Telmisartan = "84764AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Valsartan = "86056AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Olmesartan = "b3e3886e-f4d8-48c5-9789-29c4f76d0f06";
        public static final String other_arb = "03860e0e-9bc2-40c3-8e1e-c97602cd9a50";
        //Beta blockers group B
        public static final String Atenolol = "71652AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Labetolol = "78599AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Propranolol = "82732AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Carvedilol = "72944AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Nebivolol = "80470AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Metoprolol = "79764AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Bisoprolol = "72247AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String other_b = "07af8a70-6281-4f4b-a553-23501387b340";

        //Long-acting CCB group c
        public static final String Amlodipine = "71137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Felodipine = "76211AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Nifedipine = "80637AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        //Thiazide diuretic group d
        public static final String Chlorthalidone = "73338AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String HydrochlorothiazideHCTZ = "77696AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Indapamide = "77985AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String other_d1 = "94cecd48-fade-42f8-b579-69280634add6";
        public static final String other_d2 = "c2c74198-9ec4-4331-8cca-42ff145cb3d9";

        //group Z
        public static final String Methyldopa = "79723AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Hydralazine = "77675AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String Prazocin = "77985AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String other_z = "88680685-27ee-4a6f-a3b9-f0fa8ef3cb8b";

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
    public static class ListsofConcepts{
        public final static List<Concept> dietAndExercise(){
            List<Concept> list = new ArrayList<Concept>();
            list.add(Dictionary.getConcept(Dictionary.DIET));
            list.add(Dictionary.getConcept(Dictionary.PHYSICAL_EXERCISE));
            return list;
        }

        public final static List<Concept> oglas(){
            List<Concept> list = new ArrayList<Concept>();
            list.add(Dictionary.getConcept(Dictionary.OGLAS_GILBERCLAMIDE));
            list.add(Dictionary.getConcept(Dictionary.OGLAS_METFORMIN));
            list.add(Dictionary.getConcept(Dictionary.OGLAS_OTHER));
            return list;
        }
        public final static List<Concept> insulin(){
            List<Concept> listInsulin = new ArrayList<Concept>();
            listInsulin.add(Dictionary.getConcept(Dictionary.INSULIN_70_30));
            listInsulin.add(Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_1));
            listInsulin.add(Dictionary.getConcept(Dictionary.INSULIN_NPH_TYPE_2));
            listInsulin.add(Dictionary.getConcept(Dictionary.INSULIN_OTHER_MEDICATION));
            listInsulin.add(Dictionary.getConcept(Dictionary.INSULIN_SOLUBLE));
            return listInsulin;
        }
        public final static List<Concept> herbal(){
            List<Concept> list = new ArrayList<Concept>();
            list.add(Dictionary.getConcept(Dictionary.HERBAL));
            return list;
        }

        public final static List<Concept> other(){
            List<Concept> list = new ArrayList<Concept>();
            list.add(Dictionary.getConcept(Dictionary.OTHER_NON_CODED));
            return list;
        }
        public final static List<Concept> hypertensive(){
            List<Concept> list = new ArrayList<Concept>();
            list.add(Dictionary.getConcept(Dictionary.Captopril));
            list.add(Dictionary.getConcept(Dictionary.Enalapril));
            list.add(Dictionary.getConcept(Dictionary.Lisinopril));
            list.add(Dictionary.getConcept(Dictionary.Perindopril));
            list.add(Dictionary.getConcept(Dictionary.Ramipril));
            list.add(Dictionary.getConcept(Dictionary.other_ace));
            list.add(Dictionary.getConcept(Dictionary.Candesartan));
            list.add(Dictionary.getConcept(Dictionary.Irbesartan));
            list.add(Dictionary.getConcept(Dictionary.Losartan));
            list.add(Dictionary.getConcept(Dictionary.Telmisartan));
            list.add(Dictionary.getConcept(Dictionary.Valsartan));
            list.add(Dictionary.getConcept(Dictionary.Olmesartan));
            list.add(Dictionary.getConcept(Dictionary.other_arb));
            list.add(Dictionary.getConcept(Dictionary.Atenolol));
            list.add(Dictionary.getConcept(Dictionary.Labetolol));
            list.add(Dictionary.getConcept(Dictionary.Propranolol));
            list.add(Dictionary.getConcept(Dictionary.Carvedilol));
            list.add(Dictionary.getConcept(Dictionary.Metoprolol));
            list.add(Dictionary.getConcept(Dictionary.Bisoprolol));
            list.add(Dictionary.getConcept(Dictionary.other_b));
            list.add(Dictionary.getConcept(Dictionary.Amlodipine));
            list.add(Dictionary.getConcept(Dictionary.Felodipine));
            list.add(Dictionary.getConcept(Dictionary.Nifedipine));
            list.add(Dictionary.getConcept(Dictionary.Chlorthalidone));
            list.add(Dictionary.getConcept(Dictionary.HydrochlorothiazideHCTZ));
            list.add(Dictionary.getConcept(Dictionary.Indapamide));
            list.add(Dictionary.getConcept(Dictionary.other_d1));
            list.add(Dictionary.getConcept(Dictionary.other_d2));
            list.add(Dictionary.getConcept(Dictionary.Methyldopa));
            list.add(Dictionary.getConcept(Dictionary.Hydralazine));
            list.add(Dictionary.getConcept(Dictionary.Prazocin));
            list.add(Dictionary.getConcept(Dictionary.other_z));
            list.add(Dictionary.getConcept(Dictionary.Nebivolol));
            return list;
        }


    }
}
