<html> 
<head></head>
<body>
    <p>Hi ${toName},</p>
    <p>Below are the employees whose <b>workpermit is expiring in this month !!!</b></p>
    
    <table class="datatable">
    <tr>
      <th>EMP ID</th>
      <th>Emp Name</th>
      <th>Workpermit Expiry Date</th>
    </tr>
    <#list wplist as wp>
      <tr>
        <td>${wp.employee_id}</td>
        <td>${wp.employee_name}</td>
        <td>${wp.work_permit_name_expiry_date}</td>
      </tr>
    </#list>
    </table>
    
    <p>Thanks</p>
    <p>HAP</p>
</body>
</html>