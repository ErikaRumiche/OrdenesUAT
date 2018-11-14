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
<%@ page import="java.util.*"%>
<%
 try{
   String    objectType  = request.getParameter("objectType")==null?"":(String)request.getParameter("objectType");
   String    item_index  = request.getParameter("item_index")==null?"0":(String)request.getParameter("item_index");
   String    type_window = MiUtil.getString(request.getParameter("type_window"));
   String    objTypeEvent= MiUtil.getString(request.getParameter("objTypeEvent"));
   String    strFieldReadOnly = request.getParameter("strFieldReadOnly")==null?"":(String)request.getParameter("strFieldReadOnly"); //CEM COR0323 
   String    strItemId   = request.getParameter("strItemId"); 
   request.setAttribute("type_window",type_window);
   request.setAttribute("objTypeEvent",objTypeEvent);
   request.setAttribute("strItemId",strItemId);
   NewOrderService objNewOrderServicePopUp = new NewOrderService();
   GeneralService  objGeneralService = new GeneralService();
   Hashtable hshtinputNewSection = new Hashtable();
   HashMap hshValidateStock   = new HashMap();
   String    strCodigoCliente= "",
             strnpSite ="",
             strCodBSCS = "",
             strSpecificationId = "",
             strTypeCompany = "",
             strDivision = "",
             strStockFlag = "",
             strMessage   = "",
             strStatus = "",
             //JOYOLA, 06/03/2008, se agrego variable strSiteOppId
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strGeneratorType = "",
             strSessionId = "",
             strGeneratorId = "",
             /*JPEREZ: Se agrega strDispatchPlace*/
             strDispatchPlace = "",
	     strOrderId="",
             strSalesStuctOrigenId = "";
   
   strCodigoCliente   = (String)request.getParameter("strCustomerId");
   strSpecificationId = (String)request.getParameter("strSpecificationId");
   strnpSite          = (String)request.getParameter("strSiteId");
   strCodBSCS         = (String)request.getParameter("strCodBSCS");
   strDivision      = (String)request.getParameter("strDivisionId");
   
   strTypeCompany     = (String)request.getParameter("strTypeCompany");
   strStatus          = (String)request.getParameter("strStatus");
   strSiteOppId       = MiUtil.getString(request.getParameter("strSiteOppId"));
   strUnknwnSiteId    = MiUtil.getString(request.getParameter("strUnknwnSiteId"));
   strGeneratorType   = (String)request.getParameter("strGeneratorType");
   strSessionId       = (String)request.getParameter("strSessionId");   
   strGeneratorId     = (String)request.getParameter("strGeneratorId");
   strOrderId         = (String)request.getParameter("strOrderId");
   strSalesStuctOrigenId = (String)request.getParameter("strSalesStuctOrigenId");
   System.out.println("[PopUpOrder][strGeneratorType]"+strGeneratorType);
   System.out.println("[PopUpOrder][strGeneratorId]"+strGeneratorId);
   System.out.println("[PopUpOrder][strOrderId]"+strOrderId);
   System.out.println("[PopUpOrder][strSalesStuctOrigenId]"+strSalesStuctOrigenId);
	
   if( strSiteOppId.equals("0") || strSiteOppId.equals("null")    ) strSiteOppId = "";
   if( strUnknwnSiteId.equals("0")  || strUnknwnSiteId.equals("null") ) strUnknwnSiteId = "";
   strDispatchPlace = (String)request.getParameter("strDispatchPlace");
   
   String  flgClosePopUp = objGeneralService.getValue("CLOSE_POPUP_BY_ESPECIFICATION",strSpecificationId);
   
   if( !MiUtil.getString(strGeneratorType).equals("") ){
      if( strUnknwnSiteId!=null && !strUnknwnSiteId.equals("") ){
        strnpSite = strUnknwnSiteId;
      }else if( strSiteOppId!=null && !strSiteOppId.equals("")){
        strnpSite = strSiteOppId;
      }
   } 
   
   ArrayList objItemHEader = objNewOrderServicePopUp.ItemDAOgetItemHeaderSpecGrp(MiUtil.parseInt(strSpecificationId));
   
   /*hshtinputNewSection.put("strCustomerId",""+strCodigoCliente);
   hshtinputNewSection.put("strSiteId",""+strnpSite);
   hshtinputNewSection.put("strCodBSCS",""+strCodBSCS);
   hshtinputNewSection.put("strSpecificationId",""+strSpecificationId);
   hshtinputNewSection.put("strSolution",""+strSolution);
   hshtinputNewSection.put("strTypeCompany",""+strTypeCompany);*/
   
   hshtinputNewSection.put("strCodigoCliente",""+strCodigoCliente);
   hshtinputNewSection.put("strnpSite",""+strnpSite);
   hshtinputNewSection.put("strCodBSCS",""+strCodBSCS);
   hshtinputNewSection.put("hdnSpecification",""+strSpecificationId);
   hshtinputNewSection.put("strDivision",""+strDivision);
   hshtinputNewSection.put("strTypeCompany",""+strTypeCompany);
   hshtinputNewSection.put("strStatus",""+strStatus);
   hshtinputNewSection.put("strSiteOppId",""+strSiteOppId);
   hshtinputNewSection.put("strUnknwnSiteId",""+strUnknwnSiteId);
   hshtinputNewSection.put("strGeneratorType",""+strGeneratorType);
   hshtinputNewSection.put("strSessionId",""+strSessionId);
   hshtinputNewSection.put("strOrderId",""+strOrderId); //se agrego la orden
   hshtinputNewSection.put("strSalesStuctOrigenId",""+strSalesStuctOrigenId);
   request.setAttribute("hshtInputNewSection",hshtinputNewSection); 
   request.setAttribute("strSessionId",strSessionId);
   
    strStockFlag = "N";
    if (strSpecificationId != null){      
      hshValidateStock = objNewOrderServicePopUp.getValidateStock(MiUtil.parseInt(strSpecificationId), MiUtil.parseInt(strDispatchPlace)   );
      if (hshValidateStock!=null && hshValidateStock.size() > 0){
        strStockFlag = (String)hshValidateStock.get("wv_flag");
        strMessage = (String)hshValidateStock.get("wv_message");        
        if (strMessage!= null){         
          System.out.println("strMessage luego de llamar a getValidateStock ==="+strMessage);
          throw new Exception(strMessage);
        } 
      }
    }
   
%>
<html>
  <head>
    <title>Test Compania</title>
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>
    
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
     <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
     
  </head>
  
  <script defer>
  

  
  function fxIsValidate(){
    var nameHtmlControl      = "";
    var mandatoryControl     = "";
    var datatypeControl      = "";
    var valueControl         = "";
    
    
    //Verificar si tiene campos mandatorios
    for( x = 0; x < vctItemOrder.size(); x++ ){
      nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
      mandatoryControl  = vctItemOrder.elementAt(x).npvalidateflg;
      datatypeControl   = vctItemOrder.elementAt(x).npdatatype;
      objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
      //alert("Datos : " + nameHtmlControl + " mandatoryControl : " + mandatoryControl  + " -> " + mandatoryControl.length)
      
        if( mandatoryControl == "S" ){
          //alert("Entramos a mandatoryControl ");
          valueControl  = eval("form." + nameHtmlControl + ".value");
            
            if( vctItemOrder.elementAt(x).npcontroltype == "TEXT" ){
              if( trim(valueControl).length == 0 ){
                if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                  alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatoria");
                  eval("form." + nameHtmlControl + ".focus()");
                  return false;
                }
              }
              
            }else if( vctItemOrder.elementAt(x).npcontroltype == "SELECT" ){
              if( valueControl == "" ){
                if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                  alert("La opción '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatoria");
                  eval("form." + nameHtmlControl + ".focus()");
                  return false;
                }
              }
            }else if( vctItemOrder.elementAt(x).npcontroltype == "OTRO" ){
              if( valueControl == "" ){
                if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                  alert("La opción '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatoria");
                  //eval("form." + nameHtmlControl + ".focus()");
                  return false;
                }
              }
            }
            
      }//Fin del if mandatory
    }//Fin del for
    
    //Verificar si los valores ingresados son del Tipo de DATO que se requiere
    for( x = 0; x < vctItemOrder.size(); x++ ){
      nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
      mandatoryControl  = vctItemOrder.elementAt(x).npvalidateflg;
      readOnlyControl   = vctItemOrder.elementAt(x).npobjreadonly;
      datatypeControl   = vctItemOrder.elementAt(x).npdatatype;
      objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
      //alert("Datos : " + nameHtmlControl + " mandatoryControl : " + mandatoryControl  + " -> " + mandatoryControl.length)
      
        //alert("Entramos a mandatoryControl ");
        valueControl  = eval("form." + nameHtmlControl + ".value");
          //Solo es válido para los campos TEXT
          if( vctItemOrder.elementAt(x).npcontroltype == "TEXT" ){
              if( datatypeControl == "<%=Constante.VALIDATE_INTEGER%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if( !ContentOnlyNumber(valueControl) ){
                    alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS");
                    eval("form." + nameHtmlControl + ".select()");
                    eval("form." + nameHtmlControl + ".focus()");
                    return false;
                  }
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_INT_POSITIVE%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if( !ContentOnlyNumber(valueControl) || parseInt(valueControl) == 0 ){
                    alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS positivos");
                    eval("form." + nameHtmlControl + ".select()");
                    eval("form." + nameHtmlControl + ".focus()");
                    return false;
                  }
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_FLOAT%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if( !ContentOnlyNumberDec(valueControl) ){
                    alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos DECIMALES");
                    eval("form." + nameHtmlControl + ".select()");
                    eval("form." + nameHtmlControl + ".focus()");
                  return false;
                  }
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_DATE%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                    if( !isValidDate(valueControl) ){
                      eval("form." + nameHtmlControl + ".select()");
                      eval("form." + nameHtmlControl + ".focus()");
                      return false;
                    }
                  }  
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_PLUS%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                    if( !fxCheckDateCurrent(valueControl) ){
                      eval("form." + nameHtmlControl + ".select()");
                      eval("form." + nameHtmlControl + ".focus()");
                      return false;
                    }
                  }
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_TIME%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                    if( !fxValidateDateTime(valueControl) ){
                      eval("form." + nameHtmlControl + ".select()");
                      eval("form." + nameHtmlControl + ".focus()");
                      return false;
                    } 
                  }
                }
              }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_TIME_PLUS%>" ){
                //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                  if (eval("form." + nameHtmlControl + ".disabled == false") == true){         
                    if( !fxValidateDateTimePlus(valueControl) ){
                      eval("form." + nameHtmlControl + ".select()");
                      eval("form." + nameHtmlControl + ".focus()");
                      return false;
                    }
                  }
                }
              }
          }
          
    }//Fin del for
       
    return true;
  }//Fin de Function
  
  function fxValidateGeneral(){
    <%if(!objectType.equals("INC")){%>
    if( !fxValidateSA() ) return false;
    <%}%>
    <%if(!objectType.equals("INC")){%>
    if( !fxValidateMatch() ) return false;
    <%}%>
    <%if(!objectType.equals("INC")){%>
    if( !fxValidateChangeBag() ) return false;
    <%}%>
    return true;
  }
  
  /**MVERAE
   Objetivo : Permite validar la edicion del item seleccionado.
 **/
  function fxValidateGeneralEdit(){
    if( !fxValidateSA() ) return false;
    if( !fxValidateMatchEdit() ) return false;
    if( !fxValidateChangeBag() ) return false;
    return true;
  }
  
 /**Odubock
   Objetivo : Permite validar que el ingreso del nuevo producto Bolsa sea diferente al que ya tenía.
 **/
 function fxValidateChangeBag(){
  // Se anula la validacion Bolsa Fase III. Si se puede seleccionar el mismo producto bolsa
  /*  
    var form = parent.mainFrame.frmdatos;    
    if ("<%=strSpecificationId%>" == 2022){
       if(form.cmb_ItemProductBolsaId.value == form.cmb_ItemProducto.value){
          alert("Para realizar la transacción necesita cambiar el tipo de Bolsa de Minutos actual; transacción de CAMBIO de Bolsa no permitida.");
          form.cmb_ItemProducto.focus();
          return false;
       }else
          return true;
    }else
    */
       return true;  
 }

  /*Developer : Lee Rosales
    Objetivo  : Validar que un item ingresado no se repita. Por ahora
                se considera como PK el Teléfono o el IMEI o el contrato
  */
  function fxValidateMatch(){
    var cntTablesItems = parent.opener.items_table.rows.length;
    var openerForm     = parent.opener.frmdatos;
    
    if( cntTablesItems == 1 ) return true;
    
    if( !fxIsNecessaryValidate() ) return true;
    //alert("Entraremos a buscar : " + vctItemOrder.size())
    //Recorro cada fila del ITEM
    for( i = 0; i < (cntTablesItems-1); i ++ ){
       //Recorro los datos a pasar
       for(j = 0; j < vctItemOrder.size(); j ++ ){
          npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
          npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;
          
          if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
            if( npobjitemheaderid != 22 && npobjitemheaderid != 25 && npobjitemheaderid != 72 ){
              //Si es un SIM (IMEI para Eagle)/Teléfono/IMEI/Nro Contrato
              if( npobjitemheaderid == 2 || npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){
              //alert("Entramos : " + objitemheaderid )
                //Si es un contrato
                if( npobjitemheaderid == 27 && (
                    ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                    ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
                    )
                    ){
                  continue;
                }
                
                //Si es un SIM (IMEI para Eagle)
                if( npobjitemheaderid == 2 && (
                    ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                    ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
                    )
                    ){
                  continue;
                }
                
                npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;
                if( cntTablesItems == 2 )
                  npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
                else
                  npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
                //Comparamos el valor actual y el valor de la cabecera de la fila
                //alert("Valor Actual : " + npobjitemvalue + " -> Valor Cabecera : " + npitemobjectvalue );
                  if( npitemobjectvalue == npobjitemvalue ){
                    alert("Este registro ya fue ingresado. Ingrese otros datos"); 
                    return false;
                  }
              }
            }//Fin del If de las excepciones de controles
          }//Fin del If de distinto de null
      }//Fin del for del vector del PopUp
    }//Fin del for de ITEM's

    
    
    return true;
  }
  
  /*Developer : MVERAE
    Objetivo  : Validar que un item editado no se repita. Por ahora
                se considera como PK el Teléfono o el IMEI o el contrato
  */
  function fxValidateMatchEdit(){
    var cntTablesItems = parent.opener.items_table.rows.length;
    var openerForm     = parent.opener.frmdatos;
    
    if( cntTablesItems == 1 ) return true;
    
    if( !fxIsNecessaryValidate() ) return true;
    //alert("Entraremos a buscar : " + vctItemOrder.size())
    //Recorro cada fila del ITEM
    for( i = 0; i < (cntTablesItems-1); i ++ ){
       //Recorro los datos a pasar
       for(j = 0; j < vctItemOrder.size(); j ++ ){
          npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
          npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;
          if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
            if( npobjitemheaderid != 22 && npobjitemheaderid != 25 && npobjitemheaderid != 72 ){
              if( npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){
                
                //Si es un contrato
                if( npobjitemheaderid == 27 && (
                    ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                    ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
                    )
                    ){
                  continue;
                }     
                
                //Si es un SIM (IMEI para Eagle)
                if( npobjitemheaderid == 2 && (
                    ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                    ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
                    )
                    ){
                  continue;
                }

                npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;
                if( cntTablesItems == 2 )
                  npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
                else
                  npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
                //Comparamos el valor actual y el valor de la cabecera de la fila
                //alert("Valor Actual : " + npobjitemvalue + " -> Valor Cabecera : " + npitemobjectvalue );
                  if(i!= <%=item_index%>){//el indice tiene que ser distinto al indice del item editado
                    if( npitemobjectvalue == npobjitemvalue ){
                      alert("Este registro ya fue ingresado. Ingrese otros datos"); 
                      return false;
                    }
                  }
              }
            }//Fin del If de las excepciones de controles
          }//Fin del If de distinto de null
      }//Fin del for del vector del PopUp
    }//Fin del for de ITEM's

    
    
    return true;
  }
    
  function fxIsNecessaryValidate(){
    for(j = 0; j < vctItemOrder.size(); j ++ ){
        npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
        //Por ahora solo se validan los teléfonos, IMEIS, Contratos
        if( npobjitemheaderid == 2 || npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 )
        return true;
    }
    return false;
  }
  
  function fxGetObjectByHeaderId(objHeaderId){
    for( x = 0; x <parent.opener.vctItemHeaderOrder.size(); x++ ){
        objitemheaderid   = parent.opener.vctItemHeaderOrder.elementAt(x).npobjitemheaderid;
         if( objHeaderId == objitemheaderid )
          return parent.opener.vctItemHeaderOrder.elementAt(x); 
    }
  }
  
  function fxValidateSA(){
    var form = document.frmdatos;
    for( x = 0; x < vctItemOrder.size(); x++ ){
      objheaderid = vctItemOrder.elementAt(x).npobjitemid;
      //Si existen servicios adicionales
      if( objheaderid == 23 ){
         //Si la categoría es de Banda Ancha
         if( <%=strDivision%> == 2 ){
            //Obtengo los SA Seleccionados
            //alert("Tamaño : " + parent.mainFrame.vServicioPorDefecto.size() );
            //alert("rentServicesSelected : " + parent.mainFrame.rentServicesSelected );
            //alert("alquilerServicesSelected : " + parent.mainFrame.alquilerServicesSelected );
            //Si es un Servicio de Enlace de Datos
             if(form.cmb_ItemSolution.value == 4){
               if ("<%=strSpecificationId%>" == "<%=Constante.KN_VTA_INTERNET_ACC_INTERNET%>" || "<%=strSpecificationId%>" == "<%=Constante.KN_VTA_INTERNET_ENLACE_DATOS%>"){
                  if( parent.mainFrame.rentServicesSelected == 0 ){
                     alert("Debe seleccionar al menos un servicio garantizado");
                     return false;
                  }
               }
            }
            if(form.cmb_ItemSolution.value == 5){
               if ("<%=strSpecificationId%>" == "<%=Constante.KN_VTA_INTERNET_ACC_INTERNET%>" || "<%=strSpecificationId%>" == "<%=Constante.KN_VTA_INTERNET_ENLACE_DATOS%>"){
                  if( parent.mainFrame.rentServicesSelectedEnd == 0 ){
                     alert("Debe seleccionar al menos un servicio garantizado");
                     return false;
                  }
              }
            }
            
            if( parent.mainFrame.alquilerServicesSelected == 0 ){
              alert("Debe seleccionar al menos un servicio de alquiler");
              return false;
            }
            
            return true;
         }
         
      }
    }
    
    return true;
  }
  
  /*Developer: Hugo Moreno
    Objetivo : Valida que todos los ITEMS sean de un mismo tipo de moneda
  */
  function fxCurrencyValidate(v_moneda, v_flag){
     var form = document.frmdatos;
      
     //Cuando es llamado desde un Incidente
     if (parent.opener.items_table==null){
        return true; // indica que no hay problema con la moneda
     }
     
     v_numRows = parent.opener.items_table.rows.length;   
          
     // NEW ORDEN
     if (parent.opener.frmdatos.hdnCurrency != null){        
        //Cuando se agrega el primer ITEM, su moneda será la GUÍA           
        if (v_numRows == 1){                
           parent.opener.frmdatos.hdnCurrency.value = v_moneda;                                   
        }
        else{
        //Cuando ya exista un ITEM, se debe validar que sea del mismo tipo de moneda que la GUÍA                      
           if (v_numRows == 2){
              if (v_flag == 0)
                 v_flagCurrency = parent.opener.frmdatos.hdnCurrency.value;
               else{
                 v_flagCurrency = v_moneda;            
                 parent.opener.frmdatos.hdnCurrency.value = v_moneda;
               }
           }
           if (v_numRows > 2)
              v_flagCurrency = parent.opener.frmdatos.hdnCurrency.value;                                     
           if (v_moneda != v_flagCurrency){           
              alert("Ingresar Productos con el mismo tipo de moneda"); 
              return false;
           }   
         }        
     } 
     // EDIT ORDEN
     else{     
        // Si hay más de 1 ROW, se toma la primera fila como GUÍA        
        if (v_numRows > 2)
           parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda[1].value;
        else{
           // Si viene de NUEVO ITEM
           if (v_flag == 0){
              if (v_numRows == 1)
                 parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;     
              else
                 parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda.value;     
           }
           // Si viene de EDIT ITEM
           else
              parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;     
        }     
        if (v_numRows == 1)
           // Si viene de NUEVO ITEM
           if (v_flag == 0)
              parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;     
           //Si viene de EDIT ITEM
           else 
              parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda[1].value;     
        else{           
           if (v_numRows > 1) {                    
              v_flagCurrency = parent.opener.frmdatos.hdnCurrencyEdit.value;              
              if (v_moneda != v_flagCurrency){           
                 alert("Ingresar Productos con el mismo tipo de moneda"); 
                 return false;
              }   
           }
        }     
     }
     return true;
  }
  
  /*Developer: Lee Rosales
    Objetivo : Crea un nuevo item
  */
  function fxSendItemValuesOrder(){
    var form = document.frmdatos;
    var nameHTMLInput      = "";
    var valueDescription   = "";
    try{
        form.item_services.value = GetSelectedServices();
    }catch(e){}
    
    var bResult;
    if( fxIsValidate() ){    
      if(form.txt_ItemMoneda != null){
         bResult = fxCurrencyValidate(form.txt_ItemMoneda.value, 0);
      } 
      else{
         bResult = true;  
      }      
      
      if (bResult){      
      for( x = 0; x < vctItemOrder.size(); x++ ){
          nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
          
          /*Obtenemos el Value*/
          vctItemOrder.elementAt(x).npobjitemvalue    =   eval("form." + nameHTMLInput + ".value");
			 
			 /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/
			 
			 if( vctItemOrder.elementAt(x).npobjheaderid == 52 || vctItemOrder.elementAt(x).npobjheaderid == 12 ){

            if( eval("form." + nameHTMLInput + ".value") == "SI" ) vctItemOrder.elementAt(x).npobjitemvalue = "S";

            else if( eval("form." + nameHTMLInput + ".value") == "NO" ) vctItemOrder.elementAt(x).npobjitemvalue = "N";

            else  vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");

          }

          /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/

			 
          /*Obtenemos la Description*/
          
          if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" )
            valueDescription  = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
          else{            
            valueDescription  = eval("form." + nameHTMLInput + ".value");
          }
          
          //alert("Nombre : "+nameHTMLInput+" -> Value : " + vctItemOrder.elementAt(x).npobjitemvalue + " -> Desc : " + valueDescription )
          vctItemOrder.elementAt(x).npobjitemvaluedesc = valueDescription;
          vctItemOrder.elementAt(x).npobjitemflagsave = 'N';  
      }
      /*Si el item se ha modificado, se setea un flag*/  
      <%if(!objectType.equals("INC")){%>
      fxValidateItemChange();
      <%}%>
    
      //Ahora hacemos una validación General
      if( !fxValidateGeneral() ) return false;
                                      
      fxCalculateTotalItem();
      //JPEREZ: Evalúa la modalidad de salida para el ítem
      try{
        var strModality = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
        if ( !fxValidateItemModality("<%=strSpecificationId%>", strModality, -1) ){
          alert("La Modalidad de Salida debe ser igual para todos los ítemes de la orden");
          return false;
        }
      }catch(e){}  
      
      //KSALVADOR: Se agrega nuevo parametro Tipo:(Nuevo/Usados) para validar en el stock a que subalmacen pertnecen
      //Actualmente este valor sólo se encuentra configurado para Reposiciones
      try{
        var strTipo = "";
            strTipo = form.cmbItemProductStatus.value;
       }catch(e){
      }  
      
      //Validación de Stock  - JPEREZ
      if ("<%=strStockFlag%>"=="Y" ){ //VERIFICAR FLAG("N")
         //Debe validarse stock para la categoría ===                      
         var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strSpecificationId=<%=strSpecificationId%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strTipo="+strTipo"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&hdnMethod=getStockMessage";
         parent.bottomFrame.location.replace(url);
         
      }else{        
        //Si estoy en la página de EDICIÓN
        <%if( objTypeEvent.equals("EDIT") ){%>
            //Si es abierta desde la página de INCIDENTES
            <%if(objectType.equals("INC")){%>
            parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
            //Si es abierta desde la página de ÓRDENES
            <%}else{%>
            var intSimQuantity = parent.mainFrame.validateMassiveFrame.intQuantitySIMPropios;
            var arrSIMPropio   = parent.mainFrame.validateMassiveFrame.arrSIMPropio;
            parent.opener.fxAddRowOrderItemsGlobalEditBySimPropio(vctItemOrder,intSimQuantity,arrSIMPropio);
            <%}%>
        //Si estoy en la página de CREACIÓN
        <%}else if( objTypeEvent.equals("NEW") ){ %>
            //Si es abierta desde la página de INCIDENTES
            <%if(objectType.equals("INC")){%>
            parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobal(vctItemOrder);
            //Si es abierta desde la página de ÓRDENES
            <%}else{%>
            var intSimQuantity = parent.mainFrame.validateMassiveFrame.intQuantitySIMPropios;
            var arrSIMPropio   = parent.mainFrame.validateMassiveFrame.arrSIMPropio;
            parent.opener.fxAddRowOrderItemsGlobalBySimPropio(vctItemOrder,intSimQuantity,arrSIMPropio);
            <%}%>
        <%}%>
            
        parent.opener.deleteItemEnabled       =	true;
        parent.mainFrame.frmdatos.btnAceptar.disabled = true;
            
        <%if(flgClosePopUp!=null && (!flgClosePopUp.equals(Constante.NPERROR))){%>
          parent.close();
        <%}%>
        
        //Reiniciar todo y se puede seleccionar nuevamente
        formResetGeneral();
      }//Fin de ELSE
      
      }
    
    }//Fin de isValidate()
      
   }
   
   /*Developer: Lee Rosales
     Objetivo : Actualiza un item
   */
   function fxSendItemValuesEditOrder(){         
   /****************INICIO DE RECOGER ACTUALIZAR EL VECTOR*********************/
      form = document.frmdatos;
      
      try{
        form.item_services.value = GetSelectedServices();
      }catch(e){}
     if(fxIsValidate()){      
        var bResult;        
        if(form.txt_ItemMoneda != null){
           bResult = fxCurrencyValidate(form.txt_ItemMoneda.value, 1);
        } 
        else{
           bResult = true;  
        }
        
        if (bResult){
           for( x = 0; x < vctItemOrder.size(); x++ ){        
              var nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;        
              //alert(nameHTMLInput);
              /*Obtenemos el Value*/
              vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");      
				  
				 /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/
				 
				 if( vctItemOrder.elementAt(x).npobjheaderid == 52 || vctItemOrder.elementAt(x).npobjheaderid == 12 ){
	
					if( eval("form." + nameHTMLInput + ".value") == "SI" ) vctItemOrder.elementAt(x).npobjitemvalue = "S";
	
					else if( eval("form." + nameHTMLInput + ".value") == "NO" ) vctItemOrder.elementAt(x).npobjitemvalue = "N";
	
					else  vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");
	
				 }

					/**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/
				  
				  
              /*Obtenemos la Description*/
              if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" ){
                 var valie = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                 vctItemOrder.elementAt(x).npobjitemvaluedesc = valie;
              }else{
                 vctItemOrder.elementAt(x).npobjitemvaluedesc = eval("form." + nameHTMLInput + ".value");
              }          
              vctItemOrder.elementAt(x).npobjitemflagsave = 'A';          
           }      
           var lista_servicios = GetSelectedServices();
      
           /*Si el item se ha modificado, se setea un flag*/  
           <%if(!objectType.equals("INC")){%>
           fxValidateItemChange();
           <%}%>
           
           <%if(!objectType.equals("INC")){%>
           //fxValidateItemChangeWithGuide();
           
            var isChangeGuide     = 0;
            var isChangePlanRate  = 0;
            var isChangePrice     = 0;
            var isChangePriceExc  = 0;
              for(j = 0; j < vctItemOrder.size(); j ++ ){
                //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
                if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
                    vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
                    vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
                    vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
                    vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
                    vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
                    vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción
                  
                  if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
                    isChangeGuide = 1;
                    //alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
                    //break;
                  }
                }else{
                    //Plan Tarifario
                    if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                      isChangePlanRate = 1;
                    }
                    //Precio
                    if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                      isChangePrice = 1;
                    }
                    //Precio de Excepción
                    if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                      isChangePriceExc = 1;
                    }
                  
                }
              }
           
           <%}%>
           
           <%if(!objectType.equals("INC")){%>
           //Ahora hacemos una validación General
           
           if( !fxValidateGeneralEdit() ) return false;
           <%}%>
           fxCalculateTotalItem();
           
           //JPEREZ: Evalúa la modalidad de salida para el ítem
           try{
             var strModality = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
             if ( !fxValidateItemModality("<%=strSpecificationId%>", strModality, item_index) ){
              alert("La Modalidad de Salida debe ser igual para todos los ítemes de la orden");
              return false;
             }
           }catch(e){}
                
           //Validación de Stock  - JPEREZ
           if ("<%=strStockFlag%>"=="Y" ){
             //Debe validarse stock para la categoría
             var url = "<%=request.getContextPath()%>/itemServlet?&strMode='EDIT'&strSpecificationId=<%=strSpecificationId%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&hdnMethod=getStockMessage";
             parent.bottomFrame.location.replace(url);
           }else{
             <%if(objectType.equals("INC")){%>
             parent.opener.parent.mainFrame.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>);
             <%}else{%>
             parent.opener.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>,isChangeGuide,isChangePlanRate);
           <%}%>
           /****************FIN DE RECOGER ACTUALIZAR EL VECTOR*********************/
           <%if(!objectType.equals("INC")){%>
           parent.opener.fxTransferVectorItems(vctItemOrder,<%=item_index%>);
           <%}%>
           //Cerrar la ventana
           fxCancelItemEditWindow();
         }// if result
      }//if validate
    }
  }
   
  /**
  Objetivo :  Permite calcular o recalcular el precio del monto total del ITEM
  Developer:  Lee Rosales
  Fecha    :  08/09/2007
  **/
  function fxCalculateTotalItem(){
    var precioCta = "0";
    var precioExc = "";
    var cantidad  = "1";
    
    //En caso la categoría contenga el campo TOTAL
    if( getTotal() != -1 ){
 
    for( x = 0; x < vctItemOrder.size(); x++ ){
      objitemid = vctItemOrder.elementAt(x).npobjitemid;
        if( objitemid == 17 ){
          nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
          precioCta  = eval("form." + nameHTMLInput + ".value");
        }if( objitemid == 18 ){
          nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
          precioExc  = eval("form." + nameHTMLInput + ".value");
        }if( objitemid == 20 ){
          nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
          cantidad   = eval("form." + nameHTMLInput + ".value");
        }
    }/*Fin del for*/
    
    if( precioExc != "" ) precioCta = precioExc;
    vctItemOrder.elementAt(getTotal()).npobjitemvalue       =   parseFloat(precioCta) * parseInt(cantidad);
    vctItemOrder.elementAt(getTotal()).npobjitemvaluedesc   =   parseFloat(precioCta) * parseInt(cantidad);
    }/*Fin del if(getTotal())*/
  }

  /**
  Objetivo :  Indica si para la categoría actual existe el campo TOTAL
  Developer:  Lee Rosales
  Fecha    :  08/09/2007
  **/
  function getTotal(){
    for( x = 0; x < vctItemOrder.size(); x++ ){
      objitemid = vctItemOrder.elementAt(x).npobjitemid;
      if( objitemid == 22 ) return x;
    }
    return -1;
  }
   
  function fxValidateDateTime(objectValue){
    var wv_fecha_firma         = objectValue.substring(0,10);
    var wv_hora_firma          = objectValue.substring(11,16);
   
    if (!isValidDate(wv_fecha_firma) || !isValidHour(wv_hora_firma) ){
      return false;
    }
     
    return true;
  }
  
  function fxValidateDateTimePlus(objectValue){
    var wv_fecha_firma         = objectValue.substring(0,10);
    var wv_hora_firma          = objectValue.substring(11,16);
   
    if( !fxCheckDateCurrent(wv_fecha_firma) )
      return false;
    if (!isValidDate(wv_fecha_firma) || !isValidHour(wv_hora_firma) ){
      return false;
    }
     
    return true;
  }
  
  function fxCheckDateCurrent(obj) {
   var wv_datestr = obj;
   var wv_hoy_str = new Date();
   var day_act    = parseFloat(wv_hoy_str.getDate());
   var month_act  = parseFloat(wv_hoy_str.getMonth()+1);
   var year_act   = parseFloat(wv_hoy_str.getYear());
   
   var wv_message = "La fecha debe ser mayor o igual a la fecha actual";
   
   if ( wv_datestr != "" ){
      if ( !isValidDate(wv_datestr)){
         //obj.select();
         return false;
      };
   
      var day_new    = parseFloat(wv_datestr.substring(0,2));
      var month_new  = parseFloat(wv_datestr.substring(3,5));
      var year_new   = parseFloat(wv_datestr.substring(6,10));
      if (year_new < year_act){
         alert(wv_message);
         //obj.select();
         return false;
      };
      if (year_new == year_act){
         if (month_new < month_act){
            alert(wv_message);
            //obj.select();
            return false;
         };
         if (month_new == month_act){
            if (day_new < day_act){
               alert(wv_message);
               //obj.select();
               return false;
            };
         }
      }
   }
   return true;
  };
   
  /**
  Objetivo :  Permite cerrar la ventana del ITEM
  Developer:  Lee Rosales
  Fecha    :  08/09/2007
  **/
  function fxCancelItemEditWindow() {
  <%if(objectType.equals("INC")){%>
    parent.opener.parent.mainFrame.document.frmdatos.flgSave.value="0";
    parent.opener.parent.mainFrame.deleteItemEnabled       =	true;
    parent.close();
  <%}else{%>
    parent.opener.document.frmdatos.flgSave.value="0";
    parent.opener.deleteItemEnabled       =	true;
    parent.close();
  <%}%>
  }
  
  /**
  Objetivo :  Permite obtener 
  Developer:  Lee Rosales
  Fecha    :  08/09/2007
  **/
  function fxValidateItemChangeWithGuide(){
    var isChangeGuide     = 0;
    var isChangePlanRate  = 0;
    var isChangePrice     = 0;
    var isChangePriceExc  = 0;
    for(j = 0; j < vctItemOrder.size(); j ++ ){
      //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
      if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
          vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
          vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
          vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
          vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
          vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
          vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción
        
        if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
          isChangeGuide = 1;
          alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
          //break;
        }
      }else{
          //Plan Tarifario
          if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
            isChangePlanRate = 1;
          }
          //Precio
          if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
            isChangePrice = 1;
          }
          //Precio de Excepción
          if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
            isChangePriceExc = 1;
          }
        
      }
    }
    if( isChangeGuide == 1 ){
      alert("hubieron cambios que afectan a la guía");
    }else if( isChangePlanRate == 1 || isChangePrice == 1 || isChangePriceExc == 1 ){
      alert("hubieron cambios que NO afectan a la guía");
      if( isChangePlanRate == 1 ){
        alert("hubieron cambios en Cambio de Plan");
      }
      if( isChangePrice == 1 ){
        alert("hubieron cambios en Precio");
      }
      if( isChangePriceExc == 1 ){
        alert("hubieron cambios en Precio Excepción");
      }
    }
  }
   
  /*JPEREZ 
  Valida si es que un ítem ha sido editado
  */
  function fxValidateItemChange(){
  //vctItemOrderOriginal  = vctItemOrder ;       
    for(j = 0; j < vctItemOrder.size(); j ++ ){        
      if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){          
        parent.opener.document.frmdatos.hdnChangedOrder.value="S";
        break;
      }
   }
  }
   
   //JPEREZ: Valida que los ítemes de la orden sean de la misma modalidad de salida
   function fxValidateItemModality(specificationId, strItemModality, itemIndex){
      //itemIndex == -1 => ítem nuevo
       //Por ahora sólo para el caso de Venta Prepago (2002) y prepago tde. y reposicion prepago tde
      var cntTablesItems = parent.opener.items_table.rows.length;
      var npitemobjectvalue;      
      var openerForm     = parent.opener.frmdatos;
      if( cntTablesItems == 1 ) return true;       
      if (specificationId == <%=Constante.SPEC_PREPAGO_NUEVA%> || specificationId == <%=Constante.SPEC_PREPAGO_TDE%> 
     || specificationId == <%=Constante.SPEC_REPOSICION_PREPAGO_TDE%> ){ //TDECONV034        
        for( i = 0; i < (cntTablesItems-1); i ++ ){        
          //Recorro los datos a pasar         
          if ( (cntTablesItems == 2) && (itemIndex!=i+1) ){
            npitemobjectvalue = npitemobjectvalue = parent.opener.frmdatos.txtItemModality.value;           
            if (strItemModality != npitemobjectvalue) return false;
          }
          else if (itemIndex!=i+1){
            npitemobjectvalue = npitemobjectvalue = parent.opener.frmdatos.txtItemModality[i].value;          
            if (strItemModality != npitemobjectvalue) return false;
          }
        }
        return true;
      }
      else
        return true;
   }
   
   /*
   JPEREZ
   Copia un vector   
   */
   function fxCopyVector(vctOriginal, vctCopia){
      for( x = 0; x < vctOriginal.size(); x++ ){         
         var objMake = new fxMakeOrderItem(
                              vctOriginal.elementAt(x).npobjitemheaderid,
                              vctOriginal.elementAt(x).npobjspecgrpid,
                              vctOriginal.elementAt(x).npobjitemid,
                              vctOriginal.elementAt(x).npobjitemname,
                              vctOriginal.elementAt(x).namehtmlheader,
                              vctOriginal.elementAt(x).namehtmlitem,
                              vctOriginal.elementAt(x).npcontroltype,
                              vctOriginal.elementAt(x).npdefaultvalue,
                              vctOriginal.elementAt(x).npsourceprogram,
                              vctOriginal.elementAt(x).npspecificationgrpid,
                              vctOriginal.elementAt(x).npdisplay,
                              vctOriginal.elementAt(x).npobjreadonly,
                              vctOriginal.elementAt(x).npobjitemvalue,
                              vctOriginal.elementAt(x).npobjitemvaluedesc
                              );        
        vctCopia.addElement(objMake);     
      }
                    
   }
   
   /*
   JPEREZ
   Maneja la respuesta de stock   
   */
   function fxStockResponse(strStockFlag, strStockMessage, strMode){
      if ( (strStockFlag == "Y") && confirm(strStockMessage) ){
         if (strMode == "NEW")
            fxAddItem();
         else
            fxUpdateItem();
      }else if (strStockFlag == "N"){
         if (strMode == "NEW")
            fxAddItem();
         else
            fxUpdateItem();
      }
   }
   
   /*
   JPEREZ
   Agrega un ítem a la orden. 
   */
   function fxAddItem(){
    <%if( objTypeEvent.equals("EDIT") ){%>
          <%if(objectType.equals("INC")){%>
          parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
          <%}else{%>
          var intSimQuantity = parent.mainFrame.validateMassiveFrame.intQuantitySIMPropios;
          var arrSIMPropio   = parent.mainFrame.validateMassiveFrame.arrSIMPropio;
            parent.opener.fxAddRowOrderItemsGlobalEditBySimPropio(vctItemOrder,intSimQuantity,arrSIMPropio);
          <%}%>
      <%}else if( objTypeEvent.equals("NEW") ){ %>
          <%if(objectType.equals("INC")){%>
          parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobal(vctItemOrder);
          <%}else{%>
          parent.mainFrame.frmdatos.btnAceptar.disabled = true;
          var intSimQuantity = parent.mainFrame.validateMassiveFrame.intQuantitySIMPropios;
          var arrSIMPropio   = parent.mainFrame.validateMassiveFrame.arrSIMPropio;
            parent.opener.fxAddRowOrderItemsGlobalBySimPropio(vctItemOrder,intSimQuantity,arrSIMPropio);
          <%}%>
      <%}%>
          parent.opener.deleteItemEnabled       =	true;
          //parent.close();
          //Reiniciar todo y se puede seleccionar nuevamente
          formResetGeneral();
   
   }

  function fxUpdateItem(){
    <%if(objectType.equals("INC")){%>
      parent.opener.parent.mainFrame.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>);
    <%}else{%>
      var isChangeGuide     = 0;
      var isChangePlanRate  = 0;
      var isChangePrice     = 0;
      var isChangePriceExc  = 0;
        for(j = 0; j < vctItemOrder.size(); j ++ ){
          //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
          if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
              vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
              vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
              vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
              vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
              vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
              vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción
            
            if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
              isChangeGuide = 1;
              alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
              //break;
            }
          }else{
              //Plan Tarifario
              if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                isChangePlanRate = 1;
              }
              //Precio
              if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                isChangePrice = 1;
              }
              //Precio de Excepción
              if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                isChangePriceExc = 1;
              }
            
          }
        }
      parent.opener.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>,isChangeGuide,isChangePlanRate);
    <%}%>
        
  /****************FIN DE RECOGER ACTUALIZAR EL VECTOR*********************/
       
    <%if(!objectType.equals("INC")){%>
      parent.opener.fxTransferVectorItems(vctItemOrder,<%=item_index%>);
    <%}%>
        
    //Cerrar la ventana
    fxCancelItemEditWindow();   
  }
  /*Odubock; valida que para el campo fecha disbaled, no tenga acción sobre el calendario */
  function preShowCalendar(obj,valor1,valor2,strFormato){
     if(eval(obj+".disabled == false") == true){
        show_calendar(obj,valor1,valor2,strFormato);
     }
  }
  
  </script>
  
  <body> 
  <form method="post" name="frmdatos">
  <!-- <input type="hidden" name="hdnCurrency" value="">-->
  <table  align="center" width="100%" border="0">
  
  <tr><td>      
    
    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
       <tr class="PortletHeaderColor">
          <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
          <td class="PortletHeaderColor" align="left" valign="top">
          <font class="PortletHeaderText">
          Item de Orden
          </font></td>
          <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
       </tr>
    </table>
   </td></tr>
   <tr><td>   
   
   <!--Sección de objetos de ITEMS -->
   <table align="center" width="100%" border="0">
   <input type="hidden"  value="<%=MiUtil.getDate("dd/MM/yyyy")%>"  name="txtFechaServidor" >
   <%
     try{
        //Si existe una configuración para el grupo
        if( objItemHEader != null && objItemHEader.size() > 0 ){
         
         NpObjItemSpecgrp objnpObjHeaderSpecgrp = null;
         String  strExecMaxLength = "";
         
		 
         for ( int i=0 ; i<objItemHEader.size(); i++ ){
             objnpObjHeaderSpecgrp = new NpObjItemSpecgrp();
             objnpObjHeaderSpecgrp =  (NpObjItemSpecgrp)objItemHEader.get(i);
             
             //Si no es un tipo de objeto CONTROL
             if( !Constante.CONTROL_OTRO.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ){
         %>
         <tr id="idDisplay<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>"  style=display:<%=objnpObjHeaderSpecgrp.getNpdisplay().equals("N")?"none":""%> >
            <td class="CellLabel" align="left" valign="top">
            <div id="idDisplayValidate<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>">
            <%
            //Si el campo es obligatorio
            if(MiUtil.getString(objnpObjHeaderSpecgrp.getNpvalidateflg()).equals("S") ){ %>
              <font color="red">&nbsp;*&nbsp;</font>
            <%}else{%>
              &nbsp;&nbsp;&nbsp;
            <%}%>
            
            <%=objnpObjHeaderSpecgrp.getNpobjitemname()%>&nbsp;
            </div>
            <td align="left" class="CellContent">&nbsp;
            
            <%//Si es un TEXT. Elegimos que mostrar
            if( Constante.CONTROL_TEXT.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ){
              if( objnpObjHeaderSpecgrp.getNplength() > 0 )
                strExecMaxLength = "maxlength='"+objnpObjHeaderSpecgrp.getNplength()+"'";
            %>
				
             <input type="TEXT" <%=strExecMaxLength%> value="<%=MiUtil.getString(objnpObjHeaderSpecgrp.getNpdefaultvalue())%>" <%=objnpObjHeaderSpecgrp.getNpobjreadonly().equals("S")?"readonly":""%> name="<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>" onBlur="fxActionObjectText('<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',this)" >
            <!--
                Se define la etiqueta del objeto item. 
                En caso sea un tipo de dato fecha se debe de validar agregar un 
                link para ingresar la fecha
            -->
            <%
              if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE) ||
                  MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_PLUS) ||
                  MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME) ||
                  MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME_PLUS)
                ){
            %>
            <%if(MiUtil.getString(objnpObjHeaderSpecgrp.getNpobjreadonly()).equals("S")){%>
            <a>
              <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0">
            </a>
            <%}else{%>
            <!--a href="javascript:show_calendar('frmdatos.<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',null,null,'DD/MM/YYYY');"-->
            <a href="javascript:preShowCalendar('frmdatos.<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',null,null,'DD/MM/YYYY');">
              <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0">
            </a>  
            <%}%>
            <%
            if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE) ||
                MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_PLUS)
              ){
            %>
            &nbsp;DD/MM/YYYY 
            <%}%>
            
            <%
            if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME) ||
                MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME_PLUS)
              ){
            %>
            &nbsp; DD/MM/YYYY hh:mm
            <%}%>
            
            <%
                }
            %>  
            <%//Si es un SELECT
            }else if( Constante.CONTROL_SELECT.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ) {%>
            <%
             String path =  "/htdocs/jsp/controls/"+objnpObjHeaderSpecgrp.getNpsourceprogram()+"&nameObjectHtml="+objnpObjHeaderSpecgrp.getNpnamehtmlitem()+"&strObjectReadOnly="+objnpObjHeaderSpecgrp.getNpobjreadonly()+"&strObjectValidate="+objnpObjHeaderSpecgrp.getNpvalidateflg()+"&strObjectHeaderId="+objnpObjHeaderSpecgrp.getNpobjitemheaderid();
            %>
             <jsp:include page='<%=path%>' flush="true" />
            <%}%>
           </td>
           
           </tr> 
              <%
              //Si es un CONTROL
              }else{
                String path2 =  "/htdocs/jsp/controls/"+objnpObjHeaderSpecgrp.getNpsourceprogram()+"&nameObjectHtml="+objnpObjHeaderSpecgrp.getNpnamehtmlitem()+"&strObjectReadOnly="+objnpObjHeaderSpecgrp.getNpobjreadonly()+"&strObjectValidate="+objnpObjHeaderSpecgrp.getNpvalidateflg()+"&strObjectHeaderId="+objnpObjHeaderSpecgrp.getNpobjitemheaderid();
              %>
              <tr id="idDisplay<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>"  style=display:<%=objnpObjHeaderSpecgrp.getNpdisplay().equals("N")?"none":""%> >
               
                  <jsp:include page='<%=path2%>' flush="true" />
                  
                
              </tr>
              <%}%>
              
           <%} //Fin del for para los Text y Select
         //For para los campos de tipo OTROS
     }else{
     %>
     <tr>
         <td class="CellLabel" align="left" valign="top">No se ha configurado ningún item para esta sub-categoría. Consulte con Sistemas</td>
     </tr>
     <%
     }
     
     }catch(Exception ex){
     ex.printStackTrace();
     %>
     <script>alert("<%=MiUtil.getMessageClean(ex.getMessage())%>")</script>
     <%
     }
     %>
   </table>
   <!--Fin de Sección de objetos de ITEMS -->

         <table><tr align="center"><td></td></tr></table>
         <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
               <!--<td align="center"><input type="button" name="btnLoadMasiveSIM"  value=" Carga SIM Masiva " onclick="javascript:fxSendItemValuesEditOrder();">&nbsp;&nbsp;&nbsp;</td>-->
               
               <% if( type_window.equals("EDIT") ) {%>
               <td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " onclick="javascript:fxSendItemValuesEditOrder();">&nbsp;&nbsp;&nbsp;</td>
               
               <%}else if( type_window.equals("NEW") ){%>
               <td align="center"><input type="button" name="btnAceptar"  value=" Agregar " onclick="javascript:fxSendItemValuesOrder();">&nbsp;&nbsp;&nbsp;</td>
               <%}else if( type_window.equals("DETAIL") ){%>
                  <td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " disabled>&nbsp;&nbsp;&nbsp;</td>
               <%}%>
               <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCancelItemEditWindow();"></td><!--javascript:CancelItemEditWindow();-->
            </tr>
         </table>
         
      </td></tr>
      </table>
      </form>
      
      <!--Para carga de SIMs -->
      <form name="frmdatosload" method="POST" target="validateMassiveFrame" enctype="multipart/form-data">
        <input type="file" name="fileImeiSim" size="50" />
        <input type="button" value="Cargar" onclick="javascript:fxSubmitLoadCSV();" />
        <p></p>
        <!--<iframe name="validateMassiveFrame" width="100%" height="280" frameborder="1" src="../../../pages/buildTableSimByPrepago.jsp"></iframe>-->
        <iframe name="validateMassiveFrame" width="100%" height="280" frameborder="1" src="<%=request.getContextPath()%>/blank.html" ></iframe>
        <table align="center">
          <tr>
            <td>
              <!--<input type="button" value="Aceptar" />
              <input type="button" value="Cancelar" />-->
            </td>
          </tr>
        </table>  
      </form>
      
      
      </body>
   </html>
   
