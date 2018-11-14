<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.BillingAccountService"%>
<%@ page import="pe.com.nextel.service.SiteService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
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
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();   
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
   String strCustomerBSCS= MiUtil.getString((String)hshParam.get("strCodBSCS"));   
 
 /*
   long lCustomerId=1063;
   long lSiteId=0;    
   long lOrderId=2006800;
   String strSessionId="98102396";   
   String strCustomerBSCS= "2.35";  
 */ 
   //DATOS QUE ENTRAN AL PORTLET EDICION
   //-----------------------------------
   System.out.println("DATOS EN EDITDISPLAY");
   System.out.println("strCustomerId:"+lCustomerId);
   System.out.println("strSiteId:"+lSiteId);
   System.out.println("strOrderId:"+lOrderId);
   System.out.println("strSessionId:"+strSessionId);
   System.out.println("strCodBSCS:"+strCustomerBSCS);
   
   
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
   ArrayList arrHeader=null;
   ArrayList arrLista=null;   
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
   String strTitle=null;
   String strTitleId=null;
   String strJobTitle=null;
   String strJobTitleId=null;
   
   
   
   //PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);     
   System.out.println("[EditOnDisplayChangeCustomerInfo]Sesión a consultar : " + strSessionId);
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[EditOnDisplayChangeCustomerInfo]Sesión a consultar : " + objPortalSesBean);
   if( objPortalSesBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   
   String strAppID=objPortalSesBean.getAppId()+"";     
   
   if (lSiteId!=0){
      lObjectId=lSiteId;
      strObjectType=Constante.CUSTOMERTYPE_SITE;
      iObjectType=2;     //1=CUSTOMER   2=SITE    3=BILLINGACCOUNT
   }else{
      lObjectId=lCustomerId;
      strObjectType=Constante.CUSTOMERTYPE_CUSTOMER; 
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
            System.out.println("strBillingAccName:"+strBillingAccName);
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
  <input type="hidden" name="hdnCSID" value="<%=strCustomerBSCS%>"/>
  <input type="hidden" name="hdnAppId" value="<%=strAppID%>"/>
  <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>"/>
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
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone1New','spanPhone1New','txtPhone1New')">Area 1</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone1New" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainPhoneArea())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone1New','spanPhone1New','txtPhone1New')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone1New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainPhone())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone1New">
                <%= MiUtil.getString(objCustomerBeanNew.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone2New','spanPhone2New','txtPhone2New')">Area 2</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone2New" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getNpPhone2areacode())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone2New','spanPhone2New','txtPhone2New')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone2New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBeanNew.getSwPhone2())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone2New">
                <%= MiUtil.getString(objCustomerBeanNew.getDepPhone2areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone3New','spanPhone3New','txtPhone3New')">Area 3</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone3New" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getNpPhone3areacode())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone3New','spanPhone3New','txtPhone3New')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone3New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBeanNew.getSwPhone3())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone3New">
                <%= MiUtil.getString(objCustomerBeanNew.getDepPhone3areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodeFaxNew','spanFaxNew','txtFaxNew')">Area Fax</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodeFaxNew" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainFaxArea())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodeFaxNew','spanFaxNew','txtFaxNew')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtFaxNew" size="10" maxlength="7" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainFax())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanFaxNew">
                <%= MiUtil.getString(objCustomerBeanNew.getDepMainFaxArea())%>
              </span>
            </small>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="20%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top" id="tdCustomer">Cliente&nbsp; 
            <a href="javascript:fxExpand('tblCustomer');">
              <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif" alt="Ver datos de cliente" width="15" height="15" border="0"/>
            </a>
          </td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table id="tblCustomer" style="display:'none'" border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 1</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhoneArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhone())%> 
            <small><span id="spanPhone1Custm">
                <%= MiUtil.getString(objCustomerBean.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 2</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone2areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone2())%> 
            <small><span id="spanPhone2Custm">
                <%= MiUtil.getString(objCustomerBean.getDepPhone2areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 3</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone3areacode())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone3())%> 
            <small><span id="spanPhone3Custm">
                <%= MiUtil.getString(objCustomerBean.getDepPhone3areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFax())%> 
            <small><span id="spanFaxCustm">
                <%= MiUtil.getString(objCustomerBean.getDepMainFaxArea())%>
              </span>
            </small>
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
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Codigo</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("npcodbscs"))%>
          </td>
          <td align="left" class="CellLabel" width="20%">Nombre del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtSiteNameNew" size="40" maxlength="40" value="<%=strSiteNameNew%>" onchange="this.value=trim(this.value.toUpperCase())"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">
            <a href="javascript:fxCodeArea('txtCodePhone1New','spanPhone1New','txtPhone1New')">Area 1</a> 
          </td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <input type="text" name="txtCodePhone1New" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainPhoneArea())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone1New','spanPhone1New','txtPhone1New')"/>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtPhone1New" size="10" maxlength="7" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainPhone())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone1New">
                <%= MiUtil.getString(objCustomerBeanNew.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">
            <a href="javascript:fxCodeArea('txtCodeFaxNew','spanFaxNew','txtFaxNew')">Area Fax</a> 
          </td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <input type="text" name="txtCodeFaxNew" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainFaxArea())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodeFaxNew','spanFaxNew','txtFaxNew')"/>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtFaxNew" size="10" maxlength="7" value="<%=MiUtil.getString(objCustomerBeanNew.getSwMainFax())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanFaxNew">
                <%= MiUtil.getString(objCustomerBeanNew.getDepMainFaxArea())%>
              </span>
            </small>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="20%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top" id="tdSite">Site&nbsp; 
            <a href="javascript:fxExpand('tblSite');">
              <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif" alt="Ver datos del site" width="15" height="15" border="0"/>
            </a>
          </td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table id="tblSite" style="display:'none'" border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
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
  <% }%>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="60%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top">Billing&nbsp;Account Modificado</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Nombre</td>
          <td align="left" class="CellContent" width="35%">
            <input type="text" name="txtBillAccNameNew" size="50" maxlength="30" onchange="this.value=trim(this.value.toUpperCase())" value="<%=strBillingAccNameNew%>"  <% if (strBillingAccName==""){%>  readonly <%}%>/>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="30%">
        <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"/>
          <td class="SubSectionTitle" align="left" valign="top" id="tdBillAcc">Billing&nbsp;Account 
            <a href="javascript:fxExpand('tblBillAcc');">
              <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif" alt="Ver datos del billing account" width="15" height="15" border="0"/>
            </a>
          </td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
        </tr>
      </table>
      <table id="tblBillAcc" style="display:'none'" border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Nombre</td>
          <td align="left" class="CellContent" width="35%">
            <%= strBillingAccName%>
            <input type="hidden" name="hdnBillingAccId" value="<%=MiUtil.getString(objBillingAccBean.getSwCustomerId())%>"/>
            <!--select name=&quot;cmbBillingAcc&quot; style=&quot;width: 50%;&quot; onchange=&quot;javascript:document.frmdatos.hndBillAccName.value=this[this.selectedIndex].text&quot;&gt;                         
                  &lt;option value=&quot;0&quot;&gt;&lt;/option-->
            <%  /*BillingAccountBean objBillingAcBean=null;
                  for (int k=0;k<arrBillAcc.size();k++){ 
                     objBillingAcBean= (BillingAccountBean)arrBillAcc.get(k);*/
               %>
            <!--option value=&quot;&lt;%-- =MiUtil.getString(objBillingAcBean.getNpBillaccountNewId())--%&gt;&quot;-->
            <%-- =MiUtil.getString(objBillingAcBean.getNpBillacCName2())--%>
            <!--/option-->
            <%  //}%>
            <!--/select&gt;
               &lt;input type=&quot;hidden&quot; name=&quot;hndBillAccName&quot; value=&quot;&lt;%-- =strBillingAccName--%&gt;&quot;/&gt;
               &lt;script&gt;
                  document.frmdatos.cmbBillingAcc.value=&quot;&lt;%-- =MiUtil.getString(objBillingAccBean.getSwCustomerId())--%&gt;&quot;;
               &lt;/script-->
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <!--Contacto y Direccion de Facturacion -->
  <!--tr&gt;&lt;td style=&quot;height: '2%'&quot;&gt;&amp;nbsp;&lt;/td&gt;&lt;/tr-->
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
            <select name="cmbRegionNew">
              <%= MiUtil.buildComboSelected(arrRegion,"swregionid","swname",MiUtil.getString(objAddressBeanNew.getSwregionid()))%>
            </select>
          </td>
          <td align="left" class="CellLabel" width="8%">T&iacute;tulo</td>
          <td align="left" class="CellContent" width="14%">
            <select name="cmbTitleNew" style="width: 20%;">
              <%  hshData = objGeneralService.getTableList("PERSON_TITLE","1"); 
                  strMessage=(String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage);                               
                  arrLista=(ArrayList)hshData.get("arrTableList");
                  strTitle=MiUtil.getString(objContactBeanNew.getSwtitle());
                  strTitleId=MiUtil.getValueId(arrLista,"wv_npValue","wv_npValueDesc",strTitle);
                  System.out.println("VALOR DEL TITULO :"+strTitle);
                  System.out.println("VALOR DEL TITULO ID  :"+strTitleId);
               %>
              <%= MiUtil.buildComboSelected(arrLista,"wv_npValueDesc","wv_npValueDesc",strTitle)%>
            </select>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%" height="31" rowspan="3" valign="top">Direcci&oacute;n </td>
          <td align="left" class="CellContent" width="20%" height="31">
            <input type="text" name="txtAddress1New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="100" value="<%=MiUtil.getString(objAddressBeanNew.getSwaddress1())%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%" height="31">Nombres</td>
          <td align="left" class="CellContent" width="14%" height="31">
            <input type="text" name="txtFirstNameNew" size="40" maxlength="40" onchange="this.value=trim(this.value.toUpperCase())" value="<%=MiUtil.getString(objContactBeanNew.getSwfirstname())%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtAddress2New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="50" value="<%=MiUtil.getString(objAddressBeanNew.getSwaddress2())%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap. Paterno</td>
          <td align="left" class="CellContent" width="14%">
            <input type="text" name="txtLastNameNew" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())" value="<%=MiUtil.getString(objContactBeanNew.getSwlastname())%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtAddress3New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="50" value="<%=MiUtil.getString(objAddressBeanNew.getSwaddress3())%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%">Ap.&nbsp;Materno</td>
          <td align="left" class="CellContent" width="14%">
            <input type="text" name="txtMiddleNameNew" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())" value="<%=MiUtil.getString(objContactBeanNew.getSwmiddlename())%>"/>
          </td>
        </tr>
        <tr>
         <td align="left" class="CellLabel" width="8%">Departamento</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbDpto" onChange="fxLoadPlace(1);DeleteSelectOptions(document.frmdatos.cmbDist);DeleteSelectOptions(document.frmdatos.cmbProv);javascript:document.frmdatos.hdnDptoNew.value=this[this.selectedIndex].text">
              <%  hshData = objGeneralService.getUbigeoList(0,0,"0"); 
                     strMessage=(String)hshData.get("strMessage");
                     if (strMessage!=null)
                        throw new Exception(strMessage);                                  
                     arrLista=(ArrayList)hshData.get("arrUbigeoList");                     
                  %>
              <%= MiUtil.buildComboSelected(arrLista,"ubigeo","nombre",MiUtil.getString(objAddressBeanNew.getNpdepartamentoid()))%>
            </select>
            <input type="hidden" name="hdnDptoNew" value="<%=MiUtil.getString(objAddressBeanNew.getSwstate())%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%">Cargo</td>
          <td align="left" class="CellContent" width="14%">
            <select name="cmbJobTitle" style="width: '90%'">
              <%  hshData = objGeneralService.getTitleList(); 
                  strMessage= (String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage);                              
                  arrLista=(ArrayList)hshData.get("arrTitleList");
                  strJobTitle=MiUtil.getString(objContactBeanNew.getSwjobtitle());
                  strJobTitleId=MiUtil.getValueId(arrLista,"jobtitlteId","descripcion",strJobTitle);
                  System.out.println("VALOR DEL JOBTITLE :"+strJobTitle);
                  System.out.println("VALOR DEL JOBTITLE ID  :"+strJobTitleId);
               %>
              <%= MiUtil.buildComboSelected(arrLista,"jobtitlteId","descripcion",strJobTitleId)%>
            </select>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Provincia</td>
          <td align="left" class="CellContent" width="20%">
              <select name="cmbProv" onChange="fxLoadPlace(2);DeleteSelectOptions(document.frmdatos.cmbDist);javascript:document.frmdatos.hdnProvNew.value=this[this.selectedIndex].text" onclick="fxFirstLoadPlace(1);">
              <%= MiUtil.buildComboWithOneOption(MiUtil.getString(objAddressBeanNew.getNpprovinciaid()),MiUtil.getString(objAddressBeanNew.getSwprovince()))%>
            </select>
            <input type="hidden" name="hdnProvNew" value="<%=MiUtil.getString(objAddressBeanNew.getSwprovince())%>"/>
            </td>
          <td align="left" class="CellLabel" width="8%">E-Mail</td>
          <td align="left" class="CellContent" width="14%">
            <input type="text" name="txtEmailNew" size="40" maxlength="80" value="<%=MiUtil.getString(objContactBeanNew.getSwemailaddress())%>" onchange="this.value=trim(this.value.toLowerCase())"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Distrito</td>
          <td align="left" class="CellContent" colspan="3">
            <select name="cmbDist" onChange="fxLoadCodPostal(this.selectedIndex);javascript:document.frmdatos.hdnDistNew.value=this[this.selectedIndex].text" onclick="fxFirstLoadPlace(2);">
              <%= MiUtil.buildComboWithOneOption(MiUtil.getString(objAddressBeanNew.getNpdistritoid()),MiUtil.getString(objAddressBeanNew.getSwcity()))%>
            </select>
            <input type="hidden" name="hdnDistNew" value="<%=MiUtil.getString(objAddressBeanNew.getSwcity())%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Cod. Postal</td>
          <td align="left" class="CellContent" colspan="3">
            <input type="text" name="txtZipNew" value="<%=MiUtil.getString(objAddressBeanNew.getSwzip())%>" readOnly/>
          </td>
        </tr>
		
		   
		    <!-- SAR 0037-164510 INI-->
        
        <tr>
          <td align="left" class="CellLabel" width="10%">Referencia</td>
          <td align="left" class="CellContent" colspan="3" width="20%">
           <input type="text" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" name="txtRefNew"value="<%=MiUtil.getString(objAddressBeanNew.getSwnote())%>"/>
          </td>
        </tr>




        <!--  SAR 0037-164510 FIN--> 

	  
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr class="PortletHeaderColor">
          <td class="SubSectionTitleLeftCurve" width="18"/>
          <td id="tdContDireFac" class="SubSectionTitle" align="left" valign="top">Direcci&oacute;n y Contacto de Facturaci&oacute;n
              <a href="javascript:fxExpand('tblContDireFac');">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif" alt="Ver datos del contacto y dirección de facturación" width="15" height="15" border="0"/>
                </a>
         </td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"/>
      </tr>
      </table>
      <table id="tblContDireFac" style="display:'none'" border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        
        <tr>
          <td align="left" class="CellLabel" width="8%">Regi&oacute;n</td>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtRegionName" size="20" style="border :0; background-color:transparent;" value="<%=MiUtil.getDescripcion(arrRegion,"swregionid","swname",MiUtil.getString(objAddressBean.getSwregionid()))%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%">T&iacute;tulo</td>
          <td align="left" class="CellContent" width="14%">&nbsp;
            <%= MiUtil.getString(objContactBean.getSwtitle())%>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%" rowspan="3"  valign="top">Direcci&oacute;n </td>
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
  <!--Fin Contacto y Direccion de Facturacion-->
