package pe.com.nextel.test;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import pe.com.nextel.bean.*;
import pe.com.nextel.dao.*;

public class Test {
    public Test() {
    }

    public static void main(String[] args) {
        Test test = new Test();
        HashMap h = new HashMap();
        ResultSet rs = null;
        
      /*
        // Prueba de customer
         //1.1 Sección de Captura de parametros
         //------------------------------------
         int intCodigoCliente = 2;
         
         //1.2 Sección de proceso y captura de datos
         //-----------------------------------------
         
          int wn_swcustomerid = 0;   
          String wv_swname    = "";         
          String wv_swnamecom = ""; 
          String wv_swruc = "";          
          String wv_codbscs = "";       
          String wv_swtype = ""; 
          String wv_swrating = "";        
          String wv_nplineacredito = "";  
          String wv_swmainphone = ""; 
          String wv_swmainfax = "";       
          String wv_swnameRegion = "";  
          String wv_swnameGiro = "";       
          String wv_tipoCuenta = "";      
          int wn_vendedorid = 0;  
          String wv_vendedorname = "";    
          String wv_dealer = ""; 
          String wv_domiciliodir = "";     
          String wv_entregadir = "";      
          String wv_cuName = "";      
          String wv_cuTele = "";          
          String wv_cuFax = ""; 
          String wv_cgName = "";           
          String wv_cgTele = "";          
          String wv_cgFax = "";        
          String wv_facturacionDir = ""; 
          String wv_entregacDir = ""; 
          String wv_comunicacionDir = "";  
          String wv_cfName = "";          
          String wv_cfTele = "";       
          String wv_cfFax = ""; 
          String wv_npcustomerrelationtype = "";     
          String wn_npcustomerrelationid = "";    
          String wv_swname_customerrelation = "";
          //531165
        h = CustomerDAO.getCustomerData(888,"DTEODOSIO",110);
        
        System.out.println(" Test  "  );
        System.out.println("-----------------------------");
        System.out.println(" tamaño de h =" + h.size());
             
             if (h.size() != 0 ) {

                 // Datos del HastTable
                  wn_swcustomerid = Integer.parseInt((String)h.get("wn_swcustomerid"));
                  
                  wv_swname    = (String)h.get("wv_swname");         
                  wv_swnamecom = (String)h.get("wv_swnamecom"); 
                  
                  wv_swruc = (String)h.get("wv_swruc");          
                  wv_codbscs = (String)h.get("wv_codbscs");       
                  wv_swtype = (String)h.get("wv_swtype"); 
                  wv_swrating = (String)h.get("wv_swrating");        
                  wv_nplineacredito = (String)h.get("wv_nplineacredito");  
                  wv_swmainphone = (String)h.get("wv_swmainphone"); 
                  wv_swmainfax = (String)h.get("wv_swmainfax");       
                  wv_swnameRegion = (String)h.get("wv_swnameregion");  
                  wv_swnameGiro = (String)h.get("wv_swnamegiro");       
                  wv_tipoCuenta = (String)h.get("wv_tipocuenta");      
                  wn_vendedorid = Integer.parseInt((String)h.get("wn_vendedorid"));  
                  wv_vendedorname = (String)h.get("wv_vendedorname");    
                  wv_dealer = (String)h.get("wv_dealer"); 
                  
                  wv_domiciliodir = (String)h.get("wv_domiciliodir");     
                  wv_entregadir = (String)h.get("wv_entregadir");      
                  wv_cuName = (String)h.get("wv_cuname");      
                  wv_cuTele = (String)h.get("wv_cutele");          
                  wv_cuFax = (String)h.get("wv_cufax"); 
                  wv_cgName = (String)h.get("wv_cgname");           
                  wv_cgTele = (String)h.get("wv_cgtele");          
                  wv_cgFax = (String)h.get("wv_cgfax");        
                  wv_facturacionDir = (String)h.get("wv_facturaciondir"); 
                  wv_entregacDir = (String)h.get("wv_entregacdir"); 
                  wv_comunicacionDir = (String)h.get("wv_comunicaciondir");  
                  wv_cfName = (String)h.get("wv_cfname");          
                  wv_cfTele = (String)h.get("wv_cftele");       
                  wv_cfFax = (String)h.get("wv_cffax"); 
                  
                  wv_npcustomerrelationtype = (String)h.get("wv_npcustomerrelationtype");     
                  wn_npcustomerrelationid = (String)h.get("wn_npcustomerrelationid");
                  wv_swname_customerrelation = (String)h.get("wv_swname_customerrelation");
                 
             }

         
         //1.3 Sección de visualización de datos
         //-------------------------------------
          
         System.out.println("form.txtCompany.value = '" + wv_swname + "';");
         System.out.println("if (form.txtCompany!=null)              form.txtCompany.value                = '" + wv_swname + "';");
        
         System.out.println("if (form.hdnCustomerName!=null)         form.hdnCustomerName.value           = '" + wv_swname + "';");
         
         System.out.println("if (form.txtCompanyNameCom!=null)       form.txtCompanyNameCom.value         = '" + wv_swnamecom+ "';");
         System.out.println("if (form.txtCompanyId!=null)            form.txtCompanyId.value              = '" + wn_swcustomerid+ "';");
         System.out.println("if (form.txtCodBSCS!=null)              form.txtCodBSCS.value                = '" + wv_codbscs+ "';");
        
         System.out.println("if (form.txtRucDisabled!=null)          form.txtRucDisabled.value            = '" + wv_swruc+ "';");
         System.out.println("if (form.txtRegion!=null)               form.txtRegion.value                 = '" + wv_swnameRegion + "';");
         System.out.println("if (form.txtIndustria!=null)            form.txtIndustria.value              = '" + wv_swnameGiro + "';");
         System.out.println("if (form.txtTipoCuenta!=null)           form.txtTipoCuenta.value             = '" + wv_tipoCuenta + "';");
         System.out.println("if (form.txtTipoCompania!=null)         form.txtTipoCompania.value           = '" + wv_swtype + "';");
         System.out.println("if (form.cmbVendedor!=null)             form.cmbVendedor.value               = '" + wn_vendedorid + "';");
         System.out.println("if (form.cmbVendedor!=null)             form.hdnOrderCreator.value           = '" + wn_vendedorid + "';");
         System.out.println("if (form.hdnVendedor!=null)             form.hdnVendedor.value               = '" + wn_vendedorid + "';");
         System.out.println("if (form.txtDealer!=null)               form.txtDealer.value                 = '" + wv_dealer + "';");
         System.out.println("if (form.txtScoring!=null)              form.txtScoring.value                = '" + wv_swrating + "';");
         System.out.println("if (form.txtLineaCredito!=null)         form.txtLineaCredito.value           = '" + wv_nplineacredito + "';");
         System.out.println("if (form.txtTipoCliente!=null)          form.txtTipoCliente.value            = '" + wv_npcustomerrelationtype+ "';");
         System.out.println("if (form.txtClientePrincipal!=null)     form.txtClientePrincipal.value       = '" + wv_swname_customerrelation + "';");
         System.out.println("if (form.txtTelefono!=null)             form.txtTelefono.value               = '" + wv_swmainphone + "';");
         System.out.println("if (form.txtFax!=null)                  form.txtFax.value                    = '" + wv_swmainfax + "';");
        
        
         
        /*
          //No considerar
          System.out.println("if (form.txtDirLegal!=null)             form.txtDirLegal.value               = '" + wv_domiciliodir + "';");
          System.out.println("if (form.txtDirEntrega!=null)           form.txtDirEntrega.value             = '" + wv_entregacDir + "';");
         System.out.println("if (form.txtContactoUsuario!=null)      form.txtContactoUsuario.value        = '" + wv_cuName + "';");
         System.out.println("if (form.txtTelefonoContacto!=null)     form.txtTelefonoContacto.value       = '" + wv_cuTele + "';");
         System.out.println("if (form.txtFaxContacto!=null)          form.txtFaxContacto.value            = '" + wv_cuFax + "';");
         System.out.println("if (form.txtGerenteGeneral!=null)       form.txtGerenteGeneral.value         = '" + wv_cgName + "';");
         System.out.println("if (form.txtTelefonoContGeneral!=null)  form.txtTelefonoContGeneral.value    = '" + wv_cgTele + "';");
         System.out.println("if (form.txtFaxContGeneral!=null)       form.txtFaxContGeneral.value         = '" + wv_cgFax + "';");
         System.out.println("if (form.txtDirFacturacion!=null)       form.txtDirFacturacion.value         = '" + wv_facturacionDir + "';");
         System.out.println("if (form.txtDirCorrespondencia!=null)   form.txtDirCorrespondencia.value     = '" + wv_comunicacionDir + "';");
         System.out.println("if (form.txtContactoPagoFact!=null)     form.txtContactoPagoFact.value       = '" + wv_cfName + "';");
         System.out.println("if (form.txtTelefonoContPagoFact!=null) form.txtTelefonoContPagoFact.value   = '" + wv_cfTele + "';");
         System.out.println("if (form.txtFaxContPagoFact!=null)      form.txtFaxContPagoFact.value        = '" + wv_cfFax + "';");
         
         ------------------------------------------------------------------------------------------------
         */
         
        // Prueba de COMBO ORDER_TYPE_SEARCH_CUSTOMER  --> NP_TABLE
        // -----------------------------------------------------
        
         /*
         GeneralDAO general = new GeneralDAO();
        
         ArrayList l = general.getComboList("ORDER_TYPE_SEARCH_CUSTOMER");
         
         String strError   = null;
         strError = general.getError();
        
         for ( int i=0; i<l.size(); i++ ){
           
               h = (Hashtable)l.get(i);
               System.out.println("<option value='" + h.get("wn_npvalue") + ">" +  h.get("wv_npvaluedesc")  + "</option>");
                  
         }*/
         
         // Prueba de COMBO Region --> utilizado en JSp Order_ClienteBuscar
         // -----------------------------------------------------
    
          /*
          GeneralDAO general = new GeneralDAO();
     
          ArrayList l = general.getComboRegionList();
          String strError   = null;
          strError = general.getError();
         
         
          if ( strError == null || strError.substring(0,8).equals("ORA-0000")  ){
               for ( int i=0; i<l.size(); i++ ){ 
                  Hashtable h1 = (Hashtable)l.get(i); 

                  System.out.println("<option value='" + h1.get("wn_npregion")+ ">" + h1.get("wv_npregiondesc") + "</option>");

           
             }

           }
          
        */
         
         
        
        // Prueba de DIRECCIONES
        // -----------------------------------------------------
        
         /*
         
         CustomerDAO cust = new CustomerDAO();
        
         ArrayList l = cust.getCustomerAddress(888,"CUSTOMER");
         
         String strError   = null;
       
         try {
               
                for( int i=0; i<l.size();i++ ){
                
                     h = (Hashtable)l.get(i);
                     
                     System.out.println( h.get("wn_swaddressid") + " - " + h.get("wv_swaddress1") + " - " + h.get("wv_swcity") + " - " + h.get("wv_swprovince") + " - " + h.get("wv_swstate") + " - " + h.get("wv_swflaglegal")+ " - " + h.get("wv_swflagfacturacion")+ " - " + h.get("wv_swflagentrega") + " - " + h.get("wv_mensaje") );
              
                }
                
               
            
         }
         catch(Exception e) {
               System.out.println("strError = " + strError );
               e.printStackTrace();
               
         }
         */
         
        // Prueba de CONTACTOS
        // -----------------------------------------------------
        /*
         
         
         CustomerDAO cust = new CustomerDAO();
        
         ArrayList l = cust.getCustomerContacts(888,"CUSTOMER");
         
         String strError   = null;
        
         
               
        for( int i=0; i<l.size();i++ ){
        
             h = (Hashtable)l.get(i);
             
             System.out.println( (i+1) + " : " + h.get("wn_swpersonid") + " - " + h.get("wv_swname") + " - " + h.get("wv_swphonearea") + " - " + h.get("wv_swphone") + " - " + h.get("wv_swfaxarea") + " - " + h.get("wv_swfax") + " - " + h.get("wv_swflagusuario") + " - " + h.get("wv_swflagfacturacion") + " - " + h.get("wv_swflaggerenteg"));
             
            
        }
               
        */   
         

          // Prueba de SITES
          // -----------------------------------------------------
          
       

           /*ArrayList l=null;
           CustomerService cust = new CustomerService();
           String strMsgError=null;
           
           l =cust.getCustomerSites( 27300,"Activo",strMsgError);
                      
           String strError   = null;
                 
          for( int i=0; i<l.size();i++ ){
          
               h = (HashMap)l.get(i);
               
               System.out.println( (i+1) + " : " + h.get("swsiteid") + " - " + h.get("swsitename") + " - " + h.get("npcodbscs") );
               
              
          }*/
                
            
              
        
        
         //------------------------------------------------------------------------------------------------
         // Prueba de SESSIONES
         // -----------------------------------------------------
         /*
         
         PortalSessionBean s = new PortalSessionBean();
         
         PortalSessionDAO.ubicar( "98102396",Integer.parseInt("27"),s);
         
         
         // Variable de session
         //----------------------------------------------------------------
         
         
          System.out.println("s.getPersonid() =" + s.getPersonid()); 
          System.out.println("s.getPersonid() =" + s.getCreatedby());
          System.out.println("s.getPersonid() =" + s.getBuildingid()+"");
          System.out.println("s.getPersonid() =" + s.getAppid()+"");
          System.out.println("s.getPersonid() =" + s.getLevel()+"");
          System.out.println("s.getPersonid() =" + s.getCode());
          System.out.println("s.getPersonid() =" + s.getUserid()+"");
          
          System.out.println("s.getLogin() =" + s.getLogin() );
          System.out.println("s.getDn_Num() =" + s.getDn_Num() );
        
          System.out.println("s.getPersonid() =" + s.getType() );
          System.out.println("s.getPersonid() =" + s.getNom_user() );
          System.out.println("s.getPersonid() =" + s.getBusunitId()+"" );
          System.out.println("s.getRegionId() =" + s.getRegionId()+"" );
          System.out.println("s.getPersonid() =" + s.getChnlPartnerId() +"");
          System.out.println("s.getPersonid() =" + s.getPartnerCodBscs() );
          System.out.println("s.getPersonid() =" + s.getProviderGrpId()+"" );
          System.out.println("s.getPersonid() =" + s.getTerritoryId()+"" );
          
          
         //pSession.setAttribute("rolId",s.getRolId()+"" );
          System.out.println("s.getPersonid() =" + s.getAreaId()+"" );
          System.out.println("s.getPersonid() =" +  s.getAreaName()+"" );
          System.out.println("s.getPersonid() =" + s.getVicepresidenciaId()+"" );
          System.out.println("s.getPersonid() =" + s.getDefaultInBox()+"" );
          System.out.println("s.getPersonid() =" + s.getCashDeskId()+"" );
          System.out.println("s.getPersonid() =" + s.getIdApp()+"" );
          System.out.println("s.getPersonid() =" + s.getAppName()+"" ); 
          System.out.println("s.getPersonid() =" + s.getHome()+"" );
          
          System.out.println("s.getPersonid() =" + s.getMessage());
          
          */
         
         // Prueba de BUSQUEDA DE CLIENTES
         // -----------------------------------------------------
        /*
         CustomerDAO cust = new CustomerDAO();
         
         ArrayList l = cust.getCustomerSearch(null,null,"BANCO", null,null,0,0,"SRVT",2);
               
         for( int i=0; i<l.size();i++ ){
         
             h = (Hashtable)l.get(i);
             
             System.out.println( (i+1) + " : " + h.get("wv_swruc") + " - " + h.get("wv_swname") + " - " + h.get("wv_swnamecom") + " - " + h.get("wv_swtype") + " - " + h.get("wv_swcreatedby") );
             
         }
         */
          
        // Prueba de OrderDAO.getBuildingName --> Obtiene el id de la Tienda y Nombre de la Región de Trámite
        // -----------------------------------------------------               
        // Falta privilegios de orders a websales
        /*
         h = OrderDAO.getBuildingName(21,"DTEODOSIO");
         
         System.out.println(" Test - NOMBRE DE TIENDA Y REGION DE TRAMITE"  );
         System.out.println("-----------------------------");
              
          if (h.size() != 0 ) {

            System.out.println( "wv_name       = " + h.get("wv_name") );
            System.out.println( "wn_regionid   = " + h.get("wn_regionid"));
            System.out.println( "wv_regionname = " + h.get("wv_regionname"));
            System.out.println( "wv_mensaje    = " + h.get("wv_mensaje"));
               
          }
          */
         
         // Prueba de OrderDAO.getPersonBuilding --> Obtener campos del Person y Building del personId ingresado
         // -----------------------------------------------------
         // Falta privilegios de orders a websales
        /*
         ArrayList l = OrderDAO.getPersonBuilding(139261);
               
         for( int i=0; i<l.size();i++ ){
         
              h = (Hashtable)l.get(i);
              
              System.out.println( (i+1) + " : " + h.get("wn_npuserid") + " - " + h.get("wv_npnombre") + " - " + h.get("wv_nplogin") + " - " + h.get("wn_npbuildingid") + " - " + h.get("wn_npcode") + " - " + h.get("wv_npname") + " - " + h.get("wn_npactive") + " - " + h.get("wn_npregionid") + " - " + h.get("wv_nptype") + " - " + h.get("wv_npshortname") + " - " + h.get("wv_npprocessgroup") );
             
             
         } */

         // Prueba de OrderDAO.getCategoryList --> Obtener la lista e categoria
         // -----------------------------------------------------
         
         /*
         ArrayList l = OrderDAO.getSolutionList("");
         System.out.println ( "l.size() =" + l.size() ) ;
         
         for( int i=0; i<l.size();i++ ){
         
              h = (Hashtable)l.get(i);
              
              System.out.println( (i+1) + " : " + h.get("wn_npsolutionid") + " - " + h.get("wv_npname") );
             
             
         }*/
          
          
        // Prueba de OrderDAO.getCategoryList --> Obtener la lista e categoria
        // -----------------------------------------------------
        // Falta privilegios de orders a websales
       /*
        ArrayList l = OrderDAO.getCategoryList(2);
        System.out.println ( "l.size() =" + l.size() ) ;
        
        for( int i=0; i<l.size();i++ ){
        
             h = (Hashtable)l.get(i);
             
             System.out.println( (i+1) + " : " + h.get("wv_npType") );
            
            
        }
         */
        // Prueba de OrderDAO.getSubCategoryList --> Obtener la lista de Subcategoria
        // -----------------------------------------------------
        // Falta privilegios de orders a websales
        /*
        ArrayList l = OrderDAO.getSubCategoryList("VENTA MOVILES");
              
        for( int i=0; i<l.size();i++ ){
        
             h = (Hashtable)l.get(i);
             
             System.out.println( (i+1) + " : " + h.get("wn_npSpecificationId") + " - " +  h.get("wv_npSpecification") );
            
            
        }*/
        
          
          
         // Prueba de OrderDAO.getDispatchPlaceList --> Obtener la lista de lugares de despacho
         // -----------------------------------------------------
         // Falta privilegios de orders a websales
         /*
         ArrayList l = OrderDAO.getDispatchPlaceList(1);
               
         for( int i=0; i<l.size();i++ ){
         
              h = (Hashtable)l.get(i);
              
              System.out.println( (i+1) + " : " + h.get("wn_npBuildingId") + " - " +  h.get("wv_npShortName") );
             
             
         }*/
         
          // Prueba de OrderDAO.getModePaymentList --> Obtener la lista de Modos de pagos
          // -----------------------------------------------------
          // Falta privilegios de orders a websales
          /*
          ArrayList l = OrderDAO.getModePaymentList("PAY_FORM","1");
                
          for( int i=0; i<l.size();i++ ){
          
               h = (Hashtable)l.get(i);
               
               System.out.println( (i+1) + " : " + h.get("wv_npValue") + " - " +  h.get("wv_npValueDesc") + " - " + h.get("wv_npTag1") + " - " +  h.get("wv_npTag2") + " - " +  h.get("wn_npOrder") );
              
              
          }*/
          
           // Prueba de OrderDAO.getSalesList --> Obtener la lista de vendedores
           // -----------------------------------------------------
           // Falta privilegios de orders a websales
           /*
           ArrayList l = OrderDAO.getSalesList(139261,382); //382
                 
           for( int i=0; i<l.size();i++ ){
           
                h = (Hashtable)l.get(i);
                
                System.out.println( (i+1) + " : " + h.get("wn_npCampo01") + " - " +  h.get("wv_npCampo02") + " - " + h.get("wv_npCampo03")  );
              
               
           }*/
           
            // Prueba de OrderDAO.getCustPatternQty --> Obtener la lista de compañia
            // -----------------------------------------------------
              /*        
             h = CustomerDAO.getCustPatternQty("BANCO DE LA");
                  
             System.out.println( "wv_name = " + h.get("wv_name") );
             System.out.println( "wn_customerid = " + h.get("wn_customerid") );
             System.out.println( "wv_message = " + h.get("wv_message") );
             System.out.println( "wn_customer_qty = " + h.get("wn_customer_qty") );

*/
            // Prueba de OrderDAO.getCustRucQty --> Obtener la lista de Ruc
            // -----------------------------------------------------
             /*        
             h = CustomerDAO.getCustRucQty("20100147514");
                  
             System.out.println( "wv_ruc = " + h.get("wv_ruc") );
             System.out.println( "wn_customerid = " + h.get("wn_customerid") );
             System.out.println( "wv_message = " + h.get("wv_message") );
             System.out.println( "wn_customer_qty = " + h.get("wn_customer_qty") );               
              */
         
            // Prueba de OrderDAO.getOrderIdNew --> Obtener un nuevo orderId
            // -----------------------------------------------------
            // Falta privilegios de orders a websales
            /*
            h = OrderDAO.getOrderIdNew();
            
            if ( h!= null) {
                System.out.println( " : " + h.get("wn_orderid") + " - " +  h.get("wv_message") );
            }*/
             
            // Prueba de ESTRACCION DE CADENAS
            // -----------------------------------------------------
             /*
             String strCodigoCliente = "4.1008.10.11.100000";  //
             int nPuntos =0;
             int posicion[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
             int j=0;
             
             for(int i=0; i<strCodigoCliente.length();i++) {
                 
                 System.out.println(" " + i + " ->" + strCodigoCliente.charAt(i) );
                 char dato = '.';
                 
                 if(strCodigoCliente.charAt(i)== dato) {
                     nPuntos ++;
                     posicion[j] = i;
                     j++;
                 }
               
             }
             
                System.out.println(" Conclusion" );
                System.out.println(" ----------" );
                System.out.println(" Numero de puntos -> " + nPuntos );
                System.out.println(" " );
                System.out.println(" Posicion de los puntos");
                
                for(int z=0; z<posicion.length;z++) {
                    if( posicion[z] != 0) {
                        System.out.println(" Posicion " + (z+1) + " punto -> " +posicion[z] );
                            
                    }
                        
                }
                System.out.println(" " );
                System.out.println(" Posicion del 2do punto -> " + posicion[1] );
                System.out.println(" " );
                
                if(posicion[1] == 0) {
                    System.out.println(" No se hizo substraccion de cadena -> " + strCodigoCliente );
                        
                }
                else {
                    System.out.println(" Substraccion de cadena -> " + strCodigoCliente.substring(0,posicion[1]) );
                    
                }
                */

              // Prueba de getRol
              // -----------------------------------------------------
              /*
               int valor = 0;
               valor = CustomerDAO.getRol(2707,139261,27);
               
               System.out.println( " valor = " + valor );
              */
              
               // Prueba de OrderDAO.getPlanList --> Obtener la lista de planes tarifarios
               // -------------------------------------------------------------------------
               // Falta privilegios de orders a websales
               /*
               ArrayList l = OrderDAO.getPlanList("C","Employee");
                     
               for( int i=0; i<l.size();i++ ){
                    h = (Hashtable)l.get(i);
                    System.out.println( (i+1) + " : " + h.get("wv_description") + " - " +  h.get("wn_tmcode") + " - " + h.get("wv_nivelplan")  );
                   
               }           
                */
                
            // Prueba de CustomerDAO.getSiteData --> Obtener los datos del Site
            // ----------------------------------------------------------------
           /*
            h = CustomerDAO.getSiteData(258,"DAVID TEODOSIO",139261,27,110);
 
            if ( h!= null) {
                System.out.println( h.get("wv_message") + " - " +h.get("wv_codbscs") + " - " +  h.get("wv_nplineacredito") + " - " +  h.get("wv_swnameregion") + " - " +  h.get("wn_vendedorid")+ " - " +  h.get("wv_vendedorname") + " - " +  h.get("wv_dealer")   );
            }  
            
            */
            // Prueba de CustomerDAO.getSiteData --> Obtener los datos del Site
            // ----------------------------------------------------------------
             /*
            h = CustomerDAO.getCompanyInfo(888);
             
            if ( h!= null) {
                 System.out.println( h.get("wv_message") + " - " +h.get("wn_codigoCompania") + " - " +  h.get("wv_tipoCompania") + " - " +  h.get("wn_montoGarantia") + " - " +  h.get("wn_valorTotalGarantia")+ " - " +  h.get("wv_nombreCompania")    );
            }  
            */
            
            
              // Prueba de getCustomerIdCrmByBSCS --> Obtener la lista de Ruc
              // -----------------------------------------------------
                    /* 
               h = CustomerDAO.getCustomerIdCrmByBSCS(158);
                    
               System.out.println( "wn_codbscs = " + h.get("wn_codbscs") );
               System.out.println( "wn_customerid = " + h.get("wn_customerid") );
               System.out.println( "wv_message = " + h.get("wv_message") );
              */
           
            
         
         
    }
}
