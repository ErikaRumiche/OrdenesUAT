<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>

<%
  System.out.println("*********************************************objItemPlanTarifario.jsp*********************************************");
  Hashtable hshtinputNewSection = null;
  HashMap hshData  = null;
  ArrayList objArrayList  = new ArrayList();
  NewOrderService objNewOrderService = new NewOrderService();
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strOrderId = "",
            strSessionId = ""
            //INICIO: AMENDEZ | PRY-1049
            ,strHdnCobertura=""
            //FIN: AMENDEZ | PRY-1049
          ;
  hshtinputNewSection       = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
    strOrderId              =   (String)hshtinputNewSection.get("strOrderId");	 
    //INICIO: AMENDEZ | PRY-1049
    strHdnCobertura         =   (String)hshtinputNewSection.get("strHdnCobertura");
    System.out.println("strCodigoCliente   :"+strCodigoCliente);
    System.out.println("strnpSite          :"+strnpSite);
    System.out.println("strCodBSCS         :"+strCodBSCS);
    System.out.println("hdnSpecification   :"+hdnSpecification);
    System.out.println("strSessionId       :"+strSessionId);
    System.out.println("strOrderId         :"+strOrderId);
    System.out.println("strHdnCobertura    :"+strHdnCobertura);
    //FIN: AMENDEZ | PRY-1049
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
    System.out.println("******************************************************************************************");
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  System.out.println("nameHtmlItem    :"+nameHtmlItem);
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String strMessage = "";
  String strErrorLocal = "";
  PlanTarifarioBean pBean = new PlanTarifarioBean();
  String type = request.getParameter("strTypeCompany");
   
%>

<script language="javascript">

var vctPlan = new Vector();

