<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>

<%
  //EFH001 Añadido Código para control de sesión.
  System.out.println("--------- popUpEquipPendRecojo.jsp ---------");
  String urlIngreso = request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : "");
  String jspIngreso = "popUpEquipPendRecojo.jsp";
  String ipIngreso =  request.getRemoteAddr();
  String  contexto = request.getContextPath();
  //FIN
try {
  long lCustomerId = (request.getParameter("customerId")==null?0:MiUtil.parseLong(request.getParameter("customerId"))); 
  long lSiteId =(request.getParameter("siteId")==null?0:MiUtil.parseLong(request.getParameter("siteId")));  

  System.out.println("[popUpEquipPendRecojo]lCustomerId:"+lCustomerId);
  System.out.println("[popUpEquipPendRecojo]lSiteId:"+lSiteId);
  
	GeneralService objGeneralService = new GeneralService();
	HashMap objHashMap = objGeneralService.getListaEquipPendRecojo(lCustomerId,lSiteId);
	ArrayList arrEquipPendRecojoList = (ArrayList) objHashMap.get("arrEquipPendRecojoList");
	String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
	if(strMessage!=null) {
		throw new Exception(strMessage);
	}
  System.out.println("Tamaño arrEquipPendRecojoList: " + arrEquipPendRecojoList.size());
  
	if(arrEquipPendRecojoList != null) {
		request.setAttribute("arrEquipPendRecojoList", arrEquipPendRecojoList);
	}
  
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>

        
<script language="javascript">
  //EFH001 Añadido Código para control de sesión.
  if (parent.opener==undefined||parent.opener== null){

    window.location.replace("<%=contexto%>"+"/htdocs/jp_session_validate/Session_Validate.jsp"+"?urlIngreso="+"<%=urlIngreso%>"+"&jspIngreso="+"<%=jspIngreso%>"+"&ipIngreso="+"<%=ipIngreso%>");
  }
  //FIN
  function fxCerrar(){
    parent.close();
  }

  function fxAceptar(){
    var table = document.all ? document.all["lstPendRecojo"]:document.getElementById("lstPendRecojo");    
    if (table.rows.length < 2){
      alert("No hay equipos pendiente de recojo")
      return;
    }
    else{
      if (table.rows.length == 2){        
			try{
				parent.opener.document.frmdatos.txt_ItemIMEI.value=txtImeiAux.value;
				// --- jsalazar 08/07/2011 Penalidad inicio
        parent.opener.document.frmdatos.txt_ItemModel.value=txtIdModeloAux.value;
        // --- jsalazar 08/07/2011 Penalidad fin
				parent.opener.document.frmdatos.txt_ItemEquipment.value=txtEquipoAux.value;
				parent.opener.document.frmdatos.txt_ItemPhone.value=txtTelefonoAux.value;
				parent.close();
				return;
			}
			catch(e){
				parent.close();
			}
      }
      else{
        for(i=0;i<rdbopcion.length;i++){
          if(rdbopcion[i].checked){
            indice=rdbopcion[i].value;
            parent.opener.document.frmdatos.txt_ItemIMEI.value=txtImeiAux[(indice % 100)-1].value; // --- %100 para obtener solo los de la página
            // --- jsalazar 08/07/2011 Penalidad inicio
            parent.opener.document.frmdatos.txt_ItemModel.value=txtIdModeloAux[(indice % 100)-1].value;
            // --- jsalazar 08/07/2011 Penalidad fin
            parent.opener.document.frmdatos.txt_ItemEquipment.value=txtEquipoAux[(indice % 100)-1].value;
            parent.opener.document.frmdatos.txt_ItemPhone.value=txtTelefonoAux[(indice % 100)-1].value;
            parent.close();
            return;
          }
        }//fin del FOR
        if ( i== rdbopcion.length){
          alert("Debe seleccionar una opción")
          return;
        }        
      }      
    }    
  }
  
  function paginar(numPagina) {   
  
	//mgarcia  29/09/2011
    //parametros ="";
    //parametros = "&customerId=<%=lCustomerId%>&siteId=<%=lSiteId%>";
    //cadena = "/apporderweb/htdocs/jsp/controls/POPUP/popUpEquipPendRecojo.jsp?"+"&d-447055-p="+numPagina;
    //mgarcia  29/09/2011

    parametros = "&siteId=<%=lSiteId%>&d-3753902-p="+numPagina+"&d-447055-p="+numPagina+"&customerId=<%=lCustomerId%>";

		cadena = "<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/popUpEquipPendRecojo.jsp?";
		cadena += parametros;
    
    //document.location.href = cadena;
    try { 
      location.replace(url)
    } catch(e) {
      window.location.href = cadena
    }
	}
	
	function fxPrimeraPagina(param) {
    paginar(param);
  }
  
  function fxAnteriorPagina(param) {
    paginar((param - 1));
  }
  
  function fxSiguientePagina(param) {
    paginar((param + 1));
  }
  
  function fxUltimaPagina(param) {
    paginar(param);
  } 
  
  // -- mfauda 12/09/2011 paginado
	function fxObtenerParametrosDisplayTag(url) {
		var indice = url.indexOf('d-');
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf('&');
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			}
			else {
				paramDisplayTag = url;
			}
		}
		return paramDisplayTag;
	}

  // -- mfauda 12/09/2011 paginado
	function fxGetPageNumber(url) {
		var indice = url.indexOf("d-");
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf("&");
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			} else {
				paramDisplayTag = url;
			}
			indice3 = url.indexOf("=");
			if(indice3 > 0) {
				pageNumber = paramDisplayTag.substr(indice3+1);
			} else {
				pageNumber = 0;
			}
		}
		return "&hdnNumPagina="+pageNumber;
	}
  
