/*
 * El objetivo de este requerimiento es asegurar que una Orden de Portabilidad (Subcategoria 2068 Portabilidad:
 * Solicitud de Postpago y 2069 Portabilidad: Solicitud de Prepago) no pueda avanzar del Inbox inicial (TIENDA01
 * en caso de órdenes creadas desde Incidentes, y VENTAS en el caso que las órdenes sean creadas desde
 * oportunidades) si es que no se han ingresado todos los números telefónicos a portar.
 */
function validateAddedItemQuantity(originName, orderStatus) {

    // Solo se validan los siguientes casos:
    // Si la orden es creada desde incidentes y el estado es TIENDA01
    // Si la orden es creada desde opotunidades y el estado es VENTAS.
    if (!((originName == 'INCIDENTE' && orderStatus == 'TIENDA01') || (originName == 'OPORTUNIDAD' && orderStatus == 'VENTAS'))) {
        return true;
    }

    // Si la cantidad de items total es igual a la ingresada pasa la validación.
    var addedItemQuantity = getAddedItemQuantity();
    if (addedItemQuantity == getTotalItemQuantity()) {
        return true;
    }

    var message;
    if (addedItemQuantity == 0) {
        message = "No existen números para ser portados";
    } else {
        message = "No ingresó todos los números para ser portados";
    }

    alert(message);
    return false;
}

/*
 * Devuelve la cantidad total de items que se deben ingresar.
 */
function getTotalItemQuantity() {
    var total = 0;

    $("#items_table tr td div input[name='txtItemQuantity']").each(function () {
        var itemQuantity = $(this).val();
        total += parseInt(itemQuantity);
    });

    return total;
}

/*
 * Devuelve la cantidad total de items ingresados.
 */
function getAddedItemQuantity() {
    var total = 0;

    $("#tableItemsPortab tr td input[name='txtphonenumber']").each(function () {
        var phoneNumber = $(this).val();

        if (phoneNumber != "") {
            total++;
        }
    });

    return total;
}