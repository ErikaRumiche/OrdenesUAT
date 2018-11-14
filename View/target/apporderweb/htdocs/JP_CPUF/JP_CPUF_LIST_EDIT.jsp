
<body bgcolor="#FBFBFB">

 				<h1 class="CellLabel">CENTRO POBLADO DE USO FRECUENTE(CPUF)</h1>
				<form id="cpufForm" method="post">
				<table border="0" id="populateCenter" class="RegionBorder" style="text-align: left" width="100%">
					<thead class="RegionHeaderColor">
						<tr>
							<th><span style="color: red">*</span>Departamento</th>
							<th><span style="color: red">*</span>Provincia</th>
							<th><span style="color: red">*</span>Distrito</th>
							<th><span style="color: red">*</span>Centro Poblado</th>
							<th><span style="color: red">*</span>Es mi domicilio?</th>
							<th><span style="color: red">*</span>Tiene Cobertura?</th>
						</tr>
					</thead>
					<tbody class="RegionBorder">
					<tr>
						<td><select name="selectDep" id="selectDep" onchange="changeDep()" required><option value="">-Seleccionar-</option></select></td>
						<td><select name="selectProv" id="selectProv" onchange="changeProv()" required><option value="">-Seleccionar-</option></select></td>
						<td><select name="selectDist" id="selectDist" required><option value="">-Seleccionar-</option></select></td>
						<td><input type="text" id="cpuf"  maxlength="50" required/></td>
						<td><input type="radio" name="home" value="1" required/> Si<input type="radio" name="home" value="0"> No</td>
						<td><input type="radio" name="coverage" value="1" required/> Si<input type="radio" name="coverage" value="0"> No</td>
					</tr>

					</tbody>
				</table>
					<br>
					<input type="submit" value="Agregar CPUF"/>
				</form>
				<table id="populateCenterList" width="100%" class="RegionBorder" style="text-align: left">
					<thead>
					<tr>
						<th>Departamento</th>
						<th>Provincia</th>
						<th>Distrito</th>
						<th>Centro Poblado</th>
						<th>Es mi domicilio?</th>
						<th>Tiene Cobertura?</th>
						<th>Accion</th>
					</tr>
					</thead>
					<tbody></tbody>
					<br>

				</table>
				<br>
				<div>
				<input type="checkbox" id="acceptterms">ACEPTO CONTRATAR EL SERVICIO A PESAR QUE ME HAN INFORMADO QUE MI CPUF NO CUENTA CON COBERTURA
				</div>
				<br>
		<div id="footer">
			<input type="button" id="btnGuardar" name="btnGuardar" value="Guardar" onclick="fxSavePopulateCenter()"/>
			<input type="button" id="btnCancelar" name="btnCancelar" value="Cancelar" onclick="fxCancel()"/>
		</div>
				<script type="text/javascript">
                    var populateCenter;
                    var populateCenterDetList;
                    var num;
                    $(document).ready(function () {
                        populateCenter=new Object();
                        populateCenter.populateCenterDetBeanList=[];
                        num=0;
                        cargarDepartamentos();
                        fxGetPopulateCenter();
                        $('form').h5Validate().formValidate;
                        $("#cpufForm").submit(function (e) {
                            e.preventDefault();
                            var result = $('#cpufForm').h5Validate('allValid'); // call the validator function here
                            if (result == true) {
                                addCPUF();
                                $('#cpufForm')[0].reset();

                            } else {
                                alert("Debe llenar todo los campos");
                            }
                        });
                    });
                    function cargarDepartamentos(){
                        jQuery.ajax({
                            url: '<%=actionURL_PopulateCenterServlet%>',
                            data: {
                                myaction:'getUbigeoList',
                                codProv:'00',
                                codDist:'00'
                            },
                            type: "POST",
                            dataType: 'json'
                        }).done(function(data){
                            var arrayDep = data.arrUbigeoList;
                            try {
                                jQuery("#selectDep").html('<option value="">--Seleccionar--</option>');
                                for(i=0; i<arrayDep.length; i++) {
                                    jQuery("#selectDep").append(jQuery("<option/>")
                                        .attr("value",arrayDep[i].departamento)
                                        .text(arrayDep[i].nombre));
                                }
                            }catch(err) {}

                        }).fail(function(jqXHR, textStatus, errorThrown){
                            alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
                        });
                    }


                    function changeDep(){
                        jQuery("#selectProv").html('<option value="">-Seleccionar-</option>');
                        jQuery("#selectDist").html('<option value="">-Seleccionar-</option>');
                        var codDep=jQuery("#selectDep").val();
                        if(codDep=='')return;
                        jQuery.ajax({
                            url: '<%=actionURL_PopulateCenterServlet%>',
                            data: {
                                myaction: 'getUbigeoList',
                                codDep: codDep,
                                codDist: '00'
                            },
                            type: "POST",
                            dataType: 'json'
                        }).done(function(data){
                            var arrayProv = data.arrUbigeoList;
                            for(var i=0; i < arrayProv.length; i++) {
                                $("#selectProv").append(jQuery("<option/>")
                                    .attr("value",arrayProv[i].provincia)
                                    .text(arrayProv[i].nombre));
                            }

                            var firstProv = arrayProv[0];

                        }).fail(function(jqXHR, textStatus, errorThrown){
                            alert("Error interno: "+jqXHR.responseText+". Informar a soporte");
                        });
                    }

                    function changeProv() {
                        jQuery("#selectDist").html('<option value="">-Seleccionar-</option>');
                        var codDep = jQuery("#selectDep").val();
                        var codProv = jQuery("#selectProv").val();
                        jQuery.ajax({
                            url: '<%=actionURL_PopulateCenterServlet%>',
                            data: {
                                myaction: 'getUbigeoList',
                                codDep: codDep,
                                codProv: codProv
                            },
                            type: "POST",
                            dataType: 'json'
                        }).done(function (data) {
                            var arrayDist = data.arrUbigeoList;
                            for (i = 0; i < arrayDist.length; i++) {
                                jQuery("#selectDist").append(jQuery("<option/>")
                                    .attr("value", arrayDist[i].distrito)
                                    .text(arrayDist[i].nombre));
                            }
                        }).fail(function (jqXHR, textStatus, errorThrown) {
                            alert("Error interno: " + jqXHR.responseText + ". Informar a soporte");
                        });
                    }
                    function addCPUF() {
                        if (num >= <%=Constante.MAX_CPUF%>) {
                            alert("Se pueden registar como máximo <%=Constante.MAX_CPUF%> centros poblados");
                            return
                        }
                        var populaCenterDet = new Object();
                        populaCenterDet.id = num;
                        populaCenterDet.ubigeo = $('#selectDep').val() + $('#selectProv').val() + $('#selectDist').val();
                        var depName = $('#selectDep option:selected').text();
                        var provName = $('#selectProv option:selected').text();
                        var distName = $('#selectDist option:selected').text();
                        var cpufCode = $('#cpuf').val();
                        populaCenterDet.cpufCode = cpufCode;
                        var flag = $('input[name=home]:checked').val();
                        var homeAnswer = flag == 1 ? 'SI' : 'NO';
                        populaCenterDet.isHome = flag;
                        flag = $('input[name=coverage]:checked').val();
                        populaCenterDet.isCoverage = flag;
                        var coverageAnswer = flag == 1 ? 'SI' : 'NO';
                        var row = '<tr><td>' + depName + '</td>';
                        row = row + '<td>' + provName + '</td>';
                        row = row + '<td>' + distName + '</td>';
                        row = row + '<td>' + cpufCode + '</td>';
                        row = row + '<td>' + homeAnswer + '</td>';
                        row = row + '<td>' + coverageAnswer + '</td>';
                        row = row + '<td><a onclick="javascript:deleteCPUF(this,'+num+');" shape=""><img width="15" height="20" alt="Eliminar" src="/websales/images/Eliminar.gif" border="no" style="cursor: pointer"></a></td></tr>';
                        jQuery('#populateCenterList > tbody').append(row);
                        populateCenter.populateCenterDetBeanList.push(populaCenterDet);
                        num++;
                    }

                    function deleteCPUF(input, i) {
                        num--;
                        $(input).parent().parent().remove();
                        populateCenter.populateCenterDetBeanList = $.grep(populateCenter.populateCenterDetBeanList, function (data, index) {
                            return data.id != i
                        });
                    }

                    function fxCancel() {

                        if (confirm("Esta ud. seguro que desea Cancelar?")) {
                            parent.close();

                        }
                    }

                    function fxGetPopulateCenter() {

                        jQuery.ajax({
                            url: '<%=actionURL_PopulateCenterServlet%>',
                            data: {
                                myaction: 'getPopulateCenterDetail',
                                orderId:<%=orderId%>
                            },
                            type: "POST",
                            dataType: 'json'
                        }).done(function (data) {
                            var populateCenterData = data.populateCenterBean || '';
                            if (populateCenterData!='') {
                                populateCenter = populateCenterData;
                                $.each(populateCenter.populateCenterDetBeanList, function (i, item) {
                                    populateCenter.populateCenterDetBeanList[i].id=i;
                                    var homeAnswer = item.isHome == 1 ? 'SI' : 'NO';
                                    var coverageAnswer = item.isCoverage == 1 ? 'SI' : 'NO';
                                    var row = '<tr><td>' + item.department + '</td>';
                                    row = row + '<td>' + item.province + '</td>';
                                    row = row + '<td>' + item.district + '</td>';
                                    row = row + '<td>' + item.cpufCode + '</td>';
                                    row = row + '<td>' + homeAnswer + '</td>';
                                    row = row + '<td>' + coverageAnswer+ '</td>';
                                    row = row + '<td><a onclick="javascript:deleteCPUF(this,'+i+');" shape=""><img width="15" height="20" alt="Eliminar" src="/websales/images/Eliminar.gif" border="no" style="cursor: pointer"></a></td></tr>';
                                    jQuery('#populateCenterList > tbody').append(row);
                                });
                                if (populateCenter.acceptterms == 1) {
                                    $("#acceptterms").prop('checked', true);
                                }
                                num = populateCenter.populateCenterDetBeanList.length ;
                            }
                        }).fail(function (jqXHR, textStatus, errorThrown) {
                            alert("Error interno: " + jqXHR.responseText + ". Informar a soporte");
                        });



                    }

                    function fxSavePopulateCenter() {

                        populateCenter.response = 2;
                        populateCenter.orderId =<%=orderId%>;
                        populateCenter.acceptterms = $('#acceptterms').is(":checked") ? 1 : 0;
                        if (populateCenter.populateCenterDetBeanList.length > 0) {
                            if (!validateTerms()) {
                                alert("Debe aceptar los terminos del servicio");
                                $('#acceptterms').focus();
                                return;
                            }

                            jQuery.ajax({
                                url: '<%=actionURL_PopulateCenterServlet%>',
                                data: {
                                    myaction: 'savePopulateCenter',
                                    data: JSON.stringify(populateCenter),
                                    userLogin: '<%=userLogin%>'
                                },
                                type: "POST",
                                dataType: 'json'
                            }).done(function (data) {
                                var message = data.strMessage||'';
                                if(message==''){
                                    parent.opener.fxUpdateResponse();
                                alert('Se guardo correctamente los datos');
                                }else{
                                    alert('Hubo un error al guardar la informacion');
								}
                                parent.close();
                            }).fail(function (jqXHR, textStatus, errorThrown) {
                                alert("Error interno: " + jqXHR.responseText + ". Informar a soporte");
                            });
                        }
                        else {
                            alert("No existen registros para guardar")
                        }

                    }

                    function validateTerms() {
                        var flag=true;
                        $.each(populateCenter.populateCenterDetBeanList,function (i,item) {
							if(item.isCoverage==0){
							    flag=false;
							    return false;
							}
                        });
                        if (!flag) {
                            flag = $("#acceptterms").is(":checked");
                        }
                        return flag;


                    }



				</script>
</body>


