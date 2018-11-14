package pe.com.nextel.service;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ImeiSimBean;
import pe.com.nextel.bean.LoadMassiveItemBean;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.ejb.SEJBOrderEditRemote;
import pe.com.nextel.ejb.SEJBOrderEditRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class UploadService extends GenericService {

    public HashMap manageUploadedFilesByMassivePropio(List items) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		HashMap hshDataMap = new HashMap();
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (!item.isFormField()) {
				parseContentCSVByMassivePropio(item, hshDataMap);
			}
		}
		return hshDataMap;
	}
  
  private void parseContentCSVByMassivePropio(FileItem item, HashMap hshDataMap) {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		ArrayList arrImeiSimList = new ArrayList();
		if (isValidFile(item)) {
			try {
				List lstLines = IOUtils.readLines(item.getInputStream());
				for (int i = 0; i < lstLines.size(); i++) {
					generateSim(arrImeiSimList, (String) lstLines.get(i), hshDataMap);
				}
			} catch (IOException ioe) {
				hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(ioe));
			}
			hshDataMap.put("arrImeiSimList", arrImeiSimList);
		} else {
			hshDataMap.put(Constante.MESSAGE_OUTPUT, "No es un archivo válido");
		}
	}
  
  private void generateSim(ArrayList arrImeiSimList, String strLine, HashMap hshDataMap) {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		ImeiSimBean imeiSimBean = new ImeiSimBean();
		String[] saImeiSim = StringUtils.splitPreserveAllTokens(strLine, ",");
		if (isValidInputs(saImeiSim,1)) {
			/*imeiSimBean.setImei(saImeiSim[0]);
			imeiSimBean.setSim(saImeiSim[1]);*/
      imeiSimBean.setSim(saImeiSim[0]);
			arrImeiSimList.add(imeiSimBean);
		} else {
			hshDataMap.put(Constante.MESSAGE_OUTPUT, "El archivo debe tener 1 columna");
		}
	}

	private boolean isValidInputs(String[] saImeiSim, int intQuantityLength) {
		return saImeiSim.length == intQuantityLength;
	}
  
  /**************************************************************************
   * 
   * RDELOSREYES - INICIO - Carga Masiva IMEI/SIM   31/10/2008
   * 
   * *************************************************************************/
   
    /**
    * Motivo: Obtiene la instancia del EJB remoto
    * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
    * <br>Fecha: 13/09/2007
    * @return    SEJBOrderEditRemote
    */     
    public static SEJBOrderEditRemote getSEJBOrderEditRemote() {
       try{
          final Context context = MiUtil.getInitialContext();
          final SEJBOrderEditRemoteHome sEJBOrderEditRemoteHome = 
             (SEJBOrderEditRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderEdit" ), SEJBOrderEditRemoteHome.class );
          SEJBOrderEditRemote sEJBOrderEditRemote;
          sEJBOrderEditRemote = sEJBOrderEditRemoteHome.create();             
          return sEJBOrderEditRemote;
       }catch(Exception ex) {
          System.out.println("Exception : [EditOrderService][getSEJBOrderEditRemote]["+ex.getMessage()+"]");
          return null;
       }
    }     
   
  public HashMap validateMassiveImeiSim(LoadMassiveItemBean loadMassiveItemBean) throws Exception{
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBOrderEditRemote().validateMassiveImeiSim(loadMassiveItemBean);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }        

	public HashMap manageUploadedFiles(List items, Long lOrderId, Long lDispatchPlace) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		HashMap hshDataMap = new HashMap();
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (!item.isFormField()) {
				hshDataMap = parseContentCSV(item, lOrderId, lDispatchPlace, hshDataMap);
				//writeTemporalFile(item);
			}
		}
		return hshDataMap;
	}

	/**
	 * @deprecated
	 * @param item
	 */
	protected void writeTemporalFile(FileItem item) {
		final String RUTA_ARCHIVO_TEMPORAL = "C:\\\\archivos\\";
		try {
			String strFileName = RUTA_ARCHIVO_TEMPORAL + FilenameUtils.getName(item.getName());
			item.write(new File(strFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap parseContentCSV(FileItem item, Long lOrderId, Long lDispatchPlace, HashMap hshDataMap) {
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		LoadMassiveItemBean loadMassiveItemBean = new LoadMassiveItemBean();
		loadMassiveItemBean.setLOrderId(lOrderId);
		loadMassiveItemBean.setLDispatchPlace(lDispatchPlace);
		OrderDAO orderDAO = new OrderDAO();
		if (isValidFile(item)) {
			try {
				List lstLines = IOUtils.readLines(item.getInputStream());
				for (int i = 0; i < lstLines.size(); i++) {
					generateImeiSim(loadMassiveItemBean, (String) lstLines.get(i), hshDataMap);
				}
        if(validateDistinctImeiSimList(loadMassiveItemBean)) {
          hshDataMap = validateMassiveImeiSim(loadMassiveItemBean);
        } else {
          hshDataMap.put("loadMassiveItemBean", loadMassiveItemBean);
        }
			} catch (IOException ioe) {
        ioe.printStackTrace();
				hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(ioe));
			} catch (SQLException sqle) {
        sqle.printStackTrace();
				hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
			} catch (Exception e) {
        e.printStackTrace();
				hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(e));
			}
		} else {
			hshDataMap.put(Constante.MESSAGE_OUTPUT, "No es un archivo válido");
		}
		return hshDataMap;
	}

	private void generateImeiSim(LoadMassiveItemBean loadMassiveItemBean, String strLine, HashMap hshDataMap) {
		/*
		if (logger.isDebugEnabled())
			logger.debug("Inicio");
		*/
		String[] saImeiSim = StringUtils.splitPreserveAllTokens(strLine, ",");
		if (isValidInputs(saImeiSim)) {
			loadMassiveItemBean.getImei().add(saImeiSim[0].trim());
			loadMassiveItemBean.getSim().add(saImeiSim[1].trim());
			loadMassiveItemBean.getMessage().add("");
		} else {
			loadMassiveItemBean.getImei().add("");
			loadMassiveItemBean.getSim().add("");
			loadMassiveItemBean.getMessage().add("El formato de línea debe ser: [IMEI,SIM]. Entrada actual: " + strLine);
      loadMassiveItemBean.agregarErroneo();
		}
	}
  
  private boolean validateDistinctImeiSimList(LoadMassiveItemBean loadMassiveItemBean) {
    boolean flag = true;
    int iOcurrencias = 0;
    for(int i=0; i<loadMassiveItemBean.getImei().size(); i++) {
      boolean detectedError = false;
      if(StringUtils.isNotBlank(loadMassiveItemBean.getImei(i))) {
        iOcurrencias = CollectionUtils.cardinality(loadMassiveItemBean.getImei(i), loadMassiveItemBean.getImei());
        if(iOcurrencias > 1) {
          flag = false;
          loadMassiveItemBean.getMessage().set(i, "El presente IMEI se encuentra repetido, por favor Verificar.");
          detectedError = true;
        }
      }
      if(StringUtils.isNotBlank(loadMassiveItemBean.getSim(i))) {
        iOcurrencias = CollectionUtils.cardinality(loadMassiveItemBean.getSim(i), loadMassiveItemBean.getSim());
        if(iOcurrencias > 1) {
          flag = false;
          if(StringUtils.isNotBlank(loadMassiveItemBean.getMessage(i))) {
            StringBuffer sbMessage = new StringBuffer();
            sbMessage.append(loadMassiveItemBean.getMessage(i));
            sbMessage.append("<br/>");
            sbMessage.append("El presente SIM se encuentra repetido, por favor Verificar.");
            loadMassiveItemBean.getMessage().set(i, sbMessage.toString());
          } else {
            loadMassiveItemBean.getMessage().set(i, "El presente SIM se encuentra repetido, por favor Verificar.");
          }
          detectedError = true;
        }
      }
      if(detectedError) {
        loadMassiveItemBean.agregarErroneo();
      }
    }
    return flag;
  }

	private boolean isValidFile(FileItem item) {
		String strFileName = item.getName();
		return StringUtils.isNotBlank(strFileName) && item.getSize() > 0 && FilenameUtils.isExtension(strFileName.toLowerCase(), "csv");
	}

	private boolean isValidInputs(String[] saImeiSim) {
		return saImeiSim.length == 2;
	}

}
