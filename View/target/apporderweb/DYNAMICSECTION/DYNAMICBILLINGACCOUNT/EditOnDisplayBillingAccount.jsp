<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>

<input type=hidden name="hdnNewBillAccId" value=""> 

<% 
try{
   //PARAMETROS
   //long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   //long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   //long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
  
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
    System.out.println("En EditOnDisplayBillingAccount Orden===="+lOrderId);   
   //inc 536
   String strCustomerId=(String)hshParam.get("strCustomerId");
   String strOrderId=(String)hshParam.get("strOrderId");
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   String strTypeCustomer=MiUtil.getString((String)hshParam.get("strTypeCompany"));
   String strSpecificationId=MiUtil.getString((String)hshParam.get("strSpecificationId"));  // INC 536
   
   System.out.println("[EditOnDisplayBillingAccount]Sesión a consultar : " + strSessionId);
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[EditOnDisplayBillingAccount]Sesión a consultar : " + objPortalSesBean);
   if( objPortalSesBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   String strLogin=objPortalSesBean.getLogin();
   int iUserId=0;
   int iAppId=0;
   iUserId=objPortalSesBean.getUserid();
   iAppId=objPortalSesBean.getAppId();
   System.out.println("iAppId:"+iAppId);
   
   String strMessage=null;
   ArrayList arrLista=null;
   BillingAccountBean objBillingABean=null;
   BillingContactBean objBillContBean=null;
   BillingAccountService objBAS=new BillingAccountService();
   EditOrderService objOrderService=new EditOrderService();
   EditOrderService objEditService=new EditOrderService();
   GeneralService objGeneralService=new GeneralService(); 
   OrderBean objOrderBean=null;
   String strCustomerType=null;
   HashMap hshScreenField=null;
   int iPagPermitida=0;
   long lGeneratorId=0;
   HashMap hshData=null;
   
   /*if (lSiteId!=0)
      if (Constante.TYPE_CRM_CUSTOMER.equals(strTypeCustomer))  // si es prospecto
         strCustomerType=Constante.CUSTOMERTYPE_SITE;
      else
         strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER;
   else
      strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER;*/
   //Portlet Cuenta Facturación: lSiteId es el valor seleccionado en el combo Responsable de Pago 
   
   
   // Obtiene GeneratorType, GeneratorId y ProviderGroup de la Orden
   System.out.println("[EditOnDisplayBilliangAccount][lOrderId]"+lOrderId);
   HashMap hshDataOrder = objEditService.getOrder(lOrderId);
   strMessage=(String)hshDataOrder.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);
   objOrderBean=(OrderBean)hshDataOrder.get("objResume"); 
   String strGeneratorType    = null; 
   long lngProviderGrpId    = 0;
   long lGenerId = 0;
   String strFlagAddRP = "0";
   int  iPermission = 0;
   
      
   lngProviderGrpId = objOrderBean.getNpProviderGrpId();
   strGeneratorType  = objOrderBean.getNpGeneratorType();
   lGenerId = objOrderBean.getNpGeneratorId();
   
   //Valida si la Categoría es de tipo GROSS
   HashMap hshDataGross = new HashMap();
	 hshDataGross = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_GROSS);
   String strResultGross = (String) hshDataGross.get("strDescription"); 
   System.out.println("[EditOnDisplayBillingAccount][strResultGross]"+strResultGross);
   
   
   // Valida si la Categoría es un Serv. Adic (2016)
   HashMap hshDataServAdic = new HashMap();
	 hshDataServAdic = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_SERV_ADIC);
   String strResultServAdic = (String) hshDataServAdic.get("strDescription");
   System.out.println("[EditOnDisplayBillingAccount][strResultServAdic]"+strResultServAdic);
   System.out.println("strGeneratorType: "+strGeneratorType);
   
   
  if (strResultGross != null && strResultGross.equals("1")){
  
   System.out.println("[EditOnDisplayBillingAccount][GROSS]");   
  //Si el origen viene desde OPP
   if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP) ){
        
      
      System.out.println("lGeneratorId: "+lGeneratorId);     
      hshData = (new GeneralService()).getNpopportunitytypeid(lGenerId);
      strMessage = (String)hshData.get("strMessage");
      if ( strMessage != null ) throw new Exception(strMessage);  
      int iResult = MiUtil.parseInt((String)hshData.get("iReturnValue"));  
      System.out.println("[EditOnDisplayBillingAccount][iResult]"+iResult);      
      int iSalesID = MiUtil.parseInt((String)hshData.get("iSalesId"));  
      System.out.println("[EditOnDisplayBilliangAccount][lCustomerId]"+lCustomerId);
      System.out.println("[EditOnDisplayBilliangAccount][iSalesID]"+iSalesID);
      System.out.println("[EditOnDisplayBilliangAccount][iResult]"+iResult);
      iPermission = (new GeneralService()).getValidateAuthorization(lCustomerId, iResult, iSalesID);
      System.out.println("[EditOnDisplayBilliangAccount][iPermission]: "+iPermission);      
  
  //Si el origen viene de un Incidente     
   } else{ // if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) )  
        if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) ){//Si el origen es un Inciden   
          
            hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_BA, iUserId, iAppId);
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);          
            iPermission=MiUtil.parseInt((String)hshData.get("iRetorno"));   
            System.out.println("[EditOnDisplayBilliangAccount][iPermission]: "+iPermission);     
       }
    } 
   
   }
  else{//Órdenes Serv. Adicionales: no se requiere validar autorizaciones o permisos a los roles de trabajo
      System.out.println("[EditOnDisplayBilliangAccount][ELSE]");  
      if(strResultServAdic != null && strResultServAdic.equals("1")){
         System.out.println("[EditOnDisplayBilliangAccount][SERV ADICIONAL]");  
         iPermission = 1;
      }
   }
  System.out.println("[EditOnDisplayBilliangAccount][iPermission]: "+iPermission);      
  
  if (iPermission == 1){ 
      if (lSiteId!=0){
          strCustomerType=Constante.CUSTOMERTYPE_SITE;
      }		 
      else{
          strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER;
      }		  
      hshData=objBAS.getAccountList(strCustomerType,lSiteId,lOrderId); 
      strMessage=(String)hshData.get("strMessage");   
      if (strMessage!=null)
        throw new Exception(strMessage); 
      
      arrLista=(ArrayList)hshData.get("objCuentas");       

      //Manejo dinamico de controles   
      hshData=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_EDIT);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
        throw new Exception(strMessage);  
   
      hshScreenField= (HashMap)hshData.get("hshData"); 
   
      //Rutas
      String strRutaContext=request.getContextPath();   
      String strURLBillAccEdit = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountEdit.jsp"; 
      String strURLBillAccDetail = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountView.jsp";
      String strURLBillAccNew = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountNew.jsp"; 
      String strURLOrderServlet =strRutaContext+"/editordersevlet";
      String strURLGeneralPage=strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";
      System.out.println(" ----------- INICIO DynamicBillingAccount-EditOnDisplayBillingAccount.jsp ---------------- ");
      System.out.println("strCustomerType-->"+strCustomerType);
      System.out.println("nCustomerId-->"+lCustomerId);
      System.out.println("nOrderId-->"+lOrderId);
      System.out.println("nSiteId-->"+lSiteId);   
      System.out.println("Tamaño del arreglo de billingAccount List-->"+arrLista.size());
      System.out.println(" ------------  FIN DynamicBillingAccount-EditOnDisplayBillingAccount.jsp----------------- ");    
    
