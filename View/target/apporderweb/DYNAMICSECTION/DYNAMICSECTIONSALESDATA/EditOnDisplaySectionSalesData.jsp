<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.bean.ItemBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%@ page import="pe.com.nextel.service.SalesDataService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>

<%!
//Declaración de Variables Globales
//---------------------------------
  long lCustomerId         = 0,
        lSiteId             = 0,
        lOrderId            = 0,
        lSiteIncId          = 0,
        lngProviderGrpId    = 0,
        lngSalesTeamId      = 0,
        lGeneratorId        = 0,
        lngUnknwnSiteId     = 0,
        lDivisionId         = 0;
   int  iUserId     = 0,
        iAppId      = 0,
        iPermission = 0;   
           
   
   String strSessionId        = null,
          strCustomerId       = null,
          strOrderId          = null,
          strSpecificationId  = null,
          strGeneratorType    = null,
          strShowDataFields   = null;
          
   HashMap hshData  = null, 
           hshSalesData = null,
           hshScreenField = null;
          
   
   CustomerBean objCustomerBean=null;
  
   ArrayList arrData    = null,    
             arrNewData = null;
%>

<%  
try{

  Hashtable hshParam = (Hashtable)request.getAttribute("hshtInputEditSection");  
  if (hshParam==null) hshParam = new Hashtable();
  
  strSessionId= MiUtil.getString((String)hshParam.get("strSessionId"));  
   
  lCustomerId       = MiUtil.parseLong((String)hshParam.get("strCustomerId"));
  lSiteId           = MiUtil.parseLong((String)hshParam.get("strSiteId"));  
  lOrderId          = MiUtil.parseLong((String)hshParam.get("strOrderId"));     
  strSpecificationId= MiUtil.getString((String)hshParam.get("strSpecificationId"));
  lDivisionId       = MiUtil.parseLong((String)hshParam.get("strDivisionId"));
  
  strShowDataFields = (String)hshParam.get("strShowDataFields");

  System.out.println("**************///************************************************");
  System.out.println("strShowDataFields:"+strShowDataFields);
  
  EditOrderService objEditService      = new EditOrderService();
  CustomerService  objCustomerService  = new CustomerService();   
  GeneralService   objGeneralService   = new GeneralService();  
  SalesDataService objSalesDataService = new SalesDataService();  
   EditOrderService objOrderService=new EditOrderService();
  
  String strMessage=null;  
   
  System.out.println("EditOnDisplaySectionSales]Sesión a consultar : " + strSessionId);
  PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
  System.out.println("[EditOnDisplaySectionSales]Sesión a consultar : " + objSessionBean);
  if( objSessionBean == null ){
     throw new Exception("No se encontró la sesión del usuario");
  }
  
  iUserId  = objSessionBean.getUserid();   
  iAppId   = objSessionBean.getAppId();   
  
  
  //if(iPermission==0){
   if (strShowDataFields.equals("1")){
     
       int iLongitudNew=0;

      //Data para la Sección de Ventas Data
      //-----------------------------------
      hshData= objSalesDataService.getAplicationCustomer(lOrderId,lDivisionId,lCustomerId,Constante.IND_STATUS_SOLICITADO);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage); 
      arrNewData=(ArrayList)hshData.get("objArrayListCustomer");               
      if (arrNewData!=null) iLongitudNew=arrNewData.size();
      
      //Rutas 
      //-----
      String strRutaContext=request.getContextPath();    
      String strURLSalesDataNew = strRutaContext+"/DYNAMICSECTION/DYNAMICSECTIONSALESDATA/SalesDataPages/SalesDataNew.jsp";
      String strURLOrderDataServlet =strRutaContext+"/salesdataservlet"; 
      String strURLGeneralPage=strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";     
      
      //Permisos para agregar mas registros a Ventas Data
      //-------------------------------------------------
      hshData=objOrderService.getOrderScreenField(lOrderId,Constante.PAGE_ORDER_EDIT);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
            throw new Exception(strMessage);  
         
      hshScreenField= (HashMap)hshData.get("hshData");        
   
%>

<br>
<!--Ventas Data-->   
<INPUT type="hidden" name="hdnDataOrderid">
<INPUT type="hidden" name="hdnDataCustomerid">
<INPUT type="hidden" name="hdnDataDivisionid">
<INPUT type="hidden" name="hdnStatusData" value="s">
<table border="0" cellspacing="0" cellpadding="0" width="65%">
<tr>
   <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
   <td class="SubSectionTitle" align="left" valign="top">&nbsp;&nbsp;Ventas Data</td>
   <td class="SubSectionTitleRightCurve"  valign="top" align="right" width="11">&nbsp;&nbsp;</td>
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
   <% String strShowData = null;
      if ( "Enabled".equals(MiUtil.getString((String)hshScreenField.get("salesdata")))){
     strShowData="style=display:''";
   }else{  
     strShowData="style=display:'none'";
   }
   %>
   <td align="right" id="salesdatanew" <%=strShowData%> >
      <a href="javascript:fxAddNewSalesData();"> 
       <img name="item_img0" Alt="Agregar Site" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no">
      </a>
   </td>
</tr>            
</table>

<table border="0"  width="65%"  class="RegionBorder">
<tr>
   <td>             
      <table align=center width="100%" border="0" id="tabSalesData" name="tabSalesData" cellpadding="0" cellspacing="1" >
         <tr align="center">                  
            <td class="CellLabel" width="5%">#</td>
            <td class="CellLabel">Ventas Data</td>                  
            <td class="CellLabel">Solución de Negocio</td>
            <td class="CellLabel">Estado</td>
            <td class="CellLabel">Creado<br>Por</td>
            <td class="CellLabel"></td>                  
         </tr>
      <% int ind=0;
         ItemBean objItemBean = null;
         String strStatus = null;
         for(int i=0; i<arrNewData.size();i++) {
             objItemBean   = new ItemBean();
             objItemBean = (ItemBean)arrNewData.get(i);
             
             strStatus = objItemBean.getNpstatusaplication();
             if (strStatus.equals(Constante.IND_STATUS_SOLICITADO) || strStatus.equals(Constante.IND_STATUS_UNKNOW)){
                strStatus="Solicitado";
             }else if (strStatus.equals(Constante.IND_STATUS_ACTIVE)){
               strStatus="Contratado";
             }else if (strStatus.equals(Constante.IND_STATUS_INACTIVE)){
               strStatus="Inactivado";
             }
      %>
      <tr  id="data<%=ind+i+1%>">
      <td  align="center" class="CellContent"><%=ind+i+1%></td>
      <td  class="CellContent">
         <a href="javascript:fxShowUnkSalesData(<%=MiUtil.getIfNotEmpty(MiUtil.getString(objItemBean.getNpbusinesssolutionid()))%>);">    
          <%=objItemBean.getNpproductname()%>
         </a>
      </td>
      <td  align="center" class="CellContent"><%=objItemBean.getNpproductlinename()%></td>
      <td  align="center" class="CellContent"><%=strStatus%></td>
      <td  align="center" class="CellContent"><%=objItemBean.getNpcreatedby()%></td>
      <td class="CellContent" align="center">    
      <% if (strStatus.equals("Solicitado")){ %>
      <a href="javascript:fxDeleteData(<%=MiUtil.getIfNotEmpty(MiUtil.getString(objItemBean.getNpbusinesssolutionid()))%>,'data<%=(ind+i+1)%>');">
        <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2>
      </a>   
      <%}else{%>
         <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2>
      <%}%>
      </td>    
      </tr>
      <%}
       int iTotalData=arrNewData.size();
      %>
      <input type="hidden" name="hdnCountData" value="<%=iTotalData%>">  
      </table>         
   </td>
</tr>
</table>
<br>

<script language='javascript' DEFER>
  var contadorData=0;   
  var flgShowNewIcon=0;
  
  function fxSalesOnLoad(){
    form = document.frmdatos;
    contadorData=parseInt(form.hdnCountData.value);      
  }
  
  function fxAddNewSalesData(){
    form = document.frmdatos;
    form.hdnDataOrderid.value ='<%=lOrderId%>';
    form.hdnDataCustomerid.value ='<%=lCustomerId%>';
    form.hdnDataDivisionid.value ='<%=lDivisionId%>';
    var parametros = "?sUrl=<%=strURLSalesDataNew%>"+"¿nCustomerId=<%=lCustomerId%>|nDivisionId=<%=lDivisionId%>|nOrderId=<%=lOrderId%>|hdnSessionId=<%=strSessionId%>|pSpecificationId=<%=strSpecificationId%>";
    var Url= "<%=strURLGeneralPage%>"+parametros; 
    window.open(Url, "Ventas_Data","status=yes, location=0, width=750, height=200, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
   }
   
  function fxShowUnkSalesData(cod){
    form = document.frmdatos;
    form.hdnDataOrderid.value ='<%=lOrderId%>';
    form.hdnDataCustomerid.value ='<%=lCustomerId%>';
    form.hdnDataDivisionid.value ='<%=lDivisionId%>';
    var parametros = "?sUrl=<%=strURLSalesDataNew%>"+"¿nCustomerId=<%=lCustomerId%>|nDivisionId=<%=lDivisionId%>|hdnSessionId=<%=strSessionId%>|pSalesDataId="+cod+"|pSpecificationId=<%=strSpecificationId%>";
    var Url= "<%=strURLGeneralPage%>"+parametros;              
     window.open(Url, "Ventas_Data","status=yes, location=0, width=750, height=200, left=100, top=100, scrollbars=no, screenX=50, screenY=100"); 
  }
  
  function fxDeleteData(codigo,ind){           
   
      form = document.frmdatos;     
      var table = document.all ? document.all["tabSalesData"]:document.getElementById("tabSalesData");         
      var rowIndice=document.getElementById(ind).rowIndex;                           
      var customerId= "<%=lCustomerId%>";         
      var orderId= "<%=lOrderId%>";          
      var divisionId = "<%=lDivisionId%>";   

      param= "?nDataId="+codigo+"&nInd="+rowIndice+"&nCount="+contadorData+"&pCustomerId="+customerId+"&pOrderId="+orderId+"&pSpecificationId=<%=strSpecificationId%>&pDivisionId="+divisionId;                           
      form.myaction.value="DeleteData";          
      form.action="<%=strURLOrderDataServlet%>"+param;                   
      form.submit();            
   }
   
   function fxDicreseContadorData(){
      contadorData--;
      flgShowNewIcon--;
     // fxChangeViewAddIcon();
   }

   
   function fxAddSalesData(strSalesDataId,strProductName,strSolutionName,strStatus,createdBy){
     
      row = tabSalesData.insertRow(-1);      
      flgShowNewIcon++;      
      contadorData++;         
      
      row.id ="data"+contadorData;
      col = row.insertCell(-1);
      col.className = "CellContent";
      col.align     = "center";
      col.innerHTML = contadorData;              

      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.innerHTML = "<a href=javascript:fxShowUnkSalesData('"+strSalesDataId+"');>"+strProductName+"</a>";      
                          
      col = row.insertCell(-1);
      col.className = "CellContent";    
      col.align     = "center";                                        
      col.innerHTML = strSolutionName; 
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "center";                             
      col.innerHTML = strStatus;       
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "center";            
      col.innerHTML = createdBy;  
      
      col = row.insertCell(-1);
      col.className = "CellContent";               
      col.align     = "center";            
      col.innerHTML = "<a href=javascript:fxDeleteData('"+strSalesDataId+"','data"+contadorData+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";            
      fxRenumeric('tabSalesData',contadorData);
     // fxChangeViewAddIcon();
   }
   
   function fxChangeViewAddIcon(){      
      var maximo=<%=Constante.NUMBER_ALLOWED_NEW_SITE%>;
      if (contadorData >= maximo ){
         salesdatanew.style.display="none";
      }else{
         salesdatanew.style.display="";
      }
   }
   
  function fxUpdSalesData(strSalesDataId,strProductName,strSolutionName,strStatus,createdBy){ 
      var fila,colum;
      var long=document.getElementById('tabSalesData').rows.length;      
      for (i=0;i<long;i++){      
         fila=document.getElementById('tabSalesData').rows[i];
         colum=fila.cells;   
         texto=colum[1].innerHTML;         
         if (texto.indexOf(strSalesDataId) >-1){            
            rowid=fila.id;        
            row = document.getElementById(rowid);      
       
            var CellNombre = row.childNodes.item(1);
            CellNombre.className = "CellContent";               
            CellNombre.innerHTML = "<a href=javascript:fxShowUnkSalesData('"+strSalesDataId+"');>"+strProductName+"</a>";
            
            var CellSolution = row.childNodes.item(2);
            CellSolution.className = "CellContent";    
            CellSolution.align     = "center";                                        
            CellSolution.innerHTML = strSolutionName; 
            
            var CellStatus = row.childNodes.item(3);
            CellStatus.className = "CellContent";               
            CellStatus.align     = "center";                             
            CellStatus.innerHTML = strStatus;       
            
            var CellCreatedBy = row.childNodes.item(4);
            CellCreatedBy.className = "CellContent";               
            CellCreatedBy.align     = "center";            
            CellCreatedBy.innerHTML = createdBy;  
            
            var CellDelete = row.childNodes.item(5);
            CellDelete.className = "CellContent";               
            CellDelete.align     = "center";            
            CellDelete.innerHTML = "<a href=javascript:fxDeleteData('"+strSalesDataId+"','"+rowid+"');><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";                       
           
         }         
   
      }      
   }
   
 

</script>



<%
  }else{
%>
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer>  
  function fxSalesOnLoad(){
  }
</script>   
</table>
<%}
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