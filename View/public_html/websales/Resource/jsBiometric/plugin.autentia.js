/*
//JSON Comando esperado...
//   "paquete":[{"comando":"IniciarSesionLogin","param":"13468309-0"},
//              {"comando":"IniciarSesion","param":"13468309-0"},
//              {"comando":"ParamsInit","param":"Rut,mail"},
//				{"comando":"ParamsSet","param":[{"idx":1,"valor":"0011644656-1"},
//			                                  {"idx":2,"valor":"dupa"}]},
//            	{"comando":"Transaccion", "param":"../DCDA/getmail"			  
//			  	{"comando":"ParamsGet","param":2},
//			  	{"comando":"cerrarSesion"}]}
*/

var PublicCERT = "-----BEGIN CERTIFICATE----- \
MIIDtDCCApwCCQDyBCwa1s/2nzANBgkqhkiG9w0BAQsFADCBmzELMAkGA1UEBhMC \
Q0wxFDASBgNVBAgMC1Byb3ZpZGVuY2lhMREwDwYDVQQHDAhTYW50aWFnbzERMA8G \
A1UECgwIQXV0ZW50aWExEzARBgNVBAsMCklubm92YWNpb24xGDAWBgNVBAMMD011 \
bHRpYnJvd3NlciBKUzEhMB8GCSqGSIb3DQEJARYSc29wb3J0ZUBhY2VwdGEuY29t \
MB4XDTE1MDczMDIxMjYzOVoXDTE2MDcyOTIxMjYzOVowgZsxCzAJBgNVBAYTAkNM \
MRQwEgYDVQQIDAtQcm92aWRlbmNpYTERMA8GA1UEBwwIU2FudGlhZ28xETAPBgNV \
BAoMCEF1dGVudGlhMRMwEQYDVQQLDApJbm5vdmFjaW9uMRgwFgYDVQQDDA9NdWx0 \
aWJyb3dzZXIgSlMxITAfBgkqhkiG9w0BCQEWEnNvcG9ydGVAYWNlcHRhLmNvbTCC \
ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK02PVvUOkj5gSANZbsaCsq2 \
5PjSHPTzRbhLsnh4ILJhjsK+22mYSdI9eUD03kxakkYgRXbaaI5Vb/bLLnE92eVB \
ej9D6zOR8H53SXwrIEWtef7mCnQv2Y5l3GQjjk/+yNHs249AQaB3CdHCCuU1FUBo \
pBWm3dLiayBZD/J46KRkwMbA6/qifh2D9PC32fA93AsDC9kHYAoNv0Yeo/0D29PL \
N8M8Qvt1UeI7Zf/l9RhVvxVyIu7pUnYZIykm+TCtLuAdw01UkCUavFDJ96zsIztD \
W0wOpM+lGlWKqMd2LqfWl2iMrB3uHQEV/oFnFM47Ltv3eNabACUcRAInGhCyfXMC \
AwEAATANBgkqhkiG9w0BAQsFAAOCAQEAK2DnMXoPwt+9Ta24oIuUkcSgjUZ/fKiA \
CQ9B/tYS/Di6c5P8w3FW1j5JqWbp9HVxizxqqAKBkEovb9aYT35iSUwKN7ZBxPqC \
VPeNZJGR4tCMhYe4Tdvs007/Ag+YX3DrEbb9e2/DhYUU9eZVfUdyt11mHqspRRAS \
PQ4GK52u8tJ3fNXdm89Vli9J5RvI/Z6W+XfdYyVwSBSTrBjRpJtLcrrXt/nO1jMh \
RPBlKhHD0xmmh3ne+F1GCmcIJXwb8S1TaJer59hR+tpgxOSY6NwKmTPmGoMBLG4J \
oZmeq9HECQEuV3Kg0iq0e1wQAJH7fIZ7y9QXeopudP6eNz/uFwXE5Q== \
-----END CERTIFICATE----- \
";

function debugLog(msg){
	/*if (!isIE()) {
		window.console && console.log(msg);	
	}*/
}

function isIE() {
	var result = false;
	if (navigator.appName.indexOf("Internet Explorer")!=-1) {
		result = true;
	}
	return result;	
}

