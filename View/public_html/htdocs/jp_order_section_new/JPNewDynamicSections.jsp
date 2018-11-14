<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.Constante" %>

<script>
         var VOrderSections = null;
             VOrderSections = new Vector();

         function fxRegisterOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject){
            VOrderSections.addElement(new fxMakeOrderSection(sectionId,objectType,objectId,evenType,eventName,status,nptypeobject,npobjectname,npbusinessobject));
         }
    
         function fxDelObjVOrderSections(v_objectType,v_objectId){
            var objSection = null;
            var v_VOrderSectionsCount = VOrderSections.size();
            for ( i = v_VOrderSectionsCount - 1; i >= 0 ; i--){
               objSection = VOrderSections.elementAt(i);
               if ((objSection.objectType == v_objectType) && (objSection.objectId == v_objectId)){
                  VOrderSections.removeElementAt(i);
               }
            }
         }

         function fxValidateSectionsforSaving(){
            //alert("Entramos fxValidateSectionsforSaving");
            var ki = 0;
            //console.log("VOrderSections : " + VOrderSections.size() );
            for ( ki = 0 ; ki < VOrderSections.size(); ki++){
               objSection = VOrderSections.elementAt(ki);
               if (    (objSection.eventHandler != "")
                    && (objSection.evenType == "ON_VALIDATE")
                  ){
                 //console.log("objSection.eventHandler: "+objSection.eventHandler);
                 //try{
                    resultado  = eval(objSection.eventHandler+" == true");                    
                    //console.log("resultado: "+resultado);
                 /*}catch(err){
                     console.log("err: "+err);
                 }*/
                  if (resultado == false )
                     return false;
               }
            }
            //console.log("FINNNNN");
            return true;
         }
         
         function fxValidateSectionsFinalStatus(){
            var ki = 0;
            for ( ki = 0 ; ki < VOrderSections.size() ; ki++){
               objSection = VOrderSections.elementAt(ki);
               if ((objSection.eventHandler != "") && (objSection.evenType == "ON_FINAL_STATUS")){
                  resultado  = eval(objSection.eventHandler+" == true");
                  if (resultado == false )
                     return false;
               }
            }
            return true;
         }

         function fxValidateSectFinalStatByObjType(objectType){
            var ki = 0;
            for ( ki = 0 ; ki < VOrderSections.size() ; ki++){
               objSection = VOrderSections.elementAt(ki);
               if ((objSection.objectType == objectType) && (objSection.evenType == "ON_FINAL_STATUS")){
                  resultado  = eval(objSection.eventHandler+" == true");
                  if (resultado == false )
                     return false;
               }
            }
            return true;
         }

         function fxValidateSectFinalStatByObjTypeObjId(objectType,objectId){
            var ki = 0;
            for ( ki = 0; ki < VOrderSections.size() ; ki++){
               objSection = VOrderSections.elementAt(ki);
               if ((objSection.objectType == objectType) && (objSection.objectId == objectId)&& (objSection.evenType == "ON_FINAL_STATUS")){
                  resultado  = eval(objSection.eventHandler+" == true");
                  if (resultado == false )
                     return false;
               }
            }
            return true;
         }

         function fxMakeIncSectionIdentifyObjects(){
            // Borramos las filas insertadas si es que hubieran
            var table = document.all?document.all["IdSectionIncSectionsIdentifyObjects"]:document.getElementById("IdSectionIncSectionsIdentifyObjects");
            var longuitud = table.rows.length - 1;
            for(i= longuitud ; i>=0 ; i--){
               table.deleteRow(i);
            };

            for ( i = 0 ; i < VOrderSections.size() ; i++){
               objSection = VOrderSections.elementAt(i);
               if (objSection.evenType == "NEW_ON_DISPLAY"){
                  fxAddRowTableSectionsIdentifyObjs(document,"IdSectionIncSectionsIdentifyObjects",objSection);
               }
            }
         }

         function fxSetStatusIncSection(objectType,objectId,v_status){
            for ( i = 0 ; i < VOrderSections.size() ; i++){
               objSection = VOrderSections.elementAt(i);
               if ((objSection.objectType == objectType)&& (objSection.objectId == objectId )){
                  objSection.status = V_status;
               }
            }
         }

         function fxDeleteSection(){
            VOrderSections = new Vector();
            parent.mainFrame.document.getElementById('IdSpecificationSections').innerHTML = "";
         }
         


</script>

<div id="IdSpecificationSections">

</div>

<table id="IdSectionIncSectionsIdentifyObjects"  width="100%">

</table>