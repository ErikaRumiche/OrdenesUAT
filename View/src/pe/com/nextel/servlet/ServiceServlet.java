package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.DecimalFormat;

import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.ejb.SEJBBagMobileBean;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class ServiceServlet extends HttpServlet
{
  private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doPost(request,response);

  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException,ServletException{
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();

        // Variables estandar.
        String strMyaction = null;

        //Captura la acción a realizar.
        strMyaction = request.getParameter("myaction");

        // Bucle estandar de seleccion de acciones.
        if ( strMyaction != null ) {
          if( strMyaction.equals("loadServiceItems") ){
            //System.out.println("Entramos al loadServiceItems");
             loadServiceItems(request,response,out);
          }else if( strMyaction.equals("loadDataPhone") ){
             loadDataPhone(request,response,out);
          }else if(strMyaction.equals("loadDetailPhone") ){
             loadDetailPhone(request,response,out);
          }else if(strMyaction.equals("doValidateNewPhoneToChange") ){
             doValidateNewPhoneToChange(request,response,out);
          }else if(strMyaction.equals("doValidateKitPrice") ){
             doValidateKitPrice(request,response,out);
          }else if(strMyaction.equals("setData") ){
             doSetData(request,response,out);
          }else if(strMyaction.equals("loadDetailFixedPhone") ){
             loadDetailFixedPhone(request,response,out);
          }else if(strMyaction.equals("validaDiasSuspension")){
             validaDiasSuspension(request, response, out);
          }else if(strMyaction.equals("validaDiasSuspensionCrear")){
             validaDiasSuspensionCrear(request, response, out);
          }else if(strMyaction.equals("loadRoamingServices")){
             loadRoamingServices(request, response, out);
          }else if(strMyaction.equals("removeAllCommunities")){
             removeAllCommunities(request, response, out);
          }
        }
  }

  public void loadDetailPhone(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws ServletException,IOException{
    String        strPhoneNumber      = request.getParameter("paramPhoneNumber");
    String        strCustomerId       = request.getParameter("paramCustomer");
    String        strSiteId           = request.getParameter("paramSite");
    String        strSpecificationId  = request.getParameter("paramSpecification");
	  String        strOrdenId  		  = request.getParameter("strOrderId");
    String        type               = request.getParameter("strTypeCompany");
    String        strDivisionId         =request.getParameter("paramDivision");//CBARZOLA:Cambio de Modelo Distintas Tecnologias
    long        lonIdProduct = 0; // EZM
    long       lonSolution =0;
    //INI PRY-1062 AMATEOM
    String strModalidadPago = request.getParameter("paramModalidadPago");
    //FIN PRY-1062 AMATEOM

    ProductBean   objProductBean      = new ProductBean();
    String        strSessionId        =   (String)request.getParameter("strSessionId");

    ServiciosBean serviciosBean = null;

    System.out.println("[ServiceServlet][loadDetailPhone] v222222");
	 System.out.println("[loadDetailPhone]strSpecificationId:"+strSpecificationId);
	 System.out.println("[loadDetailPhone]strSiteId:"+strSiteId);
	 System.out.println("[loadDetailPhone]strOrdenId:"+strOrdenId);


    NewOrderService objNewOrderService = new NewOrderService();
    GeneralService objGeneralService = new GeneralService();
    SpecificationBean objSpecificationBean  =  null;
    ArrayList         objArrayList1           = new ArrayList();

    TemplateAdendumBean templateAdendumBean = null;
    //strPhoneNumber      = objGeneralService.GeneralDAOgetWorldNumber(strPhoneNumber,"COUNTRY");
    String strWarranty  = null;
    int idProm=0; int idPlan = 0;
    String strValidateSolution = "0";

    //System.out.println("Antes de objNewOrderService en loadDetailPhone ");
    System.out.println("[loadDetailPhone]ProductDAOgetDetailByPhoneBySpecification -Inicio-"+strOrdenId+",strPhoneNumber: "+strPhoneNumber);
    HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhoneBySpecification(strPhoneNumber,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSiteId),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strOrdenId));
    System.out.println("[loadDetailPhone]ProductDAOgetDetailByPhoneBySpecification -Fin-"+strOrdenId);
    //System.out.println("Despues de objNewOrderService en loadDetailPhone ");

    out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
    out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
    out.println("<script>");
    out.println("form        = parent.mainFrame.document.frmdatos;     ");
    out.println("vDoc        = parent.mainFrame;     ");
    out.println("vDoc.fxObjectConvert('cmb_ItemNewPlantarifario','');");
    out.println("vDoc.fxObjectConvert('txt_ItemSIM','');");
    out.println("vDoc.fxObjectConvert('txt_ItemIMEI','');");
    out.println("vDoc.fxObjectConvert('txt_ItemModel','');");
    out.println("vDoc.fxObjectConvert('txt_ItemEquipment','');");
    out.println("vDoc.fxObjectConvert('txt_ItemNroOcurrence','');");
    out.println("vDoc.fxObjectConvert('txt_ItemFlgProductGN','');");
    out.println("vDoc.fxObjectConvert('txt_ItemFlgServGE','');");//FPICOY Inicializo el campo txt_ItemFlgServGE
    out.println("vDoc.fxObjectConvert('txt_ItemNewPlantarifarioId','');");
    out.println("vDoc.fxObjectConvert('hdnOriginalPlanId','');");//EZUBIAUR Inicializo el campo hdnOriginalPlanId

    if(  MiUtil.parseInt(strSpecificationId)==Constante.SPEC_SERVICIO_LOCUCION )
       out.println("vDoc.fxObjectConvert('txt_ItemDataEndService','');");

    if( objHashMap.get("strMessage") != null ){
       out.println("alert('"+MiUtil.getMessageClean((String)objHashMap.get("strMessage"))+"');");
       out.println("try{     											    ");
       //JPEREZ: No se eliminan los elementos del combo Línea de Producto (Incidencia 4851)
       //out.println("    vDoc.DeleteSelectOptions(form.cmb_ItemProductLine)  	");
       out.println("    vDoc.DeleteSelectOptions(form.cmb_ItemProducto)     	");
       if (!strSpecificationId.equals("2055")){ //Para pago de penalidad por no devolución no blanquear modalidad - solucion temporal
        out.println("    vDoc.DeleteSelectOptions(form.cmb_ItemModality)		");
       }
       out.println("vDoc.fxObjectConvert('txt_ItemFixedPhone','');");
       out.println("}catch(e){ 											 	");
       out.println("}");
       out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
       out.println("</script>");
       out.close();
    }else{
       objProductBean = (ProductBean)objHashMap.get("objProductBean");
       lonSolution=  objProductBean.getNpsolutionid();

       //Si el productBean no es vacío
       if( objProductBean != null){
          //Inicio COR0303

           //Validaciones de Teléfono Postpago y Prepago
           //-------------------------------------------
           out.println(" function fxLoadSolutionValidateServlet(objValue){");
           out.println("var form = document.frmdatos;");
           out.println("var vDoc = parent.mainFrame;");
           out.println(" var flagSolutionTypeNew='';");
           out.println(" var flagSolutionTypeOld=''; ");
           out.println(" var flagSolucion = ''; ");
           out.println(" var flagSolutionType = ''; ");
           out.println(" if (parent.opener.items_table==null){ ");
           out.println("    flagSolucion = ''; ");
           out.println("    flagSolutionType = '';");
           out.println(" }else{  ");

           objHashMap = objGeneralService.getValueTag1(strSpecificationId,"VALIDATE_SPEC_TYPE_SOLUTION");
            if( objHashMap.get("strMessage") != null ){
                out.println("alert('"+objHashMap.get("strMessage")+"');");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");
                out.close();
            }else{
               strValidateSolution =( String)objHashMap.get("strTag1");
            }

           if ( !strValidateSolution.equals("0") ){
           out.println("    v_numRows = parent.opener.items_table.rows.length;");
           out.println("    if (v_numRows > 2){ ");
           out.println("        flagSolucion = parent.opener.frmdatos.txtItemSolution[1].value; ");
           out.println("        flagSolutionType = ''; ");
           out.println("   }else{ ");
           out.println("       if (v_numRows == 2){");
           out.println("           flagSolucion = parent.opener.frmdatos.txtItemSolution[1].value; ");
           out.println("           flagSolutionType = '';");
            out.println("          if (flagSolucion == undefined)  flagSolucion = '';");
           out.println("       } else{");
           out.println("          flagSolucion = '';");
           out.println("           flagSolutionType = '';");
           out.println("       } ");
           out.println("    } ");
           }else{
              out.println("     flagSolucion = '';");
              out.println("     flagSolutionType = '';");
           }
           out.println(" } ");

           out.println(" if( flagSolucion!='' ){");
               objHashMap =objNewOrderService.getSolutionType();
               if( objHashMap.get("strMessage") != null ){
                out.println("alert('"+objHashMap.get("strMessage")+"');");
                out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
                out.println("</script>");
                out.close();
               }else{
                objArrayList1 = (ArrayList)objHashMap.get("objArrayList");
               }
               out.println(" if(objValue!=''){ ");
                                 if (objArrayList1 != null && objArrayList1.size()>0){
                                      for (int i=0; i<objArrayList1.size(); i++){
                                        objSpecificationBean = new SpecificationBean();
                                        objSpecificationBean = (SpecificationBean)objArrayList1.get(i);
                                        out.println(" if (objValue == '"+objSpecificationBean.getNpSolutionId()+"'){");
                                        out.println("     flagSolutionTypeNew = '"+objSpecificationBean.getNpSolutiontype()+"';");
                                        out.println("  } ");
                                      }
                                  }
              out.println(" } ");

              if (objArrayList1 != null && objArrayList1.size()>0){
                 for (int i=0; i<objArrayList1.size(); i++){
                      objSpecificationBean = new SpecificationBean();
                     objSpecificationBean = (SpecificationBean)objArrayList1.get(i);
                     out.println("  if (flagSolucion == '"+objSpecificationBean.getNpsolutionname()+"'){ ");
                     out.println("      flagSolutionTypeOld = '"+objSpecificationBean.getNpSolutiontype()+"';");
                      out.println("  } ");
                 }
              }

              out.println(" if(flagSolutionTypeNew!=flagSolutionTypeOld){");
              out.println(" alert('Debe ingresar una solución de tipo "+objSpecificationBean.getNpSolutiontype()+".');");
              out.println("  return false; ");
              out.println(" } ");
              out.println(" else{");
              out.println(" return true;");
              out.println(" }");
              out.println(" }else {");
              out.println(" return true;");
              out.println(" }");
             out.println(" }" );

          //Sólo se visualizará los datos en el popup si cumple con la función previa, en caso que sea false no hace nada
          //--------------------------------------------------------------------------------------------------------------

          out.println(" if ( fxLoadSolutionValidateServlet('"+ MiUtil.getString(objProductBean.getNpsolutionid())+"') ) {");

          out.println("vDoc.fxObjectConvert('cmb_ItemSolution','"+MiUtil.getString(objProductBean.getNpsolutionid()) +"');");
          out.println("parent.mainFrame.DeleteSelectOptions(form.cmb_ItemProducto);");
              /*
               * RDELOSREYES - Incidencia 6233 - INICIO
               */
               {
                System.out.println("RDELOSREYES - Incidencia 6233 - INICIO");
                out.println("try {");
                out.println("    if(form.cmb_ItemPlanTarifario!=undefined) { ");
                out.println("       vDoc.DeleteSelectOptions(form.cmb_ItemPlanTarifario);     	");
                String strMessage = "";
                String strErrorLocal = "";
                PlanTarifarioBean pBean = new PlanTarifarioBean();
                HashMap hshData  = null;
                pBean.setNptipo2("0");
                pBean.setNpsolutionid(MiUtil.parseInt(String.valueOf(objProductBean.getNpsolutionid())));
                pBean.setNpspecificationid(Constante.SPEC_INTERCAMBIO_NUMERO);

                //INICIO Telefonia Fija
                if (strSpecificationId.equals("2049"))
                {
                  pBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));
                }
                //Fin Telefonia Fija
                //INI EZUBIAURR Cambio Plan Tarifario
                if (strSpecificationId.equals("2013"))
                {
                  pBean.setNpspecificationid(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO);
                  pBean.setNprequestapprove(MiUtil.parseInt(String.valueOf(objProductBean.getNpproductid())));
                }
                //FIN EZUBIAURR Cambio Plan Tarifario

             //   String type = request.getParameter("strTypeCompany");
                System.out.println("[objItemPlanTarifario][type]"+type);
                hshData = (HashMap)(new NewOrderService()).PlanDAOgetPlanList(pBean,type);
                if( hshData.get("strMessage")!= null ) {
                  throw new ServletException((String) hshData.get("strMessage"));
                }

                ArrayList objArrayList = (ArrayList)hshData.get("objPlanList");
                if ( objArrayList != null && objArrayList.size() > 0 ){
                  PlanTarifarioBean planTarifarioBean = null;
                  for( int i=0; i<objArrayList.size();i++ ){
                    planTarifarioBean = new PlanTarifarioBean();
                    planTarifarioBean = (PlanTarifarioBean)objArrayList.get(i);
                    DecimalFormat decFormat = new DecimalFormat("###");
                    out.println("    vDoc.AddNewOption(form.cmb_ItemPlanTarifario,'"+decFormat.format(planTarifarioBean.getNpplancode())+"','"+planTarifarioBean.getNpdescripcion()+"')");
                  }
                }
                if(MiUtil.parseInt(strSpecificationId) == Constante.SPEC_INTERCAMBIO_NUMERO) {
                  //request.setAttribute("objProductBean", objProductBean);
                  out.println("    vDoc.AddNewOption(form.cmb_ItemPlanTarifario,'"+objProductBean.getNpplanid()+"','"+objProductBean.getNpplan()+"');");
                }
                out.println("     }");
                out.println("} catch(exception) { ");
                out.println("}");
               }
               
              /*
               * RDELOSREYES - Incidencia 6233 - FIN
               */
          //Fin COR0303

          out.println("try {");
          out.println("if(form.cmb_ItemProductLine!=undefined) { ");
          out.println("   vDoc.DeleteSelectOptions(form.cmb_ItemProductLine);     	");
          objHashMap = objNewOrderService.ProductLineDAOgetProductLineSpecList(MiUtil.parseInt(String.valueOf(objProductBean.getNpsolutionid())),MiUtil.parseLong(strSpecificationId),"PRODUCTLINE",objProductBean.getNpproductlineid());
          if( objHashMap.get("strMessage")!= null ) {
              throw new ServletException((String) objHashMap.get("strMessage"));
          }
          ArrayList objArrayListLinea = (ArrayList)objHashMap.get("objArrayList");
          if ( objArrayListLinea != null && objArrayListLinea.size() > 0 ){
              ProductLineBean productLineBean = null;
              for( int i=0; i<objArrayListLinea.size();i++ ){
                   productLineBean = new ProductLineBean();
                   productLineBean = (ProductLineBean)objArrayListLinea.get(i);
                   out.println(" vDoc.AddNewOption(form.cmb_ItemProductLine,'"+productLineBean.getNpProductLineId()+"','"+productLineBean.getNpName()+"')");
              }
          }
          out.println("}");
          out.println("} catch(exception) { ");
          out.println("}");

          out.println("vDoc.fxObjectConvert('cmb_ItemNewPlantarifario','"+MiUtil.getString(objProductBean.getNpplan()) +"');");
          out.println("vDoc.fxObjectConvert('txt_ItemSIM','"+MiUtil.getString(objProductBean.getNpcd_sim()) +"');");
          out.println("vDoc.fxObjectConvert('txt_ItemIMEI','"+MiUtil.getString(objProductBean.getNpequipmentimei()) +"');");
          //Se debe de comparar si el modelo del tevDoc.fxObjectConvert('txt_ItemNroOcurrence',léfono se encuentra dentro de la lista de modelos
          out.println("try{ vDoc.fxSetModelGeneric('"+MiUtil.getString(objProductBean.getNpproductmodel()) +"'); }catch(e){}");
          out.println("try{ vDoc.fxSetReplaceEquipment('"+MiUtil.getString(objProductBean.getStrRealModel())+"', '"+objProductBean.getLRealModelId()+"', '"+MiUtil.getString(objProductBean.getStrRealSim())+"', '"+MiUtil.getString(objProductBean.getStrRealImei())+"', '"+objProductBean.getLRealProductLineId()+"'); }catch(e){}");
          out.println("vDoc.fxObjectConvert('txt_ItemEquipment','"+MiUtil.getString(objProductBean.getNpequipment()) +"');");
          // CPUENTE CAP & CAL - INICIO
		  out.println("vDoc.fxObjectConvert('txtItemTiempoMeses','" + objProductBean.getNptiempoequipo() + "');");
          out.println("vDoc.fxObjectConvert('txtItemEstadoContrato','"+MiUtil.getString(objProductBean.getNpestadoContrato()) +"');");
          out.println("vDoc.fxObjectConvert('txtItemMotivoEstado','"+MiUtil.getString(objProductBean.getNpmotivoEstado()) +"');");
          out.println("vDoc.fxObjectConvert('txtItemFechaCambio','"+MiUtil.getString(objProductBean.getNpfechaCambioEstado()) +"');");
          // CPUENTE CAP & CAL -FIN
          //EZUBIAURR
          out.println("vDoc.fxObjectConvert('hdnOriginalPlanId','"+MiUtil.getString(objProductBean.getNpplanid()) +"');");

          if (objProductBean.getNpoccurrence()>=3 && strSpecificationId.equals("2010") ){
            out.println("alert('N° de ocurrencia no cubierta por Garantía Nextel, verifique fecha de renovación');" );
            out.println("vDoc.fxObjectConvert('txt_ItemNroOcurrence','0');");
          }
          else{
            out.println("vDoc.fxObjectConvert('txt_ItemNroOcurrence','"+objProductBean.getNpoccurrence() +"');");
          }

          lonIdProduct = objProductBean.getNpproductid(); //EZM

          out.println("vDoc.fxObjectConvert('txt_ItemFlgProductGN','"+MiUtil.getString(objProductBean.getNpwarranty()) +"');");
          out.println("vDoc.fxObjectConvert('txt_ItemFlgServGE','"+MiUtil.getString(objProductBean.getNpguaranteeExtFact()) +"');");//FPICOY Seteo el valor de Garantia Extendida en el control
          out.println("vDoc.fxObjectConvert('txt_ItemNewPlantarifarioId','"+MiUtil.getString(objProductBean.getNpplanid()) +"');");
          out.println("vDoc.fxObjectConvert('hdnOriginalProductId','"+objProductBean.getNpproductid() +"');");
          out.println("vDoc.fxObjectConvert('cmb_ItemPlanTarifario','"+MiUtil.getString(objProductBean.getNpplanid()) +"');");
          out.println("vDoc.fxObjectConvert('cmb_ItemProductLine','"+MiUtil.getString(objProductBean.getNpproductlineid()) +"');");

          if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){
            out.println("vDoc.deleteTable();");
          }
          setDataDefaultInControl(strSpecificationId,Constante.FROM_LOADDETAIL,objProductBean,out); //CEM COR0303

          if( ( MiUtil.parseInt(strSpecificationId)==Constante.SPEC_SERVICIO_LOCUCION )
           && ( (objProductBean.getNpcostatus().equals(Constante.STATUS_DESACTIVADO)) || ((objProductBean.getNpcostatus().equals(Constante.STATUS_SUSPENDIDO)))   ) )
             out.println("vDoc.fxObjectConvert('txt_ItemDataEndService','"+MiUtil.toFecha(objProductBean.getNpstatusdate()) +"');");
          else
             out.println("vDoc.fxObjectConvert('txt_ItemDataEndService','');");

          //Cargar nuevamente la modalidad de salida en base a los resultados
          System.out.println("----Inicio loadDetailPhone----");
          System.out.println("strSpecificationId-->"+strSpecificationId);
          System.out.println("objProductBean.getNpequipment()-->"+objProductBean.getNpequipment());
          System.out.println("objProductBean.getNpwarranty()-->"+objProductBean.getNpwarranty());
          System.out.println("objProductBean.getNpcontractid()-->"+objProductBean.getNpcontractid());
          //CPUENTE DEBUG
          System.out.println("objProductBean.getNptiempomeses()-->"+objProductBean.getNptiempoequipo());
          System.out.println("objProductBean.getNpestadoContrato()-->"+objProductBean.getNpestadoContrato());
          System.out.println("objProductBean.getNpmotivoEstado()-->"+objProductBean.getNpmotivoEstado());
          System.out.println("objProductBean.getNpfechaCambioEstado()-->"+objProductBean.getNpfechaCambioEstado());

          System.out.println("----Fin loadDetailPhone----");

          if (MiUtil.parseLong(strSpecificationId)== Constante.SPEC_REPOSICION){ //Reposiciones
             strWarranty=objProductBean.getNpwarranty();
          }

          //Inicio CEM - COR0242
          if ((MiUtil.parseLong(strSpecificationId)== Constante.SPEC_CAMBIO_MODELO)||(MiUtil.parseInt(strSpecificationId)==Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)){ //Cambio de Modelo
          out.println("form.txt_ItemSIM_Eagle.value=''");
          out.println("form.cmb_ItemModality.value=''");
          //out.println("form.cmb_ItemSolution.disabled=true");
          out.println("form.cmb_ItemProductLine.disabled=false");
          out.println("form.cmb_ItemProducto.disabled=false");
          }
          //Fin CEM - COR0242

          //Categorias que solo visualizan el objeto solucion a modo de detalle (Ordenes de PostVenta)
          //------------------------------------------------------------------------------------------
          out.println("try {");
          out.println("if(form.txt_ItemPhone!=undefined) { ");
          out.println("   if(form.txt_ItemPhone.value!='') { ");
          out.println("       form.cmb_ItemSolution.disabled=true");
          out.println("   }");
          out.println("}");
          out.println("} catch(exception) { ");
          out.println("}");

                    //CBARZOLA:Cambio de Modelo entre Distintas Tecnologias
            HashMap hshStatus=objGeneralService.getStatusByTable(Constante.NPT_VALIDATE_SOLUTION,strSpecificationId);
            if( hshStatus.get("strMessage") != null ){
               out.println("alert('"+hshStatus.get("strMessage")+"');");
            }
            String strStatus=(String)hshStatus.get("strStatus");
            if(strStatus.equals(Constante.ACTIVE_STATUS)){
              HashMap hshSolutionName=objNewOrderService.OrdergetSolutionName(objProductBean.getNpsolutionid());
              String strNameSolution=(String)hshSolutionName.get("strNameSolution");
              if(hshSolutionName.get("strMessage") != null){
                strNameSolution="";
                out.println("alert('"+hshSolutionName.get("strMessage")+"');");
              }
              out.println("try { vDoc.fxObjectConvert('txt_ItemOriginalSolutionId','"+MiUtil.getString(objProductBean.getNpsolutionid())+"');}catch(e){}");
              out.println("try { vDoc.fxObjectConvert('txt_ItemOriginalSolution','"+strNameSolution+"');}catch(e){}");
              out.println("form.cmb_ItemSolution.disabled=false");
              out.println("vDoc.DeleteSelectOptions(form.cmb_ItemSolution)");
              HashMap hshOtherSolutions=objNewOrderService.getOtherSolutionsbySubMarket(MiUtil.parseLong(strSpecificationId),objProductBean.getNpsolutionid());
              if( hshOtherSolutions.get("strMessage") != null ){
                  out.println("alert('"+hshOtherSolutions.get("strMessage")+"');");
                 }
                ArrayList listSolutions=(ArrayList)hshOtherSolutions.get("objArrayList");
                if(listSolutions!=null) {
                  for( int i=0; i<listSolutions.size();i++ ){
                    SolutionBean objsolution= new SolutionBean();
                    objsolution= (SolutionBean)listSolutions.get(i);
                    out.println("    vDoc.AddNewOption(form.cmb_ItemSolution,'"+MiUtil.getString(objsolution.getNpsolutionid())+"','"+objsolution.getNpsolutionname()+"')");
                   }
                }
                out.println("form.cmb_ItemProductLine.value=''");
                out.println("form.cmb_ItemPlanTarifario.value=''");
                //}

              }

          objHashMap = objNewOrderService.OrderDAOgetModalityList(MiUtil.parseLong(strSpecificationId),objProductBean.getNpequipment(),strWarranty,null);
          if( objHashMap.get("strMessage") != null ){
            out.println("alert('"+objHashMap.get("strMessage")+"');");
            out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
            out.println("</script>");
            out.close();
          }else{
            ArrayList     objArrayList = (ArrayList)objHashMap.get("objArrayList");
            ModalityBean  objModalityBean = null;
            if ( objArrayList != null && objArrayList.size() > 0 ){
               out.println("vDoc.DeleteSelectOptions(form.cmb_ItemModality)");
               for( int i=0; i<objArrayList.size();i++ ){
                    objModalityBean = new ModalityBean();
                    objModalityBean = (ModalityBean)objArrayList.get(i);

                    out.println("vDoc.AddNewOption(form.cmb_ItemModality,'"+objModalityBean.getNpmodality()+"','"+objModalityBean.getNpmodality()+"')");
               }

            }
          }


          //Servicios Adicionales
          //----------------------
          out.println("  function ServicioJS(id, name ,nameShort ,exclude,group) {     ");  //johncmb
          out.println("        this.id = id;     ");
          out.println("        this.name = name;     ");
          out.println("        this.nameDisplay = (exclude==\"\")?name:name+\" - \"+exclude;     ");
          out.println("        this.nameShort = nameShort;     ");
          out.println("        this.nameShortDisplay = (exclude==\"\")?nameShort:nameShort+\" - \"+exclude;     ");
          out.println("        this.exclude = exclude;     ");
          out.println("        this.active_new = 'N';    ");
          out.println("        this.modify_new = 'N';    ");
          out.println("        this.group = group;    "); //johncmb
          out.println("     }     ");
          out.println("     // Vector de Servicios     ");
          out.println("     var vServicio = new Vector();     ");

          out.println("  function ServicioArr(id, name ,nameShort ,exclude, active_new, modify_new) {     ");
          out.println("         this.id = id;                                                             ");
          out.println("         this.name = name;                                                         ");
          out.println("         this.nameDisplay = (exclude=='')?name:name+' - '+exclude;                 ");
          out.println("         this.nameShort = nameShort;                                               ");
          out.println("         this.nameShortDisplay = (exclude=='')?nameShort:nameShort+' - '+exclude;  ");
          out.println("         this.exclude = exclude;                                                   ");
          out.println("         this.active_new = active_new;//'N;                                        ");
          out.println("         this.modify_new = modify_new;//'N';                                       ");
          out.println("  }                                                                                ");
          out.println("     // Vector de Servicios de Arrendamiento                                       ");
          out.println("     var vServicioArren = new Vector();     ");

          String strType = null;
          out.println("try {");
          out.println(" if(form.cmbAvailableServices!=undefined) { ");
          out.println("    vDoc.DeleteSelectOptions(form.cmbAvailableServices);     	");
          if (MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_DES_SERVICIOS_BA){
              strType = "A";
          }


          /**LROSALES: INI - Cambio de parámetros**/
          int intSolutionId = MiUtil.parseInt(objProductBean.getNpsolutionid()+"");
          int intPlanId  = MiUtil.parseInt(objProductBean.getNpplanid()+"");
          int intProductId  = MiUtil.parseInt(objProductBean.getNpproductid()+"");
          String strSSAAType  = null; //johncmb
          //johncmb inicio
          if( MiUtil.parseInt(strSpecificationId) == Constante.SPEC_POSTPAGO_VENTA ||
              MiUtil.parseInt(strSpecificationId) == Constante.SPEC_PREPAGO_NUEVA ||
              MiUtil.parseInt(strSpecificationId) == Constante.SPEC_SSAA_SUSCRIPCIONES ||
              MiUtil.parseInt(strSpecificationId) == Constante.SPEC_SSAA_PROMOTIONS ||
              MiUtil.parseInt(strSpecificationId) == Constante.SPEC_PREPAGO_TDE ||
              MiUtil.parseInt(strSpecificationId) == Constante.SPEC_REPOSICION_PREPAGO_TDE //Se agrega reposicion prepago tde - TDECONV034 
          ) {
            strSSAAType  = "0,1,2,3,5";} //johncmb
          else{
            strSSAAType  = "0,1,5";
          }
           HashMap hServiceList  = objGeneralService.getServiceList(intSolutionId,intPlanId,intProductId,strSSAAType,strType);
          //johncmb fin

          /**LROSALES: FIN - Cambio de parámetros**/
          if( hServiceList.get("strMessage") != null ){
              out.println("alert('"+hServiceList.get("strMessage")+"');");
              out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
              out.println("</script>");
              out.close();
          }else{
             ArrayList  objArrayList =(ArrayList)hServiceList.get("objServiceList");
             if ( objArrayList != null && objArrayList.size() > 0 ){
                 out.println("vDoc.vServicio.removeElementAll()");
                 serviciosBean = null;
                 serviciosBean = (ServiciosBean)objArrayList.get(0);
                 int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
                 for( int j = 1; j < objArrayList.size(); j++ ){
                      serviciosBean = (ServiciosBean)objArrayList.get(j);
                      if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
                      longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
                 }
                 out.println("vDoc.longMaxServices = '30'");

                 for( int i = 0; i < objArrayList.size(); i++ ){
                    serviciosBean = (ServiciosBean)objArrayList.get(i);
                  out.println("vDoc.vServicio.addElement(new ServicioJS('"+serviciosBean.getNpservicioid()+"','"+serviciosBean.getNpnomserv()+"','"+serviciosBean.getNpnomcorserv()+"','"+MiUtil.getString(serviciosBean.getNpexcludingind())+"','"+MiUtil.getString(serviciosBean.getNpgroup())+"'))"); //johncmb
                  }
             }
            out.println("serviceAditional = GetSelectedServices()");
            out.println("vDoc.fxCargaServiciosItem(serviceAditional)");
              //out.println("vDoc.fxLongMaxServices("+objArrayList+")");
          }
          out.println("  } ");
          out.println("} catch(exception) { ");
          out.println("}");

          //Servicios Adicionales Contratados
          //---------------------------------
          //HAA 04032010: Se valida que la lista de servicios contratados sea diferente de null
          if(objProductBean.getNpssaa_contratado() != null){
            if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_CAMBIO_MODELO){
              String ssaa_contratado = objProductBean.getNpssaa_contratado().replace('|',':');
              String serviciosAdicionales[] = ssaa_contratado.split(":");
              String servicioAdicional = null;
              //Registro de logs en la tabla NP_LOG_ITEM
              HashMap hNpTableList  = objGeneralService.getValueNpTable("FLAG_LOG_ITEM_CAMBIO_MODELO");
              ArrayList objNpTableList =(ArrayList)hNpTableList.get("objArrayList");
              String  strflagLogItem= "I";
              HashMap objLogItem = new HashMap();
              TableBean nptableBean = null;
              if (objNpTableList.size()>0) {
                nptableBean = (TableBean)objNpTableList.get(0);
                strflagLogItem = nptableBean.getNpValue().trim();
                System.out.println("El strflagLogItem tiene---->" + strflagLogItem);
              }
              String servArrend = "";
              String servContract = "";
              String messageLog = "";
              for(int i=0; i<serviciosAdicionales.length; i++){
                servicioAdicional = serviciosAdicionales[i];
                try{
                  System.out.println("servicioAdicional " + servicioAdicional);
                  serviciosBean = objGeneralService.getDetailService(Long.parseLong(servicioAdicional));
                  servContract = servContract + serviciosBean.getNpservicioid() + "|" + serviciosBean.getNpnomserv() + "|";
                }catch(NumberFormatException e){
                  servContract = servContract + servicioAdicional + "|";
                  continue;
                }
                if("ALQ".equals(serviciosBean.getNpexcludingind())){
                  servArrend = servArrend + serviciosBean.getNpnomserv() + "-";
                  out.println("form.hdnIdCM.value = '"+serviciosBean.getNpservicioid() +"'");
                  out.println("form.hdnNameCM.value = '"+serviciosBean.getNpnomserv() +"'");
                  out.println("form.hdnNameShortCM.value = '"+serviciosBean.getNpnomcorserv() +"'");
                  out.println("parent.mainFrame.vServicioArren.removeElementAll();");
                  out.println("vDoc.vServicioArren.addElement(new ServicioArr('"+serviciosBean.getNpservicioid()+"','"+serviciosBean.getNpnomserv()+"','"+serviciosBean.getNpnomcorserv()+"','"+MiUtil.getString(serviciosBean.getNpexcludingind())+"','S','N'));");
                  //out.println("vDoc.fxCargaServicioCambioModelo('"+serviciosBean.getNpnomserv() +"', '"+serviciosBean.getNpnomcorserv() +"');");
                }
              }
              if ("A".equals(strflagLogItem)) {
                String strTypeEvent = request.getParameter("strTypeEvent");
                PortalSessionBean objSessionBean2 = (PortalSessionBean)SessionService.getUserSession(strSessionId);
                String strModality = MiUtil.getString(objProductBean.getNpequipment());
                System.out.println("Servicios Contratados->" + servContract);
                System.out.println("Servicios de Arrendamiento->" + servArrend);
                messageLog = "[Ingresando Telefono..." + strPhoneNumber + "][ServiceServlet][loadDetailPhone]Modalidad:" + strModality + " Servicios Contratados:" + servContract + " Servicios de Arrendamiento:" + servArrend;
                objLogItem.put("nporderid",strOrdenId);
                objLogItem.put("npinbox",strTypeEvent);
                objLogItem.put("npdescription",messageLog.length()<2000?messageLog:messageLog.substring(0,2000));
                objLogItem.put("npcreatedby",objSessionBean2==null?"MWONG2":objSessionBean2.getLogin());
                objGeneralService.doSaveLogItem(objLogItem);
              }
            }
            out.println("vDoc.fxCargaServiciosItem('"+objProductBean.getNpssaa_contratado() +"');");

              //EFLORES CDM+CDP PRY-0817 Añade string de servicio adicional temporal, solo para cmt, para recuperar los ssaa contratados
              //si son de la misma tecnologia
              if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS){
                  out.println("vDoc.varSSAASelectedTemp = '"+objProductBean.getNpssaa_contratado()+"'");
              }
          }

          // INI PRY-1062 | CMONTEROS
           if (strModalidadPago != null) {
               if (!strModalidadPago.equalsIgnoreCase("") && strModalidadPago.equalsIgnoreCase(Constante.PAYFORM_CARGO_EN_RECIBO)) {
                   if ((MiUtil.parseLong(strSpecificationId) == Constante.SPEC_CAMBIO_MODELO) || (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) {
               HashMap objHashItem = objNewOrderService.doValidateVEPItem(Long.valueOf(MiUtil.parseLong(strCustomerId)), strPhoneNumber);

                       if (objHashItem.get("strMessage") != null) {
                   out.println("alert('" + objHashItem.get("strMessage") + "');");
                   out.println("vDoc.fxObjectConvert('txt_ItemPhone','');");
                   out.println("vDoc.fxObjectConvert('txt_ItemOriginalSolution','');");
                   out.println("vDoc.fxObjectConvert('txt_ItemSIM','');");
                   out.println("vDoc.fxObjectConvert('cmb_ItemNewPlantarifario','');");
                   out.println("vDoc.fxObjectConvert('txt_ItemEquipment','');");
                   out.println("vDoc.fxObjectConvert('txt_ItemIMEI','');");
                   out.println("vDoc.fxObjectConvert('txt_ItemModel','');");
                   out.println("vDoc.DeleteSelectOptions(form.cmbAvailableServices);");
                   out.println("vDoc.DeleteSelectOptions(form.cmbSelectedServices);");
               }
           }
               }
           }
          // FIN PRY-1062 | CMONTEROS

          //CBARZOLA:Carga servicios core de un plan
          int iResultado = 0;
          HashMap oHashMap=objGeneralService.getDataNpTable("VALIDATE_SERVICE_CORE",strSpecificationId);
          ArrayList     objArrayListValues = (ArrayList)oHashMap.get("objArrayList");
          //System.out.println("hdnSpecification"+strSpecificationId);
          //System.out.println("objArrayList.size()"+objArrayList1.size());
          if (objArrayListValues.size()!=0){
          iResultado=1;
          }
          if(iResultado==1){
          fxLoadServiceComercialCore(MiUtil.parseInt(MiUtil.getString(objProductBean.getNpplanid())),out);
          }
          out.println("try{vDoc.fxValidateAddendumAct('"+strPhoneNumber+"');}catch(e){}");
          //out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
          if(MiUtil.contentInArray(MiUtil.parseInt(strSpecificationId),Constante.SPEC_LOAD_ADEND_DEFAULT)){
              PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
              int iUserId = 0;
              int iAppId  = 0;
              iUserId = objSessionBean.getUserid();
              iAppId  = objSessionBean.getAppId();
              HashMap hshScreenOptions = new HashMap();
              hshScreenOptions = objGeneralService.getRol(Constante.SCRN_OPTTO_ADDENDUM_TERM, iUserId, iAppId);
              String strMessage = (String)hshScreenOptions.get("strMessage");
              if (strMessage!=null)
                try{throw new Exception(strMessage);}
                catch (Exception e){}

              int iFlagCarrier=MiUtil.parseInt((String)hshScreenOptions.get("iRetorno"));

              if(MiUtil.getString(objProductBean.getNpplanid()) != null)
                idPlan = MiUtil.parseInt(MiUtil.getString(objProductBean.getNpplanid()));

              HashMap objHashMap2 = new HashMap();
              HashMap hshData     = null;
              ItemService objItemServiceTransaction = new ItemService();
              //objHashMap2 = objItemServiceTransaction.OrderDAOgetAddendasList( 0, 0, MiUtil.parseInt(strSpecificationId),0) ;
              objHashMap2 = objItemServiceTransaction.OrderDAOgetAddendasList( 0, idPlan, MiUtil.parseInt(strSpecificationId),0) ;

              strMessage = "";
              ArrayList list = new ArrayList();

              strMessage = (String) objHashMap2.get("strMessage");
              list = (ArrayList)objHashMap2.get("objArrayList");
              Iterator iterator = list.iterator();

              String adendasId = "";

              while(iterator.hasNext()) {
                 templateAdendumBean = new TemplateAdendumBean();
                 templateAdendumBean = (TemplateAdendumBean) iterator.next();

                out.println("row = parent.mainFrame.itemsTemplate.insertRow(-1);");
                out.println("col = row.insertCell(0);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                if (("S").equals(templateAdendumBean.getNptemplatedefa())){
                     adendasId += templateAdendumBean.getNptemplateid()+"-"+templateAdendumBean.getNpaddendumterm()+";";

                     out.println("col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="+templateAdendumBean.getNptemplateid()+" checked=true style=text-align:center onClick=javascript:agregar(this)  >';");
                 //    out.println("parent.mainFrame.frmdatos.hdn_ListAdenda.value = "+templateAdendumBean.getNptemplateid()+"-"+templateAdendumBean.getNpaddendumterm()+";");
                     out.println("parent.mainFrame.frmdatos.hdn_ListAdenda.value = '"+adendasId+"'");

                }
                if (!(("S").equals(templateAdendumBean.getNptemplatedefa()))){

                      out.println("col.innerHTML = '<input type=checkbox name=checkSelec size=10 maxlength=15 value="+templateAdendumBean.getNptemplateid()+" style=text-align:center onClick=javascript:agregar(this);getTipoAdenda(this)>';");
                   }
                out.println("col.innerHTML = col.innerHTML;");
                out.println("col = row.insertCell(1);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                out.println("col.innerHTML = '<input type=hidden name=txtDescTemplate size=10 maxlength=15 value=Venta style=text-align:center readOnly>';");
                out.println("col.innerHTML = col.innerHTML + '"+templateAdendumBean.getNptemplatedesc()+"';");

                hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpaddendtype(), "TIPOADENDA");
                strMessage=(String)hshData.get("strMessage");
                if (strMessage!=null)
                try{throw new Exception(strMessage);}
                catch (Exception e){}
                String strTipoAdenda=(String)hshData.get("strDescription");

                out.println("col = row.insertCell(2);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                out.println("col.innerHTML = '<input type=hidden name=txtTypeAdendum size=10 maxlength=15 value=Venta style=text-align:center readOnly>';");
                out.println("col.innerHTML = col.innerHTML + '"+strTipoAdenda+"';");

                out.println("col = row.insertCell(3);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                out.println("col.innerHTML = '<input type=hidden name=txtDateCreated size=10 maxlength=15 value=Venta style=text-align:center readOnly>';");
                out.println("col.innerHTML = col.innerHTML +'"+MiUtil.getDate(templateAdendumBean.getNpcreateddate(), "dd/MM/yyyy")+"';");

                hshData=objGeneralService.getDescriptionByValue(templateAdendumBean.getNpbenefittype(), "BENEFICIO");
                strMessage=(String)hshData.get("strMessage");
                if (strMessage!=null)
                try{throw new Exception(strMessage);}
                catch (Exception e){}
                String strBeneficioAdenda=(String)hshData.get("strDescription");

                out.println("col = row.insertCell(4);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                out.println("col.innerHTML = '<input type=hidden name=txtTypeBenefit size=10 maxlength=15 value=Venta style=text-align:center readOnly>';");
                out.println("col.innerHTML = col.innerHTML + '"+strBeneficioAdenda+"';");

                out.println("col = row.insertCell(5);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                if(iFlagCarrier == 1){
                  out.println("col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="+templateAdendumBean.getNpaddendumterm()+" style=text-align:right onkeyup=\"javascript: agregar(form.checkSelec);\" onKeyPress= \"return fxOnlyNumber(event);\">';");
                }else{
                  out.println("col.innerHTML = '<input type=text name=txtAddendumTerm size=2 maxlength=2 value="+templateAdendumBean.getNpaddendumterm()+" style=text-align:right readonly onkeyup=\"javascript: agregar(form.checkSelec);\" >';");
                }
                out.println("col.innerHTML = col.innerHTML;");

                out.println("col = row.insertCell(6);");
                out.println("col.className = 'CellContent';");
                out.println("col.align     = 'center';");
                out.println("col.innerHTML = '<input type=hidden name=txtTipoAden size=10 maxlength=15 style=text-align:center>';");
                out.println("col.id='flagAdenda"+templateAdendumBean.getNptemplateid()+"';");

              }
            }

            //Suspensiones Temporales rmartinez
             if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_SUSPENSIONES[0])
                validaDiasSuspension(request, response, out);
            //Fin Suspensiones Temporales rmartinez

            out.println("}"); //Fin de la validación de la Función de Postpago y Prepago
          }
          out.println("if (form.cmb_ItemDevolution != null){");
          out.println("vDoc.fxLoadModality();");
          out.println("}");
          
          /*
          if (MiUtil.parseInt(strSpecificationId)==Constante.SPEC_CAMBIAR_PLAN_TARIFARIO){// EZM || MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_DES_SERVICIOS_BA){
          out.println("vDoc.fxGetPlainsDefaultTel('"+lonIdProduct +"','"+lonSolution+"');"); //EZM 08/12
          }*/

           if (MiUtil.parseInt(strSpecificationId)==Constante.SPEC_TRANSFERENCIA){// EZM || MiUtil.parseInt(strSpecificationId)==Constante.KN_ACT_DES_SERVICIOS_BA){

          //out.println("vDoc.fxGetPlainsDefaultTel('"+lonIdProduct +"','"+lonSolution+"');"); //EZM 08/12
          out.println("vDoc.fxGetPlainsDefault('"+lonIdProduct +"');"); //EZM
          }

        if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_ACTIVAR_PAQUETES_ROAMING) {
        // MMONTOYA [ADT-RCT-092 Roaming con corte]
        // Carga del combo servicios roaming y del vector de servicios.
            fillRoamingServicesCombo(strPhoneNumber, objProductBean.getNpplanid(), out);
        } else if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_CAMBIAR_ESTRUCT_CUENTA || MiUtil.parseInt(strSpecificationId) == Constante.SPEC_TRANSFERENCIA) {
            // MMONTOYA [Bolsa Celular]
            validateCommunites(request, objProductBean, strPhoneNumber, null, out);
        }

          out.println("</script>");
          out.close();
      }
  }

    /**
     * MMONTOYA [ADT-RCT-092 Roaming con corte]
     * Carga los servicios roaming en modo edición. Invocado desde la función fxloadItemsEdit del PopUpOrder.jsp
     * @param request
     * @param response
     * @param out
     * @throws ServletException
     * @throws IOException
     */
    public void loadRoamingServices(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
        out.println("<script language='JavaScript' src='" + Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
        out.println("<script language='JavaScript' src='" + Constante.PATH_APPORDER_SERVER + "/Resource/BasicOperations.js'></script>");
        out.println("<script language='JavaScript' src='" + Constante.PATH_APPORDER_SERVER + "/Resource/FunctionsItemsIXEdit.js'></script>");
        out.println("<script>");

        // Inicio definiciones javascript.
        // Este código debe ser identico al de loadDetailPhone.
        out.println("    form = parent.mainFrame.document.frmdatos;");
        out.println("    vDoc = parent.mainFrame;");
        out.println("    function ServicioJS(id, name , nameShort, exclude, group) {");
        out.println("        this.id = id;");
        out.println("        this.name = name;");
        out.println("        this.nameDisplay = (exclude==\"\")?name:name+\" - \"+exclude;");
        out.println("        this.nameShort = nameShort;");
        out.println("        this.nameShortDisplay = (exclude==\"\")?nameShort:nameShort+\" - \"+exclude;");
        out.println("        this.exclude = exclude;");
        out.println("        this.active_new = 'N';");
        out.println("        this.modify_new = 'N';");
        out.println("        this.group = group;");
        out.println("    }");
        // Fin definiciones javascript.

        String strPhoneNumber = request.getParameter("paramPhoneNumber");
        long planId = Long.valueOf(request.getParameter("strPlanId"));

        fillRoamingServicesCombo(strPhoneNumber, planId, out);

        // Se selecciona en el combo de servicios el servicio actual.
        out.println("SetCmbDefaultText(form.cmbItemServiceROA, form.txtItemSelectedServiceROA.value);");
        out.println("vDoc.document.getElementById('cmbItemServiceROA').onchange();");
        out.println("form.item_services.value = GetSelectedServices();");

        out.println("</script>");
        out.close();
    }

    /**
     * MMONTOYA [ADT-RCT-092 Roaming con corte]
     * Carga el combo de servicios roaming y el vector de servcios.
     * @param strPhoneNumber
     * @param planId
     * @param out
     * @throws ServletException
     */
    private void fillRoamingServicesCombo(String strPhoneNumber, long planId, PrintWriter out) throws ServletException {
        HashMap hshDataMap = (new RoamingService()).loadRoamingServices(strPhoneNumber, planId);
        if (hshDataMap.get("strMessage") != null) {
            out.println("alert('" + MiUtil.getMessageClean((String)hshDataMap.get("strMessage")) + "');");
            out.println("location.replace('" + Constante.PATH_APPORDER_SERVER + "/Bottom.html');");
            out.println("</script>");
            out.close();
            return;
        }

        List<RoamingServiceBean> serviceBeans = (List)hshDataMap.get("serviceBeans");

        for (RoamingServiceBean serviceBean : serviceBeans) {
            // Agregar el item al combo.
            out.println("vDoc.AddNewOption(form.cmbItemServiceROA,'" + serviceBean.getNpservicioid() + "','" + serviceBean.getNpnomserv() + "')");

            // Agregar el item al vector.
            out.println("vDoc.vServicio.addElement(new ServicioJS('" + serviceBean.getNpservicioid() + "','" + serviceBean.getNpnomserv() + "','" + serviceBean.getNpnomcorserv() + "','" + MiUtil.getString(serviceBean.getNpexcludingind()) + "','" + MiUtil.getString(serviceBean.getNpgroup()) + "'))");

            // Se asignan valores adicionales.
            out.println("vDoc.vServicio.elementAt(vDoc.vServicio.size() - 1).bagType = '" + serviceBean.getBagType() + "'");
            out.println("vDoc.vServicio.elementAt(vDoc.vServicio.size() - 1).price = " + serviceBean.getPrice());
            out.println("vDoc.vServicio.elementAt(vDoc.vServicio.size() - 1).planType = " + serviceBean.getPlanType());
            out.println("vDoc.vServicio.elementAt(vDoc.vServicio.size() - 1).bagCode = '" + serviceBean.getBagCode() + "'");
            out.println("vDoc.vServicio.elementAt(vDoc.vServicio.size() - 1).validity = " + serviceBean.getValidity());
        }
    }

  public void loadDataPhone(HttpServletRequest request, HttpServletResponse response,PrintWriter out){
      String[]  av_value_phone      = request.getParameterValues("txtPhoneNumber");
      String    av_current_index    = (String)request.getParameter("indexActual");
      String    strPhoneNumber      = new String();
      ArrayList objArrayList        = new ArrayList();
      //Inicio CEM COR0458
      String    strCustomerId       = request.getParameter("pCustomerId"); //CEM COR0458
      String    strSiteId       		= request.getParameter("pSiteId");
      String    strSpecificationId  = request.getParameter("hdnSpecification"); //CGC COR0448
      String    strOrdenId  		  	= request.getParameter("strOrderId");

      ProductBean objProductBean; // MMONTOYA [Bolsa Celular]

		System.out.println("[ServiceServlet][loadDataPhone]");
		System.out.println("[loadDataPhone]strSpecificationId:"+strSpecificationId);
		System.out.println("[loadDataPhone]strSiteId:"+strSiteId);
		System.out.println("[loadDataPhone]strOrdenId:"+strOrdenId);


	  GeneralService objGeneralService = new GeneralService();
	  strPhoneNumber=av_value_phone[MiUtil.parseInt(av_current_index)];
	  //strPhoneNumber  = objGeneralService.GeneralDAOgetWorldNumber(strPhoneNumber,Constante.COUNTRY);

	  NewOrderService objNewOrderService = new NewOrderService();
	  //HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhoneBySpecification(av_value_phone[MiUtil.parseInt(av_current_index)],MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSpecificationId)); //CGC COR0448
	  //HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhone(strPhoneNumber,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSiteId),MiUtil.parseLong(strSpecificationId));
	  System.out.println("[loadDataPhone]ProductDAOgetDetailByPhoneBySpecification -Inicio-"+strOrdenId);
	  HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhoneBySpecification(strPhoneNumber,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSiteId),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strOrdenId));
	  System.out.println("[loadDataPhone]ProductDAOgetDetailByPhoneBySpecification -Fin-"+strOrdenId);
	  if( objHashMap.get("strMessage") != null ){
		  //out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
         out.println("<script>");
         out.println(" parent.mainFrame.document.frmdatos.txtPhoneNumber["+av_current_index+"].value =''; ");
         out.println("alert('"+MiUtil.getMessageClean((String)objHashMap.get("strMessage"))+"');");
         out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
         out.println("</script>");
         out.close();
         return;
      }
      //Fin CEM COR0458
      else {
          objProductBean = (ProductBean)objHashMap.get("objProductBean");
      }

      BillingAccountService objBillingAccountService =  new BillingAccountService();
      //strPhoneNumber  = objGeneralService.GeneralDAOgetWorldNumber(av_value_phone[MiUtil.parseInt(av_current_index)],"COUNTRY");
      //HashMap objHashMap = objBillingAccountService.BillingAccountDAOgetCoAssignmentSiteOrig(strPhoneNumber);
      objHashMap = objBillingAccountService.BillingAccountDAOgetCoAssignmentSiteOrig(strPhoneNumber);
      System.out.println("strPhoneNumber : " + strPhoneNumber);

        out.println("<script language='JavaScript' src='"+ Constante.PATH_APPORDER_SERVER + "/Resource/library.js'></script>");
        out.println("<script>");
        if( objHashMap.get("strMessage") != null ){

          out.println("alert('"+objHashMap.get("strMessage")+"');");

        }else{

          CoAssignmentBean objCoAssignmentBean = (CoAssignmentBean)objHashMap.get("objBean");

          String idSiteOrig     = objCoAssignmentBean.getNpbscspaymntrespcustomeridId();
          //String desSiteOrig    = objCoAssignmentBean.getNporigsitedesc();
          String desSiteOrig    = MiUtil.getMessageClean(objCoAssignmentBean.getNporigsitedesc());
          String idContracto    = objCoAssignmentBean.getNpbscscontractId();

          System.out.println("idContracto : " + idContracto);
          System.out.println("idSiteOrig : " + idSiteOrig);
          System.out.println("desSiteOrig : " + desSiteOrig);

          objHashMap = objBillingAccountService.BillingAccountDAOgetCoAssignmentBillingByContract(MiUtil.parseLong(idContracto));

          if( objHashMap.get("strMessage") != null )
            out.println("alert('"+objHashMap.get("strMessage")+"');");
          else{
            objArrayList = (ArrayList)objHashMap.get("objArrayList");

            //out.println("try{     ");
            out.println("form = parent.mainFrame.document.frmdatos;     ");
            out.println("form.txtOrigResponsablePago["+av_current_index+"].value = '"+desSiteOrig +"';");
            /**Codigo Original de RP**/
            out.println("form.hdnOrigResP["+av_current_index+"].value = '"+idSiteOrig +"';");
            /**Codigo del Contrato**/
            out.println("form.hdnContractId["+av_current_index+"].value = '"+idContracto +"';");
            /**Servicios de Factura**/
            for(int i=0; i<objArrayList.size(); i++){
              objCoAssignmentBean = new CoAssignmentBean();
              objCoAssignmentBean = (CoAssignmentBean)objArrayList.get(i);

              out.println("opcion=new Option( '"+objCoAssignmentBean.getNporigsitedesc()+"' , '"+objCoAssignmentBean.getNpbscssncode()+"' ) ");
              //out.println("form.txtOrigFactura["+av_current_index+"].options["+i+"+1]=opcion;");
            }
          }
          //out.println("}catch(e){} ");
        }

      if (MiUtil.parseInt(strSpecificationId) == Constante.SPEC_CAMBIAR_ESTRUCT_CUENTA || MiUtil.parseInt(strSpecificationId) == Constante.SPEC_TRANSFERENCIA) {
          // MMONTOYA [Bolsa Celular]
          validateCommunites(request, objProductBean, strPhoneNumber, av_current_index, out);
      } else {
        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
      }

        out.println("</script>");
  }

    /**
     * MMONTOYA [Bolsa Celular]
     * Método invocado por las ordenes que realizan takeover:
     * 2016 SERVICIOS ADICIONALES CAMBIAR ESTRUCTURA DE CUENTA
     * 2017 TRANSFERENCIA TRANSFERENCIA NUEVA & ADICION
     * @param request
     * @param objProductBean
     * @param strPhoneNumber
     * @param av_current_index
     * @param out
     */
    private void validateCommunites(HttpServletRequest request, ProductBean objProductBean, String strPhoneNumber, String av_current_index, PrintWriter out) {
        System.out.println("***objProductBean.getNpcustomerid_subscriber()***: " + objProductBean.getNpcustomerid_subscriber());
        System.out.println("***objProductBean.getNpcustomerid_paymntresp()***: " + objProductBean.getNpcustomerid_paymntresp());

        long customerBscsId = objProductBean.getNpcustomerid_paymntresp();
        HashMap objHashMap = (new BagMobileService()).validateCommunites(strPhoneNumber, customerBscsId);

        SEJBBagMobileBean.CommunitesValidationResult validationResult = (SEJBBagMobileBean.CommunitesValidationResult) objHashMap.get("validationResult");
        System.out.println("***validationResult:*** " + validationResult);

        String strMessage = null;
        if (objHashMap.get("strMessage") != null) {
            strMessage = MiUtil.getMessageClean((String)objHashMap.get("strMessage"));
  }

        boolean isPopUp = av_current_index == null; // Ordenes 2017 TRANSFERENCIA TRANSFERENCIA NUEVA & ADICION

        // Inicio funcion java script.
        out.println("function disabledSaveButtonAndReplaceDocument(disabled) {");
        if (!isPopUp) {
            out.println("    if (form.btnSaveOrder) {");
            out.println("        form.btnSaveOrder.disabled = disabled;");
            out.println("    } else {");
            out.println("        form.btnUpdOrder.disabled = disabled;");
            out.println("        form.btnUpdOrderInbox.disabled = disabled;");
            out.println("    }");

            out.println("    if (disabled) {");
            out.println("        form.txtPhoneNumber[" + av_current_index + "].focus();");
            out.println("    }");

            out.println("    location.replace('" + Constante.PATH_APPORDER_SERVER + "/Bottom.html'); ");
        } else {
            out.println("    if (disabled) {");
            out.println("        form.txt_ItemPhone.value = '';"); // Se limpia para que no pase la validación de campo requerido.
            out.println("        form.txt_ItemPhone.focus();");
            out.println("    }");
        }
        out.println("}");
        // Fin funcion java script.

        switch (validationResult) {
            case PARENT_CONTRACT:
                out.println("alert('" + strMessage + "');");
                out.println("disabledSaveButtonAndReplaceDocument(true);");
                break;
            case CHILD_CONTRACT:
                out.println("if (confirm('" + strMessage + "')) {");
                out.println("    location.replace('" + request.getContextPath() + "/serviceservlet?myaction=removeAllCommunities&phoneNumber=" + strPhoneNumber + "&customerBscsId=" + customerBscsId + "&isPopUp="  + isPopUp + "');");
                out.println("} else {");
                out.println("    disabledSaveButtonAndReplaceDocument(true);");
                out.println("}");
                break;
            case NONE:
                if (strMessage == null) {
                    // Validación OK.
                    out.println("disabledSaveButtonAndReplaceDocument(false);");
                } else {
                    // Mensaje de error.
                    out.println("alert('" + strMessage + "');");
                    out.println("disabledSaveButtonAndReplaceDocument(true);");
                }

                break;
        }
    }

    /**
     * MMONTOYA [Bolsa Celular]
     * @param request
     * @param response
     * @param out
     */
    private void removeAllCommunities(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String phoneNumber = request.getParameter("phoneNumber");
        long customerBscsId = Integer.parseInt(request.getParameter("customerBscsId"));
        boolean isPopUp = Boolean.parseBoolean(request.getParameter("isPopUp"));

        System.out.println("[ServiceServlet][removeAllCommunities]");
        System.out.println("phoneNumber: " + phoneNumber);
        System.out.println("customerBscsId: " + customerBscsId);

        HashMap hshDataMap = (new BagMobileService()).removeAllCommunities(phoneNumber, customerBscsId);
        if (hshDataMap.get("strMessage") != null) {
            String strMessage = MiUtil.getMessageClean((String)hshDataMap.get("strMessage"));

            out.println("<script>");

            // Inicio funcion java script.
            out.println("function disabledSaveButtonAndReplaceDocument(disabled) {");
            out.println("    var form = parent.mainFrame.frmdatos;");
            if (!isPopUp) {
                out.println("    if (form.btnSaveOrder) {");
                out.println("        form.btnSaveOrder.disabled = disabled;");
                out.println("    } else {");
                out.println("        form.btnUpdOrder.disabled = disabled;");
                out.println("        form.btnUpdOrderInbox.disabled = disabled;");
                out.println("    }");

                out.println("    location.replace('" + Constante.PATH_APPORDER_SERVER + "/Bottom.html');");
            } else {
                out.println("    form.txt_ItemPhone.value = '';"); // Se limpia para que no pase la validación de campo requerido.
                out.println("    form.txt_ItemPhone.focus();");
            }
            out.println("}");
            // Fin funcion java script.

            // Mensaje de error.
            out.println("alert('" + strMessage + "');");
            out.println("disabledSaveButtonAndReplaceDocument(true);");

            out.println("</script>");
            out.close();
        }
    }

  /*Se elimina la invocación solución de la cabecera para cargar la solución del header del item  */
  public void loadServiceItems(HttpServletRequest request, HttpServletResponse response,PrintWriter out){

        String    av_services_item    = (String)request.getParameter("servicios_item");
        String    strphoneNumber      = (String)request.getParameter("phonenumber");
        String    wv_service_name     = "";
        String    wv_serv_short_name  = "";
        String    wv_npexcludingind   = "";
        String    wv_message          = "";
        Vector    la_services         = new Vector();
        int       wn_service_id       = 0;
        int       wn_num_elementos    = 0;
        int       wn_indice           = 0;
        int       wn_id_servicio      = 0;
        //int       n                   = 0;
        ArrayList objArrayList        = new ArrayList();

       System.out.println("Entramos : " + av_services_item);

    out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/library.js'></script>");
    out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
    out.println("<script language='JavaScript'>");
    out.println("v_doc  = parent.mainFrame;     ");
    out.println("     form = parent.mainFrame.document.frmdatos;     ");
    out.println("     var valor;     ");
    out.println("     var duracion;     ");
    out.println("     var orderDeactId;     ");
    out.println("     function ServicioJS(id, name ,nameShort ,exclude, group, duration) {     ");
    out.println("        this.id = id;     ");
    out.println("        this.name = name;     ");
    out.println("        this.nameDisplay = (exclude==\"\")?name:name+\" - \"+exclude;     ");
    out.println("        this.nameShort = nameShort;     ");
    out.println("        this.nameShortDisplay = (exclude==\"\")?nameShort:nameShort+\" - \"+exclude;     ");
    out.println("        this.group = group;     ");
    out.println("        this.duration = duration;     ");
    out.println("     }     ");
    out.println("     // Vector de Servicios     ");
    out.println("     var vServicio = new Vector();     ");
    out.println("     function deleteAllValues(listField){     ");
    out.println("        for (i=listField.options.length-1; i>=0; i--){     ");
    out.println("           listField.options[i] = null;     ");
    out.println("        }     ");
    out.println("     }");

    int intPlanId             = MiUtil.parseInt(request.getParameter("cmb_ItemPlanTarifario"));
    int intSolutionId         = MiUtil.parseInt(request.getParameter("cmb_ItemSolution"));
    String hdnSpecification = (String)request.getParameter("hdnSpecification");
    long lorderId = MiUtil.parseLong(request.getParameter("lorderId"));
    System.out.println("[loadServiceItems][iPlanId]"+intPlanId);
    System.out.println("[loadServiceItems][strSolutionId]"+intSolutionId);
    System.out.println("[loadServiceItems][hdnSpecification]"+hdnSpecification);
    String strType = null;
	 String sExcludingind="";
    GeneralService objGeneralService = new GeneralService();
    if (MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_CAMB_PLAN_BA || MiUtil.parseInt(hdnSpecification)==Constante.KN_ACT_DES_SERVICIOS_BA){
         strType = "A";
    }
    System.out.println("[loadServiceItems][strType]"+strType);

    /**INI - Cambios LROSALES**/

     int intProductId =0;
     if (MiUtil.parseInt(hdnSpecification)==Constante.SPEC_POSTPAGO_VENTA){
      intProductId = MiUtil.parseInt(request.getParameter("hdnItemValuetxtItemProduct"));
     } else {
      intProductId          = MiUtil.parseInt(request.getParameter("txtProductId"))==0?MiUtil.parseInt(request.getParameter("hdnItemValuetxtItemModel")):MiUtil.parseInt(request.getParameter("txtProductId"));
     }

    String strSSAAType        = null;//johncmb
    //john inicio
    if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES ||
        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS ||
        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
        MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE //Se agrega reposicion prepago tde - TDECONV034
    ) {
      strSSAAType  = "0,1,2,3,5";}
    else{
      strSSAAType = "0,1,5"; }

    /**FIN - Cambios LROSALES**/

    /**Cambios LROSALES**/
    HashMap hServiceList        = objGeneralService.getServiceList(intSolutionId,intPlanId,intProductId,strSSAAType,strType);
    //john fin
    objArrayList =(ArrayList)hServiceList.get("objServiceList");

    if ( objArrayList != null && objArrayList.size() > 0 ){

          ServiciosBean serviciosBean = null;
          for( int i = 0; i < objArrayList.size(); i++ ){
          serviciosBean = (ServiciosBean)objArrayList.get(i);
					sExcludingind="";
					if (serviciosBean.getNpexcludingind()!=null){
						sExcludingind=serviciosBean.getNpexcludingind();
					}
        out.println("vServicio.addElement(new ServicioJS("+serviciosBean.getNpservicioid()+",'"+serviciosBean.getNpnomserv()+"','"+serviciosBean.getNpnomcorserv()+"','"+sExcludingind+"','"+serviciosBean.getNpgroup()+"','"+serviciosBean.getNpduration()+"'));");
        out.println("");
          }
    }
    StringTokenizer tokens  = new StringTokenizer(av_services_item,"|");
        while(tokens.hasMoreTokens()){
        String aux = tokens.nextToken();
            la_services.addElement((String)aux);

        }

    wn_num_elementos = (la_services.size())/3;
    //Ingresamos los servicios del arreglo que no existe en el vector de servicio
   //---------------------------------------------------------------------------
   /* int n1=0;
    int n2=0;
    for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
        ServiciosBean serviciosBeanDef = null;
        if ( objArrayList != null && objArrayList.size() > 0 ){
            for( int z = 0; z < objArrayList.size(); z++ ){
                serviciosBeanDef = (ServiciosBean)objArrayList.get(z);
                if (serviciosBeanDef.getNpservicioid() == Long.parseLong((String)la_services.elementAt(n)) ) { n1=1;
                }else{ n2=1;}
            }
        }
         if(n1==0 || n2==0){
          if(wn_num_elementos > 1){
              ServiciosBean serviciosBean1 = objGeneralService.getDetailService(Long.parseLong((String)la_services.elementAt(n)));
              out.println("vServicio.addElement(new Servicio("+serviciosBean1.getNpservicioid()+",'"+serviciosBean1.getNpnomserv()+"','"+serviciosBean1.getNpnomcorserv()+"','"+serviciosBean1.getNpexcludingind()+"'));");
          }
         }
      n1=0;
      n2=0;
    }*/

    for (int n=0,j = 1;  j <= (la_services.size()/3); n=n+3,j++)
    {
       boolean isExist=false;
       ServiciosBean serviciosBeanDef = null;
       if ( objArrayList != null && objArrayList.size() > 0 ){
            for( int z = 0; z < objArrayList.size(); z++ ){
                serviciosBeanDef = (ServiciosBean)objArrayList.get(z);
                if (serviciosBeanDef.getNpservicioid() == Long.parseLong((String)la_services.elementAt(n)) ){
                  isExist=true;
                  break;
                }
            }
         if(!isExist){
          ServiciosBean serviciosBean1 = objGeneralService.getDetailService(Long.parseLong((String)la_services.elementAt(n)));
          out.println("vServicio.addElement(new ServicioJS("+serviciosBean1.getNpservicioid()+",'"+serviciosBean1.getNpnomserv()+"','"+serviciosBean1.getNpnomcorserv()+"','"+serviciosBean1.getNpexcludingind()+"','"+serviciosBean1.getNpgroup()+"','"+serviciosBean1.getNpduration()+"'));");
         }
         }
       }

    //HashMap hOrderDeactList   = objGeneralService.getOrderDeactList()
    wn_indice = 1;

    if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_SUSCRIPCIONES ){ //Servicios a nivel de Orden
        out.println(" ");
        out.println("     if (form.cmbSelectedSubscription!=null){ ");
        out.println("        deleteAllValues(form.cmbSelectedSubscription); ");
        out.println("        wn_cantServ = "+wn_num_elementos+"; ");
        out.println("        // variable para separacion de las \"x\" en el combo \"cmbSelectedSubscription\" ");
        out.println("        var txt_separacion=\"   \"; ");
        out.println("        var txt_separacion2=\"    \"; ");
        out.println("        var txt_separacion3=\"            \"; ");
        out.println("        // tomamos como maximo 25 caracteres (blancos)para almacenar los servicios seleccionados ");
        out.println("        var txt_vacios_2=\"                         \"; ");
        out.println("        var txt_texto=\"\"; ");
        out.println("             //alert(wn_cantServ); ");

        for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
            out.println("    for (i=0; i < vServicio.size();i++) {    ");
            out.println("          objServicio = vServicio.elementAt(i);    ");
            out.println("          if (objServicio.id == \""+(String)la_services.elementAt(n)+"\") {    ");
            out.println("             txt_servicio = objServicio.nameDisplay + txt_vacios_2;    ");
            out.println("             txt_servicio_2 = txt_servicio.substr(0,30);    ");
            out.println("             duracion = objServicio.duration + txt_separacion2;    ");
            out.println("             duracion = duracion.substr(0,3);      ");
            HashMap hOrderDeactId = objGeneralService.getOrderDeact(lorderId,strphoneNumber,MiUtil.parseLong((String)la_services.elementAt(n)));
            String strorderDeactId = (String)hOrderDeactId.get("orderDeact");
            String strorderDeactId2 = "0";
            if(!strorderDeactId.equals("0")){
              strorderDeactId2 = strorderDeactId;
              out.println("             orderDeactId = " + strorderDeactId2 + " + txt_separacion3;    ");
              out.println("             orderDeactId = orderDeactId.substr(0,10);      ");
            }else{
              out.println("             orderDeactId = " + strorderDeactId2 + " + txt_separacion3;    ");
              out.println("             orderDeactId = orderDeactId.substr(1,10);      ");
            }
            out.println("             if ( \""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\" ){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\"x\" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             };    ");
            out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"N\"){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\" \" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             }    ");
            out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"N\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\"){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \" \" + txt_separacion +\"x\" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             }    ");
            out.println("             oOption = parent.mainFrame.document.createElement(\"option\");    ");
            out.println("             oOption.value  = objServicio.name;    ");
            out.println("             oOption.text   = txt_texto;    ");
            out.println("             form.cmbSelectedSubscription.add(oOption);    ");
            out.println("             txt_texto=\"\";    ");
            out.println("          } /* end_if */    ");
            out.println("       } //end_for i    ");
        }
    }else if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_SSAA_PROMOTIONS ){ //DLAZO
        out.println(" ");
        out.println("     if (form.cmbSelectedPromotion!=null){ ");
        out.println("        deleteAllValues(form.cmbSelectedPromotion); ");
        out.println("        wn_cantServ = "+wn_num_elementos+"; ");
        out.println("        // variable para separacion de las \"x\" en el combo \"cmbSelectedPromotion\" ");
        out.println("        var txt_separacion=\"   \"; ");
        out.println("        var txt_separacion2=\"    \"; ");
        out.println("        var txt_separacion3=\"            \"; ");
        out.println("        // tomamos como maximo 25 caracteres (blancos)para almacenar los servicios seleccionados ");
        out.println("        var txt_vacios_2=\"                         \"; ");
        out.println("        var txt_texto=\"\"; ");
        out.println("             //alert(wn_cantServ); ");

        for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
            out.println("    for (i=0; i < vServicio.size();i++) {    ");
            out.println("          objServicio = vServicio.elementAt(i);    ");
            out.println("          if (objServicio.id == \""+(String)la_services.elementAt(n)+"\") {    ");
            out.println("             txt_servicio = objServicio.nameDisplay + txt_vacios_2;    ");
            out.println("             txt_servicio_2 = txt_servicio.substr(0,30);    ");
            out.println("             duracion = objServicio.duration + txt_separacion2;    ");
            out.println("             duracion = duracion.substr(0,3);      ");
            HashMap hOrderDeactId = objGeneralService.getOrderDeact(lorderId,strphoneNumber,MiUtil.parseLong((String)la_services.elementAt(n)));
            String strorderDeactId = (String)hOrderDeactId.get("orderDeact");
            String strorderDeactId2 = "0";
            if(!strorderDeactId.equals("0")){
              strorderDeactId2 = strorderDeactId;
              out.println("             orderDeactId = " + strorderDeactId2 + " + txt_separacion3;    ");
              out.println("             orderDeactId = orderDeactId.substr(0,10);      ");
            }else{
              out.println("             orderDeactId = " + strorderDeactId2 + " + txt_separacion3;    ");
              out.println("             orderDeactId = orderDeactId.substr(1,10);      ");
            }
            out.println("             if ( \""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\" ){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\"x\" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             };    ");
            out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"N\"){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\" \" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             }    ");
            out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"N\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\"){    ");
            out.println("                txt_texto = txt_servicio_2 + txt_separacion + \" \" + txt_separacion +\"x\" + txt_separacion2 + duracion + txt_separacion + orderDeactId;");
            out.println("             }    ");
            out.println("             oOption = parent.mainFrame.document.createElement(\"option\");    ");
            out.println("             oOption.value  = objServicio.name;    ");
            out.println("             oOption.text   = txt_texto;    ");
            out.println("             form.cmbSelectedPromotion.add(oOption);    ");
            out.println("             txt_texto=\"\";    ");
            out.println("          } /* end_if */    ");
            out.println("       } //end_for i    ");
        }
    }else{

    out.println(" ");
    out.println("     if (form.cmbSelectedServices!=null){ ");
    out.println("        deleteAllValues(form.cmbSelectedServices); ");
    out.println("        wn_cantServ = "+wn_num_elementos+"; ");
    out.println("        // variable para separacion de las \"x\" en el combo \"cmbSelectedServices\" ");
    out.println("        var txt_separacion=\"  \"; ");
    out.println("        // tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados ");
    out.println("        var txt_vacios_2=\"          \"; ");
    out.println("        var txt_texto=\"\"; ");

        for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
        out.println("    for (i=0; i < vServicio.size();i++) {    ");
        out.println("          objServicio = vServicio.elementAt(i);    ");
        out.println("          if (objServicio.id == \""+(String)la_services.elementAt(n)+"\") {    ");
        out.println("             txt_servicio = objServicio.nameShortDisplay + txt_vacios_2;    ");
        out.println("             txt_servicio_2 = txt_servicio.substr(0,12);    ");
        out.println("             if ( \""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\" ){    ");
        out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\"x\";    ");
        out.println("             };    ");
        out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"S\" && \""+(String)la_services.elementAt(n+2)+"\" == \"N\"){    ");
        out.println("                txt_texto = txt_servicio_2 + txt_separacion + \"x\" + txt_separacion +\" \";    ");
        out.println("             }    ");
        out.println("             else if (\""+(String)la_services.elementAt(n+1)+"\" == \"N\" && \""+(String)la_services.elementAt(n+2)+"\" == \"S\"){    ");
        out.println("                txt_texto = txt_servicio_2 + txt_separacion + \" \" + txt_separacion +\"x\";    ");
        out.println("             }    ");
        out.println("             oOption = parent.mainFrame.document.createElement(\"option\");    ");
        out.println("             oOption.value  = objServicio.nameShort;    ");
        out.println("             oOption.text   = txt_texto;    ");
        out.println("             form.cmbSelectedServices.add(oOption);    ");
        out.println("             txt_texto=\"\";    ");
        out.println("          } /* end_if */    ");
        out.println("       } //end_for i    ");
        }
    }

        out.println("       } //end if    ");
        out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
        out.println("</script>");
        out.close();
  }


  public void doValidateNewPhoneToChange(HttpServletRequest request, HttpServletResponse response,PrintWriter out){

      String strPhoneNumber      = MiUtil.getString(request.getParameter("paramPhoneNumber"));
      String strCustomerId       = MiUtil.getString(request.getParameter("paramCustomerId"));
      String strSpecificationId  = MiUtil.getString(request.getParameter("paramSpecificationId"));
      String strSiteId           = MiUtil.getString(request.getParameter("paramSiteId"));

      System.out.println("strSiteId en doValidateNewPhoneToChange "+strSiteId);

      NewOrderService objNewOrderService = new NewOrderService();
      GeneralService objGeneralService = new GeneralService();
      strPhoneNumber  = objGeneralService.GeneralDAOgetWorldNumber(strPhoneNumber,"COUNTRY");
      HashMap objHashMap = objNewOrderService.ProductDAOgetDetailByPhone(strPhoneNumber,MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSiteId),MiUtil.parseLong(strSpecificationId));

      out.println("<script>");
      out.println("form = parent.mainFrame;     ");

      if( objHashMap.get("strMessage") != null ){
         out.println("alert('"+MiUtil.getMessageClean((String)objHashMap.get("strMessage"))+"');");
         out.println(" form.fxObjectConvert('txt_ItemNewNumber','');");
      }
      out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
      out.println("</script>");
      out.close();
  }


  public void doValidateKitPrice(HttpServletRequest request, HttpServletResponse response,PrintWriter out)
  throws ServletException, IOException
  {

      String strModality      = MiUtil.getString(request.getParameter("strModality"));
      String strProductId       = MiUtil.getString(request.getParameter("strProductId"));
      String strExceptionPrice       = MiUtil.getString(request.getParameter("strExceptionPrice"));
      String strPrice       = MiUtil.getString(request.getParameter("strPrice"));
      long lSalesStructOrigenId = MiUtil.parseLong((String)request.getParameter("strSalesStuctOrigenId"));
      float precio   = MiUtil.parseLong(strPrice);
      float precioExcepcion   = MiUtil.parseLong(strExceptionPrice);
      float descuento = precio - precioExcepcion;

      System.out.println("strSiteId en doValidateKitPrice ");
      System.out.println("strModality en doValidateKitPrice: "+strModality);
      System.out.println("strProductId en doValidateKitPrice: "+strProductId);
      System.out.println("strExceptionPrice en doValidateKitPrice: "+strExceptionPrice);
      System.out.println("strPrice en doValidateKitPrice: "+strPrice);
      System.out.println("lSalesStructOrigenId en doValidateKitPrice: "+lSalesStructOrigenId);
      System.out.println("precio en doValidateKitPrice: "+precio);
      System.out.println("precioExcepcion en doValidateKitPrice: "+precioExcepcion);
      System.out.println("descuento en doValidateKitPrice: "+descuento);

      EditOrderService objEditOrderService = new EditOrderService();
      GeneralService objGeneralService = new GeneralService();

      float productPrice = objEditOrderService.getKitEquipmentPrice(MiUtil.parseLong(strProductId),strModality,lSalesStructOrigenId);
      System.out.println("productPrice en doValidateKitPrice: "+productPrice);

      out.println("<script>");
      out.println("form = parent.mainFrame;     ");
      if  (descuento > productPrice){
        out.println("alert('El descuento máximo que puede hacer es de "+ productPrice +"');");
        out.println("form = parent.mainFrame.frmdatos.txt_ItemPriceException.focus();     ");
      }
      out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
      out.println("</script>");
      out.close();
  }


	public void doSetData(HttpServletRequest request, HttpServletResponse response,PrintWriter out){

		String        strProductLineId      = request.getParameter("pProductLine");
		String        strProductId       	= request.getParameter("pProduct");
		String        strModalityId  		= request.getParameter("pModality");
		String        strSpecificationId  	= request.getParameter("pSpecificationId");
		String        strPlanTarifarioId  	= request.getParameter("pPlanTarifario");

		ProductBean   objProductParamBean      = new ProductBean();
		objProductParamBean.setNpcategoryid(MiUtil.parseLong(strSpecificationId));
		objProductParamBean.setNpproductlineid(MiUtil.parseLong(strProductLineId));
		objProductParamBean.setNpproductid((MiUtil.parseLong(strProductId)));
		objProductParamBean.setNpmodality(strModalityId);
		objProductParamBean.setNpplanid((MiUtil.parseLong(strPlanTarifarioId)));
		out.println("<script>");
		setDataDefaultInControl(strSpecificationId,Constante.FROM_ONCHANGE,objProductParamBean,out);
		out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html')");
		out.println("</script>");
	}

	public void setDataDefaultInControl(String strSpecificationId,String strOrigenLlamada,ProductBean objProductBean,PrintWriter out){

		//Inicio CEM - COR0303
		GeneralService objGeneralService = new GeneralService();
		HashMap oHashMap=objGeneralService.getDataNpTable(Constante.SET_VALUE_IN_CONTROL,strSpecificationId);
		ArrayList     objArrayList = (ArrayList)oHashMap.get("objArrayList");

    if( oHashMap.get("strMessage") != null ){
      out.println("alert('"+oHashMap.get("strMessage")+"');");
      out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
      out.println("</script>");
      out.close();
		}else{
			if (objArrayList.size()!=0){
				ProductBean   objAuxProductBean      = new ProductBean();
				//Datos del producto que se utilizan como parametros de filtro
				objAuxProductBean.setNpcategoryid(MiUtil.parseLong(strSpecificationId));
				objAuxProductBean.setNpproductlineid(objProductBean.getNpproductlineid());
				objAuxProductBean.setNpmodality(objProductBean.getNpmodality());

				UtilForServlet objUtilForServlet = new UtilForServlet();
				HashMap objHashMapResultado = new HashMap();
				objHashMapResultado.put("objProductBean",objAuxProductBean);
				objHashMapResultado.put("objProductDefault",MiUtil.getString(objProductBean.getNpproductid()));
				objHashMapResultado.put("objProductLineDefault",MiUtil.getString(objProductBean.getNpproductlineid()));
				objHashMapResultado.put("objPlanTarifarioDefault",MiUtil.getString(objProductBean.getNpplanid()));
				objHashMapResultado.put("strOrigenLlamada",strOrigenLlamada);
				for (int i=0; i<objArrayList.size();i++){
					NpTableBean objNpTableBean=(NpTableBean)objArrayList.get(i);
					objUtilForServlet.selectControl(objNpTableBean.getNptag1(),objNpTableBean.getNptag2(),out,objHashMapResultado);
				}
			}
		}
		//Fin CEM - COR0303

	}


  public void loadDetailFixedPhone(HttpServletRequest request, HttpServletResponse response,PrintWriter out) throws ServletException,IOException{
    String strPhoneNumber       = request.getParameter("paramPhoneNumber");
    long   lngSwCustomerId      = MiUtil.parseLong((String)request.getParameter("paramCustomer"));
    long   lngSpecificationId   = MiUtil.parseLong((String)request.getParameter("paramSpecification"));
    long   lngSolutionId        = MiUtil.parseLong((String)request.getParameter("strSolution"));
    long   lngInstallAddressId  = MiUtil.parseLong((String)request.getParameter("strInstallAddressId"));

    GeneralService objGeneralService = new GeneralService();


    //String strControlName     = MiUtil.getString((String)request.getParameter("nameText"));
    HashMap hValidatePhone = objGeneralService.getValidatePhoneVoIp(lngSwCustomerId, strPhoneNumber,lngSpecificationId, lngSolutionId, lngInstallAddressId);
    //hValidatePhone.put("strControlName", strControlName);
    //hValidatePhone.put("strItemSolutionId", MiUtil.getString(lngSolutionId));
    //request.setAttribute("hValidatePhone", hValidatePhone);
    if( hValidatePhone.get("strMessage") != null ){
      out.println("<script>");
      out.println("alert('"+hValidatePhone.get("strMessage")+"');");
      out.println(" location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html'); ");
      out.println("</script>");
      out.close();
    }else{
      loadDetailPhone(request,response,out);
    }
  }

   public void fxLoadServiceComercialCore (long lIdPlan,PrintWriter out){
    out.println("try {");
    out.println("vDoc.vServiciosComercialesCore.removeElementAll();");
    NewOrderService objNewOrderService = new NewOrderService();
    System.out.println("[ServiceServlet][fxLoadServiceComercialCore]lIdPlan----->"+lIdPlan);
     if(lIdPlan!=0)
     {
      HashMap hshCoreService    = objNewOrderService.ServiceDAOgetCoreServicebyPlan(lIdPlan);
      ServiciosBean objServiciosBean = null;
      //   HashMap hServiceList = (HashMap) request.getAttribute("hServiceList");
      ArrayList listServices =(ArrayList)hshCoreService.get("objServiciosBean");
      String strMessageService = (String)hshCoreService.get("strMessage");
      //System.out.println("size:"+listServices.size());
        if (strMessageService==null){
           System.out.println("[ServiceServlet][fxLoadSCC]size:"+listServices.size());
           for(int i=0;i<listServices.size();i++){
            objServiciosBean = (ServiciosBean)listServices.get(i);
          out.println("vDoc.vServiciosComercialesCore.addElement(new Servicio('"+MiUtil.getString(objServiciosBean.getNpservicioid())+"','"+objServiciosBean.getNpnomserv()+"','"+objServiciosBean.getNpnomcorserv()+"','"+MiUtil.getString(objServiciosBean.getNpexcludingind())+"'));");
           }
        }else{
          out.println("alert('"+MiUtil.getMessageClean(strMessageService)+"');");
           // alert(" [LoadServiceList.jsp ] <%=MiUtil.getMessageClean(strMessageService)%>");
        }
    }
     out.println("}catch(e) {}");

  }

  public void validaDiasSuspension(HttpServletRequest request,HttpServletResponse response, PrintWriter out){
     String        strPhoneNumber      = request.getParameter("paramPhoneNumber");
     String        strNpScheduleDate   = request.getParameter("strNpScheduleDate");
     String        strNpScheduleDate2  = request.getParameter("strNpScheduleDate2");
     String        strSpecificationId  = request.getParameter("paramSpecification");
     NewOrderService objNewOrderService = new NewOrderService();

     System.out.println("[validaDiasSuspension]validaDiasSuspension -Inicio-"+strPhoneNumber);
     HashMap objHashMapSusp = objNewOrderService.ProductDAOvalidaDiasSuspension(strPhoneNumber,MiUtil.parseInt(strSpecificationId),strNpScheduleDate, strNpScheduleDate2);
     System.out.println("[validaDiasSuspension]validaDiasSuspension -Fin-"+strPhoneNumber);
        if( objHashMapSusp.get("strMessage") != null ){
           out.println("alert('"+MiUtil.getMessageClean((String)objHashMapSusp.get("strMessage"))+"');");
        }
  }

  public void validaDiasSuspensionCrear(HttpServletRequest request,HttpServletResponse response, PrintWriter out){

     String        strPhoneNumberCadena = (String)request.getParameter("paramPhoneNumber");
     String        strNpScheduleDate    = request.getParameter("strNpScheduleDate");
     String        strNpScheduleDate2   = request.getParameter("strNpScheduleDate2");
     String        strSpecificationId   = request.getParameter("paramSpecification");
     Vector        vPhonosItemsOrder    = (Vector)request.getAttribute("vPhonosItemsOrder");
     String        strPhonosMessage     = "";
     NewOrderService objNewOrderService = new NewOrderService();
     StringTokenizer  stPhonos;
     stPhonos = new StringTokenizer(strPhoneNumberCadena, "/");
     int tokens = stPhonos.countTokens();

     while(stPhonos.hasMoreTokens()){
         String strPhoneNumber = stPhonos.nextToken();
         System.out.println("[validaDiasSuspension]validaDiasSuspension -Inicio-"+strPhoneNumber);
         HashMap objHashMapSusp = objNewOrderService.ProductDAOvalidaDiasSuspension(strPhoneNumber,MiUtil.parseInt(strSpecificationId),strNpScheduleDate, strNpScheduleDate2);
         System.out.println("[validaDiasSuspension]validaDiasSuspension -Fin-"+strPhoneNumber);
            if( objHashMapSusp.get("strMessage") != null ){
               strPhonosMessage += strPhoneNumber;
               strPhonosMessage += ",";
            }
     }

     if( strPhonosMessage != "" ){
        if (tokens > 1)
           out.println("<SCRIPT>alert('Los Teléfonos: "+strPhonosMessage+" excedieron los 60 días de suspensión.');</SCRIPT>");
        else
           out.println("<SCRIPT>alert('El Teléfono: "+strPhonosMessage+" excedió los 60 días de suspensión.');</SCRIPT>");
     }

  }

}