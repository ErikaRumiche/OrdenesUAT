
<body bgcolor="#FBFBFB">

<h1 class="CellLabel">CENTRO POBLADO DE USO FRECUENTE(CPUF)</h1>

<table id="populateCenterList" width="100%" class="RegionBorder" style="text-align: left">
	<thead>
	<tr>
		<th>Departamento</th>
		<th>Provincia</th>
		<th>Distrito</th>
		<th>Centro Poblado</th>
		<th>Es mi domicilio?</th>
		<th>Tiene Cobertura?</th>
	</tr>
	</thead>
	<tbody></tbody>
	<br>
	<tfoot><tr><td colspan="7"><input type="checkbox" id="acceptterms" disabled="disabled">ACEPTO CONTRATAR EL SERVICIO A PESAR QUE ME HAN INFORMADO QUE MI CPUF NO CUENTA CON COBERTURA</td></tr></tfoot>
</table>

<script type="text/javascript">
    var populateCenter;
    var populateCenterDetList;
    $(document).ready(function () {
        populateCenter = new Object();
        populateCenter.populateCenterDetBeanList = [];
        fxGetPopulateCenter();
    });

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
                    populateCenter.populateCenterDetBeanList.id=i;
                    var homeAnswer = item.isHome == 1 ? 'SI' : 'NO';
                    var coverageAnswer = item.isCoverage == 1 ? 'SI' : 'NO';
                    var row = '<tr><td>' + item.department + '</td>';
                    row = row + '<td>' + item.province + '</td>';
                    row = row + '<td>' + item.district + '</td>';
                    row = row + '<td>' + item.cpufCode + '</td>';
                    row = row + '<td>' + homeAnswer + '</td>';
                    row = row + '<td>' + coverageAnswer+ '</td>';
                    jQuery('#populateCenterList > tbody').append(row);
                });
                if (populateCenter.acceptterms == 1) {
                    $("#acceptterms").prop('checked', true);
                }
                if(populateCenter.populateCenterDetBeanList.length==0){
                    var row = '<tr><td colspan="6" style="text-align: center;color: red"><span>No se encuentra información</span></td>';
                    jQuery('#populateCenterList > tbody').append(row);
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error interno: " + jqXHR.responseText + ". Informar a soporte");
        });



    }


</script>
</body>