<script defer>
  function fxSubmitLoadCSV(){
  var formload = document.frmdatosload;
  var form     = document.frmdatos;
  form.btnAceptar.disabled = true;
  formload.action= "<%=request.getContextPath()%>/uploadServlet?hdnMethod=uploadFileByMassivePropio&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=strSpecificationId%>&strModality='Propio'";
  formload.submit();
  }

</script>
   
<script language="javascript" >
/**
  Developer : Lee Rosales 
*/
   var vctItemOrder = new Vector();
   var vctItemOrderOriginal = new Vector();
   

   function loadItems(){
      if ( !(vctItemOrder.size() > 0) ) {
         <%
         if( objItemHEader != null && objItemHEader.size() > 0 ){                            
             NpObjItemSpecgrp objnpObjHeaderSpecgrpItem = null;
             for ( int j=0 ; j<objItemHEader.size(); j++ ){
             String  strCadena = "'";                                          
                 objnpObjHeaderSpecgrpItem = new NpObjItemSpecgrp();
                 objnpObjHeaderSpecgrpItem =  (NpObjItemSpecgrp)objItemHEader.get(j);
                 
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemheaderid() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjspecgrpid() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemid() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemname() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpnamehtmlheader() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpnamehtmlitem() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemcontroltype() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpdefaultvalue() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpsourceprogram() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpspecificationgrpid() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpdisplay() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpobjreadonly() + "' , '";
            strCadena += "' , '";//Value
            strCadena += "' , '";//Desc
            strCadena += "' , '";//Flag Save
            strCadena += "' , '";//Item Id
            strCadena += objnpObjHeaderSpecgrpItem.getNpdatatype() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNpvalidateflg() + "' , '";
            strCadena += objnpObjHeaderSpecgrpItem.getNplength() + "'";
         %>
         vctItemOrder.addElement(new fxMakeOrderItem(<%=strCadena%>));
        <%
            }//Fin del For
        }//Fin del If
        %>
      }
   }
  
  function fxLoadValues(){
  <%
    if( type_window.equals("EDIT") || type_window.equals("DETAIL") ){
      String[] paramNpobjitemheaderid = request.getParameterValues("a");
      String[] paramNpobjitemvalue    = request.getParameterValues("b");
        for(int i=0; i<paramNpobjitemheaderid.length; i++){                 
    %>     
    //alert("");
          for( x = 0; x < vctItemOrder.size(); x++ ){
              if ( vctItemOrder.elementAt(x).npobjitemheaderid == <%=paramNpobjitemheaderid[i]%> ){
                try{     
                    if( vctItemOrder.elementAt(x).namehtmlitem != "hdn_ListAdenda" ){                    
                      if( vctItemOrder.elementAt(x).namehtmlitem != "item_services" ){                            
                          var strParamDesc  = "form." + vctItemOrder.elementAt(x).namehtmlitem + ".value = '<%=MiUtil.escape2(paramNpobjitemvalue[i])%>'";
                          vctItemOrder.elementAt(x).npobjitemvalue = "<%=MiUtil.escape2(paramNpobjitemvalue[i])%>";
                          eval(strParamDesc);
                      }else{                        
                          fxCargaServiciosItem("<%=MiUtil.escape2(paramNpobjitemvalue[i])%>");                        
                      }   
                    }
                }catch(e){}
                break;
               }
            }
       <%}//Fin de for
      }//Fin del if%>
   }
   
   function fxSetDetail(){  
   <%if(type_window.equals("DETAIL") ){%>
      var x= document.getElementById("frmdatos");
      for (var i=0;i<x.length;i++){
        try{
        if(x.elements[i].name == "cmb_ItemContact")fxObjectConvertImgByParam("","HIDE",61);
        x.elements[i].disabled = true;
        }catch(e){}
      }
      x.btnCerrar.disabled = false;
   <%}%>   
   }
   
   function fxShowHideControls(){
   /*   if (document.frmdatos.cmb_ItemContractServ == undefined)
          return false;
*/
      /*var selection = document.frmdatos.cmb_ItemContractServ.options[document.frmdatos.cmb_ItemContractServ.selectedIndex].value; 
      document.frmdatos.txt_ItemContractNumber.value="";

      if (selection=="S"){                       // Compartida
          fxOcultarTr("yes",idDisplay27);    
          fxOcultarTr("none",idDisplay28); 
          //alert("entro jsp");
          fxOcultarTr("none",idDisplay30); 
          fxOcultarTr("none",idDisplay18);                     
      }else{                                     // Anterior
          fxOcultarTr("none",idDisplay27);            
          fxOcultarTr("yes",idDisplay28);            
      }*/
   }
   
   function fxViewAddendumAct(){
      <%
      String numAddenAct = (String)request.getAttribute("numAddenAct");
      if(numAddenAct != null){
          if (!(("0").equals(numAddenAct))){
          %>
              alert("El contrato tiene adendas vigentes, le recomendamos verificarlas.");
          <%
          }
      }
      %>    
   }
