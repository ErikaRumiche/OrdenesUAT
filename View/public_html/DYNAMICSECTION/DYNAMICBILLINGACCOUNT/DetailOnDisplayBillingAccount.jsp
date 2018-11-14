<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<%  
try{
   //PARAMETROS
   //long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   //long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   //long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
     
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputDetailSection");  
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   String strSpecificationId=MiUtil.getString((String)hshParam.get("strSpecificationId"));  // INC 536
   
   String strMessage=null;
   ArrayList arrLista=null;
   BillingAccountBean objBillingABean=null;
   BillingContactBean objBillContBean=null;
   BillingAccountService objBillAccService=new BillingAccountService();
   EditOrderService objOrderService=new EditOrderService();
   GeneralService objGeneralService=new GeneralService(); 
   OrderBean objOrderBean     =null;
   String strCustomerType=null;
   int iUserId=0;
   int iAppId=0;
   String strGeneratorType    = null; 
   long lngProviderGrpId    = 0;
   long lGenerId = 0;
   long lGeneratorId=0;
   HashMap hshData=null;
   HashMap hshDataOrder=null;
   String strFlagAddRP = "0";
   int  iPermission = 0;
   
   System.out.println("[DetailOnDisplayBillingAccount]Sesión a consultar : " + strSessionId);
   PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[DetailOnDisplayBillingAccount]Sesión a consultar : " + objSessionBean);
   if( objSessionBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   
   iUserId=objSessionBean.getUserid();   
   iAppId=objSessionBean.getAppId();
   
   
   
   // Obtiene GeneratorType, GeneratorId y ProviderGroup de la Orden
   System.out.println("lOrderId: "+lOrderId);
   hshDataOrder = objOrderService.getOrder(lOrderId);
   strMessage=(String)hshDataOrder.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);
   objOrderBean=(OrderBean)hshDataOrder.get("objResume"); 
   
   lngProviderGrpId = objOrderBean.getNpProviderGrpId();
   strGeneratorType  = objOrderBean.getNpGeneratorType();
   lGenerId = objOrderBean.getNpGeneratorId();
   
   //strGeneratorType = "OPP";
   System.out.println("[DetailOnDisplayBillingAccount][lngProviderGrpId]: "+lngProviderGrpId);
   System.out.println("[DetailOnDisplayBillingAccount][lGenerId]: "+lGenerId);
   System.out.println("[DetailOnDisplayBillingAccount][strGeneratorType]: "+strGeneratorType);
   
   
   //Valida si la Categoría es de tipo GROSS
   HashMap hshDataGross = new HashMap();
	 hshDataGross = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_GROSS);
   String strResultGross = (String) hshDataGross.get("strDescription"); 
   System.out.println("[DetailOnDisplayBillingAccount][strResultGross]"+strResultGross);
   
   
   // Valida si la Categoría es un Serv. Adic (2016)
   HashMap hshDataServAdic = new HashMap();
	 hshDataServAdic = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_SERV_ADIC);
   String strResultServAdic = (String) hshDataServAdic.get("strDescription");
   System.out.println("[DetailOnDisplayBillingAccount][strResultServAdic]"+strResultServAdic);
   System.out.println("strGeneratorType: "+strGeneratorType);

   if (strResultGross != null && strResultGross.equals("1")){
   System.out.println("[DetailOnDisplayBillingAccount][GROSS]");  
   
   //Si el origen viene desde OPP
   if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP) ){
      System.out.println("lGeneratorId: "+lGeneratorId);     
      hshData = (new GeneralService()).getNpopportunitytypeid(lGenerId);
      strMessage = (String)hshData.get("strMessage");
      if ( strMessage != null ) throw new Exception(strMessage);  
      int iResult = MiUtil.parseInt((String)hshData.get("iReturnValue"));  
      System.out.println("[DetailOnDisplayBillingAccount][iResult]"+iResult);    
      strFlagAddRP = "1";
      System.out.println("[DetailOnDisplayBillingAccount][OPP_TYPE_BA]");   
      int iSalesID = MiUtil.parseInt((String)hshData.get("iSalesId"));  
      System.out.println("[DetailOnDisplayBillingAccount][lCustomerId]"+lCustomerId);
      System.out.println("[DetailOnDisplayBillingAccount][iSalesID]"+iSalesID);
      System.out.println("[DetailOnDisplayBillingAccount][iResult]"+iResult);
      iPermission = (new GeneralService()).getValidateAuthorization(lCustomerId, iResult, iSalesID);
      System.out.println("[DetailOnDisplayBillingAccount][iPermission]: "+iPermission);      

   //Si el origen viene de un Incidente     
   } else{ // if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) )
       if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) ){//Si el origen es un Inciden   
            hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_BA, iUserId, iAppId);  
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage); 
            iPermission=MiUtil.parseInt((String)hshData.get("iRetorno"));     
        }
      }         
  }
  else{//Órdenes Serv. Adicionales: no se requiere validar autorizaciones o permisos a los roles de trabajo
      System.out.println("[DetailOnDisplayBillingAccount][ELSE]");  
      if(strResultServAdic != null && strResultServAdic.equals("1")){
         System.out.println("[DetailOnDisplayBillingAccount][SERV ADICIONAL]");  
         iPermission = 1;
      }
  }
  /*Se obvia la validación anterior. Los usuarios si podrán ver esta sección en modo consulta
    Consultar con JGalindo
  */
  iPermission = 1; 
  if (iPermission == 1){
      if (lSiteId!=0)
          strCustomerType="SITE";
      else
          strCustomerType="CUSTOMER";
   
   
      HashMap hshBillAcc=objBillAccService.getAccountList(strCustomerType,lSiteId,lOrderId); 
      strMessage=(String)hshBillAcc.get("strMessage");   
      if (strMessage!=null)
          throw new Exception(strMessage); 
   
      arrLista=(ArrayList)hshBillAcc.get("objCuentas");    

      String strRutaContext=request.getContextPath();   
      String strURLBillAccView = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountView.jsp";        
      String strURLGeneralPage=strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";
   
      System.out.println(" ----------- INICIO BillingAccoutnConsulta.jsp---------------- ");
      System.out.println("Tamaño del arreglo de billingAccount-->"+arrLista.size());
      System.out.println("lOrderId-->"+lOrderId);
      System.out.println("lCustomerId-->"+lCustomerId);
      System.out.println(" ------------  FIN BillingAccoutnConsulta.jsp----------------- ");    
    
%>
<script  language='javascript' defer="defer">
   var apNewBillAcc = new Array();    
   var indBillAcc=0;  
   /*function fxSectionNameOnload(){   
      alert("On Load Sec Din Consulta");
      return true;
   }*/
   
   //Seccion 1 EJEMPLO de implemntacion 
   function fxSectionBillAccountValidate(){          
      //alert("Validate Sec Din Consulta");   
      return true;
   }
   
   /*function fxSectionNameFinalStatus(){
      //alert("final status Sec Din Consulta");   
      return true;  
   }*/
   
   function fxShowDetailBA(baId,customerId,siteId,index){
      var v_parametros = "?sUrl=<%=strURLBillAccView%>"+"¿nNewBillAccId=" + baId +"|hdnSessionId=<%=strSessionId%>|nOrderId=<%=lOrderId%>|nCustomerId="+customerId+"|nSiteId="+siteId+"|nIndex="+index; //1:Agregar  2:Editar                              
      var v_Url=   "<%=strURLGeneralPage%>" +v_parametros;         
      window.open(v_Url, "Billing_Account","status=yes, location=0, width=450, height=450, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
   }
   
   
</script>

 <table border="0" cellspacing="0" cellpadding="0" width="40%">
     <tr class="PortletHeaderColor"> 
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Cuenta Facturaci&oacute;n</td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
     </tr>     
  </table>
 <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table id="tableBillAcc" name="tableBillAcc" border="0" width="100%" cellpadding="2" cellspacing="1">                     
              <tr>
                  <td class="CellLabel" align="center">#</td>
                  <td class="CellLabel" align="center">Cuenta Facturaci&oacute;n</td>                                                
              </tr>
              <% for (int i=0;i< arrLista.size();i++)
                 {   objBillingABean=(BillingAccountBean)arrLista.get(i);
                     objBillContBean=objBillingABean.getObjBillingContactB();
                     System.out.println( i+1 + " -> " + objBillingABean.getNpBillaccountNewId()+ " + " + objBillingABean.getNpBillacCName() );
                
              %>
              <tr>
                  <td class="CellContent" align="right"><%=i+1%></td>
                  <td class="CellContent" align="left">
                    <a href="javascript:fxShowDetailBA(<%=objBillingABean.getNpBillaccountNewId()%>,<%=lCustomerId%>,<%=lSiteId%>,<%=i+1%>);"><%=MiUtil.getString(objBillingABean.getNpBillacCName())%></a>
                  </td>   
              </tr>
              <script defer="defer">      
                 apNewBillAcc[indBillAcc]= 
                 new fxMakeBillAccNew(
                 "<%=MiUtil.getString(objBillingABean.getNpBillaccountNewId())%>",
                 "<%=MiUtil.getString(objBillingABean.getNpBillacCName())%>", 
                 "<%=MiUtil.getString(objBillContBean.getNpTitle())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpfname())%>",
                 "<%=MiUtil.getString(objBillContBean.getNplname())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpjobtitle())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpphonearea())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpphone())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpaddress1())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpaddress2())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpdepartment())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpstate())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpcity())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpzipcode())%>","Listado",
                 "<%=MiUtil.parseInt(objBillingABean.getNpBscsCustomerId())%>","<%=MiUtil.parseInt(objBillingABean.getNpBscsSeq())%>");     
                  indBillAcc=indBillAcc+1;
              </script>
              <% }%>
              
         </table>
       </td>
      </tr>
 </table>   
<%
   }else{
%>
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer="defer">  
  function fxSectionBillAccountValidate(){
     return true;
   }
</script>   
</table>
<%}
}
catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>