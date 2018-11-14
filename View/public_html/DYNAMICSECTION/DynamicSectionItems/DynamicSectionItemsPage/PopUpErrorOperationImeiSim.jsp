<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<!--[CMT][vcedenos][Se agrega referencia a service]-->
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.ItemService"%>
<%@ page import="pe.com.nextel.service.*"%>
<!--[CMT][vcedenos][Se obtiene el specificationId]-->
<%
    String  strMessage         = "",
            strResult          = "",
            strOrderIdv = (request.getParameter("strOrderId")==null?"0":request.getParameter("strOrderId"));
            System.out.println("[Popup]Id de la Orden:" + strOrderIdv);
    int intResultCategory=0;
    //[CMT][vcedenos][Se obtiene el specificationId]
    AutomatizacionService objAutomatService_ = new AutomatizacionService();
    HashMap hsAutomat = new HashMap();
    if(!strOrderIdv.equals("0")){
       try{
         hsAutomat         = objAutomatService_.getOrderSpecificationInbox(Long.parseLong(strOrderIdv));
         strMessage        = (String)hsAutomat.get("strMessage");
         strResult         = (String)hsAutomat.get("strResult");
         
         if(null == hsAutomat.get("strResultCategory")){
            intResultCategory = 0;
         }
         else{
            intResultCategory = Integer.parseInt(hsAutomat.get("strResultCategory").toString());
         }
         
         System.out.println("strResultCategory_getOrderSpecificationInbox ===> "+intResultCategory);
         
       }catch(Exception e) {
            throw new Exception("[PopUpErrorOperationImeiSim.jsp][objAutomatService.getOrderSpecificationInbox]= "+ e.getMessage());
       }
       if(strMessage!=null){
            throw new Exception("[PopUpErrorOperationImeiSim.jsp][objAutomatService.getOrderSpecificationInbox]= " + strMessage);    
       }
    }
    //[CMT][vcedenos][Se verifican el valor de las variables]
    System.out.println("<----------[PopUpErrorOperationImeiSim.jsp]------------->");
    System.out.println("strResult_getOrderSpecificationInbox ===> "+strResult);
    System.out.println("strResultCategory_getOrderSpecificationInbox ===> "+intResultCategory);
    System.out.println("Mens_getOrderSpecificationInbox ===> "+strMessage);
    System.out.println("<----------[Fin_PopUpErrorOperationImeiSim.jsp]------------->");
%>
<!--[Fin CMT]-->
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show Error IMEI_SIM</title>
    
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>
    
    <script LANGUAGE="JavaScript" src="websales/Resource/library.js"></script>
    <script language="JavaScript">
      function fxCorregir() {
        alert("Necesita_Correccion");
      }
    </script>
  </head>
  <body>
  
  <%
      String strOrderId         = (String)request.getParameter("strOrderId");
      String strItemId          = (String)request.getParameter("strItemId");
      String strItemDeviceId    = (String)request.getParameter("strItemDeviceId");
      
      System.out.println("Parametros ===>"+strOrderId+" "+strItemId+" "+strItemDeviceId+" ");
      
      String telefono     = "",
             accion       = "",
             descripcion  = "",
             fecha        = "";
      
       //Llamada al service
       AutomatizacionService objAutomatService = new AutomatizacionService();
       HashMap hshAutomat = new HashMap();
       HashMap hshOp = new HashMap();
       HashMap hshErrorOperationImeiSim = new HashMap();
       
      //Ventana de Error De operacion Imei/Sim
      hshOp = objAutomatService.doErrorOperationImeiSim(Long.parseLong(strOrderId),Long.parseLong(strItemId),Long.parseLong(strItemDeviceId));
      String strMessage1 = (String)hshOp.get("strMessage");
      System.out.println("Mensaje_ErrorOperation_ImeiSim -->"+strMessage1);
       if (strMessage1!=null){
        throw new Exception(strMessage1);    
      }
      hshErrorOperationImeiSim = (HashMap)hshOp.get("hshItemError");
      
      System.out.println("Mensaje_npphone_ImeiSim -->"+MiUtil.parseInt((String)hshErrorOperationImeiSim.get("npphone")));
      System.out.println("Mensaje_npaction_ImeiSim -->"+(String)hshErrorOperationImeiSim.get("npaction"));
      System.out.println("Mensaje_npdescription_ImeiSim -->"+(String)hshErrorOperationImeiSim.get("npdescription"));
      System.out.println("Mensaje_npcreateddate_ImeiSim -->"+MiUtil.getDate((Date)hshErrorOperationImeiSim.get("npcreateddate"),"dd/MM/yyyy"));
      
      if(hshErrorOperationImeiSim != null){
        telefono     = MiUtil.getString((String)hshErrorOperationImeiSim.get("npphone"));
        accion       = MiUtil.getString((String)hshErrorOperationImeiSim.get("npaction"));
        descripcion  = MiUtil.getString((String)hshErrorOperationImeiSim.get("npdescription"));
        fecha        = MiUtil.getString(MiUtil.getDate((Date)hshErrorOperationImeiSim.get("npcreateddate"),"dd/MM/yyyy"));
      }//Fin de LLamada
      
      
  %>
      
      <form method="post" name="frmdatos">
      <!-- Table General -->
      <table  align="center" width="100%" border="0">
        <tr><td> 
        <!--Cabecera del Error-->      
        <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
           <tr class="PortletHeaderColor">
              <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
              <td class="PortletHeaderColor" align="left" valign="top">
              <font class="PortletHeaderText">
              <!--[CMT][vcedenos][Se Valida la Categoria 2065 -> Cambio de Modelo entre Tecnologias]-->
              <% System.out.println("Categoria: " + intResultCategory); %>
              <% if(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS==intResultCategory){ %>
                    ERROR ENCONTRADO!
              <%}else{%>
                    Error Operación IMIE/SIM
               <%}%>
               <!--[Fin CMT]-->
              </font></td>
              <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
           </tr>
        </table>
        </td></tr>
        <tr><td>   
        <!--Sección Datos del teléfono -->
        <table align="center" width="100%" border="0">
           <!--
           <tr> 
              <td width="20%" align="left" valign="middle" class="CellLabel">&nbsp;Tel&eacute;fono</td>
              <td><input type ="text" name = "txtTelefono" value = "<%=telefono%>" readOnly></td>
           </tr>
           -->
           <tr> 
              <td width="20%" align="left" valign="middle" class="CellLabel">&nbsp;Tipo</td>
              <td><input type ="text" name = "txtTipo" value="<%=accion%>" readOnly></td>
           </tr>
           <tr> 
              <td width="20%" align="left" valign="middle" class="CellLabel">&nbsp;Fecha</td>
              <td><input type ="text" name = "txtFecha" value="<%=fecha%>" readOnly></td>
           </tr>
        </table> 
        </td></tr>
        <tr><td>   
        <!--Sección Información de Error -->
        <table align="center" width="100%" border="0">
          <tr>
              <td width="100%" align="center" valign="middle" class="CellLabel">&nbsp;Error</td>
          </tr>
          <tr>
              <td width="100%" align="center" valign="middle" class="CellLabel">
              <textarea name = "txtDescripcionError" cols="30" rows="4"><%=descripcion%></textarea></td>

          </tr>
        </table> 
        </td></tr>
      </table>   
      </form>
  
  </body>
</html>
