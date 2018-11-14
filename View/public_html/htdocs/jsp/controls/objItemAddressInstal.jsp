<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.service.GeneralService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.ArrayList" %>
<%@page import="pe.com.nextel.util.Constante" %>

<%
/*
Recuperando Parametros de Input
*/
  
  Hashtable hshtinputNewSection = null;
  GeneralService  objGeneralService = new GeneralService();
  String    strCustomerId= "",
            strSiteId ="",
            strCodBSCS = "",
            strSpecificationId = "",
            strDivisionId = "",
            //JOYOLA, 06/03/2008, se agrega la variable strSiteOppId
            strSiteOppId = "",
            strUnknwnSiteId = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  
  if ( hshtinputNewSection != null ) {
    strCustomerId           =   (String)hshtinputNewSection.get("strCodigoCliente");
    strSiteId               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    strSpecificationId      =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivisionId           =   (String)hshtinputNewSection.get("strDivision"); 
    strSiteOppId            =   (String)hshtinputNewSection.get("strSiteOppId");
    strUnknwnSiteId         =   (String)hshtinputNewSection.get("strUnknwnSiteId");
  }
  
  strUnknwnSiteId = strUnknwnSiteId.equals("null")?"":strUnknwnSiteId;
  strSiteOppId    = strSiteOppId.equals("null")?"":strSiteOppId;
  
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");  
  String strMessage = "";  
  String URL_Order_AddressInstalation     = request.getContextPath()+"/htdocs/jsp/Order_AddressInstal.jsp";
  
  System.out.println("4. objItemAddressInstal.jsp.strSiteId ===>"+strSiteId);
  System.out.println("4. objItemAddressInstal.jsp.strSiteOppId ===>" +strSiteOppId); 
  
%>

<script>
    function fxShowAddressInstalation(){       
       url= "<%=URL_Order_AddressInstalation%>?strSiteOppId=<%=strSiteOppId%>&strUnknwnSiteId=<%=strUnknwnSiteId%>&strCustomerId=<%=strCustomerId%>&strSpecificationId=<%=strSpecificationId%>";
       //var winUrl = "PopUpOrderFrame.jsp?av_url="+escape(url);
       //url  = escape(url);
       var popupWin = window.open(url, "WinAsist2001","status=yes, location=0, width=550, scrollbars=yes, height=400, left=300, top=30, screenX=50, screenY=100");
    }
</script>

<td class="CellLabel"   align="left">
<font color="red">&nbsp;*&nbsp;</font>
Direccion de Instalación</td>
<td class="CellContent" align="left">&nbsp;   
   <input type="hidden" name="txt_ItemAddressInstall" value="">
   <textarea readonly name="txt_ItemAddressInstallView" value="" rows="2" cols="40"></textarea>
   <a href="javascript:fxShowAddressInstalation();" onmouseover="window.status='Ver Detalle'; return true" onmouseout="window.status='';" >
   <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>      
</td>     

<script language="javascript">
    <%
    if( !type_window.equals("NEW") ){
      String[] paramNpobjitemheaderid = request.getParameterValues("a");
      String[] paramNpobjitemvalue    = request.getParameterValues("b");
       for(int i=0; i<paramNpobjitemheaderid.length; i++){
        if (("30").equals(paramNpobjitemheaderid[i])){

          String sAddressId   = paramNpobjitemvalue[i]==null?"0":paramNpobjitemvalue[i];
          
          if (! ("").equals(sAddressId))
          {
            int iAddressId = Integer.parseInt(sAddressId);
          
            HashMap hAddress = objGeneralService.getAddressPuntual(iAddressId, "DIRECCION");
            String AddressName= (String)hAddress.get("sswAddressName"); 
            strMessage= (String)hAddress.get("strMessage");
          
              if (strMessage==null){
              %>
                document.frmdatos.txt_ItemAddressInstallView.value='<%=AddressName%>';
              <%
              }else{
              %>
                alert(" [ObjItemAddressInstal.jsp ] <%=strMessage%>");
              <%
              }
           }
        }
       }
      }
    %>
</script>