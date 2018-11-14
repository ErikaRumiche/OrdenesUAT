<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.bean.TemplateAdendumBean"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.service.ItemService" %>

<%--@ page import="org.apache.commons.lang.ArrayUtils"--%>
<%String  strSpecificationId = (String)request.getAttribute("hdnSpecification");%>
<%String  disable1 = "";%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script>
        function fxOnlyNumber(evt){
            // NOTA: '0' = 48, '9' = 57
            var vKey = vValor ? evt.which : evt.keyCode;
            return (vKey <= 13 || (vKey >= 48 && vKey <= 57));
        }
        function getTipoAdenda(obj){
            <%if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){%>
            var phone = form.txt_ItemPhone.value;
            if (phone=="" ){
                alert("Debe ingresar el número de teléfono.");
                obj.checked = false;
                return;
            }
            if(obj.checked){
                var iTemplateId = obj.value;
                var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getTipoPlantillaAdenda&strNumeroNextel="+phone+"&iTemplateId="+iTemplateId+"&txtToSet="+"flagAdenda"+obj.value;
                parent.bottomFrame.location.replace(url);
            }else{
                document.getElementById("flagAdenda"+obj.value).innerText = '';
            }
            <%}else{%>
            if(obj.checked){
                document.getElementById("flagAdenda"+obj.value).innerText = '<%=Constante.TIPO_PLANTILLA_NUEVA%>';
                document.getElementById("flagAdenda"+obj.value).className = 'TemplateLabelNew';
            }else{
                document.getElementById("flagAdenda"+obj.value).innerText = '';
            }
            <%}%>
        }
    </script>
    <script> //alert("loadtemplateaddendum");
    parent.mainFrame.frmdatos.hdn_ListAdenda.value = "";
    if (parent.mainFrame.document.getElementById("itemsTemplate")!=null){
        <%
            GeneralService objGeneralService = new GeneralService();
            ArrayList ubigeoList = (ArrayList) request.getAttribute("tblListAdendum");
            String iFlagCarrier = (String) request.getAttribute("iFlagCarrier");
            Iterator iterator2 = ubigeoList.iterator();
            TemplateAdendumBean templateAdendumBean2 = null;
            ItemService itemService = new ItemService();
            String flagAdendum = itemService.OrderDAOgetValidationAdenda(MiUtil.parseInt(strSpecificationId));
            HashMap hshData=null;
            String strMessage=null;

            //inicio jobregon
              while(iterator2.hasNext()){
                templateAdendumBean2 = new TemplateAdendumBean();
                templateAdendumBean2 = (TemplateAdendumBean) iterator2.next();
            // inicio jvillanuevar
                if (("S").equals(templateAdendumBean2.getNptemplatedefa())){
                   if (MiUtil.parseInt(flagAdendum) == 1 ){
                       disable1 = "disabled";
                   }
                }
             // fin jvillanuevar
             }


          Iterator iterator = ubigeoList.iterator();
          TemplateAdendumBean templateAdendumBean = null;
          // fin  jobregon

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
        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" checked="true" style="text-align:center" onClick="javascript:agregar(this)" <%=disable1%>>';
        parent.mainFrame.frmdatos.hdn_ListAdenda.value += "<%=templateAdendumBean.getNptemplateid()%>-<%=templateAdendumBean.getNpaddendumterm()%>;";
        <%
     }
     if (!(("S").equals(templateAdendumBean.getNptemplatedefa()))){
     %>
        col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="<%=templateAdendumBean.getNptemplateid()%>" style="text-align:center" onClick="javascript:agregar(this);getTipoAdenda(this)" <%=disable1%>>';
        <%
           }
        %>
        col.innerHTML = col.innerHTML;

        col = row.insertCell(1);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtDescTemplate size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=templateAdendumBean.getNptemplatedesc()%>';


        <%
        hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpaddendtype(), "TIPOADENDA");
        strMessage=(String)hshData.get("strMessage");
        if (strMessage!=null)
        throw new Exception(strMessage);
        String strTipoAdenda=(String)hshData.get("strDescription");
        %>
        col = row.insertCell(2);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTypeAdendum size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=strTipoAdenda%>';

        col = row.insertCell(3);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtDateCreated size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=MiUtil.getDate(templateAdendumBean.getNpcreateddate(), "dd/MM/yyyy")%>';

        <%
       hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpbenefittype(), "BENEFICIO");
       strMessage=(String)hshData.get("strMessage");
       if (strMessage!=null)
       throw new Exception(strMessage);
       String strBeneficioAdenda=(String)hshData.get("strDescription");
       %>
        col = row.insertCell(4);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTypeBenefit size=10 maxlength=15 value="Venta" style="text-align:center" readOnly>';
        col.innerHTML = col.innerHTML + '<%=strBeneficioAdenda%>';

        col = row.insertCell(5);
        col.className = 'CellContent';
        col.align     = 'center';
        <%if(iFlagCarrier.equals("1")){%>
        col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" onkeyup="javascript: agregar(form.checkSelec);" onKeyPress="return fxOnlyNumber(event);">';
        <%}else{%>
        col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="<%=templateAdendumBean.getNpaddendumterm()%>" style="text-align:right" readonly onkeyup="javascript: agregar(form.checkSelec);" >';
        <%}%>
        col.innerHTML = col.innerHTML;

        col = row.insertCell(6);
        col.className = 'CellContent';
        col.align     = 'center';
        col.innerHTML = '<input type=hidden name=txtTipoAden size=10 maxlength=15 style="text-align:center">';
        col.id='flagAdenda<%=templateAdendumBean.getNptemplateid()%>';
        <%}%>

    }
    </script>
</head>
<body bgcolor="#AABBCC">
</body>
</html>