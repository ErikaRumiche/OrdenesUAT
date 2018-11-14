package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ContractResponse;
import pe.com.nextel.bean.ContractWorkflow;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.OrderWorkFlow;
import pe.com.nextel.bean.UbigeoBean;
import pe.com.nextel.bo.OrdersWorkFlowManageBO;
import pe.com.nextel.form.RetailForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


//import wsp.contractcreate.proxy.ContractCreateOut;
public class RetailServlet extends GenericServlet {

    /**
     * Motivo:  Método que se invoca al seleccionar la informacion referida al Combo de Tipo de Documento
     *          e ingresar el Número de Documento.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 22/08/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadCustomerInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                  IOException {
        String cmbTipoDocumento = StringUtils.defaultString(request.getParameter("cmbTipoDocumento"));
        String txtNroDocumento = StringUtils.defaultString(request.getParameter("txtNroDocumento"));
        PrintWriter out = response.getWriter();
        String strError  = null;      
        try {
            System.out.println("TipoDocLoadCust : " + cmbTipoDocumento);
            System.out.println("NumDocLoadCust : "  + txtNroDocumento);
            CustomerBean customer = objCustomerService.loadCustomerInfo(cmbTipoDocumento, txtNroDocumento);
            strError = customer.getStrMessage();
            //AddressObjectBean address = objCustomerService.getAddressByCustomer(cmbTipoDocumento, txtNroDocumento);            
            if (StringUtils.isNotEmpty(strError)){                     
               request.setAttribute("strErrorLoadCust", strError);                                  
               out.println("<html>");              
               out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
               out.println("<script language='JavaScript'>");
               out.println("var form = parent.mainFrame.document.frmdatos;");  
               out.println("form.txtNroDocumento.value     = '';");
               out.println("form.txtCodigoBSCS.value     = '';");
               out.println("form.txtTipoCliente.value     = '';");
               out.println("form.txtRazonSocial.value     = '';");
               out.println("form.txtNombres.value     = '';");
               out.println("form.txtApellidos.value     = '';");
               out.println("form.txtTelefono.value     = '';");               
               out.println("form.cmbGiro.selectedIndex     = '';");
               out.println("form.cmbDepartamento.selectedIndex     = '';");               
               out.println("DeleteSelectOptions(form.cmbCuenta);");
               out.println("form.cmbTituloCliente.options[0].selected = true;");
               out.println("DeleteSelectOptions(form.cmbProvincia);");
               out.println("DeleteSelectOptions(form.cmbDistrito);");
               out.println("DeleteSelectOptions(form.cmbSubGiro);");
               out.println("for(a=0;a<parent.mainFrame.document.frmdatos.txtDireccion.length;a++) {");
               out.println("form.txtDireccion[a].value = '';");
               out.println("var optionProv = new Option('', 0);");
               out.println("parent.mainFrame.document.frmdatos.cmbProvincia.options[0] = optionProv;");
               out.println("var optionDist = new Option('', 0);");
               out.println("parent.mainFrame.document.frmdatos.cmbDistrito.options[0] = optionDist;");               
               out.println("}");                                                                        
               out.println("alert(\""+strError+"\");");               
               out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
               out.println("</script>");
               out.close();
            }
            else{
			if(logger.isDebugEnabled())
				logger.debug("Información del Customer: "+customer.toString());
            request.setAttribute("customer", customer);
         	long lGiroId = customer.getNpGiroId();
            HashMap hshDataMap = objGeneralService.getSubGirosByGiroList(lGiroId);         
            //si no carga subGiro, que lo busque éste método
            ArrayList arrSubGiroList = (ArrayList) hshDataMap.get("arrSubGiroList");
            if(arrSubGiroList != null){
              if(arrSubGiroList.size()<1){
                hshDataMap = null;
                hshDataMap = objGeneralService.getSubGirosByIndustry(lGiroId); 
              }
            }            
            request.setAttribute("hshDataMap", hshDataMap);
            if(logger.isDebugEnabled())
               logger.debug("Antes de hacer el getRequestDispatcher");            
            request.getRequestDispatcher("pages/loadCustomerInfo.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(formatException(e));                  
        }
    }

    /*
     * Motivo:  Método que muestra el confirm si encuentra coincidencia de documentos
     * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
     * <br>Fecha: 05/08/2008        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                            IOException {
        PrintWriter out = response.getWriter();    
        String strMessage = null;
        String strNumDoc="",strTipoDoc="";
        String cmbTipoDocumento = StringUtils.defaultString(request.getParameter("cmbTipoDocumento"));
        String txtNroDocumento = StringUtils.defaultString(request.getParameter("txtNroDocumento"));
        HashMap hashDataValidate=null;
        if (logger.isDebugEnabled()) {
            logger.debug("cmbTipoDocumento:::" + cmbTipoDocumento);
            logger.debug("txtNroDocumento:::" + txtNroDocumento);
        }        
        hashDataValidate = objCustomerService.getValidateDniRucEquals(txtNroDocumento,cmbTipoDocumento);
        strMessage=(String)hashDataValidate.get("strMessage");
        if (strMessage!=null){
           out.println("<html>");
           out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
           out.println("<script language='JavaScript'>");               
           out.println("alert(\""+strMessage+"\");");   
           out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
           out.println("</script>");
           out.close();               
        }
        strNumDoc  = (String)hashDataValidate.get("strNumDoc");
        strTipoDoc = (String)hashDataValidate.get("strTipoDoc");
        
        System.out.println("TipoOriginal : " + cmbTipoDocumento);
        System.out.println("NumDocOriginal : "  + txtNroDocumento);
        System.out.println("strNumDoc : "  + strNumDoc);
        System.out.println("strTipoDoc : " + strTipoDoc);
        System.out.println(request.getContextPath());
        
        if(strNumDoc != null && strTipoDoc != null){
            out.println("<html>");
            out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
            out.println("<script language='JavaScript'>"); 
            out.println("var rpta = confirm('Ya existe la compañía "+strTipoDoc+" con número de documento "+strNumDoc+", ¿Desea continuar? ');");
            out.println("if(rpta){");
            out.println("  location.replace('"+request.getContextPath()+"/retailServlet?hdnMethod=loadCustomerInfo&cmbTipoDocumento="+cmbTipoDocumento+"&txtNroDocumento="+txtNroDocumento+"');");            
            out.println("}else{");
            out.println("var form = parent.mainFrame.document.frmdatos;");  
            out.println("form.txtNroDocumento.value     = '';");
            out.println("form.txtCodigoBSCS.value     = '';");
            out.println("form.txtTipoCliente.value     = '';");
            out.println("form.txtRazonSocial.value     = '';");
            out.println("form.txtNombres.value     = '';");
            out.println("form.txtApellidos.value     = '';");
            out.println("form.txtTelefono.value     = '';");               
            out.println("form.cmbGiro.selectedIndex     = '';");
            out.println("form.cmbDepartamento.selectedIndex     = '';");               
            out.println("DeleteSelectOptions(form.cmbCuenta);");
            out.println("form.cmbTituloCliente.options[0].selected = true;");
            out.println("DeleteSelectOptions(form.cmbProvincia);");
            out.println("DeleteSelectOptions(form.cmbDistrito);");
            out.println("DeleteSelectOptions(form.cmbSubGiro);");
            out.println("for(a=0;a<parent.mainFrame.document.frmdatos.txtDireccion.length;a++) {");
            out.println("form.txtDireccion[a].value = '';");
            out.println("var optionProv = new Option('', 0);");
            out.println("parent.mainFrame.document.frmdatos.cmbProvincia.options[0] = optionProv;");
            out.println("var optionDist = new Option('', 0);");
            out.println("parent.mainFrame.document.frmdatos.cmbDistrito.options[0] = optionDist;");               
            out.println("}");                                                                                     
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");            
            out.println("}");
            out.println("</script>");
            out.close();
        }else{
            out.println("<html>");
            out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
            out.println("<script language='JavaScript'>"); 
            out.println("  location.replace('"+request.getContextPath()+"/retailServlet?hdnMethod=loadCustomerInfo&cmbTipoDocumento="+cmbTipoDocumento+"&txtNroDocumento="+txtNroDocumento+"');");
            out.println("</script>");
            out.close();
        }

    }

    /*
     * Motivo:  Método que se invoca al seleccionar los Combos de Provincia y Distrito
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 22/08/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadUbigeo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                            IOException {
        UbigeoBean objUbigeoBeanParametro = new UbigeoBean();
        objUbigeoBeanParametro.setDepartamento(StringUtils.defaultString(request.getParameter("cmbDepartamento")));
        objUbigeoBeanParametro.setProvincia(StringUtils.defaultString(request.getParameter("cmbProvincia")));
        objUbigeoBeanParametro.setDistrito(StringUtils.defaultString(request.getParameter("cmbDistrito")));
        String strJerarquia = StringUtils.defaultString(request.getParameter("jerarquia"));        
        if (logger.isDebugEnabled()) {
            logger.debug("objUbigeoParametro:::" + objUbigeoBeanParametro);
            logger.debug("strJerarquia:::" + strJerarquia);
        }
        try {
            HashMap hshDataMap = objGeneralService.getUbigeoList(objUbigeoBeanParametro);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            request.setAttribute("strJerarquia", strJerarquia);
            if(logger.isDebugEnabled())
				logger.debug("Antes de hacer el getRequestDispatcher");
            request.getRequestDispatcher("pages/loadUbigeo.jsp").forward(request, response);            
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /*
     * Motivo:  Método que se invoca al seleccionar el Combo de Giro de Negocio
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 22/08/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadSubGiro(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                             IOException {
        String cmbGiro = StringUtils.defaultString(request.getParameter("cmbGiro"), "0");
        long giroId = Long.parseLong(cmbGiro);
        if (logger.isDebugEnabled())
            logger.debug("giroId:::" + giroId);
        try {
            HashMap hshDataMap = null;
            hshDataMap = objGeneralService.getSubGirosByGiroList(giroId);
            ArrayList arrSubGiroList = (ArrayList) hshDataMap.get("arrSubGiroList");
            if(arrSubGiroList != null){
              if(arrSubGiroList.size()<1){
                hshDataMap = null;
                hshDataMap = objGeneralService.getSubGirosByIndustry(giroId); 
              }
            }
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled())
				logger.debug("Antes de hacer el getRequestDispatcher");
            request.getRequestDispatcher("pages/loadSubGiro.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }


    /*
     * Motivo:  Método que se invoca al seleccionar algún Combo de Kits para cargar
     *          la información del Equipo, Servicio y Plan.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 13/09/2007
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadKitInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                             IOException {
        String strCmbKit = StringUtils.defaultString(request.getParameter("cmbKit"), "");
        String strIndex = StringUtils.defaultString(request.getParameter("index"), ""); 
        String strRetailId = StringUtils.defaultString(request.getParameter("strRetailId"), ""); 
        long lKitId = Long.parseLong(strCmbKit);
        String strMessage = null;
        String strStructOrigen = null;
        if (logger.isDebugEnabled()) {
            logger.debug("loadKitInfo*****************************************");
            logger.debug("lKitId:::" + lKitId);
            logger.debug("strIndex:::" + strIndex);
            logger.debug("strRetailId:::" + strRetailId);
        }
        try {
            
            HashMap hshStructDetailMap = objGeneralService.getSalesStructOrigenxRetail(MiUtil.parseLong(strRetailId));
            strMessage=(String)hshStructDetailMap.get("strMessage");
            if (strMessage!=null)
                throw new Exception(MiUtil.getMessageClean(strMessage));
            strStructOrigen = (String)hshStructDetailMap.get("iSalesStructOrigen");
            
            System.out.println("strStructOrigen:::" + strStructOrigen);
          
            HashMap hshKitDetailMap = objGeneralService.getKitDetail(lKitId, Constante.MODALIDAD_VENTAS,MiUtil.parseLong(strStructOrigen));
            request.setAttribute("hshKitDetailMap", hshKitDetailMap);
            if(logger.isDebugEnabled())
				logger.debug("Antes de hacer el getRequestDispatcher");
            request.setAttribute("index", strIndex);
            request.getRequestDispatcher("pages/loadKitInfo.jsp").forward(request, response);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException) e;
                logger.error(formatException((Exception) se.getRootCause()));
            }
            logger.error(formatException(e));
        }
    }
	
	public void loadImeiInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strImei = StringUtils.defaultString(request.getParameter("txtIMEI"));
        String strIndex = StringUtils.defaultString(request.getParameter("index"));
		if (logger.isDebugEnabled()) {
            logger.debug("strImei:::" + strImei);
            logger.debug("strIndex:::" + strIndex);   
        }
        try {
            HashMap hshDataMap = objGeneralService.getInfoImei(strImei);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled()) {
				logger.debug("hshDataMap: "+hshDataMap);
				logger.debug("Antes de hacer el getRequestDispatcher");
			}
            request.setAttribute("index", strIndex);
            request.getRequestDispatcher("pages/loadImeiInfo.jsp").forward(request, response);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException) e; 
                logger.error(formatException((Exception) se.getRootCause()));
            }
            logger.error(formatException(e));
        }
    }
                                                                           
   
   /*public void newOrderRetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                          IOException {
      if (logger.isDebugEnabled())
            logger.debug("--Inicio newOrderRetail--");
		HashMap hshDataMap	=new HashMap();		
        try {
			String strMessage = null;
         PrintWriter out = response.getWriter();
            RetailForm objRetailForm = new RetailForm();
            objRetailForm = (RetailForm) objRetailForm.populateForm(request);
            System.out.println("[RetailServlet][neworderRetail][getCmbCuenta]"+objRetailForm.getCmbCuenta());            
            //int iCustomerIdBSCS = objRetailService.getSiteidByCodbscs(objRetailForm.getCmbCuenta());
            //System.out.println("iCustomerIdBSCS: "+iCustomerIdBSCS);
            BigDecimal bgCustomerIdBSCS;
            String codBscs = objRetailForm.getCmbCuenta();
            System.out.println("codBscs: "+codBscs+"****");
            String strLogin=null;                                       
            strLogin=MiUtil.getString(objPortalSesBean.getLogin());
            System.out.println("strLogin: "+strLogin);
				//objOrderService.updSinchronizeActiv(MiUtil.parseLong(strOrderId));
            if (!codBscs.equals("")){     
               System.out.println("[RetailServlet][codBscs][NOT NULL]");
               int iCustomerIdBSCS = objRetailService.getSiteidByCodbscs(codBscs);
               bgCustomerIdBSCS = new BigDecimal(Integer.toString(iCustomerIdBSCS));
            }
            else{
               System.out.println("[RetailServlet][codBscs][NULL]");
               //bgCustomerIdBSCS = new BigDecimal((String)hshCustomerMap.get("strCustomerIdBSCS"));                                    
            }
        }
        catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException) e;
                logger.error(formatException((Exception) se.getRootCause()));
            }
            logger.error(formatException(e));
        }
   }      */                                                                           
   
