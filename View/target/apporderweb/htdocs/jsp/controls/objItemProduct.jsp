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
    System.out.println("*********************************************objItemProduct.jsp*********************************************");
    Hashtable hshtinputNewSection = null;
    String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
    String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "",
            strSessionId = ""
            //INICIO: AMENDEZ | PRY-1049
            ,strHdnCobertura=""
            //FIN: AMENDEZ | PRY-1049
                    ;

    hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");

    if ( hshtinputNewSection != null ) {
        strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
        strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
        strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
        hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
        strDivision             =   (String)hshtinputNewSection.get("strDivision");
        strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
        //INICIO: AMENDEZ | PRY-1049
        strHdnCobertura         =   (String)hshtinputNewSection.get("strHdnCobertura");
        System.out.println("strCodigoCliente :     "+strCodigoCliente);
        System.out.println("strnpSite        :     "+strnpSite);
        System.out.println("strCodBSCS       :     "+strCodBSCS);
        System.out.println("hdnSpecification :     "+hdnSpecification);
        System.out.println("strDivision      :     "+strDivision);
        System.out.println("strSessionId     :     "+strSessionId);
        System.out.println("strHdnCobertura  :     "+strHdnCobertura);
        //FIN: AMENDEZ | PRY-1049
        request.setAttribute("hshtInputNewSection",hshtinputNewSection);
    }
    System.out.println("******************************************************************************************");
    String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
    System.out.println("nameHtmlItem  :     "+nameHtmlItem);
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
  
   /*Obtener npvalue del product line Equipo M�vil 3G HPPTT*///EZUBIAURR 17/01
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

    function fxChangeProduct(objValue){
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
        
      	//PRY-0762 JQUISPE Renta Adelantada
        try{
        	fxResetFieldRentaAdelantada();
        }catch(e){}
        
        //alert(a); // Letra que escoje
        //alert(strDescBolsa); //Letra que debe escoger

        if ((hdnSpecification == bolsacreacion) || (hdnSpecification == bolsacambio))   {  // MA - 67733
            var b = form.cmb_ItemProducto.options[form.cmb_ItemProducto.selectedIndex].text.substring(6,8);
            if (form.txt_ItemMntsRates && form.txt_ItemPriceByMnts) {
                if (b == "0K"){
                    if (strLevel2 == 1 ){
                        var strBolsaN2 = <%=strBolsaN2%>;
                        var strMinutesrate = <%=strMinutesrate%>;
                        var strPrecioxmin = <%=strPrecioxmin%>;
                        form.txt_ItemMntsRates.value = 0;
                        form.txt_ItemMntsRates.disabled = true;
                        form.txt_ItemPriceByMnts.value  = 0;
                        form.txt_ItemPriceByMnts.disabled = true;
                    }
                    else{
                        if (strLevel2 == 2 ){
                            var strBolsaN2 = <%=strBolsaN2%>;
                            var strMinutesrate = <%=strMinutesrate%>;
                            var strPrecioxmin = <%=strPrecioxmin%>;
                            form.txt_ItemMntsRates.value = strMinutesrate;
                            form.txt_ItemMntsRates.disabled = true;
                            form.txt_ItemPriceByMnts.value  = strPrecioxmin;
                            form.txt_ItemPriceByMnts.disabled = true;
                        }
                    }
                }
                else{
                    form.txt_ItemMntsRates.disabled = false;
                    form.txt_ItemPriceByMnts.disabled = false;
                    form.txt_ItemMntsRates.value   = "";
                    form.txt_ItemPriceByMnts.value = "";
                }
            }
        }
        try{
            form.txt_ItemPriceBolsa.value    = fxSearchProductMinutes(objValue);
        }catch(e){}
        try{
            form.txt_ItemCantMntsBolsa.value = fxSearchMinutes(objValue);
        }catch(e){}

        hdnSpecification=<%=hdnSpecification%>;
        fxShowObject(objValue);
        /*
         if (hdnSpecification==2001 || hdnSpecification==2009){
         try{
         vDoc.fxClearObjects("cmb_ItemPlanTarifario","SELECT");
         }catch(e){}
         //Se carga el servicio x defecto en base al producto
         //---------------------------------------------------
         fxGetServicesDefault(objValue);
         */
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

        //PRY-0721 DERAZO Obtenemos las regiones habilitadas por producto solo si el control esta configurado
        var sectionRegion = parent.mainFrame.document.getElementById("idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>");
        if(sectionRegion != null){
            fxGetEnabledRegions(objValue);
        }else{
            //EFLORES BAFI2 Se ocultan los campos de provincia y distrito
            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
        }

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
                try{// EZUBIAURR HPPT+ Validaci�n del productid seg�n categor�a

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
                try{// EZUBIAURR HPPT+ Validaci�n del productid seg�n categor�a

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
        //PRY-1049 | INICIO: AM0001
        var flagcoverage=-5;
        try{
            flagcoverage=parent.opener.parent.mainFrame.document.frmdatos.hdnCobertura.value;
        }catch(e){
            flagcoverage=-5;
        }
        //PRY-1049 | FIN: AM001

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

        var url = "<%=request.getContextPath()%>/itemServlet?strProductLineId="+productline+"&strModality="+vmodalityid+"&strProductId="+objValue+"&type=<%=type%>&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&hdnMethod=getProductPlanList"+"&flagcoverage="+flagcoverage; //EZUBIAURR 15/03/11
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

    //EST-1098 | INICIO: AM0001
    function validateConfigBafi2300(){
        try{
            var url_server = "<%=request.getContextPath()%>/editordersevlet";
            var varValidateConfigBafi2300=0;
            var ItemModality    = parent.mainFrame.frmdatos.cmb_ItemModality.value;
            var ItemSolution    = parent.mainFrame.frmdatos.cmb_ItemSolution.value;
            var ItemProductLine = parent.mainFrame.frmdatos.cmb_ItemProductLine.value;
            var params = 'myaction=validateConfigBafi2300&av_npmodality='+ItemModality+'&av_npsolutionid='+ItemSolution+'&av_npproductlineid='+ItemProductLine;

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
    //EST-1098 | FIN: AM0001

    //PRY-0721 DERAZO Obtener regiones habilitadas por producto
    function fxGetEnabledRegions(objValue){
        var url_server = "<%=request.getContextPath()%>/itemServlet";
        var params = "hdnMethod=getEnabledRegions&strProductId="+objValue;

        //EST-1098 | INICIO: AM0001
        var flagcoverage="-404";
        var regionValue='';
        var provinceValue='';
        var districtValue='';
        var regionValueId='';
        var provinceValueId='';
        var districtValueId='';
        var form=parent.opener.parent.mainFrame.document.frmdatos;        
        var baficonfig=validateConfigBafi2300();

        try{
            flagcoverage=form.hdnCobertura.value;
            if ((flagcoverage==1 || flagcoverage==0) && baficonfig==1){
                regionValue   = form.hdnRegion.value;
                provinceValue = form.hdnProvince.value;
                districtValue = form.hdnDistrict.value;
                regionValueId   = form.hdnRegiondId.value;
                provinceValueId = form.hdnProvinceId.value;
                districtValueId = form.hdnDistrictId.value;

                //LGONZALES PRY-1049
                if(parent.mainFrame.frmdatos.cmb_ItemRegion.length &&
                   parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId)){
                    parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId);
                }else{
                parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId,regionValue);
                    parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemRegion,regionValueId);
                }
                parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = 'block';//LGONZALES

                //LGONZALES PRY-1049
                if(parent.mainFrame.frmdatos.cmb_NpProvinceZone.length &&
                        parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId)){
                    parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId);
                }else{
                parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId,provinceValue);
                    parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpProvinceZone,provinceValueId);
                }
                parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_PROVINCE_ID%>.style.display = 'block';//LGONZALES

                <!-- BEGIN: PRY-1049 | DOLANO-0002 -->
                <!-- Ocultar el campo de distrito cuando no hay cobertura a nivel de distrito -->
                if(districtValueId == null || districtValueId == -1){
                   parent.mainFrame.frmdatos.cmb_NpDistrictZone.value = "";
                   fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                } else {
                    //LGONZALES PRY-1049
                    if(parent.mainFrame.frmdatos.cmb_NpDistrictZone.length &&
                            parent.mainFrame.CheckSelectItemExistance(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId)){
                        parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId);
                    }else{
                   parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId,districtValue);
                        parent.mainFrame.SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_NpDistrictZone,districtValueId);
                    }
                    parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_DISTRICT_ID%>.style.display = 'block';//LGONZALES
                }
                <!-- END: PRY-1049 | DOLANO-0002 -->

                parent.mainFrame.frmdatos.cmb_ItemRegion.disabled     =true;
                parent.mainFrame.frmdatos.cmb_NpProvinceZone.disabled =true;
                parent.mainFrame.frmdatos.cmb_NpDistrictZone.disabled =true;

            }else{
                flagcoverage=-6;
                parent.mainFrame.frmdatos.cmb_ItemRegion.disabled     =false;
                parent.mainFrame.frmdatos.cmb_NpProvinceZone.disabled =false;
                parent.mainFrame.frmdatos.cmb_NpDistrictZone.disabled =false;
                //LGONZALES
                fxShowDivByHeaderId(<%=Constante.CONTROL_ITEM_REGION_ID%>);
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
            }

        }catch(e){
            flagcoverage=-6;
        }

        //EST-1098 | FIN: AM0001
        if (flagcoverage==-6){
        jQuery.ajax({
            url: url_server,
            data: params,
            type: "POST",
            dataType: 'json',
            success:function(data) {
                var formCurrentRegion = parent.mainFrame.frmdatos;

                if(!(typeof data.strMessage === "undefined")){
                    alert(data.strMessage);
                    formCurrentRegion.cmb_ItemRegion.value = "";
                    parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = "none";
                    //EFLORES BAFI2
                    fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                    fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                }
                else{
                    var result = data.result;
                    if(result > 0){
                        parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = 'block';
                        //EFLORES BAFI2
                        fxShowDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                        fxShowDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                        parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_ItemRegion);
                        parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_NpProvinceZone);//EFLORES BAFI2
                        parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_NpDistrictZone);//EFLORES BAFI2
                        var arrayRegions = data.listaRegiones;
                        if(arrayRegions != null){
                            for(i=0; i<arrayRegions.length; i++) {
                                parent.mainFrame.AddNewOption(formCurrentRegion.cmb_ItemRegion,arrayRegions[i].regionId,arrayRegions[i].regionName);
                            }
                        }
                    }
                    else{
                        formCurrentRegion.cmb_ItemRegion.value = "";
                        parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = "none";
                        //EFLORES BAFI2
                        fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                        fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                    }
                }
            },
            error:function(xhr, status, error) {
                var formCurrentRegion = parent.mainFrame.frmdatos;
                alert("Error interno al validar regiones: "+xhr.responseText);
                formCurrentRegion.cmb_ItemRegion.value = "";
                parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = "none";

                //EFLORES BAFI2
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
            }
        });

    }


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
<select name="<%=nameHtmlItem%>" onChange="javascript:fxChangeProduct(this.value);"  >
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
    String strTypeProductBC   = "";

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
            //Soluci�n
            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);
            }
      if( paramNpobjitemheaderid[i].equals("146") ) {
          strTypeProductBC = MiUtil.getString(paramNpobjitemvalue[i]);
      }
        }

        //if( MiUtil.getString(strModalidad).equals("Prestamo") )
        //strModalidad = "Venta";
%>

<script>
    function fxLoadEditProduct(){
        formCurrent = parent.mainFrame.frmdatos;
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);
        parent.mainFrame.vctProduct.removeElementAll();
        <%
         ProductBean objProductBean = new ProductBean();

         objProductBean.setNpproductlineid(MiUtil.parseLong(strProdLineId));
         objProductBean.setNpmodality(strModalidad);
         objProductBean.setNpcategoryid(MiUtil.parseLong(hdnSpecification));
         objProductBean.setNpsolutionid(MiUtil.parseLong(strSolutionId));
       if(objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_CREAR) objProductBean.setNpproduct_type(Integer.parseInt(strTypeProductBC));

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
        parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProducto,'<%=objProductBean.getNpproductid()%>','<%=MiUtil.getMessageClean(objProductBean.getNpproductname())%>');
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