package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.BillingAccountBean;
import pe.com.nextel.bean.BillingContactBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.dao.BillingAccountDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.nextel.util.StaticProperties;

public class SEJBBillingAccountBean implements SessionBean {
   private SessionContext _context;
   private BillingAccountDAO objBillingAccountDAO=null;
   private DataSource ds = null;

   public void ejbCreate() {
      objBillingAccountDAO=new BillingAccountDAO();
      try {
         Context context = new InitialContext();
          StaticProperties singleton = StaticProperties.instance();
          Properties properties = singleton.props;
         ds = (DataSource)context.lookup(properties.getProperty("JNDI.DATASOURCE"));
      }catch(Exception ex){
         ex.printStackTrace();
      }        
   }
   public void setSessionContext(SessionContext context) throws EJBException {
     _context = context;
   }
   
   public void ejbRemove() throws EJBException {}
   
   public void ejbActivate() throws EJBException {}
   
   public void ejbPassivate() throws EJBException {}

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/10/2007
***********************************************************************
***********************************************************************
***********************************************************************/    
  
   public HashMap getAccountList(String strObjectType,long lObjectId,long lOrderId)
   throws SQLException,Exception {
      return objBillingAccountDAO.getAccountList(strObjectType,lObjectId,lOrderId);
   }
   
   public HashMap getContactBillCreateList(long lNpcustomerid,long lNpSiteId)
   throws SQLException,Exception {
      return objBillingAccountDAO.getContactBillCreateList(lNpcustomerid,lNpSiteId);
   }  
   
   public HashMap getNewContactBilling(long lNpbillaccnewid)  
   throws Exception {
      return objBillingAccountDAO.getNewContactBilling(lNpbillaccnewid);
   }  
   public long getNewBillAccId()   
   throws Exception
   { 
      return objBillingAccountDAO.getNewBillAccId();
   }   
       
   public HashMap insBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception {  
   
      String strMessage=null;
      HashMap hshRetorno=new HashMap();
      try {
         //Obtener la conexion del pool de conexiones (DataSource)
         Connection conn = ds.getConnection();        
         //Desactivar el commit automatico de la conexion obtenida
         conn.setAutoCommit(false);  
         
         BillingContactBean objContactBean=new BillingContactBean();      
         BillingAccountBean objBillAccBean=new BillingAccountBean();
         CustomerBean objCustomerBean =new CustomerBean();
         
         try {            
            int iAppId=0;//psbSesion.getAppId(); 
            String strLogin=null;      
            
            iAppId =objPortalSesBean.getAppId();        
            strLogin=MiUtil.getString(objPortalSesBean.getLogin());
            
            long lOrderId = (request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));
            long lCustomerId = (request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));
            long lSiteId = (request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));
            
            String strBillName=(request.getParameter("txtBillAccName")==null?"":request.getParameter("txtBillAccName"));                                     
            String strBillTitle=(request.getParameter("cmbTitle")==null?"":request.getParameter("cmbTitle"));                                     
            String strContfName=(request.getParameter("txtContactName")==null?"":request.getParameter("txtContactName"));                                     
            String strContlName=(request.getParameter("txtContactApellido")==null?"":request.getParameter("txtContactApellido"));                                     
            String strCargo=(request.getParameter("cmbJobTitle")==null?"":request.getParameter("cmbJobTitle"));                                           
            String strPhoneArea=(request.getParameter("txtAreaCode")==null?"":request.getParameter("txtAreaCode"));                                     
            String strPhoneNumber=(request.getParameter("txtNumTelefono")==null?"":request.getParameter("txtNumTelefono"));                                           
            String strAddress1=(request.getParameter("txtAddress1")==null?"":request.getParameter("txtAddress1"));                                     
            String strAddress2=(request.getParameter("txtAddress2")==null?"":request.getParameter("txtAddress2"));                                           
            String strDepart=(request.getParameter("cmbDpto")==null?"":request.getParameter("cmbDpto"));                                     
            String strProv=(request.getParameter("cmbProv")==null?"":request.getParameter("cmbProv"));                                           
            String strDist=(request.getParameter("cmbDist")==null?"":request.getParameter("cmbDist"));                                                 
            String strZipCode=(request.getParameter("txtZip")==null?"":request.getParameter("txtZip"));                                     
            String strSwtypeCust=(request.getParameter("hdnTypeCustom")==null?"":request.getParameter("hdnTypeCustom"));                                           
            String strBscsCustId=(request.getParameter("hndBscsCustId")==null?"":request.getParameter("hndBscsCustId"));    
            String strBscsSeq=(request.getParameter("hndBscsSeq")==null?"":request.getParameter("hndBscsSeq"));				
            long lNewBillAccId = (request.getParameter("hdnNewBillAccId")==null?0:Long.parseLong(request.getParameter("hdnNewBillAccId")));
            //String strSwtypeCust= objCustomerS.getCustomerType(lCustomerId);                     
            String strBscsCusId=null;
            String strBscsSq=null;

