<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.bean.NpObjItemSpecgrp" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.service.ItemService" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.service.MassiveOrderService" %>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.*"%>
<%
 try{
   System.out.println("ACTIVOS========="+request.getParameter("strListAct"));
   System.out.println("DESACTIVOS========="+request.getParameter("strListDes"));    
   
   //Servicios que tiene el quipo 
   String item_services     = request.getParameter("strListAct")==null?"":(String)request.getParameter("strListAct");
   String item_index        = request.getParameter("strIndex")==null?"":(String)request.getParameter("strIndex");
   String strItemActSSAA    = request.getParameter("strItemActSSAA")==null?"":(String)request.getParameter("strItemActSSAA");
   String strItemDesSSAA    = request.getParameter("strItemDesSSAA")==null?"":(String)request.getParameter("strItemDesSSAA");
   String strSolutionId     = request.getParameter("strSolutionId"); 
   
   MassiveOrderService  objMassiveOrderService = new MassiveOrderService();
   Hashtable hshtinputNewSection = new Hashtable();
   HashMap objHashItemServices   = new HashMap();
   ArrayList objArrayItemServices = new ArrayList();
   String    strMessage   = "";
  
   objHashItemServices = objMassiveOrderService.getServiceItemListBySolution(MiUtil.parseInt(strSolutionId));
   strMessage=(String)objHashItemServices.get("strMessage");
   System.out.println("Valores =================>"+strMessage);
   System.out.println("item_services===============>"+item_services);
   System.out.println("item_index============>"+item_index);
   System.out.println("strSolutionId============>"+strSolutionId);
   if (strMessage!=null)
     throw new Exception(strMessage);
     
   objArrayItemServices = (ArrayList)objHashItemServices.get("objArrayList"); 
   
%>
<html>
  <head>
    <title>Servicios Adicionales</title>
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
     <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
     
  </head>
  
  
  <body> 
  <form method="post" name="frmdatos">
  <table  align="center" width="100%" border="0">
  
  <tr><td>      
    
    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
       <tr class="PortletHeaderColor">
          <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
          <td class="PortletHeaderColor" align="left" valign="top">
          <font class="PortletHeaderText">
          Servicios Adicionales
          </font></td>
          <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
       </tr>
    </table>            
    
  </td></tr>
   
   
     <td  class="" colspan="2"  align="left">

         <!--Linea de separación -->
         <table><tr align="center"><td></td></tr></table>
         <div id="services">
            
            <!-- Servicios Disponibles y Seleccionados -->
            <div id="tableServiciosAdicionales">
            <!--<table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top">Servicios Adicionales</td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>-->
            <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder">
               <tr align="center">
                  <td class="RegionHeaderColor" valign="top">
                     <table border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr >
                           <td class="CellLabel" align="center">Disponibles</td>
                           <td class="CellLabel"></td>
                           <td class="CellLabel" align="center">Seleccionados</td>
                           <td class="CellLabel"> <div id="dServiceDetail"></div>
                           </td>
                        </tr>
                        <tr>
                           <td class="CellLabel" width="25%">&nbsp;</td>
                           <td class="CellLabel" width="25%">&nbsp;</td>
                           <td class="CellLabel" width="50%" colspan="2">
                           <table width="100%" border="0">
                             <tr class="CellLabel" align="center">
                                <td class="CellLabel" align="center">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Servicio&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td class="CellLabel" align="right" colspan="4">
                                &nbsp;|&nbsp;Act.&nbsp;|&nbsp;Modf.&nbsp;
                                </td>
                              </tr>
                           </table>
                           </td>
                        </tr>

                        </tr>
                        <tr>
                           <td class="CellContent" align="center">
                              <select name="cmbAvailableServices" disabled size="10" multiple ondblclick="javascript:fxAddService();">
                              </select>                              
                              <input type="hidden" value="" name="item_services">
                           </td>
                           <td class="CellContent" align="center">
                              <input type="button" value="&gt;&gt;" name="baddService" onclick="javascript:fxAddService();">
                              <input type="hidden" name="hdnFlagServicio" value="0"> <!-- Almacena el Flag de modificacion de los Servicios de Items -->
                              <br>
                           </td>                                                            
                           <td class="CellContent" align="center" colspan="2">
                              <select name="cmbSelectedServices" disabled size="10" ondblclick="javascript:fxEliminaServicio(this);"  multiple style="font-family: Courier New; font-size: 9 pt;" >
                              </select>
                           </td>
                        </tr>
                     </table>
                  </td>
               </tr>
            </table>
            </div>
         </div>   <!-- de " div Servicios" -->    

     </td>
   
   
  <tr><td>   
   
         <table><tr align="center"><td></td></tr></table>
         <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
               <!--<td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " onclick="javascript:fxSendSSAAItems();">&nbsp;&nbsp;&nbsp;</td>-->
               <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCancelWindow();"></td>
            </tr>
        </table>
         
  </td></tr>
  </table>
  </form>
  </body>
  </html>
 

<script DEFER> 
  var servicios_item = "<%=item_services%>";
  var item_index = "<%=item_index%>";
  var vServicioItem    = new Vector();
  var longMaxServices  = 0;
  var alquilerServicesSelected = 0;
  var rentServicesSelected     = 0;
  var rentServicesSelectedEnd  = 0;
  var strsuccess;
  var strActServices = "       NO";
  var strDescServices = "       NO";
  var strItemActSSAA = "<%=strItemActSSAA%>";
  var strItemDesSSAA = "<%=strItemDesSSAA%>";
  
  function fxSendSSAAItems(){       
  
    parent.opener.frmdatos.hdnItemServicesNew[item_index-1].value = fxGetSelectedServices(); 
    
    /*alert("strActServices=============>"+strActServices);
    alert("strDescServices=============>"+strDescServices);*/
    
    if (strActServices == "       SI" && strDescServices == "       SI"){
      parent.opener.frmdatos.txtItemActSSAA[item_index-1].value = strActServices;
      parent.opener.frmdatos.txtItemActSSAA[item_index-1].style.color = "blue"; 
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "SUCCESS";   
      parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "SUCCESS";
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].style.color = "blue";
      
      parent.opener.frmdatos.txtItemDesSSAA[item_index-1].value = strDescServices;
      parent.opener.frmdatos.txtItemDesSSAA[item_index-1].style.color = "blue"; 
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "SUCCESS";   
      parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "SUCCESS";
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].style.color = "blue";
      
    }
    if (strActServices == "       SI" && strDescServices == "       NO"){
      
      if (strItemDesSSAA == "       NO"){
         parent.opener.frmdatos.txtItemDesSSAA[item_index-1].value = "";          
         parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "";   
         parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "ERROR";         
      }
      
      parent.opener.frmdatos.txtItemActSSAA[item_index-1].value = strActServices;
      parent.opener.frmdatos.txtItemActSSAA[item_index-1].style.color = "blue"; 
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "SUCCESS";   
      parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "SUCCESS";
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].style.color = "blue";
      
    }
    
    if (strDescServices == "       SI" && strActServices == "       NO"){
      
      if (strItemActSSAA == "       NO"){
         parent.opener.frmdatos.txtItemActSSAA[item_index-1].value = "";          
         parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "";   
         parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "ERROR";         
      }
      
      parent.opener.frmdatos.txtItemDesSSAA[item_index-1].value = strDescServices;
      parent.opener.frmdatos.txtItemDesSSAA[item_index-1].style.color = "blue"; 
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].value =  "SUCCESS";   
      parent.opener.frmdatos.hdnItemMsjResSSAA[item_index-1].value =  "SUCCESS";
      parent.opener.frmdatos.txtItemMsjResSSAA[item_index-1].style.color = "blue";
    }
    
    parent.close();    
  }
    
  function fxGetSelectedServices(){
    var str = "";
    var wn_cantServ = vServicioItem.size();
    for(j=0; j<wn_cantServ; j++){
      objServicio = vServicioItem.elementAt(j);
      if (objServicio.active_new == "S") {
        str =  str + "|" + objServicio.id + "|" + objServicio.active_new + "|" + objServicio.modify_new;

        
      };
      else
        if (objServicio.active_new == "N" && objServicio.modify_new == "S") {
          str =  str + "|" + objServicio.id + "|" + objServicio.active_new + "|" + objServicio.modify_new;

          
        };
    };
    return str;     
  }
    
  function fxCancelWindow() {  
    parent.close();
  }
  
  function ServicioItem(id, name ,nameShort ,exclude) {        
    this.id = id;
    this.name = name;
    this.nameDisplay = (exclude=="")?name:name+" - "+exclude;
    this.nameShort = nameShort;
    this.nameShortDisplay = (exclude=="")?nameShort:nameShort+" - "+exclude;
    this.exclude = exclude;
    this.active_new = "N";
    this.modify_new = "N";        
  }
   
  function fxloadServicesItems(){    
    var form = document.frmdatos;
    deleteAllValues(form.cmbAvailableServices);
    deleteAllValues(form.cmbSelectedServices);
    //Se deben reiniciar los contadores globales
    alquilerServicesSelected = 0;
    rentServicesSelected     = 0;
    rentServicesSelectedEnd  = 0;
    //vServicioItem.removeElementAll();
    
    <%if (objArrayItemServices!= null && objArrayItemServices.size() > 0){
      //ServiciosBean serviciosBySolution = null;
      
      ServiciosBean serviciosBySolution = null;
      serviciosBySolution = (ServiciosBean)objArrayItemServices.get(0);
       int longMaxServices = MiUtil.getString(serviciosBySolution.getNpnomserv()).length();
       for( int j = 1; j < objArrayItemServices.size(); j++ ){
         serviciosBySolution = (ServiciosBean)objArrayItemServices.get(j);
          if( MiUtil.getString(serviciosBySolution.getNpnomserv()).length() > longMaxServices )
            longMaxServices = MiUtil.getString(serviciosBySolution.getNpnomserv()).length();
       }
       
       %>
      longMaxServices = '30<%//=longMaxServices%>';
       <%
      
      for( int i = 1; i < objArrayItemServices.size(); i++ ){
        serviciosBySolution = (ServiciosBean)objArrayItemServices.get(i);     
    %>
        //AddNewOption(document.frmdatos.cmbAvailableServices,'<%=serviciosBySolution.getNpservicioid()%>','<%=serviciosBySolution.getNpnomserv()%>');  
        vServicioItem.addElement(new ServicioItem("<%=serviciosBySolution.getNpservicioid()%>","<%=serviciosBySolution.getNpnomserv()%>","<%=serviciosBySolution.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBySolution.getNpexcludingind())%>"));
    <%}
    }%>
    
    fxCargaServiciosItem(servicios_item);
  }
  
  
  function fxCleanServicesVector(vServicioItem){
    for(i=0; i<vServicioItem.size(); i++){
        vServicioItem.elementAt(i).modify_new = "N";
    }
  }
  /* Objetivo : Carga los servicios adicionales a los seleccionados y resta
               los servicios disponibles.             
  */  
  function fxCargaServiciosItem(servicios_item){ 

      /*alert("servicios_item==============="+servicios_item);
      alert("alquilerServicesSelected======================"+alquilerServicesSelected);
      alert("rentServicesSelected======================"+rentServicesSelected);
      alert("rentServicesSelectedEnd======================"+rentServicesSelectedEnd);*/
      //v_doc  = parent.mainFrame;      
      form   = document.frmdatos;
     
      if (form.cmbAvailableServices!=null){          
          deleteAllValues(form.cmbAvailableServices);           
          deleteAllValues(form.cmbSelectedServices);
          /*Se deben dejar en "N" los flags del vector*/
          fxCleanServicesVector(vServicioItem);
          wn_cantServ = vServicioItem.size();
          wv_Serv_item = servicios_item;
          /* variable para separacion de las "x" en el combo "cmbSelectedServices" */
          var txt_separacion="  ";
          /* tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados */
          var txt_vacios_2="";
          wv_Serv_bscs = servicios_item;
          for(i=0; i<wn_cantServ; i++){
              objServicio = vServicioItem.elementAt(i);              
              difServices = 0;
              if( objServicio.name.length < longMaxServices )
                difServices = longMaxServices - objServicio.name.length;
              
              for( k = 0; k < difServices; k++ )
                txt_vacios_2+=" ";
          
              wv_servicio = objServicio.id + "|";
              
              /*Antes obtenía el nombre corto*/
              //var txt_servicio = objServicio.nameShortDisplay + txt_vacios_2;
              /*Ahora obtengo el nombre largo*/
              var txt_servicio = objServicio.name + txt_vacios_2;
              
              var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);
              /* El teléfono tiene este servicio */
              
              if ( wv_Serv_bscs.indexOf(wv_servicio)>-1 ){
                 var wv_indice = wv_Serv_bscs.indexOf(wv_servicio);
                 var wv_long   = wv_servicio.length;
                 var wv_servicio_act = wv_Serv_item.substring(wv_indice + wv_long ,wv_indice + wv_long + 1);
                 var wv_servicio_new = wv_Serv_item.substring(wv_indice + wv_long + 2 ,wv_indice + wv_long + 3);
                 
                 if ( wv_servicio_act == "S" && wv_servicio_new == "S" ){                    
                     AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +"x");
                     
                     if( objServicio.exclude == "ALQ" )
                        //parent.mainFrame.alquilerServicesSelected++;
                        alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        //parent.mainFrame.rentServicesSelected++;
                        rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        //parent.mainFrame.rentServicesSelectedEnd++;
                        rentServicesSelectedEnd++;
                     
                 }//Desactivar
                 else if (wv_servicio_act == "S" && wv_servicio_new == "N"){                        
                     //AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
                     AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");
                     if( objServicio.exclude == "ALQ" )
                        //parent.mainFrame.alquilerServicesSelected++;
                        alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        //parent.mainFrame.rentServicesSelected++;
                        rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        //parent.mainFrame.rentServicesSelectedEnd++;
                        rentServicesSelectedEnd++;
                 }//Activar
                 else if (wv_servicio_act == "N" && wv_servicio_new == "S"){                      
                     AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
                     //alert("Entramos " + objServicio.nameShort)
                     if( objServicio.exclude == "ALQ" )
                        //parent.mainFrame.alquilerServicesSelected++;
                        alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        //parent.mainFrame.rentServicesSelected++;
                        rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        //parent.mainFrame.rentServicesSelectedEnd++;
                        rentServicesSelectedEnd++;
                 }                    
                     objServicio.active_new=wv_servicio_act;                     
                     objServicio.modify_new=wv_servicio_new;
               }
               else{
                 /* El teléfono no tiene este servicio */
                 /*Nombre Corto*/
                 //v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.nameShortDisplay);
                 /*Nombre Largo*/
                 AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.name);
               }
               
               //alert("alquilerServicesSelected : " + parent.mainFrame.alquilerServicesSelected)
               //alert("rentServicesSelected : " + parent.mainFrame.rentServicesSelected)
          } // end_for
          
          /* Si el ComboBox de Serv. Seleccionados no tiene elementos entonces añadimos un
             elemento "vacio" para una mejor visualizacion del combo */
             if (form.cmbSelectedServices.length < 1) {                
                 txt_servicio_2 = "            ";
                 AddNewOption(form.cmbSelectedServices, "", txt_servicio_2 + txt_separacion + " " + txt_separacion +" ");
             }
     }  /* end_if */
   } /* end_function */
   

  /*  Objetivo : Valida y agrega un servicio disponible a la lista de 
               servicios adicionales ( Contratado, Solicitado, Removido )
  */
  function fxAddService() {      
    var form = document.frmdatos;
    var wn_cantServ = vServicioItem.size();
    var objServicio;
    var exclude_serv;
    /*  variable para separacion de las "x" en el combo "cmbSelectedServices"  */
    var txt_separacion="  ";
    //var txt_separacion="--";
    /* tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados */
    //var txt_vacios_2 = "          ";
    //var txt_vacios_2 = "##########";
    var txt_vacios_2 = "";
    //var txt_vacios_2 = "                    ";
                                                                                        
    /* Busca los roles que ya se seleccionaron */
    var aRolesSelected = new Array();
    var nameRol = "";
    var nameRol_selec = "";
    var idxARoles = 0;
    var i=0;
      
    /* Limpiamos el comboBox SelectedServices cuando se va a insertar el primer servicio */
    if (form.cmbSelectedServices.length == 1 && form.cmbSelectedServices.options[0].value == "") {
      deleteAllValues(form.cmbSelectedServices);
    }
   
    /* Guardamos los Roles de los servicios seleccionados */
    for (i=0;i<form.cmbSelectedServices.options.length;i++){
      nameRol = getRol(form.cmbSelectedServices.options[i].value);
      if (nameRol!="" && !valueExistsInArray(aRolesSelected,nameRol)){
        for(k=0; k<wn_cantServ; k++){
          objServicio = vServicioItem.elementAt(k);
          if (objServicio.nameShort == form.cmbSelectedServices.options[i].value && objServicio.modify_new == "S" ) {
            aRolesSelected[idxARoles] = nameRol;
            idxARoles++;
          }
        }
      };
    };
       
    /* Valido que no existan servicios excluyentes seleccionados en selectedServices */
    for (i=0; i<form.cmbAvailableServices.options.length; i++){
      if (form.cmbAvailableServices.options[i].selected){
        nameRol = getRol(form.cmbAvailableServices.options[i].value);
        if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
          alert("Ya existe un Servicio Excluyente seleccionado para "+ nameRol);
          return;
        };
      };
    };

    /* Valido que no existan servicios excluyente seleccionados en availableServices */
    for (i=0;i<form.cmbAvailableServices.options.length;i++){
      if (form.cmbAvailableServices.options[i].selected){
        nameRol = getRol(form.cmbAvailableServices.options[i].value);
                
        if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
          alert("Se han seleccionado varios Servicios Excluyentes de "+nameRol);
          return;
        }else if (nameRol!="" ){
          aRolesSelected[idxARoles] = nameRol;
          idxARoles++;
        }
      }
    }
       
       /* Pasar los elementos de disponbible a asignado */
       for (i=0;i<form.cmbAvailableServices.options.length;i++){
            if (form.cmbAvailableServices.options[i].selected){
                difServices = 0;
                if( form.cmbAvailableServices.options[i].text.length < longMaxServices )
                  difServices = longMaxServices - form.cmbAvailableServices.options[i].text.length;
                
                for( k = 0; k < difServices; k++ )
                  txt_vacios_2+=" ";
                  //txt_vacios_2+="#";
                
                var txt_texto   =  form.cmbAvailableServices.options[i].text + txt_vacios_2;
                //var txt_texto_2 = txt_texto.substr(0,12) + txt_separacion + " " + txt_separacion + "x";
                var txt_texto_2 = txt_texto.substr(0,longMaxServices) + txt_separacion + " " + txt_separacion + "x";
                
                //var txt_texto   =  form.cmbAvailableServices.options[i].text + txt_vacios_2;
                //var cantTexto = txt_texto.substr(0,12)
                //var txt_texto_2 = txt_texto.substr(0,12) + txt_separacion + " " + txt_separacion + "x";
                //var txt_texto_2 = txt_texto.substr(0,20) + txt_separacion + " " + txt_separacion + "x";
                fxAddToList(form.cmbSelectedServices, txt_texto_2, form.cmbAvailableServices.options[i].value);
    
                   for(j=0; j<wn_cantServ; j++){
                       objServicio = vServicioItem.elementAt(j);
                       //alert("Excluding ID : " + objServicio.exclude)
                       if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                           objServicio.modify_new = "S";
                           if( objServicio.exclude == "ALQ" )
                              alquilerServicesSelected++;
                           if( objServicio.exclude == "INT" )
                              rentServicesSelected++;
                           if( objServicio.exclude == "END" )
                              rentServicesSelectedEnd++;
                       }
                   }
             }
       }
      
       deleteSelectedValues(form.cmbAvailableServices);
       /*form.hdnFlagServicio.value = "1";
       bServiceModified = true;
       
       form.item_services.value = GetSelectedServices();*/
       
       return;
   } /* end _function addService*/
   
  function fxAddToList(listField, newText, newValue) {
    var len = listField.length++; /* Incrementa el tamaño de la lista y retorno el tamaño actual*/
    listField.options[len].value = newValue;
    listField.options[len].text  = newText;
  }
  
  // Función que elimina un servicio del combo de Servicios Seleccionados
  function fxEliminaServicio(objThis){    
    form = document.frmdatos;
    var txt_value_elimina;
    var txt_elimina_modify_new = "";
    var txt_modify;
    var aRolesSelected = new Array();
    var nameRol= "";
    var idxARoles = 0;
    var objServicio;
    var wn_cantServ;
    //alert(objThis.value);
    if (objThis !=  null){
       txt_value_elimina = objThis.value;
    }else{
       txt_value_elimina = form.cmbSelectedServices.value;
    }
    
       wn_cantServ = vServicioItem.size();

    /*  Inicio: Se valida que no exista un servicio del mismo grupo  */
    for (i=0 ; i<form.cmbSelectedServices.options.length; i++){
       nameRol = getRol(form.cmbSelectedServices.options[i].value);
       if (nameRol!="" && !valueExistsInArray(aRolesSelected,nameRol)){
          for(k=0; k<wn_cantServ; k++){
             objServicio = vServicioItem.elementAt(k);
             if (objServicio.nameShort == txt_value_elimina ){
                txt_elimina_modify_new = objServicio.modify_new;
             };
             if (objServicio.nameShort == form.cmbSelectedServices.options[i].value && objServicio.modify_new == "S" ) {
                aRolesSelected[idxARoles] = nameRol;
                idxARoles++;
             }
          }
       };
    };

    if (txt_elimina_modify_new == "N"){      
       nameRol = getRol(form.cmbSelectedServices.value);
       if (nameRol!="" && valueExistsInArray(aRolesSelected,nameRol)){
          alert("Ya existe un Servicio Excluyente seleccionado para "+ nameRol);
          return;
       };
    };
    /* Fin: Validación de grupo */


    deleteAllValues(form.cmbSelectedServices);
    /* variable para separacion de las "x" en el combo "cmbSelectedServices" */
    var txt_separacion="  ";
    /* tomamos como maximo 10 caracteres (blancos) para almacenar los servicios seleccionados */
    //var txt_vacios_2 = "          ";
    var txt_vacios_2 = "";
    for(i=0; i<wn_cantServ; i++){
       objServicio = vServicioItem.elementAt(i);
       
       difServices = 0;
       if( objServicio.name.length < longMaxServices )
         difServices = longMaxServices - objServicio.name.length;
      
       for( k = 0; k < difServices; k++ )
         txt_vacios_2+=" ";
       
       
       /*Antes obtenía el nombre corto*/
       //var txt_servicio = objServicio.nameShortDisplay + txt_vacios_2;
       /*Ahora obtengo el nombre largo*/
       var txt_servicio = objServicio.name + txt_vacios_2;
       
       var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);
       /* Si el Item tiene el servicio actualmente  */
       if (objServicio.active_new == "S"){
       //alert("objServicio.active_new == S");
          if (objServicio.nameShort == txt_value_elimina){
             if (objServicio.modify_new == "S"){ /* Si al Item continua o se le agrega el servicio */
                objServicio.modify_new = "N";                
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion + " ");                
             }
             else{                
                objServicio.modify_new = "S";
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +"x");
             }
          }
          else{
             /* Si al Item continua o se le agrega el servicio */
             if (objServicio.modify_new == "S"){                
                txt_modify ="x";
             }
             else{                  
                txt_modify =" ";
             }
             AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion + txt_modify);
          }
       }
       else {            
          //alert("objServicio else")          
          if (objServicio.active_new == "N" && objServicio.modify_new == "S"){            
             if (objServicio.nameShort == txt_value_elimina){
                //alert("objServicio.nameShort == txt_value_elimina")
                objServicio.modify_new = "N";
                
                if( objServicio.exclude == "ALQ" )
                   //parent.mainFrame.alquilerServicesSelected--;
                   alquilerServicesSelected--;
                if( objServicio.exclude == "INT" )
                   //parent.mainFrame.rentServicesSelected--;
                   rentServicesSelected--;
                if( objServicio.exclude == "END" )
                   //parent.mainFrame.rentServicesSelectedEnd--;
                   rentServicesSelectedEnd--;
                
                
                /*Antes mostraba el nombre corto*/
                //option1 =  new Option(objServicio.nameShortDisplay,objServicio.nameShort);
                /*Ahora mostraba el nombre largo*/
                option1 =  new Option(objServicio.name,objServicio.nameShort);
                form.cmbAvailableServices.options[form.cmbAvailableServices.length-1] = option1;
                form.cmbAvailableServices.options[form.cmbAvailableServices.length] = new Option("                                ");
             }
             else{                
                AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
             }
          }
       }
    } /* end_for */
    /*form.hdnFlagServicio.value = "1";
    bServiceModified = true;*/
  }; /* end_function */
  
  function getRol( nameShort ){
    var i=0;
    groupName = "";
    wn_cantServ = vServicioItem.size();
      
    for(i=0; i<wn_cantServ; i++){
      objServicio = vServicioItem.elementAt(i);
        if (objServicio.nameShort == nameShort){
          groupName = objServicio.exclude;
          break;
        }
    }
      
    return groupName;
  }
   
  function valueExistsInArray(aArreglo,valor,tipo){
    var i=0;
    bReturn = false;
    if (aArreglo!=null){
      if (aArreglo.length>0){
        for (i=0;i<aArreglo.length;i++){
          if (aArreglo[i]==valor){
            bReturn = true;
            break;  
          }
        }
      }else{
         bReturn = false;
      }
    }
    return bReturn;
  }
   
  onload = fxloadServicesItems;
   
   
   
</script>


<%}catch(Exception ex){
   ex.printStackTrace();
%>
  <script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>