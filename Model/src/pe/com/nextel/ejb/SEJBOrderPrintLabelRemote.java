package pe.com.nextel.ejb;

import javax.ejb.EJBObject;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Jorge Gabriel on 07/08/2015.
 */
public interface SEJBOrderPrintLabelRemote extends EJBObject {
     /**
     Method : valOrderPrintLabel
     *
     * Motivo: Valida y obtiene datos de la orden para impresion de etiquetas.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 07/08/2015
     * @param lOrderId ID de la Orden
     * @return HashMap que conbtiene datos para la impresion de etiqueta.
     */
    public HashMap valOrderPrintLabel(long lOrderId) throws SQLException, Exception;

    /**
     *
     * @throws java.lang.Exception
     * @throws java.sql.SQLException
     * @return String, mensaje de error.
     * @param lOrderId
     * @param lSpecificationId
     * @param lItemsIds
     * @param lCustomerId
     * @param lCustomerName
     */
    public String sendPrintLabels(long lOrderId, long lSpecificationId, String lItemsIds, long lCustomerId, String lCustomerName, String wsLogin,long buildingId) throws SQLException, Exception;
}
