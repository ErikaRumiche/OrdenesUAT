package pe.com.portability.section.sectionPortability;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.portability.bean.PortabilityItemBean;
import pe.com.portability.bean.PortabilityOrderBean;
import pe.com.portability.dao.PortabilityItemDAO;
import pe.com.portability.dao.PortabilityOrderDAO;


/**
 * Motivo: Clase de Secciones de Dinámicas que ejecuta eventos referidos a Portabilidad (Save, Update, Delete).
 * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
 * <br>Fecha: 11/08/2009
 * @see SectionRepairEvents
 */
public class SectionPortabilityEvents extends GenericObject {

    public String updateSectionPortabilityLow(RequestHashMap request, Connection conn) throws SQLException, Exception {
        if(logger.isDebugEnabled())
            logger.debug("updateSectionPortability");

        PortabilityOrderBean objPortabilityOrderBean = null;
        PortabilityOrderDAO objPortabilityDAO = new PortabilityOrderDAO();
        String strMessage = null;

        String hdnLogin=request.getParameter("strLogin");
        System.out.println("=========== Login de Portabilidad ===========::: "+hdnLogin);


        String strEvalSolBajaL = null;
        String strMotivosL = null;
        String strDocAtatchmentL = null;
        String strRutaL = null;
        String strPorItemidL = null;
        String strTxtRutaL = null;

        String hdnOrderId = request.getParameter("hdnOrderId");
        long strTable  = MiUtil.parseLong(request.getParameter("hdntblListaPortabilityLow"));

        if (strTable == 2){

            objPortabilityOrderBean = new PortabilityOrderBean();

            String strEvalSolBaja=request.getParameter("hdncmbEvalSolBaja"); //cmbEvalSolBaja
            String strMotivos=request.getParameter("hdncmbMotivos");
            String strDocAtatchment=request.getParameter("hdncmbDocAtatchment");
            String strRuta=request.getParameter("hdnav_ruta");
            String strTxtRuta=request.getParameter("hdntxt_ruta");
            String strPorItemid=request.getParameter("hdnPortabitemid");

            strEvalSolBajaL = strEvalSolBaja;
            strMotivosL = strMotivos;
            strDocAtatchmentL = strDocAtatchment;
            strRutaL = strRuta;

            if (!strRuta.equals("")){
                strRutaL = strRuta;
            }else{
                strRutaL = strTxtRuta;
            }

            strPorItemidL = strPorItemid;

            objPortabilityOrderBean.setNpcmbEvalSolBaja(strEvalSolBajaL);
            objPortabilityOrderBean.setNpcmbMotivos(strMotivosL);
            objPortabilityOrderBean.setNpcmbDocAtatchment(strDocAtatchmentL);
            objPortabilityOrderBean.setNpav_ruta(strRutaL);
            objPortabilityOrderBean.setNporderid(hdnOrderId);
            objPortabilityOrderBean.setNpmodificate(hdnLogin);
            objPortabilityOrderBean.setNportabitemid(strPorItemidL);

            strMessage = objPortabilityDAO.updatePortabilityLow(objPortabilityOrderBean,hdnLogin,conn);

        }else if (strTable > 2 ){

            String[] strEvalSolBajaA = request.getParameterValues("hdncmbEvalSolBaja");
            String[] strMotivosA = request.getParameterValues("hdncmbMotivos");
            String[] strDocAtatchmentA = request.getParameterValues("hdncmbDocAtatchment");
            String[] strRutaA = request.getParameterValues("hdnav_ruta");
            String[] strTxtRutaA = request.getParameterValues("hdntxt_ruta");
            String[] strPorItemidA = request.getParameterValues("hdnPortabitemid");

            for (int i=0; i<strEvalSolBajaA.length;i++){

                strEvalSolBajaL = (String)strEvalSolBajaA[i];
                strMotivosL = (String)strMotivosA[i];

                if (strMotivosL.equals("d") ){
                    strMotivosL = null;
                }

                strDocAtatchmentL = (String)strDocAtatchmentA[i];

                if (strDocAtatchmentL.equals("d") ){
                    strDocAtatchmentL = null;
                }

                strRutaL = (String) strRutaA[i];
                strTxtRutaL = (String) strTxtRutaA[i];

                if (strRutaL.equals("d") ){
                    strRutaL = null;
                }else if(!strRutaL.equals("") && (!strTxtRutaL.equals("d")) ){
                    strRutaL = strRutaL;
                }else{
                    strRutaL = strTxtRutaL;
                }

                strPorItemidL = (String) strPorItemidA[i];
                objPortabilityOrderBean = new PortabilityOrderBean();
                objPortabilityOrderBean.setNpcmbEvalSolBaja(strEvalSolBajaL);
                objPortabilityOrderBean.setNpcmbMotivos(strMotivosL);
                objPortabilityOrderBean.setNpcmbDocAtatchment(strDocAtatchmentL);
                objPortabilityOrderBean.setNpav_ruta(strRutaL);
                objPortabilityOrderBean.setNporderid(hdnOrderId);
                objPortabilityOrderBean.setNpmodificate(hdnLogin);
                objPortabilityOrderBean.setNportabitemid(strPorItemidL);
                strMessage = objPortabilityDAO.updatePortabilityLow(objPortabilityOrderBean,hdnLogin,conn);
            }
        }
        return strMessage;
    }

