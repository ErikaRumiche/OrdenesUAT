<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%
/*Recuperando Parametros de Input*/
  Hashtable hshtinputNewSection = null;
  HashMap   objHashMapList = null;
  ArrayList arrLista = null;
  GeneralService  objGeneralService = new GeneralService();
  String    strCustomerId= "",
            strSiteId ="",
            strCodBSCS = "",
            strSpecificationId = "",
            strDivisionId = "",
            nameHtmlItem  = "",
            nameNpTable   = "",
            objReadOnly   = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  nameHtmlItem = MiUtil.getString((String)request.getParameter("nameObjectHtml"));
  nameNpTable  = MiUtil.getString((String)request.getParameter("tablename"));
  objReadOnly  = MiUtil.getString((String)request.getParameter("strObjectReadOnly")); 
  
  if ( hshtinputNewSection != null ) {
    strCustomerId           =   (String)hshtinputNewSection.get("strCodigoCliente");
    strSiteId               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    strSpecificationId      =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivisionId           =   (String)hshtinputNewSection.get("strDivision");
  }
  System.out.println("[objItemNpTable.jsp][objReadOnly]"+objReadOnly);
  if( objReadOnly.equals("S") ) 
     objReadOnly = "disabled"; 
  else objReadOnly = "";
  System.out.println("[objItemNpTable.jsp][nameNpTable]"+nameNpTable);
  objHashMapList = objGeneralService.getTableList(nameNpTable,"1");
  
  if( objHashMapList != null ){
    if( (String)objHashMapList.get("strMessage")!=null )
      throw new Exception(MiUtil.getMessageClean((String)objHashMapList.get("strMessage")));
  }else
      throw new Exception("Hubieron errores obteniendo datos para " + nameNpTable);
  
  arrLista = (ArrayList)objHashMapList.get("arrTableList"); 
 %>

<select name="<%=nameHtmlItem%>" onchange="fxShowObjectNP('<%=nameNpTable%>',this.value)" <%=objReadOnly%> >
    <%=MiUtil.buildCombo(arrLista,"wv_npValue","wv_npValueDesc")%> 
</select>

<script>   

