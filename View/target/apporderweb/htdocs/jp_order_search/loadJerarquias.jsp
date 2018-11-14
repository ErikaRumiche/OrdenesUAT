<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.service.OrderSearchService"%> 

<%//Carga inicial de variables globales:
	int an_ruleid = 33;
  
	String av_retriverepresentative = (String)request.getParameter("av_retriverepresentative");//"LASTLEVEL";//CONST_RETRIEVEREPRESENT_LAST
	String av_madatoryrepresentative = (String)request.getParameter("av_madatoryrepresentative");//"SI";
	
  int an_salesstructid = 0;
  String strNpsalesstructid = (String)request.getParameter("strNpsalesstructid");//(String)session.getValue("npsalesstructid");
  if(strNpsalesstructid!=null && !strNpsalesstructid.trim().equals("")){
      an_salesstructid = Integer.parseInt(strNpsalesstructid);
  }
  
  int an_providergrpid = 0;
  String strSwprovidergrpid = (String)request.getParameter("strSwprovidergrpid");//(String)session.getValue("swprovidergrpid");
  if(strSwprovidergrpid!=null && !strSwprovidergrpid.trim().equals("")){
      an_providergrpid = Integer.parseInt(strSwprovidergrpid);
  }
  
  //Valores default
  int an_salesstructdefaultid = 0;
  String strSalesstructdefaultid = (String)request.getParameter("pn_npsalesstructid");//(String)session.getValue("swprovidergrpid");
  if(strSalesstructdefaultid!=null && !strSalesstructdefaultid.trim().equals("")){
      an_salesstructdefaultid = Integer.parseInt(strSalesstructdefaultid);
  }
  
  int an_providergrpdefaultid = 0;
  String strProvidergrpdefaultid = (String)request.getParameter("pn_swproviderid");//(String)session.getValue("swprovidergrpid");
  if(strProvidergrpdefaultid!=null && !strProvidergrpdefaultid.trim().equals("")){
      an_providergrpdefaultid = Integer.parseInt(strProvidergrpdefaultid);
  }
  
  
  //------------------------------------
  OrderSearchService orderSearchService = new OrderSearchService();
  int wn_salesstructid = 
    orderSearchService.getParentForAssist(an_salesstructid);
  //NP_SALES_STRUCTURE_PKG.sp_get_parent_for_assist(nvl(an_salesstructdefaultid,an_salesstructid),wn_salesstructid,wv_message); 
  if(wn_salesstructid==0)
    wn_salesstructid = an_salesstructid;
  
  
  int wn_swprovidergrpid = orderSearchService.getPrvdStructAssist(an_salesstructid);
  if(wn_swprovidergrpid==0){
    wn_swprovidergrpid = an_providergrpid;
  }
  System.out.println("wn_swprovidergrpid="+wn_swprovidergrpid);
  //NP_SALES_STRUCTURE_PKG.SP_GET_PRVDRGRP_SSTRUCT_ASSIST(ans_salesstructid, wn_swprovidergrpid,wv_message);
  
  String wv_last_position = orderSearchService.getLastPosition(wn_salesstructid);
  //NP_SALES_STRUCTURE01_PKG.FX_CHECK_LAST_POSITION2(wn_salesstructid);
  //------------------------------------




  
  int valIni = 0;