            strBscsCusId=null;
            strBscsSq=null;
               
            objContactBean=new BillingContactBean();
            /*if ("Prospect".equals(strSwtypeCust)){                  
               strBscsCusId=null;
               strBscsSq=null;
            }else{ 
               lCustomerId=0;
               lSiteId=0;
               strBscsCusId=strBscsCustId;
               strBscsSq=strBscsSeq;                     
            }      */
            if ("-1".equals(strBscsCustId)){//Cuando se esta creando un contacto para la cuenta
               
               //objBillAccBean.setNpBillaccountNewId(Long.parseLong(strBillAccID));                       
               objCustomerBean.setSwCustomerId(lCustomerId);
               objBillAccBean.setObjCustomerB(objCustomerBean);      
               objContactBean.setNpTitle(MiUtil.getStringNull(strBillTitle));
               objContactBean.setNpfname(MiUtil.getStringNull(strContfName));
               objContactBean.setNplname(MiUtil.getStringNull(strContlName));
               objContactBean.setNpjobtitle(MiUtil.getStringNull(strCargo));
               objContactBean.setNpphonearea(MiUtil.getStringNull(strPhoneArea));
               objContactBean.setNpphone(MiUtil.getStringNull(strPhoneNumber));
               objContactBean.setNpaddress1(MiUtil.getStringNull(strAddress1));
               objContactBean.setNpaddress2(MiUtil.getStringNull(strAddress2));
               objContactBean.setNpcity(MiUtil.getStringNull(strDist));
               objContactBean.setNpstate(MiUtil.getStringNull(strProv));            
               objContactBean.setNpdepartment(MiUtil.getStringNull(strDepart));
               objContactBean.setNpzipcode(MiUtil.getStringNull(strZipCode));
               objBillAccBean.setNpBillacCName(strBillName);
               objBillAccBean.setObjBillingContactB(objContactBean);
               objBillAccBean.setNpCreatedby(strLogin);
               objBillAccBean.setNpOrderId(lOrderId);
               objBillAccBean.setNpSiteId(lSiteId);
               objBillAccBean.setNpBscsCustomerId(null);
               objBillAccBean.setNpBscsSeq(null);    
					objBillAccBean.setNpBillaccountNewId(lNewBillAccId);
					
            }else{                 
               //objBillAccBean.setNpBillaccountNewId(Long.parseLong(strBillAccID));                    
               objCustomerBean.setSwCustomerId(lCustomerId);
               objBillAccBean.setObjCustomerB(objCustomerBean);      
               objContactBean.setNpTitle(null);
               objContactBean.setNpfname(null); 
               objContactBean.setNplname(null); 
               objContactBean.setNpjobtitle(null);
               objContactBean.setNpphonearea(null);
               objContactBean.setNpphone(null);
               objContactBean.setNpaddress1(null);
               objContactBean.setNpaddress2(null);
               objContactBean.setNpcity(null);
               objContactBean.setNpstate(null);            
               objContactBean.setNpdepartment(null);
               objContactBean.setNpzipcode(null);
               objBillAccBean.setNpBillacCName(strBillName);  
               objBillAccBean.setObjBillingContactB(objContactBean);
               objBillAccBean.setNpCreatedby(strLogin);
               objBillAccBean.setNpOrderId(lOrderId);
               objBillAccBean.setNpSiteId(lSiteId);
               objBillAccBean.setNpBscsCustomerId(strBscsCusId);
               objBillAccBean.setNpBscsSeq(strBscsSq);
					objBillAccBean.setNpBillaccountNewId(lNewBillAccId);
            }
            strMessage=objBillingAccountDAO.insBillAccount(objBillAccBean,conn);                   
             
