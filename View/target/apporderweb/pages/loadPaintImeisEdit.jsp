<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="pe.com.nextel.bean.ItemBean"%>
<%@page import="pe.com.nextel.bean.ProductBean"%>
<%@page import="pe.com.nextel.bean.ProductLineBean"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>
<html>
  <head>
<script>
 var cont = 1;
<%
  try{
  HashMap hshItemDeviceMap  = (HashMap)request.getAttribute("hshItemDeviceMap");
  HashMap hshKitMap         = new HashMap();
  NewOrderService   objNewOrderService = new NewOrderService();
  
  String strModality        =   MiUtil.getString(request.getParameter("strModality"));
  String strProductName     =   MiUtil.getString(request.getParameter("strProductName"));
  String strPlanName        =   MiUtil.getString(request.getParameter("strPlanName"));
  String strProductId       =   MiUtil.getString((String)request.getParameter("strProductId"));
  String strProductLineId   =   MiUtil.getString((String)request.getParameter("intProductLineId"));
  int    intGroupId         =   MiUtil.parseInt((String)request.getParameter("strGroupId"));
  int    intQuantity        =   MiUtil.parseInt((String)request.getParameter("strQuantity"));
  int    intIndMax          =   MiUtil.parseInt((String)request.getParameter("strIndMax"));
  int    intIndMin          =   MiUtil.parseInt((String)request.getParameter("strIndMin"));
  int    intDiference       =   MiUtil.parseInt((String)request.getParameter("strDiference"));
  int    intQuantityCurrent =   MiUtil.parseInt((String)request.getParameter("intQuantityCurrent"));
  String strFlgProductGN  	 =   (request.getParameter("strFlgProducstGN")==null?"":request.getParameter("strFlgProductGN"));  
  String strPhoneNumber   =   MiUtil.getString(request.getParameter("strPhoneNumber"));//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
  
  System.out.println("======Carga de Items_Device [loadPaintImeis.jsp]======");
  System.out.println("[loadPaintImeis.jsp]strModality       : "+strModality);
  System.out.println("[loadPaintImeis.jsp]strPlanName       : "+strPlanName);
  System.out.println("[loadPaintImeis.jsp]strProductLineId  : "+strProductLineId);
  System.out.println("[loadPaintImeis.jsp]strProductId      : "+strProductId);
  System.out.println("[loadPaintImeis.jsp]intGroupId        : "+intGroupId);
  System.out.println("[loadPaintImeis.jsp]intQuantity       : "+intQuantity);
  System.out.println("[loadPaintImeis.jsp]strFlgProductGN   : "+strFlgProductGN);
  
  if( intIndMin <= 0 ) intIndMin = 1;
  
  System.out.println("strProductLineId:"+strProductLineId);
  System.out.println("strFlgProductGN:"+strFlgProductGN);
  //Si el HashMap tiene data
  if( hshItemDeviceMap != null ){
  System.out.println("[loadPaintImeis.jsp][Se comprueba si el producto es un KIT]");
    //Si no hay errores
    System.out.println("[loadPaintImeis.jsp][Se comprueba si el producto es un KIT]["+hshItemDeviceMap.get("strMessage")+"]");
    if( hshItemDeviceMap.get("strMessage")==null || MiUtil.getString((String)hshItemDeviceMap.get("strMessage")).equals("PRODUCT.SPI_GET_KIT_DET : ORA-01403: no data found") ){
        hshKitMap  = (HashMap)hshItemDeviceMap.get("hshKitMap");
    
        //Si el Producto es un Kit
        if( hshKitMap.size() > 0 ){
        System.out.println("[loadPaintImeis.jsp][El producto es un KIT]");
        
           ArrayList arrProductDetailList = (ArrayList) hshItemDeviceMap.get("arrProductDetailList");
           Iterator iterator = arrProductDetailList.iterator();
           int      cont = 1;
           
         %>
            //Elimina los IMEIS pertenecientes al Kit- Ok
            parent.mainFrame.fxDeleteImeiTable(<%=intGroupId%>);
            indMin  = <%=intIndMin%>; 
         <%
            while (iterator.hasNext()) {
              HashMap hshProductMap = (HashMap) iterator.next();
              
              String prodLineId = MiUtil.defaultString(MiUtil.defaultBigDecimal(hshProductMap.get("productLineId"),null),null);
              
              HashMap objHshProdLineDet =  objNewOrderService.getProductLineDetail(MiUtil.parseLong(prodLineId));
              
              if( objHshProdLineDet!= null )
                if( objHshProdLineDet.get("strMessage")!= null )
                  throw new Exception((String)objHshProdLineDet.get("strMessage"));
              
              ProductLineBean objProdLineBean = (ProductLineBean)objHshProdLineDet.get("objProductLineBean");
                if( objProdLineBean.getNptype() == 20 ){
         %>
            for(j=0; j < <%=intQuantity%>; j++){
              var vctItemsImei       = new parent.mainFrame.Vector();
              <%
              //System.out.println("Product Id : " + MiUtil.defaultString(MiUtil.defaultBigDecimal(hshProductMap.get("productId"),null),null));
              strProductId   = MiUtil.defaultString(MiUtil.defaultBigDecimal(hshProductMap.get("productId"),null),null);
              strProductName = (String)hshProductMap.get("productName");
              System.out.println("[loadPaintImeis.jsp][KIT][ID]["+strProductId+"]["+strProductName+"]");
              %>
                //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
                vctItemsImei.addElement(new parent.mainFrame.fxMakeItemImei(<%=intGroupId%>,cont,<%=intGroupId%> + "-" + cont ,"","","","<%=strProductName%>","<%=strProductId%>",<%=strPlanName%>,"","N","<%=strModality%>","<%=strFlgProductGN%>","<%=strPhoneNumber%>") );
                parent.mainFrame.fxAddRowTableImeis("table_imeis",vctItemsImei.elementAt(0),9,indMin);
                parent.mainFrame.vctItemsMainImei.addElement(vctItemsImei);
                cont++;
                indMin++;
            }
         <%   
                }//Fin del If - ProductLine
            }
            //Fin del While
            %>
           <%
         }//Fin del Kit
         else{
            System.out.println("[loadPaintImeis.jsp][El producto no es un KIT]");
            HashMap objHshProdLineDet =  objNewOrderService.getProductLineDetail(MiUtil.parseLong(strProductLineId));
              System.out.println("objHshProdLineDet.get(strMessage):"+objHshProdLineDet.get("strMessage"));
              if( objHshProdLineDet!= null )
                if( objHshProdLineDet.get("strMessage")!= null )
                  throw new Exception((String)objHshProdLineDet.get("strMessage"));
              
              ProductLineBean objProdLineBean = (ProductLineBean)objHshProdLineDet.get("objProductLineBean");
              ProductBean objProductBean = new ProductBean();
              HashMap    objHashMapProductDetail = new HashMap();
              System.out.println("objProdLineBean.getNptype() :"+objProdLineBean.getNptype() );
                if( objProdLineBean.getNptype() == 20 ){
                
                objHashMapProductDetail = objNewOrderService.getProductDetail(MiUtil.parseLong(strProductId));
                if( objHashMapProductDetail.get("strMessage")!=null ) throw new Exception((String)objHashMapProductDetail.get("strMessage"));
                
                objProductBean  = (ProductBean)objHashMapProductDetail.get("objProductBean");
                strProductName = MiUtil.getString(objProductBean.getNpproductname());
                 System.out.println("[loadPaintImeis.jsp][NO KIT][ID]["+strProductId+"]["+strProductName+"]");
         %>
            var indMax = <%=intIndMax%>;//cont = <%=(intQuantityCurrent+1)%>;
            //alert("Eliminando a los del Grupo : " + <%=intGroupId%>);
            //Elimina los IMEIS - Ok
            parent.mainFrame.fxDeleteImeiTable(<%=intGroupId%>);
            indMin  = <%=intIndMin%>; // - <%=intQuantityCurrent%> + 1;
            for(j=0; j < <%=intQuantity%>; j++){
              var vctItemsImei       = new parent.mainFrame.Vector();
                //Se agregó el parametro strPhoneNumber - RHUACANI-ASISTP - 24/10/2010
                vctItemsImei.addElement(new parent.mainFrame.fxMakeItemImei(<%=intGroupId%>,cont,<%=intGroupId%> + "-" + cont ,"","","","<%=strProductName%>","<%=strProductId%>",<%=strPlanName%>,"","N","<%=strModality%>","<%=strFlgProductGN%>","<%=strPhoneNumber%>") );
                //alert("Indice a insertar : " + indMin);
                parent.mainFrame.fxAddRowTableImeis("table_imeis",vctItemsImei.elementAt(0),9,indMin);
                parent.mainFrame.vctItemsMainImei.addElement(vctItemsImei);
                cont++;
                indMin++;
            }
            <%
         }
        }
      }else{
        System.out.println("Exception : Problemas al mostrar ITEM_DEVICE_EDIT -> " + (String)hshItemDeviceMap.get("strMessage") );
      %>
        alert('<%=MiUtil.getMessageClean((String)hshItemDeviceMap.get("strMessage"))%>');
      <%
        throw new Exception((String)hshItemDeviceMap.get("strMessage"));
      }
  }
%>
/*Si no hay errores de JavaScript se habilita el botón de Grabar*/
  try{
    //Si estamos en la página de Creación de Órden
    parent.mainFrame.frmdatos.btnSaveOrder.disabled = false;
  }catch(ex){
    //Si falló es porque estamos en la página de Actualización de la órden
    try{
      parent.mainFrame.frmdatos.btnUpdOrder.disabled = false;
    }catch(exc){}
    
    try{
      parent.mainFrame.frmdatos.btnUpdOrderInbox.disabled = false;
    }catch(exp){}
  }
  
<%}catch(Exception exception){
  System.out.println("Exception : Error en ejecución -> " + exception.getClass() + " - " + exception.getMessage() );%>
    alert('No hay respuesta del servidor. Debe abortar la operación. El item ingresado presenta errores. Causa del Error : <%=MiUtil.getMessageClean(exception.getMessage())%>');
    /*Si en caso se encuentren errores en la página*/
    try{
      //Si estamos en la página de Creación de Órden
      parent.mainFrame.frmdatos.btnSaveOrder.disabled = true;
    }catch(ex){
      //Si falló es porque estamos en la página de Actualización de la órden
      try{
        parent.mainFrame.frmdatos.btnUpdOrder.disabled = true;
      }catch(exc){}
      
      try{
        parent.mainFrame.frmdatos.btnUpdOrderInbox.disabled = true;
      }catch(exp){}
    }
<%}%>

</script>
</head>
  <body bgcolor="#AABBCC">
  </body>
</html>