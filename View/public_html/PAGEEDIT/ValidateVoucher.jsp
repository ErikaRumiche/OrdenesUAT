<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<%

  int iStatus=(request.getParameter("nStatus")==null?0:Integer.parseInt(request.getParameter("nStatus")));
  String strMessage=(request.getParameter("sMensaje")==null?"":request.getParameter("sMensaje"));
  /*long lImpDispnbl=(request.getParameter("nImpDisp")==null?0:MiUtil.parseLong(request.getParameter("nImpDisp")));
  long lImpPagado=(request.getParameter("nImpPagado")==null?0:MiUtil.parseLong(request.getParameter("nImpPagado")));*/
  double lImpDispnbl=(request.getParameter("nImpDisp")==null?0.0:MiUtil.parseDouble(request.getParameter("nImpDisp")));
  double lImpPagado=(request.getParameter("nImpPagado")==null?0:MiUtil.parseDouble(request.getParameter("nImpPagado")));
  String strFecha=(request.getParameter("sFecha")==null?"":request.getParameter("sFecha"));
  
  System.out.println("iStatus-->"+iStatus);
  System.out.println("strMessage-->"+strMessage);
  System.out.println("lImpDispnbl-->"+lImpDispnbl);
  System.out.println("lImpPagado-->"+lImpPagado);
  System.out.println("strFecha-->"+strFecha);
  
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <title>Validate Voucher</title>
  </head>
  <body> 
  </body>
</html>

<script language="JavaScript">
  var status= "<%=iStatus%>";
  var importeDispon="<%=lImpDispnbl%>";
  var importePagado="<%=lImpPagado%>";
  var Form = parent.mainFrame.document.formdatos;

  if (status <= 0){
     alert("<%=strMessage%>");
     fxSetearValError();
  }else if(status == 1){
       if (importeDispon == 0) {         
            status=2;
            alert("El Voucher Ingresado ya fue usado para otros pagos");                           
           fxSetearValError();
      }else{       
         Form.txtImporte.value   =importePagado;
         Form.txtFecha.value     ="<%=strFecha%>";
         Form.txtDisponible.value=importeDispon;
         Form.btnAceptar.disabled=false;      

      }
  }

  Form.hdnStatus.value=status;
  
  function fxSetearValError(){
    Form.btnAceptar.disabled=true;
    Form.hdnStatus.value=status;
    Form.txtImporte.value   ="";
    Form.txtFecha.value     ="";
    Form.txtDisponible.value="";
    Form.txtNroVoucher.value="";
    location.replace("websales/Bottom.html");
  
  }
</script>