<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat"%>
<%
try{
   
   String strBuildingId =null;
   String strLogin=null;
   int iLevel=0;  
   String strCode=null;
   String strDefaultInbox=null;
   
   String strFecha=MiUtil.getDate("dd/MM/yyyy");
   
  //Parametros de la pagina
  int iOrderId=(request.getParameter("hdnOrderId")==null?0:Integer.parseInt(request.getParameter("hdnOrderId")));        
  String strCurrentStatus=(request.getParameter("txtEstadoOrden")==null?"":request.getParameter("txtEstadoOrden"));
  String strWorkflowType=(request.getParameter("WorkFlowType")==null?"":request.getParameter("WorkFlowType"));
  long lSpecialtyId=(request.getParameter("specialtyid")==null?0:Long.parseLong(request.getParameter("specialtyid")));
  String strSessionId =(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
  String strRejectPermit =request.getParameter("hdnRejectPermit");

  PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   
  strBuildingId=objPortalSesBean.getBuildingid()+"";
  strLogin=objPortalSesBean.getLogin();
  iLevel=objPortalSesBean.getLevel();
  strCode=objPortalSesBean.getCode();
  strDefaultInbox=MiUtil.getString(objPortalSesBean.getDefaultInBox());   
  
  //Parametros propios de la invocacion al sp
  String strtrNextStatus="BAGLOCK";  // debe quedar asi
  String strObjectType="ORDENFF";  //debe quedar asi
  String strResult=null; //hasta q se consulte el sp
  String strMessage=null;   
  HashMap hshData=null;
  
  //Obteniendo los datos de la Orden
  //EditService ologRejectS=new EditService();  
  GeneralService objGeneralService= new GeneralService();   
  
  if (strRejectPermit.equals("E"))  strResult="S";
  else strResult="N";
 //String  strResult=orbOrder=ologRejectS.getState(lCurrentStatus,ltrNextStatus,lSpecialtyId,strObjectType,strResult,strMessage);
   
  /*WEBCCARE.NPAC_ORDER_EXT2_PKG.SP_IS_PREVIOUS_STATUS(
        txtEstadoOrden,  TIENDA01
        'BAGLOCK',
        specialtyid,   21
        WorkFlowType,   FULFILLMENT
        'ORDENFF',
        strResult,
         wv_message           
   );*/
%>
 <!--Inicio de OrderRejectEdit.jsp --> 
  <script>window.focus();</script>
     <HEAD>
     <TITLE>Registro de Rechazos</TITLE>
     </HEAD>     
     <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         

   
    <script>
               var gDefaultInbox="<%=strDefaultInbox%>";
               var gTableID="tableReject";
               var gNumCells=7;
      	       var gLabelArr = new Array();
      	       gLabelArr[0]="Motivo";
      	       gLabelArr[1]="Descripción";
      	       gLabelArr[2]="Estado";               
               gLabelArr[3]="Creado Por";               
               gLabelArr[4]="Grupo";               
               gLabelArr[5]="Subsanado Por";               
               gLabelArr[6]="Subsanar";               
               var gRejectArr  =new Array();
               var gIndexReject=0;
               var gOrderId   ="<%=iOrderId%>";
               var gUser      ="<%=strLogin%>";
               var gToday     ="<%=strFecha%>";
               
               function fxCloneObject(ObjectReject){
                  for (var i in ObjectReject){
                     this[i] = ObjectReject[i];
                  }
               }                
               function fxLoadRejectsFromParent(){
                  var lon=parent.opener.gpRejectArr.length;                  
                  for(var i=0;i<lon;i++){
                     var RejectElement       =  parent.opener.gpRejectArr[i];
                     gRejectArr[gIndexReject]=  new fxCloneObject(RejectElement) ; 
                     gIndexReject++;
                  }
               }
               //inserta en la tabla los rechazos existentes
               function fxAddRejects(){                
                  for(var i=0;i<gRejectArr.length;i++){
                     //var modified=(gDefaultInbox==gRejectArr[i].npinbox && gRejectArr[i].npstatus=="0")?"true":"false";
                     var modified=(gRejectArr[i].npstatus=="N")?"true":"false";                     
                     fxAddRowGeneral(gTableID,
                                    gRejectArr[i].npreason,
                                    gRejectArr[i].npdescription,
                                    (gRejectArr[i].npstatus=="S")?"Subsanado":"Pendiente",
                                    gRejectArr[i].npcreatedby+"-"+gRejectArr[i].npdatecreated,
                                    gRejectArr[i].npinbox,
                                    gRejectArr[i].npmodifiedby+"-"+gRejectArr[i].mpmodifydate,
                                    modified);
                 }
               }                
               
               //pasa el arreglo de rechazos a la ventana de Ordenes.
               function fxSendValues(){
                  var Form          =document.formdatos; 
                  var indexReject   =gRejectArr.length;
                  <% if ("S".equals(strResult)){%>                      
                        var reason        =Form.cmbReasons.options[Form.cmbReasons.selectedIndex].value;
                        var description   =trim(Form.txtDescripcion.value);
                        if (Form.cmbReasons.selectedIndex!=0 ){                           
                           description = description.replace(/\r\n/g,"[br]");                           
                           gRejectArr[indexReject]=new fxCreateReject("",gOrderId,reason,description,"N",gUser,gToday,"","",gDefaultInbox);
                        }else if (Form.cmbReasons.selectedIndex==0 && description.length >0){
                           alert("Seleccione un motivo");
                           Form.cmbReasons.focus();
                           return false;
                        }   
                  <%}%>                  
                  
                  parent.opener.fxSetRejectArr(gRejectArr.length);
                  for (var i=0;i<gRejectArr.length;i++){
                     parent.opener.fxLoadRejectArr(gRejectArr[i]);
                  }
                  parent.opener.fxReloadRejects();                   
                  parent.close();                  
               }
               
               function fxCancel(){
                  parent.close();
               }                 
               
               function fxCreateReject(nporderrejectid,nppedidoid,npreason,npdescription,npstatus,npcreatedby,npdatecreated,npmodifiedby,mpmodifydate,npinbox){
                  this.nporderrejectid =nporderrejectid;
                  this.nppedidoid      =nppedidoid;
                  this.npreason        =npreason;
                  this.npdescription   =npdescription;
                  this.npstatus        =npstatus;
                  this.npcreatedby     =npcreatedby;
                  this.npdatecreated   =npdatecreated;
                  this.npmodifiedby    =npmodifiedby;
                  this.mpmodifydate    =mpmodifydate;
                  this.npinbox         =npinbox;
                  this.isModified      ="false"; //para verficar q el rechazo ha sido subsanado o no 
               }
               //agrega un rechazo a la tabla de rechazos
               function fxAddReject(){
                  var Form          =document.formdatos;
                  var reason        =Form.cmbReasons.options[Form.cmbReasons.selectedIndex].value;
                  var description   =trim(Form.txtDescripcion.value);
                  var indexReject   =gRejectArr.length;
                  if (Form.cmbReasons.selectedIndex!=0){
                     gRejectArr[indexReject]=new fxCreateReject("",gOrderId,reason,description,"N",gUser,gToday,"","",gDefaultInbox);
                     fxReloadRejects();  
                     Form.cmbReasons.selectedIndex=0;
                     Form.txtDescripcion.value="";
                     Form.cmbReasons.focus();
                  }else{
                     alert("Seleccione un motivo")
                     Form.cmbReasons.focus();
                  }   
               }

               function fxRectifyReject(element){ //
      		  var table = document.all?document.all[gTableID]:document.getElementById(gTableID);
      		  var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.
                  var indexRow=element.parentNode.parentNode.rowIndex-1;
                  element.parentNode.removeChild(element);
                  gRejectArr[indexRow].npstatus       ="S";
                  gRejectArr[indexRow].mpmodifydate   =gToday;
                  gRejectArr[indexRow].npmodifiedby   =gUser;
                  gRejectArr[indexRow].isModified   ="true";
                  fxReloadRejects();  
               }
                      
               function fxReloadRejects(){
                  var table = document.all?document.all[gTableID]:document.getElementById(gTableID);
      		  var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.
                  var longitud=tbody.childNodes.length;  
                  var rows=tbody.childNodes;
         for (var i=longitud-1 ;i>=0 ;i--){
                     var row=rows.item(i);                                          
                     row.parentNode.removeChild(row);
                  }
                  fxAddRejects();
               }                
      	       function fxAddHeadContent(THead){
      			var row=document.createElement("tr"); //creo una fila
      			var etiquet;
      			for(var i=0;i<gNumCells;i++){
      			        var cell=document.createElement("td"); //creo una celda
                                cell.setAttribute("align","center");
      				etiquet=gLabelArr[i];
      				var element=document.createTextNode(etiquet);                  
      				cell.appendChild(element);
      				row.appendChild(cell);
      			}
      			row.setAttribute("className", "CellLabel");
      			THead.appendChild(row);
      			return THead;
      		}
      		
                function fxCreateTable(id,numCells,width, className,align,border,cellpadding,cellspacing ){
                        var Table = document.createElement("table");
                        Table.setAttribute("id",gTableID);
                        Table.setAttribute("width",width);
                        Table.setAttribute("className",className);
                        Table.setAttribute("align",align);
                        Table.setAttribute("border",border);
                        Table.setAttribute("cellpadding",cellpadding);
                        Table.setAttribute("cellspacing",cellspacing);
                        var THead = document.createElement("thead");
                        var TFoot = document.createElement("tfoot");
                        var TBody = document.createElement("tbody");
                        THead=fxAddHeadContent(THead);
                        Table.appendChild(THead);
                        Table.appendChild(TBody);
                        Table.appendChild(TFoot);
                        document.body.appendChild(Table);
      			}
               function round(number,X) {
                       X = (!X ? 2 : X);
                       return Math.round(number*Math.pow(10,X))/Math.pow(10,X);
               }  
                        
      		function fxCreateElementInput(tag,itype, iname, ivalue){//tipo, name, value
      				var element=document.createElement(tag);
      				element.setAttribute("type",	itype);
      				element.setAttribute("name",	iname);
      				element.setAttribute("value", ivalue);
      				element.setAttribute("id", iname);
      				return element;
      		}
      		function fxSetEvent(element,eventEl,func){
              <% if ("S".equals(strResult)){%>  
              element.disabled=false;
              <%}else{%>
              element.disabled=true;
              <%}%>
      				element.attachEvent(eventEl, function() { eval(func) }); //es de IE, no W3C
      				return element;
      		}                             
      		function fxAddCellContent(cell,indexCell,argumentsCell, indexRow){
      		  //el arreglo argumentsCell se lee a  partir del indice 1                  
                  var nrowsText=0;
                  var nrowsTextRound=0;
                  if (indexCell<6){
                     if (indexCell==1){ //textarea                       
                        nrowsText   =  trim(argumentsCell[indexCell+1]).length/50;
                        nrowsTextRound=Math.round((trim(argumentsCell[indexCell+1]).length)/50);
                        if (nrowsText > nrowsTextRound) { //redondeo de 0.5 para abajo
                           nrowsTextRound++;
                        }
                        if (nrowsTextRound=="" || nrowsTextRound==0)
                           nrowsTextRound=1;
                        var element=document.createElement("TEXTAREA");
                        element.cols=60;
                        element.rows=nrowsTextRound;
                        element.wrap="soft";
                        element.readOnly=true;                        
                        element.value=trim(argumentsCell[indexCell+1].replace(/\[br\]/g,"\n"));                                                
                        element.style.backgroundColor="#F5F5EB";
                        element.style.borderRight="none";
                        element.style.borderLeft="none";
                        element.style.borderTop="none";
                        element.style.borderBottom="none";
                        cell.appendChild(element);
                     }else{                        
                        var element=document.createTextNode(argumentsCell[indexCell+1]);                        
                        cell.appendChild(element);                     
                     }   
                  }else{
                     if (argumentsCell[indexCell+1]=="true"){
                        var element=fxCreateElementInput("INPUT","checkbox", "chkSubsanar",indexCell+1);
                        element=fxSetEvent(element,"onclick", "fxRectifyReject(element)");
                        cell.setAttribute("align","center");
                        cell.appendChild(element);                     
                     }    
                  }
      				return cell;
      		}
                          
      		function fxAddRowGeneral(gTableID){
      				var table = document.all?document.all[gTableID]:document.getElementById(gTableID);
      				var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.
      				var row=document.createElement("tr"); 
      				for(var i=0;i <gNumCells;i++){
      					var cell=document.createElement("td") //creo una celda
      					var cell=fxAddCellContent(cell,i,arguments,i+1); 
      					row.appendChild(cell); 
      				}
      				row.setAttribute("className", "CellContent");
                  row.setAttribute("id", "rowReject"+tbody.childNodes.length);
      				tbody.appendChild(row); 
      		}               
               function fxClear(){
                  var Form=document.formdatos;               
                  Form.cmbReasons.selectedIndex=0;
                  Form.cmbReasons.focus();
                  Form.txtDescripcion.value="";
               }
               
               function fxValidateLength(ObjectText){
                  var descripcion=ObjectText.value;
                  if (descripcion.length>2000 ){
                     alert("No puede ingresar mas de 2000 caracteres");
                     ObjectText.select();
                     ObjectText.focus();
                     return false;
                  }
                  return true;
               }               
            </script>
            
            
            <form method="post" name="formdatos">
            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
            <tr class="PortletHeaderColor">
               <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
               <td class="PortletHeaderColor" align="LEFT" valign="top"> <font class="PortletHeaderText">Rechazos</font></td>
               <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
            </tr>
            </table>            
            <script>
               //tabla de rechazos
               fxCreateTable(gTableID,gNumCells,"100%","RegionBorder","center","0","0","0");
            </script>
            <table><tr align="center"><td></td></tr></table>
            <table><tr align="center"><td></td></tr></table>
            <% if ("S".equals(strResult)) {%>                
               <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
               <tr class="PortletHeaderColor">
                  <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
                  <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Agregar Rechazo</font></td>
                  <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
               </tr>
               </table>            
               <table border="0" class="RegionBorder" cellspacing="1" cellpadding="0" align="center" width="100%">               
                  <tr><td class="CellLabel"   align="left" width="14%">&nbsp;&nbsp;Motivo</td>  
                      <td class="CellContent" align="left" width="86%">
                       <select name="cmbReasons">
                       <option>&nbsp;&nbsp;&nbsp;&nbsp;</option>
                             <% 
                                hshData = objGeneralService.getTableList("MOTIVO_RECHAZO","1");                                
                                strMessage=(String)hshData.get("strMessage");
                                HashMap hshMap=null;
                                String strDatoT=null;
                                String strDatoV=null;                                
                                if (strMessage!=null)
                                   throw new Exception(strMessage);                               
                                ArrayList arrLista=(ArrayList)hshData.get("arrTableList");    
                                for (int i=0;i<arrLista.size();i++){                                   
                                   hshMap=(HashMap)arrLista.get(i);
                                   strDatoV= (String)hshMap.get("wv_npValue");  
                                   strDatoT= (String)hshMap.get("wv_npValueDesc");                                                                      
                                   if( hshMap.get("wv_npTag1") == null  || strDefaultInbox.equals(MiUtil.getString((String)(hshMap.get("wv_npTag1")))) ){                                   
                             %>                                      
                                       <option value="<%=strDatoV%>"><%=strDatoT%></option>
                             <%     }
                                }
                             %>                       
                         </select>                   
                      </td>     
                  </tr>
                  <tr><td class="CellLabel" colspan="2"  align="left">&nbsp;&nbsp;Descripción&nbsp;&nbsp;(máximo 2000 caracteres)</td> </tr>
                  <tr>
                      <td class="CellContent" colspan="2" align="left" width="100"><textarea name="txtDescripcion" cols="90" rows="4" WRAP="HARD" onBlur="javascript:fxValidateLength(this)" ></textarea></td>     
                  </tr>
                  <tr>
                      <td align="left" class="CellContent" width="14%"><a href="javascript:fxAddReject()">Agregar</a></td>
                      <td align="left" class="CellContent" width="86%"><a href="javascript:fxClear()">Limpiar</a></td>
                  </tr>
               </table>
            <%}%>            
            <table border="0" cellspacing="0" cellpadding="0" align="center">               
               <tr>
                  <% if ("S".equals(strResult)){%>    
                   <td align="center"><input type="button" name="btnAceptar" value=" Aceptar "   onclick="javascript:fxSendValues()">
                   </td>
                   <td align="center">&nbsp;&nbsp;<input type="button" name="btnCancelar" value=" Cancelar " onclick="javascript:fxCancel()">                     
                   </td>
                   <%}else{%>
                   <td align="center"><input type="button" name="btncCerrar" value=" Cerrar "   onclick="javascript:fxCancel()">
                   </td>
                   <%}%>
                   
               </tr>
            </table>
            </form>                                 
    <script>
          fxLoadRejectsFromParent();//cargo el arreglo de rechazos del padre          
          fxAddRejects(); //inserto las filas con la  data del arreglo');          
          <% if ("S".equals(strResult)){%>    
             document.formdatos.cmbReasons.focus();
          <% }%>       
               
    </script>

<%}catch(Exception  e){ 
   e.printStackTrace();
   System.out.println("OrderRejectEdit-->"+MiUtil.getMessageClean(e.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(e.getMessage())%>");
</script>
<%
}
%>          
<!--Fin de OrderRejectEdit.jsp --> 

