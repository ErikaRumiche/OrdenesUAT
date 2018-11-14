package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import java.util.Properties;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.dao.SalesDataDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

import pe.com.nextel.util.StaticProperties;

public class SEJBSalesDataBean implements SessionBean {

  private SessionContext _context;
  SalesDataDAO          objSalesDataDAO           = null;
  private DataSource ds = null;
  
  public void ejbCreate(){
  
    /*Creamos las intancias a los DAO's*/
    objSalesDataDAO         = new SalesDataDAO();
    
    try {
      Context context = new InitialContext();

        StaticProperties singleton = StaticProperties.instance();
        Properties properties = singleton.props;
      ds = (DataSource)context.lookup(properties.getProperty("JNDI.DATASOURCE"));
    }catch(Exception ex){
      ex.printStackTrace();
    }  
  }
    
  public void ejbActivate(){}

  public void ejbPassivate(){}

  public void ejbRemove(){}

  public void setSessionContext(SessionContext ctx){}
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 06/05/2010
 * @see pe.com.nextel.dao.SalesDataDAO#getAplicationDataList(long,long,long,long)      
 ************************************************************************************************************************************/ 
  public HashMap getAplicationDataList (long lnOrderId,long lngDivisionId,long lngCustomerId,long lngSpecificationId) throws  Exception, SQLException{
    return objSalesDataDAO.getAplicationDataList(lnOrderId,lngDivisionId,lngCustomerId,lngSpecificationId);
 }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvaor@nextel.com.pe">Karen Salvador</a>
 * <br>Motivo: Inserta las aplicacines de una venta data relacionada al Cliente
 * <br>Fecha: 03/05/2010     
 / ************************************************************************************************************************************/

  public HashMap setSalesData(RequestHashMap request,PortalSessionBean objPortalSesBean)throws Exception {   
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {    
        Connection conn = ds.getConnection();              
        conn.setAutoCommit(false);    
  
      try {            
         SalesDataDAO objSalesDataDAO=new SalesDataDAO();         
         ItemBean objItemBean =new ItemBean();         
         HashMap hshData=new HashMap();
         int lOrigen = 0;

         String strLogin=objPortalSesBean.getLogin();  
    
         long lOrderId =(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));     
         long lCustomerId =(request.getParameter("hdnCustomerId")==null?0:MiUtil.parseLong(request.getParameter("hdnCustomerId")));  
         long lDivisionId =(request.getParameter("hdnDivisionId")==null?0:MiUtil.parseLong(request.getParameter("hdnDivisionId")));  
         long lSolucionId =(request.getParameter("hdnSolucionId")==null?0:MiUtil.parseLong(request.getParameter("hdnSolucionId"))); 
         long lSpecificationId =(request.getParameter("hdnSolucionId")==null?0:MiUtil.parseLong(request.getParameter("hdnSpecificationId"))); 
         long lProducLineId=(request.getParameter("cmbSolucionNegocio")==null?0:MiUtil.parseLong(request.getParameter("cmbSolucionNegocio")));      
         long lProducId=(request.getParameter("cmbAplicacion")==null?0:MiUtil.parseLong(request.getParameter("cmbAplicacion")));     
         String strProductLineName =(request.getParameter("hdnSolucionName")==null?"":request.getParameter("hdnSolucionName"));      
         String strProductName     =(request.getParameter("hdnAplicacionName")==null?"":request.getParameter("hdnAplicacionName")); 
         long lSalesDataId =(request.getParameter("hdnSalesDataId")==null?0:MiUtil.parseLong(request.getParameter("hdnSalesDataId")));
         
         objItemBean.setNpsolutionid(lSolucionId);
         objItemBean.setNpproductlineid(lProducLineId);
         objItemBean.setNpproductid(lProducId);
         objItemBean.setNpproductlinename(strProductLineName);
         objItemBean.setNpproductname(strProductName);
         objItemBean.setNpbusinesssolutionid(lSalesDataId);
         
         if(lSalesDataId>0) {
            hshData = objSalesDataDAO.doUpdateSalesData(objItemBean,strLogin,conn); 
            lOrigen = 1;
         }else{
           hshData = objSalesDataDAO.insSalesData(objItemBean,lOrderId,lCustomerId,lDivisionId,Constante.IND_STATUS_UNKNOW,strLogin,conn); 
           lOrigen = 0;
         }
         strMessage=(String)hshData.get("strMessage");
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
            conn.commit();    
            
         lSalesDataId = MiUtil.parseLong((String)hshData.get("lSalesDataId"));
    
         // Se vuelve a setear los valores para devolverlo al jsp y este lo muestre.
         //------------------------------------------------------------------------
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strDivisionId",lDivisionId+"");
         hshResultado.put("strSalesDataId",lSalesDataId+"");
         hshResultado.put("strSpecificationId",lSpecificationId+"");
         hshResultado.put("strLogin",strLogin);
         hshResultado.put("strStatus",Constante.IND_STATUS_UNKNOW);
         hshResultado.put("strOrigen",lOrigen+"");
         hshResultado.put("objItemBean",objItemBean);
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
            strMessage= e.getMessage();
            hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][insSalesData]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;
 }  
 
 
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 06/05/2010
 * @see pe.com.nextel.dao.SalesDataDAO#getAplicationCustomer(long,long,long, String)      
 ************************************************************************************************************************************/ 
  public HashMap getAplicationCustomer (long lngOrderId,long lngDivisionId,long lngCustomerId,String strStatus) throws  Exception, SQLException{
    return objSalesDataDAO.getAplicationCustomer(lngOrderId,lngDivisionId,lngCustomerId,strStatus);
 }
 
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 06/05/2010
 * @see pe.com.nextel.dao.SalesDataDAO#getAplicationDetail(long)      
 ************************************************************************************************************************************/ 
  public HashMap getAplicationDetail (long lnpbusinesssolutionid) throws  Exception, SQLException{
    return objSalesDataDAO.getAplicationDetail(lnpbusinesssolutionid);
 }
 
 
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 06/05/2010
 * @see pe.com.nextel.dao.SalesDataDAO#delSalesdata(request)      
 ************************************************************************************************************************************/ 
  public HashMap delSalesData(RequestHashMap request) throws Exception {
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {
      Connection conn = ds.getConnection();        
      conn.setAutoCommit(false);     
  
      try {      
         SalesDataDAO objSalesDataDAO=new SalesDataDAO();  
         HashMap hshData=new HashMap();
         long lUnkwDataId=(request.getParameter("nDataId")==null?0:Long.parseLong(request.getParameter("nDataId")));         
         int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));
         int iResult=0;
         hshData=objSalesDataDAO.delSalesData(lUnkwDataId,conn);    
         strMessage=(String)hshData.get("strMessage");
         iResult=MiUtil.parseInt((String)hshData.get("iReturn"));
         
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();   
             
         hshResultado.put("strIndId",iInd+"");
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][delSalesData]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;              
}  
 



}