</script>              
<table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
	<tr>		
    <td class="CellLabel" width="15%">Lista de Equipos: </td>      
    <td class="CellContent" width="85%" align="left">Pendiente de Recojo y Robo</td>
	</tr>
	<tr>
		<td colspan="2">
			<display:table id="lstPendRecojo" name="arrEquipPendRecojoList" class="orders" pagesize="100" style="width: 100%">
        <display:column>
          <input type='radio' name='rdbopcion' value='<%=((HashMap) lstPendRecojo).get("rownum")%>'/>
        </display:column>      
				<display:column property="telefono" title="Tel&eacute;fono"/>          
				<display:column property="imei" title="IMEI"/>          
				<display:column property="sim" title="SIM"/>          
				<display:column property="modelo" title="Modelo"/>                  
				<display:column property="equipo" title="Equipo"/>
        <display:column property="estado" title="Estado"/>
        <display:column>        
          <input type='hidden' name='txtImeiAux' value='<%=((HashMap) lstPendRecojo).get("imei")%>'/>
          <input type='hidden' name='txtTelefonoAux' value='<%=((HashMap) lstPendRecojo).get("telefono")%>'/>
          <input type='hidden' name='txtModeloAux' value='<%=((HashMap) lstPendRecojo).get("modelo")%>'/>
          <input type='hidden' name='txtEquipoAux' value='<%=((HashMap) lstPendRecojo).get("equipo")%>'/>
          <input type='hidden' name='txtEstadoAux' value='<%=((HashMap) lstPendRecojo).get("estado")%>'/>
          <!--jsalazar 08/07/2011 Penalidad inicio-->
          <input type='hidden' name='txtIdModeloAux' value='<%=((HashMap) lstPendRecojo).get("idmodelo")%>'/> 
          <!--jsalazar 08/07/2011 Penalidad fin-->
        </display:column>
			</display:table>
		</td>
	</tr>
</table>
<table><tr align="center"><td></td></tr></table>
<table border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>              
    <td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " onclick="javascript:fxAceptar();">&nbsp;&nbsp;&nbsp;</td>
    <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCerrar();"></td>
  </tr>
</table>
<%} 
  catch(Exception e) {
    e.printStackTrace();
    out.println("<script>alert('"+e.getMessage()+"');</script>");
  }
%>