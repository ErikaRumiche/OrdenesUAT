<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.bean.SolutionBean" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.util.Constante" %>


<%

    //Declaramos Variables
//--------------------
    HashMap           objHashMap            = null;
    Hashtable         hshtinputNewSection   = null;
    String            hdnSpecification      = "";
    String            strSessionId          = "";
    String            strDivisionId         = "";
    String            strnpSite             = "";
    NewOrderService   objNewOrderService    = new NewOrderService();
    GeneralService    objGeneralService     = new GeneralService();
    SpecificationBean objSpecificationBean  = null;
    ArrayList         objArrayList           = new ArrayList();
    String strSolutiontype = null;
    String strCustomerId   = "";
    String strSolution= null;
    String strValidateSolution = null;
    String strOriginalSolutionId=null;
%>

<%
    //Recogemos valores necesarios del input del item y de la orden
//-------------------------------------------------------------

//1.Recogemos el nombre del objeto del PopUpItem
//-----------------------------------------------
    String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
    if  ( nameHtmlItem==null ) nameHtmlItem = "";

//2.Obtenemos datos que se encuentran en la Orden
//------------------------------------------------
    hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
    if ( hshtinputNewSection!= null ){
        hdnSpecification     = (String)hshtinputNewSection.get("hdnSpecification");
        strSessionId         =   (String)hshtinputNewSection.get("strSessionId");
        strDivisionId        =   (String)hshtinputNewSection.get("strDivision");
        strnpSite            =   (String)hshtinputNewSection.get("strnpSite");
        request.setAttribute("hshtInputNewSection",hshtinputNewSection);
    }

//3.Otros valores que se recogen al cargar la sección dinámica
//------------------------------------------------------------
    String strTypeCompany = request.getParameter("strTypeCompany");
    strCustomerId = request.getParameter("strCustomerId");

%>
<script>

    //Hide Check Marcar SIM - vasp
    //fxHideDivByHeaderId(151);

    /*Inicio JPEREZ*/

    function fxShowContractFields(objValue){
        var form = parent.mainFrame.frmdatos;
        try{
            fxOcultarTr("none",idDisplay20);
        }catch(e){}
        if (objValue  == "<%=Constante.KN_ACCESO_INTERNET%>"){
            try{
                fxOcultarTr("none",idDisplay99);
                fxOcultarTr("none",idDisplay101);
                try{
                    fxOcultarTr("none",idDisplay26);
                }catch(e){}
                <%if ( MiUtil.parseInt(hdnSpecification) != Constante.SPEC_ACCESO_INTERNET ){%>
                fxOcultarTr("yes",idDisplay100);
                form.txt_ItemContratoInternet.value="";
                <%}%>
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay111);
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay33);
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay34);
            }catch(e){}
        }
        if (objValue  == "<%=Constante.KN_ENLACE_DATOS%>"){
            try{
                fxOcultarTr("none",idDisplay99);
                fxOcultarTr("none",idDisplay100);
                <%if ( MiUtil.parseInt(hdnSpecification) != Constante.KN_VTA_INTERNET_ENLACE_DATOS ){%>
                fxOcultarTr("yes",idDisplay101);
                fxOcultarTr("yes",idDisplay26);
                form.txt_ItemContratoDatos.value="";
                <%}%>
            }catch(e){}
            try{
                fxOcultarTr("none",idDisplay111);
            }catch(e){}
            try{
                fxOcultarTr("none",idDisplay33);
            }catch(e){}
            try{
                fxOcultarTr("none",idDisplay34);
            }catch(e){}
        }
        if (objValue  == "<%=Constante.KN_TELEFONIA_FIJA%>"){
            try{

                fxOcultarTr("none",idDisplay100);
                fxOcultarTr("none",idDisplay101);
                fxOcultarTr("none",idDisplay99); //nbravo
                fxOcultarTr("yes",idDisplay26);
                fxOcultarTr("yes",idDisplay20); //nbravo
                //form.txt_ItemNumeroExistenteTF.value="";
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay111);
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay33);
            }catch(e){}
            try{
                fxOcultarTr("yes",idDisplay34);
            }catch(e){}
        }
    }

    function fxLoadServicesPlanFromSolutionEdit(objValue, valorRespPago ){
        var form = parent.mainFrame.frmdatos;
        var valueCurrent = objValue;
        if (trim(valueCurrent)!=""){
            var hdnSpecification = "<%=hdnSpecification%>";
            var strCustomerId   = "<%=strCustomerId%>";
            var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getSoluctionRespPago&strSpecificationId="+hdnSpecification+"&strSessionId=<%=strSessionId%>&strSolutionId="+valueCurrent+"&respPago="+valorRespPago+"&strCustomerId="+strCustomerId;
            parent.bottomFrame.location.replace(url);
        }
    }
    /*Fin JPEREZ*/
