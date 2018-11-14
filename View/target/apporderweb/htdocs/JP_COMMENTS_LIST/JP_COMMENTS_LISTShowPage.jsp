<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.CommentBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.OrderTabsService" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
try {
	String strOrderId = StringUtils.defaultString(request.getParameter(Constante.PARAM_ORDER_ID),"0");
	long lOrderId = Long.parseLong(strOrderId);
	System.out.println(Constante.PARAM_ORDER_ID+": "+strOrderId);
	OrderTabsService objOrderTabsService = new OrderTabsService();
	ArrayList arrCommentsList = objOrderTabsService.getCommentByOrderList(lOrderId);
    System.out.println("Tamaño del ArrayList: " + arrCommentsList.size());
	if(arrCommentsList!=null) {
		request.setAttribute("arrCommentsList", arrCommentsList);
	}
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<style>
	table.orders {
		border: 0px;
		width: 100%;
		margin: 1px 0 1px 0 !important;
		font-weight: normal;
		font-size: 8pt;
		color: #000000;
		font-family: Arial, Helvetica; 
	}
	th {
		padding: 2px 4px 2px 4px !important;
		text-align: left;
		vertical-align: top;
		font-weight: bold;
	}
	thead tr {
		background-color: #E8E4B7
	}
	tr.odd {
		background-color: #F5F5EB
	}
	tr.even {
		background-color: #E8E4B7
	}
	.spanPaging {
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
		color: #000000;
	}
	span.excel {
		background-image: url(../../websales/img/ico_file_excel.png);
	}
	span.csv {
		background-image: url(../../websales/img/ico_file_csv.png);
	}
	span.xml {
		background-image: url(../../websales/img/ico_file_xml.png);
	}
	span.pdf {
		background-image: url(../../websales/img/ico_file_pdf.png);
	}
	span.rtf {
		background-image: url(../../websales/img/ico_file_rtf.png);
	}
	span.pagebanner {
		background-color: #eee;
		border: 1px dotted #999;
		padding: 2px 4px 2px 4px;
		width: 79%;
		margin-top: 10px;
		display: block;
		border-bottom: none;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
	div.exportlinks {
		background-color: #eee;
		border: 1px dotted #999;
		padding: 2px 4px 2px 4px;
		margin: 2px 0 10px 0;
		width: 79%;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
	span.export {
		padding: 0 4px 1px 20px;
		display: inline;
		display: inline-block;
		cursor: pointer;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
</style>
<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
	
    <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td>
	
		<table cellspacing="0" cellpadding="0" border="0" width="100%" height="20" align="center">
            <tr>
                <td valign="BOTTOM" class="CellContent">
                    <a href="/portal/page/portal/orders/ORDER_ADD_COMMENT?an_nporderid=<%=lOrderId%>">
                        <img src="/websales/images/crear.gif" border="0" alt="Agregar">
                    </a>
                </td>
            </tr>
		</table>
	
		<display:table id="comment" name="arrCommentsList" class="orders" pagesize="100" style="width: 100%">
			<display:column property="npCommentId" title="Id"/>
			<display:column property="npAction" title="Acción"/>
			<display:column property="npSubject" title="Asunto"/>
			<display:column title="Notas">
				<%	String strComment = ((CommentBean) comment).getNpComment(); 
					strComment = StringUtils.replace(strComment, "<", "&lt;");
					strComment = StringUtils.replace(strComment, ">", "&gt;");
					strComment = StringUtils.replace(strComment, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
					strComment = StringUtils.replace(strComment, "\n", "<br/>");
					out.println(strComment);
				%>
			</display:column>
			<display:column property="npCreatedBy" title="Creado por"/>
			<display:column property="npCreatedDate" title="Fecha de Creación"/>
		</display:table>
	
    </td></tr></table>
    
</form>
<script type="text/javascript">
    
</script>
<%} catch(Exception e) {
	e.printStackTrace();
    out.println("Mensaje::::" + e.getMessage() + "<br>");
    out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++)
        out.println("    " + e.getStackTrace()[i] + "<br>");
}%>