//Función generica para realizar acciones con los select de NPTABLE 
  function fxShowObjectNPContractAssociates(objValue, solution){

    //var selection = objThis.options[objThis.selectedIndex].value; 
        var selection = objValue;
        try{
        document.frmdatos.txt_ItemContractNumber.value="";
        }catch(e){;}
        try{
        document.frmdatos.txt_ItemNodoPrincipal.value="";
        }catch(e){;}       
        //Instalación Compartida
        if (selection=="S"){
            /**(Inicio) - Ocultar los posibles valores*/
            //Nro de Contrato / Nodo Principal
            fxOcultarTr("none",idDisplay27);
            fxOcultarTr("none",idDisplay111);
            //Nro de Instalación Compartida
            
            //Instalación Compartida
            fxOcultarTr("none",idDisplay29);
            /**(Fin) - Ocultar los posibles valores*/
          
            fxOcultarTr("yes",idDisplay27);    
            fxOcultarTr("yes",idDisplay111);
            fxOcultarTr("none",idDisplay28);
        //Contrato Adicional
        }else if(selection=="N"){
            /**(Inicio) - Ocultar los posibles valores*/
            //Nro de Contrato                
            if (solution == "<%=Constante.KN_ACCESO_INTERNET%>"){              
              fxOcultarTr("yes",idDisplay27);    
              fxOcultarTr("yes",idDisplay111); 
            }else{
              fxOcultarTr("none",idDisplay27);
              fxOcultarTr("none",idDisplay111);
            }
            //Nro de Instalación Compartida
            fxOcultarTr("none",idDisplay28);
            //Instalación Compartida
            fxOcultarTr("yes",idDisplay29);
            /**(Fin) - Ocultar los posibles valores*/
                        
            fxOcultarTr("yes",idDisplay28);  
        //Primera Instalación
        }else if(selection=="P"){
            //Nro de Contrato
            fxOcultarTr("yes",idDisplay27);
            fxOcultarTr("yes",idDisplay111);
            //Nro de Instalación Compartida
            fxOcultarTr("yes",idDisplay28);
            //Instalación Compartida
            fxOcultarTr("yes",idDisplay29);
                              
        }
  }

  function fxShowObjectNP(nameNpTable,objValue){
    
    if (nameNpTable=="<%=Constante.NPT_CONTR_ASOCIATE%>"){
        
        var selection = objValue; 
        var solution = "";
        solution = document.frmdatos.cmb_ItemSolution[document.frmdatos.cmb_ItemSolution.selectedIndex].value;
        try{
          document.frmdatos.txt_ItemContractNumber.value="";
        }catch(e){;}
        try{
          document.frmdatos.txt_ItemNodoPrincipal.value="";
        }catch(e){;}
                
        //Inicio CEM - COR1059
        document.frmdatos.txt_ItemSharedInstall.value="";
        document.frmdatos.txt_ItemSharedInstallView.value="";
        document.frmdatos.cmb_ItemSharedInstallNumber.value="";
        //Fin CEM - COR1059
                  
        //Instalación Compartida        
        if (selection=="S"){
            /**(Inicio) - Ocultar los posibles valores*/
            //Nro de Contrato            
            fxOcultarTr("none",idDisplay27);
            fxOcultarTr("none",idDisplay111);

            //Nro de Instalación Compartida
            //fxOcultarTr("none",idDisplay28);
            //Instalación Compartida
            fxOcultarTr("none",idDisplay29);
            /**(Fin) - Ocultar los posibles valores*/
          
            fxOcultarTr("yes",idDisplay27);    
            fxOcultarTr("yes",idDisplay111); 
            fxOcultarTr("none",idDisplay28);
        //Contrato Adicional
        }else if(selection=="N"){
            /**(Inicio) - Ocultar los posibles valores*/
            //Nro de Contrato
            if (solution == "<%=Constante.KN_ACCESO_INTERNET%>"){              
              fxOcultarTr("yes",idDisplay27);
              fxOcultarTr("yes",idDisplay111);
            }else{
              fxOcultarTr("none",idDisplay27);
              fxOcultarTr("none",idDisplay111);
            }
            
            //Nro de Instalación Compartida
            fxOcultarTr("none",idDisplay28);
            //Instalación Compartida
            fxOcultarTr("yes",idDisplay29);
            /**(Fin) - Ocultar los posibles valores*/
                       
            fxOcultarTr("yes",idDisplay28);  
        //Primera Instalación
        }else if(selection=="P"){
            //Nro de Contrato
            fxOcultarTr("yes",idDisplay27);
            fxOcultarTr("yes",idDisplay111);
            //Nro de Instalación Compartida
            fxOcultarTr("yes",idDisplay28);
            //Instalación Compartida
            fxOcultarTr("yes",idDisplay29);                            
        }
    }
    //Devolución
    if (nameNpTable=="<%=Constante.NPT_EQUIPMENT_RETURN%>"){     
        //var selection = objThis.options[objThis.selectedIndex].value; 
        document.frmdatos.hdnFlagReturn.value  = '1'
        var selection = objValue; 
        var pendrecojo =document.frmdatos.cmb_ItemPendDevolution.value;
		                
		//Modificacion: el campo Pend. Recojo toma el mismo valor que se selecciona en Devolucion
		document.frmdatos.cmb_ItemPendDevolution.value=document.frmdatos.cmb_ItemDevolution.value;
        if (selection=="S"){ //&& pendrecojo=="S"){
            fxOcultarTr("none",idDisplay13);
			fxOcultarTr("none",idDisplay14);//Fecha de devolucion
			document.frmdatos.txt_ItemFecDevolution.value=document.frmdatos.txtFechaServidor.value;
            /*if (pendrecojo=="S"){
               fxOcultarTr("none",idDisplay14);
            }*/
			
        }else{
            //alert("Entramos 2: " + selection);
            document.frmdatos.txt_ItemFecDevolution.value="";
            document.frmdatos.cmb_ItemPendDevolution.value="";
            fxOcultarTr("yes",idDisplay14);//Fecha de devolucion
            fxOcultarTr("yes",idDisplay13);//Pend de recojo            
        }
        try{
          //document.frmdatos.cmb_ItemPromocion.selectedIndex = "";
          //DeleteSelectOptions(document.frmdatos.cmb_ItemPromocion);
        }catch(e){;}
        fxClearObjects("txt_ItemMoneda");
        fxClearObjects("txt_ItemPromotionId");
        fxClearObjects("txt_ItemPriceCtaInscrip");
        fxClearObjects("txt_ItemPriceException");
        fxClearObjects("txt_ItemEquipmentRent");
    }
    //Inicio CGC COR0447
    if (nameNpTable=="<%=Constante.NPT_EQUIPMENT_NOTYET_GIVEBACK%>"){     
        //var selection = objThis.options[objThis.selectedIndex].value; 
        var selection  = objValue; 
        var devolucion = document.frmdatos.cmb_ItemDevolution.value; 
        
        if (selection=="S" && devolucion=="S"){
            fxOcultarTr("none",idDisplay14);
            //fxOcultarTr("none",idDisplay13);
        }else{
            document.frmdatos.txt_ItemFecDevolution.value="";
            fxOcultarTr("yes",idDisplay14);
            //fxOcultarTr("yes",idDisplay13);            
        };
    }if (nameNpTable=="LINK_TYPE"){
        var selection  = objValue; 
        var cadena = "";
        var objNameObject = "";
        var objObject;
        //Si es Adicional
        if( selection == "A" ){
          //Seteamos el control para que sea obligatorio
          objObject = parent.mainFrame.fxGetObjectProperties(111);
          objObject.npvalidateflg = 'S';
          objNameObject = objObject.npobjitemname;
          cadena = "<font color='red'>&nbsp;*&nbsp;</font> "+objNameObject;
          idDisplayValidate111.innerHTML = cadena;
        //Si es Nuevo
        }else if ( selection == "N" ){
          objObject = parent.mainFrame.fxGetObjectProperties(111);
          objObject.npvalidateflg = 'N';
          objNameObject = objObject.npobjitemname;
          cadena = "<font color='red'>&nbsp;&nbsp;&nbsp;</font> "+objNameObject;
          idDisplayValidate111.innerHTML = cadena;
        }
        
    }
    //Fin CGC COR0447
  }
  </script>
  
  <script>
  function fxClearObjects(objObject){
    form = parent.mainFrame.frmdatos;
    try{
         eval("form."+objObject+".value=''");
    }catch(e){
    }
  }
  
  function fxShowObjectNPEdit(nameNpTable,objValue,ocjValueAux){
    
    //var selection = objThis.options[objThis.selectedIndex].value; 
    var selection  = objValue; 
    var devolucion = ocjValueAux; 

    if (selection=="S" && devolucion=="S"){
        fxOcultarTr("none",idDisplay14);
        //fxOcultarTr("none",idDisplay13);
    }else{
        document.frmdatos.txt_ItemFecDevolution.value="";
        fxOcultarTr("yes",idDisplay14);
        //fxOcultarTr("yes",idDisplay13);            
    }
  
  }
  
  function fxLoadNpTable(){

<%
  String[] paramNpobjitemheaderid = request.getParameterValues("a");
  String[] paramNpobjitemvalue    = request.getParameterValues("b");
  
  if (paramNpobjitemvalue!= null){
       for (int i = 0; i < paramNpobjitemheaderid.length ; i++){
          if( paramNpobjitemheaderid[i] != null ){
%>
           
<%
            //CGC COR0447
            if (("12").equals(paramNpobjitemheaderid[i])){ // Devolucion
%>
            fxShowObjectNP("<%=Constante.NPT_EQUIPMENT_RETURN%>","<%=paramNpobjitemvalue[i]%>");
<%
            }
            if (("13").equals(paramNpobjitemheaderid[i])){ // Pendiente de Recojo
%>
            fxShowObjectNPEdit("<%=Constante.NPT_EQUIPMENT_NOTYET_GIVEBACK%>","<%=paramNpobjitemvalue[i]%>","<%=paramNpobjitemvalue[i]%>");
<%
            }
            
            if (("33").equals(paramNpobjitemheaderid[i])){ // Tipo de Enlace
%>
            fxShowObjectNP("LINK_TYPE","<%=paramNpobjitemvalue[i]%>");
<%
            // Fin CGC COR0447
            }
          }
       }
  }
%>

  }
 
