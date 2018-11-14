<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.RepairService"%>

<%!
      HashMap hshDataMap = new HashMap();
      HashMap hshDataMapStock = new HashMap();
      HashMap hshParametrosMap = new HashMap();
      HashMap hshParametrosMapAux = new HashMap();
      RepairService objRepairService = null;
      
      String flagCount = null;
      String strFlagStock = null;
      String strStockMessage = null;
      String strMessage = null;
            
      String hdnOrderId;
      String hdnRepairId;
      String hdnReplaceType;
      String hdnLogin;
      String hdnFlagGenerate;
      String strProceso;
            
      String[] hdnRepListId;
      String[] txtImeiLista;
      String[] txtSimLista;
      String[] txtSerieLista;
      String[] cmbTipoImeiLista;
      String[] chkCrearDocLista;
      String[] cmbModeloLista;
      String[] cmbTipoLista;
      String[] cmbFlagAccesorioLista;
      String[] chkDevolverEquipoLista;
      
      String cadenaHTML = "";   
      
      String flagOrigen = "";
      String aceptar = "";
      
      String hdnRepListIdLength = "";
      String txtImeiListaLength = "";
      String txtSimListaLength = "";
      String txtSerieListaLength = "";
      String cmbTipoImeiListaLength = "";
      String chkCrearDocListaLength = "";
      String cmbModeloListaLength = "";
      String cmbTipoListaLength = "";
      String cmbFlagAccesorioListaLength = "";
      String chkDevolverEquipoListaLength = "";
      
             
%>