            System.out.println(" -------------------- INICIO SEJBBillingAccountBean - insBillAccount ---------------------- ");
				System.out.println("lNewBillAccId :" + lNewBillAccId);
            System.out.println("lSiteId :" + lSiteId);          
            System.out.println(" lCustomerId--> "+lCustomerId);
            System.out.println(" lSiteId--> "+lSiteId);       
            System.out.println(" strSwtypeCust--> "+strSwtypeCust);     
            System.out.println(" -------------------- FIN SEJBBillingAccountBean - insBillAccount ---------------------- ");
            if (strMessage!=null && !strMessage.startsWith("dbmessage"))
               strMessage="dbmessage"+strMessage;     
            else
                conn.commit();            
            hshRetorno.put("strCustomerId",lCustomerId+"");
            hshRetorno.put("strOrderId",lOrderId+"");
            hshRetorno.put("strSiteId",lSiteId+"");         
            hshRetorno.put("strMessage",strMessage);
         } catch (Exception e) {
            if (conn != null) conn.rollback();
            //e.printStackTrace();
            strMessage= e.getMessage();
            hshRetorno.put("strMessage",strMessage);
         } finally {   
            if (conn != null) conn.close();
         } 
      } catch (Exception e) { //Error al obtener la conexion
         //e.printStackTrace();
         strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][insBillAccount]";
         hshRetorno.put("strMessage",strMessage);
      }
      return hshRetorno;
   
   }
   
   public HashMap updBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception {        
   
      String strMessage=null;
      HashMap hshRetorno=new HashMap();
      try {      
         
         Connection conn = ds.getConnection();              
         conn.setAutoCommit(false);  
         try {          
            BillingContactBean objContactBean=new BillingContactBean();      
            BillingAccountBean objBillAccBean=new BillingAccountBean();
            CustomerBean objCustomerBean =new CustomerBean();         
            
            int iAppId=objPortalSesBean.getAppId();//psbSesion.getAppId(); 
            String strLogin=MiUtil.getString(objPortalSesBean.getLogin());
   
            long lOrderId = (request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));
            long lCustomerId = (request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));
            long lSiteId = (request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));
            
            String strBillName=(request.getParameter("txtBillAccName")==null?"":request.getParameter("txtBillAccName"));                                     
            String strBillTitle=(request.getParameter("cmbTitle")==null?"":request.getParameter("cmbTitle"));                                     
            String strContfName=(request.getParameter("txtContactName")==null?"":request.getParameter("txtContactName"));                                     
            String strContlName=(request.getParameter("txtContactApellido")==null?"":request.getParameter("txtContactApellido"));                                     
            String strCargo=(request.getParameter("cmbJobTitle")==null?"":request.getParameter("cmbJobTitle"));                                           
            String strPhoneArea=(request.getParameter("txtAreaCode")==null?"":request.getParameter("txtAreaCode"));                                     
            String strPhoneNumber=(request.getParameter("txtNumTelefono")==null?"":request.getParameter("txtNumTelefono"));                                           
            String strAddress1=(request.getParameter("txtAddress1")==null?"":request.getParameter("txtAddress1"));                                     
            String strAddress2=(request.getParameter("txtAddress2")==null?"":request.getParameter("txtAddress2"));                                           
            String strDepart=(request.getParameter("cmbDpto")==null?"":request.getParameter("cmbDpto"));                                     
            String strProv=(request.getParameter("cmbProv")==null?"":request.getParameter("cmbProv"));                                           
            String strDist=(request.getParameter("cmbDist")==null?"":request.getParameter("cmbDist"));                                                 
            String strZipCode=(request.getParameter("txtZip")==null?"":request.getParameter("txtZip"));                                     
            String strBillAccID=(request.getParameter("hdnNewBillAcc")==null?"":request.getParameter("hdnNewBillAcc"));                                           
            String strBscsCustId=(request.getParameter("hndBscsCustId")==null?"":request.getParameter("hndBscsCustId"));    
            String strBscsSeq=(request.getParameter("hndBscsSeq")==null?"":request.getParameter("hndBscsSeq"));                                           
            String strSwtypeCust=(request.getParameter("hdnTypeCustom")==null?"":request.getParameter("hdnTypeCustom")); 
            
            String strBscsCusId=null;
            String strBscsSq=null;
         
            objContactBean=new BillingContactBean();
            //Los billing account que se registran por este medio son Prospect porque estan asociados a SITES nuevos.
               strBscsCusId=null;
               strBscsSq=null;

            /*if ("Prospect".equals(strSwtypeCust)){                  
               strBscsCusId=null;
               strBscsSq=null;
            }else{ 
               lCustomerId=0;
               lSiteId=0;
               strBscsCusId=strBscsCustId;
               strBscsSq=strBscsSeq;                     
            }*/      
            if ("-1".equals(strBscsCustId)){//Cuando se esta creando un contacto para la cuenta
            
               objBillAccBean.setNpBillaccountNewId(Long.parseLong(strBillAccID));                       
               objCustomerBean.setSwCustomerId(lCustomerId);
               objBillAccBean.setObjCustomerB(objCustomerBean);      
               objContactBean.setNpTitle(MiUtil.getStringNull(strBillTitle));
               objContactBean.setNpfname(MiUtil.getStringNull(strContfName));
               objContactBean.setNplname(MiUtil.getStringNull(strContlName));
               objContactBean.setNpjobtitle(MiUtil.getStringNull(strCargo));
               objContactBean.setNpphonearea(MiUtil.getStringNull(strPhoneArea));
               objContactBean.setNpphone(MiUtil.getStringNull(strPhoneNumber));
               objContactBean.setNpaddress1(MiUtil.getStringNull(strAddress1));
               objContactBean.setNpaddress2(MiUtil.getStringNull(strAddress2));
               objContactBean.setNpcity(MiUtil.getStringNull(strDist));
               objContactBean.setNpstate(MiUtil.getStringNull(strProv));            
               objContactBean.setNpdepartment(MiUtil.getStringNull(strDepart));
               objContactBean.setNpzipcode(MiUtil.getStringNull(strZipCode));
               objBillAccBean.setNpBillacCName(strBillName);
               objBillAccBean.setObjBillingContactB(objContactBean);
               objBillAccBean.setNpCreatedby(strLogin);
               objBillAccBean.setNpOrderId(lOrderId);
               objBillAccBean.setNpSiteId(lSiteId);
               objBillAccBean.setNpBscsCustomerId(strBscsCusId);
               objBillAccBean.setNpBscsSeq(strBscsSq);         
            }else{                 
               objBillAccBean.setNpBillaccountNewId(Long.parseLong(strBillAccID));                    
               objCustomerBean.setSwCustomerId(lCustomerId);
               objBillAccBean.setObjCustomerB(objCustomerBean);      
               objContactBean.setNpTitle(null);
               objContactBean.setNpfname(null); 
               objContactBean.setNplname(null); 
               objContactBean.setNpjobtitle(null);
               objContactBean.setNpphonearea(null);
               objContactBean.setNpphone(null);
               objContactBean.setNpaddress1(null);
               objContactBean.setNpaddress2(null);
               objContactBean.setNpcity(null);
               objContactBean.setNpstate(null);            
               objContactBean.setNpdepartment(null);
               objContactBean.setNpzipcode(null);
               objBillAccBean.setNpBillacCName(strBillName);  
               objBillAccBean.setObjBillingContactB(objContactBean);
               objBillAccBean.setNpCreatedby(strLogin);
               objBillAccBean.setNpOrderId(lOrderId);
               objBillAccBean.setNpSiteId(lSiteId);
               objBillAccBean.setNpBscsCustomerId(strBscsCusId);
               objBillAccBean.setNpBscsSeq(strBscsSq);
            }
            strMessage=objBillingAccountDAO.updBillAccount(objBillAccBean,conn);  
            
            if (strMessage!=null && !strMessage.startsWith("dbmessage"))
               strMessage="dbmessage"+strMessage;
            else
                conn.commit();            
            System.out.println(" -------------------- INICIO EDITORDERSERVLET ---------------------- ");
            System.out.println("lSiteId :" + lSiteId);          
            System.out.println(" lCustomerId--> "+lCustomerId);
            System.out.println(" lSiteId--> "+lSiteId);       
            System.out.println(" strSwtypeCust--> "+strSwtypeCust);     
            System.out.println(" -------------------- FIN EDITORDERSERVLET ---------------------- ");
            hshRetorno.put("strCustomerId",lCustomerId+"");
            hshRetorno.put("strOrderId",lOrderId+"");
            hshRetorno.put("strSiteId",lSiteId+"");         
            hshRetorno.put("strMessage",strMessage);
         } catch (Exception e) {
            if (conn != null) conn.rollback();
            //e.printStackTrace();
            strMessage= e.getMessage();
            hshRetorno.put("strMessage",strMessage);
         } finally {   
            if (conn != null) conn.close();
         } 
      } catch (Exception e) { //Error al obtener la conexion
         //e.printStackTrace();
         strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updBillAccount]";
         hshRetorno.put("strMessage",strMessage);
      }
      return hshRetorno;             
   
   }
   
   public HashMap delBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception{        
   
      String strMessage=null;
      HashMap hshRetorno=new HashMap();
      try {
         
         Connection conn = ds.getConnection();              
         conn.setAutoCommit(false);  
         BillingContactBean objContactBean=new BillingContactBean();      
         BillingAccountBean objBillAccBean=new BillingAccountBean();
         CustomerBean objCustomerBean =new CustomerBean();
         
         try {            
            String strLogin=MiUtil.getString(objPortalSesBean.getLogin());         
            
            long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
            long lNewBillAccId=(request.getParameter("hdnNewBillAccId")==null?0:Long.parseLong(request.getParameter("hdnNewBillAccId")));                 
            long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));     
            long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));         
            //lNewBillAccId=lNewBillAccId-1;
				//System.out.println("lNewBillAccId (LUEGO DE RESTAR)-->"+lNewBillAccId);
            strMessage=objBillingAccountDAO.delBillAccount(lNewBillAccId,conn);     
            
            System.out.println("-------------------- INICIO SEJBBillingAccountBean - delBillAccount----------------------");				
            System.out.println("lNewBillAccId-->"+lNewBillAccId);        
            System.out.println("lCustomerId-->"+lCustomerId);        
            System.out.println("lSiteId-->"+lSiteId);        
            System.out.println("strLogin-->"+strLogin);   
            System.out.println("Mensaje de Eliminacion CONtacto -->"+strMessage);    
            System.out.println("-------------------- FIn SEJBBillingAccountBean - delBillAccount ----------------------");                            
   
            if (strMessage!=null && !strMessage.startsWith("dbmessage"))
               strMessage="dbmessage"+strMessage;         
            else
                conn.commit();            
            hshRetorno.put("strCustomerId",lCustomerId+"");
            hshRetorno.put("strOrderId",lOrderId+"");
            hshRetorno.put("strSiteId",lSiteId+"");         
            hshRetorno.put("strMessage",strMessage);
         } catch (Exception e) {
            if (conn != null) conn.rollback();
               //e.printStackTrace();
               strMessage= e.getMessage();
               hshRetorno.put("strMessage",strMessage);
         } finally {   
            if (conn != null) conn.close();
         } 
      } catch (Exception e) { //Error al obtener la conexion
         //e.printStackTrace();
         strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updBillAccount]";
         hshRetorno.put("strMessage",strMessage);
      }       
      return hshRetorno;
   }


   public HashMap delBillAccountAssign(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception{        
   
      String strMessage=null;
      HashMap hshRetorno=new HashMap();
      try {
         
         Connection conn = ds.getConnection();              
         conn.setAutoCommit(false);  
         BillingContactBean objContactBean=new BillingContactBean();      
         BillingAccountBean objBillAccBean=new BillingAccountBean();
         CustomerBean objCustomerBean =new CustomerBean();
         
         try {            
            String strLogin=MiUtil.getString(objPortalSesBean.getLogin());         
            
            long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
            long lNewBillAccId=(request.getParameter("hdnNewBillAccId")==null?0:Long.parseLong(request.getParameter("hdnNewBillAccId")));                 
            long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));     
            long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));         
            
            strMessage=objBillingAccountDAO.delBillAccountAssign(lNewBillAccId,lOrderId,conn);     
            
            System.out.println("-------------------- INICIO EDITORDERSERVLET.jsp----------------------");
            System.out.println("lNewBillAccId-->"+lNewBillAccId);        
            System.out.println("lCustomerId-->"+lCustomerId);        
            System.out.println("lSiteId-->"+lSiteId);        
            System.out.println("strLogin-->"+strLogin);   
            System.out.println("Mensaje de Eliminacion CONtacto -->"+strMessage);    
            System.out.println("-------------------- FIn EDITORDERSERVLET.jsp----------------------");                            
   
            if (strMessage!=null && !strMessage.startsWith("dbmessage"))
               strMessage="dbmessage"+strMessage;         
            else
                conn.commit();            
            hshRetorno.put("strCustomerId",lCustomerId+"");
            hshRetorno.put("strOrderId",lOrderId+"");
            hshRetorno.put("strSiteId",lSiteId+"");         
            hshRetorno.put("strMessage",strMessage);
         } catch (Exception e) {
            if (conn != null) conn.rollback();
               //e.printStackTrace();
               strMessage= e.getMessage();
               hshRetorno.put("strMessage",strMessage);
         } finally {   
            if (conn != null) conn.close();
         } 
      } catch (Exception e) { //Error al obtener la conexion
         //e.printStackTrace();
         strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updBillAccount]";
         hshRetorno.put("strMessage",strMessage);
      }       
      return hshRetorno;
   }


    /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES - FIN
    *  REALIZADO POR: Carmen Gremios
    *  FECHA: 27/10/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/   
    
    /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES - INICIO
    *  REALIZADO POR: Lee Rosales Crispin
    *  FECHA: 30/10/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/    
      


    public HashMap  BillingAccountDAOgetBillingAccountList(long iNpcustomerid) throws Exception,SQLException{
        return objBillingAccountDAO.getBillingAccountList(iNpcustomerid);
    }

    public String BillingAccountDAOinsertarAssignementAccount(BaAssignmentBean baAssignmentBean,Connection conn) throws  SQLException, Exception{
        return objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
    }

    public HashMap BillingAccountDAOgetAccountList(long longSiteId, long longCustomerId, long longOrderId) throws  SQLException, Exception{
        return objBillingAccountDAO.getAccountList(longSiteId,longCustomerId,longOrderId) ;
    }

    public HashMap  BillingAccountDAOgetCoAssignmentSiteOrig(String strPhone) throws  SQLException, Exception{
        return objBillingAccountDAO.getCoAssignmentSiteOrig(strPhone);
    }
    
    public HashMap BillingAccountDAOgetCoAssignmentList(long longOrderId)  throws Exception, SQLException{
        return objBillingAccountDAO.getCoAssignmentList(longOrderId);
    }

    public HashMap  BillingAccountDAOgetCoAssignmentBillingByContract(long longContractId) throws  SQLException, Exception{
      return objBillingAccountDAO.getCoAssignmentBillingByContract(longContractId);
    }
    
    /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES - FIN
    *  REALIZADO POR: Lee Rosales Crispin
    *  FECHA: 30/10/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/  
    
    
    
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 09/12/2007
   */   
    public HashMap  BillingAccountDAOgetBillingAccountListNew(long iNpcustomerid,long iNpsiteid) throws Exception,SQLException{
        return objBillingAccountDAO.getBillingAccountListNew(iNpcustomerid,iNpsiteid);
    }
      

}
