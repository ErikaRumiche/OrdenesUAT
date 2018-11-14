<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
try {

	String strOrderId = StringUtils.defaultString(request.getParameter(Constante.PARAM_ORDER_ID),"0");
	long lOrderId = Long.parseLong(strOrderId);
   //lOrderId = 123456;
	OrderTabsService objOrderTabsService = new OrderTabsService();
   ArrayList arrInvoiceToShowList=new ArrayList();     
   String strMessage=null;
   
   //----------------------------------------INICIO SECCION FACTURAS----------------------------------------
   HashMap hshInvoiceToShowMap = new HashMap();
   //Trayendo el listado de facturas
   //lOrderId=2004687;
	HashMap hshInvoiceMap = objOrderTabsService.getInvoicetoShowList(lOrderId);   
   strMessage=(String)hshInvoiceMap.get("strMessage");
   arrInvoiceToShowList = (ArrayList) hshInvoiceMap.get("arrInvoiceToShowList");
   if (strMessage!=null)
      throw new Exception(strMessage);  

   GeneralService objGenServ = new GeneralService();
   HashMap objHashMap=objGenServ.getNameServerReport(Constante.REPORT,Constante.URL_SERVER_REPORT);
   strMessage=(String)objHashMap.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   String strNameServerReport=(String)objHashMap.get("strNameServerReport");
   System.out.println("Nombre de la impresora: "+strNameServerReport);
   
   //----------------------------------------FIN SECCION FACTURAS----------------------------------------
   
   //----------------------------------INICIO SECCION GUIAS DE REMISION-----------------------------------
   HashMap hshInventoryToShowMap = new HashMap();

   //Trayendo el listado de guias
   hshInventoryToShowMap = objOrderTabsService.getInventoryOrder(lOrderId);   
   strMessage=(String)hshInventoryToShowMap.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);  
   ArrayList arrInventoryToShowList = (ArrayList) hshInventoryToShowMap.get("arrInventoryList");
   if (arrInventoryToShowList==null) arrInventoryToShowList=new ArrayList();   
  //----------------------------------------FIN SECCION GUIAS DE REMISION----------------------------------------
  
  //----------------------------------INICIO SECCION PARTES DE INGRESO-----------------------------------
   HashMap hshInventoryPIToShowMap = new HashMap();

   //Trayendo el listado de guias
   hshInventoryPIToShowMap = objOrderTabsService.getInventoryPIOrder(lOrderId);   
   strMessage=(String)hshInventoryPIToShowMap.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage);  
   ArrayList arrInventoryPIToShowList = (ArrayList) hshInventoryPIToShowMap.get("arrInventoryPIList");
   if (arrInventoryPIToShowList==null) arrInventoryPIToShowList=new ArrayList();   
  //----------------------------------------FIN SECCION GUIAS DE REMISION----------------------------------------
   

	 request.setAttribute("arrInvoiceToShowList", arrInvoiceToShowList);
    request.setAttribute("arrInventoryToShowList", arrInventoryToShowList);
    request.setAttribute("arrInventoryPIToShowList", arrInventoryPIToShowList);
   
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>

<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">

<!--table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
<tr><td>
<display:table name="arrInvoiceToShowList" class="orders" export="true" pagesize="100" style="width: 100%"/>
</td></tr>
</table-->

<table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
<tr><td class="SectionTitle" colspan="9" align="center">Listado de Facturas</td></tr>
<tr><td>
<display:table id="orders" name="arrInvoiceToShowList" class="orders" pagesize="100" export="true" style="width: 100%">       
   <display:column property="NPSALETRXID" title="id"/>
   
   <display:column title="Comprobante Generado" style="white-space: nowrap;" media="html">
      <a href='javascript:fxViewInvoice("<%=((HashMap) orders).get("NPSALETRXID")%>","<%=((HashMap) orders).get("NPSALETRXCODETYPE")%>","<%=((HashMap) orders).get("NPFELPREFIX")%>")'>
       <%=((HashMap) orders).get("NPSALETRXNUMBER")%></a>      
   </display:column>
   
   <display:column property="BUILDINGNAME" title="Tienda"/>
   <display:column property="NPCREATIONDATE" title="Fecha de Creación"/>      
   <display:column property="NPORDERINVOICEAMOUNT" title="Monto de Venta"/>
   <display:column property="NPCURRENCYCODTYPE" title="Tipo Moneda"/>
   <display:column property="NPCUSTOMERNAME" title="Nombre del Cliente"/>
   <display:column property="NPCUSTOMERTAXNUMBER" title="RUC"/>   
   <display:column property="NPSALETRXTYPE" title="Tipo Documento"/>   
   <display:column property="NPSTATUSDOC" title="Estado del Documento"/>
   <display:column property="NPPRINTSTATUS" title="Estado de Impresión"/>
   <display:column property="NPPAYMENTMETHOD" title="Forma de Pago"/>
   <display:column property="NPSTATUSSUNAT" title="SUNAT" />
</display:table>
</td></tr>
</table>

