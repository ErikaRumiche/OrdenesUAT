<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.portability.bean.PortabilityItemBean"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.service.SessionService" %>


<%
try{
   
   PortabilityOrderService  objPortabilityOrderService  = new PortabilityOrderService();
   SessionService   objSession          = new SessionService();
   HashMap objHashItemPortability = new HashMap();
   ArrayList objItemOrderPortability = new ArrayList();
   PortabilityOrderBean objOrderPortabilityReturn = null;
   HashMap hshOrderPortability = new HashMap();
   String  strMessage = null;
   
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));      
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   PortalSessionBean sessionUser  = objSession.getUserSession(strSessionId);
   String strLogin=sessionUser.getLogin();
   //Traemos datos para la orden de Portabilidad
   //-------------------------------------------
   
   hshOrderPortability = (HashMap)objPortabilityOrderService.getOrderPortabilityReturn(lOrderId);
   strMessage         =  (String)hshOrderPortability.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);  
      
   objOrderPortabilityReturn=(PortabilityOrderBean)hshOrderPortability.get("objOrderPortabilityReturn");

  
   //Traemos el detalle del item  la Orden de Portabilidad Retorno
   //------------------------------------------------------
   objHashItemPortability    = (HashMap)objPortabilityOrderService.getItemOrderPortabilityReturn(lOrderId);
   strMessage = (String)objHashItemPortability.get("strMessage");
   
   if ( objHashItemPortability.get("strMessage") != null ) {
  %>
  <script>alert("<%=objHashItemPortability.get("strMessage")%>")</script>
  <%}
  objItemOrderPortability = (ArrayList)objHashItemPortability.get("objArrayList");
  
  
