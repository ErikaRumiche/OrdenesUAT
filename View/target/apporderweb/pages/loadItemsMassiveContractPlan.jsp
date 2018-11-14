<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*, pe.com.nextel.bean.ContractServiceBscsBean"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.*, pe.com.nextel.bean.ServiciosBean"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>

<%
      ArrayList arrayListBySol = new ArrayList();
      ArrayList arrayList =  (ArrayList)request.getAttribute("setAttService");
      ArrayList arrayListBySolution =  (ArrayList)request.getAttribute("setAttServiceBySol");
      
      
      for (int i= 0; i<arrayListBySolution.size(); i++){
          HashMap hServiceBySol = (HashMap)arrayListBySolution.get(i);
          System.out.println("xxxxxxxxxxxxxxx ===> "+hServiceBySol);
            
          arrayListBySol = (ArrayList)hServiceBySol.get("objArrayList");
           
          System.out.println("arrayListBySol ====> "+arrayListBySol.size());
            
      }     
     
           
%>

<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script DEFER>
  var list_services;
  var vServiceBySolution  = new Vector();
  var strServiceFinal = "";
  var count = 0; 
  
  vServiceBySolution.removeElementAll();
  
  <%if (arrayListBySol!= null && arrayListBySol.size() > 0){
       System.out.println("[arrayListBySol.size]======"+arrayListBySol.size());
       ServiciosBean serviceBySolution = null;
       for( int i = 1; i < arrayListBySol.size(); i++ ){
          serviceBySolution = (ServiciosBean)arrayListBySol.get(i);
          //System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviceBySolution.getNpnomserv());
          /*System.out.println("[ServiceAditiional][getNpservicioid]"+serviceBySolution.getNpservicioid());
          System.out.println("[ServiceAditiional][getNpexcludingind]"+serviceBySolution.getNpexcludingind());
          System.out.println("[ServiceAditiional][getNpnomcorserv]"+serviceBySolution.getNpnomcorserv());*/
       %>                    
          vServiceBySolution.addElement(new ServiceItem("<%=serviceBySolution.getNpservicioid()%>","<%=serviceBySolution.getNpnomserv()%>","<%=serviceBySolution.getNpnomcorserv()%>","<%=MiUtil.getString(serviceBySolution.getNpexcludingind())%>"));
     <%}
    }%>
  
  function ServiceItem(id, name ,nameShort ,exclude) {        
     this.id = id;
     this.name = name;
     this.nameDisplay = (exclude=="")?name:name+" - "+exclude;
     this.nameShort = nameShort;
     this.nameShortDisplay = (exclude=="")?nameShort:nameShort+" - "+exclude;
     this.exclude = exclude;
     this.active_new = "N";
     this.modify_new = "N";        
  }
  
  function fxValidateServices(listServices){
     var arrServices;
     var nLength = vServiceBySolution.size();
     var strValue = "";
     
     arrServices    = listServices.split("|");
     
     for (n=0; n < arrServices.length; n++){    
       for(m=0; m < nLength ; m++){         
          objvServiceBySolution = vServiceBySolution.elementAt(m);         
          if(objvServiceBySolution.id == arrServices[n]){
            strValue = strValue + "|"+arrServices[n]+"|"+"S"+"|"+"S";
          }
        }
     }
     
     return strValue.substring(1);
    
  }
<%
  if (arrayList.size()>0) {
    Iterator iterator = arrayList.iterator();
    while(iterator.hasNext()) {
	     ContractServiceBscsBean objContractServiceBscsBean = (ContractServiceBscsBean)iterator.next();
       System.out.println(objContractServiceBscsBean.getHiddenId()+" <==> "+objContractServiceBscsBean.getServiceCode());
%>       
        list_services = "<%=objContractServiceBscsBean.getServiceCode()%>";
        
        strServiceFinal = fxValidateServices(list_services);
        //parent.mainFrame.document.frmdatos.hdnItemServices[<%=objContractServiceBscsBean.getHiddenId()%>].value = "<%=objContractServiceBscsBean.getServiceCode()%>";
        //parent.mainFrame.document.frmdatos.hdnItemServicesNew[<%=objContractServiceBscsBean.getHiddenId()%>].value = "<%=objContractServiceBscsBean.getServiceCode()%>";
        parent.mainFrame.document.frmdatos.hdnItemServices[<%=objContractServiceBscsBean.getHiddenId()%>].value = strServiceFinal;
        parent.mainFrame.document.frmdatos.hdnItemServicesNew[<%=objContractServiceBscsBean.getHiddenId()%>].value = strServiceFinal;
        parent.mainFrame.document.frmdatos.hdnGetServicio[<%=objContractServiceBscsBean.getHiddenId()%>].value = "true"
        
        if ("<%=objContractServiceBscsBean.getStrTypeError()%>" == 0 && "<%=objContractServiceBscsBean.getStrMessage()%>" != ""){        
           parent.mainFrame.document.frmdatos.txtItemMsjResSSAA[<%=objContractServiceBscsBean.getHiddenId()%>].value = "<%=objContractServiceBscsBean.getStrMessage()%>";
           parent.mainFrame.document.frmdatos.txtItemMsjResSSAA[<%=objContractServiceBscsBean.getHiddenId()%>].style.color = "red";
           parent.mainFrame.document.frmdatos.chkPhoneNumber[<%=objContractServiceBscsBean.getHiddenId()%>].checked = false;
           parent.mainFrame.document.frmdatos.chkPhoneNumber[<%=objContractServiceBscsBean.getHiddenId()%>].disabled = true;
           parent.mainFrame.document.frmdatos.hdnchkPhone[<%=objContractServiceBscsBean.getHiddenId()%>].value = "off";
           parent.mainFrame.document.frmdatos.txtNewPlan[<%=objContractServiceBscsBean.getHiddenId()%>].value = '';
           parent.mainFrame.document.frmdatos.hdnItemNewPlanId[<%=objContractServiceBscsBean.getHiddenId()%>].value = '0';
        
           
        }
        if ("<%=objContractServiceBscsBean.getStrTypeError()%>" == 1){
           parent.mainFrame.document.frmdatos.txtItemMsjResSSAA[<%=objContractServiceBscsBean.getHiddenId()%>].value = "ERROR al obtener los datos del Equipo";
           parent.mainFrame.document.frmdatos.txtItemMsjResSSAA[<%=objContractServiceBscsBean.getHiddenId()%>].style.color = "red";
           parent.mainFrame.document.frmdatos.chkPhoneNumber[<%=objContractServiceBscsBean.getHiddenId()%>].checked = false;
           parent.mainFrame.document.frmdatos.chkPhoneNumber[<%=objContractServiceBscsBean.getHiddenId()%>].disabled = true;
           parent.mainFrame.document.frmdatos.hdnchkPhone[<%=objContractServiceBscsBean.getHiddenId()%>].value = "off";
           parent.mainFrame.document.frmdatos.txtNewPlan[<%=objContractServiceBscsBean.getHiddenId()%>].value = '';
           parent.mainFrame.document.frmdatos.hdnItemNewPlanId[<%=objContractServiceBscsBean.getHiddenId()%>].value = '0';
        }
       
       
        if (parent.mainFrame.document.frmdatos.hdnItemNewPlanId[<%=objContractServiceBscsBean.getHiddenId()%>].value != 0){                   
             count++;           
             
            } 
        
<% }  %> 
    
      parent.mainFrame.document.frmdatos.txtNumRecord.value = '  '+count;
    
   
<% } else {%>          
          //alert("No se Pudo Obtener los Servicios");
<%}%>
   location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
</script>      
 