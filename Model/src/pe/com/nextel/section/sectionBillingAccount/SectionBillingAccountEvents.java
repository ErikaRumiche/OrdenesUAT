package pe.com.nextel.section.sectionBillingAccount;

import java.sql.SQLException;

import pe.com.nextel.bean.BillingAccountBean;
import pe.com.nextel.bean.BillingContactBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.dao.BillingAccountDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionBillingAccountEvents 
{
   public SectionBillingAccountEvents()
   {
   }

/**
* Motivo:  Metodo que contiene la lógica del guardado, actualización y eliminación de una cuenta billing
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 20/09/2007
* @param      RequestHashMap
* @param      Connection 
* @return     String 
*/     
public String doSaveBillinAccount(RequestHashMap request,java.sql.Connection conn)
{    
   BillingAccountDAO objBillAccDAO=new BillingAccountDAO();
   BillingAccountBean objBillAccBean=new BillingAccountBean();
   CustomerBean objCustomerBean =new CustomerBean();
   BillingContactBean objContactBean=null; 
   
   System.out.println("doSaveBillinAccount");
   String strBillAccID[]=request.getParameterValues("hndBillAccId");
   String strBillName[] =request.getParameterValues("hndBillAccName");
   String strBillTitle[]=request.getParameterValues("hndTitleName");
   String strContfName[]=request.getParameterValues("hndContfName");
   String strContlName[]=request.getParameterValues("hndContlName");
   String strCargo[] =request.getParameterValues("hndCargo");
   String strPhoneArea[]=request.getParameterValues("hndPhoneArea");
   String strPhoneNumber[]=request.getParameterValues("hndPhoneNumber");      
   String strAddress1[]=request.getParameterValues("hndAddress1");
   String strAddress2[] =request.getParameterValues("hndAddress2");
   String strDepart[]=request.getParameterValues("hndDepart");
   String strProv[]=request.getParameterValues("hndProv");      
   String strDist[]=request.getParameterValues("hndDist");
   String strZipCode[] =request.getParameterValues("hndZipCode");
   String strState[]=request.getParameterValues("hndState");   
   String strBscsCustId[]=request.getParameterValues("hndBscsCustId");
   String strBscsSeq[]=request.getParameterValues("hndBscsSeq");
   
   //Valores que se recuperan cuando la pagina es invocada desde Creación de Ordenes 
   String strLogin=request.getParameter("hdnLogin");
   long lOrderId = MiUtil.parseLong(request.getParameter("hdnOrderId"));
   //System.out.println("request.getParameter(hdnCustomerId)-->"+request.getParameter("hdnCustomerId"));
   long lCustomerId = MiUtil.parseLong(request.getParameter("hdnCustomerId"));
   long lSiteId = MiUtil.parseLong(request.getParameter("hdnSiteId"));
   String strSwtypeCust=request.getParameter("hdnTypeCustomer");
   strSwtypeCust=MiUtil.initCap(strSwtypeCust);
   
   //Valores q se recuperan cuando la pagina es invocada desde Edicion de Ordenes.
   /*if (strLogin==null) 
      strLogin=(request.getParameter("hdnSessionLogin")==null?"":request.getParameter("hdnSessionLogin"));      */
   /*if (lOrderId==0)
      lOrderId= (request.getParameter("hdnNumeroOrder")==null?0:MiUtil.parseLong(request.getParameter("hdnNumeroOrder"))); 
   */
   //no pasa lo mismo con lCustomerId porque el nombre de la caja q contiene el id es el mismo para creacion y edicion.
   
   /*if (lSiteId==0) 
      lSiteId=(request.getParameter("hdnSite")==null?0:MiUtil.parseLong(request.getParameter("hdnSite")));    */
   /*if (strSwtypeCust==null) 
      strSwtypeCust=request.getParameter("txtTipoCliente")==null?"":request.getParameter("txtTipoCliente");*/ //txtTipoCliente   

   System.out.println(" ----------- INICIO SectionBillingAccountEvents.jsp---------------- ");
   System.out.println("strLogin-->"+strLogin);
   System.out.println("lOrderId-->"+lOrderId);
   System.out.println("lCustomerId-->"+lCustomerId);   
   System.out.println("lSiteId-->"+lSiteId);   
   System.out.println("strSwtypeCust-->"+strSwtypeCust);   
      
   
   //Fin de valores de otros jsp
   String strBscsCusId=null;
   String strBscsSq=null;
   String strMessage=null;
   int iLongitud=0;
   if (strBillAccID!=null)
      iLongitud=strBillAccID.length;
      
   System.out.println("longitud del arreglo->"+iLongitud);
   System.out.println(" ------------  FIN SectionBillingAccountEvents.jsp----------------- ");  
      
   for(int i = 0; i < iLongitud ; i++){               
      System.out.println("ID "+(i+1)+ "-> "+strBillAccID[i]);
      System.out.println("NOMBRE "+(i+1)+ "-> "+strBillName[i]);
      System.out.println("TITLE "+(i+1)+ "-> "+strBillTitle[i]);
      System.out.println("FNOMBRE "+(i+1)+ "-> "+strContfName[i]);             
      System.out.println("LNAME "+(i+1)+ "-> "+strContlName[i]);
      System.out.println("CARGO "+(i+1)+ "-> "+strCargo[i]);
      System.out.println("PHONE AREA "+(i+1)+ "-> "+strPhoneArea[i]);
      System.out.println("PHONE NUMBER "+(i+1)+ "-> "+strPhoneNumber[i]);
      System.out.println("ADDRES1 "+(i+1)+ "-> "+strAddress1[i]);
      System.out.println("ADDRES2 "+(i+1)+ "-> "+strAddress2[i]);
      System.out.println("DEPART "+(i+1)+ "-> "+strDepart[i]);
      System.out.println("PROV "+(i+1)+ "-> "+strProv[i]);
      System.out.println("DISTR "+(i+1)+ "-> "+strDist[i]);
      System.out.println("ZIPCODE "+(i+1)+ "-> "+strZipCode[i]);                
      System.out.println("ESTADO "+(i+1)+ "-> "+strState[i]);
      System.out.println("BSCSCUSTOMERID "+(i+1)+ "-> "+strBscsCustId[i]);
      System.out.println("BSCSSEQ "+(i+1)+ "-> "+strBscsSeq[i]);
      
      if (Constante.TYPE_CRM_CUSTOMER.equals(strSwtypeCust)){                  
         strBscsCusId=null;
         strBscsSq=null;
      }else{ //("Customer".equals(strSwtypeCust)){
         lCustomerId=0;
         lSiteId=0;
         strBscsCusId=strBscsCustId[i];
         strBscsSq=strBscsSeq[i];                     
      }
      
      objContactBean=new BillingContactBean();
      objBillAccBean.setNpBillaccountNewId(MiUtil.parseLong(strBillAccID[i]));                       
      objCustomerBean.setSwCustomerId(lCustomerId);
      System.out.println("XXXX lCustomerId--> "+lCustomerId);
      System.out.println("XXXX lSiteId--> "+lSiteId);
      objBillAccBean.setObjCustomerB(objCustomerBean);
      
      objContactBean.setNpTitle(MiUtil.getStringNull(strBillTitle[i]));
      objContactBean.setNpfname(MiUtil.getStringNull(strContfName[i]));
      objContactBean.setNplname(MiUtil.getStringNull(strContlName[i]));
      objContactBean.setNpjobtitle(MiUtil.getStringNull(strCargo[i]));
      objContactBean.setNpphonearea(MiUtil.getStringNull(strPhoneArea[i]));
      objContactBean.setNpphone(MiUtil.getStringNull(strPhoneNumber[i]));
      objContactBean.setNpaddress1(MiUtil.getStringNull(strAddress1[i]));
      objContactBean.setNpaddress2(MiUtil.getStringNull(strAddress2[i]));
      objContactBean.setNpcity(MiUtil.getStringNull(strDist[i]));
      objContactBean.setNpstate(MiUtil.getStringNull(strProv[i]));            
      objContactBean.setNpdepartment(MiUtil.getStringNull(strDepart[i]));
      objContactBean.setNpzipcode(MiUtil.getStringNull(strZipCode[i]));
      objBillAccBean.setNpBillacCName(strBillName[i]);
      objBillAccBean.setObjBillingContactB(objContactBean);
      objBillAccBean.setNpCreatedby(strLogin);
      objBillAccBean.setNpOrderId(lOrderId);
      objBillAccBean.setNpSiteId(lSiteId);
      objBillAccBean.setNpBscsCustomerId(strBscsCusId);
      objBillAccBean.setNpBscsSeq(strBscsSq);
      
      try{
         /*Se comenta esta parte de código porque las inserción de BA se harán cada vez que se agregue un registro
           en la tabla (en pantalla)*/
         /*if ("Nuevo".equals(strState[i])){              
            strMessage=objBillAccDAO.insBillAccount(objBillAccBean,conn);  
            if (strMessage!=null){
               strMessage="dbmessage"+strMessage;
               break;             
            } 
            System.out.println("el mesaje de error "+strMessage);
         }else */if ("Actualizado".equals(strState[i])){
            strMessage=objBillAccDAO.updBillAccount(objBillAccBean,conn);    
            if (strMessage!=null){
               strMessage="dbmessage"+strMessage;
               break;   
            } 
            System.out.println("el mesaje de actualizado "+strMessage);
         }else if ("Eliminado".equals(strState[i])){
            System.out.println("antes de llamar al METODO eliminar ");
            strMessage=objBillAccDAO.delBillAccount(Long.parseLong(strBillAccID[i]),conn);   
            System.out.println("el mesaje de ERROR->"+strMessage);
            if (strMessage!=null){
               strMessage="dbmessage"+strMessage;
               break;  
            }
            System.out.println("el mesaje de eliminado "+strMessage);
         }
      }catch(SQLException ex){
         strMessage=ex.getMessage();
      }
   }   
   return strMessage;        
}   
}