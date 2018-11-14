package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.Connection;

import java.util.HashMap;

import java.util.Properties;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.SiteBean;
import pe.com.nextel.dao.SiteDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

import pe.com.nextel.util.StaticProperties;

public class SEJBSiteBean implements SessionBean 
{ 
SiteDAO objSiteDAO=null;
private DataSource ds = null;
public void ejbCreate()
{  objSiteDAO=new SiteDAO();
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

  
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/
 /* public ArrayList getNoChangeContacts(long lSiteId) 
   throws Exception
  { 
     return objSiteDAO.getNoChangeContacts(lSiteId);
  }  */


/*public HashMap getNoChangeContacts(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getNoChangeContacts(lCustomerId,lSiteId);
} */



public HashMap getLongMaxSite() 
throws Exception
{
   return objSiteDAO.getLongMaxSite();
}


public HashMap getSiteData(long iSiteid) 
throws Exception
{
   return objSiteDAO.getSiteData(iSiteid);
}

public HashMap getSiteDetail(long intCustomerId,long lSiteId)
throws Exception
{ 
   return objSiteDAO.getSiteDetail(intCustomerId,lSiteId);
}

public HashMap getSiteSolutionGroup(long lSiteId)
throws Exception
{ 
   return objSiteDAO.getSiteSolutionGroup(lSiteId);
}

public HashMap getAddressList(String strObjectType,long lObjectId,int iResultado) 
throws Exception
{ 
   return objSiteDAO.getAddressList(strObjectType,lObjectId,iResultado);
}

public HashMap getCheckMandatoryAddress(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getCheckMandatoryAddress(lCustomerId,lSiteId);
}

public HashMap getAddressOrContactList(String strObjectType,long lObjectId,String strSearchType) 
throws Exception
{ 
   return objSiteDAO.getAddressOrContactList(strObjectType,lObjectId,strSearchType);
}

public HashMap getSiteType(long lSiteId)
throws Exception
{ 
   return objSiteDAO.getSiteType(lSiteId);
}

public String doAddrContValidation(long lCustomerId,long lUnknwnSiteId)
throws Exception
{
   return objSiteDAO.doAddrContValidation(lCustomerId,lUnknwnSiteId);  
} 

public HashMap getTypeAddresses(long lCustomerId,long lSiteId)   
throws Exception{
   return objSiteDAO.getTypeAddresses(lCustomerId,lSiteId);
}   

public HashMap getCheckUniqueAddress(long lCustomerId,long lSiteId ) 
throws Exception{
   return objSiteDAO.getCheckUniqueAddress(lCustomerId,lSiteId);
}  

/*public HashMap getUniqueAddresses(String strObjectId,String strObjectType)     
throws Exception{
   return objSiteDAO.getUniqueAddresses(strObjectId,strObjectType);
} */

public HashMap validateRegionDelivAddr(AddressObjectBean objAddressBean)     
throws Exception{
   return objSiteDAO.validateRegionDelivAddr(objAddressBean);
}  

public HashMap getNoChangeAddress(long lCustomerId,long lSiteId) 
throws Exception
{
   return objSiteDAO.getNoChangeAddress(lCustomerId,lSiteId);  
}  

public HashMap getCheckMandatoryContacts(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getCheckMandatoryContacts(lCustomerId,lSiteId);
}   

public HashMap getCheckUniqueContacts(long lCustomerId,long lSiteId)
throws Exception
{ 
   return objSiteDAO.getCheckUniqueContacts(lCustomerId,lSiteId);
} 

public HashMap getTypeContacts(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getTypeContacts(lCustomerId,lSiteId);
} 

public HashMap getCheckNoChangeContacts(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getCheckNoChangeContacts(lCustomerId,lSiteId);
}  

/*public HashMap getUniqueContacts(long lCustomerId,long lSiteId) 
throws Exception
{ 
   return objSiteDAO.getUniqueContacts(lCustomerId,lSiteId);
}   */

public HashMap insSites(RequestHashMap request,PortalSessionBean objPortalSesBean)
throws Exception
{   
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
   
      try {            
         SiteDAO objSiteDAO=new SiteDAO();         
         SiteBean objSiteBean =new SiteBean();         
         HashMap hshData=new HashMap();
         int iAppId=objPortalSesBean.getAppId(); //17;//psbSesion.getAppId(); 
         String strLogin=objPortalSesBean.getLogin();//"DTEODISIO";                           
         
         long lOrderId=MiUtil.parseLong(request.getParameter("hdnOrderId"));//;==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         if (lOrderId==0)
            lOrderId=MiUtil.parseLong(request.getParameter("hdnNumeroOrder"));//;==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                              
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:MiUtil.parseLong(request.getParameter("hdnCustomerId")));                  
         long lRegionId=(request.getParameter("cmbRegion")==null?0:MiUtil.parseLong(request.getParameter("cmbRegion")));                  
         String strSiteName=(request.getParameter("txtSiteName")==null?"":request.getParameter("txtSiteName"));                
         String strPhoneCode=(request.getParameter("txtAreaCode")==null?"":request.getParameter("txtAreaCode"));         
         String strPhoneNumber=(request.getParameter("txtPhone")==null?"":request.getParameter("txtPhone"));         
         String strFaxCode=(request.getParameter("txtAreaCodeFax")==null?"":request.getParameter("txtAreaCodeFax"));         
         String strFaxNumber=(request.getParameter("txtFax")==null?"":request.getParameter("txtFax"));
         String strRegionName=(request.getParameter("hdnRegionName")==null?"":request.getParameter("hdnRegionName"));  
         long lSolutionGroupId=(request.getParameter("hdnSolutionGroup")==null?0:MiUtil.parseLong(request.getParameter("hdnSolutionGroup")));  
         
         objSiteBean.setSwcustomerid(lCustomerId);  
         objSiteBean.setSwSiteName(strSiteName);
         objSiteBean.setSwRegionId(lRegionId);
         objSiteBean.setSwofficephone(strPhoneNumber);
         objSiteBean.setSwofficephonearea(strPhoneCode);
         objSiteBean.setSwfax(strFaxNumber);
         objSiteBean.setSwfaxarea(strFaxCode);             
         objSiteBean.setSwRegionName(strRegionName);
         objSiteBean.setNpStatus(Constante.SITE_STATUS_UNKNOWN);
         objSiteBean.setSwCreatedBy(strLogin);         
         objSiteBean.setNpsolutiongroupid(lSolutionGroupId);
         System.out.println("lSolutionGroupId==="+lSolutionGroupId );         
         System.out.println("sol group==="+objSiteBean.getNpsolutiongroupid() );
         long lSiteId=0;
          
         hshData = objSiteDAO.insSites(objSiteBean,lOrderId,strLogin,iAppId,conn);         
         strMessage=(String)hshData.get("strMessage");
            
         lSiteId = MiUtil.parseLong((String)hshData.get("lSiteId"));
         objSiteBean.setSwSiteId(lSiteId); 
         
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();         

         // Se vuelve a setear el estado a NUEVO para devolverlo al jsp y este lo muestre.
         objSiteBean.setNpStatus(Constante.SITE_STATUS_NUEVO);
         
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+"");
         hshResultado.put("objSite",objSiteBean);
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
            //e.printStackTrace();
            strMessage= e.getMessage();
            hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;
}  


public HashMap insAddress(RequestHashMap request,PortalSessionBean objPortalSesBean) 
throws Exception
{

   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {   
      Connection conn = ds.getConnection();           
      conn.setAutoCommit(false);     
   
      try {              
         SiteDAO objSiteDAO=new SiteDAO();         
         AddressObjectBean objAddressBean =new AddressObjectBean();       
         HashMap hshData=new HashMap();
         int iAppId=objPortalSesBean.getAppId(); //17;//psbSesion.getAppId(); 
         String strLogin=MiUtil.getString(objPortalSesBean.getLogin());// "DTEODISIO";         
         
         long lSiteId=(request.getParameter("hdnId")==null?0:Long.parseLong(request.getParameter("hdnId")));                  
         String strObjectType =(request.getParameter("hdnTipo")==null?"":request.getParameter("hdnTipo"));                
         long lRegionId=(request.getParameter("cmbRegion")==null?0:Long.parseLong(request.getParameter("cmbRegion")));                  
         String strAddress1=(request.getParameter("txtAddress1")==null?"":request.getParameter("txtAddress1"));                
         String strAddress2=(request.getParameter("txtAddress2")==null?"":request.getParameter("txtAddress2"));                
         String strAddress3=(request.getParameter("txtAddress3")==null?"":request.getParameter("txtAddress3"));                
         String strCountry=(request.getParameter("hdnPais")==null?"":request.getParameter("hdnPais"));                   
         String strDpto=(request.getParameter("hdnDpto")==null?"":request.getParameter("hdnDpto"));                   
         String strProv=(request.getParameter("hdnProv")==null?"":request.getParameter("hdnProv"));                            
         String strDist=(request.getParameter("hdnDist")==null?"":request.getParameter("hdnDist"));                               
         int iDptoId=(request.getParameter("cmbDpto")==null?0:MiUtil.parseInt(request.getParameter("cmbDpto")));                           
         int iProvId=(request.getParameter("cmbProv")==null?0:MiUtil.parseInt(request.getParameter("cmbProv")));                           
         int iDistId=(request.getParameter("cmbDist")==null?0:MiUtil.parseInt(request.getParameter("cmbDist")));                           
         String strCodPostal=(request.getParameter("txtZip")==null?"":request.getParameter("txtZip"));
         String strNote=(request.getParameter("txtNote")==null?"":request.getParameter("txtNote")); 
         //Inicio Cambios NBRAVO
         String strflagAddress2=(request.getParameter("chkflagAddress")==null?"":request.getParameter("chkflagAddress"));             
         int iflagAddress=(request.getParameter("hdnflagAddress")==null?0:MiUtil.parseInt(request.getParameter("hdnflagAddress")));                           
         //Fin cambios NBRAVO

         
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                  
         String strTrama=null;
         String strTramaArray[] = request.getParameterValues("chkAddressType");   
         if (strTramaArray!=null){
            strTrama="";
            for(int i = 0; i < strTramaArray.length; i++)
            {
               strTrama = strTrama + "," +strTramaArray[i];
            }
         }         
         
         objAddressBean.setSwobjectid(lSiteId);
         objAddressBean.setSwobjecttype(strObjectType);         
         objAddressBean.setSwregionid(lRegionId);
         objAddressBean.setSwaddress1(strAddress1);
         objAddressBean.setSwaddress2(strAddress2);
         objAddressBean.setSwaddress3(strAddress3);
         objAddressBean.setSwcountry(strCountry);
         objAddressBean.setSwstate(strDpto);
         objAddressBean.setSwprovince(strProv);         
         objAddressBean.setSwcity(strDist);
         objAddressBean.setNpdepartamentoid(iDptoId);
         objAddressBean.setNpprovinciaid(iProvId);
         objAddressBean.setNpdistritoid(iDistId);
         objAddressBean.setSwzip(strCodPostal);
         objAddressBean.setSwnote(strNote);         
         objAddressBean.setSwcreatedby(strLogin);  
         objAddressBean.setFlagAddress(iflagAddress);  //NBRAVO
         
         
         long lAddressId=0;                  
         hshData=objSiteDAO.insAddress(objAddressBean,strTrama,conn);         
         strMessage=(String)hshData.get("strMessage");
         
         lAddressId = MiUtil.parseLong((String)hshData.get("lAddressId"));
         
         System.out.println(" -------------------- INICIO EJB SITEBEAN ---------------------- ");
         System.out.println("lSiteId :" + lSiteId);
         System.out.println("lCustomerId :" + lCustomerId);
         System.out.println("lAddressId :" + lAddressId);
         System.out.println("Distrito id :" + request.getParameter("cmbDist"));
         System.out.println("Provincia id :" + request.getParameter("cmbProv"));
         System.out.println("Departamente Id :" + request.getParameter("cmbDpto"));        
         System.out.println("strMessage -->"+strMessage);    
         System.out.println("--------------COMENTARIOS-----------------");
         System.out.println("iflagAddress -->"+iflagAddress);    
         System.out.println(" -------------------- FIN EJB SITEBEAN ---------------------- ");    
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();
             
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+""); 
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
            //e.printStackTrace();
            strMessage= e.getMessage();
            hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;
} 

public HashMap updAddress(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception{

   String strMessage=null;
   HashMap hshRetorno=new HashMap();    
   try {   
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
   
      try {         
         SiteDAO objSiteDAO=new SiteDAO();         
         AddressObjectBean objAddressBean =new AddressObjectBean();
         int iAppId=objPortalSesBean.getAppId(); //17;//psbSesion.getAppId(); 
         String strLogin=objPortalSesBean.getLogin();// "DTEODISIO";
         
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                           
         long lSiteId=(request.getParameter("hdnId")==null?0:Long.parseLong(request.getParameter("hdnId")));                            
         String strNochangeAddstypeSelected[]=request.getParameterValues("hdnNoChangeAddsTypeSelected"); 
         String strCadAddsSelected[]  = request.getParameterValues("hdnCadAddsSelected");             
         String strSwaddressid[]      = request.getParameterValues("hdnAddressId");
         String strEdicion[]          = request.getParameterValues("hdnEdicion");      
         String strSwregionid[]       = request.getParameterValues("cmbRegion");      
         String strArraySwaddress1[]  = request.getParameterValues("txtAddress1");      
         String strArraySwaddress2[]  = request.getParameterValues("txtAddress2");      
         String strArraySwaddress3[]  = request.getParameterValues("txtAddress3");      
         String strArraySwnote[]      = request.getParameterValues("txtNote");      
         String strArraySwstate[]     = request.getParameterValues("hdnDpto");      
         String strArraySwprovince[]  = request.getParameterValues("hdnProv");      
         String strArraySwcity[]      = request.getParameterValues("hdnDist");      
         String strNpdepartamentoid[] = request.getParameterValues("cmbDpto");      
         String strNpprovinciaid[]    = request.getParameterValues("cmbProv");      
         String strNpdistritoid[]     = request.getParameterValues("cmbDist");      
         String strArraySwzip[]       = request.getParameterValues("txtZip");                  
         String strObjectType         =(request.getParameter("hdnTipo")==null?"":request.getParameter("hdnTipo"));               
         int iCounter                 =(request.getParameter("hdnCounter")==null?0:Integer.parseInt(request.getParameter("hdnCounter")));                        
                  
         //Inicio Cambios NBRAVO         
         String strFlagAddress[]=request.getParameterValues("hdnflagAddress");
         //Fin cambios NBRAVO
         
         
         int iPosIni=1,iPosFin=0;            
         long lSwaddressid=0;
         int iEdicion=0;
         long lSwregionid;
         int iCountNoEditable=0, iCountSemiEditable=0,iIndex1=0,iIndex2=0;
         String strSwaddress1=null,strSwaddress2=null,strSwaddress3=null;
         String strSwnote=null;
         int  iFlagAddress=0; // NBRAVO
         String strSwstate=null;
         String strSwprovince=null;
         String strSwcity=null;
         int iNpdepartamentoid=0,iNpprovinciaid=0,iNpdistritoid=0;
         String strSwzip=null;         
         int iFlagError=0;
         String strTramaTypeAddress="";
         HashMap hshResultado=null;               
         System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 0 ---------------------- ");         
         for( int iCount=0;iCount <iCounter; iCount++){       
            iPosIni = 1;
            iPosFin = 0;
            System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 1 ---------------------- ");         
            // Concatenamos todas las direcciones obligatorias, seleccionadas, no modificables.
            strTramaTypeAddress = MiUtil.getString(strCadAddsSelected[iCount]) + MiUtil.getString(strNochangeAddstypeSelected[iCount]);
            
            lSwaddressid  = Long.parseLong(strSwaddressid[iCount]);
            iEdicion = MiUtil.parseInt((String)strEdicion[iCount]);
            objAddressBean=new AddressObjectBean();
            objAddressBean.setAddressId(lSwaddressid);
            if ( iEdicion == 1){
               iIndex1 = iCount - iCountNoEditable - iCountSemiEditable;
               lSwregionid     = Long.parseLong(strSwregionid[iIndex1]); 
               strSwaddress1   = MiUtil.getString(strArraySwaddress1[iIndex1]);
               strSwaddress2   = MiUtil.getString(strArraySwaddress2[iIndex1]);
               strSwaddress3   = MiUtil.getString(strArraySwaddress3[iIndex1]);
               strSwnote       = MiUtil.getString(strArraySwnote[iIndex1]);
               iFlagAddress    = MiUtil.parseInt(strFlagAddress[iIndex1]);  // NBRAVO
               System.out.println(iIndex1);
               System.out.println(iFlagAddress); //********NBRAVO
               
               iIndex2 = iCount - iCountNoEditable;
               strSwstate       = MiUtil.getString(strArraySwstate[iIndex2]);  
               strSwprovince    = MiUtil.getString(strArraySwprovince[iIndex2]); 
               strSwcity        = MiUtil.getString(strArraySwcity[iIndex2]); 
               iNpdepartamentoid= MiUtil.parseInt(strNpdepartamentoid[iIndex2]); 
               iNpprovinciaid   = MiUtil.parseInt(strNpprovinciaid[iIndex2]); 
               iNpdistritoid    = MiUtil.parseInt(strNpdistritoid[iIndex2]); 
               strSwzip         = MiUtil.getString(strArraySwzip[iIndex2]);             
               
               objAddressBean.setSwobjectid(lSiteId);
               objAddressBean.setSwobjecttype(strObjectType);
               objAddressBean.setSwregionid(lSwregionid);
               objAddressBean.setSwaddress1(strSwaddress1);
               objAddressBean.setSwaddress2(strSwaddress2);
               objAddressBean.setSwaddress2(strSwaddress3);
               objAddressBean.setSwstate(strSwstate);
               objAddressBean.setSwprovince(strSwprovince);
               objAddressBean.setSwcity(strSwcity);
               objAddressBean.setNpdepartamentoid(iNpdepartamentoid);
               objAddressBean.setNpprovinciaid(iNpprovinciaid);
               objAddressBean.setNpdistritoid(iNpdistritoid);
               objAddressBean.setSwzip(strSwzip);
               objAddressBean.setSwnote(strSwnote);
               objAddressBean.setFlagAddress(iFlagAddress); //NBRAVO
               objAddressBean.setSwcreatedby(strLogin);
               //objAddressBean.setSwcreatedby(iflagAddress);  NBRAVO F
               System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 2 ---------------------- ");         
            // Actualizar todos los datos             
            }else if( iEdicion == 0){
               iCountNoEditable = iCountNoEditable + 1;
               System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 3 ---------------------- ");         
               // Actualizar solo los campos de tipos de dirección --llamar a metodo de insercion
            }else if ( iEdicion == 2){
               iIndex1 = iCount - iCountNoEditable;
               strSwstate         = MiUtil.getString(strArraySwstate[iIndex1]);  
               strSwprovince      = MiUtil.getString(strArraySwprovince[iIndex1]); 
               strSwcity          = MiUtil.getString(strArraySwcity[iIndex1]); 
               iNpdepartamentoid  = MiUtil.parseInt(strNpdepartamentoid[iIndex1]); 
               iNpprovinciaid     = MiUtil.parseInt(strNpprovinciaid[iIndex1]); 
               iNpdistritoid      = MiUtil.parseInt(strNpdistritoid[iIndex1]); 
               strSwzip           = MiUtil.getString(strArraySwzip[iIndex1]); 
               iCountSemiEditable = iCountSemiEditable + 1;
               
               objAddressBean.setSwobjectid(lSiteId);
               objAddressBean.setSwobjecttype(strObjectType);       
               objAddressBean.setSwstate(strSwstate);
               objAddressBean.setSwprovince(strSwprovince);
               objAddressBean.setSwcity(strSwcity);
               objAddressBean.setNpdepartamentoid(iNpdepartamentoid);
               objAddressBean.setNpprovinciaid(iNpprovinciaid);
               objAddressBean.setNpdistritoid(iNpdistritoid);
               objAddressBean.setSwzip(strSwzip);         
               objAddressBean.setSwcreatedby(strLogin);            
               // Actualizar solo los de ubigeo.
               System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 4 ---------------------- ");         
            }
            hshResultado = objSiteDAO.updAddress(objAddressBean,iEdicion,strTramaTypeAddress,iCount+1,conn);//,wv_error, wn_resultado
            
            strMessage=(String)hshResultado.get("strMessage");         
            if (strMessage!=null){
               break;
            }         
            iFlagError=MiUtil.parseInt((String)hshResultado.get("iFlgRetorno"));    
         
         } // del for
         System.out.println(" -------------------- INICIO EJB SITEBEAN.java PUNTO 5 ---------------------- ");         
      /*  if (iFlagError == 0){    //NBRAVO     
            // Vericando si si hay mas de una región para las Direcciones de Entrega
            hshResultado=(HashMap)objSiteDAO.validateRegionDelivAddr(objAddressBean) ;
            int iFlagRegDelivAddr =MiUtil.parseInt((String)hshResultado.get("iRetorno")); 
            if (iFlagRegDelivAddr == 1){
               iFlagError = 1;
               strMessage = "No puede ingresar mas de una dirección de Entrega para la misma región";
            }
         }*/
         
         System.out.println(" -------------------- INICIO EJB SITEBEAN.java ---------------------- ");
         System.out.println("lSiteId :" + lSiteId);          
         System.out.println("lOrderId :" + lOrderId);          
         System.out.println("lCustomerId :" + lCustomerId);          
         System.out.println("iFlagError :" + iFlagError);     
         System.out.println("strMessage :" + strMessage);  
         System.out.println("M--------strSwaddress1 :" + strSwaddress1);  
         System.out.println("M----iFlagAddress------  -->"+iFlagAddress); 
         System.out.println(" -------------------- FIN EJB SITEBEAN.java ---------------------- ");
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();
             
         hshRetorno.put("strOrderId",lOrderId+"");
         hshRetorno.put("strCustomerId",lCustomerId+"");
         hshRetorno.put("strSiteId",lSiteId+""); 
         hshRetorno.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();         
         strMessage= e.getMessage();
         e.printStackTrace();
         hshRetorno.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion      
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      e.printStackTrace();
      hshRetorno.put("strMessage",strMessage);
   }        
   return hshRetorno;               
}  

public HashMap delAddress(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception
{
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
   
      try {      
         SiteDAO objSiteDAO=new SiteDAO(); 
         int iAppId=objPortalSesBean.getAppId();
         String strLogin=MiUtil.getString(objPortalSesBean.getLogin());
    
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         long lAddressId=(request.getParameter("hdnAddressId")==null?0:Long.parseLong(request.getParameter("hdnAddressId")));         
         long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));         
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                 
         String strObjectType=(request.getParameter("hdnObjectType")==null?"":request.getParameter("hdnObjectType"));
         //int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));
         
         strMessage=objSiteDAO.delAddress(lAddressId,lSiteId,strObjectType,strLogin,lCustomerId,lSiteId,conn);                 
         System.out.println("-------------------- INICIO EDITORDERSERVLET.jsp----------------------");
         System.out.println("lAddressId-->"+lAddressId);
         System.out.println("strObjectType-->"+strObjectType);
         System.out.println("lCustomerId-->"+lCustomerId);        
         System.out.println("Mensaje de Eliminacion  -->"+strMessage); 
         System.out.println("-------------------- FIn EDITORDERSERVLET.jsp----------------------");                            
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();
             
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+""); 
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
         //e.printStackTrace();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;       
}   

public HashMap updSites(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception
{
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
   
      try {      
         SiteDAO objSiteDAO=new SiteDAO();        
         SiteBean objSiteBean =new SiteBean();
         HashMap hshData=new HashMap();
         int iAppId=objPortalSesBean.getAppId();// 17;//psbSesion.getAppId(); 
         String strLogin=MiUtil.getString(objPortalSesBean.getLogin()); //"DTEODISIO";
  
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));                  
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                  
         long lRegionId=(request.getParameter("cmbRegion")==null?0:Long.parseLong(request.getParameter("cmbRegion")));                  
         String strSiteName=(request.getParameter("txtSiteName")==null?"":request.getParameter("txtSiteName"));                
         String strPhoneCode=(request.getParameter("txtAreaCode")==null?"":request.getParameter("txtAreaCode"));         
         String strPhoneNumber=(request.getParameter("txtPhone")==null?"":request.getParameter("txtPhone"));         
         String strFaxCode=(request.getParameter("txtAreaCodeFax")==null?"":request.getParameter("txtAreaCodeFax"));         
         String strFaxNumber=(request.getParameter("txtFax")==null?"":request.getParameter("txtFax"));
         String strRegionName=(request.getParameter("hdnRegionName")==null?"":request.getParameter("hdnRegionName"));         
         
         objSiteBean.setSwSiteId(lSiteId);  
         objSiteBean.setSwSiteName(strSiteName);
         objSiteBean.setSwRegionId(lRegionId);
         objSiteBean.setSwofficephone(strPhoneNumber);
         objSiteBean.setSwofficephonearea(strPhoneCode);
         objSiteBean.setSwfax(strFaxNumber);
         objSiteBean.setSwfaxarea(strFaxCode);
         
         objSiteBean.setSwRegionName(strRegionName);
         objSiteBean.setNpStatus(Constante.SITE_STATUS_UNKNOWN);
         objSiteBean.setSwCreatedBy(strLogin);             
         int iResult=0;
         hshData= objSiteDAO.updSites(objSiteBean,strLogin,iAppId,conn);          
         strMessage = (String)hshData.get("strMessage");
         
         iResult = MiUtil.parseInt((String)hshData.get("iReturn"));
         
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();   

         // Se vuelve a setear el estado a NUEVO para devolverlo al jsp y este lo muestre.
         objSiteBean.setNpStatus(Constante.SITE_STATUS_NUEVO);             
         
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+""); 
         hshResultado.put("objSite",objSiteBean);
         System.out.println("--------INICIO  SEJBSiteBean.java / updSites ---------");
         System.out.println("SITE ID-->"+objSiteBean.getSwSiteId());
         System.out.println("SITE NAME-->"+objSiteBean.getSwSiteName());
         System.out.println("REGION NAME-->"+objSiteBean.getSwRegionName());
         System.out.println("STATUS-->"+objSiteBean.getNpStatus());
         System.out.println("CREATED BY-->"+objSiteBean.getSwCreatedBy());
         System.out.println("--------FIN  SEJBSiteBean.java / updSites ---------");
         hshResultado.put("strMessage",strMessage);              
      } catch (Exception e) {
         if (conn != null) conn.rollback();
         //e.printStackTrace();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;            
}
  

public HashMap delSites(RequestHashMap request) throws Exception  
{
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {
      //Obtener la conexion del pool de conexiones (DataSource)
      Connection conn = ds.getConnection();        
      
      conn.setAutoCommit(false);     
   
      try {      
         SiteDAO objSiteDAO=new SiteDAO();    
         HashMap hshData=new HashMap();
         long lUnkwSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));         
         int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));
         int iResult=0;
         hshData=objSiteDAO.delSites(lUnkwSiteId,conn);    
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
         //e.printStackTrace();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;              
}  


