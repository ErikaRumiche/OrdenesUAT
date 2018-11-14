package pe.com.nextel.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import pe.com.nextel.bean.InboxResponse;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderWorkFlow;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.SpecificationBean;
import pe.com.nextel.bo.OrdersWorkFlowManageBO;
import pe.com.nextel.util.MiUtil;


/**
 * Clase de tipo Service, permite preparar la invocacion a Workflow de ordenes (OAS)
 *
 * @author      Hugo Tenorio
 * @version     %I%, %G%
 * @since       1.0
 */
public class WorkflowService 
{
  public WorkflowService()
  {
  }

  public HashMap doInvokeBPELProcess(HashMap hashData, PortalSessionBean objPortalSesBean){
      String strMessage=null;
      HashMap hshResult=new HashMap();
      String strOrderId=null;
      Date fechaOcur = new Date();
      try{      

           SpecificationBean objSpecificationBean=(SpecificationBean)hashData.get("objSpecificationBean");
           strOrderId=(String)hashData.get("strOrderId");
           String strCustomerId=(String)hashData.get("strCustomerId");
           //String strSolutionId=(String)hashData.get("strSolutionId");
			  String strDivisionId=(String)hashData.get("strDivisionId");

           String strNpBpelconversationid=(String)hashData.get("strNpBpelconversationid");
           String strActionName=(String)hashData.get("strActionName");
           String strNextInboxName=(String)hashData.get("strNextInboxName");         
           String strOldInboxName=(String)hashData.get("strCurrentInbox");
           
           System.out.println("doInvokeBPELProcess INICIO ORDEN = "+strOrderId+", fechaOcur "+fechaOcur);    
           System.out.println("strOrderId-->"+strOrderId);
           System.out.println("strCustomerId-->"+strCustomerId);
           //System.out.println("strSolutionId-->"+strSolutionId);
			  System.out.println("strDivisionId-->"+strDivisionId);
           System.out.println("strNpBpelconversationid-->"+strNpBpelconversationid);
           System.out.println("strActionName-->"+strActionName);
           System.out.println("strNextInboxName-->"+strNextInboxName);
           System.out.println("objPortalSesBean.getLogin()-->"+objPortalSesBean.getLogin());         
           System.out.println("objSpecificationBean.getNpBpelflowgroup()-->"+objSpecificationBean.getNpBpelflowgroup());
           System.out.println("strOldInboxName-->"+strOldInboxName);                    
           OrderWorkFlow orderWorkFlow = new OrderWorkFlow();
           orderWorkFlow.setNpOrderId(String.valueOf(MiUtil.parseLong(strOrderId)));
           orderWorkFlow.setNpOrderType(objSpecificationBean.getNpBpelflowgroup());
           orderWorkFlow.setNpLog("");
           orderWorkFlow.setNpLastUpdateBy(objPortalSesBean.getLogin());
           orderWorkFlow.setNpCustomerId(strCustomerId);
           orderWorkFlow.setNpOrder("");   
           orderWorkFlow.setStrFlagSourceMigration("");   
           
           //orderWorkFlow.setNpSolutionId(MiUtil.parseLong(strSolutionId));
           orderWorkFlow.setNpDivisionId(MiUtil.parseLong(strDivisionId));
           orderWorkFlow.setNpBpelConversationId(strNpBpelconversationid);            
           orderWorkFlow.setStrActionName(strActionName);
           orderWorkFlow.setStrNextInboxName(strNextInboxName);       
           orderWorkFlow.setNpNameInboxAnterior(strOldInboxName);       
           //HTENORIO: Invocación indicando el inbox de origen
           orderWorkFlow.setNpNameInbox(strOldInboxName);       
  
           OrdersWorkFlowManageBO ordersWorkFlowManageBO = new OrdersWorkFlowManageBO();
           //HTENORIO: Invocación con respuesta síncrona
           InboxResponse inboxResponse = ordersWorkFlowManageBO.updateWorkFlowSyn(orderWorkFlow);  
           
           if (inboxResponse.getMessage()!=null && 
              (inboxResponse.getMessage().startsWith("BPEL ServerException") || inboxResponse.getMessage().startsWith("ERROR ServerException"))) {
           
             EditOrderService objOrderS=new EditOrderService();
             OrderBean objOrder = new OrderBean();
             HashMap hshRetorno = objOrderS.getOrderDetailFlow(MiUtil.parseLong(orderWorkFlow.getNpOrderId()),orderWorkFlow.getNpLastUpdateBy());
             
             strMessage = (String)hshRetorno.get("strMessage");
             if (strMessage!=null)
             throw new Exception(strMessage);              
             
             objOrder   = (OrderBean)hshRetorno.get("objResume");
             //Se busca los datos de la orden para poder reestablecer   TIENDA01,ADM_VENTAS
             //String strStatus=(String)hshRetorno.get("NPSTATUS");
             String strStatus = objOrder.getNpStatus();
             String strBpelBackInbox = objOrder.getNpBpelbackinboxs();
             //System.out.println("Invoke strStatus getOrderDetailFlow : " + strStatus);
             //System.out.println("Invoke strBpelBackInbox getOrderDetailFlow : " + strBpelBackInbox);
             String npInboxAnterior = "", npBackInboxs = "";
             if(strBpelBackInbox != null){
                StringTokenizer tokens    = new StringTokenizer(strBpelBackInbox,",");
                int numInboxs = tokens.countTokens();
                int count = 0;
                  for(int i = 0; i<numInboxs; i++){
                      count++;
                      if(count == numInboxs){
                         npInboxAnterior = tokens.nextToken();        
                         //System.out.println("NumTok: " + numInboxs);
                         //System.out.println("CountEquals: " + count);
                         //System.out.println("ValorTokInboxAnterior: " + npInboxAnterior);
                      }else{
                         if(npBackInboxs.equals("")){
                            npBackInboxs = tokens.nextToken();
                         }else{
                            npBackInboxs = npBackInboxs+","+tokens.nextToken();
                         }
                      }
                  }
                  //System.out.println("npBackInboxs: " + npBackInboxs);
             }             
             System.out.println("doInvokeBPELProcess Error del servidor SOA Workflow, se restablecera el flujo de la orden "+strOrderId+", su estado actual es : " + strStatus+", fechaOcur "+fechaOcur);
             orderWorkFlow.setStrFlagSourceMigration("MIGRACION");
             // Si es BAGLOCK se envia adelante la palabra BAGLOCK y como inbox anterior el inbox inicial.
             if (strStatus.equalsIgnoreCase("BAGLOCK")) {
                //Se debe obtener el inbox anterior desde el historial de accion
               	long lOrderId = Long.parseLong(strOrderId);
                OrderTabsService objOrderTabsService = new OrderTabsService();
                ArrayList arrHistoryActionList = objOrderTabsService.getHistoryActionListByOrder(lOrderId);
                  System.out.println("Tamaño del ArrayList: " + arrHistoryActionList.size());
                if(arrHistoryActionList!=null && arrHistoryActionList.size()>0 ) {
                  int ultimo = arrHistoryActionList.size()-1;        
                  HashMap hmObjeto= null;
                  hmObjeto = (HashMap)arrHistoryActionList.get(ultimo);
                  String strOldInbox = (String)hmObjeto.get("swsender");
                  System.out.println("doInvokeBPELProcess se va generar en BAGLOCK_"+strOldInbox);
                  strStatus = "BAGLOCK_"+strOldInbox;
                } 
                else {
                  strStatus = "BAGLOCK_"+npInboxAnterior;                                    
                }

             }
             
             orderWorkFlow.setStrNextInboxName(strStatus);
             orderWorkFlow.setNpNameInboxAnterior(npInboxAnterior);   
             orderWorkFlow.setNpBackInboxs(npBackInboxs);
             
             InboxResponse newInboxResponse = ordersWorkFlowManageBO.updateWorkFlowSyn(orderWorkFlow);        
             inboxResponse = newInboxResponse;
             inboxResponse.setMessage("Se produjo un error en el Servidor SOA. Vuelva a intentarlo.");
            
           }                 
           strMessage = inboxResponse.getMessage();                            
           System.out.println("inboxResponse.getNameNewInbox() = "+inboxResponse.getNameNewInbox());
           System.out.println("inboxResponse.getNameOldInbox() = "+inboxResponse.getNameOldInbox());
           //System.out.println("inboxResponse.getMessage() = "+inboxResponse.getMessage());  
           //System.out.println("strMessage = "+strMessage);           
           
           hshResult.put("strNextInbox",inboxResponse.getNameNewInbox());
           hshResult.put("strOldInbox",inboxResponse.getNameOldInbox());         
           hshResult.put("strMessage",inboxResponse.getMessage());                  
                  
         }catch(Exception e){
            e.printStackTrace();
            strMessage = e.getMessage();
            hshResult.put("strMessage",strMessage);         
         }     
        System.out.println("doInvokeBPELProcess FIN ORDEN = "+strOrderId+", message:"+strMessage+", fechaOcur "+fechaOcur);             
        return hshResult;
   }   
   