    //--------------------------------------------- Alta


    public String updateSectionPortabilityHigh(RequestHashMap request, Connection conn) throws SQLException, Exception {
        if(logger.isDebugEnabled())
            logger.debug("updateSectionPortabilityHigh");

        HashMap objHashMap = new HashMap();
        HashMap hshPortabItem = new HashMap();

        //HashMap para actualizar los ItemDevice de los Items portados
        HashMap hshItem = new HashMap();
        HashMap hshItemDevice = new HashMap();
        HashMap hshPortItem = new HashMap();

        ArrayList arrPortabItemList = null;
        ArrayList arrItemList = null;
        ArrayList arrItemDeviceList = null;
        ArrayList arrPortItemList = null;
        PortabilityOrderBean objPortabilityOrderBean = new PortabilityOrderBean();
        PortabilityOrderBean objPortabOrderBean = new PortabilityOrderBean();
        PortabilityOrderBean objPortabBean = new PortabilityOrderBean();

        //Bean para actualizar los ItemDevice de los Items portados
        PortabilityOrderBean objItem = new PortabilityOrderBean();
        PortabilityOrderBean objItemDevice = new PortabilityOrderBean();
        PortabilityOrderBean objPortItem = new PortabilityOrderBean();

        PortabilityOrderDAO objPortabilityDAO = new PortabilityOrderDAO();
        String strMessage = null;
        boolean searchOrder = false;

        String hdnLogin=request.getParameter("hdnLogin");
        System.out.println(hdnLogin);
        long hdnOrderId = MiUtil.parseLong((String)request.getParameter("hdnOrderId"));
        System.out.println(hdnOrderId);
        long strCodigoCliente = MiUtil.parseLong((String)request.getParameter("hdnCustomerId"));
        String strSpecification = (String)request.getParameter("hdnSpecificationId");
        String npPortabType = "";
        if(strSpecification.equals("2068")){
            npPortabType = "01";
        }else if(strSpecification.equals("2069")){
            npPortabType = "02";
        }
        System.out.println(strCodigoCliente);


        Date date_device_ini = new Date();
        System.out.println("[PM0010354] INCIO DEL BLOQUE LISTA ITEM DEVICE: "+date_device_ini);

        // Inicio JV001  Se mueve logica java a plsql
        strMessage = objPortabilityDAO.updateValidItemDevice(hdnOrderId,conn);
        if( strMessage!= null)
            return strMessage;
        // Fin JV001  Se mueve logica java a plsql
        Date date_device_fin = new Date();
        System.out.println("[PM0010354] FIN DEL BLOQUE LISTA ITEM DEVICE: "+date_device_fin);


        //Valores de la Cabecera de la Orden de Portabilidad
        //---------------------------------------------------
        String strCedente = request.getParameter("cmbAssignor");
        String strTypeDocument = (request.getParameter("cmbDocumento")==null?request.getParameter("hdnTypeDocumento"):request.getParameter("cmbDocumento"));
        String strDocument =(request.getParameter("txtDocumento")==null?request.getParameter("hdnDocumento"):request.getParameter("txtDocumento"));
        String strCustomersType=request.getParameter("cmbCustomerType");
        String strScheduleDays=request.getParameter("cmbScheduleDays");
        long npPortabOrderId = (long)objPortabilityDAO.getPortabilityOrderId(hdnOrderId,conn);

        //Validaciones de los valores antes de la actualización
        //-----------------------------------------------------
        strMessage = objPortabilityDAO.getValidateDocument(hdnOrderId,strDocument,strTypeDocument,conn);
        System.out.println("strMessage:"+strMessage);
        if( strMessage!= null)
            return strMessage;

        //Actualización de los datos a nivel de cabecera de la Orden de Portabilidad
        //----------------------------------------------------------------------------
        strMessage = objPortabilityDAO.updateSectionHeaderHigh(npPortabOrderId,strCedente,strDocument,strTypeDocument,conn);
        if( strMessage!= null)
            return strMessage;

        //Actualización de los datos a nivel de cabecera de la Orden de Portabilidad 2
        //----------------------------------------------------------------------------
        Date date__ini = new Date();
        System.out.println("[PM0010354] INCIO PORTABILIDAD 2: "+date__ini);

        strMessage = objPortabilityDAO.updateCusTypeScheduleDays(npPortabOrderId, strCustomersType, strScheduleDays, conn);
        if( strMessage!= null)
            return strMessage;

        String strState = ((String)request.getParameter("txtEstadoOrden")).trim();


        if(strState.equals("TIENDA01") || strState.equals("ADM_VENTAS") || strState.equals("VENTAS")){

            long strTable  = MiUtil.parseLong(request.getParameter("hdntblListaPortabilityHigh"));


            if (strTable == 2){
                long strItemId = MiUtil.parseLong(request.getParameter("hddItemId"));
                long strItemDeviceId = MiUtil.parseLong(request.getParameter("hdnItemDeviceId"));
                String strPhoneNumber = request.getParameter("txtphonenumber");
                String strModalitySell = request.getParameter("hdnModalitySell");
                String strModalityCont = request.getParameter("cmbmodalidadcont");
                String strProductLineId = request.getParameter("hdnProductLineId");
                String strUfmi = request.getParameter("hdnUfmi");//Se obtiene de request el numero de radio UFMI reservado - FPICOY 28/10/2010
                String strUfmiId = request.getParameter("hdnUfmiId");
                boolean searchItem1 = false;
                boolean searchItem2 = false;

                if(!strPhoneNumber.equals("")){

                    if(!strModalitySell.equals("Propio") && !strModalitySell.equals("Alquiler en Cliente") && !strProductLineId.equals("11")){

                        hshPortabItem = (HashMap)objPortabilityDAO.getPortabItemDevList(hdnOrderId,conn);
                        arrPortabItemList = new ArrayList();
                        arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");

                        searchItem1 = (boolean)objPortabilityDAO.searchPortabilityItem(npPortabOrderId,strItemId,strItemDeviceId,conn);

                        for(int i=0; i<arrPortabItemList.size(); i++){
                            objPortabBean = new PortabilityOrderBean();
                            objPortabBean = (PortabilityOrderBean)arrPortabItemList.get(i);
                            if(strItemDeviceId == objPortabBean.getNpItemDeviceId()){
                                searchItem2 = true;
                                break;
                            }else{
                                searchItem2 = false;
                            }
                        }
                    }else{

                        searchItem2 = (boolean)objPortabilityDAO.searchItem(strItemId,conn);

                        searchItem1 = (boolean)objPortabilityDAO.searchPortabilityItem1(npPortabOrderId,strItemId,conn);

                    }

                    objPortabilityOrderBean.setNpItemid(strItemId);
                    objPortabilityOrderBean.setNpItemDeviceId(strItemDeviceId);
                    objPortabilityOrderBean.setNpPhoneNumber(strPhoneNumber);
                    objPortabilityOrderBean.setNpModalityContract(strModalityCont);
                    objPortabilityOrderBean.setNpAssignor(strCedente);
                    objPortabilityOrderBean.setNpOrderid(hdnOrderId);
                    objPortabilityOrderBean.setNpCustomerId(strCodigoCliente);
                    objPortabilityOrderBean.setNpCreateBy(hdnLogin);
                    objPortabilityOrderBean.setNpPortabType(npPortabType);
                    //Se setean en el bean los valores obtenidos del numero de radio reservado - Inicio FPICOY 28/10/2010
                    objPortabilityOrderBean.setNpUfmi(strUfmi);
                    if ("".equals(strUfmi)) {
                        objPortabilityOrderBean.setNpRqstdUfmi(Constante.UFMI_NO_RESERVADO);
                    } else {
                        objPortabilityOrderBean.setNpRqstdUfmi(Constante.UFMI_RESERVADO);
                    }
                    //Fin FPICOY

                    if(searchItem1 && searchItem2){

                        strMessage = objPortabilityDAO.updateSectionPortabItemHigh(npPortabOrderId,objPortabilityOrderBean,conn);

                    }else if(searchItem1 && !searchItem2){

                        strMessage = objPortabilityDAO.deleteSectionPortabItemHigh(npPortabOrderId,objPortabilityOrderBean,conn);

                    }else if(!searchItem1 && searchItem2){

                        strMessage = objPortabilityDAO.insertSectionPortabItemHigh(objPortabilityOrderBean,npPortabOrderId,conn);

                    }

                }

                System.out.println("termina 1");
            }else if (strTable > 2 ){

                String[] strItemId = request.getParameterValues("hddItemId");
                String[] strItemDeviceId = request.getParameterValues("hdnItemDeviceId");
                String[] strPhoneNumber = request.getParameterValues("txtphonenumber");
                String[] strModalitySell = request.getParameterValues("hdnModalitySell");
                String[] strModalityCont = request.getParameterValues("cmbmodalidadcont");
                String[] strProductLineId = request.getParameterValues("hdnProductLineId");
                String[] strPortabOrderId = request.getParameterValues("hddPortabOrderid");
                String[] strUfmi = request.getParameterValues("hdnUfmi");//Se obtiene de request el numero de radio UFMI reservado - FPICOY 28/10/2010

                for (int i=0; i<strItemId.length;i++){

                    long strItemIdH = MiUtil.parseLong((String)strItemId[i]);
                    long strItemDeviceIdH = MiUtil.parseLong((String)strItemDeviceId[i]);
                    String strPhoneNumberH = (String)strPhoneNumber[i];
                    String strModalityContH = (String)strModalityCont[i];
                    String strPortabOrderIdH = (String)strPortabOrderId[i];
                    //Indica si la modalidad de venta es (Venta, Alquiler, Alquiler de Cliente o Propio)
                    String strModalitySellH = (String)strModalitySell[i];
                    String strProductLineIdH = (String)strProductLineId[i];
                    String strUfmiH = (String)strUfmi[i];//Se obtiene de request el numero de radio UFMI reservado - FPICOY 28/10/2010

                    boolean searchItem1 = false;
                    boolean searchItem2 = false;

                    if(!strPhoneNumberH.equals("")){

                        if(!strModalitySellH.equals("Propio") && !strModalitySellH.equals("Alquiler en Cliente") && !strProductLineIdH.equals("11")){

                            hshPortabItem = (HashMap)objPortabilityDAO.getPortabItemDevList(hdnOrderId,conn);
                            arrPortabItemList = new ArrayList();
                            arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");

                            searchItem1 = (boolean)objPortabilityDAO.searchPortabilityItem(npPortabOrderId,strItemIdH,strItemDeviceIdH,conn);

                            for(int j=0; j<arrPortabItemList.size(); j++){
                                objPortabBean = new PortabilityOrderBean();
                                objPortabBean = (PortabilityOrderBean)arrPortabItemList.get(j);
                                long npItemDevId = (long)objPortabBean.getNpItemDeviceId();
                                if(strItemDeviceIdH == npItemDevId){
                                    searchItem2 = true;
                                    break;
                                }else{
                                    searchItem2 = false;
                                }
                            }

                        }else{

                            searchItem2 = (boolean)objPortabilityDAO.searchItem(strItemIdH,conn);

                            searchItem1 = (boolean)objPortabilityDAO.searchPortabilityItem1(npPortabOrderId,strItemIdH,conn);

                        }

                        objPortabilityOrderBean.setNpItemid(strItemIdH);
                        objPortabilityOrderBean.setNpItemDeviceId(strItemDeviceIdH);
                        objPortabilityOrderBean.setNpPhoneNumber(strPhoneNumberH);
                        objPortabilityOrderBean.setNpModalityContract(strModalityContH);
                        objPortabilityOrderBean.setNpAssignor(strCedente);
                        objPortabilityOrderBean.setNpOrderid(hdnOrderId);
                        objPortabilityOrderBean.setNpCustomerId(strCodigoCliente);
                        objPortabilityOrderBean.setNpCreateBy(hdnLogin);
                        objPortabilityOrderBean.setNpPortabType(npPortabType);
                        //Se setean en el bean los valores obtenidos del numero de radio reservado - Inicio FPICOY - 28/10/2010
                        objPortabilityOrderBean.setNpUfmi(strUfmiH);
                        if ("".equals(strUfmiH)) {
                            objPortabilityOrderBean.setNpRqstdUfmi(Constante.UFMI_NO_RESERVADO);
                        } else {
                            objPortabilityOrderBean.setNpRqstdUfmi(Constante.UFMI_RESERVADO);
                        }
                        //Fin FPICOY
                        if(searchItem1 && searchItem2){

                            strMessage = objPortabilityDAO.updateSectionPortabItemHigh(npPortabOrderId,objPortabilityOrderBean,conn);

                        }else if(searchItem1 && !searchItem2){

                            strMessage = objPortabilityDAO.deleteSectionPortabItemHigh(npPortabOrderId,objPortabilityOrderBean,conn);

                        }else if(!searchItem1 && searchItem2){

                            strMessage = objPortabilityDAO.insertSectionPortabItemHigh(objPortabilityOrderBean,npPortabOrderId,conn);

                        }
                    }
                }

                System.out.println("termina 2");
            }
        }
        Date date__FIN = new Date();
        System.out.println("[PM0010354] INCIO PORTABILIDAD 2: "+date__FIN);

        return strMessage;

    }


