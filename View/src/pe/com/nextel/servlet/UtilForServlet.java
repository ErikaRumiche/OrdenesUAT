package pe.com.nextel.servlet;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;

import pe.com.nextel.bean.ProductBean;
import pe.com.nextel.service.NewOrderService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class UtilForServlet {


	public UtilForServlet()	{}
	
	public void selectControl(String strNameControl,String strUtilizarDefault,PrintWriter out,HashMap objHashMap)	{
		
		if( strNameControl.equals("cmb_ItemProducto") ){
			setProductList(strUtilizarDefault,objHashMap,out);
        }else if( strNameControl.equals("cmb_ItemProductLine") ){
			setProductLine(strUtilizarDefault,objHashMap,out);			
        }else if( strNameControl.equals("cmb_ItemPlanTarifario") ){
			setPlanTarifario(strUtilizarDefault,objHashMap,out);			
        } 
	}
		
	public void setProductLine(String strUtilizarDefault,HashMap objHMParametros,PrintWriter out){
		   
	   String strProductLineDefault=(String)objHMParametros.get("objProductLineDefault");	        
	   out.println("vDocCurrent = parent.mainFrame;     ");       
  	   out.println("vDocCurrent.fxObjectConvert('cmb_ItemProductLine','"+strProductLineDefault +"');");
            
    }

	public void setPlanTarifario(String strUtilizarDefault,HashMap objHMParametros,PrintWriter out){
		   
	   String strPlanTarifarioDefault=(String)objHMParametros.get("objPlanTarifarioDefault");	        
	   out.println("vDocCurrent = parent.mainFrame;     ");       
  	   out.println("vDocCurrent.fxObjectConvert('cmb_ItemPlanTarifario','"+strPlanTarifarioDefault +"');");        
    }

	public void setProductList(String strUtilizarDefault,HashMap objHMParametros,PrintWriter out){
	
	   String strProductSelected="";
	   ProductBean objProductBean = (ProductBean)objHMParametros.get("objProductBean");
	   if (strUtilizarDefault.equalsIgnoreCase(Constante.UTILIZAR_DEFAULT))	   {		   
		   strProductSelected=(String)objHMParametros.get("objProductDefault");		
	   }
	   else {
		  objProductBean.setNpproductid(0); //Si es 0 el productid es nulo
		  String strOrigenLlamada=(String)objHMParametros.get("strOrigenLlamada");
		  //Se elimina la condición de setear por defecto el valor inicial en caso del combo de Producto debido
      //a que para Automatizaciones, se necesita que si se modifica la Modalidad se actualice nuevamente el combo
      //de productos para que este a su vez refresque sus servicios relacionados
      //---------------------------------------------------------------------------------------------------------
		 /* if (strOrigenLlamada.equals(Constante.FROM_ONCHANGE)) { 
			  //la llamada se origino desde el OnChange de Modalidad
			  //En este caso me interesa conservar el valor del producto seleccionado en el combo (este no 
			  //es el que setea al llamar el loadDetailPhone)
			  strProductSelected=(String)objHMParametros.get("objProductDefault");			  
		  }*/
	   }

	   
       NewOrderService objNewOrderService = new NewOrderService();
       HashMap objHashMap = objNewOrderService.getProductType(objProductBean);
       
       ArrayList objArrayList = new ArrayList();
       
       if( (String)objHashMap.get("strMessage")!= null ){
          String variable = (MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
          out.println("alert('"+variable+"')");
       }else{
          out.println("formCurrent = parent.mainFrame.frmdatos;");
		  out.println("vDocCurrent = parent.mainFrame;     ");
          out.println("parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProducto);");         
          objArrayList = (ArrayList)objHashMap.get("objArrayList");
          System.out.println("objArrayList.size() : " + objArrayList.size());
          if ( objArrayList != null && objArrayList.size() > 0 ){         
			 for( int i=0; i<objArrayList.size();i++ ){
				objProductBean = new ProductBean();
				objProductBean = (ProductBean)objArrayList.get(i);             
				out.println("parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProducto,'"+objProductBean.getNpproductid()+"','"+MiUtil.getMessageClean(objProductBean.getNpproductname())+"')");
			 }
          }
		  out.println("vDocCurrent.fxObjectConvert('cmb_ItemProducto','"+strProductSelected +"');");
       }       
    }
	
}