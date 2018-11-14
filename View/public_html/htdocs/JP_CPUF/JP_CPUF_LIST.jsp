<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%
    try{
        String strRutaContext=request.getContextPath();
        String actionURL_PopulateCenterServlet = strRutaContext+"/populateCenterServlet";
        String orderId = request.getParameter("orderId");
        String display= request.getParameter("display");
		String userLogin= request.getParameter("userLogin");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <title>CENTRO POBLADO DE USO FRECUENTE</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/json2.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/jquery-1.10.2.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/jquery.h5validate.min.js"></script>
	<style type="text/css">
		table tbody td{
			background-color: #FAFAF3;
			color: #000000;
			font-family: Arial,Helvetica;
			font-size: 8pt;
			font-weight: normal;
			padding: 3px 5px;

		}
		table thead th{
			font-family: Arial, Helvetica;
			font-size: 8pt;
			color: #000000;
			font-weight: bold;
			background-color: #e8e4b7;
		}
	</style>

</head>
<% if (Constante.SHOW_TYPE_DETAIL.equals(display)){ %>
<%@include file="JP_CPUF_LIST_DETAIL.jsp" %>
<%}else{%>
<%@include file="JP_CPUF_LIST_EDIT.jsp" %>
<%}%>
</html>
<% }catch(Exception ex){
	ex.printStackTrace();
%>
<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>
