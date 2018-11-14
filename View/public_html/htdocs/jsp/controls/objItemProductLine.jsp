<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.bean.ProductLineBean" %>
<%@ page import="pe.com.nextel.bean.TableBean" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%
    try{
        Hashtable hshtinputNewSection = null;
        HashMap hshValueNpTableSerTel = null;
        String npValue = "";
        String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
        NewOrderService objNewOrderService = new NewOrderService();
        GeneralService objGeneralService = new GeneralService();
        String    hdnSpecification = "";

  /*INICIO ADT-BLC-083 --LHUAPAYA*/
        String strnpSite= "";
        String strnpCustomerid="";
  /*FIN ADT-BLC-083 --LHUAPAYA*/

        hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");

        hshValueNpTableSerTel   = objGeneralService.getValueNpTable("PRODUCT_LINE_SER_TEL");
        if (hshValueNpTableSerTel.get("strMessage")!=null){%>
<script>alert(<%=hshValueNpTableSerTel.get("strMessage")%>)</script>
<%}

    ArrayList arrNpTableSerTel = new ArrayList();
    arrNpTableSerTel = (ArrayList)hshValueNpTableSerTel.get("objArrayList");

    for(int i = 0; i < arrNpTableSerTel.size(); i++){
        TableBean tableBean = (TableBean)arrNpTableSerTel.get(i);
        npValue +=tableBean.getNpValue()+"|";
    }
    npValue = npValue.substring(0,npValue.length()-1);

    if ( hshtinputNewSection != null ) {
        hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    /*INICIO ADT-BLC-083 --LHUAPAYA*/
        strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
        strnpCustomerid         =   (String)hshtinputNewSection.get("strCodigoCliente");
    /*FIN ADT-BLC-083 --LHUAPAYA*/
    }

    String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
    if ( nameHtmlItem==null ) nameHtmlItem = "";

    String strSolution= null;
    String strLineProduct=null;

    if( !type_window.equals("NEW") ){
        String[] paramNpobjitemheaderid = request.getParameterValues("a");
        String[] paramNpobjitemvalue    = request.getParameterValues("b");
        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolution = MiUtil.getString(paramNpobjitemvalue[i]);
            }
        }
    }
    String idSpecificationId = (String)request.getAttribute("idSpecificationId");
    HashMap objHashMap = objNewOrderService.ProductLineDAOgetProductLineSpecList(MiUtil.parseLong(strSolution),MiUtil.parseLong(hdnSpecification),"PRODUCTLINE",0);
    if (objHashMap.get("strMessage")!=null){%>
<script>alert(<%=objHashMap.get("strMessage")%>)</script>
<%}
    ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
    ProductLineBean productLineBean = null;
