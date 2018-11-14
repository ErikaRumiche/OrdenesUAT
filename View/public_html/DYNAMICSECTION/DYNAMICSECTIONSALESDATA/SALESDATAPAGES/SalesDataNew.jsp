<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>


<html>
    <head>
       <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
      <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
       <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    </head>
<body>

<%
  //Ingresamos lógica del negocio
  //-----------------------------
  try{
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lDivisionid=(request.getParameter("nDivisionId")==null?0:Long.parseLong(request.getParameter("nDivisionId")));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   long lSpecificationid=(request.getParameter("pSpecificationId")==null?0:Long.parseLong(request.getParameter("pSpecificationId")));
   long lSalesDataid=(request.getParameter("pSalesDataId")==null?0:Long.parseLong(request.getParameter("pSalesDataId")));
   
   System.out.println(" --------------------  INCIO SalesDataNew.jsp---------------------- ");   
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSessionId-->"+strSessionId);   
   System.out.println("lCustomerId :" + lCustomerId);
   System.out.println("lSpecificationid :" + lSpecificationid);
   System.out.println("lSalesDataid : " + lSalesDataid);
   System.out.println(" --------------------  FIN SalesDataNew.jsp---------------------- ");   
   
   String strURLDataServlet =request.getContextPath()+"/salesdataservlet";  
   
    String strMessage=null;
    String strCustomerName="",strProducLineName="",strProductName="";
    HashMap hshData  = null, hshDataAplication =null;
    long lproductlineid =0, lproductid=0;
    
   //Obtenemos el Nombre del Cliente
   //------------------------------
    CustomerService  objCustomerService  = new CustomerService();   
    CustomerBean objCustomerBean=null;
    ItemBean objItemBean = null;
  
    hshData=objCustomerService.getCustomerData(lCustomerId);
    strMessage=(String)hshData.get("strMessage");
    if (strMessage!=null)
         throw new Exception(strMessage); 
         
    objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");         
    strCustomerName=MiUtil.getString(objCustomerBean.getSwName()); 
   
   //Obtenemos los Valores del Combo
   //--------------------------------
   ArrayList arrDataList =  new ArrayList();
   SalesDataService objSalesDataService = new SalesDataService();
   hshData = objSalesDataService.getAplicationDataList(lOrderId,lDivisionid,lCustomerId,lSpecificationid);
   if (hshData!=null){
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
          throw new Exception(strMessage);
      arrDataList=(ArrayList)hshData.get("objArrayList");
   }   
   
   //Obtenemos el Detalle de una Aplicación
   //--------------------------------------
   if (lSalesDataid>0){
      hshDataAplication=objSalesDataService.getAplicationDetail(lSalesDataid);
      strMessage=(String)hshDataAplication.get("strMessage");
      if (strMessage!=null)
          throw new Exception(strMessage);
      objItemBean = (ItemBean)hshDataAplication.get("objItemBean");
      lproductlineid = objItemBean.getNpproductlineid();
      lproductid     = objItemBean.getNpproductid();
      strProducLineName = objItemBean.getNpproductlinename();
      strProductName    = objItemBean.getNpproductname();
   }
%>

<script language="javascript">

  var vctSalesData   = new Vector();
  var vctProductLine = new Vector();		
  var vctProduct     = new Vector();		

  function fxMakSaleseData(solutionId,productLineId,productLineName,productId,productName){
      this.solutionId       = solutionId;
      this.productLineId    = productLineId;
      this.productLineName  = productLineName;
      this.productId        = productId;
      this.productName      = productName;
  }
  
   function makeProductLine(productLineId,productLineName) {
      this.productLineId        = productLineId;
      this.productLineName      = productLineName;
   }	
   
   function makeProduct(productLineId,productId,productName){
      this.productLineId      = productLineId;
      this.productId          = productId;
      this.productName        = productName;
   }
  
  function fx_fillElementsVectorData(){
      
      for(i=0;i<vctSalesData.size();i++){
         obj_spec = null;
         obj_spec = vctSalesData.elementAt(i);
			
         if (!fx_existsElementVProducLine(obj_spec.productLineId))
             fx_addElementVProducLine(obj_spec.productLineId,obj_spec.productLineName);

         if (!fx_existsElementVProductLineProduct(obj_spec.productLineId,obj_spec.productId))
             fx_addElementVProduct(obj_spec.productLineId,obj_spec.productId,obj_spec.productName);      
      }
   }
   
   function fx_existsElementVProducLine(v_productLineId){
      var l = 0;
      
      for(l=0;l<vctProductLine.size();l++){
          obj_productLineId = null;
          obj_productLineId = vctProductLine.elementAt(l);
          if (obj_productLineId.productLineId == v_productLineId){
              return true;
          }
      }
      return false;
   }	
   
   function fx_addElementVProducLine(v_productLineId,v_productLineName){
      vctProductLine.addElement(new makeProductLine(v_productLineId,v_productLineName));
   }
   
   function fx_existsElementVProductLineProduct(v_productLineId,v_productId){
      var j = 0;
      
      for(j=0;j<vctProduct.size();j++){
           obj_vProduct = null;
           obj_vProduct = vctProduct.elementAt(j);
           if ( (obj_vProduct.productLineId == v_productLineId) && (obj_vProduct.productId == v_productId) ){
               return true;
           }
      }
      return false;
   }
   
   function fx_addElementVProduct(v_productLineId,v_productId,v_productName){
      vctProduct.addElement(new makeProduct(v_productLineId,v_productId,v_productName));
   }
   
   function fx_fillProductLine(){
      DeleteSelectOptions(document.formdatos.cmbSolucionNegocio);
        for(i=0;i<vctProductLine.size();i++){
            obj_vProductLine = null;
            obj_vProductLine = vctProductLine.elementAt(i);
            AddNewOption(document.formdatos.cmbSolucionNegocio,obj_vProductLine.productLineId,obj_vProductLine.productLineName);
        }
   }
   
  function fx_fillProduct(v_productLineId){      
		DeleteSelectOptions(document.formdatos.cmbAplicacion);      
		for(i=0;i<vctProduct.size();i++){              
			obj_vProduct = null;
			obj_vProduct = vctProduct.elementAt(i);
			if( obj_vProduct.productLineId == v_productLineId )
				AddNewOption(document.formdatos.cmbAplicacion,obj_vProduct.productId,obj_vProduct.productName);
		}
   }
   
  function fxSubmit(){
    form = document.formdatos;
      if (fxValidate()==false) 
        return false; 
        form.myaction.value="SetSalesData";  
        form.hdnSessionId.value="<%=strSessionId%>";
        form.hdnOrderId.value="<%=lOrderId%>";
        form.hdnCustomerId.value="<%=lCustomerId%>"; 	
        form.hdnDivisionId.value="<%=lDivisionid%>"; 
        form.hdnSpecificationId.value="<%=lSpecificationid%>";
        form.hdnSalesDataId.value="<%=lSalesDataid%>";
        form.action="<%=strURLDataServlet%>";           
        form.submit();    
  }
  
  
 function fxValidate(){
      form = document.formdatos;
      
      // Validación de la Solución
      if (form.cmbSolucionNegocio.selectedIndex==0){
          alert("Seleccione la Solución de Negocio.");
          Form.cmbSolucionNegocio.focus();
          return false;
      }
      
       // Validación de la Aplicación
      if (form.cmbAplicacion.selectedIndex==0){
          alert("Seleccione una Aplicación.");
          Form.cmbAplicacion.focus();
          return false;
      }
    
      return true;
   }
   
  function fxCheckOnClose(){
      Form = document.formdatos;
      parent.window.close();
      return true;
   }
</script>

  <FORM method="get" name="formdatos" target="bottomFrame">
     <input type="hidden" name="hdnSessionId"> 
     <input type="hidden" name="hdnOrderId" value=""> 
     <input type="hidden" name="hdnCustomerId" value=""> 
     <input type="hidden" name="hdnDivisionId" value=""> 
     <input type="hidden" name="hdnSolucionId" value=""> 
     <input type="hidden" name="hdnSpecificationId" value=""> 
     <input type="hidden" name="hdnSalesDataId" value=""> 
     <input type="hidden" name="myaction" value=""> 
	 
     <table border="0" cellspacing="0" cellpadding="0" width="100%" class="RegionBorder">
        <tr>
           <td width="100%" class="CellContent" align="center"><font class="TabForegroundText">Ingreso de Aplicaciones para Venta Data</font></td>
        </tr>
     </table>
     
     <table border="0" cellspacing="0" cellpadding="0">
          <tr>
              <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="5"></td>
           </tr>
     </table>
     <table border="0" width="99%" cellpadding="0" cellspacing="0" align="center">
        <tr>
           <td>
              <table border="0" cellspacing="0" cellpadding="0">
                 <tr class="PortletHeaderColor">
                    <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                    <td class="SubSectionTitle" align="left" valign="top">Datos Generales</td>
                    <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
                 </tr>
              </table>
           </td>
        </tr>
     </table>
     <table border="0" width="99%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center">
        <tr>
           <td class="RegionHeaderColor" width="100%">
              <center>
              <table width="100%" border="0" cellspacing="1" cellpadding="0">
                 <tr>
                    <td class="CellLabel">&nbsp;Compañía</TD>          
                    <td colspan="3" class="CellContent"><%=strCustomerName%></td>
                 </tr>
                 <tr>
                    <td class="CellLabel"><font class="Required">* </font>Solución de Negocio</TD>
                    <td class="CellContent">
                        <select name="cmbSolucionNegocio" onchange="javascript:fx_fillProduct(this.value);document.formdatos.hdnSolucionName.value=this[this.selectedIndex].text">                        
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>                                  
                       </select>  
                        <input type="hidden" name="hdnSolucionName" >
                    </TD>
                    <td class="CellLabel"><font class="Required">* </font>Aplicación</TD>
                    <td class="CellContent">
                       <select name="cmbAplicacion" onBlur="javascript:document.formdatos.hdnAplicacionName.value=this[this.selectedIndex].text" >                        
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>                                   
                       </select>     
                       <input type="hidden" name="hdnAplicacionName" >
                    </td>
                 </tr>
              </table>
              </center>
           </td>
        </tr>
     </table>

     <table border="0" cellspacing="0" cellpadding="0"><tr><td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="5"></td></tr></table>

     <table width="99%" align="center">
        <tr>
           <td align="center">
              <input type="button" value="   Guardar   " name="btnSubmit" onclick="javascript:fxSubmit()">
              <input type="button" value="   Cancelar   " name="btnCancel" onclick="javascript:fxCheckOnClose()">
           </td>
        </tr>
     </table>
     <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
           <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="6"></td>
        </tr>
     </table>
  </FORM>
  
  <script LANGUAGE="JavaScript">
   <%if ( arrDataList != null && arrDataList.size() > 0 ){%>
           fxLoadSalesData();
   <%}%>
   <%if(lSalesDataid!=0){%>
        fxLoadSalesEditData();
   <%}%>
   
   function fxLoadSalesData(){
      form = document.formdatos;
      <% if ( arrDataList != null && arrDataList.size() > 0 ){
          objItemBean = null;
          for( int i=0; i<arrDataList.size();i++ ){
                objItemBean   = new ItemBean();
                objItemBean = (ItemBean)arrDataList.get(i);
                System.out.println("objItemBean.getNpproductlinename():"+objItemBean.getNpproductlinename());
      %>
           vctSalesData.addElement(new fxMakSaleseData('<%=objItemBean.getNpsolutionid()%>','<%=objItemBean.getNpproductlineid()%>','<%=objItemBean.getNpproductlinename()%>','<%=objItemBean.getNpproductid()%>','<%=objItemBean.getNpproductname()%>'));
       <% }
       %>
       //Cómo solo se configurará a la Solucion Data(12) obtendremos la soluticón del último registro
       //--------------------------------------------------------------------------------------------
       form.hdnSolucionId.value = "<%=objItemBean.getNpsolutionid()%>";
       fx_fillElementsVectorData();
       fx_fillProductLine();
       
      <%
      } 
      %>
   }
   
   function fxLoadSalesEditData(){
      form = document.formdatos;
      SetCmbDefaultValue(form.cmbSolucionNegocio,'<%=lproductlineid%>');
      fx_fillProduct('<%=lproductlineid%>');
      SetCmbDefaultValue(form.cmbAplicacion,'<%=lproductid%>');
      form.hdnSolucionName.value = "<%=strProducLineName%>";
      form.hdnAplicacionName.value = "<%=strProductName%>";
   }
 </script>

 
<% 
}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  
</body>
</html>