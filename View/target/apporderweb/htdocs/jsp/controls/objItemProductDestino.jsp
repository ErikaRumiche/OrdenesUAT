<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.bean.*"%>

<%
    Hashtable hshtinputNewSection = null;
    String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
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

    String idSpecificationId = (String)request.getAttribute("idSpecificationId");

    NewOrderService objNewOrderService = new NewOrderService();
    GeneralService  objGeneralService  = new GeneralService();
    ArrayList objArrayList = new ArrayList();


    HashMap hshFlagControl = new HashMap();
    hshFlagControl = objGeneralService.getStatusByTable("FLAG_CTRL_EVENTO_CAMBIO_MODELO", "1");
    String flagControl = (String) hshFlagControl.get("strStatus");

    String type = request.getParameter("strTypeCompany");
    HashMap hshConfiguration = new HashMap();
    ArrayList arrConfiguration = new ArrayList();
    hshConfiguration = objGeneralService.GeneralDAOgetNpConfValues("ORDER_IMEI_CUSTOMER",null,null,"1",null,null,null);
    arrConfiguration = (ArrayList)hshConfiguration.get("objArrayList");

   /*Obtener npvalue del product line Equipo Móvil 3G HPPTT*///EZUBIAURR 17/01
    String strProdLine3GHPPTT = objGeneralService.getValue(Constante.EQUIPO_MOVIL_3G_HPPTT_NPTABLE, Constante.EQUIPO_MOVIL_3G_HPPTT_NPDESC);
    String strProdLineBucket = objGeneralService.getValue(Constante.PRODUCT_LINE_BUCKET_NPTABLE, Constante.PRODUCT_LINE_BUCKET_NPDESC);
    //*nbravo
    String strBolsaN2 = "";
    String strLevel2  = "";
    String strMinutesrate = "";
    String strPrecioxmin = "";
    Float fServiceCost;
    String strDescrBolsa = "";
    NewOrderService objNewOrderServicePopUp = new NewOrderService();
    HashMap hshBolsaN2 = objNewOrderServicePopUp.getBolsaCreacionN2(MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strnpSite));
    strBolsaN2 =  (String)(hshBolsaN2.get("strBolsaN2")==null?"":hshBolsaN2.get("strBolsaN2"));
    strLevel2  =  (String)(hshBolsaN2.get("strLevel2")==null?"":hshBolsaN2.get("strLevel2"));
    strMinutesrate  =  (String)(hshBolsaN2.get("strMinutesrate")==null?"":hshBolsaN2.get("strMinutesrate"));
    strPrecioxmin  =  (String)(hshBolsaN2.get("strPrecioxmin")==null?"":hshBolsaN2.get("strPrecioxmin"));
    strDescrBolsa = (String)(hshBolsaN2.get("strDescrBolsa")==null?"":hshBolsaN2.get("strDescrBolsa"));
    //*nbravo
