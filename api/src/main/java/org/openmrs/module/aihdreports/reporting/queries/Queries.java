package org.openmrs.module.aihdreports.reporting.queries;

public class Queries {

    /**
     * Get patients who are deceased with the specified dates
     * @return String
     */
    public static String getDeceasedPatientsWithBoundaries(int causeOfDeath) {
        String query = "SELECT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id=pe.person_id INNER JOIN encounter e ON e.patient_id=pa.patient_id WHERE e.location_id=:location AND pe.death_date BETWEEN :startDate AND :endDate AND pe.cause_of_death=%d";
        return String.format(query, causeOfDeath);
    }
}
