<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.bean.ServiciosBean" %>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.GeneralService" %>
<%@page import="pe.com.nextel.bean.TableBean" %>

<%
  HashMap hshServiceRentList = (HashMap)request.getAttribute("hshServiceRentList");
  String strSessionId = null;
  String hdnSpecification = (String)request.getAttribute("hdnSpecification");
  
  
  if( hshServiceRentList.get("strMessage") != null )
      throw new Exception(MiUtil.getMessageClean((String)hshServiceRentList.get("strMessage")));
      
  strSessionId            =   (String)request.getAttribute("strSessionId");
  
  ArrayList objServiceRent = (ArrayList)hshServiceRentList.get("objServiciosBean");
  
  
  
   //Valores Principales
  //--------------------
  String strSpecificationId = (String)request.getAttribute("strSpecificationId");
  
  String strPermission_alq = (String)request.getAttribute("strPermission_alq");
  String strPermission_msj = (String)request.getAttribute("strPermission_msj");
    
  System.out.println("strPermission_alq:"+strPermission_alq);
  System.out.println("strPermission_msj:"+strPermission_msj);

  
  
  System.out.println("loadServiceList.jsp>>>>>>>>>>><<");
  System.out.println("strSessionId:"+strSessionId);
  System.out.println("strSpecificationId:"+strSpecificationId);
 
  //Listado de Servicios x Defecto
  //------------------------------
  HashMap hServiceDefaultList     = (HashMap)request.getAttribute("hServiceDefaultList");
  ArrayList objArrayListServiceDefault = new ArrayList();
  if (hServiceDefaultList!=null){
    objArrayListServiceDefault = (ArrayList)hServiceDefaultList.get("objServiceDefaultList");
  }
  
 ServiciosBean serviciosBeanAdicionales     = (ServiciosBean)request.getAttribute("serviciosBean");
 
 //Inicio SSAA por Defecto - 23/11/2011 - Frank Picoy
 ArrayList objArrayListServiceDefaultByPlan = new ArrayList();
 String strSSAAByDefault = (String)request.getAttribute("strSSAAByDefault");
 String strSSAAByDefWithLSA = (String)request.getAttribute("strSSAAByDefWithLSA");
 if (Constante.SPECIFICATION_TO_SSAA_DEFAULT_YES.equals(strSSAAByDefault)) {
   HashMap hshServiceDefaultListByPlan     = (HashMap)request.getAttribute("hshServiceDefaultListByPlan");
   objArrayListServiceDefaultByPlan = new ArrayList();
   if (hshServiceDefaultListByPlan!=null){
     objArrayListServiceDefaultByPlan = (ArrayList)hshServiceDefaultListByPlan.get("objArrayList");
   }
 }
 //Fin SSAA por Defecto - 23/11/2011 - Frank Picoy
 GeneralService objGeneralService = new GeneralService();
 HashMap hNpTableList  = objGeneralService.getValueNpTable("FLAG_LOG_ITEM_CAMBIO_MODELO");
 ArrayList objNpTableList =(ArrayList)hNpTableList.get("objArrayList");
 String  strflagLogItem= "I";
 TableBean nptableBean = null;
 if (objNpTableList.size()>0) {
     nptableBean = (TableBean)objNpTableList.get(0);
     strflagLogItem = nptableBean.getNpValue().trim();
     System.out.println("El strflagLogItem tiene---->" + strflagLogItem);
 }
 String    type_window =  MiUtil.getString((String)request.getAttribute("strTypeEvent"));
 System.out.println("El type_window en loadServiceList.jsp es--->" + type_window);
 
 String strMessageEmployeeValid = request.getAttribute("strMessageEmployeeValid")!=null?(String)request.getAttribute("strMessageEmployeeValid"):""; //SAR N_O000012485 - FPICOY - 09/01/2014
 
 %>

