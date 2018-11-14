<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.ArrayList" %>
<%@page import="pe.com.nextel.bean.ProductBean" %>
<%@page import="pe.com.nextel.bean.ProductPriceBean" %>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.*"%>

<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script> 

<%

  NewOrderService      objNewOrderService  = new NewOrderService();
  ProductBean          objProductBean      = new ProductBean();
  ProductPriceBean     objProductPriceBean      = new ProductPriceBean();
  ArrayList            objArrayList        = new ArrayList();
  Hashtable            hshtinputNewSection = null;

  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strSessionId = "",
            strSalesStuctOrigenId = "",
            strflagvep = "",
            strnpnumcuotas = "",
            strOrderId = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite")==null?"":(String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
    strSalesStuctOrigenId   =   (String)hshtinputNewSection.get("strSalesStuctOrigenId");
    strflagvep              =   (String)hshtinputNewSection.get("strflagvep");
    strnpnumcuotas          =   (String)hshtinputNewSection.get("strnpnumcuotas");
    strOrderId              =   (String)hshtinputNewSection.get("strOrderId");
  }
  
  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<Combo Promocion>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+strSalesStuctOrigenId);
  
%>
<script>
   var valorOldProductNew  = "";
   var valorOldModalityNew = "";
   var valorOldPlanNew     = "";
   var valorOldQuantity    = "";
   var valorOldReturn      = "";
   var strnpSite           = "<%=strnpSite%>";
   
   function fxLoadPromotionEvaluate(){

        /*INICIO ADT-BLC-083 --LHUAPAYA*/
        if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_UPGRADE%>"  || "<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_DOWNGRADE%>"){
            var valorNewProductNew  = parent.mainFrame.frmdatos.cmb_ItemProductoDestino.value;
        }else {/*FIN ADT-BCL-083 -- LHUAPAYA*/
     var valorNewProductNew  = parent.mainFrame.frmdatos.cmb_ItemProducto.value;
        }
     var valorNewModalityNew = parent.mainFrame.frmdatos.cmb_ItemModality.value;
     var valorNewQuantity    = "1";
     var  valorSolution='';
     var strflagvep = "<%=strflagvep%>"; 
     var strnpnumcuotas = "<%=strnpnumcuotas%>";
     
     try{
        valorSolution        =parent.mainFrame.frmdatos.cmb_ItemSolution.value;
     }catch(e){
        valorSolution='';
     }
     
     //Acuerdos Comerciales
     var phoneNumber ='';
     try{
        phoneNumber         = parent.mainFrame.frmdatos.txt_ItemPhone.value;
     }catch(e){
        phoneNumber='';
     }
     
     try{
     valorNewQuantity        = parent.mainFrame.frmdatos.txt_ItemQuantity.value;
     }catch(e){}
     
     //Plan Tarifario
     var valorNewPlanNew     = '';
     try{
        valorNewPlanNew      = parent.mainFrame.frmdatos.cmb_ItemPlanTarifario.value;
     }catch(e){
     
        try{    
           valorNewPlanNew      = parent.mainFrame.frmdatos.txt_ItemNewPlantarifarioId.value;
        }catch(e){
           valorNewPlanNew = ''; 
      }
         
     }
     /***************/
     //Producto Original
     var valorOriginalProductId = '';
        try{ /*INICIO ADT-BLC-083 --LHUAPAYA*/
            if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_UPGRADE%>"  || "<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_DOWNGRADE%>")valorOriginalProductId = '';
            /*FIN ADT-BCL-083 -- LHUAPAYA*/
            else valorOriginalProductId      = parent.mainFrame.frmdatos.hdnOriginalProductId.value;
     }catch(e){
        valorOriginalProductId      = '';
     }
     /*******************/
     //Ocurrencia
     var valorOcurence       = '';
     try{
        valorOcurence        = parent.mainFrame.frmdatos.txt_ItemNroOcurrence.value;
     }catch(e){
        valorOcurence        = '';
     }
     
     //Modalidad Original
     var valorOldEquipment     = '';
     var valorOriginalModality = '';
     try{
        valorOldEquipment    = parent.mainFrame.frmdatos.txt_ItemEquipment.value;
        if( valorOldEquipment == 'Cliente' ) valorOldEquipment = 'Venta';
        valorOriginalModality  = valorOldEquipment;
     }catch(e){
        valorOriginalModality  = '';
     }
     
     //Modelo Original
     var valorModel = '';
     try{
        valorModel    = parent.mainFrame.frmdatos.txt_ItemModel.value;
     }catch(e){
        valorModel  = '';
     }
     
     
     //Garantía
     var valorWarrant        = '';
     try{
        //alert("Antes")
        valorWarrant        = parent.mainFrame.frmdatos.txt_ItemFlgProductGN.value;
        //alert("Luego : " + valorWarrant)
        if( valorWarrant == 'N' ) valorWarrant = '0';
        if( valorWarrant == 'S' ) valorWarrant = '1';
        //alert("Luego : " + valorWarrant)
     }catch(e){
        //alert("Catch")
        valorWarrant        = '0';
     }
     
     //Devolución
     var valorDevolucion     = '';
     try{
        valorDevolucion      = parent.mainFrame.frmdatos.cmb_ItemDevolution.value;
        if( valorDevolucion == 'N' || valorDevolucion == '' ) valorDevolucion = '0';
        if( valorDevolucion == 'S' ) valorDevolucion = '1';
     }catch(e){
        valorDevolucion        = '0';
     }
     
     //Flag Devolución
     var flagDevolucion     = '';
     try{
        flagDevolucion      = parent.mainFrame.frmdatos.hdnFlagReturn.value;
       // alert("flagDevolucion : " + flagDevolucion);
     }catch(e){
        flagDevolucion      = '0';
      //  alert("flagDevolucion : " + flagDevolucion);
     }
     
     //hdnFlagReturn
     
     //Evaluar los Campos
     
     if( valorNewProductNew   != valorOldProductNew ||
         valorNewModalityNew  != valorOldModalityNew ||
         valorNewPlanNew      != valorOldPlanNew ||
         valorNewQuantity     != valorOldQuantity ||
         //Ahora también la devolución
         flagDevolucion       == '1'
         ){
  
         var url = "<%=request.getContextPath()%>/itemServlet?customerId=<%=strCodigoCliente%>&productId="+valorNewProductNew;
         url+="&productoldId="+valorOldProductNew+"&modalityId="+valorNewModalityNew;
         url+= "&quantityId="+valorNewQuantity+"&specificationId="+<%=hdnSpecification%>+"&solutionId="+valorSolution;
 
    
         url+="&salesstructorigenId="+<%=strSalesStuctOrigenId%>+"";
     
         //Modalidad Original
         if( valorOriginalModality != '' )
         url+= "&modalityoldId="+valorOriginalModality;
         //Plan Tarifario Nuevo
         if( valorNewPlanNew != '' )
         url+= "&planId="+valorNewPlanNew;
         //Garantía
         if( valorWarrant != '' )
         url+= "&warrant="+valorWarrant;
         //Ocurrencia
         if( valorOcurence != '' )
         url+= "&ocurrence="+valorOcurence;
         //Devolución
         if( valorDevolucion != '' )
         url+= "&devolut="+valorDevolucion;
         //Producto Original
         if( valorOriginalProductId != '' )
         url+= "&origProductId="+valorOriginalProductId;
         //Modelo Original
         if( valorModel != '' )
         url+= "&paramModel="+valorModel;         
         //Teléfono
         if( phoneNumber != '' )
         url+= "&phoneNumber="+phoneNumber;
         //Site Id
         if( strnpSite != '' )
         url+= "&siteId="+strnpSite;
         //Acción del execute
         url+= "&hdnMethod=getPromotionList";
          //Flag VEP
          if(strflagvep != '' )
          url+= "&strflagvep="+strflagvep;
          //Flag Numero de cuotas
          if(strnpnumcuotas != '' )
      		url+= "&strnpnumcuotas="+strnpnumcuotas;
         //fin nbravo VEP

         //INICIO: PRY-1200 | AMENDEZ
         url+="&orderId="+<%=strOrderId%>+"";
         //FIN: PRY-1200 | AMENDEZ

     parent.bottomFrame.location.replace(url);
    
        } /*INICIO ADT-BLC-083 --LHUAPAYA*/
        if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_UPGRADE%>"  || "<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_DOWNGRADE%>"){
            valorOldProductNew  = parent.mainFrame.frmdatos.cmb_ItemProductoDestino.value;
        }else {/*FIN ADT-BLC-083 --LHUAPAYA*/
            valorOldProductNew  = parent.mainFrame.frmdatos.cmb_ItemProducto.value;
     }
     valorOldModalityNew = parent.mainFrame.frmdatos.cmb_ItemModality.value;
     valorOldPlanNew     = '';
     
     try{     
        valorOldPlanNew     = parent.mainFrame.frmdatos.cmb_ItemPlanTarifario.value;          
     }catch(e){     
        try{       
           valorOldPlanNew      = parent.mainFrame.frmdatos.txt_ItemNewPlantarifarioId.value;
        }catch(e){}     
     }                       
     
     valorOldQuantity    = "1";          
     try{
     valorOldQuantity    = parent.mainFrame.frmdatos.txt_ItemQuantity.value;
     }catch(e){}
     try{
     parent.mainFrame.frmdatos.hdnFlagReturn.value  = '0';
     }catch(e){}
     
     
   }
   
   
   
   function fxDelegateActionsPromotion(objType){
    var myString = objType.value;
    var tokens = myString.tokenize("|", "|", true);
    
    if( tokens.length > 0 ){
        //PromotionId
        /*fxObjectConvert('txt_ItemPromotionId',tokens[0]);
        //Precio      
        fxObjectConvert('txt_ItemPriceCtaInscrip',round_decimals(tokens[1],2)); 
        //Renta
        fxObjectConvert('txt_ItemEquipmentRent',tokens[2]==0?"0.0":tokens[2]);
        //Tipo Moneda
        fxObjectConvert('txt_ItemMoneda',tokens[3]==undefined?"":tokens[3]);

        fxGetListAdendas(tokens[0]);
        */
        
        //PromotionId
        fxObjectConvert('txt_ItemMoneda',tokens[0]==undefined?"":tokens[0]);
        //Precio      
        fxObjectConvert('txt_ItemPriceCtaInscrip',round_decimals(tokens[1],2)); 
        //Renta
        fxObjectConvert('txt_ItemEquipmentRent',tokens[2]==0?"0.0":tokens[2]);
        //Tipo Precio
        fxObjectConvert('txt_ItemPriceType',tokens[3]==undefined?"":tokens[3]);
        //Codigo Tipo Precio
        fxObjectConvert('txt_ItemPriceTypeId',tokens[4]==undefined?"":tokens[4]);
        //Precio Original      
        fxObjectConvert('txt_ItemOriginalPrice',round_decimals(tokens[5],2)); 
        //Codigo del Item del Tipo de Precio
        fxObjectConvert('txt_ItemPriceTypeItemId',tokens[6]); 
        //Codigo del Item Cobro para suspensiones Temporales 
        fxObjectConvert('txt_ItemCobro',tokens[1]);
        //Total del Item
        fxObjectConvert('txt_ItemTotal',tokens[1]);
        
        var specificationId = '<%=hdnSpecification%>' ;
        var valorNewModalityNew = parent.mainFrame.frmdatos.cmb_ItemModality.value;
        
        if((specificationId == '2001' || specificationId == '2009' || specificationId == '2065' || specificationId == '2068') && valorNewModalityNew == 'Venta'){
          <%if(strnpnumcuotas !=  null) {
              if(!strnpnumcuotas.equals("")) {%>
                  var precioCoutaVep = 0.00;
                  var numCoutas = <%=strnpnumcuotas%> ;
                  if(numCoutas>0) {
                    precioCoutaVep =   round_decimals(round_decimals(tokens[1],2)/numCoutas,2);
                    fxObjectConvert('txt_ItemVentaVEP','<%=strflagvep%>');
                    fxObjectConvert('txt_ItemPriceVentaVEP',precioCoutaVep);
                  }
            <%}else{%>
                   fxObjectConvert('txt_ItemVentaVEP','0');
                   fxObjectConvert('txt_ItemPriceVentaVEP',round_decimals(0,2));
            <%}
          }else{%>
                 fxObjectConvert('txt_ItemVentaVEP','0');
                 fxObjectConvert('txt_ItemPriceVentaVEP',round_decimals(0,2));
        <%}%>
            fxObjectConvert('txt_ItemTotalVEP',tokens[1]);

        }
        
        
        if(tokens[3]=="PROMOCION"){
        //PromotionId
          fxObjectConvert('txt_ItemPromotionId',tokens[4]);
          fxGetListAdendas(tokens[4]);
        }else{
          //PromotionId
          fxObjectConvert('txt_ItemPromotionId','');
          fxGetListAdendas(tokens[4]);
        }     
    
    }
    
   }
   

   function fxGetListAdendas(valueObj){
      v_doc  = parent.mainFrame;
      form   = document.frmdatos;
      var plan_aux;
      if (v_doc.form.hdn_ListAdenda == null){
        return false;
      }
      try{
          plan_aux = v_doc.form.cmb_ItemPlanTarifario.options[v_doc.form.cmb_ItemPlanTarifario.selectedIndex].value;
        }catch(e){
          plan_aux = 0;
        }
       try{
          if(plan_aux == 0) {      
            plan_aux = v_doc.form.txt_ItemNewPlantarifarioId.value;
          }
        }catch(e){
          plan_aux = 0;
        }          
      var promocion = valueObj; 
     // alert(promocion);
      if(promocion == null)promocion = 0;
      var plan = plan_aux*10/10;
      if ( (plan!="") || (promocion != "") ){
         deleteTable();
         var hdnSpecification = <%=hdnSpecification%>;
         var action = "newItem";
         var url = "<%=request.getContextPath()%>/itemServlet?cmbPromocion="+promocion+"&hdnSpecification="+hdnSpecification+"&cmbPlan="+plan+"&hdnMethod=doListarPlantillasNew&strSessionId=<%=strSessionId%>";
         parent.bottomFrame.location.replace(url);
      }    
   }

   function deleteTable(){
      var table = document.getElementById("itemsTemplate");
      if ( table != null ){
        var length = table.rows.length;
        if (length > 1){
          for(i = 1; i < length; i++){
             table.deleteRow(table.rows.length-1);      
          }
        }
      }
   }
