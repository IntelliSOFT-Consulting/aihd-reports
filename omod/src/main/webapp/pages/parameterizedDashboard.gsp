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
        <table>
            <tr>
                <td colspan="2">
                    Start Date:<b>${startDate}</b>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    End Date:<b>${endDate}</b>
                </td>
            </tr>
            <% if(facility){%>
            <tr><td colspan="2">Facility: <b>${facility}</b></td></tr>
            <%}%>
            <% if(subcounty){%>
                <tr>

                </tr>
                <td colspan="2">Sub-County:<b>${region}</b></td>
                <tr>
                    <td></td>
                    <td>
                        <table>
                            <% subcounty.each {%>
                            <tr>
                                <td>${it}</td>
                            </tr>
                            <%}%>

                        </table>
                    </td>
                </tr>
            <%}%>
        </table>
    </fieldset>
</div>
<div class="dashboard clear">

    <table border="0">
        <tr>

            <td valign="top">
                ${ ui.includeFragment("aihdreports", "parameterized/diabeticHypertension", [location: facility, startDate:startDate, endDate:endDate, subcounty:subcounty, male:male, female:female]) }
            </td>
        </tr>
    </table>

</div>