%>
<script>
    function fxValueInArray(objArray, objValue){
        var strArraySplit = objArray.split("|");
        for(i = 0; i < strArraySplit.length; i++){
            if(strArraySplit[i]==objValue){
                return true;
            }
        }
        return false;
    }

    function fxChangeObjectProduct(objValue){
        var kitGE = <%=Constante.PRODUCT_LINE_KIT_GAR_EXT%>;
        var kitGolden = <%=Constante.PRODUCT_LINE_KIT_GOLDEN%>;
        var itemHI_RN = <%=Constante.ITEM_HEADER_ID_RESERVA_NUMEROS%>;
        var cmb_product_model_id = <%=Constante.CMB_ITEM_PRODUCT_MODEL_ID%>;

        if (objValue==kitGE || objValue==kitGolden) {
            if (objValue==kitGolden) {
                fxShowDivByHeaderId(itemHI_RN);
            } else {
                fxHideDivByHeaderId(itemHI_RN);
            }
            parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProductModel);
            fxLoadProductModelFromLine(objValue);
        } else {
            /*INICIO ADT-BLC-083 --LHUAPAYA*/
            if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_CREAR%>"){
                parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProducto);
                parent.mainFrame.DeleteSelectOptions(form.cmb_ItemPromocion);
                vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');
                vDoc.fxObjectConvert('txt_ItemPriceException','');
                vDoc.fxObjectConvert('txt_ItemEquipmentRent','');
                form.cmb_itemType.value = null;
            }else{ /*FIN ADT-BLC-083 --LHUAPAYA*/
                fxHideDivByHeaderId(cmb_product_model_id);
                fxHideDivByHeaderId(itemHI_RN);
                fxLoadProductFromLine(objValue);
            }
        }
    }

    function fxLoadProductFromLine(objValue){

        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var solution = "";
        try {
            solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        }catch(e){}

        var valueCurrent = "";
        valueCurrent = objValue;
        if( fxValueInArray("<%=npValue.toString()%>",valueCurrent)) {
            fxActionServicioShow();
        }else
            fxActionServicioHide();

        if( objValue != "" ){
            var modality = form.cmb_ItemModality.value;
            var origPlanId = form.hdnOriginalPlanId.value;
            /*JPEREZ 19/02/2010*/
            /*EZUBIAURR 15/10/2010 - Se agrego el parametro strPlanId a la invocacion por Compatibilidad de Productos con el Plan*/
            /*ADT-BCL-083 -- LHUAPAYA 11/08/2015 - Se agregaron los parametros: strSiteId y strCustomerId al url*/
            var url = "<%=request.getContextPath()%>/itemServlet?strModality="+modality+"&strProductLineId="+objValue+"&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&strPlanId="+origPlanId+"&strSiteId=<%=strnpSite%>&strCustomerId=<%=strnpCustomerid%>&hdnMethod=getProducTypeList";
            vDoc.DeleteSelectOptions(form.cmb_ItemPromocion);
            vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');
            vDoc.fxObjectConvert('txt_ItemPriceException','');
            vDoc.fxObjectConvert('txt_ItemEquipmentRent','');
            /*Inicio JPEREZ*/
            fxContractFields(objValue, solution, 1);
            if ( (objValue=="<%=Constante.PRODUCT_LINE_EQ_TF%>") || (objValue=="<%=Constante.PRODUCT_LINE_KIT_TF%>") ) { //Equipo de TF o Kit de TF
                try{
                    fxOcultarTr("yes",idDisplay10);
                }catch(e){}
            }
            /*Fin JPEREZ*/
            /*INICIO ADT-BLC-083 --LHUAPAYA*/
            if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_CREAR%>"){
                document.getElementById("cmb_ItemProducto");
                out.println("formCurrent = parent.mainFrame.frmdatos;");
                out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);");
                out.println("parent.mainFrame.vctProduct.removeElementAll();");
            }else{/*FIN ADT-BLC-083 --LHUAPAYA*/
                parent.bottomFrame.location.replace(url);
            }
        }else{
            parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProducto);
            parent.mainFrame.DeleteSelectOptions(form.cmb_ItemPromocion);
            vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');
            vDoc.fxObjectConvert('txt_ItemPriceException','');
            vDoc.fxObjectConvert('txt_ItemEquipmentRent','');
        }

    }

    function fxLoadProductModelFromLine(objValue){
        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var solution = "";
        var kitGE = <%=Constante.PRODUCT_LINE_KIT_GAR_EXT%>;
        var cmb_product_model_id = <%=Constante.CMB_ITEM_PRODUCT_MODEL_ID%>;
        try {
            solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        }catch(e){}
        var url = "";

        if( objValue != "" ){
            var modality = form.cmb_ItemModality.value;
            var url = "<%=request.getContextPath()%>/itemServlet?strModality="+modality+"&strProductLineId="+objValue+"&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&hdnMethod=getModelProducTypeList";
            vDoc.DeleteSelectOptions(form.cmb_ItemPromocion);
            fxShowDivByHeaderId(cmb_product_model_id);
            parent.bottomFrame.location.replace(url);
        } else{
            parent.mainFrame.DeleteSelectOptions(form.cmb_ItemPromocion);
        }
        parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProducto);
    }

    function fxSendGetProduct(modality,productLineId){

    }


    function fxActionServicioShow(){
        try{


            <%if( MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") ||   MiUtil.getString(hdnSpecification).equals("2069")
                || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%>
            //Sim EAGLE
            fxShowDivByHeaderId(2);
            //Modelo
            fxShowDivByHeaderId(85);
            <%}%>

        }catch(e){}
    }


    function fxActionServicioHide(){
        try{


            <%if( MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") || MiUtil.getString(hdnSpecification).equals("2069") 
                || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%>
            //Sim EAGLE
            fxHideDivByHeaderId(2);
            //Modelo
            fxHideDivByHeaderId(85);
            <%}%>

        }catch(e){}
    }

    /* Función que muestra el campo dado el
     HeaderId.
     */
    function fxShowDivByHeaderId(headerId){
        try{
            eval("idDisplay"+headerId+".style.display = '';");
        }catch(e){}
    }


    /**
     Función que oculta el campo dado el
     HeaderId.
     */
    function fxHideDivByHeaderId(headerId){
        try{
            eval("idDisplay"+headerId+".style.display = 'none';");
        }catch(e){}
    }

    /*Inicio JPEREZ*/

    function fxContractFields(objValue, solution, clearPhone){
        var form = parent.mainFrame.frmdatos;

        if (solution  == "<%=Constante.KN_ACCESO_INTERNET%>"){
            try{
                fxOcultarTr("none",idDisplay99);
                fxOcultarTr("none",idDisplay101);
                <%if ( MiUtil.parseInt(hdnSpecification) != Constante.SPEC_ACCESO_INTERNET ){%>
                fxOcultarTr("yes",idDisplay100);
                form.txt_ItemContratoInternet.value="";
                <%}%>
            }catch(e){}
        }
        if (solution  == "<%=Constante.KN_ENLACE_DATOS%>"){
            try{
                fxOcultarTr("none",idDisplay99);
                fxOcultarTr("none",idDisplay100);
                <%if ( MiUtil.parseInt(hdnSpecification) != Constante.KN_VTA_INTERNET_ENLACE_DATOS ){%>
                fxOcultarTr("yes",idDisplay101);
                form.txt_ItemContratoDatos.value="";
                <%}%>
            }catch(e){}
        }
        if ( (solution  == "<%=Constante.KN_TELEFONIA_FIJA%>") && (objValue=="<%=Constante.PRODUCT_LINE_SERV_INST_TF%>") ){
            try{
                fxOcultarTr("none",idDisplay10);
                fxOcultarTr("none",idDisplay100);
                fxOcultarTr("none",idDisplay101);
                fxOcultarTr("none",idDisplay99); //nbravo

            }catch(e){}
        }
        if ( (solution  == "<%=Constante.KN_TELEFONIA_FIJA%>") && (objValue!="<%=Constante.PRODUCT_LINE_SERV_INST_TF%>") ){
            try{
                fxOcultarTr("yes",idDisplay100);
                fxOcultarTr("yes",idDisplay101);
                fxOcultarTr("yes",idDisplay99);
            }catch(e){}
        }
        if (clearPhone==1){
            try{
                form.txt_ItemNumeroExistenteTF.value="";
            }catch(e){}
        }

    }

    function fxShowLineRelatedFields(objValue, strSolution){
        try{
            if ( (objValue=="<%=Constante.PRODUCT_LINE_EQ_TF%>") || (objValue=="<%=Constante.PRODUCT_LINE_KIT_TF%>") ){ //Equipo de TF o Kit de TF
                fxOcultarTr("yes",idDisplay10); //Oculta "Plan"
            }else{
                fxOcultarTr("none",idDisplay10);
            }
        }catch(e){}
        fxContractFields(objValue, strSolution, 0);

    }
    /*Fin JPEREZ*/

    function fxLoadEditProductLine(){

        formCurrent = parent.mainFrame.frmdatos;
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProductLine);

        <% if ( objArrayList != null && objArrayList.size() > 0 ){
              for( int i=0; i<objArrayList.size();i++ ){
                productLineBean = new ProductLineBean();
                productLineBean = (ProductLineBean)objArrayList.get(i);
        %>
        parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductLine,"<%=productLineBean.getNpProductLineId()%>","<%=productLineBean.getNpName()%>");

        <%   }

         }

        %>

    }


