<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.EquipmentBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>

<%      
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  Hashtable hshtinputNewSection = (Hashtable)request.getAttribute("hshtInputNewSection");
  String hdnSpecification = (String)hshtinputNewSection.get("hdnSpecification");
  boolean isOrderReposicion = hdnSpecification.equals(String.valueOf(Constante.SPEC_REPOSICION));
  
  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<Combo Tipo de almacén>>>>>>>>>>>>>>>>>>>>>>>>>>>");
%>

<script language="javascript">
   /* MMONTOYA
    * Asigna el valor por defecto del combo cmbItemFlagAccessory según el
    * valor seleccionado en el combo cmbItemProductStatus
    */
    function fxSetDefaultValueCmbItemFlagAccessory() {
        // Solo para las órdenes de repocisión.
        if (!isOrderReposicion) {
            return;
        }
    
        // Índices de cmbItemFlagAccessory.
        var IDX_DEFAULT_VALUE = 0;
        var IDX_ACCESORIO_SI = 1;
        var IDX_ACCESORIO_NO = 2;
        
        var cmbItemProductStatus = document.frmdatos.cmbItemProductStatus;                        
        var cmbModelOriginal = document.frmdatos.txt_ItemModel;      // orders.np_obj_item, npobjitemid = 14        
        var cmbModelSolicitado = document.frmdatos.cmb_ItemProducto; // orders.np_obj_item, npobjitemid = 8    
        var cmbItemFlagAccessory = document.frmdatos.cmbItemFlagAccessory;                
        
        if (cmbItemProductStatus.value == "Usados") {        
            cmbItemFlagAccessory.options[IDX_ACCESORIO_NO].selected = true;            
        } else if (cmbItemProductStatus.value == "Nuevos") {        
            if (cmbModelOriginal.value != "" && cmbModelSolicitado.value != "" && cmbModelOriginal.value == cmbModelSolicitado.value) {
                cmbItemFlagAccessory.options[IDX_ACCESORIO_NO].selected = true;                
            } else {
                cmbItemFlagAccessory.options[IDX_ACCESORIO_SI].selected = true;
            }            
        }
        
        fxSetEnableCmbItemFlagAccessory();
    }
    
    function fxSetEnableCmbItemFlagAccessory() {
        var cmbItemFlagAccessory = document.frmdatos.cmbItemFlagAccessory;
        var IDX_ACCESORIO_NO = 2;
        
        if (cmbItemProductStatus.value == "Usados") {
            cmbItemFlagAccessory.disabled = true;
            cmbItemFlagAccessory.options[IDX_ACCESORIO_NO].selected = true;
        } else {
            cmbItemFlagAccessory.disabled = false;
        }
    }
</script>

<select name="<%=nameHtmlItem%>" onchange="fxSetDefaultValueCmbItemFlagAccessory();"> 
   <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
   <option value="Nuevos">Nuevos</option>
   <option value="Usados">Usados</option>
</select>

<script language="javascript">
    var isOrderReposicion = <%=isOrderReposicion%>;

    // MMONTOYA
    // Selecciona el valor por defecto al cargar la página.
    if (isOrderReposicion) {        
        var IDX_PRODUCT_STATUS_USADOS = 2;
        var cmbItemProductStatus = document.frmdatos.cmbItemProductStatus;
        cmbItemProductStatus.options[IDX_PRODUCT_STATUS_USADOS].selected = true;
    }
</script>