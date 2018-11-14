<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>

<%
try{
   String strSessionId="";
   
   
   try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JPDetailDynamicSectionsShowPage : " + objetoUsuario1.getName() + " - " + strSessionId );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JPDetailDynamicSectionsShowPage Not Found");
    return;
  }
   
  // strSessionId="152799090529749711670278018086688074998102396";
  // strSessionId="1180094936398917317037012279192645982994001614";
   //Inicio Acuerdos Comerciales
   PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
   if(portalSessionBean==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionId);
		throw new SessionException("La sesión finalizó");
	 }
   //Fin Acuerdos Comerciales
   
   String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
   long lOrderId=Long.parseLong(strOrderId);
   
   HashMap hshOrder=null;
   String strMessage=null;  
   ArrayList arrLista=null;
   NewOrderService objNewOrderService=new NewOrderService();
   EditOrderService objEditOrderService =new EditOrderService();
   GeneralService objGeneralService= new GeneralService();
   OrderBean objOrderBean=new OrderBean();
   SectionDinamicBean objSectionDinamBean=new SectionDinamicBean();
   Hashtable hshtInputNewSection = new Hashtable();
   
   hshOrder=objEditOrderService.getOrder(lOrderId);
   strMessage=(String)hshOrder.get("strMessage");
   
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshOrder.get("objResume");     
   
   long lSiteId=objOrderBean.getNpSiteId();
   long lCustomerId=objOrderBean.getCsbCustomer().getSwCustomerId();
   int iSpecificationId=objOrderBean.getNpSpecificationId() ;
   
   
   hshOrder = objNewOrderService.CategoryDAOgetSpecificationData(iSpecificationId, null);    
   strMessage=(String)hshOrder.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);                  
      
   //Cambios Lee Rosales
   String strGeneratorType = objOrderBean.getNpGeneratorType();
   long   lngGeneratorId   = objOrderBean.getNpGeneratorId();
   
   String strNewSiteId     = "";
   String strUnknownSiteId = "";
   
   strUnknownSiteId = ""+objNewOrderService.getUnkownSiteIdByOportunity(lngGeneratorId);
   
   HashMap objHasCustSite = objNewOrderService.getCustSiteIdByOportunity(lngGeneratorId);
   strMessage=(String)objHasCustSite.get("strMessage");
   
   if (strMessage!=null)
     strNewSiteId = null;
   else
     strNewSiteId = (String)objHasCustSite.get("newSiteId");
   
   //Fin Cambios Lee Rosales
           
   arrLista = (ArrayList)hshOrder.get("objArrayList");
   
   //Para obtener datos del cliente
   //------------------------------
   HashMap hshData=null;
   CustomerBean objCustomerBean=null;
   CustomerService objCustomerService =new CustomerService();
   hshData=objCustomerService.getCustomerData(lCustomerId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
    throw new Exception(strMessage);       
   objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");  
   
   
  /*Cambios en Data
  -----------------*/ 
  HashMap hshDataValidate = new HashMap();
  hshDataValidate = objGeneralService.getSalesDataShow( iSpecificationId,strGeneratorType,lngGeneratorId, lCustomerId, lSiteId);  
  strMessage = (String)hshDataValidate.get("strMessage");
  if (strMessage!=null)
    throw new Exception(strMessage);
    
  String strShowDataFields = (String)hshDataValidate.get("strShowDataFields");
   
  //Validaciones del valor de la estructura de ventas del usuario generador de la Orden
  //-----------------------------------------------------------------------------------
  long iSalesStructId  = objOrderBean.getSalesStructureOriginalId();
  long iSalesStructOrigenId = 0;
  System.out.println("iSalesStructId***:"+iSalesStructId);
  
  if( iSalesStructId  ==0){
     HashMap hshResultStruct = new HashMap();
     hshResultStruct = objGeneralService.getSalesStructOrigen(iSalesStructId,Constante.SALES_STRUCT_PORTAL_DEFAULT) ;
     strMessage = (String)hshResultStruct.get("strMessage");
     if ( strMessage != null )
        throw new Exception(strMessage);
     iSalesStructOrigenId = MiUtil.parseLong((String)hshResultStruct.get("iSalesStructOrigen"));
  }else{
     iSalesStructOrigenId = iSalesStructId;
  }

  System.out.println("<<<<<<Datos de SalesStructId>>>>>>");
  System.out.println("iSalesStructId:"+iSalesStructId);
  System.out.println("iSalesStructOrigenId:"+iSalesStructOrigenId);
   
   String strObjectName="";
   String strFxROrderSection="";
   String strEventOnLoad="";      
   String strPath="../../DYNAMICSECTION/";
     
   hshtInputNewSection.put("strCustomerId",""+lCustomerId);
   hshtInputNewSection.put("strSiteId",""+lSiteId);   
   hshtInputNewSection.put("strOrderId",""+strOrderId);
   //hshtInputNewSection.put("strSolutionId",""+objOrderBean.getNpSolutionId());
   hshtInputNewSection.put("strDivisionId",""+objOrderBean.getNpDivisionId());
   hshtInputNewSection.put("strCustomerType",""+objOrderBean.getNpCompanyType());
   hshtInputNewSection.put("strTypeCompany",""+MiUtil.getString(objOrderBean.getNpCompanyType()));
   hshtInputNewSection.put("strStatus",""+objOrderBean.getNpStatus());
   hshtInputNewSection.put("strSpecificationId",""+objOrderBean.getNpSpecificationId());   
   hshtInputNewSection.put("strSessionId",strSessionId);   
   hshtInputNewSection.put("strSalesStuctOrigenId",""+iSalesStructOrigenId);
   
   //Inicio Acuerdos Comerciales
   hshtInputNewSection.put("strEstadoOrden",objOrderBean.getNpStatus());
   hshtInputNewSection.put("strLogin",portalSessionBean.getLogin());
   hshtInputNewSection.put("strCreatedBy",objOrderBean.getNpCreatedBy());
   //Fin Acuerdos Comerciales
   
   //Cambios Lee Rosales
   hshtInputNewSection.put("strSiteOppId",strNewSiteId+""); 
   hshtInputNewSection.put("strGeneratorId",lngGeneratorId+"");
   hshtInputNewSection.put("strUnknwnSiteId",strUnknownSiteId+"");
   hshtInputNewSection.put("strGeneratorType",strGeneratorType+"");
   //Fin Cambios Lee Rosales
   
   hshtInputNewSection.put("strDocument",MiUtil.getString(objCustomerBean.getSwRuc()));  
   hshtInputNewSection.put("strTypeDocument",MiUtil.getString(objCustomerBean.getNpTipoDoc()));  
   hshtInputNewSection.put("strShowDataFields",strShowDataFields+"");
   
     
   //rmartinez - modif 15/11/2012 - inicio
   hshtInputNewSection.put("strFlagVep",""+objOrderBean.getNpFlagVep());
   hshtInputNewSection.put("strNumCuotas",""+objOrderBean.getNpNumCuotas());
   hshtInputNewSection.put("strAmountVep",""+objOrderBean.getNpAmountVep());
   //rmartinez - modif 15/11/2012 - fin

   //PRY-0890 JBALCAZAR
   if(objOrderBean.getNpProrrateo()!=null){
	   hshtInputNewSection.put("strProrrateo",MiUtil.getString(objOrderBean.getNpProrrateo()));
	   }

   if(objOrderBean.getNpOrderChildId()!=null){
	   hshtInputNewSection.put("strOrderchildid",MiUtil.getString(objOrderBean.getNpOrderChildId()));
	   }
   
   if(objOrderBean.getNpOrderParentId()!=null){
	   hshtInputNewSection.put("strOrderparentId",MiUtil.getString(objOrderBean.getNpOrderParentId()));
	   } 

   if(objOrderBean.getNpPaymentTotalProrrateo()!=null){
	   hshtInputNewSection.put("strPaymentTotalProrrateo",MiUtil.getString(objOrderBean.getNpPaymentTotalProrrateo()));
	   }
    
//INICIO: PRY-0864 | AMENDEZ
    hshtInputNewSection.put("strInitialQuota",""+objOrderBean.getInitialQuota());
    //FIN: PRY-0864 | AMENDEZ
     
    //INICIO: PRY-0980 | AMENDEZ
    hshtInputNewSection.put("strNpPaymentTermsIQ",""+objOrderBean.getNpPaymentTermsIQ());
    //FIN: PRY-0980 | AMENDEZ
     
    //INICIO: PRY-1049 | LGONZALES
    hshtInputNewSection.put("strHdnCobertura",""+objOrderBean.getNpflagcoverage());
    //FIN: PRY-1049 | LGONZALES
     
   request.setAttribute("hshtInputDetailSection",hshtInputNewSection);    
   
   System.out.println(" ----------- INICIO DetailDynamicSectionShowPage.jsp---------------- ");
   System.out.println("Detail Dynamic Section ShowPage");   
   System.out.println("Detail strProrrateo-->"+objOrderBean.getNpProrrateo());
   System.out.println("Detail strOrderchildid-->"+objOrderBean.getNpOrderChildId());
   System.out.println("Detail strOrderparentId-->"+objOrderBean.getNpOrderParentId());
   System.out.println("Detail strPaymentTotalProrrateo-->"+objOrderBean.getNpPaymentTotalProrrateo());
   System.out.println("Detail strGeneratorId-->"+lngGeneratorId);
   System.out.println("Detail strGeneratorType-->"+strGeneratorType);
   System.out.println("Detail strTypeDocument-->"+objCustomerBean.getNpTipoDoc());
   //INICIO: PRY-1049 | LGONZALES
   System.out.println("Detail strHdnCobertura-->"+ objOrderBean.getNpflagcoverage());
   //FIN: PRY-1049 | LGONZALES
   
   System.out.println(" ------------  FIN DetailDynamicSectionShowPage.jsp----------------- ");    
%>
<script>
     var VOrderSections = null;
         VOrderSections = new Vector(); 
     
     function fxRegisterOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject){
            VOrderSections.addElement(new fxMakeOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject));
     }
     
     