%>
<script  language='javascript' defer="defer">
   var apNewBillAcc = new Array();    
   var indBillAcc=0;      
    
   /*function fxSectionNameOnload(){   
      //alert("seccion dinamica");
   }*/
   
   function verVector()
   {
      for(var i=0;i<apNewBillAcc.length;i++){                        
         var variable=  "id->"+apNewBillAcc[i].billAccId +" name-->"+apNewBillAcc[i].billAccName + " "+
         " title->"+apNewBillAcc[i].titleName +" FName->"+apNewBillAcc[i].contfName +" "+ 
         " lName->"+apNewBillAcc[i].contlName +" Cargo->"+apNewBillAcc[i].cargo +" "+        
         " pArea->"+apNewBillAcc[i].phoneArea +" pNumber->"+apNewBillAcc[i].phoneNumber +" "+
         " ad1->"+apNewBillAcc[i].address1 +" ad2->"+apNewBillAcc[i].address2 +" "+ 
         " dep->"+apNewBillAcc[i].depart +" prov->"+apNewBillAcc[i].prov +" "+
         " dist->"+apNewBillAcc[i].dist +" zipCode->"+apNewBillAcc[i].zipCode +" "+
         " state->"+apNewBillAcc[i].state + 
         " bscsCustId->"+apNewBillAcc[i].bscsCustId +" bscsSeq->"+apNewBillAcc[i].bscsSeq;
         alert(variable);    
      }    
      //return true;
   };   

   function fxSectionBillAccountValidate(){           
   // Pasando los valores del vectos a hiddens       
      var Form = document.frmdatos;
      var codigoBSCS,varbscsSeq;     
      var vardist,varprov;
      fxDeleteRowsOfTable('hdnBillAcc');
      var table = document.all?document.all["hdnBillAcc"]:document.getElementById("hdnBillAcc");
      var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.         
      
      for(var i=0;i<apNewBillAcc.length;i++){       
         if ((apNewBillAcc[i].billAccId==-1 && apNewBillAcc[i].state=='Eliminado') || 
             (apNewBillAcc[i].state=='Listado')){                    
         }else{    
             row = tbody.insertRow(-1);                                   
                  
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndBillAccId' value='" + apNewBillAcc[i].billAccId + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndBillAccName' value='" + apNewBillAcc[i].billAccName + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndTitleName' value='" + apNewBillAcc[i].titleName + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndContfName' value='" + apNewBillAcc[i].contfName + "' >";		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndContlName' value='" + apNewBillAcc[i].contlName + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndCargo' value='" + apNewBillAcc[i].cargo + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndPhoneArea' value='" + apNewBillAcc[i].phoneArea + "' >" ;		                 
              
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndPhoneNumber' value='" + apNewBillAcc[i].phoneNumber + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndAddress1' value='" + apNewBillAcc[i].address1 + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndAddress2' value='" + apNewBillAcc[i].address2 + "' >";		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndDepart' value='" + apNewBillAcc[i].depart + "' >" ;		                              
             
             if (apNewBillAcc[i].prov=="0")
               varprov='';
             else
               varprov=apNewBillAcc[i].prov;
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndProv' value='" + varprov + "' >" ;		                          
             
             if (apNewBillAcc[i].dist=="0")
               vardist='';
             else
               vardist=apNewBillAcc[i].dist;
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndDist' value='" + vardist + "' >";		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndZipCode' value='" + apNewBillAcc[i].zipCode + "' >" ;		                 
         
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndState' value='" + apNewBillAcc[i].state + "' >";	
         
             /*col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndTipo' value='" + apNewBillAcc[i].tipo + "' >";	*/
             
             if (apNewBillAcc[i].bscsCustId=="0" || apNewBillAcc[i].bscsCustId=="-1")
                codigoBSCS='';
             else
                codigoBSCS=apNewBillAcc[i].bscsCustId;
                
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndBscsCustId' value='" + codigoBSCS + "' >";	
             
             if (apNewBillAcc[i].bscsSeq=="0")
               varbscsSeq='';
             else
               varbscsSeq=apNewBillAcc[i].bscsSeq;
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndBscsSeq' value='" + varbscsSeq + "' >";	         
         }       
      } 
      //verVector();
      return true;
   }
  
   /*function fxSectionNameFinalStatus(){
      alert("final status seccion 1");   
      return true;  
   }*/
   
   function fxShowDetailBA(baId,customerId,siteId,arrInd,pagpermitida){      
      // Inicio CGC - COR0386   
      var v_nombre_pagina;      
      if (pagpermitida==1) //Edicion
         v_nombre_pagina="<%=strURLBillAccEdit%>";
      else //2: Consulta
         v_nombre_pagina="<%=strURLBillAccDetail%>";
      // Fin CGC - COR0386    
      
      var v_parametros = "?sUrl="+v_nombre_pagina+"¿nNewBillAccId=" + baId +"|hdnSessionId=<%=strSessionId%>|nOrderId=<%=lOrderId%>|nCustomerId="+customerId+"|nSiteId="+siteId+"|nIndex="+arrInd; //1:Agregar  2:Editar                              
      var v_Url=   "<%=strURLGeneralPage%>" +v_parametros;         
      window.open(v_Url, "Billing_Account","status=yes, location=0, width=450, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
   }
   
   function fxAddNewBillAcc(){       
      //var v_parametros = "?sUrl=<%=strURLBillAccNew%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId=<%=lSiteId%>";                             
      var v_parametros = "?sUrl=<%=strURLBillAccNew%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId=<%=lSiteId%>|strSpecificationId=<%=strSpecificationId%>|strSessionId=<%=strSessionId%>";                             
      var v_Url=   "<%=strURLGeneralPage%>" +v_parametros;         
      window.open(v_Url, "Billing_Account","status=yes, location=0, width=440, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");      
   }

   function fxDelete(index){
      form = document.frmdatos; 
      var table = document.all ? document.all["tableBillAcc"]:document.getElementById("tableBillAcc");
      var rowIndice=document.getElementById(index).rowIndex; 
      table.deleteRow(rowIndice);     
      apNewBillAcc[index-1].state='Eliminado';     
      indBillAcc=indBillAcc-1;
      fxRenumeric('tableBillAcc',indBillAcc);
      //fxDeleteTableBA();     
      //IND 536
      var baId = apNewBillAcc[index-1].billAccId;         
      var nameBA= apNewBillAcc[index-1].billAccName;
	   var orderId= "<%=strOrderId%>";
	   var customerId= "<%=strCustomerId%>";      
      var specificationId= "<%=strSpecificationId%>";         
     
	   param= "?nbaId="+baId;
      //param= "?nbaId="+baId+"&hdnSpecificationId="+specificationId;
      form.hdnNewBillAccId.value = baId;
      form.myaction.value="DeleteBillAcc";           
      form.action="<%=strURLOrderServlet%>"+param;        
      form.submit();        
   }
   
   function fxDeleteTableBA(){
      form = document.frmdatos;
      
      var table = document.getElementById("table_assignment");      
      for (z=table.rows.length;z>1;z--)
         table.deleteRow(z-1);      
   }
   
  /* function fxAAA(){      
      for(var i=0;i<apNewBillAcc.length;i++){      
      var variable=  "id->"+apNewBillAcc[i].billAccId +" name-->"+apNewBillAcc[i].billAccName + " "+
      " title->"+apNewBillAcc[i].titleName +" FName->"+apNewBillAcc[i].contfName +" "+ 
      " lName->"+apNewBillAcc[i].contlName +" Cargo->"+apNewBillAcc[i].cargo +" "+        
      " pArea->"+apNewBillAcc[i].phoneArea +" pNumber->"+apNewBillAcc[i].phoneNumber +" "+
      " ad1->"+apNewBillAcc[i].address1 +" ad2->"+apNewBillAcc[i].address2 +" "+ 
      " dep->"+apNewBillAcc[i].depart +" prov->"+apNewBillAcc[i].prov +" "+
      " dist->"+apNewBillAcc[i].dist +" zipCode->"+apNewBillAcc[i].zipCode +" "+
      " state->"+apNewBillAcc[i].state + 
      " bscsCustId->"+apNewBillAcc[i].bscsCustId +" bscsSeq->"+apNewBillAcc[i].bscsSeq;      
      alert(variable);  
      }
   }*/

  </script>
<input type=hidden name="hdnTypeCustomer" value="<%=strTypeCustomer%>">
<input type=hidden name="hdnCustomerId" value="<%=lCustomerId%>"> 
<input type=hidden name="hdnSiteId" value="<%=lSiteId%>">   
<input type=hidden name="hdnLogin" value="<%=strLogin%>"> 
<table border="0" cellspacing="0" cellpadding="0" width="40%">
<tr> 
   <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
   <td class="SubSectionTitle" align="left" valign="top">Cuenta Facturaci&oacute;n</td>
   <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
   <td align="right">   
   <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("billingacc")))){ 
      iPagPermitida=1; //Edicion  CGC - COR0386
   %>
   <a href="javascript:fxAddNewBillAcc();" > 
   <img name="item_img0" Alt="Agregar Billing Account" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no">
   </a>
   <%}else{
      iPagPermitida=2; //Consulta CGC - COR0386
   %>
   &nbsp;
   <%}%>
   </td>
