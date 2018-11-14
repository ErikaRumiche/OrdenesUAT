package pe.com.nextel.section.sectionRegularizeCustomerInfo;

import java.util.HashMap;

import pe.com.nextel.bean.SubRegCustomerInfoBean;
import pe.com.nextel.dao.SubRegCustomerInfoDAO;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionRegularizeCustomerInfoEvents 
{
  public SectionRegularizeCustomerInfoEvents()
  {
  }
  
  public String insertCustomerInfo(RequestHashMap request,java.sql.Connection conn){
    HashMap hshDataMap = new HashMap();
    String strMessage = null;
    SubRegCustomerInfoBean customerInfo = new SubRegCustomerInfoBean();
    SubRegCustomerInfoDAO customerInfoDAO = new SubRegCustomerInfoDAO();
    String flgExist;
    try{
      String strRazonSoc = request.getParameter("txtRazonsocial"); //NPRAZON
      String strTipoDocumento = request.getParameter("txtTipoDocumento");//NPTIPODOC
      
      
      long lOrderId = MiUtil.parseLong(request.getParameter("hdnNumeroOrder"));//NPORDERID
      String strLogin = request.getParameter("hdnSessionLogin");//NPCREATEDBY
      long lCustomerId = MiUtil.parseLong(request.getParameter("txtCompanyId"));//NPCUSTOMERID
      String strPhone = request.getParameter("txtNumeroTelefono");
      long lIncidentId = MiUtil.parseLong((String)request.getParameter("hdnGeneratorId"));
      
      customerInfo.setStrLogin(strLogin);
      customerInfo.setOrderId(lOrderId);
      customerInfo.setCustomerId(lCustomerId);
      customerInfo.setStrPhone(strPhone);
      customerInfo.setStrRazonSoc(strRazonSoc);
      
      System.out.println("[SectionRegularizeCustomerInfoEvents.insertCustomerInfo] customerInfo : "  + customerInfo.toString());
      System.out.println("[SectionRegularizeCustomerInfoEvents.insertCustomerInfo] lIncidentId : "  + lIncidentId);
      
      hshDataMap = customerInfoDAO.isExistOrderSubReg(lCustomerId,lIncidentId,conn);
      
      flgExist = (String)hshDataMap.get("an_exist");
      System.out.println("[SectionRegularizeCustomerInfoEvents.insertCustomerInfo] flgExist : "  + flgExist);
      
      if(flgExist!=null && flgExist.equals("0")){
        strMessage = customerInfoDAO.insertCustomerInfo(customerInfo,conn);
      }else{
        strMessage = "El cliente tiene una orden abrierta para esta categoria";
      }
      
      System.out.println("[SectionRegularizeCustomerInfoEvents.insertCustomerInfo] strMessage : "  + strMessage);
      
    }catch(Exception e){
      return e.getClass() + " - " + e.getMessage() + " - " + e.getCause();
    }
    
  return strMessage;
  }
  
  public String updateCustomerInfo(RequestHashMap request,java.sql.Connection conn){
    String strMessage = null;
    SubRegCustomerInfoBean customerInfo = new SubRegCustomerInfoBean();
    SubRegCustomerInfoDAO customerInfoDAO = new SubRegCustomerInfoDAO();
    
    try{
      String strRazonSoc = request.getParameter("txtRazonsocial"); //NPRAZON
      long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));//NPORDERID
      String strLogin = request.getParameter("hdnSessionLogin");//NPCREATEDBY
      long lCustomerId = MiUtil.parseLong(request.getParameter("txtCompanyId"));//NPCUSTOMERID
      String strStatusOrder = request.getParameter("txtEstadoOrden");
      String strPhone = request.getParameter("txtNumeroTelefono");
      
      customerInfo.setStrRazonSoc(strRazonSoc);
      customerInfo.setStrLogin(strLogin);
      customerInfo.setOrderId(lOrderId);
      customerInfo.setCustomerId(lCustomerId);
      customerInfo.setStatus("a");
      customerInfo.setStatusOrder(strStatusOrder);
      customerInfo.setStrPhone(strPhone);
      
      System.out.println("[SectionRegularizeCustomerInfoEvents.updateCustomerInfo] customerInfo : "  + customerInfo.toString());
      
      strMessage = customerInfoDAO.updatetCustomerInfo(customerInfo,conn);
      
      System.out.println("[SectionRegularizeCustomerInfoEvents.updateCustomerInfo] strMessage : "  + strMessage);
    }catch(Exception e){
      return e.getClass() + " - " + e.getMessage() + " - " + e.getCause();
    }
    
  return strMessage;
  }
}