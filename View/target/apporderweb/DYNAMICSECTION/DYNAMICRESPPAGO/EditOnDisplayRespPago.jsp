<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.OrderBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.SiteService"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%  
 try{  
   
   //PARAMETRO del PAGE   
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));    
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   //CEM COR0354
   String strCustomerId=(String)hshParam.get("strCustomerId");
   String strOrderId=(String)hshParam.get("strOrderId");
   String strSpecificationId=(String)hshParam.get("strSpecificationId");
   //CEM COR0354    
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));   
   int iUserId=0;
   int iAppId=0;
   HashMap hshData=null;
   HashMap hshSites=null;
   HashMap hshOrder=null;   
   OrderBean objOrderBean=null;
   CustomerBean objCustomerBean=null;
   ArrayList arrSite=null;    
   ArrayList arrNewSite=null;  
   EditOrderService objEditService=new EditOrderService();
   CustomerService objCustomerService=new CustomerService();   
   GeneralService objGeneralService=new GeneralService();  
   EditOrderService objOrderService=new EditOrderService();
   SiteService objSiteService=new SiteService();
   String strMessage=null;   
   HashMap hshScreenField=null;
   long lGeneratorId=0;
   
   System.out.println("[EditOnDisplayRespPago]Sesión a consultar : " + strSessionId);
   PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[EditOnDisplayRespPago]Sesión a consultar : " + objSessionBean);
   if( objSessionBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   
   iUserId=objSessionBean.getUserid();
   iAppId=objSessionBean.getAppId();
      
   
   // Obtiene GeneratorType, GeneratorId y ProviderGroup de la Orden   
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
   System.out.println("[NewOnDisplayRespPago][strResultGross]"+strResultGross);
   
   // Valida si la Categoría es un Serv. Adic (2016)
   HashMap hshDataServAdic = new HashMap();
	hshDataServAdic = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_SERV_ADIC);
   String strResultServAdic = (String) hshDataServAdic.get("strDescription");   
   System.out.println("[NewOnDisplayRespPago][strResultServAdic]"+strResultServAdic);
      
   //Obtiene origen del site: OPP, Orden   
   int iSourceRP = objSiteService.getSourceSite(lGenerId);   
      
   // Órdenes GROSS
   if (strResultGross != null && strResultGross.equals("1")){
      //strGeneratorType = "OPP";      
      if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP) || MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP_PORTAB) ){
         // Se valida si el origen de la oportunidad es Banda Ancha         
         //lGeneratorId=702039;
         hshData = (new GeneralService()).getNpopportunitytypeid(lGenerId);
         strMessage = (String)hshData.get("strMessage");
         if ( strMessage != null ) throw new Exception(strMessage);  
            int iResult = MiUtil.parseInt((String)hshData.get("iReturnValue"));  
            //Si = 3 -> Validación para Banda Ancha            
            strFlagAddRP = "1";            
            int iSalesID = MiUtil.parseInt((String)hshData.get("iSalesId"));              
            iPermission = (new GeneralService()).getValidateAuthorization(lCustomerId, iResult, iSalesID);            
      }
      else{   
         if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) ){//Si el origen es un Incidente            
            hshData=objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_SITE, iUserId, iAppId);
            strMessage=(String)hshData.get("strMessage");
               if (strMessage!=null)
                  throw new Exception(strMessage);          
            iPermission=MiUtil.parseInt((String)hshData.get("iRetorno"));         
         }
      }  
   }
   else{//Órdenes Serv. Adicionales: no se requiere validar autorizaciones o permisos a los roles de trabajo      
      if(strResultServAdic != null && strResultServAdic.equals("1")){
         iPermission = 1;
      }      
   }
      
   System.out.println("------ INCIO DE EditOnDisplayResPago.jsp -------");   
   System.out.println("iPermission-->"+iPermission);
   System.out.println("lCustomerId--> "+lCustomerId);
   System.out.println("lOrderId--> "+lOrderId);   
   System.out.println("lSiteId--> "+lSiteId);      
   System.out.println("strSessionId--> "+strSessionId);   
   System.out.println("iUserId--> "+iUserId);       
   System.out.println("iUserId--> "+iAppId);       
   System.out.println("strSpecificationId -> "+strSpecificationId); //CEM - COR0354
   System.out.println("------ FIN DE EditOnDisplayResPago.jsp -------");
   
     
   if (iPermission == 1){ 
      String strCustomerName="";
      
      hshOrder=(HashMap)objEditService.getOrder(lOrderId);
      strMessage=(String)hshOrder.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);
       
      /*objOrderBean=(OrderBean)hshOrder.get("objResume");
      if (objOrderBean!=null){*/
         hshData=objCustomerService.getCustomerData(lCustomerId);
         strMessage=(String)hshData.get("strMessage");
         if (strMessage!=null)
            throw new Exception(strMessage); 
            
         objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");
            
         strCustomerName=MiUtil.getString(objCustomerBean.getSwName());                     
         
         hshData=objCustomerService.getCustomerSitesList(lCustomerId,lOrderId,lGeneratorId,Constante.SITE_STATUS_UNKNOWN);         
         strMessage=(String)hshData.get("strMessage");
         if (strMessage!=null)      
            throw new Exception(strMessage);       
         arrNewSite=(ArrayList)hshData.get("arrCustomerSiteList");

         //Manejo dinamico de controles   
         hshData=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_EDIT);
         strMessage=(String)hshData.get("strMessage");
         if (strMessage!=null)
            throw new Exception(strMessage);  
         
         hshScreenField= (HashMap)hshData.get("hshData");          
     // } 
      
      //Rutas    
      String strRutaContext=request.getContextPath();         
      String strURLSiteNew = strRutaContext+"/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/SiteNew.jsp"; 
      String strURLSiteGeneral = strRutaContext+"/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/SiteGeneralNew.jsp"; 
      String strURLSiteView = strRutaContext+"/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/SiteGeneralView.jsp"; 
      String strURLGeneralPage=strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";
      String strURLOrderServlet =strRutaContext+"/editordersevlet";      

