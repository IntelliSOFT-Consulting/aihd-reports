package org.openmrs.module.aihdreports.reporting.queries;

public class Queries {

    /**
     * Get patients who are deceased with the specified dates
     * @return String
     */
    public static String getDeceasedPatientsWithBoundaries(final int causeOfDeath) {
        final String query = "SELECT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id=pe.person_id INNER JOIN encounter e ON e.patient_id=pa.patient_id WHERE e.location_id=:location AND pe.death_date BETWEEN :startDate AND :endDate AND pe.cause_of_death=%d";
        return String.format(query, causeOfDeath);
    }

    /**
     * Get patient list for the system usage report
     * 
     * @return
     */
    public static String getPatientsListForSystemUsageReport() {
        final String query = "SELECT pn.person_id,pi.identifier,pn.given_name,pn.middle_name,pn.family_name, lo.name,pi.date_created, ob.value_coded"
        + " FROM person_name pn JOIN patient_identifier pi ON pi.patient_id=pn.person_id"
        + " JOIN location lo ON lo.location_id=pi.location_id JOIN obs ob ON ob.person_id=pn.person_id "
        + " WHERE pi.identifier_type=4 AND pi.identifier NOT LIKE '00000-%'  ORDER BY lo.name ASC ";
        
        return query;
    }
}
