package pe.com.nextel.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//JQUISPE PRY-0762 
import java.util.Map;
import com.google.gson.Gson;

import java.util.Hashtable;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;


import pe.com.nextel.bean.*;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.ProductBean;
import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.ProductPriceBean;
import pe.com.nextel.bean.PlanTarifarioBean;
import pe.com.nextel.bean.TemplateAdendumBean;
import pe.com.nextel.dao.ProductDAO;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.bean.TableBean;
import pe.com.nextel.util.RequestHashMap;



public class ItemServlet extends GenericServlet {

    public void getDetailImei(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException {
        PrintWriter out    = response.getWriter();
        out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
        out.println("<script>");

        try{
            // Inicio - COR0429
            out.println("form        = parent.mainFrame.document.frmdatos;     ");
            out.println("vDoc        = parent.mainFrame;     ");
            // Fin - COR0429
            String        strImei             = request.getParameter("strParamInput");
            String        strFrom             = MiUtil.getString(request.getParameter("strFrom"));
            String        strCategoryId       = MiUtil.getString(request.getParameter("strSpecificationId"));
            String        strCustomerId       = MiUtil.getString(request.getParameter("strCustomerId"));
            String        strModality         = MiUtil.getString(request.getParameter("strItemModality"));
            String        strSolutionId       = MiUtil.getString(request.getParameter("strSolutionId"));
            String        strFlagMigration    = MiUtil.getString(request.getParameter("strFlagMigration"));//[TDECONV003-1] EFLORES Nuevo parametro anadido

            //ProductBean   objProductBean = new ProductBean();
            NewOrderService objNewOrderService = new NewOrderService();
            ItemService objItemService = new ItemService();
            HashMap objHashMap = new HashMap();
            HashMap hServiceDefaultList = null;

            String  strValidateSimImei = "";

            String strMessage = null;
            int iReturn = 0;
            String strConst_Modalidad=null;
            //Validamos si el SIM Eagle se encuentra en algún almacen.
            if(logger.isDebugEnabled()) logger.debug("strFrom: "+strFrom);
            //CPUENTE3
            int iReturnOwnEquip = -1;
            String msgOwnEquip = null;

            if( strFrom.equals("SIMEagle") ) {


                //CPUENTE2 CAP & CAL - INICIO
                if (strCategoryId.equals(Constante.SPEC_CAMBIO_MODELO + "") || strCategoryId.equals(Constante.SPEC_REPOSICION + "")) {

                    HashMap objHashMapOwnEquip = new HashMap();
                    if (Constante.MODALITY_PROPIO.equals(strModality)) { // Si la modalidad es igual a Propio
                        System.out.println("Validando Equipo Propio - Modalidad Salida Propio");
                        System.out.println("Modalidad SIMEagle:" +
                                strModality);
                        objHashMapOwnEquip =
                                objItemService.doValidateOwnEquipment(strImei);

                        iReturnOwnEquip =
                                MiUtil.parseInt(objHashMapOwnEquip.get("strReturn").toString());
                        if (objHashMapOwnEquip.get("strMessage") != null)
                            msgOwnEquip =
                                    objHashMapOwnEquip.get("strMessage").toString();

                        switch(iReturnOwnEquip)
                        {
                            case 0:
                                out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                        iReturnOwnEquip + "');");
                                break;
                            case 1: case 2:
                            out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                    iReturnOwnEquip + "');");
                            out.println("alert('Advertencia: IMEI presenta incidencia. " +
                                    msgOwnEquip + ".');");
                            break;
                            case 3:
                                out.println("vDoc.fxObjectConvert('txt_ItemSIM_Eagle','');");
                                out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                                out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                                out.println("form.txt_ItemSIM_Eagle.focus();");
                                //  out.println("vDoc.fxObjectConvert('cmb_ItemSolution','');"); 
                                throw new Exception("IMEI inválido. " + msgOwnEquip + ".");
                        };

                        strConst_Modalidad = strModality;
                        objHashMap =
                                objNewOrderService.ProductDAOgetDetailByImeiBySpecification(strImei,
                                        MiUtil.parseLong(strCustomerId),
                                        MiUtil.parseLong(strCategoryId),
                                        strConst_Modalidad,strFlagMigration); //[TDECONV003-1] EFLORES
                        // CPUENTE3 Temporal, modificar para traer codigo de error
                        if ((objHashMap.get("strMessage")!= null && objHashMap.get("strMessage").toString().toUpperCase().split(Constante.MSG_SIM).length>1) ||
                                (objHashMap.get("strMessage")!= null && objHashMap.get("strMessage").toString().toUpperCase().split(Constante.MSG_IMEI).length>1))
                        {
                            out.println("vDoc.fxObjectConvert('txt_ItemSIM_Eagle','');");
                            out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                            out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                            out.println("form.txt_ItemSIM_Eagle.focus();");
                            throw new Exception(objHashMap.get("strMessage").toString());
                        }
                    }

                } else {
                    if (Constante.MODALITY_PROPIO.equals(strModality) ||
                            strModality.equals("Alquiler en Cliente")) { // Si la modalidad es igual a Propio
                        System.out.println("Modalidad SIMEagle:" +
                                strModality);
                        if(Constante.MODALITY_PROPIO.equals(strModality) && (strCategoryId.equals(Constante.SPEC_POSTPAGO_VENTA + "") || strCategoryId.equals(Constante.SPEC_POSTPAGO_PORTA + ""))){
                            HashMap objHashMapOwnEquip = new HashMap();
                            objHashMapOwnEquip = objItemService.doValidateOwnEquipment(strImei);
                            iReturnOwnEquip = MiUtil.parseInt(objHashMapOwnEquip.get("strReturn").toString());
                            if (objHashMapOwnEquip.get("strMessage") != null)
                                msgOwnEquip = objHashMapOwnEquip.get("strMessage").toString();

                            switch(iReturnOwnEquip){
                                case 0:
                                    out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                            iReturnOwnEquip + "');");
                                    break;
                                case 1: case 2:
                                    out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                            iReturnOwnEquip + "');");
                                    out.println("alert('Advertencia: IMEI presenta incidencia. " +
                                            msgOwnEquip + ".');");
                                    break;
                                case 3:
                                    out.println("vDoc.fxObjectConvert('txt_ItemSIM_Eagle','');");
                                    out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                                    out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                                    out.println("form.txt_ItemSIM_Eagle.focus();");
                                    throw new Exception("IMEI inválido. " + msgOwnEquip + ".");
                            };
                        }
                        objHashMap = objItemService.doValidateIMEI(strImei);
                        // No valido el mensaje de salida porque, el error lo controlo por las salidas iReturn               
                        iReturn =
                                MiUtil.parseInt((String)objHashMap.get("strReturn"));
                        System.out.println("iReturn:" + iReturn);
                        if (iReturn == 0) {
                            out.println("vDoc.fxObjectConvert('txt_ItemSIM_Eagle','');");
                            out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                            out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                            //  out.println("vDoc.fxObjectConvert('cmb_ItemSolution','');"); 
                            throw new Exception("IMEI inválido. El IMEI " +
                                    strImei +
                                    " se encuentra disponible en almacén");
                        }
                        if (iReturn == 2 || iReturn == 1) {

                            // iReturn =2: si el IMEI existe como NO DISPONIBLE, buscamos en BSCS
                            // iReturn =1: NO encuentra en el sistema de inventarios entonces, quiere decir q no es un lote nuevo, por lo tanto buscamos en bscs
                            strConst_Modalidad = strModality;
                            objHashMap =
                                    objNewOrderService.ProductDAOgetDetailByImeiBySpecification(strImei,
                                            MiUtil.parseLong(strCustomerId),
                                            MiUtil.parseLong(strCategoryId),
                                            strConst_Modalidad,strFlagMigration);//[TDECONV003-1] EFLORES
                        } else {
                            if (objHashMap.get("strMessage") != null) {
                                throw new Exception((String)objHashMap.get("strMessage"));
                            }
                        }
                    }

                }
                //CPUENTE2 CAP & CAL - FIN
              }
            // [TDECONV003-6] PCACERES INI se agrega validacion para txt_ItemImeiFS
            else if (strFrom.equals("IMEIFS")){
                if (Constante.MODALITY_PROPIO.equals(strModality) ||
                        strModality.equals("Alquiler en Cliente")) { // Si la modalidad es igual a Propio
                    System.out.println("Modalidad IMEIFS:" +
                            strModality);
                    if(Constante.MODALITY_PROPIO.equals(strModality) && (strCategoryId.equals(Constante.SPEC_POSTPAGO_VENTA + "") || strCategoryId.equals(Constante.SPEC_POSTPAGO_PORTA + ""))){
                        HashMap objHashMapOwnEquip = new HashMap();
                        objHashMapOwnEquip = objItemService.doValidateOwnEquipment(strImei);
                        iReturnOwnEquip = MiUtil.parseInt(objHashMapOwnEquip.get("strReturn").toString());
                        if (objHashMapOwnEquip.get("strMessage") != null)
                            msgOwnEquip = objHashMapOwnEquip.get("strMessage").toString();

                        switch(iReturnOwnEquip){
                            case 0:
                                out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                        iReturnOwnEquip + "');");
                                break;
                            case 1: case 2:
                                out.println("vDoc.fxObjectConvert('txtItemEquipoPropio','" +
                                        iReturnOwnEquip + "');");
                                out.println("alert('Advertencia: IMEI presenta incidencia. " +
                                        msgOwnEquip + ".');");
                                break;
                            case 3:
                                out.println("vDoc.fxObjectConvert('txt_ItemImeiFS','');");
                                out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                                out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                                out.println("form.txt_ItemImeiFS.focus();");
                                throw new Exception("IMEI inválido. " + msgOwnEquip + ".");
                        };
                    }
                    objHashMap = objItemService.doValidateIMEI(strImei);
                    // No valido el mensaje de salida porque, el error lo controlo por las salidas iReturn
                    iReturn =
                            MiUtil.parseInt((String)objHashMap.get("strReturn"));
                    System.out.println("iReturn:" + iReturn);
                    if (iReturn == 0) {
                        out.println("vDoc.fxObjectConvert('txt_ItemImeiFS','');");
                        out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                        out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                        out.println("form.txt_ItemImeiFS.focus();");
                        throw new Exception("IMEI inválido. El IMEI " +
                                strImei +
                                " se encuentra disponible en almacén");
                    }
                    if (iReturn == 2 || iReturn == 1) {

                        // iReturn =2: si el IMEI existe como NO DISPONIBLE, buscamos en BSCS
                        // iReturn =1: NO encuentra en el sistema de inventarios entonces, quiere decir q no es un lote nuevo, por lo tanto buscamos en bscs
                        strConst_Modalidad = strModality;
                        objHashMap =
                                objNewOrderService.ProductDAOgetDetailByImeiBySpecification(strImei,
                                        MiUtil.parseLong(strCustomerId),
                                        MiUtil.parseLong(strCategoryId),
                                        strConst_Modalidad,strFlagMigration);//[TDECONV003-1] EFLORES
						if (objHashMap.get("strMessage") != null) {
                            out.println("vDoc.fxObjectConvert('txt_ItemImeiFS','');");
                            out.println("form.txt_ItemImeiFS.focus();");
                        }
										
            }else{
                        if (objHashMap.get("strMessage") != null) {
							out.println("form.txt_ItemImeiFS.focus();");
                            throw new Exception((String)objHashMap.get("strMessage"));
                        }
                    }
                }
            } // [TDECONV003-6] PCACERES FIN
            else{
                if(logger.isDebugEnabled()){
                    logger.debug("strImei: "+strImei);
                    logger.debug("strCustomerId: "+strCustomerId);
                    logger.debug("strCategoryId: "+strCategoryId);
                }

                objHashMap = objNewOrderService.ProductDAOgetDetailByImeiBySpecification(strImei,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strCategoryId),null,strFlagMigration);//[TDECONV003-1] EFLORES
            }

            if(logger.isDebugEnabled()) logger.debug("ProductDAOgetDetailByImeiBySpecification - strMessage: "+objHashMap.get("strMessage"));

            ProductBean objProBean = new ProductBean();
            objProBean  = (ProductBean)objHashMap.get("objProductBean");
            String      strResultSimImei;

            if(strImei.equals(objProBean.getNpcd_sim())){
                strValidateSimImei = " * IMEI : "+objProBean.getNpequipmentimei();
                strResultSimImei   = objProBean.getNpequipmentimei();
            }else if (objProBean.getNpcd_sim()!=null){
                strValidateSimImei = " * SIM : "+objProBean.getNpcd_sim();
                strResultSimImei   = objProBean.getNpcd_sim();
            }else{
                strResultSimImei = "";
            }


            if( objHashMap.get("strMessage") != null ){
                if( strFrom.equals("SIMEagle") ) {
                    if (strCategoryId.equals(Constante.SPEC_CAMBIO_MODELO + "") || strCategoryId.equals(Constante.SPEC_REPOSICION + ""))
                    {
                        if (Constante.MODALITY_PROPIO.equals(strModality))
                        {
                            // En caso exito cargar datos producto, lista producto
                            completarInfoProducto(out, objHashMap, strFrom, strCategoryId, strConst_Modalidad, strValidateSimImei, strResultSimImei, strMessage);
                        }
                    }
                    else {
                        //out.println("alert('" + MiUtil.parseInt((String)objHashMap.get("strReturn")) + "');");
                        out.println("vDoc.fxObjectConvert('txt_ItemSIM_Eagle','');");
                        out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                        out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                        out.println("vDoc.fxObjectConvert('cmb_ItemModality','');");
                        // out.println("vDoc.fxObjectConvert('cmb_ItemSolution','');");

                        String strValorStatus=(String)objHashMap.get("strStatusImei");
                        System.out.println("strValorStatus:"+strValorStatus);
                        if( strModality.equals("Propio") && (strValorStatus !=null)) {
                            System.out.println("strValorStatus:"+strValorStatus);
                            if (strValorStatus.equals("20")){
                                out.println("vDoc.fxObjectConvert('cmb_ItemModality','Alquiler en Cliente');");
                            }
                        }
                    }
                }

                if (!strCategoryId.equals(Constante.SPEC_CAMBIO_MODELO + "") && !strCategoryId.equals(Constante.SPEC_REPOSICION + ""))
                {
                    if(MiUtil.contentInArray(MiUtil.parseInt(strCategoryId),Constante.SPEC_DEVOLUCION)){
                        //En Cambio de modelo no se pueden limpiar esos valores
                        //ya que esos campos se cargaron por el telefono y no por IMEI ingresado
                        out.println("vDoc.fxObjectConvert('txt_ItemEquipment','');");
                        out.println("vDoc.fxObjectConvert('txt_ItemModel','');");
                    }
                    //if(logger.isDebugEnabled()) logger.debug("Antes de lanzar el throw new Exception");
                    throw new Exception((String)objHashMap.get("strMessage"));
                }
            }
            else{
                // En caso exito cargar datos producto, lista producto
                if (strCategoryId.equals(Constante.SPEC_POSTPAGO_VENTA + "") && (strConst_Modalidad.equals("Alquiler en Cliente")) ){

                    ProductBean   objProductBean = new ProductBean();
                    objProductBean = (ProductBean)objHashMap.get("objProductBean");

                    ProductBean objProdBeanLine = new ProductBean();
                    objProdBeanLine.setNpproductlineid(objProductBean.getNpproductlineid());

                    objProdBeanLine.setNpmodality(strConst_Modalidad);
                    objProdBeanLine.setNpcategoryid(MiUtil.parseLong(strCategoryId));

                    NewOrderService objNewOrderServices = new NewOrderService();
                    HashMap objHashMaps = objNewOrderServices.getProductType(objProdBeanLine);


                    //Encontramos los permisos para visualizar servicios de mensajeria o arrendamiento
                    //--------------------------------------------------------------------------------
                    GeneralService objGeneralServices= new GeneralService();

                    HashMap objHashMapPer   =    objGeneralServices.getPermissionServiceDefault(
                            MiUtil.parseLong(strCategoryId),
                            null,
                            strModality,
                            0,
                            MiUtil.parseInt(MiUtil.getString(objProductBean.getNpproductid()))
                            ,"");

                    String strPermission_alq  = (String) objHashMapPer.get("ipermission_alq");
                    String strPermission_msj = (String) objHashMapPer.get("ipermission_msj");

                    if ( (MiUtil.parseInt(strPermission_alq)==1)  || (MiUtil.parseInt(strPermission_msj)==1)){

                        hServiceDefaultList   = objNewOrderService.getProductServiceDefaultList(MiUtil.parseLong(strCategoryId),
                                MiUtil.parseInt(MiUtil.getString(objProductBean.getNpproductid())),
                                MiUtil.parseInt(MiUtil.getString(objProductBean.getNpplanid())),
                                MiUtil.parseInt(strPermission_alq),
                                MiUtil.parseInt(strPermission_msj));

                        if (hServiceDefaultList.get("strMessage") != null){
                            throw new Exception((String)hServiceDefaultList.get("strMessage"));
                        }

                    }

                    if(hServiceDefaultList.get("objServiceDefaultList") != null)
                        System.out.println("hServiceDefaultList.size() " + ((ArrayList)hServiceDefaultList.get("objServiceDefaultList")).size());
                    request.setAttribute("strProductLineId", MiUtil.getString(objProductBean.getNpproductlineid()) );
                    request.setAttribute("strProductId", MiUtil.getString(objProductBean.getNpproductid()));
                    request.setAttribute("strPlanTarifario",MiUtil.getString(objProductBean.getNpplanid()));
                    request.setAttribute("hServiceDefaultList", hServiceDefaultList);
                    request.setAttribute("strSessionId", null);
                    request.setAttribute("strSpecificationId",strCategoryId);
                    request.setAttribute("serviciosBean", null);
                    request.setAttribute("objHashMaps", objHashMaps);

                    try{
                        request.getRequestDispatcher("/pages/loadServicesLeaseList.jsp").forward(request, response);
                    }catch (Exception e) {
                        logger.error(formatException(e));
                    }
                }else{
                    if (Constante.MODALITY_PROPIO.equals(strModality) &&
                            (String.valueOf(Constante.SPEC_POSTPAGO_VENTA).equals(strCategoryId) || String.valueOf(Constante.SPEC_PREPAGO_NUEVA).equals(strCategoryId)
                                    || String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO).equals(strCategoryId)|| String.valueOf(Constante.SPEC_PREPAGO_TDE).equals(strCategoryId)
                                    || String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE).equals(strCategoryId) )) {
                        ProductBean obj = (ProductBean)objHashMap.get("objProductBean");
                        obj.setNpsolutionid(MiUtil.parseLong(strSolutionId));
                        obj.setNpcategoryid(MiUtil.parseLong(strCategoryId));
                        this.loadProductPlanList(obj,out);
                    }
                    completarInfoProducto(out, objHashMap, strFrom, strCategoryId, strConst_Modalidad, strValidateSimImei, strResultSimImei, strMessage);
                }
            }

        }catch(Exception ex){
            if(logger.isDebugEnabled()) logger.debug("Esta en el catch: "+ex.getMessage() );
            out.println("alert('"+ex.getMessage()+"');");
        }
        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
        out.println("</script>");
    }

    private void completarInfoProducto(PrintWriter out,  HashMap objHashMap, String strFrom, String strCategoryId, String strConst_Modalidad, String strValidateSimImei, String strResultSimImei, String strMessage)
    {
        NewOrderService objNewOrderService = new NewOrderService();
        ProductBean   objProductBean = new ProductBean();
        objProductBean = (ProductBean)objHashMap.get("objProductBean");
        if(logger.isDebugEnabled()) logger.debug("objProductBean:"+objProductBean);
        //Si el productBean no es vacío
        if( objProductBean != null){
            if(logger.isDebugEnabled()) logger.debug("objProductBean es diferente de null");
            out.println("form        = parent.mainFrame.document.frmdatos;     ");
            out.println("vDoc        = parent.mainFrame;     ");
            if( strFrom.equals("SIMEagle") ) {
                if(logger.isDebugEnabled()) logger.debug("SIMEagle");
                //Setear la linea de producto y el producto
                out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','"+objProductBean.getNpproductlineid() +"');");

                out.println("vDoc.fxObjectConvert('txt_ItemImeiSim','"+strResultSimImei +"');");
                out.println("vDoc.idValidateImeSim.innerHTML = '"+strValidateSimImei+"'");


                //if (objProductBean.getNpproductlineid()!=0 && MiUtil.parseInt(strCategoryId)==Constante.SPEC_CAMBIO_MODELO) {
                if ((MiUtil.parseInt(strCategoryId)==Constante.SPEC_CAMBIO_MODELO)||(MiUtil.parseInt(strCategoryId)==Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) {
                    //el campo es de modo de solo lectura
                    out.println("if (form.cmb_ItemProductLine.value!=''){");
                    out.println("  form.cmb_ItemProductLine.disabled=true");
                    out.println("}");
                }
                //Ya seteada la Linea de Producto Cargamos los productos de la Línea
                ProductBean objProdBeanLine = new ProductBean();
                objProdBeanLine.setNpproductlineid(objProductBean.getNpproductlineid());
                // objProdBeanLine.setNpmodality(Constante.MODALITY_PROPIO);
                objProdBeanLine.setNpmodality(strConst_Modalidad);
                objProdBeanLine.setNpcategoryid(MiUtil.parseLong(strCategoryId));
                loadProductList(objProdBeanLine,out);
                //out.println("vDoc.fxObjectConvert('cmb_ItemSolution','"+objProductBean.getNpsolutionid() +"');");
                out.println("vDoc.fxObjectConvert('cmb_ItemProducto','"+objProductBean.getNpproductid() +"');");
                out.println("vDoc.fxObjectConvert('cmb_ItemPlanTarifario','"+objProductBean.getNpplanid() +"');");
                //if (objProductBean.getNpproductlineid()!=0 && MiUtil.parseInt(strCategoryId)==Constante.SPEC_CAMBIO_MODELO) {
                if (MiUtil.parseInt(strCategoryId)==Constante.SPEC_CAMBIO_MODELO) {
                    //el campo es de modo de solo lectura
                    out.println("if (form.cmb_ItemProducto.value!=''){");
                    out.println("  form.cmb_ItemProducto.disabled=true");
                    out.println("}");
                }
                //out.println("       form.cmb_ItemSolution.disabled=true");
                out.println("try{");
                out.println("form.valorOldProductNew.value = ''");
                out.println("form.valorOldModalityNew.value = ''");
                out.println("form.valorOldPlanNew.value = ''");
                out.println("form.valorOldQuantity.value = ''");
                out.println("vDoc.DeleteSelectOptions(form.cmb_ItemPromocion);}catch(e){}");
                out.println("vDoc.fxObjectConvert('txt_ItemPriceCtaInscrip','');");
                out.println("vDoc.fxObjectConvert('txt_ItemPriceException','');");
                out.println("vDoc.fxObjectConvert('txt_ItemEquipmentRent','');");
                out.println("try{");
                out.println("form.txt_ItemPriceBolsa.value    = fxSearchProductMinutes(objValue);}catch(e){}");
                out.println("try{");
                out.println("form.txt_ItemCantMntsBolsa.value = fxSearchMinutes(objValue);}catch(e){}");
                //Inicio [#1546]
                System.out.println("txt_ItemModel:"+MiUtil.getString(objProductBean.getNpproductmodel()));
                if (MiUtil.parseInt(strCategoryId)==Constante.SPEC_PREPAGO_NUEVA || MiUtil.parseInt(strCategoryId)==Constante.SPEC_PREPAGO_PORTA || MiUtil.parseInt(strCategoryId)==Constante.SPEC_PREPAGO_TDE
                    || MiUtil.parseInt(strCategoryId)==Constante.SPEC_REPOSICION_PREPAGO_TDE ) {
                    out.println("vDoc.fxObjectConvert('txt_ItemModel','"+MiUtil.getString(objProductBean.getNpproductmodel())+"');");
                    out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','');");
                    out.println("vDoc.fxObjectConvert('cmb_ItemProducto','');");
                    //Si no encuentra el modelo, muestra el mensaje
                    if ("".equals(MiUtil.getString(objProductBean.getNpproductmodel()))){
                        out.println("try{");
                        out.println(" form.txt_ItemModel.disabled=false;");
                        out.println(" form.txt_ItemModel.focus();");
                        out.println("}catch(e){}");
                        out.println("alert('No se encontro el modelo, seleccione uno de la lista');");
                    }
                }
                //Fin [#1546]
                //Servicios Adicionales Mensajeria - Ventas moviles
                String    serviceMessage      = objProductBean.getNpssaa_contratado();
                ArrayList objArrayListDefault = new ArrayList();
                String    strObjectId         = "FFPEDIDOS";

                String    serviceId           = "", serviceDefault ="|";

                objArrayListDefault   = objNewOrderService.ServiceDAOgetServiceDefaultList(strObjectId,MiUtil.parseInt(strCategoryId),strMessage);

                if( objArrayListDefault != null && objArrayListDefault.size() > 0 ) {
                    ServiciosBean serviciosBeanDefault = null;
                    for( int j = 0; j < objArrayListDefault.size(); j++ ){
                        serviciosBeanDefault = (ServiciosBean)objArrayListDefault.get(j);
                        serviceId =MiUtil.getString(serviciosBeanDefault.getNpservicioid());
                        serviceDefault += serviceId+"|N|S|";
                    }

                    int longitud = serviceDefault.length()-1;
                    serviceDefault = serviceDefault.substring(0,longitud)+serviceMessage;
                    out.println("vDoc.fxCargaServiciosItem('"+ serviceDefault +"');");
                }
                // Fin Servicios Adicionales Mensajeria
            }
            else{
                if(logger.isDebugEnabled()) logger.debug("Diferente SIMEagle");
                out.println("vDoc.fxObjectConvert('cmb_ItemSolution','"+objProductBean.getNpsolutionid() +"');");
                out.println("vDoc.fxObjectConvert('cmb_ItemNewPlantarifario','"+objProductBean.getNpplan() +"');");
                out.println("vDoc.fxObjectConvert('txt_ItemSIM','"+MiUtil.getString(objProductBean.getNpcd_sim()) +"');");
                out.println("vDoc.fxObjectConvert('txt_ItemIMEI','"+objProductBean.getNpequipmentimei() +"');");
                //out.println("vDoc.fxObjectConvert('txt_ItemModel','"+objProductBean.getNpproductmodel() +"');");
                out.println("try{ vDoc.fxSetModelGeneric('"+objProductBean.getNpproductmodel() +"'); }catch(e){}");
                out.println("try{ vDoc.fxSetReplaceEquipment('"+MiUtil.getString(objProductBean.getStrRealModel())+"', '"+objProductBean.getLRealModelId()+"', '"+MiUtil.getString(objProductBean.getStrRealSim())+"', '"+MiUtil.getString(objProductBean.getStrRealImei())+"', '"+objProductBean.getLRealProductLineId()+"'); }catch(e){}");
                out.println("try{ vDoc.fxSetReplaceEquipment('"+objProductBean.getNpproductmodel() +"'); }catch(e){}");
                out.println("vDoc.fxObjectConvert('txt_ItemEquipment','"+objProductBean.getNpequipment() +"');");
                out.println("vDoc.fxObjectConvert('txt_ItemNroOcurrence','"+objProductBean.getNpoccurrence() +"');");
                out.println("vDoc.fxObjectConvert('txt_ItemFlgProductGN','"+objProductBean.getNpwarranty() +"');");
                out.println("vDoc.fxObjectConvert('txt_ItemNewPlantarifarioId','"+objProductBean.getNpplanid() +"');");
                out.println("form.cmb_ItemSolution.disabled=true");
            }

            //PRY-0721 DERAZO Si se lee el producto con el IMEI validamos si hay que mostrar el combo Regiones
            System.out.println("[ItemServlet][completarInfoProducto] productBean diferente de nulo");
            out.println("var sectionRegionIMEI = parent.mainFrame.document.getElementById('idDisplay"+Constante.CONTROL_ITEM_REGION_ID+"');");
            out.println("formCurrentRegion = parent.mainFrame.frmdatos;");
            out.println("if(sectionRegionIMEI != null){");
            String strProductId = String.valueOf(objProductBean.getNpproductid());
            System.out.println("[ItemServlet][completarInfoProducto] strProductId: "+strProductId);
            HashMap objHashMapRegion = objGeneralService.getEnabledRegions(strProductId);
            int result = 0;
            List<RegionBean> listaRegiones;
            RegionBean objRegionBean;
            String strMessageRegion = null;
            strMessageRegion = (String)objHashMapRegion.get("strMessage");
            System.out.println("[ItemServlet][completarInfoProducto] strMessage: "+strMessageRegion);

            if(strMessageRegion != null){
                out.println("formCurrentRegion.cmb_ItemRegion.value = ''");
                out.println("parent.mainFrame.idDisplay"+Constante.CONTROL_ITEM_REGION_ID+".style.display = 'none';");
        }
            else{
                result = ((Integer)objHashMapRegion.get("result")).intValue();
                if(result > 0){
                    //Se muestra combo regiones
                    System.out.println("[ItemServlet][completarInfoProducto] muestra comboBox region");
                    out.println("parent.mainFrame.idDisplay"+Constante.CONTROL_ITEM_REGION_ID+".style.display = 'block';");
                    out.println("parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_ItemRegion);");

                    listaRegiones = (ArrayList<RegionBean>)objHashMapRegion.get("listaRegiones");
                    System.out.println("[ItemServlet][completarInfoProducto] cantidad de regiones habilitadas: " + listaRegiones.size());

                    if(listaRegiones != null && listaRegiones.size() > 0){
                        for(int i=0; i<listaRegiones.size();i++){
                            objRegionBean = (RegionBean)listaRegiones.get(i);
                            out.println("parent.mainFrame.AddNewOption(formCurrentRegion.cmb_ItemRegion,'"+objRegionBean.getRegionId()+"','"+MiUtil.getMessageClean(objRegionBean.getRegionName())+"')");
    }
                    }
                }
                else{
                    //Se oculta combo regiones
                    System.out.println("[ItemServlet][completarInfoProducto] oculta comboBox region");
                    out.println("formCurrentRegion.cmb_ItemRegion.value = ''");
                    out.println("parent.mainFrame.idDisplay"+Constante.CONTROL_ITEM_REGION_ID+".style.display = 'none';");
                }
            }
            out.println("}");
        }
    }
  
  /*Incio Cambios JPEREZ*/
    /**
     * Motivo:  Método que se invoca para validar número de contrato
     * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">ISRAEL RONDON</a>
     * <br>Fecha: 29/10/2007
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getValidateContract(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        long lngContractNumber    = MiUtil.parseLong((String)request.getParameter("strContractNumber"));
        long lngSwCustomerId      = MiUtil.parseLong((String)request.getParameter("strCustomerId"));
        long lngSpecificationId   = MiUtil.parseLong((String)request.getParameter("strSpecificationId"));

        long lngSolutionId        = MiUtil.parseLong((String)request.getParameter("strSolution"));
        long lngInstallAddressId  = MiUtil.parseLong((String)request.getParameter("strInstallAddressId"));
        String strControlName     = MiUtil.getString((String)request.getParameter("nameText"));
        HashMap hValidateContract = objGeneralService.getValidateContract(lngSwCustomerId, lngContractNumber,lngSpecificationId, lngSolutionId, lngInstallAddressId);
        hValidateContract.put("strControlName", strControlName);
        hValidateContract.put("strItemSolutionId", MiUtil.getString(lngSolutionId));
        hValidateContract.put("strSpecificationId", MiUtil.getString(lngSpecificationId));

        request.setAttribute("hValidateContract", hValidateContract);
        try {
            request.getRequestDispatcher("pages/loadValidateContract.jsp").forward(request, response);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que se invoca para validar el teléfono (telefonía fija)
     * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br>Fecha: 27/05/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getValidatePhoneVoIp(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String strPhoneNumber     = MiUtil.getString((String)request.getParameter("strPhoneNumber"));
        long lngSwCustomerId      = MiUtil.parseLong((String)request.getParameter("strCustomerId"));
        long lngSpecificationId   = MiUtil.parseLong((String)request.getParameter("strSpecificationId"));
        long lngSolutionId        = MiUtil.parseLong((String)request.getParameter("strSolution"));
        long lngInstallAddressId  = MiUtil.parseLong((String)request.getParameter("strInstallAddressId"));
        String strControlName     = MiUtil.getString((String)request.getParameter("nameText"));
        HashMap hValidatePhone = objGeneralService.getValidatePhoneVoIp(lngSwCustomerId, strPhoneNumber,lngSpecificationId, lngSolutionId, lngInstallAddressId);
        hValidatePhone.put("strControlName", strControlName);
        hValidatePhone.put("strItemSolutionId", MiUtil.getString(lngSolutionId));
        request.setAttribute("hValidatePhone", hValidatePhone);
        try {
            request.getRequestDispatcher("pages/loadValidatePhoneVoIp.jsp").forward(request, response);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
    }
  /*Fin Cambios JPEREZ*/

    /**
     * Motivo:  Método que se invoca para validar número de contrato
     * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">ISRAEL RONDON</a>
     * <br>Fecha: 06/11/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getServiceList(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException,Exception {

        int iSolutionId         = Integer.parseInt(request.getParameter("cmb_ItemSolution"));
        int iPlanId             = Integer.parseInt(request.getParameter("cmb_ItemPlanTarifario"));
        /**DLAZO: INICIO - Parametros para Suscripciones**/
        //johncmb
        int iModelId            = request.getParameter("txt_ItemModel")!=null?Integer.parseInt(request.getParameter("txt_ItemModel")):0;
        String strSSAAType  = null; //johncmb
        /**DLAZO: FIN**/
        //String serviceAditional = (String)request.getParameter("serviceAditional");

        //String strSession       = (String)request.getParameter("strSessionId");

        //john viernes
        String hdnSpecification = (String)request.getParameter("hdnSpecification");
        System.out.println("El hdnSpecification es ---->" + hdnSpecification);
        System.out.println("El REQUEST strProductId es ---->" + request.getParameter("strProductId"));

        if(request.getParameter("strProductId")!=null && (MiUtil.parseInt(hdnSpecification)==Constante.SPEC_POSTPAGO_VENTA
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_EMPLEADO_AMIGO || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_EMPLEADO_FAMILIAR
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_EMPLEADO_ASIGNACION || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_PORTABILIDAD_POSTPAGO
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_TRANSFERENCIA
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_ACCESO_INTERNET || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_VENTA_INTERNET_ENLACE_DATOS
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_PRESTAMO_TEST
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_CAMBIAR_PLAN_TARIFARIO || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS
                || MiUtil.parseInt(hdnSpecification)==Constante.SPEC_CAMBIO_MODELO//EZUBIAURR
        )){
            System.out.println("Ingreso al IF EZ---->"+MiUtil.parseInt(hdnSpecification));//EZUBIAURR

            String dat = request.getParameter("strProductId");


            if(!("".equals(dat.trim()))){
                iModelId = Integer.parseInt(request.getParameter("strProductId"));
            }else{
                iModelId = 0;
            }
        }
        //john viernes

        String serviceAditional = (String)request.getParameter("serviceAditional");
        //String hdnSpecification = (String)request.getParameter("hdnSpecification");
        String strSession       = (String)request.getParameter("strSessionId");

        String strobjectItem       = (String)request.getParameter("strobjectItem");
        String strProductId        = (String)request.getParameter("strProductId")==null?"0":(String)request.getParameter("strProductId");
        String strModality         = (String)request.getParameter("strModality")==null?"":(String)request.getParameter("strModality");
        String strModelId          = (String)request.getParameter("strModelId")==null?"0":(String)request.getParameter("strModelId");
        String strMessage          = null;
        String strPermission_alq   = null;
        String strPermission_msj   = null;


        NewOrderService objNewOrderService = new NewOrderService();
        String strType = null;

        HashMap objHashMap = null;
        HashMap hServiceDefaultList = null;
        ArrayList objArrayListDefault = new ArrayList();
        GeneralService objGeneralService= new GeneralService();


        if (logger.isDebugEnabled()) {
            logger.debug("iPlanId:::" + iPlanId);
        }
        System.out.println("[ItemServlet][getServiceList][iPlanId]"+iPlanId);
        System.out.println("[ItemServlet][getServiceList][iSolutionId]"+iSolutionId);
        System.out.println("[ItemServlet][getServiceList][serviceAditional]"+serviceAditional);

        if (MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_DES_SERVICIOS_BA){
            strType = "A";
        }
        /**DLAZO: INICIO**/
        //johncmb inicio
        if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE     ||
                MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE //Se agrego subcategoria reposicion prepago tde - TDECONV034
                ) {
            strSSAAType  = "0,1,2,3,5";}
        else{
            strSSAAType  = "0,1,5";
        }

        HashMap hServiceList        = objGeneralService.getServiceList(iSolutionId, iPlanId, iModelId, strSSAAType, strType);
        //johncmb fin
        /**DLAZO: FIN**/
        HashMap hshServiceRentList  = objNewOrderService.getServiceRentList(MiUtil.parseLong((String)request.getParameter("cmb_ItemPlanTarifario")));

        request.setAttribute("hServiceList", hServiceList);
        request.setAttribute("serviceAditional", serviceAditional);
        request.setAttribute("hshServiceRentList", hshServiceRentList);
        request.setAttribute("hdnSpecification", hdnSpecification);
        request.setAttribute("strSessionId",strSession);

        //Encontramos los permisos para visualizar servicios de mensajeria o arrendamiento
        //--------------------------------------------------------------------------------
        objHashMap   =    objGeneralService.getPermissionServiceDefault(MiUtil.parseLong(hdnSpecification),strobjectItem,strModality,
                MiUtil.parseInt(strModelId),MiUtil.parseInt(strProductId),"");

        strPermission_alq  = (String) objHashMap.get("ipermission_alq");
        strPermission_msj = (String) objHashMap.get("ipermission_msj");

        System.out.println("strPermission_alq:"+strPermission_alq);
        System.out.println("strPermission_msj:"+strPermission_msj);

        if ( (MiUtil.parseInt(strPermission_alq)==1)  || (MiUtil.parseInt(strPermission_msj)==1)){

            if ((MiUtil.parseInt(strPermission_msj)==1) &&   (MiUtil.parseInt(strModelId)!=0) &&  (iPlanId==0)){
                iPlanId = MiUtil.parseInt(strModelId);
            }
            hServiceDefaultList   = objNewOrderService.getProductServiceDefaultList(MiUtil.parseLong(hdnSpecification),
                    MiUtil.parseInt(strProductId),
                    iPlanId,
                    MiUtil.parseInt(strPermission_alq),
                    MiUtil.parseInt(strPermission_msj));
            if (hServiceDefaultList.get("strMessage") != null){
                throw new Exception((String)hServiceDefaultList.get("strMessage"));
            }
        }
        //Inicio SSAA por Defecto - 23/11/2011 - Frank Picoy
        HashMap hshServiceDefaultListByPlan  = new HashMap();
        request.setAttribute("strSSAAByDefault",Constante.SPECIFICATION_TO_SSAA_DEFAULT_NOT);
        request.setAttribute("strSSAAByDefWithLSA",Constante.SPECIFICATION_TO_SSAA_DEFAULT_NOT);
        if (iPlanId>0) {
            HashMap hNpTableList  = objGeneralService.getTableList("SPECIFICATION_TO_SSAA_DEFAULT","1");
            ArrayList objNpTableList =(ArrayList)hNpTableList.get("arrTableList");
            boolean isSpecSSAADef = false;
            boolean isSpecSSAADefWithLSA = false; //Categoria con logica adicional al agregar los servicios por Defecto
            //TableBean nptableBean = null;
            HashMap objTable = null;
            for (int k=0;k<objNpTableList.size();k++) {
                objTable = (HashMap)objNpTableList.get(k);
                if (hdnSpecification.equals(objTable.get("wv_npValue").toString())) {
                    isSpecSSAADef = true;
                    if ("1".equals(objTable.get("wv_npTag1").toString())) {
                        isSpecSSAADefWithLSA = true;
                    }
                    break;
                }
            }
            if (isSpecSSAADef){
                request.setAttribute("strSSAAByDefault",Constante.SPECIFICATION_TO_SSAA_DEFAULT_YES);
                hshServiceDefaultListByPlan  = objNewOrderService.getServiceDefaultListByPlan(MiUtil.parseLong(String.valueOf(iPlanId)));
            }
            if (isSpecSSAADefWithLSA){
                request.setAttribute("strSSAAByDefWithLSA",Constante.SPECIFICATION_TO_SSAA_DEFAULT_YES);
            }
        }
        request.setAttribute("hshServiceDefaultListByPlan",hshServiceDefaultListByPlan);
        //Fin SSAA por Defecto - 23/11/2011 - Frank Picoy

        HashMap hNpTableList  = objGeneralService.getValueNpTable("FLAG_LOG_ITEM_CAMBIO_MODELO");
        ArrayList objNpTableList =(ArrayList)hNpTableList.get("objArrayList");
        String  strflagLogItem= "I";
        TableBean nptableBean = null;
        if (objNpTableList.size()>0) {
            nptableBean = (TableBean)objNpTableList.get(0);
            strflagLogItem = nptableBean.getNpValue().trim();
            System.out.println("El strflagLogItem tiene---->" + strflagLogItem);
        }
        if (String.valueOf(Constante.SPEC_CAMBIO_MODELO).equals(hdnSpecification) && "A".equals(strflagLogItem)) {//Si esta permitido escribir logs
            String strPhone = (String)request.getParameter("strPhone");
            String strOrderId = (String)request.getParameter("strOrderId");
            String strDescProduct = (String)request.getParameter("strDescProduct");
            String strTypeEvent = request.getParameter("strTypeEvent");
            request.setAttribute("strTypeEvent",strTypeEvent);
            PortalSessionBean objSessionBean2 = (PortalSessionBean)SessionService.getUserSession(strSession);
            String ssaa_contratado = serviceAditional.replace('|',':');
            String serviciosAdicionales[] = ssaa_contratado.split(":");
            String servicioAdicional = null;
            String servContract = "";
            ServiciosBean serviciosBean = null;
            for(int i=0; i<serviciosAdicionales.length; i++){
                servicioAdicional = serviciosAdicionales[i];
                try{
                    System.out.println("servicioAdicional " + servicioAdicional);
                    serviciosBean = objGeneralService.getDetailService(Long.parseLong(servicioAdicional));
                    servContract = servContract + serviciosBean.getNpservicioid() + "|" + serviciosBean.getNpnomserv() + "|";
                }  catch(NumberFormatException e){
                    servContract = servContract + servicioAdicional + "|";
                    continue;
                }
            }
            String messageLog = "[Seleccionando Producto..." +  strDescProduct + "][ItemServlet][getServiceList]strPhone:" + strPhone + " Modalidad:" + strModality + " hdnSpecification:" + hdnSpecification + " Servicios Contratados:" + servContract + " strPermission_alq:" + strPermission_alq + " strPermission_msj:" + strPermission_msj;
            HashMap objLogItem = new HashMap();
            objLogItem.put("nporderid",strOrderId);
            objLogItem.put("npinbox",strTypeEvent);
            objLogItem.put("npdescription",messageLog.length()<2000?messageLog:messageLog.substring(0,2000));
            objLogItem.put("npcreatedby",objSessionBean2==null?"MWONG2":objSessionBean2.getLogin());
            objGeneralService.doSaveLogItem(objLogItem);
            System.out.println("***********************************************************INICIO LOG CAMBIO DE MODELO***********************************************");
            System.out.println("[ItemServlet][getServiceList][strPhone]"+strPhone);
            System.out.println("[ItemServlet][getServiceList][hdnSpecification]"+hdnSpecification);
            System.out.println("[ItemServlet][getServiceList][iPlanId]"+iPlanId);
            System.out.println("[ItemServlet][getServiceList][iSolutionId]"+iSolutionId);
            System.out.println("[ItemServlet][getServiceList][serviceAditional]"+serviceAditional);
            System.out.println("[ItemServlet][getServiceList][strPermission_alq]"+strPermission_alq);
            System.out.println("[ItemServlet][getServiceList][strPermission_msj]"+strPermission_msj);
            System.out.println("***********************************************************FIN LOG CAMBIO DE MODELO***********************************************");
        }

        request.setAttribute("strSpecificationId",hdnSpecification);
        request.setAttribute("hServiceDefaultList", hServiceDefaultList);
        request.setAttribute("strPermission_alq", strPermission_alq);
        request.setAttribute("strPermission_msj", strPermission_msj);
        request.setAttribute("serviciosBean", null);

        //Inicio SAR N_O000012485 - FPICOY - 09/01/2014
        if (String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO).equals(hdnSpecification) || String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS).equals(hdnSpecification)){
            RequestHashMap objReqHashMap =  new RequestHashMap();
            System.out.println("strPhone--->"+ request.getParameter("txt_ItemPhone"));
            System.out.println("strPlanId--->"+ request.getParameter("cmb_ItemPlanTarifario"));
            objReqHashMap.put("strPhone", (String)request.getParameter("txt_ItemPhone"));
            objReqHashMap.put("strPlanId", request.getParameter("cmb_ItemPlanTarifario"));
            HashMap objResultHashMap=objNewOrderService.doValidateChangePlanToEmployee(objReqHashMap);
            String strMessageEmployeeValid = objResultHashMap.get("strMessageValid")!=null?objResultHashMap.get("strMessageValid").toString():"";
            System.out.println("Resultado Servlet--->" + strMessageEmployeeValid);
            request.setAttribute("strMessageEmployeeValid", strMessageEmployeeValid);
        }
        //Fin SAR N_O000012485 - FPICOY - 09/01/2014

        //Inicio SAR N_O000012485 - FPICOY - 09/01/2014
        if (String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO).equals(hdnSpecification) || String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS).equals(hdnSpecification)){
            RequestHashMap objReqHashMap =  new RequestHashMap();
            System.out.println("strPhone--->"+ request.getParameter("txt_ItemPhone"));
            System.out.println("strPlanId--->"+ request.getParameter("cmb_ItemPlanTarifario"));
            objReqHashMap.put("strPhone", (String)request.getParameter("txt_ItemPhone"));
            objReqHashMap.put("strPlanId", request.getParameter("cmb_ItemPlanTarifario"));
            HashMap objResultHashMap=objNewOrderService.doValidateChangePlanToEmployee(objReqHashMap);
            String strMessageEmployeeValid = objResultHashMap.get("strMessageValid")!=null?objResultHashMap.get("strMessageValid").toString():"";
            System.out.println("Resultado Servlet--->" + strMessageEmployeeValid);
            request.setAttribute("strMessageEmployeeValid", strMessageEmployeeValid);
        }
        //Fin SAR N_O000012485 - FPICOY - 09/01/2014

        try{
            request.getRequestDispatcher("/pages/loadServicesList.jsp").forward(request, response);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que se invoca para validar número de contrato
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales Crispin</a>
     * <br>Fecha: 06/11/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getProducTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        logger.info("*************************** INICIO ItemServlet > getProducTypeList***************************");
        PrintWriter out            = response.getWriter();
        ProductBean objProductBean = new ProductBean();
        String strModality         = request.getParameter("strModality");
        String strProductLineId    = request.getParameter("strProductLineId");
        String strCategoryId       = request.getParameter("strSpecificationId");
        String strSolutionId       = request.getParameter("strSolutionId");
        int intSpecificationId       = Integer.parseInt(request.getParameter("strSpecificationId"));//EZUBIAUUR

        objProductBean.setNpproductlineid(MiUtil.parseLong(strProductLineId));
        objProductBean.setNpmodality(strModality);
        objProductBean.setNpcategoryid(MiUtil.parseLong(strCategoryId));
        objProductBean.setNpsolutionid(MiUtil.parseLong(strSolutionId));

        logger.info("strModality          :"+strModality);
        logger.info("strProductLineId     :"+strProductLineId);
        logger.info("strCategoryId        :"+strCategoryId);
        logger.info("strSolutionId        :"+strSolutionId);
        logger.info("intSpecificationId   :"+intSpecificationId);

       /*INICIO ADT-BLC-083 --LHUAPAYA*/
        if(intSpecificationId == Constante.SPEC_BOLSA_CREAR){
            int typeProduct = Integer.parseInt(request.getParameter("strTypeProduct"));
            logger.info("typeProduct   :"+typeProduct);
            objProductBean.setNpproduct_type(typeProduct);
        }
        if(intSpecificationId == Constante.SPEC_BOLSA_UPGRADE || intSpecificationId == Constante.SPEC_BOLSA_DOWNGRADE || intSpecificationId == Constante.SPEC_BOLSA_DESACTIVAR){
            if(!"".equals(request.getParameter("strSiteId"))){
                long siteId = Long.parseLong(request.getParameter("strSiteId"));
                logger.info("siteId   :"+siteId);
                objProductBean.setNpsiteid(siteId);
            }else{
                long customerId = Long.parseLong(request.getParameter("strCustomerId"));
                logger.info("customerId   :"+customerId);
                objProductBean.setNpcustomerid(customerId);
            }
            objProductBean.setNpproduct_type(1);
            objProductBean.setNpcategoryid(intSpecificationId);
        }
       /*FIN ADT-BLC-083 --LHUAPAYA*/
        out.println("<script>");
        //INICIO FPICOY - Garantia Extendida
        if (strProductLineId!=null && (Constante.PRODUCT_LINE_KIT_GAR_EXT.equals(strProductLineId) || Constante.PRODUCT_LINE_KIT_GOLDEN.equals(strProductLineId))) {
            String strModelid = request.getParameter("strModelId");
            logger.info("strModelid   :"+strModelid);
            objProductBean.setNpproductid(MiUtil.parseLong(strModelid));
        }
        //FIN FPICOY
        //INICIO EZUBIAUUR - Compatibilidad Plan - Producto - Servicios
        if (strProductLineId!=null && Constante.SPEC_CAMBIO_MODELO == intSpecificationId) {
            String strPlanid = request.getParameter("strPlanId");
            logger.info("strPlanid   :"+strPlanid);
            objProductBean.setNpplanid(MiUtil.parseLong(strPlanid));
        }
        //FIN EZUBIAUUR
        loadProductList(objProductBean,out);
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
        out.println("</script>");
        logger.info("*************************** FIN ItemServlet > getProducTypeList***************************");
    }

    public void loadProductList(ProductBean objProductBean,PrintWriter out){
        NewOrderService objNewOrderService = new NewOrderService();
        HashMap objHashMap = objNewOrderService.getProductType(objProductBean);

        ArrayList objArrayList = new ArrayList();

        if( (String)objHashMap.get("strMessage")!= null ){
            String variable = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
            out.println("alert('"+variable+"')");
        }else{

            /*INICIO ADT-BLC-083 -- LHUAPAYA*/
            long categoriaIdBolsa = 0; // variable añadida para bolsa de celulares
            if(objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_UPGRADE || objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_DOWNGRADE || objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_DESACTIVAR) {
                categoriaIdBolsa = objProductBean.getNpcategoryid();
                ProductBean objProductBeanTemp = new ProductBean();
                HashMap objHashMapTemp = objNewOrderService.getBolsaCelulares(objProductBean.getNpsiteid(), objProductBean.getNpcustomerid(), objProductBean.getNpproductlineid());
                objProductBeanTemp = (ProductBean) objHashMapTemp.get("objProductBean");
                out.println("formCurrent = parent.mainFrame.frmdatos;");
                if ((String) objHashMapTemp.get("message") != null) {
                    out.println("alert('No existe producto registrado para el cliente.')");
                    out.println("formCurrent.cmb_ItemProductoOrigen.value = '';");
                    out.println("formCurrent.cmb_ItemProductBolsaId.value = '';");
                    out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProductoDestino);");
                    return;
                } else {
                    if (objProductBean.getNpcategoryid() == Constante.SPEC_BOLSA_DESACTIVAR) {
                        out.println("formCurrent.cmb_ItemProductoOrigen.value = '" + objProductBeanTemp.getNpproductname() + "'");
                        out.println("formCurrent.cmb_ItemProductBolsaId.value =" + objProductBeanTemp.getNpproductid());
                        out.println("formCurrent.txt_ItemPriceCtaInscrip.value = '" + objProductBeanTemp.getNpcost() + "'");
                        return;
                    } else {
                        out.println("formCurrent.cmb_ItemProductoOrigen.value = '" + objProductBeanTemp.getNpproductname() + "'");
                        out.println("formCurrent.cmb_ItemProductBolsaId.value =" + objProductBeanTemp.getNpproductid());
                        out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProductoDestino);");
                        objArrayList = (ArrayList)objHashMap.get("objArrayList");
                        System.out.println("objArrayList.size() : " + objArrayList.size());
                    }

                }
            }else{ /*FIN ADT-BLC-083 --LHUAPAYA*/
                out.println("formCurrent = parent.mainFrame.frmdatos;");
                out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);");
                out.println("parent.mainFrame.vctProduct.removeElementAll();");
                objArrayList = (ArrayList)objHashMap.get("objArrayList");
                System.out.println("objArrayList.size() : " + objArrayList.size());
            }

            if ( objArrayList != null && objArrayList.size() > 0 ){

                for( int i=0; i<objArrayList.size();i++ ){
                    objProductBean = new ProductBean();
                    objProductBean = (ProductBean)objArrayList.get(i);
                /*INICIO ADT-BLC-083 --LHUAPAYA*/
                    if(categoriaIdBolsa == Constante.SPEC_BOLSA_UPGRADE || categoriaIdBolsa == Constante.SPEC_BOLSA_DOWNGRADE){
                        out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductoDestino,'"+objProductBean.getNpproductid()+"','"+MiUtil.getMessageClean(objProductBean.getNpproductname())+"')");
                    }else {/*FIN ADT-BLC-083 --LHUAPAYA*/
                        out.println("parent.mainFrame.vctProduct.addElement(new parent.mainFrame.fxMakeProduct('"+objProductBean.getNpproductid()+"','"+objProductBean.getNpminute()+"','"+objProductBean.getNpminuteprice()+"'))");
                        out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProducto,'"+objProductBean.getNpproductid()+"','"+MiUtil.getMessageClean(objProductBean.getNpproductname())+"')");
                    }
                }
            }
        }

    }
    /**
     * Motivo:  Método que se invoca para validar número de contrato
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales Crispin</a>
     * <br>Fecha: 06/11/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getPromotionList(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        logger.info("*************************** INICIO ItemServlet > getPromotionList***************************");
        PrintWriter out        = response.getWriter();
        NumberFormat formatter = new DecimalFormat("#0.00");
        String renta           = request.getParameter("renta")==null?"0.0":request.getParameter("renta");
        String price           = request.getParameter("price")==null?"0":request.getParameter("price");
        String currency        = MiUtil.getString((String)request.getParameter("currency"));
        String solutionId      = MiUtil.getString((String)request.getParameter("solutionId"));
        String promotionId     = MiUtil.getString((String)request.getParameter("promotionid"));
        String planId          = (String)request.getParameter("planId");

        String modelo          = (String)request.getParameter("paramModel");

        String customerId      = (String)request.getParameter("customerId");
        //Numero de Ocurrencia
        String ocurrence       = (String)request.getParameter("ocurrence");
        //Numero de Garantía
        String warrant         = (String)request.getParameter("warrant");
        //Devolución
        String devolut         = (String)request.getParameter("devolut");
        //Producto
        String productId       = request.getParameter("productId");
        //Producto Original
        String productOldId    = request.getParameter("origProductId");

        //Modalidad Nueva
        String modalityId      = request.getParameter("modalityId");
        //Modalidad Original
        String modalityOldId   = request.getParameter("modalityoldId");
        //Cantidad
        String quantityId      = request.getParameter("quantityId");
        //Especificación de la categoría
        String categoryId      = request.getParameter("specificationId");

        String flgSearch       = request.getParameter("flgSearch");
        String promotionValue  = request.getParameter("strPromotionValue");

        //site id
        String siteId          = (String)request.getParameter("siteId");

        //SalesStructureId
        //-----------------
        String salesSrtuctureOrigenId   = (String)request.getParameter("salesstructorigenId");

        //Acuerdos Comerciales
        String origPrice       = request.getParameter("origPrice")==null?"0.0":request.getParameter("origPrice");
        String priceType       = MiUtil.getString((String)request.getParameter("priceType"));
        String priceTypeId     = MiUtil.getString((String)request.getParameter("priceTypeId"));
        String phoneNumber     = MiUtil.getString((String)request.getParameter("phoneNumber"));
        String priceTypeItemId = MiUtil.getString((String)request.getParameter("priceTypeItemId"));

        if ( priceTypeId.equals("") ) priceTypeId = "0";
        if ( priceTypeItemId.equals("") ) priceTypeItemId = "0";
        if( renta.equals("") ) renta = "0.0";
        if( promotionId.equals("") ) promotionId = "0";

        String type_window          = (String)request.getParameter("type_window"); //Agregado por RAMN_09-11-2009
        String strflagvep = MiUtil.getString((String)request.getParameter("strflagvep"));
        String strnpnumcuotas = MiUtil.getString((String)request.getParameter("strnpnumcuotas"));
        String orderId = MiUtil.getString((String)request.getParameter("orderId"));


        logger.info("-------------------------------INICIO VALORES CAPTURADOS DE PANTALLA-------------------------------");
        logger.info("type_window            : " + type_window);
        logger.info("renta                  : " + renta);
        logger.info("price                  : " + price);
        logger.info("solutionId             : " + solutionId);
        logger.info("promotionId            : " + promotionId);
        logger.info("planId                 : " + planId);
        logger.info("modelo                 : " + modelo);
        logger.info("flgSearch              : " + flgSearch);
        logger.info("customerId             : " + customerId);
        logger.info("ocurrence              : " + ocurrence);
        logger.info("warrant                : " + warrant);
        logger.info("devolut                : " + devolut);
        logger.info("productId              : " + productId);
        logger.info("productOldId           : " + productOldId);
        logger.info("modalityId             : " + modalityId);
        logger.info("modalityOldId          : " + modalityOldId);
        logger.info("quantityId             : " + quantityId);
        logger.info("origPrice              : " + origPrice);
        logger.info("priceType              : " + priceType);
        logger.info("priceTypeId            : " + priceTypeId);
        logger.info("currency               : " + currency);
        logger.info("priceTypeItemId        : " + priceTypeItemId);
        logger.info("strflagvep             : " + strflagvep);
        logger.info("strnpnumcuotas         : " + strnpnumcuotas);
        logger.info("phoneNumber            : " + phoneNumber);
        logger.info("salesSrtuctureOrigenId : " + salesSrtuctureOrigenId);
        logger.info("siteId                 : " + siteId);
        logger.info("categoryId :           " + categoryId);
        logger.info("promotionValue         : " + promotionValue);
        logger.info("orderId                : " + orderId);
        logger.info("-------------------------------FIN VALORES CAPTURADOS DE PANTALLA-------------------------------");

        NewOrderService objNewOrderService = new NewOrderService();
        ProductBean objProductBean = new ProductBean();
        ProductPriceBean objProductPriceBean = new ProductPriceBean();
        objProductBean.setNpcustomerid(MiUtil.parseLong(customerId));
        objProductBean.setNpproductid_old(MiUtil.parseLong(productOldId));
        objProductBean.setNpproductid_new(MiUtil.parseLong(productId));
        objProductBean.setNpmodality_new(modalityId);
        objProductBean.setNpmodality_old(modalityOldId);
        objProductBean.setNpcategoryid(MiUtil.parseLong(categoryId));
        objProductBean.setNpquantity(quantityId);
        objProductBean.setNpsolutionid(MiUtil.parseLong(solutionId));
        objProductBean.setNpmodel(modelo);
        objProductBean.setSalesStructureOriginalId(MiUtil.parseLong(salesSrtuctureOrigenId));

        if( planId != null )
            objProductBean.setNpplanid(MiUtil.parseLong(planId));

        if( ocurrence != null )
            objProductBean.setNpoccurrence(MiUtil.parseInt(ocurrence));
        else
            objProductBean.setNpoccurrence(-1);

        objProductBean.setNpflg_garanty(warrant);
        objProductBean.setNpflg_return(devolut);
        //se envia el siteId
        objProductBean.setNpsiteid(MiUtil.parseLong(siteId));
        objProductBean.setNpphonenumber(phoneNumber);

        objProductBean.setNpflagvep(MiUtil.parseInt(strflagvep));
        objProductBean.setNpnumcuotas(MiUtil.parseInt(strnpnumcuotas));
        objProductBean.setOrderId(orderId);

        logger.info("objProductBean.toString()      : " + objProductBean.toString());
        HashMap objHashMap = objNewOrderService.OrderDAOgetProductPriceType(objProductBean);
        ArrayList objArrayList = new ArrayList();
        objArrayList = (ArrayList)objHashMap.get("objArrayList");
        if(objArrayList.size()==0){
            //SAR 0037-170378 INI
            objHashMap.put("strMessage","No existe un precio configurado para este producto, consúltelo con Marketing.");
            //SAR 0037-170378 FIN
        }
        out.println("<script>");
        if( (String)objHashMap.get("strMessage")!= null ){
            out.println("parent.mainFrame.deleteAllValues(parent.mainFrame.frmdatos.cmb_ItemPromocion);");
            out.println("parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemPromocion,'','')");
            String variable = MiUtil.getMessageClean((String)objHashMap.get("strMessage"));
            out.println("alert('"+variable+"')");
        }else{
            out.println("parent.mainFrame.deleteAllValues(parent.mainFrame.frmdatos.cmb_ItemPromocion);");
            out.println("parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemPromocion,'','')");
            if ( objArrayList != null && objArrayList.size() > 0 ){
                out.println("formCurrent = parent.mainFrame.frmdatos;");

                if(type_window!=null && type_window.equals("DETAIL")){
                    out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'|"+ currency + "|" + MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price))) + "|" + MiUtil.parseDouble(renta) + "|" + priceType+ "|" +priceTypeId+ "|" +MiUtil.getString(""+formatter.format(MiUtil.parseDouble(origPrice)))+ "|" +priceTypeItemId +"','"+currency+" - "+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price)))+" - "+priceType+" - "+priceTypeId+"')");
                    logger.info("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'|"+ currency + "|" + MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price))) + "|" + MiUtil.parseDouble(renta) + "|" + priceType+ "|" +priceTypeId+ "|" +MiUtil.getString(""+formatter.format(MiUtil.parseDouble(origPrice)))+ "|" +priceTypeItemId +"','"+currency+" - "+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price)))+" - "+priceType+" - "+priceTypeId+"')");
                }else{
                    for( int i=0; i<objArrayList.size();i++ ){

                        objProductPriceBean = new ProductPriceBean();
                        objProductPriceBean =(ProductPriceBean)objArrayList.get(i);
                        out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'|"+ objProductPriceBean.getNpcurrency() + "|" + formatter.format(objProductPriceBean.getNppriceonetime()) + "|" + objProductPriceBean.getNppricerecurring() + "|" + objProductPriceBean.getNpobjecttype()+ "|" +objProductPriceBean.getNpobjectid()+ "|" +formatter.format(objProductPriceBean.getNporiginalprice())+ "|" +objProductPriceBean.getNpobjectitemid() +"','"+objProductPriceBean.getNpcurrency()+" - "+MiUtil.getString(""+formatter.format(objProductPriceBean.getNppriceonetime()))+" - "+MiUtil.getString(objProductPriceBean.getNpobjecttype())+" - "+objProductPriceBean.getNpobjectid()+"')");
                        logger.info("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPromocion,'|"+ objProductPriceBean.getNpcurrency() + "|" + formatter.format(objProductPriceBean.getNppriceonetime()) + "|" + objProductPriceBean.getNppricerecurring() + "|" + objProductPriceBean.getNpobjecttype()+ "|" +objProductPriceBean.getNpobjectid()+ "|" +formatter.format(objProductPriceBean.getNporiginalprice())+ "|" +objProductPriceBean.getNpobjectitemid() +"','"+objProductPriceBean.getNpcurrency()+" - "+MiUtil.getString(""+formatter.format(objProductPriceBean.getNppriceonetime()))+" - "+MiUtil.getString(objProductPriceBean.getNpobjecttype())+" - "+objProductPriceBean.getNpobjectid()+"')");
                    }
                }
                if( flgSearch != null )

                    if (solutionId.equalsIgnoreCase(MiUtil.getString(Constante.KN_TELEFONIA_FIJA))){
                        out.println(" if (formCurrent.cmb_ItemPromocion.length > 1) formCurrent.cmb_ItemPromocion.selectedIndex = 1;  ");

                    }else{
                        out.println("formCurrent.cmb_ItemPromocion.value = '|"+currency+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price)))+"|"+MiUtil.parseDouble(renta)+"|"+priceType+"|"+priceTypeId+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(origPrice)))+"|"+priceTypeItemId+"';");
                    }
                logger.info("formCurrent.cmb_ItemPromocion.value = '|"+currency+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(price)))+"|"+MiUtil.parseDouble(renta)+"|"+priceType+"|"+priceTypeId+"|"+MiUtil.getString(""+formatter.format(MiUtil.parseDouble(origPrice)))+"|"+priceTypeItemId+"';");
            }
        }
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
        out.println("</script>");
        logger.info("*************************** FIN ItemServlet > getPromotionList***************************");
    }

    /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/items");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que selecciona adendas para un plan y/o promoción.
     * <br>Realizado por: <a href="mailto:jose.espinoza@nextel.com.pe">JOSÉ MARÍA ESPINOZA B.</a>
     * <br>Fecha: 24/10/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doListarPlantillasNew(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        try {
            System.out.println("[ItemServlet.doListarPlantillasNew] ");

            //Variable que controla la Transaction del listado de las plantillas
            int iUserId         = 0;
            int iAppId          = 0;
            int iFlagCarrier    = 0;
            int idProm          = 0;
            int idPlan          = 0;
            int idSpecification = 0;
            int idKit           = 0;
            String strSessionId     = null;
            String hdnSpecification = null;
            String strMessage       = null;
            String cmbPromocion     = null;
            String cmbPlan          = null;
            String cmbKit           = null;


            strSessionId     = (String)request.getParameter("strSessionId");
            hdnSpecification = (String)request.getParameter("hdnSpecification");
            cmbPromocion     = (String)request.getParameter("cmbPromocion");
            cmbPlan          = (String)request.getParameter("cmbPlan");
            cmbKit           = (String)request.getParameter("cmbKit");

            // Se obtiene el id de la aplicación y del usuario
            PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
            iUserId = objSessionBean.getUserid();
            iAppId  = objSessionBean.getAppId();

            // Obtiene si el usuario puede editar el plazo de la plantilla
            HashMap hshScreenOptions = new HashMap();
            hshScreenOptions = objGeneralService.getRol(Constante.SCRN_OPTTO_ADDENDUM_TERM, iUserId, iAppId);
            strMessage = (String)hshScreenOptions.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);

            //System.out.println("[ItemServlet.doListarPlantillasNew] strMessage =  "+ strMessage);

            // Si iFlagCarrier > 0 entonces puede editar el plazo de la plantilla.
            iFlagCarrier     = MiUtil.parseInt((String)hshScreenOptions.get("iRetorno"));

            idProm          = MiUtil.parseInt(cmbPromocion);
            idPlan          = MiUtil.parseInt(cmbPlan);
            idSpecification = MiUtil.parseInt(hdnSpecification);
            idKit           = MiUtil.parseInt(cmbKit);
         
         /*System.out.println("[ItemServlet.doListarPlantillasNew] ");
         System.out.println("[ItemServlet.doListarPlantillasNew] idProm = "+idProm);
         System.out.println("[ItemServlet.doListarPlantillasNew] idPlan = "+idPlan);
         System.out.println("[ItemServlet.doListarPlantillasNew] idSpecification = "+idSpecification);*/

            HashMap objHashMap                    = new HashMap();
            ItemService objItemServiceTransaction = new ItemService();
            ArrayList list                        = new ArrayList();

            strMessage = null;

            // Obtiene la lista de plantillas asociadas al plan y a la promoción
            objHashMap = objItemServiceTransaction.OrderDAOgetAddendasList( idProm, idPlan, idSpecification, idKit) ;
            strMessage = (String) objHashMap.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);

            list = (ArrayList)objHashMap.get("objArrayList");

            request.setAttribute("tblListAdendum", list);
            request.setAttribute("iFlagCarrier", iFlagCarrier+"");
            request.setAttribute("hdnSpecification", hdnSpecification);
            request.getRequestDispatcher("pages/loadTemplateAdendum.jsp").forward(request, response);

        }catch (Exception e) {
            logger.error(formatException(e));
            PrintWriter out = response.getWriter();
            MiUtil.printErrorMessage(e.getMessage(), out);
        }
    }

    /**
     * Motivo:  Método que Lista las plantillas para que sean editadas.
     * <br>Realizado por: <a href="mailto:jose.espinoza@nextel.com.pe">JOSÉ MARÍA ESPINOZA B.</a>
     * <br>Fecha: 24/10/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void doListarPlantillasEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        try {
            //Variable que controla la Transaction del listado de las plantillas
            ItemService objItemServiceTransaction = new ItemService();
            String detail = "";
            String id_prom ="", id_plan = "",cadena = "";
            String id_kit = "";
            String type_window = (String)request.getParameter("type_window");
            String hdnSpecification;

            id_prom = request.getParameter("cmbPromocion");
            id_plan = request.getParameter("cmbPlan");
            id_kit  = request.getParameter("cmbKit");
            cadena = request.getParameter("cadena");
            hdnSpecification = (String)request.getParameter("hdnSpecification");

            if (("").equals(cadena)){
                detail = "DETAIL";
                String id_order = "", id_item = "";

                id_order = request.getParameter("id_order");
                id_item  = request.getParameter("id_item");

                int idOrder = MiUtil.parseInt(id_order);
                int idItem  = MiUtil.parseInt(id_item);

                HashMap objHashMapTemplOrder = new HashMap();
                objHashMapTemplOrder         = objItemServiceTransaction.OrderDAOgetTemplateOrder(idOrder, idItem);
                ArrayList listTemplOrder     = new ArrayList();
                listTemplOrder               = (ArrayList)objHashMapTemplOrder.get("objArrayList");
                String strMessageTemplOrder  = "";
                strMessageTemplOrder = (String) objHashMapTemplOrder.get("strMessage");
                if (strMessageTemplOrder!=null)
                    throw new Exception(strMessageTemplOrder);

                TemplateAdendumBean templateAdendumBean = null;
                for(int i = 0; i < listTemplOrder.size(); i++){
                    templateAdendumBean = new TemplateAdendumBean();
                    templateAdendumBean = (TemplateAdendumBean)listTemplOrder.get(i);
                    cadena = cadena + templateAdendumBean.getNptemplateid()+"-"+templateAdendumBean.getNpaddendumterm()+";";
                }
            }

            String [] cadenas = cadena.split(";",cadena.split(";").length);

            int idProm = MiUtil.parseInt(id_prom);
            int idPlan = MiUtil.parseInt(id_plan);
            int idSpecification = MiUtil.parseInt(hdnSpecification);
            int idkit = MiUtil.parseInt(id_kit);

            HashMap objHashMap = new HashMap();
            objHashMap = objItemServiceTransaction.OrderDAOgetAddendasList( idProm, idPlan, idSpecification, idkit) ;

            String strMessage = "";
            ArrayList list    = new ArrayList();

            strMessage        = (String) objHashMap.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);

            list = (ArrayList)objHashMap.get("objArrayList");
            TemplateAdendumBean templateAdendumBean = null;

            ArrayList listEdit = new ArrayList();
            for(int i=0;i < list.size();i++ ){
                templateAdendumBean = new TemplateAdendumBean();
                templateAdendumBean = (TemplateAdendumBean)list.get(i);

                for(int j=0 ;j < cadenas.length ; j++){
                    String [] template = cadenas[j].split("-",cadenas[j].split("-").length);
                    template[1] = template[1].replaceAll(";","");

                    if(templateAdendumBean.getNptemplateid() == MiUtil.parseInt(template[0])){
                        templateAdendumBean.setNpaddendumterm(MiUtil.parseInt(template[1]));
                        templateAdendumBean.setNptemplatedefa("S");
                    }
                }
                listEdit.add(templateAdendumBean);
            }

            request.setAttribute("tblListAdendum", listEdit);

            if(("DETAIL").equals(type_window)){
                request.getRequestDispatcher("pages/loadTemplateAdendumDetail.jsp").forward(request, response);
            }else{
                request.getRequestDispatcher("pages/loadTemplateAdendum.jsp").forward(request, response);
            }

        }catch (Exception e) {
            logger.error(formatException(e));
            PrintWriter out = response.getWriter();
            MiUtil.printErrorMessage(e.getMessage(), out);
        }
    }

    /**
     * Motivo:  Método que obtiene la cantidad de adendas activas.
     * <br>Realizado por: <a href="mailto:jose.espinoza@nextel.com.pe">JOSÉ MARÍA ESPINOZA B.</a>
     * <br>Fecha: 24/10/2007        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
 public void doGetNumAddendumAct(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        System.out.println("[ItemServlet.java.doGetNumAddendumAct]: Ejecutando este metodo");

        String  id_customer ="", num_nextel = "", specification_id = "";

        id_customer = request.getParameter("id_customer");
        num_nextel  = request.getParameter("num_nextel");
        specification_id = request.getParameter("id_specification");

        System.out.println("[ItemServlet.java.doGetNumAddendumAct]: id_customer: "+ id_customer+" num_nextel: "+num_nextel+" specification_id: "+specification_id);

        num_nextel  = num_nextel.trim();

        int idCustomer = MiUtil.parseInt(id_customer);

        HashMap objHashMap = new HashMap();
        ItemService objItemServiceTransaction = new ItemService();
        PenaltyService obj = new PenaltyService();
        Constante constante = new Constante();

        objHashMap = obj.getConfigurationList(constante.SPECIFICATION_INBOX_PENALTY);
        ArrayList<ListBoxBean> listBoxList = new ArrayList<ListBoxBean>();
        listBoxList = (ArrayList)objHashMap.get("listBoxList");
        if(listBoxList.isEmpty()){
            System.out.println("[ItemServlet.java.doGetNumAddendumAct]: empty: ");
            objHashMap = objItemServiceTransaction.OrderDAOgetNumAddendumAct(idCustomer, num_nextel);
            System.out.println("[ItemServlet.java.doGetNumAddendumAct]: Specificacion distinta a 2065 o 2009");
        }else{
            System.out.println("[ItemServlet.java.doGetNumAddendumAct]: no empty: ");
            System.out.println("[ItemServlet.java.doGetNumAddendumAct]: Specificacion igual a 2065 o 2009");
            objHashMap = objItemServiceTransaction.OrderDAOgetNumAddendumActSpec(idCustomer, num_nextel, specification_id);
        }
/*
        if(specification_id.equals("2065") || specification_id.equals("2009")){
                    System.out.println("[ItemServlet.java.doGetNumAddendumAct]: Specificacion igual a 2065 o 2009");
            objHashMap = objItemServiceTransaction.OrderDAOgetNumAddendumActSpec(idCustomer, num_nextel, specification_id);
        }else{
                    objHashMap = objItemServiceTransaction.OrderDAOgetNumAddendumAct(idCustomer, num_nextel);
            System.out.println("[ItemServlet.java.doGetNumAddendumAct]: Specificacion distinta a 2065 o 2009");
        }
        */

        String strMessage = "";
        String numAddenAct = "";
        //EFLORES REQ-0428_2 22/08/2016 Se agrega orden para auditoria
        String strOrderId = "";

        strMessage  = (String) objHashMap.get("strMessage");
        numAddenAct = (String)objHashMap.get("numAddenAct");
        if(strMessage==null){
            request.setAttribute("numAddenAct", numAddenAct);
            request.setAttribute("strOrderId",request.getParameter("order_id"));
            request.setAttribute("strNextelNum",request.getParameter("num_nextel"));
            request.getRequestDispatcher("pages/messageNumAddenAct.jsp").forward(request, response);
        }
    }
    /**
     * Motivo:  Método que valida que el Imei Exista y este disponible
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 15/01/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doValidateImei(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap hshItemDeviceMap = new HashMap();
        hshItemDeviceMap.put("imei", request.getParameter("imei"));
        hshItemDeviceMap.put("lugarDespacho", request.getParameter("lugarDespacho"));
        hshItemDeviceMap.put("producto", request.getParameter("producto"));
        hshItemDeviceMap.put("modalidad", request.getParameter("modalidad"));
        hshItemDeviceMap.put("subCategoria", request.getParameter("subCategoria"));
        hshItemDeviceMap.put("garantia", request.getParameter("garantia"));
        hshItemDeviceMap.put("tipoSalida", request.getParameter("tipoSalida"));
        //if (logger.isDebugEnabled())
        //logger.debug("hshItemDeviceMap:::" + hshItemDeviceMap);
        try {
            ItemService objItemService = new ItemService();
            /*JPEREZ: ya no se validan IMEIs al agregarlos sino al grabar la orden*/
            //HashMap hshDataMap = objItemService.doValidateIMEI(hshItemDeviceMap);
            HashMap hshDataMap = new HashMap();
            hshDataMap.put("strType", "IMEI");
            hshDataMap.put("strType", "IMEI");
            hshDataMap.put("strSubCategoria", request.getParameter("subCategoria"));
            hshDataMap.put(Constante.MESSAGE_OUTPUT, null);

            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled())
                //logger.debug("Antes de hacer el getRequestDispatcher");
                request.getRequestDispatcher("pages/validateItemDevice.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que genera la lista de IMEIS al agregarse un Item
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGetImeisByProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long   lngProductId   =   MiUtil.parseLong((String)request.getParameter("strProductId"));
        String strModalityId  =   MiUtil.getString(request.getParameter("intModalityID"));
        long  lngSalesStructOrigenId   =   MiUtil.parseLong((String)request.getParameter("strSalesStuctOrigenId"));


        HashMap hshItemDeviceMap = new HashMap();
        HashMap hshKitMap = new HashMap();

        hshItemDeviceMap = objGeneralService.getKitDetail(lngProductId,strModalityId,lngSalesStructOrigenId);
        //hshItemDeviceMap = objGeneralService.getKitDetail(4050,"20");

        request.removeAttribute("hshItemDeviceMap");
        request.setAttribute("hshItemDeviceMap",hshItemDeviceMap);
        request.getRequestDispatcher("pages/loadPaintImeis.jsp").forward(request, response);
    }

    /**
     * Motivo:  Método que genera la lista de IMEIS al agregarse un Item
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     **/
    public void doGetImeisByProductEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long   lngProductId   =   MiUtil.parseLong((String)request.getParameter("strProductId"));
        String strModalityId  =   MiUtil.getString(request.getParameter("intModalityID"));
        long  lngSalesStructOrigenId   =   MiUtil.parseLong((String)request.getParameter("strSalesStuctOrigenId"));


        HashMap hshItemDeviceMap = new HashMap();
        HashMap hshKitMap = new HashMap();

        hshItemDeviceMap = objGeneralService.getKitDetail(lngProductId,strModalityId,lngSalesStructOrigenId);
        //hshItemDeviceMap = objGeneralService.getKitDetail(4050,"20");

        request.removeAttribute("hshItemDeviceMap");
        request.setAttribute("hshItemDeviceMap",hshItemDeviceMap);
        request.getRequestDispatcher("pages/loadPaintImeisEdit.jsp").forward(request, response);
    }

    /**
     * Motivo:  Método que genera la lista de IMEIS al agregarse un Item
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     **/
    public void doGetContractAssociates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long   lnCustomerId   =   MiUtil.parseLong((String)request.getParameter("strCustomerId"));
        long   lngAddressId   =   MiUtil.parseLong((String)request.getParameter("strAddressId"));

        HashMap hshItemDeviceMap = new HashMap();
        HashMap hshKitMap = new HashMap();

        //hshItemDeviceMap = objGeneralService.getKitDetail(lngProductId,strModality);
        //hshItemDeviceMap = objGeneralService.getKitDetail(4050,"20");

        //request.setAttribute("hshItemDeviceMap",hshItemDeviceMap);
        request.getRequestDispatcher("pages/loadPaintContractAssociate.jsp").forward(request, response);
    }

    /**
     * Motivo:  Obtiene un mensaje de stock para un ítem y especificación
     * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br/>Fecha: 05/03/2008
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getStockMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NewOrderService objNewOrderService = new NewOrderService();
        String strMode         = (String)request.getParameter("strMode");
        int iSpecificationId   = MiUtil.parseInt((String)request.getParameter("strSpecificationId"));
        long lProductId        = MiUtil.parseLong((String)request.getParameter("strProductId"));
        int iDispatchPlace     = MiUtil.parseInt((String)request.getParameter("strDispatchPlace"));
        String strSaleModality = (String)request.getParameter("strSaleModality");
        long lSalesStructOrigenId = MiUtil.parseLong((String)request.getParameter("strSalesStuctOrigenId"));
        String strTipo         = (String)request.getParameter("strTipo");

        PrintWriter out = response.getWriter();

        String strFlagStock = "";
        String strMensajeStock = "";
        String strMensaje = "";

        HashMap hshstockMessage = new HashMap();
        hshstockMessage = objNewOrderService.getStockMessage(iSpecificationId, lProductId, iDispatchPlace, strSaleModality,lSalesStructOrigenId,strTipo);
        if (hshstockMessage!=null && hshstockMessage.size() > 0){
            strFlagStock = (String)hshstockMessage.get("wv_flag_stock");
            strMensajeStock = (String)hshstockMessage.get("wv_message_stock");
            strMensaje = (String)hshstockMessage.get("wv_message");
            out.println("<script>");
            out.println("</script>");
            if (strMensaje != null){
                System.out.println("getValidateStock ==="+strMensaje);
                out.println("<script>");
                out.println("alert('"+strMensaje+"')");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");

            }else{
                out.println("<script>");
                out.println("parent.mainFrame.fxStockResponse('"+strFlagStock+"','"+strMensajeStock+"',"+strMode+")");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");
            }

        }
    }



    /**
     * Motivo:  Obtiene un mensaje de stock para un ítem y especificación y después realiza la validación de compatibilidad Modelo-Plan-Servicio
     * <br/>Realizado por: <a href="mailto:ezubiaurr@hp.com">Enrique Zubiaurr</a>
     * <br/>Fecha: 21/12/2010
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getStockMessage2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NewOrderService objNewOrderService = new NewOrderService();
        String strMode         = (String)request.getParameter("strMode");
        int iSpecificationId   = MiUtil.parseInt((String)request.getParameter("strSpecificationId"));
        long lProductId        = MiUtil.parseLong((String)request.getParameter("strProductId"));
        int iDispatchPlace     = MiUtil.parseInt((String)request.getParameter("strDispatchPlace"));
        String strSaleModality = (String)request.getParameter("strSaleModality");
        long lSalesStructOrigenId = MiUtil.parseLong((String)request.getParameter("strSalesStuctOrigenId"));
        String strTipo         = (String)request.getParameter("strTipo");
        // INI EZM Compatibilidad M-P-S
        String strServicesSelectedList         = (String)request.getParameter("strServices");
        String strDescServicesSelectedList         = (String)request.getParameter("strServicesDesc");
        String strProduct         = (String)request.getParameter("strProduct");
        String strPlanId         = (String)request.getParameter("strPlanId");
        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String messageDesc = "";
        String messageFinal="";

        // FIN EZM Compatibilidad M-P-S

        //Flag que indica si se debe realizar la validacion de la compatibilidad MPS - FPICOY 07/09/2011
        String strflagValidCompMPS  = request.getParameter("strflagValidCompMPS")!=null?(String)request.getParameter("strflagValidCompMPS"):"";

        PrintWriter out = response.getWriter();

        String strFlagStock = "";
        String strMensajeStock = "";
        String strMensaje = "";

        HashMap hshstockMessage = new HashMap();
        hshstockMessage = objNewOrderService.getStockMessage(iSpecificationId, lProductId, iDispatchPlace, strSaleModality,lSalesStructOrigenId,strTipo);
        System.out.println("El flag de validacion de compatibilidad es --->" + strflagValidCompMPS);
        if ("A".equals(strflagValidCompMPS)) {
            messageFinal=getValidServSelectedData(strServicesSelectedList,strDescServicesSelectedList,strProduct,strPlanId,strMode,strSpecificationId);//EZM Compatibilidad M-P-S
        }
        if (hshstockMessage!=null && hshstockMessage.size() > 0){
            strFlagStock = (String)hshstockMessage.get("wv_flag_stock");
            strMensajeStock = (String)hshstockMessage.get("wv_message_stock");
            strMensaje = (String)hshstockMessage.get("wv_message");
            out.println("<script>");
            out.println("</script>");
            if (strMensaje != null){
                System.out.println("getValidateStock ==="+strMensaje);
                out.println("<script>");
                if ((!"".equals(messageFinal))){
                    out.println("alert('"+messageFinal+"');");} //EZM Compatibilidad M-P-S
                out.println("alert('"+strMensaje+"')");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");

            }else{
                out.println("<script>");
                if ((!"".equals(messageFinal))){
                    out.println("alert('"+messageFinal+"');");}  //EZM Compatibilidad M-P-S
                out.println("parent.mainFrame.fxStockResponse('"+strFlagStock+"','"+strMensajeStock+"',"+strMode+")");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");
            }

        }
    }


    /**
     * Motivo:  Obtiene un mensaje indicando que el sevicio se le aplica comision
     * <br/>Realizado por: <a href="mailto:ruth.polo@nextel.com.pe">Ruth Polo</a>
     * <br/>Fecha: 13/01/2009
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getComissionMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String          strSaleModality     = (String)request.getParameter("strServiceId");
        NewOrderService objNewOrderService  = new NewOrderService();
        Vector          la_services         = new Vector();
        String          strMensaje, strComission;
        PrintWriter     out = response.getWriter();
        StringTokenizer tokens  = new StringTokenizer(strSaleModality,"|");
        int             intDivisionId;
        HashMap         hshcomissionMessage = new HashMap();
        String          strMensajeConfirmation ="Al servicio seleccionado se le aplica comisión, verifique el vendedor!!!";
        int             aux_divisionid;

        tokens.countTokens();
        System.out.println("getComissionMessage");
        while(tokens.hasMoreTokens()){
            aux_divisionid = Integer.parseInt((String)tokens.nextToken());
            String aux_1 = tokens.nextToken();
            String aux_2 = tokens.nextToken();

            System.out.println("aux_1="+aux_1+"aux_2="+aux_2+"aux_divisionid="+aux_divisionid);
            if (aux_1.equals("N") && aux_2.equals("S")){//se verifica si tiene comission
                intDivisionId =aux_divisionid;
                hshcomissionMessage = objNewOrderService.getComissionMessage(intDivisionId);
                if (hshcomissionMessage!=null && hshcomissionMessage.size() > 0){
                    strComission = (String)hshcomissionMessage.get("wv_npcommission");
                    System.out.println("strComission="+strComission);

                    strMensaje = (String)hshcomissionMessage.get("wv_message");
                    out.println("<script>");
                    out.println("</script>");

                    System.out.println("getComissionMessage ==="+strMensaje);

                    if (strMensaje != null){
                        System.out.println("getComissionMessage ==="+strMensaje);
                        out.println("<script>");
                        out.println("alert('"+strMensaje+"')");
                        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                        out.println("</script>");

                    }else
                    if (strComission.equals("S")){
                        out.println("<script>");
                        out.println("alert('"+strMensajeConfirmation+"')");
                        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                        out.println("</script>");
                    }
                }
            }
        }
    }



    /**
     * Motivo:  Método que se invoca para obtener el id de la dirección
     * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
     * <br>Fecha: 05/03/2008        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getNextAddressList(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        PrintWriter out  = response.getWriter();
        AddressObjectBean objAddressObjectBean = new AddressObjectBean();
        String strAddressId        = request.getParameter("strAddressId");
        objAddressObjectBean.setAddressId(MiUtil.parseLong(strAddressId));
        out.println("<script>");
        loadNextAddressList(objAddressObjectBean,out);
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
        out.println("</script>");

    }

    /**
     * Motivo:  Método que se invoca para obtener el listado de las direcciones de destino
     * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
     * <br>Fecha: 05/03/2008        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void loadNextAddressList(AddressObjectBean objAddressObjectBean,PrintWriter out){
        CustomerService objCustomerService = new CustomerService();
        HashMap objHashMap = objCustomerService.getDestinyAddress(objAddressObjectBean.getAddressId());

        ArrayList objArrayList = new ArrayList();

        if( (String)objHashMap.get("strMessage")!= null ){
            String variable = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
            out.println("alert('"+variable+"')");
        }else{
            out.println("formCurrent = parent.mainFrame.frmdatos;");
            out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemDestinyAddress);");
            objArrayList = (ArrayList)objHashMap.get("objAddressObjectlist");
            System.out.println("objArrayList.size() : " + objArrayList.size());
            if ( objArrayList != null && objArrayList.size() > 0 ){
                for( int i=0; i<objArrayList.size();i++ ){
                    objAddressObjectBean = new AddressObjectBean();
                    objAddressObjectBean = (AddressObjectBean)objArrayList.get(i);
                    out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemDestinyAddress,'"+objAddressObjectBean.getAddressId()+"','"+MiUtil.getString(objAddressObjectBean.getSwaddress1())+"')");
                }
            }
        }
    }

    /**
     * Motivo:  Método que valida un SIM
     * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br>Fecha: 08/03/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doValidateSim(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strSIM = request.getParameter("strSIM");
        try {
            ItemService objItemService = new ItemService();
            /*JPEREZ: ya no se validan SIMSs al agregarlos sino al grabar la orden*/
            //HashMap hshDataMap = objItemService.doValidateSIM(strSIM);
            HashMap hshDataMap = new HashMap();
            hshDataMap.put("strType", "SIM");
            hshDataMap.put(Constante.MESSAGE_OUTPUT, null);
            request.setAttribute(Constante.DATA_STRUCT, hshDataMap);
            if(logger.isDebugEnabled())
                request.getRequestDispatcher("pages/validateItemDevice.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que verifica si la Plantilla es Nueva o de Renovación
     * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
     * <br>Fecha: 25/04/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getTipoPlantillaAdenda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ItemService objItemService = new ItemService();
            String strMessage = "",strFlagTipoAdend = "";
            String strNumeroNextel=(request.getParameter("strNumeroNextel")==null?"0":request.getParameter("strNumeroNextel"));
            int iTemplateId=(request.getParameter("iTemplateId")==null?0:MiUtil.parseInt(request.getParameter("iTemplateId")));
            String txtToSet = (String)request.getParameter("txtToSet");
            HashMap hshDataMap = objItemService.getTipoPlantillaAdenda(strNumeroNextel,iTemplateId);
            strMessage  =  (String)hshDataMap.get("strMessage");
            strFlagTipoAdend = (String)hshDataMap.get("strFlagTipoAdend");
            request.setAttribute("strFlagTipoAdend", strFlagTipoAdend);
            request.setAttribute("txtToSet",txtToSet);
            if(logger.isDebugEnabled())
                request.getRequestDispatcher("pages/loadTipoPlantilla.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * Motivo:  Método que realiza la búsqueda de producto de acuerdo a filtros de búsqueda.
     * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
     * <br>Fecha: 06/05/2008
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getLoadProductoWhitFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ItemService objItemService = new ItemService();
            NewOrderService objNewOrderService = new NewOrderService();
            ArrayList objArrayList = new ArrayList();
            String strMessage = "",strFlagTipoAdend = "";
            String itemModalidad=(request.getParameter("itemModalidad")==null?"":request.getParameter("itemModalidad"));
            String lineProduct=(request.getParameter("lineProduct")==null?"":request.getParameter("lineProduct"));
            String productId=(request.getParameter("productId")==null?"0":request.getParameter("productId"));
            String productDesc=(request.getParameter("productDesc")==null?"":request.getParameter("productDesc"));
            String specificationId=(request.getParameter("specificationId")==null?"":request.getParameter("specificationId"));

            ProductBean objProductBean = new ProductBean();

            objProductBean.setNpcategoryid(MiUtil.parseLong(specificationId));
            objProductBean.setNpproductlineid(MiUtil.parseLong(lineProduct));
            objProductBean.setNpmodality(itemModalidad);
            objProductBean.setNpinventorycode(productId);
            objProductBean.setNpproductname(productDesc);

            HashMap objHashMap = objNewOrderService.getProductType(objProductBean);
            if( objHashMap == null )
                throw new Exception("Surgieron errores al cargar los productos");
            else if( (String)objHashMap.get("strMessage")!= null )
                throw new Exception(MiUtil.getMessageClean((String)objHashMap.get("strMessage")));

            objArrayList = (ArrayList)objHashMap.get("objArrayList");

            if (objArrayList != null){
                request.setAttribute("listProduct",objArrayList);
            }

            if(logger.isDebugEnabled())
                request.getRequestDispatcher("pages/loadComboProductoWhitFilter.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    public void doValidateImeiSimMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            ItemService objItemService = new ItemService();
            String strImei  = (request.getParameter("imei")==null?"":request.getParameter("imei"));
            String strSim   = (request.getParameter("sim")==null?"":request.getParameter("sim"));
            String strSpecification = (request.getParameter("specificationID")==null?"":request.getParameter("specificationID"));
            String strPosition      = (request.getParameter("position")==null?"":request.getParameter("position"));
            String strType          = (request.getParameter("strType")==null?"":request.getParameter("strType"));
      /*JPEREZ: ya no se valida  que el IMEI y el SIM coincidan al agregarlos sino al grabar la orden*/
            //HashMap objHashMap  = objItemService.getImeiSimMatch( MiUtil.parseLong(strSpecification), strImei, strSim  );
            HashMap objHashMap  = new HashMap();
            objHashMap.put("strImei",strImei);
            objHashMap.put("strSim",strSim);
            objHashMap.put("strPosition",strPosition);
            objHashMap.put("strType",strType);
            request.setAttribute(Constante.DATA_STRUCT, objHashMap);
            if(logger.isDebugEnabled())
                request.getRequestDispatcher("pages/validateImeiSimMatch.jsp").forward(request, response);
        }catch(Exception e) {
            logger.error(formatException(e));

        }
    }

    /**
     * Motivo:  Método que obtiene la lista de los planes tarifarios y servicios cuando se carga la solución
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 04/05/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException*/

    public void getSoluctionPlanTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException,IOException {
        logger.info("******************************INICIO ItemServelt > getSoluctionPlanTypeList ******************************");
        String strSolutionId       = (String)request.getParameter("strSolutionId");
        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String strTypeCompany      = (String)request.getParameter("strTypeCompany");
        String strCustomerId       = (String)request.getParameter("strCustomerId");
        String strSessionId        = (String)request.getParameter("strSessionId");
        String service             = (String)request.getParameter("service");
        String plan                = (String)request.getParameter("plan");
        String linea               = (String)request.getParameter("linea");
        String respPago            = (String)request.getParameter("respPago");
        String strType             = null;
        String strMessage          = null;
        String strnpSite           = (String)request.getParameter("strnpSite");
        String strSSAAType  = null; //johncmb

        PrintWriter out  = response.getWriter();
        PlanTarifarioBean objPlanBean    = new PlanTarifarioBean();
        NewOrderService objNewOrderService = new NewOrderService();
        GeneralService objGeneralService= new GeneralService();
        CustomerService objCustomerService = new CustomerService();
        ArrayList objArrayListDefault = new ArrayList();


        logger.info("[ItemServlet][getSoluctionPlanTypeList][strSolutionId]"+strSolutionId);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strSpecificationId]"+strSpecificationId);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strTypeCompany]"+strTypeCompany);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strCustomerId]"+strCustomerId);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strSessionId]"+strSessionId);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][service]"+service);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strSessionId]"+strSessionId);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][plan]"+plan);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][linea]"+linea);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][respPago]"+respPago);
        logger.info("[ItemServlet][getSoluctionPlanTypeList][strnpSite]"+strnpSite);

        try{

            if (MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_DES_SERVICIOS_BA){
                strType = "A";
            }
            logger.info("[ItemServlet][getSoluctionPlanTypeList][strType]"+strType);

            HashMap hPlanList = null;

            if(!plan.equals("0")){
                //Obtenemos la Data de los Planes
                //-------------------------------
                objPlanBean.setNptipo2("0");
                objPlanBean.setNpsolutionid(MiUtil.parseInt(strSolutionId));
                objPlanBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));

                logger.info("objPlanBean.toString()  :"+objPlanBean.toString());
                hPlanList = objNewOrderService.PlanDAOgetPlanList(objPlanBean,strTypeCompany);
                if (hPlanList.get("strMessage") != null){
                    logger.error(""+(String)hPlanList.get("strMessage"));
                    throw new Exception((String)hPlanList.get("strMessage"));
                }
            }

            HashMap hServiceList  = null;
            if(!service.equals("0")){
                //Obtenemos la Data de los Servicios
                //----------------------------------
                /**DLAZO: Modificación de Parametros**/
                //johncmb inicio
                if( MiUtil.parseInt(strSpecificationId) == Constante.SPEC_POSTPAGO_VENTA ||
                        MiUtil.parseInt(strSpecificationId) == Constante.SPEC_PREPAGO_NUEVA ||
                        MiUtil.parseInt(strSpecificationId) == Constante.SPEC_SSAA_SUSCRIPCIONES ||
                        MiUtil.parseInt(strSpecificationId) == Constante.SPEC_SSAA_PROMOTIONS ||
                        MiUtil.parseInt(strSpecificationId) == Constante.SPEC_PREPAGO_TDE ||
                        MiUtil.parseInt(strSpecificationId) == Constante.SPEC_REPOSICION_PREPAGO_TDE //Se agrego subcategoria reposicion prepago tde - TDECONV034
                        ) {
                    strSSAAType  = "0,1,2,3,5";}
                else {
                    strSSAAType  = "0,1,5";
                }
                logger.info("[ItemServlet][getSoluctionPlanTypeList][strSSAAType]"+strSSAAType);
                hServiceList       = objGeneralService.getServiceList(MiUtil.parseInt(strSolutionId), MiUtil.parseInt("0"), MiUtil.parseInt("0"), strSSAAType, strType);
                if (hServiceList.get("strMessage") != null){
                    logger.error(""+(String)hServiceList.get("strMessage"));
                    throw new Exception((String)hServiceList.get("strMessage"));
                }
                //johncmb fin
                objArrayListDefault        = objNewOrderService.ServiceDAOgetServiceDefaultList(Constante.NAME_ORIGEN_FFPEDIDOS,MiUtil.parseInt(strSpecificationId),strMessage);
                if (strMessage != null){
                    logger.error(""+(String)hServiceList.get("strMessage"));
                    throw new Exception((String)hServiceList.get("strMessage"));
                }

            }
            HashMap hRespPagoList = null;
            if(!respPago.equals("0")){
                //Obtenemos los responsables de pago
                hRespPagoList =  objCustomerService.CustomerDAOgetCustomerSitesBySolution(MiUtil.parseInt(strCustomerId) , MiUtil.parseInt(strSolutionId) , MiUtil.parseInt(strSpecificationId));
                if (hRespPagoList.get("strMessage") != null){
                    logger.error(""+(String)hRespPagoList.get("strMessage"));
                    throw new Exception((String)hRespPagoList.get("strMessage"));
                }
            }

            HashMap hLineaList  = null;
            if(!linea.equals("0")){
                //Obtenemos la Data de los Servicios
                //----------------------------------
                hLineaList       =   objNewOrderService.ProductLineDAOgetProductLineSpecList(MiUtil.parseInt(strSolutionId),MiUtil.parseInt(strSpecificationId),"PRODUCTLINE",0);
                if (hLineaList.get("strMessage") != null){
                    logger.error(""+(String)hServiceList.get("strMessage"));
                    throw new Exception((String)hServiceList.get("strMessage"));
                }

            }

            if ( !strSpecificationId.equals(String.valueOf(Constante.SPEC_POSTPAGO_VENTA)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_EMPLEADO_ASIGNACION)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_EMPLEADO_FAMILIAR))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_EMPLEADO_AMIGO)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_PRESTAMO_TEST))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_ACT_DES_SERVICIOS_BA))
                    //&& !strSpecificationId.equals(String.valueOf(Constante.SPEC_ACT_CAMB_PLAN_BA)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA)) //&& !strSpecificationId.equals(String.valueOf(Constante.SPEC_ACCESO_INTERNET))
                    && !strSpecificationId.equals(String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO)) && !strSpecificationId.equals(String.valueOf(Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION))
                    ){
                request.setAttribute("hPlanList", hPlanList);
            }

            if((MiUtil.parseInt(strSolutionId) == Constante.SOLUTION_3G_HPPTT_POST) || (MiUtil.parseInt(strSolutionId) == Constante.SOLUTION_3G_POST))//EZM HPPTT+ 13/12 Mostrar planes asociados a la solución
            {
                request.setAttribute("hPlanList", hPlanList);
            }




            request.setAttribute("hServiceList", hServiceList);
            request.setAttribute("hRespPagoList", hRespPagoList);
            request.setAttribute("objArrayListDefault", objArrayListDefault);
            request.setAttribute("hLineaList", hLineaList);
            request.setAttribute("strSpecificationId", strSpecificationId);
            request.setAttribute("strSolutionId", strSolutionId);
            request.setAttribute("strSessionId",strSessionId);

            if(strSpecificationId.equals("2022")) { // strSpecificationId= Bolsa cambio
                request.setAttribute("strCustomerId",strCustomerId);
                request.setAttribute("strnpSite",strnpSite);
            }

            out.println("<script>");
            request.getRequestDispatcher("/pages/loadPlanServiceInfo.jsp").forward(request, response);
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
            out.println("</script>");

        } catch(Exception e){
            logger.error("Exception getSoluctionPlanTypeList",e);
            if(logger.isDebugEnabled()) logger.debug("Esta en el catch: "+e.getMessage() );
            out.println("alert('"+e.getMessage()+"');");
        }
        logger.info("******************************FIN ItemServelt > getSoluctionPlanTypeList ******************************");
    }
    /**
     * Motivo:  Método que obtiene la lista de responsables de pago cuando se carga la solución
     * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
     * <br/>Fecha: 28/05/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException*/

    public void getSoluctionRespPago(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException,IOException {

        String strSolutionId       = (String)request.getParameter("strSolutionId");
        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String strCustomerId       = (String)request.getParameter("strCustomerId");
        String strSessionId        = (String)request.getParameter("strSessionId");
        String respPago            = (String)request.getParameter("respPago");

        PrintWriter out  = response.getWriter();

        ArrayList objArrayListDefault = new ArrayList();

        if (logger.isDebugEnabled()) {
            logger.debug("strSolutionId:::" + strSolutionId);
        }
        System.out.println("[ItemServlet][getSoluctionPlanTypeList][strSolutionId]"+strSolutionId);
        System.out.println("[ItemServlet][getSoluctionPlanTypeList][strSpecificationId]"+strSpecificationId);
        System.out.println("[ItemServlet][getSoluctionPlanTypeList][strCustomerId]"+strCustomerId);

        HashMap hRespPagoList = null;
        if(!respPago.equals("0")){
            //Obtenemos los responsables de pago
            hRespPagoList =  objCustomerService.CustomerDAOgetCustomerSitesBySolution(MiUtil.parseInt(strCustomerId) , MiUtil.parseInt(strSolutionId) , MiUtil.parseInt(strSpecificationId));
            if (hRespPagoList.get("strMessage") != null){
                throw new Exception((String)hRespPagoList.get("strMessage"));
            }
        }

        request.setAttribute("hRespPagoList", hRespPagoList);
        request.setAttribute("strSpecificationId", strSpecificationId);
        request.setAttribute("strSolutionId", strSolutionId);
        request.setAttribute("strSessionId",strSessionId);

        try{

            request.getRequestDispatcher("/pages/loadRespPago.jsp").forward(request, response);
        } catch(Exception e){
            if(logger.isDebugEnabled()) logger.debug("Esta en el catch: "+e.getMessage() );
            out.println("alert('"+e.getMessage()+"');");
        }
        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
        out.println("</script>");
    }

    /**
     * Motivo:  Método que obtiene la lista de los servicios por defecto en base al producto seleccionado
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 30/06/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException*/

    public void getProductServiceDefaultList(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException,IOException {

        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String strSessionId        = (String)request.getParameter("strSessionId");
        String strobjectItem       = (String)request.getParameter("strobjectItem");
        String strProductId        = (String)request.getParameter("strProductId")==null?"0":(String)request.getParameter("strProductId");
        String strModality         = (String)request.getParameter("strModality")==null?"":(String)request.getParameter("strModality");
        String strPlanId           = (String)request.getParameter("strPlanId")==null?"0":(String)request.getParameter("strPlanId");
        String strModelId          = (String)request.getParameter("strModelId")==null?"0":(String)request.getParameter("strModelId");
        String strServiceMsjId       = (String)request.getParameter("strServiceMsjId");
        String strMessage          = null;
        String strPermission_alq   = null;
        String strPermission_msj   = null;

        PrintWriter out  = response.getWriter();
        HashMap objHashMap = null;
        HashMap hServiceDefaultList = null;
        ArrayList objArrayListDefault = new ArrayList();
        GeneralService objGeneralService= new GeneralService();
        NewOrderService objNewOrderService = new NewOrderService();
        ServiciosBean servicioBean = null;

        System.out.println("<<<<<<<<<<<<<<<<<<getProductServiceDefaultList:");
        System.out.println("strSpecificationId:"+strSpecificationId);
        System.out.println("strobjectItem:"+strobjectItem);
        System.out.println("strProductId:"+strProductId);
        System.out.println("strModality:"+strModality);
        System.out.println("strPlanId:"+strPlanId);
        System.out.println("strModelId:"+strModelId);
        try{

            //Encontramos los permisos para visualizar servicios de mensajeria o arrendamiento
            //--------------------------------------------------------------------------------
            objHashMap   =    objGeneralService.getPermissionServiceDefault(MiUtil.parseLong(strSpecificationId),strobjectItem,strModality,
                    MiUtil.parseInt(strModelId),MiUtil.parseInt(strProductId),strServiceMsjId);
            if ((String)objHashMap.get("strMessage") != null){
                throw new Exception((String)objHashMap.get("strMessage"));
            }
            strPermission_alq  = (String) objHashMap.get("ipermission_alq");
            strPermission_msj = (String) objHashMap.get("ipermission_msj");

            System.out.println("strPermission_alq:"+strPermission_alq);
            System.out.println("strPermission_msj:"+strPermission_msj);


            if ( (MiUtil.parseInt(strPermission_alq)==1)  || (MiUtil.parseInt(strPermission_msj)==1)){

                if ((MiUtil.parseInt(strPermission_msj)==1) &&   (MiUtil.parseInt(strModelId)!=0) &&  (MiUtil.parseInt(strPlanId)==0)){
                    strPlanId = strModelId;
                }
                hServiceDefaultList   = objNewOrderService.getProductServiceDefaultList(MiUtil.parseLong(strSpecificationId),
                        MiUtil.parseInt(strProductId),
                        MiUtil.parseInt(strPlanId),
                        MiUtil.parseInt(strPermission_alq),
                        MiUtil.parseInt(strPermission_msj));
                if (hServiceDefaultList.get("strMessage") != null){
                    throw new Exception((String)hServiceDefaultList.get("strMessage"));
                }
                if(hServiceDefaultList.get("objServiceDefaultList") != null)
                    System.out.println("hServiceDefaultList.size() " + ((ArrayList)hServiceDefaultList.get("objServiceDefaultList")).size());
            }

            request.setAttribute("hServiceDefaultList", hServiceDefaultList);
            request.setAttribute("strSessionId",strSessionId);
            request.setAttribute("strSpecificationId",strSpecificationId);
            request.setAttribute("serviciosBean", null);

            out.println("<script>");
            request.getRequestDispatcher("/pages/loadServiceDefaultInfo.jsp").forward(request, response);
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
            out.println("</script>");


        }catch(Exception e){
            if(logger.isDebugEnabled()){
                logger.error(formatException(e));
                out.println("<script>");
                out.println("alert('"+e.getMessage()+"');");
                out.println("</script>");
            }
        }

    }

    /**
     * Motivo:  Método que obtiene la lista de los servicios por defecto al editar un item
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 30/06/2009
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException*/

    public void getServiceAdicionalList(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException,IOException {

        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String strSessionId        = (String)request.getParameter("strSessionId");
        //Ahora se recibira un arreglo de servicios
        String[] strServiceId        = (String[])request.getParameterValues("strServiceId");
        String[] strServiceMod       = (String[])request.getParameterValues("strServiceMod");
        String[] strServiceAct       = (String[])request.getParameterValues("strServiceAct");

        PrintWriter out  = response.getWriter();
        HashMap objHashMap = null;
        HashMap hServiceDefaultList = null;
        ArrayList objArrayListDefault = new ArrayList();
        GeneralService objGeneralService= new GeneralService();
        NewOrderService objNewOrderService = new NewOrderService();
        ArrayList list = null;

        System.out.println("<<<<<<<<<<<<<<<<<<getServiceAdicionalList:");
        System.out.println("strSpecificationId:"+strSpecificationId);
        System.out.println("strServiceId:"+strServiceId);
        System.out.println("strServiceMod:"+strServiceMod);
        System.out.println("strServiceAct:"+strServiceAct);

        request.setAttribute("strServiceAct", strServiceAct);
        request.setAttribute("strServiceMod", strServiceMod);
        try{

            if(strServiceId == null){
                strServiceId = new String[0];
            }
            list = new ArrayList();
            ServiciosBean serviciosBean = null;
            //Se obtiene la lista de servicios
            for(int i=0; i<strServiceId.length; i++){
                serviciosBean = objGeneralService.getDetailService(Long.parseLong(strServiceId[i]));
                list.add(serviciosBean);
            }

            request.setAttribute("strSessionId",strSessionId);
            request.setAttribute("strSpecificationId",strSpecificationId);
            request.setAttribute("hServiceDefaultList", null);
            request.setAttribute("serviciosBean", list);

            out.println("<script>");
            request.getRequestDispatcher("/pages/loadServiceDefaultInfo.jsp").forward(request, response);
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
            out.println("</script>");

        }catch(Exception e){
            if(logger.isDebugEnabled()){
                logger.error(formatException(e));
                out.println("<script>");
                out.println("alert('"+e.getMessage()+"');");
                out.println("</script>");
            }
        }

    }

    /**
     * Motivo:  Método que se encarga de obtener la lista de modelos de equipos de acuerdo a la linea de producto
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 30/09/2010        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getModelProducTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        PrintWriter out            = response.getWriter();
        ProductBean objProductBean = new ProductBean();
        String strModality         = request.getParameter("strModality");
        String strProductLineId    = request.getParameter("strProductLineId");
        String strCategoryId       = request.getParameter("strSpecificationId");
        String strSolutionId       = request.getParameter("strSolutionId");
        objProductBean.setNpproductlineid(MiUtil.parseLong(strProductLineId));
        objProductBean.setNpmodality(strModality);
        objProductBean.setNpcategoryid(MiUtil.parseLong(strCategoryId));
        objProductBean.setNpsolutionid(MiUtil.parseLong(strSolutionId));
        out.println("<script>");
        loadModelProductList(objProductBean,out);
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
        out.println("</script>");

    }

    /**
     * Motivo:  Método que se encarga de pintar en el combo de Modelos la lista de modelos de equipos de acuerdo a la linea de producto
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 30/09/2010        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadModelProductList(ProductBean objProductBean,PrintWriter out){
        NewOrderService objNewOrderService = new NewOrderService();
        HashMap objHashMap = objNewOrderService.getProductModelList(objProductBean);
        ArrayList objArrayList = null;
        if( (String)objHashMap.get("strMessage")!= null ){
            String variable = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
            out.println("alert('"+variable+"')");
        }else{
            out.println("formCurrent = parent.mainFrame.frmdatos;");
            out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);");
            out.println("parent.mainFrame.vctProduct.removeElementAll();");
            objArrayList = (ArrayList)objHashMap.get("objArrayList");
            if ( objArrayList != null && objArrayList.size() > 0 ){
                System.out.println("objArrayList.size() : " + objArrayList.size());
                for( int i=0; i<objArrayList.size();i++ ){
                    objProductBean = (ProductBean)objArrayList.get(i);
                    out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductModel,'"+objProductBean.getNpproductid()+"','"+MiUtil.getMessageClean(objProductBean.getNpproductname())+"')");
                }
            }
        }
    }

    /**
     * Motivo:  Método que se encarga de obtener la lista de modelos de equipos de acuerdo a la linea de producto
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 30/09/2010        
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getProductPlanList(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        logger.info("******************************INICIO ItemServelt > getProductPlanList ******************************");
        PrintWriter out            = response.getWriter();
        ProductBean objProductBean = new ProductBean();
        String strModality         = request.getParameter("strModality");
        String strProductId        = request.getParameter("strProductId");
        String strCategoryId       = request.getParameter("strSpecificationId");
        String strSolutionId       = request.getParameter("strSolutionId");
        String strProductLine      = request.getParameter("strProductLineId");//EZUBIAURR 15/03/11

        String strTypeCompany      = (String)request.getParameter("type");
        //PRY-1049 | INICIO: AM0001
        String strFlagcoverage     = request.getParameter("flagcoverage");
        int flagcoverage           = MiUtil.parseInt(strFlagcoverage);
        objProductBean.setFlagCoverage(flagcoverage);
        logger.info("strModality     : "+strModality);
        logger.info("strProductId    : "+strProductId);
        logger.info("strCategoryId   : "+strCategoryId);
        logger.info("strProductLine  : "+strProductLine);
        logger.info("strSolutionId   : "+MiUtil.parseLong(strSolutionId));
        logger.info("strTypeCompany  : "+strTypeCompany);
        logger.info("strFlagcoverage : "+strFlagcoverage);
        //PRY-1049 | FIN: AM0001

        objProductBean.setNpproductid(MiUtil.parseLong(strProductId));
        objProductBean.setNpmodality(strModality);
        objProductBean.setNpcategoryid(MiUtil.parseLong(strCategoryId));
        objProductBean.setNpsolutionid(MiUtil.parseLong(strSolutionId));
        objProductBean.setNpsubtype(strTypeCompany);

        objProductBean.setNpproductlineid(MiUtil.parseLong(strProductLine));
        out.println("<script>");

        logger.info("objProductBean.toString()   :"+objProductBean.toString());
        loadProductPlanList(objProductBean,out);
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
        out.println("</script>");
        logger.info("******************************FIN ItemServelt > getProductPlanList ******************************");

    }

    public void loadProductPlanList(ProductBean objProductBean,PrintWriter out){
        logger.info("******************************INICIO ItemServelt > loadProductPlanList(ProductBean objProductBean,PrintWriter out) ******************************");
        NewOrderService objNewOrderService = new NewOrderService();
        HashMap objHashMap = objNewOrderService.getProductPlanList(objProductBean);
        PlanTarifarioBean planTarifBean = null;
        ArrayList objArrayList = null; //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
        planTarifBean = new PlanTarifarioBean(); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)

        if( (String)objHashMap.get("strMessage")!= null ){
            String variable = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
            logger.info("variable: "+variable);
            out.println("alert('"+variable+"')");
        }else{
            out.println("formCurrent = parent.mainFrame.frmdatos;");
            out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemPlanTarifario);");
            //out.println("parent.mainFrame.vctProduct.removeElementAll();");
            objArrayList = (ArrayList)objHashMap.get("objArrayList");

            if ( objArrayList != null && objArrayList.size() > 0 ){
                //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                for( int i=0; i<objArrayList.size();i++ ){

                    planTarifBean = (PlanTarifarioBean)objArrayList.get(i);
                    out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPlanTarifario,'"+planTarifBean.getNpplantarifarioid()+"','"+MiUtil.getMessageClean(planTarifBean.getNpdescripcion())+"')");
                }
            }
        }
        logger.info("******************************FIN ItemServelt > loadProductPlanList(ProductBean objProductBean,PrintWriter out) ******************************");
    }



    /**
     * Motivo:  Obtiene un mensaje de validacion de Servicios
     * <br/>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br/>Fecha: 09/10/2010
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getValidServSelectedList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strServicesSelectedList         = (String)request.getParameter("strServices");
        String strDescServicesSelectedList         = (String)request.getParameter("strServicesDesc");
        String strProduct         = (String)request.getParameter("strProduct");
        String strPlanId         = (String)request.getParameter("strPlanId");
        String strMode           = (String)request.getParameter("strMode");
        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String messageDesc = "";
        String messageFinal="";
        messageFinal=getValidServSelectedData(strServicesSelectedList,strDescServicesSelectedList,strProduct,strPlanId,strMode,strSpecificationId);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if ((!"".equals(messageFinal))){
            out.println("alert('"+messageFinal+"');");
            System.out.println("strMode es ---->" + strMode);}
        if (Constante.EDIT.equals(strMode)){
            System.out.println("ENTRO AL CLOSE ---->" + strMode);
            out.println("parent.mainFrame.fxCancelItemEditWindow();");
        }
        //out.println("alert('Existen Servicios no compatibles con el Plan:-SMS Variable\\n Existen Servicios no compatibles con el Producto-CDI Variable|')");
        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
        out.println("</script>");


    }

    /**
     * Motivo:  Realiza validación de los servicios y arma los mensajes para las advertencias de incompatibilidad
     * <br/>Realizado por: <a href="mailto:ezubiaurr@hp.com">Enrique Zubiaurr</a>
     * <br/>Fecha: 24/11/2010
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public String getValidServSelectedData(String strServicesSelectedList,String strDescServicesSelectedList,String strProduct,String strPlanId,String strMode, String strSpecificationIdp) throws ServletException, IOException {
        NewOrderService objNewOrderService = new NewOrderService();
        ArrayList listServices = null;
        StringTokenizer tokens = null;
        String arrServicesId[] = null;
        String arrServicesDesc[] = null;
        String arrServicesResult[] = null;
        String strSpecificationId  = strSpecificationIdp;
        String aux1 = "";
        String aux2 = "";
        int i=0;
        int j=0;
        int k=0;
        String messageFinal = "";
        String messageDesc = "";
        StringBuffer buf = new StringBuffer(); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)

        arrServicesId = strServicesSelectedList.split(",");
        arrServicesDesc = strDescServicesSelectedList.split(",");



        String strFlagStock = "";
        String strMensajeStock = "";
        String strMessagePlan = "";
        String strMessageProduct = "";

        HashMap hshValidateServMessage = new HashMap();
        hshValidateServMessage = objNewOrderService.getValidServSelectedList(strServicesSelectedList, strDescServicesSelectedList, strPlanId, strProduct);//EZUBIAURR 28/02/11

        HashMap mapa = (HashMap)hshValidateServMessage.get("objServicebyPlan");

        if (hshValidateServMessage.get("objServicebyPlan")!=null){
            listServices = (ArrayList)((HashMap)(hshValidateServMessage.get("objServicebyPlan"))).get("objServiciosBeanList");
            for (j=0; j<listServices.size();++j) {
                for (k=0; k<arrServicesId.length;++k) {
                    if (arrServicesId[k].equals(listServices.get(j).toString())) { //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                        buf.append(arrServicesDesc[k]); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                        buf.append(","); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                    }
                }
                messageDesc = buf.toString(); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
            }
            strMessagePlan = (String)((HashMap)(hshValidateServMessage.get("objServicebyPlan"))).get("strMessageValid");
            strMessagePlan = strMessagePlan + "\\n" + messageDesc;
        }


        if (hshValidateServMessage.get("objServicebyProduct")!=null){
            messageDesc = "";
            buf.delete(0,buf.length()); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)

            listServices = (ArrayList)((HashMap)(hshValidateServMessage.get("objServicebyProduct"))).get("objServiciosBeanList");
            for (j=0; j<listServices.size();++j) {
                for (k=0; k<arrServicesId.length;++k) {

                    if (arrServicesId[k].equals(listServices.get(j).toString())) { //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                        buf.append(arrServicesDesc[k]); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                        buf.append(","); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
                    }
                }
                messageDesc = buf.toString(); //EZM 23/12/10 Compatibilidad Modelo-Plan-Servicio (FindBugs Correction)
            }
            strMessageProduct = (String)((HashMap)(hshValidateServMessage.get("objServicebyProduct"))).get("strMessageValid");
            strMessageProduct = strMessageProduct + "\\n" + messageDesc;


        }

        if ((!"".equals(strMessagePlan)) || (!"".equals(strMessageProduct))){

            if (!"".equals(strMessagePlan)) {
                aux1 = strMessagePlan.substring(0,(strMessagePlan.length()-1));
                messageFinal = aux1 + "\\n";
            }
            if (!"".equals(strMessageProduct)) {
                aux2 = strMessageProduct.substring(0,(strMessageProduct.length()-1));
                messageFinal = messageFinal + "\\n" + aux2;
            }
        }
        return messageFinal;
    }

    /**
     * Motivo:  Método que envia los datos de los items para la evaluación de volumen de orden
     * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
     * <br>Fecha: 02/11/2010
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void evaluateOrderVolume(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strSpecificationId  = (String)request.getParameter("strSpecificationId");
        String strCustomerId       = (String)request.getParameter("strCustomerId");

        String [] itemIndexs = request.getParameterValues("hdnIndice");
        String [] itemIds = request.getParameterValues("hdnItemId");
        String [] productIds = request.getParameterValues("hdnItemValuetxtItemProduct");
        String [] planIds = request.getParameterValues("hdnItemValuetxtItemRatePlan");
        String [] quantitys = request.getParameterValues("hdnItemValuetxtItemQuantity");
        String [] prices = request.getParameterValues("hdnItemValuetxtItemPriceCtaInscrip");
        String [] modalitys = request.getParameterValues("hdnItemValuetxtItemModality");
        String [] promotionIds = request.getParameterValues("hdnItemValuetxtItemPromotioId");
        String [] rents = request.getParameterValues("hdnItemValuetxtItemRentEquipment");
        String [] solutions = request.getParameterValues("hdnItemValuetxtItemSolution");
        String typeWindow = (String)request.getParameter("type_window");

        long orderId = 0;
        int itemIndex = 0;
        long itemId = 0;
        long productId = 0;
        long planId = 0;
        int quantity = 0;
        String price = null;
        String modality = null;
        int promotionId = 0;
        String rent = null;
        int solution = 0;

        double discount = 0.0;

        request.setAttribute("type_window", typeWindow);

        ItemBean itemBean = null;
        List itemBeanList = new ArrayList();
        HashMap objHashMap = null;
        ItemService objItemService = new ItemService();

        if(Constante.PAGE_ORDER_EDIT.equals(typeWindow)){
            orderId = Long.parseLong(request.getParameter("hdnOrderId"));
        }

        for(int i = 0; i < productIds.length; i++){

            itemBean = new ItemBean();

            if(Constante.PAGE_ORDER_EDIT.equals(typeWindow)){
                if(itemIds[i] != null && !"".equals(itemIds[i])){
                    itemId = Long.parseLong(itemIds[i]);
                }
                else{
                    itemId = 0;
                }
            }
            itemIndex = Integer.parseInt(itemIndexs[i]);
            productId = Long.parseLong(productIds[i]);
            planId = Long.parseLong(planIds[i]);
            quantity = Integer.parseInt(quantitys[i]);
            price = prices[i];
            modality = modalitys[i];
            promotionId = ((promotionIds[i].equals(""))?0:Integer.parseInt(promotionIds[i]));
            rent = rents[i];
            solution = Integer.parseInt(solutions[i]);

            itemBean.setNporderid(orderId);
            itemBean.setNpIndice(itemIndex);
            itemBean.setNpitemid(itemId);
            itemBean.setNpproductid(productId);
            itemBean.setNpplanid(planId);
            itemBean.setNpquantity(quantity);
            itemBean.setNpprice(price);
            itemBean.setNporiginalprice(price);
            itemBean.setNpmodalitysell(modality);
            itemBean.setNppromotionid(promotionId);
            itemBean.setNprent(rent);
            itemBean.setNpsolutionid(solution);

            itemBeanList.add(itemBean);
        }

        objHashMap = objItemService.evaluateOrderVolume(MiUtil.parseInt(strCustomerId), MiUtil.parseInt(strSpecificationId), typeWindow, itemBeanList);

        request.removeAttribute("objHashMapVO");
        request.setAttribute("objHashMapVO",objHashMap);
        request.getRequestDispatcher("pages/loadItems.jsp").forward(request, response);
    }

    public void doValidateIMEICustomer(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException,IOException{
        String strMessage = null;
        try{
            HashMap hResult = null;
            ItemService objItemService = new ItemService();
            String strIMEI = request.getParameter("imei")==null?"":request.getParameter("imei");
            hResult = objItemService.ItemDAOdoValidateIMEICustomer(strIMEI);

            strMessage = (String)hResult.get(Constante.MESSAGE_OUTPUT);
            request.setAttribute(Constante.MESSAGE_OUTPUT,strMessage);
            request.getRequestDispatcher("pages/loadValidateIMEI.jsp").forward(request, response);

        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute(Constante.MESSAGE_OUTPUT, strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }

    /**
     * Motivo:  Valida el tipo de Plan, si corresponde a un plan Prepago con Bucket
     * <br/>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br/>Fecha: 05/12/2011
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getValidateTypePlan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NewOrderService objNewOrderService = new NewOrderService();
        String strPlanId         = (String)request.getParameter("strPlanId");
        String strMode           = (String)request.getParameter("strMode");
        String strTypePlan = "";
        String messageFinal= "Plan Destino Incorrecto. Debe ser un Plan Full Prepago con Bucket";
        String strMessage = "";
        try{
            strTypePlan=objNewOrderService.getTypePlan(MiUtil.parseLong(strPlanId));
            PrintWriter out = response.getWriter();
            out.println("<script>");
            if ((!"FPB".equals(strTypePlan))){
                out.println("alert('"+messageFinal+"');");
                out.println("parent.mainFrame.fxSetHiddenTypePlan('1');");
            } else {
                out.println("parent.mainFrame.fxSetHiddenTypePlan('0');");
                out.println("parent.mainFrame.fxSendItemValuesOrder();");
            }
            out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
            out.println("</script>");
        }   catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
            request.setAttribute(Constante.MESSAGE_OUTPUT, strMessage);
            request.getRequestDispatcher("pages/loadMessage.jsp").forward(request, response);
        }
    }

    /**
     * MMONTOYA [ADT-RCT-092 Roaming con corte]
     * @param request
     * @param response
     * @throws Exception
     * @throws IOException
     */
    public void validateRecurrentRoamingService(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException {
        String phone = request.getParameter("strPhone");
        String activationDate = request.getParameter("strProcessDate");
        int validity = Integer.valueOf(request.getParameter("strValidity").toString());
        long orderId = Long.valueOf(request.getParameter("strOrderId").toString());
        String bagCode = request.getParameter("strBagCode"); // CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]

        //HashMap hshDataMap = (new RoamingService()).validateRecurrentRoamingService(phone, activationDate, validity, orderId);
        HashMap hshDataMap = (new RoamingService()).validateRecurrentRoamingService(phone, activationDate, validity, orderId, bagCode); // CFERNANDEZ [PRY-0858]
        if (hshDataMap.get("strMessage") != null) {
            PrintWriter out = response.getWriter();
            out.println(MiUtil.getMessageClean((String)hshDataMap.get("strMessage")));
            out.close();
        }
    }

    /**
     * Motivo:  Valida la cantidad de unidades en X % para el cambio de productos bolsa
     * <br/>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br/>Fecha: 04/08/2015
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getValidateCountUnitsBolCel(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        GeneralService objGeneralServices= new GeneralService();
        int idProductOrigen           = Integer.parseInt(request.getParameter("strProductOrigen"));
        int idProductDestino          = Integer.parseInt(request.getParameter("strProductDestino"));
        int specification             = Integer.parseInt(request.getParameter("strSpecification"));
        String descProductDestino             = request.getParameter("strDescProductDestino");
        String result = objGeneralServices.getValidateCountUnitsBolCel(specification,idProductOrigen,idProductDestino);
        PrintWriter out = response.getWriter();

        if(result != null){
            out.println("<script>");
            out.println("var formCurrent = parent.mainFrame.frmdatos;");
            out.println("alert('"+result+"')");
            out.println("formCurrent.cmb_ItemProductoDestino.value = null;");
            out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemPromocion);");
            out.println("formCurrent.txt_ItemPriceCtaInscrip.value = '';");
            out.println("</script>");
        }
    }

    /**
     * Motivo:  Obtiene todos los numero dentro del blacklist
     * <br/>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br/>Fecha: 04/08/2015
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @param response
     * @param request
     */
    public void getValidatePhoneBlacklist(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        GeneralService objGeneralServices= new GeneralService();
        int strSiteId = 0;
        int strCustomerId = 0;
        if(!"".equals(request.getParameter("strSiteId"))){
            strSiteId         = Integer.parseInt(request.getParameter("strSiteId"));
        }else{
            strCustomerId         = Integer.parseInt(request.getParameter("strCustomerId"));
        }
        String mensaje = objGeneralService.getPhoneBlackList(strSiteId,strCustomerId,Constante.IN_BLACKLIST);
        PrintWriter out = response.getWriter();
        if(mensaje != null){
            out.println("<script>");
            out.println("alert('"+mensaje+"')");
            out.println("</script>");
        }
    }

    /**
     * Motivo: Método que obtiene la cantidad de Renta Adelantada y precio del plan.
     * Código: PRY-0762
     * <br>Realizado por: <a href="mailto:jquispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 10/03/2017
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void showValueRentaAdelantada(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
    	System.out.println("[ItemServlet][showValueRentaAdelantada] Inicio ---------------------");
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");        
        PrintWriter out = response.getWriter();
        
        int iAppId          = 0;
        String strMessage = null;        
        int idProducto = 0;
        int idPlan = 0;
        
        
        try{
        	
        	String strSessionId     = (String)request.getParameter("strSessionId");
        	String strProductId = request.getParameter("strProductId");
        	String strPlanId = request.getParameter("strPlanId");
        	
        	System.out.println("[ItemServlet][showValueRentaAdelantada] strProductId: "+strProductId);
        	System.out.println("[ItemServlet][showValueRentaAdelantada] strPlanId: "+strPlanId);
        	
        	// Se obtiene el id de la aplicación y del usuario
            PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);            
            iAppId  = objSessionBean.getAppId();
            
            idProducto  = MiUtil.parseInt(strProductId);
            idPlan      = MiUtil.parseInt(strPlanId);                                  
            Map<String, Object> respJson = new HashMap<String, Object>();        	
        	        	
        	HashMap objHashMapCantidadRA = objGeneralService.getCantidadRentaAdelantada(idProducto, idPlan);            	
        	strMessage = (String) objHashMapCantidadRA.get("strMessage");
            if (strMessage!=null){
            	respJson.put("strMessage", strMessage);
            }else{
            	int intCantidadRA = (Integer)objHashMapCantidadRA.get("intCantidadRA");                                 
                
                HashMap objHashMapTotalRA = objGeneralService.getPrecioPlan(iAppId, String.valueOf(idPlan));
                strMessage = (String) objHashMapTotalRA.get("strMessage");
                if (strMessage!=null){            	
                	throw new Exception(strMessage);
                }
                                
                float[] arrRentFee = (float[])objHashMapTotalRA.get("afAccessFee");
                String strPrecioPlan = null;
                
                if(arrRentFee != null && arrRentFee.length > 0){
                	strPrecioPlan = String.valueOf(arrRentFee[0]);
                }
                
                if(strPrecioPlan == null){
                	strMessage = "El plan seleccionado no tiene precio.";
                }
                                                        
                if(strMessage == null){
                	respJson.put("intCantidadRA", intCantidadRA);
                    respJson.put("strPrecioPlan", strPrecioPlan);
                }else{
                	respJson.put("strMessage", strMessage);
                }
                
            }
                        
            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        	
        }catch(Exception e){
        	System.out.println("ERROR: ItemServlet showValueRentaAdelantada:" + e);
        	e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }finally{
            out.close();
        }
        
        System.out.println("[ItemServlet][showValueRentaAdelantada] Fin ---------------------");
    }

    /**
     * Motivo: Método que obtiene la lista de regiones habilitadas por producto
     * Código: PRY-0721
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 14/02/2017
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getEnabledRegions(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        System.out.println("[ItemServlet][getEnabledRegions] Inicio ------------------");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            String strProductId = request.getParameter("strProductId");
            System.out.println("[ItemServlet][getEnabledRegions] strProductId: "+strProductId);

            HashMap objHashMap = objGeneralService.getEnabledRegions(strProductId);
            List<RegionBean> listaRegiones;

            int result = 0;

            Map<String, Object> respJson = new HashMap<String, Object>();

            if((String)objHashMap.get("strMessage")!= null){
                String strMessage = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
                System.out.println("[ItemServlet][getEnabledRegions] strMessage: "+strMessage);
                respJson.put("strMessage",strMessage);
            }
            else{
                result = ((Integer)objHashMap.get("result")).intValue();
            }

            respJson.put("result",result);

            if(result > 0){
                //Se muestra combo regiones
                System.out.println("[ItemServlet][getEnabledRegions] muestra comboBox region");

                listaRegiones = (ArrayList<RegionBean>)objHashMap.get("listaRegiones");
                System.out.println("[ItemServlet][getEnabledRegions] cantidad de regiones habilitadas: " + listaRegiones.size());

                respJson.put("listaRegiones",listaRegiones);
            }
            else{
                //Se oculta combo regiones
                System.out.println("[ItemServlet][getEnabledRegions] oculta comboBox region");
            }

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }
        finally{
            out.close();
        }

        System.out.println("[ItemServlet][getEnabledRegions] Fin ---------------------");
    }

/**
     * Motivo: Método que obtiene la lista de provincias habilitadas por Region
     * Código: BAFI2
     * <br>Realizado por: EFLORES
     * <br>Fecha: 14/02/2017
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getEnabledProvinces(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        System.out.println("[ItemServlet][getEnabledProvinces] Inicio ------------------");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            String strProductId = request.getParameter("strProductId");
            String strRegionId = request.getParameter("strRegionId"); //ZonaCoberturaId
            System.out.println("[ItemServlet][getEnabledProvinces] strProductId: "+strProductId);
            System.out.println("[ItemServlet][getEnabledProvinces] strRegionId: "+strRegionId);

            HashMap objHashMap = objGeneralService.getListProvinceBAFI(strProductId,strRegionId);
            List<RegionBean> listaProvinces;

            int result = 0;

            Map<String, Object> respJson = new HashMap<String, Object>();

            if((String)objHashMap.get("strMessage")!= null){
                String strMessage = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
                System.out.println("[ItemServlet][getEnabledRegions] strMessage: "+strMessage);
                respJson.put("strMessage",strMessage);
            }
            else{
                result = ((Integer)objHashMap.get("result")).intValue();
            }

            respJson.put("result",result);

            if(result > 0){
                //Se muestra combo regiones
                System.out.println("[ItemServlet][getEnabledProvince] muestra comboBox Provincia");

                listaProvinces = (ArrayList<RegionBean>)objHashMap.get("listaRegiones");
                System.out.println("[ItemServlet][getEnabledProvinces] cantidad de provincias habilitadas: " + listaProvinces.size());

                respJson.put("listaRegiones",listaProvinces);
            }
            else{
                //Se oculta combo regiones
                System.out.println("[ItemServlet][getEnabledProvinces] oculta comboBox provincia");
            }

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }
        finally{
            out.close();
        }

        System.out.println("[ItemServlet][getEnabledProvinces] Fin ---------------------");
    }

    /**
     * Motivo: Método que obtiene la lista de provincias habilitadas por Region
     * Código: BAFI2
     * <br>Realizado por: EFLORES
     * <br>Fecha: 14/02/2017
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getEnabledDistricts(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        System.out.println("[ItemServlet][getEnabledProvinces] Inicio ------------------");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            String strProductId = request.getParameter("strProductId");
            String strProvinceId = request.getParameter("strProvinceId"); //Id de Provincia
            System.out.println("[ItemServlet][getEnabledDistricts] strProductId: "+strProductId);
            System.out.println("[ItemServlet][getEnabledDistricts] strProvinceId: "+strProvinceId);

            HashMap objHashMap = objGeneralService.getListDistrictBAFI(strProductId,strProvinceId);
            List<RegionBean> listaDistricts;

            int result = 0;

            Map<String, Object> respJson = new HashMap<String, Object>();

            if((String)objHashMap.get("strMessage")!= null){
                String strMessage = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
                System.out.println("[ItemServlet][getEnabledDistricts] strMessage: "+strMessage);
                respJson.put("strMessage",strMessage);
            }
            else{
                result = ((Integer)objHashMap.get("result")).intValue();
            }

            respJson.put("result",result);

            if(result > 0){
                //Se muestra combo regiones
                System.out.println("[ItemServlet][getEnabledDistricts] muestra comboBox Distrito");

                listaDistricts = (ArrayList<RegionBean>)objHashMap.get("listaRegiones");
                System.out.println("[ItemServlet][getEnabledDistricts] cantidad de distritos habilitadas: " + listaDistricts.size());

                respJson.put("listaRegiones",listaDistricts);
            }
            else{
                //Se oculta combo regiones
                System.out.println("[ItemServlet][getEnabledDistricts] oculta comboBox distrito");
            }

            Gson gson = new Gson();
            String resp = gson.toJson(respJson);

            out.write(resp);
        }
        catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        }
        finally{
            out.close();
        }

        System.out.println("[ItemServlet][getEnabledDistricts] Fin ---------------------");
    }

    /**
     * Motivo: Método que valida el SIM y el MSISDN mediante consulta a servicio de FS
     * Código: TDECONV003-8
     * <br>Realizado por: PZACARIAS
     * <br>Fecha: 18/06/2018
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void validateSIM_MSISDN(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        logger.debug("[ItemServlet] [validateSIM_MSISDN] INICIO");
        String sim =(request.getParameter("sim")==null?"":request.getParameter("sim"));
        String newNumber     =(request.getParameter("newNumber")==null?"":request.getParameter("newNumber"));
        String tdeValNumTelSIM =(request.getParameter("tdeValNumTelSIM")==null?"0":request.getParameter("tdeValNumTelSIM"));
        String tdeValNumTelMSISDN     =(request.getParameter("tdeValNumTelMSISDN")==null?"0":request.getParameter("tdeValNumTelMSISDN"));
        logger.info("[ItemServlet] [validateSIM_MSISDN] sim = "+sim);
        logger.info("[ItemServlet] [validateSIM_MSISDN] newNumber = "+newNumber);
        logger.info("[ItemServlet] [validateSIM_MSISDN] tdeValNumTelSIM = "+tdeValNumTelSIM);
        logger.info("[ItemServlet] [validateSIM_MSISDN] tdeValNumTelMSISDN = "+tdeValNumTelMSISDN);
        String data, flagValid="0", message="";
        GeneralService objGeneralService = new GeneralService();
        NormalizarDireccionService objNormalizarDireccionService = new NormalizarDireccionService();
        ItemService objItemService = new ItemService();
        RequestContractFSBean requestBean = new RequestContractFSBean();
        HashMap responseMap = new HashMap();
        try{
            String contractStatus = objGeneralService.getValue(Constante.TDECONV003_8, Constante.CONTRACT_STATUS_FS);
            String plmnCode = objGeneralService.getValue(Constante.TDECONV003_8, Constante.PLMN_CODE_FS);

            requestBean.setSn(newNumber);
            //Consultando al servicio ContractFS
            responseMap = objItemService.getSIM_MSISDN_FS(requestBean);

            String statusFS = (responseMap.get("status")==null)?"":String.valueOf(responseMap.get("status"));
            String messageFS = (responseMap.get("message")==null)?"":String.valueOf(responseMap.get("message"));

            if(statusFS.equals(Constante.RESPONSE_FS_STATUS_OK)) {

                if(tdeValNumTelSIM.equals("1")){
                    String simFS = (responseMap.get("sim")==null)?"":String.valueOf(responseMap.get("sim"));

                    if(sim.equals(simFS)){
                        flagValid="1";
                        message="";
                    }else{
                        flagValid="0";
                        message=objNormalizarDireccionService.getValueDescXTableAndValue(Constante.TDECONV003_8, Constante.MSJ_VAL_NUMTEL_SIM);
                    }
                }

                if(tdeValNumTelMSISDN.equals("1") &&
                        (!tdeValNumTelSIM.equals("1") || (tdeValNumTelSIM.equals("1") && flagValid.equals("1")))){
                    String contractStatusFS = (responseMap.get("contractStatus")==null)?"":String.valueOf(responseMap.get("contractStatus"));
                    String plmnCodeFS = (responseMap.get("plmnCode")==null)?"":String.valueOf(responseMap.get("plmnCode"));

                    if((contractStatus.equals(contractStatusFS) && plmnCode.equals(plmnCodeFS))){
                        flagValid="1";
                        message="";
                    }else{
                        flagValid="0";
                        message=objNormalizarDireccionService.getValueDescXTableAndValue(Constante.TDECONV003_8, Constante.MSJ_VAL_NUMTEL_MSISDN);
                    }
                }

            }else{
                flagValid="0";
                message=messageFS;
            }
        }catch(Exception e){
            logger.error("[ItemServlet] [validateSIM_MSISDN] Exception: "+e.getMessage());
        }
        data=flagValid+"|"+message;
        logger.info("[ItemServlet] [validateSIM_MSISDN] data = " + data);
        PrintWriter out = response.getWriter() ;
        out.print(data);
        logger.debug("[ItemServlet] [validateSIM_MSISDN] FIN");
    }

    /**
     * Motivo: Método que valida el tipo de documento Portal - Siebel mediante consulta al servicio CustomerAccountFS
     * Código: TDECONV003-8
     * <br>Realizado por: PZACARIAS
     * <br>Fecha: 18/06/2018
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void validateTipDoc(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException,IOException {
        logger.debug("[ItemServlet] [validateTipDoc] INICIO");
        String tipDoc =(request.getParameter("tipDoc")==null?"":request.getParameter("tipDoc"));
        String newNumber     =(request.getParameter("newNumber")==null?"":request.getParameter("newNumber"));
        logger.info("[ItemServlet] [validateTipDoc] tipDoc = "+tipDoc);
        logger.info("[ItemServlet] [validateTipDoc] newNumber = " + newNumber);
        String data, flagValid="0", message="";
        NormalizarDireccionService objNormalizarDireccionService = new NormalizarDireccionService();
        ItemService objItemService = new ItemService();
        RequestCustomerAccountFSBean requestBean = new RequestCustomerAccountFSBean();
        HashMap responseMap = new HashMap();
        try{
            requestBean.setSn(newNumber);
            //Consultando al servicio CustomerAccountFS
            responseMap = objItemService.getTipDocFS(requestBean);

            String statusFS = (responseMap.get("status")==null)?"":String.valueOf(responseMap.get("status"));
            String messageFS = (responseMap.get("message")==null)?"":String.valueOf(responseMap.get("message"));

            if(statusFS.equals(Constante.RESPONSE_FS_STATUS_OK)) {
                String tipDocFS = (responseMap.get("tipDoc")==null)?"":String.valueOf(responseMap.get("tipDoc"));

                if(tipDoc.equals(tipDocFS)){
                    flagValid="1";
                    message="";
                }else{
                    flagValid="0";
                    message=objNormalizarDireccionService.getValueDescXTableAndValue(Constante.TDECONV003_8, Constante.MSJ_VAL_NUMTEL_TIP_DOC);
                }
            }else{
                flagValid="0";
                message=messageFS;
            }
        }catch(Exception e){
            logger.error("[ItemServlet] [validateTipDoc] Exception: "+e.getMessage());
        }
        data=flagValid+"|"+message;
        logger.info("[ItemServlet] [validateTipDoc] data = " + data);
        PrintWriter out = response.getWriter() ;
        out.print(data);
        logger.debug("[ItemServlet] [validateTipDoc] FIN");
    }

}