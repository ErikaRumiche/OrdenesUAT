<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
    <%  
      HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
      String strImei        = (String) hshDataMap.get("strImei");
      String strSim         = (String) hshDataMap.get("strSim");      
      String strPosition    = (String) hshDataMap.get("strPosition");      
      String strType        = (String) hshDataMap.get("strType");      
      String strMessage     = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
      
      if (StringUtils.isNotBlank(strMessage)){
          %>
            alert("<%=MiUtil.getMessageClean(strMessage)%>");
            if ( "<%=strType%>" == "SIM"  )
              parent.mainFrame.document.frmdatos.txtSims.focus();
            else
              parent.mainFrame.document.frmdatos.txtImeis.focus();
          <%          
      }else if (strType.equalsIgnoreCase("IMEI")){
          %>fxAddImei("<%=strImei%>", <%=MiUtil.parseInt(strPosition)%>);<%
      }else if (strType.equalsIgnoreCase("SIM")){
          %>fxAddSim("<%=strSim%>", <%=MiUtil.parseInt(strPosition)%>);<%
      }                    
     %>
      function fxAddImei(strImei, position){
        var vForm = parent.mainFrame.document.frmdatos;
        var table = parent.mainFrame.document.all ? parent.mainFrame.document.all["table_imeis"] : parent.mainFrame.document.getElementById("table_imeis");
        if (position >= 0){
          vForm.item_imei_imei[position].value = strImei;
          vForm.txtImeis.value="";
          vForm.hdnImeiChange[position].value="S";
          if (vForm.txtSims.readOnly){
            for (m=0; m<(table.rows.length - 1); m++){
              if (vForm.item_imei_imei[m].value==""){
                  vForm.item_imei_radio[m].checked = true;
                  vForm.txtImeis.focus();      
                  vForm.hdn_item_imei_selecc.value=m;
                  break;
               }
             }
          }else
            vForm.txtSims.focus();
        }
        else{
          vForm.item_imei_imei.value = vForm.txtImeis.value;        
          vForm.txtImeis.value="";
           vForm.hdnImeiChange.value="S";
        }
        
        try{
          vForm.hdnChangedOrder.value="S";
        }catch(e){;}
        
        return true;
      }
      
      function fxAddSim(strSim, position){
        var vForm = parent.mainFrame.document.frmdatos;
        var grid_region=parent.mainFrame.document.getElementById("grid_region");
        var table = parent.mainFrame.document.all ? parent.mainFrame.document.all["table_imeis"] : parent.mainFrame.document.getElementById("table_imeis");
        //alert(vForm.hdn_item_imei_selecc.value);
        if (position >= 0){
          vForm.item_imei_sim[position].value = strSim;           
          vForm.txtSims.value="";
          vForm.hdnSimChange[position].value="S";
          for (m=0; m<(table.rows.length - 1); m++){
             if (vForm.item_imei_sim[m].value==""){
               vForm.item_imei_radio[m].checked = true;                             
               vForm.hdn_item_imei_selecc.value=m;
               break;
              }
          }
        }else{
          vForm.item_imei_sim.value = strSim;
          vForm.txtSims.value="";
          vForm.hdnSimChange.value="S";
        }
        if (vForm.item_imei_sim.value == ""){
                 vForm.item_imei_sim.value = strSim.value;
                 grid_region.scrollTop=table.childNodes.item(0).childNodes.item(1).childNodes.item(0).offsetTop;
           }
        else{
              //alert(table.rows.length);
              if (table.rows.length >2){             
                if (vForm.item_imei_sim[vForm.hdn_item_imei_selecc.value].value == "" ){
                   //vForm.item_imei_sim[vForm.hdn_item_imei_selecc.value].value =  strSim;
                   grid_region.scrollTop=table.childNodes.item(0).childNodes.item(vForm.hdn_item_imei_selecc.value).childNodes.item(0).offsetTop;
               }                 
              }               
          };        
        vForm.txtSims.value = "";
        vForm.txtImeis.focus();
        try{
          vForm.hdnChangedOrder.value="S";
        }catch(e){;}      
      }
      

      
    </script>    
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>