</script>



<script DEFER> 
   
   function fxloadItemsEdit(){
      loadItems();
      
      fxShowHideControls();
      
      <%if( type_window.equals("EDIT") || type_window.equals("DETAIL") ){%>
      try{
      fxOnLoadListAddendum();
      }catch(e){}
      <%}%>
      //alert("0");
      try{
      fxViewAddendumAct();
      }catch(e){}
      //alert("1");
      try{
      fxLoadServiceAditiional();
      }catch(e){}
      
      try{
      fxCargaServiciosItem("");
      }catch(e){}
      
      try{
      fxCargaServiciosPorDefecto();
      }catch(e){}
      //Eventos OnLoad que pueden o no cargarse
      /*try{
      //Para la carga de los Productos
      fxLoadProduct();
      }catch(e){}*/
      
      try{
      fxLoadEditProduct();
      }catch(e){}
      
      try{
      fxLoadEditModalidad();
      }catch(e){}
      
      try{
      fxLoadEditPromotion();
      }catch(e){}
      
      try{
      fxLoadEditRatePlanId();
      }catch(e){}
      
      /*Nuevo Servicio Principal*/
      try{
      fxLoadEditNewMainService();
      }catch(e){}
      
      //alert("2");
      
	  /*Inicio CESPINOZA*/
      /*Source Address*/
      try{
      fxLoadItemDestinyAddress();
      }catch(e){}
	  /*Fin CESPINOZA*/
	  
      //Para Contrato a Asociar 
      try{
      fxLoadNpTable();
      }catch(e){}
      
      try{
      fxLoadNpTable2();
      }catch(e){}
      try{
      fxLoadModalityByPhone();
      }catch(e){}
      
      //alert("3");
      fxLoadValues();
      
      //alert("4");
      
      try{
      <%if (strSpecificationId!=null && !strSpecificationId.trim().equals(String.valueOf(Constante.SPEC_BOLSA_CAMBIO))) {%> // strSpecificationId <> Bolsa cambio
        fxLoadProductBolsa();
      <%}%>
      }catch(e){}
      
      fxSetDetail();
      fxGeneralRules();
      
      /*Solo para la carga de IMEIS*/
      
      parent.mainFrame.frmdatos.cmb_ItemModality.value = 'Propio';
      parent.mainFrame.fxChangeObjectModality('Propio');
      parent.mainFrame.frmdatos.cmb_ItemModality.disabled = true;
      parent.mainFrame.frmdatos.txt_ItemSIM_Eagle.disabled = true;
      parent.mainFrame.frmdatos.txt_ItemModel.disabled = false;
      
      parent.mainFrame.frmdatos.txt_ItemQuantity.disabled = true;
      
      var objObjectItemAux = null;
      var objObjectItemAux2 = null;
      
      objObjectItemAux  = fxGetObjectProperties(2);
      //alert(objObjectItemAux.namehtmlitem)
      objObjectItemAux.npvalidateflg = 'N';
      
      parent.mainFrame.frmdatos.btnAceptar.disabled = true;
      
      /*objObjectItemAux2 = fxGetObjectProperties(2);
      alert(objObjectItemAux2.npvalidateflg)*/
      
      
      /*Saca una copia del vector*/
      fxCopyVector(vctItemOrder, vctItemOrderOriginal);                      
   }
   
   onload = fxloadItemsEdit;
   