%>
<link rel="stylesheet" href="/websales/Resource/salesweb.css" type="text/css">
<script language="javascript" src="/websales/Resource/BasicOperations.js"></script>
<script language="javascript" src="/websales/Resource/library.js"></script>
<script language="javascript" src="/websales/Resource/DateTimeBasicOperations.js"></script>
<script language="javascript">
	var v_num_global = "";
	function fxUnidadJerarquica(){
     //alert('En fxUnidadJerarquica');
	   var url="";
	   var v_providerid            = "";
	   var v_providertext          = "";
     //alert('parent.mainFrame.document.formdatos='+parent.mainFrame.document.formdatos);
	   var v_unid_jerar            = parent.mainFrame.document.formdatos.v_unidadjer.value;
	   var v_retriverepresentative = '<%=av_retriverepresentative%>';
	   var v_salesstructid         = '<%=wn_salesstructid%>';
	   //if(v_retriverepresentative == '||NP_SALES_STRUCT_CONST_PKG.CONST_RETRIEVEREPRESENT_ALL||'" || v_retriverepresentative=="'||NP_SALES_STRUCT_CONST_PKG.CONST_RETRIEVEREPRESENT_LAST||'"){
     if(v_retriverepresentative=='ALL' || v_retriverepresentative=='LASTLEVEL'){
        var index = parent.mainFrame.document.formdatos.cmbRepresentante.selectedIndex;
        v_providerid = parent.mainFrame.document.formdatos.cmbRepresentante.options[index].value;
        
        v_providerid = parent.mainFrame.document.formdatos.cmbRepresentante.options[index].value;
        v_providertext = parent.mainFrame.document.formdatos.cmbRepresentante.options[index].text;               
	   }
	   if(v_salesstructid == ""){
        alert('Usted no puede visualizar las Unidades Jerarquicas');
	   }else{
        //url = "/portal/pls/portal/WEBSALES.NP_SALES_STRUCTURE_PL_PKG.PL_SALES_STRUCT_POPUP?an_ruleid='||an_ruleid||'&av_retriverepresentative='||av_retriverepresentative||'&av_madatoryrepresentative='||av_madatoryrepresentative||'&an_salesstructid='||an_salesstructid||'&av_providergrpid='||an_providergrpid||'&an_salesstructdefaultid="+v_unid_jerar+"&an_providergrpdefaultid="+v_providerid+"&av_providertext="+v_providertext;
        //url = "/portal/pls/portal/websales.npsl_general_pl_pkg.window_frame?av_title=" + escape("Websales > Jerarquía de Ventas") + "&av_url=" + escape(url);
        
        url = "/portal/pls/portal/WEBSALES.NP_SALES_STRUCTURE_PL_PKG.PL_SALES_STRUCT_POPUP?av_frmdatos=parent.mainFrame.document.formdatos&an_ruleid=<%=an_ruleid%>&av_retriverepresentative=<%=av_retriverepresentative%>&av_madatoryrepresentative=<%=av_madatoryrepresentative%>&an_salesstructid=<%=wn_salesstructid%>&av_providergrpid=<%=wn_swprovidergrpid%>&an_salesstructdefaultid="+v_unid_jerar+"&an_providergrpdefaultid="+v_providerid+"&av_providertext="+v_providertext;
        url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title=" + escape("Websales > Jerarquía de Ventas") + "&av_url=" + escape(url);
        
        WinAsist = window.open(url,"WinAsistUnidJerarq", "toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=850,height=600,modal=yes");
	   }
	}
	function fx_validarObligatorio(){            
	   var vform                        = parent.mainFrame.document.formdatos;
	   var cmbRepres                    = vform.cmbRepresentante.value;
	   var v_madatoryrepresentative     = '<%=av_madatoryrepresentative%>';
	   //alert(cmbRepres);
	   if(v_madatoryrepresentative == 'SI'){
        if(cmbRepres != ""){
          return true;
        }else{
          return false;
        }
	   }else{
		  return true;
	   }
	}
  function fx_onChangeCheckSeller(){
    /*try{
        fx_CheckSeller();
     }catch(err){
        if (err="Object Error"){
        }else{
         throw err;
        }
     }*/
  }
            
	function fx_opcionBlancoSelect(){
     //alert('fx_opcionBlancoSelect');
	   //if(!fx_opcionBlancoLastlevel()){
        alert("Se debe seleccionar un representante");
        v_num = parent.mainFrame.document.formdatos.v_indice.value;
        
        parent.mainFrame.document.formdatos.cmbRepresentante.value = v_num;
        //parent.mainFrame.document.formdatos.cmbRepresentante.options[v_num].selected = true;
	   //}
	}

	function fx_opcionBlancoLastlevel(){
	   var index = parent.mainFrame.document.formdatos.cmbRepresentante.selectedIndex;
	   v_providerid = parent.mainFrame.document.formdatos.cmbRepresentante.options[index].value;
	   v_providertext = parent.mainFrame.document.formdatos.cmbRepresentante.options[index].text;
	   var v_retriverepresentative = '<%=av_retriverepresentative%>';
	   //alert("El indice es: " + index);
	   if(v_retriverepresentative == 'LASTLEVEL'){
        if(index == 0){
           return false;
        }else{
           return true;
        }              
	   }else{
        return true;
	   }
	}
</script>
<tr id="unidadJ">
	<td align="left" valign="middle" class="CellLabel">&nbsp;
		<a href="javascript:fxUnidadJerarquica();">Unidad Jerárquica:</a>
	</td>
	
	<td align="left" valign="middle" class="CellContent"><%
String wn_indice = null;

//wc_list
String wv_name,wv_parentsalesstructid,wv_message = "";
Integer wn_count = null;


HashMap contAux = new HashMap();