    public String insertSectionPortabilityHigh(RequestHashMap request, Connection conn) throws SQLException, Exception {
        if(logger.isDebugEnabled())
            logger.debug("insertSectionPortabilityHigh");

        HashMap objHashMap = new HashMap();
        PortabilityOrderDAO objPortabilityDAO = new PortabilityOrderDAO();
        PortabilityOrderBean objPortabOrderBean = new PortabilityOrderBean();
        String strMessage = null;
        boolean searchOrder = false;

        String hdnLogin=request.getParameter("hdnSessionLogin");
        System.out.println(hdnLogin);
        long hdnOrderId = MiUtil.parseLong((String)request.getParameter("hdnNumeroOrder"));
        System.out.println(hdnOrderId);
        long strCodigoCliente = MiUtil.parseLong((String)request.getParameter("txtCompanyId"));
        String strSpecification = (String)request.getParameter("hdnSpecification");
        String strCedente = request.getParameter("cmbAssignor");
        //Buscamos si existe la orden en la tabla np_portability_order.
        searchOrder = (boolean)objPortabilityDAO.searchPortabilityOrder(hdnOrderId,conn);
        String strTypeDocument =(request.getParameter("cmbDocumento")==null?request.getParameter("hdnTypeDocumento"):request.getParameter("cmbDocumento"));
        String strDocument =(request.getParameter("txtDocumento")==null?request.getParameter("hdnDocumento"):request.getParameter("txtDocumento"));

        if(!searchOrder){

            objPortabOrderBean.setNpCreateBy(hdnLogin);
            objPortabOrderBean.setNpOrderid(hdnOrderId);
            objPortabOrderBean.setNpCustomerId(strCodigoCliente);
            objPortabOrderBean.setNpAssignor(strCedente);
            objPortabOrderBean.setNpTypeDocument(strTypeDocument);
            objPortabOrderBean.setNpDocument(strDocument);

            //Validaciones de los valores antes de la inserción
            //-----------------------------------------------------
            strMessage = objPortabilityDAO.getValidateDocument(hdnOrderId,strDocument,strTypeDocument,conn);
            System.out.println("strMessage:"+strMessage);
            if( strMessage!= null)
                return strMessage;


            objHashMap = (HashMap)objPortabilityDAO.insertSectionPortabOrderHigh(objPortabOrderBean,conn);
            strMessage = (String)objHashMap.get("strMessage");

        }

        return strMessage;
    }

