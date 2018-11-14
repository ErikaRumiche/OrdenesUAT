package pe.com.nextel.bpel;


import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.MessageVO;

import wsp.addresswrite.proxy.BPEL_AddressWritePortClient;
import wsp.addresswrite.proxy.addresswrite.types.WriteAddressElement;
import wsp.addresswrite.proxy.addresswrite.types.WriteAddressResponseElement;
import wsp.addresswrite.proxy.be.AddressWriteBeanIn;
import wsp.addresswrite.proxy.be.AddressWriteBeanOut;


public class AddressCustomerUpdate {

  public AddressCustomerUpdate(){}
  
/**
* Motivo:  Metodo que contiene la lógica de actualización de datos en BSCS del Cliente  a traves del API  wsp.addresswrite
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 05/12/2007
*/     
    
  public MessageVO createCustomerBSCS(CustomerBean objCustomerBeanNewAct, AddressObjectBean objAddressBeanNewAct,
                                      ContactObjectBean objContactBeanNewAct, long CS_ID,
                                      String wv_adr_seq, String strIdAPP,long lTitleNew,
                                      String strSiteNameNew) throws Exception{
   
     
     //Traemos al bean MessageVO para seterale los valores de los mensajes (al final de todo el proceso)
     //--------------------------------------------------------------------------------------------------
     MessageVO messageVO = new MessageVO();         
       
       try{
            
         AddressCustomerUpdate tester = new AddressCustomerUpdate();
        
          //-----------------------------------------------------------
          
          AddressWriteBeanIn in =new AddressWriteBeanIn();
          AddressWriteBeanOut out = new AddressWriteBeanOut();
        
          //Parametros Requeridos
          //---------------------
          in.setIdApp(strIdAPP); //Identificador de Aplicacion
          in.setADR_SEQ(new Long(Long.parseLong( wv_adr_seq)));
          in.setCS_ID(new Long(CS_ID));
                           
       
          //Seteamos los valores para la actualizacion de valores en BSCS (objCustomerBeanNew)
          //---------------------------------------------------------------------------------
          in.setADR_PHN1_AREA(objCustomerBeanNewAct.getSwMainPhoneArea());
          in.setADR_PHN1(objCustomerBeanNewAct.getSwMainPhone());   
          in.setADR_PHN2_AREA(objCustomerBeanNewAct.getNpPhone2areacode());
          in.setADR_PHN2(objCustomerBeanNewAct.getSwPhone2());
          in.setADR_FAX_AREA(objCustomerBeanNewAct.getSwMainFaxArea());
          in.setADR_FAX(objCustomerBeanNewAct.getSwMainFax());
        
         
          
         //Seteamos los valores para la actualizacion de valores en BSCS (objAddressBeanNew)
         //---------------------------------------------------------------------------------
         in.setCLA_DEPARTAMENTO(objAddressBeanNewAct.getSwstate());
         in.setCLA_PROVINCIA(objAddressBeanNewAct.getSwprovince());
         in.setCLA_DISTRITO(objAddressBeanNewAct.getSwcity());
         in.setCLA_NOTA(objAddressBeanNewAct.getSwregionname());
         
         //String strCalle=objAddressBeanNewAct.getSwaddress1()+" "+objAddressBeanNewAct.getSwaddress2()+" "+objAddressBeanNewAct.getSwaddress3();  MCB SAR 0037 167824  
         String strCalle=objAddressBeanNewAct.getSwaddress1();
         if (objAddressBeanNewAct.getSwaddress2() != "")
            strCalle=strCalle+" "+objAddressBeanNewAct.getSwaddress2();
         if (objAddressBeanNewAct.getSwaddress3() != "")
            strCalle=strCalle+" "+objAddressBeanNewAct.getSwaddress3();        
         
         in.setCLA_CALLE(strCalle);
       
         in.setADR_ZIP(objAddressBeanNewAct.getSwzip());
      
          
         //Seteamos los valores para la actualizacion de valores en BSCS (objContactBeanNew)
        //---------------------------------------------------------------------------------
         in.setADR_FNAME(objContactBeanNewAct.getSwfirstname());

         //SAR 0033-182328 
         in.setADR_LNAME(objContactBeanNewAct.getSwlastname()+ " "+ objContactBeanNewAct.getSwmiddlename());
         
     
      //   in.setTTL_ID(new Long(lTitleNew));
         in.setADR_JBDES(objContactBeanNewAct.getSwjobtitle());
         in.setADR_EMAIL(objContactBeanNewAct.getSwemailaddress());
         if(strSiteNameNew != null)
          in.setCLA_NOMBRE(strSiteNameNew);

         
         //Se llama al metodo
         //-------------------
         
         //Se define el objeto "Invoker" que hara la llamada del Proceso BPEL 
          //------------------------------------------------------------------
          BPEL_AddressWritePortClient invokeCustomerCreate = new BPEL_AddressWritePortClient();
    

         //Se utiliza el metodo "process" que llamara al proceso BPEL y se almacena en la variable resultOUT
         //-------------------------------------------------------------------------------------------------
         long ini = System.currentTimeMillis();
         out = invokeCustomerCreate.process(in);
         long fin = System.currentTimeMillis();
         long tot = 0;
         tot = fin - ini;
         System.out.println("Tiempo Invoke: " + tot);
        
     
         
         //Validamos Codigo de Error: coderror = 0 significa OK, caso contrario es un error
         //--------------------------------------------------------------------------------
         if (out.getCodError().equals("0")) {
            //Lectura de Valores de Auditoria
            //-------------------------------
            System.out.println("ID_BPEL: " + out.getIdBpel());
            messageVO.setStrIdBpel(out.getIdBpel());
             
            
       } else {
              System.out.println("[CodError] " + out.getCodError());
              System.out.println("[ErrMsg] " + out.getErrMsg());
              messageVO.setStrError(out.getCodError());
              messageVO.setStrMessage(out.getErrMsg());
         
      }     
         
         
          
     } catch (Exception e) {
         e.printStackTrace();
         throw new Exception(e.getMessage());
     }    
       
     
     return messageVO;

  }
  


  
  

  
}