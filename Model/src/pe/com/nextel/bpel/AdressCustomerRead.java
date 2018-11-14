package pe.com.nextel.bpel;

import java.util.HashMap;

import wsp.addressesread.proxy.BPEL_AddressesReadPortClient;
import wsp.addressesread.proxy.be.AddressesReadBeanIn;
import wsp.addressesread.proxy.be.AddressesReadBeanOut;
import wsp.addressesread.proxy.be.ListOfAllAddressesBeanOut;



public class AdressCustomerRead 
{
  public AdressCustomerRead()
  {}
  
 /**
* Motivo:  Metodo que contiene la lógica de obtención de datos en BSCS del Cliente  a traves del API wsp.addressesread
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 05/12/2007
*/     
    
   public static HashMap getAddressCustomer(long CS_ID, String strIdAPP) throws Exception {
      
      String  wv_adr_seq="";
      HashMap hshDataMap = new HashMap();
      String wv_adr_rol=null; 
      boolean wv_dirfac=false;
        try {

             //Se debe definir los objetos que contendran los datos de entrada
             //---------------------------------------------------------------
             AddressesReadBeanIn input = new AddressesReadBeanIn();
             AddressesReadBeanOut addressesReadOut = new AddressesReadBeanOut();

            //Seteamos los parametros de input deseados
            //-----------------------------------------
            input.setIdApp(strIdAPP);
            input.setCS_ID(Long.valueOf(Long.toString(CS_ID)));
          
      
          
            //Se define al proceso que invocara la llamada al Proceso BPEL
            //------------------------------------------------------------
            BPEL_AddressesReadPortClient iniciaproceso = new BPEL_AddressesReadPortClient();
            addressesReadOut = iniciaproceso.process(input);
            
        
            //Se invoca al Bean que tiene los metodos donde se setearan los valores
            //---------------------------------------------------------------------
            ListOfAllAddressesBeanOut[] listOfAllAddressesOut = new ListOfAllAddressesBeanOut[1];
            listOfAllAddressesOut =  addressesReadOut.getLIST_OF_ALL_ADDRESSES();
            
            System.out.println("LISTA LONG:"+listOfAllAddressesOut.length);
            int flag = 0;
            for(int i=0 ; i<listOfAllAddressesOut.length ; i++ ){
            
              wv_adr_rol=listOfAllAddressesOut[i].getADR_ROLES();
              
              if(wv_adr_rol!=null){
                  for (int j=0; j<wv_adr_rol.length(); j++){
                      if(wv_adr_rol.charAt(j)=='B'){ 
                      wv_dirfac=true;
                       break;
                      }
                  }
                  System.out.println("Valor de Fact.:"+wv_adr_rol);
              
                if (wv_dirfac==true){
                    wv_adr_seq= Long.toString( (listOfAllAddressesOut[i].getADR_SEQ()).longValue());
                    hshDataMap.put("wv_adr_seq", wv_adr_seq);
                    hshDataMap.put("strMessage", null);
                    flag=1;
                }
                else{
                 if (flag!=1){
                    hshDataMap.put("wv_adr_seq", "0");
                    hshDataMap.put("strMessage", "No se ha encontrado una dirección de facturación para actualizar!!!");    
                 }
                } 
              
              }
              else{
                if (flag!=1){
                hshDataMap.put("wv_adr_seq", "0");
                hshDataMap.put("strMessage", "No se ha encontrado una dirección de facturación para actualizar!!!");    
                }
              }
            }
          
                

        } catch (Exception e) {
            e.printStackTrace();
            hshDataMap.put("strMessage", "Ha ocurrido un error al encontrar el numero de secuencia");    
            throw new Exception(e.getMessage());

        }
        
        System.out.println("MENSAJE DE ADRESSCUSTOMERREAD:"+wv_adr_seq);

      //  return wv_adr_seq;
        return hshDataMap;
        
    }
    
    
}