    /*******************************************************************************************************
     Method : updateSectionPortabilityReturn
     Purpose: Actualiza los items de Portabilidad de Retorno
     Developer       Fecha       Comentario
     =============   ==========  ===========================================================================
     Karen Salvador  02/09/2009  Creación
     ********************************************************************************************************/

    public String updateSectionPortabilityReturn(RequestHashMap request, Connection conn) throws SQLException, Exception {

        String    pn_order_id                   = request.getParameter("hdnNumeroOrder");
        String    pv_session_login              = request.getParameter("hdnSessionLogin");
        String[]  pv_item_port_cantidad         = request.getParameterValues("hdnIndicePort");

        PortabilityItemDAO objPortabilityItemDAO = new PortabilityItemDAO();
        HashMap objHashMap = new HashMap();
        String strMessage = null;

        if( pv_item_port_cantidad != null ){

            int cantItemsPort = pv_item_port_cantidad.length;

            for(int i=0; i<cantItemsPort; i++){

                //Id del Item de Portabilidad
                //---------------------------
                String[]  pv_item_portability_id        = request.getParameterValues("hdnItemPortId");

                //Id de la Orden de Portabilidad
                //------------------------------
                String[]  pv_order_portability_id        = request.getParameterValues("hdnOrderPortId");

                //Observaciones
                //--------------
                String[]  pv_item_comment               = request.getParameterValues("txtS");

                PortabilityItemBean  objPortabilityItemBean = null;
                objPortabilityItemBean = new PortabilityItemBean();

                objPortabilityItemBean.setNpPortabItemId(MiUtil.parseLong(MiUtil.getStringObject(pv_item_portability_id,i)));
                objPortabilityItemBean.setNpPortabOrderId(MiUtil.parseLong(MiUtil.getStringObject(pv_order_portability_id,i)));
                objPortabilityItemBean.setNpComment(MiUtil.getStringObject(pv_item_comment,i));
                objPortabilityItemBean.setNpmodificationdate(MiUtil.getDateBD("dd/MM/yyyy"));
                objPortabilityItemBean.setNpmodificationby(pv_session_login);

                strMessage = (String)objPortabilityItemDAO.doUpdateItemPortabilityReturn(objPortabilityItemBean,conn);
                if( strMessage!= null)
                    return strMessage;

            }
        }

        return null;

    }
}  