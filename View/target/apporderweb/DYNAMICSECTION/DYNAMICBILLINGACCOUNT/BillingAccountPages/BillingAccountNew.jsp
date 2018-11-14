<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<% 

try{
   //PARAMETROS   
   //ArrayList objItemHEader = new ArrayList();    
   //Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");  
   //String strSpecificationId=(String)hshParam.get("strSpecificationId"); 
   //System.out.println("cristian");
   //System.out.println("strSpecificationId "+strSpecificationId);
   
   //Obteniendo datos del request   
   // int iAction=(request.getParameter("sAction")==null?1:Integer.parseInt(request.getParameter("sAction"))); //1:Agregar  2:Editar
   long lNewBillAccId=(request.getParameter("nNewBillAccId")==null?-1:Integer.parseInt(request.getParameter("nNewBillAccId")));   
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   //HMORENO   
   String strSessionId=(request.getParameter("strSessionId")==null?"0":request.getParameter("strSessionId"));
   System.out.println("----------------->strSessionId: "+strSessionId);
   String strSpecificationId=(request.getParameter("strSpecificationId")==null?"0":request.getParameter("strSpecificationId"));    
   String strMessage=null;
   HashMap hshData=null;
   ArrayList arrLista=new ArrayList();

   //Consultando Service
   GeneralService objGeneralService=new GeneralService();   
   BillingAccountService objBillAccService=new BillingAccountService();
   BillingContactBean objContactBean = null;
   BillingAccountBean objAccountBean = null;
   BillingContactBean objNewContactBean = new BillingContactBean();
   ArrayList arrContact=null;   
   
   hshData=objBillAccService.getContactBillCreateList(lCustomerId,lSiteId);   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);    
   arrContact=(ArrayList)hshData.get("arrListado");     
   
   if (arrContact==null) arrContact=new ArrayList();

   //Rutas
   String strRutaContext=request.getContextPath();  
   String strURLOrderServlet =strRutaContext+"/editordersevlet";      
   String strURLAreaCode=strRutaContext+"/GENERALPAGE/AreaCodeList.jsp";       

   System.out.println(" ----------- INICIO BillingAccountList.jsp---------------- ");
   System.out.println("nCustomerId-->"+lCustomerId);
   System.out.println("nOrderId-->"+lOrderId);
   System.out.println("nSiteId-->"+lSiteId);   
   //System.out.println("Tamaño del arreglo de billingAccount List-->"+arrLista.size());
   System.out.println(" ------------  FIN BillingAccountList.jsp----------------- ");       
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Contact</title>
  </head>
   
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <!--script language="JavaScript" src="../../websales/Resource/date-picker.js"></script-->                        
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
    <!--script language="JavaScript" src="../../websales/Resource/DateTimeBasicOperations.js"></script-->            
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script>

   var codPostal = new Array(); 
   var vpAreaCode = new Vector();
   var bscsCustId=0;
   var bscsSeq =0;   
