package pe.com.portability.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.service.GenericService;
import pe.com.nextel.util.MiUtil;
import pe.com.portability.ejb.SEJBPortabilityGeneralRemote;
import pe.com.portability.ejb.SEJBPortabilityGeneralRemoteHome;


public class PortabilityGeneralService  extends GenericService 
{
  private static String newLine = "<br>";
	private static String tabSpace = "&nbsp;&nbsp;&nbsp;&nbsp;";
  
  public PortabilityGeneralService()
  {
  }
  
  public static SEJBPortabilityGeneralRemote getSEJBPortabilityGeneralRemote() {
		try {
			final Context context = MiUtil.getInitialContext();


      final SEJBPortabilityGeneralRemoteHome sEJBPortabilityGeneraRemotelHome =          
      (SEJBPortabilityGeneralRemoteHome) PortableRemoteObject.narrow( context.lookup("SEJBPortabilityGeneral"), SEJBPortabilityGeneralRemoteHome.class );
      SEJBPortabilityGeneralRemote sEJBPortabilityGeneralRemote;
      sEJBPortabilityGeneralRemote = sEJBPortabilityGeneraRemotelHome.create();

			return sEJBPortabilityGeneralRemote;
		}
		catch(Exception e) {
			logger.error(formatException(e));
      System.out.println("Exception : "+ e.getMessage()+"]");
      return null;
		}
	}
  
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha:09/08/2009
   * @see pe.com.portability.dao.GeneralDAO#getSectionDocumentValidate(String,String,String)      
   */    
   public HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) {      
      try {
        return getSEJBPortabilityGeneralRemote().getSectionDocumentValidate(strNptable,strNpDescripcion,strNpvalue);
      }catch(RemoteException ex){
        System.out.println("[PortabilityGeneralService][getSectionDocumentValidate][RemoteException][" + ex.getMessage()+"]");
         return null;
      }catch(SQLException ex){
        System.out.println("[PortabilityGeneralService][getSectionDocumentValidate][SQLException][" + ex.getMessage()+"]");            
        return null;
      }catch(Exception ex){
        System.out.println("[PortabilityGeneralService][getSectionDocumentValidate][Exception][" + ex.getMessage()+"]");            
        return null;
      }		
   }
  
}