function fxMakePlan(objPlanCode,objPlanType){
  this.objPlanCode         = objPlanCode;
  this.objPlanType         = objPlanType;
}

 function fxShowService(objThis){
 

    if (trim(objThis.value)!=""){
      var serviceAditional = "";
      var hdnSpecification = <%=hdnSpecification%>;
      var  valorSolution='';
      var vmodalityid = 0;
      var vproductid = 0;
      var vmodel = '';
      var phone = '';//SAR N_O000012485 - FPICOY - 09/01/2014
      try{
        serviceAditional = GetSelectedServices();
        valorSolution    =parent.mainFrame.frmdatos.cmb_ItemSolution.value;
        //Inicio SAR N_O000012485 - FPICOY - 09/01/2014
        if (hdnSpecification==2065 || hdnSpecification==2013) {
            phone = parent.mainFrame.frmdatos.txt_ItemPhone.value;
        }
        //Fin SAR N_O000012485 - FPICOY - 09/01/2014
         try{
         vmodalityid = form.cmb_ItemModality.value;
        }catch(e){}
        try{// EZM HPPT+ Validación del productid según categoría
           <%if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA))
           || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
            ){%>
              vproductid = parent.mainFrame.frmdatos.txt_ItemModel.value;
           <% }  
           else {%>
              vproductid = parent.mainFrame.frmdatos.cmb_ItemProducto.value;
             
    <% } %>
        
        }catch(e){}
        try{
          vmodel = form.txt_ItemModel.value;
       }catch(e){}
        
      }catch(e){ valorSolution='';phone='';}//SAR N_O000012485 - FPICOY - 09/01/2014
      
        var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&cmb_ItemPlanTarifario="+objThis.value+"&hdnSpecification="+hdnSpecification+"&serviceAditional="+serviceAditional+"&strSessionId=<%=strSessionId%>&cmb_ItemSolution="+valorSolution+"&strobjectItem=<%=nameHtmlItem%>"+"&strPlanId="+objThis.value+"&strModality="+vmodalityid+"&strProductId="+vproductid+"&strModelId="+vmodel+"&txt_ItemPhone="+phone;//SAR N_O000012485 - FPICOY - 09/01/2014
        parent.bottomFrame.location.replace(url);
      }
    }
    
 function fxShowService_Trans(objThis){


    if (trim(objThis.value)!=""){
      var serviceAditional = "";
      var hdnSpecification = <%=hdnSpecification%>;
      var  valorSolution='';
      var vmodalityid = 0;
      var vproductid = 0;
      var vmodel = '';
      var phone = '';//SAR N_O000012485 - FPICOY - 09/01/2014
      try{
        serviceAditional = GetSelectedServices();
        valorSolution    =parent.mainFrame.frmdatos.cmb_ItemSolution.value;
	//Inicio SAR N_O000012485 - FPICOY - 09/01/2014
        if (hdnSpecification==2065 || hdnSpecification==2013) {
            phone = parent.mainFrame.frmdatos.txt_ItemPhone.value;
        }
        //Fin SAR N_O000012485 - FPICOY - 09/01/2014
         try{
         vmodalityid = form.cmb_ItemModality.value;
        }catch(e){}
        try{// EZM HPPT+ Validación del productid según categoría
           <%if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA))
           || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
            ){%>
              vproductid = parent.mainFrame.frmdatos.txt_ItemModel.value;
           <% }  
           else {%>
              vproductid = parent.mainFrame.frmdatos.cmb_ItemProducto.value;
             
    <% } %>
        
        }catch(e){}
        try{
          vmodel = form.txt_ItemModel.value;
       }catch(e){}
        
      }catch(e){ valorSolution='';phone='';}//SAR N_O000012485 - FPICOY - 09/01/2014
      
        var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&cmb_ItemPlanTarifario="+objThis.value+"&hdnSpecification="+hdnSpecification+"&serviceAditional="+serviceAditional+"&strSessionId=<%=strSessionId%>&cmb_ItemSolution="+valorSolution+"&strobjectItem=<%=nameHtmlItem%>"+"&strPlanId="+objThis.value+"&strModality="+vmodalityid+"&strProductId="+vproductid+"&strModelId="+vmodel+"&txt_ItemPhone="+phone;//SAR N_O000012485 - FPICOY - 09/01/2014
        parent.bottomFrame.location.replace(url);
      }
    }
      
  function fxExecuteMethod(objThis){
    var form = parent.mainFrame.frmdatos;
    var vDoc = parent.mainFrame;
    var hdnSpecification = <%=hdnSpecification%>;
    vDoc.DeleteSelectOptions(form.cmb_ItemPromocion);
    vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');
    vDoc.fxObjectConvert('txt_ItemPriceException','');
    vDoc.fxObjectConvert('txt_ItemEquipmentRent','');
    /*parent.mainFrame.DeleteSelectOptions(form.cmb_ItemPromocion);
    form.txt_ItemPriceCtaInscrip.value = '';
    form.txt_ItemPriceException.value = '';
    form.txt_ItemEquipmentRent.value = '';*/
    
    //PRY-0762 JQUISPE Renta Adelantada
    try{
    	fxResetFieldRentaAdelantada();
    }catch(e){}
    
    //PRY-0762 JQUISPE Renta Adelantada
    var flagRentaAdelantada = parent.mainFrame.document.getElementById("hdnFlagBuscarRA");
    if(flagRentaAdelantada){    	
    	if(flagRentaAdelantada.value == "true"){
    		var idProductoRA = form.cmb_ItemProducto.value;
    		var idPlanRA = form.cmb_ItemPlanTarifario.value;
    		
    		if(idProductoRA != "" && idPlanRA != ""){
    			fxShowValueRentaAdelantada(idProductoRA,idPlanRA);
    		}   		
    	}
    }
    
    if (hdnSpecification = 2017){
      fxShowService_Trans(objThis);
      }
    else{
      fxShowService(objThis);
      }
    //alert('va a llamar a cadena');
    var cadena = GetSelectedServices();//geDefaultIdServices();
    //alert(cadena);
  }
  
  function geDefaultIdServices() {
     //alert('entro a default');
      var str = "";
      var cont=0;
      var wn_cantServ = vServicio.size();
      //alert('la cantidad es--' + wn_cantServ);
      for(j=0; j<wn_cantServ; j++){
          objServicio = vServicio.elementAt(j);
          //alert('El name222 es--->' + objServicio.name + '--' + objServicio.exclude + '--' + objServicio.active_new + '--' + objServicio.modify_new + '--');
          if (objServicio.modify_new == "S") {
            str =  str +  objServicio.id + ",";
            ++cont;
          }
      }
      if (cont>0) {
         str =  str.substr(0,str.length-1);
      }
      return str;
  }
  
	//PRY-0762 JQUISPE Renta Adelantada
	function fxShowValueRentaAdelantada(idProductoRA, idPlanRA){
		  var url_server = "<%=request.getContextPath()%>/itemServlet";
		  var form = parent.mainFrame.frmdatos;
	      var params = "hdnMethod=showValueRentaAdelantada&strProductId="+idProductoRA+"&strPlanId="+idPlanRA+"&strSessionId=<%=strSessionId%>";
	
	      jQuery.ajax({
	          url: url_server,
	          data: params,
	          type: "POST",          
	          dataType: 'json',
	          success:function(data) {        	  
	        	  if(data.strMessage != null){
	        		  alert(data.strMessage);        		  
	        	  }else{
	        		  var objCantidadRA = parent.mainFrame.frmdatos.txt_CantidadRA;
	              	  if(objCantidadRA){
	              		 objCantidadRA.value = data.intCantidadRA;
	              	  }
	            	  parent.mainFrame.hdnPrecioPlanRA.value = data.strPrecioPlan;
	            	  
	            	  fxAddValueTotalRentaAdelantada();
	        	  }        	  
	          },
	          error:function(xhr, status, error) {
	              alert("Error interno al obtener los valores para Renta Adelantada "+xhr.responseText);              
	          }
	      });
	  }
	
   
  </script>

