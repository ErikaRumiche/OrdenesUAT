<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
  
  int iAction=(request.getParameter("nAction")==null?-1:Integer.parseInt(request.getParameter("nAction")));
  String strMsgError=(request.getParameter("sMsgError")==null?"":request.getParameter("sMsgError"));
  String strMensaje=(request.getParameter("sMensaje")==null?"":request.getParameter("sMensaje"));
   
  String strSellerRegionId=(request.getParameter("nSellerRegionId")==null?"":request.getParameter("nSellerRegionId"));  
  String strDealer=(request.getParameter("sDealer")==null?"":request.getParameter("sDealer"));
  int iOption=(request.getParameter("nAction")==null?-1:Integer.parseInt(request.getParameter("nAction")));;
  String strOutHTML="";
  System.out.println("strSellerRegionId:"+ strSellerRegionId);
  System.out.println("strDealer:"+ strDealer);
  if (iAction==1){
     strOutHTML= "<script language=\"javascript\">  \n" + 
                    "form = parent.mainFrame.document.frmdatos;" +
                    "form.cmbVendedor.value  = form.hdnVendedor.value;"+
                    "form.hdnVendedor.value  = form.hdnVendedor.value;"+
                    " alert('"+strMsgError+"'); \n " + 
                  "</script>";   

 }else if(iAction==2){
      out.println("<script language=\"javascript\">" );            
      out.println("if (parent.mainFrame.document.frmdatos.hdnSellerRegionId!=null)  parent.mainFrame.document.frmdatos.hdnSellerRegionId.value = '" + strSellerRegionId + "';");      
      out.println("if (parent.mainFrame.document.frmdatos.txtDealer!=null)  parent.mainFrame.document.frmdatos.txtDealer.value = '" + strDealer + "';");          
      out.println("</script" );
                   
 }else if(iAction==4){
      strOutHTML= "<script language=\"javascript\">  \n" + 
                    "form = parent.mainFrame.document.frmdatos;" +
                    "form.cmbVendedor.value  = form.hdnVendedor.value;"+
                    "form.hdnVendedor.value  = form.hdnVendedor.value;"+
                    "form.txtDealer.value    = form.hdnDealer.value;"+
                    " alert('"+strMsgError+"'); \n " + 
                  "</script>";  
 }else if (iAction==3){
     strOutHTML= "<script language=\"javascript\">  \n" + 
                   " try { " +
                    " Form = parent.mainFrame.document.frmdatos; " +
                    " Form.hdnSellerRegionId.value = " + strSellerRegionId + ";" +
                    " }catch(e){} " +
                   " form = parent.mainFrame.document.frmdatos; " + 
                   " form.cmbVendedor.value  = form.hdnVendedor.value; " +
                   " form.hdnVendedor.value  = form.hdnVendedor.value; " +
                   " alert('"+strMsgError+"'); " +
                 "</script>";                       
       if (iOption==1){
          strOutHTML= "<script language=\"javascript\">  \n" + 
                          " form = parent.mainFrame.document.frmdatos; " +
                          " form.txtDealer.value  = "+ strDealer +"; " +
                          " objVendedor = form.cmbVendedor; " +                        
                          " form.hdnVendedor.value = objVendedor.value; " +
                       "</script>";     
                         
     
      }else if (iOption==2){
          strOutHTML= "<script language=\"javascript\">  \n" + 
                        " alert('El vendedor no puede ser cambiado ya que tiene una oportunidad activa relacionado a otro consultor:'"+strMensaje+"');"+ 
                          " form = parent.mainFrame.document.frmdatos;" +
                          " form.cmbVendedor.value  = form.hdnVendedor.value;" +                        
                       "</script>";                        
      }else if (iOption==3){
          strOutHTML= "<script language=\"javascript\">  \n" + 
                        " alert(' Error:'"+strMensaje+"'); " +
                       "</script>";                                           
      }               
 }
 
 
  out.print(strOutHTML);        
  out.print("location.replace('websales/Bottom.html');");
  //out.close();                 

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ValidateSalesMan</title>
  </head>
  <body></body>
</html>