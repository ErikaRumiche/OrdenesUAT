<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.bean.ModalityBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<% 
String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
Hashtable hshtinputNewSection = null;
String    strCustomerId= "",
          strnpSite ="",
          strCodBSCS = "",
          strSpecificationId = "",
          strDivision = "",
          strSessionId = "",
          strOrderId="",
          strObjectReadOnly = "",
          strTypeCompany="",
          strReadOnly = null,
          strObjectValidate = null;
																				    
  strObjectReadOnly = request.getParameter("strObjectReadOnly");
  strObjectValidate = request.getParameter("strObjectValidate");
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if("S".equals(strObjectReadOnly)){
    strReadOnly="readonly";
  }
  
  if ( hshtinputNewSection != null ) {
    strCustomerId        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite            =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS           =   (String)hshtinputNewSection.get("strCodBSCS");
    strSpecificationId   =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision          =   (String)hshtinputNewSection.get("strDivision");
    strSessionId         =   (String)hshtinputNewSection.get("strSessionId");
    strOrderId           =   (String)hshtinputNewSection.get("strOrderId");	
    strTypeCompany       =   (String)hshtinputNewSection.get("strTypeCompany");
  }

ItemService itemService = new ItemService();
String flagAdendam = itemService.OrderDAOgetNpAllowAdenda(MiUtil.parseInt(strSpecificationId));
%>
<script>
  function fxValidatePhone(objThis){   
    if (!ContentOnlyNumber(objThis.value)){
      alert("Número de Nextel no válido");
      form.txtItemPhone.select();
      return false;      
    }  
  }

