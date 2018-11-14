<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));  
   //String strAction=(request.getParameter("sAction")==null?"R":request.getParameter("sAction"));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   SiteService objSiteService=new SiteService();
   GeneralService objGeneralService=new GeneralService();
   CustomerService objCustomerService=new CustomerService();
   String strMessage=null; 
   HashMap hshData=null;
   hshData=objGeneralService.getRegionList();
   
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralService.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");    
   
   ArrayList arrLista=null;
   ArrayList arrMandatoryAddrtype=new ArrayList();
   ArrayList arrMandatoryAddrtypeflag=new ArrayList();
   ArrayList arrUniqueTypeAddr=new ArrayList();
   ArrayList arrUniqueTypeAddrFlag=null;    
   ArrayList arrTypeAddr=null;   
   ArrayList arrNoChangeAddrtype=new ArrayList();
   ArrayList arrNoChangeAddrtypeflag=new ArrayList();  
   ArrayList arrAddress=null;
   String strObjectType=null;   
   HashMap hshMandatoryAdd=null;
   HashMap hshAddress=null;
   HashMap hshAddType=null;
   HashMap hshAddTypeFlg=null;  
   HashMap hshNoChangeAdd=null;
   String strNoChngTypeAddr =null;
   HashMap hshUbigeoList=null;
   HashMap hshTypeAddr =null;
   HashMap hshUniqueTypeAddr =null;
   String strTypeAddr = null;  
   long lObjectId=0; 
   int iAddrTypeLength=0;     
    
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strRutaContext=request.getContextPath();         
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLContactos="ContactList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;  
   String strURLAddress="AddressList.jsp"+strParam;  
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
   
   System.out.println(" -------------------- INICIO ADDRESS_EDIT.jsp---------------------- ");   
   System.out.println("strSessionId :" + strSessionId);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN ADDRESS_EDIT.jsp---------------------- ");
%>    
    
<% 
   if (lSiteId != 0){
      strObjectType=Constante.CUSTOMERTYPE_SITE;
      lObjectId=lSiteId;
   }else{
      strObjectType=Constante.CUSTOMERTYPE_CUSTOMER;
      lObjectId= lCustomerId;
   }
%>  

<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>Dirección</title>
</head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<DIV ALIGN="LEFT">
<TABLE  BORDER="0" CELLPADDING="0" CELLSPACING="0">
   <TR>
      <TD valign="top">&nbsp;&nbsp;</TD><TD>
         <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
         <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
         <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<a href="<%=strURLGeneralData%>"><FONT class="TabBackgroundText">Datos Generales</FONT></a>&nbsp;&nbsp;</td>
         <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
         </TABLE>
      </TD>
      <TD>
         <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
         <TD class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>    
         <td valign="middle" NOWRAP class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Direcciones</FONT>&nbsp;&nbsp;</td>
         <TD align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
         </TABLE>
      </TD>
      <TD>
         <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
         <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
         <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLContactos%>"><FONT class="TabBackgroundText">Contactos</FONT></A>&nbsp;&nbsp;</td>
         <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
         </TABLE>
      </TD>
      <!--Deshabilitar esta opción. Valor en la NP_TABLE-->
      <%if( strStatusBA.equals(Constante.IND_STATUS_ACTIVE) ){%>
      <TD>
          <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
          <TR>
             <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
             <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLBillAcc%>"><FONT class="TabBackgroundText">Billing Account</FONT></A>&nbsp;&nbsp;</td>
             <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
          </TR>
          </TABLE>
      </TD>      
      <%}%>
      <!--Deshabilitar esta opción. Valor en la NP_TABLE-->
   </TR>
</TABLE>
<TABLE  WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0" class="TabForegroundColor">
   <TR><TD  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD></TR>
</TABLE>   

</DIV>
<TABLE  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
<TR><TD WIDTH="100%">
<TABLE BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
   <TR class="PortletHeaderColor">
      <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
      <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
      <FONT class="PortletHeaderText">Direcciones para Site Nuevo</FONT>
      &nbsp;&nbsp;&nbsp;
      </TD>
      <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;</TD>
      <TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD>
   </TR> 
</TABLE>

<TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
   <TR><TD class="RegionHeaderColor" WIDTH="100%">
  
<%          
   hshAddress= objSiteService.getAddressOrContactList("SITE",lSiteId,"TIPODIRECCION"); 
   arrAddress=(ArrayList)hshAddress.get("objArrayList");
   strMessage=(String)hshAddress.get("strMessage");           
   if (strMessage!=null) 
      throw new Exception(strMessage);     

   String strType =objCustomerService.getCustomerType(lCustomerId);            
   if (lSiteId ==0 ){
      strType = "Prospect";  // Cosideramos Tipo de Site Nuevo 'Prospect'
   }         
          