</script>
<select name="<%=nameHtmlItem%>" onChange="javascript:fxChangeObjectProduct(this.value);" >
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    <%
        if ( objArrayList != null && objArrayList.size() > 0 ){

            for( int i=0; i<objArrayList.size();i++ ){
                productLineBean = new ProductLineBean();
                productLineBean = (ProductLineBean)objArrayList.get(i);
    %>

    <option value='<%=productLineBean.getNpProductLineId()%>'>
        <%=productLineBean.getNpName()%>
    </option>
    <%
            }
        }
    %>

</select>


<%
    /*Inicio JPEREZ*/
    if( !type_window.equals("NEW") ){
        String[] paramNpobjitemheaderid = request.getParameterValues("a");
        String[] paramNpobjitemvalue    = request.getParameterValues("b");
        String strProductLine           = "";
        String strSolutionId               = "";
        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            if( paramNpobjitemheaderid[i].equals("51") ) {
                strProductLine = MiUtil.getString(paramNpobjitemvalue[i]);
            }
            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);
            }
        }
        if (strProductLine != null){%>
<script>
    fxShowLineRelatedFields("<%=strProductLine%>", "<%=strSolutionId%>");
</script>
<%}


}
/*Fin JPEREZ*/
}catch(Exception e){
    e.printStackTrace();
}
%>