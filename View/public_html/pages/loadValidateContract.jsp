<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.ItemBean"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>

<html>
  <head>
    <script type="text/javascript">
 <%  
   HashMap hValidateContract = (HashMap)request.getAttribute("hValidateContract");
   long lngSpecificationId   = MiUtil.parseLong((String)hValidateContract.get("strSpecificationId"));
   String strSpecificationId = (String)hValidateContract.get("strSpecificationId");  //EZUBIAURR 27/12 Compatibilidad M-P-S
   String strControlName     = (String)hValidateContract.get("strControlName");   
   String   strMessage       = (String)hValidateContract.get("strMessage");
   
   if (strMessage!=null){%>
      alert('<%=MiUtil.getMessageClean(strMessage)%>');
      try{
      parent.mainFrame.frmdatos.txt_ItemNewPlantarifarioId.value = '';
      parent.mainFrame.frmdatos.cmb_ItemNewPlantarifario.value = '';
      parent.mainFrame.frmdatos.txt_ItemOrigMainServiceId.value = '';
      parent.mainFrame.frmdatos.txt_ItemOrigMainService.value = '';      
      }catch(e){}
      try{
        parent.mainFrame.frmdatos.<%=strControlName%>.focus();
      }catch(e){}
      //parent.mainFrame.frmdatos.txt_ItemContractNumber.focus();
      
   <%
   }else{
    ItemBean objItemBean      =  (ItemBean)hValidateContract.get("objItemBean");
    %>
    //Seteamos los valores para la categoria
    //ACTIVAR & DESACTIVAR SERVICIOS
    //objItemBean
    try{
      parent.mainFrame.frmdatos.txt_ItemNewPlantarifarioId.value = '<%=objItemBean.getNporiginalplanid()%>';
      parent.mainFrame.frmdatos.cmb_ItemNewPlantarifario.value = '<%=objItemBean.getNporiginalplanname()%>';
      parent.mainFrame.frmdatos.txt_ItemOrigMainServiceId.value = '<%=objItemBean.getNporigmainservice()%>';
      parent.mainFrame.frmdatos.txt_ItemOrigMainService.value = '<%=objItemBean.getNporigmainservicedesc()%>';
    }catch(e){}
    
    <%if(lngSpecificationId == 2048 || lngSpecificationId == 2049 ){%>
    try{
      var serviceAditional = "";
    
     // parent.mainFrame.fxCargaServiciosItem('<%=MiUtil.getString(objItemBean.getNpitemservices())%>');
   }catch(e){}
    <%}%>
      var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&cmb_ItemPlanTarifario=<%=objItemBean.getNporiginalplanid()%>&cmb_ItemSolution=<%=objItemBean.getNpsolutionid()%>&serviceAditional=<%=MiUtil.getString(objItemBean.getNpitemservices())%>&strPlanId=<%=objItemBean.getNporiginalplanid()%>&hdnSpecification="+<%=strSpecificationId%>+"&serviceAditional="+serviceAditional;  //EZM 27/12
      parent.bottomFrame.location.replace(url); //EZUBIAURR 27/12
    parent.mainFrame.fxCargaServiciosItem('<%=MiUtil.getString(objItemBean.getNpitemservices())%>');
   // location.replace('<%=Constante.PATH_APPORDER_SERVER%>+"/Bottom.html')
    <%}%>
    </script>
  </head>
</html>

   
  
      