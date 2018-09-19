package org.openmrs.module.aihdreports.page.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.aihdreports.AIHDReportUtil;
import org.openmrs.module.aihdreports.metadata.Roles;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DashboardPageController {

    public void controller(PageModel model,
                           @RequestParam(value = "startDate", required = false) String dateStart,
                           @RequestParam(value = "endDate", required = false) String dateEnd,
                           @RequestParam(value = "chosenLocation", required = false) String loc,
                           @SpringBean("locationService") LocationService locationService) {

        List<Location> requiredLocations = new ArrayList<Location>();
        List<Location> allLocations = Context.getLocationService().getAllLocations();
        boolean isSuperUser = false;
        boolean hasTheRequiredRole = false;
        Set<Role> roles = Context.getAuthenticatedUser().getRoles();
        for(Role role:roles){
            if(role.getUuid().equals(Roles.REPORT_MANAGER.uuid())){
                hasTheRequiredRole = true;
            }
        }

        if (Daemon.isDaemonUser(Context.getAuthenticatedUser()) || Context.getAuthenticatedUser().isSuperUser() || hasTheRequiredRole) {
            requiredLocations.addAll(allLocations);
            isSuperUser = true;
        }
        else {
            User user = Context.getAuthenticatedUser();
            PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid("8930b69a-8e7c-11e8-9599-337483600ed7");
            PersonAttribute personAttribute = user.getPerson().getAttribute(personAttributeType);
            if(personAttribute != null && StringUtils.isNotEmpty(personAttribute.getValue())){
                Location userLocation = Context.getLocationService().getLocation(Integer.parseInt(personAttribute.getValue()));
                if(userLocation != null) {
                    requiredLocations.add(userLocation);
                }
            }
        }

            User user = Context.getAuthenticatedUser();
            PersonAttribute attribute = user.getPerson().getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid("8930b69a-8e7c-11e8-9599-337483600ed7"));
            Location loggedInLocation = locationService.getLocation(1);
            if(attribute != null && StringUtils.isNotEmpty(attribute.getValue())){
                loggedInLocation = locationService.getLocation(Integer.parseInt(attribute.getValue()));
            }

            //get the location logged in
            List<Integer> required_locations_super = new ArrayList<Integer>();
            List<Integer> user_location = new ArrayList<Integer>();
            List<Integer> overral_location = null;



            for(Location loc1: allLocations){
                required_locations_super.add(loc1.getId());//will be used for the administrators
            }

            user_location.add(loggedInLocation.getId());//specific user location

            if (Daemon.isDaemonUser(Context.getAuthenticatedUser()) || Context.getAuthenticatedUser().isSuperUser() || hasTheRequiredRole) {
                overral_location = new ArrayList<Integer>(required_locations_super);
            }
            else {
                overral_location = new ArrayList<Integer>(user_location);
            }


            model.addAttribute("location", requiredLocations);
            model.addAttribute("startDate", AIHDReportUtil.formatDates(firstDayOfTheMonth()));
            model.addAttribute("endDate", AIHDReportUtil.formatDates(new Date()));
            model.addAttribute("dateStart", dateStart);
            model.addAttribute("dateEnd", dateEnd);
            model.addAttribute("loo", loc);
            model.addAttribute("requiredLocations", overral_location);
            model.addAttribute("allPatients", Context.getPatientService().getAllPatients());
            model.addAttribute("isSuperUser", isSuperUser);
            model.addAttribute("hasRole", hasTheRequiredRole);


    }

    private Date firstDayOfTheMonth(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public String post(PageModel model, UiUtils ui,
                       @RequestParam(value = "chosenLocation", required = false) Integer locationId,
                       @RequestParam(value = "startDate", required = false) String startDate,
                       @RequestParam(value = "endDate", required = false) String endDate,
                       @RequestParam(value = "subcounty", required = false) String subCounty){
        String parameterString = "startDate="+startDate+"&endDate="+endDate;
        if(locationId != null){
            parameterString +="&location="+locationId;
        }
        if(StringUtils.isNotEmpty(subCounty)){
            parameterString +="&subcounty="+subCounty;
        }
        return "redirect:" + ui.pageLink("aihdreports", "parameterizedDashboard"+"?"+parameterString);

    }
}