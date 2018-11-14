<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<%    
try{      
  // int iAction=(request.getParameter("sAction")==null?1:Integer.parseInt(request.getParameter("sAction"))); //1:Agregar  2:Editar
   long lNewBillAccId=(request.getParameter("nNewBillAccId")==null?-1:Integer.parseInt(request.getParameter("nNewBillAccId")));   
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));      
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   String strTypeCustom=Constante.TYPE_CRM_CUSTOMER;
   String strMessage=null;
   HashMap hshData=null;
   ArrayList arrLista=new ArrayList();

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
   if (arrContact==null)
      arrContact=new ArrayList();
      
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;   
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354   
   //Rutas
   String strRutaContext=request.getContextPath();  
   String strURLDirecciones="AddressList.jsp"+strParam;
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLContactos="ContactList.jsp"+strParam;    
   String strURLAreaCode=strRutaContext+"/GENERALPAGE/AreaCodeList.jsp";       
   String strURLOrderServlet =strRutaContext+"/editordersevlet";      
   
   System.out.println(" ----------- INICIO BillingAccountNEW.jsp---------------- ");
   System.out.println("nCustomerId-->"+lCustomerId);
   System.out.println("nOrderId-->"+lOrderId);
   System.out.println("nSiteId-->"+lSiteId);   
   System.out.println("strSessionId-->"+strSessionId);      
   System.out.println("strSpecificationId-->"+strSpecificationId);
   System.out.println("Tama�o del arreglo de billingAccount List-->"+arrLista.size());
   System.out.println(" ------------  FIN BillingAccountNEW.jsp----------------- ");    
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Contact</title>
  </head>
   
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<!--script language="JavaScript" src="../../websales/Resource/date-picker.js"></script-->                        
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script-->         
<!--script language="JavaScript" src="../../websales/Resource/DateTimeBasicOperations.js"></script-->            
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<table   BORDER="0" CELLPADDING="0" CELLSPACING="0">
<tr>
   <td valign="top">&nbsp;&nbsp;</td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
      <tr>
         <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
         <td valign="middle" NOWRAP class="TabBackgroundColor">&nbsp;&nbsp;<a href="<%=strURLGeneralData%>"><FONT class="TabBackgroundText">Datos Generales</FONT></a>&nbsp;&nbsp;</td>
         <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
      </tr>
      </table>
   </td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
      <tr>
         <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
         <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLDirecciones%>"><FONT class="TabBackgroundText">Direcciones</FONT></A>&nbsp;&nbsp;</td>
         <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
      </tr>
      </table>
   </td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
      <tr>
         <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
         <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLContactos%>"><FONT class="TabBackgroundText">Contactos</FONT></A>&nbsp;&nbsp;</td>
         <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
      </tr>
      </table>
   </td>
   <td valign="top">&nbsp;&nbsp;</TD><TD>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
      <tr>
         <td class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
         <td valign="middle" NOWRAP class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Billing Account</FONT>&nbsp;&nbsp;</td>
         <td align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
      </tr>
      </table>
   </td>   
</tr>
</table>
<table  WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0" class="TabForegroundColor">
<tr>
   <td  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD>
</td>
</table>  
</DIV>
<table  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
<tr>
   <td WIDTH="100%">
      <table BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
      <tr class="PortletHeaderColor">
         <td class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</td>
         <td class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
         <font class="PortletHeaderText">Cuenta Facturaci�n</font>
         &nbsp;&nbsp;&nbsp;</TD>
         <td align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
         </td><td align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</td>
      </tr> 
      </table>
      <table  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
      <TR><TD class="RegionHeaderColor" WIDTH="100%"> 
      
      <script>
         var codPostal = new Array(); 
         var vpAreaCode = new Vector();
      </script>  

      <form method="post" name="formdatos" target="bottomFrame">          
       <input type="hidden" name="myaction"/>    
       <input type="hidden" name="hdnSizeArrayContact" value="<%=arrContact.size()%>"/>  
       <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
       <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
       <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>">    
       <input type="hidden" name="hdnTypeCustom"     value="<%=strTypeCustom%>">  
       <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
       <input type="hidden" name="hndBscsCustId">
       <input type="hidden" name="hndBscsSeq">
	    <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->	 
       <input type="hidden" name="hdnFlag" value="0">   <!-- HME COR0536 -->	 
      
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
                    <input type="radio" name="rdbBaccount"  value="-1" onclick="fxRadioSection(this.value,'')">
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
                            <% hshData = objGeneralService.getTableList("PERSON_TITLE","1"); 
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
                          <select name="cmbJobTitle" style="width: 90%;" >                         
                            <% hshData=objGeneralService.getTitleList(); 
                               strMessage= (String)hshData.get("strMessage");
                               if (strMessage!=null)
                                  throw new Exception(strMessage);                              
                               arrLista=(ArrayList)hshData.get("arrTitleList");
                            %>
                            <%=MiUtil.buildCombo(arrLista,"descripcion","descripcion")%>                                      
                          </select>                         
                     </td>                  
                 </tr>            
                 <tr>                  
                     <td class="CellLabel" align="left" colspan="2"><!--fxCodeArea('txtAreaCode','spanAreaNom1','txtNumTelefono')-->
                     <a href="javascript:fxCodeArea('txtAreaCode','spanAreaNom1','txtNumTelefono')">�rea</a> /Tel�fono</td>
                     <td class="CellContent" colspan="3" >&nbsp;                  <!--fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNom1','txtNumTelefono')-->
                     <input type="text" name="txtAreaCode" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value,'txtAreaCode','spanAreaNom1','txtNumTelefono')"   >
                     <input type="text" name="txtNumTelefono" size="7" maxlength="8" onBlur="javascript:fxValidatePhone(this,'�rea/Tel�fono');"  >
                     <small><span id="spanAreaNom1"></span></small>
                     </td>                  
                 </tr>
                 <tr>                           
                     <td class="CellLabel" align="left" colspan="2">Direcci�n</td>
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
                              <option value=""> </option>
                          </select>  
                          <input type="hidden" name="hdnProv" >
                     </td>   
                 </tr>
                 <tr>                  
                     <td class="CellLabel" align="left" colspan="2" width="20%">Distrito</td>
                     <td class="CellContent" width="30%">&nbsp;
                          <select name="cmbDist" style="width: 90%;" onChange="fxLoadCodPostal(this.selectedIndex);javascript:document.formdatos.hdnDist.value=this[this.selectedIndex].text">                                                           
                               <option value=""> </option>
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
                     <td><input type="button" value="Guardar" onClick="fxAddBillAcc()"></td>
                     <td><input type="button" value="Cancelar" onClick="javascript:fxCancelAction();"></td>                     
                   </tr>
                  </table>
                  </td>  
             </tr>        
          </table>    
      </form>