<%if( ("S").equals(flagAdendam) ){%>
  function fxValidateAddendumAct(objPhoneNumber){   
    if(objPhoneNumber != ""){
      //DERAZO Se modifica para enviar un parametro de penalidad simulada
      //EFLORES 22/08/2016 Se envia la orden para auditoria de logs
      //var url = "<%=request.getContextPath()%>/itemServlet?id_customer=<%=strCustomerId%>&num_nextel="+objPhoneNumber+"&hdnMethod=doGetNumAddendumAct";
      var url = "<%=request.getContextPath()%>/itemServlet?id_customer=<%=strCustomerId%>&id_specification=<%=strSpecificationId%>&num_nextel="+objPhoneNumber+"&hdnMethod=doGetNumAddendumAct&order_id="+<%=strOrderId%>;
      parent.bottomFrame.location.replace(url);   
    }
  }

  function fxGetAdendasActivas(){
   var phone = form.txt_ItemPhone.value;
   form.txt_ItemPhone.value = phone;
   if (phone=="" ){
      alert("Debe ingresar el número de teléfono.");
      return;
   }
   
   url = "/portal/page/portal/addendum/ADDENDUM_LIST?an_nextel_number="+phone+"&CUSTOMERID=<%=strCustomerId%>";
   window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
   return;
  }
<%}%>
//CBARZOLA:Modificacion(Agregacion de parametro al request) 19/07/2009
  function fxGetDetailPhone(objPhone){
   var phone = objPhone;
   var bolValidateItemPhone=false;
   var vtype_window = "NEW";
   if(objPhone.length > 7) {    
         if ( <%=strSpecificationId%> == <%=Constante.SPEC_SUSPENSIONES[0]%>){
           var strNpScheduleDate = parent.opener.document.frmdatos.txtFechaProceso.value;
           var strNpScheduleDate2 = parent.opener.document.frmdatos.txtFechaReconexion.value;
         }else{
           var strNpScheduleDate = null;
           var strNpScheduleDate2 = null;
         }
         <%if( "EDIT".equals(type_window) ){%>
           vtype_window = "EDIT";
         <%}%>

         // MMONTOYA [ADT-RCT-092 Roaming con corte]
         // Inicio Inicializa el combo y el arreglo de servicios.
         if (<%=strSpecificationId%> == <%=Constante.SPEC_ACTIVAR_PAQUETES_ROAMING%>) {
            var form = parent.mainFrame.frmdatos;
            vDoc = parent.mainFrame;

            // Remover todos los items del combo.
            vDoc.DeleteSelectOptions(form.cmbItemServiceROA);

            // Remover todos los items del vector.
            vDoc.vServicio.removeElementAll();
         }
         //DERAZO Ocultamos el link de ver penalidad
         parent.mainFrame.document.getElementById("v_penalidad").style.display = "none";
         parent.mainFrame.document.getElementById("h_penalidad").value="0";

         // Fin Inicializa el combo y el arreglo de servicios.
       //INI PRY-1062 AMATEOM
       if (<%=strSpecificationId%> == <%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%> || <%=strSpecificationId%> == <%=Constante.SPEC_CAMBIO_MODELO%>) {
           var modalidadPago = parent.opener.document.frmdatos.cmbFormaPago.value;
       }else{
           var modalidadPago = null;
       }
       //FIN PRY-1062 AMATEOM
         var phone = objPhone;
         var url = "<%=request.getContextPath()%>/serviceservlet?paramModalidadPago="+modalidadPago+"&paramPhoneNumber="+objPhone+"&paramCustomer=<%=strCustomerId%>&paramDivision=<%=strDivision%>&paramSite=<%=strnpSite%>&paramSpecification=<%=strSpecificationId%>&myaction=loadDetailPhone&strSessionId=<%=strSessionId%>&strOrderId=<%=strOrderId%>&strTypeCompany=<%=strTypeCompany%>&strNpScheduleDate="+strNpScheduleDate + "&strNpScheduleDate2="+strNpScheduleDate2 + "&strTypeEvent="+vtype_window;
         parent.bottomFrame.location.replace(url);
   }//Fin CEM
  }
  
  // EZM 26/10/10
  function fxGetPlainsDefaultTel(lonIdProduct,solution){ //EZM 08/12
      //var form = parent.mainFrame.frmdatos;      
      var vmodalityid = 0;      
      var vmodel = 0;
      //var solution = 0;
      var vserviceid = '';
      
      /*try{
        vmodalityid = form.cmb_ItemModality.value;
      }catch(e){}*/
      
      try{
        //vmodel = form.hdnOriginalProductId.value;
        //vmodel = parent.opener.document.frmdatos.hdnOriginalProductId.value;
      }catch(e){}
         
      //var url = "/apporderweb/itemServlet?strModality="+vmodalityid+"&strProductId="+objValue+"&strSpecificationId=2001&strSolutionId="+solution+"&hdnMethod=getProductPlanList";
      var url = "/apporderweb/itemServlet?strModality="+vmodalityid+"&strProductId="+lonIdProduct+"&strSpecificationId=2001&strSolutionId="+solution+"&hdnMethod=getProductPlanList";      
          
      parent.bottomFrame.location.replace(url);
  }

  // EZ 26/10/10
  
  
    function fxGetPlainsDefault(lonIdProduct){ //EZM 08/12
      //var form = parent.mainFrame.frmdatos;      
      var vmodalityid = 0;      
      var vmodel = 0;
      var solution = 0;
      var vserviceid = '';

      /*try{
        vmodalityid = form.cmb_ItemModality.value;
      }catch(e){}*/
      
      try{
        //vmodel = form.hdnOriginalProductId.value;
        //vmodel = parent.opener.document.frmdatos.hdnOriginalProductId.value;
      }catch(e){}
      
      //alert("getplains del itemtelefono");
         
      //var url = "/apporderweb/itemServlet?strModality="+vmodalityid+"&strProductId="+objValue+"&strSpecificationId=2001&strSolutionId="+solution+"&hdnMethod=getProductPlanList";
      var url = "/apporderweb/itemServlet?strModality="+vmodalityid+"&strProductId="+lonIdProduct+"&strSpecificationId=2001&strSolutionId="+solution+"&hdnMethod=getProductPlanList";      
          
      parent.bottomFrame.location.replace(url);
  }
  
  function fxValidateItemPhone(phoneNumber){
    //alert("phoneNumber-->"+phoneNumber);    
    for( x = 0; x < vctItemsMainFrameOrder.size(); x++ ){    
        vctItemsMainFrameOrder.elementAt(x).elementAt(x).npobjitemheaderid;
        alert("campo del vector principal-->"+vctItemsMainFrameOrder.elementAt(x).elementAt(x).npobjitemheaderid);
        
      /*"'"+vctItemOrderMain.elementAt(x).npobjspecgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemid+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemname+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlheader+"'",
        "'"+vctItemOrderMain.elementAt(x).namehtmlitem+"'",
        "'"+vctItemOrderMain.elementAt(x).npcontroltype+"'",
        "'"+vctItemOrderMain.elementAt(x).npdefaultvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npsourceprogram+"'",
        "'"+vctItemOrderMain.elementAt(x).npspecificationgrpid+"'",
        "'"+vctItemOrderMain.elementAt(x).npdisplay+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjreadonly+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvalue+"'",
        "'"+vctItemOrderMain.elementAt(x).npobjitemvaluedesc+"'"
        );
        vctItemsAuxOrder.addElement(objMake);*/
        
    } 
    return false;
    //vctItemsMainFrameOrder.setElementAt(vctItemsAuxOrder,aux_index); 
  
  }
    
  
