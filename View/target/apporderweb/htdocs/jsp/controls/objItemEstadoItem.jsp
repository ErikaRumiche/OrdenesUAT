<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable" %>

<%
String nameHtmlItem                = (String)request.getParameter("nameObjectHtml");
HashMap objHashMap                 = null;
NewOrderService objNewOrderService = new NewOrderService();
ArrayList  objArrayList            = new ArrayList();
SpecificationBean objSpecificationBean  = null;
Hashtable         hshtinputNewSection   = null;
String            hdnSpecification      = "";

hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
if ( hshtinputNewSection!= null ){
    hdnSpecification     = (String)hshtinputNewSection.get("hdnSpecification");
}
%>  

  <script language="javascript">   
    var vctEstadoItem = new Vector();
            
    function fxMakeEstadoItem(objEstadoItemid, objEstadoItemName){
      this.objEstadoItemid         = objEstadoItemid;
      this.objEstadoItemName       = objEstadoItemName;
    }
    
    function fxOnloadListStatusItem(ObjectStatusItem){
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemEstado);
    
    <%
    //Encontramos la data para el Estado del Item
    //-------------------------------------
    objHashMap = objNewOrderService.getStatusItemList(Constante.ITEM_STATUS, Constante.UTILIZAR_DEFAULT, hdnSpecification);
    if( objHashMap.get("strMessage")!= null )
         throw new Exception((String)objHashMap.get("strMessage"));
    
    //Recogemos los registros que llenaran los datos en el combo Estado del Item
    //---------------------------------------------------------------------------------
      objArrayList = (ArrayList)objHashMap.get("objArrayList"); 
    
    //Llenamos los valores en el combo Solución
    //-----------------------------------------
    if (objArrayList != null && objArrayList.size()>0){
   
      for (int i=0; i<objArrayList.size(); i++){
          objSpecificationBean = new SpecificationBean();
          objSpecificationBean = (SpecificationBean )objArrayList.get(i);
      %>                 
          parent.mainFrame.vctEstadoItem.addElement(new parent.mainFrame.fxMakeEstadoItem('<%=objSpecificationBean.getNpEstadoItemId()%>','<%=objSpecificationBean.getNpEstadoItemName()%>'));
          parent.mainFrame.AddNewOption(formCurrent.cmb_ItemEstado,'<%=objSpecificationBean.getNpEstadoItemId()%>','<%=objSpecificationBean.getNpEstadoItemName()%>');
      
     <%     
       }//end del for    
     }//end del if
     %>
     
    }

  </script>
      
  <input type="HIDDEN" name="hdnOriginalProductId">
    <select name="<%=nameHtmlItem%>" > 
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    </select>
  <script>fxOnloadListStatusItem('<%=nameHtmlItem%>');</script>
