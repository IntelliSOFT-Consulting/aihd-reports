<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("reportingui", "reportsapp/home.css")
    ui.includeCss("adminui", "systemadmin/account.css")
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
    <form id="form-parameters" name="form-parameters" class="simple-form-ui" novalidate  action="">
        <fieldset>
            <legend>Report Parameters</legend>
            <table width="100%">
                <tr>
                    <td>Start Date:</td>
                    <td><input type="text" id="startDate" name="startDate"  onclick="startDate()" value="${startDate}"/></td>
                </tr>
                <tr>
                    <td>End date</td>
                    <td><input type="text" id="endDate" name="endDate"  onclick="endDate()" value="${endDate}"/></td>
                </tr>
                <tr>
                    <td>Health Facility</td>
                    <td>
                        <select name="chosenLocation" id="chosenLocation">
                            <% if(location.size() > 0) {%>
                                <% location.each{%>
                                <option value="${it.locationId}">${it.name}</option>
                                <%}%>
                            <%}%>
                        </select>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
<br />
<div class="dashboard clear">
<table border="0">
   <tr>
        <td valign="top">
            ${ ui.includeFragment("aihdreports", "diabeticHypertension") }
        </td>
        <td valign="top">
            ${ ui.includeFragment("aihdreports", "bmiSummary") }
            <br />
            ${ ui.includeFragment("aihdreports", "bpSummary") }
        </td>
        <td valign="top">
            ${ ui.includeFragment("aihdreports", "dmhtnSummary") }
            <br />
            ${ ui.includeFragment("aihdreports", "tbSummary") }
        </td>
   </tr>
</table>
<br />
<table>
   <tr>
       <td valign="top">
           ${ ui.includeFragment("aihdreports", "diabeticTypes") }
            <br />
           ${ ui.includeFragment("aihdreports", "visitRevisit") }
       </td>
       <td valign="top">
           ${ ui.includeFragment("aihdreports", "complications") }
      </td>

        <td valign="top">
            ${ ui.includeFragment("aihdreports", "treatment") }
           <br />
            ${ ui.includeFragment("aihdreports", "waistCircumference") }
      </td>
   </tr>
</table>
</div>

<script type="text/javascript">
    function startDate() {
        jQuery("#startDate").datepicker({
            dateFormat: 'dd-mm-yy',
            gotoCurrent: true,
            maxDate: 0,
            minDate: new Date(2018, 1 - 1, 1)
        });
    }
    function endDate() {
        jQuery("#endDate").datepicker({
            dateFormat: 'dd-mm-yy',
            gotoCurrent: true,
            maxDate: 0,
            minDate: new Date(2018, 1 - 1, 1)
        });
    }
    jQuery(function () {
        startDate();
        endDate();
    });

</script>