  public HashMap doInvokeBPELCreateWorkflow(HashMap hashData, PortalSessionBean objPortalSesBean){
      String strMessage=null;
      HashMap hshResult=new HashMap();
      String strOrderId=null;
      Date fechaOcur = new Date();
      try{      

           SpecificationBean objSpecificationBean=(SpecificationBean)hashData.get("objSpecificationBean");
           strOrderId=(String)hashData.get("strOrderId");
           String strCustomerId=(String)hashData.get("strCustomerId");
           //String strSolutionId=(String)hashData.get("strSolutionId");
			  String strDivisionId=(String)hashData.get("strDivisionId");

           String strNpBpelconversationid=(String)hashData.get("strNpBpelconversationid");
           String strActionName=(String)hashData.get("strActionName");
           String strNextInboxName=(String)hashData.get("strNextInboxName");         
           String strOldInboxName=(String)hashData.get("strCurrentInbox");
           
           System.out.println("doInvokeBPELCreateWorkflow INICIO ORDEN = "+strOrderId+", fechaOcur "+fechaOcur);    
           System.out.println("strOrderId-->"+strOrderId);
           System.out.println("strCustomerId-->"+strCustomerId);
           //System.out.println("strSolutionId-->"+strSolutionId);
			  System.out.println("strDivisionId-->"+strDivisionId);
           System.out.println("strNpBpelconversationid-->"+strNpBpelconversationid);
           System.out.println("strActionName-->"+strActionName);
           System.out.println("strNextInboxName-->"+strNextInboxName);
           System.out.println("objPortalSesBean.getLogin()-->"+objPortalSesBean.getLogin());         
           System.out.println("objSpecificationBean.getNpBpelflowgroup()-->"+objSpecificationBean.getNpBpelflowgroup());
           System.out.println("strOldInboxName-->"+strOldInboxName);         
           //System.out.println("------doInvokeBPELCreateWorkflow / FIN-DATOS A ENVIAR AL WORKFLOW, ORDEN = "+strOrderId);    
           OrderWorkFlow orderWorkFlow = new OrderWorkFlow();
           orderWorkFlow.setNpOrderId(String.valueOf(MiUtil.parseLong(strOrderId)));
           orderWorkFlow.setNpOrderType(objSpecificationBean.getNpBpelflowgroup());
           orderWorkFlow.setNpLog("");
           orderWorkFlow.setNpLastUpdateBy(objPortalSesBean.getLogin());
           orderWorkFlow.setNpCustomerId(strCustomerId);
           orderWorkFlow.setNpOrder("");   
           orderWorkFlow.setStrFlagSourceMigration("");   
           
           //orderWorkFlow.setNpSolutionId(MiUtil.parseLong(strSolutionId));
           orderWorkFlow.setNpDivisionId(MiUtil.parseLong(strDivisionId));
           orderWorkFlow.setNpBpelConversationId(strNpBpelconversationid);            
           orderWorkFlow.setStrActionName(strActionName);
           orderWorkFlow.setStrNextInboxName(strNextInboxName);       
           orderWorkFlow.setNpNameInboxAnterior(strOldInboxName);       
           //HTENORIO: Invocación indicando el inbox de origen
           orderWorkFlow.setNpNameInbox(strOldInboxName);       
  
           OrdersWorkFlowManageBO ordersWorkFlowManageBO = new OrdersWorkFlowManageBO();
           //HTENORIO: Invocación con respuesta síncrona
           InboxResponse inboxResponse = ordersWorkFlowManageBO.updateWorkFlowSyn(orderWorkFlow);             
                            
           strMessage = inboxResponse.getMessage();                 
           System.out.println("doInvokeBPELCreateWorkflow INICIO RESPUESTA DEL WORKFLOW ORDEN = "+strOrderId);         
           System.out.println("inboxResponse.getNameNewInbox() = "+inboxResponse.getNameNewInbox());
           System.out.println("inboxResponse.getNameOldInbox() = "+inboxResponse.getNameOldInbox());
           //System.out.println("inboxResponse.getMessage() = "+inboxResponse.getMessage());  
           
           hshResult.put("strNextInbox",inboxResponse.getNameNewInbox());
           hshResult.put("strOldInbox",inboxResponse.getNameOldInbox());         
           hshResult.put("strMessage",inboxResponse.getMessage());                  
                  
         }catch(Exception e){
            e.printStackTrace();
            strMessage = e.getMessage();
            
            hshResult.put("strMessage",strMessage);         
         }     
        System.out.println("doInvokeBPELCreateWorkflow FIN ORDEN = "+strOrderId+", message:"+strMessage+", fechaOcur "+fechaOcur);             
        return hshResult;
   }   
   
   
}
