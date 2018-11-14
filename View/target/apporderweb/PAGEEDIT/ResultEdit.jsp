<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
   Object obj=request.getAttribute("objResultado");
   HashMap hshResult=null;
   if (obj!=null)  hshResult=(HashMap)request.getAttribute("objResultado");
   else hshResult=new HashMap();
   String  strErrorBPEL=(String)request.getAttribute("strErrorBPEL");   
	System.out.println("strErrorBPEL: "+strErrorBPEL);
   String strOrderId=MiUtil.getString((String)hshResult.get("strOrderId"));    
   int iSaveOption=MiUtil.parseInt((String)hshResult.get("strSaveOption")); 
   String strMessage=(String)hshResult.get("strMessage");    
   String strMensajeAsig=(String)hshResult.get("strMesgAsig");    
   //int iOrderPermEdit=MiUtil.parseInt((String)hshResult.get("strOrderPermEdit")); 
   String strCurrentStatus=MiUtil.getString((String)hshResult.get("strCurrentStatus"));  
   String strInboxId=MiUtil.getString((String)hshResult.get("strInboxId")); 
   String strTipoOrigen=MiUtil.getString((String)hshResult.get("strTipoOrigen")); 
   int iErrorNumer=MiUtil.parseInt((String)hshResult.get("strErrorNumer")); 
   String strNextInboxName=(String)hshResult.get("strNextInboxName"); 
   String strTipoOrden= MiUtil.getString((String)hshResult.get("strTipoOrden"));    
   String strAddItemPayOrder=MiUtil.getString((String)hshResult.get("strAddItemPayOrder")); // CEM - COR0720
   String strCodOrden = MiUtil.getString((String)hshResult.get("lngOrdenId"));
   String strSuccessMessage=(String)hshResult.get("strSuccessMessage");    
   String strPIType= MiUtil.getString((String)hshResult.get("strPIType")); 
   String strPhoneNumberList=(String)request.getAttribute("phoneNumberList");
   int lengthNumberPhone=MiUtil.parseInt((String)request.getAttribute("lengthNumberPhone"));
   int flagGeneration=MiUtil.getInt(hshResult.get("flagGeneration"));
   String strMessageGeneration=(String)hshResult.get("messageGeneration");
   String strMessageValidation=(String)hshResult.get("messageValidation");
   String strNextInboxNamePA=(String)hshResult.get("strNextInboxNamePA");   //JCURI
   String strMessageOrderProrrateo=(String)hshResult.get("messageOrderProrrateo");//PRY-0890 JCURI
   String strMessageConcat="";//PRY-0890 JCURI
   
   String strUrl="";    
   String strRutaContext= request.getContextPath();    
   StringBuffer strOutHTML=new StringBuffer();
   
  /* if ("null".equals(strMessage))
      strMessage=null;
   else if (strMessage.startsWith("dbmessage"))
      strMessage=strMessage.substring(9,strMessage.length());
   
   if ("null".equals(strMensajeAsig))
      strMensajeAsig="";         
*/
   System.out.println(" ----------- INICIO RESULTEDIT.jsp---------------- ");
   System.out.println("iSaveOption-->"+iSaveOption); 
   System.out.println("nOrderId-->"+strOrderId);
	 System.out.println("strCodOrden-->"+strCodOrden);
   //System.out.println("iOrderPermEdit-->"+iOrderPermEdit);      
   System.out.println("strCurrentStatus-->"+strCurrentStatus);   
   System.out.println("strInboxId-->"+strInboxId);   
   System.out.println("strRutaContext-->"+strRutaContext);   
   System.out.println("sTipoOrigen-->"+strTipoOrigen);      
   System.out.println("strMensajeAsig-->"+strMensajeAsig);
   System.out.println("strMessage-->"+strMessage);
   System.out.println("strErrorNumer-->"+iErrorNumer);   
   System.out.println("strNextInboxName-->"+strNextInboxName); 
   System.out.println("strNextInboxNamePA-->"+strNextInboxNamePA);   
   System.out.println("strSuccessMessage-->"+strSuccessMessage);
   System.out.println("strPIType-->"+strPIType);
   System.out.println("strPhoneNumberList-->"+strPhoneNumberList);
   System.out.println("intLengthNumberPhone-->"+lengthNumberPhone);
   System.out.println("flagGeneration-->"+flagGeneration);
   System.out.println("strMessageGeneration-->"+strMessageGeneration);
   System.out.println("strMessageOrderProrrateo-->"+strMessageOrderProrrateo);
   System.out.println(" ------------  FIN RESULTEDIT.jsp----------------- ");       
   
   if (strMensajeAsig==null)
      strMensajeAsig=""; 
   
   if (strMessage!=null  && strMessage.startsWith("dbmessage"))
      strMessage=strMessage.substring(9,strMessage.length());
      
   if (strNextInboxName!=null)
      strNextInboxName = " Estado " + strNextInboxName;
   else
      strNextInboxName = "";
   
   if (strNextInboxNamePA!=null)
	  strNextInboxName =  strNextInboxNamePA;	   
   
   strOutHTML.append("<script type=\"text/javascript\"> \n ");
   if ("UpdOrden".equals(strTipoOrigen)){
         
      if (strMessage!=null && iErrorNumer < 0){     
         if ("ERRORTIMESTAMP".equals(strMessage)){
           strOutHTML.append(" alert('No puede grabar los cambios. La orden fue modificada por otro usuario mientras Ud. la tenia en edición'); \n ");       
           //strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";
           //strOutHTML.append("location.replace('"+strUrl+"');");
			  strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
         }else{
              if(flagGeneration==Constante.FLAG_SECTION_ACTIVE){
              if("GENERATION_ERROR".equals(strMessage)){
                  strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessageGeneration) + "'); \n ");
              }}else {
                  strOutHTML.append(" alert('no se completó la transacción : " + MiUtil.getMessageClean(strMessage) + "'); \n ");
              }
              strOutHTML.append("form = parent.mainFrame.document.frmdatos;");
              strOutHTML.append("form.btnUpdOrder.disabled = false;");
              strOutHTML.append("form.btnUpdOrderInbox.disabled = false;");
					if (strErrorBPEL !=null){
						if (strErrorBPEL.equals("1")){ 
							//fallo por un error de BPEL - debe refrescar lo que ya esta con commit.
							strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";								
							strOutHTML.append("location.replace('"+strUrl+"');");
						}	
						else{ 
							strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
						}	
					}
					else{
						strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
					}
         }
         //strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
      }else{
         if(flagGeneration==Constante.FLAG_SECTION_ACTIVE) {
             strOutHTML.append("alert('" + strMessageGeneration+ "'); \n ");
         }
        
        //PRY-0890 JCURI								  
			strMessageConcat = "Orden Nro. " + strOrderId + " se grabó correctamente" + strMensajeAsig + ". " + strNextInboxName;
     	if(strMessageOrderProrrateo!=null && !strMessageOrderProrrateo.equals("")) {
     		strMessageConcat = strMessageConcat.concat("\\n"+strMessageOrderProrrateo);
         }
      	strOutHTML.append("alert('" + strMessageConcat + "'); \n ");
         
         
         if (strMessage!=null && !"".equals(strMessage) && iErrorNumer >= 0){
            strOutHTML.append("alert('"+ MiUtil.getMessageClean(strMessage) + "'); \n ");           
         }
         if (iSaveOption==1){ //Solo Grabar          
            strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";
            strOutHTML.append("location.replace('"+strUrl+"');");	
      
         }else{ //Grabar e ir al inbox        
            strUrl="/portal/page/portal/nextel/INBOX_LIST?InboxID="+strInboxId+"&InboxName="+strCurrentStatus;            
            strOutHTML.append("parent.mainFrame.location.replace('"+strUrl+"');");          
            strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
         }        
      } 
   }
   else if("UpdOrdenDetail".equals(strTipoOrigen)){ //Actualiza Orden desde el detalle
   
      if (strMessage!=null){ // con mensaje 
         strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
         strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
      }
      else{
         strOutHTML.append("alert('Orden Nro. "+ strOrderId + " se actualizó correctamente.'); \n "); 
         strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
         strOutHTML.append("location.replace('"+strUrl+"');");         
    }   
    
    
    
   }else if("Repair".equals(strTipoOrigen)){ //Repair

          if (strMessage!=null){ // con mensaje
              if("VALIDATE_GENERATION".equals(strMessage)){
              strMessage=strMessageValidation;
              }
            strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
            //strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
            strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strCodOrden+"&av_execframe=BOTTOMFRAME";
            strOutHTML.append("location.replace('"+strUrl+"');");
            }else{
            strOutHTML.append(" alert('se creó la orden "+strTipoOrden +":"+ strOrderId + "'); \n ");
            strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strCodOrden+"&av_execframe=BOTTOMFRAME";
            strOutHTML.append("location.replace('"+strUrl+"');");
      }
   
   
   /*-Agregado Ini TM 16/11/2009--*/
   
     }else if("PortAlta".equals(strTipoOrigen)){ 
   
      if (strMessage!=null){ 
         strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
         strOutHTML.append("parent.mainFrame.history.go();");
         strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
         
      }
      else{
         strOutHTML.append(" alert('se creó la orden "+strTipoOrden +":"+ strOrderId + "'); \n ");
         strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
         /*strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strCodOrden+"&av_execframe=BOTTOMFRAME";   			
         strOutHTML.append("location.replace('"+strUrl+"');");*/
      }                      
      
   /*-Agregado Fin TM 16/11/2009--*/
   
   /*-Agregado Ini DLAZO 17/11/2009--*/
   
     }else if("PortValidNumber".equals(strTipoOrigen)){
      boolean validateNumbers = true;
      if(strPhoneNumberList != null && !strPhoneNumberList.equals("")){
          String[] phoneNumberList = strPhoneNumberList.split("\\|");
          for(int i = 0; i < phoneNumberList.length ; i++) {
              if (phoneNumberList[i].length() != lengthNumberPhone) {
                  validateNumbers = false;
                  break;
              } else {
                  for (int j = i + 1; j < phoneNumberList.length; j++) {
                      if (phoneNumberList[i].equals(phoneNumberList[j])) {
                          validateNumbers = false;
                      }
                  }
              }
          }
      }
   
      if (strMessage != null){
         strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");               
         long numberFila = MiUtil.parseLong((String)request.getAttribute("numFilas"));
         int index = MiUtil.parseInt((String)request.getAttribute("index"));
         if(numberFila == 2){
            strOutHTML.append("parent.mainFrame.document.frmdatos.txtphonenumber.value =''; ");
            strOutHTML.append("parent.mainFrame.document.frmdatos.txtphonenumber.focus(); ");
            strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
          }else if(numberFila > 2){
            strOutHTML.append("parent.mainFrame.document.frmdatos.txtphonenumber["+(index-1)+"].value =''; ");
            strOutHTML.append("parent.mainFrame.document.frmdatos.txtphonenumber["+(index-1)+"].focus(); ");
            strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
          }
          //Ini PM0010311 - OTrillo 24/09/2015
          strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrder.disabled ='true'; ");
          strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrderInbox.disabled ='true'; ");
          //Fin PM0010311 - OTrillo 24/09/2015
      }
      else{
         strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
          if(validateNumbers) {
          //Ini PM0010311 - OTrillo 24/09/2015
          strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrder.removeAttribute('disabled'); ");
          strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrderInbox.removeAttribute('disabled'); ");
          //Fin PM0010311 - OTrillo 24/09/2015
          }else{
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrder.disabled ='true'; ");
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnUpdOrderInbox.disabled ='true'; ");
          }
      }          
      
   /*-Agregado Fin DLAZO 17/11/2009--*/
   /*-Modificado MVALLE 17/11/2010-- */
   /*-Modificado MVALLE 03/12/2010-- */
   }else if("ParteIngreso".equals(strTipoOrigen)){      
      System.out.println("iErrorNumer:"+iErrorNumer);      
      if (iErrorNumer < 0){
        strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");
        //Inicio - Habilitar nuevamente botón para creación de PARTE DE INGRESO            
        if (strPIType.equalsIgnoreCase("DEV")){
          strOutHTML.append("parent.mainFrame.document.frmdatos.btnParteIngreso.disabled=false;");
        }
        else{
          if (strPIType.equals("BAD")){
            strOutHTML.append("parent.mainFrame.document.frmdatos.btnParteIngresoBadImei.disabled=false;");
          }
          else{
            if (strPIType.equals("BADADDRESS")){
            strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=false;");
            strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=true;"); //AÑADIDO MVALLE
            }
          }
        }
        //Fin - Habilitar nuevamente botón para creación de PARTE DE INGRESO        
      }else{//Exito
         if (strMessage!=null) // con mensaje 
            if(iErrorNumer == 1){
                strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
                      if (strPIType.equalsIgnoreCase("DEV")){
                        strOutHTML.append("parent.mainFrame.document.frmdatos.btnParteIngreso.disabled=false;");
                      }
                      else{
                        if (strPIType.equals("BAD")){
                          strOutHTML.append("parent.mainFrame.document.frmdatos.btnParteIngresoBadImei.disabled=false;");
                        }
                        else{
                          if (strPIType.equals("BADADDRESS")){
                            if(strMessage.equals(Constante.MSG_GENERATED_PI)){
                              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=true;");  //AÑADIDO EXTERNO.MVALLE
                              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=false;"); //AÑADIDO EXTERNO.MVALLE                  
                            }else{
                              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=false;");  //AÑADIDO EXTERNO.MVALLE
                              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=true;"); //AÑADIDO EXTERNO.MVALLE                  
                            }
                          }
                        }
                      }
              }
              //INI JNINO 09-02-2011
              else{
                if(iErrorNumer == 2){
            strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
        strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
        strOutHTML.append("location.replace('"+strUrl+"');");
                     strOutHTML.append("parent.mainFrame.document.frmdatos.btnParteIngreso.disabled=false;");              
                }     
              }
              //FIN JNINO 09-02-2011
      }
   }else if("autorizacionDevolucion".equals(strTipoOrigen)){
       if (strMessage == null || strMessage.equals("") ){ //Exito
         strOutHTML.append(" alert('" + MiUtil.getMessageClean(strSuccessMessage) + "'); \n ");
         strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
         strOutHTML.append("location.replace('"+strUrl+"');");         
      }else{
         strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");                
      }      
      
   }else if("GenerateDocument".equals(strTipoOrigen)){
       if (iErrorNumer < 0){
         strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");                
         strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarDocumentos.disabled=false;");
      }else{//Exito
         if (strMessage!=null) {// con mensaje 
            if (iErrorNumer == 2){
                  strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
                  strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
                  strOutHTML.append("location.replace('"+strUrl+"');");
            }else{
                strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
                strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarDocumentos.disabled=false;");
            }
        }
      }
     } else if("GenerateDocumentDetail".equals(strTipoOrigen)){
       if (iErrorNumer < 0){
         strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");                
      }else{//Exito
         if (strMessage!=null) {// con mensaje 
            if (iErrorNumer == 2){
                  strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
                  strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
                  strOutHTML.append("location.replace('"+strUrl+"');");
            }else{
                strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
            }
        }        
    }   
      
   } else if("replaceHandset".equals(strTipoOrigen)){
   
      if (strMessage!=null) {// con mensaje 
           strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
           strUrl="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";   
           strOutHTML.append("location.replace('"+strUrl+"');");
      }
   
  } else if("statusNumber".equals(strTipoOrigen)){
      
      if (strMessage!=null) {// con mensaje 
        strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
        strOutHTML.append("form = parent.mainFrame.document.frmdatos;");
        strOutHTML.append("form.item_imei_numTel.value = '';");
        strOutHTML.append("form.item_imei_numTel.focus();");
        strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
      }
  /* MODIFICADO EXTERNO.MVALLE 03/12/2010 */
  } else if("guiaRemision".equals(strTipoOrigen)){
      if (iErrorNumer < 0){
        strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");                
        strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=false;");
        strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=true;"); //AÑADIDO EXTERNO.MVALLE
      }else{//Exito
         if (strMessage!=null) {// con mensaje 
            if (iErrorNumer == 2){
              strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=true;"); //AÑADIDO EXTERNO.MVALLE
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=false;"); //AÑADIDO EXTERNO.MVALLE                   
            }else{
              strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarGuia.disabled=false;");
              strOutHTML.append("parent.mainFrame.document.frmdatos.btnGenerarPI.disabled=true;");//AÑADIDO EXTERNO.MVALLE
            }
        }
      } 
  } else{ //Otros
      if (iErrorNumer < 0){ //strMessage!=null   Existe un error
         strOutHTML.append(" alert('se produjo errores durante el proceso : " + MiUtil.getMessageClean(strMessage) + "'); \n ");                
      }else{ //exito
         if (strMessage!=null) // con mensaje 
            strOutHTML.append(" alert('" + MiUtil.getMessageClean(strMessage) + "'); \n ");                
			// Inicio CEM - COR0720			
			//En el SP_GENERATE_PAYMENT_ORDER si se ejecuta correctamente iErrorNumer =1
			/*
			if (strAddItemPayOrder.equals("1")){ // Tiene documentos de pago
				strOutHTML.append("form = parent.mainFrame.document.frmdatos;     ");          
				strOutHTML.append("form.hdnFlagAgregarItem.value = 'N';");				
			}
			*/
			// Fin CEM - COR0720			
         /*else
            strOutHTML.append(" alert('se creó la orden "+strTipoOrden +":"+ strOrderId + "'); \n ");    */                     
      }
      strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
   }      
                        
   strOutHTML.append("</script>");   
   out.print(strOutHTML.toString());   
        
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>    
    <title>PrintPageEdit</title>
  </head>
  <body> 
  </body>
</html>


<%     
}catch(Exception  ex){  
   ex.printStackTrace();
   System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>