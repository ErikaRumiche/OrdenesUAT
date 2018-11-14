<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.Constante" %>

<%
    // Tema de variables de session
    // Alternativa: Peticion bajo demanda
    //----------------------------------------------------------------------------------------------
    PortalSessionBean sessionBean = new PortalSessionBean();

    //AGAMARRA
    int npsalesstructid = 
      session.getValue("npsalesstructid")!=null?Integer.parseInt(session.getValue("npsalesstructid")+""):0;
    
    if( session.getAttribute("sessionBean") == null ) {
        PortalSessionDAO.ubicar( request.getParameter("portaluser"),
                                 Integer.parseInt(request.getParameter("rolid")),
                                 npsalesstructid,
                                 sessionBean);
        session.setAttribute("sessionBean",sessionBean);  
    }
    else {
        sessionBean = (PortalSessionBean)session.getAttribute("sessionBean");
    }
    //----------------------------------------------------------------------------------------------    
 %>
 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>SELECCIÓN DE CLIENTE</title>
    
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="../../web_operacion/js/syncscroll.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script>
    
     function fxChooseCustomer(swcustomerid){
          document.frmdatos.txtDato.value = swcustomerid;
          document.frmdatos.myaction.value = "buscarCliente";
          document.frmdatos.detallemyaction.value = 'id';
          document.frmdatos.submit();
          
          parent.close();
     }
    </script>
      
 
<form name="frmdatos" id="frmdatos" method="post" action="../../customerservlet" target="bottomFrame">
         
      <table border="0" width="100%" cellpadding="0" cellspacing="1" class="RegionBorder">
         <table border="0" cellspacing="0" cellpadding="0" height="5"><tr align="center"><td></td></tr></table>
         <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center">
            <tr>
               <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
               <td class="SectionTitle">&nbsp;Seleccion un Cliente</td>
               <td class="SectionTitleRightCurve" width="8">&nbsp;</td>
            </tr>
         </table>
         <table border="1" cellspacing="1" cellpadding="1" align="center" width="99%">
            <tr>
               <td class="ListRow0" width="5%"  >&nbsp;<b>Nro.</b></td>
               <td class="ListRow0" width="40%" >&nbsp;<b>Razón Social</b></td>
               <td class="ListRow0" width="25%" >&nbsp;<b>Cod. BSCS</b></td>
               <td class="ListRow0" width="30%" >&nbsp;<b>Tipo</b></td>
            </tr>
               
               <tr>
                  <td class="ListRow1" valign="top" align="center">1 <a href="javascript:fxChooseCustomer('888');"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de la Línea de Producto" width="15" height="15" border="0"></a></td>
                  <td class="ListRow1" valign="top" align="left">IBM DEL PERU S.A.C</td>                  
                  <td class="ListRow1" valign="top" align="left">2.66</td>
                  <td class="ListRow1" valign="top" align="left">Principal</td>
               </tr>
              

               <tr>
                  <td class="ListRow0" valign="top" align="center">2 <a href="javascript:fxChooseCustomer('105334');"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de la Línea de Producto" width="15" height="15" border="0"></a></td>
                  <td class="ListRow0" valign="top" align="left">IBM LATIN AMERICAN REGION S.A.</td>                  
                  <td class="ListRow0" valign="top" align="left"></td>
                  <td class="ListRow0" valign="top" align="left">Principal</td>
               </tr>
              
            <input type="hidden" name="txtDato"  value="">
            <input type="hidden" name="myaction"  value="">
            <input type="hidden" name="detallemyaction" value="">
      
      </table>
    </form>