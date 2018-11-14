<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.portability.bean.PortabilityItemBean"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="pe.com.portability.bean.PortabilityContactBean"%>
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
   PortabilityContactBean objOrderPortabilityReturnHome = null;
   HashMap hshOrderPortability = new HashMap();
   String  strMessage = null;
   
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));      
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   PortalSessionBean sessionUser  = objSession.getUserSession(strSessionId);
   String strLogin=sessionUser.getLogin();
   
   //Traemos datos para la orden de Portabilidad
   //-------------------------------------------                         
   hshOrderPortability = (HashMap)objPortabilityOrderService.getOrderPortabilityReturnHome(lOrderId);
   strMessage         =  (String)hshOrderPortability.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);  
      
   objOrderPortabilityReturnHome=(PortabilityContactBean)hshOrderPortability.get("objOrderPortabilityReturnHome");

   //Traemos el detalle del item  la Orden de Portabilidad Retorno
   //------------------------------------------------------
   objHashItemPortability    = (HashMap)objPortabilityOrderService.getItemOrderPortabilityReturnHome(lOrderId);
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
               <table border="0" cellspacing="0" cellpadding="0" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Retorno</td>
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
                  <tr>				
                     <td class="CellContent" align="center" width="7%"><%=objOrderPortabilityReturnHome.getNpreceptorname()%></td>
                     <td class="CellContent" align="center" width="7%"><%=objOrderPortabilityReturnHome.getNpconctactname()%></td>				
                     <td class="CellContent" align="center" width="10%"><%=objOrderPortabilityReturnHome.getNpcontactemail()%></td>            
                     <td class="CellContent" align="center" width="8%"><%=objOrderPortabilityReturnHome.getNpcontactphone()%></td>            
                     <td class="CellContent" align="center" width="8%"><%=objOrderPortabilityReturnHome.getNpcontactfax()%></td>                   							
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
                     <td class="CellLabel" align="center" width="5%">Nro</td>
                     <td class="CellLabel" align="center" width="10%">Nro. Móvil</td>
                     <td class="CellLabel" align="center" width="10%">Fecha Act. Programada</td>				
                     <td class="CellLabel" align="center" width="10%">Motivo de Retorno</td>            
                     <td class="CellLabel" align="center" width="15%">Estado de Portabilidad</td>  
                     <td class="CellLabel" align="center" width="10%">Id. Proceso</td> 
                     <td class="CellLabel" align="center" width="10%">Programaci&oacute;n en BSCS</td>  
                     
                     <td class="CellLabel" align="center" width="10%">Num. a Programar</td>
                     
                     <td class="CellLabel" align="center" width="30%">Observaciones</td>
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
    var appContext = "<%=request.getContextPath()%>";
    var vctItemsMainFrameOrder = new Vector();

    //Funcion General
    //---------------
    function fxGetRowIndex(objTable){
      try{
        var index_table = objTable.parentNode.parentNode.parentNode.rowIndex;
      }catch(e){
        var index_table = objTable.parentNode.rowIndex;
      }
        return index_table;
    }
    
    function fxaddElementsItemsMain(vctItemOrderMain){
      var vctItemsAuxOrder = new Vector();  
    
      //alert("vctItemOrderMain : " + vctItemOrderMain.size() ) ;
  
       for( x = 0; x < vctItemOrderMain.size(); x++ ){       
            var objMake = new fxMakeItemPortability(
                "'"+vctItemOrderMain.elementAt(x).id+"'",
                "'"+vctItemOrderMain.elementAt(x).itemid+"'",
                "'"+vctItemOrderMain.elementAt(x).orderid+"'",
                "'"+vctItemOrderMain.elementAt(x).objItemName+"'",
                "'"+vctItemOrderMain.elementAt(x).objValue+"'",
                "'"+vctItemOrderMain.elementAt(x).objDescr+"'"
                );
        vctItemsAuxOrder.addElement(objMake);
      
      }
  
    vctItemsMainFrameOrder.addElement(vctItemsAuxOrder);
  }
    
    //Función que invoca al Popup para visualizar el detalle del Item
    //---------------------------------------------------------------
    function fxGetItemDetail(item_index,vctrItemOrden) {
   
      var itemId = 0;
      var num_rows = getNumRows("items_table");
      var vctAuxRead = new Vector();
      
      if( num_rows == 1 ) itemId = form.hdnItemPortId.value;
        else  itemId = form.hdnItemPortId[item_index-1].value;
      
      vctAuxRead = vctrItemOrden.elementAt(item_index-1);
    
      var urlPage = "&";
      for( i = 0 ; i < vctAuxRead.size(); i++ ){
           var objVector = vctAuxRead.elementAt(i);
           if (replace(objVector.objItemName,"'","") == "objPhoneNumber"){
               urlPage = urlPage + replace(objVector.objItemName,"'","") + "=" + replace(objVector.objValue,"'","");
           }
      }
      var frameUrl = appContext+"/DYNAMICSECTION/DynamicSectionPortabilityReturnHome/DynamicSectionPortabilityReturnHomePage/PopUpOrderPortabilityReturn.jsp?strItemId="+itemId+urlPage+"&item_index="+(item_index-1);
      var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionPortabilityReturnHome/DynamicSectionPortabilityReturnHomePage/PopUpOrderPortabilityFrame.jsp?av_url="+escape(frameUrl);            
      var popupWin = window.open(winUrl, "OrdenPortability_Item","status=yes, location=0, width=400, height=250, left=50, top=30, screenX=50, screenY=50");
    }
    
    //Se carga los datos de BD del Item, esta función se invoca desde la tabla websales.np_page_section_event
    //-------------------------------------------------------------------------------------------------------
    function fxOnloadPortabilityReturnHome(){
      fxLoadDataPortabilityReturnHome();
    }
    
    //Funcion que llena los datos en el vector de los items de Portabilidad de Retorno
    //---------------------------------------------------------------------------------
    function fxLoadDataPortabilityReturnHome(){
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
                  ///Fecha de Ejecución Programada
                  //------------------------------
                  objValue  =   MiUtil.getDate(MiUtil.getString(portabilityItemBean.getNpexecutiondatereturn()));
                  objDescr  =   MiUtil.getDate(MiUtil.getString(portabilityItemBean.getNpexecutiondatereturn()));
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objExecDate','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Motivo de Retorno
                  //-------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpReasonType());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpReasonType());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objReturnReject','<%=objValue%>','<%=objDescr%>'));
                  
                  <%
                  ///Estado del Mensaje de Portabilidad
                  //-----------------------------------
                  objValue  =   MiUtil.getString(portabilityItemBean.getMessagetype());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getMessagetype());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objStatusMessage','<%=objValue%>','<%=objDescr%>'));
                  
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
                  ///Comentario
                  //-----------
                  objValue  =   MiUtil.getString(portabilityItemBean.getNpComment());
                  objDescr  =   MiUtil.getString(portabilityItemBean.getNpComment());
                  %>
                  
                  vctOrderItemPortability.addElement(new fxMakeItemPortability('<%=i+1%>','<%=port_itemId%>','<%=port_orderId%>','objComment','<%=objValue%>','<%=objDescr%>'));
                  
                  fxAddRowOrderItemsPortabilityReturnEdit(vctOrderItemPortability);
            
                  <%
              }
          }
        %>
      
    }
    
    function fxAddRowOrderItemsPortabilityReturnEdit(vctOrderItemPortability) {
    
      form = parent.mainFrame.document.frmdatos;
      fxaddElementsItemsMain(vctOrderItemPortability);
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
                          "<a href=\'javascript:;\'  onclick=\'javascript: fxGetItemDetail(fxGetRowIndex(this),vctItemsMainFrameOrder);\' style =\'text-decoration:none\' ALT=\'Editar Item\'><font color=\'black\' size=\'1\' face=\'Arial\' ><b>"+"<img src=\'<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif\' border=\'0\'>" +"  "+ vctOrderItemPortability.elementAt(0).id +"</b></font></a> "+
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
              
              cell = row.insertCell(index_arg - 1);
              elemText += "<div div  id='"+ objItemPortability.objItemName +"'  align='center' class='CellContent' style='height:35px;' > ";
              elemText += "<font class='CellContent'>"+ objItemPortability.objDescr +"</font>";
              elemText += "<input type='hidden' name='hdnItemValue"+trim(objItemPortability.objItemName)+"' "+valuevisible+" >";   
              elemText += "</div>";
              cell.innerHTML = elemText;               
              elemText =  "";
              index_arg++;
              
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