%>
<script>
    var vctProduct = new Vector();

    function fxMakeProduct(objProductId,objProductPrice,objProductMintsPrice){
        this.objProductId         = objProductId;
        this.objProductPrice      = objProductPrice;
        this.objProductMintsPrice = objProductMintsPrice;
    }

    function fxChangeProductDestino(objValue){
        var formulario = document.frmdatos;
        var idProductOrigen = formulario.cmb_ItemProductBolsaId.value;
        var descProductDestino = objValue.text;
        var idProductDestino = objValue.value;
        var specification = "<%=hdnSpecification%>";
        var url = "<%=request.getContextPath()%>/itemServlet?strProductOrigen="+idProductOrigen+"&strProductDestino="+idProductDestino+"&strSpecification="+specification+"&strDescProductDestino="+descProductDestino+"&hdnMethod=getValidateCountUnitsBolCel";
        parent.bottomFrame.location.replace(url);

        /*
         var bolsacreacion = 2021;
         var bolsacambio = 2022;
         var form = parent.mainFrame.frmdatos;
         var vDoc = parent.mainFrame;
         vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');
         vDoc.fxObjectConvert('txt_ItemPriceException','');
         vDoc.fxObjectConvert('txt_ItemEquipmentRent','');
         var c = form.cmb_ItemProducto.options[form.cmb_ItemProducto.selectedIndex].text;
         var a = form.cmb_ItemProducto.options[form.cmb_ItemProducto.selectedIndex].text.substring(c.length-1, c.length);
         var strDescBolsa = "<%=strDescrBolsa%>";
         var strLevel2 = <%=strLevel2%>;

         hdnSpecification=<%=hdnSpecification%>;
         //alert(a); // Letra que escoje
         //alert(strDescBolsa); //Letra que debe escoger

         try{
         form.txt_ItemPriceBolsa.value    = fxSearchProductMinutes(objValue);
         }catch(e){}
         try{
         form.txt_ItemCantMntsBolsa.value = fxSearchMinutes(objValue);
         }catch(e){}

         hdnSpecification=<%=hdnSpecification%>;
         fxShowObject(objValue); aqui */
        /*
         if (hdnSpecification==2001 || hdnSpecification==2009){
         try{
         vDoc.fxClearObjects("cmb_ItemPlanTarifario","SELECT");
         }catch(e){}
         //Se carga el servicio x defecto en base al producto
         //---------------------------------------------------
         fxGetServicesDefault(objValue);
         */
        /* aqui
         var idProdLineCurrent = form.cmb_ItemProductLine.value;
         var idProdLineEquipoMovil = <%=Constante.PRODUCT_LINE_EQUIPO_MOVIL%>;
         var idProdLineEquipoMovil3G = <%=Constante.PRODUCT_LINE_EQUIPO_MOVIL_3G%>;
         //var idProdLineEquipoMovil3GHPPTT = <%=Constante.PRODUCT_LINE_EQUIPO_MOVIL_3G_HPPTT%>;
         var idProdLineUSBModem = <%=Constante.PRODUCT_LINE_USB_MODEM %>;
         var idProdLineTransferenciaEquipo = <%=Constante.PRODUCT_LINE_TRANSFERENCIA_EQUIPO%>;
         var idProdLineEquipoMovil3GHPPTT = <%=strProdLine3GHPPTT%>;
         var idProdLineBucket = <%=strProdLineBucket%>;

         if (hdnSpecification==2006){
        <% int specification2 = hdnSpecification!=null && !"".equals(hdnSpecification)?Integer.parseInt(hdnSpecification):0;
                System.out.println("Entro antes de validacion de venta tel. banda ancha");
                if (specification2==Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION) { %>


         fxGetPlainsDefault(null);

        <% } %>
         }

         //EZM Compatibilidad Modelo-Plan-Servicio
         if (idProdLineCurrent==idProdLineBucket || idProdLineCurrent==idProdLineEquipoMovil || idProdLineCurrent==idProdLineEquipoMovil3G || idProdLineCurrent==idProdLineEquipoMovil3GHPPTT || idProdLineCurrent==idProdLineTransferenciaEquipo
         || idProdLineCurrent==idProdLineUSBModem) {
        <% int specification = hdnSpecification!=null && !"".equals(hdnSpecification)?Integer.parseInt(hdnSpecification):0;
           if (specification==Constante.SPEC_EMPLEADO_AMIGO || specification==Constante.SPEC_EMPLEADO_FAMILIAR
          || specification==Constante.SPEC_EMPLEADO_ASIGNACION || specification==Constante.SPEC_PORTABILIDAD_POSTPAGO
          || specification==Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS || specification==Constante.SPEC_ACCESO_INTERNET
          || specification==Constante.SPEC_VENTA_INTERNET_ENLACE_DATOS || specification==Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO
          || specification==Constante.SPEC_POSTPAGO_VENTA|| specification==Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO
          || specification==Constante.SPEC_PRESTAMO_TEST || specification==Constante.SPEC_SSAA_RENOV_BCKT_CCP) { %>

         fxGetPlainsDefault(objValue);

        <% } else if (specification==Constante.SPEC_CAMBIO_MODELO) { %>
         try{
         vDoc.fxClearObjects("cmb_ItemPlanTarifario","SELECT");
         fxShowServiceSincronic(objValue);
         } catch(e){}

         //fxGetServicesDefault(objValue);

        <% } else if (specification==Constante.SPEC_TRANSFERENCIA_SUB_REG) { %>
         try{
         system.out.println("transferencia");
         } catch(e){}

         fxGetServicesDefault(objValue);

        <% } %>
         }

         // MMONTOYA Despacho en tienda.
         if (hdnSpecification == <%=Constante.SPEC_REPOSICION%>) {
         fxSetDefaultValueCmbItemFlagAccessory();
         }
         vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip',100);
         */
    }

    function fxGetServicesDefault(objValue){
        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var vmodalityid = 0;
        var vplanid = 0;
        var vmodel = 0;
        var vplanoldid = 0;
        var vserviceid = '';
        var vserviceMsjId  = '';

        try{
            vmodalityid = form.cmb_ItemModality.value;
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
        try{
            vserviceMsjId = form.hdnServDefIdMsj.value;
        }catch(e){}


        if (vplanid==0){
            vplanid=vplanoldid;
        }

        if(hdnSpecification == 2009){
            if(vmodel != ""){
                form.hdnOriginalProductIdCM.value = vmodel;
            }
            else{
                vmodel = form.hdnOriginalProductIdCM.value;
            }
        }

        var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getProductServiceDefaultList&strSessionId=<%=strSessionId%>&strSpecificationId=<%=hdnSpecification%>"+"&strobjectItem=<%=nameHtmlItem%>"+"&strProductId="+objValue+"&strModality="+vmodalityid+"&strPlanId="+vplanid+"&strModelId="+vmodel+"&strServiceMsjId="+vserviceMsjId;


        parent.bottomFrame.location.replace(url);
    }
    function fxShowServiceSincronic(objValue){

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



            var url_server = "<%=request.getContextPath()%>/itemServlet";
            var params = "hdnMethod=getServiceList&cmb_ItemPlanTarifario="+vplanid+"&hdnSpecification="+hdnSpecification+"&serviceAditional="+serviceAditional+"&strSessionId=<%=strSessionId%>&cmb_ItemSolution="+valorSolution+"&strobjectItem=<%=nameHtmlItem%>"+"&strPlanId="+vplanid+"&strModality="+vmodalityid+"&strProductId="+objValue+"&strModelId="+vmodel;

            var flagControl = false;
            if(<%=flagControl%> == "1")  flagControl = true;
            stringFlagControl = <%=flagControl%>;

            jQuery.ajax({
                url: url_server,
                data: params,
                type: 'GET',
                async: true,
                cache: false,
                beforeSend: function(){
                    // desactivamos productos.
                    if (hdnSpecification == <%=Constante.SPEC_CAMBIO_MODELO%> && flagControl) {
                        parent.mainFrame.frmdatos.cmb_ItemPromocion.disabled = true;
                        // desactivamos boton grabar.
                        parent.mainFrame.frmdatos.btnAceptar.disabled = true;
                        fxShowService(objValue);
                    }
                },
                complete : function(){
                    // activamos productos.
                    if (hdnSpecification == <%=Constante.SPEC_CAMBIO_MODELO%> && flagControl){
                        parent.mainFrame.frmdatos.cmb_ItemPromocion.disabled = false;
                        // activamos boton grabar.
                        parent.mainFrame.frmdatos.btnAceptar.disabled = false;
                    }
                }

            });

        }
    }

    function fxShowService(objValue){

        if (trim(objValue)!=""){

            var serviceAditional = "";
            var hdnSpecification = <%=hdnSpecification%>;
            var  valorSolution='';
            var vmodalityid = 0;
            var vplanid = 0;
            var vmodel = '';
            var vOrderId = '';
            var vDescProduct = '';
            var vobjTypeEvent = '';

            try{
                serviceAditional = GetSelectedServices();
                valorSolution    =parent.mainFrame.frmdatos.cmb_ItemSolution.value;
                vPhone =  parent.mainFrame.frmdatos.txt_ItemPhone.value;
                vOrderId = parent.opener.frmdatos.hdnNumeroOrder.value;
                vDescProduct = form.cmb_ItemProducto.options[form.cmb_ItemProducto.selectedIndex].text;
                vobjTypeEvent = "NEW";
                <%if( "EDIT".equals(type_window) ){%>
                vobjTypeEvent="EDIT";
                <%}%>
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
            var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&cmb_ItemPlanTarifario="+vplanid+"&hdnSpecification="+hdnSpecification+"&serviceAditional="+serviceAditional+"&strSessionId=<%=strSessionId%>&cmb_ItemSolution="+valorSolution+"&strobjectItem=<%=nameHtmlItem%>"+"&strPlanId="+vplanid+"&strModality="+vmodalityid+"&strProductId="+objValue+"&strModelId="+vmodel+"&strPhone="+vPhone+"&strOrderId="+vOrderId+"&strDescProduct="+vDescProduct+"&strTypeEvent="+vobjTypeEvent;;

            parent.bottomFrame.location.replace(url);

        }
    }

    function fxGetPlainsDefault(objValue){ //EZM Compatibilidad Modelo-Plan-Servicios
        //alert('el productid es (objItemProduct)--->' + objValue);
        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var vmodalityid = 0;
        //var vplanid = 0;
        var vmodel = 0;
        var solution = 0;
        var vserviceid = '';
        var prodline ='';
        try{
            vmodalityid = form.cmb_ItemModality.value;
        }catch(e){}
        try{
            vmodel = form.hdnOriginalProductId.value;
        }catch(e){}
        try{
            solution = parent.mainFrame.frmdatos.cmb_ItemSolution.value;
        }catch(e){}
        try{
            productline = parent.mainFrame.frmdatos.cmb_ItemProductLine.value; //EZUBIAURR 15/03/11
        }catch(e){}

        var url = "<%=request.getContextPath()%>/itemServlet?strProductLineId="+productline+"&strModality="+vmodalityid+"&strProductId="+objValue+"&type=<%=type%>&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&hdnMethod=getProductPlanList"; //EZUBIAURR 15/03/11
        //alert(url);
        parent.bottomFrame.location.replace(url);
    }

    function fxChangeProductEdit(objValue){
        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        try{
            form.txt_ItemPriceBolsa.value    = fxSearchProductMinutes(objValue);
        }catch(e){}
        try{
            form.txt_ItemCantMntsBolsa.value = fxSearchMinutes(objValue);
        }catch(e){}
    }

    function fxSearchProductMinutes(objValue){
        //alert("fxSearchProductMinutes : " + vctProduct.size());
        for( i=0; i<vctProduct.size(); i++ ){
            if( vctProduct.elementAt(i).objProductId == objValue )
                return vctProduct.elementAt(i).objProductMintsPrice;
        }

        return 0;
    }

    function fxSearchMinutes(objValue){
        //alert("fxSearchMinutes : " + vctProduct.size());
        for( i=0; i<vctProduct.size(); i++ ){
            if( vctProduct.elementAt(i).objProductId == objValue )
                return vctProduct.elementAt(i).objProductPrice;
        }

        return 0;
    }

    function fxLoadProduct(){

    }

    function fxShowObject(objValue){
        var form = parent.mainFrame.frmdatos;
        var modality = form.cmb_ItemModality.value;
        var productLine = form.cmb_ItemProductLine.value;
        var product = objValue;
        var flagShow = 'N';
        <%ConfigurationBean objConfiguration = null;
          String strModality = null;
          String strProductLine = null;
          String strProduct = null;
          for( int j = 0; j < arrConfiguration.size(); j++ ){
            objConfiguration = new ConfigurationBean();
            objConfiguration = (ConfigurationBean)arrConfiguration.get(j);
            strModality = objConfiguration.getNpTag1();
            strProductLine = objConfiguration.getNpTag2();
            strProduct = objConfiguration.getNpTag3();
        %>
        if ( modality == '<%=strModality%>' && productLine == '<%=strProductLine%>' && product == '<%=strProduct%>') {
            try{
                idDisplay121.style.display = '';
                <%
                  hshtinputNewSection.put("strModality",strModality);
                  hshtinputNewSection.put("strProductLine",strProductLine);
                  hshtinputNewSection.put("strProduct",strProduct);
                %>
            }catch(e){}

            flagShow = 'S';
        }
        <%}%>
        if (flagShow == 'N'){
            try{
                idDisplay121.style.display = 'none';
            }catch(e){}
        }
    }

</script>
<input type="HIDDEN" name="hdnOriginalProductId">
<select name="<%=nameHtmlItem%>" onChange="javascript:fxChangeProductDestino(this);"  >
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
</select>

<!--    CPUENTE2 CAP & CAL - INICIO -->
<% if (hdnSpecification.equals(Constante.SPEC_CAMBIO_MODELO+"") || hdnSpecification.equals(Constante.SPEC_REPOSICION+"")) {
%>

<a href="javascript:fShowDetailStock();">
    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"/>&nbsp;Stock en Tiendas
</a>
<script type="text/javascript">
    function fShowDetailStock() {
        var obj = document.frmdatos.cmb_ItemProducto;
        var lblProducto= obj[obj.selectedIndex].text;

        if (obj.value!="") {
            var url = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpStock.jsp?strModel="+lblProducto;
            WinAsist = window.open(url,"DetalleStock","toolbar=no,location=0,directories=no," +
            "status=yes,menubar=0,scrollbars=yes,resizable=yes,screenX=100," +
            "top=80,left=150,screenY=80,width=700,height=650,modal=yes");

        }else {
            alert("Seleccione un Modelo de Producto.");
            form.cmb_ItemProducto.focus();
        }
    }
</script>

<%}%>
<!--    CPUENTE2 CAP & CAL - FIN -->

<%
    if( !type_window.equals("NEW") ){
        String strModalidad    = "";
        String strProdLineId   = "";
        String strProductId    = "";
        String strSolutionId   = "";

        String[] paramNpobjitemvalue      = request.getParameterValues("b");
        String[] paramNpobjitemheaderid   = request.getParameterValues("a");

        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            //Modalidad
            if( paramNpobjitemheaderid[i].equals("1") )
                strModalidad = paramNpobjitemvalue[i];
            //Product
            if( paramNpobjitemheaderid[i].equals("9") )
                strProductId = paramNpobjitemvalue[i];
            //ProductLineId
            if( paramNpobjitemheaderid[i].equals("51") )
                strProdLineId = paramNpobjitemvalue[i];
            //Solución
            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);
            }
        }

        //if( MiUtil.getString(strModalidad).equals("Prestamo") )
        //strModalidad = "Venta";