<html>
  <head>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
    <script language="javascript">
    
    function fxLoadServiceRentList(){
    
    try{
      parent.mainFrame.DeleteSelectOptions(parent.mainFrame.frmdatos.cmb_ItemNewMainService);
    <%  
     for(int i=0;i<objServiceRent.size();i++){
        ServiciosBean  objServiciosBean =(ServiciosBean)objServiceRent.get(i);
    %>
      
      parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemNewMainService,'<%=objServiciosBean.getNpservicioid()%>','<%=objServiciosBean.getNpnomserv()%>');
      
    <%}%>
      }catch(e){}
    }
    //CBARZOLA 
  //Función para cargar el vector con los servicios Core  
  //según plan Tarifario
  function fxLoadServiceComercialCore (){
    parent.mainFrame.vServiciosComercialesCore.removeElementAll();
    <%  
    NewOrderService objNewOrderService = new NewOrderService(); 
    String strplanId=(String)request.getParameter("cmb_ItemPlanTarifario");
      System.out.println("[loadServicesList.jsp][fxLoadServiceComercialCore]strplanId----->"+strplanId);
      HashMap hshCoreService    = objNewOrderService.ServiceDAOgetCoreServicebyPlan(MiUtil.parseLong(strplanId));
      ServiciosBean objServiciosBean = null;      
      ArrayList listServices =(ArrayList)hshCoreService.get("objServiciosBean");
      String strMessageService = (String)hshCoreService.get("strMessage");         
        if (strMessageService==null){
           System.out.println("[loadSL]size:"+listServices.size());
           for(int i=0;i<listServices.size();i++){
            objServiciosBean = (ServiciosBean)listServices.get(i);
           System.out.println("[loadSL.jsp]getNpservicioid:"+objServiciosBean.getNpservicioid()+"getNpnomserv:"+objServiciosBean.getNpnomserv()+"getNpnomcorserv:"+objServiciosBean.getNpnomcorserv()+"objServiciosBean:"+objServiciosBean.getNpexcludingind());
      %>           
            parent.mainFrame.vServiciosComercialesCore.addElement(new Servicio("<%=objServiciosBean.getNpservicioid()%>","<%=objServiciosBean.getNpnomserv()%>","<%=objServiciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(objServiciosBean.getNpexcludingind())%>"));                   
           <%}
        }else{%>
            alert("[LoadServiceList.jsp ]<%=MiUtil.getMessageClean(strMessageService)%>");      
        <%}
        %>
  }     
     
  //Función para cargar el vector con los servicios Adicionales 
  //según plan Tarifario
        function fxCargaServiciosVector(){
          //vServicio = new Vector();
          parent.mainFrame.vServicio.removeElementAll();
          // --- johncmb inicio
          parent.mainFrame.cmbServiciosval="";
          // --- johncmb fin
        <%  
          ServiciosBean serviciosBean = null;
          HashMap hServiceList = (HashMap) request.getAttribute("hServiceList");
          ArrayList list =(ArrayList)hServiceList.get("objServiceList");
          String strMessage = (String)hServiceList.get("strMessage");              
            if (strMessage==null){
               for(int i=0;i<list.size();i++){
                  serviciosBean = (ServiciosBean)list.get(i);
                  /*HashMap h=(HashMap)list.get(i);
                 
                  String npservicioid=(String)h.get("npservicioid");
                  String npnomserv=(String)h.get("npnomserv");
                  String npnomcorserv=(String)h.get("npnomcorserv");
                  String npexcludingind=(String)h.get("npexcludingind");
                  System.out.println("[loadServicesList][fxCargaServiciosVector][npservicioid]"+npservicioid);
                  System.out.println("[loadServicesList][fxCargaServiciosVector][npnomserv]"+npnomserv);
                  System.out.println("[loadServicesList][fxCargaServiciosVector][npnomcorserv]"+npnomcorserv);
                  System.out.println("[loadServicesList][fxCargaServiciosVector][npexcludingind]"+npexcludingind);*/
                  //johncmb incio
                   %>
                  parent.mainFrame.vServicio.addElement(new ServicioJS("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>","<%=MiUtil.getString(serviciosBean.getNpgroup())%>"));                   
                  //johncmb fin 
                   //modificado LR INICI
                   <%
                  }
            }else
              {%>
                alert(" [LoadServiceList.jsp ] <%=strMessage%>");      
            <%}%>
            }
            
        function fxGetListAdendas(valueObj){
           v_doc  = parent.mainFrame;
           form   = document.frmdatos;
           var plan_aux;
           var kit_aux;
           if (v_doc.form.hdn_ListAdenda == null){
              return false;
           }
           try{
              plan_aux = v_doc.form.cmb_ItemPlanTarifario.options[v_doc.form.cmb_ItemPlanTarifario.selectedIndex].value;
           }catch(e){
              plan_aux = 0;
           }
            
           try{
              kit_aux = v_doc.form.cmb_ItemProducto.options[v_doc.form.cmb_ItemProducto.selectedIndex].value;
           }catch(e){
              kit_aux = 0;
           }
           
           var promocion = valueObj;
           if(promocion == null)promocion = 0;
           var plan = plan_aux*10/10;
           var kit = kit_aux*10/10;
           if ( (plan!="") || (promocion != "") || (kit!="")){
               deleteTable();
               var hdnSpecification = <%=hdnSpecification%>;
               var url = "<%=request.getContextPath()%>/itemServlet?cmbPromocion="+promocion+"&hdnSpecification="+hdnSpecification+"&cmbPlan="+plan+"&cmbKit="+kit+"&hdnMethod=doListarPlantillasNew&strSessionId=<%=strSessionId%>";
               parent.bottomFrame.location.replace(url);
           }
        }
        
   function deleteTable(){
     // alert("deleteTable");
     v_doc  = parent.mainFrame;
      var table = v_doc.document.getElementById("itemsTemplate");
      
      if (table != null){
        var length = table.rows.length;
        if (length > 1){
          for(i = 1; i < length; i++){
             table.deleteRow(table.rows.length-1);      
          }
        }      
      }
   }
   
   function fxLoadServiceDefault(){

    parent.mainFrame.vServicioArren.removeElementAll();
    parent.mainFrame.fxLongMaxServices("<%=objArrayListServiceDefault%>");
    
    <% if (String.valueOf(Constante.SPEC_CAMBIO_MODELO).equals(strSpecificationId) && serviciosBean!=null){ %>  
 
       parent.mainFrame.vServicioArren.addElement(new ServicioArr(parent.mainFrame.frmdatos.hdnIdCM.value,parent.mainFrame.frmdatos.hdnNameCM.value, parent.mainFrame.frmdatos.hdnNameShortCM.value,"ALQ","S","N"));
       parent.mainFrame.frmdatos.hdnServArrCM.value = parent.mainFrame.frmdatos.hdnNameShortCM.value;    
    
   <% } %>  
   <%if( objArrayListServiceDefault != null && objArrayListServiceDefault.size() > 0 ) {
        System.out.println("Carga Servicios Acionales Inicialmente");
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListServiceDefault.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListServiceDefault.get(j);
          System.out.println("serviciosBeanDefault.getNpnomserv():"+serviciosBeanDefault.getNpnomserv());  
          %>
          if ('<%=Constante.SPEC_POSTPAGO_VENTA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_NUEVA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_TDE%>'=='<%=strSpecificationId%>'
            || '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>'=='<%=strSpecificationId%>') {//Se agrego subcategoria reposicion prepago tde - TDECONV034
            parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","N","S"));
          } else if ('<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>') {
            var aServiceArr = new Array();
            var idxAServArr= 0;
            for (k=0;k<parent.mainFrame.vServicioArren.size();k++){
              aServiceArr[idxAServArr] = parent.mainFrame.vServicioArren.elementAt(k).nameShort;
              idxAServArr++; 
             }          
          
            if (!valueExistsInArray(aServiceArr,"<%=serviciosBeanDefault.getNpnomcorserv()%>") && parent.mainFrame.frmdatos.hdnNameShortCM.value == "<%=serviciosBeanDefault.getNpnomcorserv()%>"){
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","S","N"));
            }else if (!valueExistsInArray(aServiceArr,"<%=serviciosBeanDefault.getNpnomcorserv()%>") && parent.mainFrame.frmdatos.hdnNameShortCM.value != "<%=serviciosBeanDefault.getNpnomcorserv()%>"){
               var strflagLogItem = ""+'<%=strflagLogItem%>';
               if (strflagLogItem=="A") {   
                  var strTypeWindow = ""+'<%=type_window%>';
                  vPhone =  parent.mainFrame.frmdatos.txt_ItemPhone.value;
                  vOrderId = parent.opener.frmdatos.hdnNumeroOrder.value;
                  var mensaje = "[Pintando Servicios con loadServiceList.jsp]"+ "strPhone:"+  vPhone + " " +"<%=serviciosBeanDefault.getNpservicioid()%>" + " " + "<%=serviciosBeanDefault.getNpnomserv()%>" + " " + "<%=serviciosBeanDefault.getNpnomcorserv()%>" + " " + "<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>" + " " + "N" + " " + "S";
                  var param = "strTexto="+ mensaje + "&strSessionId=<%=strSessionId%>&strOrderId="+ vOrderId + "&strTypeWindow="+ strTypeWindow ;
                  var parametros = fxRequestXML("generalServlet","requestDoSetLogItem",param);
                  if (parametros ==null ){      
                    alert("Error al realizar la operación");
                  }
               }
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","N","S"));
            }else{
              for(z=0; z<parent.mainFrame.vServicioArren.size(); z++){
                objServicioArrAux = parent.mainFrame.vServicioArren.elementAt(z);
                if (objServicioArrAux.nameShort == "<%=serviciosBeanDefault.getNpnomcorserv()%>"){
                  objServicioArrAux.active_new = 'S';
                  objServicioArrAux.modify_new = 'S';
                }
              }
            }
          } else{
            parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","S","N"));              
          }
       <%}
     }else{ 
      if (serviciosBeanAdicionales!=null){
          System.out.println("Carga Servicios Acionales en Edición");
       %> 
          if ('<%=Constante.SPEC_POSTPAGO_VENTA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_NUEVA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_TDE%>'=='<%=strSpecificationId%>'
            ||'<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>'=='<%=strSpecificationId%>') { 
            //Se agrego subcategoria reposicion prepago tde - TDECONV034
            parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>","N","S")); 
          }else{
            parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>","S","N")); 
          }
     <%  }
     }%>
     
   }
   
    function fxDeleteServiceAdicional(){
      
      var aServiceAvailable = new Array();
      var idxAServAvai = 0;
      
      //Verificamos si los valores del vector vServicioArren existen en el vector vServicio de Servicios Disponibles
      //------------------------------------------------------------------------------------------------------------
      if ( parent.mainFrame.vServicioArren.size()>0){
          for (i=0;i<parent.mainFrame.frmdatos.cmbAvailableServices.options.length;i++){
              aServiceAvailable[idxAServAvai] = parent.mainFrame.frmdatos.cmbAvailableServices.options[i].value;
              idxAServAvai++; 
          }
          
         var wn_size =  parent.mainFrame.vServicioArren.size();
         for(j=0; j<wn_size; j++){
             objServicio =  parent.mainFrame.vServicioArren.elementAt(j);
             if (objServicio.nameShort!="" && !valueExistsInArray(aServiceAvailable,objServicio.nameShort) ) {    
                return;
            }else{
                //Si existe el valor del vector  vServicioArren en el arreglo lo seleccionamos en el combo Disponibles
                //----------------------------------------------------------------------------------------------------
                for (k=0;k<parent.mainFrame.frmdatos.cmbAvailableServices.options.length;k++){
                     if (objServicio.nameShort == parent.mainFrame.frmdatos.cmbAvailableServices.options[k].value) {
                         parent.mainFrame.frmdatos.cmbAvailableServices.options[k].selected = true;
                         if( objServicio.exclude == "ALQ" )
                              parent.mainFrame.alquilerServicesSelected++;
                      }
                }
                
                 deleteSelectedValues(parent.mainFrame.frmdatos.cmbAvailableServices); 
                 parent.mainFrame.frmdatos.hdnFlagServicio.value = "1";
                 bServiceModified = true;
             }
          }
      }
   }
   
   function fxAddServiceDefectoAdicional(){
   
     // var form = document.frmdatos;
      var aServiceAvailable = new Array();
      var aServiceDefault    = new Array();
      var idxAServAvai = 0;
      var idxAServDef  = 0;
      var txt_vacios_2 = "";
      var txt_separacion="  ";
      var wn_size = parent.mainFrame.vServicioArren.size();
      var wn_cantServ = parent.mainFrame.vServicio.size();
      
      for (i=0;i<parent.mainFrame.frmdatos.cmbSelectedServices.options.length;i++){
           aServiceDefault[idxAServDef] = parent.mainFrame.frmdatos.cmbSelectedServices.options[i].value;
           idxAServDef++; 
      }

      for(j=0; j<wn_size; j++){
          objServicio = parent.mainFrame.vServicioArren.elementAt(j);
          if (objServicio.nameShort!="" && !valueExistsInArray(aServiceDefault,objServicio.nameShort) ) { 
              difServices = 0;
              if( objServicio.name.length < parent.mainFrame.longMaxServices )
                  difServices = parent.mainFrame.longMaxServices - objServicio.name.length;
              
              for( k = 0; k < difServices; k++ )
                  txt_vacios_2+=" ";
              
              var txt_texto   =  objServicio.name + txt_vacios_2;
              var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + " " + txt_separacion + "x";
              
              parent.mainFrame.fxAddToList(parent.mainFrame.frmdatos.cmbSelectedServices, txt_texto_2, objServicio.nameShort);
              
              for(z=0; z<wn_cantServ; z++){
                  objServicioAvai = parent.mainFrame.vServicio.elementAt(z);
                  if (objServicio.nameShort == objServicioAvai.nameShort) {
                        objServicioAvai.active_new = "N";
                        objServicioAvai.modify_new = "S";
                   }
              }
          } else if(objServicio.nameShort!="" && objServicio.modify_new == "S" && objServicio.exclude == "ALQ" && valueExistsInArray(aServiceDefault,objServicio.nameShort) && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>') {
              for (i=0;i<parent.mainFrame.frmdatos.cmbSelectedServices.options.length;i++){
                if (objServicio.nameShort == parent.mainFrame.frmdatos.cmbSelectedServices.options[i].value){
                  difServices = 0;
                  var txt_texto_2 = "";
                  if( objServicio.name.length < parent.mainFrame.longMaxServices )
                    difServices = parent.mainFrame.longMaxServices - objServicio.name.length;
              
                  for( k = 0; k < difServices; k++ )
                    txt_vacios_2+=" ";
                    
                  var txt_texto   =  objServicio.name + txt_vacios_2;
                  if (objServicio.active_new=="S"){
                    txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + "x";
                  }else if (objServicio.active_new=="N"){
                    txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + " " + txt_separacion + "x";
                  }
                  parent.mainFrame.frmdatos.cmbSelectedServices.options[i].value = objServicio.nameShort;
                  parent.mainFrame.frmdatos.cmbSelectedServices.options[i].text = txt_texto_2;
                }
              }
          }  
      }
   }
   
  function fxCargaServiciosAdicionales(){
      var aServiceDefault    = new Array();
      var aServiceArrend     = new Array();
      var idxAServDef  = 0;
      var idxAServArr = 0;
      var av_ssaa_contratado = "";
      var av_serv_exc_alq="";
      var av_serv_exc_msj="";
      var av_SSAA_By_Default="";
      
      for (i=0;i<parent.mainFrame.frmdatos.cmbSelectedServices.options.length;i++){
           aServiceDefault[idxAServDef] = parent.mainFrame.frmdatos.cmbSelectedServices.options[i].value;
           idxAServDef++; 
      }
 
    for(k=0; k<parent.mainFrame.vServicioArren.size(); k++){
         objServicio = parent.mainFrame.vServicioArren.elementAt(k);
         if(objServicio.exclude=="ALQ") av_serv_exc_alq="ALQ";
         if(objServicio.exclude=="MSJ") av_serv_exc_msj="MSJ";
         aServiceArrend[idxAServArr] = objServicio.nameShort;
         idxAServArr++;
    }
      
    for(j=0; j<parent.mainFrame.vServicio.size(); j++){
       objServicio = parent.mainFrame.vServicio.elementAt(j);
       if (objServicio.nameShort!="" && valueExistsInArray(aServiceDefault,objServicio.nameShort) && !valueExistsInArray(aServiceArrend,objServicio.nameShort) &&  objServicio.active_new=="S")  { 
            if ( (objServicio.exclude!='' && objServicio.exclude==av_serv_exc_alq) || (objServicio.exclude!='' && objServicio.exclude==av_serv_exc_msj) ){
                av_ssaa_contratado=av_ssaa_contratado+"|"+objServicio.id+"|S|N";
          } else{
                av_ssaa_contratado=av_ssaa_contratado+"|"+objServicio.id+"|S|S";
           }
      }else if(objServicio.nameShort!="" && valueExistsInArray(aServiceDefault,objServicio.nameShort) && 
            valueExistsInArray(aServiceArrend,objServicio.nameShort) &&  objServicio.active_new=="S"){
            av_ssaa_contratado=av_ssaa_contratado+"|"+objServicio.id+"|S|S";
       }

    }  

    parent.mainFrame.fxCargaServiciosItem(av_ssaa_contratado);
    //Inicio SSAA por Defecto - 23/11/2011 - Frank Picoy
    av_SSAA_By_Default = ""+'<%=strSSAAByDefault%>';
    av_SSAA_By_DefWithLSA = ""+'<%=strSSAAByDefWithLSA%>';
    //alert("av_SSAA_By_Default--->" + av_SSAA_By_Default);
    if (av_SSAA_By_Default=="1") {
      fxcargaVectorSSAAPorDefecto();
      if (av_SSAA_By_DefWithLSA=="1") {
        parent.mainFrame.fxCargaServiciosPorDefectoNuevo();
      } else {
        parent.mainFrame.fxCargaServiciosPorDefecto();
      } 
    } else {
      parent.mainFrame.fxCargaServiciosPorDefecto();
    }
    //Fin SSAA por Defecto - 23/11/2011 - Frank Picoy
    //parent.mainFrame.fxCargaServiciosPorDefecto();
}
  //Inicio SSAA por Defecto - 23/11/2011 - Frank Picoy
  function fxcargaVectorSSAAPorDefecto() {
     parent.mainFrame.vServicioPorDefecto.removeElementAll();
      <%if( objArrayListServiceDefaultByPlan != null && objArrayListServiceDefaultByPlan.size() > 0 ) {
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListServiceDefaultByPlan.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListServiceDefaultByPlan.get(j);%>
          parent.mainFrame.vServicioPorDefecto.addElement(new Servicio("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>"));
       <%}
     }%>
  }
  //Fin SSAA por Defecto - 23/11/2011 - Frank Picoy    