public HashMap delSitesAssign(RequestHashMap request) throws Exception  
{
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {
      //Obtener la conexion del pool de conexiones (DataSource)
      Connection conn = ds.getConnection();        
      
      conn.setAutoCommit(false);     
   
      try {      
         SiteDAO objSiteDAO=new SiteDAO();    
         HashMap hshData=new HashMap();         
         long lUnkwSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));                           
         long lOrderId=(request.getParameter("pOrderId")==null?0:Long.parseLong(request.getParameter("pOrderId")));             
         int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));
         int iResult=0;
         System.out.println("------------------------------------------------nSiteId: "+lUnkwSiteId);
         System.out.println("------------------------------------------------lOrderId: "+lOrderId);
         hshData=objSiteDAO.delSitesAssign(lUnkwSiteId,lOrderId,conn);    
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
         //e.printStackTrace();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;              
}  

public HashMap insContact(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception 
{ 

   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
      
      try {      
         SiteDAO objSiteDAO=new SiteDAO();                
         ContactObjectBean objContactBean =new ContactObjectBean();
         HashMap hshResult=null;         
         
         int iAppId=objPortalSesBean.getAppId(); //17;//psbSesion.getAppId(); 
         String strLogin=objPortalSesBean.getLogin();// "DTEODISIO";

         long lJobTitleId=(request.getParameter("cmbJobTitle")==null?0:Long.parseLong(request.getParameter("cmbJobTitle")));            
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                  
         long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));                  
         String strEmailaddress=(request.getParameter("txtEmail")==null?"":request.getParameter("txtEmail"));   
         String strFirstname=(request.getParameter("txtFirstName")==null?"":request.getParameter("txtFirstName"));                
         String strLastname=(request.getParameter("txtLastName1")==null?"":request.getParameter("txtLastName1"));                
         String strMiddlename=(request.getParameter("txtLastName2")==null?"":request.getParameter("txtLastName2"));                
         String strTitle=(request.getParameter("cmbTitle")==null?"":request.getParameter("cmbTitle"));                         
         String strOfficephonearea=(request.getParameter("txtAreaCode")==null?"":request.getParameter("txtAreaCode"));                   
         String strOfficephone=(request.getParameter("txtPhone")==null?"":request.getParameter("txtPhone"));                   
         String strFaxarea=(request.getParameter("txtAreaCodeFax")==null?"":request.getParameter("txtAreaCodeFax"));                            
         String strFax=(request.getParameter("txtFax")==null?"":request.getParameter("txtFax"));                               
         String strOfficephoneext=(request.getParameter("txtAnexoPhone")==null?"":request.getParameter("txtAnexoPhone"));
         String strJobTitleDesc=(request.getParameter("txtJobTitleDesc")==null?"":request.getParameter("txtJobTitleDesc"));    
           
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
             
         String strTrama=null;
         String strTramaArray[] = request.getParameterValues("chkContactType");   
         if (strTramaArray!=null){
            strTrama="";
            for(int i = 0; i < strTramaArray.length; i++)
            {
               strTrama = strTrama + "," +strTramaArray[i];
            }
         }         
         
         objContactBean.setSwjobtitleid(lJobTitleId);
         objContactBean.setSwjobtitle(strJobTitleDesc); 
         objContactBean.setSwcustomerid(lCustomerId);
         objContactBean.setSwsiteid(lSiteId);
         objContactBean.setSwemailaddress(strEmailaddress);
         objContactBean.setSwfirstname(strFirstname);
         objContactBean.setSwlastname(strLastname);
         objContactBean.setSwmiddlename(strMiddlename);
         objContactBean.setSwtitle(strTitle);
         objContactBean.setSwofficephonearea(strOfficephonearea);
         objContactBean.setSwofficephone(strOfficephone);
         objContactBean.setSwfaxarea(strFaxarea);
         objContactBean.setSwfax(strFax);
         objContactBean.setSwofficephoneext(strOfficephoneext);                  
         objContactBean.setSwcreatedby(strLogin);  
         
         long lPersonId=0;                   
         hshResult = objSiteDAO.insContact(objContactBean,strTrama,conn);            
         strMessage=(String)hshResult.get("strMessage");
         
         lPersonId=Long.parseLong(MiUtil.getIfNotEmpty((String)hshResult.get("lPersonId")));
         
         System.out.println(" -------------------- INICIO EDITORDERSERVLET ---------------------- ");
         System.out.println("lPersonId :" + lPersonId);
         System.out.println("lCustomerId :" + lCustomerId);  
         System.out.println("lOrderId :" + lOrderId);  
         System.out.println(" -------------------- FIN EDITORDERSERVLET ---------------------- ");
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();         
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+""); 
         hshResultado.put("strMessage",strMessage);            
      } catch (Exception e) {
      if (conn != null) conn.rollback();
         //e.printStackTrace();
         strMessage= e.getMessage();
         hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;           
}