<% 
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  ProductBean objProductBean = null;
  GeneralService objGeneralService = new GeneralService();
  ArrayList arrList = new ArrayList();
  if( !type_window.equals("NEW") ){
  
    String[] paramNpobjitemvalue      = request.getParameterValues("b");
    String[] paramNpobjitemheaderid   = request.getParameterValues("a");
    String strSolution     = "";
    String strPhoneNumber   = "";
    String strEvaluateWarrant = "";
    String strEquipment = null;
    String strProductLine = "";
    String strProductId = "";
    //INICIO: AMENDEZ | PRY-1049
    String strModality = "";
    //FIN: AMENDEZ | PRY-1049
    if(paramNpobjitemheaderid!=null && paramNpobjitemheaderid.length>0) {
      for(int i=0;i<paramNpobjitemheaderid.length; i++){
        if( paramNpobjitemheaderid[i].equals("93") ) {
            strSolution = MiUtil.getString(paramNpobjitemvalue[i]);
        }
        if( paramNpobjitemheaderid[i].equals("9") && !String.valueOf(Constante.SPEC_TRANSFERENCIA).equals(hdnSpecification)) {//EZM HPPTT+ Compatibilidad Modelo-Plan-Servicio
            strProductId = paramNpobjitemvalue[i];
        }
        if( paramNpobjitemheaderid[i].equals("3") ) {
            strPhoneNumber = MiUtil.getString(paramNpobjitemvalue[i]);
        }
        if( paramNpobjitemheaderid[i].equals("11") ) {
            strEquipment = paramNpobjitemvalue[i];
        }
        if( paramNpobjitemheaderid[i].equals("51") ) {
            strProductLine = paramNpobjitemvalue[i];
        }
        if( paramNpobjitemheaderid[i].equals("15") && (String.valueOf(Constante.SPEC_TRANSFERENCIA).equals(hdnSpecification) || String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO).equals(hdnSpecification))) {//EZM HPPTT+ Compatibilidad Modelo-Plan-Servicio
            strProductId = paramNpobjitemvalue[i];
        }
        //INICIO: AMENDEZ | PRY-1049
        if( paramNpobjitemheaderid[i].equals("1") ) {
          strModality = paramNpobjitemvalue[i];
        }
        //FIN: AMENDEZ | PRY-1049
        
      }
        String  strSpecificationId  = request.getParameter("strSpecificationId");
        if(StringUtils.isNotBlank(strPhoneNumber)) {
        strPhoneNumber      = objGeneralService.GeneralDAOgetWorldNumber(strPhoneNumber,"COUNTRY");
        HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhoneBySpecification(strPhoneNumber,MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strnpSite),MiUtil.parseLong(hdnSpecification),MiUtil.parseLong(strOrderId));
        if( objHashMap.get("strMessage") != null ){
          System.out.println("<script>alert('"+MiUtil.getMessageClean((String)objHashMap.get("strMessage"))+"');</script>");
        } else {
           objProductBean = (ProductBean)objHashMap.get("objProductBean");         
        }
      }
    }
  %>
  <script>
  <%
     
   if ( ( MiUtil.parseInt(strProductLine) == Constante.PRODUCT_LINE_EQ_TF  ) || ( MiUtil.parseInt(strProductLine) == Constante.PRODUCT_LINE_KIT_TF  ) || ( MiUtil.parseInt(strProductLine) == Constante.PRODUCT_LINE_EQ_INTERNET  )   ) {
      %>
      try{
      fxOcultarTr("yes",idDisplay10);
      }catch(e){}
      <%
   }
  %>
  //ACA====
  </script>

 <script> 
 
  function fxLoadEditPlan(){
   
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemPlanTarifario);
    parent.mainFrame.vctPlan.removeElementAll();
    var flagcoverage=parent.opener.parent.mainFrame.document.frmdatos.hdnCobertura.value;
    var baficonfig=validateConfigBafi2300ForLoad();

    <%

     pBean.setFlagCoverage(MiUtil.parseInt(strHdnCobertura));
     pBean.setNptipo2("0");
     pBean.setNpsolutionid(MiUtil.parseInt(strSolution));
     pBean.setNpspecificationid(MiUtil.parseInt(hdnSpecification));
        
     pBean.setNpprodlineval(MiUtil.parseInt(strProductLine));//EZUBIAURR 14/03/11
     //INICIO: AMENDEZ | PRY-1049
     pBean.setNpmodality(strModality);
     pBean.setNpproductid(MiUtil.parseLong(strProductId));
     pBean.setNpproductlineid(MiUtil.parseLong(strProductLine));
     pBean.setNpsolutionidbafi(MiUtil.parseInt(strSolution));
     System.out.println("pBean.toString(): "+pBean.toString());
     //FIN: AMENDEZ | PRY-1049
     
     System.out.println("ProductId: "+MiUtil.parseInt(strProductId));
     
     if (MiUtil.parseInt(strSolution) == Constante.PRODUCT_KIT_INTERNET){//EZUBIAURR
     pBean.setNprequestapprove(0);
     } else{
     pBean.setNprequestapprove(MiUtil.parseInt(strProductId));
     }
     
     
     hshData = (HashMap)(new NewOrderService()).PlanDAOgetPlanList(pBean,type);
     if( hshData.get("strMessage")!= null )
         throw new Exception((String)hshData.get("strMessage"));
      objArrayList = (ArrayList)hshData.get("objPlanList"); 
      
      int cant = 0;
      if ( objArrayList != null && objArrayList.size() > 0 ){
          System.out.println("objArrayList.size(): "+objArrayList.size());
          PlanTarifarioBean planTarifarioBean = null;
          for(int i=0; i<objArrayList.size(); i++) {
              planTarifarioBean = new PlanTarifarioBean();
              planTarifarioBean = (PlanTarifarioBean)objArrayList.get(i);
              DecimalFormat decFormat = new DecimalFormat("###");
              if( objProductBean != null) {
                  if(decFormat.format(planTarifarioBean.getNpplancode()).equalsIgnoreCase(String.valueOf(objProductBean.getNpplanid()))) {
                      cant=1;
                    }
              }
    %>
            parent.mainFrame.vctPlan.addElement(new parent.mainFrame.fxMakePlan('<%=decFormat.format(planTarifarioBean.getNpplancode())%>','<%=planTarifarioBean.getNptipo2()%>'));
            parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPlanTarifario,'<%=decFormat.format(planTarifarioBean.getNpplancode())%>','<%=MiUtil.getMessageClean(planTarifarioBean.getNpdescripcion())%>');
           
      <%
          }
           if(cant==0 && objProductBean!=null) {
      %>
            parent.mainFrame.vctPlan.addElement(new parent.mainFrame.fxMakePlan('<%=objProductBean.getNpplanid()%>','<%=planTarifarioBean.getNptipo2()%>'));
            parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPlanTarifario,'<%=objProductBean.getNpplanid()%>','<%=MiUtil.getMessageClean(objProductBean.getNpplan())%>');
      
      <%
           }
          
      }
      
     %>
    
      //INICIO: PRY-1049
      if(baficonfig==1 && (flagcoverage==1 ||flagcoverage==0 )){
          var form=parent.opener.parent.mainFrame.document.frmdatos;
          var regionValue   = form.hdnRegion.value;
          var provinceValue = form.hdnProvince.value;
          var districtValue = form.hdnDistrict.value;
          var regionValueId   = form.hdnRegiondId.value;
          var provinceValueId = form.hdnProvinceId.value;
          var districtValueId = form.hdnDistrictId.value;

          //LGONZALES PRY-1140
          if(parent.mainFrame.frmdatos.cmb_ItemRegion.length &&
              parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId)){
              parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId);
          }else{
          parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId,regionValue);
              parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId);
          }

          //LGONZALES PRY-1140
          if(parent.mainFrame.frmdatos.cmb_NpProvinceZone.length &&
              parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId)){
              parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId);
          }else{
          parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId,provinceValue);
              parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId);
          }

          <!-- BEGIN: PRY-1049 | DOLANO-0002 -->
          <!-- Ocultar el campo de distrito cuando no hay cobertura a nivel de distrito -->
          if(districtValueId == null || districtValueId == -1){
             parent.mainFrame.frmdatos.cmb_NpDistrictZone.value = "";
             fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
          } else {
              //LGONZALES PRY-1140
              if(parent.mainFrame.frmdatos.cmb_NpDistrictZone.length &&
                  parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId)){
                  parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId);
              }else{
             parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId,districtValue);
                  parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId);
              }
          }
          <!-- END: PRY-1049 | DOLANO-0002 -->

          parent.mainFrame.frmdatos.cmb_ItemRegion.disabled     =true;
          parent.mainFrame.frmdatos.cmb_NpProvinceZone.disabled =true;
          parent.mainFrame.frmdatos.cmb_NpDistrictZone.disabled =true;

      } else{
        parent.mainFrame.frmdatos.cmb_ItemRegion.disabled     =false;
        parent.mainFrame.frmdatos.cmb_NpProvinceZone.disabled =false;
        parent.mainFrame.frmdatos.cmb_NpDistrictZone.disabled =false;
      }
      //FIN: PRY-1049
  }
  //LGONZALES Solo se ejecutara al cargar popup items, para la edicion de un item
  function validateConfigBafi2300ForLoad(){
      try{

          <%
              //INICIO: AMENDEZ | PRY-1049
              if(paramNpobjitemheaderid!=null && paramNpobjitemheaderid.length>0) {
                for(int i=0;i<paramNpobjitemheaderid.length; i++){
                  if( paramNpobjitemheaderid[i].equals("93") ) {
                      strSolution = MiUtil.getString(paramNpobjitemvalue[i]);
                  }

                  if( paramNpobjitemheaderid[i].equals("51") ) {
                      strProductLine = paramNpobjitemvalue[i];
                  }


                  if( paramNpobjitemheaderid[i].equals("1") ) {
                    strModality = paramNpobjitemvalue[i];
                  }


                }
              }
              //FIN: AMENDEZ | PRY-1049
            %>

          var url_server = "<%=request.getContextPath()%>/editordersevlet";
          var varValidateConfigBafi2300=0;

          var params = 'myaction=validateConfigBafi2300&av_npmodality='+'<%=strModality%>'+'&av_npsolutionid='+'<%=strSolution%>'+'&av_npproductlineid='+'<%=strProductLine%>';

          $.ajax({
              url: url_server,
              data: params,
              async: false,
              type: "POST",
              success:function(data){
                  varValidateConfigBafi2300=data;
  }
          });

          return varValidateConfigBafi2300;
      }catch (e){
          alert("Hubo un error en la validacion de BAFI 2300");
          return varValidateConfigBafi2300;
      }
  }

 </script>
    
<%}%>
 
 
<select name="<%=nameHtmlItem%>"  onchange="fxExecuteMethod(this)"> 
   <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option> 
</select>