</html>  
<script type="text/javascript">
    
  //var radioSelect=1;
  var bscsCustId=0;
  var bscsSeq =0;
   function fxCancelAction(){
      param="?nCustomerId=<%=lCustomerId%>&nSiteId=<%=lSiteId%>&nOrderId=<%=lOrderId%>&pSpecificationId=<%=strSpecificationId%>&hdnSessionId=<%=strSessionId%>";//&nRegionId=<%-- =lRegionId --%>";   //CEM - COR0354
	  //alert(param);
      url="BillingAccountList.jsp"+param;
      parent.mainFrame.location.replace(url);
   }  
   
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
  
  function AreaCode(n1,n2){
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
               alert("El campo "+ str +" debe ser n�merico");
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
      
      if (frm.hdnSizeArrayContact.value >0)
         frm.rdbBaccount[0].checked=true;              
   }
      
    function getNewBillAccId(){    
    	var frm = document.formdatos;      
      var cod = requestXML("requestGetNewBillAccId","nothing");
      alert("cod-->"+cod);
      if (cod ==null )
         cod=0;
      return cod;
    }      
    
    /* Manejo de inserci�n  */
    var aNewBillAcc = new Array();
    var customerId=  <%=lCustomerId%>;
    var siteId= <%=lSiteId%>;
    
    function fxAddBillAcc(){
    	frm = document.formdatos;    
      /*var billAccName=frm.txtBillAccName.value;
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
      var zipCode=frm.txtZip.value;*/
  
    /*  var billaccid =0;
      
      billaccid=getNewBillAccId();      
      if (billaccid<=0 )
         alert("Se produjo un error al intetar generar el c�digo para el site, intente nuevamente");*/
      
      if(frm.txtBillAccName.value==""){
         alert('ingrese un nombre de cuenta');
         frm.txtBillAccName.focus();
         return false;
      } 
     
     if (bscsCustId==-1){  // se eligio nuevo contacto       
          if (!fxValidate()) 
             return;            
          frm.hndBscsCustId.value=bscsCustId;
          frm.hndBscsSeq.value="";
      }else if(bscsCustId>=0 ){   
          frm.hndBscsCustId.value="";
          frm.hndBscsSeq.value="";                                               
      }
      
      frm.myaction.value="InsertBillAcc";
	  frm.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
	  frm.hdnCustomerId.value="<%=lCustomerId%>"; //CEM COR0354
	  frm.hdnOrderId.value="<%=lOrderId%>"; //CEM COR0354
	  frm.hdnSiteId.value="<%=lSiteId%>"; //CEM COR0354 	  
      frm.action='<%=strURLOrderServlet%>';          
      frm.submit();  
   }   
      
   /*  Fin de manejo de insercion  */

      
</script>

<% hshData=objGeneralService.getAreaCodeList(null,null);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);       
   ArrayList arrAreaCode=(ArrayList)hshData.get("arrAreaCodeList");
   HashMap hshMapData=null;
   for (int i=0;i<arrAreaCode.size();i++){
        hshMapData=((HashMap)arrAreaCode.get(i));                  
%>
   <script>                
    vpAreaCode.addElement(new AreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
   </script>
<%  }
%>       
    
<script>
   onload=fxLoadBody;  
</script>
<% 
}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
  history.back(-1);
</script>
<%
}
%>  