</tr>     
</table>
<table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
<tr>
   <td>
      <table id="tableBillAcc" name="tableBillAcc" border="0" width="100%" cellpadding="2" cellspacing="1">                     
      <tr id="cabecera">
         <td class="CellLabel" align="center">#</td>
         <td class="CellLabel" align="center">Cuenta Facturaci&oacute;n</td>
         <td class="CellLabel" align="right" >&nbsp;  
         </td>                                 
      </tr>
      <% for (int i=0;i< arrLista.size();i++)
      {   objBillingABean=(BillingAccountBean)arrLista.get(i);
         objBillContBean=objBillingABean.getObjBillingContactB();                     
      
      %>
      <tr id="<%=i+1%>">
         <td class="CellContent" align="left"><%=i+1%></td>
         <td class="CellContent" align="left">         
            <a href="javascript:fxShowDetailBA(<%=objBillingABean.getNpBillaccountNewId()%>,<%=lCustomerId%>,<%=lSiteId%>,<%=i+1%>,<%=iPagPermitida%>);">
            <%=MiUtil.getString(objBillingABean.getNpBillacCName())%>         
            </a>         
         </td>
         <td class="CellContent" align="right">&nbsp;
         <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("billingacc")))){%>
         <a href="javascript:fxDelete(<%=i+1%>);"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2>         
         </a>
         <%}%>
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
      <%}%>              
      </table>
   </td>
</tr>
<tr>
   <td>
      <table id="hdnBillAcc" name="hdnBillAcc"> </table>
   </td>
</tr> 
</table> 

<%
   }else{    
    request.setAttribute("hshtInputDetailSection",hshParam);    
    String url = "/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/DetailOnDisplayBillingAccount.jsp";
%>
<jsp:include page="<%=url%>" flush="true" />
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer="defer">  
  function fxSectionBillAccountValidate(){
     return true;
   }
</script>   
</table>
<%}

 }catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>