</script>

<%
String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
System.out.println("TRAEMOS LOS VALORES DEL POPUP AL INCIO:"+nameHtmlItem);
if ( nameHtmlItem==null ) nameHtmlItem = "";

System.out.println("TRAEMOS LOS VALORES DEL POPUP AL INCIO");
//System.out.println(objProductBean.getNpproductid());
System.out.println("tamaño=="+objArrayList.size());
%>

<select name="<%=nameHtmlItem%>" onClick="javascript:fxLoadPromotionEvaluate();" onchange="javascript:fxDelegateActionsPromotion(this); " >
<!--<option value="118">Opción 1</option>-->
<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
<% 
   if ( objArrayList != null && objArrayList.size() > 0 ){
      for( int i=0; i<objArrayList.size();i++ ){ 
           /*objProductBean = new ProductBean();
           objProductBean = (ProductBean)objArrayList.get(i);*/
           objProductPriceBean = new ProductPriceBean();
           objProductPriceBean = (ProductPriceBean)objArrayList.get(i);

           //System.out.println("objProductBean.getNpcurrency() : " + objProductPriceBean.getNpcurrency());
%>

   <option value="|<%=objProductPriceBean.getNpcurrency()%>|<%=objProductPriceBean.getNppriceonetime()%>|<%=objProductPriceBean.getNppricerecurring()%>|<%=MiUtil.getString(objProductPriceBean.getNpcurrency())%>">
      <%=MiUtil.getString(objProductPriceBean.getNpcurrency())%> - 
      <%=MiUtil.getString(""+objProductPriceBean.getNppriceonetime())%> -
      <%=MiUtil.getString(objProductPriceBean.getNpcurrency())%>
   </option>

<%    }
   }%>
   
