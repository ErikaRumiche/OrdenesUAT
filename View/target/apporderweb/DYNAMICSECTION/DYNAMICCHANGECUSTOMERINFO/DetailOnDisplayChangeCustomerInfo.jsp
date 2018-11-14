<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.BillingAccountService"%>
<%@ page import="pe.com.nextel.service.SiteService"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.bean.AddressObjectBean"%>
<%@ page import="pe.com.nextel.bean.BillingAccountBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.ContactObjectBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>
<% 
try{
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
   if (hshParam==null) hshParam=new Hashtable();   
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   
   GeneralService objGeneralService=new GeneralService();
   CustomerService objCustomerService=new CustomerService();
   BillingAccountService objBillingAccService=new BillingAccountService();
   SiteService objSiteService=new SiteService();
   CustomerBean objCustomerBean=null;
   CustomerBean objCustomerBeanNew=null;   
   CustomerBean objBillingAccBean=null;
   AddressObjectBean objAddressBean=null;
   AddressObjectBean objAddressBeanNew=null;
   ContactObjectBean objContactBean=null;
   ContactObjectBean objContactBeanNew=null;
   ArrayList arrLista=null;  
   ArrayList arrHeader=null;
   HashMap hshData=null;
   HashMap hshSite=null;
   String strMessage=null;
   String strObjectType=null;
   long lObjectId=0;
   int iObjectType=0;
   String strSiteName="";
   String strSiteNameNew="";
   String strBillingAccName="";
   String strBillingAccNameNew="";
   
   if (lSiteId!=0){
      lObjectId=lSiteId;
      strObjectType="SITE";
      iObjectType=2;     //1=CUSTOMER   2=SITE    3=BILLINGACCOUNT
   }else{
      lObjectId=lCustomerId;
      strObjectType="CUSTOMER";
      iObjectType=1; 
    }
   
   //Extrayendo los datos que se están pidiendo modificar 
   hshData=objCustomerService.getHeaderChange(lOrderId,iObjectType,lObjectId);   
   strMessage=(String)hshData.get("strMessage");
   
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
   arrHeader=(ArrayList)hshData.get("arrHeader");   
   int iSize=(arrHeader==null?0:arrHeader.size());
   if (iSize>0){
      for(int i=0;i<iSize;i++){
         hshData=(HashMap)arrHeader.get(i);
         // si los datos pertenecen al customer o al site
         if ( Constante.TYPE_CUSTOMER==MiUtil.parseInt((String)hshData.get("strType")) || Constante.TYPE_SITE==MiUtil.parseInt((String)hshData.get("strType"))){ 
            objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");
            objCustomerBeanNew=(CustomerBean)hshData.get("objCustomerBeanNew");               
            strSiteName=MiUtil.getString((String)hshData.get("strName"));
            strSiteNameNew=MiUtil.getString((String)hshData.get("strNameNew"));            
         }else{
            // De este objeto solo usaremos el Id = swcustomerId que almacena el id del billing account
            objBillingAccBean=(CustomerBean)hshData.get("objCustomerBean");            
            strBillingAccName=MiUtil.getString((String)hshData.get("strName"));
            strBillingAccNameNew=MiUtil.getString((String)hshData.get("strNameNew"));            
         }         
      }
   }
      
   if (objCustomerBean==null) objCustomerBean=new CustomerBean();      
   if (objCustomerBeanNew==null) objCustomerBeanNew=new CustomerBean();      
   
   if (objBillingAccBean==null) objBillingAccBean=new CustomerBean();    

   
   //Extrayendo el detalle de la direccion de Facturacion 
   hshData=objCustomerService.getAddressChange(lOrderId,iObjectType,lObjectId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 

   objAddressBean=(AddressObjectBean)hshData.get("objAddressBean");   
   objAddressBeanNew=(AddressObjectBean)hshData.get("objAddressBeanNew");  
   
   if (objAddressBean==null) objAddressBean=new AddressObjectBean();
   if (objAddressBeanNew==null) objAddressBeanNew=new AddressObjectBean();
   
    
   //Extrayendo el detalle del contacto de Facturacion
   hshData=objCustomerService.getContactChange(lOrderId,iObjectType,lObjectId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 

   objContactBean=(ContactObjectBean)hshData.get("objContactBean");  
   
   System.out.println("objContactBean :" + objContactBean.getSwjobtitleid());
   System.out.println("objContactBean :" + objContactBean.getSwjobtitle());
   System.out.println("objContactBean :" + objContactBean.getSwtitle());

   
   objContactBeanNew=(ContactObjectBean)hshData.get("objContactBeanNew");   
   if (objContactBean==null) objContactBean=new ContactObjectBean();
   if (objContactBeanNew==null) objContactBeanNew=new ContactObjectBean();
   
   //Obteniendo el listado de regiones
   hshData =objGeneralService.getRegionList(); 

   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");    

   hshData=objBillingAccService.BillingAccountDAOgetBillingAccountList(lCustomerId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrBillAcc=(ArrayList)hshData.get("objArrayList");  

   
   //Rutas
   String strRutaContext=request.getContextPath();       
   String strURLAreaCode=strRutaContext+"/GENERALPAGE/AreaCodeList.jsp";
   String strURLOrderServlet =strRutaContext+"/editordersevlet";     
   
   
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <% if(lSiteId==0){%>
  <tr>
    <td width="50%">
      <table border="0" cellspacing="0" cellpadding="0" width="30%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Cliente Modificado</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 1</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainPhoneArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainPhone())%>&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getDepMainphonearea())%> 
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 2</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getNpPhone2areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwPhone2())%>&nbsp; 
            <%= MiUtil.getString(objCustomerBeanNew.getDepPhone2areacode())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 3</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getNpPhone3areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwPhone3())%>&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getDepPhone3areacode())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainFaxArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainFax())%>&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getDepMainFaxArea())%> 
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="30%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Cliente</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 1</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhoneArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhone())%> &nbsp;
            <%= MiUtil.getString(objCustomerBean.getDepMainphonearea())%> 
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 2</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone2areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone2())%> &nbsp;
            <%= MiUtil.getString(objCustomerBean.getDepPhone2areacode())%> 
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 3</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone3areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone3())%> &nbsp;
            <%= MiUtil.getString(objCustomerBean.getDepPhone3areacode())%> 
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFax())%> &nbsp;
            <%= MiUtil.getString(objCustomerBean.getDepMainFaxArea())%> 
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <%  }else if (lSiteId!=0){
      hshData=objSiteService.getSiteDetail(lCustomerId,lSiteId);   
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage); 
      hshSite=(HashMap)hshData.get("hshData");
      
      if (hshSite==null) hshSite=new HashMap();    
      
   %>
  <tr>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Site Modificado</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Codigo</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("npcodbscs"))%>
          </td>
          <td align="left" class="CellLabel" width="20%">Nombre del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strSiteNameNew%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">Area 1</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainPhoneArea())%>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainPhone())%> &nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getDepMainphonearea())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">Area Fax</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainFaxArea())%>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getSwMainFax())%> &nbsp;
            <%= MiUtil.getString(objCustomerBeanNew.getDepMainFaxArea())%> 
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="15%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Site</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Codigo del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("npcodbscs"))%>
          </td>
          <td align="left" class="CellLabel" width="20%">Nombre del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strSiteName%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">&nbsp;Area 1</td>
          <td align="left" class="CellContent" width="10%">
            <%=  MiUtil.getString(objCustomerBean.getSwMainPhoneArea())%>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhone())%> 
            <small><span id="spanPhone1Site">
                <%= MiUtil.getString(objCustomerBean.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFax())%> 
            <small><span id="spanFaxSite">
                <%= MiUtil.getString(objCustomerBean.getDepMainFaxArea())%>
              </span>
            </small>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <% }
      hshData = objGeneralService.getTitleList(); 
      strMessage= (String)hshData.get("strMessage");
      if (strMessage!=null)
        throw new Exception(strMessage);                              
      arrLista=(ArrayList)hshData.get("arrTitleList");      
   %>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="45%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Cuentas Facturaci&oacute;n Modificado</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Nombre</td>
          <td align="left" class="CellContent" width="35%">
            <%= strBillingAccNameNew%>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="40%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Cuentas Facturaci&oacute;n</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Nombre</td>
          <td align="left" class="CellContent" width="35%">
            <%= strBillingAccName%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <!--Billing Account-->
  <tr>
    <td style="height: '7%'">&nbsp;</td>
  </tr>
  <tr>
    <td width="50%">
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td colspan="2" class="CellLabel">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr class="PortletHeaderColor">
                <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
                <td class="SubSectionTitle" align="left" valign="top">Direcci&oacute;n Facturaci&oacute;n Modif.</td>
                <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
              </tr>
            </table>
          </td>
          <td colspan="2" class="CellLabel">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr class="PortletHeaderColor">
                <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
                <td class="SubSectionTitle" align="left" valign="top">Contacto de Facturaci&oacute;n Modif.</td>
                <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Regi&oacute;n</td>
          <td align="left" class="CellContent" width="20%">
            <%= MiUtil.getDescripcion(arrRegion,"swregionid","swname",MiUtil.getString(objAddressBeanNew.getSwregionid()))%>
          </td>
           <td align="left" class="CellLabel" width="8%">Título</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwtitle()) %>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%" rowspan="3" valign="top">Direcci&oacute;n </td>
          <td align="left" class="CellContent" width="20%">
            <%= MiUtil.getString(objAddressBeanNew.getSwaddress1())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Nombres</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwfirstname())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">
            <%= MiUtil.getString(objAddressBeanNew.getSwaddress2())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap. Paterno</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwlastname())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">
            <%= MiUtil.getString(objAddressBeanNew.getSwaddress3())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap.&nbsp;Materno</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwmiddlename())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Departamento</td>
          <td align="left" class="CellContent" width="20%">
             <%= MiUtil.getString(objAddressBeanNew.getSwstate())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Cargo</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwjobtitle())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Provincia</td>
          <td align="left" class="CellContent" width="20%">
              <%= MiUtil.getString(objAddressBeanNew.getSwprovince())%>
          </td>
          <td align="left" class="CellLabel" width="8%">E-Mail</td>
          <td align="left" class="CellContent" width="14%">
            <%= MiUtil.getString(objContactBeanNew.getSwemailaddress())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Distrito</td>
          <td align="left" class="CellContent" colspan="3">
            <%= MiUtil.getString(objAddressBeanNew.getSwcity())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Cod. Postal</td>
          <td align="left" class="CellContent" colspan="3">
            <%= MiUtil.getString(objAddressBeanNew.getSwzip())%>
          </td>
        </tr>
       
       <!-- SAR 0037-167824 INI --> 
        <tr>
          <td align="left" class="CellLabel" width="10%">Referencia</td>
          <td align="left" class="CellContent" colspan="3" width="20%">          
          <%=MiUtil.getString(objAddressBeanNew.getSwnote())%>
          </td>
        </tr>
        <!-- SAR 0037-167824 FIN -->
        
        
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td colspan="2" class="CellLabel">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr class="PortletHeaderColor">
                <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
                <td class="SubSectionTitle" align="left" valign="top">Direcci&oacute;n Facturaci&oacute;n</td>
                <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
              </tr>
            </table>
          </td>
          <td colspan="2" class="CellLabel">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr class="PortletHeaderColor">
                <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
                <td class="SubSectionTitle" align="left" valign="top">Contacto de Facturaci&oacute;n</td>
                <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Regi&oacute;n</td>
          <td align="left" class="CellContent" width="20%">
            <%= MiUtil.getDescripcion(arrRegion,"swregionid","swname",MiUtil.getString(objAddressBean.getSwregionid()))%>
          </td>
          <td align="left" class="CellLabel" width="8%">T&iacute;tulo</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwtitle()) %>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%" rowspan="3" valign="top">Direcci&oacute;n </td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objAddressBean.getSwaddress1())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Nombres</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwfirstname())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objAddressBean.getSwaddress2())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap. Paterno</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwlastname())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objAddressBean.getSwaddress3())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap. Materno</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwmiddlename())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Departamento</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
              <%= MiUtil.getString(objAddressBean.getSwstate())%>
          </td>
          <td align="left" class="CellLabel" width="8%">Cargo</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwjobtitle())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Provincia</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
             <%= MiUtil.getString(objAddressBean.getSwprovince())%>
          </td>
          <td align="left" class="CellLabel" width="8%">E-Mail</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwemailaddress())%>
          </td>
        </tr>
        
        <tr>
          <td align="left" class="CellLabel" width="8%">Distrito</td>
          <td align="left" class="CellContent" colspan="3">&nbsp;
            <%= MiUtil.getString(objAddressBean.getSwcity())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Cod. Postal</td>
          <td align="left" class="CellContent" colspan="3">&nbsp;
           <%= MiUtil.getString(objAddressBean.getSwzip())%>
          </td>
        </tr>
        
        <!-- SAR 0037-167824 INI -->
        
        <tr>
          <td align="left" class="CellLabel" width="10%"> Referencia </td>
          <td align="left" class="CellContent" colspan="3" width="20%">&nbsp;
            <%= MiUtil.getString(objAddressBean.getSwnote())%>
          </td>
        </tr>
        
        <!-- SAR 0037-167824 FIN -->
        
        
      </table>
    </td>
  </tr>
  <!--Fin Billing Account-->
</table>
<%   
}catch(Exception ex){
  System.out.println("Error try catch-->"+ex.getMessage());   
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<% 
}%>