</script>  
  <body>
  <form method="post" name="formdatos" target="bottomFrame">          
    <input type="hidden" name="myaction"/>    
    <input type="hidden" name="hdnSizeArrayContact" value="<%=arrContact.size()%>"/>  
    
    <!--hmoreno -->
    <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>">
    <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
    <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
    <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>">   
    <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">    
    <input type="hidden" name="hdnFlag" value="1"> 
	 <input type="hidden" name="hdnNewBillAccId" value="">
	  <input type="hidden" name="hndBscsCustId">
    <!--hmoreno -->
    <table  border="0" width="60%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table  border="0" width="100%" cellpadding="2" cellspacing="1">              
              <tr>             
                  <td class="CellLabel" align="center" colspan="2">Cuenta Facturaci&oacute;n</td>
                  <td class="CellContent" align="left" colspan="3">
                  <input type="text" name="txtBillAccName" MAXLENGTH="30" style="width: 100%;" onchange="this.value=trim(this.value.toUpperCase());" >
                  </td>
              </tr>
              <tr>
                  <td align="center" colspan="3"></td>
              </tr>
              <tr>
                  <td class="CellLabel" align="center" width="5%">&nbsp;</td>
                  <td class="CellLabel" align="center" width="15%">Nombre Contacto</td>
                  <td class="CellLabel" align="center" colspan="2" width="20%">Direcci&oacute;n Contacto</td>
                  <td class="CellLabel" align="center" width="10%">Tipo</td>                  
              </tr> 
              <% for (int i=0;i< arrContact.size();i++){                     
                       objAccountBean=(BillingAccountBean)arrContact.get(i);
                       objContactBean = objAccountBean.getObjBillingContactB();  
                     if (i==0){
                     System.out.println("objAccountBean.getNpBscsCustomerId()-->"+MiUtil.parseInt(MiUtil.getIfNotEmpty(objAccountBean.getNpBscsCustomerId())));
                     System.out.println("objAccountBean.getNpBscsSeq()-->"+MiUtil.getString(objAccountBean.getNpBscsSeq()));
              %>
                  <script>
                     bscsCustId ="<%=MiUtil.parseInt(MiUtil.getIfNotEmpty(objAccountBean.getNpBscsCustomerId()))%>";
                     bscsSeq ="<%=MiUtil.getString(objAccountBean.getNpBscsSeq())%>";                  
                  </script>
              <%     }
              %>
              <tr>
                 <td class="CellContent" align="left">
                 <input type="radio" name="rdbBaccount" value="<%=MiUtil.getIfNotEmpty(objAccountBean.getNpBscsCustomerId())%>"  onclick="fxRadioSection(fxGetBscsCod(this.value),'<%=MiUtil.getString(objAccountBean.getNpBscsSeq())%>')">
                 </td>
                 <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpfname())%></td>
                 <td class="CellContent" align="left" colspan="2"><%=MiUtil.getString(objContactBean.getNpaddress1())%></td>
                 <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpTypeContact())%></td>
              </tr>
              <%}%>
              <tr>
                 <td class="CellContent" align="left">
                 <input type="radio" name="rdbBaccount"  value="-1" onclick="fxRadioSection(this.value,0)">
                 </td>
                 <td class="CellContent" align="left" colspan="4"><em>&lt; Nuevo &gt;</em></td>                 
              </tr>
              <tr>
                  <td align="center" colspan="3"></td>
              </tr>             
              <tr>
              <td colspan="5">
              <!--span id="divNuevo" style="display:'none'"-->
              <table width="100%">
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2" width="20%">Titulo</td>
                  <td class="CellContent" colspan="3" width="80%">&nbsp;
                       <select name="cmbTitle" style="width: 20%;" onchange="javascript:document.formdatos.hndCmbTitle.value=this[this.selectedIndex].text">                         
                         <% 
                            hshData = objGeneralService.getTableList("PERSON_TITLE","1"); 
                            strMessage=(String)hshData.get("strMessage");
                            if (strMessage!=null)
                               throw new Exception(strMessage);                               
                            arrLista=(ArrayList)hshData.get("arrTableList");    
                         %>
                         <%=MiUtil.buildCombo(arrLista,"wv_npValueDesc","wv_npValueDesc")%>                                      
                       </select>
                       <input type="hidden" name="hndCmbTitle"/>            
                   </td>
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Nombres</td>
                  <td class="CellContent" colspan="3">&nbsp;
                  <input type="text" name="txtContactName" onchange="this.value=trim(this.value.toUpperCase());" ></td>                  
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Apellidos</td>
                  <td class="CellContent" colspan="3">&nbsp;
                  <input type="text" name="txtContactApellido" onchange="this.value=trim(this.value.toUpperCase());" ></td>                  
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Cargos</td>
                  <td class="CellContent" colspan="3">&nbsp;
                       <select name="cmbJobTitle" style="width: 90%;" onchange="javascript:document.formdatos.hndCmbJobTitle.value=this[this.selectedIndex].text">                         
                         <% 
                            hshData=objGeneralService.getTitleList(); 
                            strMessage= (String)hshData.get("strMessage");
                            if (strMessage!=null)
                               throw new Exception(strMessage);                              
                            arrLista=(ArrayList)hshData.get("arrTitleList");                         
                         %>
                         <%=MiUtil.buildCombo(arrLista,"descripcion","descripcion")%>                                      
                       </select>
                       <input type="hidden" name="hndCmbJobTitle" />
                  </td>                  
              </tr>            
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><!--fxCodeArea('txtAreaCode','spanAreaNom1','txtNumTelefono')-->
                  <a href="javascript:fxCodeArea('txtAreaCode','spanAreaNom1','txtNumTelefono')">Área</a> /Teléfono</td>
                  <td class="CellContent" colspan="3" >&nbsp;                  <!--fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNom1','txtNumTelefono')-->
                  <input type="text" name="txtAreaCode" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value,'txtAreaCode','spanAreaNom1','txtNumTelefono')"   >
                  <input type="text" name="txtNumTelefono" size="7" maxlength="12" onBlur="javascript:fxValidatePhone(this,'Área/Teléfono');"  >
                  <small><span id="spanAreaNom1"></span></small>
                  </td>                  
              </tr>
              <tr>                           
                  <td class="CellLabel" align="left" colspan="2">Dirección</td>
                  <td class="CellContent" colspan="3">
                  <!--input type="text" name="txtAddress"-->
                   <!--textarea name="txtAddress" cols="80" rows="3"></textarea-->
                   &nbsp;<input type="text" name="txtAddress1" SIZE="50"  onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                   &nbsp;<input type="text" name="txtAddress2" SIZE="50"  onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                  </td>                  
              </tr>
              <tr>
                  <td class="CellLabel" align="left" colspan="2" width="20%">Departamento</td>
                  <td class="CellContent" width="30%">&nbsp;
                       <select name="cmbDpto" onChange="fxLoadPlace(formdatos.cmbDpto.value,'','1');javascript:document.formdatos.hdnDpto.value=this[this.selectedIndex].text">                                                           
                          <% 
                             hshData= objGeneralService.getUbigeoList(null,null,"0"); 
                             strMessage=(String)hshData.get("strMessage");
                             if (strMessage!=null)
                                throw new Exception(strMessage);                                  
                             arrLista=(ArrayList)hshData.get("arrListado");                            
                          %>
                          <%=MiUtil.buildCombo(arrLista,"nombre","nombre")%>                            
                       </select>
                       <input type="hidden" name="hdnDpto">           
                  </td>     
                  <td class="CellLabel" align="left" width="20%">Provincia</td>
                  <td class="CellContent" width="30%">&nbsp;
                       <select name="cmbProv" style="width: 85%;" onChange="fxLoadPlace(formdatos.cmbDpto.value,formdatos.cmbProv.value,'2');javascript:document.formdatos.hdnProv.value=this[this.selectedIndex].text">                                                          
                           <option value="0"> </option>
                       </select>  
                       <input type="hidden" name="hdnProv" >
                  </td>   
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2" width="20%">Distrito</td>
                  <td class="CellContent" width="30%">&nbsp;
                       <select name="cmbDist" style="width: 90%;" onChange="fxLoadCodPostal(this.selectedIndex);javascript:document.formdatos.hdnDist.value=this[this.selectedIndex].text">                                                           
                            <option value="0"> </option>
                       </select>                   
                      <input type="hidden" name="hdnDist" >
                  </td>     
                  <td class="CellLabel" align="left">Cod. Postal</td>
                  <td class="CellContent">&nbsp;
                  <input type="text" readonly="readonly" name="txtZip" >
                  </td>   
              </tr>  
             </table>
             <!--/span-->
            </td> 
           </tr>
           <tr>
               <td align="center" colspan="6">
                 <table>
                   <tr>
                     <!--td><input type="button" value="Aceptar" onClick="doSubmit('InsertBillAcc')"></td-->
                     <td><input type="button" value="Aceptar" onClick="fxAddBillAcc()"></td>
                     <td><input type="button" value="Cancelar" onClick="parent.close();"></td>                     
                   </tr>
                  </table>
                </td>  
             </tr>        
         </table>    
     </td>
    </tr>
    <tr>
		<td>
			<table  border="0" width="100%" cellpadding="2" cellspacing="1"  id="tableMsgProcesando" style="visibility: hidden;">
			<tr>
				<td class="CellLabel" align="center">Transacción en proceso...</td>
			</tr>
			<tr>
				<td class="CellContent"  align="center">
				<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/progressbar.gif" WIDTH="50%" HEIGHT="15" BORDER="0">
				</td>
			</tr>
			</table> 
		</td>
	</tr>	 
   </table> 
  </form>
  </body>