</script>
<!--&lt;input type=&quot;hidden&quot; name=&quot;txt_ItemPhone&quot;&gt;-->
<tr>
  <td class="CellLabel" align="left" valign="top">
    <%if("S".equals(strObjectValidate)){%>
      <font color="red">&nbsp;*&nbsp;</font>
    <%}else{%>&nbsp;&nbsp;&nbsp;<%}%>Tel&eacute;fono
  </td>
  <td align="left" class="CellContent">
  <%if(("S").equals(flagAdendam)){%>
  &nbsp;
   <!--JPEREZ: El teléfono se valida cuando el campo pierde el foco-->
    <input type="text" <%=strReadOnly%> name="txt_ItemPhone" maxlength="30" onKeyPress="return fxOnlyNumber(event);" onKeyDown="javascript:if (fxInputTabEnter()){fxGetDetailPhone(this.value);}" onblur="javascript:fxGetDetailPhone(this.value);" />
    <!--CEM-->
    <a href="javascript:fxGetAdendasActivas()">
      <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Adendas activas" width="15" height="15" border="0"/>
    </a>
    <%}else{%>&nbsp;
    <input type="text" <%=strReadOnly%> value="" name="txt_ItemPhone" maxlength="30" onKeyPress="return fxOnlyNumber(event);" onKeyDown="javascript:if (fxInputTabEnter()){fxGetDetailPhone(this.value);}" onblur="javascript:fxGetDetailPhone(this.value);"/>
    <!--CEM-->
    <%}%>
  <!--DERAZO REQ-0428, EFLORES REQ-0428_2 -->
    <a id="v_penalidad" style="display: none;" href="javascript:fShowPenalty();">Ver penalidad</a>
    <input type="hidden" id="h_penalidad" value="0"/>

    <a href="javascript:fShowDetailRepo();">Detalle Reposici&oacute;n</a>
  <script type="text/javascript">
		function fShowDetailRepo() {
			var form = document.frmdatos;
      var txt_ItemPhone = form.txt_ItemPhone.value;
      
      if (txt_ItemPhone != "") {
        var url = "<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/popUpDetalleReposicion.jsp?txt_ItemPhone="+txt_ItemPhone;
        WinAsist = window.open(url,"DetalleRepo","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
			}else {
				alert("Ingrese un número de teléfono");
        form.txt_ItemPhone.focus();
      }
    }
	</script>
  <!--DERAZO-->
  <script type="text/javascript">
      function fShowPenalty() {
          var form = document.frmdatos;
          var txt_ItemPhone = form.txt_ItemPhone.value;

          if (txt_ItemPhone != "") {
              var url = "<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/popUpVerPenalidad.jsp?txt_ItemPhone="+txt_ItemPhone+"&paramCustomer=<%=strCustomerId%>";
              WinAsist = window.open(url,"VerPenalidad","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=400,height=300,modal=yes");
          }else{
              alert("Ingrese un número de teléfono");
              form.txt_ItemPhone.focus();
          }
      }
  </script>
  </td>
</tr>


<%if( !type_window.equals("NEW") ){
  
  String[] paramNpobjitemvalue      = request.getParameterValues("b");
  String[] paramNpobjitemheaderid   = request.getParameterValues("a");
  String strWarrant   = null;
  String strEvaluateWarrant = "";
  String strEquipment = null;
  for(int i=0;i<paramNpobjitemheaderid.length; i++){
    if( paramNpobjitemheaderid[i].equals("52") ) {
        strEvaluateWarrant = MiUtil.getString(paramNpobjitemvalue[i]);
        if(strEvaluateWarrant.equals("NO")) strWarrant = "N";
        else if( strEvaluateWarrant.equals("SI")) strWarrant = "S";
        else strWarrant = strEvaluateWarrant;
    }
    if( paramNpobjitemheaderid[i].equals("11") ) {
        strEquipment = paramNpobjitemvalue[i];
    }
  }
%>

  <script>
  function fxLoadModalityByPhone(){
  <%HashMap objHashMap = null;
    objHashMap = (new NewOrderService()).OrderDAOgetModalityList(MiUtil.parseLong(strSpecificationId),strEquipment,strWarrant,null);
    if( objHashMap.get("strMessage") != null ){
      System.out.println("Error");
      //throw new Exception( objHashMap.get("strMessage") );
    }else{
      ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
      ModalityBean  objModalityBean = null;
      
      if ( objArrayList != null && objArrayList.size() > 0 ){%>
      var vDoc = parent.mainFrame;
      var form = vDoc.frmdatos;
      vDoc.DeleteSelectOptions(form.cmb_ItemModality);
  <%  for( int i=0; i<objArrayList.size();i++ ){
        objModalityBean = new ModalityBean();
        objModalityBean = (ModalityBean)objArrayList.get(i);
  %>
      vDoc.AddNewOption(form.cmb_ItemModality,'<%=objModalityBean.getNpmodality()%>','<%=objModalityBean.getNpmodality()%>');
  <%  }}} %>   
  }
  </script>  
<%}%>