<%@page contentType="text/html" pageEncoding="UTF-8"%>
 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="include.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
     <script type="text/javascript" charset="utf-8">
 
	$(document).ready(function() {
		 
		var table=	$('#ActivityReportTable').dataTable( {
				"bLengthChange": false,
				"sDom": 'T<"clear">lfrtip',
				"oTableTools" : {
					"sSwfPath" : "${reportHearder}"
				},
				"bJQueryUI" : true ,
				 
			} );


		//Date Picker Class
		$( ".datepicker" ).datepicker({
			showOn: "button",
			buttonImage: "${calendarPath}",
			buttonImageOnly: true
		});
		
		});
	</script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Audittracking</title>
    </head>
<body  background="${bgPath}">
 <div> 
    <a href="/" class="logo">
        <img src="${logoHearder}" alt="Audittracking"  height="4%" width="7%" border="0">
   </a>  
</div>
</br>
 <form:form method="POST" modelAttribute="reportDTO" action="getReport" id="reportAdminForm">
	   <div style="margin: 10px auto; padding: 0; width: 90%;">
	        <fieldset class="ui-widget ui-widget-content" style="position: absolute; left: 1%; width:95%;">
				<legend class="ui-widget-header ui-corner-all">
			<div>Override Audit Report</div>
				</legend>   
				
				<span style="position:absolute; left:10%;" style="font-size: 80%">
			 
					<label> Report Type :</label><br/> 
				                <form:select path="reportType">
					                  <form:option value="audit">Audit</form:option>
					                  <form:option value="override">Override audit</form:option>
					                  <form:option value="Mark">Marked for Audit</form:option>
					                  <form:option value="Unmark">UnMarked from Audit</form:option>
					                  <form:option value="Deselect">Deselected from Audit</form:option>
					            </form:select>
					<br/> 
					<br/>
					
							  
				
		  		</span>
				
				 
	              <span style="position:absolute; left:30%;" style="font-size: 80%">
		   
					<label> Store Number :</label><br/> 
					 <form:select path="storeNumber">
					     <form:options items="${storeNumberList}" />
					 </form:select>
				 
					<br/> 
					<br/> 
			 
					<label>  Operator Number :</label><br/> 
				                <form:input path="operatorNumber" type="number"   
                                id="operatorNumber" placeholder="operatorNumber" />
					<br/> 
					<br/>
					
		  		</span>
		  		
	            <span style="position:absolute; left:50%;" style="font-size: 80%">
		   
					<label> Report Begin Date :</label><br/>   
				     <form:input path="beginDate" type="text" class="datepicker" 
                                id="beginDate" placeholder="beginDate" />
					<br/> 
					<br/> 
			 
					<label> Report End Date :</label><br/> 
				             <form:input path="endDate" type="text" class="datepicker" 
                                id="endDate" placeholder="endDate" />
					<br/> 
					<br/> 
				
		  		</span>
		  		<span style="position:absolute; left:70%;" style="font-size: 80%">
		            	<br/> 
					   <br/> 
					
							 	   <input type="submit"  value="Submit"  class="ui-button ui-widget ui-state-default ui-corner-all ui-state-hover" style="font-size: 90%" role="button" aria-disabled="false"/> 
		  		     <br/> 
					<br/>   
				
		    </span>
		  		
		  		 <br/> <br/> <br/> <br/> <br/> <br/> <br/>
		  	</fieldset>	
		  		<br/> <br/> <br/> <br/> <br/>  <br/> <br/> <br/> 
		  		<br/> <br/> <br/>   <br/> 
			    <br/>   
			     
				<br/>   
					<div id="reportContainer">
						<table cellpadding="3" cellspacing="3" border="1" class="display"
						id="ActivityReportTable" width="99%" style="font-size: 80%">
						<thead>
							<tr>
								<th>Action Date</th>
								<th>Manager Operator Number</th>
								<th>Manager Name</th>
								<th>Employee Operator Number</th>
								<th>Employee Name</th>
								<th>Override Reason</th>
								<th>Store Number</th>
								 
							</tr>
						</thead>
						<tbody>
						   <c:forEach items="${reportDTO.activities}" var="reportActivityLogs" varStatus="activityRow">
								<tr>
									<td align="center" width="10%">
									    <fmt:formatDate pattern="yyyy-MM-dd" value="${reportActivityLogs.activityLog.createDate}" />
									</td>
									<td align="center"><c:out value="${reportActivityLogs.activityLog.mgrOpNum}" /></td>
									<td align="center"><c:out value="${reportActivityLogs.manager.operatorName}" /></td>
									<td align="center"><c:out value="${reportActivityLogs.activityLog.empOpNum}" /></td>
									<td align="center"><c:out value="${reportActivityLogs.employee.operatorName}" /></td>
									<td align="center"><c:out value="${reportActivityLogs.activityLog.mgrReason}" /> </td>
									<td align="center"><c:out value="${reportActivityLogs.activityLog.storeNumber}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
			 	
					<br/><br/><br/><br/><br/>
				</div>
 </form:form>
	 
</body>
 
</html>