</html>  
<script type="text/javascript">
    
  //var radioSelect=1;
 
  function fxRadioSection(bscsCusId,bscsSq){ 
       bscsCustId =bscsCusId;
       bscsSeq =bscsSq;
  }
  
  function fxGetBscsCod(valor){
     if (valor=="")
       return 0;
     else
       return valor;  
  }
  
  function fxAreaCode(n1,n2){
         this.name = n1;
         this.codearea = n2;
  }
 
  function fxSetValue(nombre,texto){
     Form = document.formdatos; 
     eval("Form."+nombre+"value="+texto);
  }
  
  function fxValidatePhone(obj, str){
         if (obj.value != ""){
            if (!ContentOnlyNumber(obj.value)){
               alert("El campo "+ str +" debe ser númerico");
               obj.value="";
               obj.focus();
               return false;
            }
            return true;
         }
  }
      
   function fxSearchAreaCode(code, nAreaCode ,nAreaNom, ntelf){
         if (code==""){
            document.formdatos.txtAreaCode.value = '';
            spanAreaNom1.innerHTML = '';
            document.formdatos.txtNumTelefono.focus();
            return;
         }
         cadena = "";
         bEntro = false;
         for(j=0;j<vpAreaCode.size();j++){
            objAreaCode = vpAreaCode.elementAt(j);
            if (objAreaCode.codearea==code){
               if (cadena!=""){ cadena = cadena+"/"; }
               cadena = cadena + objAreaCode.name;
               bEntro = true;               
            }
         }
         if ( bEntro == true){
             document.formdatos.txtAreaCode.value = code;
             spanAreaNom1.innerHTML = "("+cadena+")";
             document.formdatos.txtNumTelefono.focus();
         }else{
            fxCodeArea(nAreaCode, nAreaNom, ntelf );
            document.formdatos.txtAreaCode.value = '';
         }
         return bEntro;
      }

      function fxCodeArea(nAreaCode, nAreaNom, ntelf ){
      //function fxCodeArea(){//nAreaCode, nAreaNom, ntelf ){
         var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName=formdatos";
         url="<%=strURLAreaCode%>"+param;
         window.open(url ,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=330,height=400");
      }

      function fxValidate(){
         Form = document.formdatos; 
         
         if (Form.txtContactName.value==""){
            alert('ingrese el nombre del contacto');
            Form.txtBillAccName.focus();
            return false;
         }
         if (Form.txtContactApellido.value==""){
            alert('ingrese el apellido del contacto');
            Form.txtBillAccName.focus();
            return false;
         }
         if (Form.cmbJobTitle.value=="" || Form.cmbJobTitle.value=="0"){
            alert('elija un cargo');
            Form.txtBillAccName.focus();
            return false;
         }
         return true;
      }
      
      function fxLoadPlace(depId,provId,tipo) {                 
        Form = document.formdatos;        
           
        if (tipo==1 && depId==""){ //  0: Carga Departamentos 1: Carga Provincia  2: Carga Distrito                                                 
             deleteOptionIE(document.formdatos.cmbDist); 
             deleteOptionIE(document.formdatos.cmbProv);   
             flag2=1;
             Form.txtZip.value="";
        }else if (tipo==2 && (provId=="" || provId=="0")){
             deleteOptionIE(document.formdatos.cmbDist);              
             Form.txtZip.value="";
        }else{                
            Form.myaction.value="LoadUbigeo";                
            var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&nTipo="+tipo+"&myaction=LoadUbigeo"; 
            parent.bottomFrame.location.replace(url);            
         }
      }
      
     
      function fxLoadCodPostal(posicion){
          Form = document.formdatos;     
          if (posicion==0) 
            Form.txtZip.value="";
          else{           
             var dato=codPostal[posicion-1];
             Form.txtZip.value=dato;
          }          
      }     
      
    function fxLoadBody(){
      frm = document.formdatos;            
      
      //Si solo se creo el radio button "Nuevo"
      if (frm.hdnSizeArrayContact.value==0){      
           frm.rdbBaccount.checked=true;
           fxRadioSection(-1,'');
           return;
      }
      
      if (frm.rdbBaccount.length>1)
         frm.rdbBaccount[0].checked=true;              
   }
      
    function getNewBillAccId(){    
    	var frm = document.formdatos;      
      var cod = requestXML("requestGetNewBillAccId","nothing");      
      if (cod ==null )
         cod=0;
      return cod;
    }     
    

   function requestXML(funcion, params){
      var url = "<%=request.getContextPath()%>/generalServlet?metodo=" + funcion + "&"+params; 
      var msxml = new ActiveXObject("msxml2.XMLHTTP");
      msxml.Open("GET", url, false);
      msxml.Send("");
      var ret = msxml.responseText;	    
      if(ret.indexOf("OK")!=0){      
         return null;
      }
      return ret.substr(2);
   }    
    
    /* Manejo de inserción  */
    var aNewBillAcc = new Array();
    var customerId=  <%=lCustomerId%>;
    var siteId= <%=lSiteId%>;
    
    function fxAddBillAcc(){
    	frm = document.formdatos;    
      var billAccName=frm.txtBillAccName.value;
      var titleName=frm.cmbTitle.value;
      var contfName=frm.txtContactName.value;
      var contlName=frm.txtContactApellido.value;
      var cargo=frm.cmbJobTitle.value;
      var phoneArea=frm.txtAreaCode.value;
      var phoneNumber=frm.txtNumTelefono.value;
      var address1=frm.txtAddress1.value;
      var address2=frm.txtAddress2.value;
      var depart =frm.cmbDpto.value;
      var prov   =frm.cmbProv.value;
      var dist   =frm.cmbDist.value;
      var zipCode=frm.txtZip.value;
      var estado='Nuevo';
      var billaccid =0;
      
      if(frm.txtBillAccName.value==""){
         alert('ingrese un nombre de cuenta');
         frm.txtBillAccName.focus();
         return false;
      } 
         
     if (bscsCustId==-1){  // se eligio nuevo contacto       
         if (!fxValidate()) 
            return;

         billaccid=getNewBillAccId();      
         if (billaccid<=0 ){
            alert("Se produjo un error al intentar generar el código para el billing account, intente nuevamente");
            return false;
         }
         
         //alert("depart-->"+depart+"  prov-->"+prov+"  dist-->"+dist);
         aNewBillAcc[0]= new fxMakeBillAccNew(billaccid,billAccName,titleName,contfName,contlName,
                                            cargo,phoneArea,phoneNumber,address1,address2,
                                            depart,prov,dist,zipCode,estado,"-1","");             
          
      }else if(bscsCustId>=0 ){
   
         billaccid=getNewBillAccId();      
         if (billaccid<=0 ){
            alert("Se produjo un error al intetar generar el código para el billing account, intente nuevamente");
            return false;
         }

          aNewBillAcc[0]= new fxMakeBillAccNew(billaccid,billAccName,"","","",
                                               "","","","","",
                                               "","","","",estado,
                                               bscsCustId,bscsSeq);      
      }      
      frm.myaction.value="InsertBillAcc";
      frm.hdnSpecificationId.value="<%=strSpecificationId%>"; 
      frm.hdnCustomerId.value="<%=lCustomerId%>"; 
      frm.hdnOrderId.value="<%=lOrderId%>"; 
      frm.hdnSiteId.value="<%=lSiteId%>";       
      frm.hdnNewBillAccId.value=billaccid;
		frm.hndBscsCustId.value=bscsCustId;
      parent.opener.fxLoadNewBillAccArr(aNewBillAcc[0]);      
      parent.opener.fxReloadBillAccList(customerId,siteId,billaccid,0); 
      fxShowElement('tableMsgProcesando');
      frm.action='<%=strURLOrderServlet%>';             
      frm.submit();                    
    }
      
   /*  Fin de manejo de insercion  */
      
</script>

<% 
   hshData=objGeneralService.getAreaCodeList(null,null);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);       
   ArrayList arrAreaCode=(ArrayList)hshData.get("arrAreaCodeList");

   HashMap hshMapData=null;
   for (int i=0;i<arrAreaCode.size();i++){
        hshMapData=((HashMap)arrAreaCode.get(i));                  
%>
   <script>                
    vpAreaCode.addElement(new fxAreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
   </script>
<%  }
%>       
    
<script>
   onload=fxLoadBody;  
</script>
<%      
}catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>


