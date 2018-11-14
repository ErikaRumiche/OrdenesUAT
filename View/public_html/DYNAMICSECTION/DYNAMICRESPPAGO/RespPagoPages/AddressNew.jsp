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
   CustomerService objCustomerService=new CustomerService();
   String strMessage=null;
   HashMap hshData=null;
   GeneralService objGeneralService=new GeneralService();
   
   hshData=objGeneralService.getRegionList();
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");    
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralService.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   
   ArrayList arrLista=null;
   ArrayList arrMandatoryAddrtype=new ArrayList();
   ArrayList arrMandatoryAddrtypeflag=new ArrayList();
   ArrayList arrUniqueTypeAddr=new ArrayList();
   ArrayList arrUniqueTypeAddrFlag=null;   
   ArrayList arrTypeAddr=null;
   
   String strObjectType=null;
   long lObjectId=0;
   
   int iAddrTypeLength=0;
   
   HashMap hshMandatoryAdd=null;  
   HashMap hshTypeAddr =null;
   HashMap hshUniqueTypeAddr =null;
   String strTypeAddr = null;
   String strUniqueTypeAddr =null;
   HashMap hshUnqTypeAddrFlg=null;
   HashMap hshUbigeoList=null;
   HashMap hshAddType=null;
   int iFlagAddress=0;         //NBRAVO 
   int contadorFA=0;          //NBRAVO
   int iEntrega=0;          //NBRAVO
   int iUnqTypeAddrFlg=0;
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLContactos="ContactList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;    
   String strURLAddress="AddressList.jsp"+strParam; 
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
   
   System.out.println(" -------------------- INICIO ADDRESS_NEW.jsp---------------------- ");   
   System.out.println("strSessionId :" + strSessionId);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);     
   System.out.println("pSpecificationId :" + strSpecificationId);     
   System.out.println(" --------------------  FIN ADDRESS_NEW.jsp---------------------- ");
   
   //// NBRAVO
   HashMap hshAddress=null;
   ArrayList arrAddress=null;
   hshAddress= objSiteService.getAddressOrContactList("SITE",lSiteId,"TIPODIRECCION"); 
   arrAddress=(ArrayList)hshAddress.get("objArrayList");
   int ccc=0;
   int ccc2=0;
   hshAddress=null;
    for(int iIndexAddres=0; iIndexAddres<arrAddress.size();iIndexAddres++){
    hshAddress       =(HashMap)arrAddress.get(iIndexAddres);
    iFlagAddress     =MiUtil.parseInt((String)hshAddress.get("flagAddress")); // NBRAVO e
    iEntrega         =Integer.parseInt((String)hshAddress.get("entrega"));
    if (iFlagAddress==1){
      contadorFA++;
    }
    if (iEntrega==1){
      ccc2++;
    }
    ccc++;
    }
    ////  NBRAVO
  
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
      <script language="javascript">
         function fxCancelAction(){            
            url="<%=strURLAddress%>";
            parent.mainFrame.location.replace(url);
         }
      </script>
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
    <TR>
    <TD  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD></TR>
  </TABLE>   
  
  </DIV>
 <TABLE  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
  <TR><TD WIDTH="100%">

  <TABLE BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
  <TR class="PortletHeaderColor">
  <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
  <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
  <FONT class="PortletHeaderText">Direcciones para Site Nuevo</FONT>
  &nbsp;&nbsp;&nbsp;</TD>
  <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
  </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD></TR> 
  </TABLE>

 <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
 <TR><TD class="RegionHeaderColor" WIDTH="100%">
  
<%
          String strType =objCustomerService.getCustomerType(lCustomerId) ;
