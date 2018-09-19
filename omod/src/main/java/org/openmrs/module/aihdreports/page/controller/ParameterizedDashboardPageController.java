package org.openmrs.module.aihdreports.page.controller;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

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
        List<Integer> eesc = Arrays.asList(18271,18463,12935,13175,23373,12869,12977,21273,18668,13101);
        List<Integer> ewsc = Arrays.asList(291107,22012,13016,13015,22769,13222,13240,18334,12872,17473);
        List<Integer> rsc = Arrays.asList(13172,12997,12876,19234,13077,13130,13071,18895,13000,13205,13208,13246);
        List<Integer> wsc = Arrays.asList(13258,13001,13049,22077,18888,19507,12870,13052,18887,13093,13209,21146,13009);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("facility", location);
        model.addAttribute("subcounty", subCounty);
        model.addAttribute("allPatients", Context.getPatientService().getAllPatients());


    }

}
