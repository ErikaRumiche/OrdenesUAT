package pe.com.nextel.section.sectionAssignment;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.StringTokenizer;
import java.util.Vector;

import pe.com.nextel.bean.CoAssignmentBean;
import pe.com.nextel.dao.BillingAccountDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionAssignmentEvents {
  
  public String saveSection1(RequestHashMap request,Connection conn)  throws Exception, SQLException{
     System.out.println("Grabado de Assignment");  
     
     String   strOrderId                    =     request.getParameter("hdnNumeroOrder");
     
     String[] strIndItems                   =     request.getParameterValues("indItems");
     String[] strTelefono                   =     request.getParameterValues("txtPhoneNumber");
     
     String[] strOrigResponsablePagoDesc    =     request.getParameterValues("txtOrigResponsablePago");
     String[] strOrigResponsablePagoValue   =     request.getParameterValues("hdnOrigResP");

     String[] strOrigFacturaValue           =     request.getParameterValues("hdnOrigFactura");
     
     String[] strNewResponsablePago         =     request.getParameterValues("hdnNewResponsablePago");
     String[] strcmbNewFactura              =     request.getParameterValues("hdnNewFactura");
     
     String[] strContractId                 =     request.getParameterValues("hdnContractId");
     String[] strSnCodeId                   =     request.getParameterValues("hdnSnCodeId");
     String[] strFlagES                     =     request.getParameterValues("hdnFlagES");
     
     String   strMessage                    =     null;
     
     BillingAccountDAO objBillingAccountDAO = new BillingAccountDAO();
     CoAssignmentBean objCoAssignmentBean = null; 
     
     //Insertamos según la cantidad de Items que haya
     if( strIndItems!= null){
     
       for( int i=1; i<strIndItems.length; i++){
          objCoAssignmentBean = new CoAssignmentBean();
          
          /**Numero de la Orden**/
          objCoAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
          /**Numero  del Teléfono**/
          objCoAssignmentBean.setNpphone(MiUtil.getStringObject(strTelefono,i));
          /**Numero  de Contrato**/
          objCoAssignmentBean.setNpbscscontractId(MiUtil.getStringObject(strContractId,i));
          /**Original Responsable de Pago del Teléfono**/
          objCoAssignmentBean.setNpbscspaymntrespcustomeridId(MiUtil.getStringObject(strOrigResponsablePagoValue,i));
          /**Id Servicio**/
          String strOrigFacturaValueAux =strOrigFacturaValueAux=MiUtil.getStringObject(strOrigFacturaValue,i); 			  
          strOrigFacturaValueAux=MiUtil.deleteTagHTML(strOrigFacturaValueAux,Constante.TAGHTML_NBSP);/*eliminando los &nbsp*/			  
          objCoAssignmentBean.setNpbscssncode(strOrigFacturaValueAux);
          /**Nuevo Responsable de Pago**/
          int intNewSite = MiUtil.parseInt(MiUtil.getStringObject(strNewResponsablePago,i));
          intNewSite  = intNewSite<0?(intNewSite*-1):intNewSite;
          String strNewResponsablePagoAux = ""+intNewSite;
          strNewResponsablePagoAux=MiUtil.deleteTagHTML(strNewResponsablePagoAux,Constante.TAGHTML_NBSP);
          objCoAssignmentBean.setNpnewsiteid(strNewResponsablePagoAux);
          
          /**Nuevo Billing Account**/
          String strcmbNewFacturaAux =MiUtil.getStringObject(strcmbNewFactura,i);
          /**Si es una cuenta de facturación existente**/
          if( strFlagES[i].equals("E") ){
            objCoAssignmentBean.setNpbscsbillingAccountId(strcmbNewFacturaAux);
          /**Si es una cuenta de facturación solicitada**/
          }else if ( strFlagES[i].equals("S") ){
            objCoAssignmentBean.setNpbillaccnewid(strcmbNewFacturaAux);
          /**Si es una cuenta primaria**/
          }else{
            objCoAssignmentBean.setNpbillaccnewid(null);				
          }
          
          strMessage  = objBillingAccountDAO.insAssignmentBillAccount(objCoAssignmentBean,conn);
              
          if( strMessage != null ) return strMessage;
           
       }
       
     }
     
     return null;
  }
  
  public String updateSection1(RequestHashMap request,Connection conn){
   try{
   System.out.println("Actualización de Assignment");  
   
   String   strOrderId                    =     request.getParameter("hdnOrderId");
   String   hdnDeleteRegister             =     request.getParameter("hdnDeleteRegister");
   
   String[] strIndItems                   =     request.getParameterValues("indItems");
   String[] strhdnCoAssignmentID          =     request.getParameterValues("hdnCoAssignmentID");
   
   String[] strTelefono                   =     request.getParameterValues("txtPhoneNumber");
   
   String[] strOrigResponsablePagoDesc    =     request.getParameterValues("txtOrigResponsablePago");
   String[] strOrigResponsablePagoValue   =     request.getParameterValues("hdnOrigResP");

   String[] strOrigFacturaValue           =     request.getParameterValues("hdnOrigFactura");
   
   //String[] strNewResponsablePago         =     request.getParameterValues("cmbNewResponsablePago");
   String[] strNewResponsablePago         =     request.getParameterValues("hdnNewResponsablePago");
   String[] strcmbNewFactura              =     request.getParameterValues("hdnNewFactura");
   
   String[] strContractId                 =     request.getParameterValues("hdnContractId");
   String[] strSnCodeId                   =     request.getParameterValues("hdnSnCodeId");
   String[] strFlagES                     =     request.getParameterValues("hdnFlagES");     
   String[] strhdnFlagSave                =     request.getParameterValues("hdnFlagSave");
   
   String   strMessage                    =     null;
        
   BillingAccountDAO objBillingAccountDAO = new BillingAccountDAO();
   
   CoAssignmentBean objCoAssignmentBean = null; 
   

   //Insertamos según la cantidad de Items que haya
   
   if( strIndItems!= null){
       for( int i=1; i<strIndItems.length; i++){
          //Si se debe de actualizar
          if( strhdnFlagSave[i].equals("U") || strhdnFlagSave[i].equals("I") ) { //El registro :  Update (U) / Insert (I)
          
            objCoAssignmentBean = new CoAssignmentBean();  
            
            /**Numero de la Orden**/
            objCoAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
            /**Id del registro**/
            objCoAssignmentBean.setNpcoassignmentid(MiUtil.parseLong(MiUtil.getStringObject(strhdnCoAssignmentID,i)));
            /**Numero del teléfono**/
            objCoAssignmentBean.setNpphone(MiUtil.getStringObject(strTelefono,i));
            /**Numero de Contrato**/
            objCoAssignmentBean.setNpbscscontractId(MiUtil.getStringObject(strContractId,i));
            /**Original Responsable de Pago del Teléfono**/
            objCoAssignmentBean.setNpbscspaymntrespcustomeridId(MiUtil.getStringObject(strOrigResponsablePagoValue,i));
            
            /**Id Servicio**/
            String strOrigFacturaValueAux = MiUtil.getStringObject(strOrigFacturaValue,i); 			  
            strOrigFacturaValueAux  = MiUtil.deleteTagHTML(strOrigFacturaValueAux,Constante.TAGHTML_NBSP);//eliminando los &nbsp			  
            objCoAssignmentBean.setNpbscssncode(strOrigFacturaValueAux);
            
            /**Nuevo Responsable de Pago**/
            int intNewSite = MiUtil.parseInt(MiUtil.getStringObject(strNewResponsablePago,i));
            intNewSite  = intNewSite<0?(intNewSite*-1):intNewSite;
            String strNewResponsablePagoAux = ""+intNewSite;
            strNewResponsablePagoAux=MiUtil.deleteTagHTML(strNewResponsablePagoAux,Constante.TAGHTML_NBSP);
            objCoAssignmentBean.setNpnewsiteid(strNewResponsablePagoAux);
            
            /**Nuevo Billing Account**/
            String strcmbNewFacturaAux =MiUtil.getStringObject(strcmbNewFactura,i);
            /**Si es una cuenta de facturación existente**/
            if( strFlagES[i].equals("E") ){
              objCoAssignmentBean.setNpbscsbillingAccountId(strcmbNewFacturaAux);
            /**Si es una cuenta de facturación solicitada**/
            }else if ( strFlagES[i].equals("S") ){
              objCoAssignmentBean.setNpbillaccnewid(strcmbNewFacturaAux);
            /**Si es una cuenta primaria**/
            }else{
              objCoAssignmentBean.setNpbillaccnewid(null);				
            }
            
            /**Permite identificar si se actualiza o se registra una asignación**/
            if( strhdnFlagSave[i].equals("U") )
              strMessage  = objBillingAccountDAO.updAssignmentBillAccount(objCoAssignmentBean,conn);
            else if ( strhdnFlagSave[i].equals("I") )
              strMessage  = objBillingAccountDAO.insAssignmentBillAccount(objCoAssignmentBean,conn);

            if( strMessage != null ) return strMessage;             
            }

        }
        
        /**Permite eliminar las asignaciones de cuentas de facturación**/
        strMessage = delCoAssignment(hdnDeleteRegister,objBillingAccountDAO,conn);
        if( strMessage != null ) return strMessage;
   }
   
   }catch(SQLException ex){
      return ex.getClass() + " - " + ex.getMessage() + " - " + ex.getCause();
   }catch(Exception ex){
     return ex.getClass() + " - " + ex.getMessage() + " - " + ex.getCause();
   }
   
   return null;
   
  }
  
  public String delCoAssignment(String hdnDeleteRegister,BillingAccountDAO objBillingAccountDAO,Connection conn)   throws Exception, SQLException{
  
    if( hdnDeleteRegister != null ){
    
            CoAssignmentBean objCoAssignmentBean = null; 
            StringTokenizer tokens = new StringTokenizer(hdnDeleteRegister,"|");
            Vector vctDeleted = new Vector();
            
            while(tokens.hasMoreElements()){
                String str = (String)tokens.nextElement();
                vctDeleted.addElement(""+str);
            }
            
            //El vector está lleno
            String resultDeleteItem = "";
            //**Eliminar
            for( int x=0; x<vctDeleted.size(); x++ ){
              objCoAssignmentBean = new CoAssignmentBean();
              objCoAssignmentBean.setNpcoassignmentid(MiUtil.parseLong((String)vctDeleted.elementAt(x)));
              resultDeleteItem  = objBillingAccountDAO.deleteAssignmentBillAccount(objCoAssignmentBean,conn);
              
              if( resultDeleteItem != null ) return resultDeleteItem;
              
            }
            
    }
    
    return null;
  
  }
  
  
}