<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityParticipantBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%
try{
    Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");  
    if (hshParam==null) hshParam=new Hashtable();
    long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
    long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));       
    long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));         
    String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));  
    
    String strDocument =MiUtil.getString((String)hshParam.get("strDocument"));
    String strTypeDocument =MiUtil.getString((String)hshParam.get("strTypeDocument"));

    // MMONTOYA [ADT-FMO-088 Fijo Móvil Fase I]
    int specificationId = Integer.parseInt((String)hshParam.get("strSpecificationId"));
    int divisionId = Integer.parseInt((String)hshParam.get("strDivisionId"));
    
    PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    String strLogin=objPortalSesBean.getLogin();
    
    String strMessage=null;
    ArrayList arrassignortList = null;
    HashMap hshAssignor = new HashMap();
    PortabilityParticipantBean objParticipantBean = null;
    
    PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
    arrassignortList = new ArrayList();
    hshAssignor = (HashMap)objPortabilityOrderService.getParticipantList(specificationId, divisionId);
    arrassignortList = (ArrayList)hshAssignor.get("objParticipantList");
    int cont = 0;
    
   
    
     //Obtiene los valores del combo Documentos
    //-----------------------------------------
    HashMap hshDocument = objPortabilityOrderService.getDocumentList("DOCUMENT_CUSTOMER"); 
    strMessage=(String)hshDocument.get("strMessage");
    if (strMessage!=null)
      throw new Exception(strMessage);                               
    ArrayList arrDocument=(ArrayList)hshDocument.get("arrDocumentList"); 
    
    
    
  
%>

   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   <script type="text/javascript" defer>
      
      function fxSectionPortabilityHighValidate(){
        return true;
      }
      
      function fxSectionPortHighHeaderValidate(){
         var vForm = document.frmdatos;
         if( vForm.cmbAssignor.value == ""){
            alert("Debe ingresar el Cedente ");
            vForm.cmbAssignor.focus();
            return false;  
         }
         return true;
      }
      
     function getShowDocumment (strDocument,strTypeDocument){
     
         var vForm = document.frmdatos; 
         v_typedoc = form.cmbDocumento.value;
         
         if(v_typedoc==""){
          alert("Debe ingresar un tipo de documento");
          vForm.cmbDocumento.value =strTypeDocument;
        }
         
         if(strTypeDocument==vForm.cmbDocumento.value ){
            vForm.txtDocumento.value =strDocument;
            vForm.cmbDocumento.value =strTypeDocument;
            vForm.txtDocumento.disabled=true; 
         }else{
            vForm.txtDocumento.value = "";
            vForm.txtDocumento.disabled=false;
          }
      }
      
      function fxValidateDocument(objValue,strDocument,strTypeDocument){
        form = document.frmdatos;
        v_typedoc = form.cmbDocumento.value;
        if ((objValue.length)!=0){
          if( (objValue.length)!=8 && (v_typedoc=="DNI") ){
              alert("Debe ingresar un numero de 8 digitos")
              if(strTypeDocument != v_typedoc){
                form.txtDocumento.value="";
              }else{
                form.txtDocumento.value=strDocument;
              }  
          }
          if ( (objValue.length)!=11 && (v_typedoc=="RUC") ){
            alert("Debe ingresar un numero de 11 digitos")
            if(strTypeDocument != v_typedoc){
                form.txtDocumento.value="";
            }else{
                form.txtDocumento.value=strDocument;
            }  
          }
        }else{
          alert("Debe ingresar un numero de documento");
          form.txtDocumento.focus();
          return false;        
        }
        
        return true;
      }
      
   </script>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
      <tr>
        <td align="left">
          <table border="0" cellspacing="0" cellpadding="0" align="left">
            <tr>
              <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
              <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Alta</td>
              <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
      <td>
        <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">      
          <tr>				            
          <td class="CellLabel" align="center" width="25%">Nombre Cedente</td>
          <td class="CellContent" align="left" width="25%">
              <select name = "cmbAssignor">
                <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                <%
                  if(arrassignortList.size() > 0){
                    for(int i=0; i<arrassignortList.size(); i++){
                      objParticipantBean = new PortabilityParticipantBean();
                      objParticipantBean = (PortabilityParticipantBean)arrassignortList.get(i);  
                %>
                <option value="<%=objParticipantBean.getNpParticipantId()%>"><%=objParticipantBean.getNpDescripcion()%></option>
                <%  }
                  }
                %>
              </select>
            </td>
          <td class="CellLabel" align="center" width="25%"></td> 
          <td class="CellContent" align="left" width="25%"></td>  
          </tr>
          <tr height="20px">                        
            <td class="CellLabel" align="center" width="25%">Tipo de Documento</td>
            <td class="CellContent" align="left" width="25%">
             <select name = "cmbDocumento" onchange="javascript:getShowDocumment('<%=strDocument%>','<%=strTypeDocument%>');" >
                <%= MiUtil.buildComboSelected(arrDocument,"swvalue","swdescription",strTypeDocument)%>
             </select></td>
            <td class="CellLabel" align="center" width="25%">Número de Documento</td>
            <td class="CellContent" align="center" width="25%">
             <input type="text" name="txtDocumento" size="15"  onchange="javascript:fxValidateDocument(trim(this.value),'<%=strDocument%>','<%=strTypeDocument%>');"  onKeyPress="return AcceptNumber(event)"  value="<%=strDocument%>" disabled></td> 
             <input type=hidden name='hdnDocumento'  value="<%=strDocument%>">
          </tr>
         
        </table>
      </td>
      </tr>
    </table>
<%
} catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
      System.out.println("    " + e.getStackTrace()[i] + "<br>");
	  }
}%>