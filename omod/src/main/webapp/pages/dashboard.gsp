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
           <table border="0">
               <tr>
                   <th>Treatment</th>
                   <th>Male</th>
                   <th>Female</th>
               </tr>
               <tr>
                   <td>Diet & Physical Activity</td>
                   <td></td>
                   <td></td>
               </tr>
               <tr>
                  <td>OGLAs</td>
                  <td></td>
                  <td></td>
              </tr>
              <tr>
                <td>Insulin</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>Antihypertensives</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>Herbal</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>Other</td>
                <td></td>
                <td></td>
            </tr>
           </table>
           <br />
           <table border="0">
              <tr>
                  <th colspan="2">Waist Circumference</th>
              </tr>
              <tr>
                 <td>Males with</td>
                 <td>Increased risks</td>
             </tr>
             <tr>
               <td>WC &gt;102 cm</td>
               <td></td>
             </tr>
             <tr>
               <td>WC &gt;88 cm</td>
               <td></td>
             </tr>
           </table>
      </td>
   </tr>
</table>
</div>