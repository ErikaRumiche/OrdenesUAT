<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.*" %>
<%
try{   
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   String strMessage=null;
   ArrayList arrPaymentList=null;   
   HashMap hshData=null;   
   PaymentBean objPaymentBean= null;
   EditOrderService objEditOrderService=new EditOrderService();   
   int iLongitud=0;
   
   hshData=objEditOrderService.getPaymentListBySource(Constante.PAYMENT_SOURCE,lOrderId);

   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)      
      throw new Exception(strMessage);       
   
   arrPaymentList=(ArrayList)hshData.get("arrPaymentList");
   
   if (arrPaymentList!=null) iLongitud=arrPaymentList.size();
   
   System.out.println(" ----------- INICIO PaymentList.jsp---------------- ");
   System.out.println("Constante.PAYMENT_SOURCE-->"+Constante.PAYMENT_SOURCE);
   System.out.println("nOrderId-->"+lOrderId);
   System.out.println("iLongitud-->"+iLongitud);         
   System.out.println(" ------------  FIN PaymentList.jsp----------------- ");       
%>
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>Listado de Pagos</title>
</head>
     <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
 
   <body>
      <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
      <tr class="PortletHeaderColor">
         <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
         <td class="PortletHeaderText" align="LEFT" valign="top">N&uacute;mero de Orden&nbsp;<%=lOrderId%> </td>
         <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
      </tr>  
      </table>    
      <table border="0"  width="100%"  class="RegionBorder">
         <tr>
            <td>             
               <table align=center width="100%" border="0" id="tabSite" name="tabSite" cellpadding="0" cellspacing="1" class="CTable">
               <tr align="center">                  
               <td class="CellLabel" width="5%">#</td>         
               <td class="CellLabel">Fecha</td>         
               <td class="CellLabel">Comprobante</td>
               <td class="CellLabel">Tienda</td>
               <td class="CellLabel">Nombre</td>
               <td class="CellLabel">RUC/DNI</td>               
               <td class="CellLabel">Monto Concepto</td>
               <td class="CellLabel">Concepto</td>               
               <td class="CellLabel">Id OP</td>                  
               <td class="CellLabel">Moneda</td>
               <td class="CellLabel">Pendiente OP</td>                                 
               <td class="CellLabel">Total OP</td>
               <td class="CellLabel">Caja</td>                                 
               
               </tr>    
               <% 
               for(int i=0; i<iLongitud;i++) {                                  
                  objPaymentBean = (PaymentBean)arrPaymentList.get(i);         
               %>
               <tr>                      
               <td class="CellContent"><%=i+1%></td> 
               <td class="CellContent">
               <%= MiUtil.toFecha(objPaymentBean.getNpCreatedDate())%></td>
               <td  class="CellContent">
               <%= MiUtil.getString(objPaymentBean.getVoucher())%></td>
               <td  class="CellContent">
                 <%= MiUtil.getString(objPaymentBean.getTienda())%>
               </td>
               <td  class="CellContent">
                 <%= MiUtil.getString(objPaymentBean.getNpName())%>
               </td>
               <td  class="CellContent">
               <%= MiUtil.getString(objPaymentBean.getNpRuc())%></td>         
               <td  class="CellContent">
               <%= MiUtil.formatDecimal(objPaymentBean.getMonto())%></td>         
               <td  class="CellContent"><%=MiUtil.getString(objPaymentBean.getNpConceptName())%></td>         
               <td  class="CellContent">
                 <%= MiUtil.getString(objPaymentBean.getNpPaymentorderId())%>
               </td>         
               <td  class="CellContent">
                 <%= MiUtil.getString(objPaymentBean.getMoneda())%>
               </td>         
               <td  class="CellContent">
                 <P>
                   <%= MiUtil.formatDecimal(objPaymentBean.getNpDeudaAmount())%>
                 </P>
               </td>         
               <td  class="CellContent">
                 <%= MiUtil.formatDecimal(objPaymentBean.getNpTotalAmount())%>
               </td>         
               <td  class="CellContent"><%=MiUtil.getString(objPaymentBean.getCaja())%></td>                  
               </tr>
               <%} %>    
               <%if(iLongitud==0){%>
               <tr>
               <td class="CellContent" valign="top" align="center" colspan="13"><b><font color="#FFOOOO">No existe Pagos</b></font></td>
               </tr>               
               <%}%>
               </table>  
            </td>            
         </tr>
         <tr>
            <table width="99%">
               <tr>
                  <td colspan="4" align="center">
                     <input type="button" name="btnClose" value="   Cerrar   " onclick="self.close();">
                  </td>
               </tr>
            </table>         
         </tr>
      </table>
   </body>
</html>
<%
}catch(Exception ex){
   System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%> 