%>          
              
   <script language="JavaScript">
      var ArrayUniqueAddress = new Array();
      var ArrayMandatoryAddress = new Array();
      var ArrayMandatoryAddressFlag = new Array();
      var ArrayNoChangeAddress = new Array();
      var ArrayNoChangeAddressFlag = new Array();    
      var arrAddress = new Vector(); 
      var CPIndex = -1;
      var Heading = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";     // heading for pages menu
      var selected = "";
   
      function fxAddress(addr1,addr2,addr3) {
         this.addr1 = addr1;
         this.addr2 = addr2;
         this.addr3 = addr3;
      }
     
   </script>     
   <form method="post" name="formdatos" target="bottomFrame" >
      <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
      <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
      <input type="hidden" name="hdnSiteId"     value="<%=lSiteId%>">            
      <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
      <input type="hidden" name="hdnTipo" value="SITE">
      <input type="hidden" name="hdnId"   value="<%=lSiteId%>"> 
      <input type="hidden" name="flg_enabled" VALUE="1">
      <input type="hidden" name="myaction"/>   
      <input type="hidden" name="hdnSessionId"/>
	  <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
            
      <table width="60%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
         <tr>
            <td class="CellLabelContent" valign="top" nowrap>
               <font class="Required">* : </font>Datos Obligatorios<br>
               <font class="Required">Observaci&oacute;n : </font>          
            
            <% //Direcciones Obligatorias
               hshMandatoryAdd=objSiteService.getCheckMandatoryAddress(lCustomerId,lSiteId); 
               strMessage=(String)hshMandatoryAdd.get("strMessage");
               if (strMessage!=null)
                  throw new Exception(strMessage);
                                 
               arrMandatoryAddrtype=(ArrayList)hshMandatoryAdd.get("objAddrType");
               arrMandatoryAddrtypeflag= (ArrayList)hshMandatoryAdd.get("objAddrTypeFlg");
            
               if (arrMandatoryAddrtype.size() >0){
            %>
                  <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Direcciones obligatorias -                     
            <%                     
                  for (int i=0; i<arrMandatoryAddrtype.size();i++){        
                     hshAddType = (HashMap)arrMandatoryAddrtype.get(i);
                     hshAddTypeFlg = (HashMap)arrMandatoryAddrtypeflag.get(i);
            %>
                  <script>
                     ArrayMandatoryAddress[ArrayMandatoryAddress.length] = '<%=MiUtil.getString((String)hshAddType.get("addstype"))%>';
                     ArrayMandatoryAddressFlag[ArrayMandatoryAddressFlag.length] = '<%=MiUtil.getString((String)hshAddTypeFlg.get("addrtypeflag"))%>';
                  </script>                     
                  <b><%=MiUtil.initCap(MiUtil.getString((String)hshAddType.get("addstype")))%></b>);
                  <%   if(i > 1 && i != arrMandatoryAddrtype.size()){%>                        
                  ,                         
                  <%   }
                  }
               }
            
            //Direcciones Unicas
               String strTypeCustom=null;
               if (lSiteId == 0){                                       
                  strTypeCustom=Constante.CUSTOMERTYPE_CUSTOMER;
               }else{    
                  strTypeCustom=Constante.CUSTOMERTYPE_SITE; 
               }
                        
               //hshData = objSiteService.getUniqueAddresses(strType,strTypeCustom);
                hshData=objSiteService.getCheckUniqueAddress(lCustomerId,lSiteId);
                strMessage=(String)hshData.get("strMessage");
                if (strMessage!=null)
                   throw new Exception(strMessage);                 
                arrUniqueTypeAddr=(ArrayList)hshData.get("arrType");

            %>
               <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Direcciones únicas -                 
            <% for (int i=0; i<arrUniqueTypeAddr.size();i++){   
                     hshUniqueTypeAddr=  (HashMap)arrUniqueTypeAddr.get(i);
            %>
               <script>
                  ArrayUniqueAddress[ArrayUniqueAddress.length] = '<%=MiUtil.initCap(((String)hshUniqueTypeAddr.get("addrtype")))%>';
               </script>
               <b><%=MiUtil.initCap(MiUtil.getString((String)hshUniqueTypeAddr.get("addrtype")))%></b>                     
               <%  if ((i+1)<arrUniqueTypeAddr.size()){%>
               ,
               <%  }         
               }                   
               %>                     
               <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               Una misma direcci&oacute;n puede tener todos los tipos de direcci&oacute;n a la vez, o una direcci&oacute;n por cada tipo,
               o la combinaci&oacute;n de cualquiera de ellas.
               </td>
         </tr>
      </table>
      <%  
             int wn_count_no_editable=0;            
            //Obtenemos todos los tipos de direcciones            
            hshAddType=objSiteService.getTypeAddresses(lCustomerId,lSiteId);
            strMessage=(String)hshAddType.get("strMessage");
            if (strMessage!=null){
               throw new Exception(strMessage);
            }                                             
            
            arrTypeAddr=(ArrayList)hshAddType.get("arrListado");
            hshAddType=null;
            System.out.println("arrTypeAddr-->"+arrTypeAddr);            
            //Obtenemos los tipos de direcciones que no pueden cambiar
            hshNoChangeAdd=objSiteService.getNoChangeAddress(lCustomerId,lSiteId);            
            strMessage=(String)hshNoChangeAdd.get("strMessage");
            if (strMessage!=null){
               throw new Exception(strMessage);
            }                                  
            arrNoChangeAddrtype=(ArrayList)hshNoChangeAdd.get("objAddrType");
            arrNoChangeAddrtypeflag= (ArrayList)hshNoChangeAdd.get("objAddrTypeFlg");                                   
            
            if (strMessage!=null ){
               throw new Exception(strMessage);
            }
            for (int i =0; i<arrNoChangeAddrtype.size();i++){                        
              hshAddType = (HashMap)arrNoChangeAddrtype.get(i);             
              hshAddTypeFlg = (HashMap)arrNoChangeAddrtypeflag.get(i);
            %>   
               <script>
                  ArrayNoChangeAddress[ArrayNoChangeAddress.length] = '<%=MiUtil.initCap((String)hshAddType.get("addstype"))%>';
                  ArrayNoChangeAddressFlag[ArrayNoChangeAddressFlag.length] = '<%=MiUtil.getString((String)hshAddType.get("addrtypeflag"))%>';
               </script>              
            <%}
            //Inicio Loop de Direcciones
            String strNoChngAddTypeSelec=null;
            int iEdicion=0;
            String strAddressId=null;            
            String strAddress1=null;
            String strAddress2=null;
            String strAddress3=null;
            String strZip=null;     
            int iLegal=0;
            int iFacturacion=0;
            int iEntrega=0;
            int iOtra=0;
            int iComunicacion=0;
            int iInstalacion=0;
            int iCorrespondencia=0;
            String strDepartmento=null;
            String strProvincia=null;
            String strDistrito=null;    
            long lRegionId=0;
            String strNote=null;
            String strFlagAddress=null; //NBRAVO e
            int iFlagAddress=0;         //NBRAVO e
            int cont=0;     //NBRAVO
            String strDptoId=null;
            String strProvId=null;
            String strDistId=null;  
            String flag=null;
            flag="true";
            int indice =1;

            hshAddress=null;
            for(int iIndexAddres=0; iIndexAddres<arrAddress.size();iIndexAddres++){
              hshAddress       =(HashMap)arrAddress.get(iIndexAddres);
              strAddressId     =MiUtil.getIfNotEmpty((String)hshAddress.get("swaddressid"));
              strAddress1      =MiUtil.getString((String)hshAddress.get("swaddress1"));              
              strAddress2      =MiUtil.getString((String)hshAddress.get("swaddress2"));
              strAddress3      =MiUtil.getString((String)hshAddress.get("swaddress3"));
              strZip           =MiUtil.getString((String)hshAddress.get("swzip"));
              iLegal           =Integer.parseInt((String)hshAddress.get("domicilio"));
              iFacturacion     =Integer.parseInt((String)hshAddress.get("facturacion"));
              iEntrega         =Integer.parseInt((String)hshAddress.get("entrega"));
              iOtra            =Integer.parseInt((String)hshAddress.get("otra"));
              iComunicacion    =Integer.parseInt((String)hshAddress.get("comunicacion"));
              iInstalacion     =Integer.parseInt((String)hshAddress.get("instalacion"));
              iCorrespondencia =Integer.parseInt((String)hshAddress.get("correspondencia"));
              strDepartmento   =MiUtil.getString((String)hshAddress.get("departamento"));
              strProvincia     =MiUtil.getString((String)hshAddress.get("provincia"));
              strDistrito      =MiUtil.getString((String)hshAddress.get("distrito"));
              lRegionId        =Long.parseLong((String)hshAddress.get("swregionid"));
              strNote          =MiUtil.getString((String)hshAddress.get("swnote"));
              iFlagAddress     =MiUtil.parseInt((String)hshAddress.get("flagAddress")); // NBRAVO e
              strDptoId        =MiUtil.getString((String)hshAddress.get("npdepartamentoid"));
              strProvId        =MiUtil.getString((String)hshAddress.get("npprovinciaid"));
              strDistId        =MiUtil.getString((String)hshAddress.get("npdistritoid"));
              
            
            /*   FETCH wc_list INTO rn_swaddressid, rv_swaddress1, rv_swaddress2, rv_swaddress3, rv_swzip,
                                  rv_legal,rv_facturacion,rv_entrega,rv_otra,rv_comunicacion,rv_instalacion,rv_departamento,
                                  rv_swprovincia, rv_swdistrito, rn_swregionid, rv_swnote, rn_npdepartamentoid,
                                  rn_npprovinciaid, rn_npdistritoid;
               EXIT WHEN wc_list%NOTFOUND;*/
               
               strNoChngAddTypeSelec="";               
               // Verificando si se puede Editar??
               /*if ("Prospect".equals(strType)){               
                  iEdicion = 1; // 
               }else if (iLegal==1 || iFacturacion==1 ||  iEntrega==1 || iInstalacion==1){
                  // no permite modificar datos de dirección, tampoco permite desasignar la dirección legal pero si permite
                  // asignar o desasignar otro tipo de dirección.                  
                  iEdicion = 0;
               }else{
                  iEdicion = 1;
               }*/

               iEdicion = 1;

               /*if (iEdicion == 0) {
                  if (strDepartmento==null || strProvincia ==null || strDistrito ==null ||
                     "0".equals(MiUtil.getIfNotEmpty(strDptoId)) || "0".equals(MiUtil.getIfNotEmpty(strProvId))|| 
                     "0".equals(MiUtil.getIfNotEmpty(strDistId))){
                      iEdicion = 2;
                  }else{
                      wn_count_no_editable = wn_count_no_editable + 1;
                  }
               }*/
               System.out.println("VALORES DE EDICION iEdicion-->"+iEdicion+" wn_count_no_editable-->"+wn_count_no_editable);
               System.out.println("----iFlagAddress-----"+iFlagAddress);
             // wn_index = wn_counter - wn_count_no_editable;
           %>         
              <input type="hidden" name="hdnAddressId" value="<%=strAddressId%>">
              <input type="hidden" name="hdnEdicion" value="<%=iEdicion%>">
         
         <table border="0" cellspacing="0" cellpadding="0" width="60%">
            <tr>
               <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="7"></td>
            </tr>
         </table>
         <!--<center>-->
         <table border="0" cellspacing="0" cellpadding="0">
            <tr class="PortletHeaderColor">
               <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
               <td class="SubSectionTitle" align="LEFT" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dirección&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
            </tr>
          </table>
          <!-- Inicio tabla de direccion-->
          <table width="90%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
             <tr><!-- Inicio fila de tipos de direcciones-->
                <td class="CellLabel" VALIGN="TOP" WIDTH="120"><font class="Required">*</font>Tipo Dirección</td>
                <td class="CellContent" COLSPAN="3">
                   <table width="375" cellpadding="0" cellspacing="0" align="left">                       
                 
                     <% int iAddUpdIndex=0;
                        int wn_addrxrow=3;
                        int wn_k=0;
                        int wn_i=0;                      
                        int iFlagCheck=0;
                        int iNumAddsAvailable=0; 
                        strNoChngTypeAddr=null;
                        HashMap hshNoChngTypeAddr=null;
                        for (int i=0; i<arrTypeAddr.size();i++){
                            if ((wn_i% wn_addrxrow )==0){
                               if(wn_i>0){ %>                               
                                 </tr>
                              <%}
                               wn_i=0;
                               %>
                                 <tr>
                      <%      }                       
                          
                          hshTypeAddr =(HashMap)arrTypeAddr.get(i);
                          strTypeAddr = MiUtil.getString((String)hshTypeAddr.get("valuedesc"));
                          //hshUnqTypeAddrFlg=null;                   
                          //La direccion se encuentra seleccionada??
                             if ("Facturacion".equals(strTypeAddr)){
                                if (iFacturacion == 1){
                                   iFlagCheck = 1;
                                }
                             }else if ("Entrega".equals(strTypeAddr)){
                                if (iEntrega ==1){
                                   iFlagCheck = 1;
                                }
                             }else if ("Comunicaciones".equals(strTypeAddr)){
                                if (iComunicacion == 1){
                                   iFlagCheck = 1;
                                }
                             }else if ("Instalacion".equals(strTypeAddr)){
                                if (iInstalacion == 1){
                                   iFlagCheck = 1;
                                }
                             }else if ("Otra".equals(strTypeAddr)){
                                if( iOtra == 1){
                                   iFlagCheck = 1;
                                }
                             }else if ("Correspondencia".equals(strTypeAddr)){
                                if( iCorrespondencia == 1){
                                   iFlagCheck = 1;
                                }
                             }
                             
                          wn_k = 0;   
                         // try{
                             //hshNoChngTypeAddr =(HashMap)arrNoChangeAddrtype.get(wn_k);                          
                             //strNoChngTypeAddr = MiUtil.getString((String)hshNoChngTypeAddr.get("addstype"));
                             strNoChngTypeAddr = MiUtil.getValue(arrNoChangeAddrtype,wn_k,"addstype");
                          /*}catch(Exception ex){
                              strNoChngTypeAddr="";     
                          }      */                 
                          //Verificar si esta direccion no es intercambiable                      
                          while ((wn_k<arrNoChangeAddrtype.size()) && (!strTypeAddr.equals(strNoChngTypeAddr))){ 
                             //try{
                             //hshNoChngTypeAddr =(HashMap)arrNoChangeAddrtype.get(wn_k+1);                            
                             //strNoChngTypeAddr = MiUtil.initCap((String)hshNoChngTypeAddr.get("addstype"));                             
                             strNoChngTypeAddr = MiUtil.getValue(arrNoChangeAddrtype,wn_k+1,"addstype");
                            /* }catch(Exception e){
                             }        */                    
                             wn_k++;
                          }                          
                          if (wn_k < arrNoChangeAddrtype.size() ){ //direccion no intercambiable en posicion wn_k                           
                             if (iFlagCheck==1){
                                //if (iEdicion != 1){
                        %>                                  
                                  <%-- td class="CellContent"><input type="checkbox" name="chkAddressDisabled" value="<%=MiUtil.getString(strTypeAddr)%>" checked disabled> <%=MiUtil.initCap(strTypeAddr)%></td --%>   
                        <%       /* strNoChngAddTypeSelec = strNoChngAddTypeSelec + " , " + MiUtil.getString(strTypeAddr);
                                }else{*/ %>
                                  <td class="CellContent"><input type="checkbox" name="chkAddressType" onclick="fxCheckUniqueAddress(this,<%=indice%>)" value="<%=MiUtil.initCap(strTypeAddr)%>" checked><%=MiUtil.initCap(strTypeAddr)%></td>
                        <%        iNumAddsAvailable ++;
                                //}
                             }else{
                        %>   
                             <td class="CellContent"><input type="checkbox" name="chkAddressType" onclick="fxCheckUniqueAddress(this,<%=indice%>)" value="<%=MiUtil.initCap(strTypeAddr)%>"><%=MiUtil.initCap(strTypeAddr)%></td>
                        <%   iNumAddsAvailable ++;
                             }
                           }else{
                               if (iFlagCheck == 0){
                        %>      
                                  <td class="CellContent"><input type="checkbox" name="chkAddressType" onclick="fxCheckUniqueAddress(this,<%=indice%>)" value="<%=MiUtil.initCap(strTypeAddr)%>"><%=MiUtil.initCap(strTypeAddr)%></td>
                        <%     }else{ %>
                                   <td class="CellContent"><input type="checkbox" name="chkAddressType" onclick="fxCheckUniqueAddress(this,<%=indice%>)" value="<%=MiUtil.initCap(strTypeAddr)%>" checked><%=MiUtil.initCap(strTypeAddr)%></td>
                        <%     }
                            iNumAddsAvailable ++;                       
                           }
                             iFlagCheck = 0;
                             wn_i ++;
                      }                      
                      if (( wn_addrxrow - wn_i ) > 0 ){ //wn_addrxrow tipo de direcciones por fila
                             //Completar la última fila con celdas en blanco.
                             for (int x=1;x<=(wn_addrxrow - wn_i);x ++){  
                     %>      
                                <td class="CellContent">&nbsp;</td>                               
                     <%      }%>                         
                             </tr>
                             
                    <% } %>                          
                          <input type="hidden" name="hdnNumAddsAvailable" value="<%=iNumAddsAvailable%>">
                          <input type="hidden" name="hdnNumAddsSelected" value="">
                          <input type="hidden" name="hdnCadAddsSelected" value="">
                        </table>
                     </td>
                  </tr><!--Fin fila de tipos de direcciones-->
             <tr><!-- Inicio fila de región-->
                <td class="CellLabel" valign="top"><font class="Required">*</font>Región&nbsp;</td>
                <td class="CellContent" valign="top" COLSPAN="3">
                <% if(iEdicion == 1 || iEdicion == 2){ %>
                 <select name="cmbRegion">                        
                   <%=MiUtil.buildComboSelected(arrRegion,"swregionid","swname",lRegionId+"")%>                                      
                 </select>  
                 <script>                   
                    document.formdatos.cmbRegion.value=<%=lRegionId%>
                 </script>                
                <%}else{%>
                <%=MiUtil.getDescripcion(arrRegion,"swregionid","swname",lRegionId+"")%>"
                <%}%>
                </td>
             </tr><!-- Fin fila de región-->
             <tr><!-- Inicio fila de Direcciones-->
                <td class="CellLabel" valign="top"><font class="Required">*</font>Dirección&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                   <% 
                    if (iEdicion == 1){
                        iAddUpdIndex = iAddUpdIndex + 1;                
                    %>    
                    <input type="text" name="txtAddress1" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                    <input type="text" name="txtAddress2" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                    <input type="text" name="txtAddress3" SIZE="50" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                    <script>
                        arrAddress.addElement(new fxAddress('<%=MiUtil.getString(strAddress1)%>','<%=MiUtil.getString(strAddress2)%>','<%=MiUtil.getString(strAddress3)%>'));
                    </script>
                  <%}else{%>
                        <%=(strAddress1 + " " + strAddress2 +" "+strAddress3+ " ")%>
                  <%}%>
                </td>
             </tr><!-- Fin fila de direcciones-->
             <tr><!-- Inicio fila de departamento, provincia-->
                <!-- Departamento-->
                <td class="CellLabel" valign="top" width="120"><font class="Required">*</font>Departamento&nbsp;</td>
                <td class="CellContent" valign="top" width="300">                
                    <% if(iEdicion == 1 || iEdicion == 2){ %>
                    <select name="cmbDpto" onChange="fxLoadPlace(1,<%=iIndexAddres%>);fxSetValueHidden('cmbDpto','hdnDpto',<%=iIndexAddres%>);">                                                           
                      <% hshUbigeoList = objGeneralService.getUbigeoList(0,0,"0"); 
                         strMessage = (String)hshUbigeoList.get("strMessage");
                         if (strMessage!=null)
                           throw new Exception(strMessage);
                           
                         arrLista = (ArrayList)hshUbigeoList.get("arrUbigeoList");
                      %>
                      <%=MiUtil.buildComboSelected(arrLista,"ubigeo","nombre",strDptoId)%>                    
                   </select>
                   <input type="hidden" name="hdnDpto" value="<%=strDepartmento%>">
                     <%}else{%>
                        <%=strDepartmento%>
                    <% }%>                  
                </td>
                <!-- Provincia-->
                <td class="CellLabel" VALIGN="TOP" WIDTH="110"><font class="Required">*</font>Provincia&nbsp;</td>
                <td class="CellContent" VALIGN="TOP">
                   <% if(iEdicion == 1 || iEdicion == 2){ %>                  
                   <select name="cmbProv" style="width: 85%;" onChange="fxLoadPlace(2,<%=iIndexAddres%>);fxSetValueHidden('cmbProv','hdnProv',<%=iIndexAddres%>);"
                     onclick="fxFirstLoadPlace(1,<%=iIndexAddres%>);">                                                                            
                      <%=MiUtil.buildComboWithOneOption(strProvId,strProvincia)%>   
                   </select>  
                   <input type="hidden" name="hdnProv" value="<%=strProvincia%>">
                     <%}else{%>
                        <%=strProvincia%>
                    <% }%>                   
                </td>
             </tr><!-- fin fila de departamento, provincia-->
             <tr><!-- inicio fila de distrito, cod. postal-->
                <!-- Distrito-->
                <td class="CellLabel" valign="top"><font class="Required">*</font>Distrito&nbsp;</td>
                <td class="CellContent" valign="top">
                   <% if(iEdicion == 1 || iEdicion ==2){ %>                
                   <select name="cmbDist" style="width: 90%;" onChange="fxLoadCodPostal(this.selectedIndex);fxSetValueHidden('cmbDist','hdnDist',<%=iIndexAddres%>);"
                   onclick="fxFirstLoadPlace(2,<%=iIndexAddres%>);">                                                                              
                       <%=MiUtil.buildComboWithOneOption(strDistId,strDistrito)%>   
                   </select>                                 
                   <input type="hidden" name="hdnDist" value="<%=strDistrito%>">
                    <%}else{%>
                       <%=strDistrito%>
                   <% }%>                    
                </td>
                <!-- Cod. Postal-->
                <td class="CellLabel" valign="top">&nbsp;Cod Postal&nbsp;</td>
                <td class="CellContent" valign="top">
                  <% if(iEdicion == 1 || iEdicion == 2){ %>                  
                   <input type="text" readonly="readonly" name="txtZip" ONFOCUS="this.blur();">
                  <%}else{%>
                      <%=strZip%>
                  <%}%>                   
                </td>
             </tr><!-- fin fila de distrito, cod. postal-->
             <tr><!-- inicio fila de Pais-->
                <td class="CellLabel" valign="top">&nbsp;País&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                   PERÚ<input type="hidden" name="hdnPais" value="PERU">
                </td>
             </tr><!-- Fin fila de Pais-->
             <tr><!-- inicio fila de Referencia-->
                <td class="CellLabel" valign="top">&nbsp;Referencia&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                  <% if(iEdicion == 1){ %>                
                   <textarea name="txtNote" COLS="120" ROWS="5" ONCHANGE="this.value=trim(this.value.toUpperCase());" maxlength="2000"><%=strNote%></textarea>
                  <%}else{%>
                     <%=strNote%>
                  <%}%>                       
                </td>
             </tr>
             <div id="trEntrega<%=indice%>" style="visibility:hidden">
             <tr id="trEntrega<%=indice%>" ><!--Inicio Direccion de entrega por defecto NBRAVO -->


                <td class="CellLabel" valign="top">&nbsp;Dir. de entrega por defecto&nbsp;</td>
                <td class="CellContent" colspan="3">  
                <% if(iEdicion == 1){    
                      if(iFlagAddress ==1){ %>
                        <input type="checkbox" name="chkflaAddress"  value="<%=strFlagAddress%>" checked>
                        <input type="hidden" name="hdnflagAddress" value="<%=iFlagAddress%>" ></td>
                   <%   }
                      else{ %>
                        <input type="checkbox" name="chkflaAddress" value="<%=strFlagAddress%>" >
                        <input type="hidden" name="hdnflagAddress" value="<%=iFlagAddress%>"  ></td>
                   <%   }  
                 }else{%>
                       <%=strFlagAddress%>
                  <%} indice++;%>  
                </td> 
               
             </tr><!--Fin Direccion de entrega por defecto-->
             
              </div>
          </table>
               <input type="hidden" name="hdnNoChangeAddsTypeSelected" value="<%=strNoChngAddTypeSelec%>">
               <!--Fin Tabla x Direccion-->              
               <%-- wn_counter=wn_counter + 1; --%>
               </CENTER>
            <%} //fin del recorrido de direcciones%>       
          <!-- Fin tabla de direccion-->
         <br>
         <table width="60%" cellpadding="0" cellspacing="1" >
           <tr>
             <td align="right">
               <input type="button" value="   Guardar   " name="btnsubmit" onclick="javascript:fxSubmit();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             </td>
             <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               <input type="button" value="   Cancelar   " name="btnCancelar" onClick="javascript:fxCancelAction();">
             </td>
           </tr>
         <table>
            <script>
               /*function fxDisabledNoChangeAddress(){                 
                  form=document.formdatos;                  
                  for(var j = 0; j < ArrayNoChangeAddress.length ; j++){                  
                     if(ArrayNoChangeAddressFlag[j]=='1'){
                       for(var i = 0; i < form.chkAddressType.length; i++){                          
                          if(form.chkAddressType[i].value == ArrayNoChangeAddress[j]){
                             //if ("<%-- =wn_unconditional_update --%>" != "1" ) {
                                form.chkAddressType[i].disabled = true;
                             //}
                          }
                       }
                     }
                  }
               }*/
            </script>

            <input type="hidden" name="hdnCounter" value="<%=arrAddress.size()%>">
            <script>
               var form = document.formdatos;
               if ( form.txtAddress1 != null ) {
                  if (form.txtAddress1.length > 1) {
                     for(idx = 0; idx < arrAddress.size(); idx++) {
                        objAddr = arrAddress.elementAt(idx);
                        form.txtAddress1[idx].value = objAddr.addr1;
                        form.txtAddress2[idx].value = objAddr.addr2;
                        form.txtAddress3[idx].value = objAddr.addr3;
                     }
                  }else { 
                     objAddr = arrAddress.elementAt(0);
                     form.txtAddress1.value = objAddr.addr1;
                     form.txtAddress2.value = objAddr.addr2;
                     form.txtAddress3.value = objAddr.addr3;
                  }
               }

                //fxDisabledNoChangeAddress();
            </script>
         <script language="JavaScript">
          // Agregado por CGREMIOS
          var flagProv;
          var flagDist;      
          var totalEditables=0;
          
          function fxCountEditable(){
                        
              //nya2
            var conHid=0;
            for (var i = 1; i <  document.formdatos.chkAddressType.length; i=i+6)
            {     
                  if (document.formdatos.chkAddressType[i].value=="Entrega"){
                    if (document.formdatos.chkAddressType[i].checked == true){                                       
                        //document.all[1].style.visibility='hidden';
                        conHid++;
                    }
                    else{
                        document.formdatos.chkflaAddress[conHid].disabled=true;
                        //document.all[1].style.visibility='hidden';
                        conHid++;
                    }
                  }
           
            }
              //fin
               if(form.hdnCounter.value > 1){
                 for(i=0; i < form.hdnCounter.value; i++){
                    if ( (eval("form.hdnEdicion["+i+"].value")=='1')  ){
                       totalEditables = eval(parseInt(totalEditables) + 1);
                    }
                 }
               }          
          }
          
          fxSetValueFlagProvDist();
          fxCountEditable();
          
         function fxSetValueFlagProvDist(){
             form = document.formdatos;
             var i=0;     
             var tam =totalEditables;
             if (tam>1){
               flagProv=new Array();
               flagDist=new Array();
               for (i=0;i< tam;i++){
                  flagProv[i]=0;  
                  flagDist[i]=0;
               }
             }else{                          
                 flagDist=0;
                 flagProv=0;
             }
         }          
          
          function fxFirstLoadPlace(tipo,ind){  
              form = document.formdatos;              
              if (form.hdnCounter.value>1){
                  if (tipo== 1 && flagProv[ind]==0){
                    flagProv[ind]=1;
                    flagDist[ind]=1; 
                    fxLoadPlace(tipo,ind);          
                  }
                  if (tipo== 2 && flagDist[ind]==0){
                    flagDist[ind]=1;                
                    fxLoadPlace(tipo,ind);                 
                  }             
              }else{    
                  if (tipo== 1 && flagProv==0){                    
                    flagProv=1;
                    flagDist=1;
                    fxLoadPlace(tipo,ind);          
                  }
                  if (tipo== 2 && flagDist==0){
                    flagDist=1;                
                    fxLoadPlace(tipo,ind);                 
                  }             
              }
          }
          
          function fxLoadPlace(tipo,ind) {                
              form = document.formdatos;
              var depId=0;
              var provId=0;

              if (totalEditables>1){
                depId= eval("document.formdatos.cmbDpto["+ind+"].value"); 
                provId= eval("document.formdatos.cmbProv["+ind+"].value");               
              }else{
                depId= eval("document.formdatos.cmbDpto.value"); 
                provId= eval("document.formdatos.cmbProv.value"); 
              }              
              if (tipo==1){ //Fue invocado por el combo de departamento                
                if (totalEditables>1){  
                   flagProv[ind]=1;
                   flagDist[ind]=1;                 
                }else{                          
                   flagProv=1;
                   flagDist=1;
                }
              }
              //if (tipo==2 && flagDist==0)
                 
              if (tipo==1 && depId==""){                    
                 if (totalEditables>1){          
                   eval("deleteOptionIE(document.formdatos.cmbDist["+ind+"])"); 
                   eval("deleteOptionIE(document.formdatos.cmbProv["+ind+"])");               
                 }else{
                   deleteOptionIE(document.formdatos.cmbDist);
                   deleteOptionIE(document.formdatos.cmbProv);                   
                   //ind=-1;
                 }                               
                 form.txtZip.value="";
              }else if (tipo==2 && (provId=="" || provId=="0")){                  
                  if (totalEditables>1){
                   eval("deleteOptionIE(document.formdatos.cmbDist["+ind+"])");    
                  }else{    
                   deleteOptionIE(document.formdatos.cmbDist);
                   //ind=-1;
                  }
                   form.txtZip.value="";
              }else{                      
                  form.myaction.value="LoadUbigeo2";                  
                  if (totalEditables>1){
                    eval("deleteOptionIE(document.formdatos.cmbDist["+ind+"])");                       
                  }else{
                    deleteOptionIE(document.formdatos.cmbDist);
                    ind=-1;
                  }
  
                  var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&nTipo="+tipo+"&nInd="+ind+"&sCodName=ubigeo&myaction=LoadUbigeo2"; 
                  parent.bottomFrame.location.replace(url);            
               }
            }

            function fxSetValueHidden(comboName,hiddenName,ind){               
               form = document.formdatos; 
               if(totalEditables>1){                  
                  eval("form."+hiddenName+"["+ind+"].value=form."+comboName+"["+ind+"][form."+comboName+"["+ind+"].selectedIndex].text");
               }else{
                  eval("form."+hiddenName+".value=form."+comboName+"[form."+comboName+".selectedIndex].text");
               }
            }        
            
            function fxLoadCodPostal(posicion){
                form = document.formdatos;     
                if (posicion==0) 
                  form.txtZip.value="";
                else{           
                   var dato=codPostal[posicion-1];
                   form.txtZip.value=dato;
                }          
            }      

            
            function fxSubmit(){
                form = document.formdatos;
                if (fxValidate()==false) 
                    return; 
                form.myaction.value="UpdateAddress";           
                form.hdnSessionId.value="<%=strSessionId%>"; 
				form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
                form.action='<%=strURLOrderServlet%>';          
                form.submit();                             
            }
     
             // Fin Agregado por CGREMIOS                  
            //algunos clientes pueden tener varias direcciones del tipo unico,con la validacion se obliga a actualizar.
            function fxValidaUniqueAddress(){
               form=document.formdatos;
               var cont = 0;
               //alert("Direccion Unica -->"+ArrayUniqueAddress[0]);
               for(var j = 0; j < ArrayUniqueAddress.length && ArrayUniqueAddress[j] != "Entrega"; j++){
                   for(var i = 0; i < form.chkAddressType.length; i++){
                      if( (form.chkAddressType[i].checked == true) && (ArrayUniqueAddress[j] == form.chkAddressType[i].value) ){
                         cont++;
                      }
                   }
                   if(cont > 1){
                      alert("Solo puede seleccionar una dirección de tipo "+ArrayUniqueAddress[j]);
                      return false;
                   }
                   cont = 0;
               }
               return true;
            }
            //Al hacer click en un checkbox que corresponde a una direccion tipo unica
           function fxCheckUniqueAddress(chkAddress, addressid,nombrediv){
            form = document.formdatos; 
            //nya           
            var conHid=0;      
//            alert(addressid);
            
                    if (document.formdatos.chkAddressType[6*addressid-5].checked == true){                                       
                        //document.all[1].style.visibility='hidden';
                        document.formdatos.chkflaAddress[addressid-1].disabled=false;
                    }
                    else{
                        document.formdatos.chkflaAddress[addressid-1].disabled=true;
                        document.formdatos.chkflaAddress[addressid-1].checked =false;
                        //document.all[1].style.visibility='hidden';      
                    }
                    
                    
           /* for (var i = 1; i <  document.formdatos.chkAddressType.length; i=i+6)
            {     
                  if (document.formdatos.chkAddressType[i].value=="Entrega"){
                  
                    if (document.formdatos.chkAddressType[i].checked == true){                                       
                        //document.all[1].style.visibility='hidden';
                        document.formdatos.chkflaAddress[addressid-1].disabled=false;
                    }
                    else{
                        document.formdatos.chkflaAddress[addressid-1].disabled=true;
                        //document.all[1].style.visibility='hidden';      
                    }
                  }           
            }*/

            
              
            if (chkAddress.value == "Instalacion" && chkAddress.checked == false){
              try {
               if (eval("form.hdnContractCountType"+addressid+".value") > 0){
                  alert("No se puede desmarcar la dirección de Instalación, tiene contratos relacionados.");
                  chkAddress.checked = true;
                  return false;
               }
              } catch(exception) {}
       
              
            }
         
             if(chkAddress.checked){              
                for(var j = 0; j < ArrayUniqueAddress.length ; j++){                
                   if(chkAddress.value == ArrayUniqueAddress[j]){ //es una direccion de tipo unica                                            
                      //desabilitar las otras direcciones del mismo tipo
                      for(var i = 0; i < form.chkAddressType.length; i++){                         
                         if(chkAddress.value == form.chkAddressType[i].value){
                            form.chkAddressType[i].checked = false;
                         }
                      }
                      chkAddress.checked = true;
                      break;
                   }
                }
              }
           }
         
            function fxCheckMandatoryAddress(){
               form=document.formdatos;
               for(var j = 0; j < ArrayMandatoryAddress.length ; j++){
                  cont = 0;
                  if(ArrayMandatoryAddressFlag[j]=='0'){
                     for(var i = 0; i < form.chkAddressType.length; i++){
                        if((ArrayMandatoryAddress[j] == form.chkAddressType[i].value) && (form.chkAddressType[i].checked)){
                           cont++;
                        }
                     }
                     if(cont==0){
                        alert("Debe haber por lo menos una dirección de tipo "+ArrayMandatoryAddress[j]);
                        return false;
                     }
                  }
               }
               return true;
            }
                    
            function fxUpdateNumAddsSelected(){
              form=document.formdatos;
              var v_cad_address_type = "";
              if( form.hdnCounter.value > 1){
                 for ( i=0; i < form.hdnCounter.value ; i++ ) {
                   v_cad_address_type = ""; 
                   v_count = 0;
                   num = eval("form.hdnNumAddsAvailable["+i+"].value");
                   //avanzamos hasta los checks que corresponden a la dirección
                   posIni = 0;                  
                   posFin = 0;
                   for(var j = 0; j < i; j++){
                      posIni = eval(parseInt(posIni) + parseInt(eval("form.hdnNumAddsAvailable["+j+"].value")));
                   }
                   posFin = eval(parseInt(posIni) + eval(parseInt(num) - 1));
                   for (var j = posIni; j <= posFin; j++) {
                      if ( form.chkAddressType[j].checked){
                         v_cad_address_type = v_cad_address_type + form.chkAddressType[j].value + ",";
                         v_count++;
                      }
                   }
                   form.hdnNumAddsSelected[i].value = v_count;
                   
                   form.hdnCadAddsSelected[i].value = v_cad_address_type;
                }
              }else{
                   v_count = 0;
                   num = form.hdnNumAddsAvailable.value;
                   //avanzamos hasta los checks que corresponden a la dirección
                   posIni = 0;                  
                   posFin = 0;
                   posFin = eval(parseInt(num) - 1);
                   for (var j = posIni; j <= posFin; j++) {
                      if ( form.chkAddressType[j].checked){
                         v_cad_address_type = v_cad_address_type + form.chkAddressType[j].value + ",";
                         v_count++;
                      }
                   }
                   form.hdnNumAddsSelected.value = v_count;
                   form.hdnCadAddsSelected.value = v_cad_address_type;
              }
           }
            function fxValidate(){
               var totalEditables=0;
               var totalSemiEditables=0;
               var num;
               var numNoEditables = 0;
               var numSemiEditables = 0;
               var index = 0;
               var exp="";
               var bool_exp;
               // INICIO nbravo
                var checkSelects = form.chkflaAddress;
                var restrictionItemId = form.hdnflagAddress;
                var contFlagAddress = 0;
                // FIN
               form=document.formdatos;
               
               /*Inicio cambios*/

               //VALIDA QUE HAYA SOLO UN FLAG DE DIRECCIOND E ENRTREGA SETEADO
                for (i= 0; i<checkSelects.length;i++){
                  if(checkSelects[i].checked == true){
                  contFlagAddress++; 
                  
                  }
                }
              
              if (contFlagAddress >1){       
              alert("ingrese una sola direccion de entrega por defecto");
              return false;
              }
               
             if (contFlagAddress==0){       
           
              alert("ingrese por lo menos una direccion de entrega por defecto");
              return false;
              }
               
               
               /// guarda
           /// function fxa(chkflaAddress)
            //{
          //  form = document.formdatos;
            for (i=0; i < form.chkflaAddress.length; i++){
           // alert(form.chkflaAddress[i].checked); 
              if (form.chkflaAddress[i].checked ==true){
                  form.hdnflagAddress[i].value=1;                            
                }
              else{
                  form.hdnflagAddress[i].value=0;
              }
              // alert(form.hdnflagAddress[i].value);             
            }
           // }
               /*fin cambios*/
               if (form.flg_enabled.value == 0) return false;
               if(form.hdnCounter.value > 1){
                 for(i=0; i < form.hdnCounter.value; i++){
                    if ( (eval("form.hdnEdicion["+i+"].value")=='1')  ){
                       totalEditables = eval(parseInt(totalEditables) + 1);
                    }
                 }
                 for(i=0; i < form.hdnCounter.value; i++){
                    if ( (eval("form.hdnEdicion["+i+"].value")=='2') ){
                       totalSemiEditables = eval(parseInt(totalSemiEditables) + 1);
                    }
                 }
                 for ( i=0; i < form.hdnCounter.value; i++ ){
                    index = eval(parseInt(i) - parseInt(numNoEditables));
                    //Los datos son editables???
                    if ( eval("form.hdnEdicion["+i+"].value")=='0' ){
                        numNoEditables = eval(parseInt(numNoEditables) + 1);
                        continue;
                    }
                    if ( eval("form.hdnEdicion["+i+"].value")=='2' ){
                        numSemiEditables = eval(parseInt(numSemiEditables) + 1);
                    }
                    if( (eval("form.hdnEdicion["+i+"].value")=='1')  ){
                       //Seleccionó por lo menos un tipo de direccion???
                       //avanzamos hasta los checks que corresponden a la dirección
                       posIni = 0;
                       posFin = 0;
                       for(var k = 0; k < i; k++){
                          posIni = eval(parseInt(posIni) + parseInt(eval("form.hdnNumAddsAvailable["+k+"].value")));
                       }
                       //numero de direcciones permitidas a seleccionar
                       num = eval("form.hdnNumAddsAvailable["+i+"].value");
                       posFin = eval(parseInt(posIni) + eval(parseInt(num) - 1));
                       exp = "";
                       if (posFin>posIni){
                          for (var k = posIni; k <= posFin; k++) {
                             exp = exp + " !form.chkAddressType["+k+"].checked ";
                             if (k<posFin){
                                exp = exp + "&&";
                             }
                          }
                       }else{
                          if(posFin>0){
                             exp = "!form.chkAddressType.checked ";
                          }
                       }
                       //alert("exp ="+exp);
                       if(exp != ""){
                         bool_exp = eval(exp);
                         if (bool_exp){
                             alert("Seleccione por lo menos un tipo de dirección.");
                             if(totalEditables>1){
                                eval("form.cmbRegion["+index+"].focus()");
                             }else{
                                form.cmbRegion.focus();
                             }
                             return false;
                         }
                       }
                       //Seleccionó Region???
                       index2 =  eval(parseInt(i) - parseInt(numNoEditables) - parseInt(numSemiEditables));
                       if(totalEditables>1){
                         if ( eval("form.cmbRegion["+index2+"].selectedIndex") == 0) {
                             alert("Seleccione la Región.");
                             eval("form.cmbRegion["+index+"].focus()");
                             return false;
                         }
                        }else{
                         if ( form.cmbRegion.selectedIndex == 0) {
                             alert("Seleccione la Región.");
                             form.cmbRegion.focus();
                             return false;
                         }
                       }
                       //Ingresó direccion???
                       if(totalEditables>1){
                         if ( (eval("form.txtAddress1["+index2+"].value.length")==0) && (eval("form.txtAddress2["+index2+"].value.length")==0) && (eval("form.txtAddress3["+index2+"].value.length")==0)){
                             alert("Ingrese la dirección.");
                             eval("form.txtAddress1["+index2+"].focus()");
                             return false;
                         }
                       }else{
                         if ( (form.txtAddress1.value.length==0) && (form.txtAddress2.value.length==0) && (form.txtAddress3.value.length==0)){
                             alert("Ingrese la dirección.");
                             form.txtAddress1.focus();
                             return false;
                         }
                       }
                    }
                    //Seleccionó Departamento
                    if(eval(parseInt(totalEditables) + parseInt(totalSemiEditables))>1){
                      if (eval("form.cmbDpto["+index+"].selectedIndex")==0){
                          alert("Seleccione el Departamento.");
                          eval("form.cmbDpto["+index+"].focus()");
                          return false;
                      }
                    }else{
                      if (form.cmbDpto.selectedIndex==0){
                          alert("Seleccione el Departamento.");
                          form.cmbDpto.focus();
                          return false;
                      }
                    }
                    //Seleccionó Provincia
                    if(eval(parseInt(totalEditables) + parseInt(totalSemiEditables))>1){
                      if (eval("form.cmbProv["+index+"].selectedIndex")==0){
                          alert("Seleccione la Provincia.");
                          eval("form.cmbProv["+index+"].focus()");
                          return false;
                      }
                    }else{
                      if (form.cmbProv.selectedIndex==0){
                          alert("Seleccione la Provincia.");
                          form.cmbProv.focus();
                          return false;
                      }
                    }
                    //Seleccionó distrito
                    if(eval(parseInt(totalEditables) + parseInt(totalSemiEditables))>1){
                      if (eval("form.cmbDist["+index+"].selectedIndex")==0){
                          alert("Seleccione el Distrito.");
                          eval("form.cmbDist["+index+"].focus()");
                          return false;
                      }
                    }else{
                      if (form.cmbDist.selectedIndex==0){
                          alert("Seleccione el Distrito.");
                          form.cmbDist.focus();
                          return false;
                      }
                    }
                 }
               }else{ //if hdnCounter
                 if( (form.hdnEdicion.value=='1') || (form.hdnEdicion.value=='2') ){
                   // Validar Tipo de direcciones
                   //numero de direcciones permitidas a seleccionar
                   num = form.hdnNumAddsAvailable.value;
                   if (num>1){
                      for (i = 0; i < num; i++) {
                         exp = exp + " !form.chkAddressType["+i+"].checked ";
                         if (i<num-1){
                            exp = exp + "&&";
                         }
                      }
                   }else{
                      exp = "!form.chkAddressType.checked ";
                   }
                   bool_exp = eval(exp);
                   if (bool_exp){
                       alert("Seleccione por lo menos un tipo de dirección.");
                       form.cmbRegion.focus();
                       return false;
                   }
                   if( form.hdnEdicion.value=='1'){
                      if (form.cmbRegion.selectedIndex==0){
                          alert("Seleccione la Región.");
                          form.cmbRegion.focus();
                          return false;
                      }
                      if (form.txtAddress1.value.length==0 && form.txtAddress2.value.length==0 && form.txtAddress3.value.length==0){
                          alert("Ingrese la dirección.");
                          form.txtAddress1.focus();
                          return false;
                      }
                   }
                   if (form.cmbDpto.selectedIndex==0){
                       alert("Seleccione el Departamento.");
                       form.cmbDpto.focus();
                       return false;
                   }
                   if (form.cmbProv.selectedIndex==0){
                       alert("Seleccione la Provincia.");
                       form.cmbProv.focus();
                       return false;
                   }
                   if (form.cmbDist.selectedIndex==0){
                       alert("Seleccione el Distrito.");
                       form.cmbDist.focus();
                       return false;
                   }
                 }
               }
               if ( !(fxValidaUniqueAddress()) ){
                  return false;
               }
               if( !(fxCheckMandatoryAddress()) ){
                  return false;
               }
               fxUpdateNumAddsSelected();
               form.flg_enabled.value = 0;
               // Actualizar los campos de dirección de entrega NBRAVO

               return true;
            }
            
            function fxSetComboOption(id,desc,cboName){             
               opcion=new Option(desc,id);               
               /*eval('document.formdatos.'+cboName+'.options[1]'=opcion);
               eval('document.formdatos.'+cboName+'.value'=id);     */                             
            }       
      
            function fxCancelAction(){               
               url="<%=strURLAddress%>";
               parent.mainFrame.location.replace(url);
            }

 
            </script>
            <!-- Fin Formulario de Edicion -->
            </form>
</html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>