<table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
<tr><td class="SectionTitle" colspan="6" align="center">Listado de Guías</td></tr>
<tr><td>
<display:table id="orders2" name="arrInventoryToShowList" class="orders" pagesize="100" export="true" style="width: 100%">
   
   
   <display:column property="NPTRANSACTIONID" title="id"/>
   
   <display:column title="N° Documento" style="white-space: nowrap;" media="html">
      <a href='javascript:fxViewInvoice("<%=((HashMap) orders2).get("NPTRANSACTIONID")%>","<%=((HashMap) orders2).get("NPSALETRXTYPE")%>","<%="null"%>")'>
       <%=((HashMap) orders2).get("NPTRANSNNUMBER")%>

      </a>
   </display:column>
         
   <display:column property="NPTRANSACTIONDATE" title="Fecha Transacción"/>
   <display:column property="NPCUSTOMERNAME" title="Nombre del Cliente"/>
   <display:column property="NPCUSTOMERTAXNUMBER" title="RUC"/>
   <display:column property="WV_NPDOCUMENTPARENTTYPENAME" title="Tipo Documento"/>
   <display:column property="NPVALUEDESC" title="Estado del Documento"/>
   <display:column property="STATUSPRINTER" title="Estado de Impresión"/>
   <display:column property="NPOPERATIONTYPEID" title="Tipo Operación"/>   
</display:table>
</td></tr>
</table>

 <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
<tr><td class="SectionTitle" colspan="6" align="center">Listado de Partes de Ingreso</td></tr>
<tr><td>
<display:table id="orders3" name="arrInventoryPIToShowList" class="orders" pagesize="100" export="true" style="width: 100%">
   
   <display:column property="NPTRANSACTIONID" title="id"/>
   
   <display:column title="N° Documento" style="white-space: nowrap;" media="html">
      <a href='javascript:fxViewInvoice("<%=((HashMap) orders3).get("NPTRANSACTIONID")%>","<%=((HashMap) orders3).get("NPSALETRXTYPE")%>","<%="null"%>")'>
       <%=((HashMap) orders3).get("NPTRANSNNUMBER")%></a>      
   </display:column>        
   <display:column property="NPTRANSACTIONDATE" title="Fecha Transacción"/>
   <display:column property="NPCUSTOMERNAME" title="Nombre del Cliente"/>
   <display:column property="NPCUSTOMERTAXNUMBER" title="RUC"/>
   <display:column property="WV_NPDOCUMENTPARENTTYPENAME" title="Tipo Documento"/>
   <display:column property="NPVALUEDESC" title="Estado del Documento"/>
   <display:column property="STATUSPRINTER" title="Estado de Impresión"/>
   <display:column property="NPOPERATIONTYPEID" title="Tipo Operación"/>   
</display:table>
</td></tr>
</table>
    
</form>
<script type="text/javascript">
 
 function  fxViewInvoice(strSalesId,strCodeTypeDoc,strFelprefix){
    var v_url_rpt;    
    iCodeTypeDoc=parseInt(strCodeTypeDoc);
    if(strFelprefix!=null && strFelprefix!="null"){
        switch(iCodeTypeDoc){
          case <%=Constante.TYPE_DOC_INVOICE_FACT%>:
                    v_url_rpt = "rp_sale_trx_fel_pdf";
                    break;
          case <%=Constante.TYPE_DOC_INVOICE_BOLE%>:
                    v_url_rpt = "rp_sale_trx_fel_pdf";
                    break;
          case <%=Constante.TYPE_DOC_INVOICE_ND%>:
                    v_url_rpt = "rp_sale_trx_fel_pdf";
                    break;
          case <%=Constante.TYPE_DOC_INVOICE_NC%>:
                    v_url_rpt = "rp_sale_trx_fel_pdf";
                    break;
        }
    }else{
    switch(iCodeTypeDoc){
      case <%=Constante.TYPE_DOC_INVOICE_FACT%>:  
                v_url_rpt = "np_sale_trx_v1";
                break;                                       
      case <%=Constante.TYPE_DOC_INVOICE_BOLE%>:  
                v_url_rpt = "np_sale_trx_v1";
                break;         
      case <%=Constante.TYPE_DOC_INVOICE_ND%>:  
                v_url_rpt = "np_sale_trx_v1";
                break;        
      case <%=Constante.TYPE_DOC_INVOICE_NC%>:  
                v_url_rpt = "np_sale_trx_v1";
                break;
      case <%=Constante.TYPE_DOC_INVOICE_GR%>:  
                v_url_rpt = "np_inv_rep_guia_remision";
                break;
      case <%=Constante.TYPE_DOC_INVOICE_PI%>:  
                v_url_rpt = "np_sr_inv_parte_ingreso";
                break;   
    }
    }

    var v_url = "<%=strNameServerReport%>"+v_url_rpt+"&p_id_trx="+strSalesId;
    window.open(v_url);         
 }  
    
</script>
<%} catch(Exception ex) {

System.out.println("Error JP_DOCS_INVENTORY_LIST-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>