<%
try{
        objRepairService = new RepairService();
        
        System.out.println("Servlet: ----manageListPrestamosCambios-----");
        //hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);        
        hshDataMapStock = (HashMap) request.getAttribute("hshDataMapStock");        
        hshParametrosMap = (HashMap) request.getAttribute("hshParametrosMap");
        
        strMessage = (String)request.getAttribute("strMessage");      
        flagOrigen = (String)request.getAttribute("flagOrigen");   
        flagCount  = (String)request.getAttribute("flagCount"); 
        
        System.out.println("flagOrigen: "+flagOrigen);
        if(flagOrigen == null){
            //flagOrigen = request.getParameter("flagOrigen");
            flagOrigen = request.getParameter("flagOrigen");
        }
        
        System.out.println("flagCount: "+flagCount);
        if(flagCount == null){
            flagCount = request.getParameter("flagCount");
        }
        
        System.out.println("aceptar: "+ aceptar);
        System.out.println("new flagOrigen: "+ flagOrigen);
        System.out.println("new flagCount: "+ flagCount);
        
        
        //Se ejcuta cuando viene desde el formulario
        if (flagOrigen.equals("0") ){
            
            System.out.println("---PRIMERA LLLAMADA----");
            
            strFlagStock = (String)hshDataMapStock.get("strFlagStock");
            strStockMessage = (String)hshDataMapStock.get("strStockMessage");
                                    
            hdnOrderId = (String) hshParametrosMap.get("hdnOrderId");            
            hdnRepairId = (String) hshParametrosMap.get("hdnRepairId");            
            hdnReplaceType = (String) hshParametrosMap.get("hdnReplaceType");            
            hdnLogin = (String) hshParametrosMap.get("hdnLogin");            
            hdnFlagGenerate = (String) hshParametrosMap.get("hdnFlagGenerate");            
            strProceso = (String) hshParametrosMap.get("strProceso");
            
            hdnRepListId = (String[]) hshParametrosMap.get("hdnRepListId");
            hdnRepListIdLength = String.valueOf(hdnRepListId.length);
            System.out.println("hdnRepListIdLength = " + hdnRepListIdLength);
      
            txtImeiLista = (String[]) hshParametrosMap.get("txtImeiLista");
            txtImeiListaLength = String.valueOf(txtImeiLista.length);
            System.out.println("txtImeiListaLength = " + txtImeiListaLength);
      
            txtSimLista = (String[]) hshParametrosMap.get("txtSimLista"); 
            txtSimListaLength = String.valueOf(txtSimLista.length);
            System.out.println("txtSimListaLength = " + txtSimListaLength);
      
            txtSerieLista = (String[]) hshParametrosMap.get("txtSerieLista");
            txtSerieListaLength = String.valueOf(txtSerieLista.length);
            System.out.println("txtSerieListaLength = " + txtSerieListaLength);
      
            cmbTipoImeiLista = (String[]) hshParametrosMap.get("cmbTipoImeiLista");
            cmbTipoImeiListaLength = String.valueOf(cmbTipoImeiLista.length);
            System.out.println("cmbTipoImeiListaLength = " + cmbTipoImeiListaLength);
      
            chkCrearDocLista = (String[]) hshParametrosMap.get("chkCrearDocLista");
            chkCrearDocListaLength = String.valueOf(chkCrearDocLista.length);
            System.out.println("chkCrearDocListaLength = " + chkCrearDocListaLength);
      
            cmbModeloLista = (String[]) hshParametrosMap.get("cmbModeloLista");
            cmbModeloListaLength = String.valueOf(cmbModeloLista.length);
            System.out.println("cmbModeloListaLength = " + cmbModeloListaLength);
      
            cmbTipoLista = (String[]) hshParametrosMap.get("cmbTipoLista");
            cmbTipoListaLength = String.valueOf(cmbTipoLista.length);
            System.out.println("cmbTipoListaLength = " + cmbTipoListaLength);
      
            cmbFlagAccesorioLista = (String[]) hshParametrosMap.get("cmbFlagAccesorioLista");
            cmbFlagAccesorioListaLength = String.valueOf(cmbFlagAccesorioLista.length);
            System.out.println("cmbFlagAccesorioListaLength = " + cmbFlagAccesorioListaLength);
      
            chkDevolverEquipoLista = (String[]) hshParametrosMap.get("chkDevolverEquipoLista"); 
            chkDevolverEquipoListaLength  = String.valueOf(chkDevolverEquipoLista.length);
            System.out.println("chkDevolverEquipoListaLength = " + chkDevolverEquipoListaLength);
        }

        //Se ejecuta cuando se llama desde la misma pagina
        if (flagOrigen.equals("1")){
        
            System.out.println("---SEGUNDA LLLAMADA----");
            
            strFlagStock = request.getParameter("strFlagStock");
            strStockMessage = request.getParameter("strStockMessage");
            strMessage = request.getParameter("strMessage");
            flagCount = request.getParameter("flagCount");
                        
            hdnOrderId = request.getParameter("hdnOrderId");
            hdnRepairId = request.getParameter("hdnRepairId");            
            hdnReplaceType = request.getParameter("hdnReplaceType");            
            hdnLogin = request.getParameter("hdnLogin");            
            hdnFlagGenerate = request.getParameter("hdnFlagGenerate");            
            strProceso = request.getParameter("strProceso");
            
            System.out.println("---RECUPERO LOS VALORES DEL ARREGLOS---");
            //---- Obtenemos arreglos ------ 
            String hdnRepListIdLength = request.getParameter("hdnRepListIdLength");
            System.out.println("hdnRepListIdLength = " + hdnRepListIdLength);
            hdnRepListId = new String[Integer.parseInt(hdnRepListIdLength)];
            for(int i=0; i< Integer.parseInt(hdnRepListIdLength); i++){
                hdnRepListId[i] = request.getParameter("hdnRepListId_"+i); 
                System.out.println("hdnRepListId["+i+"] = "+hdnRepListId[i]);
            }
                        
            String txtImeiListaLength = request.getParameter("txtImeiListaLength");
            System.out.println("txtImeiListaLength = " + txtImeiListaLength);
            txtImeiLista = new String[Integer.parseInt(txtImeiListaLength)];
            for(int i=0; i< Integer.parseInt(txtImeiListaLength); i++){
                txtImeiLista[i] = request.getParameter("txtImeiLista_"+i);
                System.out.println("txtImeiLista["+i+"] = "+txtImeiLista[i]);
            }
             
            String txtSimListaLength = request.getParameter("txtSimListaLength");
            System.out.println("txtSimListaLength = " + txtSimListaLength);
            txtSimLista = new String[Integer.parseInt(txtSimListaLength)];
            for(int i=0; i< Integer.parseInt(txtSimListaLength); i++){
                txtSimLista[i] = request.getParameter("txtSimLista_"+i);
                System.out.println("txtSimLista["+i+"] = "+txtSimLista[i]);
            }
            
            
            String txtSerieListaLength = request.getParameter("txtSerieListaLength");
            System.out.println("txtSerieListaLength = " + txtSerieListaLength);
            txtSerieLista = new String[Integer.parseInt(txtSerieListaLength)];
            for(int i=0; i< Integer.parseInt(txtSerieListaLength); i++){
                txtSerieLista[i] = request.getParameter("txtSerieLista_"+i);
                System.out.println("txtSerieLista["+i+"] = "+txtSerieLista[i]);
            }
            
            String cmbTipoImeiListaLength = request.getParameter("cmbTipoImeiListaLength");
            System.out.println("cmbTipoImeiListaLength = " + cmbTipoImeiListaLength);
            cmbTipoImeiLista = new String[Integer.parseInt(cmbTipoImeiListaLength)];
            for(int i=0; i< Integer.parseInt(cmbTipoImeiListaLength); i++){
                cmbTipoImeiLista[i] = request.getParameter("cmbTipoImeiLista_"+i);
                System.out.println("cmbTipoImeiLista["+i+"] = "+cmbTipoImeiLista[i]);
            }
            
            
            String chkCrearDocListaLength = request.getParameter("chkCrearDocListaLength");
            System.out.println("chkCrearDocListaLength = " + chkCrearDocListaLength);
            chkCrearDocLista = new String[Integer.parseInt(chkCrearDocListaLength)];
            for(int i=0; i< Integer.parseInt(chkCrearDocListaLength); i++){
                chkCrearDocLista[i] = request.getParameter("chkCrearDocLista_"+i);
                System.out.println("chkCrearDocLista["+i+"] = "+chkCrearDocLista[i]);
            }
            
            
            String cmbModeloListaLength = request.getParameter("cmbModeloListaLength");
            System.out.println("cmbModeloListaLength = " + cmbModeloListaLength);
            cmbModeloLista = new String[Integer.parseInt(cmbModeloListaLength)];
            for(int i=0; i< Integer.parseInt(cmbModeloListaLength); i++){
                cmbModeloLista[i] = request.getParameter("cmbModeloLista_"+i);  
                System.out.println("cmbModeloLista["+i+"] = "+cmbModeloLista[i]);
            }
            
            
            String cmbTipoListaLength = request.getParameter("cmbTipoListaLength");
            System.out.println("cmbTipoListaLength = " + cmbTipoListaLength);
            cmbTipoLista = new String[Integer.parseInt(cmbTipoListaLength)];
            for(int i=0; i< Integer.parseInt(cmbTipoListaLength); i++){
                cmbTipoLista[i] = request.getParameter("cmbTipoLista_"+i); 
                System.out.println("cmbTipoLista["+i+"] = "+cmbTipoLista[i]);
            }
            
            
            String cmbFlagAccesorioListaLength = request.getParameter("cmbFlagAccesorioListaLength");
            System.out.println("cmbFlagAccesorioListaLength = " + cmbFlagAccesorioListaLength);
            cmbFlagAccesorioLista = new String[Integer.parseInt(cmbFlagAccesorioListaLength)];
            for(int i=0; i< Integer.parseInt(cmbFlagAccesorioListaLength); i++){
                cmbFlagAccesorioLista[i] = request.getParameter("cmbFlagAccesorioLista_"+i);
                System.out.println("cmbFlagAccesorioLista["+i+"] = "+cmbFlagAccesorioLista[i]);
            }
                        
            
            String chkDevolverEquipoListaLength = request.getParameter("chkDevolverEquipoListaLength");
            System.out.println("chkDevolverEquipoListaLength = " + chkDevolverEquipoListaLength);
            chkDevolverEquipoLista = new String[Integer.parseInt(chkDevolverEquipoListaLength)];
            for(int i=0; i< Integer.parseInt(chkDevolverEquipoListaLength); i++){
                chkDevolverEquipoLista[i] = request.getParameter("chkDevolverEquipoLista_"+i);  
                System.out.println("chkDevolverEquipoLista["+i+"] = "+chkDevolverEquipoLista[i]);
            }
            
            
            hshParametrosMapAux.put("strFlagStock",strFlagStock);
            
            hshParametrosMapAux.put("strStockMessage", strStockMessage);
            hshParametrosMapAux.put("strMessage", strMessage);
            hshParametrosMapAux.put("flagCount", flagCount);                        
            hshParametrosMapAux.put("hdnOrderId", hdnOrderId);
            hshParametrosMapAux.put("hdnRepairId", hdnRepairId);
            hshParametrosMapAux.put("hdnReplaceType", hdnReplaceType);
            hshParametrosMapAux.put("hdnLogin",hdnLogin);
            hshParametrosMapAux.put("hdnFlagGenerate",hdnFlagGenerate);
            hshParametrosMapAux.put("strProceso", strProceso);
                                             
            hshParametrosMapAux.put("hdnRepListId",hdnRepListId);
            hshParametrosMapAux.put("txtImeiLista",txtImeiLista);
            hshParametrosMapAux.put("txtSimLista",txtSimLista);
            hshParametrosMapAux.put("txtSerieLista",txtSerieLista);
            hshParametrosMapAux.put("cmbTipoImeiLista",cmbTipoImeiLista);
            hshParametrosMapAux.put("chkCrearDocLista",chkCrearDocLista);
            hshParametrosMapAux.put("cmbModeloLista",cmbModeloLista);
            hshParametrosMapAux.put("cmbTipoLista",cmbTipoLista);
            hshParametrosMapAux.put("cmbFlagAccesorioLista",cmbFlagAccesorioLista);
            hshParametrosMapAux.put("chkDevolverEquipoLista",chkDevolverEquipoLista);
                                    
            System.out.println("strFlagStock = "+strFlagStock);
            System.out.println("strStockMessage = "+strStockMessage);
            System.out.println("strMessage = "+strMessage);
            System.out.println("flagCount = "+flagCount);            
            
            System.out.println("hdnOrderId = "+hdnOrderId);                        
            System.out.println("hdnRepairId = "+hdnRepairId);                       
            System.out.println("hdnReplaceType = "+hdnReplaceType);                        
            System.out.println("hdnLogin = "+hdnLogin);                        
            System.out.println("hdnFlagGenerate = "+hdnFlagGenerate);                        
            System.out.println("strProceso = "+strProceso);                        
            
        }
        
        
        ///----
        System.out.println("-------------------------------");
        System.out.println("flagCount = " + flagCount);
        System.out.println("strFlagStock = " + strFlagStock);
        if("-1".equals(flagCount)){
        
            if("0".equals(MiUtil.trimNotNull(strFlagStock))){
            
                  System.out.println("Stock mayor al minimo");
                  System.out.println("Invoco al generar documento");
                  HashMap hshDataMapDoc = objRepairService.generateDocument(hshParametrosMap);
                  strMessage = (String) hshDataMapDoc.get(Constante.MESSAGE_OUTPUT);
      
        if(StringUtils.isBlank(strMessage)) {            
                      System.out.println("strMessage");
                      String url ="<script>";
                      url += "alert('Se generaron satisfactoriamente los documentos');"; 
                      url += "strUrl='/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+hdnOrderId+"&av_execframe=BOTTOMFRAME';"; 
                      url += "location.replace(strUrl);";
                      url +="</script>";
                      out.println(url);
                  }
        else{
                      System.out.println("strMessage not blank");
                      String url ="<script>";
                      url += "alert('Hubo un error al generar los documentos: "+MiUtil.getMessageClean(strMessage)+"');";
                      url += "parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;";
                      url +="</script>";
                      out.println(url);
                  }
            }
            
            if("2".equals(MiUtil.trimNotNull(strFlagStock))){
                System.out.println("No hay stock");
                String url ="<script>";
                url += "alert('"+MiUtil.getMessageClean(strStockMessage)+"');";
                url += "parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;";
                url += "</script>";
                out.println(url);                
            }            
        }
        
        //Se hizo click en el boton aceptar del confirm
        if("10".equals(flagCount)){
            if("1".equals(strFlagStock)){
                System.out.println("Stock es menor al minimo"); 
                System.out.println("Invoco al generar documento");
                HashMap hshDataMapDoc = objRepairService.generateDocument(hshParametrosMapAux);
                strMessage = (String) hshDataMapDoc.get(Constante.MESSAGE_OUTPUT);
                System.out.println("strMessage = " + strMessage);  
                if("".equals(MiUtil.trimNotNull(strMessage)) ) {            
                      
                      String url ="<script>";
                      url += "alert('Se generaron satisfactoriamente los documentos');"; 
                      url += "strUrl='/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+hdnOrderId+"&av_execframe=BOTTOMFRAME';"; 
                      url += "location.replace(strUrl);";
                      url +="</script>";
                      out.println(url);
        }
      else{
                      System.out.println("strMessage not blank");
                      String url ="<script>";
                      url += "alert('Hubo un error al generar los documentos: "+MiUtil.getMessageClean(strMessage)+"');";
                      url += "parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;";
                      url +="</script>";
                      out.println(url);
               }
            }
        }
         
        
        
        
                
        %>		

<html>
	<head>
	</head>
	<body >
    <form name="formulario" ></form>
	</body>
  
  <script type="text/javascript">
    
    var flagCount = '<%=flagCount%>';
    var strFlagStock = '<%=strFlagStock%>';
        
   
    //No hay problemas en la validación de Stock.
    if(flagCount =="-1"){
        if(strFlagStock == "1"){ //El Stock es menor al minimo
            
            if(confirm('<%=MiUtil.getMessageClean(strStockMessage)%>')){
                                
                var strUrl = "<%=request.getContextPath()%>" + "/pages/manageListaPrestamosCambios.jsp"; 
                // Creamos el formulario auxiliar
                
                var formulario = document.formulario;
                // Le añadimos atributos como el name, action y el method
                formulario.setAttribute( "name", "formulario" );
                formulario.setAttribute( "action", strUrl );
                formulario.setAttribute( "method", "post" );
                
                // Creamos un input para enviar el valor
                var flagOrigen = document.createElement("input");
                var strFlagStock = document.createElement("input");
                var flagCount = document.createElement("input");
                
                var strMessage = document.createElement("input");
                
                var hdnOrderId = document.createElement("input");
                var hdnRepairId = document.createElement("input");
                var hdnReplaceType = document.createElement("input");
                var hdnLogin	 = document.createElement("input");
                var hdnFlagGenerate = document.createElement("input");
                var strProceso = document.createElement("input");
                
                var hdnRepListIdLength = document.createElement("input");
                var txtImeiListaLength = document.createElement("input");
                var txtSimListaLength = document.createElement("input");
                var txtSerieListaLength = document.createElement("input");
                var cmbTipoImeiListaLength = document.createElement("input");
                var chkCrearDocListaLength = document.createElement("input");
                var cmbModeloListaLength = document.createElement("input");
                var cmbTipoListaLength = document.createElement("input");
                var cmbFlagAccesorioListaLength = document.createElement("input");
                var chkDevolverEquipoListaLength = document.createElement("input");
                
                // Le añadimos atributos como el name, type y el value
                
                flagCount.setAttribute( "name", "flagCount" );
                flagCount.setAttribute( "type", "hidden" );
                flagCount.setAttribute( "value", "10" );
                formulario.appendChild( flagCount );
                                
                flagOrigen.setAttribute( "name", "flagOrigen" );
                flagOrigen.setAttribute( "type", "hidden" );
                flagOrigen.setAttribute( "value", "1" );
                formulario.appendChild( flagOrigen );
                                
                strFlagStock.setAttribute( "name", "strFlagStock" );
                strFlagStock.setAttribute( "type", "hidden" );
                strFlagStock.setAttribute( "value", "1" );
                formulario.appendChild( strFlagStock );
                                
                strMessage.setAttribute( "name", "strMessage" );
                strMessage.setAttribute( "type", "hidden" );
                strMessage.setAttribute( "value", "<%=strMessage%>" );                
                formulario.appendChild( strMessage );
                                
                hdnOrderId.setAttribute( "name", "hdnOrderId" );
                hdnOrderId.setAttribute( "type", "hidden" );
                hdnOrderId.setAttribute( "value", "<%=hdnOrderId%>" );
                formulario.appendChild( hdnOrderId );
                
                hdnRepairId.setAttribute( "name", "hdnRepairId" );
                hdnRepairId.setAttribute( "type", "hidden" );
                hdnRepairId.setAttribute( "value", "<%=hdnRepairId%>" );
                formulario.appendChild(hdnRepairId);
                
                hdnReplaceType.setAttribute( "name", "hdnReplaceType" );
                hdnReplaceType.setAttribute( "type", "hidden" );
                hdnReplaceType.setAttribute( "value", "<%=hdnReplaceType%>" );
                formulario.appendChild(hdnReplaceType);
                
                hdnLogin.setAttribute( "name", "hdnLogin" );
                hdnLogin.setAttribute( "type", "hidden" );
                hdnLogin.setAttribute( "value", "<%=hdnLogin%>" );
                formulario.appendChild(hdnLogin);
                
                hdnFlagGenerate.setAttribute( "name", "hdnFlagGenerate" );
                hdnFlagGenerate.setAttribute( "type", "hidden" );
                hdnFlagGenerate.setAttribute( "value", "<%=hdnFlagGenerate%>");
                formulario.appendChild(hdnFlagGenerate);
                
                strProceso.setAttribute( "name", "strProceso" );
                strProceso.setAttribute( "type", "hidden" );
                strProceso.setAttribute( "value", "<%=strProceso%>" );
                formulario.appendChild(strProceso);
                
                hdnRepListIdLength.setAttribute( "name", "hdnRepListIdLength" );
                hdnRepListIdLength.setAttribute( "type", "hidden" );
                hdnRepListIdLength.setAttribute( "value", "<%=hdnRepListIdLength%>" );
                formulario.appendChild(hdnRepListIdLength);
                
                txtImeiListaLength.setAttribute( "name", "txtImeiListaLength" );
                txtImeiListaLength.setAttribute( "type", "hidden" );
                txtImeiListaLength.setAttribute( "value", "<%=txtImeiListaLength%>" );
                formulario.appendChild(txtImeiListaLength);
                
                txtSimListaLength.setAttribute( "name", "txtSimListaLength" );
                txtSimListaLength.setAttribute( "type", "hidden" );
                txtSimListaLength.setAttribute( "value", "<%=txtSimListaLength%>" );
                formulario.appendChild(txtSimListaLength);
                
                txtSerieListaLength.setAttribute( "name", "txtSerieListaLength" );
                txtSerieListaLength.setAttribute( "type", "hidden" );
                txtSerieListaLength.setAttribute( "value", "<%=txtSerieListaLength%>" );
                formulario.appendChild(txtSerieListaLength);
                
                cmbTipoImeiListaLength.setAttribute( "name", "cmbTipoImeiListaLength" );
                cmbTipoImeiListaLength.setAttribute( "type", "hidden" );
                cmbTipoImeiListaLength.setAttribute( "value", "<%=cmbTipoImeiListaLength%>" );
                formulario.appendChild(cmbTipoImeiListaLength);
                
                chkCrearDocListaLength.setAttribute( "name", "chkCrearDocListaLength" );
                chkCrearDocListaLength.setAttribute( "type", "hidden" );
                chkCrearDocListaLength.setAttribute( "value", "<%=chkCrearDocListaLength%>" );               
                formulario.appendChild(chkCrearDocListaLength);
                
                cmbModeloListaLength.setAttribute( "name", "cmbModeloListaLength" );
                cmbModeloListaLength.setAttribute( "type", "hidden" );
                cmbModeloListaLength.setAttribute( "value", "<%=cmbModeloListaLength%>" ); 
                formulario.appendChild(cmbModeloListaLength);
                
                cmbTipoListaLength.setAttribute( "name", "cmbTipoListaLength" );
                cmbTipoListaLength.setAttribute( "type", "hidden" );
                cmbTipoListaLength.setAttribute( "value", "<%=cmbTipoListaLength%>" );
                formulario.appendChild(cmbTipoListaLength);
                
                cmbFlagAccesorioListaLength.setAttribute( "name", "cmbFlagAccesorioListaLength" );
                cmbFlagAccesorioListaLength.setAttribute( "type", "hidden" );
                cmbFlagAccesorioListaLength.setAttribute( "value", "<%=cmbFlagAccesorioListaLength%>" );
                formulario.appendChild(cmbFlagAccesorioListaLength);
                
                chkDevolverEquipoListaLength.setAttribute( "name", "chkDevolverEquipoListaLength" );
                chkDevolverEquipoListaLength.setAttribute( "type", "hidden" );
                chkDevolverEquipoListaLength.setAttribute( "value", "<%=chkDevolverEquipoListaLength%>" );
                formulario.appendChild(chkDevolverEquipoListaLength);
                
                
                <%
                    for(int i=0; i< hdnRepListId.length; i++){
                        
                        out.println("var hdnRepListId_"+ i +" = document.createElement('input');");
                        out.println("hdnRepListId_"+ i +".setAttribute('name','hdnRepListId_"+i+"');");
                        out.println("hdnRepListId_"+ i +".setAttribute('type', 'hidden');");
                        out.println("hdnRepListId_"+ i +".setAttribute( 'value', '"+hdnRepListId[i]+"');");
                        out.println("formulario.appendChild(hdnRepListId_"+ i +");");
                    }
                %>
                
                              
                <%
                    for(int i=0; i< txtImeiLista.length; i++){
                        
                        out.println("var txtImeiLista_"+ i +" = document.createElement('input');");
                        out.println("txtImeiLista_"+ i +".setAttribute('name','txtImeiLista_"+i+"');");
                        out.println("txtImeiLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("txtImeiLista_"+ i +".setAttribute( 'value', '"+txtImeiLista[i]+"');");
                        out.println("formulario.appendChild(txtImeiLista_"+ i +");");
                    }
                %>               
                
                           
                <%
                    for(int i=0; i< txtSimLista.length; i++){
                        //out.println("parametros += '&txtSimLista_"+ i +"="+txtSimLista[i]+"';");
                        out.println("var txtSimLista_"+ i +" = document.createElement('input');");
                        out.println("txtSimLista_"+ i +".setAttribute('name','txtSimLista_"+i+"');");
                        out.println("txtSimLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("txtSimLista_"+ i +".setAttribute( 'value', '"+txtSimLista[i]+"');");
                        out.println("formulario.appendChild(txtSimLista_"+ i +");");
                    }
                %>
                 
                          
                <%
                    for(int i=0; i< txtSerieLista.length; i++){
                        //out.println("parametros += '&txtSerieLista_"+ i +"="+txtSerieLista[i]+"';");                                                
                        out.println("var txtSerieLista_"+ i +" = document.createElement('input');");
                        out.println("txtSerieLista_"+ i +".setAttribute('name','txtSerieLista_"+i+"');");
                        out.println("txtSerieLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("txtSerieLista_"+ i +".setAttribute( 'value', '"+txtSimLista[i]+"');");
                        out.println("formulario.appendChild(txtSerieLista_"+ i +");");                        
                    }
                %>
                
                            
                <%
                    for(int i=0; i< cmbTipoImeiLista.length; i++){
                        //out.println("parametros += '&cmbTipoImeiLista_"+ i +"="+cmbTipoImeiLista[i]+"';");
                        out.println("var cmbTipoImeiLista_"+ i +" = document.createElement('input');");
                        out.println("cmbTipoImeiLista_"+ i +".setAttribute('name','cmbTipoImeiLista_"+i+"');");
                        out.println("cmbTipoImeiLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("cmbTipoImeiLista_"+ i +".setAttribute( 'value', '"+cmbTipoImeiLista[i]+"');");
                        out.println("formulario.appendChild(cmbTipoImeiLista_"+ i +");"); 
                    }
                %>
                
                        
                <%
                    for(int i=0; i< chkCrearDocLista.length; i++){
                        //out.println("parametros += '&chkCrearDocLista_"+ i +"="+chkCrearDocLista[i]+"';");
                        out.println("var chkCrearDocLista_"+ i +" = document.createElement('input');");
                        out.println("chkCrearDocLista_"+ i +".setAttribute('name','chkCrearDocLista_"+i+"');");
                        out.println("chkCrearDocLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("chkCrearDocLista_"+ i +".setAttribute( 'value', '"+chkCrearDocLista[i]+"');");
                        out.println("formulario.appendChild(chkCrearDocLista_"+ i +");"); 
                    }
                %>
                
                              
                <%
                    for(int i=0; i< cmbModeloLista.length; i++){
                        //out.println("parametros += '&cmbModeloLista_"+ i +"="+cmbModeloLista[i]+"';");
                        out.println("var cmbModeloLista_"+ i +" = document.createElement('input');");
                        out.println("cmbModeloLista_"+ i +".setAttribute('name','cmbModeloLista_"+i+"');");
                        out.println("cmbModeloLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("cmbModeloLista_"+ i +".setAttribute( 'value', '"+cmbModeloLista[i]+"');");
                        out.println("formulario.appendChild(cmbModeloLista_"+ i +");"); 
                    }
                %>
                             
                <%
                    for(int i=0; i< cmbTipoLista.length; i++){
                        
                        out.println("var cmbTipoLista_"+ i +" = document.createElement('input');");
                        out.println("cmbTipoLista_"+ i +".setAttribute('name','cmbTipoLista_"+i+"');");
                        out.println("cmbTipoLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("cmbTipoLista_"+ i +".setAttribute( 'value', '"+cmbTipoLista[i]+"');");
                        out.println("formulario.appendChild(cmbTipoLista_"+ i +");"); 
                    }
                %>
                          
                <%
                    for(int i=0; i< cmbFlagAccesorioLista.length; i++){
                        
                        out.println("var cmbFlagAccesorioLista_"+ i +" = document.createElement('input');");
                        out.println("cmbFlagAccesorioLista_"+ i +".setAttribute('name','cmbFlagAccesorioLista_"+i+"');");
                        out.println("cmbFlagAccesorioLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("cmbFlagAccesorioLista_"+ i +".setAttribute( 'value', '"+cmbFlagAccesorioLista[i]+"');");
                        out.println("formulario.appendChild(cmbFlagAccesorioLista_"+ i +");"); 
                    }
                %>
                          
                <%
                    for(int i=0; i< chkDevolverEquipoLista.length; i++){
                        
                        out.println("var chkDevolverEquipoLista_"+ i +" = document.createElement('input');");
                        out.println("chkDevolverEquipoLista_"+ i +".setAttribute('name','chkDevolverEquipoLista_"+i+"');");
                        out.println("chkDevolverEquipoLista_"+ i +".setAttribute('type', 'hidden');");
                        out.println("chkDevolverEquipoLista_"+ i +".setAttribute( 'value', '"+chkDevolverEquipoLista[i]+"');");
                        out.println("formulario.appendChild(chkDevolverEquipoLista_"+ i +");"); 
                    }
                %>
                
                // Hacemos submit
                document.formulario.submit();
                    
            }else{
                parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false; 
                
            }
        }
        
    }
    
    //chkCrearDocLista = null
    if(flagCount == "2"){
					alert('<%=MiUtil.getMessageClean(strMessage)%>');
          parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;
		  }
    
    //No invoca a generar documento.
    if(flagCount == "0"){
         alert("Para generar Documentos, necesita añadir y seleccionar al menos un item de Préstamo & Cambio");
         parent.mainFrame.document.frmdatos.btnGenerarDocumento.disabled=false;                           
    }
      
    
    
		
		</script>
  
  
  <%
  }catch(Exception ex){
      System.out.println("Error jsp: " + ex.getMessage());
  }
  %>
</html>