<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "",
            strSessionId = "";            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision             =   (String)hshtinputNewSection.get("strDivision");
    strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
   int idSpecificationId = MiUtil.parseInt(hdnSpecification); //sar 0037-162284
  
  String strMessage = "";
  String strErrorLocal = "";
  
  HashMap objHashMap = objNewOrderService.getModelListByCategory(idSpecificationId); //sar 0037-162284
  
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayModel");
  
  ProductBean objProductBean = null;
  
%>
    <select name="<%=nameHtmlItem%>" onChange="javascript:fxChangeModel(this.value)"  > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <% 
            if ( objArrayList != null && objArrayList.size() > 0 ){
            
               for( int i=0; i<objArrayList.size();i++ ){ 
                    objProductBean = new ProductBean();
                    objProductBean = (ProductBean)objArrayList.get(i);
          %>
          <!-- jsalazar Compatibilidad Modelo Plan Servicio inicio 22-11-2010-->
          <option value='<%=objProductBean.getNpproductid()%>'>
                         <%=objProductBean.getNpname()%>
          </option>
          <!--jsalazar fin   -->
          <%
              
                }
            }
          %>
                         
     </select>
     
<script>
  parent.mainFrame.frmdatos.hdnHtmlItemCM.value = '<%=nameHtmlItem%>'; 
  
  
function fxChangeModel(objValue){//EZM Método onchange del combo
   var form = parent.mainFrame.frmdatos;
   var vDoc = parent.mainFrame;

   <% int specification = hdnSpecification!=null && !"".equals(hdnSpecification)?Integer.parseInt(hdnSpecification):0;
     //out.println("alert('"+specification+"EZM');");
        if (specification==Constante.SPEC_TRANSFERENCIA || specification==Constante.SPEC_TRANSFERENCIA_SUB_REG
         || specification==Constante.SPEC_CAMBIAR_PLAN_TARIFARIO
            //||specification==Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS //EZUBIAURR Compatibilidad M-P-S
       ) { %>
       
          fxGetPlansDefault(objValue);
   
      <% }else if (specification==Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS || specification==Constante.SPEC_SSAA_SUSCRIPCIONES || specification==Constante.SPEC_SSAA_PROMOTIONS){%>
        fxShowService(objValue);
      <%} %>   
    }
   