function fxLoadNpTable2(){ 

<%
  paramNpobjitemheaderid = request.getParameterValues("a");
  paramNpobjitemvalue    = request.getParameterValues("b");
  String strSolutionId   = ""; 
  String contract        = null;
  if (paramNpobjitemvalue!= null){
       for (int i = 0; i < paramNpobjitemheaderid.length ; i++){
          if( paramNpobjitemheaderid[i] != null ){            
            if (("26").equals(paramNpobjitemheaderid[i])){ // Contrato a asociar
              contract = MiUtil.getString(paramNpobjitemvalue[i]);
            }
            if( paramNpobjitemheaderid[i].equals("93") ) {
              strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);        
            }
            
          }
       }
       if (contract != null){
       %>
          fxShowObjectNPContractAssociates("<%=contract%>", "<%=strSolutionId%>");
       <%
       }
  }
  
%>

}

function fxLoadModality(){      
   vform = parent.mainFrame.document.frmdatos;
   vEquipo = vform.txt_ItemEquipment.value;   
   if (vEquipo == "Alquiler" ){
      vform.cmb_ItemDevolution.value="S";       
      vform.cmb_ItemDevolution.disabled=true;
      if (vform.cmb_ItemPendDevolution != null)
         vform.cmb_ItemPendDevolution.value="S";
      fxOcultarTr("none",idDisplay13);
      }   
   else{      
      vform.cmb_ItemDevolution.disabled = false; 
      vform.cmb_ItemDevolution.value = ""; 
      if (vform.cmb_ItemPendDevolution != null)
         vform.cmb_ItemPendDevolution.value="";
   }   
}

</script>