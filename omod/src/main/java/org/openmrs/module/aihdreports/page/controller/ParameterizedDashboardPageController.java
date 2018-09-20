package org.openmrs.module.aihdreports.page.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.module.aihdreports.AIHDReportUtil;
import org.openmrs.module.aihdreports.metadata.SubCountiesAndTheirFacilities;
import org.openmrs.module.aihdreports.reporting.utils.Filters;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ParameterizedDashboardPageController {

    public void controller(PageModel model,
                           @RequestParam(value = "location", required = false) Integer locationId,
                           @RequestParam(value = "startDate", required = false) String startDate,
                           @RequestParam(value = "endDate", required = false) String endDate,
                           @RequestParam(value = "subcounty", required = false) String subCounty) throws ParseException {

        Date stDate = AIHDReportUtil.firstDayOfTheCurrentMonth();
        Date edDate = new Date();

        if(StringUtils.isNotEmpty(startDate)){
            stDate = AIHDReportUtil.formatDateStringWithoutHours(startDate);
        }

        if(StringUtils.isNotEmpty(endDate)){
            edDate = AIHDReportUtil.formatDateStringWithoutHours(endDate);
        }
        //get all the patients that are required for our use case
        List<Integer> cohort = new ArrayList<>();
        for (Patient patient : Context.getPatientService().getAllPatients()) {
            cohort.add(patient.getPatientId());
        }
        //get default location for every user
        User user = Context.getAuthenticatedUser();
        Person person = user.getPerson();
        PersonAttributeType type = MetadataUtils.existing(PersonAttributeType.class, "8930b69a-8e7c-11e8-9599-337483600ed7");
        PersonAttribute personAttribute = person.getAttribute(type);

        Location location = null;
        LocationService service = Context.getLocationService();

        if(locationId != null){
            location = service.getLocation(locationId);
        }
        else if(personAttribute != null){
            location = service.getLocation(Integer.parseInt(personAttribute.getValue()));
        }
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        Set<Integer> alivePatients = Filters.alive(cohort, context);
        Set<Integer> male = Filters.male(alivePatients, context);
        Set<Integer> female = Filters.female(alivePatients, context);

        List<Location> eesc = new ArrayList<Location>();
        List<Location> ewsc = new ArrayList<Location>();
        List<Location> rsc = new ArrayList<Location>();
        List<Location> wsc = new ArrayList<Location>();
        List<Location> finalList = new ArrayList<Location>();
        String subCountyName = "";

        for(String string: SubCountiesAndTheirFacilities.EMBAKASI_EAST_SUB_COUNTY()){
            eesc.add(service.getLocation(string));
        }

        for(String string: SubCountiesAndTheirFacilities.EMBAKASI_WEST_SUB_COUNTY()){
            ewsc.add(service.getLocation(string));
        }

        for(String string: SubCountiesAndTheirFacilities.RUARAKA_SUB_COUNTY()){
            rsc.add(service.getLocation(string));
        }
        for(String string: SubCountiesAndTheirFacilities.WESTLANDS_SUB_COUNTY()){
            wsc.add(service.getLocation(string));
        }

        if(StringUtils.isNotEmpty(subCounty) && subCounty.equals("eesc")){
            finalList.addAll(eesc);
            subCountyName = "Embakasi East";
        }
        else if(StringUtils.isNotEmpty(subCounty) && subCounty.equals("ewsc")){
            finalList.addAll(ewsc);
            subCountyName = "Embakasi West";
        }
        else if(StringUtils.isNotEmpty(subCounty) && subCounty.equals("rsc")){
            finalList.addAll(rsc);
            subCountyName = "Ruaraka";
        }
        else if(StringUtils.isNotEmpty(subCounty) && subCounty.equals("wsc")){
            finalList.addAll(wsc);
            subCountyName = "Westlands";
        }

        model.addAttribute("startDate", stDate);
        model.addAttribute("endDate", edDate);
        model.addAttribute("facility", location);
        model.addAttribute("subcounty", finalList);
        model.addAttribute("male", male);
        model.addAttribute("female", female);
        model.addAttribute("region", subCountyName);


    }

}