    /*
     * Motivo:  Método que se invoca al hacer click en el Botón Procesar.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 22/08/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void newOrderRetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
    if (logger.isDebugEnabled())logger.debug("--Inicio newOrderRetail--");
		HashMap hshDataMap	=new HashMap();		
    try {
			String strMessage = null;
      String strMessageCreNewCont = null;
      PrintWriter out = response.getWriter();
      RetailForm objRetailForm = new RetailForm();
      objRetailForm = (RetailForm) objRetailForm.populateForm(request);
			hshDataMap = objRetailService.newOrderRetail(objRetailForm);		
			if(logger.isDebugEnabled())logger.debug("Mostrando el hshDataMap: "+hshDataMap);
			String strOrderId = MiUtil.defaultString(hshDataMap.get("orderId"),"");  
			if(StringUtils.isNotBlank(strOrderId)) {
				long lOrderId = Long.parseLong(strOrderId);
				//Inicio creación del cliente 
				OrderWorkFlow orderWorkFlow = new OrderWorkFlow();								
				orderWorkFlow.setNpOrderId(String.valueOf(lOrderId));			
				//orderWorkFlow.setNpSolutionId(Constante.SOLUCION_PREPAGO);
				orderWorkFlow.setNpDivisionId(Constante.KN_TELEFONIA_IDEN);
				OrdersWorkFlowManageBO ordersWorkFlowManageBO = new OrdersWorkFlowManageBO();												
				String strMessageCreaNewCust = ordersWorkFlowManageBO.createNewCustomer(orderWorkFlow);							
				if (strMessageCreaNewCust==null){if(logger.isDebugEnabled())logger.debug("Se ejecuto correctamente - createNewCustomer-> null");}
				if (strMessageCreaNewCust!=null){
					strMessageCreaNewCust="Hubo un error en la creación del cliente en BSCS: "+strMessageCreaNewCust;	
					hshDataMap.put("strMessageCustCodeBSCS", strMessageCreaNewCust);        
				}											
			    //Fin creación del cliente              
				if(StringUtils.isBlank(strMessageCreaNewCust)) { //Se creo correctamente el cliente en BSCS se puede iniciar creación de contratos
					//Obtener el codigo BSCS
					HashMap hshCustomerMap=objCustomerService.getInfoCustomer(lOrderId);
					String strMsgInfoCust=(String)hshCustomerMap.get("strMessage");               
					if(StringUtils.isBlank(strMsgInfoCust)){
						if(logger.isDebugEnabled())logger.debug("No hubo errores en la invocacion de getInfoCustomer");	
            hshDataMap.put("strCustCodeBSCS", hshCustomerMap.get("strCustCodeBSCS"));
						hshDataMap.put("strCustomerIdBSCS", hshCustomerMap.get("strCustomerIdBSCS"));
						hshDataMap.put("strCustomerId", hshCustomerMap.get("strCustomerId"));
						hshDataMap.put("strOrderId", lOrderId+"");
						//Inicio Creación de contratos
						ArrayList arrContractInfoList = new ArrayList();
						HashMap hshContractInfoMap=null;
						boolean bCreoContrato=false;
						int contador =0;                                    
            String codBscs = objRetailForm.getCmbCuenta();
            BigDecimal bgCustomerIdBSCS;
            System.out.println("SOP_codBscs : " + codBscs);
            System.out.println("SOP_strCustomerIdBSCSHshCustMap : " + hshCustomerMap.get("strCustomerIdBSCS"));
            if (!codBscs.equals("")){  
               int iCustomerIdBSCS = objRetailService.getSiteidByCodbscs(codBscs);
               bgCustomerIdBSCS = new BigDecimal(Integer.toString(iCustomerIdBSCS));
            }
            else{
               bgCustomerIdBSCS = new BigDecimal((String)hshCustomerMap.get("strCustomerIdBSCS"));                                    
            }
            System.out.println("SOP_bgCustomerIdBSCS : " + bgCustomerIdBSCS);
            String strCadItem = MiUtil.defaultString(hshDataMap.get("cadItem"),"");  
            String strCadItemDev = MiUtil.defaultString(hshDataMap.get("caditemDev"),""); 
            String[] strItem = strCadItem.split("-");   
            String[] strItemDev = strCadItemDev.split("-");
						for (int i = 0; i < objRetailForm.getHdnItem().length; i++){                                          
							if(StringUtils.isNotBlank(objRetailForm.getHdnItem()[i])){                        
								String strImei=objRetailForm.getTxtIMEI()[i];                        
								BigDecimal bgTmCode=new BigDecimal((String)objRetailForm.getHdnPlanTarifario()[i]);                        
								ContractWorkflow contractWorkflow = new ContractWorkflow();	                        
								contractWorkflow.setBgCsID(bgCustomerIdBSCS);                        
								contractWorkflow.setStrImei(strImei);                        
								contractWorkflow.setBgTmCode(bgTmCode);
								if(logger.isDebugEnabled()){
									logger.debug("Fila ["+i+"]");
									logger.debug("HdnItem: "+objRetailForm.getHdnItem()[i]);
									logger.debug("strImei --> "+strImei);
									logger.debug("bgCustomerIdBSCS --> "+bgCustomerIdBSCS.intValue()+"");
									logger.debug("bgTmCode --> "+bgTmCode.intValue()+"");
								}	
								ordersWorkFlowManageBO = new OrdersWorkFlowManageBO();								              
                ContractResponse contractResponse = null;                           
                contractResponse = ordersWorkFlowManageBO.createNewContract(orderWorkFlow,contractWorkflow);								                           									
                if(logger.isDebugEnabled())logger.debug("Mensaje de createNewContract: "+contractResponse.getStrMessage());                          
                if (contractResponse.getStrMessage()==null){                           
                  if(logger.isDebugEnabled())logger.debug("Se ejecuto correctamente - createNewContract");
                  hshContractInfoMap = new HashMap();										
                  hshContractInfoMap.put("lContratoId", (String)contractResponse.getStrCoId());
                  hshContractInfoMap.put("strTelefono", (String)contractResponse.getStrTelefonia());
                  hshContractInfoMap.put("hdnItem", objRetailForm.getHdnItem()[i]); 
                  // Actualiza teléfono y contrato
                  String strMessageItem = objRetailService.updPhoneItem(Long.parseLong(strItem[i]),(String)contractResponse.getStrTelefonia());
                  String strMessageItemDev = objRetailService.updContractItemDev(Long.parseLong(strItemDev[i]), Long.parseLong(contractResponse.getStrCoId()));
                  if(logger.isDebugEnabled()){
                    logger.debug("lContratoId: "+ contractResponse.getStrCoId());
                    logger.debug("strTelefono: "+ contractResponse.getStrTelefonia());
                    logger.debug("hdnItem["+i+"] -->"+objRetailForm.getHdnItem()[i]);											
                  }										
										arrContractInfoList.add(hshContractInfoMap);
										contador=contador+1;										
									}
									else{
										if(logger.isDebugEnabled()){
                        logger.debug("Hubo errores en la ejecución del createNewContract");	
                        String strResponseMessage = contractResponse.getStrMessage();
                        strMessageCreNewCont = "Error en la creación de contratos en BSCS: "+((strResponseMessage.length()>100)?strResponseMessage.substring(0,100):strResponseMessage);                        
                    }
						break;
									}																		
						    }//fin del IF que evalua las filas a las cuales se les ha ingresado IMEI
						}//fin del FOR que crea los contratos
						//Si la creación de contratos concluyo satisfactoriamente pasar de Prospect a Customer
            hshDataMap.put("strMessageCreateContract", strMessageCreNewCont);						
            hshDataMap.put("arrContractInfoList", arrContractInfoList);            
            String strLogin = objPortalSesBean.getLogin();
            System.out.println("RetailOrdenLogin : " + strLogin);
            System.out.println("RetailOrdenLoginForm : " + objRetailForm.getHdnLogin());
            //if(objRetailForm.getTxtTipoCliente().equals(Constante.CUSTOMER_CONDICION_PROSPECT)){
                if (contador>0){ //Si creo por lo menos un contrato actualiza de Prospect a Customer
                  if(logger.isDebugEnabled())logger.debug("Se invoca al API que actualiza el estado del cliente: Prospect a Customer");													                                          
                   String strMessageEx = objOrderService.updSinchronizeActiv(MiUtil.parseLong(strOrderId),objRetailForm.getHdnLogin());
                   if (strMessageEx!=null){
                      if(logger.isDebugEnabled()){
                        logger.debug("Hubo errores en la ejecución del updSinchronizeActiv");
                        logger.debug("strMessageEx --> "+strMessageEx);
                      }	
                      hshDataMap.put("strMessageEx", strMessageEx);                  
                  }                     
                }
                else{
                  if(logger.isDebugEnabled())logger.debug("No se invoco el API que actualiza el estado del cliente: Prospect a Customer");
                }
            //}
            String strMessageInsNotAction = objOrderService.insNotificationAction(MiUtil.parseLong(strOrderId),Constante.ORDER_STATUS_CERRADO,objRetailForm.getHdnLogin());
            if(strMessageInsNotAction!=null){
               if(logger.isDebugEnabled()){
                 logger.debug("Hubo errores en la ejecución del insNotificationAction");
                 logger.debug("strMessageInsNotAction --> "+strMessageInsNotAction);
               }	
               hshDataMap.put("strMessageinsNotificationAction", strMessageInsNotAction);           
            }             
					}	
					else{											
						strMsgInfoCust="[RetailServlet][getInfoCustomer][" + strMsgInfoCust+"]";  		
						if(logger.isDebugEnabled())logger.debug("strMessagegetInfoCustomer --> "+strMsgInfoCust);
            hshDataMap.put("strMessagegetInfoCustomer", strMsgInfoCust);
					}																	
				} // fin //Se creo correctamente el cliente en BSCS se puede iniciar creación de contratos
			}
      } catch (Exception e) {
          if (e instanceof ServletException) {
              ServletException se = (ServletException) e;
              logger.error(formatException((Exception) se.getRootCause()));
          }
          logger.error(formatException(e));
      }
      request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
      request.getRequestDispatcher("pages/loadInfoRetail.jsp").forward(request, response);		
    }   
    
    /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                IOException {
        try {
			PrintWriter out = response.getWriter();
            logger.debug("Antes de hacer el sendRedirect");
			String strUrl = "/portal/page/portal/orders/RETAIL_NEW";
			out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /***********************************************************************************************************************************
     * Motivo:  Método que se invoca al hacer click en el combo de Tienda.
     * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
     * <br>Fecha: 25/11/2010
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     ***********************************************************************************************************************************/
    public void loadKitInfoRetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strTiendaRetail = StringUtils.defaultString(request.getParameter("strTiendaRetail"));
        String strInfDetail = StringUtils.defaultString(request.getParameter("strInfDetail"));
        
        if (logger.isDebugEnabled()) {
            logger.debug("strTiendaRetail:::" + strTiendaRetail);
        }
        try {
            
            System.out.println("entra loadKitInfoRetail**************");
            System.out.println("strTiendaRetail:"+strTiendaRetail);
            System.out.println("strInfDetail:"+strInfDetail);
            HashMap hshDataMap = objGeneralService.getKitsList(strTiendaRetail);
            
           // HashMap hshDataMap = objGeneralService.getInfoImei(strImei);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled()) {
              logger.debug("hshDataMap: "+hshDataMap);
              logger.debug("Antes de hacer el getRequestDispatcher");
            }
            request.setAttribute("hshDataMapKit", hshDataMap);
            request.setAttribute("strInfDetail", strInfDetail);
            request.getRequestDispatcher("pages/loadRetailKitInfo.jsp").forward(request, response);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException) e; 
                logger.error(formatException((Exception) se.getRootCause()));
            }
            logger.error(formatException(e));
        }
    }

}
