package pe.com.nextel.dao;


import java.lang.Exception;
import java.lang.Object;
import java.lang.String;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.form.RetailForm;
import pe.com.nextel.util.Constante;


//import oracle.jdbc.OracleCallableStatement;


public class RetailDAO extends GenericDAO {
     
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 12/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * Motivo: Crea un nueva Orden, en base al Formulario de Retail
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 10/08/2007
     * @return	HashMap conteniendo la siguiente información:<br>
	 * <pre>
	 * 1. orderId: Id de la Orden creada.<br>
	 * 2. customerId: Id del Cliente creado (sólo en caso sea Prospect)<br>
	 * 3. statusProcess: Resultado de la ejecución (Si es 0 todo ok)
	 * </pre>
     */
    public HashMap newOrderRetail(RetailForm retailForm) throws SQLException, Exception {
       if (logger.isDebugEnabled())
           logger.debug("--Inicio--");
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       String strMessage = null;
       HashMap hshDataMap = new HashMap();       
       try{
          String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_CREATE_ORDER_FROM_RETAIL(?, ?, ?); END;";
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        
         //Concatenando los numeros de vouchers según el siguiente formato voucher1|voucher2|voucher3
         String strVouchers="";
         for (int i=0;i<retailForm.getTxtNroVoucher().length;i++){
            if(!(retailForm.getTxtNroVoucher()[i].equals(""))){
              strVouchers=strVouchers+retailForm.getTxtNroVoucher()[i]+"|";
           }
         }	
         strVouchers=strVouchers.substring(0,strVouchers.length()-1);
         if(logger.isDebugEnabled())
            logger.debug("Vouchers concatenados: "+strVouchers);		
         //Si el tipo de documento es diferente de RUC se debe guardar en el campo SWNAME el nombre y apellido
         if (!(retailForm.getCmbTipoDocumento().equals(Constante.TIPO_DOC_RUC))){
           retailForm.setTxtRazonSocial(retailForm.getTxtApellidos()+" "+retailForm.getTxtNombres());	
         }
    
         ArrayList arrItemList = new ArrayList();
         StructDescriptor sdItem = StructDescriptor.createDescriptor("ORDERS.TO_ITEM", conn);
         ArrayDescriptor adItemList = ArrayDescriptor.createDescriptor("ORDERS.TT_ITEM_LST", conn);
         StructDescriptor sdOrder = StructDescriptor.createDescriptor("ORDERS.TO_ORDER", conn);
         int a = 0;
         String modelo = "";
         for (int i = 0; i < retailForm.getHdnItem().length; i++) {
            try {
              if (StringUtils.isNotBlank(retailForm.getHdnItem()[i])) {
                  modelo = retailForm.getTxtModelo()[i].toString();
                  if(modelo.length()>20){
                      modelo=modelo.substring(0,20);
                  }
                Object[] objItem = {	retailForm.getTxtIMEI()[i],
                                           retailForm.getTxtEquipo()[i],
                                           retailForm.getTxtServicio()[i],
                                           retailForm.getTxtPlanTarifario()[i],
                                           retailForm.getHdnPlanTarifario()[i],
                                           retailForm.getCmbKit()[a],
                                           retailForm.getHdnKit()[i],
                                           retailForm.getTxtContrato()[i],
                                           retailForm.getTxtNextel()[i],
                                           modelo,
                                           retailForm.getTxtSIM()[i]
                            };
              STRUCT stcItem = new STRUCT(sdItem, conn, objItem);
              arrItemList.add(stcItem);
              a++;
            }
          } catch(StringIndexOutOfBoundsException siobee) {
            if(StringUtils.isBlank(retailForm.getTxtPlanTarifario()[i])) {
              throw new Exception("No hay información relacionada a Plan Tarifario");
            }
          }
        }
        ARRAY aryItemList = new ARRAY(adItemList, conn, arrItemList.toArray());
    
          String direccion1 = "";
          String direccion2 = "";
          if(retailForm.getTxtDireccion()[0].length()>40){
             direccion1 = retailForm.getTxtDireccion()[0].substring(0,40);
             direccion2 = retailForm.getTxtDireccion()[0].substring(40,retailForm.getTxtDireccion()[0].length());
          }else{
              direccion1 = retailForm.getTxtDireccion()[0];
              direccion2 = retailForm.getTxtDireccion()[1];
          }
    
        Object[] objOrder = { retailForm.getCmbTipoDocumento(),
                    retailForm.getHdnTipoDocumento(),
                    retailForm.getTxtNroDocumento(),
                    retailForm.getTxtCodigoBSCS(),
                    retailForm.getTxtTipoCliente(),
                    retailForm.getCmbCuenta(),
                    retailForm.getHdnCuenta(),
                    retailForm.getTxtRazonSocial(),
                    retailForm.getCmbTituloCliente(),
                    retailForm.getHdnTituloCliente(),
                    retailForm.getTxtNombres(),
                    retailForm.getTxtApellidos(),
                    //retailForm.getTxtDireccion()[0],
                    //retailForm.getTxtDireccion()[1],
                    direccion1,
                    direccion2,                
                    retailForm.getCmbDepartamento(),
                    retailForm.getHdnDepartamento(),
                    retailForm.getCmbProvincia(),
                    retailForm.getHdnProvincia(),
                    retailForm.getCmbDistrito(),
                    retailForm.getHdnDistrito(),
                    retailForm.getTxtTelefono(),
                    retailForm.getCmbGiro(),
                    retailForm.getHdnGiro(),
                    retailForm.getCmbSubGiro(),
                    retailForm.getHdnSubGiro(),
                    retailForm.getTxtNroSolicitud(),
                    retailForm.getCmbTienda(),
                    retailForm.getHdnTienda(),
                    retailForm.getTxtObservaciones(),
                    retailForm.getHdnLogin(),
                    aryItemList,
                    strVouchers
                  };
        STRUCT stcOrder = new STRUCT(sdOrder, conn, objOrder);
          System.out.println("[newOrderRetail][Antes STRUCT]");
          cstmt.setSTRUCT(1, stcOrder);
          System.out.println("[newOrderRetail][Despues STRUCT]");
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.STRUCT, "ORDERS.TO_ORDER_RETAIL");
        cstmt.execute();
        strMessage = cstmt.getString(2);
        if(StringUtils.isNotBlank(strMessage)) {
          logger.error(strMessage);
        }
        else {
          STRUCT stcRetail = (STRUCT)cstmt.getObject(3);
          hshDataMap.put("orderId", stcRetail.getAttributes()[0]);
          hshDataMap.put("customerId", stcRetail.getAttributes()[1]);
          hshDataMap.put("statusProcess", stcRetail.getAttributes()[2]);
             hshDataMap.put("cadItem", stcRetail.getAttributes()[3]);
             hshDataMap.put("caditemDev", stcRetail.getAttributes()[4]);
        }
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        logger.debug(hshDataMap);
     
       }catch(Exception e){
         hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage()); 
      }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
           logger.error(formatException(e));
         }
      }   
       
       return hshDataMap;
    }

   /**
     * Motivo: Actualiza campo NPPHONE de la NP_ITEM
     * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
     * <br>Fecha: 16/04/2008     
   */
   public String updPhoneItem(long lItemId,String strPhone)   
   throws SQLException {
      System.out.println("[RetailDAO][updPhoneItem][lItemId]"+lItemId);
      System.out.println("[RetailDAO][updPhoneItem][strPhone]"+strPhone);
      Connection conn = null;
      String strMessage=null;         
      OracleCallableStatement cstmt = null;      
      try{
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_UPD_ITEM_PHONE(?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
        cstmt.setLong(1, lItemId);
        cstmt.setString(2, strPhone);
        cstmt.registerOutParameter(3, Types.VARCHAR);
        int i=cstmt.executeUpdate();   
        strMessage = cstmt.getString(3);           
      }catch(Exception e){
         strMessage = e.getMessage();
      }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
           logger.error(formatException(e));
         }
      }   
      
      return strMessage;
   }
   
   /**
     * Motivo: Actualiza campo NPCONTRACTNUMBER de la NP_ITEM_DEVICE
     * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
     * <br>Fecha: 16/04/2008     
   */
   public String updContractItemDev(long lItemDevId,long iCoId)   
   throws SQLException {
      System.out.println("[RetailDAO][updContractItemDev][lItemDevId]"+lItemDevId);
      System.out.println("[RetailDAO][updContractItemDev][iCoId]"+iCoId);
      Connection conn = null;
      String strMessage=null;   
      OracleCallableStatement cstmt = null;    
      try{
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.SP_UPD_ITEM_DEVICE_CONTRACT(?, ?, ?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);   
        cstmt.setLong(1, lItemDevId);
        cstmt.setLong(2, iCoId);
        cstmt.registerOutParameter(3, Types.VARCHAR);
        int i=cstmt.executeUpdate();   
        strMessage = cstmt.getString(3);           
      }catch(Exception e){
         strMessage = e.getMessage();
      }finally{
         try{
           closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
           logger.error(formatException(e));
         }
      }   
      
      return strMessage;
   }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 12/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
}
