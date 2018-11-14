<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
    // Obtiene el nombre del control
    String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
    if (nameHtmlItem == null) nameHtmlItem = "";
%>
<script>
    var form = parent.mainFrame.frmdatos;

    function fxAddService(combo) {
        /*
         * Marca el servicio como seleccionado en el array de servicios.
         * Asigna los atributos del servicio a los controles.
         */

        for (i = 0; i < vServicio.size(); i++) {
            var objServicio = vServicio.elementAt(i);

            // Inicializa cada servicio.
            objServicio.modify_new = "N";

            if (objServicio.id == combo.value) {
                // Marca el servicio como seleccionado.
                objServicio.modify_new = "S";

                // Asigna los atributos del servicio a los controles.
                form.txtServiceId.value = "|" + objServicio.id;
                form.txtServicePrice.value = "|" + objServicio.price;
                form.txt_ItemPriceCtaInscrip.value = objServicio.price;
                form.txtItemServPlanType.value = objServicio.planType;
                form.txtItemServBagCode.value = objServicio.bagCode;
                form.txtItemServBagType.value = objServicio.bagType;
                form.txtItemServValidActivationDate.value = parent.opener.document.frmdatos.txtFechaProceso.value;
                form.txtItemServValidity.value = objServicio.validity;

				//TTORRES 18704/2017 SE COMENTO SENTENCIA BREAK
                //EFLORES se comento la sentencia break.
                //break;
            }
        }

        /*
         * Asigna el nombre del servicio a la caja de texto del
         * servicio seleccionado.
         */

        form.txtItemSelectedServiceROA.value = combo.options[combo.selectedIndex].text;
    }
</script>
<td align="left" class="CellLabel" vAlign="top">
    <div>
        <font color="red">&nbsp;*&nbsp;</font>Servicios Disponibles
    <div>
</td>
<td align="left" class="CellContent">&nbsp;
    <select name="cmbItemServiceROA" onchange="fxAddService(this);">
        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
    </select>
</td>
<input type="hidden" value="" name="item_services">