</select>

<input type="hidden" name="hdnFlagReturn" >

<%
if( !type_window.equals("NEW") ){
  String[] paramNpobjitemheaderid   = request.getParameterValues("a");
  String[] paramNpobjitemvalue      = request.getParameterValues("b");
  
  String strPromotionId = "0"; 
  String strPrice       = "0";
  String strRent        = "0.0";
  String strCurrency    = "";
  String strProductId   = "";
  String strModalidad   = "";
  String strPlanId      = "";
  String strQuantity    = "";
  String strModel       = null;
  String strEquipment   = null;
  String strOcurrence   = null;
  String strDevolucion  = null;
  String strWarrant     = null;
  //Acuerdos Comerciales
  String strOrigPrice   = "0";
  String strPriceTypeId = "0";
  String strPriceTypeItemId = "0";
  String strPriceType   = null;
  String strPhoneNumber = null;
  String strSolution    = "";
  
  for(int i=0;i<paramNpobjitemheaderid.length; i++){
  
    //Obtengo el valor de la Solución que tiene como HeaderID -> 93
    if( paramNpobjitemheaderid[i].equals("93") )
        strSolution = paramNpobjitemvalue[i];
        
    //Obtengo el valor de la Modalidad que tiene como HeaderID -> 1
    if( paramNpobjitemheaderid[i].equals("1") )
        strModalidad = paramNpobjitemvalue[i];
    
    //Obtengo el valor del ProductId que tiene como HeaderID -> 9
    if( paramNpobjitemheaderid[i].equals("9") )
        strProductId = paramNpobjitemvalue[i];
        
    //Obtengo el valor del Cantidad que tiene como HeaderID -> 21
    if( paramNpobjitemheaderid[i].equals("21") )
        strQuantity  = paramNpobjitemvalue[i];
    
    //Obtengo el valor del PlanId que tiene como HeaderID -> 10
    if( paramNpobjitemheaderid[i].equals("10") )
        strPlanId  = paramNpobjitemvalue[i];
        
    //Obtengo el valor del Precio Cta Inscripción que tiene como HeaderID -> 18  <--Se agrego condicion para HeaderID ->102 Cobro para Susp Temporales RMARTINEZ2_25-09-2009-->
    if( paramNpobjitemheaderid[i].equals("18") ||  paramNpobjitemheaderid[i].equals("102"))
        strPrice  = paramNpobjitemvalue[i];
        
    //Obtengo el valor del Renta que tiene como HeaderID -> 20
    if( paramNpobjitemheaderid[i].equals("20") )
        strRent  = paramNpobjitemvalue[i];    
    
    //Obtengo el valor del Moneda que tiene como HeaderID -> 54
    if( paramNpobjitemheaderid[i].equals("54") )
        strCurrency  = paramNpobjitemvalue[i];
        
    //Obtengo el valor de la Promoción que tiene como HeaderID -> 55
    if( paramNpobjitemheaderid[i].equals("55") )
        strPromotionId  = paramNpobjitemvalue[i];
    
    //Obtengo el valor del Modelo Original -> 15
    if( paramNpobjitemheaderid[i].equals("15") )
        strModel  = paramNpobjitemvalue[i];
    
    //Obtengo el valor del Equipo -> 11
    if( paramNpobjitemheaderid[i].equals("11") )
        strEquipment  = paramNpobjitemvalue[i];
    
    //Obtengo el valor de la Ocurrencia -> 53
    if( paramNpobjitemheaderid[i].equals("53") )
        strOcurrence  = paramNpobjitemvalue[i];
    
    //Obtengo el valor de la Devolución -> 12
    if( paramNpobjitemheaderid[i].equals("12") )
        strDevolucion  = paramNpobjitemvalue[i];
    
    //Obtengo el valor de la Garantia -> 52
    if( paramNpobjitemheaderid[i].equals("52") )
        strWarrant  = paramNpobjitemvalue[i];
        
    //Para Acuerdos Comerciales
    //Obtengo el valor del Precio Original -> 89
    if( paramNpobjitemheaderid[i].equals("89") )
        strOrigPrice  = paramNpobjitemvalue[i];
    
    //Obtengo el valor del Tipo de Precio -> 90
    if( paramNpobjitemheaderid[i].equals("90") )
        strPriceType  = paramNpobjitemvalue[i];
        
    //Obtengo el valor del Codigo del Tipo de Precio -> 91
    if( paramNpobjitemheaderid[i].equals("91") )
        strPriceTypeId  = paramNpobjitemvalue[i];
    
    //Obtengo el valor del Codigo del Item del Tipo de Precio -> 92
    if( paramNpobjitemheaderid[i].equals("92") )
        strPriceTypeItemId  = paramNpobjitemvalue[i];
        
    //Obtengo el telefono -> 91
    if( paramNpobjitemheaderid[i].equals("3") )
        strPhoneNumber  = paramNpobjitemvalue[i];
    
            //Obtengo el productid del destino para bolsa de celulares
            if(MiUtil.parseLong(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE || MiUtil.parseLong(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE){
                if( paramNpobjitemheaderid[i].equals("138") ) {
                    strProductId = paramNpobjitemvalue[i];
  }
            }
        }
  %> 
    <script>
    function fxLoadEditPromotion(){
    
      <%      
       java.text.NumberFormat formatter = new java.text.DecimalFormat("#0.00");       
             
       strPriceType = MiUtil.getString(strPriceType);
       strPriceTypeId = MiUtil.getString(strPriceTypeId);
       strPhoneNumber = MiUtil.getString(strPhoneNumber);
       strPriceTypeItemId = MiUtil.getString(strPriceTypeItemId);
       strCurrency = MiUtil.getString(strCurrency);
       strSolution = MiUtil.getString(strSolution);
       strPromotionId = MiUtil.getString(strPromotionId);
       
       if( strOrigPrice.equals("") ) strOrigPrice = "0.0";
       if ( strPriceTypeId.equals("") ) strPriceTypeId = "0";
       if ( strPriceTypeItemId.equals("") ) strPriceTypeItemId = "0";
       if( strRent.equals("") ) strRent = "0.0";
       if( strPromotionId.equals("") ) strPromotionId = "0";
       if( strPrice.equals("") ) strPrice = "0";
       
       if(strDevolucion!=null){
         if(strDevolucion.equals("S"))
           strDevolucion = "1";
         else
           strDevolucion = "0";
       }else{
         strDevolucion = "0";
       }
       
       if(strWarrant!=null){
         if(strWarrant.equals("S"))
           strWarrant = "1";
         else
           strWarrant = "0";
       }else{
         strWarrant = "0";
       }
       
       if(strEquipment!=null){
           if(strEquipment.equals("Cliente") )
              strEquipment = "Venta";
       }
       
       //NewOrderService objNewOrderService = new NewOrderService();
       //ProductBean objProductBean = new ProductBean();
       //ProductPriceBean objProductPriceBean = new ProductPriceBean();
       objProductBean.setNpcustomerid(MiUtil.parseLong(strCodigoCliente));
       objProductBean.setNpproductid_old(MiUtil.parseLong(null));      
       objProductBean.setNpproductid_new(MiUtil.parseLong(strProductId));
       objProductBean.setNpmodality_new(strModalidad);
       objProductBean.setNpmodality_old(strEquipment);
       objProductBean.setNpcategoryid(MiUtil.parseLong(hdnSpecification));
       objProductBean.setNpquantity(strQuantity);
       objProductBean.setNpsolutionid(MiUtil.parseLong(strSolution));
       objProductBean.setNpmodel(strModel);
       objProductBean.setSalesStructureOriginalId(MiUtil.parseLong(strSalesStuctOrigenId));
       
       
       if( strPlanId != null )
        objProductBean.setNpplanid(MiUtil.parseLong(strPlanId));
       
       if( strOcurrence != null )
        objProductBean.setNpoccurrence(MiUtil.parseInt(strOcurrence));
       else
        objProductBean.setNpoccurrence(-1);
       
       objProductBean.setNpflg_garanty(strWarrant);
       objProductBean.setNpflg_return(strDevolucion);
       //se envia el siteId
       objProductBean.setNpsiteid(MiUtil.parseLong(strnpSite));
       objProductBean.setNpphonenumber(strPhoneNumber);
        
       //se envia el flag VEP y el numero de cuotas
       objProductBean.setNpflagvep (MiUtil.parseInt(strflagvep));
       objProductBean.setNpnumcuotas(MiUtil.parseInt(strnpnumcuotas));
        
       HashMap objHashMap = objNewOrderService.OrderDAOgetProductPriceType(objProductBean);
       //ArrayList objArrayList = new ArrayList();
     
       if( (String)objHashMap.get("strMessage")!= null ){%>
         parent.mainFrame.deleteAllValues(parent.mainFrame.frmdatos.cmb_ItemPromocion);
         parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemPromocion,'','');
       <%
       }else{%>
         parent.mainFrame.deleteAllValues(parent.mainFrame.frmdatos.cmb_ItemPromocion);
         parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemPromocion,'','');
         <% objArrayList = (ArrayList)objHashMap.get("objArrayList");
         if ( objArrayList != null && objArrayList.size() > 0 ){%>
         
         formCurrent = parent.mainFrame.frmdatos;
        
         <%
         String value = null;
         String desc =null;
         
         if (!type_window.equals("DETAIL")){
            for( int i=0; i<objArrayList.size();i++ ){                
               objProductPriceBean = new ProductPriceBean();
               objProductPriceBean =(ProductPriceBean)objArrayList.get(i);
               value = "|"+ objProductPriceBean.getNpcurrency() + "|" + formatter.format(objProductPriceBean.getNppriceonetime()) + "|" + objProductPriceBean.getNppricerecurring() + "|" + objProductPriceBean.getNpobjecttype()+ "|" +objProductPriceBean.getNpobjectid()+ "|" +formatter.format(objProductPriceBean.getNporiginalprice())+ "|" +objProductPriceBean.getNpobjectitemid();
               desc = objProductPriceBean.getNpcurrency()+" - "+MiUtil.getString(""+formatter.format(objProductPriceBean.getNppriceonetime()))+" - "+MiUtil.getString(objProductPriceBean.getNpobjecttype())+" - "+objProductPriceBean.getNpobjectid();
            %>
               parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'<%=value %>','<%=desc %>');
            <%
            }
          }else{
            value = "|"+strCurrency+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strPrice)))+"|"+MiUtil.parseDouble(strRent)+"|"+strPriceType+"|"+strPriceTypeId+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strOrigPrice)))+"|"+strPriceTypeItemId;
            desc = strCurrency+" - "+strPrice+" - "+strPriceType+" - "+strPriceTypeId;
            %>
               parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'<%=value %>','<%=desc %>');
            <%
          }
          if( "S" != null ){
             value = "|"+strCurrency+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strPrice)))+"|"+MiUtil.parseDouble(strRent)+"|"+strPriceType+"|"+strPriceTypeId+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strOrigPrice)))+"|"+strPriceTypeItemId;
          %>  
            formCurrent.cmb_ItemPromocion.value = '<%=value %>';
          <%
          }         
         }
       }  
  
  %>          
                                                                  
         var url = "<%=request.getContextPath()%>/itemServlet?customerId=<%=strCodigoCliente%>&productId=<%=strProductId%>&modalityId=<%=strModalidad%>&salesstructorigenId=<%=strSalesStuctOrigenId%>";
             url+= "&planId=<%=strPlanId%>&quantityId=<%=strQuantity%>&specificationId=<%=hdnSpecification%>&solutionId=<%=strSolution%>&flgSearch=S&hdnMethod=getPromotionList";
             url+= "&price=<%=strPrice%>&currency=<%=strCurrency%>&renta=<%=strRent%>&promotionid=<%=strPromotionId%>&type_window=<%=type_window%>";
             
             <%if(strOrigPrice!=null){%>
             url+= "&origPrice=<%=strOrigPrice%>";
             <%}%>
            
             <%if(strPriceType!=null){%>
             url+= "&priceType=<%=strPriceType%>";
             <%}%>
             
             <%if(strPriceTypeId!=null){%>
             url+= "&priceTypeId=<%=strPriceTypeId%>";
             <%}%>
             
             <%if(strPriceTypeItemId!=null){%>
             url+= "&priceTypeItemId=<%=strPriceTypeItemId%>";
             <%}%>
             
             <%if(strnpSite!=null){%>
             url+= "&siteId=<%=strnpSite%>";
             <%}%>
             
             <%if(strPhoneNumber!=null){%>
             url+= "&phoneNumber=<%=strPhoneNumber%>";
             <%}%>
             
             <%if(strModel!=null){%>
             url+= "&paramModel=<%=strModel%>";
             <%}%>
             <%if(strEquipment!=null){
               if(strEquipment.equals("Cliente") )
                  strEquipment = "Venta";
             %>
             url+= "&modalityoldId=<%=strEquipment%>";
             <%}%>
             <%if(strOcurrence!=null){%>
             url+= "&ocurrence=<%=strOcurrence%>";
             <%}%>
             
             //Devolución
             <%if(strDevolucion!=null){
              if(strDevolucion.equals("S"))
                strDevolucion = "1";
              else
                strDevolucion = "0";
             %>
             url+= "&devolut=<%=strDevolucion%>";
             <%}else{%>
             url+= "&devolut=0";
             <%}%>
             
             //Garantía
             <%if(strWarrant!=null){
              if(strWarrant.equals("S") || strWarrant.equals("1")) //se agregó condición RAMN_05-10-2009
                strWarrant = "1";
              else
                strWarrant = "0";
             %>
             url+= "&warrant=<%=strWarrant%>";
             <%}else{%>
             url+= "&warrant=0";
             <%}%>
             //INICIO: PRY-1200 | AMENDEZ
             url+="&orderId="+<%=strOrderId%>+"";
             //FIN: PRY-1200 | AMENDEZ

      parent.bottomFrame.location.replace(url);
      fxPromotionSelected();
    }
     //MGARCIA INI  Selecciona el precio en cargado para el teléfono en edición.
    function fxPromotionSelected(){
     for(i=0; i <= parent.mainFrame.frmdatos.cmb_ItemPromocion.length; i++){
       var stringPrecio = parent.mainFrame.frmdatos.cmb_ItemPromocion.options[i].value;
       var sub=" ";
       var cantBarras = 0;
       var x = 0;
       while( (cantBarras < 3)&&( x < stringPrecio.length ) ){
          if (stringPrecio.substring(x,x+1) != "|" ){  
            if(cantBarras >= 2){			 
            sub= sub+stringPrecio.substring(x,x+1);
			      }
          }else{
            cantBarras++;	
          }
          x++;
        } 

        //[PRY-0710] Se comenta para que al momento de mostrar el precio promocion del producto al editar este se seleccione correctamente
        /*if( sub == " <%=objProductPriceBean.getNppriceonetime()%>" ){
             parent.mainFrame.frmdatos.cmb_ItemPromocion.options[i].selected = true;
             parent.mainFrame.frmdatos.cmb_ItemPromocion.selectedIndex = i;
        }*/
        }
      }
    //MGARCIA FIN 
    //Cambios de Evaluacion de Volumen de Orden
    //Para setear en el pop up el precio anterior de un item que haya sido grabado con un precio de volumen de orden
    function fxUpdatePriceVO(){
    
      var priceTypeOrig;
      var priceTypeIdOrig;
    
      var price;
      var priceType; //ORIGINAL - PROMOCION - AC
      var priceTypeId;//0 - Id de promocion
      var rent;
      
      var tokens;
    
      <%
         String valueVO = "|"+strCurrency+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strOrigPrice)))+"|"+MiUtil.parseDouble(strRent)+"|"+strPriceType+"|"+strPriceTypeId+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(strOrigPrice)))+"|"+strPriceTypeItemId;;
      %>
    
      formCurrent = parent.mainFrame.frmdatos;
      
      priceTypeOrig = '<%=strPriceType %>';
      priceTypeIdOrig = '<%=strPriceTypeId %>'; 
      
      //Si el precio que se habia elegido fue un AC, se seleccionará el precio original
      if(priceTypeOrig == 'AC'){
        priceTypeOrig = 'ORIGINAL'
      }
     
      if(formCurrent.cmb_ItemPromocion.selectedIndex == -1){
        
        for(i = 0; formCurrent.cmb_ItemPromocion.length; i++){
          
          tokens = formCurrent.cmb_ItemPromocion[i].value.tokenize("|", "|", true);
          if( tokens.length > 0 ){
            price = round_decimals(tokens[1],2);
            priceType = tokens[3]==undefined?"":tokens[3];
            priceTypeId = tokens[4]==undefined?"":tokens[4];
            rent = tokens[2]==0?"0.0":tokens[2];
           
            if(priceTypeOrig == priceType){
            
              if(priceType == 'ORIGINAL'){
                formCurrent.cmb_ItemPromocion.selectedIndex = i;
                break;
              }
              else if(priceType == 'PROMOCION' || priceType == 'PROMOTION'){
                if(priceTypeIdOrig == priceTypeId){
                  formCurrent.cmb_ItemPromocion.selectedIndex = i;
                  break;
                }              
              }            
            }            
          }        
        }
        
        formCurrent.txt_ItemPriceCtaInscrip.value = price;
        formCurrent.txt_ItemPromotionId.value = priceTypeId;
        formCurrent.txt_ItemEquipmentRent.value = rent;
      }    
    }
      
   </script>
<%  
 }
%>