</script>
<%

    //4.Encontramos el tipo de ventana del PopUp: NEW, EDIT, DETAIL
//-------------------------------------------------------------
    String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));

//5.Validaciones para todos los casos a nivel de EDI y DETAIL
    if( !type_window.equals("NEW") ){
        String[] paramNpobjitemheaderid = request.getParameterValues("a");
        String[] paramNpobjitemvalue    = request.getParameterValues("b");
        strSolution= null;
        int valorRespPago = 0;


        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolution = MiUtil.getString(paramNpobjitemvalue[i]);
            }else if( paramNpobjitemheaderid[i].equals("97") ) {
                valorRespPago = 1;
            }
            //CBARZOLA:obteniendo dato de Solucion Original Id  a través del header
            if( paramNpobjitemheaderid[i].equals("104") ) {
                strOriginalSolutionId =MiUtil.getString(MiUtil.getString(paramNpobjitemvalue[i]));
            }
        }
        if ( strSolution != null ){%>
<script>
    //fxShowContractFields("<%=strSolution%>");
    fxLoadServicesPlanFromSolutionEdit("<%=strSolution%>","<%=valorRespPago%>"  );
</script>
<%}

}
%>

<!--Empiezan Funciones en Javascript para el Objeto-->

<script>

    var vctSolution = new Vector();

    function fxMakeSolution(objSolutionid,objSolutionName){
        this.objSolutionid         = objSolutionid;
        this.objSolutionName       = objSolutionName;
    }
    /**********************************************************************************************/
    /* Motivo:Carga los datos de los Servicios Adicionales cuando se escoge el valor de la Solución.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /**********************************************************************************************/
    function fxLoadServicesPlanFromSolution(objValue){

        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var valueCurrent = objValue;
        var valorPlan = 0;
        var valorService = 0;
        var valorLinea = 0;
        var valorRespPago = 0

        if (trim(valueCurrent)!=""){
            var hdnSpecification = <%=hdnSpecification%>;
            var strCustomerId   = "<%=strCustomerId%>";
            if(<%=hdnSpecification%>==<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>)
            { //Limpia los valores si cambia la solucion
                fxDeleteObjectSolutionbySubcategory();
            }
            else
            { //Limpia los valores si cambia la solucion
                fxDeleteObjectSolution();
            }

            //Verificamos si los objetos Plan Tarifarios, Línea de Producto y Servicios existen para esta categoria:
            for( x = 0; x < vctItemOrder.size(); x++ ){
                objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
                if (objitemheaderid == 10) valorPlan = 1;
                if (objitemheaderid == 25) valorService = 1;
                if (objitemheaderid == 51) valorLinea = 1;
                if (objitemheaderid == 97) valorRespPago = 1;
            }
            var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getSoluctionPlanTypeList&strSpecificationId="+hdnSpecification+"&strSessionId=<%=strSessionId%>&strTypeCompany=<%=strTypeCompany%>&strSolutionId="+valueCurrent+"&service="+valorService+"&plan="+valorPlan+"&linea="+valorLinea+"&respPago="+valorRespPago+"&strCustomerId="+strCustomerId+"&strnpSite="+'<%=strnpSite%>';
            parent.bottomFrame.location.replace(url);
        }
        else{
            //Limpia los valores incluyendo la solución
            if(<%=hdnSpecification%>==<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>)
            {
                fxDeleteObjectSolutionbySubcategory();
            }
            else
            {
                fxDeleteObjectSolution();
            }
        }
    }

    /*******************************************************************************
     /* Motivo: Ejecuta la acción del Objeto Solución al setear otro valor del combo.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /*******************************************************************************/
    function fxExecuteSolution(objValue){
        //Ocultar Check Marcar SIM - vasp
        fxHideDivByHeaderId(151);

        try{
            var ItemLevel = parent.mainFrame.frmdatos.cmb_ItemNivel.value;
        }catch(e){}
        var valueCurrent = objValue;
        if (trim(valueCurrent)!=""){
            if (fxLoadSolutionValidate(objValue)){
                fxLoadServicesPlanFromSolution(objValue);
            }
        }
        else{
            //Limpia los valores si  la solucion es vacia
            //--------------------------------------------
            fxDeleteObjectSolution();
        }

        /*Inicio JPEREZ*/
        fxShowContractFields(objValue);
        /*Fin JPEREZ*/
        fxActionItem(objValue);
        try{
            parent.mainFrame.frmdatos.cmb_ItemNivel.value = ItemLevel;
        }catch(e){ }
        /*INICIO ADT-BLC-083 --LHUAPAYA*/
        if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_UPGRADE%>"  || "<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_DOWNGRADE%>"){
            var x=document.frmdatos.cmb_itemType;
            x.value = 1;
            x.disabled = true;
        }
        if("<%=hdnSpecification%>" == "<%=Constante.SPEC_BOLSA_DESACTIVAR%>"){
           var x=document.frmdatos.cmb_ItemModality;
            x.value = "Venta";
        }
        /*FIN ADT-BLC-083 --LHUAPAYA*/
    }

    /*******************************************************************************
     /* Motivo:Validamos que todos las soluciones del item pertenezcan al mismo tipo
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /*******************************************************************************/
    function  fxLoadSolutionValidate(objValue){

        var form = document.frmdatos;
        var vDoc = parent.mainFrame;
        var flagSolutionTypeNew='';
        var flagSolutionTypeOld='';
        var flagSolucion = '';
        var flagSolutionType = '';

        //Cuando es llamado desde un Incidente
        //------------------------------------
        if (parent.opener.items_table==null){
            flagSolucion = '';
            flagSolutionType = '';

        }else{

            <%
            //Verificamos si la categoría debe ser validada para el caso de Prepago o Postpago en el item
            //-------------------------------------------------------------------------------------------
            objHashMap = objGeneralService.getValueTag1(hdnSpecification,"VALIDATE_SPEC_TYPE_SOLUTION");
            if( objHashMap.get("strMessage")!= null )
               throw new Exception((String)objHashMap.get("strMessage"));
            strValidateSolution =( String)objHashMap.get("strTag1");

            if ( !strValidateSolution.equals("0") ){
            %>
            v_numRows = parent.opener.items_table.rows.length;

            //Hay mas de un item en la orden - Se toma la primera fila como guia
            //-------------------------------------------------------------------
            if (v_numRows > 2){
                flagSolucion = parent.opener.frmdatos.txtItemSolution[1].value;
                flagSolutionType = '';
            }else{
                if (v_numRows == 2){ // Cuando tiene un item.
                    flagSolucion = parent.opener.frmdatos.txtItemSolution.value;
                    flagSolutionType = '';
                    if (flagSolucion == undefined)  flagSolucion = '';
                }
                else{ // Cuando no tiene items.
                    flagSolucion = '';
                    flagSolutionType = '';
                }
            }
            <%}else{%>
            flagSolucion = '';
            flagSolutionType = '';
            <%}%>
        }


        //Encontramos el tipo de la solución que esta escogiendo en el combo cuando ya se se ha generado un item
        //------------------------------------------------------------------------------------------------------
        if( flagSolucion!='' ){

            <%
             objHashMap =objNewOrderService.getSolutionType();
             if( objHashMap.get("strMessage")!= null )
               throw new Exception((String)objHashMap.get("strMessage"));

                ArrayList objArrayList1 = (ArrayList)objHashMap.get("objArrayList");
             %>

            //Cuando la solución que se escoge en el combo es diferente de vacio
            //------------------------------------------------------------------
            if(objValue!=''){
                <%if (objArrayList1 != null && objArrayList1.size()>0){
                        for (int i=0; i<objArrayList1.size(); i++){
                          objSpecificationBean = new SpecificationBean();
                          objSpecificationBean = (SpecificationBean)objArrayList1.get(i);
                %>
                if (objValue == '<%=objSpecificationBean.getNpSolutionId()%>'){
                    flagSolutionTypeNew = '<%=objSpecificationBean.getNpSolutiontype()%>';
                }
                <%
                  }//for
                }//if
                %>
            }

            //Se encuentra la solución seleccionada en el primer item
            //-------------------------------------------------------
            <%if (objArrayList1 != null && objArrayList1.size()>0){
                  for (int i=0; i<objArrayList1.size(); i++){
                       objSpecificationBean = new SpecificationBean();
                      objSpecificationBean = (SpecificationBean)objArrayList1.get(i);
            %>
            if (flagSolucion == '<%=objSpecificationBean.getNpsolutionname()%>'){
                flagSolutionTypeOld = '<%=objSpecificationBean.getNpSolutiontype()%>';
            }
            <%
                  }//for
              }//if
            %>

            //Verificamos si los tipos de soluciones son iguales
            //--------------------------------------------------
            if(flagSolutionTypeNew!=flagSolutionTypeOld){
                alert("Debe ingresar una solución de tipo "+flagSolutionTypeOld+".");
                fxClearObjects("cmb_ItemSolution","SELECT");
                fxDeleteObjectSolution();
                return false;
            }
            else{
                return true;
            }
        }else{
            //Si no existen items no se realizar ninguna validación
            //------------------------------------------------------
            return true;
        }
    }

    /********************************************************************************************
     /* Motivo: Limpia  los valores de los objetos del item dependientes del combo Soluci?n cuando este cambie.
     /* Realizado por:  Cesar Barzola
     /* Fecha: 12/06/2009 */
    /********************************************************************************************/
    function fxDeleteObjectSolutionbySubcategory(){
        //declaracion de constantes
        var telefonoObjItemId=2;
        var devolucionObjectItemId=11;
        var fechDevolucionObjItemId=13;
        var solucionObjItemId=81;
        var serviciosAdHeaderItemId=25;
        var planTarifObjItemId=9;
        var precInscripObjItemId=17;
        var rentaObjItemId=19;

        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;
        var nameHtmlControl      = "";
        var mandatoryControl     = "";
        var datatypeControl      = "";
        var valueControl         = "";
        var valueDisplay         = "";
        var readOnly             ="";


        //Verificar si tiene campos mandatorios
        for( x = 0; x < vctItemOrder.size(); x++ ){
            nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
            objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
            objitemid         = vctItemOrder.elementAt(x).npobjitemid;
            valueDisplay      = vctItemOrder.elementAt(x).npdisplay;
            controlType       = vctItemOrder.elementAt(x).npcontroltype;
            readOnly          = vctItemOrder.elementAt(x).npobjreadonly;
            //HACER UNA NUEVA FUNCION PARECIDA PERO QUE LIMPIE SOLO LOS QUE ESTAN CON READONLY 'S'

            if(readOnly == "S"){
                if(objitemid==planTarifObjItemId||objitemid==precInscripObjItemId||objitemid==rentaObjItemId){
                    if (controlType=="TEXT") {
                        vDoc.fxObjectConvert(nameHtmlControl,'');
                    }
                    if(controlType=="SELECT"){
                        fxClearObjectsSolution(nameHtmlControl,"SELECT");
                    }
                }
            }
            if(readOnly == "N"){ //No son readonly
                if(objitemid!=telefonoObjItemId && objitemid!=devolucionObjectItemId &&
                        objitemid!=fechDevolucionObjItemId && objitemid!=solucionObjItemId)
                {
                    if("<%=hdnSpecification%>" == 2065 && objitemheaderid == 57){
                        continue;
                    }

                    if (controlType=="TEXT") {
                        vDoc.fxObjectConvert(nameHtmlControl,'');
                    }
                    if(controlType=="SELECT"){
                        fxClearObjectsSolution(nameHtmlControl,"SELECT");
                    }
                    if(controlType=="OTRO"){
                        if (objitemheaderid==serviciosAdHeaderItemId){
                            //vasp01

                            var bypass="";
                            if("<%=hdnSpecification%>" == "2065" && form.txt_ItemOriginalSolutionId.value == form.cmb_ItemSolution.value) {

                                bypass="true";

                            }else{
                            vDoc.vServicio.removeElementAll();
                            vDoc.vServicioPorDefecto.removeElementAll();
                            vDoc.fxLoadServiceAditiional();
                            try{
                                vDoc.fxCargaServiciosItem("");
                                } catch (e) {
                                }
                            try{
                                vDoc.fxCargaServiciosPorDefecto();
                                } catch (e) {
                                }

                            }

                        }
                    }
                }
            }

        }

    }

    /********************************************************************************************
     /* Motivo: Limpia todos los valores de los objetos del item cuando el combo Solución es vacío.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /********************************************************************************************/
    function fxDeleteObjectSolution(){

        var form = parent.mainFrame.frmdatos;
        var vDoc = parent.mainFrame;

        var nameHtmlControl      = "";
        var mandatoryControl     = "";
        var datatypeControl      = "";
        var valueControl         = "";
        var valueDisplay         = "";

        vDoc.DeleteSelectOptions(form.cmb_ItemRespPago);

        //Verificar si tiene campos mandatorios
        for( x = 0; x < vctItemOrder.size(); x++ ){
            nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
            objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
            valueDisplay      = vctItemOrder.elementAt(x).npdisplay;
            controlType       = vctItemOrder.elementAt(x).npcontroltype;
            objreadonly       = vctItemOrder.elementAt(x).npobjreadonly;

            if( valueDisplay == "S" ){
                if( objitemheaderid!=21  && objitemheaderid!=25 && objitemheaderid!=47 &&  objitemheaderid!=10 && objitemheaderid!=31 && objitemheaderid!=79 && objitemheaderid!=73 && objitemheaderid!=86){
                    if (controlType=="TEXT") {
                        vDoc.fxObjectConvert(nameHtmlControl,'');
                    }
                    else{
                        if(objitemheaderid!=93 && objitemheaderid!=110 ){
                            fxClearObjectsSolution(nameHtmlControl,"SELECT");
                        }
                    }
                }else{
                    if (objitemheaderid==10){
                        parent.mainFrame.DeleteSelectOptions(form.cmb_ItemPlanTarifario)
                    }
                    if (objitemheaderid==25){
                        vDoc.vServicio.removeElementAll();
                        vDoc.vServicioPorDefecto.removeElementAll();
                        vDoc.fxLoadServiceAditiional();
                        try{
                            vDoc.fxCargaServiciosItem("");
                        }catch(e){}
                        try{
                            vDoc.fxCargaServiciosPorDefecto();
                        }catch(e){}
                    }
                }
            }
        }
    }

    /***********************************************************************************************************************
     /* Motivo: Carga los valores que se visualizaran en el combo de la Solución, esta función se invoca desde el PopUpOrder.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /***********************************************************************************************************************/
    function fxOnLoadListSolution(ObjectSolution){

        formCurrent = parent.mainFrame.frmdatos;
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemSolution);
        parent.mainFrame.vctSolution.removeElementAll();

        <%
        //Encontramos la data para la solución
        //-------------------------------------
        objHashMap = objNewOrderService.getSolutionSpecificationList(MiUtil.parseLong(hdnSpecification),MiUtil.parseLong(strDivisionId), MiUtil.parseLong(strnpSite));
        if( objHashMap.get("strMessage")!= null )
             throw new Exception((String)objHashMap.get("strMessage"));

        //Recogemos los registros que llenaran los datos en el combo Solución en un arreglo
        //---------------------------------------------------------------------------------
          objArrayList = (ArrayList)objHashMap.get("objArrayList");

        //Llenamos los valores en el combo Solución
        //-----------------------------------------
        if (objArrayList != null && objArrayList.size()>0){

          for (int i=0; i<objArrayList.size(); i++){
              objSpecificationBean = new SpecificationBean();
              objSpecificationBean = (SpecificationBean )objArrayList.get(i);
          %>
        if (ObjectSolution=='' || ObjectSolution =='<%=objSpecificationBean.getNpSolutiontype()%>'){

            parent.mainFrame.vctSolution.addElement(new parent.mainFrame.fxMakeSolution('<%=objSpecificationBean.getNpSolutionId()%>','<%=objSpecificationBean.getNpsolutionname()%>'));
            parent.mainFrame.AddNewOption(formCurrent.cmb_ItemSolution,'<%=objSpecificationBean.getNpSolutionId()%>','<%=MiUtil.getMessageClean(objSpecificationBean.getNpsolutionname())%>');
        }
        <%
          }//end del for
        }//end del if

       //Encontramos el valor por defecto de la solución
        //-----------------------------------------------
        objHashMap = objGeneralService.getValueTag1(hdnSpecification,"SPECIFICATION_SOLUTION_DEFAULT");
          if( objHashMap.get("strMessage")!= null )
            throw new Exception((String)objHashMap.get("strMessage"));
        strSolution =( String)objHashMap.get("strTag1");
        if ( (!strSolution.equals("0")) && (type_window.equals("NEW")) ){
        %>
        parent.mainFrame.fxObjectConvert('cmb_ItemSolution','<%=strSolution%>');

        //Se invoca a la función que carga Planes y Servicios
        //---------------------------------------------------
        fxLoadServicesPlanFromSolution('<%=strSolution%>');

        <%}%>
        <%
          //CBARZOLA:Cambio para que liste las soluciones para cambio de modelo distintas tecnologias
          HashMap hshStatus=objGeneralService.getStatusByTable(Constante.NPT_VALIDATE_SOLUTION,hdnSpecification);
            if( hshStatus.get("strMessage")!= null ){%>
        alert("<%=hshStatus.get("strMessage")%>");
        <%}
        String strStatus=(String)hshStatus.get("strStatus");
        if(strStatus.equals("1")) {
           // if(MiUtil.parseLong(hdnSpecification)==Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS){
        %>
        fxLoadSolutionbySubMarket();
        <%
           // }
          }
        %>

    }


    /************************************************************************************
     /* Motivo: Función Genral que ayudan a limpiar de valores todos los objetos del item.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /************************************************************************************/
    function fxClearObjectsSolution(objObject,typeObject){
        form = parent.mainFrame.frmdatos;
        try{
            if( typeObject == "TEXT" )
                eval("form."+objObject+".value=''");
            else
                eval("form."+objObject+".value=''");
        }catch(e){

        }
    }

    //INCIO Telefonia fija
    function fxActionItem(objValue){
        try{
            <%if( MiUtil.getString(hdnSpecification).equals("2049") ){%>//Servicios Adicionales BA - Cambio de plan
            if (objValue == '6'){//Telefonia fija
                fxShowDivByHeaderId(94);
                fxHideDivByHeaderId(27);
                fxHideDivByHeaderId(82);
                fxHideDivByHeaderId(83);
            }else{
                fxHideDivByHeaderId(94);
                fxShowDivByHeaderId(27);
                fxShowDivByHeaderId(82);
                fxShowDivByHeaderId(83);
            }
            <%}%>
            <%if( MiUtil.getString(hdnSpecification).equals("2048") ){%>//Servicios Adicionales BA - Activar & Desactivar Servicios
            if (objValue == '6'){//Telefonia fija
                fxShowDivByHeaderId(94);
                fxHideDivByHeaderId(27);
                fxHideDivByHeaderId(82);
            }else{
                fxHideDivByHeaderId(94);
                fxShowDivByHeaderId(27);
                fxShowDivByHeaderId(82);
            }
            <%}%>
            <%if( MiUtil.getString(hdnSpecification).equals("2047") ){%>//Reconexion BA - Reconexion
            if (objValue == '6'){//Telefonia fija
                fxShowDivByHeaderId(94);
                fxHideDivByHeaderId(27);
                fxHideDivByHeaderId(82);
            }else{
                fxHideDivByHeaderId(94);
                fxShowDivByHeaderId(27);
                fxShowDivByHeaderId(82);
            }
            <%}%>
        }catch(e){}
    }

    function fxHideDivByHeaderId(headerId){
        try{
            eval("idDisplay"+headerId+".style.display = 'none';");
        }catch(e){}
    }

    function fxShowDivByHeaderId(headerId){
        try{
            eval("idDisplay"+headerId+".style.display = '';");
        }catch(e){}
    }

    function fxLoadFixedPhone(){
        var objValue = parent.mainFrame.frmdatos.cmb_ItemSolution.value;
        fxActionItem(objValue);
    }
    //FIN Telefonia fija
    /************************************************************************************
     /* Motivo: Funci?n que permite el listado de las soluciones para un determinado SubMarket.
     /* Realizado por:  Cesar Barzola
     /* Fecha: 12/06/2009 */
    /************************************************************************************/
    function fxLoadSolutionbySubMarket(){
        DeleteSelectOptions(form.cmb_ItemSolution);
        <%
          HashMap hshOtherSolutions =objNewOrderService.getOtherSolutionsbySubMarket(MiUtil.parseLong(hdnSpecification),MiUtil.parseLong(strOriginalSolutionId));
          if(hshOtherSolutions.get("strMessage")!= null){%>
        alert("<%=hshOtherSolutions.get("strMessage")%>");
        <%}
        ArrayList listSolutions=(ArrayList)hshOtherSolutions.get("objArrayList");
          if(listSolutions!=null){
               for( int i=0; i<listSolutions.size();i++ ){
                SolutionBean objsolution= new SolutionBean();
                objsolution= (SolutionBean)listSolutions.get(i);
                %>
        AddNewOption(form.cmb_ItemSolution,'<%=MiUtil.getString(objsolution.getNpsolutionid())%>','<%=objsolution.getNpsolutionname()%>');
        <%}%>
        <%}%>
        SetCmbDefaultValue(form.cmb_ItemSolution,'<%=strOriginalSolutionId%>');
    }
</script>



<select name="<%=nameHtmlItem%>" onChange="javascript:fxExecuteSolution(this.value);" >
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
</select>
