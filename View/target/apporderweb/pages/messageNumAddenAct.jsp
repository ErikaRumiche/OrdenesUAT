<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.Constante" %>
<html>
  <head>
    <script>        
      <%
      String numAddenAct = (String)request.getAttribute("numAddenAct");
      //EFLORES REQ-0428_2 Agrega la orden para auditoria
      String strOrderId = (String)request.getAttribute("strOrderId");
      String strNextelNum = (String)request.getAttribute("strNextelNum");
      String msgPenaltyShow = "El contrato tiene adendas vigentes, le recomendamos verificarlas";

      System.out.println("[messageNumAddenAct.jsp] orderId = "+strOrderId+" nextelNum = "+strNextelNum+" numAddenAct = "+numAddenAct+" - msgShowPenaltySim = "+msgPenaltyShow);
      if(numAddenAct != null){
          if (!(("0").equals(numAddenAct))){%>
            //DERAZO REQ-0428
            if (parent.opener.flagPenaltyFunct == 1){
              <%
                System.out.println("[messageNumAddenAct.jsp] Existe adenda activa, guardando en h_penalidad para = "+strOrderId+"nextelNum = "+strNextelNum);
              %>
              parent.mainFrame.document.getElementById("h_penalidad").value = "1";
              parent.mainFrame.document.getElementById("v_penalidad").style.display = "inline-block";

              if (parent.opener.arrMsgPenaltyFunct[1] == null){
                  alert("<%=msgPenaltyShow%>");
              }
              else{
                  alert(parent.opener.arrMsgPenaltyFunct[1]);
              }
            }
            else{
              alert("El contrato tiene <%= numAddenAct%> adendas vigentes, le recomendamos verificarlas.");
            }
        <%}
      }
      %>  
      var url = "<%=Constante.PATH_APPORDER_SERVER%>" + "/Bottom.html";      
      location.replace(url);
    </script>
  </head>
  <body>
  </body>
</html>
