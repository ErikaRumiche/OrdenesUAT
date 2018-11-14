/*
###### VALIDACION DE PROPUESTAS ANTES DE GUARDAR.
*/

      function fxvalidaPlanes(){
         
         var vform= document.frmdatos;
         //1ra Validacion de planes
         //Se valida que las cantidades de cada plan
          var vform =  parent.mainFrame.frmdatos;
          var vtmcode_act;
          var vcant_act = 0;
          var vtmcode_cam;
          var vcant_cam = 0;
          var vactretail;
          
          var vtmcode_equi;
          var vtmcode_equiCamDes;
          
          var vcant_equi = 0;
          var vcant_equiCamDes = 0;
          
          var vtmcode_pro;
          var vcant_pro = 0;
          var vfound=0;
          var vfoundCamDes=0;
          
          var vcodepropA;
          var vcodepropB;
          //variable para acumular la suma de equipos bolsa en la seccion cambio de plan.
          var vcant_camBolsa = 0;

         // ********** Valida que se hayan ingresado datos en plan bolsa (en el caso que exista bolsa) 
         if( vform.cmbTypeEvaluation.value == 2 || vform.cmbTypeEvaluation.value == 3 ){
            if( vform.cmbpplanes.value == 0 || vform.txtpcantequipos.value == "" || vform.txtpminucontract.value =="" || vform.txtpminutinterconteleftot.value =="" || vform.txtpprecioxmin.value=="" ) {
               alert("Debe ingresar todos lo valores de la seccion bolsa");
               return false;
            }
         }
          
         // ***** Valida que no exista mas de un mismo plan en la situacion propuesta
                  
         var atablepro   = parent.mainFrame.document.getElementById("proptabplanes");
         var mTotRowpro = atablepro.rows.length-6;
         if (mTotRowpro>0){
            for(i=0;i<=(mTotRowpro);i++){                              
               vcodepropA = vform.cmbpplanesori[i].value;
               for   ( j=0;j<=(mTotRowpro);j++ )   {
                  vcodepropB = vform.cmbpplanesori[j].value;
                  if( vcodepropA == vcodepropB && j!=i ){
                     alert( "El plan " + vform.cmbpplanesori[j].options[vform.cmbpplanesori[j].selectedIndex].text+  " esta presente en más de una ocasión en la situación propuesta." );
                     return false;
                  }
               }
            }
         }
         
         var atableequip   = parent.mainFrame.document.getElementById("proptabplanesequip");
         var mTotRowequip = atableequip.rows.length-5;
         var vcodeequiA;
         var vcodeequiB;
         if (mTotRowequip>0){
            for(k=0;k<=(mTotRowequip);k++){               
               vcodeequiA  = vform.cmbpplanesequipnew[k].value;
               for(l=0;l<=(mTotRowequip);l++){
                  vcodeequiB  = vform.cmbpplanesequipnew[l].value;
                  if ( vcodeequiA == vcodeequiB && l!=k){
                     alert( "El plan " + vform.cmbpplanesequipnew[l].options[vform.cmbpplanesequipnew[l].selectedIndex].text+  " esta presente en más de una ocasión en unidades nuevas." );
                     return false;
                  }
               }
            }
         }

         // *************************** PRIMERA Y SEGUNDA VALIDACION *****************************        
          var tableact    = parent.mainFrame.document.getElementById("actualtabplanes");
          var nTotRowact  = tableact.rows.length-6;
          
          
          
          if (nTotRowact==0){
                //Obtiene los valores del plan actual          
                vtmcode_act  = vform.hdn_vtmcode.value;
                vcant_act    = Number(vform.hdn_vcant.value);
                vactretail   = vform.hdn_act_retail.value;
                //Buscando el plan en Cambio de Plan
                var tablecamb   = parent.mainFrame.document.getElementById("proptabcambplanes");
                
                var nTotRowcamb = tablecamb.rows.length-5;
                if (nTotRowcamb==0){
                   vtmcode_cam = vform.cmbpplanesoricamb.value
                   if (vtmcode_act==vtmcode_cam){
                      vcant_cam   = Number(vform.hdn_wcamvcant[0].value);
                      if (vcant_act<vcant_cam){
                           alert("El número de unidades del Plan "+ vform.hdn_vdes.value +" en cambio de plan no puede ser mayor a " + vcant_act );
                           return false;
                      }
                   }
                   //comparamos con el plan bolsa inclusive.
                   if ( (vform.cmbTypeEvaluation.value == 3 || vform.cmbTypeEvaluation.value == 4) && vform.txtidplanbolsa.value == vtmcode_cam ){
                      vcant_cam   = Number(vform.hdn_wcamvcant[0].value);
                      if ( vform.txtcantequipos.value < vcant_cam){
                           alert("El número de unidades del Plan "+ vform.txtplanes.value +" en cambio de plan no puede ser mayor a " + vform.txtcantequipos.value );
                           return false;
                      }
                   }
                   
                }else{                  
                   vcant_cam = 0;
                   for(j=0;j<=(nTotRowcamb);j++){
                      vtmcode_cam  = vform.cmbpplanesoricamb[j].value;
                      if (vtmcode_act==vtmcode_cam){
                         vcant_cam    = vcant_cam + Number(vform.hdn_wcamvcant[j].value);
                      }
                   }

                   if (vcant_act<vcant_cam){
                      alert("El número de unidades del Plan "+ vform.hdn_vdes.value +" en cambio de plan no puede ser mayor a " + vcant_act );
                      return false;
                   }
                }
                
                if (vactretail=="RETAIL"){
                  if (vcant_act!=vcant_cam){
                     alert("Se debe de hacer el cambio del total de equipos de PLANES RETAIL");
                     return false;
                  }
                }
          }else{ 
           
            if (nTotRowact>0){          
                for(j=0;j<=(nTotRowact);j++){  
                   vcant_cam=0;
                   vtmcode_act  = vform.hdn_vtmcode[j].value;
                   vcant_act    = Number(vform.hdn_vcant[j].value);                   
                   vactretail   = vform.hdn_act_retail[j].value;
                   //Buscando el plan en Cambio de Plan
                   var tablecamb   = parent.mainFrame.document.getElementById("proptabcambplanes");  

                   var nTotRowcamb = tablecamb.rows.length-5;
                   if (nTotRowcamb==0){
                                       
                      vtmcode_cam = vform.cmbpplanesoricamb.value
                      if (vtmcode_act==vtmcode_cam){
                         vcant_cam   = Number(vform.hdn_wcamvcant[0].value);
                         if (vcant_act<vcant_cam){
                           alert("El número de unidades del Plan "+ vform.hdn_vdes[j].value +" en cambio de plan no puede ser mayor a " + vcant_act );                           
                           return false;
                         }
                      }
                   }else{
					  vcant_cam = 0;
                      for(k=0;k<=(nTotRowcamb);k++){
                         vtmcode_cam  = vform.cmbpplanesoricamb[k].value;
                         //alert("comparando vtmcode_cam:"+vtmcode_cam+" cantidad:"+vform.hdn_wcamvcant[k].value);
                         if (vtmcode_act==vtmcode_cam){
                           vcant_cam = vcant_cam + Number(vform.hdn_wcamvcant[k].value);
                         }
                      }
	                  if (vcant_act<vcant_cam){
	                     alert("El número de unidades del Plan "+ vform.hdn_vdes[j].value +" en cambio de plan no puede ser mayor a " + vcant_act );
	                     return false;
	                  }
                   }
                   //2da validacion
                   if (vactretail=="RETAIL"){
                     if (vcant_act!=vcant_cam){
                        alert("Se debe de hacer el cambio del total de equipos de PLANES RETAIL");
                        return false;
                     }
                   }
                }
            }
          }
          //validacion para bolsa 
          if ( (vform.cmbTypeEvaluation.value == 3 || vform.cmbTypeEvaluation.value == 4) ){

                //Buscando el plan en Cambio de Plan
                var tablecamb   = parent.mainFrame.document.getElementById("proptabcambplanes");
                
                var nTotRowcamb = tablecamb.rows.length-5;
                if (nTotRowcamb==0){
                   vtmcode_cam = vform.cmbpplanesoricamb.value                   
                   if ( vform.txtidplanbolsa.value == vtmcode_cam ){
                      vcant_cam   = Number(vform.hdn_wcamvcant[0].value);
                      if ( vform.txtcantequipos.value < vcant_cam){
                           alert("El número de unidades del Plan "+ vform.txtplanes.value +" en cambio de plan no puede ser mayor a " + vform.txtcantequipos.value );
                           return false;
                      }
                   }
                   
                }else{
                   vcant_cam = 0 ;
                   for(j=0;j<=(nTotRowcamb);j++){
                      vtmcode_cam  = vform.cmbpplanesoricamb[j].value;
                      if ( vform.txtidplanbolsa.value == vtmcode_cam ) {
                         vcant_cam    = vcant_cam + Number(vform.hdn_wcamvcant[j].value);
                      }
                   }
				   if ( vform.txtcantequipos.value < vcant_cam ){
                      alert("El número de unidades del Plan "+ vform.txtplanes.value +" en cambio de plan no puede ser mayor a " + vform.txtcantequipos.value );
                      return false;
                   }
                }
               
          }
          
          
          /***************** TERCERA VALIDACION ***/
          var tableequip   = parent.mainFrame.document.getElementById("proptabplanesequip");
          var nTotRowequip = tableequip.rows.length-5;           
          var typeClient = vform.txtCompanyType.value;   
          var tablepro   = parent.mainFrame.document.getElementById("proptabplanes");
          var nTotRowpro = tablepro.rows.length-6;       
          
          if (nTotRowequip==0){
                //Obtiene los valores de la seccion de venta
                vtmcode_equi  = vform.cmbpplanesequipnew.value;
                var equipDesc = vform.cmbpplanesequipnew.options[vform.cmbpplanesequipnew.selectedIndex].text ;
                vcant_equi    = Number(vform.hdn_wvcantequip[nTotRowequip+1].value);
                vfound        = 0;
                if (vcant_equi==0){
                  alert("Cantidad de equipos venta no puede ser cero, verifique");
                  return false;
                }
                var tablepro   = parent.mainFrame.document.getElementById("proptabplanes");
                var nTotRowpro = tablepro.rows.length-6;
                if (nTotRowpro==0){
                   vtmcode_pro = vform.cmbpplanesori.value;
                   if (vtmcode_pro==vtmcode_equi){
                     vfound=1;  
                   }
                }else{
                   if (nTotRowpro>0){  
                      for(j=0;j<=(nTotRowpro);j++){           
                         vtmcode_pro  = vform.cmbpplanesori[j].value;
                         //si es cliente prospect entonces todos los planes deben estar en la seccion nuevos.
                         if ( typeClient == "Prospect" && vtmcode_pro != vtmcode_equi ){
                           alert("todos los planes de de la seccion propuesta deben existir en los planes venta");
                           return false;
                         }
                         if (vtmcode_pro==vtmcode_equi){
                           vfound=1;
                         }
                      }      
                     }
                 }
                //Si tiene una bolsa propuesta entonces verficamos.
                if( vform.cmbTypeEvaluation.value == 2 || vform.cmbTypeEvaluation.value == 3 ) {
                    //si es cliente prospect entonces todos los planes deben estar en la seccion nuevos.
                    if ( typeClient == "Prospect" && Number(vform.cmbpplanes.value) != vtmcode_equi ){
                        alert("El plan de la sección bolsa propuesta debe existir en los planes venta");
                        return false;
                    }
                    if( vtmcode_equi == Number(vform.cmbpplanes.value) ){
                        vfound=1;
                    }
                }
                if (vfound==0){
                  alert("El plan " + equipDesc + " de ventas no pertenece a la seccion propuesta");
                  return false;                
                }
          }else{
            if (nTotRowequip>0){
                for(k=0;k<=(nTotRowequip);k++){   
                   //Obtiene los valores de la seccion de venta                   
                   vtmcode_equi  = vform.cmbpplanesequipnew[k].value;
                   vcant_equi    = Number(vform.hdn_wvcantequip[k].value);
                   var equipDesc = vform.cmbpplanesequipnew[k].options[vform.cmbpplanesequipnew[k].selectedIndex].text ;
                   vfound        = 0;
                   if (vcant_equi==0){
                     alert("Cantidad de equipos venta no puede ser cero, verifique");
                     return false;
                   }
                   var tablepro   = parent.mainFrame.document.getElementById("proptabplanes");
                   var nTotRowpro = tablepro.rows.length-6;  
                   if (nTotRowpro==0){
                      vtmcode_pro = vform.cmbpplanesori.value;
                      if (vtmcode_pro==vtmcode_equi){
                        vfound=1;  
                      }
                   }else{
                      if (nTotRowpro>0){  
                         for(j=0;j<=(nTotRowpro);j++){           
                            vtmcode_pro  = vform.cmbpplanesori[j].value;                            
                            if (vtmcode_pro==vtmcode_equi){
                              vfound=1;
                            }
                         }
                       } 
                    }
                    //Si tiene una bolsa propuesta entonces verficamos.
                    if( vform.cmbTypeEvaluation.value == 2 || vform.cmbTypeEvaluation.value == 3 ) {                        
                        if( vtmcode_equi == Number(vform.cmbpplanes.value)){
                           vfound=1;
                        }
                    }
                    if (vfound==0){
                       alert("El plan " + equipDesc + " de ventas no pertenece a la seccion propuesta");
                       return false;
                    }
                 }
            }
          }
                    
          // VALIDACION CLIENTE PROSPECTO 
          if ( typeClient == "Prospect" ){
                   var foundProTabPlan = 0;                                      
                      if (nTotRowpro >= 0){  
                         for(j=0;j<=(nTotRowpro);j++){                          
                            foundProTabPlan=0;         
                            
                            if(nTotRowpro == 0){
                               vtmcode_pro  = vform.cmbpplanesori.value;
                            }
                            else {
                               vtmcode_pro  = vform.cmbpplanesori[j].value;
                            }
                            
                            if (nTotRowequip>=0){
                              for( m=0; m<=( nTotRowequip ); m++ ){
                                 if( nTotRowequip == 0 ){
                                    vtmcode_equi  = vform.cmbpplanesequipnew.value;
                                 }
                                 else {
                                    vtmcode_equi  = vform.cmbpplanesequipnew[m].value;
                                 }
                                 if ( vtmcode_pro == vtmcode_equi ) {
                                    foundProTabPlan = 1;
                                 }
                              }
                            }
                            if ( foundProTabPlan == 0 ) {
                              alert("todos los planes propuestos deben estar en planes de venta");
                              return false;
                            }
                         }
                      } 
                    
                    //Si tiene una bolsa propuesta entonces verficamos.
                    if( vform.cmbTypeEvaluation.value == 2 || vform.cmbTypeEvaluation.value == 3 ) {                        
                        if (nTotRowequip>0){
                           for( n=0; n<=( nTotRowequip ); n++ ) {
                              vtmcode_equi  = vform.cmbpplanesequipnew[n].value;
                              if ( Number(vform.cmbpplanes.value) == vtmcode_equi ) {
                                 foundProTabPlan = 1;
                              }
                           }
                        }
                        if (nTotRowequip == 0){
                              vtmcode_equi  = vform.cmbpplanesequipnew.value;
                              if ( Number(vform.cmbpplanes.value) == vtmcode_equi ) {
                                 foundProTabPlan = 1;
                              }
                        }
                        if ( foundProTabPlan == 0 ) {
                              alert("El plan de la sección bolsa propuesta debe existir en planes de venta");
                              return false;
                        }
                    }
                    
          }
         
         // *************************** CUARTA VALIDACION *******************************************    
         // Se valida que todos los item de la sección de cambio de plan destino se encuentren en la seccion de planes
          var tableequip1   = parent.mainFrame.document.getElementById("proptabcambplanes");
          var nTotRowequipCamDes = tableequip1.rows.length-5; 
          if (nTotRowequipCamDes==0){
                //Obtiene los valores de la seccion de cambio de plan
                vtmcode_equiCamDes  = vform.cmbpplanesdes.value;
                var camDesLab = vform.cmbpplanesdes.options[vform.cmbpplanesdes.selectedIndex].text;

                //verificamos si el plan de origen y el de destino no son iguales 
                if ( vform.cmbpplanesoricamb.value == vtmcode_equiCamDes ) {
                  alert( "El plan origen y destino: " + camDesLab + " no deben ser iguales."  );
                  return false;
                }

                //var camDesLab = vform.cmbpplanesdes.label;
                vfoundCamDes        = 0;
                vcant_equiCamDes    = Number(vform.hdn_wcamvcant[nTotRowequipCamDes+1].value);
                
                var tablepro   = parent.mainFrame.document.getElementById("proptabplanes");
                var nTotRowpro = tablepro.rows.length-6;  
                
                var tableact   = parent.mainFrame.document.getElementById("proptabplanes");
                var nTotRowpro = tablepro.rows.length-6;
                
                if (nTotRowpro==0){
                   vtmcode_pro = vform.cmbpplanesori.value;
                   if (vtmcode_pro==vtmcode_equiCamDes){
                     vfound=1;  
                   }
                }else{
                   if (nTotRowpro>0){  
                      for(j=0;j<=(nTotRowpro);j++){           
                         vtmcode_pro  = vform.cmbpplanesori[j].value;
                         if (vtmcode_pro==vtmcode_equiCamDes){
                           vfound=1;
                         }
                      }   
                         
                     } 
                 }
                if( Number(vform.cmbpplanes.value) == Number (vtmcode_equiCamDes) ){
                     vfound=1;
                }
                if (vfound==0){
                  alert("El plan destino "+ camDesLab +" no pertenece a la situación Propuesta");
                  return false;                
                }
          }else{
            if (nTotRowequipCamDes>0){
                for(k=0;k<=(nTotRowequipCamDes);k++){   
                
                   //Obtiene los valores de la seccion de cambio de plan
                   vtmcode_equiCamDes  = vform.cmbpplanesdes[k].value;
                   
                   var camDesLab = vform.cmbpplanesdes[k].options[vform.cmbpplanesdes[k].selectedIndex].text ;                   
                   
                   //verificamos si el plan de origen y el de destino no son iguales 
                   if ( vform.cmbpplanesoricamb[k].value == vtmcode_equiCamDes ) {
                     alert( "El plan origen y destino: " + camDesLab + " no deben ser iguales."  );
                     return false;
                   }
                   
                   vfoundCamDes        = 0;
                   vfound = 0;

                   var tablepro   = parent.mainFrame.document.getElementById("proptabplanes");
                   var nTotRowpro = tablepro.rows.length-6;
                   if (nTotRowpro==0){
                      vtmcode_pro = vform.cmbpplanesori.value;
                      if (vtmcode_pro==vtmcode_equiCamDes){
                        vfound=1;  
                      }
                   }else{
                      if (nTotRowpro>0){  
                         for(j=0;j<=(nTotRowpro);j++){           
                            vtmcode_pro  = vform.cmbpplanesori[j].value;
                            if (vtmcode_pro==vtmcode_equiCamDes){
                              vfound=1;
                            }
                         }      
                       } 
                    }
                    if( Number(vform.cmbpplanes.value) == Number (vtmcode_equiCamDes) ){
                      vfound=1;
                    }
                    if (vfound==0){
                       alert("El plan destino "+ camDesLab +" no pertenece a la situación Propuesta");
                       return false;
                    }
                 }
            }  
          }
         
         // **************************** SEXTA VALIDACION ************** //         
         var cantidadPlanPropuesto = 0;
         var cantidadPlanActualBolsaNuevo = 0;
         var cantidadPlanBolsaNuevo = 0;
         var cantidadPlanActual = 0;
         
         if (nTotRowpro >= 0){
            for(j=0;j<=(nTotRowpro);j++){
               cantidadPlanActual = 0;
               cantidadPlanActualBolsaNuevo=0;
               cantidadPlanPropuesto = 0;
               cantidadPlanBolsaNuevo =0;
               
               if( nTotRowpro == 0 ) {
                  vtmcode_pro = vform.cmbpplanesori.value;
                  var description = vform.cmbpplanesori.options[vform.cmbpplanesori.selectedIndex].text ;
               }
               else {
                  vtmcode_pro = vform.cmbpplanesori[j].value;
                  var description = vform.cmbpplanesori[j].options[vform.cmbpplanesori[j].selectedIndex].text ;
               }
               
               cantidadPlanPropuesto = Number( vform.hdn_wvcant[j].value );
               cantidadPlanActualBolsaNuevo = 0;
               
               // buscamos el plan en situacion actual
               if( nTotRowact == 0 ){
                  if ( Number(vform.hdn_vtmcode.value) == vtmcode_pro ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_vcant.value );
                     cantidadPlanActual = cantidadPlanActual +  Number ( vform.hdn_vcant.value );
                  }
               }
               if( nTotRowact > 0 ){
                  for ( i=0; i <= (nTotRowact); i++ ) {
                     if ( Number(vform.hdn_vtmcode[i].value) == vtmcode_pro ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_vcant[i].value );
                        cantidadPlanActual = cantidadPlanActual + Number ( vform.hdn_vcant[i].value );
                     }
                  }
               }
                              
               // buscamos el plan cambio de planes               
               if( nTotRowequipCamDes == 0 ){
                  if ( Number(vform.cmbpplanesoricamb.value) == vtmcode_pro ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo - Number ( vform.hdn_wcamvcant[0].value );
                  }
                  if ( Number(vform.cmbpplanesdes.value) == vtmcode_pro ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wcamvcant[0].value );                     
                  }
               }
               if( nTotRowequipCamDes > 0 ){
                  for ( i=0; i <= (nTotRowequipCamDes); i++ ) {
                     if ( Number(vform.cmbpplanesoricamb[i].value) == vtmcode_pro ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo - Number ( vform.hdn_wcamvcant[i].value );
                     }
                     if ( Number(vform.cmbpplanesdes[i].value) == vtmcode_pro ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wcamvcant[i].value );
                     }
                  }
               }
                              
               // buscamos el plan en nuevos planes
               if( nTotRowequip == 0 ){
                  if ( Number(vform.cmbpplanesequipnew.value) == vtmcode_pro ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wvcantequip[0].value );
                  }
               }
               if( nTotRowequip > 0 ){
                  for(i=0; i<=nTotRowequip; i++ ){
                     if ( Number(vform.cmbpplanesequipnew[i].value) == vtmcode_pro ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wvcantequip[i].value );
                     }
                  }
               }
               //fin
               cantidadPlanBolsaNuevo = cantidadPlanActualBolsaNuevo - cantidadPlanActual;
               //alert( "evaluamos:" + description + " cantidadPlanBolsaNuevo:" + cantidadPlanBolsaNuevo + " cantidadPlanActualBolsaNuevo:"+cantidadPlanActualBolsaNuevo+"  cantidadPlanActual:"+cantidadPlanActual );
                              
               //evaluamos todas las sumas
               if ( cantidadPlanPropuesto > cantidadPlanActualBolsaNuevo  ){
                  alert( "La cantidad de equipos del plan " + description + " no debe ser mayor que la suma de equipos de la situación actual, cambio de plan y ventas." );
                  return false;
               }
               //evaluamos todas las sumas
               if ( cantidadPlanPropuesto < cantidadPlanBolsaNuevo ){
                  alert( "Las unidades del plan " + description + " de la situación propuesta son menores a las unidades ingresadas en las secciones de cambio de plan y unidades adicionales. Porfavor verifique" );
                  return false;
               }
            }            
         }
         
         //VALIDACION BOLSA VS BOLSA ACTUAL + CAMBIO PLAN + VENTAS.
         if( vform.cmbTypeEvaluation.value == 2 || vform.cmbTypeEvaluation.value == 3 ) { 
               
               var codBolsaProp = vform.cmbpplanes.value;
               var descriptionBolsa = vform.cmbpplanes.options[vform.cmbpplanes.selectedIndex].text ;
               
               cantidadPlanPropuesto = Number( vform.txtpcantequipos.value );
               cantidadPlanActualBolsaNuevo = 0;
               cantidadPlanBolsaNuevo = 0;
               cantidadPlanActual = 0;
               
               if( vform.cmbTypeEvaluation.value == 3 || vform.cmbTypeEvaluation.value == 4 ){
                  if( Number(vform.txtidplanbolsa.value) == Number(codBolsaProp) ) {
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number( vform.txtcantequipos.value );
                     cantidadPlanActual = cantidadPlanActual + Number( vform.txtcantequipos.value );
                  }
               }
               
               // buscamos el plan cambio de planes 
               if( nTotRowequipCamDes == 0 ){
                  if ( Number(vform.cmbpplanesoricamb.value) == codBolsaProp ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo - Number ( vform.hdn_wcamvcant[0].value );
                  }
                  if ( Number(vform.cmbpplanesdes.value) == codBolsaProp ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wcamvcant[0].value );
                  }
               }
               if( nTotRowequipCamDes > 0 ){
                  for ( i=0; i <= (nTotRowequipCamDes); i++ ) {
                     if ( Number(vform.cmbpplanesoricamb[i].value) == codBolsaProp ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo - Number ( vform.hdn_wcamvcant[i].value );
                     }
                     if ( Number(vform.cmbpplanesdes[i].value) == codBolsaProp ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wcamvcant[i].value );
                     }
                  }
               }
                              
               // buscamos el plan en nuevos planes
               if( nTotRowequip == 0 ){
                  if ( Number(vform.cmbpplanesequipnew.value) == codBolsaProp ){
                     cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wvcantequip[0].value );
                  }
               }
               if( nTotRowequip > 0 ){
                  for(i=0; i<=nTotRowequip; i++ ){
                     if ( Number(vform.cmbpplanesequipnew[i].value) == codBolsaProp ){
                        cantidadPlanActualBolsaNuevo = cantidadPlanActualBolsaNuevo + Number ( vform.hdn_wvcantequip[i].value );
                     }
                  }
               }    
               //fin 
               cantidadPlanBolsaNuevo = cantidadPlanActualBolsaNuevo - cantidadPlanActual;
               //evaluamos todas las sumas
               if ( cantidadPlanPropuesto > cantidadPlanActualBolsaNuevo  ){
                  if( (vform.cmbTypeEvaluation.value == 3 || vform.cmbTypeEvaluation.value == 4) && Number(vform.txtidplanbolsa.value) != Number(codBolsaProp) ) {
                     alert( "Se ha cambiado el plan bolsa actual. Debe ingresar los cambio de plan de los equipos.");
                  }
                  else {
                     alert( "La cantidad de equipos del plan bolsa " + descriptionBolsa + " no debe ser mayor que la suma de equipos de la situación actual bolsa, cambio de plan y ventas." );
                  }
                  return false;
               }
               if ( cantidadPlanPropuesto < cantidadPlanBolsaNuevo ) {
                  alert( "Las unidades del plan " + descriptionBolsa + " de la situación propuesta son menores a las unidades ingresadas en las secciones de cambio de plan y unidades adicionales. Porfavor verifique " );
                  return false;
               }
         }
         
         
         return true;   
      }
