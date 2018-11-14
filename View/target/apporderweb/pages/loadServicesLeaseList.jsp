<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.bean.ServiciosBean" %>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.bean.ProductBean" %>

<%
  
  try{  
    String strProductLineId = (String)request.getAttribute("strProductLineId");
    String strProductId = (String)request.getAttribute("strProductId");
    String strPlanTarifario = (String)request.getAttribute("strPlanTarifario");  
  
    //-------------------------------------------------------------------------------------------------------- Agregado Ini 
    
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
        
    HashMap objHashMaps     = (HashMap)request.getAttribute("objHashMaps");
    ArrayList objArrayList  = new ArrayList();
    if (objHashMaps!=null){
      objArrayList = (ArrayList)objHashMaps.get("objArrayList");      
    }    
        
    //-------------------------------------------------------------------------------------------------------------- Agregado Fin
  
%>
<html>
  <head>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
    <script language="javascript">
    
    function fxload(){             
        
        form = parent.mainFrame.document.frmdatos;     
        vDoc = parent.mainFrame;
                
        fxLoadProductList();
                
        vDoc.form.cmb_ItemProductLine.value   = '<%=strProductLineId%>';              
        vDoc.form.cmb_ItemProducto.value      = '<%=strProductId%>';
        vDoc.form.cmb_ItemPlanTarifario.value = '<%=strPlanTarifario%>';  
                                            
        vDoc.valorOldProductNew.value = '';
        vDoc.valorOldModalityNew.value = '';
        vDoc.valorOldPlanNew.value = '';
        vDoc.valorOldQuantity.value = '';      
  
        vDoc.DeleteSelectOptions(form.cmb_ItemPromocion);
        form.txt_ItemPriceCtaInscrip.value = "";
        form.txt_ItemPriceException.value  = "";
        form.txt_ItemEquipmentRent.value = "" ;                    
                                                                      
    }        
    
    //-----------------------------------------------------------------------  Agregado Ini
    
  function fxLoadProductList(){          
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);
    parent.mainFrame.vctProduct.removeElementAll();
    
    <%if( objArrayList != null && objArrayList.size() > 0 ){     
      for( int i=0; i<objArrayList.size();i++ ){
        ProductBean objProductBean = new ProductBean();
        objProductBean = (ProductBean)objArrayList.get(i);
        %>     
        parent.mainFrame.vctProduct.addElement(new parent.mainFrame.fxMakeProduct("<%=objProductBean.getNpproductid()%>",  "<%=objProductBean.getNpminute()%>"  , "<%=objProductBean.getNpminuteprice()%>") )
        parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProducto, "<%=objProductBean.getNpproductid()%>" , "<%=MiUtil.getMessageClean(objProductBean.getNpproductname())%>" )      
     <%}      
    }%>        
  }
            
    
  function fxLoadServiceDefault(){
    parent.mainFrame.vServicioArren.removeElementAll();
    parent.mainFrame.fxLongMaxServices("<%=objArrayListServiceDefault%>");
    parent.mainFrame.vServicioArren.addElement(new ServicioArr(parent.mainFrame.frmdatos.hdnIdCM.value,parent.mainFrame.frmdatos.hdnNameCM.value, parent.mainFrame.frmdatos.hdnNameShortCM.value,"ALQ","S","N"));
    parent.mainFrame.frmdatos.hdnServArrCM.value = parent.mainFrame.frmdatos.hdnNameShortCM.value;    
    
   <%if( objArrayListServiceDefault != null && objArrayListServiceDefault.size() > 0 ) {
        System.out.println("Carga Servicios Acionales Inicialmente");
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListServiceDefault.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListServiceDefault.get(j);
          %>parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>","S","N"));
       <%}
     }else{ 
      if (serviciosBean!=null){
          System.out.println("Carga Servicios Acionales en Edición");
          ServiciosBean servicioBean = null;
          for(int i=0; i<serviciosBean.size(); i++){
             servicioBean = (ServiciosBean)serviciosBean.get(i);                    
       %> 
          parent.mainFrame.vServicioArren.addElement(new ServicioArr("<%=servicioBean.getNpservicioid()%>","<%=servicioBean.getNpnomserv()%>","<%=servicioBean.getNpnomcorserv()%>","<%=MiUtil.getString(servicioBean.getNpexcludingind())%>","S","N")); 
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
              else{
                 var txt_texto_2 = txt_texto.substr(0,parent.mainFrame.longMaxServices) + txt_separacion + " " + txt_separacion + "x";
              }
              
              parent.mainFrame.fxAddToList(parent.mainFrame.frmdatos.cmbSelectedServices, txt_texto_2, objServicio.nameShort);
              objServicio.active_new = "N";
              objServicio.modify_new = "S";
              for(z=0; z<wn_cantServ; z++){
                  objServicioAvai = parent.mainFrame.vServicio.elementAt(z);
                  if (objServicio.nameShort == objServicioAvai.nameShort) {
                     objServicioAvai.active_new = "N";
                     objServicioAvai.modify_new = "S";
                  }
              }
          } 
      }      
   }
   
   function fxAddServiceDefectoAdicionalEdicion(){
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
             
    
    //--------------------------------------------------------------------------  Agregado Fin
                    
    </script>
   
   <script "language=javascript">
    fxload();    
    
    //-----------------------------------------------------------------------------------
    
    fxLoadServiceDefault();
    <% 
    if (serviciosBean==null){ %>
      fxCargaServiciosAdicionales(); 
    <%}%>
    fxDeleteServiceAdicional();
    <% 
    if (serviciosBean==null){ %>
      fxAddServiceDefectoAdicional();
    <% }else{%>
    fxAddServiceDefectoAdicional();
     <% }%>
    <%//}%>
    
              
    //-----------------------------------------------------------------------------------
            
   </script>
   
  <%}catch(Exception ex){%>
  <script DEFER>
    alert("<%=MiUtil.getMessageClean(ex.getMessage())%>")
  </script>
  <%}%>
     
  </head>
  <body bgcolor="#AABBCC">
        
  </body>
</html>
