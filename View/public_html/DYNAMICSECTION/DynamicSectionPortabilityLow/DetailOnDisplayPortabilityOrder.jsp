<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
try{
   
   
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));      
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));                                    
%>

   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   <script type="text/javascript" >
      
   </script>
                                                                                          
      <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">               
         <tr>
            <td align="left">
               <table border="0" cellspacing="0" cellpadding="0" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Baja</td>
                  <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
               </tr>
               </table>
            </td>
         </tr>         
         <tr>
            <td align="center">         
               <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">                                                                              
                  <tr>				
                     <td class="CellLabel" align="center" width="7%">Nombre Receptor</td>
                     <td class="CellLabel" align="center" width="7%">Nombre Contacto</td>				
                     <td class="CellLabel" align="center" width="10%">Email Contacto</td>            
                     <td class="CellLabel" align="center" width="8%">Teléfono Contacto</td>            
                     <td class="CellLabel" align="center" width="8%">Fax Contacto</td>                   							
                  </tr>
                                                                     
         
         <%	//String strRepairId = String.valueOf(objRepairBean.getNprepairid());
            
            PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();                        
            HashMap hshPortabilityLowListMap = objPortabilityOrderService.getPortabilityLowHeader(lOrderId);            
            ArrayList arrPortabilityLowList = (ArrayList) hshPortabilityLowListMap.get("arrPortabilityLowList");
            Iterator itPortabilityLowList = arrPortabilityLowList.iterator();
            int index = 1;
            int cont = 0;
            int contAux = 0;                                         
            while(itPortabilityLowList.hasNext()) {
               HashMap hshPortabilityLowMap = (HashMap) itPortabilityLowList.next();
               //String strRepairReplaceId = (String) hshRepairReplaceMap.get("npreplistid");	
               if(StringUtils.isNotEmpty((String) hshPortabilityLowMap.get("nom_receptor"))) { %>
                  <tr height="20px">                        
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("nom_receptor")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("nom_contact")%></td>                                 
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("email_contact")%></td>                                                       
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("tel_contact")%></td>                  
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("fax_contacto")%></td>                                                                                                                                                                                                                                                                                                                                                         
                  </tr>
               <% index++; }  
                  }
               %>                           
            </table>      
         </td>
      </tr>
   </table>
            
<%
} catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
        System.out.println("    " + e.getStackTrace()[i] + "<br>");
	      }
}%>