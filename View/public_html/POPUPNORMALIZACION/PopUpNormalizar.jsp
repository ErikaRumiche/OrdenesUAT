<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    try{
        String strRutaContext=request.getContextPath();
        String actionURL_NormalizarServlet = strRutaContext+"/normalizarDireccionServlet";

        //datosNorm=division-categoria-subcategoria-codcliente-codpromotor-idorden
        String[] arrayDatos = request.getParameter("datosNorm").split("-");
        String codCliente = arrayDatos[3];
        String codPromotor = arrayDatos[4];
        String idOrden = "";
        if(arrayDatos.length == 6){
            idOrden = arrayDatos[5];
        }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <title>NORMALIZACION DE DIRECCIONES</title>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jquery-1.10.2.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="text/css" media="screen">@import "<%=Constante.PATH_APPORDER_SERVER%>/Resource/tabs.css";</style>

	<script type="text/javascript">
		var arrayValueTipoVia = new Array(4);
	    var arrayValueTipoZona = new Array(4);
	    var arrayValueTipoInterior = new Array(4);

	    var arrayValueDepart = new Array(4);

		cargarArraySelectValue(arrayValueTipoVia);
		cargarArraySelectValue(arrayValueTipoZona);
		cargarArraySelectValue(arrayValueTipoInterior);

		cargarArraySelectValue(arrayValueDepart);

		cargarListTipoNor('<%=Constante.TIPOVIA%>', 'selectTipoVia');
		cargarListTipoNor('<%=Constante.TIPOZONA%>', 'selectTipoZona');
		cargarListTipoNor('<%=Constante.TIPOINTERIOR%>', 'selectTipoInterior');
	    cargarDepartamentos();

	    function cargarArraySelectValue(val){
	    	val[0] = null;
	    	val[1] = null;
	    	val[2] = null;
	    	val[3] = null;
	    }

		jQuery(function() {
            jQuery(window).unload(function(){
                parent.opener.parent.mainFrame.redirectOrder('<%=idOrden%>');
            });

			for(p=0; p<4; p++){
				jQuery("#tab-container-tab-"+p).hide();
				jQuery("#idMainTab"+p).hide();
			}

			cargarDireccionesANorm();
		});

		var arrayTipoDirActive = [];
	    function cargarDireccionesANorm(){
	    	jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: {
	            	myaction: 'obtenerDirecciones',
			        codCliente: '<%=codCliente%>'
			    },
	            type: "POST",
	            dataType: 'json'
	    	}).done(function(data){
            	var arrayDir = data.listDir;

				var arrayTabActive = [];
            	for(p=0; p<arrayDir.length; p++) {
            		var flagCodTipoDir = arrayDir[p].codTipoDireccion.substr(0, 1);
            		var estado = (typeof  arrayDir[p].estado === 'undefined') ? "" :  jQuery.trim(arrayDir[p].estado);
	            	for(j=1; j<=4; j++){
	            		try {
		            		var codTab = j - 1;
		            		if(flagCodTipoDir==j && estado.length == 0){
		            			arrayTabActive.push(codTab);
		            			arrayTipoDirActive.push(j);
		            			jQuery("#tab-container-tab-"+codTab).show();

		            			jQuery("#txtNormalizar"+j).val(jQuery.trim(arrayDir[p].direccion));
		            			jQuery("#txtDep"+j).val(arrayDir[p].departamento);
		            			jQuery("#txtProv"+j).val(arrayDir[p].provincia);
		            			jQuery("#txtDist"+j).val(arrayDir[p].distrito);

	            				jQuery("#idDireccion"+j).val(arrayDir[p].idDireccion);
		            			jQuery("#idTipoDireccion"+j).val(arrayDir[p].tipoDireccion);
		            			jQuery("#idCodTipoDireccion"+j).val(arrayDir[p].codTipoDireccion);
		            			jQuery("#idCodDep"+j).val(arrayDir[p].departamentoId);
		            			jQuery("#idCodProv"+j).val(arrayDir[p].provinciaId);
		            			jQuery("#idCodDist"+j).val(arrayDir[p].distritoId);

		            			$("#referencia"+j).val(jQuery.trim(arrayDir[p].referencia));

		            			break;
		            		}
	            		}catch(err) {}
	            	}
            	}

            	var valorMin = 999999;
            	for(i=0; i<arrayTabActive.length; i++) {
            		var codTab = arrayTabActive[i];
            		if(valorMin > codTab){
            			valorMin = codTab;
            		}
            	}

            	jQuery("#idMainTab"+valorMin).show();
            	jQuery("#tab-container-tab-"+valorMin+" a:first").addClass("tab-active");

            	arrayTipoDirActive.sort();
	        }).fail(function(jqXHR, textStatus, errorThrown){
	        	alert('Error interno carga de direcciones. Informar a soporte');
	        });
	    }

	    function cargarListTipoNor(tipo, selectTipo){
	    	jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: {
	            	myaction: 'obtenerListTipos',
			        tipo: tipo
			    },
	            type: "POST",
	            dataType: 'json'
	    	}).done(function(data){
            	var arrayTV = data.listTipo;
            	for(j=1; j<=4; j++){
            		try {
            			jQuery("#"+selectTipo+j).empty();
            			jQuery("#"+selectTipo+j).append(jQuery("<option></option>")
	                            .attr("value","-1")
	                            .text(""));
	            		for(i=0; i<arrayTV.length; i++) {
		                    jQuery("#"+selectTipo+j).append(jQuery("<option></option>")
		    		                            .attr("value",arrayTV[i].wv_npValue)
		    		                            .text(arrayTV[i].wv_npValueDesc));
		    			}

	            		if(selectTipo == 'selectTipoVia'){
	            			if(arrayValueTipoVia[j-1] != null){
		            			jQuery("#"+selectTipo+j).value=arrayValueTipoVia[j-1];
		            		}
	            		}else if(selectTipo == 'selectTipoZona'){
	            			if(arrayValueTipoZona[j-1] != null){
		            			jQuery("#"+selectTipo+j).value=arrayValueTipoZona[j-1];
		            		}
	            		}else{
	            			if(arrayValueTipoInterior[j-1] != null){
		            			jQuery("#"+selectTipo+j).value=arrayValueTipoInterior[j-1];
		            		}
	            		}
	            	}catch(err) {}
            	}
	        }).fail(function(jqXHR, textStatus, errorThrown){
	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        });
	    }

	    function cargarDepartamentos(){
	        var params = 'myaction=obtenerDepartamentos';
	    	jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: params,
	            type: "POST",
	            dataType: 'json'
	    	}).done(function(data){
            	var arrayDep = data.listDepProvDist;
            	for(j=1; j<=4; j++){
            		try {
            			jQuery("#selectDepNor"+j).empty();
	            		for(i=0; i<arrayDep.length; i++) {
	            			var codInei = (typeof arrayDep[i].codigoinei === 'undefined') ? "" : arrayDep[i].codigoinei;
		                    jQuery("#selectDepNor"+j).append(jQuery("<option></option>")
		    		                            .attr("value",arrayDep[i].dep+''+arrayDep[i].prov+''+arrayDep[i].dist+ ';' + arrayDep[i].npubigeoid)
		    		                            .text(arrayDep[i].nombre));
		    			}

	            		if(arrayValueDepart[j-1] != null){
	            			jQuery("#selectDepNor"+j).value=arrayValueDepart[j-1];
	            		}
	            	}catch(err) {}
            	}
	        }).fail(function(jqXHR, textStatus, errorThrown){
	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        });
	    }

		function changeTipoVia(num){
			jQuery("#txtNombreVia"+num).val("");
			var indexSelected = jQuery( "#selectTipoVia"+num+" option:selected" ).val();
			if(indexSelected!="-1"){
				jQuery("#txtNombreVia"+num).removeAttr("disabled");
			}else{
				jQuery("#txtNombreVia"+num).attr("disabled", true);
			}
	    }

		function changeTipoInterior(num){
			jQuery("#txtNumeroInterior"+num).val("");
			var indexSelected = jQuery( "#selectTipoInterior"+num+" option:selected" ).val();
			if(indexSelected!="-1"){
				jQuery("#txtNumeroInterior"+num).removeAttr("disabled");
			}else{
				jQuery("#txtNumeroInterior"+num).attr("disabled", true);
			}
	    }

		function changeTipoZona(num){
			jQuery("#txtNombreZona"+num).val("");
			var indexSelected = jQuery( "#selectTipoZona"+num+" option:selected" ).val();
			if(indexSelected!="-1"){
				jQuery("#txtNombreZona"+num).removeAttr("disabled");
			}else{
				jQuery("#txtNombreZona"+num).attr("disabled", true);
			}
	    }

		var arrayLegal;
		var arrayFacturacion;
		var arrayEntrega;
		var arrayComunicacion;

		function changeDireccionNormalizacion(num){
			var indexSelected = jQuery( "#selectDirNor"+num+" option:selected" ).val();
			var obj;
			switch (num) {
			    case 1: obj = arrayLegal[indexSelected]; break;
			    case 2: obj = arrayFacturacion[indexSelected]; break;
			    case 3: obj = arrayEntrega[indexSelected]; break;
			    case 4: obj = arrayComunicacion[indexSelected]; break;
			}

 			jQuery.ajax({
 	            url: '<%=actionURL_NormalizarServlet%>',
 	            data: {
 	            	myaction: 'obtenerIdDepProvDist',
 			        ubigeoIneiSelect: obj.ubigeoInei,
 			        depId: jQuery("#idCodDep"+num).val(),
 	                provId: jQuery("#idCodProv"+num).val(),
 	                distId: jQuery("#idCodDist"+num).val()
 			    },
 	            type: "POST",
 	            dataType: 'json'
 			}).done(function(data){
            	var tiposDirecciones = jQuery("#idTiposDirEquals"+num).val();
            	var numOcurr = jQuery("#idNumOcurrencia"+num).val();
            	var init = 0;
				setValuesDireccionesNormalizacion(init, numOcurr, obj, num, tiposDirecciones, data.depId, data.provId, data.distId);

 			}).fail(function(jqXHR, textStatus, errorThrown){
	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        });
		}

		function setValuesDireccionesNormalizacion(init, numOcurr, obj, num, tiposDirecciones, depId, provId, distId){
			var objUbigeo = new Object();
			objUbigeo.valDep = '';
			objUbigeo.textDep = '';
			objUbigeo.valProv = '';
			objUbigeo.textProv = '';
			objUbigeo.valDist = '';
			objUbigeo.textDist = '';

			var flag = '-1';
			jQuery("#selectTipoVia"+num+" option").each(function(){
				var valueVia = jQuery(this).val();
				arrayValueTipoVia[num-1]=obj.tipoVia;
				if(valueVia == obj.tipoVia){
					flag = '1';
					jQuery(this).attr('selected', 'selected');
				}

				if(flag == '1'){
					return false;
				}
		    });
			if(flag == '-1'){
				jQuery("#selectTipoVia"+num+" option[value='-1']").attr('selected', 'selected');
				jQuery("#txtNombreVia"+num).attr("disabled", true);
			}else{
				jQuery("#txtNombreVia"+num).removeAttr("disabled");
			}
			jQuery("#txtNombreVia"+num).val((typeof obj.nombreVia === 'undefined') ? "" : obj.nombreVia);

			jQuery("#numeroPuerta"+num).val((typeof obj.numeroPuerta === 'undefined') ? "" : obj.numeroPuerta);

			flag = '-1';
			jQuery("#selectTipoInterior"+num+" option").each(function(){
				var valueInterior = jQuery(this).val();
				arrayValueTipoInterior[num-1] = obj.tipoInterior;
				if(valueInterior == obj.tipoInterior){
					flag = '1';
					jQuery(this).attr('selected', 'selected');
				}

				if(flag == '1'){
					return false;
				}
		    });
			if(flag == '-1'){
				jQuery("#selectTipoInterior"+num+" option[value='-1']").attr('selected', 'selected');
				jQuery("#txtNumeroInterior"+num).attr("disabled", true);
			}else{
				jQuery("#txtNumeroInterior"+num).removeAttr("disabled");
			}
			jQuery("#txtNumeroInterior"+num).val((typeof obj.numeroInterior === 'undefined') ? "" : obj.numeroInterior);

            jQuery("#txtMza"+num).val((typeof obj.manzana === 'undefined') ? "" : obj.manzana);
            jQuery("#txtLote"+num).val((typeof obj.lote === 'undefined') ? "" : obj.lote);

            flag = '-1';
			jQuery("#selectTipoZona"+num+" option").each(function(){
				var valueZona = jQuery(this).val();
				arrayValueTipoZona[num-1]=obj.tipoZona;
				if(valueZona == obj.tipoZona){
					flag = '1';
					jQuery(this).attr('selected', 'selected');
				}

				if(flag === '1'){
					return false;
				}
		    });
			if(flag == '-1'){
				jQuery("#selectTipoZona"+num+" option[value='-1']").attr('selected', 'selected');
				jQuery("#txtNombreZona"+num).attr("disabled", true);
			}else{
				jQuery("#txtNombreZona"+num).removeAttr("disabled");
			}
			jQuery("#txtNombreZona"+num).val((typeof obj.nombreZona === 'undefined') ? "" : obj.nombreZona);

            jQuery("#txtReferencia"+num).val((typeof obj.referencia === 'undefined') ? "" : obj.referencia);

            jQuery("#selectDepNor"+num+" option").each(function(){
            	var arrayValueZona = jQuery(this).val().split(";");
				var valUbigeoDepId = arrayValueZona[1];

				arrayValueDepart[num-1]=jQuery(this).val();

				jQuery("#selectProvNor"+num).empty();
				jQuery("#selectDistNor"+num).empty();
				if(valUbigeoDepId==depId){
					objUbigeo.valDep = jQuery(this).val();
					objUbigeo.textDep = jQuery(this).text();

					jQuery(this).attr('selected', 'selected');
					var codDep = arrayValueZona[0].substr(0, 2);

			    	jQuery.ajax({
			            url: '<%=actionURL_NormalizarServlet%>',
			            data: {
			            	myaction: 'obtenerProvincias',
					        codDep: codDep
					    },
			            type: "POST",
			            dataType: 'json'
			    	}).done(function(data){
		            	var arrayProv = data.listDepProvDist;
		            	var codProv = "";
	            		for(i=0; i<arrayProv.length; i++) {
	            			if(provId==arrayProv[i].npubigeoid){
	            				codProv = arrayProv[i].prov;
	            				objUbigeo.valProv = arrayProv[i].dep+''+arrayProv[i].prov+''+arrayProv[i].dist+ ';' + arrayProv[i].npubigeoid;
	            				objUbigeo.textProv = arrayProv[i].nombre;
	            				jQuery("#selectProvNor"+num).append(jQuery("<option></option>")
    		                            .attr("value", objUbigeo.valProv)
    		                            .attr("selected","selected")
    		                            .text(objUbigeo.textProv));
	            			}else{
	            				jQuery("#selectProvNor"+num).append(jQuery("<option></option>")
    		                            .attr("value", arrayProv[i].dep+''+arrayProv[i].prov+''+arrayProv[i].dist+ ';' + arrayProv[i].npubigeoid)
    		                            .text(arrayProv[i].nombre));
	            			}
		    			}

	            		jQuery.ajax({
				            url: '<%=actionURL_NormalizarServlet%>',
				            data: {
				            	myaction: 'obtenerDistritos',
						        codDep: codDep,
						        codProv: codProv
						    },
				            type: "POST",
				            dataType: 'json'
	            		}).done(function(data){
			            	var arrayDist = data.listDepProvDist;
		            		for(i=0; i<arrayDist.length; i++) {
		            			if(distId==arrayDist[i].npubigeoid){
		            				objUbigeo.valDist = arrayDist[i].dep+''+arrayDist[i].prov+''+arrayDist[i].dist+ ';' + arrayDist[i].npubigeoid;
		            				objUbigeo.textDist = arrayDist[i].nombre;
		            				jQuery("#selectDistNor"+num).append(jQuery("<option></option>")
	    		                            .attr("value", objUbigeo.valDist)
	    		                            .attr("selected","selected")
	    		                            .text(objUbigeo.textDist));
		            			}else{
		            				jQuery("#selectDistNor"+num).append(jQuery("<option></option>")
	    		                            .attr("value",arrayDist[i].dep+''+arrayDist[i].prov+''+arrayDist[i].dist+ ';' + arrayDist[i].npubigeoid)
	    		                            .text(arrayDist[i].nombre));
		            			}
			    			}

	            			callbackAjaxNorm(init, numOcurr, num, tiposDirecciones, obj, objUbigeo);
	            		}).fail(function(jqXHR, textStatus, errorThrown){
	        	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        	        });
			    	}).fail(function(jqXHR, textStatus, errorThrown){
			        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
			        });
				}
		    });
		}

		function callbackAjaxNorm(init, numOcurr, num, tiposDirecciones, obj, objUbigeo){
			if(init == 0){
				for(i=0; i<arrayMemoryDirNom.length; i++){
					var objE = arrayMemoryDirNom[i];
					if(objE.tipo == num.toString()){
						arrayMemoryDirNom.splice(i, 1);
						break;
					}
				}
			}else{
				jQuery("#idTiposDirEquals"+num).val(tiposDirecciones);
				jQuery("#idNumOcurrencia"+num).val(numOcurr);
			}

			var infoDirNorm = new Object();
			infoDirNorm.tipo=num.toString();
			infoDirNorm.tiposDirEquals=tiposDirecciones;
			infoDirNorm.numOcurrencia=numOcurr;
			infoDirNorm.valueOpcionDirNorm=jQuery("#selectDirNor"+num+" option:selected").val();
			infoDirNorm.data=obj;
			infoDirNorm.idDir=jQuery("#idDireccion"+num).val();
			infoDirNorm.textUbigeoDep=objUbigeo.textDep;
			infoDirNorm.textUbigeoProv=objUbigeo.textProv;
			infoDirNorm.textUbigeoDist=objUbigeo.textDist;

			infoDirNorm.tipoViaText=jQuery("#selectTipoVia"+num+" option:selected").text();
			infoDirNorm.tipoInteriorText=jQuery("#selectTipoInterior"+num+" option:selected").text();
			infoDirNorm.tipoZonaText=jQuery("#selectTipoZona"+num+" option:selected").text();

			infoDirNorm.idPKDep=objUbigeo.valDep.split(";")[1];
			infoDirNorm.idPKProv=objUbigeo.valProv.split(";")[1];
			infoDirNorm.idPKDist=objUbigeo.valDist.split(";")[1];

			infoDirNorm.codUbigeo=objUbigeo.valDist.split(";")[0];

			infoDirNorm.estado='<%=Constante.ESTADO_DIR_NORMALIZADO%>';
			infoDirNorm.motivo='<%=Constante.MENSAJE_MOTIVO_NORMALIZADO%>';
			infoDirNorm.operacion='U';
			arrayMemoryDirNom.push(infoDirNorm);
		}

		function initArrayDirNorm(tipoDireccion){
			switch (tipoDireccion) {
			    case 1: arrayLegal=new Array(); break;
			    case 2: arrayFacturacion=new Array(); break;
			    case 3: arrayEntrega=new Array(); break;
			    case 4: arrayComunicacion=new Array(); break;
			}
		}

		function setArrayDirNorm(tipoDireccion, arrayDirNorm){
			switch (tipoDireccion) {
			    case 1: arrayLegal = arrayDirNorm; break;
			    case 2: arrayFacturacion = arrayDirNorm; break;
			    case 3: arrayEntrega = arrayDirNorm; break;
			    case 4: arrayComunicacion = arrayDirNorm; break;
			}
		}

	    function changeDepNor(num){
	    	jQuery("#selectProvNor"+num).empty();
	    	jQuery("#selectDistNor"+num).empty();

	    	var valueSelected = jQuery( "#selectDepNor"+num+" option:selected" ).val().split(";");

	    	var codDep = valueSelected[0].substr(0, 2);
	    	jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: {
	            	myaction: 'obtenerProvincias',
			        codDep: codDep
			    },
	            type: "POST",
	            dataType: 'json'
	    	}).done(function(data){
            	var arrayProv = data.listDepProvDist;
           		for(i=0; i<arrayProv.length; i++) {
         				jQuery("#selectProvNor"+num).append(jQuery("<option></option>")
                            .attr("value",arrayProv[i].dep+''+arrayProv[i].prov+''+arrayProv[i].dist+ ';' + arrayProv[i].npubigeoid)
                            .text(arrayProv[i].nombre));
    			}

           		var firstProv = arrayProv[0];
           		jQuery.ajax({
       	            url: '<%=actionURL_NormalizarServlet%>',
       	            data: {
       	            	myaction: 'obtenerDistritos',
       	            	codDep: codDep,
       			        codProv: firstProv.prov
       			    },
       	            type: "POST",
       	            dataType: 'json'
           		}).done(function(data){
   	            	var arrayDist = data.listDepProvDist;
               		for(i=0; i<arrayDist.length; i++) {
             			jQuery("#selectDistNor"+num).append(jQuery("<option></option>")
   	                            .attr("value",arrayDist[i].dep+''+arrayDist[i].prov+''+arrayDist[i].dist+ ';' + arrayDist[i].npubigeoid)
   	                            .text(arrayDist[i].nombre));
   	    			}
           		}).fail(function(jqXHR, textStatus, errorThrown){
    	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
    	        });
	    	}).fail(function(jqXHR, textStatus, errorThrown){
	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        });
	    }

	    function changeProvNor(num){
	    	jQuery("#selectDistNor"+num).empty();

	    	var valueSelectedDep = jQuery( "#selectDepNor"+num+" option:selected" ).val().split(";");
	    	var valueSelectedProv = jQuery( "#selectProvNor"+num+" option:selected" ).val().split(";");

	    	var codDep = valueSelectedDep[0].substr(0, 2);
	    	var codProv = valueSelectedProv[0].substr(2, 2);
	    	jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: {
	            	myaction: 'obtenerDistritos',
	            	codDep: codDep,
			        codProv: codProv
			    },
	            type: "POST",
	            dataType: 'json'
	    	}).done(function(data){
            	var arrayDist = data.listDepProvDist;
           		for(i=0; i<arrayDist.length; i++) {
         			jQuery("#selectDistNor"+num).append(jQuery("<option></option>")
                            .attr("value",arrayDist[i].dep+''+arrayDist[i].prov+''+arrayDist[i].dist+ ';' + arrayDist[i].npubigeoid)
                            .text(arrayDist[i].nombre));
    			}
	    	}).fail(function(jqXHR, textStatus, errorThrown){
	        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
	        });
	    }

	    function cargarListDireccionNormalizada(tipoDireccion){
	    	initArrayDirNorm(tipoDireccion);
        	jQuery("#selectDirNor"+tipoDireccion).empty();
        	jQuery("#btnNormalizar"+tipoDireccion).attr("disabled", true);

        	var dataDir = new Object();
			dataDir.idOrden = '<%=idOrden%>';
			dataDir.codigoCliente = '<%=codCliente%>';
	       	dataDir.usuarioApp = '<%=codPromotor%>';
	       	dataDir.nombreAplicacion = '<%=Constante.Source_CRM%>';
	   		dataDir.idDireccion = jQuery("#idDireccion"+tipoDireccion).val();
	        dataDir.direccion = jQuery("#txtNormalizar"+tipoDireccion).val();
	        dataDir.referencia = jQuery("#referencia"+tipoDireccion).val();
	       	dataDir.tipoDireccion = jQuery("#idCodTipoDireccion"+tipoDireccion).val().substr(0, 1);
	       	dataDir.depId = jQuery("#idCodDep"+tipoDireccion).val();
	       	dataDir.provId = jQuery("#idCodProv"+tipoDireccion).val();
	       	dataDir.distId = jQuery("#idCodDist"+tipoDireccion).val();

	        jQuery.ajax({
	            url: '<%=actionURL_NormalizarServlet%>',
	            data: {
	            	myaction: 'getDirNorm',
			        dataNeg: JSON.stringify(dataDir)
			    },
	            type: "POST",
	            dataType: 'json'
	        }).done(function(data){
	            var arrayTipoDir = obtenerArraytipoDirEq(tipoDireccion);
	            var mensajeAlert = (typeof data.mensajeAlert === 'undefined') ? "" : data.mensajeAlert;
            	llenarDatosNormalizacion(mensajeAlert, arrayTipoDir, data.flag, data.numOcurrencia, data.lista, tipoDireccion, data.depId, data.provId, data.distId);
	        }).fail(function(jqXHR, textStatus, errorThrown){
	        	var mensajeAlert = (typeof jqXHR.responseText === 'undefined') ? "" : jqXHR.responseText;
                var arrayTipoDir = obtenerArraytipoDirEq(tipoDireccion);
                operDirNoNormalizado(mensajeAlert, arrayTipoDir, '<%=Constante.MENSAJE_MOTIVO_GENERICERROR%>', '<%=Constante.ERROR_PAGE%>');
	        });
	    }

	    function obtenerArraytipoDirEq(tipoDireccion){
            var arrayTipoDir = [];
            arrayTipoDir.push(tipoDireccion);
            var idDirOld =  jQuery("#idDireccion"+tipoDireccion).val();
            for(i=1; i<=4; i++){
                try {
                    if(i!=tipoDireccion){
                        if(idDirOld == jQuery("#idDireccion"+i).val()){
                            arrayTipoDir.push(i)
                        }
                    }
                }catch(err) {}
            }

            arrayTipoDir = arrayTipoDir.sort();

            return arrayTipoDir;
        }

        function operDirNoNormalizado(mensajeAlert, arrayTipoDir, motivo, flagGeo){
            var tiposDirecciones = arrayTipoDir.join();
            for(j=0; j<arrayTipoDir.length; j++){
                var num = arrayTipoDir[j];
                var infoDirNorm = new Object();
                infoDirNorm.idDir=jQuery("#idDireccion"+num).val();
                infoDirNorm.tipo=num.toString();
                infoDirNorm.tiposDirEquals=tiposDirecciones;
                infoDirNorm.estado='<%=Constante.ESTADO_DIR_NO_NORMALIZADO%>';
                infoDirNorm.operacion='U';
                infoDirNorm.motivo=motivo;
                arrayMemoryDirNom.push(infoDirNorm);

                jQuery("#btnNormalizar"+num).hide();
            }

            alert(mensajeAlert);
        }

	    var arrayMemoryDirNom = [];
		function llenarDatosNormalizacion(mensajeAlert, arrayTipoDir, flagGeo, numOcurr, arrayData, tipoDireccion, depId, provId, distId){
        	var tiposDirecciones = arrayTipoDir.join();
			if(flagGeo==1 || (flagGeo==0 && numOcurr > 0)){
				for(j=0; j<arrayTipoDir.length; j++){
					var num = arrayTipoDir[j];
					setArrayDirNorm(num, arrayData);
	            	for(i=0; i<arrayData.length; i++) {
	                    jQuery("#selectDirNor"+num).append(jQuery("<option></option>")
	    		                            .attr("value",i)
	    		                            .text(arrayData[i].direccionNormalizada));
	    			}

	    			var obj = arrayData[0];
                                var objClone = JSON.parse(JSON.stringify(obj));
	    			var init = 1;
	                setValuesDireccionesNormalizacion(init, numOcurr, objClone, num, tiposDirecciones, depId, provId, distId);

	                jQuery("#btnNormalizar"+num).hide();
	    			jQuery("#idExpandDivTipoDir"+num).css({ display: "block" });
				}

				if(arrayTipoDir.length > 1){
					var direccionesNorm = getDescTiposDir(arrayTipoDir);
					alert('Direcciones Normalizadas: '+direccionesNorm);
				}
        	}else if(flagGeo==0 && numOcurr == 0){
                operDirNoNormalizado(mensajeAlert, arrayTipoDir, '<%=Constante.MENSAJE_MOTIVO_NO_NORMALIZADO%>', flagGeo);
        	}else if(flagGeo < 0){
        	    if(flagGeo == '<%=Constante.CONSULTA_EXCEPTION_TIMEOUT%>'){
        			operDirNoNormalizado(mensajeAlert, arrayTipoDir, '<%=Constante.MENSAJE_MOTIVO_TIMEOUT%>', flagGeo);
        		}else if(flagGeo == '<%=Constante.CONSULTA_EXCEPTION_DISPONIBILIDAD%>'){
        			operDirNoNormalizado(mensajeAlert, arrayTipoDir, '<%=Constante.MENSAJE_MOTIVO_ERROR404%>', flagGeo);
        		}else{
        			operDirNoNormalizado(mensajeAlert, arrayTipoDir, '<%=Constante.MENSAJE_MOTIVO_NO_NORMALIZADO%>', flagGeo);
        		}
        	}
		}

		function getDescTiposDir(arrayTipoDir){
			var descTiposDir = '';
			for(k=0; k<arrayTipoDir.length; k++){
				var obj = arrayTipoDir[k];
				switch (obj) {
				    case 1: descTiposDir = descTiposDir + '<%=Constante.DESC_LEGAL%>, '; break;
				    case 2: descTiposDir = descTiposDir + '<%=Constante.DESC_FACTURACION%>, '; break;
				    case 3: descTiposDir = descTiposDir + '<%=Constante.DESC_ENTREGA%>, '; break;
				    case 4: descTiposDir = descTiposDir + '<%=Constante.DESC_COMUNICACION%>, '; break;
				}
			}

			if(descTiposDir.length > 0){
				descTiposDir = descTiposDir.substr(0, (descTiposDir.length - 2));
			}
			return descTiposDir;
		}

		function getValUpperNorm(valor){
			return (typeof valor === 'undefined') ? "" : jQuery.trim(valor).toUpperCase();
		}

		function getValUpperNormOpc(valor){
			var cad = (typeof valor === 'undefined') ? "" : jQuery.trim(valor).toUpperCase();
			if(cad == '-1'){
				cad = '';
			}
			return cad;
		}

		function guardarNormalizacion(){
			var arrayTipoDirActiveNoNorm = [];
			for(i=0; i<arrayTipoDirActive.length; i++){
				var tipoDirActive = arrayTipoDirActive[i];
				var flagNoNorm = 1;
				for(j=0; j<arrayMemoryDirNom.length; j++){
					var infoDirNorm = arrayMemoryDirNom[j];
					if(infoDirNorm.tipo == tipoDirActive){
						flagNoNorm = 0;
						break;
					}
				}

				if(flagNoNorm == 1){
					arrayTipoDirActiveNoNorm.push(tipoDirActive);
				}
			}

			if(arrayTipoDirActiveNoNorm.length > 0){
				var myObj = new Object();
				myObj.table = '<%=Constante.MENSAJE_ALERTA_NORMALIZACION%>';
				myObj.value = '<%=Constante.MENSAJE_ALERTA_FALTA_NORM_NUMERO%>';

				jQuery.ajax({
		            url: '<%=actionURL_NormalizarServlet%>',
		            data: {
		            	myaction: 'mensajeAlertServer',
		            	myObjMain: JSON.stringify(myObj)
				    },
		            type: "POST",
		            dataType: 'json'
		        }).done(function(data){
		        	var msg = (typeof data.mensajeAlert === 'undefined') ? "" : data.mensajeAlert;
		        	msg = msg.replace('%n%', arrayTipoDirActiveNoNorm.length);
		            alert(msg);
		        }).fail(function(jqXHR, textStatus, errorThrown){
		        	alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
		        });
			}else{
				var listDirEqual = [];
				var cloneArrayMemoryDirNom = JSON.parse(JSON.stringify(arrayMemoryDirNom));
				for(k=0; k<cloneArrayMemoryDirNom.length; k++){
					var memoryDir = cloneArrayMemoryDirNom[k];
					var tipoDir = memoryDir.tipo;
					var dataDir = memoryDir.data;
					var estadoFlag = memoryDir.estado;

					if(estadoFlag != '1'){
						var flagDir = 0;

						if(getValUpperNorm(dataDir.tipoVia) != getValUpperNormOpc(jQuery("#selectTipoVia"+tipoDir+" option:selected").val())){
							dataDir.tipoVia = getValUpperNormOpc(jQuery("#selectTipoVia"+tipoDir+" option:selected").val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.nombreVia) != getValUpperNorm(jQuery("#txtNombreVia"+tipoDir).val())){
							dataDir.nombreVia = getValUpperNorm(jQuery("#txtNombreVia"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.numeroPuerta) != getValUpperNorm(jQuery("#numeroPuerta"+tipoDir).val())){
							dataDir.numeroPuerta = getValUpperNorm(jQuery("#numeroPuerta"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.tipoInterior) != getValUpperNormOpc(jQuery("#selectTipoInterior"+tipoDir+" option:selected").val())){
							dataDir.tipoInterior = getValUpperNormOpc(jQuery("#selectTipoInterior"+tipoDir+" option:selected").val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.numeroInterior) != getValUpperNorm(jQuery("#txtNumeroInterior"+tipoDir).val())){
							dataDir.numeroInterior = getValUpperNorm(jQuery("#txtNumeroInterior"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.manzana) != getValUpperNorm(jQuery("#txtMza"+tipoDir).val())){
							dataDir.manzana = getValUpperNorm(jQuery("#txtMza"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.lote) != getValUpperNorm(jQuery("#txtLote"+tipoDir).val())){
							dataDir.lote = getValUpperNorm(jQuery("#txtLote"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.tipoZona) != getValUpperNormOpc(jQuery("#selectTipoZona"+tipoDir+" option:selected").val())){
							dataDir.tipoZona = getValUpperNormOpc(jQuery("#selectTipoZona"+tipoDir+" option:selected").val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.nombreZona) != getValUpperNorm(jQuery("#txtNombreZona"+tipoDir).val())){
							dataDir.nombreZona = getValUpperNorm(jQuery("#txtNombreZona"+tipoDir).val());
							flagDir = 1;
						}
						if(getValUpperNorm(dataDir.referencia) != getValUpperNorm(jQuery("#txtReferencia"+tipoDir).val())){
							dataDir.referencia = getValUpperNorm(jQuery("#txtReferencia"+tipoDir).val());
							flagDir = 1;
						}

						var arrayValUbiUp = getValUpperNormOpc(jQuery("#selectDistNor"+tipoDir+" option:selected").val()).split(";");
						var codUbiUp = '';
						if(arrayValUbiUp.length > 0){
							codUbiUp = arrayValUbiUp[0];
						}
						if(getValUpperNorm(memoryDir.codUbigeo) != codUbiUp){
							memoryDir.codUbigeo = codUbiUp;

                            memoryDir.textUbigeoDep=jQuery("#selectDepNor"+tipoDir+" option:selected").text();
							memoryDir.textUbigeoProv=jQuery("#selectProvNor"+tipoDir+" option:selected").text();
							memoryDir.textUbigeoDist=jQuery("#selectDistNor"+tipoDir+" option:selected").text();
							
							var valDep  = jQuery("#selectDepNor"+tipoDir+" option:selected").val();
							var valProv = jQuery("#selectProvNor"+tipoDir+" option:selected").val();
							var valDist = jQuery("#selectDistNor"+tipoDir+" option:selected").val();
							
							memoryDir.idPKDep =valDep.split(";")[1];
							memoryDir.idPKProv=valProv.split(";")[1];
							memoryDir.idPKDist=valDist.split(";")[1];
							
                                                        flagDir = 1;
						}

						if(flagDir == 1){
							memoryDir.estado='<%=Constante.ESTADO_DIR_NORMALIZADO_EDITADO%>';
							memoryDir.motivo='<%=Constante.MENSAJE_MOTIVO_NORMALIZADO_EDITADO%>';
						}

						var isFoundEquals = 0;
						if(listDirEqual.length > 0){
		            		var arrayEqualsNorm = finArrayInArrayNorm(listDirEqual, tipoDir);
		            		var mantenerUpdate = 0;
		            		if(arrayEqualsNorm.length > 1){
		            			isFoundEquals = 1;
		            			var countI = 0;
		            			for(d=0; d<arrayEqualsNorm.length; d++){
		            				var objEqualNorm = arrayEqualsNorm[d];
		            				if(objEqualNorm.oper1 == 'I'){
		            					countI++;
		            				}
		            			}

		            			if(countI == (arrayEqualsNorm.length - 1)){
		            				mantenerUpdate = 1;
		            			}else{
		            				var flag1 = 1;
		            				if(flagDir == 1){
			            				for(e=0; e<listDirEqual.length; e++){
			                				var arrayFlagN1 = listDirEqual[e];
			                				for(w=0; w<arrayFlagN1.length; w++){
			                					var objFlag1 = arrayFlagN1[w];
			                					if(objFlag1.tipo1 == tipoDir){
			                						objFlag1.oper1 = 'I';
			                						flag1 = 0;
			                						break;
			                					}
			                				}
			                				if(flag1 == 0){
			                					break;
			                				}
			                			}
		            				}
		            			}
		            		}

		            		if(isFoundEquals == 1 && flagDir == 1 && mantenerUpdate == 0){
		            			memoryDir.operacion='I';
		            		}
						}

						if(isFoundEquals == 0){
							var arrayDirEqual = memoryDir.tiposDirEquals.split(",");
			            	if(arrayDirEqual.length > 1){
			            		if(flagDir == 1){
									memoryDir.operacion='I';
			            		}
			            		var arrayEncontro = finArrayInArrayNorm(listDirEqual, tipoDir);
			            		if(arrayEncontro.length > 1){
			            			var flag2 = 1;
			            			for(e=0; e<listDirEqual.length; e++){
		                				var arrayFlagN2 = listDirEqual[e];
		                				for(w=0; w<arrayFlagN2.length; w++){
		                					var objFlag2 = arrayFlagN2[w];
		                					if(objFlag2.tipo == tipoDir){
		                						flag2 = 0;
		                						for(z=0; z<arrayDirEqual.length; z++){
		            	            				var subObjDir = new Object();
		            	            				subObjDir.tipo1 = arrayDirEqual[z];
		            	            				subObjDir.oper1 = memoryDir.operacion;
		            	            				arrayFlagN1.push(subObjDir);
		            		            		}
		                						break;
		                					}
		                				}
		                				if(flag2 == 0){
		                					break;
		                				}
		                			}
			            		}else{
			            			var subListDirEqual = [];
			            			for(z1=0; z1<arrayDirEqual.length; z1++){
										var subObjDir = new Object();
			            				subObjDir.tipo1 = arrayDirEqual[z1];

			            				for(z5=0; z5<cloneArrayMemoryDirNom.length; z5++){
			            					var memoryDirZ5 = cloneArrayMemoryDirNom[z5];
			            					var tipoDirZ5 = memoryDirZ5.tipo;
			            					if(subObjDir.tipo1 == tipoDirZ5){
			            						subObjDir.oper1 = memoryDirZ5.operacion;
			            						subObjDir.valOpDirNorm1 = memoryDirZ5.valueOpcionDirNorm;
			            						break;
			            					}

			            				}
			            				subListDirEqual.push(subObjDir);
				            		}

				            		if(subListDirEqual.length > 1){
			            				listDirEqual.push(subListDirEqual);
				            		}
			            		}
			            	}
						}
					}
				}

				for(k=0; k<cloneArrayMemoryDirNom.length; k++){
					var memoryDir = cloneArrayMemoryDirNom[k];
					var estadoFlag = memoryDir.estado;
					if(estadoFlag != '1'){
						if(listDirEqual.length > 0 && memoryDir.numOcurrencia > 1 && memoryDir.operacion != 'I'){
							var tipoDir = memoryDir.tipo;
		            		var arrayEqualsNorm = finArrayInArrayNorm(listDirEqual, tipoDir);
		            		if(arrayEqualsNorm.length > 1){
	            				var esEqualsOp = 0;
	            				var valOpDirNorm = memoryDir.valueOpcionDirNorm;
		            			for(d3=0; d3<arrayEqualsNorm.length; d3++){
		            				var obNorm = arrayEqualsNorm[d3];
		            				if(obNorm.tipo1 != tipoDir){
		            					if(obNorm.valOpDirNorm1 == valOpDirNorm){
			            					esEqualsOp = 1;
			            					break;
			            				}
		            				}
		            			}

		            			if(esEqualsOp == 0){
		            				var indexElem = 0;
		            				var objEqualNorm2;
			            			for(d1=0; d1<arrayEqualsNorm.length; d1++){
			            				indexElem ++;
			            				var obNorm = arrayEqualsNorm[d1];
			            				if(obNorm.tipo1 == tipoDir){
			            					objEqualNorm2 = obNorm;
			            					break;
			            				}
			            			}

	            					if(indexElem < arrayEqualsNorm.length){
	            						objEqualNorm2.oper1 = 'I';
		            					memoryDir.operacion = 'I';
		            				}else{
		            					var countI = 0;
				            			for(d2=0; d2<arrayEqualsNorm.length; d2++){
				            				var objEqualNorm = arrayEqualsNorm[d2];
				            				if(objEqualNorm.oper1 == 'I'){
				            					countI++;
				            				}
				            			}

				            			if(countI < (arrayEqualsNorm.length - 1)){
				            				objEqualNorm2.oper1 = 'I';
				            				memoryDir.operacion = 'I';
				            			}
		            				}
	            				}
		            		}
						}
					}
				}

				bubbleSortNorm(cloneArrayMemoryDirNom);

				var dataDirNor = new Object();
				dataDirNor.arrayDirNorm = cloneArrayMemoryDirNom;

				jQuery.ajax({
		            url: '<%=actionURL_NormalizarServlet%>',
		            data: {
		            	myaction: 'guardarDireccionesNorm',
				        idDirNorm: JSON.stringify(dataDirNor)
				    },
		            type: "POST",
		            dataType: 'json',
		            async: false
				}).done(function(data){
				    parent.opener.parent.mainFrame.redirectOrder('<%=idOrden%>');
                                   					parent.close();
				}).fail(function(jqXHR, textStatus, errorThrown){
					alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
					parent.opener.parent.mainFrame.redirectOrder('<%=idOrden%>');
                    parent.close();
		        });
			}			
	    }
		
	    function bubbleSortNorm(a){
	        var swapped;
	        do {
	            swapped = false;
	            for (var i=0; i < a.length-1; i++) {
	                if (a[i].operacion < a[i+1].operacion) {
	                    var temp = a[i];
	                    a[i] = a[i+1];
	                    a[i+1] = temp;
	                    swapped = true;
	                }
	            }
	        } while (swapped);
	    }
		
		function finArrayInArrayNorm(listDirEqual, tipoDir){
			var flag0 = 1;
			var arrayFlagN0 = [];
    		if(listDirEqual.length > 0){
    			for(e=0; e<listDirEqual.length; e++){
    				var arrayFlag0 = listDirEqual[e];
    				for(w=0; w<arrayFlag0.length; w++){
    					var objFlag0 = arrayFlag0[w];
    					if(objFlag0.tipo1 == tipoDir){
    						flag0 = 0;
    						break;
    					}
    				}
    				if(flag0 == 0){
    					arrayFlagN0 = arrayFlag0;
    					break;
    				}
    			}
    		}
    		
    		return arrayFlagN0;
		}
	</script>
</head>
<body bgcolor="#FBFBFB">
	<form name="frmNormalizacion" method="post" >
		<div id='tab-container'>
			<div id="idMainTab0" class="tab-content">
 				<h1 class="tab">LEGAL</h1>
				<table border="0">
					<tr>
						<td>
							<table border="0">
								<tr>
									<td align="right">Direccion: </td>
									<td colspan="5">
										<input type="text" id="txtNormalizar1" name="txtNormalizar1" size="80" disabled/>
										<input type="hidden" id="idDireccion1" name= "idDireccion1"/>
										<input type="hidden" id="idTipoDireccion1" name= "idTipoDireccion1"/>
										<input type="hidden" id="idCodTipoDireccion1" name= "idCodTipoDireccion1"/>
										<input type="hidden" id="referencia1" name= "referencia1"/>
									</td>
								</tr>
								<tr>
									<td>Departamento: </td>
									<td>
										<input type="text" id="txtDep1" name="txtDep1" size="20" disabled/>
										<input type="hidden" id="idCodDep1" name= "idCodDep1"/>
									</td>
									<td>Provincia: </td>
									<td>
										<input type="text" id="txtProv1" name="txtProv1" size="20" disabled/>
										<input type="hidden" id="idCodProv1" name= "idCodProv1"/>
									</td>
									<td>Distrito: </td>
									<td>
										<input type="text" id="txtDist1" name="txtDist1" size="20" disabled/>
										<input type="hidden" id="idCodDist1" name= "idCodDist1"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="button" id="btnNormalizar1" name="btnNormalizar1" value="Normalizar" onclick="cargarListDireccionNormalizada(1)" />
						</td>
					</tr>
				</table>
				<div id="idExpandDivTipoDir1" style="display:none">
					<hr width="100%" size="2" noshade />
					<table>			
						<tr>
							<td>
								<table border="0">
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Dir. Normalizadas: </td>
													<td colspan="4">
														<select name="selectDirNor1" id="selectDirNor1" style="width: 100%" onchange="changeDireccionNormalizacion(1)">
														</select>
													</td>
												</tr>
												<tr>
													<td colspan="4"><input type="hidden" id="idTiposDirEquals1" name= "idTiposDirEquals1"/></td>
													<td><input type="hidden" id="idNumOcurrencia1" name= "idNumOcurrencia1"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Via: </td>
													<td>
														<select name="selectTipoVia1" id="selectTipoVia1" style="width: 100%" onchange="changeTipoVia(1)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Via: </td>
													<td><input type="text" id="txtNombreVia1" name="txtNombreVia1" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Numero Puerta: </td>
													<td>
														<input type="text" id="numeroPuerta1" name="numeroPuerta1" size="30"/>
													</td>
													<td style="width: 30px"></td>
													<td></td>
													<td></td>
												</tr>
												<tr>
													<td align="right">Tipo Interior: </td>
													<td>
														<select name="selectTipoInterior1" id="selectTipoInterior1" style="width: 100%" onchange="changeTipoInterior(1)">
														</select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Numero Interior: </td>
													<td><input type="text" id="txtNumeroInterior1" name="txtNumeroInterior1" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Mza: </td>
													<td><input type="text" id="txtMza1" name="txtMza1" size="10"/></td>
													<td style="width: 30px"></td>
													<td align="right">Lote: </td>
													<td><input type="text" id="txtLote1" name="txtLote1" size="10"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Zona: </td>
													<td>
														<select name="selectTipoZona1" id="selectTipoZona1" style="width: 100%" onchange="changeTipoZona(1)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Zona: </td>
													<td><input type="text" id="txtNombreZona1" name="txtNombreZona1" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Referencia: </td>
													<td colspan="4"><input type="text" id="txtReferencia1" name="txtReferencia1" size="50"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Departamento: </td>
													<td><select name="selectDepNor1" id="selectDepNor1" onchange="changeDepNor(1)"></select></td>
													<td>Provincia: </td>
													<td><select name="selectProvNor1" id="selectProvNor1" onchange="changeProvNor(1)"></select></td>
													<td>Distrito: </td>
													<td><select name="selectDistNor1" id="selectDistNor1"></select></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="idMainTab1" class="tab-content">
				<h1 class="tab">FACTURACION</h1>
				<table border="0">
					<tr>
						<td>
							<table border="0">
								<tr>
									<td align="right">Direccion: </td>
									<td colspan="5">
										<input type="text" id="txtNormalizar2" name="txtNormalizar2" size="80" disabled/>
										<input type="hidden" id="idDireccion2" name= "idDireccion2"/>
										<input type="hidden" id="idTipoDireccion2" name= "idTipoDireccion2"/>
										<input type="hidden" id="idCodTipoDireccion2" name= "idCodTipoDireccion2"/>
										<input type="hidden" id="referencia2" name= "referencia2"/>
									</td>
								</tr>
								<tr>
									<td>Departamento: </td>
									<td>
										<input type="text" id="txtDep2" name="txtDep2" size="20" disabled/>
										<input type="hidden" id="idCodDep2" name= "idCodDep2"/>
									</td>
									<td>Provincia: </td>
									<td>
										<input type="text" id="txtProv2" name="txtProv2" size="20" disabled/>
										<input type="hidden" id="idCodProv2" name= "idCodProv2"/>
									</td>
									<td>Distrito: </td>
									<td>
										<input type="text" id="txtDist2" name="txtDist2" size="20" disabled/>
										<input type="hidden" id="idCodDist2" name= "idCodDist2"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="button" id="btnNormalizar2" name="btnNormalizar2" value="Normalizar" onclick="cargarListDireccionNormalizada(2)" />
						</td>
					</tr>
				</table>
				<div id="idExpandDivTipoDir2" style="display:none">
					<hr width="100%" size="2" noshade />
					<table>			
						<tr>
							<td>
								<table border="0">
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Dir. Normalizadas: </td>
													<td colspan="4">
														<select name="selectDirNor2" id="selectDirNor2" style="width: 100%" onchange="changeDireccionNormalizacion(2)">
														</select>
													</td>
												</tr>
												<tr>
													<td colspan="4"><input type="hidden" id="idTiposDirEquals2" name= "idTiposDirEquals2"/></td>
													<td><input type="hidden" id="idNumOcurrencia2" name= "idNumOcurrencia2"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Via: </td>
													<td>
														<select name="selectTipoVia2" id="selectTipoVia2" style="width: 100%" onchange="changeTipoVia(2)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Via: </td>
													<td><input type="text" id="txtNombreVia2" name="txtNombreVia2" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Numero Puerta: </td>
													<td>
														<input type="text" id="numeroPuerta2" name="numeroPuerta2" size="30"/>
													</td>
													<td style="width: 30px"></td>
													<td></td>
													<td></td>
												</tr>
												<tr>
													<td align="right">Tipo Interior: </td>
													<td>
														<select name="selectTipoInterior2" id="selectTipoInterior2" style="width: 100%" onchange="changeTipoInterior(2)">
														</select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Numero Interior: </td>
													<td><input type="text" id="txtNumeroInterior2" name="txtNumeroInterior2" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Mza: </td>
													<td><input type="text" id="txtMza2" name="txtMza2" size="10"/></td>
													<td style="width: 30px"></td>
													<td align="right">Lote: </td>
													<td><input type="text" id="txtLote2" name="txtLote2" size="10"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Zona: </td>
													<td>
														<select name="selectTipoZona2" id="selectTipoZona2" style="width: 100%" onchange="changeTipoZona(2)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Zona: </td>
													<td><input type="text" id="txtNombreZona2" name="txtNombreZona2" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Referencia: </td>
													<td colspan="4"><input type="text" id="txtReferencia2" name="txtReferencia2" size="50"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Departamento: </td>
													<td><select name="selectDepNor2" id="selectDepNor2" onchange="changeDepNor(2)"></select></td>
													<td>Provincia: </td>
													<td><select name="selectProvNor2" id="selectProvNor2" onchange="changeProvNor(2)"></select></td>
													<td>Distrito: </td>
													<td><select name="selectDistNor2" id="selectDistNor2" ></select></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="idMainTab2" class="tab-content">
				<h1 class="tab">ENTREGA</h1>
				<table border="0">
					<tr>
						<td>
							<table border="0">
								<tr>
									<td align="right">Direccion: </td>
									<td colspan="5">
										<input type="text" id="txtNormalizar3" name="txtNormalizar3" size="80" disabled/>
										<input type="hidden" id="idDireccion3" name= "idDireccion3"/>
										<input type="hidden" id="idTipoDireccion3" name= "idTipoDireccion3"/>
										<input type="hidden" id="idCodTipoDireccion3" name= "idCodTipoDireccion3"/>
										<input type="hidden" id="referencia3" name= "referencia3"/>
									</td>
								</tr>
								<tr>
									<td>Departamento: </td>
									<td>
										<input type="text" id="txtDep3" name="txtDep3" size="20" disabled/>
										<input type="hidden" id="idCodDep3" name= "idCodDep3"/>
									</td>
									<td>Provincia: </td>
									<td>
										<input type="text" id="txtProv3" name="txtProv3" size="20" disabled/>
										<input type="hidden" id="idCodProv3" name= "idCodProv3"/>
									</td>
									<td>Distrito: </td>
									<td>
										<input type="text" id="txtDist3" name="txtDist3" size="20" disabled/>
										<input type="hidden" id="idCodDist3" name= "idCodDist3"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="button" id="btnNormalizar3" name="btnNormalizar3" value="Normalizar" onclick="cargarListDireccionNormalizada(3)" />
						</td>
					</tr>
				</table>
				<div id="idExpandDivTipoDir3" style="display:none">
					<hr width="100%" size="2" noshade />
					<table>			
						<tr>
							<td>
								<table border="0">
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Dir. Normalizadas: </td>
													<td colspan="4">
														<select name="selectDirNor3" id="selectDirNor3" style="width: 100%" onchange="changeDireccionNormalizacion(3)">
														</select>
													</td>
												</tr>
												<tr>
													<td colspan="4"><input type="hidden" id="idTiposDirEquals3" name= "idTiposDirEquals3"/></td>
													<td><input type="hidden" id="idNumOcurrencia3" name= "idNumOcurrencia3"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Via: </td>
													<td>
														<select name="selectTipoVia3" id="selectTipoVia3" style="width: 100%" onchange="changeTipoVia(3)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Via: </td>
													<td><input type="text" id="txtNombreVia3" name="txtNombreVia3" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Numero Puerta: </td>
													<td>
														<input type="text" id="numeroPuerta3" name="numeroPuerta3" size="30"/>
													</td>
													<td style="width: 30px"></td>
													<td></td>
													<td></td>
												</tr>
												<tr>
													<td align="right">Tipo Interior: </td>
													<td>
														<select name="selectTipoInterior3" id="selectTipoInterior3" style="width: 100%" onchange="changeTipoInterior(3)">
														</select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Numero Interior: </td>
													<td><input type="text" id="txtNumeroInterior3" name="txtNumeroInterior3" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Mza: </td>
													<td><input type="text" id="txtMza3" name="txtMza3" size="10"/></td>
													<td style="width: 30px"></td>
													<td align="right">Lote: </td>
													<td><input type="text" id="txtLote3" name="txtLote3" size="10"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Zona: </td>
													<td>
														<select name="selectTipoZona3" id="selectTipoZona3" style="width: 100%" onchange="changeTipoZona(3)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Zona: </td>
													<td><input type="text" id="txtNombreZona3" name="txtNombreZona3" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Referencia: </td>
													<td colspan="4"><input type="text" id="txtReferencia3" name="txtReferencia3" size="50"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Departamento: </td>
													<td><select name="selectDepNor3" id="selectDepNor3" onchange="changeDepNor(3)"></select></td>
													<td>Provincia: </td>
													<td><select name="selectProvNor3" id="selectProvNor3" onchange="changeProvNor(3)"></select></td>
													<td>Distrito: </td>
													<td><select name="selectDistNor3" id="selectDistNor3" ></select></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="idMainTab3" class="tab-content">
				<h1 class="tab">COMUNICACION</h1>
				<table border="0">
					<tr>
						<td>
							<table border="0">
								<tr>
									<td align="right">Direccion: </td>
									<td colspan="5">
										<input type="text" id="txtNormalizar4" name="txtNormalizar4" size="80" disabled/>
										<input type="hidden" id="idDireccion4" name= "idDireccion4"/>
										<input type="hidden" id="idTipoDireccion4" name= "idTipoDireccion4"/>
										<input type="hidden" id="idCodTipoDireccion4" name= "idCodTipoDireccion4"/>
										<input type="hidden" id="referencia4" name= "referencia4"/>
									</td>
								</tr>
								<tr>
									<td>Departamento: </td>
									<td>
										<input type="text" id="txtDep4" name="txtDep4" size="20" disabled/>
										<input type="hidden" id="idCodDep4" name= "idCodDep4"/>
									</td>
									<td>Provincia: </td>
									<td>
										<input type="text" id="txtProv4" name="txtProv4" size="20" disabled/>
										<input type="hidden" id="idCodProv4" name= "idCodProv4"/>
									</td>
									<td>Distrito: </td>
									<td>
										<input type="text" id="txtDist4" name="txtDist4" size="20" disabled/>
										<input type="hidden" id="idCodDist4" name= "idCodDist4"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="button" id="btnNormalizar4" name="btnNormalizar4" value="Normalizar" onclick="cargarListDireccionNormalizada(4)" />
						</td>
					</tr>
				</table>
				<div id="idExpandDivTipoDir4" style="display:none">
					<hr width="100%" size="2" noshade />
					<table>			
						<tr>
							<td>
								<table border="0">
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Dir. Normalizadas: </td>
													<td colspan="4">
														<select name="selectDirNor4" id="selectDirNor4" style="width: 100%" onchange="changeDireccionNormalizacion(4)">
														</select>
													</td>
												</tr>
												<tr>
													<td colspan="4"><input type="hidden" id="idTiposDirEquals4" name= "idTiposDirEquals4"/></td>
													<td><input type="hidden" id="idNumOcurrencia4" name= "idNumOcurrencia4"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Via: </td>
													<td>
														<select name="selectTipoVia4" id="selectTipoVia4" style="width: 100%" onchange="changeTipoVia(4)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Via: </td>
													<td><input type="text" id="txtNombreVia4" name="txtNombreVia4" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Numero Puerta: </td>
													<td>
														<input type="text" id="numeroPuerta4" name="numeroPuerta4" size="30"/>
													</td>
													<td style="width: 30px"></td>
													<td></td>
													<td></td>
												</tr>
												<tr>
													<td align="right">Tipo Interior: </td>
													<td>
														<select name="selectTipoInterior4" id="selectTipoInterior4" style="width: 100%" onchange="changeTipoInterior(4)">
														</select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Numero Interior: </td>
													<td><input type="text" id="txtNumeroInterior4" name="txtNumeroInterior4" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Mza: </td>
													<td><input type="text" id="txtMza4" name="txtMza4" size="10"/></td>
													<td style="width: 30px"></td>
													<td align="right">Lote: </td>
													<td><input type="text" id="txtLote4" name="txtLote4" size="10"/></td>
												</tr>
												<tr>
													<td align="right">Tipo Zona: </td>
													<td>
														<select name="selectTipoZona4" id="selectTipoZona4" style="width: 100%" onchange="changeTipoZona(4)"></select>
													</td>
													<td style="width: 30px"></td>
													<td align="right">Nombre Zona: </td>
													<td><input type="text" id="txtNombreZona4" name="txtNombreZona4" size="30"/></td>
												</tr>
												<tr>
													<td align="right">Referencia: </td>
													<td colspan="4"><input type="text" id="txtReferencia4" name="txtReferencia4" size="50"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0">
												<tr>
													<td align="right">Departamento: </td>
													<td><select name="selectDepNor4" id="selectDepNor4" onchange="changeDepNor(4)"></select></td>
													<td>Provincia: </td>
													<td><select name="selectProvNor4" id="selectProvNor4" onchange="changeProvNor(4)"></select></td>
													<td>Distrito: </td>
													<td><select name="selectDistNor4" id="selectDistNor4" ></select></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div>
			<span>&nbsp;</span>
		</div>
		<div>
			<input type="button" id="btnGuardar" name="btnGuardar" value="Guardar" onclick="guardarNormalizacion()"/>
		</div>
	</form>
	<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/tabs.js"></script>
</body>
</html>

<% }catch(Exception ex){
    ex.printStackTrace();
%>
<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>
