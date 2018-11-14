<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.NpTableBean" %>
<%
try{

  String strSessionId1="";

  try {
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
  } catch(Exception e) {
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
    return;
  }
    //EFLORES PRY-0979 Origen de orden que permitira saltar la anulacion
    GeneralService objGeneralService= new GeneralService();
    HashMap hashOrigenPermit = objGeneralService.getDataNpTable("ORDER_CPT_ANULAR_ORIGEN","ORIGEN");
    ArrayList origen_permit = new ArrayList();
    String msgErrorOrigenPermit = (String) hashOrigenPermit.get("strMessage");
    if(msgErrorOrigenPermit != null){
        System.out.println("Error ORIGEN_PERMIT [PRY-0979][JP_ORDER_DETAIL_START_ShowPage.jsp] "+msgErrorOrigenPermit);
    }else{
        origen_permit = (ArrayList)hashOrigenPermit.get("objArrayList");
    }

    // strSessionId1 = "998102396";
  String strRutaContext=request.getContextPath();
  String strURLPayment = strRutaContext+"/PAGEEDIT/OrderPaymentEdit.jsp";
  String strURLOrderServlet =strRutaContext+"/editordersevlet"; 
  String strURLReject = strRutaContext+"/PAGEEDIT/OrderRejectEdit.jsp";
    String actionURL_GeneralServlet = strRutaContext+"/generalServlet"; //EFLORES PM0010274
%>

<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></SCRIPT>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderNew.js"></script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderException.js"></script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></SCRIPT>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></SCRIPT> <!-- EFLORES PM0010274 -->

 <script>
     //EFLORES PRY-0979 Array de origenes que permitiran anular
     var arrAnularCPT = [];
     <% for (int i=0; i<origen_permit.size(); i++) {
     NpTableBean tb = (NpTableBean)origen_permit.get(i); %>
     arrAnularCPT[<%= i %>] = "<%= tb.getNpvalue() %>";
     <% } %>

 function fxObjectConvert(objObject,objValue){
    form = document.frmdatos;
    try{
       cadena = "form."+objObject+".value = '"+objValue+"'";
       eval(cadena);
    }catch(e){
    
    }
 }
 
 function fxEditPayment(){
         var form = document.frmdatos;
         var v_parametros = "?sUrl=<%=strURLPayment%>"  
                        +"¿hdnPagoBanco="     + escape(form.hdnPagoBanco.value)
                        +"|hdnPagoNroVoucher="     + escape(form.hdnPagoNroVoucher.value)
                        +"|hdnPagoImporte="        + escape(form.hdnPagoImporte.value)
                        +"|hdnPagoFecha="          + escape(form.hdnPagoFecha.value)
                        +"|hdnPagoDisponible="     + escape(form.hdnPagoDisponible.value)
                        +"|hdnRuc="                + escape(form.txtRucDisabled.value)
                        +"|txtImporteFactura="     + escape(form.txtImporteFactura.value)
                        +"|hdnTotalPaymentOrig="   + escape(form.hdnTotalPaymentOrig.value);
                             
         var v_Url=   "<%=strRutaContext%>/GENERALPAGE/GeneralFrame.jsp" +v_parametros;         
         var popupWin = window.open(v_Url, "Orden_Pagos","status=yes, location=0, width=450, height=500, left=50, top=100, screenX=50, screenY=100");
      
 }
 
  function fxGenerarComprobantes(){      
      form = document.frmdatos; 
 
      form.myaction.value='generateDocumentDetail';           
      form.action="<%=strURLOrderServlet%>";             
      form.submit();   
   }

   function fxGenerarOrdenPago(){      
      form = document.frmdatos; 

      form.myaction.value='generatePayOrder';           
      form.action="<%=strURLOrderServlet%>";             
      form.submit();        
   }

   function fxPartedeIngreso(){      
      form = document.frmdatos; 

      form.myaction.value='parteIngreso';           
      form.action="<%=strURLOrderServlet%>";             
      form.submit();        
   }  
   
   function fxAutorizacionDevolucion(){      
        form = document.frmdatos; 

        form.myaction.value='autorizacionDevolucion';           
        form.action="<%=strURLOrderServlet%>";             
        form.submit();        
        
   } 
   
   function fxCambiarInbox(valor,specId){  
   //alert(" valor-->"+valor+" specId-->"+specId);
   var Form = document.frmdatos;
   if (valor=='RETROCEDER'){    
      idRegionInboxBack.style.display='';                 
      idRegionInboxNext.style.display='none';    
      Form.cmbInboxBack.value="";
   }else if (valor=='SALTAR'){     
      idRegionInboxBack.style.display='none';        
      idRegionInboxNext.style.display='';   
      DeleteSelectOptions(Form.cmbInboxNext); 
      Form.myaction.value="loadInboxSaltar";      
      var url = "<%=strURLOrderServlet%>"+"?strComboName=cmbInboxNext&strFormName=frmdatos&strValorName=inbox&strDescripcionName=inbox"; 
      Form.action=url;             
      Form.submit(); 
   }else{ 
      DeleteSelectOptions(Form.cmbInboxNext);     
      idRegionInboxBack.style.display='none';        
      idRegionInboxNext.style.display='none';           
   }
 }   

   function fxDoSubmitDetail(myAction){
      form = document.frmdatos; 
      
      //Agregado por RMARTINEZ 26-08-2009 para validar que el campo combo destino sea requerido siempre que la accion sea "Ir a"
      if( form.hdnInboxBack != undefined) { 
        if (form.hdnInboxBack.value=="" && form.cmbAction.value != "") {
             //EFLORES PRY-0979 Se salta la alerta si el inbox de origen esta configurado
            if(($.inArray(form.hdnOrigen.value,arrAnularCPT) == -1 )){
                alert("Seleccione el inbox destino");
                return false;
            }
        }        
      }
      //Fin Agregado por RMARTINEZ 26-08-2009
      
      if(myAction=='updateOrdenDetail'){         
        form.myaction.value=myAction;          
        form.action="<%=strURLOrderServlet%>";             
        form.submit();     
      }      
                       
   }
   
    function fxDetailReject(){
        var Form = document.frmdatos;

       var v_parametros = "?hdnOrderId="+   g_orderid 
                     +"&txtEstadoOrden="+  Form.txtEstadoOrden.value
                     +"&WorkFlowType="+   Form.hdnWorkflowType.value
                     +"&specialtyid="+     Form.hdnSubCategoria.value
                     +"&hdnRejectPermit=D"
                     +"&hdnSessionId=<%=strSessionId1%>";
                window.open("<%=strURLReject%>"+v_parametros, "Orden_Rechazos","status=yes, location=0, scrollbars=yes, width=800, height=500, left=50, top=100, screenX=50, screenY=100");
               
    }
    
    function MuestraFechaReconexBySpecification(specificationId){//Agregado por rmartinez 19-06-2009
     form = document.frmdatos;
      
     //Se agrego para suspensiones temporales rmartinez
     if(specificationId == <%=Constante.SPEC_SUSPENSIONES[0]%>){
       dvFecReconexEdit.style.display="";
       dvFecReconexEditInput.style.display="";
     }else{
       dvFecReconexEdit.style.display="none";
       dvFecReconexEditInput.style.display="none";
     }          
   }

    /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/
  function MuestraFechaProgFin(specificationId){
    form = document.frmdatos;
    if(specificationId == <%=Constante.SPEC_SERVICIOS_ADICIONALES[0]%>){
      dvFechaFinProgEdit.style.display="";
      dvFechaFinProgEditInput.style.display="";
    }else{
      dvFechaFinProgEdit.style.display="none";
      dvFechaFinProgEditInput.style.display="none";
    }
   }
  /*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
// EFLORES  PM0010274
         function getCustDetail(customerId,userId){
             $.ajax({
                 url:"<%= actionURL_GeneralServlet %>",
                 type:"GET",
                 async:false,
                 data:{
                     "metodo":"requestVerifyUserCanSeeCustomer",
                     "strUserId" : userId,
                     "strObjectId":customerId,
                     "strObjectType":"CUSTOMER",
                     "strTypeMessage":"ORDEN_LISTAR"
                 },
                 success:function(data){
                     var val = data.split("|");
                     var num = val[0].substring(3,2);
                     if(num == "0"){
                         alert(val[1]);
                     }else{
                         getCustomerDetail();
                     }
                 },
                 error:function(){
                     alert("Hay un error");
                 }
             });
         }
// FIN EFLORES
 </script>
<form name="frmdatos" id="frmdatos" action="" method="post" target="bottomFrame" > 
<input type="hidden" name="hdnSessionId" value="<%=strSessionId1%>">



<%}catch(SessionException se) {
     se.printStackTrace();
     out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
     String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
   }catch(Exception e) {
     String strMessageExceptionGeneralStart = "";
     strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
     out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
   }
%>