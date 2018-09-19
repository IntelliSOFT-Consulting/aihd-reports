<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("reportingui", "reportsapp/home.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message("Dashboard Summary") }", link: "${ ui.pageLink("aihdreports",
        "dashboard")
}"
        }
    ];
</script>
<div>
    <fieldset>
        <legend>Individual Facility Reports</legend>
        <center><h2><a href="reports.page"> Run individual reports per facility</a></h2></center>
    </fieldset>
</div>
<br />
<div>
    <fieldset>
        <legend>Parameters filtered dashboard</legend>
        Start Date:<b>${startDate}</b><br />
        End Date:<b>${endDate}</b><br />
        <% if(facility){%>
        Facility: <b>${facility}</b>
        <%}%>
        <br />
        <% if(subcounty){%>
        Sub-County: ${subcounty}
        <%}%>
    </fieldset>
</div>