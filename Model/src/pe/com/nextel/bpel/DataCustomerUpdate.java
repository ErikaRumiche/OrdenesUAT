package pe.com.nextel.bpel;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.BillingAccountWriteStruct;
import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.MessageVO;


public class DataCustomerUpdate 
{
  public DataCustomerUpdate(){}
  
 /**
* Motivo:  Metodo que contiene la lógica de  modificación de datos del cliente, site y billing account
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 05/12/2007
*/     

  
  //METODO PARA ACTUALIZAR DATOS Y DIRECCIONES EN BSCS
  //--------------------------------------------------
  public String updateAddressCustomer(CustomerBean objCustomerBeanNewAct,AddressObjectBean objAddressBeanNewAct, 
                                      ContactObjectBean objContactBeanNewAct, long CS_ID,String wv_adr_seq, 
                                      String strIdAPP,long lTitleNew, String strSiteNameNew){

    MessageVO messageVO= new MessageVO();
    String strMessage=null;
    
    try {
        
                           
        //Se envia los datos a la clase que tiene el metodo que hara la insercion de los valores en BSCS
        //----------------------------------------------------------------------------------------------
        AddressCustomerUpdate addressCustomerUpdate = new  AddressCustomerUpdate();
        messageVO= addressCustomerUpdate.createCustomerBSCS( objCustomerBeanNewAct,objAddressBeanNewAct,
                                                             objContactBeanNewAct,CS_ID,
                                                             wv_adr_seq,strIdAPP,lTitleNew, strSiteNameNew);
                                                              
        //Validacion para determinar si existe algun mensaje de error
        //-----------------------------------------------------------
        if( (messageVO.getStrMessage()!=null) && (messageVO.getStrError()!=null) ){
             strMessage="ERROR AL ACTUALIZAR EN  BSCS:"+messageVO.getStrMessage();         
        }
         else{
               strMessage=null;
        }
                         
              
     }
      catch (Exception e) {
      e.printStackTrace();
     }     
     
    return strMessage;
  
  }
  
  
  //METODO PARA ACTUALIZAR EL BILLINGACCOUNT EN BSCS
  //------------------------------------------------
  public String updateBillingCustomer(String strBillAccNameNew, long lBillAccId,  boolean av_create, 
                                      long CS_ID, String strIdAPP){
  
                                              
  BillingAccountWriteStruct billingAccountWriteResult = null;
  String strMessage=null;
  
     try {            
            //Se envia los datos a la clase que tiene el metodo que hara la insercion de los valores en BSCS
            //----------------------------------------------------------------------------------------------
            BillingAccountCustomerUpdate billingAcountCustomerUpdate = new  BillingAccountCustomerUpdate();
            billingAccountWriteResult = billingAcountCustomerUpdate.createCustomerBSCS(strBillAccNameNew,lBillAccId,
                                                                    av_create,CS_ID, strIdAPP);
                                        
          //Validacion para determinar si existe algun mensaje de error
           //-----------------------------------------------------------
          if( (billingAccountWriteResult.getMessage()!=null) ){
              strMessage="ERROR AL ACTUALIZAR EN  BSCS:"+billingAccountWriteResult.getMessage();         
          }
            else{
               strMessage=null;
          }
                         
            
    }
     catch (Exception e) {
           e.printStackTrace();
    }        
  
  return strMessage;
  
  }
  
  
    

  
  
  

  
}