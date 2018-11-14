<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<%
    String strUrl=MiUtil.getString(request.getParameter("sUrl"));
    System.out.println("strUrl ORIGINAL-->"+strUrl);
    /*String strPagoBanco=MiUtil.getString(request.getParameter("hdnPagoBanco"));
    //System.out.println("strPagoBanco-->"+strPagoBanco);
    String strPagoNroVoucher=MiUtil.getString(request.getParameter("hdnPagoNroVoucher"));
    //System.out.println("strPagoNroVoucher-->"+strPagoNroVoucher);
    String strPagoImp=MiUtil.getString(request.getParameter("hdnPagoImporte"));
    //System.out.println("strPagoImp-->"+strPagoImp);
    String strPagoFecha=MiUtil.getString(request.getParameter("hdnPagoFecha"));
    //System.out.println("strPagoFecha-->"+strPagoFecha);
    String strPagoDisp=MiUtil.getString(request.getParameter("hdnPagoDisponible"));
    //System.out.println("strPagoDisp-->"+strPagoDisp);
    String strRuc=MiUtil.getString(request.getParameter("hdnRuc"));
    //System.out.println("strRuc-->"+strRuc);
    String strImpFactura=MiUtil.getString(request.getParameter("txtImporteFactura"));
    //System.out.println("strImpFactura-->"+strImpFactura);
    String strTotalPayOrig=MiUtil.getString(request.getParameter("hdnTotalPaymentOrig"));    
    //System.out.println("strTotalPayOrig-->"+strTotalPayOrig);
    
    strUrl="../"+strUrl.trim()+"?hdnPagoBanco="+strPagoBanco+"&hdnPagoNroVoucher="+strPagoNroVoucher
    +"&hdnPagoImporte="+strPagoImp+"&hdnPagoFecha="+strPagoFecha+"&hdnPagoDisponible="+strPagoDisp
    +"&hdnRuc="+strRuc+"&txtImporteFactura="+strImpFactura+"&hdnTotalPaymentOrig="+strTotalPayOrig;
    */
    System.out.println("strUrl 1-->"+strUrl);
    
    //strUrl=strUrl.substring(strUrl.indexOf(".jsp")+4);
    strUrl=strUrl.replace('¿','?');
    strUrl=strUrl.replace('|','&');
    System.out.println("strUrl 2-->"+strUrl);
    //strUrl="../"+strUrl;
   /* System.out.println("strUrl 3-->"+strUrl);*/
     //   System.out.println("strUrl-->"+strUrl);
%>

<HTML>

<FRAMESET border=0 frameSpacing=0 rows=99%,* frameBorder=NO>
  <FRAME name=mainFrame src='<%=strUrl%>' noResize>
  <FRAME name=bottomFrame src="../websales/Bottom.html" noResize scrolling=no>
</FRAMESET>
</HTML>