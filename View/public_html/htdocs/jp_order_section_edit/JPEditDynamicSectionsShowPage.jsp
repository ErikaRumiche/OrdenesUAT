<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
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
    System.out.println("Sesión capturada  JPEditDynamicSectionsShowPage : " + objetoUsuario1.getName() + " - " + strSessionId );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JPEditDynamicSectionsShowPage Not Found");
    return;
  }
  
  
 // strSessionId="998102396";
   String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
  
   long lOrderId=Long.parseLong(strOrderId);
   
   HashMap hshData=null;
   String strMessage=null;
   ArrayList arrLista=null;
   NewOrderService objNewOrderService=new NewOrderService();
   EditOrderService objEditOrderService =new EditOrderService();
   CustomerService objCustomerService =new CustomerService();
   GeneralService objGeneralService= new GeneralService();
   OrderBean objOrderBean=new OrderBean();
   CustomerBean objCustomerBean=null;
   SectionDinamicBean objSectionDinamBean=new SectionDinamicBean();
   
   hshData=objEditOrderService.getOrder(lOrderId);
   strMessage=(String)hshData.get("strMessage");
   
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshData.get("objResume"); 
   
   long lSiteId=objOrderBean.getNpSiteId();
   long lCustomerId=objOrderBean.getCsbCustomer().getSwCustomerId();
   int  iSpecificationId=objOrderBean.getNpSpecificationId();
   
   
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
   
   
   hshData = objNewOrderService.CategoryDAOgetSpecificationData(iSpecificationId, null);    
   strMessage=(String)hshData.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);                  
            
   arrLista = (ArrayList)hshData.get("objArrayList");
   
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
   
   Hashtable hshtInputEditSection = new Hashtable();   
   hshtInputEditSection.put("strCustomerId",""+lCustomerId);
   hshtInputEditSection.put("strSiteId",""+lSiteId);
   hshtInputEditSection.put("strOrderId",""+strOrderId);
   //hshtInputEditSection.put("strSolutionId",""+objOrderBean.getNpSolutionId());
	 hshtInputEditSection.put("strDivisionId",""+objOrderBean.getNpDivisionId());
   hshtInputEditSection.put("strTypeCompany",""+MiUtil.getString(objOrderBean.getNpCompanyType()));   
   hshtInputEditSection.put("strStatus",""+MiUtil.getString(objOrderBean.getNpStatus()));
   hshtInputEditSection.put("strSpecificationId",""+objOrderBean.getNpSpecificationId());
   hshtInputEditSection.put("strCodBSCS",MiUtil.getString(objCustomerBean.getSwCodBscs()));
   hshtInputEditSection.put("strSessionId",MiUtil.getString(strSessionId)); 
   hshtInputEditSection.put("strSalesStuctOrigenId",""+iSalesStructOrigenId);
   
   //Cambios Lee Rosales
   hshtInputEditSection.put("strSiteOppId",strNewSiteId+""); 
   hshtInputEditSection.put("strGeneratorId",lngGeneratorId+"");
   hshtInputEditSection.put("strUnknwnSiteId",strUnknownSiteId+"");
   hshtInputEditSection.put("strGeneratorType",strGeneratorType+"");
 
   //Fin Cambios Lee Rosales
   
   hshtInputEditSection.put("strDocument",MiUtil.getString(objCustomerBean.getSwRuc()));  
   hshtInputEditSection.put("strTypeDocument",MiUtil.getString(objCustomerBean.getNpTipoDoc()));  
   hshtInputEditSection.put("strShowDataFields",strShowDataFields+"");  
   
   //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
   System.out.println("strScheduleDate -------->"+objOrderBean.getNpScheduleDate());
   if(objOrderBean.getNpScheduleDate()!=null){
    hshtInputEditSection.put("strScheduleDate",MiUtil.getDate(objOrderBean.getNpScheduleDate(),"dd/MM/yyyy"));
   }
   //<!-- jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
     
   //rmartinez - modif 15/11/2012 - inicio
   hshtInputEditSection.put("strFlagVep",""+objOrderBean.getNpFlagVep());
   hshtInputEditSection.put("strNumCuotas",""+objOrderBean.getNpNumCuotas());
   hshtInputEditSection.put("strAmountVep",""+objOrderBean.getNpAmountVep());
   //rmartinez - modif 15/11/2012 - fin

   //PRY-0890 JBALCAZAR
   if(objOrderBean.getNpProrrateo()!=null){
	   hshtInputEditSection.put("strProrrateo",MiUtil.getString(objOrderBean.getNpProrrateo()));
	   }

   if(objOrderBean.getNpOrderChildId()!=null){
	   hshtInputEditSection.put("strOrderchildid",MiUtil.getString(objOrderBean.getNpOrderChildId()));
	   }
   
   if(objOrderBean.getNpOrderParentId()!=null){
	   hshtInputEditSection.put("strOrderparentId",MiUtil.getString(objOrderBean.getNpOrderParentId()));
	   } 

   if(objOrderBean.getNpPaymentTotalProrrateo()!=null){
	   hshtInputEditSection.put("strPaymentTotalProrrateo",MiUtil.getString(objOrderBean.getNpPaymentTotalProrrateo()));
	   }
   

    //INICIO: PRY-0864 | AMENDEZ
    hshtInputEditSection.put("strInitialQuota",""+objOrderBean.getInitialQuota());
    //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    hshtInputEditSection.put("strNpPaymentTermsIQ",""+objOrderBean.getNpPaymentTermsIQ());
    //FIN: PRY-0980 | AMENDEZ

    //INICIO: PRY-1049 | LGONZALES
    hshtInputEditSection.put("strHdnCobertura",""+objOrderBean.getNpflagcoverage());
    //FIN: PRY-1049 | LGONZALES
   request.setAttribute("hshtInputEditSection",hshtInputEditSection);

   System.out.println("---------------------- INICIO JP_EDIT_DYNAMIC_SECTION------------------");
   System.out.println("strCustomerId-->"+lCustomerId);
   System.out.println("strSiteId-->"+lSiteId);
   System.out.println("strOrderId-->"+strOrderId);
   System.out.println("strSolutionId-->"+objOrderBean.getNpSolutionId());
	 System.out.println("strDivisionId-->"+objOrderBean.getNpDivisionId());
   System.out.println("strTypeCompany-->"+objOrderBean.getNpCompanyType());   
   System.out.println("strStatus-->"+objOrderBean.getNpStatus());
   System.out.println("strSpecificationId-->"+objOrderBean.getNpSpecificationId());
   System.out.println("strCodBSCS-->"+objCustomerBean.getSwCodBscs());
   System.out.println("strSessionId-->"+strSessionId);      
   
  System.out.println("getNpScheduleDate -------->"+hshtInputEditSection.get("getNpScheduleDate"));
  System.out.println("strProrrateo-->"+objOrderBean.getNpProrrateo());
  System.out.println("strOrderchildid-->"+objOrderBean.getNpOrderChildId());
  System.out.println("strOrderparentId-->"+objOrderBean.getNpOrderParentId());
  System.out.println("strPaymentTotalProrrateo-->"+objOrderBean.getNpPaymentTotalProrrateo());  

  //INICIO: PRY-1049 | LGONZALES
  System.out.println("strHdnCobertura-->"+ objOrderBean.getNpflagcoverage());
  //FIN: PRY-1049 | LGONZALES
   System.out.println("---------------------- FIN JP_EDIT_DYNAMIC_SECTION------------------");   