</table>


<script language="JavaScript">

   function fxCodeArea(nAreaCode, nAreaNom, ntelf ){
      var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName=frmdatos";
      url="<%=strURLAreaCode%>"+param;
      window.open(url ,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=330,height=400");
   }

   function fxSearchAreaCode(code, nAreaCode ,nAreaNom, nTelf){
      if (code==""){
         eval("document.frmdatos."+nAreaCode+".value = ''");
         eval(nAreaNom + ".innerHTML = ''");
         eval("document.frmdatos."+nTelf+".focus();");
         return;
      }
      cadena = "";
      bEntro = false;
      for(j=0;j<vAreaCode.size();j++){
         objAreaCode = vAreaCode.elementAt(j);
         if (objAreaCode.codearea==code){
            if (cadena!=""){ cadena = cadena+"/"; }
            cadena = cadena + objAreaCode.name;
            bEntro = true;
         }
      }
      if ( bEntro == true){
         eval("document.frmdatos."+nAreaCode+".value = "+code);
         eval(nAreaNom + ".innerHTML = '("+cadena+")'");
         eval("document.frmdatos."+nTelf+".focus();");
      }else{
         fxCodeArea(nAreaCode, nAreaNom, nTelf);
         eval("document.frmdatos."+nAreaCode+".value = ''");
      }
      return bEntro;
   }      

   function fxAreaCode(n1,n2){
      this.name = n1;
      this.codearea = n2;
   }
   var vAreaCode = new Vector();  
   var flag1=0;
   var flag2=0;

   function fxLoadPlace(tipo) {                 
      Form = document.frmdatos;           
      if (tipo==1){
         flag1=1;
      }else{
         flag2=1;
      }
      
      depId= document.frmdatos.cmbDpto.value; 
      provId= document.frmdatos.cmbProv.value;  
      distId= document.frmdatos.cmbDist.value;
      
      if (tipo==1 && depId==""){ //  0: Carga Departamentos 1: Carga Provincia  2: Carga Distrito                                                 
          DeleteSelectOptions(document.frmdatos.cmbDist); 
          DeleteSelectOptions(document.frmdatos.cmbProv);                 
          Form.txtZipNew.value="";
      }else if (tipo==2 && (provId=="" || provId=="0")){
          DeleteSelectOptions(document.frmdatos.cmbDist);              
          Form.txtZipNew.value="";
      }else{                      
         Form.myaction.value="LoadUbigeo2";
         //DeleteSelectOptions(document.frmdatos.cmbDist); 
         var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&sDistId="+distId+"&nTipo="+tipo+"&sCodName=ubigeo&myaction=LoadUbigeo2&sFormName=frmdatos"; 
         parent.bottomFrame.location.replace(url);            
      }
   }   
   
   function fxFirstLoadPlace(tipo){
       if (tipo== 1 && flag1==0){
         flag1=1;
         //flag2=1;
         fxLoadPlace(tipo);       
       }
       if (tipo== 2 && flag2==0){
         flag2=1;
         fxLoadPlace(tipo);          
       }             
   }   
   
   function fxLoadCodPostal(posicion){
       form = document.frmdatos;     
       if (posicion==0) 
         form.txtZipNew.value="";
       else{           
          var dato=codPostal[posicion-1];
          form.txtZipNew.value=dato;
       }          
   }       

   function fxOnValidateChangeCustomerInfo(){
      form = document.frmdatos;
      /*if (form.cmbBillingAcc.value == 0){
         if (form.txtSiteNameNew ==""){
            alert("Ingrese un nuevo nombre para el Billing Account");
            return false;
         }
      }*/
      <%if (lSiteId==0){%>
         if (!fxValidatePhone('txtCodePhone1New','txtPhone1New'))
            return false;
         if (!fxValidatePhone('txtCodePhone2New','txtPhone2New'))
            return false;
         if (!fxValidatePhone('txtCodePhone3New','txtPhone3New'))
            return false;
         if (!fxValidatePhone('txtCodeFaxNew','txtFaxNew'))
            return false;
      <%}else{%>
         if (!fxValidatePhone('txtCodePhone1New','txtPhone1New'))
            return false;
         if (!fxValidatePhone('txtCodeFaxNew','txtFaxNew'))
            return false;
      <%}%>
      return true;
   }   
   function fxValidatePhone(codname,phonename){
      form = document.frmdatos;
      var codvalor;
      var phonevalor;
      eval("codvalor=form."+codname+".value");
      if(codvalor!=""){
         eval("phonevalor=form."+phonename+".value");
         if(phonevalor==""){
            alert("Ingrese un número telefónico.");
            eval("form."+phonename+".focus();");            
            return false;
         }      
      }
      return true;
   }

    function fxExpand(name){   
      if (name=='tblCustomer'){         
         if (tblCustomer.style.display==""){            
            tblCustomer.style.display='none';            
            var celda =document.getElementById("tdCustomer");            
            celda.innerHTML="Cliente&nbsp;<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif' alt='Ver datos de cliente' width=15 height=15 border=0></a>";            
         }else{
            tblCustomer.style.display='';
            var celda =document.getElementById("tdCustomer");
            celda.innerHTML="Cliente&nbsp;<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/minus.gif' alt='Ver datos de cliente' width=15 height=15 border=0></a>";         
         }            
      }
      if (name=='tblSite'){
         if (tblSite.style.display==""){
            tblSite.style.display='none';      
            var celda =document.getElementById("tdSite");
            celda.innerHTML="Site&nbsp;<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif' alt='Ver datos del site' width=15 height=15 border=0></a>";
         }else{
            tblSite.style.display='';      
            var celda =document.getElementById("tdSite");
            celda.innerHTML="Site&nbsp;<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/minus.gif' alt='Ver datos del site' width=15 height=15 border=0></a>";         
         }
      }
      if (name=='tblContDireFac'){
         if (tblContDireFac.style.display==""){ //se muestra
            tblContDireFac.style.display='none';      
            var celda =document.getElementById("tdContDireFac");
            celda.innerHTML="Dirección y Contacto de Facturación<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif' alt='Ver datos del contacto y dirección de facturación' width=15 height=15 border=0></a>";
         }else{//no se muestra
            tblContDireFac.style.display='';      
            var celda =document.getElementById("tdContDireFac");
            celda.innerHTML="Dirección y Contacto de Facturación<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/minus.gif' alt='Ver datos del contacto y dirección de facturación' width=15 height=15 border=0></a>";         
         }
      }       
      if(name=='tblBillAcc'){
         if (tblBillAcc.style.display==""){ //se muestra
            tblBillAcc.style.display='none';      
            var celda =document.getElementById("tdBillAcc");
            celda.innerHTML="Billing&nbsp;Account<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/plus.gif' alt='Ver datos del billing account' width=15 height=15 border=0></a>";                    
         }else{//no se muestra
            tblBillAcc.style.display='';      
            var celda =document.getElementById("tdBillAcc");
            celda.innerHTML="Billing&nbsp;Account<a href=javascript:fxExpand('"+name+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/minus.gif' alt='Ver datos del billing account' width=15 height=15 border=0></a>";
         }  
      } 
   } 
   
</script>
<%  hshData=objGeneralService.getAreaCodeList(null,null);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);       
   ArrayList arrAreaCode=(ArrayList)hshData.get("arrAreaCodeList");
   
   HashMap hshMapData=null;
   for (int i=0;i<arrAreaCode.size();i++){
       hshMapData=((HashMap)arrAreaCode.get(i));                  
%>
<script>                
      vAreaCode.addElement(new fxAreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
     </script>
     <%   }

}catch(Exception ex){
  System.out.println("Error try catch-->"+ex.getMessage());   
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<% 
}%>
