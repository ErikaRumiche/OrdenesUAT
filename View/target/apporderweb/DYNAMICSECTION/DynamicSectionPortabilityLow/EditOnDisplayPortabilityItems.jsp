<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="java.io.*"%>

<%
try{
      
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));       
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));         
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));  
         
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   String strLogin=objPortalSesBean.getLogin(); 
         
   ArrayList valoresCombo = new ArrayList();
   ArrayList arrComboList = new ArrayList();   
   HashMap hshComboMap = new HashMap();
   PortabilityOrderBean objPOBean = new PortabilityOrderBean();
   String strMessage=null;
   HashMap hshWsMap = new HashMap();
   HashMap hshExtensionFile = new HashMap();
   PortabilityOrderBean objPOrderBean =  new PortabilityOrderBean();
   PortabilityOrderBean objPOrdBean =  new PortabilityOrderBean();
   PortabilityOrderService objPortabOrderService = new PortabilityOrderService();
            
%>

   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   <script type="text/javascript" defer="defer" >
   
                  
      function fxSectionPortabilityLowValidate() {      
         var vForm = document.frmdatos;        
         var total = eval(tblListaPortabilityLow.rows.length);         
         var vboton = vForm.cmbAction.value;
                                                                                                                                                                                                                              
         if(total==0){
            alert("No hay data");
            return false;
         }
         
       if (vboton != "<%=Constante.ACTION_INBOX_ANULAR%>" ){           
         if (total == 2){    
            if( vForm.cmbEvalSolBaja.value != ""){
               if( vForm.cmbEvalSolBaja.value == "01"){ // Si es objeción
                  if (vForm.cmbMotivos.value == ""){                    
                     alert("Debe elegir un motivo de objeción");
                     vForm.cmbMotivos.focus();
                     return false;                                                    
                  } 
                  if (vForm.cmbDocAtatchment.value == ""){                          
                     alert("Debe elegir un documento a adjuntar");
                     vForm.cmbDocAtatchment.focus();
                     return false;                                                    
                  }
                  if (vForm.txt_ruta.value == ""){                        
                     if (vForm.av_ruta.value == ""){                          
                        alert("Debe de ingresar la ruta del documento a adjuntar");
                        vForm.av_ruta.focus();
                        return false;
                     }
                  }
                  
                  if (vForm.av_ruta.value != ""){
                     var ruta = vForm.av_ruta.value;
                     var indexExt = ruta.lastIndexOf(".");
                     var strExtension = ruta.substring(indexExt+1);
                     <%
                        hshExtensionFile = objPortabOrderService.getConfigFile("EXTENSION_DOCUMENT_LOW");
                        strMessage = (String) hshExtensionFile.get("strMessage");
                        
                        if(!StringUtils.isNotBlank(strMessage)){
                          objPOrderBean = (PortabilityOrderBean)hshExtensionFile.get("objPOBean");
                          String extFile = (String)objPOrderBean.getNpconfigFile();
                      %>
                          if(strExtension != "<%=extFile%>"){
                            alert("Sólo se debe cargar archivos (pdf)");
                            vForm.av_ruta.focus();
                            vForm.av_ruta.style.backgroundColor = "#F3F781";
                            return false;
                          }
                      <%
                        }
                      %>
                     vForm.av_ruta.style.backgroundColor = "#FFFFFF";
                  }
               }                                    
            }else{
                  
                  if (vForm.hdnMessageStatus.value == "03A01" || vForm.hdnMessageStatus.value == "01D05"){                                                      
                     alert("Debe ingresar la evaluación de la solicitud ");
                     vForm.cmbEvalSolBaja.focus();
                     return false;                     
                  }   
            }  
               
         }else{
                                                                                                                                                               
            for(var i=0; i<total-1; i++){                                                    
               if( vForm.cmbEvalSolBaja[i].value != ""){
                  if( vForm.cmbEvalSolBaja[i].value == "01"){ // Si es objeción
                     if (vForm.cmbMotivos[i].value == ""){                    
                        alert("Debe elegir un motivo de objeción");
                        vForm.cmbMotivos[i].focus();
                        return false;                                                    
                     } 
                     if (vForm.cmbDocAtatchment[i].value == ""){                          
                        alert("Debe elegir un documento a adjuntar");
                        vForm.cmbDocAtatchment[i].focus();
                        return false;                                                    
                     }
                     if (vForm.txt_ruta[i].value == ""){                          
                        if (vForm.av_ruta[i].value == ""){                          
                           alert("Debe de ingresar la ruta del documento a adjuntar");
                           vForm.av_ruta[i].focus();
                           return false;                                                    
                        }                              
                     }
                     
                     if (vForm.av_ruta[i].value != ""){
                        var ruta = vForm.av_ruta[i].value;
                        var indexExt = ruta.lastIndexOf(".");
                        var strExtension = ruta.substring(indexExt+1);
                        <%
                          hshExtensionFile = objPortabOrderService.getConfigFile("EXTENSION_DOCUMENT_LOW");
                          strMessage = (String) hshExtensionFile.get("strMessage");
                          
                          if(!StringUtils.isNotBlank(strMessage)){
                            objPOrderBean = (PortabilityOrderBean)hshExtensionFile.get("objPOBean");
                            String extFile = (String)objPOrderBean.getNpconfigFile();
                        %>
                            if(strExtension != "<%=extFile%>"){
                              alert("Sólo se debe cargar archivos (pdf)");
                              vForm.av_ruta[i].focus();
                              vForm.av_ruta[i].style.backgroundColor = "#F3F781";
                              return false;
                            }
                        <%
                          }
                        %>
                        vForm.av_ruta[i].style.backgroundColor = "#FFFFFF";
                     }
                  }                                    
               }else{                                 
                  if (vForm.hdnMessageStatus[i].value == "03A01" || vForm.hdnMessageStatus[i].value == "01D05"){                  
                     alert("Debe ingresar la evaluación de la solicitud ");
                     vForm.cmbEvalSolBaja[i].focus();
                     return false;                  
                  }                                    
               }               
            }
         }
         
         return true;     
      }                  
    }                        
      LoadTable();       
       
      function LoadTable(){
         var vForm = document.frmdatos;        
         vForm.hdntblListaPortabilityLow.value = tblListaPortabilityLow.rows.length;                     
      }
      
      
      fxShowItemsobjection();
      
      function fxPortabilityBajaDoc(indice) {
        var vForm = document.frmdatos;
	
	var numFilas = eval(tblListaPortabilityLow.rows.length);	
	
	if (numFilas == 2){
          var ruta = vForm.txt_ruta.value;
        }else if(numFilas > 2){         
          var ruta = vForm.txt_ruta[indice-1].value;
        }
	
        //var ruta = vForm.txt_ruta.value;
        //alert(ruta);
        var winUrl = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=uploadDocumentBaja&hdnruta="+ruta;
        window.open(winUrl, "Portabilidad_Alta","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
      }
      
                                          
   </script>
      <br>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">               
         <tr>
            <td align="left">
               <table border="0" cellspacing="0" cellpadding="0" align="left">
               <tr>
                  <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
                  <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Baja - Detalle</td>
                  <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
                  <input type="hidden" name="hdntblListaPortabilityLow">                                                   
               </tr>
               </table>
            </td>
         </tr>         
         <tr>
            <td align="center">                       
               <table id="tblListaPortabilityLow" border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">                                                                              
                  <tr>				
                     <td class="CellLabel" align="center" width="3%">#</td>
                     <td class="CellLabel" align="center" width="5%">Nro. Telefónico</td> 
                     <td class="CellLabel" align="center" width="5%">Id. Solicitud</td> 
                     <td class="CellLabel" align="center" width="5%">Estado Contrato</td>
                     <td class="CellLabel" align="center" width="8%">Motivo Suspensión / Desactivación</td>                       
                     <td class="CellLabel" align="center" width="8%">Días Transcurridos</td>                       
                     <td class="CellLabel" align="center" width="15%">Ultimo Estado Proceso Portabilidad</td>
                     <td class="CellLabel" align="center" width="15%">Error No Integridad</td>
                     <!--<td class="CellLabel" align="center" width="12%">Eval.Solic.Baja. Portab.</td>-->
                     <td class="CellLabel" align="center" width="8%">Motivo Objeción</td>
                     <!--LROSALES-P1D-->
                     <td class="CellLabel" align="center" width="8%">&nbsp;Monto Adeudado</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Tipo Moneda</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Fec. Venc. Ult. Recibo</td>
                     <td class="CellLabel" align="center" width="8%">&nbsp;Fecha de Ejecución</td>
                    <!--LROSALES-P1D-->
                     <!--Borrar P1D <td class="CellLabel" align="center" width="15%">DocumentoAdjuntar</td>
                     <td class="CellLabel" align="center" width="6%">Ruta</td>                     
                     <td class="CellLabel" align="center" width="1%">&nbsp;</td>
                     <td class="CellLabel" align="center" width="1%">Fec. Ejec.</td>                     
                     <td class="CellLabel" align="center" width="1%">Programación en BSCS</td>
                     
                     <td class="CellLabel" align="center" width="5%">Num. a Programar</td>-->
                  </tr>         
         <%	            
            PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();                        
            HashMap hshPortabilityLowListMap = objPortabilityOrderService.getPortabilityLowByOrder(lOrderId);            
            ArrayList arrPortabilityLowList = (ArrayList) hshPortabilityLowListMap.get("arrPortabilityLowList");
            Iterator itPortabilityLowList = arrPortabilityLowList.iterator();
            int index = 0;
            int cont = 0;
            int contAux = 0;                                         
            while(itPortabilityLowList.hasNext()) {
               HashMap hshPortabilityLowMap = (HashMap) itPortabilityLowList.next();
               //String strRepairReplaceId = (String) hshRepairReplaceMap.get("npreplistid");	
               if(StringUtils.isNotEmpty((String) hshPortabilityLowMap.get("num_tel"))) { %>
                  <tr height="20px">                                                               
                     <td class="CellContent" align="center" >&nbsp; <%=index+1%></td>                                       
                     
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("num_tel")%>                                            
                      <input type="hidden" name="hdnPortabitemid" value="<%=hshPortabilityLowMap.get("npportabitemid")%>" >                        
                     </td>
                     
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("npapplicationid")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("est_contract")%></td>
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("mot_susp")%></td>                                              
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("dias_transc")%></td>                                                                      
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("ult_est_proc_port")%></td>                      
                     <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("error_no_integridad")%></td>                                                                                                                                              
                        <input type="hidden" name="hdnMessageStatus" value="<%=hshPortabilityLowMap.get("messages_status")%>" >
                     
                     <!-- Comentado por P1D                                                                                                
                     <%if( (hshPortabilityLowMap.get("messages_status").equals("03A01")) || (hshPortabilityLowMap.get("messages_status").equals("01D05"))  )  {  %>                          
                                                                                                                                                                                                                                                                                                 
                        <td class="CellContent" align="center">                                    
                           <select name="cmbEvalSolBaja" style="width: 100%" onChange="javascript:fxShowItemsobjection();" >
                              <%hshComboMap = objPortabilityOrderService.getDominioList("EVAL_PORTABILITY_LOW");
                              valoresCombo = (ArrayList) hshComboMap.get("arrDominioList");
                              strMessage = (String) hshComboMap.get(Constante.MESSAGE_OUTPUT);
                              if (StringUtils.isBlank(strMessage)){%>               
                                 <%=MiUtil.buildComboSelected(valoresCombo, MiUtil.getString((String) hshPortabilityLowMap.get("eval_sol_baja")) )%>                                  
                                 <%}%>                                                                                                                                                               
                           </select>                                                                               
                              <input type="hidden" name="hdncmbEvalSolBaja" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("eval_sol_baja"))%>" >                                                                              
                        </td> 
                    
                        <td class="CellContent" align="center">                                                   
                           <select name="cmbMotivos" align="center" onChange="fxLoadComboLoadAtatch(this.value,<%=index%>);"  onblur="javascript:fxShowItemsobjection();">
                              <% hshComboMap = objPortabilityOrderService.getMotivos((String)hshPortabilityLowMap.get("est_contract"));
                              strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                              if (strMessage!=null) throw new Exception(strMessage);                     
                                 arrComboList = (ArrayList)hshComboMap.get("arrMotivos");%>
                              <%=MiUtil.buildComboSelected(arrComboList, MiUtil.getString((String) hshPortabilityLowMap.get("motivos")))%>
                           </select>                                                
                           <input type="hidden" name="hdncmbMotivos" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("motivos"))%>" >                           
                        </td>

                        <td class="CellContent" align="left">                        
                           <select name="cmbDocAtatchment" align="left" onChange="javascript:fxShowItemsobjection();" style="width: 100%">   
                              <% hshComboMap = objPortabilityOrderService.getAtatchment_by_mo( (String) hshPortabilityLowMap.get("motivos") );
                                 strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                                 if (strMessage!=null) throw new Exception(strMessage);                     
                                    arrComboList = (ArrayList)hshComboMap.get("arrAtatchment_by_mo");%>
                                 <%=MiUtil.buildComboSelected(arrComboList, MiUtil.getString((String) hshPortabilityLowMap.get("doc_atatchment"))  )%>                                 
                           </select>                                
                           <input type="hidden" name="hdncmbDocAtatchment" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("doc_atatchment"))%>" >                           
                       </td>
                       
                        <td class="CellContent" align="center" > 
                           <a href="javascript:fxPortabilityBajaDoc(<%=index%>);"><%=hshPortabilityLowMap.get("ruta")%></a>
                           <input type="hidden" name="txt_ruta" value="<%=hshPortabilityLowMap.get("ruta")%>">
                           <input type="hidden" name="hdntxt_ruta" value="" >                                                      
                        </td>
                                                                                                            
                        <td class="CellContent" align="center" >                                                                                                                                                                     
                           <input type="file" name="av_ruta" onblur="javascript:fxShowItemsobjection();">                             
                           <input type="hidden" name="hdnav_ruta" value="" >                           
                        </td>                                                                                                             
                                                                                                                                                           
                       <%} else{%>
                           
                           <td class="CellContent" align="center" >                                    
                           <select name="cmbEvalSolBaja" style="width: 100%" onChange="javascript:fxShowItemsobjection();" disabled>
                              <%hshComboMap = objPortabilityOrderService.getDominioList("EVAL_PORTABILITY_LOW");
                              valoresCombo = (ArrayList) hshComboMap.get("arrDominioList");
                              strMessage = (String) hshComboMap.get(Constante.MESSAGE_OUTPUT);
                              if (StringUtils.isBlank(strMessage)){%>               
                                 <%=MiUtil.buildComboSelected(valoresCombo, MiUtil.getString((String) hshPortabilityLowMap.get("eval_sol_baja")) )%>                                  
                                 <%}%>
                           </select>
                           <input type="hidden" name="hdncmbEvalSolBaja" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("eval_sol_baja"))%>" >
                                                                               
                        </td>-->
                        <!--<%}%>-->
                        <!-- Motivos de Objeción -->
                        <td class="CellContent" align="center">                                                   
                           <select name="cmbMotivos" align="center" onChange="javascript:fxShowItemsobjection();" disabled>
                              <%
                              //hshComboMap = objPortabilityOrderService.getMotivos((String)hshPortabilityLowMap.get("est_contract"));
                              hshComboMap = objPortabilityOrderService.getMotivosByTypeObjection((String)hshPortabilityLowMap.get("motivos"));

                              strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                              if (strMessage!=null) throw new Exception(strMessage);                     
                                 arrComboList = (ArrayList)hshComboMap.get("arrMotivos");%>
                              <%=MiUtil.buildComboSelected(arrComboList, MiUtil.getString((String) hshPortabilityLowMap.get("motivos")))%>
                           </select>                                                
                           <input type="hidden" name="hdncmbMotivos" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("motivos"))%>" >                           
                        </td>                                                                                                                                    				
			<!-- Motivos Adeuda / Tipo Moneda / Fecha Vencimiento Recibo / Fecha de Ejecución -->
                        <!--AGREGAR LOS CAMPOS NUEVOS, P1D-->
                        <td class="CellContent" align="center" >&nbsp;<%=hshPortabilityLowMap.get("npamountdue")%></td>
                        <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getString((String)hshPortabilityLowMap.get("npcurrencytypedesc"))%></td>
                        <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getDate((Date)hshPortabilityLowMap.get("npexpirationdatereceipt"),"dd/MM/yyy")%></td>
                        <td class="CellContent" align="center" >&nbsp;<%=MiUtil.getDate((Date)hshPortabilityLowMap.get("npreleasedate"),"dd/MM/yyy")%></td>
                        <!--AGREGAR LOS CAMPOS NUEVOS, P1D-->
                        
                        <!-- Comentado por P1D
                        
                        <td class="CellContent" align="center" >                        
                           <select name="cmbDocAtatchment" onChange="javascript:fxShowItemsobjection();" disabled>   
                              <% hshComboMap = objPortabilityOrderService.getDocAtatchment();
                                 strMessage = (String)hshComboMap.get(Constante.MESSAGE_OUTPUT);
                                 if (strMessage!=null) throw new Exception(strMessage);                     
                                    arrComboList = (ArrayList)hshComboMap.get("arrDocAtatchment");%>
                                 <%=MiUtil.buildComboSelected(arrComboList, MiUtil.getString((String) hshPortabilityLowMap.get("doc_atatchment"))  )%>
                           </select>                         
                           <input type="hidden" name="hdncmbDocAtatchment" value="<%= MiUtil.getString((String) hshPortabilityLowMap.get("doc_atatchment"))%>" >                           
                        </td>

                        <td class="CellContent" align="center" >                                                                                                                                                                     
                           <a href="javascript:fxPortabilityBajaDoc(<%=index%>);"><%=hshPortabilityLowMap.get("ruta")%></a>
                           <input type="hidden" name="txt_ruta" value="<%=hshPortabilityLowMap.get("ruta")%>">
                           <input type="hidden" name="hdntxt_ruta" value=""> 
                        </td>
                                                                                                            
                        <td class="CellContent" align="center" >                                                                                                                                                                     
                           <input type="file" name="av_ruta" onblur="javascript:fxShowItemsobjection();" disabled >                             
                           <input type="hidden" name="hdnav_ruta" value="" >                           
                        </td>                                                                        
                                                
                       <%//}%>
                        <!--<td class="CellContent" align="center">
                            <input type="text" name="txtExecDeadLine" value="<%=MiUtil.getString((String) hshPortabilityLowMap.get("fec_lim_ejec"))%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                        </td>-->
                        <!--<td class="CellContent" align="center">
                            <input type="text" name="txtExecutionDate" value="<%=MiUtil.getString((String) hshPortabilityLowMap.get("fec_ejec"))%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                        </td>
                        
                        <td class="CellContent" align="center">
                            <input type="text" name="txtProgBscs" value="<%=MiUtil.getString((String) hshPortabilityLowMap.get("np_delaybscs"))%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                        </td>  
                        
                        <!-- Se agrega campo para mostrar número con world number-->
                        <!--                       
                        <td class="CellContent" align="center">
                            <input type="text" name="txtNumTelWN" value="<%=MiUtil.getString((String) hshPortabilityLowMap.get("num_tel_wn"))%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                        </td>-->
                        
                  </tr>
               <% index++; }
                  }
               %>               
            </table>      
         </td>
      </tr>
   </table>
            
      <script>   
      
        function fxValidateSizeDocument(indice){
          /*var vForm = document.frmdatos; 
          var numFilas = eval(tblListaPortabilityLow.rows.length);
          if (numFilas == 2){
            var rutaFile = vForm.av_ruta.value;
            if(rutaFile != ''){
              //alert(indice);
              vForm.target="bottomFrame";
              vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=validateSizeDocumentLow&rutaFile="+rutaFile;
              vForm.submit();
            }
          }else if(numFilas > 2){
            var rutaFile = vForm.av_ruta[indice-1].value;
            if(rutaFile != ''){
              //alert(indice);
              vForm.target="bottomFrame";
              vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=validateSizeDocumentLow&rutaFile="+rutaFile;
              vForm.submit();
            }
          }*/
        }
               
         function fxValidateMotivos(Objvalue){            
            /*vForm = document.frmdatos;      
            var Motivo = "";             
            var numRow = eval(tblListaPortabilityLow.rows.length);
            
            if (numRow == 2){
               vForm.hdncmbMotivos.value = Objvalue;                     
            }
            
            else if(numRow > 2){                                      
               for(var i=0 ;i<numRow - 1;i++){
                  EvalSolBaja = vForm.cmbEvalSolBaja[i].value;                 
                  if ( (EvalSolBaja == '00') || ( EvalSolBaja == "") ) {                                                        
                     vForm.hdncmbMotivos[i].value = "d";                                    
                  }else{
                     vForm.hdncmbMotivos[i].value = Objvalue;                  
                  }
               
               }            
            }*/
            
         }
         
         function fxValidaDocAtatch(Objvalue){            
            /*vForm = document.frmdatos;      
            var EvalSolBaja = "";             
            var numRow = eval(tblListaPortabilityLow.rows.length);
            
            if (numRow == 2){
               vForm.hdncmbDocAtatchment.value = Objvalue;               
            }
            
            else if (numRow > 2){            
               for(var i=0 ;i<numRow - 1;i++){
                  vForm.hdncmbDocAtatchment[i].value = Objvalue;
               }
            }*/
         }
                
         function fxValidaRuta(Objvalue){
            /*vForm = document.frmdatos;      
            var EvalSolBaja = "";             
            var numRow = eval(tblListaPortabilityLow.rows.length);
            
            if (numRow == 2){
               vForm.hdnav_ruta.value = Objvalue;
            }                        
            else if (numRow > 2){            
               for(var i=0 ;i<numRow - 1;i++){
                  vForm.hdnav_ruta[i].value = Objvalue;
               }
            }*/
         }
         
         function fxShowItemsobjection() { 
            vForm = document.frmdatos;      
            var EvalSolBaja = "";             
            var numRow = eval(tblListaPortabilityLow.rows.length);
           
            if(numRow==0){
               alert("No hay data");
               return false;
            }
                                               
            if (numRow==2){
               //EvalSolBaja = vForm.cmbEvalSolBaja.value;                 
               //if ( (EvalSolBaja == '00') || ( EvalSolBaja == "") ) {                                        
                  
                  //-----vForm.cmbMotivos.value = "";
                  //vForm.cmbDocAtatchment.value = "";                                                                              
                  //vForm.txt_ruta.value = "" ;
                  
                  vForm.cmbMotivos.disabled = "disabled";
                  //vForm.cmbDocAtatchment.disabled = "disabled";
                  //vForm.av_ruta.disabled = "disabled";  
                  
                  //vForm.hdncmbEvalSolBaja.value = "";                      
                  //vForm.hdncmbEvalSolBaja.value = "00"; 
                  
               /*}else{
                  
                  vForm.hdncmbEvalSolBaja.value = "";                      
                  vForm.hdncmbEvalSolBaja.value = "01";
                                    
                  vForm.hdncmbMotivos.value = vForm.cmbMotivos.value; 
                  vForm.hdncmbDocAtatchment.value = vForm.cmbDocAtatchment.value;                     
                  vForm.hdnav_ruta.value = vForm.av_ruta.value;                  
                  vForm.hdntxt_ruta.value = vForm.txt_ruta.value;
                                    
                  if (vForm.hdnMessageStatus.value == "03A01" || vForm.hdnMessageStatus.value == "01D05"){                                       
                     vForm.cmbMotivos.disabled = "";
                     vForm.cmbDocAtatchment.disabled = "";
                     vForm.av_ruta.disabled = "";                  
                  }                                                                     
               }*/
            }else{
               for(var i=0 ;i<numRow - 1;i++){
                  vForm.cmbMotivos[i].value = "";
                  vForm.cmbMotivos[i].disabled = "disabled";
               }
                  //vForm.cmbDocAtatchment.value = "";                                                                              
                  //vForm.txt_ruta.value = "" ;
                  
                  
               /*for(var i=0 ;i<numRow - 1;i++){
                  EvalSolBaja = vForm.cmbEvalSolBaja[i].value;                 
                  if ( (EvalSolBaja == '00') || ( EvalSolBaja == "") ) {                                        
                                                         
                     vForm.cmbMotivos[i].value = "";
                     vForm.cmbDocAtatchment[i].value = "";                                                                              
                     vForm.txt_ruta[i].value = "" ;
                                                                                                                        
                     vForm.cmbMotivos[i].disabled = "disabled";
                     vForm.cmbDocAtatchment[i].disabled = "disabled";
                     vForm.av_ruta[i].disabled = "disabled";
                     
                     vForm.hdncmbEvalSolBaja[i].value = "";                      
                     vForm.hdncmbEvalSolBaja[i].value = "00"; 
                     
                     vForm.hdncmbMotivos[i].value = "d"; 
                     vForm.hdncmbDocAtatchment[i].value = "d"; 
                     vForm.hdnav_ruta[i].value = "d";                      
                     vForm.hdntxt_ruta[i].value = "d";
                  /*                                          
                  }else{
                                                            
                     vForm.hdncmbEvalSolBaja[i].value = "";                      
                     vForm.hdncmbEvalSolBaja[i].value = "01"; 
                                                            
                     vForm.hdncmbMotivos[i].value = vForm.cmbMotivos[i].value; 
                     vForm.hdncmbDocAtatchment[i].value = vForm.cmbDocAtatchment[i].value;                     
                     vForm.hdnav_ruta[i].value = vForm.av_ruta[i].value;                                          
                     vForm.hdntxt_ruta[i].value = vForm.txt_ruta[i].value;                     
                                          
                     if (vForm.hdnMessageStatus[i].value == "03A01" || vForm.hdnMessageStatus[i].value == "01D05"){                     
                        vForm.cmbMotivos[i].disabled = "";
                        vForm.cmbDocAtatchment[i].disabled = "";
                        vForm.av_ruta[i].disabled = "";                     
                     }                                                                                                         
                  }*/
               //}                                       
            }                  
         }
                  
         function fxLoadComboLoadAtatch(txtLista, index){                                                                              
            if(txtLista != "") {
               var url = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=getAtatchment_by_mo&cmbMotivos="+txtLista+"&index="+index;
               parent.bottomFrame.location.replace(url);                                          
            }                                                
         }                                             
      </script>                                 
<%
} catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
        System.out.println("    " + e.getStackTrace()[i] + "<br>");
	      }
}%>