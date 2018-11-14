<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<%   
try{
   String strAreaCod=request.getParameter("txtAreaCode");
   
   String strFormName=request.getParameter("sFormName");
   String strTextAreaName=request.getParameter("sAreaCode");
   String strHdnAreaName=request.getParameter("hAreaCode");  
   String strTextPhoneName=request.getParameter("sTelfName");
   String strSpamName=MiUtil.getString(request.getParameter("sSpamArea"));  
   int iCount =(request.getParameter("nCount")==null?1:Integer.parseInt(request.getParameter("nCount")));  
   int iIndex =MiUtil.parseInt(request.getParameter("nIndex"));  
   int iTotalItems=0;
   String strMessage=null;
   HashMap hshData=null;
   //Craendo objeto General
   GeneralService objGeneralS=new GeneralService();
   hshData=objGeneralS.getAreaCodeList(null,strAreaCod);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);       
   ArrayList arrLista=(ArrayList)hshData.get("arrAreaCodeList");   
   iTotalItems=arrLista.size();
%>

<html>
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <title>Lista de Código de Área</title>
   </head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script>   
    
    /*function fxSelectedArea(npcity, npareacode){
      if (parent.opener!=null){
       parent.opener.document.formdatos.txtAreaCode.value=npareacode;
       parent.opener.spanAreaNom1.innerHTML ="("+npcity+")";     
       parent.opener.document.formdatos.txtNumTelefono.focus();
      }     
       parent.close();
    }*/ 
               
   function fxSelectedArea(npcity, npareacode){
      var ncount = <%=iCount%>;
      var index=<%=iIndex%>;     
      var form;        
      if (ncount == 1) {
         if (parent.opener!=null){
            form=eval("parent.opener.document.<%=strFormName%>");
            eval("form.<%=strTextAreaName%>.value = " + npareacode) ;
            eval("parent.opener.<%=strSpamName%>.innerHTML = '(" +npcity+")'");
            eval("parent.opener.document.<%=strFormName%>.<%=strTextPhoneName%>.focus()");
         }
      }else if (ncount >1) {
         if (parent.opener!=null){
            var i = index;
            if (parent.opener!=null){
               form=eval("parent.opener.document.<%=strFormName%>");
               eval("form.<%=strTextAreaName%>[" + i +"].value = " + npareacode) ;               
               eval("parent.opener.<%=strSpamName%>["+i+"].innerHTML = '(" +npcity+")'");
               eval("parent.opener.document.<%=strFormName%>.<%=strTextPhoneName%>["+i+"].focus()");               
               eval("parent.opener.document.<%=strFormName%>.<%=strTextPhoneName%>["+i+"].focus()");               
               eval("parent.opener.document.<%=strFormName%>.<%=strHdnAreaName%>["+i+"].value = npareacode");  
            }        
         }
      }          
      parent.close();
   }

/*
    function goToOtherSearch (nunVisits) {
       document.frmdatos.pn_visits.value = "0";
       history.go(nunVisits);
    }*/

   function goToOtherPage (pageNumber) {
      document.frmdatos.action = location.href;
      document.frmdatos.pv_page_number.value = pageNumber;
      document.frmdatos.submit();
   }
 </script>
 <body>
    <!-- Inicio de Marco -->
<TABLE  border="0" cellpadding="0" cellspacing="0" align="center" width="99%">
   <TR valign="top">
      <TD class="RegionHeaderColor">
      <!--Inicio Linea de separación -->
         <table border="0" cellspacing="2" cellpadding="0" width="80%" align="center" height="5">
            <tr align="center"><td></td></tr>
         </table>
      <!--Fin de Linea de separación -->
      
      <!-- Titulo Seccion -->
         <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
            <tr class="PortletHeaderColor">
               <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
               <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Código de área > Buscar</font></td>
               <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
            </tr>
         </table> 
      
      <!--Inicio Linea de separación -->
         <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center" height="5">
            <tr align="center"><td></td></tr>
         </table>
      <!--Fin de Linea de separación -->
      
         <table border="0" cellspacing="1" cellpadding="1" align="center" width="99%" class="RegionBorder" > 
      <%if (iTotalItems >0) {%>
      <!-- IF wn_total_items > 0 THEN
      wn_prev_first_reg := (to_number(pv_page_number) - 1) * wn_rows_by_page;
      wn_last_reg       := to_number(pv_page_number) * wn_rows_by_page;
      
      IF wn_last_reg > wn_total_items THEN
       wn_last_reg := wn_total_items;
      END IF;
      
      FOR row IN 1..wn_prev_first_reg LOOP
       FETCH wc_list INTO rv_npcity, rv_npareacode;
      END LOOP;-->
      
      <!-- Barra de Titulo -->
            <tr>
               <td class="ListRow0">&nbsp;</td>
               <td class="ListRow0">&nbsp;<b>Departamento</b></td>
               <td class="ListRow0" align="center">&nbsp;<b>Código de área</b></td>
            </tr>
         
      <% HashMap hshMapData=null;
      for (int i=0;i<arrLista.size();i++){
         hshMapData=((HashMap)arrLista.get(i));                               
      /* LOOP
       FETCH wc_list INTO rv_npcity, rv_npareacode;
       EXIT WHEN wc_list%NOTFOUND;
       EXIT WHEN wn_last_reg < wn_prev_first_reg + wn_z;*/
          String strColor="";
          if (((i+1)%2)==0)  
             strColor="ListRow0";
          else
             strColor="ListRow1";
      
      %>
            <tr>
               <td class="<%=strColor%>" valign="top">
               <a href="javascript:fxSelectedArea('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>
               <%=(i+1)%>
               <!--'||to_char(wn_prev_first_reg + wn_z)||'-->
               </td>
               <td class="<%=strColor%>" valign="top"><%=MiUtil.getString((String)hshMapData.get("city"))%></td>
               <td class="<%=strColor%>" valign="top" align="center"><%=MiUtil.getString((String)hshMapData.get("areaCode"))%></td>
            </tr>                        
      <%   //   wn_z := wn_z + 1;                                    
      
      }
      }else{%>
            <tr>
               <td class="CellContent" valign="top" align="center"><b><font color="#FFOOOO">No existen Codigo de area que satisfagan los criterios especificados</b></font></td>
            </tr>
      <%}%>          
         </table>
      
      <!--Contenido CustomerNew : inicio-->
      <form method="post" name="frmdatos">
         <input type="hidden" name="pv_page_number">
         <input type="hidden" name="pn_visits" value="'||pn_visits||'">
         
         <input type="hidden" name="nf_AreaCode" value="'||pv_nfareacode||'">
         <input type="hidden" name="nf_AreaNom" value="'||pv_nfareanom||'">
         <input type="hidden" name="nf_telf"     value="'||pv_nftelf||'">
         
         <input type="hidden" name="txtAreaName" value="'||pv_areaname||'">
         <input type="hidden" name="txtAreaCode" value="'||pv_areacode||'">
      </form>
      
      <!--NPSL_GENERAL_PL_PKG.SP_PAGINADO(
      an_nro_filas   => wn_total_items ,
      an_page_number => pv_page_number);-->
      
      
      <!--FIN PAGINADO-->             
      
      </TD>
   </TR>
</table>
 </body>
 </html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%> 