//johncmb inicio
// funcion que inicializa las variables temporales usadas 
// para el filtrado de servicios adicionales un valor del combo
//cmbServicio
  function iniVarTempObjserviceAditional(){
  parent.mainFrame.fxSetHiddenChangePlan("<%=strMessageEmployeeValid%>");//SAR N_O000012485 - FPICOY - 09/01/2014
  if(parent.mainFrame.frmdatos.cmbSevices!=null){
     parent.mainFrame.cmbServiciosval="";
     parent.mainFrame.frmdatos.cmbSevices.selectedIndex=0;
     //arrays temporales para guardar los valores del combo cmbAvailableService cuando esta completo y cuando esta filtrado
     parent.mainFrame.aTmpAvailableService  = new Array();
     parent.mainFrame.aTmpAvailableServiceFil   = new Array();
   }
   }
//johncmb fin
function fxRequestXML(servlet,funcion, params){
  var url = "<%=request.getContextPath()%>/"+servlet+"?metodo=" + funcion + "&"+params;
  //alert(url);
  var msxml = new ActiveXObject("msxml2.XMLHTTP");
  msxml.Open("GET", url, false);
  msxml.Send("");
  var ret = msxml.responseText;		
  if(ret.indexOf("OK")!=0){      
     return null;
  }      
  return ret.substring(2,ret.length);
}  
 </script>
 <script "language=javascript">
