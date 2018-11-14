<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.bean.SectionDinamicBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  </head>
  <body>
  
  <%! //INICIO: AMENDEZ | PRY-1141
      protected static Logger logger = Logger.getLogger(HttpServlet.class);
  
      protected String formatException(Throwable e) {
          return GenericObject.formatException(e);
      }
      //FIN: AMENDEZ | PRY-1141
  %>
  <%
  logger.info("*********************************************JSP_FORWARD.jsp*********************************************");
  ArrayList objDataSpecification  = (ArrayList)request.getAttribute("objDataSpecification");
  ArrayList objDataDispatchList   = (ArrayList)request.getAttribute("arrDataDispatchList");
  ArrayList objPayForm            = (ArrayList)request.getAttribute("arrListadoPayForm");
  ArrayList arrDataSalesman       = (ArrayList)request.getAttribute("arrSalesDataList");
  String strBuildingId            = (String)request.getAttribute("strBuildingId");
  String strShowDataFields        = MiUtil.getString((String)request.getAttribute("strShowDataFields"));    
  String strDataSalesProvId       = MiUtil.getString((String)request.getAttribute("strDataSalesProvId"));
  String strFlagexclusivity       = MiUtil.getString((String)request.getAttribute("strFlagexclusivity"));
  String strLoadSellerData        = MiUtil.getString((String)request.getAttribute("strLoadSellerData"));
  String strLoadDealer            = MiUtil.getString((String)request.getAttribute("strLoadDealer"))==null?"":MiUtil.getString((String)request.getAttribute("strLoadDealer"));
    
  logger.info("strBuildingId      :  "+strBuildingId);
  logger.info("strShowDataFields  :  "+strShowDataFields);
  logger.info("strDataSalesProvId :  "+strDataSalesProvId);
  logger.info("strFlagexclusivity :  "+strFlagexclusivity);
  logger.info("strLoadSellerData  :  "+strLoadSellerData);
  logger.info("strLoadDealer      :  "+strLoadDealer);
    
  String strSpecification = ""
         //INICIO: AMENDEZ | PRY-1049
          ,strHdnCobertura=""
         //FIN: AMENDEZ | PRY-1049
          //INICIO: AMENDEZ | PRY-1141
          ,strhdnIUserId=""
          ,strCustomerId=""
          ,strURLOrderServlet=request.getContextPath()+"/editordersevlet"
          //FIN: AMENDEZ | PRY-1141