%>
<br>
<!--Reponsable de Pago-->     
<table border="0" cellspacing="0" cellpadding="0" width="65%">
<tr>
   <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
   <td class="SubSectionTitle" align="left" valign="top">&nbsp;&nbsp;Responsable de Pago</td>
   <td class="SubSectionTitleRightCurve"  valign="top" align="right" width="11">&nbsp;&nbsp;</td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
   <% String strShow=null;
      if (arrNewSite.size()<Constante.NUMBER_ALLOWED_NEW_SITE 
         && "Enabled".equals(MiUtil.getString((String)hshScreenField.get("resppago"))) && iPermission==1)
         strShow="style=display:''";
      else
         strShow="style=display:'none'";%>
   <td align="right" id="sitenew" <%=strShow%>>
      <a href="javascript:fxAddNewSite();"> 
      <img name="item_img0" Alt="Agregar Site" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no">
      </a>               
   </td>
</tr>            
</table>    
<table border="0"  width="65%"  class="RegionBorder">
<tr>
   <td>             
      <table align=center width="100%" border="0" id="tabSite" name="tabSite" cellpadding="0" cellspacing="1" class="CTable">
      <tr align="center">                  
      <td class="CellLabel" width="5%">#</td>         
      <td class="CellLabel">Responsable Pagos</td>         
      <td class="CellLabel">C&oacute;digo BSCS</td>
      <td class="CellLabel">Regi&oacute;n</td>
      <td class="CellLabel">Estado</td>
      <td class="CellLabel">Creado<br>Por</td>
      <% if (iPermission==1){ %>
      <td class="CellLabel"></td>                  
      <%}%>
      </tr>    
     
      <%  int ind=0;//arrSite.size();
      for(int i=0; i<arrNewSite.size();i++) {                                  
         hshSites = (HashMap)arrNewSite.get(i);             
      %>
      <tr id="site<%=ind+i+1%>">        
      <td class="CellContent"><%=ind+i+1%></td> 
      <td class="CellContent">
         <a href="javascript:fxShowUnkSite(<%=MiUtil.getIfNotEmpty((String)hshSites.get("swsiteid"))%>);">             
         <%=MiUtil.getString((String)hshSites.get("swsitename"))%>
         </a>
      </td>
      <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("npcodbscs"))%></td>
      <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("regionname"))%></td>
      <td  class="CellContent"><%=Constante.SITE_STATUS_NUEVO%></td>
      <td  class="CellContent"><%=MiUtil.getString((String)hshSites.get("createdby"))%></td>               
      
      <td class="CellContent" align="center">
      <%
      System.out.println("[****]"+MiUtil.getString((String)hshScreenField.get("resppago")));
      if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("resppago"))) && iPermission==1){%>
      <a href="javascript:fxDeleteSite(<%=MiUtil.getIfNotEmpty((String)hshSites.get("swsiteid"))%>,'site<%=(ind+i+1)%>');"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2></a>
      <%} else %>  
         &nbsp;
      </td>          
      </tr>
      <%}         
         int iTotal=arrNewSite.size();
      %>
      <input type="hidden" name="hdnCountSite" value="<%=iTotal%>">             
      </table>  
   </td>