%>            
            <!-- REGION DE NUEVA DIRECCION -->
            <script language="JavaScript">

               var ArrayMandatoryAddress = new Array();
               var ArrayMandatoryAddressFlag = new Array();
               var CPIndex = -1;
               var Heading = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";    
               var selected = "";              
            </script>
            <% 
                
               hshAddType=objSiteService.getTypeAddresses(lCustomerId,lSiteId);
               strMessage=(String)hshAddType.get("strMessage");
               if (strMessage!=null){
                  throw new Exception(strMessage);
               }                                             
               
               arrTypeAddr=(ArrayList)hshAddType.get("arrListado");                           
                
               hshData=objSiteService.getCheckUniqueAddress(lCustomerId,lSiteId);
               strMessage=(String)hshData.get("strMessage");
               if (strMessage!=null)
                throw new Exception(strMessage);                 
               arrUniqueTypeAddrFlag=(ArrayList)hshData.get("arrTypeFlg");
            %>

            <form method="post" name="formdatos" target="bottomFrame" >
               <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
               <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
               <input type="hidden" name="hdnSiteId"     value="<%=lSiteId%>">
               <!-- input type="hidden" name="hdnRegionId"   value="<%-- =lRegionId --%>" -->
               <input type="hidden" name="hdnSessionId"       value="<%=strSessionId%>">
               <input type="hidden" name="flg_enabled"   value="1">
               <input type="hidden" name="hdnTipo" value="<%=Constante.CUSTOMERTYPE_SITE%>">
               <input type="hidden" name="hdnId"   value="<%=lSiteId%>">
			   <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
               <input type="hidden" name="myaction"/>   
            <!-- Notas -->
            <table width="60%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
            <tr>
               <td class="CellLabelContent" valign="top" nowrap>
                  <font class="Required">* : </font>Datos Obligatorios<br>
                  <font class="Required">Observaci&oacute;n : </font>          
                  
                 <% //Direcciones Obligatorias
                      hshAddType=null;
                      HashMap hshAddTypeFlg=null;                   
           
                      hshMandatoryAdd=objSiteService.getCheckMandatoryAddress(lCustomerId,lSiteId); 
                      strMessage=(String)hshMandatoryAdd.get("strMessage");
                      
                      if (strMessage!=null){
                         throw new Exception(strMessage);
                      }                      
                      arrMandatoryAddrtype=(ArrayList)hshMandatoryAdd.get("objAddrType");
                      arrMandatoryAddrtypeflag= (ArrayList)hshMandatoryAdd.get("objAddrTypeFlg");

                      if (arrMandatoryAddrtype.size() >0){
                  %>
                     <script> 
                        document.write("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Direcciones obligatorias - "):
                     </script>
                  <%                     
                      for (int i=0; i<arrMandatoryAddrtype.size();i++){        
                          hshAddType = (HashMap)arrMandatoryAddrtype.get(i);
                          hshAddTypeFlg = (HashMap)arrMandatoryAddrtypeflag.get(i);
                  %>
                     <script>
                        ArrayMandatoryAddress[ArrayMandatoryAddress.length] = "<%=MiUtil.getString((String)hshAddType.get("addstype"))%>";
                        ArrayMandatoryAddressFlag[ArrayMandatoryAddressFlag.length] = "<%=MiUtil.getString((String)hshAddType.get("addrtypeflag"))%>";
                     </script>
                     
                      <b><%=MiUtil.initCap(MiUtil.getString((String)hshAddType.get("addstype")))%></b>);
                      <% if(i > 1 && i != arrMandatoryAddrtype.size()){%>                        
                            ,                         
                      <%}%>              
                  <% }
                    }
                 
                  //Direcciones Unicas
                  String strTypeCustom=null;
                  if (lSiteId == 0){                                       
                     strTypeCustom=Constante.CUSTOMERTYPE_CUSTOMER;// "CUSTOMER";                    
                  }else{    
                     strTypeCustom= Constante.CUSTOMERTYPE_SITE;//"SITE"; 
                  }
                  /*hshData = objSiteService.getUniqueAddresses(strType,strTypeCustom);
                  strMessage=(String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage); 
                  arrUniqueTypeAddr=(ArrayList)hshData.get("arrAddsType");    */

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
          <table width="60%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
             <tr><!-- Inicio fila de tipos de direcciones-->
                <td class="CellLabel" VALIGN="TOP" WIDTH="120"><font class="Required">*</font>Tipo Dirección</td>
                <td class="CellContent" COLSPAN="3">
                   <table width="375" cellpadding="0" cellspacing="0" align="left">                      
                   
                     <% int wn_k=0;
                        int wn_i=0;
                        int iNumAddsAvailable=0;  
                        for (int i=0; i<arrTypeAddr.size();i++){
                            if ((wn_i%3 )==0){
                               if(wn_i>0){ %>                               
                                 </TD>
                              <%}
                              wn_i=0;%>                              
                              <tr>
                        <%  }                       
                          //hshTypeAddr =(HashMap)arrTypeAddr.get(i);                          
                          //strTypeAddr = MiUtil.getString((String)hshTypeAddr.get("valuedesc"));                             
                          strTypeAddr=MiUtil.initCap(MiUtil.getValue(arrTypeAddr,i,"valuedesc"));
                          //Verificar si esta Dirección es unica y si ya existe
                          wn_k = 0;                     
                          //hshUniqueTypeAddr =(HashMap)arrUniqueTypeAddr.get(wn_k);
                          //strUniqueTypeAddr = MiUtil.initCap((String)hshUniqueTypeAddr.get("addrtype"));
                          strUniqueTypeAddr = MiUtil.initCap(MiUtil.getValue(arrUniqueTypeAddr,wn_k,"addrtype"));
                          hshUnqTypeAddrFlg=null;
                          iUnqTypeAddrFlg=0;                                      
                          while ((wn_k<arrUniqueTypeAddr.size()) && (!strTypeAddr.equals(strUniqueTypeAddr))){                             
                           //  try{
                             //hshUniqueTypeAddr =(HashMap)arrUniqueTypeAddr.get(wn_k+1);                            
                             //strUniqueTypeAddr = MiUtil.initCap((String)hshUniqueTypeAddr.get("addrtype"));  
                             strUniqueTypeAddr = MiUtil.initCap(MiUtil.getValue(arrUniqueTypeAddr,wn_k+1,"addrtype"));
                             /*}catch(Exception e){
                             }  */                           
                             wn_k++;
                          }                     
                          
                          if (wn_k < arrUniqueTypeAddr.size() ){ //Dirección única en posicion wn_k
                             hshUnqTypeAddrFlg=(HashMap)arrUniqueTypeAddrFlag.get(wn_k);
                             iUnqTypeAddrFlg=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshUnqTypeAddrFlg.get("addrtypeflag")));                             
                             if (iUnqTypeAddrFlg==1){
                        %>     
                             <td class="CellContent"><input type="checkbox" name="chkAddressDisabled" value="<%=MiUtil.getString(strTypeAddr)%>" disabled> <%=MiUtil.initCap(strTypeAddr)%></td>   
                        <%   }else{
                        %>   
                             <td class="CellContent"><input type="checkbox" name="chkAddressType" value="<%=MiUtil.getString(strTypeAddr)%>"><%=MiUtil.getString(strTypeAddr)%></td>
                        <%   iNumAddsAvailable ++;
                             }
                           }else{
                        %>      
                             <td class="CellContent"><input type="checkbox" name="chkAddressType" value="<%=MiUtil.getString(strTypeAddr)%>" onclick="fxEntrega(<%=i%>);"><%=MiUtil.getString(strTypeAddr)%></td>
                        <%
                            iNumAddsAvailable ++;                       
                           }
                           wn_i++;
                      }
                      if(( 4 - wn_i)>0) { //4 Direcciones por fila
                      //Completar la última fila con celdas en blanco.
                         for (int i=0;i<( 4 - wn_i);i++){
                    %>
                           <td class="CellContent">&nbsp;</td>      
                    <%   }
                      }

                    %>
                   </table>
                </td>
             </tr>
             <tr>
                <td class="CellLabel" valign="top"><font class="Required">*</font>Región&nbsp;</td>
                <td class="CellContent" valign="top" COLSPAN="3">
                 <select name="cmbRegion">                        
                   <%=MiUtil.buildCombo(arrRegion,"swregionid","swname")%>                                      
                 </select>  
                </td>
             </tr>
             <tr>
                <td class="CellLabel" valign="top"><font class="Required">*</font>Dirección&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                    <input type="text" name="txtAddress1" SIZE="50" VALUE="" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                    <input type="text" name="txtAddress2" SIZE="50" VALUE="" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                    <input type="text" name="txtAddress3" SIZE="50" VALUE="" onchange="this.value=trim(this.value.toUpperCase());" MAXLENGTH="40"><BR>
                </td>
             </tr>
             <tr>                
                <td class="CellLabel" valign="top" width="120"><font class="Required">*</font>Departamento&nbsp;</td>
                <td class="CellContent" valign="top" width="300">                
                    <select name="cmbDpto" onChange="fxLoadPlace('1');javascript:document.formdatos.hdnDpto.value=this[this.selectedIndex].text;">                                                           
                      <% hshUbigeoList = objGeneralService.getUbigeoList(0,0,"0"); 
                         strMessage = (String)hshUbigeoList.get("strMessage");
                         if (strMessage!=null)
                           throw new Exception(strMessage);
                           
                         arrLista = (ArrayList)hshUbigeoList.get("arrUbigeoList");
                      %>
                      <%=MiUtil.buildCombo(arrLista,"ubigeo","nombre")%>                    
                   </select>
                   <input type="hidden" name="hdnDpto">
                </td>              
                <td class="CellLabel" VALIGN="TOP" WIDTH="110"><font class="Required">*</font>Provincia&nbsp;</td>
                <td class="CellContent" VALIGN="TOP">
                   <select name="cmbProv" style="width: 85%;" onChange="fxLoadPlace('2');javascript:document.formdatos.hdnProv.value=this[this.selectedIndex].text">                                                                            
                       <option value="0"> </option>
                   </select>  
                   <input type="hidden" name="hdnProv">
                </td>
             </tr>
             <tr>
                <td class="CellLabel" valign="top"><font class="Required">*</font>Distrito&nbsp;</td>
                <td class="CellContent" valign="top">
                   <select name="cmbDist" style="width: 90%;" onChange="fxLoadCodPostal(this.selectedIndex);javascript:document.formdatos.hdnDist.value=this[this.selectedIndex].text">                                                                              
                        <option value="0"> </option>
                   </select> 
                   <input type="hidden" name="hdnDist">
                </td>              
                <td class="CellLabel" valign="top">&nbsp;Cod Postal&nbsp;</td>
                <td class="CellContent" valign="top">
                   <input type="text" name="txtZip" readonly="readonly" ONFOCUS="this.blur();">
                </td>
             </tr>
             <tr>
                <td class="CellLabel" valign="top">&nbsp;País&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                   PERÚ<input type="hidden" name="hdnPais" value="PERU">
                </td>
             </tr>
             <tr>
                <td class="CellLabel" valign="top">&nbsp;Referencia&nbsp;</td>
                <td class="CellContent" valign="top" colspan="3">
                   <textarea name="txtNote" COLS="120" ROWS="5" ONCHANGE="this.value=trim(this.value.toUpperCase());" maxlength="2000"></textarea>
                </td>
             </tr>
             <tr id="trEntrega">
                <td class="CellLabel" valign="top">&nbsp;Dir. de entrega por defecto&nbsp;</td>
                <td class="CellContent">            
                <input type="checkbox" name="chkflagAddress" onClick="fxSet(chkflagAddress, hdnflagAddress);" >
                <input type="hidden" name="hdnflagAddress">
                </td>   
             </tr>
              <tr>
                <td> <input type="hidden" name="hdnccc" value="<%=ccc%>" ></td> 
                <td> <input type="hidden" name="hdncontadorFA" value="<%=contadorFA%>" ></td> 
                 <td> <input type="hidden" name="hdnlObjectId" value="<%=lObjectId%>" ></td> 
             </tr>

          </table>       
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

         <script language="JavaScript">
          // Agregado por CGREMIOS
                        trEntrega.className = "hidden";
          function fxLoadPlace(tipo) {                 
              Form = document.formdatos;      
              var depId= Form.cmbDpto.value; 
              var provId= Form.cmbProv.value; 
              
              if (tipo==1 && depId==""){ //  0: Carga Departamentos 1: Carga Provincia  2: Carga Distrito                                                 
                   deleteOptionIE(document.formdatos.cmbDist); 
                   deleteOptionIE(document.formdatos.cmbProv);             
                   Form.txtZip.value="";
              }else if (tipo==2 && (provId=="" || provId=="0")){
                   deleteOptionIE(document.formdatos.cmbDist); 
                   Form.txtZip.value="";
              }else{                                      
                  deleteOptionIE(document.formdatos.cmbDist); 
                  var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&nTipo="+tipo+"&sCodName=ubigeo&myaction=LoadUbigeo2"; 
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
            
            function fxSubmit(){
                form = document.formdatos;
                if (fxValidate()==false) 
                    return; 
                form.myaction.value="InsertAddress";
				form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
                form.action='<%=strURLOrderServlet%>';          
                form.submit();                             
            }
     
             // Fin Agregado por CGREMIOS               
      // Inicio Agregado por NBRAVO
          function fxSet(chkflagAddress,hdn){
          form = document.formCustomer;   
          if  (chkflagAddress.checked == true){                                                                
                  hdn.value = "1";                        
                  chkflagAddress.checked = true;
               }else{                  
                  hdn.value = "0";
                  chkflagAddress.checked = false;
               }     

         }
              function fxEntrega(wk){
              v_form=document.formdatos;             
              //alert(document.formdatos.chkAddressType[1].checked);
              document.formdatos.chkflagAddress.checked=true

            if (document.formdatos.hdnccc.value==0){  
                if (wk==1 && document.formdatos.chkAddressType[1].checked ==true){  
                  trEntrega.className = "show";
                  }
                if (wk==1 && document.formdatos.chkAddressType[1].checked ==false){    
                  trEntregas.className = "hidden";                
                  }
                document.formdatos.hdnflagAddress.value=1;
                document.formdatos.chkflagAddress.checked=true;
              }
              else{
                if (wk==1 && document.formdatos.chkAddressType[0].checked ==true){ 
                  trEntrega.className = "show";
                  }
                if (wk==1 && document.formdatos.chkAddressType[0].checked ==false){    
                  trEntrega.className = "hidden";
                  }
                  document.formdatos.hdnflagAddress.value=0;
                  document.formdatos.chkflagAddress.checked=false;
              }         

        }
        // Fin agregado por NBRAVO
          
            function fxCheckMandatoryAddress(){
               v_form=document.formdatos;
               for(var j = 0; j < ArrayMandatoryAddress.length ; j++){
                  if(ArrayMandatoryAddressFlag[j]=='0'){
                    cont = 0;
                    for(var i = 0; i < v_form.chkAddressType.length; i++){
                       if((ArrayMandatoryAddress[j] == v_form.chkAddressType[i].value) && (v_form.chkAddressType[i].checked)){
                          cont++;
                       }
                    }
                    if(cont==0){
                       alert("No se ha creado el tipo de dirección obligatoria "+ArrayMandatoryAddress[j]);
                       return false;
                    }
                  }
               }
               return true;
            }
            function fxValidate(){
               var num;
               var exp="";
               var bool_exp;
               v_form=document.formdatos;
               if (document.formdatos.chkflagAddress.checked ==true && v_form.hdncontadorFA.value>0){               
                alert("Ya existe una dirección de entrega por defecto");               
                return false;
               }
               
               if (v_form.flg_enabled.value == 0) return false;
               // Validar Tipo de direcciones
               //numero de direcciones permitidas a seleccionar
               num = v_form.numAddsAvailable.value;
               if (num>1){
                  for (i = 0; i < num; i++) {
                     exp = exp + " !v_form.chkAddressType["+i+"].checked ";
                     if (i<num-1){
                        exp = exp + "&&";
                     }
                  }
               }else{
                  exp = "!v_form.chkAddressType.checked ";
               }
               bool_exp = eval(exp);
               if (bool_exp){
                   alert("Seleccione por lo menos un tipo de dirección.");
                   return false;
               }
               if (v_form.cmbRegion.selectedIndex==0){
                   alert("Seleccione la Región.");
                   v_form.cmbRegion.focus();
                   return false;
               }
               if (v_form.txtAddress1.value.length==0 && v_form.txtAddress2.value.length==0 && v_form.txtAddress3.value.length==0){
                   alert("Ingrese su dirección.");
                   v_form.txtAddress1.focus();
                   return false;
               }               
               if (v_form.cmbDpto.selectedIndex==0){
                   alert("Seleccione el Departamento.");
                   v_form.cmbDpto.focus();
                   return false;
               }               
               if (v_form.cmbProv.selectedIndex==0){
                   alert("Seleccione la Provincia.");
                   v_form.cmbProv.focus();
                   return false;
               }
               if (v_form.cmbDist.selectedIndex==0){
                   alert("Seleccione el Distrito.");
                   v_form.cmbDist.focus();
                   return false;
               }
               if( !(fxCheckMandatoryAddress()) ){
                  return false;
               }
               v_form.flg_enabled.value = 0;
               return true;
            }
         </script>
         <input type="hidden" name="numAddsAvailable" value="<%=iNumAddsAvailable%>">
         </form>
</html>
<%}catch(Exception ex){
  System.out.println("Address New try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>