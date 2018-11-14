<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="java.util.ArrayList" %>
<%
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision             =   (String)hshtinputNewSection.get("strDivision");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String idSpecificationId = (String)request.getAttribute("idSpecificationId");
  String strMessage = "";
  String strErrorLocal = "";
  
%>
    <select name="<%=nameHtmlItem%>" onchange="javascript:fxChangeObjectProductModel(this.value)" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                                  
     </select>
     
<script>

   function fxChangeObjectProductModel(objObjectValue){
      fxLoadProductFromLineInModel(objObjectValue);
   }
  
  function fxLoadProductFromLineInModel(objValue){
  
  
   var form = parent.mainFrame.frmdatos;
   var vDoc = parent.mainFrame;   
   var solution = "";  
   try {    
      solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;      
   }catch(e){}
   
  var valueCurrent = "";
  valueCurrent = objValue;
       
   if( objValue != "" ){
     var modality = form.cmb_ItemModality.value;
     var productlineid = form.cmb_ItemProductLine.value;
     var url = "<%=request.getContextPath()%>/itemServlet?strModality="+modality+"&strProductLineId="+productlineid+"&strModelId="+objValue+"&strSpecificationId=<%=hdnSpecification%>&strSolutionId="+solution+"&hdnMethod=getProducTypeList";
  
     parent.bottomFrame.location.replace(url);
   }else{
     parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProducto);
   
   }
   
  }
  
</script>


<%
if( !type_window.equals("NEW") ){
  String strModalidad    = "";
  String strProdLineId   = "";
  String strProductId    = "";
  String strSolutionId   = "";
  
  String[] paramNpobjitemvalue      = request.getParameterValues("b");
  String[] paramNpobjitemheaderid   = request.getParameterValues("a");
  
  for(int i=0;i<paramNpobjitemheaderid.length; i++){
    //Modalidad
    if( paramNpobjitemheaderid[i].equals("1") )
        strModalidad = paramNpobjitemvalue[i];
    //Product
    if( paramNpobjitemheaderid[i].equals("9") ) {
        strProductId = paramNpobjitemvalue[i];
    }
    //ProductLineId
    if( paramNpobjitemheaderid[i].equals("51") ) 
        strProdLineId = paramNpobjitemvalue[i];
    //Solución
    if( paramNpobjitemheaderid[i].equals("93") ) {
        strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);        
    }
  }

%>


<script>
    function fxLoadEditModelProduct(){
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProductModel);
    <%
       ProductBean objProductBean = new ProductBean();
       
       objProductBean.setNpproductlineid(MiUtil.parseLong(strProdLineId));
       objProductBean.setNpcategoryid(MiUtil.parseLong(hdnSpecification));
       
       HashMap objHashMap = objNewOrderService.getProductModelList(objProductBean);
       
       try {
          if (objHashMap == null ) { 
            throw new Exception("Surgieron errores al cargar los Modelos de Productos");
          } else if( (String)objHashMap.get("strMessage")!= null ) {
            throw new Exception(MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
           } 
       } catch (Exception e){}
         ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
       
       if ( objArrayList != null && objArrayList.size() > 0 ){
         
          for( int i=0; i<objArrayList.size();i++ ){
                objProductBean = new ProductBean();
                objProductBean = (ProductBean)objArrayList.get(i);
       %>
         parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductModel,'<%=objProductBean.getNpproductid()%>','<%=MiUtil.getMessageClean(objProductBean.getNpproductname())%>');
       <%
          }
       }
      %>
        
    }
</script>

<%}%>

