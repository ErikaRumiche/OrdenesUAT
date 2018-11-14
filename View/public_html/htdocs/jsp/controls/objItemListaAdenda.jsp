<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.bean.TemplateAdendumBean" %>
<%@ page import="pe.com.nextel.service.ItemService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>

<%
    try{
        String strSpecificationId = (String)request.getParameter("strSpecificationId");
        String strOrderId         = (String)request.getParameter("strOrderId");
        String strItemId          = (String)request.getAttribute("strItemId");
        String type_window        = (String)request.getAttribute("type_window");
        String strOrderPage       = (String)request.getParameter("strOrderPage");
        String strSessionId       = (String)request.getParameter("strSessionId");

        //System.out.println("****** entro a adendas ***** ");
        //System.out.println("[objItemListaAdenda.jsp] hdnSpecification = "+strSpecificationId);

        //Obtiene si la especificación puede crear adendas.
        ItemService itemService = new ItemService();
        GeneralService objGeneralService = new GeneralService();
        String flagAdendam = itemService.OrderDAOgetNpAllowAdenda(MiUtil.parseInt(strSpecificationId));


        //System.out.println("[objItemListaAdenda.jsp] flagAdendam = " + flagAdendam);

        int flagAdendas         = 0;
        int iUserId             = 0;
        int iAppId              = 0;
        String strMessage       = null;
        String strFlagTipoAdend = null;

        //Se obtiene el id del usuario y de la aplicacion
        PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        iUserId = objSessionBean.getUserid();
        iAppId  = objSessionBean.getAppId();

        // Se obtiene el rol que indica si el usuario puede editar el plazo.
        HashMap hshScreenOptions = new HashMap();
        hshScreenOptions = objGeneralService.getRol(Constante.SCRN_OPTTO_ADDENDUM_TERM, iUserId, iAppId);
        strMessage = (String)hshScreenOptions.get("strMessage");
        if (strMessage != null)
            throw new Exception(strMessage);

        //Si  iFlagCarrier = 1 puede editar el plazo
        int iFlagCarrier = MiUtil.parseInt((String)hshScreenOptions.get("iRetorno"));

        //Si la especificación puede crear adenda
        if (("S").equals(flagAdendam)){
            String paramNpobjitemheaderid [] = (String [])request.getParameterValues("a");
            String paramNpobjitemvalue []    = (String [])request.getParameterValues("b");
            String cadena    = ""; // Lista de id's de plantillas de adendas seleccionadas
            String plan      = "";
            String promocion = "";
            String telefono  = "";
            String producto  = "";


            ItemService objItemServiceTransaction = new ItemService();
            HashMap objHashMap = new HashMap();
            ArrayList list = new ArrayList();

            boolean estadoPlan = false;
            // Si es edición del item
            if (paramNpobjitemvalue!= null){
                for (int i = 0; i < paramNpobjitemheaderid.length ; i++){
                    //System.out.println("i: " + i);

                    if( paramNpobjitemheaderid[i] != null ){
                        //System.out.println("paramNpobjitemheaderid[i]: " + paramNpobjitemheaderid[i]);
                        //System.out.println("paramNpobjitemvalue[i]: " + paramNpobjitemvalue[i]);

                        if (("47").equals(paramNpobjitemheaderid[i])){ // Template ID
                            cadena = paramNpobjitemvalue[i];
                            //System.out.println("[objItemListaAdenda.jsp] cadena: " + cadena);
                        }
                        if (("55").equals(paramNpobjitemheaderid[i])){ // Promoción ID
                            promocion = paramNpobjitemvalue[i];
                            //System.out.println("[objItemListaAdenda.jsp] promocion: "+ promocion);
                        }
                        if(("3").equals(paramNpobjitemheaderid[i])){ // Telefono
                            telefono = paramNpobjitemvalue[i];
                            //System.out.println("[objItemListaAdenda.jsp] telefono: "+ telefono);
                        }
                        if (("10").equals(paramNpobjitemheaderid[i])){ // Plan Tarifario
                            plan = paramNpobjitemvalue[i];
                            //System.out.println("[objItemListaAdenda.jsp] plan: "+ plan);
                        }
                        if (("9").equals(paramNpobjitemheaderid[i])){ // Producto
                            producto = paramNpobjitemvalue[i];
                            //System.out.println("[objItemListaAdenda.jsp] producto: "+ producto);
                        }
                        if (("10").equals(paramNpobjitemheaderid[i])){ // Plan Tarifario
                            plan = paramNpobjitemvalue[i];
                            estadoPlan = true;
                            //System.out.println("[objItemListaAdenda.jsp] plan: "+ plan);
                        }

                        if(estadoPlan == false) {
                            if (("57").equals(paramNpobjitemheaderid[i])){ // Plan Tarifario
                                plan = paramNpobjitemvalue[i];
                                //System.out.println("[objItemListaAdenda.jsp] plan: "+ plan);
                            }
                        }

                    }
                }

                String [] cadenas = cadena.split(";",cadena.split(";").length);

                //System.out.println("[ItemServlet.doListarPlantillasEdit] cadena = " + cadena);

                /********* adendas segun plan y promocion *****/
                int idProm = MiUtil.parseInt(promocion);
                int idPlan = MiUtil.parseInt(plan);
                int idSpecification = MiUtil.parseInt(strSpecificationId);
                int idKit           = MiUtil.parseInt(producto);
                //HashMap objHashMap = new HashMap();

                // Obtiene las plantillas de adendas, asociadas al plan y/o a la promocion
                objHashMap = objItemServiceTransaction.OrderDAOgetAddendasList( idProm, idPlan, idSpecification, idKit);

                strMessage = "";
                //ArrayList list = new ArrayList();
                strMessage = (String) objHashMap.get("strMessage");
                if (strMessage != null)
                    throw new Exception(strMessage);

                list = (ArrayList)objHashMap.get("objArrayList");
                TemplateAdendumBean templateAdendumBean = null;

                //System.out.println("type_window" + type_window);
                //System.out.println("[ItemServlet.doListarPlantillasEdit] cadena = " + cadena);

                if(strMessage==null){

                    ArrayList listEdit = new ArrayList();

                    // Si la lista de id's de plantilla NO esta vacía.
                    if( !(("").equals(cadena))){
                        for(int i = 0; i < list.size() ; i++ ){
                            templateAdendumBean = new TemplateAdendumBean();
                            templateAdendumBean = (TemplateAdendumBean)list.get(i);

                            if(("EDIT").equals(type_window)||("DETAIL").equals(type_window)){
                                templateAdendumBean.setNptemplatedefa("N");
                            }

                            for(int j=0 ;j < cadenas.length ; j++){
                                String [] template = cadenas[j].split("-",cadenas[j].split("-").length);
                                //template[0] : Id de la plantilla
                                //template[1] : PLazo
                                //System.out.println("[ItemServlet.doListarPlantillasEdit] template.0 = " + template[0]);
                                //System.out.println("[ItemServlet.doListarPlantillasEdit] template.1 = " + template[1]);

                                // Se sacan el ultimo ";"
                                template[1] = template[1].replaceAll(";","");
                                //System.out.println("[ItemServlet.doListarPlantillasEdit] template.1 = " + template[1]);

                                if(templateAdendumBean.getNptemplateid() == MiUtil.parseInt(template[0])){
                                    templateAdendumBean.setNpaddendumterm(MiUtil.parseInt(template[1]));
                                    templateAdendumBean.setNptemplatedefa("S");

                                    //System.out.println("[ItemServlet.doListarPlantillasEdit]  id = "+template[0]);
                                    //System.out.println("[ItemServlet.doListarPlantillasEdit]  desc = "+templateAdendumBean.getNptemplatedesc());
                                }
                            }
                            listEdit.add(templateAdendumBean);
                        }

                        request.setAttribute("tblListAdendum", listEdit);

                    }
                    // Si la lista de id's de plantilla SI está vacía
                    else{
                        if(("EDIT").equals(type_window) || ("DETAIL").equals(type_window)) {
                            for(int i = 0; i < list.size() ; i++ ){
                                templateAdendumBean = new TemplateAdendumBean();
                                templateAdendumBean = (TemplateAdendumBean)list.get(i);
                                templateAdendumBean.setNptemplatedefa("N");
                            }
                        }
                        request.setAttribute("tblListAdendum", list);
                    }

                    //System.out.println("type_window:" + type_window);
                }
            }
            // Cuando se crea un item
            else{
                //System.out.println("****entro a crear un item****");

                int idProm = 0;
                int idPlan = 0;
                int idSpecification = 0;
                int idKit           = 0;
                strMessage = null;

                // Obtiene el listado de plantillas
                objHashMap = objItemServiceTransaction.OrderDAOgetAddendasList( idProm, idPlan, idSpecification, idKit);
                strMessage = (String) objHashMap.get("strMessage");
                if(strMessage!=null)
                    throw new Exception(MiUtil.getMessageClean(strMessage));

                list = (ArrayList)objHashMap.get("objArrayList");
                flagAdendas = 1;
                request.setAttribute("tblListAdendum", list);
            }%>

<script>
    var cadena;

    function agregar(objChk){   // Agrega cuando hace check
        cadena = "";
        var checkSelects = form.checkSelec;
        var plazo = form.txtAddendumTerm;
        var cont = 0;
        var i;

        for (i= 0; i<checkSelects.length;i++){
            if(checkSelects[i].checked == true){
                cont++;
                cadena = cadena + checkSelects[i].value + "-" + plazo[i].value + ";" ;
            }
        }

        //alert(" 1 seleccionados = " +cont+" cadena = "+ cadena);
        form.hdn_ListAdenda.value = cadena;
        //alert(form.hdn_ListAdenda.value);
    }

    function agregar(){   // Agrega cuando cambia el plazo
        //alert("entra 2");
        cadena = "";
        var checkSelects = form.checkSelec;
        var plazo = form.txtAddendumTerm;

        var cont = 0;
        var i;
        //alert(checkSelects.length);
        for (i= 0; i<checkSelects.length;i++){
            if(checkSelects[i].checked == true){
                cont++;
                cadena = cadena + checkSelects[i].value + "-" + plazo[i].value + ";" ;
            }
        }
        //alert(" 2 seleccionados = " +cont+" cadena = "+ cadena);
        form.hdn_ListAdenda.value = cadena;
    }

    function fxOnlyNumber(evt){
        // NOTA: '0' = 48, '9' = 57
        var vKey = vValor ? evt.which : evt.keyCode;
        return (vKey <= 13 || (vKey >= 48 && vKey <= 57));
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

    function getTipoAdenda(obj){
        <%if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){%>
        var phone = form.txt_ItemPhone.value;
        if (phone == ""){
            alert("Debe ingresar el número de teléfono.");
            obj.checked = false;
            return;
        }

        if(obj.checked){
            var iTemplateId = obj.value;
            var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getTipoPlantillaAdenda&strNumeroNextel="+phone+"&iTemplateId="+iTemplateId+"&txtToSet="+"flagAdenda"+obj.value;
            parent.bottomFrame.location.replace(url);
        }else{
            document.getElementById("flagAdenda"+obj.value).innerText = '';
        }
        <%}else{%>
        if(obj.checked){
            document.getElementById("flagAdenda"+obj.value).innerText = '<%=Constante.TIPO_PLANTILLA_NUEVA%>';
            document.getElementById("flagAdenda"+obj.value).className = 'TemplateLabelNew';
        }else{
            document.getElementById("flagAdenda"+obj.value).innerText = '';
        }
        <%}%>
    }

    function loadTipoAdenda(templateId,flag,activate){
        if(flag == 2){
            document.getElementById("flagAdenda"+templateId).innerText = '<%=Constante.TIPO_PLANTILLA_NUEVA%>';
            document.getElementById("flagAdenda"+templateId).className = 'TemplateLabelNew';
        }

        if(flag == 1){
            if(activate == "0"){
                document.getElementById("flagAdenda"+templateId).innerText = '<%=Constante.TIPO_PLANTILLA_NUEVA%>';
                document.getElementById("flagAdenda"+templateId).className = 'TemplateLabelNew';
            }else{
                document.getElementById("flagAdenda"+templateId).innerText = '<%=Constante.TIPO_PLANTILLA_RENOVACION%>';
                document.getElementById("flagAdenda"+templateId).className = 'TemplateLabelRenov';
            }
        }
    }
</script>

<script>
    function fxOnLoadListAddendum(){ //alert("fxOnLoadListAddendum");
        <% ItemService objItemService = new ItemService();
           String disabled="";
           Integer checked = 0;
           ArrayList listAddendum = (ArrayList) request.getAttribute("tblListAdendum");
           Integer flagAdendaDisabled = Integer.parseInt(itemService.OrderDAOgetValidationAdenda(MiUtil.parseInt(strSpecificationId)));

           if (listAddendum != null){

              Iterator iterator = listAddendum.iterator();
              TemplateAdendumBean templateAdendumBean = null;

              Iterator iterator2 = listAddendum.iterator();
              TemplateAdendumBean templateAdendumBean2 = null;

              if(flagAdendaDisabled==1 && !promocion.equalsIgnoreCase("")){
                   while(iterator2.hasNext()){
                      templateAdendumBean2 = new TemplateAdendumBean();
                      templateAdendumBean2 = (TemplateAdendumBean) iterator2.next();
                      if (("S").equals(templateAdendumBean2.getNptemplatedefa())){
                          checked=checked+1;
                      }
                   }
                   if(checked>0){
                       disabled="disabled";
                   }

               }


              while(iterator.hasNext()){
                 templateAdendumBean = new TemplateAdendumBean();
                 templateAdendumBean = (TemplateAdendumBean) iterator.next();


                 /*System.out.println("[objItemListaAdenda.fxOnLoadListAddendum] templateid " + templateAdendumBean.getNptemplateid());
                 System.out.println("[objItemListaAdenda.fxOnLoadListAddendum] templatedefa " + templateAdendumBean.getNptemplatedefa());
                 System.out.println("[objItemListaAdenda.fxOnLoadListAddendum] templatedesc " + templateAdendumBean.getNptemplatedesc());*/

                 String flag = "0"; %>

        row = parent.mainFrame.itemsTemplate.insertRow(-1);
        col = row.insertCell(0);
        col.className = 'CellContent';
        col.align     = 'center';

        <%  if (("S").equals(templateAdendumBean.getNptemplatedefa())){
             if(("DETAIL").equals(type_window)){   %>

        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" checked="true" style="text-align:center" disabled="true" onClick="javascript:agregar(this)"  >';
        <%

           if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){
              HashMap hshDataMap = objItemService.getTipoPlantillaAdenda(telefono,templateAdendumBean.getNptemplateid());
              strMessage         = (String)hshDataMap.get("strMessage");
              strFlagTipoAdend   = (String)hshDataMap.get("strFlagTipoAdend");
              flag = "1";
           }
           else{
              flag = "2";
           }
}
else{ %>

        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" checked="true" style="text-align:center" onClick="javascript:agregar(this);getTipoAdenda(this)" <%=disabled%>  >';
        <% if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){
              HashMap hshDataMap = objItemService.getTipoPlantillaAdenda(telefono,templateAdendumBean.getNptemplateid());
              strMessage         = (String)hshDataMap.get("strMessage");
              strFlagTipoAdend   = (String)hshDataMap.get("strFlagTipoAdend");
              flag = "1";
           }
           else{
              flag = "2";
           }
}
}

if (!(("S").equals(templateAdendumBean.getNptemplatedefa()))){

if(("DETAIL").equals(type_window)){ %>

        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" style="text-align:center" disabled="true" onClick="javascript:agregar(this)" >';
        <% }
           else{ %>

        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" style="text-align:center" onClick="javascript:agregar(this);getTipoAdenda(this)" <%=disabled%> >';
        <% }
        }

        HashMap hshData = null;
        strMessage = null;  %>

        col.innerHTML = col.innerHTML;

        col = row.insertCell(1);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtDescTemplate size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=templateAdendumBean.getNptemplatedesc()%>';

        <% hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpaddendtype(), "TIPOADENDA");
           strMessage=(String)hshData.get("strMessage");
           if (strMessage!=null)
              throw new Exception(strMessage);

           String strTipoAdenda=(String)hshData.get("strDescription"); %>

        col = row.insertCell(2);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTypeAdendum size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=strTipoAdenda%>';

        col = row.insertCell(3);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtDateCreated size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=templateAdendumBean.getNpcreateddate()%>';

        <%  hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpbenefittype(), "BENEFICIO");
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
               throw new Exception(strMessage); 
            String strBeneficioAdenda=(String)hshData.get("strDescription");  %>

        col = row.insertCell(4);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTypeBenefit size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=strBeneficioAdenda%>';

        col = row.insertCell(5);
        col.className = 'CellContent';
        col.align     = 'center';

        <%  if(("DETAIL").equals(type_window)){ %>
        col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" disabled="true" onkeyup="javascript: agregar(form.checkSelec);" >';
        <%  }
            else{
               if(iFlagCarrier == 1){ %>
        col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" onkeyup="javascript: agregar(form.checkSelec);" onKeyPress="return fxOnlyNumber(event);">';
        <% }
           else{ %>
        col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" readonly onkeyup="javascript: agregar(form.checkSelec);" >';
        <% }
        } %>

        col.innerHTML = col.innerHTML;

        col = row.insertCell(6);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTipoAden size=10 maxlength=15 style="text-align:center">';
        col.id='flagAdenda<%=templateAdendumBean.getNptemplateid()%>';

        <% if(flag.equals("1")){ %>
        loadTipoAdenda('<%=templateAdendumBean.getNptemplateid()%>',1,'<%=strFlagTipoAdend%>');
        <% }
           if(flag.equals("2")){%>
        loadTipoAdenda('<%=templateAdendumBean.getNptemplateid()%>',2,'<%=strFlagTipoAdend%>');
        <%  }%>
        <% }
        } %>

        var v_templateids = "";
        var checkSelecs   = document.frmdatos.checkSelec;
        var plazo         = document.frmdatos.txtAddendumTerm;
        var k;

        for (k = 0; k < checkSelecs.length; k++)
            if(checkSelecs[k].checked == true)
                v_templateids = v_templateids + checkSelecs[k].value + "-" + plazo[k].value + ";" ;

        document.frmdatos.hdn_ListAdenda.value = v_templateids;
    }

