package pe.com.nextel.service;

import org.apache.commons.lang.StringUtils;
import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderRequestType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.listardocumentosgenerados.v1.*;
import pe.com.entel.esb.message.gestiondocumentosdigitales.obtenerdocumentoordenretail.v1.*;
import pe.com.nextel.bean.DocumentoDigitalLinkBean;
import pe.com.nextel.bean.OrdenDocDigitalBean;
import pe.com.nextel.ejb.SEJBGestionDocumentoDigitalesRemote;
import pe.com.nextel.ejb.SEJBGestionDocumentoDigitalesRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by LROQUE on 28/10/2016.
 * [PM0011173] LROQUE
 */
public class GestionDocumentoDigitalesService {
    public static SEJBGestionDocumentoDigitalesRemote getSEJBGestionDocumentoDigitalesRemote() {
        SEJBGestionDocumentoDigitalesRemote sejbGestionDocumentoDigitalesRemote = null;
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBGestionDocumentoDigitalesRemoteHome sejbBiometricaNewRemoteHome =
                    (SEJBGestionDocumentoDigitalesRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBGestionDocumentoDigitales"), SEJBGestionDocumentoDigitalesRemoteHome.class);
            sejbGestionDocumentoDigitalesRemote = sejbBiometricaNewRemoteHome.create();

            return sejbGestionDocumentoDigitalesRemote;
        } catch (Exception ex) {
            System.out.println("Exception : [GestionDocumentoDigitalesService][getSEJBGestionDocumentoDigitalesRemote][" + ex.getMessage() + "]");
        }
        return sejbGestionDocumentoDigitalesRemote;
    }

    public Map<String, Object> listarDocumentosGenerados(String idOrden, String userLogin) throws Exception{
        Map<String, Object> mapResult = new HashMap<String, Object>();
        String traza= "[GestionDocumentoDigitalesService][listarDocumentosGenerados] ";
        try{
            if(StringUtils.isBlank(idOrden)){
                System.out.println(traza + "INICIO");
                System.out.println(traza + "orden vacia");
            }else{
                traza= traza+"[idOrden="+idOrden+"] ";
                System.out.println(traza + "INICIO");

                ListarDocumentosGeneradosRequestType request = new ListarDocumentosGeneradosRequestType();
                request.setIdOrden(idOrden);

                HeaderRequestType header = getHeaeder(userLogin);

                Map<String, Object> obDocDig = getSEJBGestionDocumentoDigitalesRemote().listarDocumentosGenerados(request, header);
                String messageError = (String)obDocDig.get("messageError");

                if(StringUtils.isBlank(messageError)){
                    ListarDocumentosGeneradosResponseType response = (ListarDocumentosGeneradosResponseType)obDocDig.get("result");
                    if(response != null){
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        System.out.println(traza + "CodResp: "+codResp);

                        if(Constante.CODIGO_RESP_OSB_OK.equals(codResp)){
                            List<ListaDocumentoItemType> listItem = response.getResponseData().getListaDocumentos().getListaDocumentoItem();
                            System.out.println(traza + "Nro de documentos digitales: "+listItem.size());

                            if(listItem.size() > 0 ){
                                List<DocumentoDigitalLinkBean> list = new ArrayList<DocumentoDigitalLinkBean>();
                                for(ListaDocumentoItemType item : listItem){
                                    DocumentoDigitalLinkBean ob = new DocumentoDigitalLinkBean();
                                    ob.setIdDocAcepta(item.getIdDocumentoAcepta());
                                    ob.setNombreDoc(item.getNombreDocumento());

                                    list.add(ob);
                                }

                                mapResult.put("listDocumentos", list);
                            }
                        }
                    }
                }
            }
        }catch(RemoteException e){
            System.out.println(traza + "RemoteException: "+e.getMessage());
        }catch(Exception e) {
            System.out.println(traza + "Exception: "+e.getMessage());
        }finally {
            System.out.println(traza + "FIN");
        }

        return mapResult;
    }