</tr>
</table>
<br>
<script language='javascript' defer="defer">
   var contador=0;   
   var flgShowNewIcon=0;
   var flagOPP = 0;
   function fxSiteOnLoad(){
      form = document.frmdatos;  
      contador=parseInt(form.hdnCountSite.value);      
   }
   
   function fxShowUnkSite(cod){
      //var parametros = "?sUrl=<%=strURLSiteView%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId="+cod+"|hdnSessionId=<%=strSessionId%>";                              
      var iPermission="<%=iPermission%>";     
      var rutaRespPago;
      if ( iPermission==1) // tiene permiso
         rutaRespPago="<%=strURLSiteGeneral%>";
      else // no tiene permiso
         rutaRespPago="<%=strURLSiteView%>";
      var parametros = "?sUrl="+rutaRespPago+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId="+cod+"|hdnSessionId=<%=strSessionId%>|pSpecificationId=<%=strSpecificationId%>"; //CEM - COR0354                             
      var Url= "<%=strURLGeneralPage%>"+parametros;              
      window.open(Url, "Responsable_Pago","status=yes, location=0, width=750, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
   }
   
   function fxAddNewSite(){
      flagOPP = 1;
      //var parametros = "?sUrl=<%=strURLSiteNew%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|hdnSessionId=<%=strSessionId%>|sCustomerName=<%=MiUtil.escape2(strCustomerName)%>";
	  var parametros = "?sUrl=<%=strURLSiteNew%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|hdnSessionId=<%=strSessionId%>|sCustomerName=<%=MiUtil.escape2(strCustomerName)%>|pSpecificationId=<%=strSpecificationId%>"; //CEM - COR0354";
      var Url= "<%=strURLGeneralPage%>"+parametros;         
      window.open(Url, "Responsable_Pago","status=yes, location=0, width=750, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
   }
   
   
   function fxDeleteSite(codigo,ind){           
      var vFlag = "<%=iSourceRP%>";           
      if (vFlag == "0" || flagOPP == 1){
         flagOPP = 0;
         form = document.frmdatos;  
         var table = document.all ? document.all["tabSite"]:document.getElementById("tabSite");
         var rowIndice=document.getElementById(ind).rowIndex;  
         //INICIO CEM - COR0354
         var customerId= "<%=strCustomerId%>";
         var orderId= "<%=strOrderId%>";
         var specificationId= "<%=strSpecificationId%>";
         param= "?nSiteId="+codigo+"&nInd="+rowIndice+"&nCount="+contador+"&pCustomerId="+customerId+"&pOrderId="+orderId+"&pSpecificationId="+specificationId; 
         //param= "?nSiteId="+codigo+"&nInd="+rowIndice+"&nCount="+contador;
         //FIN CEM - COR0354	  
         form.myaction.value="DeleteSite";           
         form.action="<%=strURLOrderServlet%>"+param;          
         form.submit();       
      }else{
          alert("No puede eliminar un Responsable de Pago creado desde una Oportunidad");
          return;
      }
   }
   
   function fxDicreseContador(){
      contador--;
      flgShowNewIcon--;
      fxChangeViewAddIcon();
   }
   
   function fxChangeViewAddIcon(){   
      var maximo=<%=Constante.NUMBER_ALLOWED_NEW_SITE%>;
      if (contador >= maximo ){
         sitenew.style.display="none";     
      }else{
         sitenew.style.display="";     
      }           
   }


   function fxSectionNameValidate22(){
      //alert("guardado de seccion dinamica seccion 22");   
      return true;
   }   


   function fxAddSite(id,siteName,regionName,status,createdBy){
      row = tabSite.insertRow(-1);
      flgShowNewIcon++;
      contador++;         
      
      row.id ="site"+contador;
      col = row.insertCell(-1);
      col.className = "CellContent";
      col.innerHTML = contador;              
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.innerHTML = "<a href=javascript:fxShowUnkSite('"+id+"');>"+siteName+"</a>";
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.innerHTML = "&nbsp;";                      
      
      col = row.insertCell(-1);
      col.className = "CellContent";    
      col.align     = "left";                                        
      col.innerHTML = regionName; 
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "left";                             
      col.innerHTML = status;       
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "left";            
      col.innerHTML = createdBy;  
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "center";            
      col.innerHTML = "<a href=javascript:fxDeleteSite('"+id+"','site"+contador+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";           
      fxRenumeric('tabSite',contador);
      fxChangeViewAddIcon();
   
   }

   function fxUpdSite(id,siteName,regionName,status,createdBy){     
      var fila,colum;
      var long=document.getElementById('tabSite').rows.length;      
      for (i=0;i<long;i++){      
         fila=document.getElementById('tabSite').rows[i];
         colum=fila.cells;   
         texto=colum[1].innerHTML;         
         if (texto.indexOf(id) >-1){            
            rowid=fila.id;        
            row = document.getElementById(rowid);      
       
            var CellNombre = row.childNodes.item(1);
            CellNombre.className = "CellContent";               
            CellNombre.innerHTML = "<a href=javascript:fxShowUnkSite('"+id+"');>"+siteName+"</a>";
            
            var CellBSCS = row.childNodes.item(2);
            CellBSCS.className = "CellContent";               
            CellBSCS.innerHTML = "&nbsp;";                      
            
            var CellRegion = row.childNodes.item(3);
            CellRegion.className = "CellContent";    
            CellRegion.align     = "left";                                        
            CellRegion.innerHTML = regionName; 
            
            var CellStatus = row.childNodes.item(4);
            CellStatus.className = "CellContent";               
            CellStatus.align     = "left";                             
            CellStatus.innerHTML = status;       
            
            var CellCreatedBy = row.childNodes.item(5);
            CellCreatedBy.className = "CellContent";               
            CellCreatedBy.align     = "left";            
            CellCreatedBy.innerHTML = createdBy;  
            
            var CellDelete = row.childNodes.item(6);
            CellDelete.className = "CellContent";               
            CellDelete.align     = "center";            
            CellDelete.innerHTML = "<a href=javascript:fxDeleteSite('"+id+"','"+rowid+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";                       
           
         }         
   
      }      
   }  
</script>
<%
   }else{
   
   request.setAttribute("hshtInputDetailSection",hshParam);    
   String url = "/DYNAMICSECTION/DYNAMICRESPPAGO/DetailOnDisplayRespPago.jsp";
%>
<jsp:include page="<%=url%>" flush="true" />
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer>  
  function fxSiteOnLoad(){
  
   }
</script>   
</table>
<%   
   }
}catch(Exception ex){
   ex.getCause();
   ex.printStackTrace();
   System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%> 