public HashMap updContact(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception
{
   String strMessage=null;
   HashMap hshRetorno=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
      
      try {      
         SiteDAO objSiteDAO=new SiteDAO();   
         ContactObjectBean objContactBean =new ContactObjectBean();
         HashMap hshResultado=null;         
         
         int iAppId=objPortalSesBean.getAppId();// 17;//psbSesion.getAppId(); 
         String strLogin=objPortalSesBean.getLogin();// "DTEODISIO";
         
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));                  
         long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));                  
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         String strArrayPersonid[]                 = request.getParameterValues("hdnPersonId");
         String strArrayEdicion[]                  = request.getParameterValues("hdnEdicion");      
         String strArrayNochangeConttypeSelected[] = request.getParameterValues("hdnNoChangeContTypeSelected");
         String strArrayTitle[]                    = request.getParameterValues("cmbTitle");
         String strArrayFirstName[]                = request.getParameterValues("txtFirstName");
         String strArrayLastName[]                 = request.getParameterValues("txtLastName");
         String strArrayMiddleName[]               = request.getParameterValues("txtMiddleName");
         String strArrayJobTitle[]                 = request.getParameterValues("cmbJobTitle");
         String strArrayJobTitleDesc[]             = request.getParameterValues("txtJobTitleDesc");
         String strArrayEmail[]                    = request.getParameterValues("txtEmail");
         String strArrayAnexo[]                    = request.getParameterValues("txtAnexoPhone");
         String strArrayMainphonearea[]            = request.getParameterValues("hdnAreaCode");
         String strArrayMainphone[]                = request.getParameterValues("hdnNumTelefono");
         String strArrayMainfaxarea[]              = request.getParameterValues("hdnAreaCodeFax");
         String strArrayMainfax[]                  = request.getParameterValues("hdnFax");
         String strArrayCadAddressSelected[]       = request.getParameterValues("hdnCadContactsSelected");      
         for(int i = 0; i < strArrayMainphone.length ; i++){               
            System.out.println("strArrayMainphone "+(i+1)+ "-> "+strArrayMainphone[i]);             
         }             
         
         String strCadTypeContact=null;
         String strSwtitle=null;
         String strSwjobtitle=null;
         String strSwfirstname=null;
         String strSwmiddlename=null;
         String strSwlastname=null;
         String strSwemailaddress=null;
         String strSwofficephonearea=null;
         String strSwofficephone=null;
         String strSwfaxarea=null;
         String strSwfax=null;
         String strSwofficephoneext=null;
         long lSwjobtitleid=0;
         long lSwpersonid=0;
         int iEdicion=0;
         int iCountNoEditable=0;
         int iCountEditable=0;
         int iCounter =(request.getParameter("hdnCounter")==null?0:Integer.parseInt(request.getParameter("hdnCounter")));                        
         
         for (int wn_count=0;wn_count<iCounter;wn_count++){         
            // Concatenamos todas las direcciones obligatorias, seleccionadas, no modificables.
            strCadTypeContact           = strArrayCadAddressSelected[wn_count] + strArrayNochangeConttypeSelected[wn_count];       
            lSwpersonid       = MiUtil.parseLong(strArrayPersonid[wn_count]);
            iEdicion          =  MiUtil.parseInt((String)strArrayEdicion[wn_count]);
            
            if (iEdicion == 1){
               iCountEditable      = wn_count - iCountNoEditable;
               strSwtitle             = MiUtil.getString(strArrayTitle[iCountEditable]);
               lSwjobtitleid        = MiUtil.parseLong(strArrayJobTitle[iCountEditable]);
               strSwjobtitle          = MiUtil.getString(strArrayJobTitleDesc[iCountEditable]);
               strSwfirstname         = MiUtil.getString(strArrayFirstName[iCountEditable]);
               strSwmiddlename        = MiUtil.getString(strArrayMiddleName[iCountEditable]);
               strSwlastname          = MiUtil.getString(strArrayLastName[iCountEditable]);
               strSwemailaddress      = MiUtil.getString(strArrayEmail[iCountEditable]);
               strSwofficephonearea   = MiUtil.getString(strArrayMainphonearea[iCountEditable]);
               strSwofficephone       = MiUtil.getString(strArrayMainphone[iCountEditable]);
               strSwfaxarea           = MiUtil.getString(strArrayMainfaxarea[iCountEditable]);
               strSwfax               = MiUtil.getString(strArrayMainfax[iCountEditable]);
               strSwofficephoneext    = MiUtil.getString(strArrayAnexo[iCountEditable]);
               
               objContactBean.setSwpersonid(lSwpersonid);              
               objContactBean.setSwjobtitleid(lSwjobtitleid);               
               objContactBean.setSwjobtitle(strSwjobtitle); 
               objContactBean.setSwtitle(strSwtitle);
               objContactBean.setSwfirstname(strSwfirstname);
               objContactBean.setSwlastname(strSwlastname);
               objContactBean.setSwmiddlename(strSwmiddlename);
               objContactBean.setSwemailaddress(strSwemailaddress);
               objContactBean.setSwofficephonearea(strSwofficephonearea);
               objContactBean.setSwofficephone(strSwofficephone);
               objContactBean.setSwfaxarea(strSwfaxarea);
               objContactBean.setSwfax(strSwfax);
               objContactBean.setSwofficephoneext(strSwofficephoneext);  
               objContactBean.setSwcreatedby(strLogin);
            
            }else if (iEdicion == 0) {
               iCountNoEditable = iCountNoEditable + 1;
            }              
                
            hshResultado = (HashMap)objSiteDAO.updContact(objContactBean,iEdicion,strCadTypeContact,wn_count+1,conn);
            
            strMessage=(String)hshResultado.get("strMessage");         
            if (strMessage!=null){
               break;
            }         
         }
         int iFlagError=MiUtil.parseInt((String)hshResultado.get("iFlgRetorno")); 
         
         System.out.println(" -------------------- INICIO EDITORDERSERVLET ---------------------- ");
         System.out.println("lSiteId :" + lSiteId);
         System.out.println("lCustomerId :" + lCustomerId);  
         System.out.println(" -------------------- FIN EDITORDERSERVLET ---------------------- ");
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();         
         hshRetorno.put("strOrderId",lOrderId+"");
         hshRetorno.put("strCustomerId",lCustomerId+"");
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
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshRetorno.put("strMessage",strMessage);
   }        
   return hshRetorno;       
}

