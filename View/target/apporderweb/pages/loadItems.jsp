<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.List"%>
<%@page import="pe.com.nextel.bean.ItemBean"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="java.util.HashMap" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script type="text/javascript">
    
      var form = parent.mainFrame.document.frmdatos;
      var registros = parent.mainFrame.items_table.rows.length -1;
      
      //Luego de la evaluacion se desabilita el botón
      parent.mainFrame.document.getElementById("btnEvaluarVO").disabled = true;
          
    <%  
      HashMap objHashMap = (HashMap)request.getAttribute("objHashMapVO");
      List itemBeanList = (List)objHashMap.get("objArrayList");
      String type_window   = (String)request.getAttribute("type_window");
      ItemBean itemBean = null;
      
      //Validar si aplico el volumen de orden
      if(objHashMap.get("strMessage") == null){
    %>
        parent.mainFrame.aplicoVO = "1";
        try{ 
          parent.mainFrame.fxShowColumnsVO(true);
        }catch(e){
          //alert('fxShowColumnsVO ' + e);
        }
3
    <%
		
        for(int i = 0; i < itemBeanList.size(); i++){
        
          itemBean = (ItemBean)itemBeanList.get(i);
                    
          if(itemBean.getNpAplicarVO() != null){          
    %>
            parent.mainFrame.document.getElementById('txtItemPriceCtaInscripVO'+'<%=itemBean.getNpIndice()%>').value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
            parent.mainFrame.document.getElementById('hdnRentVO'+'<%=itemBean.getNpIndice()%>').value = parseFloat(round_decimals('<%=itemBean.getNprent()%>' ,2));
            parent.mainFrame.document.getElementById('hdnPromoIdVO'+'<%=itemBean.getNpIndice()%>').value = '<%=itemBean.getNppromotionid()%>';
    <%
          }
          
          if(itemBean.getNpAplicarVO() == null){
    %>
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').disabled = true;
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').checked = false;
            parent.mainFrame.document.getElementById('txtItemPriceCtaInscripVO'+'<%=itemBean.getNpIndice()%>').value = "";
                        
            <%
            if(Constante.PAGE_ORDER_EDIT.equals(type_window)){
            %>
              if(registros == 1){
                if(form.txtItemPriceCtaInscrip.value == undefined){
                  form.txtItemPriceCtaInscrip[0].value = round_decimals('<%=itemBean.getNpprice()%>' ,2); ;
                }
                else{
                  form.txtItemPriceCtaInscrip.value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
                }
                form.hdnItemValuetxtItemPriceCtaInscrip.value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
                form.txtItemRentEquipment.value = round_decimals('<%=itemBean.getNprent()%>' ,2);
                form.hdnItemValuetxtItemRentEquipment.value = round_decimals('<%=itemBean.getNprent()%>' ,2);
                form.hdnItemValuetxtItemPromotioId.value = '<%=itemBean.getNppromotionid()%>';
              }
              else if(registros > 1){
                form.txtItemPriceCtaInscrip[<%=i%>].value = round_decimals('<%=itemBean.getNpprice()%>' ,2); ;              
                form.hdnItemValuetxtItemPriceCtaInscrip[<%=i%>].value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
                form.txtItemRentEquipment[<%=i%>].value = round_decimals('<%=itemBean.getNprent()%>' ,2);
                form.hdnItemValuetxtItemRentEquipment[<%=i%>].value = round_decimals('<%=itemBean.getNprent()%>' ,2);
                form.hdnItemValuetxtItemPromotioId[<%=i%>].value = '<%=itemBean.getNppromotionid()%>';
              }
    <%
            }
          }
          else if(itemBean.getNpAplicarVO().equals("1")){
    %>
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').disabled = false;
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').checked = true;
    <%
          }
          else if(itemBean.getNpAplicarVO().equals("0")){
    %>
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').disabled = false;
            parent.mainFrame.document.getElementById('chkAplicarVO'+'<%=itemBean.getNpIndice()%>').checked = false;
    <%
          }
        }
    %>        
        
        
        try{ 
          parent.mainFrame.fxCalculateTotalItems();
        }catch(e){
          //alert('fxCalculateTotalVO ' + e);
        }
        try{ 
          parent.mainFrame.fxCalculateTotal();
        }catch(e){
          //alert('fxCalculateTotalVO ' + e);
        }
        try{
          parent.mainFrame.fxCalculateTotalVO();
        }catch(e){
          //alert('fxCalculateTotalVO ' + e);
        }
    <%
      }
      else{
    %>
        parent.mainFrame.aplicoVO = "0";
        try{ 
          parent.mainFrame.fxShowColumnsVO(false);
        }catch(e){
          //alert('fxShowColumnsVO ' + e);
        }
        alert("La orden de venta no aplica a la promoción por volumen de orden");
        
        <%
        if(Constante.PAGE_ORDER_EDIT.equals(type_window)){
          
          for(int i = 0; i < itemBeanList.size(); i++){
        
            itemBean = (ItemBean)itemBeanList.get(i);
        
        %>
            if(registros == 1){
              form.hdnChangedOrder.value = "S";
              if(form.txtItemPriceCtaInscrip.value == undefined){
                form.txtItemPriceCtaInscrip[0].value = round_decimals('<%=itemBean.getNpprice()%>' ,2); ;
              }
              else{
                form.txtItemPriceCtaInscrip.value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
              }
              form.hdnItemValuetxtItemPriceCtaInscrip.value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
              form.txtItemRentEquipment.value = round_decimals('<%=itemBean.getNprent()%>' ,2);
              form.hdnItemValuetxtItemRentEquipment.value = round_decimals('<%=itemBean.getNprent()%>' ,2);
              form.hdnItemValuetxtItemPromotioId.value = '<%=itemBean.getNppromotionid()%>';
            }
            else if(registros > 1){
              form.hdnChangedOrder.value = "S";
              form.txtItemPriceCtaInscrip[<%=i%>].value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
              form.hdnItemValuetxtItemPriceCtaInscrip[<%=i%>].value = round_decimals('<%=itemBean.getNpprice()%>' ,2);
              form.txtItemRentEquipment[<%=i%>].value = round_decimals('<%=itemBean.getNprent()%>' ,2);
              form.hdnItemValuetxtItemRentEquipment[<%=i%>].value = round_decimals('<%=itemBean.getNprent()%>' ,2);
              form.hdnItemValuetxtItemPromotioId[<%=i%>].value = '<%=itemBean.getNppromotionid()%>';
            }        
    <%
          }
    %>
    
        try{ 
          parent.mainFrame.fxCalculateTotalItems();
        }catch(e){
          //alert('fxCalculateTotalVO ' + e);
        }
        try{ 
          parent.mainFrame.fxCalculateTotal();
        }catch(e){
          //alert('fxCalculateTotalVO ' + e);
        }
        
    <%
        }
      }
    %>
    
    //Solo es necesario para el modo edición
    /*try{ 
        parent.mainFrame.fxFixOriginalPrice();
    }catch(e){
      //alert('fxFixOriginalPrice ' + e);
    }*/
                
    </script>
    
  </head>
  <body></body>
</html>