function procesarJSON(data, fn)
{
	var x
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  	x=new XMLHttpRequest();
	  	debugLog('LOAD:: XMLHttpRequest()');
	  }
	else
	  {// code for IE6, IE5
	  	x=new ActiveXObject("Microsoft.XMLHTTP");
	  	debugLog('LOAD:: ActiveXObject("Microsoft.XMLHTTP")');
	  }

	var string = JSON.stringify(data);

	x.open('POST','http://plugin.autentia.mb:7777/json-handler',true);
	x.setRequestHeader('Content-type','text/plain; charset=ISO8859_1');// LLamada CrossDomain Simple, de otra forma es un CrossDomain complejo, de dos llamadas al server.
	x.setRequestHeader("Content-length", string.length);
	x.setRequestHeader("Connection", "close");

	x.onreadystatechange = function(){
		if (x.readyState != 4) return;
		if (x.status != 200 && x.status != 304) {
			debugLog('HTTP error ' + x.status);
			alert('HTTP error :' + x.status + ' - Revise si servidor Multibrowser esta en ejecucion.');

			return;
		}

		data = JSON.parse(x.responseText);

		//var tmpSignature = JSON.stringify(data).replace(/"signature":"[^"]+"/, '"signature":""');
		var tmpSignature = x.responseText.replace(/"signature":"[^"]+"/, '"signature":""');
		
		var x509 = new X509();
		x509.readCertPEM(PublicCERT);
		var isValid = x509.subjectPublicKeyRSA.verifyString(tmpSignature, data.signature);
		
		//var calcSignature = CryptoJS.SHA1(tmpSignature);
		//var signature = calcSignature.toString().toUpperCase();}
		//if (data.signature == signature) {
		if (isValid) {	
			return fn(data);

		} else {
			alert('Firma de proceso seguro invalido');
			return fn('');
		}
		
	}
	x.send(string);
};

var transaccion={};
var objPaquete=[];
var prmSet=[];

function plgAutentiaJS()
{
	debugLog('Plugin Loaded!!');
};

function excResp(response)
{
	return response;
};

plgAutentiaJS.prototype.IniciarSesion = function(rut, excResp) {
	objPaquete.push({comando:"IniciarSesion",param:rut});
	var response;
	transaccion.paquete = objPaquete;
	/*
	Para su funcionamiento con cross domain con diferentes protocoloes, es decir llamado desde
	https hacia un http en Firefox esta opciòn esta bloqueada por defecto, pero se puede habilitar 
	en una opciòn que se despliega en la barra de dirección con forma de escudo. En Chrome, solo 
	muestra un warning en la consola del browser.
	*/
	response = procesarJSON(transaccion, function(response)
	{
		transaccion = {};
		objPaquete = [];
		prmSet = [];
		
		return excResp(response);	
	});		
};

plgAutentiaJS.prototype.IniciarSesionLogin = function(rut, excResp) 
{
	objPaquete.push({comando:"IniciarSesionLogin",param:rut});
	var response;
	transaccion.paquete = objPaquete;
	/*
	Para su funcionamiento con cross domain con diferentes protocoloes, es decir llamado desde
	https hacia un http en Firefox esta opciòn esta bloqueada por defecto, pero se puede habilitar 
	en una opciòn que se despliega en la barra de dirección con forma de escudo. En Chrome, solo 
	muestra un warning en la consola del browser.
	*/
	response = procesarJSON(transaccion, function(response)
	{
		transaccion = {};
		objPaquete = [];
		prmSet = [];
		
		return excResp(response);	
	});	
}

plgAutentiaJS.prototype.Transaccion2 = function(trxName, entrada, salida, excResp)
{
	var ParamInit = '';
	if (Object.keys(entrada).length>0){		
		var i = 0;
		for (var x in entrada) {
			ParamInit += x + ',';
			i += 1;
			prmSet.push({idx:i,valor:entrada[x]});
		}
	}

	if (Object.keys(salida).length>0) {
		for (var x in salida) {
			ParamInit += salida[x] + ',';
		}
		ParamInit = ParamInit.substring(0, ParamInit.length - 1);

		objPaquete.push({comando:"ParamsInit",param:ParamInit});
		if (prmSet.length > 0) {
			objPaquete.push({comando:"ParamsSet",param:prmSet});	
		}					
		objPaquete.push({comando:"Transaccion",param:trxName});		

		var array = ParamInit.split(',');
		for (var x in array) {
			for (var y in salida) {
				if (array[x] == salida[y]) {
					var idx = +x + 1; 
					objPaquete.push({comando:"ParamsGet",param:idx, paramName:salida[y]});
					break;
				}
			}			
		}

	}
	var response;
	transaccion.paquete = objPaquete;
	/*
	Para su funcionamiento con cross domain con diferentes protocoloes, es decir llamado desde
	https hacia un http en Firefox esta opciòn esta bloqueada por defecto, pero se puede habilitar 
	en una opciòn que se despliega en la barra de dirección con forma de escudo. En Chrome, solo 
	muestra un warning en la consola del browser.
	*/
	response = procesarJSON(transaccion, function(response)
	{
		transaccion = {};
		objPaquete = [];
		prmSet = [];
		
		return excResp(response);	
	});


}

plgAutentiaJS.prototype.CerrarSesion = function() 
{
	objPaquete.push({comando:"CerrarSesion", param:""});
	var response;
	transaccion.paquete = objPaquete;
	/*
	Para su funcionamiento con cross domain con diferentes protocoloes, es decir llamado desde
	https hacia un http en Firefox esta opciòn esta bloqueada por defecto, pero se puede habilitar 
	en una opciòn que se despliega en la barra de dirección con forma de escudo. En Chrome, solo 
	muestra un warning en la consola del browser.
	*/
	response = procesarJSON(transaccion, function(response)
	{
		transaccion = {};
		objPaquete = [];
		prmSet = [];
		
		return response;	
	});		
}