</script>

<script DEFER>
  /*Developer : Lee Rosales
    Purpose   : Establecer una lógica para las categorías
  */
  var npspecificationId = <%=strSpecificationId%>;
  var npstatus          = '<%=MiUtil.getString(strStatus)%>';
  function fxGeneralRules(){
    //alert("Tamaño : "+vctItemOrder.size());
    //npstatus = 'TIENDA01';
        /**
        CATEGORÍA - TRASLADO
        //Traslado
        **/
    <%if(!type_window.equals("DETAIL")){%>        
        if( npspecificationId == 2050 ){
          //Verificamos el INBOX
          if( npstatus == 'FACTIBILIDAD' ) {
          //alert("Entramos a Factibilidad");
          //Se deshabilitan los campos
           //Dirección de Origen - 59
           fxObjectConvertByParam("","DISABLED",59);
           //Dirección destino - 60
           fxObjectConvertByParam("","DISABLED",60);
           //Contacto - 61
           fxObjectConvertByParam("","DISABLED",61);
           fxObjectConvertImgByParam("","HIDE",61);
           //Telefono Principal - 62
           fxObjectConvertByParam("","DISABLED",62);
           //Teléfono Auxiliar       
           fxObjectConvertByParam("","DISABLED",63);
           //Precio Traslado (US$)     
           fxObjectConvertByParam("","DISABLED",31);
           //Precio Traslado Exc. (US$)   
           fxObjectConvertByParam("","DISABLED",32);
           //Costo Adicional
           fxObjectConvertByParam("","ENABLED",66);
          //Se habilitan y se muestran los campos
           //Fecha Factibilidad - 35
           fxObjectConvertByParam("","ENABLED",35);
           fxObjectConvertByParam("","SHOW",35);
           //Factibilidad - 36
           fxObjectConvertByParam("","ENABLED",36);
           fxObjectConvertByParam("","SHOW",36);
           //Descripción - 80
           fxObjectConvertByParam("","ENABLED",80);
           fxObjectConvertByParam("","SHOW",80);

          }else if( npstatus == 'INSTALACION_ING' ){
          
          //Se deshabilitan los campos
           //Dirección de Origen - 59
           fxObjectConvertByParam("","DISABLED",59);
           //Dirección destino - 60
           fxObjectConvertByParam("","DISABLED",60);
           //Contacto - 61
           fxObjectConvertByParam("","DISABLED",61);
           fxObjectConvertImgByParam("","HIDE",61);
           //Telefono Principal - 62
           fxObjectConvertByParam("","DISABLED",62);
           //Teléfono Auxiliar       
           fxObjectConvertByParam("","DISABLED",63);
           //Precio Traslado (US$)     
           fxObjectConvertByParam("","DISABLED",31);
           //Precio Traslado Exc. (US$)   
           fxObjectConvertByParam("","DISABLED",32);
           //Costo Adicional   
           fxObjectConvertByParam("","DISABLED",66);
          
          //Se habilitan y se muestran los campos
           //Fecha Factibilidad - 35
           fxObjectConvertByParam("","DISABLED",35);
           fxObjectConvertByParam("","SHOW",35);
           //Factibilidad - 36
           fxObjectConvertByParam("","DISABLED",36);
           fxObjectConvertByParam("","SHOW",36);
           //Descripción - 80
           fxObjectConvertByParam("","ENABLED",80);
           fxObjectConvertByParam("","SHOW",80);
           //Fecha de Instalación - 38
           fxObjectConvertByParam("","ENABLED",38);
           fxObjectConvertByParam("","SHOW",38);
           
          }else if( npstatus == 'CALLCENTER FF' || npstatus == 'TIENDA01'  ){
           //Deshabilito todo
           //Dirección de Origen - 59
           fxObjectConvertByParam("","ENABLED",59);
           //Dirección destino - 60
           fxObjectConvertByParam("","ENABLED",60);
           //Contacto - 61
           fxObjectConvertByParam("","ENABLED",61);
           //fxObjectConvertImgByParam("","HIDE",61);
           //Telefono Principal - 62
           fxObjectConvertByParam("","ENABLED",62);
           //Teléfono Auxiliar       
           fxObjectConvertByParam("","ENABLED",63);
           //Precio Traslado (US$)     
           fxObjectConvertByParam("","ENABLED",31);
           //Precio Traslado Exc. (US$)   
           fxObjectConvertByParam("","ENABLED",32);
           //Costo Adicional   
           //fxObjectConvertByParam("","ENABLED",66);
           fxObjectConvertByParam("","DISABLED",66);
         }else if( npstatus != ''  ){
           //alert('Entramos IF Final');
           //Deshabilito todo
           //Dirección de Origen - 30
           fxObjectConvertByParam("","DISABLED",59);
           //Dirección destino - 60
           fxObjectConvertByParam("","DISABLED",60);
           //Contacto - 61
           fxObjectConvertByParam("","DISABLED",61);
           fxObjectConvertImgByParam("","HIDE",61);
           //Telefono Principal - 62
           fxObjectConvertByParam("","DISABLED",62);
           //Teléfono Auxiliar       
           fxObjectConvertByParam("","DISABLED",63);
           //Precio Traslado (US$)     
           fxObjectConvertByParam("","DISABLED",31);
           //Precio Traslado Exc. (US$)   
           fxObjectConvertByParam("","DISABLED",32);           
           //Costo Adicional   
           fxObjectConvertByParam("","DISABLED",66);
         }//Inicio CESPINOZA
		  else if( npstatus == ''  ){
           //alert('Entramos IF Final');
           //Deshabilito todo
           //Dirección de Origen - 30
           fxObjectConvertByParam("","ENABLED",59);
           //Dirección destino - 60
           fxObjectConvertByParam("","ENABLED",60);
           //Contacto - 61
           fxObjectConvertByParam("","ENABLED",61);
           //Telefono Principal - 62
           fxObjectConvertByParam("","ENABLED",62);
           //Teléfono Auxiliar       
           fxObjectConvertByParam("","ENABLED",63);
           //Precio Traslado (US$)     
           fxObjectConvertByParam("","ENABLED",31);
           //Precio Traslado Exc. (US$)   
           fxObjectConvertByParam("","ENABLED",32);           
           //Costo Adicional   
           fxObjectConvertByParam("","DISABLED",66);
         }//Fin CESPINOZA		 
        /**
        CATEGORÍA - ACTIVAR Y DESACTIVAR
        //Servicios Adicionales - Activar y Desactivar
        **/  
        }else if( npspecificationId == 2048 ){
          //Verificamos el INBOX
          if( npstatus == 'FACTIBILIDAD' ) {
          //alert("Entramos a Factibilidad");
          
          //Se deshabilitan los campos
           //Contrato de Referencia - 27
           fxObjectConvertByParam("","DISABLED",27);
           //Plan Tarifario Original - 16
           fxObjectConvertByParam("","DISABLED",16);
           //Servicio Principal Original - 82
           fxObjectConvertByParam("","DISABLED",82);
           //Servicios Adicionales - 28
           fxObjectSSAA("DISABLED");
          
          //Se habilitan y se muestran los campos
           //Factibilidad - 36
           fxObjectConvertByParam("","ENABLED",36);
           fxObjectConvertByParam("","SHOW",36);
           //Descripción - 80
           fxObjectConvertByParam("","ENABLED",80);
           fxObjectConvertByParam("","SHOW",80);

          }else if( npstatus == 'INSTALACION_ING' ){
          
          //Se deshabilitan los campos
           //Contrato de Referencia - 27
           fxObjectConvertByParam("","DISABLED",27);
           //Plan Tarifario Original - 16
           fxObjectConvertByParam("","DISABLED",16);
           //Servicio Principal Original - 82
           fxObjectConvertByParam("","DISABLED",82);
           //Servicios Adicionales - 28
           fxObjectSSAA("DISABLED");
          
          //Se habilitan y se muestran los campos
           //Factibilidad - 36
           fxObjectConvertByParam("","DISABLED",36);
           fxObjectConvertByParam("","SHOW",36);
           //Descripción - 80
           fxObjectConvertByParam("","ENABLED",80);
           fxObjectConvertByParam("","SHOW",80);
           
        }else if( npstatus != ''  ){
           //Se deshabilitan los campos
           //Contrato de Referencia - 27
           fxObjectConvertByParam("","DISABLED",27);
           //Plan Tarifario Original - 16
           fxObjectConvertByParam("","DISABLED",16);
           //Servicio Principal Original - 82
           fxObjectConvertByParam("","DISABLED",82);
           //Servicios Adicionales - 28
           fxObjectSSAA("DISABLED");
         }
		/**
        CATEGORÍA - CAMBIO DE MODELO
      **/  			
			}else if( (npspecificationId == 2009)||(npspecificationId == <%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>) ){
				var modalidad = document.frmdatos.txt_ItemEquipment.value;
				if (modalidad=="<%=Constante.TIPO_ALQUILER%>") {
					document.frmdatos.cmb_ItemDevolution.value="S";
					document.frmdatos.cmb_ItemDevolution.disabled="true";
				}
      /**
		
		/**
        CATEGORÍA - REPOSICION
      **/  			
			
      /*}else if( npspecificationId == <%=Constante.SPEC_REPOSICION%> ){
				var ocurrencias = document.frmdatos.txt_ItemNroOcurrence.value;
				if (ocurrencias==3) {
					alert("N° de ocurrencia no cubierta por Garantía Nextel, verifique fecha de renovación"); 
					document.frmdatos.txt_ItemNroOcurrence.value=0;
				}*/
      /**
        CATEGORÍA - CAMBIO DE PLAN TARIFARIO
        //Servicios Adicionales - Cambio de Plan Tarifario
      **/  
      }else if( npspecificationId == 2049 ){
        //Verificamos el INBOX
        if( npstatus == 'FACTIBILIDAD' ) {
        //alert("Entramos a Factibilidad");
          
        //Se deshabilitan los campos
        //Contrato de Referencia - 27
        fxObjectConvertByParam("","DISABLED",27);
        //Plan Tarifario Original - 16
        fxObjectConvertByParam("","DISABLED",16);
        //Servicio Principal Original - 82
        fxObjectConvertByParam("","DISABLED",82);
        //Plan Tarifario Nuevo - 10
        fxObjectConvertByParam("","DISABLED",10);
        //Servicio Principal Nuevo - 83
        fxObjectConvertByParam("","DISABLED",83);
        //Servicios Adicionales - 28
        fxObjectSSAA("DISABLED");
          
          //Se habilitan y se muestran los campos
         //Factibilidad - 36
         fxObjectConvertByParam("","ENABLED",36);
         fxObjectConvertByParam("","SHOW",36);
         //Descripción - 80
         fxObjectConvertByParam("","ENABLED",80);
         fxObjectConvertByParam("","SHOW",80);
      
      }else if( npstatus == 'INSTALACION_ING' ){
          
          //Se deshabilitan los campos
           //Contrato de Referencia - 27
           fxObjectConvertByParam("","DISABLED",27);
           //Plan Tarifario Original - 16
           fxObjectConvertByParam("","DISABLED",16);
           //Servicio Principal Original - 82
           fxObjectConvertByParam("","DISABLED",82);
           //Plan Tarifario Nuevo - 10
           fxObjectConvertByParam("","DISABLED",10);
           //Servicio Principal Nuevo - 83
           fxObjectConvertByParam("","DISABLED",83);
           //Servicios Adicionales - 28
           fxObjectSSAA("DISABLED");
          
          //Se habilitan y se muestran los campos
           //Factibilidad - 36
           fxObjectConvertByParam("","DISABLED",36);
           fxObjectConvertByParam("","SHOW",36);
           //Descripción - 80
           fxObjectConvertByParam("","ENABLED",80);
           fxObjectConvertByParam("","SHOW",80);

        }else if( npstatus != ''  ){
           //Se deshabilitan los campos
           //Contrato de Referencia - 27
           fxObjectConvertByParam("","DISABLED",27);
           //Plan Tarifario Original - 16
           fxObjectConvertByParam("","DISABLED",16);
           //Servicio Principal Original - 82
           fxObjectConvertByParam("","DISABLED",82);
           //Plan Tarifario Nuevo - 10
           fxObjectConvertByParam("","DISABLED",10);
           //Servicio Principal Nuevo - 83
           fxObjectConvertByParam("","DISABLED",83);
           //Servicios Adicionales - 28
           fxObjectSSAA("DISABLED");
        }        
      }
  <%}%>
    
    try{
      parent.mainFrame.frmdatos.txt_ItemModel.disabled = true;
    }catch(e){}
  
  }
  
  function fxObjectSSAA(objObjectAction){
    if( objObjectAction == "DISABLED" ){
      eval("form.cmbAvailableServices.disabled = true;");
      eval("form.cmbSelectedServices.disabled = true;");
      eval("form.baddService.disabled = true;");
    }else if( objObjectAction == "ENABLED" ){
      eval("form.cmbAvailableServices.disabled = false;");
      eval("form.cmbSelectedServices.disabled = false;");
      eval("form.baddService.disabled = false;");
    }
  }
  
  function fxObjectConvertByParam(objObject,objObjectAction,objHeaderId){
    var form = document.frmdatos;
    var objObjectName = "";
    var objObjectValue = "";
    objObjectName  = objObject;
    
    if( objHeaderId != "" ){
      objObjectValue  = fxGetObjectProperties(objHeaderId);
      objObjectName   = objObjectValue.namehtmlitem;
    }
    
    if( objObjectAction == "SHOW" ){
      eval("idDisplay"+objHeaderId+".style.display = ''");
    }else if( objObjectAction == "HIDE" ){
      eval("idDisplay"+objHeaderId+".style.display = 'none'");
    }else if( objObjectAction == "DISABLED" ){
      eval("form." + objObjectName + ".disabled = true;");
    }else if( objObjectAction == "ENABLED" ){
      eval("form." + objObjectName + ".disabled = false;");
    }else if( objObjectAction == "READONLY" ){
      eval("form." + objObjectName + ".readOnly = true;");
    }else if( objObjectAction == "READ" ){
      eval("form." + objObjectName + ".readOnly = false;");
    }
  
  }
  
    function fxObjectConvertImgByParam(objObject,objObjectAction,objHeaderId){
    var form = document.frmdatos;
    var objObjectName = "";
    var objObjectValue = "";
    objObjectName  = objObject;
    if( objObjectAction == "SHOW" ){
      eval("idImgVD"+objHeaderId+".style.display = ''");
    }else if( objObjectAction == "HIDE" ){
      eval("idImgVD"+objHeaderId+".style.display = 'none'");
    }  
  }
  
  function fxGetObjectProperties(objHeaderId){

    //Verificar si tiene campos mandatorios
    for( x = 0; x < vctItemOrder.size(); x++ ){
      objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
      
      if( objHeaderId == objitemheaderid )
        return vctItemOrder.elementAt(x);
    }
    
    return -1;
    
  }
  
  function fxObjectConvert(objObject,objValue){
    form = document.frmdatos;
    try{
       cadena = "form."+objObject+".value = '"+objValue+"'";
       eval(cadena);
    }catch(e){
    
    }
  }
  
  function fxObjectTry(objObject,objValue){
    form = document.frmdatos;
    try{
       cadena = "form."+objObject+".value = '"+objValue+"'";
       eval(cadena);
    }catch(e){
    
    }
  }
  
  /*Developer: Lee Rosales
    Objetivo : Carga los servicios adicionales a los seleccionados y resta
               los servicios disponibles.             
  */
  
  function fxCargaServiciosItem(servicios_item){           
      v_doc  = parent.mainFrame;      
      form   = document.frmdatos;
      //alert(servicios_item);
      if (form.cmbAvailableServices!=null){          
          deleteAllValues(form.cmbAvailableServices);
          deleteAllValues(form.cmbSelectedServices);
          wn_cantServ = vServicio.size();
          wv_Serv_item = servicios_item;
          /* variable para separacion de las "x" en el combo "cmbSelectedServices" */
          var txt_separacion="  ";
          /* tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados */
          var txt_vacios_2="";
          wv_Serv_bscs = servicios_item;
          //alert(wn_cantServ);
          for(i=0; i<wn_cantServ; i++){
              objServicio = vServicio.elementAt(i);              
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
                        parent.mainFrame.alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        parent.mainFrame.rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        parent.mainFrame.rentServicesSelectedEnd++;
                     
                 }
                 else if (wv_servicio_act == "S" && wv_servicio_new == "N"){                        
                     //AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
                     AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");
                     if( objServicio.exclude == "ALQ" )
                        parent.mainFrame.alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        parent.mainFrame.rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        parent.mainFrame.rentServicesSelectedEnd++;
                 }
                 else if (wv_servicio_act == "N" && wv_servicio_new == "S"){                      
                     AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
                     //alert("Entramos " + objServicio.nameShort)
                     if( objServicio.exclude == "ALQ" )
                        parent.mainFrame.alquilerServicesSelected++;
                     if( objServicio.exclude == "INT" )
                        parent.mainFrame.rentServicesSelected++;
                     if( objServicio.exclude == "END" )
                        parent.mainFrame.rentServicesSelectedEnd++;
                 }                    
                     objServicio.active_new=wv_servicio_act;
                     objServicio.modify_new=wv_servicio_new;
               }
               else{
                 /* El teléfono no tiene este servicio */
                 /*Nombre Corto*/
                 //v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.nameShortDisplay);
                 /*Nombre Largo*/
                 v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.name);
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
   
   function fxCargaServiciosPorDefecto(){
        var form = document.frmdatos;
        var wn_size = vServicioPorDefecto.size();
        for(j=0; j<wn_size ; j++){
          objServicio = vServicioPorDefecto.elementAt(j);
         for (i=0;i<form.cmbAvailableServices.options.length;i++){
             if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                form.cmbAvailableServices.options[i].selected = true;
             }
          }
        }      
        fxAddService();
  }
   
   
   function fxActionObjectText(nameText,objThis){
     var objValueObject = objThis.value;
     var objRefObject   = objThis;
      if (trim(objThis.value)!=""){
        //alert("Distinto de ''")
        if (nameText=="txt_ItemContractNumber"){
            var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=strSpecificationId%>&strContractNumber="+trim(objThis.value)+"&hdnMethod=getValidateContract&nameText="+nameText;
            parent.bottomFrame.location.replace(url);
        }
        
        if (nameText=="txt_ItemNewNumber"){
          if( '<%=strSpecificationId%>' == '2012' ){
           var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber="+objValueObject+"&paramCustomerId=<%=strCodigoCliente%>&paramSiteId=<%=strnpSite%>&paramSpecificationId=<%=strSpecificationId%>&myaction=doValidateNewPhoneToChange";   
            parent.bottomFrame.location.replace(url);
          }
        }
        
        if (nameText=="txt_ItemIMEI" && (!eval("parent.mainFrame.frmdatos.txt_ItemIMEI.readOnly"))){
              objThis.value=trim(objThis.value); //CEM COR0429
              var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getDetailImei&strParamInput="+objThis.value+"&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=strSpecificationId%>"; //COR0596
              parent.bottomFrame.location.replace(url);
        }
        
        if (nameText=="txt_ItemMntsRates"){
              if(!isNaN(objValueObject)){
                  if(objValueObject > parseInt(form.txt_ItemCantMntsBolsa.value)){
                    alert("Los minutos contratados deben ser menores a los minutos de la bolsa seleccionada");
                    objRefObject.value = '';
                    objRefObject.focus();
                  }
              }else{
                  alert("Los minutos contratados no son los correctos");
                  objRefObject.value = '';
                  objRefObject.focus();
              }
              
        }
        
        if (nameText=="txt_ItemPriceByMnts"){
         //var objValue = objThis.value;
         var objValue = "";
         
            objValue = parseFloat(form.txt_ItemPriceBolsa.value) / parseInt(form.txt_ItemCantMntsBolsa.value);
        
            if( !isNaN(parseFloat(objValue)) ){
              if( objValue != objValueObject ){
                alert("El precio por minuto ingresado del Producto Bolsa no es el correcto");
                objRefObject.value = '';
                objRefObject.focus();
              }
            }else{
              alert("La cantidad de minutos es cero, seleccione otro producto " );
                objRefObject.value = '';
                objRefObject.focus();
            }
        }

        // Inicio CEM COR0429 - CGC
        if( nameText=="txt_ItemSIM_Eagle" ){
          var modality="";
          try{
            modality = form.cmb_ItemModality.value;            
          }catch(e){}
          objThis.value=trim(objThis.value);
          var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getDetailImei&strFrom=SIMEagle&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=strSpecificationId%>&strParamInput="+objThis.value+"&strItemModality="+modality;
          parent.bottomFrame.location.replace(url);
        }

          if( nameText=="txt_ItemPriceException" ){
            var productLine="";
            try{
              productLine = form.cmb_ItemProductLine.value;            
            }catch(e){}
            if (productLine=="12" && form.txt_ItemPriceException.value!="" && form.cmb_ItemModality.value =="Venta" && ('<%=strSpecificationId%>' == '2002' 
                     || '<%=strSpecificationId%>' == '<%=Constante.SPEC_PREPAGO_TDE%>' || '<%=strSpecificationId%>' == '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>') ){ //Se agrega reposicion prepago tde - TDECONV034
                 //Se agrega reposicion prepago tde - TDECONV034
              //alert("Es un Kit Prepago: " + form.cmb_ItemModality.value + " productid:" +  form.cmb_ItemProducto.value);
              //objThis.value=trim(objThis.value);
              var url = "<%=request.getContextPath()%>/serviceservlet?myaction=doValidateKitPrice&strModality=20"+"&strProductId=" + form.cmb_ItemProducto.value+"&strExceptionPrice="+form.txt_ItemPriceException.value+"&strPrice="+form.txt_ItemPriceCtaInscrip.value+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>";
              //alert(url);
              parent.bottomFrame.location.replace(url);            
            }
          } 

        // Fin CEM COR0429 - CGC       
        
        /*if(nameText=="txt_ItemContractNumber"){     
            var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getValidateContract&nameText="+nameText;
            parent.bottomFrame.location.replace(url);
        }*/
      }else{
      //alert("Distinto de 'hola'")
      }
      
      
   }
   
   function fxInputTabEnter() {
		return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
	 }
   
   /*Developer: Lee Rosales
     Objetivo : Después de agregarse un registro deben de
                regresar los controles a su estado inicial
   */
   function formResetGeneral(){
     form   = document.frmdatos;
     document.getElementById("frmdatos").reset();
     
     document.getElementById("frmdatosload").reset();
     
    try{
    //Se deben reiniciar los contadores globales
    parent.mainFrame.alquilerServicesSelected = 0;
    parent.mainFrame.rentServicesSelected     = 0;
    parent.mainFrame.rentServicesSelectedEnd  = 0;
    
    //Se deben de inicializar en blanco los SSAA
    form.item_services.value = '';
    }catch(e){}
    
    try{
     parent.mainFrame.valorOldProductNew  = '';
     parent.mainFrame.valorOldModalityNew = '';
     parent.mainFrame.valorOldPlanNew     = '';
     parent.mainFrame.valorOldQuantity    = '';
    }catch(e){}
    
    parent.mainFrame.frmdatos.cmb_ItemModality.value = 'Propio';
    parent.mainFrame.fxChangeObjectModality('Propio');
    
    /**PONER EN BLANCO EL INPUT DE CARGA DE ARCHIVOS CSV**/
    
    parent.mainFrame.frmdatosload.fileImeiSim.value = '';
    parent.mainFrame.validateMassiveFrame.location.replace('<%=request.getContextPath()%>/blank.html');
    
    /**FIN**/
    
   }
</script>


<%}catch(Exception ex){
   ex.printStackTrace();
%>
  <script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>