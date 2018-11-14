<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<% 
try{
   //PARAMETROS       
   int iIndex=(request.getParameter("nIndex")==null?-1:Integer.parseInt(request.getParameter("nIndex"))); 
   long lNewBillAccId=(request.getParameter("nNewBillAccId")==null?-1:Integer.parseInt(request.getParameter("nNewBillAccId")));   
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   
   String strMessage=null;
   ArrayList arrLista=new ArrayList();  
   
   GeneralService objGeneralService=new GeneralService();   
   BillingAccountService objBillAccService=new BillingAccountService();
   BillingContactBean objContactBean = null;
   BillingAccountBean objAccountBean = null;
   BillingContactBean objNewContactBean = new BillingContactBean();
   ArrayList arrContact=null;   
   HashMap hshData=null;
   
   hshData=objBillAccService.getContactBillCreateList(lCustomerId,lSiteId);    
   strMessage=(String)hshData.get("strMessage");
   
   if (strMessage!=null)
      throw new Exception(strMessage);    
   
   arrContact=(ArrayList)hshData.get("arrListado");    
   if (arrContact==null) arrContact=new ArrayList();   
   
   //Rutas
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Contact</title>
  </head>
   
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>                        
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>             
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>            
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script>
//DISTRITO - DEPARTAMENTO - PROVINCIA
  // var codPostal  = new Array(); 
   var vpAreaCode = new Vector();
  
</script>  
  <body >
  <form method="post" name="formdatos" target="bottomFrame">      
    <input type="hidden" name="myaction"/>        
    <input type="hidden" name="hdnNewBillAcc" value="<%=lNewBillAccId%>"/>
    <input type="hidden" name="hdnSizeArrayContact" value="<%=arrContact.size()%>"/> 
    <table  border="0" width="60%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table  border="0" width="100%" cellpadding="2" cellspacing="1">              
              <tr>             
                  <td class="CellLabel" align="center" colspan="2">Cuenta Facturaci&oacute;n</td>
                  <td class="CellContent" align="left" colspan="3">
                  <small><span id="txtBillAccName"></span></small>
                  <!--input type="text" style="border :0; background-color: #F5F5EB; width: 100%;" name="txtBillAccName"-->
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
                       
                      // System.out.println("FNAME->"+MiUtil.getString(objContactBean.getNpfname()));
              %>
              <tr>
                 <td class="CellContent" align="left">
                 <input type="radio" disabled="disabled" name="rdbBaccount" value="<%=MiUtil.getString(objAccountBean.getNpBscsCustomerId())%>" >
                 </td>
                 <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpfname())%></td>
                 <td class="CellContent" align="left" colspan="2"><%=MiUtil.getString(objContactBean.getNpaddress1())%></td>
                 <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpTypeContact())%></td>
              </tr>
              <%}%>
              <tr>
                 <td class="CellContent" align="left">
                 <input type="radio" disabled="disabled" name="rdbBaccount" value="-1" checked="checked">
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
                  <input type="text" style="border :0; background-color: #F5F5EB;" name="txtTitle">
                  </td>
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Nombres</td>
                  <td class="CellContent" colspan="3">&nbsp;
                  <input type="text" name="txtContactName" style="border :0; background-color: #F5F5EB;" ></td>                  
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Apellidos</td>
                  <td class="CellContent" colspan="3">&nbsp;
                  <input type="text" name="txtContactApellido" style="border :0; background-color: #F5F5EB;"></td>                  
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Cargos</td>
                  <td class="CellContent" colspan="3">&nbsp;
                  <!--input type="text" name="txtJobTitle" style="border :0; background-color: #F5F5EB; width: 80%"-->                      
                  <small><span id="txtJobTitle"></span></small>
                  </td>                  
              </tr>            
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2">
                  Área/Teléfono</td>
                  <td class="CellContent" colspan="3" >&nbsp;               
                  <!--input type="text" name="txtAreaCode" style="border :0; background-color: #F5F5EB;" -->
                  <!--input type="text" name="txtNumTelefono" style="border :0; background-color: #F5F5EB;" -->
                  <small><span id="txtAreaCode"></span></small>&nbsp;&nbsp;&nbsp;
                  <small><span id="txtNumTelefono"></span></small>&nbsp;&nbsp;&nbsp;
                  <small><span id="spanAreaNom1"></span></small>
                  </td>                  
              </tr>
              <tr>                           
                  <td class="CellLabel" align="left" colspan="2">Dirección</td>
                  <td class="CellContent" colspan="3">
                   &nbsp;<input type="text" name="txtAddress1" SIZE="50" style="border :0; background-color: #F5F5EB;"><BR>
                   &nbsp;<input type="text" name="txtAddress2" SIZE="50" style="border :0; background-color: #F5F5EB;"><BR>
                  </td>                  
              </tr>
              <tr>
                  <td class="CellLabel" align="left" colspan="2" width="20%">Departamento</td>
                  <td class="CellContent" width="30%">&nbsp;
                  <input type="text" name="txtDpto" style="border :0; background-color: #F5F5EB; width: 100%;">                               
                  </td>     
                  <td class="CellLabel" align="left" width="20%">Provincia</td>
                  <td class="CellContent" width="30%">&nbsp;
                  <input type="text" name="txtProv" style="border :0; background-color: #F5F5EB; width: 100%;">                        
                  </td>   
              </tr>
              <tr>                  
                  <td class="CellLabel" align="left" colspan="2" width="20%">Distrito</td>
                  <td class="CellContent" width="30%">&nbsp;
                  <input type="text" name="txtDist" style="border :0; background-color: #F5F5EB; width: 100%;">                        
                  </td>     
                  <td class="CellLabel" align="left">Cod. Postal</td>
                  <td class="CellContent">&nbsp;
                  <input type="text" name="txtZip" style="border :0; background-color: #F5F5EB;" >
                  </td>   
              </tr>  
             </table>
            </td> 
           </tr>
           <tr>
               <td align="center" colspan="6">
                 <table>
                   <tr>                   
                     <td><input type="button" value="Cerrar" onClick="parent.close();"></td>                     
                   </tr>
                  </table>
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
      
   var bscsCustId=0;
   var bscsSeq =0;
   function fxRadioSection(bscsCusId,bscsSq){         
       bscsCustId =bscsCusId;
       bscsSeq =bscsSq;
   }
  
   function fxFillAreaCode(code){
         if (code==""){           
            spanAreaNom1.innerHTML = '';           
            return;
         }
         cadena = "";
         bEntro = false;
         for(j=0;j<vpAreaCode.size();j++){
            objAreaCode = vpAreaCode.elementAt(j);
            if (objAreaCode.codearea==code){
               if (cadena!=""){ cadena = cadena+"/"; }
               cadena = cadena + objAreaCode.name;
                spanAreaNom1.innerHTML = "("+cadena+")";
               break;
            }
         }       
        return;
   }

  function fxAreaCode(n1,n2){
         this.name = n1;
         this.codearea = n2;
  }
    /* Manejo de inserción  */
    var aNewBillAcc = new Array();
    var index=<%=iIndex%>; //tomando en cuenta q inicia en 0    
    aNewBillAcc[0]=new fxCloneObject(parent.opener.apNewBillAcc[index-1]);
    
    function fxLoadBody(){
    	frm = document.formdatos; 
      //aNewBillAcc[0].billAccId    =frm.hdnNewBillAcc.value;  //se lleno en la pagina de list
      txtBillAccName.innerHTML    = aNewBillAcc[0].billAccName;  
      frm.txtTitle.value          = aNewBillAcc[0].titleName;
      frm.txtContactName.value    = aNewBillAcc[0].contfName;
      frm.txtContactApellido.value= aNewBillAcc[0].contlName;
      txtJobTitle.innerHTML       = aNewBillAcc[0].cargo;
      txtAreaCode.innerHTML       = aNewBillAcc[0].phoneArea;
      txtNumTelefono.innerHTML    = aNewBillAcc[0].phoneNumber;
      spanAreaNom1.innerHTML      ="";
      frm.txtAddress1.value       = aNewBillAcc[0].address1;
      frm.txtAddress2.value       = aNewBillAcc[0].address2;
      frm.txtDpto.value           = aNewBillAcc[0].depart;  
      frm.txtProv.value           = aNewBillAcc[0].prov;         
      frm.txtDist.value           = aNewBillAcc[0].dist;         
      frm.txtZip.value            = aNewBillAcc[0].zipCode;  
      bscsCustId                  = aNewBillAcc[0].bscsCustId;
      bscsSeq                     = aNewBillAcc[0].bscsSeq;
      fxFillAreaCode(aNewBillAcc[0].phoneArea);
      var bol=false;
      var ind=0;      
      //Si solo se creo el radio button "Nuevo"
      if (frm.hdnSizeArrayContact.value==0){      
           //frm.rdbBaccount.checked=true;           
           if (frm.txtContactName.value==''){//no es un contacto nuevo
                  //        
           }else{
              frm.rdbBaccount.checked=true; 
              fxRadioSection(-1,'');         
           }          
      }else{//Se añadireon a la lista contactos de Bscs
         //De los radio button de los contactos existentes selecciona el indicado
         for (i=0;i<frm.rdbBaccount.length;i++){
            if (frm.rdbBaccount[i].value=='-1'){
               ind=i;
            }else{
               var valorRadio=frm.rdbBaccount[i].value;
               //alert("valorRadio-->"+valorRadio);
               var codigos = valorRadio.split("|");           
               /*alert("codigos[0]-->"+codigos[0]);
               alert("codigos[1]-->"+codigos[1]);*/
               if ( codigos[0]==bscsCustId && codigos[1]==bscsSeq){
                    frm.rdbBaccount[i].checked=true;
                    bol=true;
                    break;  
               }                  
            }
         }        
         if (bol==false && frm.txtContactName.value!=''){
            frm.rdbBaccount[ind].checked=true;;            
         }         
         
         
      }           
      return;
    }  
  
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