package pe.com.nextel.section.sectionChangeContactInfo;

import pe.com.nextel.bean.ContactObjectBean;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionChangeContactInfoEvents 
{
   public SectionChangeContactInfoEvents()
   {
   }

  /**
  * Motivo:  Metodo que contiene la lógica de la inserción de la petición de modificación de datos del contacto tipo pedido de un cliente
  * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
  * <br>Fecha: 18/06/2009
  * @param      RequestHashMap
  * @param      Connection 
  * @return     String 
  */     
   public String insChangeContactInfo(RequestHashMap request,java.sql.Connection conn){    

     CustomerDAO objCustomerDAO=new CustomerDAO();
     GeneralDAO objGeneralDAO = new GeneralDAO();
     CustomerBean objCustomerBean=new CustomerBean();   
     ContactObjectBean objContactBean = null;
     int contador  = 0;
       
      String  strLogin             =  request.getParameter("hdnSessionLogin");
      long lCustomerId             = (request.getParameter("hdnCustomerId")==null?0:MiUtil.parseLong(request.getParameter("hdnCustomerId")));    
      long lOrderId                = (request.getParameter("hdnNumeroOrder")==null?0:MiUtil.parseLong(request.getParameter("hdnNumeroOrder")));         
      String strSiteId             =  request.getParameter("hdnSite");     
      
      String[] strCmbTitulo        = request.getParameterValues("cmbTitulo");
      String[] strTxtNombres       = request.getParameterValues("txtNombres");
      String[] strTxtApePaterno    = request.getParameterValues("txtApePaterno");
      String[] strTxtApeMaterno    = request.getParameterValues("txtApeMaterno");
      String[] strCmbCargoId       = request.getParameterValues("cmbCargo");
      String[] strTxtEmail         = request.getParameterValues("txtEmail");      
      String[] strPersonId         = request.getParameterValues("hdnPersonId");
      String[] strFlagSave         = request.getParameterValues("hdnFlagSave");
      
      String[] strCmbTituloNameOld = request.getParameterValues("hdnTituloOld");
      String[] strTxtNombresOld    = request.getParameterValues("hdnNombresOld");
      String[] strTxtApePaternoOld = request.getParameterValues("hdnApePaternoOld");
      String[] strTxtApeMaternoOld = request.getParameterValues("hdnApeMaternoOld");
      String[] strCmbCargoNameOld  = request.getParameterValues("hdnCargoNameOld");
      String[] strTxtEmailOld      = request.getParameterValues("hdnEmailOld");     
      
      String[] strCmbCargoDescrip  = request.getParameterValues("hdmCargoDescrip");
      String[] strIndItems         = request.getParameterValues("indItems");
      
      String[] strCmbEstatus       = request.getParameterValues("cmbEstatus");
      String[] strHdmCmbEstatus    = request.getParameterValues("hdmCmbEstatus");      
            
      String strMessage       =     null;     
      String strContacttype   =     "90";
      //long  lObjectType       =     1; // "PEDIDO";      mandar 2 con el site si tiene
      long  lObjectType;
      
      if(strSiteId.length() ==0){
        System.out.println("No Tiene Site"); 
        strSiteId = "0";
        lObjectType = 1;       
      }else{
        System.out.println("Tiene Site"); 
        lObjectType = 2;       
      }
      
      
      
     //Insertamos según la cantidad de Items que haya
     if( strIndItems!= null){   
     
      int numRows    = Integer.parseInt(MiUtil.getStringObject(strIndItems,0));
      
       for( int i=0; i<numRows; i++){
      
         objContactBean = new ContactObjectBean();                  
                      
         System.out.println("==========================================================================");
         System.out.println("Datos de strCmbTituloNameOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbTituloNameOld,i)));
         System.out.println("Datos de strTxtNombresOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtNombresOld,i)));
         System.out.println("Datos de strTxtApePaternoOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApePaternoOld,i)));
         System.out.println("Datos de strTxtApeMaternoOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApeMaternoOld,i)));
         System.out.println("Datos de strCmbCargoNameOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoNameOld,i)));
         System.out.println("Datos de strTxtEmailOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtEmailOld,i)));
         System.out.println("Datos de strCmbTitulo ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbTitulo,i)));
         System.out.println("Datos de strTxtNombres ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtNombres,i)));
         System.out.println("Datos de strTxtApePaterno ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApePaterno,i)));
         System.out.println("Datos de strTxtApeMaterno ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApeMaterno,i)));
         System.out.println("Datos de strCmbCargoNameOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoId,i)));
         System.out.println("Datos de strTxtEmail ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtEmail,i)));
         System.out.println("Datos de strPersonId ["+MiUtil.getStringNull(MiUtil.getStringObject(strPersonId,i)));
         System.out.println("Datos de strContacttype ["+MiUtil.getStringNull(strContacttype));         
         System.out.println("Datos de strCmbCargoDescrip ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoDescrip,i)));
         System.out.println("Datos de strHdmCmbEstatus ["+MiUtil.getStringNull(MiUtil.getStringObject(strHdmCmbEstatus,i)));
         System.out.println("==========================================================================");
         
         if (MiUtil.getStringObject(strHdmCmbEstatus,i).trim().equals("Nuevo")){         
              System.out.println("--------REGISTRO ES NUEVO-------------");              
               objContactBean.setSwtitle(MiUtil.getStringObject(strCmbTitulo,i));        
               objContactBean.setSwfirstname(MiUtil.getStringObject(strTxtNombres,i));
               objContactBean.setSwlastname(MiUtil.getStringObject(strTxtApePaterno,i));
               objContactBean.setSwmiddlename(MiUtil.getStringObject(strTxtApeMaterno,i));
               objContactBean.setSwjobtitle(MiUtil.getStringObject(strCmbCargoDescrip,i));          
               objContactBean.setSwemailaddress(MiUtil.getStringObject(strTxtEmail,i));               
               objContactBean.setNptitlenew(MiUtil.getStringObject(strCmbTitulo,i));        
               objContactBean.setNpfirstnamenew(MiUtil.getStringObject(strTxtNombres,i));
               objContactBean.setNplastnamenew(MiUtil.getStringObject(strTxtApePaterno,i));
               objContactBean.setNpmiddlenamenew(MiUtil.getStringObject(strTxtApeMaterno,i)); 
               objContactBean.setNpjobtitlenew(MiUtil.getStringObject(strCmbCargoDescrip,i));
               objContactBean.setNpemailnew(MiUtil.getStringObject(strTxtEmail,i));  
               //objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,i));                   
               objContactBean.setNpaction(MiUtil.getStringObject(strHdmCmbEstatus,i));                   
               
         }else{           
               System.out.println("--------REGISTRO EXISTENTE-------------");
               // Si ya existe datos
               objContactBean.setSwtitle(MiUtil.getStringObject(strCmbTituloNameOld,i));        
               objContactBean.setSwfirstname(MiUtil.getStringObject(strTxtNombresOld,i));
               objContactBean.setSwlastname(MiUtil.getStringObject(strTxtApePaternoOld,i));
               objContactBean.setSwmiddlename(MiUtil.getStringObject(strTxtApeMaternoOld,i));
               objContactBean.setSwjobtitle(MiUtil.getStringObject(strCmbCargoNameOld,i));          
               objContactBean.setSwemailaddress(MiUtil.getStringObject(strTxtEmailOld,i));               
               objContactBean.setNptitlenew(MiUtil.getStringObject(strCmbTitulo,i));        
               objContactBean.setNpfirstnamenew(MiUtil.getStringObject(strTxtNombres,i));
               objContactBean.setNplastnamenew(MiUtil.getStringObject(strTxtApePaterno,i));
               objContactBean.setNpmiddlenamenew(MiUtil.getStringObject(strTxtApeMaterno,i)); 
               objContactBean.setNpjobtitlenew(MiUtil.getStringObject(strCmbCargoDescrip,i));
               objContactBean.setNpemailnew(MiUtil.getStringObject(strTxtEmail,i)); 
                    
                    // Hacer validacion el estado es Eliminado 
                  //if (MiUtil.getStringObject(strCmbEstatus,i).equals("Eliminado")){
                  if (MiUtil.getStringObject(strCmbEstatus,contador).equals("Eliminado")){
                   
                   System.out.println("----- Ingreso a Eliminado -------");
                   //objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,i));     
                   objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,contador));     
                    
                  }else{                
                    // Hacer validacion cuando se modifica la informacion.
                    System.out.println("----- Ingreso a Otros (Modificado) -------");
                    //objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,i));    
                    objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,contador));                     
                  }
                  
               contador++; 
          }         
            objContactBean.setNpcreatedby(strLogin);
            objContactBean.setSwpersonid(MiUtil.parseLong(MiUtil.getStringObject(strPersonId,i)));
            objContactBean.setNpcontacttype(strContacttype);         
            objContactBean.setNpitemid(i);
            objContactBean.setSwsiteid(Long.parseLong(strSiteId));
         
         strMessage=objCustomerDAO.insChangeContact(lOrderId, lObjectType, lCustomerId, objContactBean, conn); 
         
          
          System.out.println("strSiteId strSiteId == hrm =="+strSiteId);
          System.out.println("request SITE ["+request+"]");
          //strMessage = "SITE "+strSiteId;
         
         if (strMessage!=null)
         return strMessage;  
         
       }
    }
    
    return strMessage;        
  }  
  
  /**
  * Motivo:  Metodo que contiene la lógica de la modificación de datos del contacto tipo pedido de un cliente
  * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
  * <br>Fecha: 18/06/2009
  * @param      RequestHashMap
  * @param      Connection 
  * @return     String 
  */   
  public String updChangeContactInfo(RequestHashMap request,java.sql.Connection conn){
   
     System.out.println("Actualización de Contacto Pedido");  
     //System.out.println("REQUEST UPDATE ["+request+"]");
             
     CustomerDAO objCustomerDAO=new CustomerDAO();
     ContactObjectBean objContactBean = null;
     
      String  strLogin             =  request.getParameter("hdnUserName");
      long lCustomerId             = (request.getParameter("hdnCustomerId")==null?0:MiUtil.parseLong(request.getParameter("hdnCustomerId")));    
      long lOrderId                = (request.getParameter("hdnNumeroOrder")==null?0:MiUtil.parseLong(request.getParameter("hdnNumeroOrder")));               
      String strSiteId             =  request.getParameter("txtSiteId")==null?"0":request.getParameter("txtSiteId");            ;  // //hdnSite
      
      String[] strCmbTitulo        = request.getParameterValues("cmbTitulo");
      String[] strTxtNombres       = request.getParameterValues("txtNombres");
      String[] strTxtApePaterno    = request.getParameterValues("txtApePaterno");
      String[] strTxtApeMaterno    = request.getParameterValues("txtApeMaterno");
      String[] strCmbCargoId       = request.getParameterValues("cmbCargo");      
      String[] strTxtEmail         = request.getParameterValues("txtEmail");      
      String[] strPersonId         = request.getParameterValues("hdnPersonId");
      String[] strFlagSave         = request.getParameterValues("hdnFlagSave");
      
      String[] strCmbTituloNameOld = request.getParameterValues("hdnTituloOld");
      String[] strTxtNombresOld    = request.getParameterValues("hdnNombresOld");
      String[] strTxtApePaternoOld = request.getParameterValues("hdnApePaternoOld");
      String[] strTxtApeMaternoOld = request.getParameterValues("hdnApeMaternoOld");
      String[] strCmbCargoNameOld  = request.getParameterValues("hdnCargoNameOld");
      String[] strTxtEmailOld      = request.getParameterValues("hdnEmailOld");  
      
      String[] strCmbCargoDespNew  = request.getParameterValues("hdmCargoDescrip");            
      String[] strIndItems         = request.getParameterValues("indItems");       
      String[] strNpitemid         = request.getParameterValues("hdnNpitemid"); 
      String[] strNpaction         = request.getParameterValues("hdnNpaction"); // de la orden en contact change      
      //String[] strCmbEstatus       = request.getParameterValues("cmbEstatus");
     
      String   strMessage          = null;
      int      contador            = 0;
      
      //String strObjectType    = "1"; // customer
      //long   lObjectType      =  1; // "PEDIDO";  
      long   lObjectType;       
      String strContacttype   =     "90";
      
      
      if(strSiteId.equals("0")){
        System.out.println("No Tiene Site"); 
        //strSiteId = "0";
        lObjectType = 1;   // customer    
      }else{
        System.out.println("Tiene Site"); 
        lObjectType = 2;  // site     
      }
      
      //String strEstadoOrden = "ADM_VENTAS"; //(request.getParameter("txtEstadoOrden")==null?"":MiUtil.getString(request.getParameter("txtEstadoOrden")));
      //String strEstadoOrden = request.getParameter("txtEstadoOrden")==null?"":MiUtil.getString(request.getParameter("txtEstadoOrden"));
      String strAccion      = request.getParameter("cmbAction")==null?"":MiUtil.getString(request.getParameter("cmbAction"));
     
     
      System.out.println("=========== Henry ================");
      System.out.println("strLogin    ["+strLogin+"]");
      System.out.println("lCustomerId ["+lCustomerId+"]");
      System.out.println("lOrderId    ["+lOrderId+"]");
      System.out.println("strSiteId   ["+strSiteId+"]");
      System.out.println("strAccion   ["+strAccion+"]");      
      System.out.println("=========== Henry ================");
     
     
      //strMessage  = objCustomerDAO.updChangeContactAction(lOrderId, strObjectType, lCustomerId, "Eliminado", conn);
      strMessage  = objCustomerDAO.updChangeContactAction(lOrderId, String.valueOf(lObjectType), lCustomerId, "Eliminado", conn);
     
     //Insertamos según la cantidad de Items que haya
     if( strIndItems!= null){        
       int numRows    = Integer.parseInt(MiUtil.getStringObject(strIndItems,0));
      
       for( int i=0; i<numRows; i++){
       
         System.out.println("==================== Parametros - updChangeContactInfo =======================================");
         System.out.println("Datos de lOrderId    ["+lOrderId+"]");
         System.out.println("Datos de lObjectType ["+lObjectType+"]");
         System.out.println("Datos de lCustomerId ["+lCustomerId+"]");
         System.out.println("Datos de strSiteId   ["+strSiteId+"]");
         System.out.println("Datos de strCmbTituloNameOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbTituloNameOld,i)));
         System.out.println("Datos de strTxtNombresOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtNombresOld,i)));
         System.out.println("Datos de strTxtApePaternoOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApePaternoOld,i)));
         System.out.println("Datos de strTxtApeMaternoOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApeMaternoOld,i)));
         System.out.println("Datos de strCmbCargoNameOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoNameOld,i)));
         System.out.println("Datos de strTxtEmailOld ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtEmailOld,i)));
         System.out.println("Datos de strCmbTitulo ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbTitulo,i)));
         System.out.println("Datos de strTxtNombres ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtNombres,i)));
         System.out.println("Datos de strTxtApePaterno ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApePaterno,i)));
         System.out.println("Datos de strTxtApeMaterno ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtApeMaterno,i)));
         System.out.println("Datos de strCmbCargoId ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoId,i)));
         System.out.println("Datos de strCmbCargoDescrip ["+MiUtil.getStringNull(MiUtil.getStringObject(strCmbCargoDespNew,i)));         
         System.out.println("Datos de strTxtEmail ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtEmail,i)));
         System.out.println("Datos de strPersonId ["+MiUtil.getStringNull(MiUtil.getStringObject(strPersonId,i)));                           
         System.out.println("Datos de strNpitemid ["+MiUtil.getStringNull(MiUtil.getStringObject(strNpitemid,i)));    
         System.out.println("Datos de strNpaction ["+MiUtil.getStringNull(MiUtil.getStringObject(strNpaction,i)));                 
         System.out.println("===========================================================");
              
        objContactBean = new ContactObjectBean();   
        objContactBean.setSwtitle(MiUtil.getStringObject(strCmbTituloNameOld,i));        
        objContactBean.setSwfirstname(MiUtil.getStringObject(strTxtNombresOld,i));
        objContactBean.setSwlastname(MiUtil.getStringObject(strTxtApePaternoOld,i));
        objContactBean.setSwmiddlename(MiUtil.getStringObject(strTxtApeMaternoOld,i));
        objContactBean.setSwjobtitle(MiUtil.getStringObject(strCmbCargoNameOld,i));          
        objContactBean.setSwemailaddress(MiUtil.getStringObject(strTxtEmailOld,i));     
        objContactBean.setNptitlenew(MiUtil.getStringObject(strCmbTitulo,i));        
        objContactBean.setNpfirstnamenew(MiUtil.getStringObject(strTxtNombres,i));
        objContactBean.setNplastnamenew(MiUtil.getStringObject(strTxtApePaterno,i));
        objContactBean.setNpmiddlenamenew(MiUtil.getStringObject(strTxtApeMaterno,i));         
        objContactBean.setNpjobtitlenew(MiUtil.getStringObject(strCmbCargoDespNew,i));        
        objContactBean.setNpemailnew(MiUtil.getStringObject(strTxtEmail,i));
        objContactBean.setSwsiteid(Long.parseLong(strSiteId));
        
        // Validacion para los datos que seran actualizados - Los new son null
        if (MiUtil.getStringObject(strNpitemid,i).length()>0){
          objContactBean.setNpitemid(Integer.parseInt(MiUtil.getStringObject(strNpitemid,i)));
        }
                
            // Validacion si es Nuevo
        if ( MiUtil.getStringObject(strNpaction,i).length() == 0 || MiUtil.getStringObject(strNpaction,i).trim().equals("Nuevo")){
        
          System.out.println("----------------- NUEVO REGISTRO -----------------");
          objContactBean.setNpaction(MiUtil.getStringObject(strNpaction,i));
          
          // Validamos si ya esta registrado desde el crear - Update
          if( MiUtil.getStringObject(strNpaction,i).length() > 0 && MiUtil.getStringObject(strCmbTituloNameOld,i).length()>0){
            System.out.println("--------NUEVO REGISTRO - EXISTE EN CONTACT CHANGE - ACTUALIZA------------");
            
            //strMessage  = objCustomerDAO.updChangeContact(lOrderId,strObjectType,lCustomerId,objContactBean,conn); //update np_contact_change
            strMessage  = objCustomerDAO.updChangeContact(lOrderId,String.valueOf(lObjectType),lCustomerId,objContactBean,conn); //update np_contact_change
                        
          }else{
            // Validamos si se creo en editar por lo tanto no va tener valores old - Insert                        
            System.out.println("--------NO EXISTE EN CONTACT CHANGE - INSERTA------------");
            
               objContactBean.setSwtitle(MiUtil.getStringObject(strCmbTitulo,i));        
               objContactBean.setSwfirstname(MiUtil.getStringObject(strTxtNombres,i));
               objContactBean.setSwlastname(MiUtil.getStringObject(strTxtApePaterno,i));
               objContactBean.setSwmiddlename(MiUtil.getStringObject(strTxtApeMaterno,i));
               objContactBean.setSwjobtitle(MiUtil.getStringObject(strCmbCargoDespNew,i));          
               objContactBean.setSwemailaddress(MiUtil.getStringObject(strTxtEmail,i));
               objContactBean.setNpcontacttype(strContacttype); 
               objContactBean.setNpaction("Nuevo");
               objContactBean.setNpcreatedby(strLogin);
               
               strMessage= objCustomerDAO.insChangeContact(lOrderId, lObjectType, lCustomerId, objContactBean, conn);   
          }
        }else{
            // Validacion si ya es Existente en swperson - Update
          System.out.println("----------------- REGISTRO EXISTENTE SWPERSON - ACTUALIZA -----------------"); 
          if (!MiUtil.getStringObject(strNpaction,i).trim().equals("Eliminado")){ 
          
            //objContactBean.setNpaction(MiUtil.getStringObject(strCmbEstatus,contador));          
            objContactBean.setNpaction(MiUtil.getStringObject(strNpaction,i)); 
            //strMessage  = objCustomerDAO.updChangeContact(lOrderId,strObjectType,lCustomerId,objContactBean,conn); //update np_contact_change
            strMessage  = objCustomerDAO.updChangeContact(lOrderId,String.valueOf(lObjectType),lCustomerId,objContactBean,conn); //update np_contact_change
            
          }  
            contador++;
          
        }
       }
     }    
     if( strMessage != null ) return strMessage;                  
     
     return strMessage;   
  }  
}