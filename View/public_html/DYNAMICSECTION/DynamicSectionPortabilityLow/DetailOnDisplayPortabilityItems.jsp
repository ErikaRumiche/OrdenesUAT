<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>

<%
try{
      
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));      
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
   
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   String strLogin=objPortalSesBean.getLogin(); /* Login */
      
   ArrayList valoresCombo = new ArrayList();
   ArrayList arrComboList = new ArrayList();   
      
   HashMap hshComboMap = new HashMap();
   String strMessage=null;      
            
%>

   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   <script type="text/javascript" >
      function fxPortabilityBajaDoc(indice) {
        var vForm = document.frmdatos;
	
	var numFilas = eval(tblListaPortabilityLow.rows.length);
	
	if (numFilas == 2){
          var ruta = vForm.hdn_ruta.value;
        }else if(numFilas > 2){         
          var ruta = vForm.hdn_ruta[indice-1].value;
        }
	
        //var ruta = vForm.hdn_ruta.value;
        //alert(ruta);
        var winUrl = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=uploadDocumentBaja&hdnruta="+ruta;
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
      }
   </script>
      <br>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">               
         <tr>
            <td align="left">
               <table border="0" cellspacing="0" cellpadding="0" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Baja - Detalle</td>
                  <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
               </tr>
               </table>
            </td>
         </tr>         
         <tr>
            <td align="center">         
               <table id="tblListaPortabilityLow" border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
                  <tr>				
                     <td class="CellLabel" align="center" width="3%">#</td>
                     <td class="CellLabel" align="center" width="5%">Nro. Telefónico</td> 
                     <td class="CellLabel" align="center" width="5%">Id. Solicitud</td> 
                     <td class="CellLabel" align="center" width="5%">Estado Contrato</td>            
                     <td class="CellLabel" align="center" width="8%">Motivo Suspensión / Desactivación</td>                     
                     <td class="CellLabel" align="center" width="8%">Días Transcurridos</td>                       
                     <td class="CellLabel" align="center" width="15%">Ultimo Estado Proceso Portabilidad</td>
                     <td class="CellLabel" align="center" width="15%">Error No Integridad</td>
                     <!-- Borrar P1D <td class="CellLabel" align="center" width="10%">Eval. Solic. Baja. Portab.</td>-->
                     <td class="CellLabel" align="center" width="8%">Motivo Objeción</td>
                     <!--LROSALES-P1D-->
                     <td class="CellLabel" align="center" width="8%">&nbsp;Monto Adeudado</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Tipo Moneda</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Fec. Venc. Ult. Recibo</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Fecha de Ejecución</td>
                    <!--LROSALES-P1D-->
                     <!--Borrar P1D <td class="CellLabel" align="center" width="8%">Documento a Adjuntar</td>								                     
                     <td class="CellLabel" align="center" width="15%">Ruta</td>
                     <td class="CellLabel" align="center" width="1%">Fec. Ejec.</td>                     
                     <td class="CellLabel" align="center" width="1%">Programación en BSCS</td>     
                     
                     <td class="CellLabel" align="center" width="1%">Num. a Programar</td>-->
                     
                  </tr>         
         <%	            
            PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();                        
            HashMap hshPortabilityLowListMap = objPortabilityOrderService.getPortabilityLowByOrder(lOrderId);            
            ArrayList arrPortabilityLowList = (ArrayList) hshPortabilityLowListMap.get("arrPortabilityLowList");
            Iterator itPortabilityLowList = arrPortabilityLowList.iterator();
            int index = 1;
            int cont = 0;
            int contAux = 0;                                         
            while(itPortabilityLowList.hasNext()) {
               HashMap hshPortabilityLowMap = (HashMap) itPortabilityLowList.next();
               if(StringUtils.isNotEmpty((String) hshPortabilityLowMap.get("num_tel"))) { %>
                  <tr height="20px">   
                     <td class="CellContent" align="center" >&nbsp; <%=index%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("num_tel")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("npapplicationid")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("est_contract")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("mot_susp")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("dias_transc")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("ult_est_proc_port")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("error_no_integridad")%></td>
                     <!--<td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("desc_eval_sol_baja")%></td>-->
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("desc_motivos")%></td>
                     
                     <!--AGREGAR LOS CAMPOS NUEVOS, P1D-->
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("npamountdue")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getString((String)hshPortabilityLowMap.get("npcurrencytypedesc"))%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getDate((Date)hshPortabilityLowMap.get("npexpirationdatereceipt"),"dd/MM/yyy")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getDate((Date)hshPortabilityLowMap.get("npreleasedate"),"dd/MM/yyy")%></td>
                     <!--AGREGAR LOS CAMPOS NUEVOS, P1D-->
                     
                     <!--<td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("desc_doc_atatchment")%></td>
                     <td class="CellContent" align="left" >&nbsp;
                     <a href="javascript:fxPortabilityBajaDoc(<%=index%>);"><%=hshPortabilityLowMap.get("ruta")%></a>
                     <input type="hidden" name="hdn_ruta" value="<%=hshPortabilityLowMap.get("ruta")%>"></td>
                     <td class="CellContent" align="left" >&nbsp;<%=hshPortabilityLowMap.get("fec_ejec")%></td>                     
                     <td class="CellContent" align="left" >&nbsp;<%=hshPortabilityLowMap.get("np_delaybscs")%></td>                                                               
                     
                     <td class="CellContent" align="left" >&nbsp;<%=hshPortabilityLowMap.get("num_tel_wn")%></td>-->
                     
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