    public Map<String, Object> verDocumentoDigital(String idAceptaDoc, String userLogin) throws Exception{
        Map<String, Object> mapResult = new HashMap<String, Object>();
        String traza= "[GestionDocumentoDigitalesService][verDocumentoDigital] ";
        try{
            if(StringUtils.isBlank(idAceptaDoc)){
                System.out.println(traza + "INICIO");
                System.out.println(traza + "idAceptaDocumento es vacio");
            }else{
                traza= traza+"[idAceptaDoc="+idAceptaDoc+"] ";
                System.out.println(traza + "INICIO");

                ObtenerDocumentoOrdenRetialRequestType request = new ObtenerDocumentoOrdenRetialRequestType();
                request.setIdDocumento(idAceptaDoc);

                HeaderRequestType header = getHeaeder(userLogin);

                Map<String, Object> obDocDig = getSEJBGestionDocumentoDigitalesRemote().verDocumentoDigital(request, header);

                String messageError = (String)obDocDig.get("messageError");

                if(StringUtils.isBlank(messageError)){
                    ObtenerDocumentoOrdenRetialResponseType response = (ObtenerDocumentoOrdenRetialResponseType)obDocDig.get("result");

                    if(response != null){
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        System.out.println(traza + "CodResp: "+codResp);

                        if(Constante.CODIGO_RESP_OSB_OK.equals(codResp)){
                            byte[] contenidoPDF = response.getResponseData().getContenido();
                            mapResult.put("contenidoPDF", contenidoPDF);
                        }
                    }
                }
            }
        }catch(RemoteException e){
          System.out.println(traza + "RemoteException: "+e.getMessage());
        }catch(Exception e) {
            System.out.println(traza + "Exception: "+e.getMessage());
        }finally {
            System.out.println(traza + "FIN");
        }

        return mapResult;
    }

    public Map<String, Object> listarOrdenesDocDigitales(String customerId){
        Map<String, Object> mapResult = new HashMap<String, Object>();
        String traza = "[GestionDocumentoDigitalesService][listarOrdenesDocDigitales] ";
        try{
            if(StringUtils.isBlank(customerId)){
                System.out.println(traza + "INICIO");
                System.out.println(traza + "customerId es vacio");
            }else{
                traza = traza+"[customerId="+customerId+"] ";
                System.out.println(traza + "INICIO");

                Long lCustomerId = new Long(customerId);

                Map<String, Object> mapOrdenes = getSEJBGestionDocumentoDigitalesRemote().getOrdenesDocDigitalesList(lCustomerId);
                String messageError = (String)mapOrdenes.get("messageError");

                if(StringUtils.isBlank(messageError)){
                    List<OrdenDocDigitalBean> list = (ArrayList<OrdenDocDigitalBean>)mapOrdenes.get("result");
                    System.out.println(traza + "Nro de Ordenes: "+list.size());

                    mapResult.put("listOrdenes", list);
                }else{
                    System.out.println(traza + "messageError: "+messageError);
                }
            }
        }catch(RemoteException e){
            System.out.println(traza + "RemoteException: "+e.getMessage());
        }catch(Exception e) {
            System.out.println(traza + "Exception: "+e.getMessage());
        }finally {
            System.out.println(traza +"FIN");
        }

        return mapResult;
    }

    private HeaderRequestType getHeaeder(String usuario){
        HeaderRequestType header = new HeaderRequestType();

        header.setCanal(Constante.Source_CRM);
        header.setIdAplicacion(new Long(Constante.ID_APLICACION_CMR));
        header.setUsuario(usuario);
        header.setIdTransaccionESB("1");
        header.setIdTransaccionNegocio("1");
        header.setNodoAdicional(null);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date;
        try {
            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            header.setFechaInicio(date);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return header;
    }
}