;
  //INICIO: AMENDEZ | PRY-1141
  int ivalidatePaymentTermsCI=0;
  //FIN: AMENDEZ | PRY-1141
  
  //INICIO: PRY-1200 | AMENDEZ
  String strValidateSpecificationVep=null;
  String strDefaultQuotaVEP=null;
  //FIN: PRY-1200 | AMENDEZ

  if (objDataSpecification == null)
   objDataSpecification = new ArrayList();

  String registerSection  = "";
  String sectionEventOnloadToExec = "";
  Hashtable hshtInputNewSection = null;
  
  if( objDataSpecification != null ){
  String path = "";
  SectionDinamicBean specificationBean = null;
  
  if (strLoadDealer.equals("null") || strLoadDealer=="null" || strLoadDealer==null ) strLoadDealer="";
  
  %>
  <form name="frmdatos">
  <textarea name="txtSpecificationSections">
  <%
  //try{
  hshtInputNewSection = (Hashtable)request.getAttribute("hshtInputNewSection");
  strSpecification        =   MiUtil.getString((String)hshtInputNewSection.get("strSpecificationId"));
  //INICIO: AMENDEZ | PRY-1049
  strHdnCobertura         =   MiUtil.getString((String)hshtInputNewSection.get("strHdnCobertura"));
  logger.info("[JSP_FORWARD.jsp]hshtInputNewSection.toString(): "+hshtInputNewSection.toString());
  logger.info("[JSP_FORWARD.jsp]strHdnCobertura: "+strHdnCobertura);
  //FIN: AMENDEZ | PRY-1049
  logger.info("[JSP_FORWARD.jsp]strSpecification: "+strSpecification);
  //INICIO: AMENDEZ | PRY-1141
  strhdnIUserId=   MiUtil.getString((String)hshtInputNewSection.get("strhdnIUserId"));
  strCustomerId=   MiUtil.getString((String)hshtInputNewSection.get("strCustomerId"));
  logger.info("[JSP_FORWARD.jsp]strhdnIUserId   :  "+strhdnIUserId);
  logger.info("[JSP_FORWARD.jsp]strCustomerId  :  "+strCustomerId);
  
  NewOrderService  newOrderService=new NewOrderService();
  long lstrCustomerId=Long.parseLong(strCustomerId);
  long lstrhdnIUserId=Long.parseLong(strhdnIUserId);
  ivalidatePaymentTermsCI= newOrderService.validatePaymentTermsCI(lstrCustomerId,lstrhdnIUserId,1);
  //FIN: AMENDEZ | PRY-1141

  //INICIO: PRY-1200 | AMENDEZ
  int iSpecificationId=MiUtil.parseInt(strSpecification);
  strValidateSpecificationVep=newOrderService.validateSpecificationVep(iSpecificationId);
  strDefaultQuotaVEP=newOrderService.getConfigValueVEP(Constante.DEFAULT_QUOTA_VEP);
  logger.info("[JSP_FORWARD.jsp]strValidateSpecificationVep  :  "+strValidateSpecificationVep);
  logger.info("[JSP_FORWARD.jsp]strDefaultQuotaVEP           :  "+strDefaultQuotaVEP);
  //FIN:  PRY-1200 | AMENDEZ

  logger.info("[JSP_FORWARD.jsp]strSpecification: "+strSpecification);
  
  request.setAttribute("hshtInputNewSection",hshtInputNewSection);
  
  registerSection += "\n parent.mainFrame.VOrderSections.removeElementAll(); ";
  logger.info("******************************************************************************************");
  for(int i=0; i<objDataSpecification.size(); i++){
    specificationBean   =   new  SectionDinamicBean();
    specificationBean   =   (SectionDinamicBean)objDataSpecification.get(i);
    
    registerSection += "\n parent.mainFrame.fxRegisterOrderSection('" + specificationBean.getNppagesectionid() + "','" + "SPECIFICATION" + "','" + specificationBean.getNpSpecificationId() + "','" + specificationBean.getNpeventname() + "','" + specificationBean.getNpeventhandler() + "', '' , '" + specificationBean.getNptypeobject() + "','" + specificationBean.getNpobjectname() + "','" + specificationBean.getNpbusinessobject() + "');";
    
    path = specificationBean.getNpeventhandler();
    
    if( specificationBean.getNpeventname().equals(Constante.NEW_ON_DISPLAY)){
        //System.out.println("Eventos NEW_ON_DISPLAY");
        String urlPath = "../../DYNAMICSECTION/" + path;
        
        //System.out.println("Ruta : " + urlPath);   
        %>
        <jsp:include page="<%=urlPath%>" flush="true" />
   <%
    }else if( specificationBean.getNpeventname().equals(Constante.ON_LOAD) ){
        //System.out.println("Eventos ON_VALIDATE"); 
        sectionEventOnloadToExec += "\n parent.mainFrame."+ path;
    }
    
  }
  
  //System.out.println("registerSection : \n" + registerSection);
  
  //}catch(Exception e){System.out.println("[Exception][JSP_FORWARD]["+e.getClass()+"]["+e.getCause()+"]["+e.getMessage()+"]["+e.getLocalizedMessage()+"]" );}
  
  }
  %>
  </textarea>
  </form>
	<script language="javascript" >
   
        function fxInitShowObjectRelatedSpec(){
         
            parent.mainFrame.IdSpecificationSections.innerHTML = "";
            parent.mainFrame.IdSpecificationSections.innerHTML = document.frmdatos.txtSpecificationSections.value;

            if(parent.mainFrame.frmdatos.hdnSpecification.value != ""){
               var v_hdnSpecification = parent.mainFrame.frmdatos.hdnSpecification.value;
               parent.mainFrame.fxDelObjVOrderSections("SPECIFICATION",v_hdnSpecification);
            } 
            
            <%=registerSection%>  
            <%=sectionEventOnloadToExec%>
            fxLoadDispatchList();
            fxLoadPayForm();
            fxSetSpecificationDate();
            fxLoadPaymentRespPooling();
            fxLoadDataSalesMen();
            fxFinishLoad();
         }
         
		function fxSetSpecificationDate(){
		
			var flgActivationDate      = '<%=MiUtil.parseInt((String)request.getAttribute("strFlgEnabled"))%>';
			var activationDateProcess  = '<%=MiUtil.getString((String)request.getAttribute("strActivationDate"))%>';
         
         var form          = parent.mainFrame.frmdatos;
         var docMainFrame  = parent.mainFrame;
         
         form.txtFechaProceso.value = activationDateProcess;
         
         if( flgActivationDate == 0 ){
				form.txtFechaProceso.readOnly = true;
         }else{
            form.txtFechaProceso.readOnly = false;
         }      
      }
			
		var dispatchPlaceArr = new Array();
		function fxMakeDispatchPlace(buildingid,shortname,processgroup) {
			this.buildingid   = buildingid;
			this.shortname   	= shortname;			
			this.processgroup = processgroup;			
		};	
		
		function fxLoadDispatchList(){
			var form      = parent.mainFrame.frmdatos;
			var processgroup="";
			
			docMainFrame  = parent.mainFrame;
                        docMainFrame.DeleteSelectOptions(form.cmbLugarAtencion);
                        var flgGeneratorType  = '<%=MiUtil.getString(request.getParameter("hdnGeneratorType")).toUpperCase()%>';

                        <%
                        if( objDataDispatchList != null ){
			   for( int j=0; j<objDataDispatchList.size(); j++ ){
			      HashMap objHashMap = (HashMap)objDataDispatchList.get(j);
                        %>	
                              dispatchPlaceArr['<%=j%>'] = new fxMakeDispatchPlace('<%=objHashMap.get("wn_npBuildingId")%>','<%=objHashMap.get("wv_npShortName")%>','<%=objHashMap.get("npWorkFlowType")%>');
                              docMainFrame.AddNewOption(form.cmbLugarAtencion,'<%=objHashMap.get("wn_npBuildingId")%>','<%=objHashMap.get("wv_npShortName")%>');         
                        <% }
                        }%>
         
			form.cmbLugarAtencion.value='<%=strBuildingId%>';
			processgroup=fxLoadProcessGroup('<%=strBuildingId%>');
    
      
			//si es ventas moviles postpago o prepago
			if ( ('<%=strSpecification%>'=='2001' || '<%=strSpecification%>'=='2068' || '<%=strSpecification%>'=='2069') && (processgroup=='Tienda_Lima' || processgroup=='')){
				form.cmbLugarAtencion.value=<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>;	
                                form.hdnLugarDespacho.value=<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>; //DLAZO
			}else{
				form.cmbLugarAtencion.value='<%=strBuildingId%>';
			}

			//Si no se encuentra la tienda a la que pertencee el usuario
			//por defecto se coloca FULLFILLMENT
			if (form.cmbLugarAtencion.value ==""){ 			
				form.cmbLugarAtencion.value=<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>;
			}
      
                        //Se deshanilita el combo si viene de Oportunidades
                        if( flgGeneratorType == '<%=Constante.GENERATOR_TYPE_OPP%>'){
                          if ( processgroup==''|| processgroup=='Tienda_Lima' || processgroup=='Fulfillment' ){ 
                                form.cmbLugarAtencion.value=<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>; 
                                form.hdnLugarDespacho.value = <%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>; 
                          }else{
                               form.cmbLugarAtencion.value='<%=strBuildingId%>';
                               form.hdnLugarDespacho.value = '<%=strBuildingId%>';
                          }
                              //form.cmbLugarAtencion.disabled = true;      eiortiz-09-01-2017
                        }else{
                              form.cmbLugarAtencion.disabled = false;
                              form.hdnLugarDespacho.value = '<%=strBuildingId%>';
                        }
      

      if (form.cmbLugarAtencion.value == '<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>'){
         form.chkCourier.checked = false;
         form.chkCourier.disabled = true;
         form.hdnChkCourier.value = "0";
      }else{
         form.chkCourier.disabled = false;
      }
      
            //INICIO DERAZO REQ-0940
            var flagTraceability = parent.mainFrame.flagTraceabilityFunct;

            if(flagTraceability != null){
                //Si esta activo el flag, realizamos las validaciones
                if(flagTraceability == 1){
                    var specificationId = form.cmbSubCategoria.value;
                    var typeDocument = form.hdnTypeDocument.value;
                    var numDocument = form.hdnDocument.value;
                    var dispatchPlaceId = form.cmbLugarAtencion.value;
                    var chkContactNotification = parent.mainFrame.document.getElementById("chkContactNotification");

                    //Obtenemos las iniciales del numero de documento para validar en caso de RUC
                    var prefixNumDocument = numDocument.substring(0, 2);

                    //Realizamos las validaciones para mostrar o no la seccion de contactos
                    var resValidation = fxValidateShowContacsSection(specificationId, dispatchPlaceId, typeDocument, prefixNumDocument);
      
                    //Ocultamos o mostramos la seccion de contactos
                    displayContactsSectionForward(resValidation);

                    if(resValidation){
                        //Actualizamos el valor del hidden para validar o no al grabar la orden
                        form.hdnTracebilityValidation.value = 1;
                        chkContactNotification.checked = true;
                    }
                    else{
                        form.hdnTracebilityValidation.value = 0;
                        chkContactNotification.checked = false;
                    }
                }
            }
            //FIN DERAZO
      
     return;
    }
         
        //INICIO DERAZO REQ-0940
        function displayContactsSectionForward(display) {
            var divContacts = parent.mainFrame.document.getElementById("divContacts");

            if (display) {
                divContacts.style.display = "inline";
            } else {
                divContacts.style.display = "none";
            }
        }

        function fxValidateShowContacsSection(specificationId, dispatchPlaceId, typeDocument, numDocument){
            var arrTracSpecifications = parent.mainFrame.arrTracSpecifications;
            var arrTracDispatchPlaces = parent.mainFrame.arrTracDispatchPlaces;
            var arrTracTypeDocuments = parent.mainFrame.arrTracTypeDocuments;
            var arrTracTypeCustomersRUC = parent.mainFrame.arrTracTypeCustomersRUC;
            var validateSpecification = false;
            var validateDispatchPlace = false;
            var validateTypeDocument = false;
            var validateTypeCustomerRUC = false;

            //Realizamos las validaciones si todos los criterios estan configurados
            if(arrTracSpecifications.length > 0 && arrTracDispatchPlaces.length > 0
                    && arrTracTypeDocuments.length > 0){

                for(var i=0; i<arrTracSpecifications.length; i++){
                    if (arrTracSpecifications[i] == specificationId){
                        validateSpecification = true;
                        break;
                    }
                }

                if(!validateSpecification) return false;

                for(var i=0; i<arrTracDispatchPlaces.length; i++){
                    if (arrTracDispatchPlaces[i] == dispatchPlaceId ){
                        validateDispatchPlace = true;
                        break;
                    }
                }

                if(!validateDispatchPlace) return false;

                for(var i=0; i<arrTracTypeDocuments.length; i++){
                    if (arrTracTypeDocuments[i] == typeDocument){
                        validateTypeDocument = true;

                        //Si es RUC, validamos el tipo de cliente
                        typeDocument = typeDocument.toUpperCase();
                        if(typeDocument == 'RUC'){
                            //Si no existe configuracion, no mostramos la seccion de contactos
                            if (arrTracTypeCustomersRUC.length > 0){
                                for(var j=0; j<arrTracTypeCustomersRUC.length; j++){
                                    if(arrTracTypeCustomersRUC[j] == numDocument){
                                        validateTypeCustomerRUC = true;
                                        break;
                                    }
                                }
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            validateTypeCustomerRUC = true;
                            break;
                        }
                    }
                }

                if(!validateTypeDocument) return false;
                if(!validateTypeCustomerRUC) return false;
            }
            else{
                //Si no existe configuracion para algun criterio, no se muestra la seccion de contactos
                return false;
            }

            return true;
        }
        //FIN DERAZO
         
		function fxLoadProcessGroup(buildingId) {
			form = document.frmdatos;
			var dispatchPlace = null;
			var processgroup="";
			for (i=0; i < dispatchPlaceArr.length ; i++) {
				dispatchPlace = dispatchPlaceArr[i];
				if (dispatchPlace.buildingid == buildingId) {
					processgroup = dispatchPlace.processgroup;
					break;
				}
			}
			return processgroup
		}	
			
    function fxLoadPayForm(){   
             var form      = parent.mainFrame.frmdatos;
           
             docMainFrame0  = parent.mainFrame;
             docMainFrame0.DeleteSelectOptions(form.cmbFormaPago);
            <%HashMap objHashMap=null;
              int payCargo = 0;
              if( objPayForm != null ){
                for( int j=0; j<objPayForm.size(); j++ ){
                  objHashMap = (HashMap)objPayForm.get(j);
                  if ((objHashMap.get("value")).equals("Cargo en el Recibo")) payCargo=1;
                    
               %>    
                docMainFrame0.AddNewOption(form.cmbFormaPago,'<%=objHashMap.get("value")%>','<%=objHashMap.get("descripcion")%>');          
               <%
                 }
             }
            %>
           
           
          
           if(form.txtTipoCuenta.value=='<%=Constante.EXCLUSIVITY_TYPE_MAYORES%>' || form.txtTipoCuenta.value=='<%=Constante.EXCLUSIVITY_TYPE_ESTRATEGICA%>'|| form.txtTipoCuenta.value=='<%=Constante.EXCLUSIVITY_TYPE_CORPORATIVO%>' ){
              <%if (payCargo==1){%>
                form.cmbFormaPago.value='Cargo en el Recibo';
              <%}else{%>
                form.cmbFormaPago.value='Contado';
              <%}%>
          } else{
            form.cmbFormaPago.value='Contado'; //Independiente de la campa�ia y categoria, por default se debe seleccionar CONTADO
                      
            //No encontro la forma de pago al Contado va a seleccionar la primera opci�n que encuentre en el combo forma de pago
            if (form.cmbFormaPago.value==''){              
              try{
                if (form.cmbFormaPago.options.length > 1){
                  form.cmbFormaPago.value=form.cmbFormaPago.options[1].value;                  
                }
              }
              catch(e){
                //no deberia entrar aqui - solo un caso extremo
                form.cmbFormaPago.value='';
              }
            }
            
            }

           // Validacion VEP
        //INICIO: AMENDEZ | PRY-1200
        var varValidateSpecificationVep ='<%=strValidateSpecificationVep%>';
        if(varValidateSpecificationVep == "1"){
            //FIN: AMENDEZ | PRY-1200

               //INICIO: AMENDEZ | PRY-1141
               form.cmbFormaPago.value='Cargo en el Recibo';
               //FIN: AMENDEZ | PRY-1141

            //INICIO: AMENDEZ | PRY-1200
            form.cmbVepNumCuotas.value='<%=strDefaultQuotaVEP%>';
            //FIN: AMENDEZ | PRY-1200
              if ( form.cmbFormaPago.value != '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
                 if (form.chkVepFlag != undefined && form.cmbVepNumCuotas != undefined){
                   form.chkVepFlag.checked = false;
                   form.chkVepFlag.value = 0;
                   form.chkVepFlag.disabled = true;
                   form.cmbVepNumCuotas.selectedIndex = 0;
                   form.cmbVepNumCuotas.disabled = true;
                 }
              //INICIO: AMENDEZ | PRY-1141
              }else{
                  var vepactive=1;
                  form.chkVepFlag.checked = true;
                  form.chkVepFlag.value = vepactive;
                  form.chkVepFlag.disabled = false;
                  form.cmbVepNumCuotas.disabled = false;
                  form.txtCuotaInicial.disabled = false;
                  var validatePaymentTerms=<%=ivalidatePaymentTermsCI%>;
                  if(validatePaymentTerms == "1"){
                      form.chkPaymentTermsIQ.disabled = false;
                  }else{
                      form.chkPaymentTermsIQ.disabled = true;
              }
           }
              //FIN: AMENDEZ | PRY-1141
           }
           
           return;
         }
    
        
         function fxFinishLoad(){
            parent.mainFrame.frmdatos.hdnSpecification.value = <%=hshtInputNewSection.get("hdnSpecification")%>;
            
            location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
         }
         
         function fxLoadPaymentRespPooling(){            
   
             var form         = parent.mainFrame.frmdatos;
             var numRespPago  = form.cmbResponsablePago.length - 1;
             
             //Si no tiene responsables de pago,es una cuenta flat. No se muestra el responsable de pago de la bolsa
             if(numRespPago == 0){
                form.txtPaymentRespDesc.style.display = "none";
                parent.mainFrame.document.getElementById("txtLblPaymentResp").style.display = "none";
                return;
             }
           return;
         }
                            
          function fxLoadDataSalesMen(){
               
              var form      = parent.mainFrame.frmdatos;     
              docMainFrame0  = parent.mainFrame;
              docMainFrame0.DeleteSelectOptions(form.cmbVendedorData);
              form.hdnLoadSellerData.value = "<%=strLoadSellerData%>";
              <%
                HashMap hsh=null;
                String strDatoValue=null;
                String strDatoText=null;
              %>
              if ( "<%=strShowDataFields%>" == "1"){
                docMainFrame0.fxShowDataFields();
                <%                
                if (arrDataSalesman != null){
                  for (int i=0; i< arrDataSalesman.size(); i++){
                    hsh = (HashMap)arrDataSalesman.get(i);
                    strDatoValue= (String)hsh.get("npprovidergrpid");  
                    strDatoText= (String)hsh.get("swname");
                    if (strDatoValue!=null && !"".equals(strDatoValue)){
                      %>docMainFrame0.AddNewOption(form.cmbVendedorData,"<%=strDatoValue%>","<%=strDatoText%>");<%
                    }                 
                  }
                  if ( !"".equals(strDataSalesProvId)){
                    //Se debe setear el vendedor por defecto
                    %>docMainFrame0.SetCmbDefaultValue(form.cmbVendedorData,"<%=strDataSalesProvId%>");<%
                  }
                  if ( !strFlagexclusivity.equalsIgnoreCase("N") ){
                    //Tiene exclusividad. Se bloquea el combo
                    %>form.cmbVendedorData.disabled = true;<%
                  }
                }
                %>
                //Se Setea el Dealer
                form.txtDealer.value="<%=strLoadDealer%>";
                form.hdnDealer.value="<%=strLoadDealer%>";
              }else if ( "<%=strShowDataFields%>" == "0") {
                docMainFrame0.DeleteSelectOptions(form.cmbVendedorData);
                docMainFrame0.fxHideDataFields();
              }
              
              if ( "<%=strLoadSellerData%>" == "1" ) { 
                //Se debe cargar los vendedores de data en el combo Vendedor
                docMainFrame0.DeleteSelectOptions(form.cmbVendedor);               
                <%               
                if (arrDataSalesman != null){
                  hsh = null;
                  for (int i=0; i< arrDataSalesman.size(); i++){
                    hsh = (HashMap)arrDataSalesman.get(i);
                    strDatoValue= (String)hsh.get("npprovidergrpid");  
                    strDatoText= (String)hsh.get("swname");
                    if (strDatoValue!=null && !"".equals(strDatoValue)){
                      %>docMainFrame0.AddNewOption(form.cmbVendedor,"<%=strDatoValue%>","<%=strDatoText%>");<%
                    }                 
                  }
                  if ( !"".equals(strDataSalesProvId)){
                    //Se debe setear el vendedor por defecto
                    %>docMainFrame0.SetCmbDefaultValue(form.cmbVendedor,"<%=strDataSalesProvId%>");<%
                  }
                  if ( !strFlagexclusivity.equalsIgnoreCase("N") ){
                    //Tiene exclusividad. Se bloquea el combo
                    %>form.cmbVendedor.disabled = true;<%
                  }else{
                      //No tiene exclusividad. El combo se desbloquea  
                     %>form.cmbVendedor.disabled = false;<%
                  }
                }
                %>
                //Se Setea el Dealer
                form.txtDealer.value="<%=strLoadDealer%>";
                form.hdnDealer.value="<%=strLoadDealer%>";
              }
              
            }
         
         onload = fxInitShowObjectRelatedSpec;
      </script> 
    
  </body>
</html>