//EZUBIAURR Obtiene los planes a partir del modelo seleccionado
function fxGetPlansDefault(objValue){
    
    var form = parent.mainFrame.frmdatos;
    var vDoc = parent.mainFrame;
    var vmodalityid = 0;
    //var vplanid = 0;
    var vmodel = 0;
    var solution = 0;
    var vserviceid = '';
    try{
      vmodalityid = form.cmb_ItemModality.value;
    }catch(e){}
    try{
      vmodel = form.hdnOriginalProductId.value;      
    }catch(e){}
       
    var url = "<%=request.getContextPath()%>/itemServlet?strModality="+vmodalityid+"&strProductId="+objValue+"&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&hdnMethod=getProductPlanList";
    //alert(url);    
    parent.bottomFrame.location.replace(url);
    }
    
 function fxShowService(objValue){
 
    if (trim(objValue)!=""){

      var serviceAditional = "";
      var hdnSpecification = <%=hdnSpecification%>;
      var  valorSolution='';
      var vmodalityid = 0;
      var vplanid = 0;
      var vmodel = '';
      try{
        serviceAditional = GetSelectedServices();
        valorSolution    =parent.mainFrame.frmdatos.cmb_ItemSolution.value;
        
         try{
         vmodalityid = form.cmb_ItemModality.value;

        }catch(e){}
        try{// EZUBIAURR HPPT+ Validación del productid según categoría

              vplanid = parent.mainFrame.frmdatos.txt_ItemNewPlantarifarioId.value;

        }catch(e){}
        try{
          vmodel = form.txt_ItemModel.value;
       }catch(e){}
        
      }catch(e){ valorSolution='';}

        var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&cmb_ItemPlanTarifario="+vplanid+"&hdnSpecification="+hdnSpecification+"&serviceAditional="+serviceAditional+"&strSessionId=<%=strSessionId%>&cmb_ItemSolution="+valorSolution+"&strobjectItem=<%=nameHtmlItem%>"+"&strPlanId="+vplanid+"&strModality="+vmodalityid+"&strProductId="+objValue+"&strModelId="+vmodel;
        parent.bottomFrame.location.replace(url);
      }
    }
    

  
  function fxLoadRepositionValues(){
    try{
      var form = parent.mainFrame.frmdatos;    
      var auxRealModel = form.hdnRealModel.value;
      if (auxRealModel !=""){
        var auxRealImei = form.hdnRealImei.value;
        var auxRealSim = form.hdnRealSim.value;
        //-- jsalazar Compatibilidad Modelo Plan Servicio inicio 22-11-2010
        var auxIndiceModel = form.txt_ItemModel.selectedIndex
        var auxItemModel = form.txt_ItemModel.options[auxIndiceModel].text; 
        form.hdnRealModel.value = auxItemModel;
        //jsalazarM fin
        form.hdnRealImei.value = form.txt_ItemIMEI.value;
        form.hdnRealSim.value = form.txt_ItemSIM.value;
    
        fxSetModelGeneric(auxRealModel);
        form.txt_ItemIMEI.value = auxRealImei;
        form.txt_ItemSIM.value = auxRealSim;
        
      }
    }catch(e){}
    
  }
  
  function fxSetReplaceEquipment(strRealModel, strRealModelId, strRealSim, strRealImei, strRealProductLine){
    
    try{
      if (strRealModel != ""){
        var form = parent.mainFrame.frmdatos;
        var tr = document.getElementById("idDisplay15");
        var td = tr.getElementsByTagName("td");
        form.hdnRealModel.value = strRealModel;
        form.hdnRealModelId.value = strRealModelId;
        form.hdnRealSim.value = strRealSim;
        form.hdnRealImei.value = strRealImei;
        form.hdnRealProductLine.value = strRealProductLine;
        
        var newElement = document.createElement("<input type='checkbox' id='chkReposition' name='chkReposition' onclick='fxLoadRepositionValues()'>");      
        var newElement2 = document.createElement("<span id='spanReposition'></span>");     
        
        try{ document.all.chkReposition.removeNode(true); }catch(e){}
        try{ document.all.spanReposition.removeNode(true); }catch(e){}
        
        td[1].insertAdjacentElement('beforeEnd', newElement);            
        td[1].insertAdjacentElement('beforeEnd', newElement2);
        
        var span = document.getElementById("spanReposition");
        span.innerHTML = "Repo c/"+strRealModel;
      }
                          
    }catch(e){}
    
  }
  
  function fxSetModelGeneric(objModelOriginal){
    var form      = document.frmdatos;
    var flgSearch = false;        
    var auxIndiceModel=0;
    try{
    if( objModelOriginal != null && objModelOriginal != "null" && objModelOriginal != "" ){
      for(i=0; i<form.txt_ItemModel.length; i++){
      // jsalazar Compatibilidad Modelo Plan Servicio inicio 22-11-2010 inicio
        if( form.txt_ItemModel.options[i].text == objModelOriginal ){
           form.txt_ItemModel.selectedIndex = i; //jsalazarM cmb
          flgSearch = true;
          break;
        }
      //jsalazar fin  
      }
    }else{
      flgSearch = false;
    }
     //jsalazar Compatibilidad Modelo Plan Servicio inicio 22-11-2010 inicio
      auxIndiceModel = form.txt_ItemModel.selectedIndex
      var auxItemModel = form.txt_ItemModel.options[auxIndiceModel].text;
    //jsalazar fin
      if( flgSearch ) {
        form.txt_ItemModel.disabled = true;
        }
      else{
        form.txt_ItemModel.disabled = false;
      }
      //-- jsalazar Compatibilidad Modelo Plan Servicio inicio 22-11-2010 inicio     
      SetCmbDefaultText(form.cmb_ItemProducto, auxItemModel);
      //jsalazar fin
    
    }catch(e){}
  }
  
//Metodo para obtener los servicios adicionales para un Cambio de Modelo  
function fxGetServicesDefaultCM(objValue){
   var form = document.frmdatos;

   hdnSpecification=<%=hdnSpecification%>;
      
   if (hdnSpecification==2001 || hdnSpecification==2009){
      try{
        vDoc.fxClearObjects("cmb_ItemPlanTarifario","SELECT");
        }catch(e){}
       //Se carga el servicio x defecto en base al producto
       //---------------------------------------------------
      var form = parent.mainFrame.frmdatos;
      var vDoc = parent.mainFrame;
      var vmodalityid = 0;
      var vplanid = 0;
      var vmodel = 0;
      var vplanoldid = 0;
      var vserviceid = '';
      try{
        vmodalityid = form.txt_ItemEquipment.value;
      }catch(e){}
      try{
        vplanid = form.cmb_ItemPlanTarifario.value;
      }catch(e){}
      try{
        vmodel = form.hdnOriginalProductId.value;
      }catch(e){}
      try{
        vplanoldid = form.txt_ItemNewPlantarifarioId.value;
      }catch(e){}
         
      if (vplanid==0){
          vplanid=vplanoldid;
      }
      if(vmodalityid == "Alquiler"){
         form.hdnOriginalProductIdCM.value = objValue;
         form.hdnHtmlItemCM.value = '<%=nameHtmlItem%>';
         form.hdnServArrCM.value = "S";
      }
      else{
         form.hdnServArrCM.value = "";
      }
      
      var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getProductServiceDefaultList&strSessionId=<%=strSessionId%>&strSpecificationId=<%=hdnSpecification%>"+"&strobjectItem=<%=nameHtmlItem%>"+"&strProductId="+objValue+"&strModality="+vmodalityid+"&strPlanId="+vplanid+"&strModelId="+vmodel;      
      parent.bottomFrame.location.replace(url);     
   }
}  
</script>