%>

   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   <script type="text/javascript" >
   </script>
   
  <input type="hidden" name="hdnNumeroOrder" value="<%=lOrderId%>">
  <input type="hidden" name="hdnSessionLogin" value="<%=strLogin%>">
  <input type="hidden" name="hdn_item_portability_grupo" value="0"> <!-- Hidden que indica el # de grupos de item_pedido ingresados -->
                                                                                          
  <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">               
         <tr>
            <td align="left">
               <table border="0" cellspacing="1" cellpadding="2" width="20%" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad - Orden</td>
                  <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
               </tr>
               </table>
            </td>
         </tr>         
         <tr>
            <td align="left">         
               <table border="0" cellspacing="1" cellpadding="2" width="75%" class="RegionBorder" align="left">                                                                              
                  <tr>				
                     <td class="CellLabel" align="center" width="10%">Cedente</td>
                     <td class="CellContent" align="center" width="10%"><%=objOrderPortabilityReturn.getNpParticipantDescription()%></td>				  
                     <td class="CellContent" align="center" width="10%">
                         <input name="btnEnvioSolicitudSR" type="button" value="Envio SR" onclick="fxSendMessage('SR')">               
                     </td>  
                  </tr>
               </table>
            </td>
          </tr>                                                            
  </table>
  <br>
  <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">               
         <tr>
            <td align="left">
               <table border="0" cellspacing="1" cellpadding="1" width="20%" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad - Detalle</td>
                  <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
               </tr>
               </table>
            </td>
         </tr>         
         <tr>
            <td align="center">         
               <table id="items_table" border="0" cellspacing="1" cellpadding="1" width="100%" class="RegionBorder">                                                                              
                  <tr>				
                     <td class="CellLabel" align="center" width="2%">Nro</td>
                     <td class="CellLabel" align="center" width="8%">Nro. Móvil</td>
                     <td class="CellLabel" align="center" width="10%">Estado Contrato</td>				
                     <td class="CellLabel" align="center" width="15%">Estado Portabilidad</td>            
                     <td class="CellLabel" align="center" width="15%">Tipo Retorno</td>
                     <td class="CellLabel" align="center" width="10%">Fecha Ejec. Retorno</td>
                     <td class="CellLabel" align="center" width="10%">Id. Proceso</td> 
                     <td class="CellLabel" align="center" width="10%">Programaci&oacute;n en BSCS</td> 
                     
                     <td class="CellLabel" align="center" width="10%">Num. a Programar</td>
                     
                     <td class="CellLabel" align="center" width="10%">Causa Denegacion</td>
                     <td class="CellLabel" align="center" width="10%">Límite para envio de SR</td>
                     <td class="CellLabel" align="center" width="25%">Observaciones</td>
                  </tr>
               </table>
            </td>
          </tr>                                                            
  </table>
  
  <script>
      
   //Funciones Generales
   //-------------------
   function fxMakeItemPortability(id, itemid, orderid, objItemName, objValue, objDescr) {        
        this.id = id;
        this.itemid = itemid;
        this.orderid = orderid;
        this.objItemName = objItemName;
        this.objValue = objValue;
        this.objDescr = objDescr;
   }
   
  function fxSendMessage(messageType){
      form = document.frmdatos;
      var url = "<%=request.getContextPath()%>/portabilityOrderServlet?strOrderId=<%=lOrderId%>&strLogin=<%=strLogin%>&strCustomerId=<%=lCustomerId%>&messageType="+messageType+"&strPortabilityType=RETORNO&hdnMethod=sendPortabilityMessages";
      parent.bottomFrame.location.replace(url);
  } 
  </script>
  
  <SCRIPT DEFER>
  
    //Se carga los datos de BD del Item, esta función se invoca desde la tabla websales.np_page_section_event
    //-------------------------------------------------------------------------------------------------------
    function fxOnloadPortabilityReturn(){
      fxLoadDataPortabilityReturn();
    }
    
    //Funcion que llena los datos en el vector de los items de Portabilidad de Retorno
    //---------------------------------------------------------------------------------
    function fxLoadDataPortabilityReturn(){
       <%
          String   objValue = "";
          String   objDescr = "";
          String   port_itemId   = "";
          String   port_orderId   = "";
          PortabilityItemBean portabilityItemBean = null; 
          
          
          if( objItemOrderPortability!=null && objItemOrderPortability.size()>0 ){
         
          
              for(int i=0; i<objItemOrderPortability.size(); i++ ){
                  portabilityItemBean  = new PortabilityItemBean();
                  portabilityItemBean  = (PortabilityItemBean)objItemOrderPortability.get(i);
                  objValue = "";
                  objDescr = "";
      
                  port_itemId    =   ""+MiUtil.getString(portabilityItemBean.getNpPortabItemId()); 
                  port_orderId    =   ""+MiUtil.getString(portabilityItemBean.getNpPortabOrderId()); 
                  
                                    
                  //Número de Teléfono
                  //------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpphonenumber());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpphonenumber());
                  
                  %>
                  
                  var vctOrderItemPortability = new Vector();
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objPhoneNumber','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Estado del Teéfono en BSCS
                  //---------------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpStatusPhoneBSCS());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpStatusPhoneBSCS());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objStatusPhoneBSCS','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Estado del Mensaje
                  //---------------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getMessagetype());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getMessagetype());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objStatusMessage','<%=objValue%>','<%=objDescr%>'));

                 <%
                 ///Tipo de retorno
                 //-----------
                 objValue  =   MiUtil.getString(portabilityItemBean.getNpReasonDesc());
                 objDescr  =   MiUtil.getString(portabilityItemBean.getNpReasonDesc());
                 %>

                vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objReturnType','<%=objValue%>','<%=objDescr%>'));

                //fxAddRowOrderItemsPortabilitylEdit(vctOrderItemPortability);


                  <%
                  ///Fecha de Ejecución de Retorno
                  //------------------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpexecutiondatereturn());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpexecutiondatereturn());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objExecDate','<%=objValue%>','<%=objDescr%>'));
                  
                   <%
                  ///Id de Secuencia del Proceso
                  //----------------------------
                  objValue  =   ""+MiUtil.getString(portabilityItemBean.getNpSequenceId());
                  objDescr  =   ""+MiUtil.getString(portabilityItemBean.getNpSequenceId());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objSecuenceId','<%=objValue%>','<%=objDescr%>'));
                
                  <%
                  ///Estado de Programación Diferida en BSCS
                  //----------------------------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpScheduleDelayBSCS());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpScheduleDelayBSCS());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objStatusProgBSCS','<%=objValue%>','<%=objDescr%>'));
                  
                                    
                  <%
                  //Número de Teléfono en formato WN
                  //------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpPhoneNumberWN());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpPhoneNumberWN());
                  
                  %>
                                    
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objPhoneNumberWN','<%=objValue%>','<%=objDescr%>'));
                                                               
                  
                  <%
                  ///Denegación de Retorno
                  //----------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpRejectType());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpRejectType());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objReturnReject','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Fecha Limite de Retorno
                  //-------------------------
                  objValue  =   MiUtil.getDate(MiUtil.getString(portabilityItemBean.getNpLimitTimerDate()));
                  objDescr  =   MiUtil.getDate(MiUtil.getString(portabilityItemBean.getNpLimitTimerDate()));
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objLimitTimerDate','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Comentario
                  //-----------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpComment());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpComment());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objComment','<%=objValue%>','<%=objDescr%>'));
                  
                  fxAddRowOrderItemsPortabilitylEdit(vctOrderItemPortability);


            
                <%
              }
          }
        %>
      
    }
    
    function fxAddRowOrderItemsPortabilitylEdit(vctOrderItemPortability) {
    
      form = parent.mainFrame.document.frmdatos;
      var index_arg = 1;
      var elemText = "";
      var cantElement;
      var valuevisible = "";
      var descvisible = "";
      var typeControl = "";
      var cell;
      var contentHidden = "";
      
      cantElement = vctOrderItemPortability.size();
      
      if (cantElement > 1) {
          
          var new_grupo_portability = parseInt(form.hdn_item_portability_grupo.value) + parseInt(1);
          form.hdn_item_portability_grupo.value = new_grupo_portability;
          
          var row   = items_table.insertRow(-1);
          elemText =  "";
          
          var cellPrinc = row.insertCell(index_arg - 1);
          elemText      = "<div id=\'contTable\' valign='center' align='center' class='CellContent' style='height:35px;'  >"+
                          "<font class='CellContent'>"+ vctOrderItemPortability.elementAt(0).id +"</font>"+
                          "<input type=\'hidden\' name='hdnIndicePort' value="+ document.frmdatos.hdn_item_portability_grupo.value +" >" +
                          "<input type=\'hidden\' name='hdnItemPortId' value='"+ vctOrderItemPortability.elementAt(0).itemid +"' >" +
                          "<input type=\'hidden\' name='hdnOrderPortId' value='"+ vctOrderItemPortability.elementAt(0).orderid +"' >" +
                          "</div>";
          cellPrinc.innerHTML = elemText;
          index_arg++;
          elemText = "";
          
          for( j = 0; j < vctOrderItemPortability.size(); j++ ){
          
              var objItemPortability  = vctOrderItemPortability.elementAt(j);
              valuevisible = " value='"+ objItemPortability.objValue + "' ";
              descvisible  = " value='"+ objItemPortability.objDescr + "' ";
              
              if(objItemPortability.objItemName=="objComment"){
                cell = row.insertCell(index_arg - 1);
                elemText += "<div id='"+ objItemPortability.objItemName +"'  align='center' class='CellContent' style='height:35px;'> ";
                elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemPortability.objItemName)+"' "+valuevisible+" >";   
                elemText += "<textarea   name='txtS' rows=2 cols=50> ";  
                elemText +=  objItemPortability.objDescr;
                elemText += "</textarea> ";
                elemText += "</div>";
                cell.innerHTML = elemText;               
                elemText =  "";
                index_arg++;
              }else{
                  cell = row.insertCell(index_arg - 1);
                  elemText += "<div div  id='"+ objItemPortability.objItemName +"'  align='center' class='CellContent' style='height:35px;' > ";
                  elemText += "<font class='CellContent'>"+ objItemPortability.objDescr +"</font>";
                  elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemPortability.objItemName)+"' "+valuevisible+" >";   
                  elemText += "</div>";
                  cell.innerHTML = elemText;               
                  elemText =  "";
                  index_arg++;
                  continue;
              }
              
          }
      }
      
    }
  </SCRIPT>
            
            
<%
} catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
        System.out.println("    " + e.getStackTrace()[i] + "<br>");
	      }
}%>