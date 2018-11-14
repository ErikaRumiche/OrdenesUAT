package pe.com.nextel.section.sectionChangeCustomerInfo;

import java.util.HashMap;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.MessageVO;
import pe.com.nextel.bpel.AdressCustomerRead;
import pe.com.nextel.bpel.DataCustomerUpdate;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionChangeCustomerInfoEvents 
{
   public SectionChangeCustomerInfoEvents()
   {
   }

/**
* Motivo:  Metodo que contiene la lógica de la inserción de la petición de modificación de datos del cliente, site y billing account
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @param      RequestHashMap
* @param      Connection 
* @return     String 
*/     
public String insChangeCustomerInfo(RequestHashMap request,java.sql.Connection conn)
{    
   CustomerDAO objCustomerDAO=new CustomerDAO();
   GeneralDAO objGeneralDAO = new GeneralDAO();
   //SiteDAO objSiteDAO= new SiteDAO();
   CustomerBean objCustomerBean=new CustomerBean();
   CustomerBean objCustomerBeanNew=new CustomerBean();
   AddressObjectBean objAddressBean =new AddressObjectBean();
   AddressObjectBean objAddressBeanNew =new AddressObjectBean();
   ContactObjectBean objContactBean=new ContactObjectBean();
   ContactObjectBean objContactBeanNew=new ContactObjectBean();      
   //HashMap hshData=null;
   //HashMap hshSite=null;

   String strAreaPhone1New=request.getParameter("txtCodePhone1New");
   String strPhone1New=request.getParameter("txtPhone1New");
   String strAreaPhone2New=request.getParameter("txtCodePhone2New");
   String strPhone2New=request.getParameter("txtPhone2New");
   String strAreaPhone3New=request.getParameter("txtCodePhone3New");
   String strPhone3New=request.getParameter("txtPhone3New");
   String strAreaFaxNew=request.getParameter("txtCodeFaxNew");
   String strFaxNew=request.getParameter("txtFaxNew");

   String strAreaPhone1=request.getParameter("hdnCodePhone1");
   String strPhone1=request.getParameter("hdnPhone1");
   String strAreaPhone2=request.getParameter("hdnCodePhone2");
   String strPhone2=request.getParameter("hdnPhone2");
   String strAreaPhone3=request.getParameter("hdnCodePhone3");
   String strPhone3=request.getParameter("hdnPhone3");
   String strAreaFax=request.getParameter("hdnCodeFax");
   String strFax=request.getParameter("hdnFax");      
   
   long lRegion=MiUtil.parseLong(request.getParameter("hdnAddRegionId"));      
   String strAddress1=request.getParameter("hdnAddress1");   
   String strAddress2=request.getParameter("hdnAddress2");   
   String strAddress3=request.getParameter("hdnAddress3");   
   String strAddress4=null; 
   String strZip=request.getParameter("hdnZip");   
   String strDpto=request.getParameter("hdnDpto");   
   String strProv=request.getParameter("hdnProv");   
   String strDist=request.getParameter("hdnDist");   
   String strDptoId=request.getParameter("hdnDptoId");   
   String strProvId=request.getParameter("hdnProvId");   
   String strDistId=request.getParameter("hdnDistId");  
   
   String strReferencia=request.getParameter("hdnRef"); //SAR 0037-167824

   long lRegionNew=MiUtil.parseLong(request.getParameter("cmbRegionNew"));   
   String strAddress1New=request.getParameter("txtAddress1New");   
   String strAddress2New=request.getParameter("txtAddress2New");   
   String strAddress3New=request.getParameter("txtAddress3New");   
   String strAddress4New=null;   
   String strZipNew=request.getParameter("txtZipNew");   
   String strDptoNew=request.getParameter("hdnDptoNew");   
   String strProvNew=request.getParameter("hdnProvNew");   
   String strDistNew=request.getParameter("hdnDistNew");   
   String strDptoIdNew=request.getParameter("cmbDpto");   //1976
   String strProvIdNew=request.getParameter("cmbProv");   //1994
   String strDistIdNew=request.getParameter("cmbDist"); //1997
   
   String strTitle=request.getParameter("hdnTitle"); 
   String strFirstName=request.getParameter("hdnFirstName"); 
   String strLastName=request.getParameter("hdnLastName");    
   String strMiddleName=request.getParameter("hdnMiddleName");    
   String strJobTitle=request.getParameter("hdnJobTitle");    
   String strEmail=request.getParameter("hdnEmail");    

   String strTitleNew=request.getParameter("cmbTitleNew");
   String strTitleNewId=request.getParameter("hndTitleNew");    
   String strFirstNameNew=request.getParameter("txtFirstNameNew"); 
   String strLastNameNew=request.getParameter("txtLastNameNew");    
   String strMiddleNameNew=request.getParameter("txtMiddleNameNew");    
   String strJobTitleNewId=request.getParameter("cmbJobTitleNew"); 
   String strJobTitleNew=null;
   String strEmailNew=request.getParameter("txtEmailNew");
   
   String strReferenciaNew=request.getParameter("txtRefNew"); //SAR 0037-167824
  
   String strBillAccNameNew=request.getParameter("txtBillAccNameNew");   
   String strBillAccName=request.getParameter("hndBillAccName");   
   long lBillAccId=(request.getParameter("cmbBillingAcc")==null?0:MiUtil.parseLong(request.getParameter("cmbBillingAcc")));   

   String strSiteNameNew=request.getParameter("txtSiteNameNew");   
   String strSiteName=request.getParameter("hdnSiteName");   
   
   long lCustomerId=(request.getParameter("hdnCustomerId")==null?0:MiUtil.parseLong(request.getParameter("hdnCustomerId")));    
   long lSiteId=(request.getParameter("hdnSiteId")==null?0:MiUtil.parseLong(request.getParameter("hdnSiteId")));    
   long lOrderId=(request.getParameter("hdnNumeroOrder")==null?0:MiUtil.parseLong(request.getParameter("hdnNumeroOrder")));    
   long lObjectId=0;
   int iObjectType=0;
   String strMessage=null;
  


   try{   
      if (lSiteId!=0){
         lObjectId=lSiteId;
         iObjectType=Constante.TYPE_SITE;   
      }else{
         lObjectId=lCustomerId;
         iObjectType=Constante.TYPE_CUSTOMER;
      }
      
      
      //Datos de Cabecera actual
      objCustomerBean.setSwMainPhoneArea(strAreaPhone1);   
      objCustomerBean.setSwMainPhone(strPhone1);
      objCustomerBean.setNpPhone2areacode(strAreaPhone2);
      objCustomerBean.setSwPhone2(strPhone2);
      objCustomerBean.setNpPhone3areacode(strAreaPhone3);
      objCustomerBean.setSwPhone3(strPhone3);   
      objCustomerBean.setSwMainFax(strFax);
      objCustomerBean.setSwMainFaxArea(strAreaFax);
      //Datos que se piden modificar
      objCustomerBeanNew.setSwMainPhoneArea(strAreaPhone1New);   
      objCustomerBeanNew.setSwMainPhone(strPhone1New);
      objCustomerBeanNew.setNpPhone2areacode(strAreaPhone2New);
      objCustomerBeanNew.setSwPhone2(strPhone2New);
      objCustomerBeanNew.setNpPhone3areacode(strAreaPhone3New);
      objCustomerBeanNew.setSwPhone3(strPhone3New);   
      objCustomerBeanNew.setSwMainFax(strFaxNew);
      objCustomerBeanNew.setSwMainFaxArea(strAreaFaxNew);
      
      //Datos de Direccion actual
      objAddressBean.setSwregionid(lRegion);      
      objAddressBean.setSwaddress1(strAddress1);
      objAddressBean.setSwaddress2(strAddress2);
      objAddressBean.setSwaddress3(strAddress3);
      objAddressBean.setSwaddress4(strAddress4);
      objAddressBean.setSwzip(strZip);
      objAddressBean.setSwstate(strDpto);
      objAddressBean.setSwprovince(strProv);
      objAddressBean.setSwcity(strDist);
      objAddressBean.setNpdepartamentoid(MiUtil.parseInt(strDptoId));
      objAddressBean.setNpprovinciaid(MiUtil.parseInt(strProvId));
      objAddressBean.setNpdistritoid(MiUtil.parseInt(strDistId));
      
      objAddressBean.setSwnote(strReferencia); //SAR 0037-167824
 
      objAddressBeanNew.setSwregionid(lRegionNew);      
      objAddressBeanNew.setSwaddress1(strAddress1New);
      objAddressBeanNew.setSwaddress2(strAddress2New);
      objAddressBeanNew.setSwaddress3(strAddress3New);
      objAddressBeanNew.setSwaddress4(strAddress4New);
      objAddressBeanNew.setSwzip(strZipNew);
      objAddressBeanNew.setSwstate(strDptoNew);
      objAddressBeanNew.setSwprovince(strProvNew);
      objAddressBeanNew.setSwcity(strDistNew);
      objAddressBeanNew.setNpdepartamentoid(MiUtil.parseInt(strDptoIdNew));
      objAddressBeanNew.setNpprovinciaid(MiUtil.parseInt(strProvIdNew));
      objAddressBeanNew.setNpdistritoid(MiUtil.parseInt(strDistIdNew)); 
     
      objAddressBeanNew.setSwnote(strReferenciaNew); //SAR 0037-167824

      

      objContactBean.setSwtitle(strTitle);
      objContactBean.setSwfirstname(strFirstName);
      objContactBean.setSwlastname(strLastName);
      objContactBean.setSwmiddlename(strMiddleName);
      objContactBean.setSwjobtitle(strJobTitle);
      objContactBean.setSwemailaddress(strEmail);

      strJobTitleNew=objGeneralDAO.getTitle(strJobTitleNewId);
      objContactBeanNew.setSwtitle(strTitleNew);
      objContactBeanNew.setSwfirstname(strFirstNameNew);
      objContactBeanNew.setSwlastname(strLastNameNew);
      objContactBeanNew.setSwmiddlename(strMiddleNameNew);
      objContactBeanNew.setSwjobtitle(strJobTitleNew);
      objContactBeanNew.setSwemailaddress(strEmailNew);

      strMessage=objCustomerDAO.insChangeCustomer(lOrderId,iObjectType,lCustomerId,lSiteId,strSiteName,strSiteNameNew,
                                       objCustomerBean,objCustomerBeanNew,objAddressBean,objAddressBeanNew,
                                       objContactBean,objContactBeanNew,conn);    
      if (strMessage!=null)
         return strMessage;
         
      if (lBillAccId>0){      
         objCustomerBean=new CustomerBean();
         objCustomerBeanNew=new CustomerBean();
         objAddressBean =new AddressObjectBean();
         objAddressBeanNew =new AddressObjectBean();
         objContactBean=new ContactObjectBean();         
         objContactBeanNew=new ContactObjectBean();         
         strMessage=objCustomerDAO.insChangeCustomer(lOrderId,Constante.TYPE_BILLINGACC,lBillAccId,0,strBillAccName,strBillAccNameNew,
                                          objCustomerBean,objCustomerBeanNew,objAddressBean,objAddressBeanNew,
                                          objContactBean,objContactBeanNew,conn);    
      }
      }catch(Exception ex){
         strMessage=ex.getMessage();
      }
      
  
   return strMessage;        
}      

/**
* Motivo:  Metodo que contiene la lógica de la inserción de la petición de modificación de datos del cliente, site y billing account
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 05/11/2007
* @param      RequestHashMap
* @param      Connection 
* @return     String 
*/     
public String updChangeCustomerInfo(RequestHashMap request,java.sql.Connection conn)
{    
  
   CustomerDAO objCustomerDAO=new CustomerDAO();
   GeneralDAO objGeneralDAO = new GeneralDAO();
   CustomerBean objCustomerBean=new CustomerBean();
   CustomerBean objCustomerBeanNew=new CustomerBean();
   AddressObjectBean objAddressBean =new AddressObjectBean();
   AddressObjectBean objAddressBeanNew =new AddressObjectBean();
   ContactObjectBean objContactBean=new ContactObjectBean();
   ContactObjectBean objContactBeanNew=new ContactObjectBean();   

   String   av_loggin      = request.getParameter("hdnUserName");

   //Datos de la cabecera que se solicita modificar
   String strAreaPhone1New=request.getParameter("txtCodePhone1New");
   String strPhone1New=request.getParameter("txtPhone1New");
   String strAreaPhone2New=request.getParameter("txtCodePhone2New");
   String strPhone2New=request.getParameter("txtPhone2New");
   String strAreaPhone3New=request.getParameter("txtCodePhone3New");
   String strPhone3New=request.getParameter("txtPhone3New");
   String strAreaFaxNew=request.getParameter("txtCodeFaxNew");
   String strFaxNew=request.getParameter("txtFaxNew");

   
   //Datos de la direccion que se solicita modificar   
   long lRegionNew =MiUtil.parseLong(request.getParameter("cmbRegionNew"));   
   String strAddress1New=request.getParameter("txtAddress1New");   
   String strAddress2New=request.getParameter("txtAddress2New");   
   String strAddress3New=request.getParameter("txtAddress3New");   
   String strAddress4New=null;  
   String strZipNew=request.getParameter("txtZipNew");   
   String strDptoNew=request.getParameter("hdnDptoNew");   
   String strProvNew=request.getParameter("hdnProvNew");   
   String strDistNew=request.getParameter("hdnDistNew");   
   String strDptoIdNew=request.getParameter("cmbDpto");   
   String strProvIdNew=request.getParameter("cmbProv");   
   String strDistIdNew=request.getParameter("cmbDist"); 
   
   String strBillAccNameNew=request.getParameter("txtBillAccNameNew");   
   String strBillAccName=request.getParameter("hndBillAccName");  
   
   String strReferenciaNew=request.getParameter("txtRefNew"); //SAR 0037-167824
   
   
   long lBillAccId=(request.getParameter("hdnBillingAccId")==null?0:MiUtil.parseLong(request.getParameter("hdnBillingAccId")));   
   
   //Datos del contacto que se solicita modificar
   String strTitleNew=request.getParameter("cmbTitleNew");
   String strTitleNewId=null;
   String strFirstNameNew=request.getParameter("txtFirstNameNew"); 
   String strLastNameNew=request.getParameter("txtLastNameNew");    
   String strMiddleNameNew=request.getParameter("txtMiddleNameNew");
   //SAR 0037-178750 INI
   
   String strLastNameNewAct = strLastNameNew+" "+ strMiddleNameNew;
   
   //SAR 0037-178750 FIN
  // String strJobTitleNew=request.getParameter("cmbJobTitle");    
   String strEmailNew=request.getParameter("txtEmailNew");  
   String strSiteNameNew=request.getParameter("txtSiteNameNew");  
   String strJobTitleNewId=request.getParameter("cmbJobTitle");
   String strJobTitleNew=null;
   
  
   long lOrderId = (request.getParameter("hdnOrderId")==null?0:Long.parseLong(request.getParameter("hdnOrderId")));
   long lCustomerId = (request.getParameter("txtCompanyId")==null?0:Long.parseLong(request.getParameter("txtCompanyId")));
  // long lSiteId = (request.getParameter("txtSiteId")==null?0:Long.parseLong(request.getParameter("txtSiteId")));
   String strEstadoOrden = (request.getParameter("txtEstadoOrden")==null?"":MiUtil.getString(request.getParameter("txtEstadoOrden")));
   long lTitleNew=(request.getParameter("hndTitleNew")==null?0:Long.parseLong(request.getParameter("hndTitleNew"))); 
   String strIdAPP =(request.getParameter("hdnAppId")==null?"":MiUtil.getString(request.getParameter("hdnAppId")));
   long lSiteId=(request.getParameter("hdnSiteId")==null?0:MiUtil.parseLong(request.getParameter("hdnSiteId")));   
   String strAccion = (request.getParameter("cmbAction")==null?"":MiUtil.getString(request.getParameter("cmbAction")));
 
   String strNextInboxName = (request.getParameter("hdnInboxBack")==null?"":MiUtil.getString(request.getParameter("hdnInboxBack"))); // SAR 0037-158938
      
   long lObjectId=0;
   int iObjectType=0;
   String strMessage=null;

   try{   
      if (lSiteId!=0){
         lObjectId=lSiteId;
         iObjectType=Constante.TYPE_SITE;         
      }else{
         lObjectId=lCustomerId;
         iObjectType=Constante.TYPE_CUSTOMER;
      }

      //Datos que se piden modificar
      objCustomerBeanNew.setSwMainPhoneArea(strAreaPhone1New);   
      objCustomerBeanNew.setSwMainPhone(strPhone1New);
      objCustomerBeanNew.setNpPhone2areacode(strAreaPhone2New);
      objCustomerBeanNew.setSwPhone2(strPhone2New);
      objCustomerBeanNew.setNpPhone3areacode(strAreaPhone3New);
      objCustomerBeanNew.setSwPhone3(strPhone3New);   
      objCustomerBeanNew.setSwMainFax(strFaxNew);
      objCustomerBeanNew.setSwMainFaxArea(strAreaFaxNew);
      
      //Datos de la direccion que se pide modificar      
      objAddressBeanNew.setSwregionid(lRegionNew);
      objAddressBeanNew.setSwaddress1(strAddress1New);
      objAddressBeanNew.setSwaddress2(strAddress2New);
      objAddressBeanNew.setSwaddress3(strAddress3New);
      objAddressBeanNew.setSwaddress4(strAddress4New);
      objAddressBeanNew.setSwzip(strZipNew);
      objAddressBeanNew.setSwstate(strDptoNew);
      objAddressBeanNew.setSwprovince(strProvNew);
      objAddressBeanNew.setSwcity(strDistNew);
      objAddressBeanNew.setNpdepartamentoid(MiUtil.parseInt(strDptoIdNew));
      objAddressBeanNew.setNpprovinciaid(MiUtil.parseInt(strProvIdNew));
      objAddressBeanNew.setNpdistritoid(MiUtil.parseInt(strDistIdNew)); 
      
      objAddressBeanNew.setSwregionname(strReferenciaNew); //SAR 0037-167824
      
      objAddressBeanNew.setSwnote(strReferenciaNew);  //SAR 0037-167824
      
      
      //Datos del contacto que se pide modificar
      strJobTitleNew=objGeneralDAO.getTitle(strJobTitleNewId);
      objContactBeanNew.setSwtitle(strTitleNew);
      objContactBeanNew.setSwfirstname(strFirstNameNew);
      objContactBeanNew.setSwlastname(strLastNameNew);
      objContactBeanNew.setSwmiddlename(strMiddleNameNew);
      objContactBeanNew.setSwjobtitle(strJobTitleNew);
      objContactBeanNew.setSwemailaddress(strEmailNew);
      
      
  
      strMessage=objCustomerDAO.updChangeCustomer(lOrderId,iObjectType,lCustomerId,lSiteId,
                                                  strSiteNameNew,objCustomerBeanNew,objAddressBeanNew,
                                                  objContactBeanNew,conn);    

      if (strMessage!=null) return strMessage;
         
      if (lBillAccId>0){     
         objCustomerBeanNew=new CustomerBean();
         objAddressBeanNew =new AddressObjectBean();
         objContactBeanNew=new ContactObjectBean();      
         strMessage=objCustomerDAO.updChangeCustomer(lOrderId,Constante.TYPE_BILLINGACC,lBillAccId,0,strBillAccNameNew,
                                          objCustomerBeanNew,objAddressBeanNew,objContactBeanNew,conn);    
      }
      if (strMessage!=null) return strMessage;     
      
      System.out.println("strAccion:"+strAccion);
      System.out.println("strNextInboxName:"+strNextInboxName); // SAR 0037-158938
      
      if ((Constante.INBOX_EDICION.equals(strEstadoOrden.toUpperCase()) && ( strAccion.equals("") || strAccion==null || Constante.ACTION_INBOX_SALTAR.equals(strAccion) || Constante.ACTION_INBOX_IR_A.equals(strAccion) )  )
      || (Constante.ORDER_STATUS_CERRADO.equals(strNextInboxName)))  {        // SAR 0037-158938
      
        //Volvemos a setear valores
        //------------------------
        CustomerBean objCustomerBeanNewAct=new CustomerBean();
        objCustomerBeanNewAct.setSwMainPhoneArea(strAreaPhone1New);   
        objCustomerBeanNewAct.setSwMainPhone(strPhone1New);
        objCustomerBeanNewAct.setNpPhone2areacode(strAreaPhone2New);
        objCustomerBeanNewAct.setSwPhone2(strPhone2New);
        objCustomerBeanNewAct.setNpPhone3areacode(strAreaPhone3New);
        objCustomerBeanNewAct.setSwPhone3(strPhone3New);   
        objCustomerBeanNewAct.setSwMainFax(strFaxNew);
        objCustomerBeanNewAct.setSwMainFaxArea(strAreaFaxNew);
        
        AddressObjectBean  objAddressBeanNewAct= new AddressObjectBean(); 
        objAddressBeanNewAct.setSwregionid(lRegionNew);
        objAddressBeanNewAct.setSwaddress1(strAddress1New);
        objAddressBeanNewAct.setSwaddress2(strAddress2New);
        objAddressBeanNewAct.setSwaddress3(strAddress3New);
        objAddressBeanNewAct.setSwaddress4(strAddress4New);
        objAddressBeanNewAct.setSwzip(strZipNew);
        objAddressBeanNewAct.setSwstate(strDptoNew);
        objAddressBeanNewAct.setSwprovince(strProvNew);
        objAddressBeanNewAct.setSwcity(strDistNew);
        objAddressBeanNewAct.setNpdepartamentoid(MiUtil.parseInt(strDptoIdNew));
        objAddressBeanNewAct.setNpprovinciaid(MiUtil.parseInt(strProvIdNew));
        objAddressBeanNewAct.setNpdistritoid(MiUtil.parseInt(strDistIdNew)); 
       
        if (strReferenciaNew.length()>40)
          objAddressBeanNewAct.setSwregionname(strReferenciaNew.substring(0,40)); //SAR 0037-167824
        else
          objAddressBeanNewAct.setSwregionname(strReferenciaNew); //SAR 0037-167824;
        
        objAddressBeanNewAct.setSwnote(strReferenciaNew);       //SAR 0037-167824

       
       
        ContactObjectBean objContactBeanNewAct=new ContactObjectBean();
        objContactBeanNewAct.setSwtitle(strTitleNew);
        objContactBeanNewAct.setSwfirstname(strFirstNameNew);
        objContactBeanNewAct.setSwlastname(strLastNameNewAct); //SAR 0037-178750 Se cambia strLastNameNew por strLastNameNewAct
        objContactBeanNewAct.setSwmiddlename(strMiddleNameNew);
        
       
        objContactBeanNewAct.setSwjobtitleid(MiUtil.parseLong(strJobTitleNewId));
        objContactBeanNewAct.setSwemailaddress(strEmailNew);
        


       
       if(strJobTitleNew==null){
          objContactBeanNewAct.setSwjobtitle(null);
       }else{
       
       String Swjobtitlenew="";
         if(strJobTitleNew.length()>40){
            for(int i=0; i<40; i++){
                Swjobtitlenew=Swjobtitlenew+strJobTitleNew.charAt(i);
            }
             objContactBeanNewAct.setSwjobtitle(Swjobtitlenew);
         }else{
             objContactBeanNewAct.setSwjobtitle(strJobTitleNew);
         }
       }
         

        
      //Actualizar en CRM 
      //-----------------
      System.out.println("[SectionChangeCustomerInfEvents]: updChangeCustomerInfo");
      System.out.println("[updChangeCustomerInfo] lOrderId:"+lOrderId);
      System.out.println("[updChangeCustomerInfo] iObjectType:"+iObjectType);
      System.out.println("[updChangeCustomerInfo] lCustomerId:"+lCustomerId);
      System.out.println("[updChangeCustomerInfo] lSiteId:"+lSiteId);
      System.out.println("[updChangeCustomerInfo] strSiteNameNew:"+strSiteNameNew);
      System.out.println("[updChangeCustomerInfo] strJobTitleNew:"+strJobTitleNew);
      System.out.println("[updChangeCustomerInfo] strDptoNew:"+strDptoNew);
      
      System.out.println("[updChangeCustomerInfo] strReferenciaNew:"+strReferenciaNew);        //SAR 0037-167824
      
      strMessage=objCustomerDAO.updChangeCustomerEdit(lOrderId,iObjectType,lCustomerId,lSiteId,
                                                  strSiteNameNew,objCustomerBeanNewAct, objAddressBeanNewAct, 
                                                  objContactBeanNewAct,av_loggin,  conn);    
      
      if (strMessage!=null)  
          throw new Exception(strMessage);  
          
      System.out.println("[updChangeCustomerInfo] TERMINA DE ACTUALIZAR EN CRM:"+strMessage);
      

    
         //Actualizar en BSCS 
         //------------------
           MessageVO messageVO = new MessageVO();
         
          //Valores requeridos para actualizar en BSCS 
          //------------------------------------------
          long CS_ID=0;
          if (lSiteId!=0){
              CS_ID=objCustomerDAO.getCustomerIdBSCS(lSiteId,Constante.CUSTOMERTYPE_SITE);      
          }else{
              CS_ID=objCustomerDAO.getCustomerIdBSCS(lCustomerId,Constante.CUSTOMERTYPE_CUSTOMER);
          }
          
          System.out.println("CS_ID:"+CS_ID);
        
          //Encontramos el valor de secuencia para la direccion de facturación
          //----------------------------------------------------------------------
          HashMap hshRetorno =AdressCustomerRead.getAddressCustomer(CS_ID, strIdAPP);
          strMessage=(String)hshRetorno.get("strMessage");                   
          if (strMessage!=null)
               throw new Exception(strMessage);  
        
         String wv_adr_seq=(String)hshRetorno.get("wv_adr_seq");  
         System.out.println("[updChangeCustomerInfo] Valores que ingresan a BSCS para la actualización------>");
         System.out.println("[updChangeCustomerInfo] El valor wv_adr_seq obtenido de BSCS: "+wv_adr_seq);
         System.out.println("[updChangeCustomerInfo] Area Telefono 1:"+objCustomerBeanNewAct.getDepMainphonearea());
         System.out.println("[updChangeCustomerInfo] Telefono 1:"+objCustomerBeanNewAct.getSwMainPhone());
         System.out.println("[updChangeCustomerInfo] Area Telefono 2:"+objCustomerBeanNewAct.getDepPhone2areacode());
         System.out.println("[updChangeCustomerInfo] Telefono 2:"+objCustomerBeanNewAct.getSwPhone2());
         System.out.println("[updChangeCustomerInfo] Area Telefono 3:"+objCustomerBeanNewAct.getNpPhone3areacode());
         System.out.println("[updChangeCustomerInfo] Telefono 3:"+objCustomerBeanNewAct.getSwPhone3());
         System.out.println("[updChangeCustomerInfo] Area Fax :"+objCustomerBeanNewAct.getSwMainFaxArea());
         System.out.println("[updChangeCustomerInfo] Fax :"+objCustomerBeanNewAct.getSwMainFax());
         System.out.println("[updChangeCustomerInfo] Región de Cta Facturacion:"+objAddressBeanNewAct.getSwregionid());
         System.out.println("[updChangeCustomerInfo] Dirección 1 Cta Facturacion:"+objAddressBeanNewAct.getSwaddress1());
         System.out.println("[updChangeCustomerInfo] Dirección 2 Cta Facturacion:"+objAddressBeanNewAct.getSwaddress2());
         System.out.println("[updChangeCustomerInfo] Dirección 3 Cta Facturacion:"+objAddressBeanNewAct.getSwaddress3());
         System.out.println("[updChangeCustomerInfo] Dirección 4 Cta Facturacion:"+objAddressBeanNewAct.getSwaddress4());
         System.out.println("[updChangeCustomerInfo] Código de Dirección Cta Facturacion:"+objAddressBeanNewAct.getSwzip());
         System.out.println("[updChangeCustomerInfo] Departamento de Dirección Cta Facturacion:"+objAddressBeanNewAct.getSwstate());
         System.out.println("[updChangeCustomerInfo] Provincia de Dirección Cta Facturacion:"+objAddressBeanNewAct.getSwprovince());
         System.out.println("[updChangeCustomerInfo] Ciudad de Dirección Cta Facturacion:"+objAddressBeanNewAct.getSwcity());
         System.out.println("[updChangeCustomerInfo] Título:"+objContactBeanNewAct.getSwtitle());
         System.out.println("[updChangeCustomerInfo] Nombre del Contacto:"+objContactBeanNewAct.getSwfirstname());
         System.out.println("[updChangeCustomerInfo] A. Paterno del Conacto:"+objContactBeanNewAct.getSwlastname());
         System.out.println("[updChangeCustomerInfo] A. Materno del Contacto:"+objContactBeanNewAct.getSwmiddlename());
         System.out.println("[updChangeCustomerInfo] Desc. Job del Contacto:"+objContactBeanNewAct.getSwjobtitle());
         System.out.println("[updChangeCustomerInfo] Referencia:"+objAddressBeanNewAct.getSwregionname());     //SAR 0037-167824
         
        
          //Invocamos al Metodo de la clase que hara el processo de actualizar en BSCS
          //-------------------------------------------------------------------------
          DataCustomerUpdate dataCustomerUpdate = new DataCustomerUpdate();      
          strMessage = dataCustomerUpdate.updateAddressCustomer(objCustomerBeanNewAct,
                       objAddressBeanNewAct, objContactBeanNewAct, CS_ID, wv_adr_seq, 
                       strIdAPP,lTitleNew,strSiteNameNew);
         
          if (strMessage!=null) 
               throw new Exception(strMessage);  
          
          //Actualizamos el BillingAccount en BSCS
         //--------------------------------------
         if(lBillAccId>0){   //Si existe un codigo de Billingaccount
           boolean av_create=false;
                        
          //Enviamos todos los valores al proceso que actualizara el BillingAccount en BSCS
          //-------------------------------------------------------------------------------
          strMessage = dataCustomerUpdate.updateBillingCustomer(strBillAccNameNew, lBillAccId, av_create,CS_ID,
                                                              strIdAPP);  
          if (strMessage!=null) 
              throw new Exception(strMessage);  
         }   
        
        System.out.println("[updChangeCustomerInfo] TERMINA DE ACTUALIZAR EN BSCS:"+strMessage); 
       }
      }catch(Exception ex){
         strMessage=ex.getMessage();
      }
      
   
  
   return strMessage;        
}      
}