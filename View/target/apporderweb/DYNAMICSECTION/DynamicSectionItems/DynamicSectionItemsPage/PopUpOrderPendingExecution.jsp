<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.bean.NpObjItemSpecgrp" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.ItemService" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.bean.ItemBean" %>
<%@page import="org.apache.commons.collections.MapUtils"%>
<%@ page import="java.util.*"%>

<%
 try{
   String    objectType  = request.getParameter("objectType")==null?"":(String)request.getParameter("objectType");
   String    item_index  = request.getParameter("item_index")==null?"0":(String)request.getParameter("item_index");
   String    type_window = MiUtil.getString(request.getParameter("type_window"));
   String    objTypeEvent= MiUtil.getString(request.getParameter("objTypeEvent"));
   String    strFieldReadOnly = request.getParameter("strFieldReadOnly")==null?"":(String)request.getParameter("strFieldReadOnly"); //CEM COR0323 
   String    strItemId   = request.getParameter("strItemId"); 
      
   request.setAttribute("type_window",type_window);
   request.setAttribute("objTypeEvent",objTypeEvent);
   request.setAttribute("strItemId",strItemId);
   NewOrderService objNewOrderServicePopUp = new NewOrderService();
   GeneralService  objGeneralService = new GeneralService();
   Hashtable hshtinputNewSection = new Hashtable();
   HashMap hshValidateStock   = new HashMap();
   String    strCodigoCliente= "",
             strnpSite ="",
             strCodBSCS = "",
             hdnSpecification = "",
             strTypeCompany = "",
             strDivision = "",
             strStockFlag = "",
             strMessage   = "",
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strGeneratorType = "",
             strSessionId = "",
             strGeneratorId = "",
             strDispatchPlace = "",
				 strOrderId="",
         strTelefono="";
   
   strCodigoCliente = (String)request.getParameter("strCustomerId");
   hdnSpecification = (String)request.getParameter("strSpecificationId");
   strnpSite        = (String)request.getParameter("strSiteId");
   strCodBSCS       = (String)request.getParameter("strCodBSCS");
   strDivision      = (String)request.getParameter("strDivisionId");
   strTypeCompany   = (String)request.getParameter("strTypeCompany");
   strSiteOppId     = MiUtil.getString(request.getParameter("strSiteOppId"));
   strUnknwnSiteId  = MiUtil.getString(request.getParameter("strUnknwnSiteId"));
   strGeneratorType = (String)request.getParameter("strGeneratorType");
   strSessionId     = (String)request.getParameter("strSessionId");   
   strGeneratorId   = (String)request.getParameter("strGeneratorId");
   strOrderId       = (String)request.getParameter("strOrderId");
   strTelefono      = (String)request.getParameter("strTelefono");
   
   System.out.println("[PopUpOrder][strGeneratorType]"+strGeneratorType);
   System.out.println("[PopUpOrder][strGeneratorId]"+strGeneratorId);
   System.out.println("[PopUpOrder][strOrderId]"+strOrderId);
   System.out.println("[PopUpOrder][hdnSpecification]"+hdnSpecification);
   System.out.println("[PopUpOrder][strDivision]"+strDivision);
   System.out.println("[PopUpOrder][type_window]"+type_window);
   System.out.println("[PopUpOrder][strCodigoCliente]"+strCodigoCliente);
   System.out.println("[PopUpOrder][strnpSite]"+strnpSite);
	
   if( strSiteOppId.equals("0") || strSiteOppId.equals("null")    ) strSiteOppId = "";
   if( strUnknwnSiteId.equals("0")  || strUnknwnSiteId.equals("null") ) strUnknwnSiteId = "";
   strDispatchPlace = (String)request.getParameter("strDispatchPlace");
   
   String  flgClosePopUp = objGeneralService.getValue("CLOSE_POPUP_BY_ESPECIFICATION",hdnSpecification);
   
   if( !MiUtil.getString(strGeneratorType).equals("") ){
      if( strUnknwnSiteId!=null && !strUnknwnSiteId.equals("") ){
        strnpSite = strUnknwnSiteId;
      }else if( strSiteOppId!=null && !strSiteOppId.equals("")){
        strnpSite = strSiteOppId;
      }
   } 
   
   ArrayList objItemHEader = objNewOrderServicePopUp.ItemDAOgetItemHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
   
   hshtinputNewSection.put("strCodigoCliente",""+strCodigoCliente);
   hshtinputNewSection.put("strnpSite",""+strnpSite);
   hshtinputNewSection.put("strCodBSCS",""+strCodBSCS);
   hshtinputNewSection.put("hdnSpecification",""+hdnSpecification);
   hshtinputNewSection.put("strDivision",""+strDivision);
   hshtinputNewSection.put("strTypeCompany",""+strTypeCompany);
   hshtinputNewSection.put("strSiteOppId",""+strSiteOppId);
   hshtinputNewSection.put("strUnknwnSiteId",""+strUnknwnSiteId);
   hshtinputNewSection.put("strGeneratorType",""+strGeneratorType);
   hshtinputNewSection.put("strSessionId",""+strSessionId);
   hshtinputNewSection.put("strOrderId",""+strOrderId); //se agrego la orden
   request.setAttribute("hshtInputNewSection",hshtinputNewSection); 
   request.setAttribute("strSessionId",strSessionId);
   
    strStockFlag = "N";
    if (hdnSpecification != null){      
      hshValidateStock = objNewOrderServicePopUp.getValidateStock(MiUtil.parseInt(hdnSpecification), MiUtil.parseInt(strDispatchPlace)   );
      if (hshValidateStock!=null && hshValidateStock.size() > 0){
        strStockFlag = (String)hshValidateStock.get("wv_flag");
        strMessage = (String)hshValidateStock.get("wv_message");        
        if (strMessage!= null){                   
          throw new Exception(strMessage);
        } 
      }
    }
%>

<script>
  function fxCancelItemEditWindow() {
  <%if(objectType.equals("INC")){%>
    parent.opener.parent.mainFrame.document.frmdatos.flgSave.value="0";
    parent.opener.parent.mainFrame.deleteItemEnabled       =	true;
    parent.close();
  <%}else{%>
    parent.opener.document.frmdatos.flgSave.value="0";
    parent.opener.deleteItemEnabled       =	true;
    parent.close();
  <%}%>
  }
</script>

<html>
  <head>
         <title>Acciones Fallidas Pendientes de Ejecutar</title>
         <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
         <style type="CDATA">
            .show   { display:inline}
            .hidden { display:none }
         </style>
  </head>
  <body>
  
  <form method="post" name="frmdatos">
  
    <table  align="center" width="100%" border="0">
  
      <tr><td>      
    
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
         <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor" align="left" valign="top">
            <font class="PortletHeaderText">
              Acciones Pendientes de Ejecutar
            </font></td>
            <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
        </tr>
      </table>
      </td></tr>
      
      <tr><td>
      <table border="0" cellspacing="0" cellpadding="0" width="30%">
          <tr class="PortletHeaderColor"> 
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top">Teléfono - <%= strTelefono%></td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
          </tr> 
      </table>
      </td></tr>
      
      <tr><td>
      <table border="0" cellspacing="1" cellpadding="0" width="100%" class="RegionBorder">    
         <tr class="PortletHeaderColor"> 
            <td align="center" class="CellLabel" >&nbsp;  &nbsp;</td>    
            <td align="center" class="CellLabel">&nbsp;Acción&nbsp;</td>    
            <td align="center" class="CellLabel">&nbsp;Servicio&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Tipo&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Programación&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Fecha Inicio&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Fecha Fin&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Fecha Ejecución&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Estado&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Detalle Estado&nbsp;</td>
        </tr>
        
        <% ItemService itemService = new ItemService();
           HashMap objHashMap = (HashMap) itemService.getItemServicioPendingList(Long.parseLong(strOrderId), Long.parseLong(strItemId));
           String strMessageError = (String)MapUtils.getString(objHashMap, Constante.MESSAGE_OUTPUT);
           if (!MiUtil.isNotNull(strMessage)){
              ArrayList recoveryList = (ArrayList) MapUtils.getObject(objHashMap, "objArrayList");
              for (int i = 0; i < recoveryList.size(); i++) {
                Object[] objList = (Object[]) recoveryList.get(i);
        %>
        <tr class="CellContent">
              <td align="center"> <%= i+1%> </td>    
              <td align="center" ><%= objList[0]%></td>    
              <td align="center" ><%= objList[1]%></td>
              <td align="center" ><%= objList[2]%></td>
              <td align="center" ><%= objList[3]%></td>
              <td align="center" ><%= objList[4]%></td>
              <td align="center" ><%= objList[5]%></td>
              <td align="center" ><%= objList[6]%></td>
              <td align="center"><font color="black"><%= objList[7]%></font></td> 
              <td align="left" ><%= objList[8]%></td>
        </tr> 
              <%}
           }        
        %>
      </table>
      </td></tr>
      <td><tr>
        <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCancelItemEditWindow();"></td>
      </tr></td>
    </table>
  
  </body>
</html>
<%
} catch(Exception e){}
%>