%>

<script>
    function fxLoadEditProductDestino(){
        formCurrent = parent.mainFrame.frmdatos;
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);
        parent.mainFrame.vctProduct.removeElementAll();
        <%
         ProductBean objProductBean = new ProductBean();

         objProductBean.setNpproductlineid(MiUtil.parseLong(strProdLineId));
         objProductBean.setNpmodality(strModalidad);
         objProductBean.setNpcategoryid(MiUtil.parseLong(hdnSpecification));
         objProductBean.setNpsolutionid(MiUtil.parseLong(strSolutionId));
         objProductBean.setNpproduct_type(1);
        System.out.println("ENTRO AL JSP DEL PRODUCTO DEL DESTINO");
         HashMap objHashMap = objNewOrderService.getProductType(objProductBean);
         if( objHashMap == null )
            throw new Exception("Surgieron errores al cargar los productos");
         else if( (String)objHashMap.get("strMessage")!= null )
            throw new Exception(MiUtil.getMessageClean((String)objHashMap.get("strMessage")));

         objArrayList = (ArrayList)objHashMap.get("objArrayList");

         if ( objArrayList != null && objArrayList.size() > 0 ){

            for( int i=0; i<objArrayList.size();i++ ){
                  objProductBean = new ProductBean();
                  objProductBean = (ProductBean)objArrayList.get(i);
         %>
        parent.mainFrame.vctProduct.addElement(new parent.mainFrame.fxMakeProduct('<%=objProductBean.getNpproductid()%>','<%=objProductBean.getNpminute()%>','<%=objProductBean.getNpminuteprice()%>'));
        parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductoDestino,'<%=objProductBean.getNpproductid()%>','<%=MiUtil.getMessageClean(objProductBean.getNpproductname())%>');

        <%
           }
        }
       %>
        //Cargo los valores
        <%if(!strProductId.equals("")){%>
        fxChangeProductEdit(<%=strProductId%>);
        <%}%>

    }
</script>
<%}%>