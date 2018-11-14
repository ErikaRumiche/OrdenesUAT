<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.PrintWriter"%>
<%  
   ArrayList arrUbigeo=(ArrayList)request.getAttribute("listUbigeo");
   if (arrUbigeo==null)
      arrUbigeo=new ArrayList();
   
   String strMensaje=(request.getParameter("sMensaje")==null?"":request.getParameter("sMensaje"));   
   int iTipo=(request.getParameter("nTipo")==null?-1:Integer.parseInt(request.getParameter("nTipo")));
   int iIndCombo=(request.getParameter("nInd")==null?-1:Integer.parseInt(request.getParameter("nInd")));// en caso de q exista un arreglo de combos prov, dist
   String strCodigo=(request.getParameter("sCodName")==null?"nombre":request.getParameter("sCodName"));   
   String strFormName=(request.getParameter("sFormName")==null?"formdatos":request.getParameter("sFormName"));   
   
   String iDptoId=(request.getParameter("iDptoId")==null?"-1":request.getParameter("iDptoId"));   
   String iProvId=(request.getParameter("iProvId")==null?"-1":request.getParameter("iProvId"));   
   String iDistId=(request.getParameter("iDistId")==null?"-1":request.getParameter("iDistId"));   
   
   
   StringBuffer strOutHTML=new StringBuffer();
   String strComboName="";
   
   if ("null".equals(strMensaje))
   strMensaje="";   
   
   System.out.println(" -------------------- INICIO PRINTUBIGEO.jsp ---------------------- ");      
   
   System.out.println("strCodigo->"+strCodigo+" iIndCombo->"+iIndCombo);
   System.out.println(" -------------------- FIN PRINTUBIGEO.jsp ---------------------- ");
   strOutHTML.append("<script type=\"text/javascript\">  ");
   strOutHTML.append("form = parent.mainFrame.document."+strFormName+";");
   
   if (iIndCombo==-1){ // si No es un arreglo de combos 
      if (iTipo==1){ // 1: Carga Provincia  
         strComboName="form.cmbProv";
      }else if (iTipo==2){ //2: Carga Distrito 
         strComboName="form.cmbDist";
      }
   }else{
      if (iTipo==1){ // 1: Carga Provincia  
         strComboName="form.cmbProv["+iIndCombo+"]";
      }else if (iTipo==2){//2: Carga Distrito 
         strComboName="form.cmbDist["+iIndCombo+"]";
      }
   }
      
   if (iTipo==1 ){
      for( int i=1; i<=arrUbigeo.size();i++ ){ 
         HashMap h = (HashMap)arrUbigeo.get(i-1);
         strOutHTML.append("opcion=new Option('" + MiUtil.getString((String)h.get("nombre"))+ "','" +  MiUtil.getString((String)h.get(strCodigo)) + "');");      
         strOutHTML.append(strComboName+".options[" + i + "]=opcion;");         
      }
      strOutHTML.append(strComboName+".value="+iProvId+";");
   }else{   
      for( int i=1; i<=arrUbigeo.size();i++ ){ 
         HashMap h = (HashMap)arrUbigeo.get(i-1);
         strOutHTML.append("opcion=new Option('" + MiUtil.getString((String)h.get("nombre"))+ "','" +  MiUtil.getString((String)h.get(strCodigo)) + "');");      
         strOutHTML.append(strComboName+".options[" + i + "]=opcion;");
         strOutHTML.append("parent.mainFrame.codPostal["+(i-1)+"]= '"+MiUtil.getString((String)h.get("cpostid"))+"';");         
      }
      strOutHTML.append(strComboName+".value="+iDistId+";");
   }
   
   strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
   strOutHTML.append("</script>");  
   out.print(strOutHTML.toString());   
        
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>PrintUbigeo</title>
  </head>
  <body></body>
</html>