public HashMap delContact(RequestHashMap request,PortalSessionBean objPortalSesBean) throws Exception
{ 
   String strMessage=null;
   HashMap hshResultado=new HashMap();    
   try {      
      Connection conn = ds.getConnection();              
      conn.setAutoCommit(false);     
      
      try {      
         SiteDAO objSiteDAO=new SiteDAO();  
         
         ContactObjectBean objContactBean =new ContactObjectBean();         
         String strLogin=objPortalSesBean.getLogin();// "DTEODISIO";
         HashMap hshResult=null;        
         
         long lOrderId=(request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));                  
         long lPersonId=(request.getParameter("hdnContactId")==null?0:Long.parseLong(request.getParameter("hdnContactId")));                 
         long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:Long.parseLong(request.getParameter("hdnCustomerId")));     
         long lSiteId=(request.getParameter("hdnSiteId")==null?0:Long.parseLong(request.getParameter("hdnSiteId")));         
         
         objContactBean.setSwpersonid(lPersonId);         
         objContactBean.setSwcustomerid(lCustomerId);
         objContactBean.setSwsiteid(lSiteId);               
         objContactBean.setSwcreatedby(strLogin);
         
         hshResult =objSiteDAO.delContact(objContactBean,conn);                 
         strMessage=MiUtil.getString((String)hshResult.get("strMessage"));
         if (strMessage!=null && strMessage.startsWith(Constante.SUCCESS_ORA_RESULT)){
            strMessage=null;
         }
         System.out.println("-------------------- INICIO EDITORDERSERVLET.jsp----------------------");
         System.out.println("lPersonId-->"+lPersonId);        
         System.out.println("lCustomerId-->"+lCustomerId);        
         System.out.println("lSiteId-->"+lSiteId);        
         System.out.println("strLogin-->"+strLogin);   
         System.out.println("Mensaje de Eliminacion CONtacto -->"+strMessage);    
         System.out.println("-------------------- FIn EDITORDERSERVLET.jsp----------------------");                            
         if (strMessage!=null && !strMessage.startsWith("dbmessage"))
            strMessage="dbmessage"+strMessage;
         else
             conn.commit();         
         hshResultado.put("strOrderId",lOrderId+"");
         hshResultado.put("strCustomerId",lCustomerId+"");
         hshResultado.put("strSiteId",lSiteId+""); 
         hshResultado.put("strMessage",strMessage);            
      } catch (Exception e) {
         if (conn != null) conn.rollback();
            //e.printStackTrace();
            strMessage= e.getMessage();
            hshResultado.put("strMessage",strMessage);
      } finally {   
         if (conn != null) conn.close();
      } 
   } catch (Exception e) { //Error al obtener la conexion
      //e.printStackTrace();
      strMessage= "[Exception][Fallo la conexion]["+e.getClass() + " " + e.getMessage()+"][updOrder]";
      hshResultado.put("strMessage",strMessage);
   }        
   return hshResultado;          
} 

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/

public int getSourceSite(long lSiteId)
throws Exception
{
   return objSiteDAO.getSourceSite(lSiteId);  
}

/*JPEREZ*/
public HashMap getSpecSolutionGroup(long lSpecificationId) throws Exception{ 
   return objSiteDAO.getSpecSolutionGroup(lSpecificationId);
}

/*PBI000000042016*/
public Long  getUnknownSite(long lSiteId){
   return objSiteDAO.getUnknownSite(lSiteId, null);
   }

}