</script>    

<div id="IdSpecificationSections">

<table>
    <tr>
    <td>
    <input type="hidden" name="hdnVectorSection"></td>    
    </tr>
</table>

<% for(int i=0;i< arrLista.size();i++ ){
      objSectionDinamBean = (SectionDinamicBean)arrLista.get(i);
      strObjectName=MiUtil.getString(objSectionDinamBean.getNpeventhandler());        
      strFxROrderSection = strFxROrderSection + " fxRegisterOrderSection( '"+ 
                           MiUtil.getString(objSectionDinamBean.getNppagesectionid()) + "','"+ 
                           Constante.SPECIFICATION + "','"+ 
                           iSpecificationId + "','" + 
                           MiUtil.getString(objSectionDinamBean.getNpeventname()) + "','"+ 
                           MiUtil.getString(objSectionDinamBean.getNpeventhandler())+"','"+ 
                           Constante.MODIFY +"','"+
                           MiUtil.getString(objSectionDinamBean.getNptypeobject())+ "','"+ 
                           MiUtil.getString(objSectionDinamBean.getNpobjectname())+"','"+
                           MiUtil.getString(objSectionDinamBean.getNpbusinessobject())+"'); ";
                           
      if (Constante.DETAIL_ON_DISPLAY.equals(MiUtil.getString(objSectionDinamBean.getNpeventname()))){         
         if (strObjectName.endsWith(".jsp")){
             strObjectName=strPath+strObjectName;           
             System.out.println("JPDetailDynamicSectionsShowPage: strObjectName :" + strObjectName);
%>         
          <jsp:include page='<%= strObjectName %>'/>          
       
<%    }}else if (Constante.ON_LOAD.equals(MiUtil.getString(objSectionDinamBean.getNpeventname()))){         
         strEventOnLoad = strEventOnLoad +" "+ MiUtil.getString(objSectionDinamBean.getNpeventhandler())+"; ";
      }
    }
%>
</div>
<script>
   function fxOnLoadSections(){           
       <%=strFxROrderSection%>
       <%=strEventOnLoad%>   
   }
   
   onload=fxOnLoadSections;
</script>
<%      
}catch(Exception  ex){   
   ex.printStackTrace();
   System.out.println("Administrador Seccion Dinamica JPDetailDynamicSectionsShowPage / Message-->"+ex.getMessage());    
   System.out.println("Administrador Seccion Dinamica JPDetailDynamicSectionsShowPage / Cause-->"+ex.getCause());
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>