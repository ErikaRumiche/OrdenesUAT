package pe.com.nextel.ejb;

import pe.com.entel.esb.data.generico.entelgenericheader.v1.HeaderRequestType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.listardocumentosgenerados.v1.ListarDocumentosGeneradosRequestType;
import pe.com.entel.esb.message.gestiondocumentosdigitales.obtenerdocumentoordenretail.v1.ObtenerDocumentoOrdenRetialRequestType;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Administrador on 25/10/2016.
 */
public interface SEJBGestionDocumentoDigitalesRemote extends EJBObject {

    public Map<String, Object> listarDocumentosGenerados(ListarDocumentosGeneradosRequestType request, HeaderRequestType header) throws RemoteException, Exception;
    public Map<String, Object> verDocumentoDigital(ObtenerDocumentoOrdenRetialRequestType request, HeaderRequestType header) throws RemoteException, Exception;
    public Map<String, Object> getOrdenesDocDigitalesList(Long idCustomer) throws RemoteException, Exception;

}
