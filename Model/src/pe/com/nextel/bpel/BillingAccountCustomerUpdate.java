package pe.com.nextel.bpel;

import java.math.BigDecimal;

import pe.com.nextel.bean.BillingAccountWriteStruct;

import wsp.billingaccountwrite.proxy.BPEL_BillingAccountWritePortClient;
import wsp.billingaccountwrite.proxy.billingaccountwrite.types.BillingAccountWriteIn;
import wsp.billingaccountwrite.proxy.billingaccountwrite.types.WriteBillingAccountElement;
import wsp.billingaccountwrite.proxy.billingaccountwrite.types.WriteBillingAccountResponseElement;



public class BillingAccountCustomerUpdate 
{
  public BillingAccountCustomerUpdate()
  {
  }

/**
* Motivo:  Metodo que contiene la lógica de actualización de datos en BSCS de Billingaccount a traves del API wsp.billingaccountwrite
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 05/12/2007
*/     

  
  public BillingAccountWriteStruct createCustomerBSCS(String strBillAccNameNew,long lBillAccId, boolean av_create,
                                                      long CS_ID, String strIdAPP) throws Exception{
                                                
     String message = "";
     String billingAccountId = null;    
     //Traemos al bean BillingAccountWriteStruct para seterale los valores de los mensajes (al final de todo el proceso)
     //----------------------------------------------------------------------------------------------------------------
     BillingAccountWriteStruct billingAccountWriteResult = new BillingAccountWriteStruct();
     
     try{
        
         
         BillingAccountCustomerUpdate tester = new BillingAccountCustomerUpdate();
         
         //Se definen los objetos que contendran los datos de entrada
         //----------------------------------------------------------
         WriteBillingAccountElement input = new WriteBillingAccountElement();
         BillingAccountWriteIn in = new BillingAccountWriteIn();
        
         //Se define el objeto que almacenara el retorno
         //---------------------------------------------
         WriteBillingAccountResponseElement resultOUT = new WriteBillingAccountResponseElement();
         
         //Seteamos los parametros de input deseados (Parametros Requeridos)
         //-----------------------------------------
         in.setIdApp(strIdAPP); //Identificador de Aplicacion   
         
         
         //Seteamos los valores para la actuaalizacion de los Datos (BillingAccount)
         //-------------------------------------------------------------------------
         in.setBILLINGACCOUNTID(new Long(lBillAccId));
         in.setBILLINGACCOUNTNAME(strBillAccNameNew);
         in.setCREATE(new Boolean(av_create));
         in.setCSID(new Long(CS_ID)); 
              
         //Seteamos la variable input
         //--------------------------
         input.setBillingAccountWriteIn(in);
        
         //Se llama al metodo
         //-------------------
         resultOUT = tester.getBillingAccountWrite(input);
         
         //Validamos Codigo de Error: coderror = 0 significa OK, caso contrario es un error
         //--------------------------------------------------------------------------------
         if (resultOUT.getResult().getCodError().equals("0")) {
         
            //Imprimimos la respuesta (LECTURA DE VALORES DE AUDITORIA)
            //---------------------------------------------------------
            System.out.println("ID_BPEL: " + resultOUT.getResult().getIdBpel());
            System.out.println("resultOUT.getResult().getFcil(): " + resultOUT.getResult().getFcil());                                   
            System.out.println("resultOUT.getResult().getErrMsg(): " + resultOUT.getResult().getErrMsg());                                                                      
            System.out.println("resultOUT.getResult().getCodError(): " + resultOUT.getResult().getCodError());
            System.out.println("resultOUT.getResult().getBILLING_ACCOUNT_ID(): " + resultOUT.getResult().getBILLINGACCOUNTID());
            System.out.println("resultOUT.getResult().getBILLING_ACCOUNT_CODE(): " + resultOUT.getResult().getBILLINGACCOUNTCODE());    
            billingAccountId = resultOUT.getResult().getBILLINGACCOUNTID().toString();                                
            billingAccountWriteResult.setNpBillingAccountId(new BigDecimal(billingAccountId));
        } else {
            System.out.println("[CodError] " + resultOUT.getResult().getCodError());
            System.out.println("[ErrMsg] " + resultOUT.getResult().getErrMsg());
            message =  resultOUT.getResult().getErrMsg();
            billingAccountWriteResult.setMessage(message);
        }    
     } catch (Exception e) {
         e.printStackTrace();
         throw new Exception(e.getMessage());
     }  finally {            
         return billingAccountWriteResult;   
     }
  }
  
  
  
   public WriteBillingAccountResponseElement getBillingAccountWrite(WriteBillingAccountElement input) throws Exception {
        
        //Se define el objeto de Salida 
        //-----------------------------
        WriteBillingAccountResponseElement resultOUT = new WriteBillingAccountResponseElement();

        //Se define el objeto "Invoker" que hara la llamada del Proceso BPEL 
        //------------------------------------------------------------------
        BPEL_BillingAccountWritePortClient invokeBillingAccountWrite = new BPEL_BillingAccountWritePortClient();

        //Se utiliza el metodo "process" que llamara al proceso BPEL y se almacena en la variable resultOUT
        //-------------------------------------------------------------------------------------------------
        long ini = System.currentTimeMillis();
        resultOUT = invokeBillingAccountWrite.process(input);
        long fin = System.currentTimeMillis();
        long tot = 0;
        tot = fin - ini;
        System.out.println("Tiempo Invoke: " + tot);

        //Se retorna la variable 
        //----------------------
        return resultOUT;
    }
     
}