package org.openmrs.module.aihdreports.page.controller;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ParameterizedDashboardPageController {

    public void controller(PageModel model,
                           @RequestParam(value = "location", required = false) Integer locationId,
                           @RequestParam(value = "startDate", required = false) String startDate,
                           @RequestParam(value = "endDate", required = false) String endDate,
                           @RequestParam(value = "subcounty", required = false) String subCounty){

        Location location = null;
        LocationService service = Context.getLocationService();
        if(locationId != null){
            location = service.getLocation(locationId);
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("facility", location);
        model.addAttribute("subcounty", subCounty);


    }

}
