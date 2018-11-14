<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.bean.FormatBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>

<HTML dir="LTR" lang="en">
<HEAD>
<TITLE>CHECK_LOGIN</TITLE>
<LINK REL=Stylesheet TYPE="text/css" HREF="https://172.22.123.22/portal/pls/portal/PORTAL30.wwv_setting.render_css?p_lang_type=NOBIDI&p_subscriberid=1&p_styleid=10084&p_siteid=0&p_rctx=P">
<META name="title" content="CHECK_LOGIN">
<META name="description" content="Verifica si tiene mas de un rol">
<META name="keywords" content="">
<META name="author" content="WEBSALES">
</HEAD>
<BODY leftMargin="0" rightMargin="0" topMargin="0" marginheight="0" marginwidth="0" class="Bodyid10084siteid0" >

<NOSCRIPT></NOSCRIPT>
<STYLE TYPE="text/css">.Highlight { font-family: Arial, Helvetica; font-size: 8pt; color: #FF0000; font-weight: bold ; }</STYLE>

<TABLE  BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%" background="/images/pobtrans.gif"  HEIGHT="5"></TABLE><TABLE  BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%" background="/images/pobtrans.gif" ><TR>
<TD COLSPAN="3" WIDTH="100%"><IMG SRC="/images/pobtrans.gif" BORDER="0" HEIGHT="4" ALT=""></TD>
</TR>
<TR>
<TD WIDTH="4"><IMG SRC="/images/pobtrans.gif" BORDER="0" HEIGHT="1" WIDTH="4" ALT=""></TD><TD vAlign="top" width="100%"><TABLE  BORDER="0" WIDTH="100%" CELLPADDING="0" CELLSPACING="0" class="RegionNoBorder">
<TR>
<TD class="RegionHeaderColor" WIDTH="100%">
           <script language="javascript">
             function init(){}
           </script>
      

        <STYLE>
        .show{
            DISPLAY: inline
        }
        .hidden{
             DISPLAY: none
        }
        </STYLE>
        <script language="javascript">
            function fx_change(){
               v_form = document.formdatos;
               if(v_form.rol.selectedIndex==0){
                  return;
               }
               var app_url = '<%=request.getContextPath()%>/loginservlet';
               var app_id  = v_form.item("rol" + v_form.rol.selectedIndex.toString()).value;
               
               v_form.an_rolid.value= app_id;
               
               if (app_id == ""){
                  return;
               }
               
               url = app_url+"?an_rolid="+app_id;
               v_form.action=url;
               v_form.submit();
               //location.href=url;
            }
        </script>
         <table WIDTH="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
               <td width="0%">&nbsp;</td>
               <td width="22%" valign="top"><br>
               <!--Menu : inicio-->
               <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                  <tr class="PortletHeaderColor">
                     <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                     <td class="PortletHeaderColor"align="center" valign="top"><font class="PortletHeaderText">Aplicaciones Disponibles</font></td>
                     <td align="right" class="RightCurve" width="10">&nbsp;&nbsp;</td>
                  </tr>
               </table>
               <table border="0" width="100%" cellpadding="0" cellspacing="0" class="RegionBorder">
                  <tr valign="top">
                     <td class="RegionHeaderColor" width="250">
                        <FORM  METHOD="POST" NAME="formdatos" action="loginservlet"  >
                           <INPUT TYPE="hidden" NAME="p_requested_url" VALUE="/portal/pls/portal/">
                           <INPUT TYPE="hidden" NAME="p_cancel_url" VALUE="http://www.nextel.com.pe">
                           <INPUT TYPE="HIDDEN" NAME="wn_swpersonid" VALUE="2617172">
                           <INPUT TYPE="HIDDEN" NAME="an_rolid" VALUE="0">
                           <INPUT TYPE="HIDDEN" NAME="an_url" VALUE="0">
                           <INPUT TYPE="HIDDEN" NAME="HOME" VALUE="">
                           <INPUT TYPE="HIDDEN" NAME="HOME_WS" VALUE="/portal/page/portal/nextel/HOME">
                           <INPUT TYPE="HIDDEN" NAME="HOME_SAC" VALUE="/portal/page/portal/nextel/HOME">
                           <INPUT TYPE="HIDDEN" NAME="LOGOUT" VALUE="/portal/pls/portal/NPR_PORTAL.wwsec_app_priv.logout?p_done_url=/portal/pls/portal/NPR_PORTAL.wwsec_app_priv.login">
                           <INPUT TYPE="HIDDEN" NAME="strAction" VALUE="loadApplicationByRol">
                           <INPUT TYPE="HIDDEN" NAME="paramLogin" VALUE="<%=request.getParameter("ssousername")%>">

         				  		<TABLE WIDTH="99%" BORDER="0" CELLPADDING="2" CELLSPACING="0">
         							<TR>
         								<TD class="Principal"></TD>
         							</TR>
         							<TR>
      	    							<TD CLASS="PortletText1">
      	    							   <center>
      	    							   
                                    Seleccione Aplicación
                                    <SELECT name="rol" onchange="javascript:fx_change();">
                                       <OPTION VALUE="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION>
                                       
                                        <%
                                        GeneralService objGenServ = new GeneralService();
                                        String user = request.getParameter("ssousername");
                                        HashMap objHashUserApplication = objGenServ.getApplicationList(user);
                                        ArrayList objArrayApplication = (ArrayList)objHashUserApplication.get("objArrayApplication");
                                        %>
                                        
                                          <%for(int i=0;i<objArrayApplication.size();i++){
                                          FormatBean objFormat = (FormatBean)objArrayApplication.get(i);
                                          %>
                                             <OPTION VALUE="<%=objFormat.getNpformid()%>"> <%=objFormat.getNpformname()%></OPTION>
                                          <%}%>  

                                         <%
                                         %>   

                                    </SELECT>
                                    
                                     <input type=hidden name="rol1" value="38"> <input type=hidden name="rol2" value="27"> <input type=hidden name="rol3" value="36">
                                    
         							<TR>
         								<TD><IMG SRC="/images/pobtrans.gif" WIDTH="1" HEIGHT="8"></TD>
         							</TR>
         						</TABLE>
         					</FORM>
                     </td>
                  </tr>
               </table>
               </td>
               <td width="0%">&nbsp;</td>
            </tr>
         </table> 
         
</TD></TR>
</TABLE>
</TD><TD WIDTH="4"><IMG SRC="/images/pobtrans.gif" BORDER="0" HEIGHT="1" WIDTH="4" ALT=""></TD></TR>
<TR>
<TD COLSPAN="3" WIDTH="100%"><IMG SRC="/images/pobtrans.gif" BORDER="0" HEIGHT="4" ALT=""></TD>
</TR>
</TABLE><!-- Page Metadata Generated On: 05-AUG-2010:11:12:56  Invalidated On:   Invalidated By User:   Time Taken: 10 msecs -->
</BODY>
</HTML>
