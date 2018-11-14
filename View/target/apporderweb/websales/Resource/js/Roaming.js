/*
 * Funciones que se encargan de la validación de servicio roaming recurrente.
 * MMONTOYA 09/09/2015
 */

// Invocado desde PopUpOrder.jsp.
function validateSelectedRecurrentRoamingService(specificationId, orderId) {
    if (specificationId != SPEC_ACTIVAR_PAQUETES_ROAMING) {
        return true;
    }

    // Inicio Obtiene el bagType, el validity y el bagCode
    var bagType = null;
    var validity = null;
    var bagCode = null; //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]

    for (i = 0; i < vServicio.size(); i++){
        var objServicio = vServicio.elementAt(i);

        if (objServicio.id == form.cmbItemServiceROA.value) {
            bagType = objServicio.bagType;
            validity = objServicio.validity;
            bagCode = objServicio.bagCode;  //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]

            break;
        }
    }
    // Fin Obtiene el bagType.

    var phone = form.txt_ItemPhone.value;
    var processDate = parent.opener.document.frmdatos.txtFechaProceso.value;
    //return validateRecurrentRoamingService(bagType, phone, processDate, validity, orderId);
    return validateRecurrentRoamingService(bagType, phone, processDate, validity, orderId, bagCode); //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]
}

// Invocado desde JP_ORDER_NEW_END_showpage.jsp y JP_ORDER_EDIT_STAR.jsp
function validateAllRecurrentRoamingService(specificationId, orderId) {
    if (specificationId != SPEC_ACTIVAR_PAQUETES_ROAMING) {
        return true;
    }

    var processDate = form.txtFechaProceso.value;
    if (!validateProcessDate(specificationId)) {
        return;
    }

    var table = document.getElementById("items_table");
    var txtItemServBagType = $('[name="hdnItemValuetxtItemServBagType"]');
    var txtItemPhone = $('[name="hdnItemValuetxtItemPhone"]');
    var txtItemServValidActivationDate = $('[name="hdnItemValuetxtItemServValidActivationDate"]');
    var txtItemServValidity = $('[name="hdnItemValuetxtItemServValidity"]');
    var txtItemServBagCode = $('[name="hdnItemValuetxtItemServBagCode"]'); //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]

    for (var i = 1; i < table.rows.length; i++) {
        var index = i - 1; // El array de controles no coincide con el de filas debido al header.
        var bagType = txtItemServBagType[index].value;
        var phone = txtItemPhone[index].value;
        var activationDate = txtItemServValidActivationDate[index].value;
        var validity = txtItemServValidity[index].value;
        var bagCode = txtItemServBagCode[index].value;  //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]

        // Si se modificó la fecha de proceso, se vuelve a validar.
        if (processDate != activationDate) {
            // if (!validateRecurrentRoamingService(bagType, phone, processDate, validity, orderId)) {
            if (!validateRecurrentRoamingService(bagType, phone, processDate, validity, orderId, bagCode)) { //CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]
                return false;
            }

            // Pasó la validación, se guarda el nuevo valor de la fecha de activación.
            txtItemServValidActivationDate[index].value = processDate;
        }
    }

    return true;
}

//CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming] - Se agrego el parametro bagCode
function validateRecurrentRoamingService(bagType, phone, processDate, validity, orderId, bagCode) {

    //if (bagType == TIPO_BOLSA_RECURRENTE) {  //CFERNANDEZ [PRY-0858]
    if (bagType != null) {
        var message = null;

        $.ajax({
            url: CONTEXT_PATH + "/itemServlet",
            //data: "strPhone=" + phone + "&strProcessDate=" + processDate + "&strValidity=" + validity + "&strOrderId=" + orderId + "&hdnMethod=validateRecurrentRoamingService",
            data: "strPhone=" + phone + "&strProcessDate=" + processDate + "&strValidity=" + validity + "&strOrderId=" + orderId + "&strBagCode=" + bagCode + "&hdnMethod=validateRecurrentRoamingService", //CFERNANDEZ [PRY-0858]
            async: false,
            type: "POST",
            success:function(data) {
                message = data;
            }
        });

        if (message != null && message != "") {
            alert(message);
            return false;
        }
     }

    return true
}

/*
 * Funciones que se encargan de la validación de la fecha de activación.
 * MMONTOYA 06/10/2015
 */

// Invocado desde NewOnDisplaySectionItems.jsp y EditOnDisplaySectionItems.jsp
 function validateProcessDate(specificationId) {
    if (specificationId != SPEC_ACTIVAR_PAQUETES_ROAMING) {
        return true;
    }

    if (form.txtFechaProceso.value == "" || form.txtFechaProceso.value == null) {
        alert("No ha ingresado la fecha de activación");
        form.txtFechaProceso.focus();
        return false;
    } else {
        // Valida que la fecha de proceso sea mayor que la fecha actual.
        var params = form.txtFechaProceso.value.split("/");
        var day = params[0];
        var month = params[1];
        var year = params[2];
        var processDate = new Date();
        processDate.setFullYear(year, month - 1, day);
        var today = new Date();

        // La fecha de activación programada debe ser mayor al día de registro de la orden.
        if (!(processDate > today)) {
            alert("La fecha de activación debe ser posterior a la actual");
            form.txtFechaProceso.focus();
            return false;
        }
    }

    return true;
 }

