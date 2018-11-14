<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
 <%@page import="pe.com.nextel.util.Constante"%>
 <%@page import="pe.com.nextel.util.MiUtil"%>
 <%@page import="java.util.ArrayList"%>
 <%@page import="java.util.HashMap"%>
 <%@page import="pe.com.nextel.bean.SiteBean"%>
 
 <%
      
      ArrayList objSiteHashMap   = (ArrayList)request.getAttribute("lista_sites");   
            
      String objWv_swname  = (String)request.getAttribute("wv_swname");
      
      int objWv_customerid = MiUtil.parseInt((String)request.getAttribute("wv_customer"));
            
 %>   
 
  <textarea name="txtItemsCleanMassiveTransfer">

  </textarea>
  
  <script>
     var form = parent.mainFrame.document.frmdatos;
     
     form.cmbResponsablePagoMasivos.length=0;
     parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmbResponsablePagoMasivos, '', '           ');
          
     parent.mainFrame.DeleteSelectOptions(form.cmbAvailableServicesMassive);
     parent.mainFrame.DeleteSelectOptions(form.cmbActiveServicesMassive);
     parent.mainFrame.DeleteSelectOptions(form.cmbDesactiveServicesMassive);
     parent.mainFrame.DeleteSelectOptions(form.cmb_PlanTarifario);

     parent.mainFrame.vServicio.removeElementAll();;
     parent.mainFrame.vServicioPorSolucion.removeElementAll();
     
     parent.mainFrame.idItemsMassiveTransfer.innerHTML = "";
     parent.mainFrame.idItemsMassiveTransfer.innerHTML = txtItemsCleanMassiveTransfer.value;
        
     if (form.txtCompanya.value == "" || "<%=objWv_swname%>" != "null"){
        form.txtCompanya.value="<%=objWv_swname%>";
        form.hdnCompanyaName.value ="<%=objWv_swname%>";
     }
        
     if (form.hdnCustId1.value == "" || form.hdnCustId1.value == 0 || "<%=objWv_customerid%>" != "null")
        form.hdnCustId1.value = "<%=objWv_customerid%>";
     
     <% if( objSiteHashMap != null ){
          System.out.println("Cant1======>"+objSiteHashMap.size());
           HashMap hsMapLista = new HashMap();                   
           for( int i=0; i<objSiteHashMap.size();i++ ){
              hsMapLista = (HashMap)objSiteHashMap.get(i);
              
              System.out.println(i+" --- "+hsMapLista.get("wn_swsiteid"));
              
      %>
              if (form.cmbResponsablePagoMasivos != null)
                 parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmbResponsablePagoMasivos, '<%=hsMapLista.get("wn_swsiteid")%>', '<%=hsMapLista.get("wv_swsitename").toString()%>  <%=hsMapLista.get("wv_npcodbscs")%>');                
          <%
          }
       }
          %>

  
     location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
  </script>