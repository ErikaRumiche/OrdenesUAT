var ROW_COUNT = 8;
var COL_COUNT = 14;
function Damage(row, column, damageCode) {		
	this.row = row;
	this.column = column;
	this.damageCode = damageCode;
}

function Item(id, description) {
	this.id = id;
	this.description = description;
};

function init() {		
	var imageUrl = image1;		
	buildImageTable(imageUrl,"divImage");
	buildCaptionTable("divCaption");		
			
	//var damages = window.dialogArguments;
	
	loadDamages(damages, "divImage");
}

function init2() {		
	var imageUrl = image2;		
	buildImageTable(imageUrl,"divImage2");
	buildCaptionTable("divCaption2");		
			
	//var damages = window.dialogArguments;
	
	loadDamages(damages2, "divImage2");	
}
		
/*
 * Funciones para construir la tabla del mapa fisico del equipo. 
 */ 		

function buildImageTable(imageUrl, divImage) {			
	var table = document.createElement("table");
	table.id = "tblImage";
	table.className = "imageTable";
	table.style.backgroundImage = "url(" + imageUrl + ")";
        table.style.height="150px";
	
    for (var i = 1; i < ROW_COUNT+1; i++) {
    	var row = table.insertRow();
    	                                    
        for (var j = 1; j < COL_COUNT+1; j++) {
        	var cell = row.insertCell();
    		cell.className = "imageCell";
    	            		            		
    		/* 
			 * Crea el div.
			 * El div permite que se posicionen correctamente los controles en la celda.
			 */
    		
			var div = document.createElement("div");
			div.id = "div_"+divImage + i + "_" + j;
			div.className = "imageDiv";
			
			/* 
			 * Agrega los objetos a la fila.
			 */								        	             
            cell.appendChild(div);
        }                        
    }
    
    var div = document.getElementById(divImage);
    div.appendChild(table);
}

function buildCaptionTable(divCaption) {
	var table = document.createElement("table");
	table.id = "tblCaption";
	
	var row = table.insertRow();
			
	var cell = row.insertCell();
	cell.className = "captionTable";	
	
	var span = document.createElement("span");
	span.style.color='red';
	span.style.textDecoration='underline';
	
	var text = 'Estado F&iacute;sico';
	var textNode = document.createTextNode(text);
	span.appendChild(textNode);				
	cell.appendChild(span);
	var br = document.createElement("br");
	cell.appendChild(br);
	var br = document.createElement("br");
	cell.appendChild(br);
	
	var span = document.createElement("p");
	
  	for (var k = 0; k < damageTypes.length; k++) {						
		var text = damageTypes[k].id + ": "  + damageTypes[k].description;
		var textNode = document.createTextNode(text);		
		
		span.appendChild(textNode);		

		var br = document.createElement("br");
		span.appendChild(br);						
	}
	cell.appendChild(span);
	
	var div = document.getElementById(divCaption);
    div.appendChild(table);	            			
}

/*
 * Funciones para mostrar la información del mapa físico del equipo. 
 */ 
 
function loadDamages(damages, divImage) {
	if (damages == null) {
		return;
	}
	
	for (var i = 0; i < damages.length; i++) {
		var damage = damages[i];
		
		var id = "div_"+divImage + damage.row + "_" + damage.column;
		var divImg=document.getElementById(id);
		divImg.innerHTML=damage.damageCode;
 	}
}
     
function getImgCodeBar(data){
    var array_data = new Array();
    var array_str = new Array();
    var array_title = ["ORDEN DE REPARACI&Oacute;N","IMEI"];
    array_data=data.split(",");
    if(array_data.length>0)
    for (var i = 0; i < array_data.length; i++) {
            array_str[i]="<b>"+array_title[i]+"</b><img id='imagenBarCode"+i+"' src='data:image/png;base64,"+array_data[i]+"' title='"+array_title[i]+"' width='100%' height='100%' >";
    }
    return array_str;
}

function fillDamages(jsonObject, varDamage){
    $.each(jsonObject, function(posicion, objDamage){
        var row=objDamage.nprow;
        var column=objDamage.npcolumn;
        var code=objDamage.npdamagecode;
	varDamage[posicion] = new Damage(row, column, code);
       });       
}
     
function getCodeBarsFromServer(controllerRepair){
     $.ajax({
         type: "POST",  
         url: controllerRepair,
         data: "hdnMethod=processBarCode&codes="+codigo+"&ancho_img=150&alto_img=60&font=9",
         success: function(data){
            var array_data = getImgCodeBar(data);            	 
             $("#div_barcode1").html(array_data[0]);
             $("#div_barcode2").html(array_data[1]);
         },error: function(){
             $("#div_barcode").html("ERROR");
         }
     });    	 
 }