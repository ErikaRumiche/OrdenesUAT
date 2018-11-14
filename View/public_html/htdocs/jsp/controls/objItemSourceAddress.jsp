<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.AddressObjectBean" %>
<%
  Hashtable hshtinputNewSection = null;
  CustomerService objCustomerService = new CustomerService();
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  String strItemId   =  MiUtil.getString((String)request.getAttribute("strItemId"));
  String    strCodigoCliente = "";
  String    strMessage       = "";
  String    strOrderId       = "";
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  System.out.println("[objItemSourceAddress.jsp]nameHtmlItem: "+nameHtmlItem);
  System.out.println("[objItemSourceAddress.jsp]strCodigoCliente: "+strCodigoCliente);
  if ( nameHtmlItem==null ) nameHtmlItem = "";
    
  HashMap objHashMap = objCustomerService.getSourceAddress(MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strItemId));
  System.out.println("Error: "+ objHashMap.get("strMessage"));
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  
  ArrayList objArrayList = (ArrayList)objHashMap.get("objAddressObjectlist"); 
  System.out.println("[objItemSourceAddress.jsp]objArrayList.size(): "+objArrayList.size());
  AddressObjectBean objAddressObjectBean = null;
  
%>
   <select name="<%=nameHtmlItem%>" onChange="javascript:fxLoadNextAddress(this.value);"> 
      <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <%if ( objArrayList != null && objArrayList.size() > 0 ){
			  //System.out.println("[objItemSourceAddress.jsp]objArrayList.size(): "+objArrayList.size());		 
              for( int i=0; i<objArrayList.size();i++ ){
                 objAddressObjectBean = new AddressObjectBean();
                 objAddressObjectBean = (AddressObjectBean)objArrayList.get(i);
         %>
      <option value='<%=objAddressObjectBean.getAddressId()%>'> 
                     <%=objAddressObjectBean.getSwaddress1()%>
      </option>
         <%   }
           }
          %>                        
   </select>

<%
   if( !type_window.equals("NEW") ){
      System.out.println("[objItemSourceAddress.jsp]***********************: ");
      System.out.println("[objItemSourceAddress.jsp]Inicio diferente de NEW: ");
	  System.out.println("[objItemSourceAddress.jsp]***********************: ");
      String[] paramNpobjitemvalue      = request.getParameterValues("b");
      String[] paramNpobjitemheaderid   = request.getParameterValues("a");
      String sItemSourceAddress="";
	  String sItemDestinyAddress="";
      for(int i=0;i<paramNpobjitemheaderid.length; i++){  
	     //Dirección Origen
	     if (("59").equals(paramNpobjitemheaderid[i])){
		    sItemSourceAddress   = paramNpobjitemvalue[i]==null?"0":paramNpobjitemvalue[i];
			System.out.println("[objItemSourceAddress.jsp]sItemSourceAddress: "+sItemSourceAddress);
		 }
		 //Dirección Destino
	     if (("60").equals(paramNpobjitemheaderid[i])){
		    sItemDestinyAddress   = paramNpobjitemvalue[i]==null?"0":paramNpobjitemvalue[i];
			System.out.println("[objItemSourceAddress.jsp]sItemDestinyAddress: "+sItemDestinyAddress);
		 }	 
	  }  //fin del FOR 
	  /*quitar cuando funcione*/
	  /*if (sItemSourceAddress.equals("")){
	     sItemSourceAddress="1094";
		 System.out.println("[objItemSourceAddress.jsp]No llego el valor coloco solo para pruebas: "+sItemSourceAddress);
	  }*/
	  
%>
      <script>
         function fxLoadItemDestinyAddress(){
		    formCurrent = parent.mainFrame.frmdatos;
		    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemDestinyAddress);
<%
			ArrayList objArrayListAux = new ArrayList();
			HashMap objHashMapAux = objCustomerService.getDestinyAddress(MiUtil.parseLong(sItemSourceAddress));
            objArrayListAux = (ArrayList)objHashMapAux.get("objAddressObjectlist");
            System.out.println("objArrayListAux.size() : " + objArrayListAux.size());			
			AddressObjectBean objAddressObjectBeanAux = null;
            if ( objArrayListAux != null && objArrayListAux.size() > 0 ){			
               for( int i=0; i<objArrayListAux.size();i++ ){
                  objAddressObjectBeanAux = new AddressObjectBean();
                  objAddressObjectBeanAux = (AddressObjectBean)objArrayListAux.get(i);
				  System.out.println("["+i+"]: " + objAddressObjectBeanAux.getAddressId()+"/"+objAddressObjectBeanAux.getSwaddress1());
%>
			      parent.mainFrame.AddNewOption(formCurrent.cmb_ItemDestinyAddress,'<%=objAddressObjectBeanAux.getAddressId()%>','<%=MiUtil.getMessageClean(objAddressObjectBeanAux.getSwaddress1())%>');
<%
			   }
%>			   
			   formCurrent.cmb_ItemSourceAddress.value='<%=sItemSourceAddress%>';
<%			   
			}
%>
         } //fin fxLoadItemDestinyAddress()
		 		 
      </script>	
<%
   } //fin del if !=NEW
%>

<script>
   function fxLoadNextAddress(objValue){
      var form = parent.mainFrame.frmdatos;
         if( objValue != "" ){
            var url = "<%=request.getContextPath()%>/itemServlet?strAddressId="+objValue+"&hdnMethod=getNextAddressList";
            parent.bottomFrame.location.replace(url);
         }
   }
</script>