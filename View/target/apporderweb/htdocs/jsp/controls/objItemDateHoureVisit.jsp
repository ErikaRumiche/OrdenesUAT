<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>

 <tr>
    <td class="CellLabel" align="left">&nbsp;<font color="red">*</font>&nbsp;Fecha/Hora&nbsp;Visita&nbsp;</td>
    <td class="CellContent" >&nbsp;
    <input type="text" name="txtVisitDate" value="" size="15" onblur="fxSetDateHoureVisit();">
    <a href="javascript:show_calendar('frmdatos.txtVisitDate',null,null,'DD/MM/YYYY');">
    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" ALT="dd/mm/yyyyy" width=24 height=22 border=0></a>
    <input type="text" name="txtTime" value="" size="5" maxlength="5" onblur="fxSetDateHoureVisit();"><i>HH24:MI</i></td>
    </td>
 </tr>
 
 <input type="hidden" name="txt_ItemDateHoureVisit" value="" size="15">

<script>
  function fxSetDateHoureVisit(){
    parent.mainFrame.frmdatos.txt_ItemDateHoureVisit.value = parent.mainFrame.frmdatos.txtVisitDate.value + " " + parent.mainFrame.frmdatos.txtTime.value;
  }
</script>
