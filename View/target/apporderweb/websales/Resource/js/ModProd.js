/**
 * Created by floreedd on 05/12/2016.
 */


/*
 Funcion que devuelve el valor de una etiqueta de un select a partir de su valor
 */

function getLabelByValueFromSelect(elem,value){
    var options = elem.options;
    var text = "";
    for(var i = 0; i < options.length; i++) {
        if(options[i].value === value) {
            text = options[i].text;
            break;
        }
    }
    return text;
}

/*
 Funcion para validar plan tarifario
 Valor 9 representa al item de plan tarifario
 */

function validateIfSamePlanTarifario(nppaymentterms,nppaymenttermscst,vctItemOrderOriginal,npplantarifario,cmbPlanTarifario){
    var indPlanTarif = -1;
    if(nppaymentterms == nppaymenttermscst) {
        alert("Tipo de Orden de Pago " + nppaymenttermscst);
    }
        for(j = 0; j < vctItemOrderOriginal.size(); j ++ ){
            if(vctItemOrderOriginal.elementAt(j).npobjitemid == 9){
                indPlanTarif = j;
            }
        }

    if(indPlanTarif >= 0){
        var npplantarifariooriginal = vctItemOrderOriginal.elementAt(indPlanTarif).npobjitemvalue;
        if(Number(npplantarifariooriginal) != Number(npplantarifario)){
            alert("El plan tarifario original("+getLabelByValueFromSelect(cmbPlanTarifario,npplantarifariooriginal)+") no coincide con el seleccionado("+getLabelByValueFromSelect(cmbPlanTarifario,npplantarifario)+")");
            return false;
        }
    }
    return true;
}

/*  Valida si la orden es:
 Cargo al recibo: Deja pasar
 Contado: Se debe verificar que el monto del nuevo equipo coincida con el anterior

 Variables
 nppaymentterms = Tipo de Orden de Pago
 nppaymenttermscst = Tipo de Orden de Pago constante (para comparar con nppaymentterms)
 vctitemOrderOriginal = Vector de Orden Original
 npprice = Precio que ocurre tras el cambio
 nppriceexception = Precio de excepcion que ocurre tras el cambio
 */
function validateIfSamePrice(nppaymentterms,nppaymenttermscst,vctItemOrderOriginal,npprice,nppriceexception){

    var indPrice=-1;
    var indPriceExcep=-1;
    if(nppaymentterms == nppaymenttermscst){
        for(j = 0; j < vctItemOrderOriginal.size(); j ++ ){
            if(vctItemOrderOriginal.elementAt(j).npobjitemid == 17){
                indPrice = j;
            }else if(vctItemOrderOriginal.elementAt(j).npobjitemid == 18){
                indPriceExcep = j;
            }
        }
    }
    if(indPrice >= 0  && indPriceExcep >= 0){
        if(nppriceexception != ""){
            if(vctItemOrderOriginal.elementAt(indPriceExcep).npobjitemvalue!=""){
                if(Number(vctItemOrderOriginal.elementAt(indPriceExcep).npobjitemvalue) == Number(nppriceexception)){
                    return true;
                }
            }else if(vctItemOrderOriginal.elementAt(indPrice).npobjitemvalue!=""){
                if(Number(vctItemOrderOriginal.elementAt(indPrice).npobjitemvalue) == Number(nppriceexception)){
                    return true;
                }
            }
        }else if(npprice != ""){
            if(vctItemOrderOriginal.elementAt(indPriceExcep).npobjitemvalue!=""){
                if(Number(vctItemOrderOriginal.elementAt(indPriceExcep).npobjitemvalue) == Number(npprice)){
                    return true;
                }
            }else if(vctItemOrderOriginal.elementAt(indPrice).npobjitemvalue!=""){
                if(Number(vctItemOrderOriginal.elementAt(indPrice).npobjitemvalue) == Number(npprice)){
                    return true;
                }
            }
        }

        return false;
    }

    return true;
}

function validateCheckAddendumIfPriceExcepAndListPriceT(checks,nppricetype,nppriceexception){
    if(typeof checks != 'undefined' && nppricetype == "ORIGINAL"){
        var count = 0;
        if(nppriceexception != "" && nppriceexception >= 0){
            for(var i = 0;i<checks.length;i++){
                if(checks[i].checked){
                    count++;
                }
            }
            if(count>0){
                return true;
            }
            return false;
        }
    }
    return true;
}
