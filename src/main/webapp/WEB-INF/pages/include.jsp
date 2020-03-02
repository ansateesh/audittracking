<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form" %>

<!--  AholdUSA 2016 @iaguilar -->
 
<!-- JQuery Base and UI -->
<spring:url value="/resources/scripts/jquery-2.0.2.min.js" var="jQuery"/>
<script src="${jQuery}"></script>
<spring:url value="/resources/scripts/jquery-ui-1.10.3.custom.min.js" var="jQueryUI"/>
<script src="${jQueryUI}"></script>
 
<!-- DataTables -->
<spring:url value="/resources/scripts/jquery.dataTables.min.js" var="jQueryTableUI"/>
<script src="${jQueryTableUI}"></script>
<spring:url value="/resources/scripts/TableTools.js" var="jQueryTableTools"/>
<script src="${jQueryTableTools}"></script>
<spring:url value="/resources/scripts/ZeroClipboard.js" var="jQueryZeroClipboard"/>
<script src="${jQueryZeroClipboard}"></script>
 

<!-- Jquery Image Tools -->
<spring:url value="/resources/scripts/jquery.bxslider.min.js" var="jQueryBxslider"/>
<script src="${jQueryBxslider}"></script>


<!-- DataTables UI-->
<spring:url value="/resources/theme/css/demo_page.css" var="pageTableUI"/>
<link href="${pageTableUI}" rel="stylesheet" type="text/css" ></link>
<spring:url value="/resources/theme/css/demo_table_jui.css" var="juiTableUI"/>
<link href="${juiTableUI}" rel="stylesheet" type="text/css" ></link>
<spring:url value="/resources/theme/css/TableTools.css" var="TableToolsUI"/>
<link href="${TableToolsUI}" rel="stylesheet" type="text/css" ></link>
<spring:url value="/resources/theme/css/media/swf/copy_csv_xls_pdf.swf" var="reportHearder"/>
  


<!--Jquery UI-->
<spring:url value="/resources/theme/css/base/jquery-ui.min.css" var="baseUI"/>
<link href="${baseUI}" rel="stylesheet" type="text/css" ></link>


<!-- Jquery Image Tools -->
<spring:url value="/resources/theme/css/jquery.bxslider.css" var="bxsliderUI"/>
<link href="${bxsliderUI}" rel="stylesheet" type="text/css" ></link>

<spring:url value="/resources/theme/images/calendar.gif" var="calendarPath"/>
<spring:url value="/resources/theme/images/aholdBG.png" var="bgPath"/>
<spring:url value="/resources/theme/images/AholdLogo.png" var="logoHearder"/>
 
