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

<h3><a href="reports.page"> Run reports</a></h3>
<br />
<div class="dashboard clear">
<table border="0">
   <tr>
        <td valign="top">
            <table border="0">
                <tr>
                    <th></th>
                    <th colspan="2">Diabetes</th>
                    <th colspan="2">Hypertension</th>
                </tr>
                <tr>
                    <th>Age Group</th>
                    <th>Male</th>
                    <th>Female</th>
                    <th>Male</th>
                    <th>Female</th>
                </tr>
                <tr>
                    <td>0 to 5</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>6 to 18</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>19 to 35</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>36 to 60</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>&gt;60</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Totals</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table border="0">
                <tr>
                    <th>BMI</th>
                    <th>Male</th>
                    <th>Female</th>
                </tr>
                <tr>
                    <td>&lt;=18</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>18.5 - 24.9</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>25 - 29.9</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>&gt;= 30</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table border="0">
                <tr>
                    <th></th>
                    <th>Male</th>
                    <th>Female</th>
                </tr>
                <tr>
                    <td>Newly diagnosed DM</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Newly diagnosed HTN</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </td>
   </tr>
</table>
<p></p>
<table>
   <tr>
       <td valign="top">
            <table border="0">
                <tr>
                    <th>Types of diabetes</th>
                    <th>Male</th>
                    <th>Female</th>
                </tr>
                <tr>
                    <td>No.with type 1 DM</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>No.with type 2 DM</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>No. with GDM</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>No. with HTN</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
       </td>
       <td valign="top">
           <table border="0">
               <tr>
                   <th>Complications</th>
                   <th>Male</th>
                   <th>Female</th>
               </tr>
               <tr>
                   <td>Stroke</td>
                   <td></td>
                   <td></td>
               </tr>
               <tr>
                   <td>Ischemic <br />heart Disease</td>
                   <td></td>
                   <td></td>
               </tr>
               <tr>
                   <td>Peripheral <br />Vascular disease</td>
                   <td></td>
                   <td></td>
               </tr>
               <tr>
                   <td>Heart failure</td>
                   <td></td>
                   <td></td>
               </tr>
               <tr>
                  <td>Neuropathy(b, k, l)</td>
                  <td></td>
                  <td></td>
              </tr>
              <tr>
              <tr>
                  <td>Retinopathy</td>
                  <td></td>
                  <td></td>
              </tr>
              <tr>
                <td>Nephropathy</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>Diabetic foot</td>
                <td></td>
                <td></td>
            </tr>
           </table>
      </td>
   </tr>
</table>
</div>