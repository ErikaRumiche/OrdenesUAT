<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>

<%
try {
  Hashtable hshtinputNewSection = null;
  String    strCustomerId= "",
            strnpSite ="",
            strItemId = "",
            hdnSpecification = "",
            strMessage = "";
  GeneralService objGeneralService = new GeneralService();
  HashMap hshSubscripActive = new HashMap();
  ArrayList list = new ArrayList();
  ServiciosBean objServiceBean = null;
  
  String strTelefono      =   StringUtils.defaultString(request.getParameter("txt_ItemPhone"),"");
  strCustomerId           =   MiUtil.getString((String)request.getParameter("strCodigoCliente"));
  strnpSite               =   MiUtil.getString(((String)request.getParameter("strSideId")).equals("0")?"":(String)request.getParameter("strSideId"));
  hdnSpecification        =   MiUtil.getString((String)request.getParameter("hdnSpecification"));
  strItemId               =   MiUtil.getString((String)request.getParameter("txt_ItemId"));
  
  //Invocamos al API de BSCS para obtener los servicios activos.
  System.out.println("strTelefono: "+strTelefono);
  System.out.println("strCodigoCliente: "+strCustomerId);
  System.out.println("hdnSpecification: "+hdnSpecification);
  System.out.println("strnpSite: "+strnpSite);
  System.out.println("strItemId: "+strItemId);
  
  hshSubscripActive = (HashMap)objGeneralService.getServiceActive(strTelefono,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(hdnSpecification),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strItemId));
  list = (ArrayList)hshSubscripActive.get("listSubscrip");
  strMessage  = (String)hshSubscripActive.get("strmessage");
  if (strMessage!=null){
    throw new Exception("[popUpDetailSuscripActivas.jsp][objGeneralService.getServiceActive]= "+strMessage);    
  }
  if(list.size()==0){
    strMessage = "No se encontraron servicios activos";
  }
  System.out.println("strMessage: "+strMessage);
    
%>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<title>Consulta de Suscripciones Activas para el teléfono: <%=strTelefono%> </title>

  <table border="0" cellspacing="0" cellpadding="0">
    <tr class="PortletHeaderColor">
       <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
       <td class="SubSectionTitle" align="LEFT" valign="top" width="230px">Suscripciones Activas - N° <%=strTelefono%></td>
       <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
    </tr>
  </table>
  
  <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder">
    <tr>
       <td class="RegionHeaderColor" width="100%">
          <table cellpadding="2" cellspacing="1" border="0" width="100%" align="center">
             <tr>
                <td class="CellLabel" valign="TOP" width="5%"><b>N°</b></td>
                <td class="CellLabel" valign="TOP" width="25%"><b>Descripción</b></td>
                <td class="CellLabel" valign="TOP" width="15%" align="center" ><b>Durac. (Días)</b></td>
                <td class="CellLabel" valign="TOP" width="15%" align="center" ><b>Faltan (Días)</b></td>
                <td class="CellLabel" valign="TOP" width="20%" align="center" ><b>Fec. Activación</b></td>
                <td class="CellLabel" valign="TOP" width="20%" align="center" ><b>Fec. Termino</b></td>
             </tr>
       <%
        if(list.size() > 0){                
            for(int j=0; j<list.size(); j++){
              objServiceBean = new ServiciosBean();
              objServiceBean = (ServiciosBean)list.get(j);
              int npposition = j + 1;
       %>
              <tr>
                <td class="CellContent" valign="TOP" width="5%"><%=npposition%></td>
                <td class="CellContent" valign="TOP" width="25%"><%if(objServiceBean.getNpnomserv()==null)%><%=""%><%else%><%=objServiceBean.getNpnomserv()%></td>
                <td class="CellContent" valign="TOP" width="15%" align="center" ><%if(objServiceBean.getNpduration()==0)%><%=""%><%else%><%=objServiceBean.getNpduration()%></td>
                <td class="CellContent" valign="TOP" width="15%" align="center" ><font color="Red"><b><%if(objServiceBean.getNpmissingdays()==null)%><%=""%><%else%><%=objServiceBean.getNpmissingdays()%></b></font></td>
                <td class="CellContent" valign="TOP" width="20%" align="center" ><%if(objServiceBean.getNpsalesstartdate()==null)%><%=""%><%else%><%=MiUtil.getDate(objServiceBean.getNpsalesstartdate(),"dd/MM/yyyy")%></td>
                <td class="CellContent" valign="TOP" width="20%" align="center" ><%if(objServiceBean.getNpsalesenddate()==null)%><%=""%><%else%><%=MiUtil.getDate(objServiceBean.getNpsalesenddate(),"dd/MM/yyyy")%></td>
             </tr>
       <%
            }
       }else{
       %>
            <tr>
              <td class="CellContent" valign="TOP" width="100%" colspan="6"><%=strMessage%></td>
            </tr>
       <%
       }
       %>
         </table>
       </td>
    </tr>
  </table>

<%} catch(Exception e) {
	e.printStackTrace();
	out.println("<script>alert('"+e.getMessage()+"');</script>");
}%>