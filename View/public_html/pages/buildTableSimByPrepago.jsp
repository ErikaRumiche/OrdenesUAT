<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="pe.com.nextel.bean.ImeiSimBean"%>
<%@page import="pe.com.nextel.service.ItemService"%>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tabla Imei - Sim</title>
<link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>
<script defer>
  var intQuantitySIMPropios = 1;
  var arrSIMPropio = new Array();
  var arrSIMPropioMessages = new Array();

</script>
</head>
<body style="width: 95%">
<%	
  try{
  
  ItemService objItemService = new ItemService();
  HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
    if(hshDataMap!=null){
      String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
      if(!StringUtils.isBlank(strMessage)) 
        throw new Exception(strMessage);
      
      ArrayList arrImeiSimList = (ArrayList) hshDataMap.get("arrImeiSimList");
%>
<table id="table_sims" width="100%" align="center" border="1">
	<tr  class="PortletHeaderColor" >
		<!--<th>IMEI</th>-->
    <td class="CellLabel" align="center" >N°</td>
		<td class="CellLabel" align="center" >SIM</td>
		<td class="CellLabel" align="center" >Mensaje</td>
	</tr>
<%
  String[] strSims = null;
  long    lngCustomerId       = MiUtil.parseLong((String)request.getParameter("strCustomerId")); 
  long    lngSpecificationId  = MiUtil.parseLong((String)request.getParameter("strSpecificationId")); 
  String  strModality         = MiUtil.getString((String)request.getParameter("strModality"));
  
  if( arrImeiSimList!= null && arrImeiSimList.size() > 0 ){
   strSims = new String[arrImeiSimList.size()];
  
  for(int i=0; i<arrImeiSimList.size(); i++) {
    ImeiSimBean imeiSimBean = (ImeiSimBean) arrImeiSimList.get(i);
    strSims[i] = imeiSimBean.getSim();
    imeiSimBean = null;
  }
  
  HashMap objHashResult  =  objItemService.doValidateMassiveSim(lngCustomerId,lngSpecificationId,strModality,strSims);
  
  if( objHashResult.get("strMessage") != null )
    throw new Exception((String)objHashResult.get("strMessage"));
  
  strSims = (String[])objHashResult.get("strMessages");
  
  for(int i=0; i<arrImeiSimList.size(); i++) {
    ImeiSimBean imeiSimBean = (ImeiSimBean) arrImeiSimList.get(i);
    //strSims[i] = imeiSimBean.getSim();
%>
 <tr>
		<!--<td align="center"><%=StringUtils.defaultIfEmpty(imeiSimBean.getImei(),"&nbsp;")%></td>-->
    <td class="CellContent" align="center"><%=StringUtils.defaultIfEmpty(""+(i+1),"&nbsp;")%></td>
    <td  class="CellContent" align="center"><%=StringUtils.defaultIfEmpty(imeiSimBean.getSim(),"&nbsp;")%></td>
    <%if( !MiUtil.getString(strSims[i]).equals("SIM es válido") ){%>
      <td  class="CellContent" align="center"><font color="Red"><%=StringUtils.defaultIfEmpty(strSims[i],"&nbsp;")%></font></td>
    <%}else{%>
      <td  class="CellContent" align="center"><%=StringUtils.defaultIfEmpty(strSims[i],"&nbsp;")%></td>
    <%}%>
	</tr>
  <%imeiSimBean = null;%>
<%}%>

</table>
  <script defer>
    intQuantitySIMPropios = <%=arrImeiSimList.size()%>
    <%for(int i=0; i<arrImeiSimList.size(); i++) {
      ImeiSimBean imeiSimBean = (ImeiSimBean) arrImeiSimList.get(i);%>
      arrSIMPropio[<%=i%>] = '<%=imeiSimBean.getSim()%>';
      arrSIMPropioMessages[<%=i%>] = '<%=MiUtil.getString(strSims[i])%>';
    <%}%>
    
    var flgSimsCount = 0;
    //Validar si Hay errores
    for( i=0; i<arrSIMPropioMessages.length; i++){
      if( arrSIMPropioMessages[i] != 'SIM es válido' ){
        flgSimsCount = 1;
        break;
      }
    }
    
    if( flgSimsCount == 1 ){
      alert('Revise los SIM ingreados, ya que no todos son válidos. Vuelva a intentarlo');
      parent.parent.mainFrame.frmdatos.btnAceptar.disabled = true;
    }else{
      alert('Todos los SIM son válidos');
      parent.parent.mainFrame.frmdatos.btnAceptar.disabled = false;
    }
    
    flgSimsCount = 0;
    
  </script>
<% }  } else { %>	
    No se pudo obtener datos de SIM en la carga del archivo. Vuelva a intentarlo por favor.
<%	 }
    }catch(Exception ex){
      ex.printStackTrace();
%>
  <script defer>
    alert("Hubieron errores al cargar el archivo. Vuelva a intentarlo");
    intQuantitySIMPropios = 1;
  </script>
<%}%>
</body>
</html>