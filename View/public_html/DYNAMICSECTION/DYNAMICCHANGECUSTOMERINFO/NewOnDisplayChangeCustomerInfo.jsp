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
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");  
   if (hshParam==null) hshParam=new Hashtable();   
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
   String strCustomerBSCS= MiUtil.getString((String)hshParam.get("strCodBSCS"));   
   
   GeneralService objGeneralService=new GeneralService();
   CustomerService objCustomerService=new CustomerService();
   BillingAccountService objBillingAccService=new BillingAccountService();
   SiteService objSiteService=new SiteService();
   CustomerBean objCustomerBean=new CustomerBean();
   AddressObjectBean objAddressBean=null;
   ContactObjectBean objContactBean=null;
   ArrayList arrLista=null;   
   HashMap hshData=null;
   HashMap hshSite=null;
   String strMessage=null;
   long lObjectId=0;
   String strObjectType=null;
   String strPhoneAreaSite=null;
   String strPhoneDepSite=null;
   String strFaxAreaSite=null;
   String strFaxDepSite=null;
 

   //PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   
   System.out.println("[NewOnDisplayChangeCustomerInfo]Sesión a consultar : " + strSessionId);
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[NewOnDisplayChangeCustomerInfo]Sesión a consultar : " + objPortalSesBean);
   if( objPortalSesBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   
   String strAppID  = objPortalSesBean.getAppId()+"";  
   
   System.out.println("-->lSiteId:"+lSiteId+"**");
   if (lSiteId!=0){
      System.out.println("INICIO CON RESPONSABLE DE PAGO");
      lObjectId=lSiteId;
      strObjectType=Constante.CUSTOMERTYPE_SITE;
      
      System.out.println("-->"+lCustomerId+" -->"+lSiteId);
      //Obteniendo detalle del site 
      hshData=objSiteService.getSiteDetail(lCustomerId,lSiteId);   
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage); 
      
      System.out.println("Luego de invicar a getSiteDerail");   
      hshSite=(HashMap)hshData.get("hshData");
      
      if (hshSite==null) hshSite=new HashMap();      
      
      strPhoneAreaSite=MiUtil.getString((String)hshSite.get("swofficephonearea"));
      strPhoneDepSite=MiUtil.getString(objGeneralService.getDepartmentName(strPhoneAreaSite));
      strFaxAreaSite=MiUtil.getString((String)hshSite.get("swfaxarea"));
      strFaxDepSite=MiUtil.getString(objGeneralService.getDepartmentName(strFaxAreaSite));   
      System.out.println("FIN CON RESPONSABLE DE PAGO");
      
   }else{
      lObjectId=lCustomerId;
      strObjectType=Constante.CUSTOMERTYPE_CUSTOMER; 
      
      // Datos del cliente
      hshData =objCustomerService.getCustomerData(lCustomerId);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage); 
         
      objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");   
      if (objCustomerBean==null) objCustomerBean=new CustomerBean();
    }
   
   //Extrayendo el detalle de la direccion de Facturacion
   String strRegion=null;
   String strDireccion1=null;
   String strDireccion2=null;
   String strDireccion3=null;
   String strDireccion4=null;
   String strCodPostal=null;
   String strDepartamento=null;
   String strProvincia=null;
   String strDistrito=null;
   String strDepartamentoId=null;
   String strProvinciaId=null;
   String strDistritoId=null;
   
   // SAR 0037-167824 INI
   String strReferencia=null;
   // SAR 0037-167824 FIN
   
   hshData=objCustomerService.getAddress(lObjectId,strObjectType,Constante.TYPE_ADDRESS_FACTURACION);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 

   objAddressBean=(AddressObjectBean)hshData.get("objAddressBean");   
   if (objAddressBean==null) objAddressBean=new AddressObjectBean();
   
   strRegion=  MiUtil.getString(objAddressBean.getSwregionname());   
   strDireccion1 = MiUtil.getString(objAddressBean.getSwaddress1());   
   strDireccion2 = MiUtil.getString(objAddressBean.getSwaddress2());   
   strDireccion3 = MiUtil.getString(objAddressBean.getSwaddress3());         
   strDireccion4 = MiUtil.getString(objAddressBean.getSwaddress4());  
   strCodPostal = MiUtil.getString(objAddressBean.getSwzip());
   strDepartamento = MiUtil.getString(objAddressBean.getSwstate());
   strProvincia = MiUtil.getString(objAddressBean.getSwprovince());
   strDistrito = MiUtil.getString(objAddressBean.getSwcity());
   strDepartamentoId= String.valueOf(objAddressBean.getNpdepartamentoid()); 
   strProvinciaId = String.valueOf(objAddressBean.getNpprovinciaid());
   strDistritoId = String.valueOf(objAddressBean.getNpdistritoid());
   
       
   // SAR 0037-167824 INI
   strReferencia = MiUtil.getString(objAddressBean.getSwnote());
   // SAR 0037-167824 FIN
 
   //Extrayendo el detalle del contacto de Facturacion
   String strTitle=null;
   String strNombres=null;
   String strLastName=null;
   String strMiddleName=null;
   String strJobTitle=null;
   String strEmail=null;
   String strTitleId=null;
   String strJobTitleId=null;
   
   System.out.println("DATOS PARA CONTACTOS DE FACTURACION:");
   System.out.println("lObjectId:"+lObjectId);
   System.out.println("DstrObjectType:"+strObjectType);
   System.out.println("Constante.TYPE_CONTACT_FACTURACION:"+Constante.TYPE_CONTACT_FACTURACION);
   
   hshData=objCustomerService.getContact(lObjectId,strObjectType,Constante.TYPE_CONTACT_FACTURACION);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 

   objContactBean=(ContactObjectBean)hshData.get("objContactBean");   
   if (objContactBean==null) objContactBean=new ContactObjectBean();
   
   strTitle =MiUtil.getString(objContactBean.getSwtitle());
   strNombres =MiUtil.getString(objContactBean.getSwfirstname());
   strLastName =MiUtil.getString(objContactBean.getSwlastname());
   strMiddleName =MiUtil.getString(objContactBean.getSwmiddlename());
   strJobTitle=MiUtil.getString(objContactBean.getSwjobtitle());
   strJobTitleId=MiUtil.getString(objContactBean.getSwjobtitleid());
   strEmail=MiUtil.getString(objContactBean.getSwemailaddress());
   
   
   System.out.println("VALORES PERSONALES DEL CLIENTE:");
   System.out.println("strTitleId:"+strTitleId);
   System.out.println("strJobTitleId:"+strJobTitleId);
   System.out.println("strTitle:"+strTitle);
   System.out.println("strJobTitle:"+strJobTitle);
   
   //Obtenemos los valores corretamtente de los datos personales
   //strTitleId=MiUtil.getDescripcion()
   
   //Obteniendo el listado de regiones
   hshData =objGeneralService.getRegionList(); 
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");    
   //Antes BillingAccountDAOgetBillingAccountList
   hshData=objBillingAccService.BillingAccountDAOgetBillingAccountListNew(lCustomerId,lSiteId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrBillAcc=(ArrayList)hshData.get("objArrayList");  

   
   //Rutas
   String strRutaContext=request.getContextPath();        
   String strURLAreaCode=strRutaContext+"/GENERALPAGE/AreaCodeList.jsp";
   String strURLOrderServlet =strRutaContext+"/editordersevlet";       

   System.out.println(" ----------- INICIO NewOnDisplayChangeCustomer.jsp---------------- ");
   System.out.println("nCustomerId-->"+lCustomerId);
   System.out.println("strCustomerBSCS-->"+strCustomerBSCS);
   System.out.println("strAppID-->"+strAppID);
   System.out.println("nSiteId-->"+lSiteId);      
   System.out.println(" ------------  FIN NewOnDisplayChangeCustomer.jsp----------------- ");       
   
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>"/>
  <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>"/>
  <input type="hidden" name="hdnCSID" value="<%=strCustomerBSCS%>"/>
  <input type="hidden" name="hdnAppId" value="<%=strAppID%>"/>
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
      <table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone1New','spanPhone1New','txtPhone1New')">Area 1</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone1New" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone1New','spanPhone1New','txtPhone1New')" value="<%=MiUtil.getString(objCustomerBean.getSwMainPhoneArea())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone1New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBean.getSwMainPhone())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone1New">
                <%= MiUtil.getString(objCustomerBean.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone2New','spanPhone2New','txtPhone2New')">Area 2</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone2New" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBean.getNpPhone2areacode())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone2New','spanPhone2New','txtPhone2New')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone2New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBean.getSwPhone2())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone2New">
                <%= MiUtil.getString(objCustomerBean.getDepPhone2areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodePhone3New','spanPhone3New','txtPhone3New')">Area 3</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodePhone3New" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone3New','spanPhone3New','txtPhone3New')" value="<%=MiUtil.getString(objCustomerBean.getNpPhone3areacode())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone3New" size="10" maxlength="8" value="<%=MiUtil.getString(objCustomerBean.getSwPhone3())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone3New">
                <%= MiUtil.getString(objCustomerBean.getDepPhone3areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp; 
            <a href="javascript:fxCodeArea('txtCodeFaxNew','spanFaxNew','txtFaxNew')">Area Fax</a> 
          </td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <input type="text" name="txtCodeFaxNew" size="1" maxlength="2" value="<%=MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodeFaxNew','spanFaxNew','txtFaxNew')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtFaxNew" size="10" maxlength="7" value="<%=MiUtil.getString(objCustomerBean.getSwMainFax())%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanFaxNew">
                <%= MiUtil.getString(objCustomerBean.getDepMainFaxArea())%>
              </span>
            </small>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="30%">
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
            <input type="hidden" name="hdnCodePhone1" value="<%=MiUtil.getString(objCustomerBean.getSwMainPhoneArea())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainPhone())%>
            <input type="hidden" name="hdnPhone1" value="<%=MiUtil.getString(objCustomerBean.getSwMainPhone())%>"/>
            <small><span id="spanPhone1">
                <%= MiUtil.getString(objCustomerBean.getDepMainphonearea())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 2</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone2areacode())%>
            <input type="hidden" name="hdnCodePhone2" value="<%=MiUtil.getString(objCustomerBean.getNpPhone2areacode())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 2</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone2())%>
            <input type="hidden" name="hdnPhone2" value="<%=MiUtil.getString(objCustomerBean.getSwPhone2())%>"/>
            <small><span id="spanPhone2">
                <%= MiUtil.getString(objCustomerBean.getDepPhone2areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 3</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getNpPhone3areacode())%>
            <input type="hidden" name="hdnCodePhone3" value="<%=MiUtil.getString(objCustomerBean.getNpPhone3areacode())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 3</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwPhone3())%>
            <input type="hidden" name="hdnPhone3" value="<%=MiUtil.getString(objCustomerBean.getSwPhone3())%>"/>
            <small><span id="spanPhone3">
                <%= MiUtil.getString(objCustomerBean.getDepPhone3areacode())%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>
            <input type="hidden" name="hdnCodeFax" value="<%=MiUtil.getString(objCustomerBean.getSwMainFaxArea())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString(objCustomerBean.getSwMainFax())%>
            <input type="hidden" name="hdnFax" value="<%=MiUtil.getString(objCustomerBean.getSwMainFax())%>"/>
            <small><span id="spanFax">
                <%= MiUtil.getString(objCustomerBean.getDepMainFaxArea())%>
              </span>
            </small>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <%  }else if (lSiteId!=0){%>
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
          <td align="left" class="CellLabel" width="10%">Codigo</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("npcodbscs"))%>
          </td>
          <td align="left" class="CellLabel" width="10%">Nombre del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtSiteNameNew" size="40" maxlength="40" value="<%=MiUtil.getString((String)hshSite.get("swsitename"))%>" onchange="this.value=trim(this.value.toUpperCase())"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">
            <a href="javascript:fxCodeArea('txtCodePhone1New','spanPhone1New','txtPhone1New')">Area 1</a> 
          </td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <input type="text" name="txtCodePhone1New" size="1" maxlength="2" value="<%=strPhoneAreaSite%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodePhone1New','spanPhone1New','txtPhone1New')"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtPhone1New" size="10" maxlength="7" value="<%=MiUtil.getString((String)hshSite.get("swofficephone"))%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanPhone1New">
                <%= strPhoneDepSite%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">
            <a href="javascript:fxCodeArea('txtCodeFaxNew','spanFaxNew','txtFaxNew')">Area Fax</a> 
          </td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <input type="text" name="txtCodeFaxNew" size="1" maxlength="2" value="<%=strFaxAreaSite%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtCodeFaxNew','spanFaxNew','txtFaxNew')"/>
          </td>
          <td align="left" class="CellLabel" width="20%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <input type="text" name="txtFaxNew" size="10" maxlength="7" value="<%=MiUtil.getString((String)hshSite.get("swfax"))%>" onchange="this.value=trim(this.value)"/>
            <small><span id="spanFaxNew"/></small>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="15%">
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
          <td align="left" class="CellLabel" width="10%">Codigo del Site</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("npcodbscs"))%>
          </td>
          <td align="left" class="CellLabel" width="10%">Nombre del Site</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("swsitename"))%>
            <input type="hidden" name="hdnSiteName" value="<%=MiUtil.getString((String)hshSite.get("swsitename"))%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area 1</td>
          <td align="left" class="CellContent" width="7%">&nbsp;
            <%= strPhoneAreaSite%>
            <input type="hidden" name="hdnCodePhone1" value="<%=strPhoneAreaSite%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Telefono 1</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("swofficephone"))%>
            <input type="hidden" name="hdnPhone1" value="<%=MiUtil.getString((String)hshSite.get("swofficephone"))%>"/>
            <small><span id="spanPhone1Site">
                <%= strPhoneDepSite%>
              </span>
            </small>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">&nbsp;Area Fax</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= strFaxAreaSite%>
            <input type="hidden" name="hdnCodeFax" value="<%=strFaxAreaSite%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">&nbsp;Fax</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= MiUtil.getString((String)hshSite.get("swfax"))%>
            <input type="hidden" name="hdnFax" value="<%=MiUtil.getString((String)hshSite.get("swfax"))%>"/>
            <small><span id="spanFaxSite">
                <%= strFaxDepSite%>
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
          <td align="left" class="CellLabel" width="15%">Nombre del billing account</td>
          <td align="left" class="CellContent" width="35%">
            <input type="text" name="txtBillAccNameNew" size="50" maxlength="30" onchange="this.value=trim(this.value.toUpperCase())  ;  return fxOnValidateChangeCustomerBillingAccount(); "  <%if (arrBillAcc.size()==0){%> readonly <%}%>/>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="40%">
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
      <table id="tblBillAcc" border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        <tr>
          <td align="left" class="CellLabel" width="15%">Nombre del billing account</td>
          <td align="left" class="CellContent" width="35%">
            <select name="cmbBillingAcc" style="width: 70%;" onchange="javascript:document.frmdatos.hndBillAccName.value=this[this.selectedIndex].text ;  return fxOnValidateChangeCustomerBillingAccount();  ">
              <option value="0"/>
              <%  BillingAccountBean objBillingAccBean=null;
                  for (int k=0;k<arrBillAcc.size();k++){ 
                     objBillingAccBean= (BillingAccountBean)arrBillAcc.get(k);
               %>
              <option value="<%=MiUtil.getString(objBillingAccBean.getNpBillaccountNewId())%>">
                <%= MiUtil.getString(objBillingAccBean.getNpBillacCName2())%>
              </option>
              <%  }%>
            </select>
            <input type="hidden" name="hndBillAccName"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <!--Billing Account-->
  <tr>
    <td width="50%" valign="top">
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
          <td align="left" class="CellLabel" width="15%">Regi&oacute;n</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbRegionNew">
              <%= MiUtil.buildComboSelected(arrRegion,"swregionid","swname",MiUtil.getString(objAddressBean.getSwregionid()))%>
            </select>
          </td>
          <td align="left" class="CellLabel" width="20%">T&iacute;tulo</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbTitleNew" style="width: 20%;" onchange="javascript:document.frmdatos.hndTitleNew.value=this[this.selectedIndex].text">
              <%  hshData = objGeneralService.getTableList("PERSON_TITLE","1"); 
                  strMessage=(String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage);                               
                  arrLista=(ArrayList)hshData.get("arrTableList");
                  //Seteamos correctamente los valores
                  strTitleId=MiUtil.getValueId(arrLista,"wv_npValue","wv_npValueDesc",strTitle);
                  System.out.println("strTitleId:"+strTitleId);
                  System.out.println("strTitle:"+strTitle);
               %>
              <%= MiUtil.buildComboSelected(arrLista,"wv_npValueDesc","wv_npValueDesc",strTitle)%>
            </select>
            <input type="hidden" name="hndTitleNew"/>
          </td>
        </tr>
        <tr >
          <td align="left" class="CellLabel" width="15%" rowspan="3" valign="top">Direcci&oacute;n </td>
          <td align="left" class="CellContent" width="10%">
            <input type="text" name="txtAddress1New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="100" value="<%=strDireccion1%>"/>
          </td>
          <td align="left" class="CellLabel" width="20%">Nombres</td>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtFirstNameNew" size="40" maxlength="40" onchange="this.value=trim(this.value.toUpperCase())" value="<%=strNombres%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="10%">
            <input type="text" name="txtAddress2New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="50" value="<%=strDireccion2%>"/>
          </td>
          <td align="left" class="CellLabel" width="20%">Apellido Paterno</td>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtLastNameNew" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())" value="<%=strLastName%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="10%">
            <input type="text" name="txtAddress3New" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="50" value="<%=strDireccion3%>"/>
          </td>
          <td align="left" class="CellLabel" width="20%">Apellido Materno</td>
          <td align="left" class="CellContent" width="20%">
            <input type="text" name="txtMiddleNameNew" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())" value="<%=strMiddleName%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="15%">Departamento</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbDpto" onChange="fxLoadPlace(1);DeleteSelectOptions(document.frmdatos.cmbDist);DeleteSelectOptions(document.frmdatos.cmbProv);javascript:document.frmdatos.hdnDptoNew.value=this[this.selectedIndex].text">
              <%  hshData = objGeneralService.getUbigeoList(0,0,"0"); 
                     strMessage=(String)hshData.get("strMessage");
                     if (strMessage!=null)
                        throw new Exception(strMessage);                                  
                     arrLista=(ArrayList)hshData.get("arrUbigeoList");                     
                  %>      
              <%= MiUtil.buildComboSelected(arrLista,"ubigeo","nombre",strDepartamentoId)%>
            </select>
            <input type="hidden" name="hdnDptoNew" value="<%=strDepartamento%>"/>
          </td>
          <td align="left" class="CellLabel" width="20%">Cargo</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbJobTitleNew" style="width: 90%;">
              <%  hshData = objGeneralService.getTitleList(); 
                  strMessage= (String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage);                              
                  arrLista=(ArrayList)hshData.get("arrTitleList");
                   //Seteamos correctamente los valores 
                   if(strJobTitleId != null){
                    strJobTitle = MiUtil.getDescripcion(arrLista,"jobtitlteId","descripcion",strJobTitleId);
                   }                  
                  System.out.println("strJobTitleId:"+strJobTitleId);
                  System.out.println("strJobTitle:"+strJobTitle);                           
               %>
              <%= MiUtil.buildComboSelected(arrLista,"jobtitlteId","descripcion",strJobTitleId)%>
            </select>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Provincia</td>
          <td align="left" class="CellContent" width="20%">
            <select name="cmbProv" onChange="fxLoadPlace(2);DeleteSelectOptions(document.frmdatos.cmbDist);javascript:document.frmdatos.hdnProvNew.value=this[this.selectedIndex].text" onclick="fxFirstLoadPlace(1);">
              <%= MiUtil.buildComboWithOneOption(strProvinciaId,strProvincia)%>
            </select>
             <input type="hidden" name="hdnProvNew" value="<%=strProvincia%>"/>
          </td>
          <td align="left" class="CellLabel" width="8%">E-Mail</td>
          <td align="left" class="CellContent" width="14%">
            <input type="text" name="txtEmailNew" size="40" maxlength="80" onchange="this.value=trim(this.value.toLowerCase())" value="<%=strEmail%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Distrito</td>
          <td align="left" class="CellContent" colspan="3">
          <select name="cmbDist" onChange="fxLoadCodPostal(this.selectedIndex);javascript:document.frmdatos.hdnDistNew.value=this[this.selectedIndex].text" onclick="fxFirstLoadPlace(2);">
              <%= MiUtil.buildComboWithOneOption(strDistritoId,strDistrito)%>
            </select>
            <input type="hidden" name="hdnDistNew"  value="<%=strDistrito%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="8%">Cod. Postal</td>
          <td align="left" class="CellContent" colspan="3">
            <input type="text" name="txtZipNew" value="<%=strCodPostal%>" readOnly/>
          </td>
        </tr>
        
        <!-- SAR 0037-167824 INI -->
       
        <tr>
          <td align="left" class="CellLabel" width="10%">Referencia</td>
          <td align="left" class="CellContent" colspan="3" width="20%">
           <input type="text" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" name="txtRefNew" value="<%=strReferencia%>"/>
          </td>
        </tr>

     
        <!-- SAR 0037-167824 FIN -->
        
        
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

      <table border="0" id="tblContDireFac" style="display:'none'" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
        
  
        <tr>
          <td align="left" class="CellLabel" width="10%">Regi&oacute;n</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strRegion%>
            <input type="hidden" name="hdnAddRegionId" value="<%=MiUtil.getString(objAddressBean.getSwregionid())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">T&iacute;tulo</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strTitle%>
            <input type="hidden" name="hdnTitle" value="<%=strTitle%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%" rowspan="3" valign="top">Direcci&oacute;n </td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= strDireccion1%>
            <input type="hidden" name="hdnAddress1" value="<%=strDireccion1%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">Nombres</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strNombres%>
            <input type="hidden" name="hdnFirstName" value="<%=strNombres%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= strDireccion2%>
            <input type="hidden" name="hdnAddress2" value="<%=strDireccion2%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">Apellido Paterno</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strLastName%>
            <input type="hidden" name="hdnLastName" value="<%=strLastName%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= strDireccion3%>
            <input type="hidden" name="hdnAddress3" value="<%=strDireccion3%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%">Apellido Materno</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strMiddleName%>
            <input type="hidden" name="hdnMiddleName" value="<%=strMiddleName%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">Departamento</td>
          <td align="left" class="CellContent" width="10%">&nbsp;
            <%= strDepartamento%>
           <input type="hidden" name="hdnDpto" value="<%=strDepartamento%>"/>
          <input type="hidden" name="hdnDptoId" value="<%=MiUtil.getString(objAddressBean.getNpdepartamentoid())%>"/>
        </td>
          <td align="left" class="CellLabel" width="10%">Cargo</td>
          <td align="left" class="CellContent" width="20%">&nbsp;
            <%= strJobTitle%>
            <input type="hidden" name="hdnJobTitle" value="<%=strJobTitle%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%" height="43">Provincia</td>
          <td align="left" class="CellContent" width="10%" height="43">&nbsp;
            <%= strProvincia%>
            <input type="hidden" name="hdnProv" value="<%=strProvincia%>"/>
            <input type="hidden" name="hdnProvId" value="<%=MiUtil.getString(objAddressBean.getNpprovinciaid())%>"/>
          </td>
          <td align="left" class="CellLabel" width="10%" height="43">E-Mail</td>
          <td align="left" class="CellContent" width="20%" height="43">&nbsp;
            <%= strEmail%>
            <input type="hidden" name="hdnEmail" value="<%=strEmail%>"/>
          </td>
        </tr>
       <tr>
          <td align="left" class="CellLabel" width="10%">Distrito</td>
          <td align="left" class="CellContent" colspan="3" width="10%">&nbsp;
            <%= strDistrito%>
            <input type="hidden" name="hdnDist" value="<%=strDistrito%>"/>
            <input type="hidden" name="hdnDistId" value="<%=MiUtil.getString(objAddressBean.getNpdistritoid())%>"/>
          </td>
        </tr>
        <tr>
          <td align="left" class="CellLabel" width="10%">Cod. Postal</td>
          <td align="left" class="CellContent" colspan="3" width="10%">&nbsp;
           <%= strCodPostal%>
            <input type="hidden" name="hdnZip" value="<%=strCodPostal%>"/>
          </td>
        </tr>
        
        <!-- SAR 0037-167824 INI -->
        <tr>
          <td align="left" class="CellLabel" width="10%"> Referencia </td>
          <td align="left" class="CellContent" colspan="3" width="20%">&nbsp;
          <%= strReferencia%>
            <input type="hidden" name="hdnRef" value="<%=strReferencia%>"/>
          </td>
        </tr>
        
        <!-- SAR 0037-167824 FIN -->


      </table>
    </td>
  </tr>
  <!--Fin Billing Account-->
</table>
<script language="JavaScript" defer="defer">
 
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
         Form.txtZipNew.value="";
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
   
  function fxOnValidateChangeCustomerBillingAccount(){
      form = document.frmdatos;
      if (form.cmbBillingAcc.value == 0){
         if (form.txtSiteNameNew !="") {  
            ("Debe Seleccionar un nombre para el nuevo Billing Account");
             form.txtBillAccNameNew.value="";
             return false;
        }
        else{
             form.txtBillAccNameNew.value="";
             return false;
        }
      } 
      
       return true;
   }

   function fxOnValidateChangeCustomerInfo(){
      form = document.frmdatos;
      if (form.cmbBillingAcc.value == 0){
         if (form.txtSiteNameNew ==""){
            alert("Ingrese un nuevo nombre para el Billing Account");
            return false;
         }
      }
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
<script defer="defer">                
      vAreaCode.addElement(new fxAreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
     </script>
     <%   }

}catch(Exception ex){
  ex.printStackTrace();
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<% 
}%>
