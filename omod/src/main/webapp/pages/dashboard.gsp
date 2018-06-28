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
            ${ ui.includeFragment("aihdreports", "weight") }
      </td>
   </tr>
</table>
</div>