contAux = orderSearchService.getDataField(an_ruleid, av_retriverepresentative, an_salesstructid, wn_swprovidergrpid);

/*WEBSALES.NP_SALES_STRUCTURE_PKG.sp_get_data_field(
      an_ruleid, av_retriverepresentative, an_salesstructid,
      an_providergrpid, an_salesstructdefaultid, an_providergrpdefaultid,
      wv_name, wc_list, wn_count, wv_message);*/


ArrayList arrProvidergrpid = (ArrayList)contAux.get("arrProvidergrpid");
ArrayList arrSelected = (ArrayList)contAux.get("arrSelected");
ArrayList arrSwname = (ArrayList)contAux.get("arrSwname");
wv_message = (String)contAux.get("wv_message");
wv_name = (String)contAux.get("wv_name");
wv_parentsalesstructid=(String)contAux.get("wv_parentsalesstructid");
wn_count = (Integer)contAux.get("wn_count");
//CBARZOLA 30/09/2009
if(an_salesstructid!=Integer.parseInt(wv_parentsalesstructid)){
  an_salesstructid=Integer.parseInt(wv_parentsalesstructid);
}





if(wv_message!=null && !wv_message.equals("") ){%>
    <script>
      alert('Error:='+<%=wv_message%>);
    </script>
  </td><%
}else{
  String wv_function_js_cmb = "";
  if( av_madatoryrepresentative.equals("SI") && wv_last_position.equalsIgnoreCase("S")){

    wv_function_js_cmb = "fx_opcionBlancoSelect();";
  }%>
  &nbsp;
  <input type="text" name="v_unidadjerar" value="<%=wv_name%>" readonly="readonly" size="40">
  <input type="hidden" name="v_unidadjer" value="<%=an_salesstructid%>">
  
  <input type="hidden" name="hdnSalesstructid" value="<%=an_salesstructid%>"><!-- AGC-agregado -->
  
  <input type="hidden" name="hdnProviderid"><%-- value="<%=an_providergrpid%>"> --%>
  
  
	</td><%
	if(!av_retriverepresentative.equals("NONE")){%>
    <td align="left" valign="middle" class="CellLabel"><%
        if(av_madatoryrepresentative.equals("SI")){%>
          <font color="#FFOOOO">*</font><%
        }%>
          Representante:
    </td>
    <td align="left" valign="middle" class="CellContent">&nbsp;
          <%--
          <input type="hidden" name="hdnSalesstructid" value="<%=an_salesstructid%>"><!-- AGC-agregado -->
          <input type="hidden" name="hdnProviderid" value="<%=an_providergrpid%>"><!-- AGC-agregado -->
          --%>
          
          <input type="hidden" name="v_providerid" value="<%=an_providergrpdefaultid==0?wn_swprovidergrpid:an_providergrpdefaultid%>">
          <select id="cmbRepresentante" name="cmbRepresentante" onChange="fx_onChangeCheckSeller(); <%=wv_function_js_cmb%>">
             <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
             <%
             if(arrProvidergrpid!=null){
               for(int i=0; i<arrProvidergrpid.size();i++){
                  int wn_npprovidergrpid = (arrProvidergrpid.get(i)!=null?((Integer)arrProvidergrpid.get(i)).intValue():0);
                  String wv_npselected = (String)arrSelected.get(i);
                  String wv_swname = (String)arrSwname.get(i);
                  String wn_indice_tmp = wv_swname;%>
                  <script>
                  //alert('wv_npselected='+'<%=wv_npselected%>');
                  </script><%
                  if(wv_npselected!=null && wv_npselected.equalsIgnoreCase("SELECTED")){
                    valIni = wn_npprovidergrpid;
                    wn_indice = wn_indice_tmp;
                    System.out.println("****wn_indice="+wn_indice);%>
                    <option value="<%=wn_npprovidergrpid%>" selected><%=wv_swname%></option>
                    <script>
                      parent.mainFrame.document.formdatos.strRepresentante.value = '<%=wv_swname%>';
                      //alert(parent.mainFrame.document.formdatos.strRepresentante.value);
                    </script>
                    <%
                  }else{%>
                    <option value="<%=wn_npprovidergrpid%>" ><%=wv_swname%></option>
                  <%}
               }
              }%>
          </select>
    </td><%
	}else{%>
			<td width="20%" align="left" valign="middle" class="CellContent">&nbsp;</td>
			<td width="30%" align="left" valign="middle" class="CellContent">&nbsp;</td><%
	}%>
  <input type="hidden" name="v_indice" value="<%=valIni%>"><%
}%>
</tr>