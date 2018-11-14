<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.bean.ProposedBean"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
    //EFH001 Añadido Código para control de sesión.
    System.out.println("--------- PopUpProposedList.jsp ---------");
    String urlIngreso = request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : "");
    String jspIngreso = "PopUpProposedList.jsp";
    String ipIngreso =  request.getRemoteAddr();
    String  contexto = request.getContextPath();
    //FIN
    String strCustomerId =(request.getParameter("customerId")==null?"":request.getParameter("customerId"));
    String strSiteId=(request.getParameter("siteId")==null?"":request.getParameter("siteId"));
    String strSpecificationId=(request.getParameter("specificationId")==null?"":request.getParameter("specificationId"));
    String strSellerId=(request.getParameter("sallerId")==null?"":request.getParameter("sallerId"));
    String strMessage=null;
    NewOrderService objNewOrderService= new NewOrderService();
    HashMap hshProposedList= objNewOrderService.getProposedList(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSiteId),MiUtil.parseLong(strSpecificationId),
                                                                MiUtil.parseLong(strSellerId));
    if((String)hshProposedList.get(Constante.MESSAGE_OUTPUT)!=null)
      strMessage=(String)hshProposedList.get(Constante.MESSAGE_OUTPUT);   
   %>
   <%if(strMessage!=null){%>
      <script>alert('<%=strMessage%>');</script>
   <%}%>
   <%
   if(strMessage==null){
    ArrayList arrProposedList= (ArrayList)hshProposedList.get("objArrayList");
    request.setAttribute("arrProposedList",arrProposedList);
   }
   %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ORDENES > LISTA DE PROPUESTAS</title>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/></link>
    <link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript">
        //EFH001 Añadido Código para control de sesión.
        if (parent.opener==undefined||parent.opener== null){

            window.location.replace("<%=contexto%>"+"/htdocs/jp_session_validate/Session_Validate.jsp"+"?urlIngreso="+"<%=urlIngreso%>"+"&jspIngreso="+"<%=jspIngreso%>"+"&ipIngreso="+"<%=ipIngreso%>");
        }
        //FIN
     function fxChooseProposedId(proposedId){         
         opener.document.getElementById("txtPropuesta").value = proposedId;         
          parent.close();
     }
    </script>
  </head>  
  <body>
  <div style="text-align:center;" class="SectionTitle">LISTADO DE PROPUESTAS</div>
  <br/>
  <form name="frmProposedlist" id="frmProposedlist">
     <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
      <tr>
        <td>
         <%if(strMessage==null){%>
            <display:table id="row" class="RegionBorder" name="arrProposedList" style="width:100%">                                                   
              <display:column  title="<div class='CellHeader' style='text-align:center;vertical-align:top;'>N°</div>">
                <a href='javascript:fxChooseProposedId(<%=((ProposedBean) row).getNpproposedid()%>)'>
                  <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Elija la propuesta" width="15" height="15" border="0">
                </a>
                <span class="CellContent"><%=((ProposedBean) row).getNpindice()%></span>
              </display:column>            
              <display:column title="<div class='CellHeader' style='text-align:center;vertical-align:top;'>COD. PROPUESTA</div>" class="CellContent" style="text-align:center;" >
                <%=((ProposedBean) row).getNpproposedid()%>
              </display:column>              
              <display:column title="<div class='CellHeader' style='text-align:center;vertical-align:top;'>TIPO</div>" class="CellContent">
                <%=((ProposedBean) row).getNpproposedtype()%>
              </display:column>
              <display:setProperty name="basic.empty.showtable" value="true"/>
              <display:setProperty name="basic.msg.empty_list" value="<tr class='CellContent'><td colspan='3'>No existen propuestas.</td></tr>"/>
              <display:setProperty name="basic.msg.empty_list_row" value="<tr class='CellContent'><td colspan='3'>No existen propuestas.</td></tr>"/>
            </display:table>
         <%}%>
        </td>
      </tr>
    </table>
</form>
  </body>
</html>

