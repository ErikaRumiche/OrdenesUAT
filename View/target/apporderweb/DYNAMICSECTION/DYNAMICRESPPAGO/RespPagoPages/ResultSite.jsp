<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>

<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.BillingAccountService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@page import="pe.com.nextel.bean.SiteBean" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="pe.com.nextel.service.CustomerService" %>

    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>

<%  
   String strMessage=(request.getParameter("sMensaje")==null?"":request.getParameter("sMensaje"));
   String strParametros=(request.getParameter("sParametro")==null?"":request.getParameter("sParametro"));  
   int iOrigen=(request.getParameter("nOrigen")==null?0:Integer.parseInt(request.getParameter("nOrigen"))); //0:Insercion 1: Edicion  
   String strPage=(request.getParameter("sPage")==null?"Site":request.getParameter("sPage"));  


   strParametros=strParametros.replace('¿','?');  
   strParametros=strParametros.replace('|','&'); 
   
   //INICIO CEM - COR354
   System.out.println(":::::::::::::");
   System.out.println("Inicio -  ResultSite.jsp");
   System.out.println(":::::::::::::");
   
   String strCustomerId=(request.getParameter("pCustomerId")==null?"":request.getParameter("pCustomerId"));
   String strOrderId=(request.getParameter("pOrderId")==null?"":request.getParameter("pOrderId"));   
   String strSiteId=(request.getParameter("pSiteId")==null?"":request.getParameter("pSiteId"));   
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"":request.getParameter("pSpecificationId"));  
   String strFlag=(request.getParameter("nFlag")==null?"":request.getParameter("nFlag"));  

   if (strCustomerId.equals("")) strCustomerId=MiUtil.getParameterCadenaURL(strParametros,"nCustomerId");	
   if (strOrderId.equals("")) strOrderId=MiUtil.getParameterCadenaURL(strParametros,"nOrderId");
   if (strSiteId.equals("")) strSiteId=MiUtil.getParameterCadenaURL(strParametros,"nSiteId");
   if (strSpecificationId.equals("")) strSpecificationId=MiUtil.getParameterCadenaURL(strParametros,"pSpecificationId");
   if (strFlag.equals("")) strFlag=MiUtil.getParameterCadenaURL(strParametros,"nFlag");
   System.out.println("*******strFlag******: "+strFlag);
	
   
   String strSubSiteUrl=request.getContextPath()+ "/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/SiteGeneralNew.jsp"+strParametros;   
   
   String strSubDirUrl=request.getContextPath()+ "/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/AddressList.jsp"+strParametros;
   String strSubContUrl=request.getContextPath()+ "/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/ContactList.jsp"+strParametros;
   String strSubBillAccUrl=request.getContextPath()+ "/DYNAMICSECTION/DYNAMICRESPPAGO/RespPagoPages/BillingAccountList.jsp"+strParametros;
   String strURLButom=Constante.PATH_APPORDER_SERVER+ "/Bottom.html";
   //StringBuffer strOutHTML=new StringBuffer();    
   
   System.out.println(" -------------------- INICIO ResultSite.java  ---------------------- ");
   System.out.println("strParametros-->"+strParametros);   
   System.out.println("strMessage-->"+strMessage);         
   System.out.println("iOrigen-->"+iOrigen);      
   System.out.println("strPage-->"+strPage);      
   System.out.println("strOrderId-->"+strOrderId);
   System.out.println("nSiteId-> "+strSiteId);   
   System.out.println("nCustomerId-> "+strCustomerId);	
   System.out.println("strSpecificationId-> "+strSpecificationId);	
   System.out.println("strFlag-> "+strFlag);	
   System.out.println(" -------------------- FIN ResultSite.java ---------------------- ");    

   if ("null".equals(strMessage))
      strMessage=null;
   else if (strMessage.startsWith("dbmessage"))
      strMessage=strMessage.substring(9,strMessage.length());  
   
   long lSpecificationId=MiUtil.parseLong(strSpecificationId);
   
   System.out.println(" ----------- INICIO RESULTSITE.jsp---------------- ");   
   System.out.println("strMessage-->"+strMessage);         
   System.out.println(" ------------  FIN RESULTSITE.jsp----------------- ");    
   out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
   out.println("<script language=\"javascript\"> ");
  
   if (strPage.equals("Site")){
      if (strMessage!=null){ // El proceso tuvo errores
         if (iOrigen==0){ //insercion     
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==1){
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==2){       
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" parent.bottomFrame.location.replace('"+strURLButom+"'); ");  
         }
      }else{
         if (iOrigen==0){ //insercion
            out.println("alert('Se guardó correctamente Datos Generales para Site Nuevo '); ");
            SiteBean objSiteB=(SiteBean)request.getAttribute("objSite");
            long siteId=objSiteB.getSwSiteId();
            String strSiteName=MiUtil.getString(objSiteB.getSwSiteName());
            String strRegName=MiUtil.getString(objSiteB.getSwRegionName());
            String strStatus=MiUtil.getString(objSiteB.getNpStatus());
            String strCreatBy=MiUtil.getString(objSiteB.getSwCreatedBy());
            out.println("parent.opener.fxAddSite('"+siteId+"','"+strSiteName+"','"+strRegName+"','"+strStatus+"','"+strCreatBy+"');");
            out.println("parent.mainFrame.location.replace('"+strSubSiteUrl+"');");
            out.println("alert('Agregue direcciones y contactos');");
            out.println("location.replace('"+strURLButom+"');");             
         }else if (iOrigen==1){ // edicion
            out.println("alert('El site se actualizó correctamente '); ");
            SiteBean objSiteB=(SiteBean)request.getAttribute("objSite");
            long siteId=objSiteB.getSwSiteId();
            String strSiteName=MiUtil.getString(objSiteB.getSwSiteName());
            String strRegName=MiUtil.getString(objSiteB.getSwRegionName());
            String strStatus=MiUtil.getString(objSiteB.getNpStatus());
            String strCreatBy=MiUtil.getString(objSiteB.getSwCreatedBy());                         
            out.println("parent.opener.fxUpdSite('"+siteId+"','"+strSiteName+"','"+strRegName+"','"+strStatus+"','"+strCreatBy+"');");
            out.println("parent.mainFrame.location.replace('"+strSubSiteUrl+"');"); 
         }else if (iOrigen==2){ // borrado
            int iInd=(request.getParameter("nInd")==null?0:Integer.parseInt(request.getParameter("nInd")));            
            int iCount=(request.getParameter("nCount")==null?0:Integer.parseInt(request.getParameter("nCount")));            
            System.out.println("iInd-->"+iInd+" iCount"+iCount);        
            out.println("var table = parent.mainFrame.document.all ? parent.mainFrame.document.all['tabSite']:parent.mainFrame.document.getElementById('tabSite');");                   
            out.println("table.deleteRow("+iInd+"); ");
            out.println("parent.mainFrame.fxRenumeric('tabSite',"+(iCount-1)+");");
            out.println("parent.mainFrame.fxDicreseContador();");
            
            out.println("location.replace('"+strURLButom+"');"); 
         }      
      }
   }else if(strPage.equals("Dir")){
      if (strMessage!=null){ // Errores
         if (iOrigen==0){ //insercion     
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==1){ // edicion
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==2){   //borrado    
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }
      }else{
         if (iOrigen==0){ //insercion
            out.println("alert('La dirección se grabó correctamente.'); ");
            out.println("parent.mainFrame.location.replace('"+strSubDirUrl+"');");
         }else if (iOrigen==1){ // edicion
            out.println("alert('Se actualizaron las direcciones correctamente.'); ");
            out.println("parent.mainFrame.location.replace('"+strSubDirUrl+"');"); 
         }else if (iOrigen==2){ //borrado
            out.println("alert('La dirección se eliminó correctamente.');"); 
            out.println("parent.mainFrame.location.replace('"+strSubDirUrl+"');");             
         }        
      }
   }else if(strPage.equals("Contact")){
      if (strMessage!=null){
         if (iOrigen==0){ //insercion     
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==1){ // edicion
            out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==2){   //borrado    
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }
      }else{
         if (iOrigen==0){ //insercion
            out.println("alert('El contacto se grabó correctamente.'); ");
            out.println("parent.mainFrame.location.replace('"+strSubContUrl+"');");
         }else if (iOrigen==1){ // edicion
            out.println("alert('Se actualizaron los contactos correctamente.'); ");
            out.println("parent.mainFrame.location.replace('"+strSubContUrl+"');"); 
         }else if (iOrigen==2){ //borrado
            out.println("alert('El contacto se eliminó correctamente.');"); 
            out.println("parent.mainFrame.location.replace('"+strSubContUrl+"');");             
         }        
      }
   }else if(strPage.equals("BillAcc")){
      if (strMessage!=null){
         if (iOrigen==0){ //insercion     
            //out.println("parent.mainFrame.formdatos.flg_enabled.value = 1;"); 
				out.println("parent.mainFrame.fxHideElement('tableMsgProcesando');");
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==1){ // edicion
            //out.println("parent.mainFrame.formCustomer.flg_enabled.value = 1;"); 
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println(" location.replace('"+strURLButom+"'); ");  
         }else if (iOrigen==2){   //borrado    
            out.println(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                               
            out.println("location.replace('"+strURLButom+"'); ");  
         }
      }else{
         if (iOrigen==0){ //insercion
            out.println("alert('La cuenta se grabó correctamente.'); ");            
            if (strFlag.equals("1")){               
               out.println("parent.close();");
            }
            else{
               out.println("parent.mainFrame.location.replace('"+strSubBillAccUrl+"');");
            }
         }else if (iOrigen==1){ // edicion
            out.println("alert('Se actualizó la cuenta correctamente.'); ");
            if (strFlag.equals("1")){               
               out.println("parent.close();");
            }
            else{               
               out.println("parent.mainFrame.location.replace('"+strSubBillAccUrl+"');");
            }				
            //out.println("parent.mainFrame.location.replace('"+strSubBillAccUrl+"');");				            
         }else if (iOrigen==2){ //borrado
            out.println("alert('La cuenta se eliminó correctamente.');"); 
				System.out.println("La cuenta se eliminó correctamente");
				System.out.println("strFlag: "+strFlag);
            if (strFlag.equals("0")){
               out.println("parent.mainFrame.location.replace('"+strSubBillAccUrl+"');");
            }
				else{
				   out.println("location.replace('"+strURLButom+"'); ");
				}
         }        
      }
   }
%>


<%
	//INICIO CEM - COR0354
	
	System.out.println("strSpecificationId --> "+lSpecificationId+"");
	System.out.println("strMessage --> "+strMessage);
	
	if (strMessage==null){
  
		if(lSpecificationId==Constante.SPEC_CAMBIAR_ESTRUCT_CUENTA){ //Inicio validacion para especificacion de Cambio de Estructura de Cuenta			
			System.out.println(" -------  Inicio SPEC_CAMBIAR_ESTRUCT_CUENTA  ----------- ");
			HashMap objHashMap = new HashMap();
			ArrayList objArraySiteExistsList    = new ArrayList();
			ArrayList objArraySiteSolicitedList = new ArrayList();
			ArrayList objArrayBillingAccountSolicitedList = new ArrayList();
			ArrayList objArrayBillingAccountExitstList = new ArrayList();
      String    strCustomerStruct = "";
      
      /**Obtengo los Sites Exitentes en BSCS**/
      objHashMap = (new NewOrderService()).SiteDAOgetSiteExistsList(MiUtil.parseLong(strCustomerId),Constante.CUSTOMERTYPE_CUSTOMER);
      if( objHashMap.get("strMessage") != null )
          throw new Exception((String)objHashMap.get("strMessage"));
      objArraySiteExistsList = (ArrayList)objHashMap.get("objArrayList");
      if( objArraySiteExistsList!=null ) strCustomerStruct = objArraySiteExistsList.size()==0?"FLAT":"LARGE"; 
      
      /**Obtengo los Sites Solicitados**/
      objHashMap = (new NewOrderService()).SiteDAOgetSiteSolicitedList(MiUtil.parseLong(strOrderId));
      if( objHashMap.get("strMessage") != null )
          throw new Exception((String)objHashMap.get("strMessage"));
      objArraySiteSolicitedList = (ArrayList)objHashMap.get("objArrayList");
      
      /**Sección de Cuentas de Facturación**/
      long lCustomerIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(strCustomerId),Constante.CUSTOMERTYPE_CUSTOMER);		 
      System.out.println("Result site lCustomerIdBSCS: "+lCustomerIdBSCS+"");
      
      if( MiUtil.getString(strCustomerStruct).equals("FLAT") ){
      //Obtener los billing account existentes del customer
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lCustomerIdBSCS);
      if( objHashMap.get("strMessage") != null )
          throw new Exception((String)objHashMap.get("strMessage"));
      
      objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
      
      //Obtener los billing account solicitados del customer
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong("0"),MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strOrderId));
      if( objHashMap.get("strMessage") != null )
          throw new Exception((String)objHashMap.get("strMessage"));
      
      objArrayBillingAccountSolicitedList = (ArrayList)objHashMap.get("objArrayList");
      }
      
      System.out.println("Cantidad de Existentes  : " + objArrayBillingAccountExitstList.size());
      System.out.println("Cantidad de Solicitados : " + objArrayBillingAccountSolicitedList.size());
      
      /**Si se registrado, actualizado o eliminado una cuenta de facturación**/
      if (strPage.equals("BillAcc") ){
      
        out.println("vctBilAcc = new Vector();");
        out.println("vctBilAcc.removeElementAll();");
      
      if( objArrayBillingAccountSolicitedList != null ){
         BillingAccountBean objBillingAccountBean =  null;
          for(int i=0; i<objArrayBillingAccountSolicitedList.size(); i++ ) {
              objBillingAccountBean = new BillingAccountBean();
              objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountSolicitedList.get(i);
         out.println("vctBilAcc.addElement(new fxMakeBilling('"+objBillingAccountBean.getNpBillaccountNewId()+"','"+objBillingAccountBean.getNpBillacCName()+"','Solicitado'));");
          }
      }
      
      if( objArrayBillingAccountExitstList != null ){
         BillingAccountBean objBillingAccountBean =  null;
          for(int i=0; i<objArrayBillingAccountExitstList.size(); i++ ) {
              objBillingAccountBean = new BillingAccountBean();
              objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountExitstList.get(i);
         out.println("vctBilAcc.addElement(new fxMakeBilling('"+objBillingAccountBean.getNpBillaccountNewId()+"','"+objBillingAccountBean.getNpBillacCName()+"','Existente'));");
          }
      }
  
      out.println("try{ ");
      out.println("parent.mainFrame.fxTransferBAVector(vctBilAcc);");
      //out.println("parent.mainFrame.fxLoadSiteData();");
      out.println("}catch(e){ ");
      out.println("parent.opener.parent.mainFrame.fxTransferBAVector(vctBilAcc);");
      //out.println("parent.opener.parent.mainFrame.fxLoadSiteData();");
      out.println("}");
      
      }
      
      /**Si se registrado, actualizado o eliminado un responsable de pago**/
      else if( strPage.equals("Site")){
        out.println("vctSites = new Vector();");
        if( iOrigen==2 ) //borrado
          out.println("doc = parent.mainFrame");
        else
          out.println("doc = parent.opener.parent.mainFrame");
        
        out.println("vctSites.removeElementAll();");
      
      if( objArraySiteExistsList != null ){
        SiteBean objSiteBean =  null;
        for(int i=0; i<objArraySiteExistsList.size(); i++ ) {  
            objSiteBean = new SiteBean();
            objSiteBean = (SiteBean)objArraySiteExistsList.get(i);
        out.println("vctSites.addElement(new fxMakeSite('"+objSiteBean.getSwSiteId()+"','"+objSiteBean.getSwSiteName()+"','"+objSiteBean.getSwRegionName()+"','"+objSiteBean.getNpStatus()+"'));");
        }
      }
 
      if( objArraySiteSolicitedList != null ){
        SiteBean objSiteBean =  null;
        for(int i=0; i<objArraySiteSolicitedList.size(); i++ ) {  
            objSiteBean = new SiteBean();
            objSiteBean = (SiteBean)objArraySiteSolicitedList.get(i);
        out.println("vctSites.addElement(new fxMakeSite('"+(objSiteBean.getSwSiteId()*-1)+"','"+objSiteBean.getSwSiteName()+"','"+objSiteBean.getSwRegionName()+"','"+objSiteBean.getNpStatus()+"'));");
        }
      }
      
      out.println("doc.fxTransferNewRespPagoVector(vctSites);");
      out.println("doc.fxReloadedSite();");
      
      }
		  
       System.out.println(" -------  ENTRAMOSSSSSSSSSSSSSSSSS  ----------- ");
		   System.out.println(" -------  Fin SPEC_CAMBIAR_ESTRUCT_CUENTA  ----------- ");
		}//Fin validacion para especificacion de Cambio de Estructura de Cuenta
		else{
			System.out.println(" No es Cambio de Estructura de Cuenta");
			System.out.println("strSpecificationId --> "+strSpecificationId);		
		}
	}
	out.println("</script>");      
   //out.print(strOutHTML.toString()); 
	
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ResultSite</title>
  </head>
  <body></body>
</html>