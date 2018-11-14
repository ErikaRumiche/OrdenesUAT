<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>

<%
   long lngProviderGrpId    = 0,
        lGeneratorId        = 0;
   int  iPermission = 0,
        iUserId     = 0,
        iAppId      = 0;
   String strGeneratorType    = null;
   HashMap hshData  = null;
          
%>
<% 
try{
  //PARAMETROS
   ArrayList objItemHEader = new ArrayList();
   Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputNewSection");
   if (hshParam==null) hshParam=new Hashtable();
   long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
   long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));
   long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));
   
   // INC 536
   String strCustomerId=(String)hshParam.get("strCustomerId");
   String strOrderId=(String)hshParam.get("strOrderId");
   String strSessionId=MiUtil.getString((String)hshParam.get("strSessionId"));
   String strTypeCustomer=MiUtil.getString((String)hshParam.get("strTypeCompany"));
   String strSpecificationId=MiUtil.getString((String)hshParam.get("strSpecificationId")); 
   
   lngProviderGrpId  = MiUtil.parseLong((String)hshParam.get("strProviderGrpId"));
   strGeneratorType  = (String)hshParam.get("strGeneratorType");
   lGeneratorId      = MiUtil.parseLong((String)hshParam.get("strGeneratorId")); 
   
   System.out.println("Inicio - NewOnDisplayBillingAccount.jsp");
   System.out.println("strSpecificationId->"+strSpecificationId);
   System.out.println("strSessionId->"+strSessionId);
  
   System.out.println("[NewOnDisplayBillingAccount]Sesión a consultar : " + strSessionId);
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   System.out.println("[NewOnDisplayBillingAccount]Sesión a consultar : " + objPortalSesBean);
   if( objPortalSesBean == null ){
    throw new Exception("No se encontró la sesión del usuario");
   }
   
   iUserId  = objPortalSesBean.getUserid();   
   iAppId   = objPortalSesBean.getAppId();
  
   String strLogin=objPortalSesBean.getLogin();
   
   String strMessage=null;
   ArrayList arrLista=null;
   BillingAccountBean objBillingABean=null;
   BillingContactBean objBillContBean=null;
   String strCustomerType=null;
   BillingAccountService objBAS=new BillingAccountService();
   GeneralService   objGeneralService   = new GeneralService();  
   
   
  //Valida si la Categoría es de tipo GROSS
   HashMap hshDataGross = new HashMap();
	 hshDataGross = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_GROSS);
   String strResultGross = (String) hshDataGross.get("strDescription");
   
   System.out.println("[NewOnDisplayBillingAccount][strResultGross]"+strResultGross);
   
   
   // Valida si la Categoría es un Serv. Adic (2016)
   HashMap hshDataServAdic = new HashMap();
	 hshDataServAdic = objGeneralService.getDescriptionByValue(strSpecificationId,Constante.ST_ORDENES_SERV_ADIC);
   String strResultServAdic = (String) hshDataServAdic.get("strDescription");
   
   System.out.println("[NewOnDisplayBillingAccount][strResultServAdic]"+strResultServAdic);
   
   System.out.println("strGeneratorType: "+strGeneratorType);
   
  // Órdenes GROSS
  if (strResultGross != null && strResultGross.equals("1")){
  System.out.println("[NewOnDisplayBillingAccount][GROSS]");   
   
   //Si el origen viene desde OPP
   if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_OPP) ){
        
      System.out.println("lGeneratorId: "+lGeneratorId);      
      hshData = (new GeneralService()).getNpopportunitytypeid(lGeneratorId);
      strMessage = (String)hshData.get("strMessage");
      if ( strMessage != null ) throw new Exception(strMessage);  
      int iResult = MiUtil.parseInt((String)hshData.get("iReturnValue"));
      System.out.println("[NewOnDisplayBillingAccount][iResult]"+iResult);
       
      int iSalesID = MiUtil.parseInt((String)hshData.get("iSalesId"));  
      System.out.println("[NewOnDisplayBillingAccount][lCustomerId]"+lCustomerId);
      System.out.println("[NewOnDisplayBillingAccount][iSalesID]"+iSalesID);
      System.out.println("[NewOnDisplayBillingAccount][iResult]"+iResult);
      iPermission = (new GeneralService()).getValidateAuthorization(lCustomerId, iResult, iSalesID);
      System.out.println("[NewOnDisplayBillingAccount][iPermission]: "+iPermission);      
  
   //Si el origen viene de un Incidente   
   }else{// if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) )
        if( MiUtil.getString(strGeneratorType).equals(Constante.GENERATOR_TYPE_INC) ){//Si el origen es un Incidente
            hshData  = objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_BA, iUserId, iAppId);  
            strMessage = (String)hshData.get("strMessage");
                if ( strMessage != null ) throw new Exception(strMessage); 
            iPermission  = MiUtil.parseInt((String)hshData.get("iRetorno"));   
        }
      }
  }
 else{//Órdenes Serv. Adicionales: no se requiere validar autorizaciones o permisos a los roles de trabajo
      System.out.println("[NewOnDisplayBillingAccount][ELSE]");  
      if(strResultServAdic != null && strResultServAdic.equals("1")){
         System.out.println("[NewOnDisplayBillingAccount][SERV ADICIONAL]");  
         iPermission = 1;
      }
   }
  
  System.out.println("[NewOnDisplayBillingAccount][General]: "+iPermission);   
  if (iPermission==1){
  //if (true){
    if (lSiteId!=0)
      strCustomerType=Constante.CUSTOMERTYPE_SITE; 
    else
      strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER;
   
   HashMap hshBillAcc=objBAS.getAccountList(strCustomerType,lSiteId,lOrderId); 
   
   strMessage=(String)hshBillAcc.get("strMessage");   
   if (strMessage!=null)
      throw new Exception(strMessage); 
   
   arrLista=(ArrayList)hshBillAcc.get("objCuentas");        
   System.out.println("Tamaño del arreglo de billingAccount List-->"+arrLista.size());
   String strRutaContext=request.getContextPath();   
   String strURLBillAccEdit = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountEdit.jsp"; 
   String strURLBillAccNew  = strRutaContext+"/DYNAMICSECTION/DYNAMICBILLINGACCOUNT/BillingAccountPages/BillingAccountNew.jsp";    
   String strURLOrderServlet = strRutaContext+"/editordersevlet";      
   String strURLGeneralPage = strRutaContext+"/GENERALPAGE/GeneralFrame.jsp";
%>

<script DEFER>

   function verVector()
   {
        for(var i=0;i<apNewBillAcc.length;i++){                        
          var variable=  "id->"+apNewBillAcc[i].billAccId +" name-->"+apNewBillAcc[i].billAccName + " "+
                         " title->"+apNewBillAcc[i].titleName +" FName->"+apNewBillAcc[i].contfName +" "+ 
                         " lName->"+apNewBillAcc[i].contlName +" Cargo->"+apNewBillAcc[i].cargo +" "+        
                         " pArea->"+apNewBillAcc[i].phoneArea +" pNumber->"+apNewBillAcc[i].phoneNumber +" "+
                         " ad1->"+apNewBillAcc[i].address1 +" ad2->"+apNewBillAcc[i].address2 +" "+ 
                         " dep->"+apNewBillAcc[i].depart +" prov->"+apNewBillAcc[i].prov +" "+
                         " dist->"+apNewBillAcc[i].dist +" zipCode->"+apNewBillAcc[i].zipCode +" "+
                         " state->"+apNewBillAcc[i].state +
                         " bscsCustId->"+apNewBillAcc[i].bscsCustId +" bscsSeq->"+apNewBillAcc[i].bscsSeq;
           alert(variable);    
       }    
       return true;
   };
   



</script>
 <br>
 <input type=hidden name="hdnTypeCustomer" value="<%=strTypeCustomer%>">
 <input type=hidden name="hdnOrderId" value="<%=lOrderId%>"> 
 <input type=hidden name="hdnCustomerId" value="<%=lCustomerId%>"> 
 <input type=hidden name="hdnSiteId" value="<%=lSiteId%>"> 
 <input type=hidden name="hdnLogin" value="<%=strLogin%>"> 
 <input type=hidden name="hdnNewBillAccId" value=""> 
 <input type=hidden name="hdnSpecificationId" value="<%=strSpecificationId%>"> 
 
 <table border="0" cellspacing="0" cellpadding="0" width="40%">
     <tr> 
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Cuenta Facturaci&oacute;n</td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td align="right">    
        <a href="javascript:fxAddNewBillAcc();" > 
        <img style="height :20 px;" name="item_img0" Alt="Agregar Billing Account" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no">
        </a>
        </td>
     </tr>     
  </table>
 <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table id="tableBillAcc" name="tableBillAcc" border="0" width="100%" cellpadding="2" cellspacing="1">                     
              <tr id="cabecera">
                  <td class="CellLabel" align="center">#</td>
                  <td class="CellLabel" align="center">Cuenta Facturaci&oacute;n</td>
                  <td class="CellLabel" align="right" >&nbsp;  </td>                                 
              </tr>
              <% for (int i=0;i< arrLista.size();i++)
                 {   objBillingABean=(BillingAccountBean)arrLista.get(i);
                     objBillContBean=objBillingABean.getObjBillingContactB();
                     System.out.println( i+1 + " -> " + objBillingABean.getNpBillaccountNewId()+ " + " + objBillingABean.getNpBillacCName() );
                
              %>
              <tr id="<%=i+1%>">
                  <td class="CellContent" align="left"><%=i+1%></td>
                  <td class="CellContent" align="left">
                    <a href="javascript:fxShowDetailBA(<%=objBillingABean.getNpBillaccountNewId()%>,<%=lCustomerId%>,<%=lSiteId%>,<%=i+1%>);"><%=MiUtil.getString(objBillingABean.getNpBillacCName())%></a>
                  </td>
                  <td class="CellContent" align="right">&nbsp;
                  <a href="javascript:fxDelete(<%=i+1%>);"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2></a>
                  </td>      
              </tr>
              <script DEFER>
                 apNewBillAcc[indBillAcc]= 
                 new fxMakeBillAccNew(
                 "<%=MiUtil.getString(objBillingABean.getNpBillaccountNewId())%>",
                 "<%=MiUtil.getString(objBillingABean.getNpBillacCName())%>", 
                 "<%=MiUtil.getString(objBillContBean.getNpTitle())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpfname())%>",
                 "<%=MiUtil.getString(objBillContBean.getNplname())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpjobtitle())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpphonearea())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpphone())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpaddress1())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpaddress2())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpdepartment())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpstate())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpcity())%>",
                 "<%=MiUtil.getString(objBillContBean.getNpzipcode())%>","Listado",
                 "<%=MiUtil.getString(objBillingABean.getNpBscsCustomerId())%>","<%=MiUtil.getString(objBillingABean.getNpBscsSeq())%>");     
                  indBillAcc=indBillAcc+1;
              </script>
              <% }%>              
         </table>
       </td>
      </tr>
      <tr>
        <td>
         <table id="hdnBillAcc" name="hdnBillAcc">   
         </table>
        </td>
      </tr> 
 </table>    
 
 <br>
 
 <script DEFER>
 
 function fxAddNewBillAcc(){
 	 //Si es un cliente LARGE validar si selecciono responsable de pago
	 if (!fxValidateSelectionRP()){
		return;
	 }
     var v_parametros = "?sUrl=<%=strURLBillAccNew%>"+"¿nOrderId=<%=lOrderId%>|nCustomerId=<%=lCustomerId%>|nSiteId=<%=lSiteId%>|strSpecificationId=<%=strSpecificationId%>|strSessionId=<%=strSessionId%>";
     var v_Url=   "<%=strURLGeneralPage%>" +v_parametros;
     window.open(v_Url, "Billing_Account","status=yes, location=0, width=440, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
  }

  /*function fxAAA(){  
      for(var i=0;i<apNewBillAcc.length;i++){      
    	 var variable=  "id->"+apNewBillAcc[i].billAccId +" name-->"+apNewBillAcc[i].billAccName + " "+
                      " title->"+apNewBillAcc[i].titleName +" FName->"+apNewBillAcc[i].contfName +" "+ 
                      " lName->"+apNewBillAcc[i].contlName +" Cargo->"+apNewBillAcc[i].cargo +" "+        
                      " pArea->"+apNewBillAcc[i].phoneArea +" pNumber->"+apNewBillAcc[i].phoneNumber +" "+
                      " ad1->"+apNewBillAcc[i].address1 +" ad2->"+apNewBillAcc[i].address2 +" "+ 
                      " dep->"+apNewBillAcc[i].depart +" prov->"+apNewBillAcc[i].prov +" "+
                      " dist->"+apNewBillAcc[i].dist +" zipCode->"+apNewBillAcc[i].zipCode +" "+
                      " state->"+apNewBillAcc[i].state + 
                      " bscsCustId->"+apNewBillAcc[i].bscsCustId +" bscsSeq->"+apNewBillAcc[i].bscsSeq;
        alert(variable);  
      }
  }*/
  
  function fxShowDetailBA(baId,customerId,siteId,arrInd){
     var v_parametros = "?sUrl=<%=strURLBillAccEdit%>"+"¿nNewBillAccId=" + baId +"|hdnSessionId=<%=strSessionId%>|nOrderId=<%=lOrderId%>|nCustomerId="+customerId+"|nSiteId="+siteId+"|nIndex="+arrInd; //1:Agregar  2:Editar                              
     var v_Url=   "<%=strURLGeneralPage%>" +v_parametros;         
     window.open(v_Url, "Billing_Account","status=yes, location=0, width=450, height=500, left=100, top=100, scrollbars=no, screenX=50, screenY=100");
  }

   function fxDelete(index){      
     form = document.frmdatos; 
     var table = document.all ? document.all["tableBillAcc"]:document.getElementById("tableBillAcc");
     var rowIndice=document.getElementById(index).rowIndex; 
     table.deleteRow(rowIndice);     
     apNewBillAcc[index-1].state='Eliminado';                  
     indBillAcc=indBillAcc-1;
     fxRenumeric();
     //fxDeleteTableBA();     
     //IND 536
     //var baId = Number(apNewBillAcc[index-1].billAccId) + 1;
	  var baId = Number(apNewBillAcc[index-1].billAccId);
     var nameBA= apNewBillAcc[index-1].billAccName;
	  var orderId= "<%=strOrderId%>";
	  var customerId= "<%=strCustomerId%>";      
     var specificationId= "<%=strSpecificationId%>";         
     
	  param= "?nbaId="+baId;
     //param= "?nbaId="+baId+"&hdnSpecificationId="+specificationId;	  
     form.hdnNewBillAccId.value = baId;
     form.myaction.value="DeleteBillAcc";           
     form.action="<%=strURLOrderServlet%>"+param;        
     form.submit();         
   }
   
   function fxDeleteTableBA(){
      form = document.frmdatos;
      var table = document.getElementById("table_assignment");      
      for (z=table.rows.length;z>1;z--)
         table.deleteRow(z-1);      
   }
   
  
  function fLoadNewBillAccArr(ObjectReject){    	  
    	   var tam= apNewBillAcc.length;    
         apNewBillAcc[tam]=new fCloneObject(ObjectReject);
         indBillAcc++;
    } 
    
    function fReloadBillAccList(customerId,siteId,tipo,ind){
    	     
         var table = document.all?document.all["tableBillAcc"]:document.getElementById("tableBillAcc");
         var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.         
         var CellBillName,CellIconoDel;         
         //alert("tamaño del vercto->"+apNewBillAcc.length);
         if (tipo==1){		             
         	           var tam= apNewBillAcc.length;                    
		                 row = table.insertRow(-1); 
		                 row.id =tam;
		                 
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";               
		                 col.innerHTML = indBillAcc;
		   
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";               
		                 col.innerHTML = "<a href=javascript:fxShowDetailBA('0',"+customerId+","+siteId+","+tam+");>"+apNewBillAcc[tam-1].billAccName+"</a>"; 
		                 		                 		              
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";    
		                 col.align     = "right";              
		                 col.innerHTML =  "<a href=javascript:fxDelete("+tam+")><img src='websales/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";
           
             //fxVerArray(tam-1);
         }else if (tipo ==2){     	  
         	  if (tbody.hasChildNodes()){   
         	  	  var rowIndice=document.getElementById(ind).rowIndex;          	  	 
         	      CellBillName =  table.rows[rowIndice].cells[1];            	                      
         	      CellIconoDel =  table.rows[rowIndice].cells[2];           	                                 
         	     
         	      CellBillName.innerHTML="<a href=javascript:fxShowDetailBA("+apNewBillAcc[ind-1].billAccId+","+customerId+","+siteId+","+ind+");>"+apNewBillAcc[ind-1].billAccName+"</a>";
         	      CellIconoDel.innerHTML="<a href=javascript:fxDelete("+ind+");><img src='websales/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";
         	  }    
         	    //  fxVerArray(ind-1);
         	}
         
         //fSetRejects();
         
      }
      
    var apNewBillAcc = new Array();    
    var indBillAcc=0;   

    function fCloneObject(ObjectReject){
         for (var i in ObjectReject){
            this[i] = ObjectReject[i];
         }
    }
    
    function fLoadNewBillAccArr(ObjectReject){    	  
    	   var tam= apNewBillAcc.length;    
         apNewBillAcc[tam]=new fCloneObject(ObjectReject);
         indBillAcc++;
    }  
    
    function fLoadUpdBillAccArr(ObjectReject,index){    	 
         apNewBillAcc[index]=new fCloneObject(ObjectReject); 
    }     
    
    function fReloadBillAccList(customerId,siteId,tipo,ind){
    	     
         var table = document.all?document.all["tableBillAcc"]:document.getElementById("tableBillAcc");
         var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.         
         var CellBillName,CellIconoDel;         
         //alert("tamaño del vercto->"+apNewBillAcc.length);
         if (tipo==1){		             
         	           var tam= apNewBillAcc.length;                    
		                 row = table.insertRow(-1); 
		                 row.id =tam;
		                 
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";               
		                 col.innerHTML = indBillAcc;
		   
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";               
		                 col.innerHTML = "<a href=javascript:fxShowDetailBA('0',"+customerId+","+siteId+","+tam+");>"+apNewBillAcc[tam-1].billAccName+"</a>"; 
		                 		                 		              
		                 col = row.insertCell(-1);
		                 col.className = "CellContent";    
		                 col.align     = "right";              
		                 col.innerHTML =  "<a href=javascript:fxDelete("+tam+")><img src='websales/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";
           
             fxVerArray(tam-1);
         }else if (tipo ==2){     	  
         	  if (tbody.hasChildNodes()){   
         	  	  var rowIndice=document.getElementById(ind).rowIndex;          	  	 
         	      CellBillName =  table.rows[rowIndice].cells[1];            	                      
         	      CellIconoDel =  table.rows[rowIndice].cells[2];           	                                 
         	     
         	      CellBillName.innerHTML="<a href=javascript:fxShowDetailBA("+apNewBillAcc[ind-1].billAccId+","+customerId+","+siteId+","+ind+");>"+apNewBillAcc[ind-1].billAccName+"</a>";
         	      CellIconoDel.innerHTML="<a href=javascript:fxDelete("+ind+");><img src='websales/images/Eliminar.gif' alt='Eliminar' border='0' hspace=2></a>";
         	  }    
         	      fxVerArray(ind-1);
         	}
         
         //fSetRejects();
         
      }
      
        
    function fxRenumeric(){    	
       var table = document.all?document.all["tableBillAcc"]:document.getElementById("tableBillAcc");       
       var CellIndex;                         
           for(var i=1;i<=indBillAcc;i++){  
                CellIndex =  table.rows[i].cells[0];  	  
                CellIndex.innerHTML= i;
           }
    }
    
    function fxVerArray(i){
    	 var variable=  "id->"+apNewBillAcc[i].billAccId +" name-->"+apNewBillAcc[i].billAccName + " "+
                      " title->"+apNewBillAcc[i].titleName +" FName->"+apNewBillAcc[i].contfName +" "+ 
                      " lName->"+apNewBillAcc[i].contlName +" Cargo->"+apNewBillAcc[i].cargo +" "+        
                      " pArea->"+apNewBillAcc[i].phoneArea +" pNumber->"+apNewBillAcc[i].phoneNumber +" "+
                      " ad1->"+apNewBillAcc[i].address1 +" ad2->"+apNewBillAcc[i].address2 +" "+ 
                      " dep->"+apNewBillAcc[i].depart +" prov->"+apNewBillAcc[i].prov +" "+
                      " dist->"+apNewBillAcc[i].dist +" zipCode->"+apNewBillAcc[i].zipCode +" "+
                      " state->"+apNewBillAcc[i].state + //" tipo->"+apNewBillAcc[i].tipo +" "+ 
                      " bscsCustId->"+apNewBillAcc[i].bscsCustId +" bscsSeq->"+apNewBillAcc[i].bscsSeq;
    }
    
    function fxMakeBillAccNew(billAccId,billAccName,titleName,contfName,contlName,
                              cargo,phoneArea,phoneNumber,address1,address2,
                              depart,prov,dist,zipCode,state,bscsCustId,bscsSeq){
        this.billAccId     =   billAccId;
        this.billAccName   =   billAccName;
        this.titleName     =   titleName;
        this.contfName     =   contfName;
        this.contlName     =   contlName;
        this.cargo         =   cargo;
        this.phoneArea     =   phoneArea;
        this.phoneNumber   =   phoneNumber;
        this.address1      =   address1;
        this.address2      =   address2;        
        this.depart        =   depart;
        this.prov          =   prov;
        this.dist          =   dist;
        this.zipCode       =   zipCode;    
        this.state         =   state;    
        //this.tipo          =   tipo;  
        this.bscsCustId    =   bscsCustId;
        this.bscsSeq       =   bscsSeq;     
  }
 </script>
 
 <script defer >
 
  function fxSectionBillAccountValidate(){           
     // Pasando los valores del vectos a hiddens     
     var Form = document.frmdatos;
     var codigoBSCS,varbscsSeq;          
     var vardist,varprov;
     fxDeleteRowsOfTable('hdnBillAcc');
     var table = document.all?document.all["hdnBillAcc"]:document.getElementById("hdnBillAcc");
     var tbody = table.getElementsByTagName("tbody")[0]; //ingreso al cuerpo de la tabla.         
     //alert("longitud del arreglo billing -->"+apNewBillAcc.length);
     //if (verVector()) 
     for(var i=0;i<apNewBillAcc.length;i++){ 
     
       if ((apNewBillAcc[i].billAccId==-1 && apNewBillAcc[i].state=='Eliminado') || 
             (apNewBillAcc[i].state=='Listado')){          
          
       }else{    
             row = tbody.insertRow(-1);                      
             //row.style="display:'none'"
                  
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndBillAccId' value='" + apNewBillAcc[i].billAccId + "' >" ;		                 
        
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndBillAccName' value='" + apNewBillAcc[i].billAccName + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndTitleName' value='" + apNewBillAcc[i].titleName + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndContfName' value='" + apNewBillAcc[i].contfName + "' >";		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndContlName' value='" + apNewBillAcc[i].contlName + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndCargo' value='" + apNewBillAcc[i].cargo + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndPhoneArea' value='" + apNewBillAcc[i].phoneArea + "' >" ;		                 
              
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndPhoneNumber' value='" + apNewBillAcc[i].phoneNumber + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndAddress1' value='" + apNewBillAcc[i].address1 + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndAddress2' value='" + apNewBillAcc[i].address2 + "' >";		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndDepart' value='" + apNewBillAcc[i].depart + "' >" ;		                 
             
             if (apNewBillAcc[i].prov=="0")
                varprov='';
             else
                varprov=apNewBillAcc[i].prov;
             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndProv' value='" + varprov + "' >" ;		                 

             if (apNewBillAcc[i].dist=="0")
                vardist='';
             else
                vardist=apNewBillAcc[i].dist;
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndDist' value='" + vardist + "' >";		                 

             col = row.insertCell(-1);		                 
             col.innerHTML ="<input type='hidden' name='hndZipCode' value='" + apNewBillAcc[i].zipCode + "' >" ;		                 

             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndState' value='" + apNewBillAcc[i].state + "' >";	

             /*col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndTipo' value='" + apNewBillAcc[i].tipo + "' >";	*/

             if (apNewBillAcc[i].bscsCustId=="0" || apNewBillAcc[i].bscsCustId=="-1")
                codigoBSCS='';
             else
                codigoBSCS=apNewBillAcc[i].bscsCustId;                
             col = row.insertCell(-1);		                              
             
             col.innerHTML = "<input type='hidden' name='hndBscsCustId' value='" + codigoBSCS + "' >";	
             
             if (apNewBillAcc[i].bscsSeq=="0")                
                varbscsSeq='';
             else
                varbscsSeq=apNewBillAcc[i].bscsSeq;
             col = row.insertCell(-1);		                 
             col.innerHTML = "<input type='hidden' name='hndBscsSeq' value='" + varbscsSeq + "' >";	

        }       
     }      
     return true;
  }
</script>  
<%
   }else{
%>
<table border="0" cellspacing="0" cellpadding="0" width="65%">  
<script defer>  
  function fxSectionBillAccountValidate(){
   return true;
   }
</script>   
</table>
<% }

}catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>
