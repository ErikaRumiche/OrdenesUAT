<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.bean.ServiciosBean" %>
<%@page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%@page import="pe.com.nextel.bean.ProductLineBean" %>
<%@page import="java.text.DecimalFormat" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Servicio del Producto</title>
  <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>  
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
</head>
<body style="padding: 0px; margin: 0px" bgcolor="#AABBCC">
<%
  try{
  
  //Valores Principales
  //--------------------
  String strSessionId       = (String)request.getAttribute("strSessionId");
  String strSpecificationId = (String)request.getAttribute("strSpecificationId");
  String strParametro       = MiUtil.getString((String)request.getAttribute("strParametro"));
  
  System.out.println("loadServiceDefaultInfo.jsp>>>>>>>>>>><<");
  System.out.println("strSessionId:"+strSessionId);
  System.out.println("strSpecificationId:"+strSpecificationId);
 
  //Listado de Servicios x Defecto
  //------------------------------
  HashMap hServiceDefaultList     = (HashMap)request.getAttribute("hServiceDefaultList");
  ArrayList objArrayListServiceDefault = new ArrayList();
  if (hServiceDefaultList!=null){
    objArrayListServiceDefault = (ArrayList)hServiceDefaultList.get("objServiceDefaultList");
  }
  
  //ServiciosBean serviciosBean     = (ServiciosBean)request.getAttribute("serviciosBean");
  ArrayList serviciosBean     = (ArrayList)request.getAttribute("serviciosBean");
  String[] strServiceAct      = (String[])request.getAttribute("strServiceAct");
  String[] strServiceMod      = (String[])request.getAttribute("strServiceMod");
  
  
%>

<script language="javascript">
  function fxLoadServiceDefault(){
    parent.mainFrame.vServicioArren.removeElementAll();
    parent.mainFrame.fxLongMaxServices("<%=objArrayListServiceDefault%>");
    
   <% if (serviciosBean==null){ %>  
     
       parent.mainFrame.vServicioArren.addElement(new ServicioArr(parent.mainFrame.frmdatos.hdnIdCM.value,parent.mainFrame.frmdatos.hdnNameCM.value, parent.mainFrame.frmdatos.hdnNameShortCM.value,"ALQ","S","N"));
       parent.mainFrame.frmdatos.hdnServArrCM.value = parent.mainFrame.frmdatos.hdnNameShortCM.value;    
    
   <% } %>  
   
   <%if( objArrayListServiceDefault != null && objArrayListServiceDefault.size() > 0 ) {
        System.out.println("Carga Servicios Acionales Inicialmente");
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListServiceDefault.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListServiceDefault.get(j);
          %>
          //alert('getNpnomserv 1 : '+"<%=serviciosBeanDefault.getNpnomserv()%>");

          if ('<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>') {
            var aServiceArr = new Array();
            var idxAServArr= 0;
            for (k=0;k<parent.mainFrame.vServicioArren.size();k++){
              aServiceArr[idxAServArr] = parent.mainFrame.vServicioArren.elementAt(k).nameShort;
              idxAServArr++; 
            }          
          
            if (!valueExistsInArray(aServiceArr,"<%=serviciosBeanDefault.getNpnomcorserv()%>") && parent.mainFrame.frmdatos.hdnNameShortCM.value == "<%=serviciosBeanDefault.getNpnomcorserv()%>"){
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","S","N"));
            }else if (!valueExistsInArray(aServiceArr,"<%=serviciosBeanDefault.getNpnomcorserv()%>") && parent.mainFrame.frmdatos.hdnNameShortCM.value != "<%=serviciosBeanDefault.getNpnomcorserv()%>"){
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
          }else{//se agrega reposicion prepago tde TDECONV034
            if ('<%=Constante.SPEC_POSTPAGO_VENTA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_NUEVA%>'=='<%=strSpecificationId%>'||  '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>'=='<%=strSpecificationId%>'
              || '<%=Constante.SPEC_PREPAGO_TDE%>'=='<%=strSpecificationId%>' ) {
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","N","S"));
            }else{
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","S","N"));
            }
          }
       <%}
     }else{ 
      if (serviciosBean!=null){
          System.out.println("Carga Servicios Acionales en Edición");
          ServiciosBean servicioBean = null;
          String serviceAct = "S";
          String serviceMod = "N";
          for(int i=0; i<serviciosBean.size(); i++){
             servicioBean = (ServiciosBean)serviciosBean.get(i);
             serviceAct   = (String)strServiceAct[i];
             serviceMod   = (String)strServiceMod[i];  
        %> 
          //alert('getNpnomserv 2 : '+"<%=servicioBean.getNpnomserv()%>");
          if ('<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>') {
            parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=servicioBean.getNpservicioid()%>","<%=servicioBean.getNpnomserv()%>","<%=servicioBean.getNpnomcorserv()%>","<%=MiUtil.getString(servicioBean.getNpexcludingind())%>","<%=serviceAct%>","<%=serviceMod%>")); 
          }else{//se agrega subcategoria reposicion prepago tde TDECONV034
            if ('<%=Constante.SPEC_POSTPAGO_VENTA%>'=='<%=strSpecificationId%>' || '<%=Constante.SPEC_PREPAGO_NUEVA%>'=='<%=strSpecificationId%>'||  '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>'=='<%=strSpecificationId%>'
              || '<%=Constante.SPEC_PREPAGO_TDE%>'=='<%=strSpecificationId%>') {
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=servicioBean.getNpservicioid()%>","<%=servicioBean.getNpnomserv()%>","<%=servicioBean.getNpnomcorserv()%>","<%=MiUtil.getString(servicioBean.getNpexcludingind())%>","N","S"));           
            }else{
              parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=servicioBean.getNpservicioid()%>","<%=servicioBean.getNpnomserv()%>","<%=servicioBean.getNpnomcorserv()%>","<%=MiUtil.getString(servicioBean.getNpexcludingind())%>","S","N"));           
            }
          }
     <%  }}
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
      
      if(wn_size == 0){
         parent.mainFrame.frmdatos.hdnServArrCM.value = "";
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
              
              if(parent.mainFrame.frmdatos.hdnServArrCM.value == "S"){
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + " ";
                 parent.mainFrame.frmdatos.hdnServArrCM.value = objServicio.nameShort;
              }
              else if(parent.mainFrame.frmdatos.hdnServArrCM.value == objServicio.nameShort){
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + " ";
              }
              else if(parent.mainFrame.frmdatos.hdnOriginalProductIdCM.value == objServicio.id){
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + " ";
              }
              else if(objServicio.active_new == 'S' && objServicio.modify_new == 'S' && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>'){
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + "x";
              }
              else if(objServicio.active_new == 'S' && objServicio.modify_new == 'N' && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>'){
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + "x" + txt_separacion + " ";
              }
              else{
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + " " + txt_separacion + "x";
              }
              
              parent.mainFrame.fxAddToList(parent.mainFrame.frmdatos.cmbSelectedServices, txt_texto_2, objServicio.nameShort);
              strAactiveNew = "N";
              strModifyNew = "S";
              
              if(objServicio.active_new == 'S' && objServicio.modify_new == 'S' && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>'){
                strAactiveNew = "S";
                strModifyNew = "S";
              } else if (objServicio.active_new == 'S' && objServicio.modify_new == 'N' && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>'){
                strAactiveNew = "S";
                strModifyNew = "N";
              } 
              
              objServicio.active_new = strAactiveNew;
              objServicio.modify_new = strModifyNew;
              
              for(z=0; z<wn_cantServ; z++){
                  objServicioAvai = parent.mainFrame.vServicio.elementAt(z);
                  if (objServicio.nameShort == objServicioAvai.nameShort) {
                     objServicioAvai.active_new = strAactiveNew;
                     objServicioAvai.modify_new = strModifyNew;
                  }
              }
          }else if(objServicio.nameShort!="" && objServicio.modify_new == "S" && valueExistsInArray(aServiceDefault,objServicio.nameShort) && '<%=Constante.SPEC_CAMBIO_MODELO%>'=='<%=strSpecificationId%>') {
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
   
   function fxAddServiceDefectoAdicionalEdicion(){
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
      
      if(wn_size == 0){
         parent.mainFrame.frmdatos.hdnServArrCM.value = "";
      }
      
      <%
      if(serviciosBean != null)
      
      for(int i=0; i<serviciosBean.size(); i++){%>
      
          objServicio = parent.mainFrame.vServicioArren.elementAt('<%=i%>');
          if (objServicio.nameShort!="" && !valueExistsInArray(aServiceDefault,objServicio.nameShort) ) { 
              difServices = 0;
              if( objServicio.name.length < parent.mainFrame.longMaxServices )
                  difServices = parent.mainFrame.longMaxServices - objServicio.name.length;
              
              for( k = 0; k < difServices; k++ )
                  txt_vacios_2+=" ";
              
              var txt_texto   =  objServicio.name + txt_vacios_2;
              
              var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + 
                                txt_separacion + 
                                '<%=("S".equals(strServiceAct[i]))?"x":" "%>' + 
                                txt_separacion + 
                                '<%=("S".equals(strServiceMod[i]))?"x":" "%>';
              
              
              objServicio.active_new = '<%=strServiceAct[i]%>';
              objServicio.modify_new = '<%=strServiceMod[i]%>';                         
              
              if(objServicio.active_new == 'S' && objServicio.modify_new == 'N' && "<%=strSpecificationId%>" == "<%=Constante.SPEC_CAMBIO_MODELO%>"){
                 parent.mainFrame.frmdatos.hdnServArrCM.value = objServicio.nameShort;
              }
              
              parent.mainFrame.fxAddToList(parent.mainFrame.frmdatos.cmbSelectedServices, txt_texto_2, objServicio.nameShort);
              
              for(z=0; z<wn_cantServ; z++){
                  objServicioAvai = parent.mainFrame.vServicio.elementAt(z);
                  if (objServicio.nameShort == objServicioAvai.nameShort) {
                     //objServicioAvai.active_new = '<%=strServiceAct[i]%>';
                     //objServicioAvai.modify_new = '<%=strServiceMod[i]%>';
                     objServicioAvai.active_new = objServicio.active_new;
                     objServicioAvai.modify_new = objServicio.modify_new;
                   }
              }             
          }      
      
      <%}%>
   }   
   
  function fxCargaServiciosAdicionales(){
      var aServiceDefault    = new Array();
      var aServiceArrend     = new Array();
      var idxAServDef  = 0;
      var idxAServArr = 0;
      var av_ssaa_contratado = "";
      var av_serv_exc_alq="";
      var av_serv_exc_msj="";
      
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
           if ( (objServicio.exclude==av_serv_exc_alq) || (objServicio.exclude==av_serv_exc_msj) ){
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
    parent.mainFrame.fxCargaServiciosPorDefecto();
      
  }
    function iniVarTempObjserviceAditional(){
  if(parent.mainFrame.frmdatos.cmbSevices!=null){
     parent.mainFrame.cmbServiciosval="";
     parent.mainFrame.frmdatos.cmbSevices.selectedIndex=0;
     //arrays temporales para guardar los valores del combo cmbAvailableService cuando esta completo y cuando esta filtrado
     parent.mainFrame.aTmpAvailableService  = new Array();
     parent.mainFrame.aTmpAvailableServiceFil   = new Array();
   }
   }
</script>
   
<script "language=javascript">
  //johncmb inicio
iniVarTempObjserviceAditional();
//johncmb fin
      fxLoadServiceDefault();
     <% if (serviciosBean==null){ %>
        fxCargaServiciosAdicionales(); 
     <% }%>
      fxDeleteServiceAdicional();
     <% if (serviciosBean==null){ %>
      fxAddServiceDefectoAdicional();
     <% }else{%>
      //fxAddServiceDefectoAdicionalEdicion();
      fxAddServiceDefectoAdicional();
     <% }%>
   <%//}%>
 </script>

<%}catch(Exception ex){%>
  <script DEFER>
    alert("<%=MiUtil.getMessageClean(ex.getMessage())%>")
  </script>
<%}%>

</body>
</html>