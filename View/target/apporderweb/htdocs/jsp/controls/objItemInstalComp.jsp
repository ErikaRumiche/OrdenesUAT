<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%
/*Recuperando Parametros de Input*/
  GeneralService  objGeneralService = new GeneralService();
  
  String type_window  = MiUtil.getString((String)request.getAttribute("type_window"));
  String strMessage   = null;  
  String url_page_SI  = request.getContextPath()+"/htdocs/jsp/PopUpAddressInstallFrame.jsp";
%>

<script>
  function fxShowSharedInstalation(){
    var url= "<%=url_page_SI%>";         
    var popupWin = window.open(url, "WinAsistAddressInstall","status=yes, location=0, width=700, scrollbars=yes, height=400, left=300, top=30, screenX=50, screenY=100");
  }
</script>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>

<td class="CellLabel"   align="left">
<font color="red">&nbsp;*&nbsp;</font>
Instalación Compartida</td>
<td class="CellContent" align="left">&nbsp;
   <input type="hidden" name="txt_ItemSharedInstall" value="">
   <textarea readonly name="txt_ItemSharedInstallView" value="" rows="2" cols="40"></textarea>   
   <a href="javascript:fxShowSharedInstalation();" onmouseover="window.status='Ver Detalle'; return true" onmouseout="window.status='';" >
   <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>      
</td>     

<script language="javascript">
  <%
  if( !type_window.equals("NEW") ){
    String[] paramNpobjitemheaderid = request.getParameterValues("a");
    String[] paramNpobjitemvalue    = request.getParameterValues("b");
      
     for(int i=0; i<paramNpobjitemheaderid.length; i++){
        if ( ("29").equals(paramNpobjitemheaderid[i]) ){
        
        if( !(MiUtil.getString(paramNpobjitemvalue[i]).equals("") || 
            MiUtil.getString(paramNpobjitemvalue[i]).equals("0") ) ){

          String sAddressId   = paramNpobjitemvalue[i];
          
          int iAddressId = Integer.parseInt(sAddressId);
          
          HashMap hAddress = objGeneralService.getAddressPuntual(iAddressId, "COMPARTIDA");
          String AddressName= (String)hAddress.get("sswAddressName"); 
          strMessage= (String)hAddress.get("strMessage");
          
          if (strMessage==null){
  %>
     document.frmdatos.txt_ItemSharedInstallView.value='<%=AddressName%>';
  <%
          }else{
  %>
     alert(" [ObjItemInstalComp.jsp ] <%=strMessage%>");
  <%
          }
        }
        }
     }   
   }
 %>
</script>