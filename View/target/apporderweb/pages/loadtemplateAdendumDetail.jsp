<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.bean.TemplateAdendumBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%--@ page import="org.apache.commons.lang.ArrayUtils"--%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
   
    <script type="text/javascript">
    <%  
        System.out.println("[loadTemplateAdendumDetail.jsp.jsp] PINTADO DE LA LISTA");
        GeneralService objGeneralService = new GeneralService();
        ArrayList ubigeoList = (ArrayList) request.getAttribute("tblListAdendum");       
        Iterator iterator = ubigeoList.iterator();
        TemplateAdendumBean templateAdendumBean = null;
        while(iterator.hasNext()) {
           templateAdendumBean = new TemplateAdendumBean();
           templateAdendumBean = (TemplateAdendumBean) iterator.next();
    %>
                
           row = parent.mainFrame.itemsTemplate.insertRow(-1);             
           col = row.insertCell(0);
           col.className = 'CellContent';   
           col.align     = 'center';
           <%
           if (("S").equals(templateAdendumBean.getNptemplatedefa())){
 
               %>        
                 col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" checked="true" style="text-align:center" disabled="true" onClick="javascript:agregar(this)"  >';
               <%
           
            }
            if (!(("S").equals(templateAdendumBean.getNptemplatedefa()))){
              
            %>        
                  col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" style="text-align:center" disabled="true" onClick="javascript:agregar(this)" >';                           
            <%
               }                      
            %>               
            col.innerHTML = col.innerHTML;
   
            col = row.insertCell(1);
            col.className = 'CellContent';   
            col.align     = 'center';   
            col.innerHTML = '<input type=hidden name=txtDescTemplate size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';             
            col.innerHTML = col.innerHTML + '<%=templateAdendumBean.getNptemplatedesc()%>';               
                                              
            col = row.insertCell(2);
            col.className = 'CellContent';   
            col.align     = 'center';   
            col.innerHTML = '<input type=hidden name=txtTypeAdendum size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';             
            col.innerHTML = col.innerHTML + '<%=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpaddendtype(), "TIPOADENDA")%>';               
            
            col = row.insertCell(3);
            col.className = 'CellContent';   
            col.align     = 'center';   
            col.innerHTML = '<input type=hidden name=txtDateCreated size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';             
            col.innerHTML = col.innerHTML + '<%=templateAdendumBean.getNpcreateddate()%>';               
            
            col = row.insertCell(4);
            col.className = 'CellContent';   
            col.align     = 'center';   
            col.innerHTML = '<input type=hidden name=txtTypeBenefit size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';             
            col.innerHTML = col.innerHTML + '<%=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpbenefittype(), "BENEFICIO")%>';               
                 
            col = row.insertCell(5);
            col.className = 'CellContent';   
            col.align     = 'center';   
            col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=5 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" disabled="true" onkeyup="javascript: agregar(form.checkSelec);" >';             
            col.innerHTML = col.innerHTML;
     <%}%>
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>