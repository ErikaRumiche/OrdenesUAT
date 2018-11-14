<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.PrintWriter"%>
<%  

   Object obj=request.getAttribute("objResultado");
   HashMap hshResult=null;
   if (obj!=null)  hshResult=(HashMap)request.getAttribute("objResultado");
   else hshResult=new HashMap();      
   
   String strComboName=(String)hshResult.get("strComboName");    
   String strFormName=(String)hshResult.get("strFormName"); 
   String strValorName=(String)hshResult.get("strValorName"); 
   String strDescripcionName=(String)hshResult.get("strDescripcionName"); 
   ArrayList objLista=(ArrayList)hshResult.get("objLista");    
   String strMessage=(String)hshResult.get("strMessage");    
   String strRutaContext= request.getContextPath();    
   StringBuffer strOutHTML=new StringBuffer();   
   HashMap h=null;
   
   if (objLista==null) objLista=new ArrayList();   
  
   System.out.println(" -------------------- INICIO PRINTCOMBO.jsp ---------------------- ");         
   System.out.println("strMessage->"+strMessage);
   System.out.println("PRINTCOMBO objLista->"+objLista);
   System.out.println(" -------------------- FIN PRINTCOMBO.jsp ---------------------- ");
   
   strOutHTML.append("<script type=\"text/javascript\">  ");
   strOutHTML.append("var form = parent.mainFrame.document."+strFormName+";");
   strComboName = "form."+strComboName;
   
   
 /*  if (iIndCombo==-1){ // si No es un arreglo de combos 
      if (iTipo==1) // 1: Carga Provincia  
         strComboName="form.cmbProv";
      else if (iTipo==2) //2: Carga Distrito 
         strComboName="form.cmbDist";
   }else{
      if (iTipo==1) // 1: Carga Provincia  
         strComboName="form.cmbProv["+iIndCombo+"]";
      else if (iTipo==2)//2: Carga Distrito 
         strComboName="form.cmbDist["+iIndCombo+"]";
   }*/

   for( int i=1; i<=objLista.size();i++ ){ 
      h = (HashMap)objLista.get(i-1);
      strOutHTML.append("opcion=new Option('" + MiUtil.getString((String)h.get(strValorName))+ "','" +  MiUtil.getString((String)h.get(strDescripcionName)) + "');");      
      strOutHTML.append(strComboName+".options[" + i + "]=opcion;");         
   }
   
   strOutHTML.append("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');"); 
   strOutHTML.append("</script>");  
   //System.out.println("CADENA GENERADA-->"+strOutHTML.toString());
   out.print(strOutHTML.toString());   
        
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>PrintCombos</title>
  </head>
  <body></body>
</html>