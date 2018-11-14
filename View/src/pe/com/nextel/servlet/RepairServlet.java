package pe.com.nextel.servlet;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.service.DigitalDocumentService;
import pe.com.nextel.service.GeneralService;
import pe.com.nextel.service.RepairService;
import pe.com.nextel.service.SessionService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class RepairServlet extends GenericServlet {
    private DigitalDocumentService digitalDocumentService=new DigitalDocumentService();
    public void loadImeiInfo(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strTxtImeiLista = StringUtils.defaultString(request.getParameter(
                    "txtImeiLista"), "");
        String strTechnology = StringUtils.defaultString(request.getParameter(
                    "technology"), "");
        String strFlagTab = ((request.getParameter("flagTab") == null) ? "0"
                                                                       : request.getParameter(
                "flagTab"));
        String strReplaceType = ((request.getParameter("flagTab") == null)
            ? "0" : request.getParameter("flagTab"));
        String strIndex = StringUtils.defaultString(request.getParameter(
                    "index"), "");
        System.out.println("strTechnology orden: " + strTechnology);

        String strTechnologyItem = "";

        if (logger.isDebugEnabled()) {
            logger.debug("strTxtImeiLista:::" + strTxtImeiLista);
            logger.debug("strIndex:::" + strIndex);
        }

        try {
            RepairBean objRepairBean = new RepairBean();
            HashMap hshImeiDetailMap = null;
            objRepairBean.setNpimei(strTxtImeiLista);

            if (strFlagTab.equals("0")) {
                hshImeiDetailMap = objGeneralService.getImeiDetail(objRepairBean);

                if (logger.isDebugEnabled()) {
                    logger.debug("IMEI Detail::: " + hshImeiDetailMap);
                }
            } else {
                strTechnologyItem = objRepairService.getTechnologyByImei(objRepairBean.getNpimei());
                hshImeiDetailMap = objGeneralService.getImeiDetailTab(objRepairBean);

                if (logger.isDebugEnabled()) {
                    logger.debug("IMEI Detail::: " + hshImeiDetailMap);
                }
            }

            request.setAttribute("strTechnology", strTechnology);
            request.setAttribute("strTechnologyItem", strTechnologyItem);
            request.setAttribute("hshImeiDetailMap", hshImeiDetailMap);
            request.setAttribute("index", strIndex);
            request.getRequestDispatcher("pages/loadImeiRepairInfo.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException) e;
                logger.error(formatException((Exception) se.getRootCause()));
            }

            logger.error(formatException(e));
        }
    }

    public void generateDocument(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("--Inicio generateDocument--");
        }

        try {
            String hdnOrderId = request.getParameter("hdnOrderId");
            String hdnRepairId = request.getParameter("hdnRepairId");
            String hdnReplaceType = request.getParameter("hdnReplaceType");
            String hdnFlagGenerate = StringUtils.defaultString(request.getParameter("hdnFlagGenerate"), "N");

            String hdnLogin = request.getParameter("hdnLogin");
            String flagCount = null;

            String strStatus = request.getParameter("cmbEstadoReparacion");
            String strProceso = request.getParameter("cmbProceso");

            System.out.println("strProceso: " + strProceso);
            System.out.println("cmbEstadoReparacion: " + strStatus);
            System.out.println("hdnReplaceType: " + hdnReplaceType);

            String strEquipment = request.getParameter("txtAlquilado");

            System.out.println("strEquipment: " + strEquipment);

            String strStockMessage = null;
            String strFlagStock = null;


            if (strEquipment != null) {
                if (strEquipment.equals("Alquiler")) {
                    strEquipment = Constante.TIPO_ALQUILER;
                } else {
                    strEquipment = Constante.TIPO_CLIENTE;
                }
            }

            System.out.println("New strEquipment: " + strEquipment);
            
            RepairBean objRepairBean = new RepairBean();
            objRepairBean.setNpequipment(strEquipment);
            objRepairBean.setNpstatus(strStatus);

            String hdnCrearDocListaAux = request.getParameter("hdnCrearDocListaAux");

            System.out.println("hdnCrearDocListaAux: " + hdnCrearDocListaAux);

            String[] strGenerateDocumento = null;
            strGenerateDocumento = hdnCrearDocListaAux.split("-");
            //I - borrar
            System.out.println("strGenerateDocumento.length = " + strGenerateDocumento.length);
            for(int i=0; i<strGenerateDocumento.length;i++){
                System.out.println("strGenerateDocumento["+i+"] = " + strGenerateDocumento[i]);    
            }
            //F - Fin
            
            logger.debug("Mira: " + ArrayUtils.toString(strGenerateDocumento));

            String[] hdnRepListId = request.getParameterValues("hdnRepListId");
            String[] chkIndiceLista = request.getParameterValues("chkIndiceLista");

            String[] txtImeiLista = request.getParameterValues("txtImeiLista");
            String[] txtSimLista = request.getParameterValues("txtSimLista");
            String[] txtSerieLista = request.getParameterValues("txtSerieLista");
            String[] cmbTipoImeiLista = request.getParameterValues("cmbTipoImeiLista");


            // MMONTOYA - Despacho en tienda
            String[] cmbModeloLista = request.getParameterValues("cmbModeloLista");
            String[] cmbTipoLista = request.getParameterValues("cmbTipoLista");
            String[] cmbFlagAccesorioLista = request.getParameterValues("cmbFlagAccesorioLista");
            String[] hdnTipoDocumentoAux = request.getParameterValues("hdnTipoDocumentoAux");
            
            String hdnDevolverEquipoListaAux = request.getParameter("hdnDevolverEquipoListaAux");
            System.out.println("hdnDevolverEquipoListaAux = " + hdnDevolverEquipoListaAux);
            
            String[]  chkDevolverEquipoLista = null;
            chkDevolverEquipoLista = hdnDevolverEquipoListaAux.split("-");  
            System.out.println("chkDevolverEquipoLista = " + chkDevolverEquipoLista);
            System.out.println("chkDevolverEquipoLista.length = "+chkDevolverEquipoLista.length);
                        
            for(int i=0; i<chkDevolverEquipoLista.length;i++){
                System.out.println("chkDevolverEquipoLista["+i+ "] = " + chkDevolverEquipoLista[i]);
            }
                        
            System.out.println("hdnDevolverEquipoListaAuxhdnDevolverEquipoListaAuxhdnDevolverEquipoListaAux: " + hdnDevolverEquipoListaAux);
            
            

            String[] chkCrearDocLista = strGenerateDocumento;
            
            System.out.println("Mostrando la Reparación Id: " + hdnRepairId +
                "\t - Usuario: " + hdnLogin + "\t - FlagGenerate: " +
                hdnFlagGenerate);
                
            logger.debug("Mostrando la Reparación Id: " + hdnRepairId +
                "\t - Usuario: " + hdnLogin + "\t - FlagGenerate: " +
                hdnFlagGenerate);

            int i = 0;
            logger.debug("hdnRepListId: " + (++i) + ").- " +
                ArrayUtils.toString(hdnRepListId));
            logger.debug("txtImeiLista: " + (++i) + ").- " +
                ArrayUtils.toString(txtImeiLista));
            logger.debug("txtSimLista: " + (++i) + ").- " +
                ArrayUtils.toString(txtSimLista));
            logger.debug("txtSerieLista: " + (++i) + ").- " +
                ArrayUtils.toString(txtSerieLista));
            logger.debug("cmbTipoImeiLista: " + (++i) + ").- " +
                ArrayUtils.toString(cmbTipoImeiLista));
            logger.debug("chkCrearDocLista: " + (++i) + ").- " +
                ArrayUtils.toString(chkCrearDocLista));

            if (chkCrearDocLista != null) {
                HashMap hshParametrosMap = new HashMap();
                hshParametrosMap.put("hdnOrderId", hdnOrderId);
                hshParametrosMap.put("hdnRepairId", hdnRepairId);
                hshParametrosMap.put("hdnReplaceType", hdnReplaceType);
                hshParametrosMap.put("hdnLogin", hdnLogin);
                hshParametrosMap.put("hdnFlagGenerate", hdnFlagGenerate);
                hshParametrosMap.put("hdnRepListId", hdnRepListId);
                hshParametrosMap.put("txtImeiLista", txtImeiLista);
                hshParametrosMap.put("txtSimLista", txtSimLista);
                hshParametrosMap.put("txtSerieLista", txtSerieLista);
                hshParametrosMap.put("cmbTipoImeiLista", cmbTipoImeiLista);
                hshParametrosMap.put("chkCrearDocLista", chkCrearDocLista);
                hshParametrosMap.put("hdnTipoDocumentoAux", hdnTipoDocumentoAux);
                hshParametrosMap.put("strProceso", strProceso);
                hshParametrosMap.put("hdnCrearDocListaAux", hdnCrearDocListaAux);
                hshParametrosMap.put("chkDevolverEquipoLista", chkDevolverEquipoLista);
                hshParametrosMap.put("hdnDevolverEquipoListaAux", hdnDevolverEquipoListaAux);
                                
                // MMONTOYA - Despacho en tienda
                hshParametrosMap.put("cmbModeloLista", cmbModeloLista);
                hshParametrosMap.put("cmbTipoLista", cmbTipoLista);
                hshParametrosMap.put("cmbFlagAccesorioLista", cmbFlagAccesorioLista);

                                      
                String sResultado = objRepairService.valImeiPrestamoCambio(objRepairBean, hshParametrosMap);


                System.out.println("sResultado:" + sResultado);


                // PCASTILLO - Despacho en Tienda - Validación de Stock
                HashMap hshResultadoStock = new HashMap();
                PrintWriter out = response.getWriter();
                hshResultadoStock = objRepairService.valStockPrestCambio(objRepairBean,hshParametrosMap);
                strStockMessage = (String)hshResultadoStock.get("strStockMessage");
                System.out.println("strStockMessage:" + strStockMessage);
                strFlagStock = (String)hshResultadoStock.get("strFlagStock");
                System.out.println("strFlagStock:" + strFlagStock);
                String strMessage = (String)hshResultadoStock.get("strMessage");
                System.out.println("strMessage:" + strMessage);
                
                
                if (StringUtils.isBlank(sResultado)) {

                    if (StringUtils.isBlank(strMessage)){
                          System.out.println("Si no hay error en la validacion de Stock Lista Prestamos y Cambios");
                          System.out.println("strFlagStock: "+strFlagStock);
                          System.out.println("hdnOrderId: "+hdnOrderId);
                          
                           HashMap hshDataMapStock = new HashMap();
                           //HashMap hshDataMap = new HashMap();
                           hshDataMapStock.put("strFlagStock", strFlagStock);
                           hshDataMapStock.put("strStockMessage", strStockMessage);
                           //hshDataMap.put("hdnOrderId", hdnOrderId);
                           //hshDataMap.put("flagCount", "-1");
                           //request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
                           request.setAttribute("flagCount", "-1");
                           //request.setAttribute("hdnOrderId", hdnOrderId);
                           //request.setAttribute("strFlagStock", strFlagStock);
                           //request.setAttribute("strStockMessage", strStockMessage);
                           request.setAttribute("strMessage", "");
                           request.setAttribute("hshDataMapStock", hshDataMapStock);
                           request.setAttribute("hshParametrosMap", hshParametrosMap);
                           request.setAttribute("flagOrigen", "0");

                                                                                 
                           request.getRequestDispatcher("/pages/manageListaPrestamosCambios.jsp").forward(request,response);



                         
                    }else{
                          System.out.println("Si hay error en la validacion de Stock Lista Prestamos y Cambios");
                          out.println("<script>");
                          out.println("alert('"+strMessage+"')");
                          out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");  
                          out.println("parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;");
                          out.println("</script>");



                    }





                } else {
                    System.out.println("No invoca el generar documento..");

                    HashMap hshDataMap = new HashMap();
                    hshDataMap.put("flagCount", "2");
                    request.setAttribute("flagCount", "2");
                    request.setAttribute("hdnOrderId", hdnOrderId);
                    request.setAttribute("strMessage", strMessage);

                    hshDataMap.put("strMessage", sResultado);
                    request.setAttribute("strFlagStock", strFlagStock);
                    request.setAttribute("flagOrigen", "0");
                    request.setAttribute("strStockMessage", strStockMessage);
                    request.setAttribute("strMessage", "sResultado");       

                    request.setAttribute(Constante.DATA_STRUCT, hshDataMap);

                    request.getRequestDispatcher("pages/manageListaPrestamosCambios.jsp").forward(request, response);

                }
            } else {
                HashMap hshDataMap = new HashMap();
                hshDataMap.put("flagCount", "0");
                hshDataMap.put("hdnOrderId", hdnOrderId);
                request.setAttribute("hdnOrderId", hdnOrderId);
                request.setAttribute("flagCount", "0");
                request.setAttribute("flagOrigen", "0");
                request.setAttribute("strFlagStock", strFlagStock);
                request.setAttribute("strStockMessage", strStockMessage);
                request.setAttribute("strMessage", "");
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);

                request.getRequestDispatcher("pages/manageListaPrestamosCambios.jsp").forward(request, response);

            }
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    public void generateDocumentRepair(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        try {
            String hdnOrderId = request.getParameter("hdnOrderId");
            String hdnLogin = request.getParameter("hdnLogin");
            String flagCount = null;
            PrintWriter out = response.getWriter();
            HashMap hshParametrosMap = new HashMap();
            hshParametrosMap.put("hdnOrderId", hdnOrderId);
            hshParametrosMap.put("hdnLogin", hdnLogin);

            HashMap hshDataMap = objRepairService.generateDocumentRepair(hshParametrosMap);
            String strMessage = (String) hshDataMap.get("strMessage");
            int iErrorNumber = MiUtil.parseInt((String) hshDataMap.get("iError"));

            if (logger.isDebugEnabled()) {
                logger.debug("hdnOrderId: " + hdnOrderId);
                logger.debug("strMessage: " + strMessage);
                logger.debug("iErrorNumber: " + iErrorNumber);
            }

            if (StringUtils.isBlank(strMessage)) {
                out.println("<script>");
                out.println(
                    "alert(\"Se generaron satisfactoriamente los documentos\");");
                out.println(
                    "parent.mainFrame.document.frmdatos.btnCreateDocRep.disabled=false;");
                out.println("</script>");
                out.close();
            } else {
                if (iErrorNumber < 0) {
                    out.println("<script>");
                    out.println(
                        " alert('Hubo un error al generar los documentos: " +
                        MiUtil.getMessageClean(strMessage) + "');");
                    out.println(
                        "parent.mainFrame.document.frmdatos.btnCreateDocRep.disabled=false;");
                    out.println("</script>");
                    out.close();
                } else {
                    if (strMessage != null) { // con mensaje 

                        if (iErrorNumber == 2) {
                            out.println("<script>");
                            out.println(" alert('" +
                                MiUtil.getMessageClean(strMessage) + "');");

                            String strUrl =
                                "/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid=" +
                                hdnOrderId + "&av_execframe=BOTTOMFRAME";
                            out.println("location.replace('" + strUrl + "');");
                            out.println("</script>");
                            out.close();
                        } else {
                            out.println("<script>");
                            out.println(" alert('" +
                                MiUtil.getMessageClean(strMessage) + "');");
                            out.println(
                                "parent.mainFrame.document.frmdatos.btnCreateDocRep.disabled=false;");
                            out.println("</script>");
                            out.close();
                        }
                    }
                }
            }

            //request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            //request.getRequestDispatcher("pages/manageListaPrestamosCambios.jsp").forward(request, response);      
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    public void generateOrderInterna(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("--Inicio--");
        }

        GeneralService objGeneralService = new GeneralService();
        RepairBean objRepairBean = new RepairBean();
        OrderBean objOrderBean = new OrderBean();
        String strOrderInterna = null;
        int iErrorNumber = 1;

        long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));
        long lReparacionId = MiUtil.parseLong(request.getParameter(
                    "hdnRepairId"));
        String strUserId = ((request.getParameter("hdnSessionId") == null)
            ? "0" : request.getParameter("hdnSessionId"));

        objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(strUserId);

        String strLogin = objPortalSesBean.getLogin();

        /*String strPhone = request.getParameter("txtNextel");
        String strImei = request.getParameter("txtImei");
        String strModel = request.getParameter("cmbModelo");
        String strImeisn = request.getParameter("txtSerie");
        String strRepairType = request.getParameter("cmbProceso");
        long lFailureId = MiUtil.parseLong(request.getParameter("cmbTipoFalla"));
        long lResolutionId = MiUtil.parseLong(request.getParameter("cmbCodigoResolucion"));
        String strServiceType = request.getParameter("cmbTipoServicio");
        String strEndSituation = request.getParameter("cmbSituacion");
        String strAccessoryType = request.getParameter("cmbTipoAccesorio");
        String strAccessoryModel = request.getParameter("cmbModeloAccesorio");
        String strFrequency = request.getParameter("txtSemFabricacion");
        String strDescriptionNextel = request.getParameter("txtNextelDiagnostico");
        String strDescriptionFactory = request.getParameter("txtNextelDiagnostico"); ////
        String strWarrantNextel =request.getParameter("hndOrderId");
        String strWarrantFactory = request.getParameter("hndOrderId");
        String strContactFirstName = request.getParameter("txtContactoNombre");
        String strContactLastName = request.getParameter("txtContactoApellido");
        String strStatus = request.getParameter("cmbEstadoReparacion");
        String strCollectContact = request.getParameter("txtContactoRecojo");
        String strCollectAddress = request.getParameter("txtDireccionRecojo");
        long lCodeOsiptel = MiUtil.parseLong(request.getParameter("txtCodigoOsiptel"));
        String strSim = request.getParameter("txtCodigoOsiptel");
        String strReception = request.getParameter("cmbRecepcion");   */
        objOrderBean.setNpOrderId(lOrderId);
        objRepairBean.setNprepairid(lReparacionId);
        objRepairBean.setOrderBean(objOrderBean);

        /*objRepairBean.setNpphone(strPhone);
        objRepairBean.setNpimei(strImei);
        objRepairBean.setNpmodel(strModel);
        objRepairBean.setNpimeisn(strImeisn);
        objRepairBean.setNprepairtype(strRepairType);
        objRepairBean.setNpfailureid(lFailureId);
        objRepairBean.setNpresolutionid(lResolutionId);
        objRepairBean.setNpservicetype(strServiceType);
        objRepairBean.setNpendsituation(strEndSituation);
        objRepairBean.setNpaccessorytype(strAccessoryType);
        objRepairBean.setNpaccessorymodel(strAccessoryModel);
        objRepairBean.setNpfrequency(strFrequency);
        objRepairBean.setNpdescriptionnextel(strDescriptionNextel);
        objRepairBean.setNpdescriptionfactory(strDescriptionFactory);
        objRepairBean.setNpwarrantnextel(strWarrantNextel);
        objRepairBean.setNpwarrantfactory(strWarrantFactory);
        objRepairBean.setNpcontactfirstname(strContactFirstName);
        objRepairBean.setNpcontactlastname(strContactLastName);
        objRepairBean.setNpstatus(strStatus);
        objRepairBean.setNpcollectcontact(strCollectContact);
        objRepairBean.setNpcollectaddress(strCollectAddress);
        objRepairBean.setNpcodeosiptel(lCodeOsiptel);
        objRepairBean.setNpsim(strSim);
        objRepairBean.setNpreception(strReception); */
        System.out.println(
            "------------Inicio RepairServlet.java / generateOrderInterna--------------------");
        System.out.println("lOrderId-->" + lOrderId);

        /*System.out.println("strPhone-->"+strPhone);
        System.out.println("strImei-->"+strImei);
        System.out.println("strModel-->"+strModel);
        System.out.println("strImeisn-->"+strImeisn);
        System.out.println("strRepairType-->"+strRepairType);
        System.out.println("lFailureId-->"+lFailureId);
        System.out.println("lResolutionId-->"+lResolutionId);
        System.out.println("strServiceType-->"+strServiceType);
        System.out.println("strEndSituation-->"+strEndSituation);
        System.out.println("strAccessoryType-->"+strAccessoryType);
        System.out.println("strAccessoryModel-->"+strAccessoryModel);
        System.out.println("strFrequency-->"+strFrequency);
        System.out.println("strDescriptionNextel-->"+strDescriptionNextel);
        System.out.println("strWarrantFactory-->"+strWarrantFactory);
        System.out.println("strContactFirstName-->"+strContactFirstName);
        System.out.println("strContactLastName-->"+strContactLastName);
        System.out.println("strStatus-->"+strStatus);
        System.out.println("strCollectContact-->"+strCollectContact);
        System.out.println("strCollectAddress-->"+strCollectAddress);
        System.out.println("lCodeOsiptel-->"+lCodeOsiptel);
        System.out.println("strSim-->"+strSim);
        System.out.println("strReception-->"+strReception);*/
        System.out.println("strLogin-->" + strLogin);
        System.out.println("strOrderInterna-->" + strOrderInterna);
        System.out.println("lReparacionId-->" + lReparacionId);
        System.out.println(
            "------------Fin RepairServlet.java / generateOrderInterna-----------------------");

        HashMap hshResult = objGeneralService.generateOrderInterna(objRepairBean,
                strLogin);
        String strMessage = (String) hshResult.get("strMessage");

        if (strMessage != null) {
            System.out.println("ERROR al generateOrderInterna ");
        }

        strOrderInterna = (String) hshResult.get("strRepairId");
        System.out.println("EXITO strRepairId--> " +
            (String) hshResult.get("strRepairId"));

        hshResult.put("strMessage", strMessage);
        hshResult.put("strErrorNumer", iErrorNumber + "");
        hshResult.put("strOrderId", strOrderInterna);
        hshResult.put("strTipoOrigen", "Repair");
        hshResult.put("strTipoOrden", Constante.TYPE_ORDEN_INTERNA);
        hshResult.put("lngOrdenId", lOrderId + "");
        request.setAttribute("objResultado", hshResult);

        RequestDispatcher rd = request.getRequestDispatcher(
                "PAGEEDIT/ResultEdit.jsp");
        rd.forward(request, response);
    }

    public void generateOrderExterna(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("--Inicio--");
        }

        GeneralService objGeneralService = new GeneralService();
        RepairBean objRepairBean = new RepairBean();
        OrderBean objOrderBean = new OrderBean();
        String strOrderInterna = null;
        int iErrorNumber = 1;

        String strUserId = ((request.getParameter("hdnSessionId") == null)
            ? "0" : request.getParameter("hdnSessionId"));

        objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(strUserId);

        String strLogin = objPortalSesBean.getLogin();

        long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));
        long lReparacionId = MiUtil.parseLong(request.getParameter(
                    "hdnRepairId"));

        /*String strPhone = request.getParameter("txtNextel");
        String strImei = request.getParameter("txtImei");
        String strModel = request.getParameter("cmbModelo");
        String strImeisn = request.getParameter("txtSerie");
        String strRepairType = request.getParameter("cmbProceso");
        long lFailureId = MiUtil.parseLong(request.getParameter("cmbTipoFalla"));
        long lResolutionId = MiUtil.parseLong(request.getParameter("cmbCodigoResolucion"));
        String strServiceType = request.getParameter("cmbTipoServicio");
        String strEndSituation = request.getParameter("cmbSituacion");
        String strAccessoryType = request.getParameter("cmbTipoAccesorio");
        String strAccessoryModel = request.getParameter("cmbModeloAccesorio");
        String strFrequency = request.getParameter("txtSemFabricacion");
        String strDescriptionNextel = request.getParameter("txtNextelDiagnostico");
        String strStatus = request.getParameter("cmbEstadoReparacion");    */
        objOrderBean.setNpOrderId(lOrderId);
        objRepairBean.setNprepairid(lReparacionId);
        objRepairBean.setOrderBean(objOrderBean);

        /*objRepairBean.setNpphone(strPhone);
        objRepairBean.setNpimei(strImei);
        objRepairBean.setNpmodel(strModel);
        objRepairBean.setNpimeisn(strImeisn);
        objRepairBean.setNprepairtype(strRepairType);
        objRepairBean.setNpfailureid(lFailureId);
        objRepairBean.setNpresolutionid(lResolutionId);
        objRepairBean.setNpservicetype(strServiceType);
        objRepairBean.setNpendsituation(strEndSituation);
        objRepairBean.setNpaccessorytype(strAccessoryType);
        objRepairBean.setNpaccessorymodel(strAccessoryModel);
        objRepairBean.setNpfrequency(strFrequency);
        objRepairBean.setNpdescriptionnextel(strDescriptionNextel);
        objRepairBean.setNpstatus(strStatus);*/
        System.out.println(
            "------------Inicio RepairServlet.java / generateOrderExterna--------------------");
        System.out.println("lOrderId-->" + lOrderId);

        /*System.out.println("strPhone-->"+strPhone);
        System.out.println("strImei-->"+strImei);
        System.out.println("strModel-->"+strModel);
        System.out.println("strImeisn-->"+strImeisn);
        System.out.println("strRepairType-->"+strRepairType);
        System.out.println("lFailureId-->"+lFailureId);
        System.out.println("lResolutionId-->"+lResolutionId);
        System.out.println("strServiceType-->"+strServiceType);
        System.out.println("strEndSituation-->"+strEndSituation);
        System.out.println("strAccessoryType-->"+strAccessoryType);
        System.out.println("strAccessoryModel-->"+strAccessoryModel);
        System.out.println("strFrequency-->"+strFrequency);
        System.out.println("strDescriptionNextel-->"+strDescriptionNextel);
        System.out.println("strStatus-->"+strStatus);  */
        System.out.println("strLogin-->" + strLogin);
        System.out.println("lReparacionId-->" + lReparacionId);
        System.out.println(
            "------------Fin RepairServlet.java / generateOrderExterna-----------------------");

        HashMap hshResult = objGeneralService.generateOrderExterna(objRepairBean,
                strLogin);
        String strMessage = (String) hshResult.get("strMessage");

        if (strMessage != null) {
            System.out.println("ERROR al generateOrderExterna ");
        }

        strOrderInterna = (String) hshResult.get("strRepairId");
        System.out.println("EXITO strRepairId--> " +
            (String) hshResult.get("strRepairId"));

        /* try {
             if (logger.isDebugEnabled())
                 logger.debug("Antes de hacer el getRequestDispatcher");
             request.getRequestDispatcher("pages/manageListaPrestamosCambios.jsp").forward(request, response);
         } catch (Exception e) {
             logger.error(formatException(e));
         }*/
        hshResult.put("strMessage", strMessage);
        hshResult.put("strErrorNumer", iErrorNumber + "");
        hshResult.put("strOrderId", strOrderInterna);
        hshResult.put("strTipoOrden", Constante.TYPE_ORDEN_EXTERNA);
        hshResult.put("strTipoOrigen", "Repair");
        hshResult.put("lngOrdenId", lOrderId + "");
        request.setAttribute("objResultado", hshResult);

        RequestDispatcher rd = request.getRequestDispatcher(
                "PAGEEDIT/ResultEdit.jsp");
        rd.forward(request, response);
    }

    public void loadCombo(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("--Inicio--");
        }

        try {
            GeneralService objGeneralService = new GeneralService();
            RepairBean objRepairBean = new RepairBean();
            OrderBean objOrderBean = new OrderBean();
            HashMap hshComboMap = null;
            String strMessage = null;
            ArrayList arrComboList = null;

            String strNameField = ((request.getParameter("hdnNameField") == null)
                ? "" : request.getParameter("hdnNameField"));
            long lValue = ((request.getParameter("hdnObjectAllId") == null) ? 0
                                                                            : MiUtil.parseLong(request.getParameter(
                        "hdnObjectAllId")));

            hshComboMap = objGeneralService.getGeneralOptionList(strNameField,
                    lValue);
            strMessage = (String) hshComboMap.get(Constante.MESSAGE_OUTPUT);

            if (strMessage != null) {
                throw new Exception(strMessage);
            }

            arrComboList = (ArrayList) hshComboMap.get("arrDominioLista");

            request.setAttribute("objArreglo", arrComboList);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "DYNAMICSECTION/DynamicSectionRepair/RepairPages/PrintCombosRepair.jsp?strMensaje=" +
                    strMessage);
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Message-->" + e.getMessage());
        }
    }

    /**
    * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
    */
    public void executeDefault(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/ORDER_EDIT");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /*
      * Motivo:  Método que devuelve la lista de fallas para un tipo de falla
      * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
      * <br>Fecha: 11/03/2009
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      */
    /*public void getFailureList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                              IOException {

      int intFailureId = MiUtil.parseInt(StringUtils.defaultString(request.getParameter("intFailureId")));
      String strListName = StringUtils.defaultString(request.getParameter("strListName"));
      try{

        RepairService objRepairService=new RepairService();
        HashMap hshDataMap = objRepairService.getFailureList(intFailureId);
        request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
        request.setAttribute("objName",strListName);
        request.getRequestDispatcher("pages/loadFailureListByFailureType.jsp").forward(request, response);
      }catch(Exception e){
        logger.error(formatException(e));
      }
    }*/
    public void getFailureList(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        int intFailureId = MiUtil.parseInt(StringUtils.defaultString(
                    request.getParameter("intFailureId")));
        int intRepairId = MiUtil.parseInt(StringUtils.defaultString(
                    request.getParameter("intRepairId")));
        String strRepairTypeId = StringUtils.defaultString(request.getParameter(
                    "strRepairTypeId"));
        String strListFallasSeleccionadas = StringUtils.defaultString(request.getParameter(
                    "strListFallasSeleccionadas"));

        String strMessage = null;

        try {
            HashMap hshDataMap = objRepairService.getFailureList(intFailureId,
                    intRepairId, strRepairTypeId);
            strMessage = (String) hshDataMap.get("strMessage");

            if (!StringUtils.isNotBlank(strMessage)) {
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);

                HashMap hshDataMapAux = objRepairService.getSelectedFailureList(intRepairId,
                        strRepairTypeId);
                strMessage = (String) hshDataMapAux.get("strMessage");

                if (!StringUtils.isNotBlank(strMessage)) {
                    request.setAttribute("hshDataMapAux", hshDataMapAux);
                    request.setAttribute("strListFallasSeleccionadas",
                        strListFallasSeleccionadas);
                    request.getRequestDispatcher(
                        "pages/loadFailureListByFailureType.jsp").forward(request,
                        response);
                } else {
                    request.setAttribute("strMessage", strMessage);
                    request.getRequestDispatcher("pages/loadMessage.jsp")
                           .forward(request, response);
                }
            } else {
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                    response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /*
     * Motivo:  Método que devuelve la descripcion del diagnostico de una reparacion
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomas Mogrovejo</a>
     * <br>Fecha: 11/03/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
    */
    public void getDiagnosisDescription(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        //String strDiagnosisDescription = StringUtils.defaultString(request.getParameter("intDiagnosisId"));
        int intDiagnosisId = MiUtil.parseInt(StringUtils.defaultString(
                    request.getParameter("intDiagnosisId")));

        try {
            RepairService objRepairService = new RepairService();

            String strDiagnosisDescription = objRepairService.getDiagnosisDescription(intDiagnosisId);
            HashMap hshDataMap = new HashMap();
            hshDataMap.put("strDiagnosisDescription", strDiagnosisDescription);

            //request.setAttribute("strDiagnosisDescription",strDiagnosisDescription);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            request.getRequestDispatcher("pages/loadDiagnosisDescription.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /*
     * Motivo:  Método que devuelve la lista de repuestos para un modelo
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomas Mogrovejo</a>
     * <br>Fecha: 12/03/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getSparePartsListByModel(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        /*String strModelo = StringUtils.defaultString(request.getParameter("strModelo"));
        String strListName = StringUtils.defaultString(request.getParameter("strListName"));*/
        String strModelo = StringUtils.defaultString(request.getParameter(
                    "strModelo"));
        int intRepairId = MiUtil.parseInt((String) request.getParameter(
                    "intRepairID"));
        String strRepairTypeId = (String) request.getParameter(
                "strRepairTypeId");
        String strMessage = null;

        /*try{
          RepairService objRepairService=new RepairService();
          HashMap hshDataMap = objRepairService.getSparePartsListByModel(strModelo);
          request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
          request.setAttribute("objName",strListName);
          request.getRequestDispatcher("pages/loadSparePartsListByModel.jsp").forward(request, response);
        }catch(Exception e){
          logger.error(formatException(e));
        }
        }*/
        try {
            HashMap hshDataMap = objRepairService.getSparePartsListByModel(strModelo,
                    intRepairId, strRepairTypeId);
            strMessage = (String) hshDataMap.get("strMessage");

            if (StringUtils.isNotBlank(strMessage)) {
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                    response);
            } else {
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
                request.getRequestDispatcher(
                    "pages/loadSparePartsListByModel.jsp").forward(request,
                    response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /*
    * Motivo:  Método que se invoca al seleccionar los Combos de Proceso para un tipo de reparacion
    * <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Juan Oyola</a>
    * <br>Fecha: 04/03/2009
    * @param request
    * @param response
    * @throws ServletException
    * @throws IOException
    */
    public void loadServices(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strTipoReparacion = StringUtils.defaultString(request.getParameter(
                    "cmbTipoServicio"));
        String strMessage = null;

        try {
            HashMap hshDataMap = objRepairService.getloadServices(strTipoReparacion);
            strMessage = (String) hshDataMap.get("strMessage");

            if (!StringUtils.isNotBlank(strMessage)) {
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
                request.getRequestDispatcher("pages/loadComboServicio.jsp")
                       .forward(request, response);
            } else {
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                    response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /*
        * Motivo:  Método que inserta la Reparacion
        * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
        * <br>Fecha: 06/03/2009
        * @param request
        * @param response
        * @throws ServletException
        * @throws IOException
        */
    public void newOrderReparation(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strMessage = null;
        String strRepairId = null;
        String lOrderId = request.getParameter("hdnOrderId");
        String strRepairType = request.getParameter("hdnProcesoAux");

        String strImei = request.getParameter("txtImei");
        String strSim = request.getParameter("txtSim");
        String strSerie = request.getParameter("txtSerie");
        String strTipoAccesorio = request.getParameter("cmbTipoAccesorio");

        String strModeloAccesorio = request.getParameter("cmbModeloAccesorio");

        //String strModel              = request.getParameter("cmbModeloSOS");      
        String strModel = request.getParameter("hdnModelo");
        String lCodeOsiptel = request.getParameter("txtCodigoOsiptel");
        String strProveedor = request.getParameter("txtProveedor");
        String strProveedorId = request.getParameter("hdnProveedorId");
        String strEquipment = request.getParameter("txtAlquilado");
        String strFnc = request.getParameter("chkFnc");
        String strGarantiaFabrica = request.getParameter("hdnGarantiaFabrica");
        String strGarantiaExtend = request.getParameter("hdnGarantiaExt");
        String strGarantiaBounce = request.getParameter("hdnGarantiaBounce");
        String strGarantiaRefurbish = request.getParameter(
                "hdnGarantiaRefurbish");
        String lMesesAdional = request.getParameter("txtMesesAdicional");
        String strFecActiv = request.getParameter("txtFecActiv");
        String strFecOracle = request.getParameter("txtFechaEmision");
        String strResolucion = request.getParameter("txtResolucion");
        String strResolucionid = request.getParameter("cmbCodigoResolucion");
        String strItemid = request.getParameter("hdnItemid");
        String strNumTel = request.getParameter("txtNextel");
        String strLogin = request.getParameter("hdnLogin");
        String strTipoServicio = request.getParameter("cmbTipoServicio");
        String strSituacion = request.getParameter("cmbSituacion");
        String strCodEquipo = request.getParameter("txtCodEquipo");
        String strRecepcion = request.getParameter("cmbRecepcion");
        String strBuildingid = request.getParameter("hdnBuildingId");
        String strDiagnostico = request.getParameter("cmbDiagnostico");
        String strNextelDiagnostico = request.getParameter(
                "txtNextelDiagnostico");

        String strTimerValue = request.getParameter("txtTimerValue");
        String strSemFabricacion = request.getParameter("txtSemFabricacion");

        String strFechaVenta = request.getParameter("txtFechaVenta");
        String strTecnologia = request.getParameter("txtTecnologia");

        String strFabricanteId = request.getParameter("hdnFabricanteId");
        String strCentroReparacionId = request.getParameter(
                "hdnCentroReparacionId");

        String strGarantiaExtFabrica = request.getParameter(
                "hdnGarantiaExtFabrica");
        
        String strGarantiaReparacion = request.getParameter("hdnGarantiaReparacion");
        String strDAP = request.getParameter("hdnDAP");
        String strReporteCliente = request.getParameter("txtReporteCliente");

        String hdnFlgSDD = request.getParameter("hdnFlgSDD");

        HashMap hspMapParameters = new HashMap();
        HashMap hspMapResult = new HashMap();

        hspMapParameters.put("hdnOrderId", lOrderId);
        hspMapParameters.put("cmbProceso", strRepairType);
        hspMapParameters.put("txtImei", strImei);
        hspMapParameters.put("txtSim", strSim);
        hspMapParameters.put("txtSerie", strSerie);
        hspMapParameters.put("cmbTipoAccesorio", strTipoAccesorio);
        hspMapParameters.put("cmbModeloAccesorio", strModeloAccesorio);
        hspMapParameters.put("cmbModelo", strModel);
        hspMapParameters.put("txtCodigoOsiptel", lCodeOsiptel);
        hspMapParameters.put("txtProveedor", strProveedor);
        hspMapParameters.put("hdnProveedorId", strProveedorId);
        hspMapParameters.put("txtAlquilado", strEquipment);
        hspMapParameters.put("chkFnc", strFnc);
        hspMapParameters.put("hdnGarantiaFabrica", strGarantiaFabrica);
        hspMapParameters.put("hdnGarantiaExt", strGarantiaExtend);
        hspMapParameters.put("hdnGarantiaBounce", strGarantiaBounce);
        hspMapParameters.put("hdnGarantiaRefurbish", strGarantiaRefurbish);
        hspMapParameters.put("txtMesesAdicional", lMesesAdional);
        hspMapParameters.put("txtFecActiv", strFecActiv);
        hspMapParameters.put("txtFechaEmision", strFecOracle);
        hspMapParameters.put("txtResolucion", strGarantiaRefurbish);
        hspMapParameters.put("hdnItemid", strItemid);
        hspMapParameters.put("hdnLogin", strLogin);
        hspMapParameters.put("cmbCodigoResolucion", strResolucionid);
        hspMapParameters.put("txtNextel", strNumTel);
        hspMapParameters.put("cmbTipoServicio", strTipoServicio);
        hspMapParameters.put("cmbSituacion", strSituacion);
        hspMapParameters.put("txtCodEquipo", strCodEquipo);
        hspMapParameters.put("cmbRecepcion", strRecepcion);
        hspMapParameters.put("hdnBuildingId", strBuildingid);
        hspMapParameters.put("cmbDiagnostico", strDiagnostico);
        hspMapParameters.put("txtNextelDiagnostico", strNextelDiagnostico);

        hspMapParameters.put("txtTimerValue", strTimerValue);
        hspMapParameters.put("txtSemFabricacion", strSemFabricacion);
        hspMapParameters.put("txtFechaVenta", strFechaVenta);

        hspMapParameters.put("txtTecnologia", strTecnologia);

        hspMapParameters.put("hdnFabricanteId", strFabricanteId);
        hspMapParameters.put("hdnCentroReparacionId", strCentroReparacionId);
        hspMapParameters.put("hdnGarantiaExtFabrica", strGarantiaExtFabrica);
        
        hspMapParameters.put("hdnGarantiaReparacion", strGarantiaReparacion);
        hspMapParameters.put("hdnDAP", strDAP);
        hspMapParameters.put("txtReporteCliente", strReporteCliente);

        try {
            if("REP".equals(strRepairType) && "2".equals(hdnFlgSDD)){
                String hdnSubCategoria=request.getParameter("hdnSubCategoria");
                String hdnDivisionId=request.getParameter("hdnDivisionId");
                String hdnTransType=request.getParameter("hdnTransType");

            HashMap hashMap =digitalDocumentService.sendEmailST(lOrderId,strLogin,hdnSubCategoria,hdnDivisionId,hdnTransType);
            String messageValidation= (String) hashMap.get("messageValidation");
            if(StringUtils.isNotBlank(messageValidation)){
                hspMapResult.put("strMessage", "VALIDATE_GENERATION");
                hspMapResult.put("messageValidation", messageValidation);
                hspMapResult.put("strErrorNumer", 0 + "");
                hspMapResult.put("strOrderId", strRepairId);
                hspMapResult.put("strErrorNumer", 0 + "");
                hspMapResult.put("strTipoOrigen", "Repair");
                hspMapResult.put("strTipoOrden", Constante.TYPE_ORDEN_INTERNA);
                hspMapResult.put("lngOrdenId", lOrderId + "");
                request.setAttribute("objResultado", hspMapResult);

                RequestDispatcher rd = request.getRequestDispatcher(
                        "PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
                return;
            }}
            hspMapResult = objRepairService.newOrderReparation(hspMapParameters);
            strRepairId = (String) hspMapResult.get("intReparationId");
            strMessage = (String) hspMapResult.get("strMessage");

            if (StringUtils.isNotBlank(strMessage)) {
                hspMapResult.put("strMessage", strMessage);
                hspMapResult.put("strErrorNumer", 0 + "");
                hspMapResult.put("strOrderId", strRepairId);
                hspMapResult.put("strTipoOrigen", "Repair");
                hspMapResult.put("strTipoOrden", Constante.TYPE_ORDEN_INTERNA);
                hspMapResult.put("lngOrdenId", lOrderId + "");
                request.setAttribute("objResultado", hspMapResult);

                RequestDispatcher rd = request.getRequestDispatcher(
                        "PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
            } else {
                strMessage = "Se generó la Reparación N°." + strRepairId;
                request.setAttribute("strMessage", strMessage);

                hspMapResult.put("strMesgAsig", strMessage);
                hspMapResult.put("strErrorNumer", 0 + "");
                hspMapResult.put("strOrderId", strRepairId);
                hspMapResult.put("strTipoOrigen", "Repair");
                hspMapResult.put("strTipoOrden", Constante.TYPE_ORDEN_INTERNA);
                hspMapResult.put("lngOrdenId", lOrderId + "");
                request.setAttribute("objResultado", hspMapResult);

                RequestDispatcher rd = request.getRequestDispatcher(
                        "PAGEEDIT/ResultEdit.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(formatException(e));
            strMessage = e.getMessage();
            hspMapResult.put("strMesgAsig", strMessage);
            hspMapResult.put("strErrorNumer", 0 + "");
            hspMapResult.put("strOrderId", strRepairId);
            hspMapResult.put("strTipoOrigen", "Repair");
            hspMapResult.put("strTipoOrden", Constante.TYPE_ORDEN_INTERNA);
            hspMapResult.put("lngOrdenId", lOrderId + "");
            request.setAttribute("objResultado", hspMapResult);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "PAGEEDIT/ResultEdit.jsp");
            rd.forward(request, response);
        }
    }

    /**
       * Motivo:  Método que devuelve la lista de fallas para un tipo de falla
       * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomas Mogrovejo</a>
       * <br>Fecha: 23/03/2009
       * @param request
       * @param response
       * @throws ServletException
       * @throws IOException
       */
    public void getRepairListDetails(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        int intFailureId = MiUtil.parseInt(StringUtils.defaultString(
                    request.getParameter("intFailureId")));
        int intRepairId = MiUtil.parseInt(StringUtils.defaultString(
                    request.getParameter("intRepairId")));
        String strModelo = StringUtils.defaultString(request.getParameter(
                    "strModelo"));
        String strRepairTypeId = StringUtils.defaultString(request.getParameter(
                    "strRepairTypeId"));
        String strMessage = null;

        try {
            HashMap hshDataMap = objRepairService.getFailureList(intFailureId,
                    intRepairId, strRepairTypeId); //Carga Listas Fallas
            HashMap hshDataMapAux = objRepairService.getSelectedFailureList(intRepairId,
                    strRepairTypeId); //Carga Lista Fallas Seleccionadas
            HashMap hshDataMapRepuestos = objRepairService.getSparePartsListByModel(strModelo,
                    intRepairId, strRepairTypeId); //Carga Lista Repuestos
            HashMap hshDataMapRepuestosResultado = objRepairService.getSelectedSpareList(intRepairId,
                    strRepairTypeId); //Carga Lista Repuestos

            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            request.setAttribute("hshDataMapAux", hshDataMapAux);
            request.setAttribute("hshDataRepuestos", hshDataMapRepuestos);
            request.setAttribute("hshDataRepuestosResultado",
                hshDataMapRepuestosResultado);

            request.getRequestDispatcher("pages/loadRepairListDetails.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /*
        * Motivo:  Método que inserta la Reparacion
        * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
        * <br>Fecha: 02/04/2009
        * @param request
        * @param response
        * @throws ServletException
        * @throws IOException
        */
    public void validateTrueBounce(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strMessage = null;
        String strRepairId = null;
        String strValor = null;
        String strImei = request.getParameter("txtImei");
        String strGarantiaBounce = request.getParameter("bounce");
        long strFalla = MiUtil.parseLong(request.getParameter("falla"));

        HashMap hspMapParameters = new HashMap();
        HashMap hspMapResult = new HashMap();

        try {
            /*strValor*/ HashMap hshDataMap = objRepairService.validateTrueBounce(strImei,
                    strFalla, strGarantiaBounce);

            //hspMapResult.put(/*"strValor"*/"hshDataMap",hshDataMap/*strValor*/);      
            request.setAttribute("hshDataMap", hshDataMap);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "pages/loadValidateTrueBounce.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /**
     * Motivo:  Método que se carga los datos en base a un imei ingresado en la pag de Abrir Reparaciones
     * <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 04/04/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void SearchImei(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strIMEI = StringUtils.defaultString(request.getParameter("IMEI"));
        String strLogin = StringUtils.defaultString(request.getParameter(
                    "hdnLogin"));
        String strMessage = null;

        try {
            HashMap hshData = objRepairService.getRepairCount(strIMEI);
            int intNroReg = MiUtil.parseInt((String) hshData.get("intNroReg"));
            int intNpRepairId = MiUtil.parseInt((String) hshData.get(
                        "intNpRepairId"));

            if (intNroReg == 0) {
                HashMap hshDataMap = objRepairService.getSmallRepairDetail(strIMEI);
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);

                strMessage = (String) hshDataMap.get("strMessage");

                if (!StringUtils.isNotBlank(strMessage)) {
                    request.setAttribute("strIndicador", "1");
                    request.getRequestDispatcher(
                        "/pages/loadRepairDetailByImei.jsp").forward(request,
                        response);
                } else {
                    request.setAttribute("strMessage", strMessage);
                    request.getRequestDispatcher("pages/loadMessage.jsp")
                           .forward(request, response);
                }
            } else {
                request.setAttribute("strRepairId",
                    MiUtil.getString(intNpRepairId));
                request.setAttribute("strLogin", strLogin);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    public void Busqueda(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strIMEI = StringUtils.defaultString(request.getParameter(
                    "strRepairId"));
        String strLogin = StringUtils.defaultString(request.getParameter(
                    "hdnlogin"));
        HashMap hshDataAux = objRepairService.getNpRepairBOF(MiUtil.parseInt(
                    (strIMEI)), strLogin);
        String strMessage = (String) hshDataAux.get("strMessage");

        if (!StringUtils.isNotBlank(strMessage)) {
            request.setAttribute("strRepairId", strIMEI);
            request.setAttribute("strLogin", strLogin);

            //request.getRequestDispatcher("/htdocs/jp_order_repair/JP_REPAIR_NEW_ShowPage.jsp").forward(request, response); // esto se comenta cuando pasa al server      
            request.getRequestDispatcher(
                "/portal/page/portal/repair/REPAIR_NEW?strRepairId=" +
                strIMEI + "&strLogin=" + strLogin).forward(request, response);
        } else {
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    public void activateEquipment(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strImei = request.getParameter("txtImei");
        String strImeiNuevo = request.getParameter("imeiNuevo");
        String strSim = request.getParameter("txtSim");
        String strSimNuevo = request.getParameter("simNuevo");
        String strReplaceType = request.getParameter("replaceType");

        //System.out.println("Inicio RepairServlet.java...");
        System.out.println(
            "Ingreso de Parametros Cambio de modelo ST (Servlet)");
        System.out.println(
            "Imei Antiguo enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: " + strImei);
        System.out.println(
            "Imei Nuevo enviado a SP_GEN_ACTIVATE_EQUIPMENT ST: " +
            strImeiNuevo);
        System.out.println("strReplaceType activateEquipment: " +
            strReplaceType);

        PrintWriter out = response.getWriter();

        HashMap hshResult = objRepairService.activateEquipment(strImei,
                strImeiNuevo, strSim, strSimNuevo, strReplaceType);
        String strMessage = (String) hshResult.get("strMessage");

        System.out.println("Mensaje Bpel, cambio modelo ST : " + strMessage);

        if (StringUtils.isNotBlank(strMessage)) { //en caso de error           
            request.setAttribute("strMessage", strMessage);
            System.out.println("Mensaje no nulo!!! : " + strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        } else {
            System.out.println("Mensaje nulo!!! : " + strMessage);
            hshResult = objRepairService.transferExtendedGuarantee(strImei,
                    strImeiNuevo);
            strMessage = (String) hshResult.get("strMessage");
            System.out.println(
                "Mensaje de Transferir la GE al equipo nuevo!!! : " +
                strMessage);
            request.setAttribute("objResultado", hshResult);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "pages/loadActivationEquipment.jsp");
            rd.forward(request, response);
        }
    }

    /**
    * Motivo:  Método que genera Guia de Remisión.
    * <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Juan Oyola</a>
    * <br>Fecha: 15/03/2009
    * @param request
    * @param response
    * @throws ServletException
    * @throws IOException
    */
    public void GenerateInvDoc(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        HashMap hshResult = new HashMap();
        HashMap hshParameters = new HashMap();
        String strMessage = null;

        int iRepairId = MiUtil.parseInt(request.getParameter("txtNroReparacion"));
        String strRepairTypeId = (String) request.getParameter(
                "strRepairTypeId");
        String strLogin = (String) request.getParameter("hdnLogin");
        int iDocumentParentType = MiUtil.parseInt((String) request.getParameter(
                    "hdnDocumentParentType"));
        int iTransferReason = MiUtil.parseInt((String) request.getParameter(
                    "hdnTransferReason"));
        String strItemTypeId = (String) request.getParameter("hdnItemTypeId");
        int iOrganizationId = MiUtil.parseInt((String) request.getParameter(
                    "hdnOrganizationId"));
        String strSubInventoryCode = (String) request.getParameter(
                "hdnSubInventoryCode");

        hshParameters.put("an_repairid", MiUtil.getString(iRepairId));
        hshParameters.put("strRepairTypeId", strRepairTypeId);
        hshParameters.put("strLogin", strLogin);
        hshParameters.put("iDocumentParentType",
            MiUtil.getString(iDocumentParentType));
        hshParameters.put("iTransferReason", MiUtil.getString(iTransferReason));
        hshParameters.put("strItemTypeId", strItemTypeId);
        hshParameters.put("iOrganizationId", MiUtil.getString(iOrganizationId));
        hshParameters.put("strSubInventoryCode", strSubInventoryCode);

        try {
            hshResult = objRepairService.GenerateInvDoc(hshParameters);
            strMessage = (String) hshResult.get("strMessage");

            if (!StringUtils.isNotBlank(strMessage)) {
                int iTransactionId = MiUtil.parseInt(((String) hshResult.get(
                            "iTransactionId")));
                String strTransNumber = (String) hshResult.get("strTransNumber");
                strMessage = "La operación se ejecutó con exito, N° Transacción generada: " +
                    iTransactionId + ", N° Guía Remisión generada: " +
                    strTransNumber;
            }
        } catch (Exception e) {
            strMessage = e.getMessage();
            logger.error(formatException(e));
        }

        request.setAttribute("strMessage", strMessage);
        request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
            response);
    }

    /*
     * Motivo:  Método que devuelve la lista de modelos de accesorio dependiento del tipo de accesorio elegido
     * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
     * <br>Fecha: 12/03/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getComboAccessoryModels(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strAccesoryType = StringUtils.defaultString(request.getParameter(
                    "cmbTipoAccesorio"));
        String strMessage = null;

        try {
            HashMap hshDataMap = objRepairService.getAccesoryModels(strAccesoryType);
            strMessage = (String) hshDataMap.get("strMessage");

            if (StringUtils.isNotBlank(strMessage)) {
                request.setAttribute("strMessage", strMessage);
                request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                    response);
            } else {
                request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
                request.getRequestDispatcher(
                    "pages/loadComboAccesoryModels.jsp").forward(request,
                    response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    /*
        * Motivo:  Método que valida si existe un repuesto en stock
        * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
        * <br>Fecha: 16/06/2009
        * @param request
        * @param response
        * @throws ServletException
        * @throws IOException
        */
    public void validateStock(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        String strMessage = null;
        String strRepairId = null;
        String strValor = null;

        long strRepuesto = MiUtil.parseLong(request.getParameter("repuesto"));
        long strSpecification = MiUtil.parseLong(request.getParameter(
                    "specification"));
        String strLogin = request.getParameter("hdnlogin");

        HashMap hspMapParameters = new HashMap();
        HashMap hspMapResult = new HashMap();

        try {
            HashMap hshDataMap = objRepairService.validateStock(strRepuesto,
                    strSpecification, strLogin);
            request.setAttribute("hshDataMap", hshDataMap);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "pages/loadValidateStock.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            System.out.println("e :" + formatException(e));
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute("strMessage", strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        }
    }

    public void validateSim(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        try {
            String hdnOrderId = request.getParameter("hdnOrderId");
            String hdnLogin = request.getParameter("hdnLogin");
            String txtSimLista = request.getParameter("txtSimLista");
            String index = request.getParameter("index");

            HashMap hshParametrosMap = new HashMap();
            hshParametrosMap.put("hdnOrderId", hdnOrderId);
            hshParametrosMap.put("hdnLogin", hdnLogin);

            HashMap hshDataMap = objRepairService.validateSim(txtSimLista);
            String strStatusSim = (String) hshDataMap.get("wc_status_sim");
            String strMessage = (String) hshDataMap.get("wv_strMessage");
            System.out.println("strStatusSim : " + strStatusSim);
            System.out.println("strMessage : " + strMessage);

            if (StringUtils.isNotBlank(strMessage)) {
                request.setAttribute("strMessage", strMessage);
                request.setAttribute("index", index);
                request.setAttribute("strStatusSim", strStatusSim);

                //request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
                request.getRequestDispatcher("pages/loadSimRepairInfo.jsp")
                       .forward(request, response);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
       * Motivo:  Método que valida el proceso de reparaciones
       * <br>Realizado por: <a href="mailto:julio.herrera@hp.com">Julio Herrera</a>
       * <br>Fecha: 18/08/2010
       * @param request
       * @param response
       * @throws ServletException
       * @throws IOException
       */
    public void validateSpecProcess(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        HashMap hshResult = new HashMap();
        HashMap hshParameters = new HashMap();
        String strMessage = null;
        String strMessageOrders = null;

        String strPhoneNumber = StringUtils.defaultString(request.getParameter(
                    "strPhoneNumber"));
        String strProcess = StringUtils.defaultString(request.getParameter(
                    "strProcess"));
        String strOldProcess = StringUtils.defaultString(request.getParameter(
                    "strOldProcess"));
        String strOrderID = StringUtils.defaultString(request.getParameter(
                    "strOrderID"));

        System.out.println("");
        System.out.println(
            "[REPAIRSERVLET - validateSpecProcess - strPhoneNumber : " +
            strPhoneNumber + "]");
        System.out.println(
            "[REPAIRSERVLET - validateSpecProcess - strProcess : " +
            strProcess + "]");
        System.out.println(
            "[REPAIRSERVLET - validateSpecProcess - strOldProcess : " +
            strOldProcess + "]");
        System.out.println(
            "[REPAIRSERVLET - validateSpecProcess - strOrderID : " +
            strOrderID + "]");
        System.out.println("");

        hshParameters.put("strPhoneNumber", strPhoneNumber);
        hshParameters.put("strProcess", strProcess);
        hshParameters.put("strOldProcess", strOldProcess);
        hshParameters.put("strOrderID", strOrderID);

        try {
            hshResult = objRepairService.validateSpecOrders(hshParameters);
            strMessage = (String) hshResult.get("strMessage");
            strMessage = (String) hshResult.get("strMessageOrders");

            if (!StringUtils.isNotBlank(strMessage)) {
                if (!StringUtils.isNotBlank(strMessageOrders)) {
                    strMessage = strMessageOrders;
                }
            }
        } catch (Exception e) {
            strMessage = e.getMessage();
            logger.error(formatException(e));
        }

        request.setAttribute("strMessage", strMessage);
        request.setAttribute("strOldProcess", strOldProcess);
        request.getRequestDispatcher("pages/loadMessageProcess.jsp").forward(request,
            response);
    }

    public void generateNacionalizacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap hshResult = new HashMap();
        HashMap hshParameters = new HashMap();
        String strMessage = null;

        String strImei = StringUtils.defaultString(request.getParameter("strImei"));
        String strSim = StringUtils.defaultString(request.getParameter("strSim"));        
        String strModelId = StringUtils.defaultString(request.getParameter("strModelId"));
        String strModelName = StringUtils.defaultString(request.getParameter("strModelName"));
        String strNumSerie = StringUtils.defaultString(request.getParameter("strNumSerie"));

        System.out.println("");
        System.out.println("[REPAIRSERVLET - generateNacionalizacion - strImei : " + strImei + "]");
        System.out.println("[REPAIRSERVLET - generateNacionalizacion - strSim : " + strSim + "]");
        System.out.println("[REPAIRSERVLET - generateNacionalizacion - strModelId : " + strModelId + "]");
        System.out.println("[REPAIRSERVLET - generateNacionalizacion - strModelName : " + strModelName + "]");
        System.out.println("[REPAIRSERVLET - generateNacionalizacion - strNumSerie : " + strNumSerie + "]");
        System.out.println("");

        hshParameters.put("strImei", strImei);
        hshParameters.put("strSim", strSim);        
                
        try {          
          /*
           * Si el modelId es null se obtiene de la base de datos. 
           */
          
          if (strModelId.equals("null") || strModelId == null) {            
              hshResult = objRepairService.getBscsModelId(strModelName); 
              strMessage = (String) hshResult.get("strMessage");
              System.out.println("Luego de llamar a getBscsModelId: " + strMessage); 
              if (strMessage != null) {
                  throw new Exception(strMessage);
              }
              
              strModelId = hshResult.get("strModelId").toString();
              if (strModelId == null) {
                  strMessage = "El modelo no ha sido registrado en BSCS";
                  throw new Exception(strMessage);
              }
          }
          
          /*
           * Si no existe el imei se registra el equipo en la base de datos.
           */
           
          hshResult = objRepairService.existsImei(strImei);                       
          boolean existsImei = ((Boolean)hshResult.get("existsImei")).booleanValue();  
          if (!existsImei) {            
              hshResult = objRepairService.registerEquipmentNew(strImei, strNumSerie, strModelId);
              strMessage = (String) hshResult.get("errMsg");
              System.out.println("Luego de llamar a registerEquipmentNew: " + strMessage); 
              if (strMessage != null) {
                  throw new Exception(strMessage);
              }                                          
          }
                    
          /*
           * Se realiza la nacionalización.
           */
           
          hshResult = objRepairService.generateNacionalizacion(hshParameters);
          strMessage = (String) hshResult.get("strMessage");
          System.out.println("Luego de llamar a generateNacionalizacion: " + strMessage);   
          if (strMessage != null) {
              throw new Exception(strMessage);
          }
          
          hshResult.put("strMessage", strMessage);
          request.setAttribute("objResultado", hshResult);

          RequestDispatcher rd = request.getRequestDispatcher("pages/loadMessageNacionalizacion.jsp");
          rd.forward(request, response);
        } catch (Exception e) {
          request.setAttribute("strMessage", strMessage);
          System.out.println("Mensaje no nulo!!! : " + strMessage);
          request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }                
    }

    public void generateRAFile(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        HashMap hshResult = new HashMap();
        HashMap hshParameters = new HashMap();
        String strMessage = null;

        String strOrderID = StringUtils.defaultString(request.getParameter(
                    "iOrderID"));

        System.out.println("");
        System.out.println("[REPAIRSERVLET - generateRAFile - strOrderID : " +
            strOrderID + "]");
        System.out.println("");

        hshParameters.put("strOrderID", strOrderID);

        hshResult = objRepairService.generateRAFile(hshParameters);
        strMessage = (String) hshResult.get("strMessage");
        System.out.println("luego de llamar a generateRAFile : " + strMessage);

        if (StringUtils.isNotBlank(strMessage)) { //en caso de error           
            request.setAttribute("strMessage", strMessage);
            System.out.println("Mensaje no nulo!!! : " + strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request,
                response);
        } else {
            System.out.println("Mensaje nulo!!! : " + strMessage);
            hshResult.put("strMessage", strMessage);
            request.setAttribute("objResultado", hshResult);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "pages/loadMessageRA.jsp");
            rd.forward(request, response);
        }
    }
    
        public void processBarCode(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
                PrintWriter out = null;
                int ancho_img=Integer.parseInt(request.getParameter("ancho_img")), alto_img=Integer.parseInt(request.getParameter("alto_img")),
                                text_font=Integer.parseInt(request.getParameter("font")==null?"0":request.getParameter("font"));
                String codes=request.getParameter("codes");
                String[] array_code=codes.split(",");
                System.out.println(array_code.toString());
                StringBuilder sb=new StringBuilder();
                RepairService service=new RepairService();
                try {
                        out=response.getWriter();
                        //out.println("aaa");
                        byte[][] array_images=objRepairService.getBarCodeImg(array_code,ancho_img,alto_img,text_font);
                        if(array_images.length>0){
                                for (byte[] bs : array_images) {
                                        String encodedImage = Base64.encode(bs);
                                        sb.append(encodedImage).append(",");
                                        //System.out.println("encodedImage: "+encodedImage);
                                }
                        }
                        out.println(sb.delete(sb.length()-1, sb.length()));

                } catch (Exception e) {
                        e.printStackTrace();
                        out=response.getWriter();
                        out.println(e.getMessage());
                } finally {
                        out.close();
                }
        }
    
        public void getDataLoanForReport(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
                PrintWriter out = null;
                int ancho_img=Integer.parseInt(request.getParameter("ancho_img")), alto_img=Integer.parseInt(request.getParameter("alto_img")),
                                text_font=Integer.parseInt(request.getParameter("font")==null?"0":request.getParameter("font"));
                String codes=request.getParameter("codes");
                String[] array_code=codes.split(",");
                System.out.println(array_code.toString());
                StringBuilder sb=new StringBuilder();
                RepairService service=new RepairService();
                try {
                        out=response.getWriter();
                        //out.println("aaa");
                        byte[][] array_images=service.getBarCodeImg(array_code,ancho_img,alto_img,text_font);
                        if(array_images.length>0){
                                for (byte[] bs : array_images) {
                                        String encodedImage = Base64.encode(bs);
                                        sb.append(encodedImage).append(",");
                                        //System.out.println("encodedImage: "+encodedImage);
                                }
                        }
                        out.println(sb.delete(sb.length()-1, sb.length()));

                } catch (Exception e) {
                        e.printStackTrace();
                        out=response.getWriter();
                        out.println(e.getMessage());
                } finally {
                        out.close();
                }
        }
}
