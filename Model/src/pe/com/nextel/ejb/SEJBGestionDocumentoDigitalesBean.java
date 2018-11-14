package pe.com.nextel.ejb;

import org.apache.commons.lang.StringUtils;
import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderRequestType;
import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderResponseType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.listardocumentosgenerados.v1.ListarDocumentosGeneradosRequestType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.listardocumentosgenerados.v1.ListarDocumentosGeneradosResponseType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.obtenerdocumentoordenretail.v1.ObtenerDocumentoOrdenRetialRequestType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.obtenerdocumentoordenretail.v1.ObtenerDocumentoOrdenRetialResponseType;
import pe.com.entel.esb.venta.gestiondocumentosdigitales.v1.EntelFault;
import pe.com.entel.esb.venta.gestiondocumentosdigitales.v1.GestionDocumentosDigitalesPort;
import pe.com.entel.esb.venta.gestiondocumentosdigitales.v1.GestionDocumentosDigitalesSOAP11BindingQSService;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.ws.Holder;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrador on 25/10/2016.
 */
public class SEJBGestionDocumentoDigitalesBean extends GenericObject implements SessionBean {

    private SessionContext _context;
    private CustomerDAO objCustomerDAO = null;

    public void ejbCreate() {
        objCustomerDAO = new CustomerDAO();
    }

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        _context = ctx;
    }

    public void ejbRemove() throws EJBException, RemoteException {
        System.out.println("[SEJBGestionDocumentoDigitalesBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException, RemoteException {
        System.out.println("[SEJBGestionDocumentoDigitalesBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        System.out.println("[SEJBGestionDocumentoDigitalesBean][ejbPassivate()]");
    }

    public Map<String, Object> listarDocumentosGenerados(ListarDocumentosGeneradosRequestType request, HeaderRequestType header) throws RemoteException, Exception{
        Map<String, Object> hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBGestionDocumentoDigitalesBean][listarDocumentosGenerados][idOrden="+request.getIdOrden()+"] ";
        System.out.println(traza + "INICIO");

        ListarDocumentosGeneradosResponseType response = null;
        String messageError = null;

        long timeInicio = System.currentTimeMillis();
        try{
            System.out.println(traza + "XML [REQUEST]: " + MiUtil.transfromarAnyObjectToXmlText(request));

            GestionDocumentosDigitalesSOAP11BindingQSService service = new GestionDocumentosDigitalesSOAP11BindingQSService();
            GestionDocumentosDigitalesPort port = service.getGestionDocumentosDigitalesSOAP11BindingQSPort();

            Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();
            response = port.listarDocumentosGenerados(request, header, holder);

            hshResultMap.put("result", response);
        }catch(EntelFault e){
            messageError = (e.getFaultInfo() == null) ? Constante.CADENA_VACIA : StringUtils.defaultString(e.getFaultInfo().getDescripcionError());
            String codError = (e.getFaultInfo() == null) ? Constante.CADENA_VACIA : StringUtils.defaultString(e.getFaultInfo().getCodigoError());

            System.out.println(traza + "EntelFault. OSB GestionDocumentoDigitales: ["+codError +"] - ["+messageError+"]");
        }catch(Exception e){
            messageError = (e.getCause()==null) ? e.getMessage() : e.getCause().toString();
            System.out.println(traza + "Exception. OSB GestionDocumentoDigitales: "+messageError);
        }finally{
            System.out.println(traza + "XML [RESPONSE]: "+ MiUtil.transfromarAnyObjectToXmlText(response));
            System.out.println(traza + "Tiempo de procesamiento: "+(System.currentTimeMillis() - timeInicio));
            System.out.println(traza + "FIN");
        }

        hshResultMap.put("messageError", messageError);

        return hshResultMap;
    }

    public Map<String, Object> verDocumentoDigital(ObtenerDocumentoOrdenRetialRequestType request, HeaderRequestType header) throws RemoteException, Exception{
        Map<String, Object> hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBGestionDocumentoDigitalesBean][verDocumentoDigital][idDocumento="+request.getIdDocumento()+"] ";
        System.out.println(traza + "INICIO");

        String messageError = null;
        ObtenerDocumentoOrdenRetialResponseType response = null;

        long timeInicio = System.currentTimeMillis();
        try{
            System.out.println(traza + "XML [REQUEST]: " + MiUtil.transfromarAnyObjectToXmlText(request));

            GestionDocumentosDigitalesSOAP11BindingQSService service = new GestionDocumentosDigitalesSOAP11BindingQSService();
            GestionDocumentosDigitalesPort port = service.getGestionDocumentosDigitalesSOAP11BindingQSPort();

            Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();
            response = port.obtenerDocumentoOrdenRetial(request, header, holder);

            hshResultMap.put("result", response);
        }catch(EntelFault e){
            messageError = (e.getFaultInfo() == null) ? Constante.CADENA_VACIA : StringUtils.defaultString(e.getFaultInfo().getDescripcionError());
            String codError = (e.getFaultInfo() == null) ? Constante.CADENA_VACIA : StringUtils.defaultString(e.getFaultInfo().getCodigoError());

            System.out.println(traza + "EntelFault. OSB GestionDocumentoDigitales: ["+codError +"] - ["+messageError+"]");
        }catch(Exception e){
            messageError = (e.getCause()==null ? e.getMessage() : e.getCause().toString());
            System.out.println(traza + "Exception. OSB GestionDocumentoDigitales: "+messageError);
        }finally{
            System.out.println(traza + "XML [RESPONSE]: "+ MiUtil.transfromarAnyObjectToXmlText(response));
            System.out.println(traza + "Tiempo de procesamiento: "+(System.currentTimeMillis() - timeInicio));
            System.out.println(traza + "FIN");
        }

        hshResultMap.put("messageError", messageError);

        return hshResultMap;
    }

    public Map<String, Object> getOrdenesDocDigitalesList(Long idCustomer) throws RemoteException, Exception {
        return objCustomerDAO.getOrdenesDocDigitalesList(idCustomer);
    }

}