%>

<!--------------------- DynamicSectionShowPage.jsp 0------------------>

<script>

function fxRegisterOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject){
   VOrderSections.addElement(new fxMakeOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject));
}    
     
</script>    

<div id="IdSpecificationSections">
<!--------------------- DynamicSectionShowPage.jsp 1------------------>
<table>
   <tr>
      <td>
      <input type="hidden" name="hdnVectorSection">
      </td>    
   </tr>
</table>

<% 
for(int i=0;i< arrLista.size();i++ ){
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
            
   //'" + objSectionDinamBean.getNppagesectionid() + "','" + "SPECIFICATION" + "','" + objSectionDinamBean.getNpSpecificationId() + "','" + objSectionDinamBean.getNpeventname() + "','" + objSectionDinamBean.getNpeventhandler() + "', '' , '" + objSectionDinamBean.getNptypeobject() + "','" + objSectionDinamBean.getNpobjectname() + "','" + objSectionDinamBean.getNpbusinessobject() + "');";                           
   
   if (Constante.EDIT_ON_DISPLAY.equals(MiUtil.getString(objSectionDinamBean.getNpeventname()))){         
      if (strObjectName.endsWith(".jsp")){
         strObjectName=strPath+strObjectName;//+strParametros;
      %>
         <jsp:include page='<%= strObjectName %>'/>          
      <%}
   }else if (Constante.ON_LOAD.equals(MiUtil.getString(objSectionDinamBean.getNpeventname()))){         
      strEventOnLoad = strEventOnLoad +" "+ MiUtil.getString(objSectionDinamBean.getNpeventhandler())+"; ";
   }
}%>
<!--------------------- DynamicSectionShowPage.jsp 3------------------>
</div>
<script >
   function fxOnLoadSections(){           
      <%=strFxROrderSection%>
      <%=strEventOnLoad%>  
      return;
   }
   
   //onload=fxOnLoadSections;   
</script>
<%      
}catch(Exception  ex){   
   ex.printStackTrace();
   System.out.println("Administrador Seccion Dinamica JPEditDynamicSectionsShowPage / Message-->"+ex.getMessage());    
   System.out.println("Administrador Seccion Dinamica JPEditDynamicSectionsShowPage / Cause-->"+ex.getCause());
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>
<!---------------------END DynamicSectionShowPage.jsp ------------------>