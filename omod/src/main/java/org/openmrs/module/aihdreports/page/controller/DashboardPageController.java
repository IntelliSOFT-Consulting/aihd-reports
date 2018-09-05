package org.openmrs.module.aihdreports.page.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.aihdreports.AIHDReportUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardPageController {

    public void controller(PageModel model,
                           @RequestParam(value = "startDate", required = false) String dateStart,
                           @RequestParam(value = "endDate", required = false) String dateEnd,
                           @RequestParam(value = "chosenLocation", required = false) String loc) {
        List<Location> requiredLocations = new ArrayList<Location>();
        if (Daemon.isDaemonUser(Context.getAuthenticatedUser()) || Context.getAuthenticatedUser().isSuperUser()) {
            requiredLocations.addAll(Context.getLocationService().getAllLocations());
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
            model.addAttribute("location", requiredLocations);
            model.addAttribute("startDate", AIHDReportUtil.formatDates(firstDayOfTheMonth()));
            model.addAttribute("endDate", AIHDReportUtil.formatDates(new Date()));
            model.addAttribute("dateStart", dateStart);
            model.addAttribute("dateEnd", dateEnd);
            model.addAttribute("loo", loc);


    }

    private Date firstDayOfTheMonth(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public String post(PageModel model,
                       @RequestParam(value = "chosenLocation", required = false) Integer locationId,
                       @RequestParam(value = "startDate", required = false) String startDate,
                       @RequestParam(value = "endDate", required = false) String endDate){


        /*model.addAttribute("location", Context.getLocationService().getLocation(locationId));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("locationId", locationId);*/
        return "dashboard.page";
    }
}