</script>

<td colspan="2"  >

    <BR>
    <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraPlantillasAdendun">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
            <td class="SubSectionTitle" align="left" valign="top">Plantilla para Adendas</td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
        </tr>
    </table>
    <table align=center width="100%" border="0" cellpadding="0" cellspacing="1" name="itemsTemplate" id="itemsTemplate"  class="RegionBorder">
        <tr>
            <td class="CellLabel" align="center" >&nbsp;</td>
            <td class="CellLabel" align="center" >Descripci&oacute;n Plantilla</td>
            <td class="CellLabel" align="center" >Tipo de Adendas</td>
            <td class="CellLabel" align="center" >Fecha Creaci&oacute;n</td>
            <td class="CellLabel" align="center" >Tipo de Beneficio</td>
            <td class="CellLabel" align="center" >Plazo</td>
            <td class="CellLabel" align="center" >Tipo</td>
        </tr>

    </table>
    <P>&nbsp;</P>
    <%  } %>
    <input type="hidden" name="hdn_ListAdenda">
    <input type="hidden" name="checkSelec">
    <input type="hidden" name="txtAddendumTerm">

</td>
<% if (flagAdendas == 1){%>
<script>
    fxOnLoadListAddendum();
</script>
<%}%>
<%}catch(Exception  ex){
    System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage()));
}%>