//johncmb inicio
      iniVarTempObjserviceAditional(); 
//johncmb fin  
      fxCargaServiciosVector();  
      var serviceSSAA = "";
      serviceSSAA = ""+'<%=(String)request.getAttribute("serviceAditional")%>';
      parent.mainFrame.fxCargaServiciosItem(serviceSSAA);
      //fxCargaServiciosItem(serviceSSAA);
      fxLoadServiceRentList();
      fxGetListAdendas();
     //CBARZOLA      
      try{
      
      fxLoadServiceComercialCore();

      }catch(e) {}

      //EZUBIAURR 29/12 Se agrega condiciónes Compatibilidad Modelo-Plan-Servicio
      //FPICOY 01/12/2011 Se agregó una condicion adicional para categorias con SSAA por default.
     <% if ((strPermission_alq.equals("1") || strPermission_msj.equals("1") || 
            strSpecificationId.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))  || 
            strSpecificationId.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) ||
            strSpecificationId.equals(String.valueOf(Constante.SPEC_SSAA_SUSCRIPCIONES))  ||
            strSpecificationId.equals(String.valueOf(Constante.SPEC_SSAA_PROMOTIONS)) ||
            strSpecificationId.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS)))
            || (strPermission_alq.equals("0") && strPermission_msj.equals("0") && strSSAAByDefault.equals("1"))
            ){ %> 
      fxLoadServiceDefault();
    
     <% if (serviciosBeanAdicionales==null){ %>
         
        fxCargaServiciosAdicionales(); 
     <% }%>
      fxDeleteServiceAdicional();
      
      fxAddServiceDefectoAdicional();

      <%}%>
      
   <%//}%>
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>
