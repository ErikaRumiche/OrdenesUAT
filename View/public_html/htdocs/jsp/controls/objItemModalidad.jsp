<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ModalityBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MyUtil" %>

<%
  System.out.println("*********************************************objItemModalidad.jsp*********************************************");
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strflagvep = "",
            strDivision = "",
            strObjectReadOnly = "",
            //INICIO: AMENDEZ | PRY-1049
            strHdnCobertura="",
            //FIN: AMENDEZ | PRY-1049
            //INICIO: PRY-0864 | AMENDEZ
            strInitialQuota="";
  int evaluateExceptionPriceVep=0;
            //FIN: PRY-0864 | AMENDEZ
    strObjectReadOnly = request.getParameter("strObjectReadOnly");
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision             =   (String)hshtinputNewSection.get("strDivision");
    strflagvep              =   (String)hshtinputNewSection.get("strflagvep");
    strHdnCobertura         =   (String)hshtinputNewSection.get("strHdnCobertura");

    //INICIO: PRY-0864 | AMENDEZ
    int npvep=MiUtil.parseInt(strflagvep);
    long sw_customerid=MiUtil.parseLong(strCodigoCliente);
    strInitialQuota         =   (String)hshtinputNewSection.get("strInitialQuota");

    System.out.println("strCodigoCliente: " + strCodigoCliente);
    System.out.println("strnpSite: " + strnpSite);
    System.out.println("strCodBSCS: " + strCodBSCS);
    System.out.println("hdnSpecification: " + hdnSpecification);
    System.out.println("strDivision: " + strDivision);
    System.out.println("strflagvep: " + strflagvep);
    System.out.println("strInitialQuota: " + strInitialQuota);
    System.out.println("strHdnCobertura      : " + strHdnCobertura);
    evaluateExceptionPriceVep=objNewOrderService.evaluateExceptionPriceVep(npvep,sw_customerid);
    //FIN: PRY-0864 | AMENDEZ
      System.out.println("hshtinputNewSection.toString()      : " + hshtinputNewSection.toString());
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
    System.out.println("******************************************************************************************");
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String idSpecificationId = (String)request.getAttribute("idSpecificationId");
  String strMessage = "";
  String strErrorLocal = "";
  HashMap objHashMap = objNewOrderService.OrderDAOgetModalityList(MiUtil.parseLong(hdnSpecification),null,null,null);
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
  
  ModalityBean modalityBean = null;
  
%>
<%
    if("S".equals(strObjectReadOnly)){
%>
<select disabled="disabled" name="<%=nameHtmlItem%>" onchange="javascript:fxChangeObjectModality(this.value)">
<%}else{%>
    <select name="<%=nameHtmlItem%>" onchange="javascript:fxChangeObjectModality(this.value)" > 
<%}%>
       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
         <% 
            if ( objArrayList != null && objArrayList.size() > 0 ){
                int cont = 1;
               for( int i=0; i<objArrayList.size();i++ ){ 
                    modalityBean = new ModalityBean();
                    modalityBean = (ModalityBean)objArrayList.get(i);
                  
                  if ( modalityBean.getNpstatus().equals("a") ) {
                      if( (cont == 1)  && ("2085".equals(hdnSpecification)) ){
          %>
                        <option selected="selected" value='<%=modalityBean.getNpmodality()%>'>
                            <%=modalityBean.getNpmodality()%>
                        </option>
                     <%}else{%>
          <option value='<%=modalityBean.getNpmodality()%>'>
                         <%=modalityBean.getNpmodality()%>
          </option>
                      <%}
                  }
                      cont++;
                }
            }
          %>
                         
     </select>
     
<script>

   function fxChangeObjectModality(objObjectValue){
      var valueCurrent = "";
      valueCurrent = objObjectValue; 
      
      if( valueCurrent == 'Venta' )    
         fxActionVenta();	 
      else if( valueCurrent == 'Alquiler' )
         fxActionAlquiler();	
      else if( valueCurrent == 'Propio' ) 
         fxActionPropio();		 
      else if( valueCurrent == 'Alquiler en Cliente' )
         fxActionAlquilerCliente();
      else if( valueCurrent == 'Prestamo' )
         fxActionPrestamo();
      else if( valueCurrent == 'Asignacion' )
         fxActionAsignacion();
      
	  try{
		parent.mainFrame.frmdatos.cmb_ItemProductLine.disabled=false;
		parent.mainFrame.frmdatos.cmb_ItemProducto.disabled=false;	  
		ProductLineId=parent.mainFrame.frmdatos.cmb_ItemProductLine.value;
		ProductId=parent.mainFrame.frmdatos.cmb_ItemProducto.value;
		PlanTarifarioId=parent.mainFrame.frmdatos.cmb_ItemPlanTarifario.value;	  
	  }
	  catch(e){
		PlanTarifarioId=""
	  }
   
      formReset();
      parent.mainFrame.frmdatos.cmb_ItemModality.value = valueCurrent;
	  //Inicio CEM COR0303
	    var url = "<%=request.getContextPath()%>/serviceservlet?myaction=setData&pProductLine="+ProductLineId+"&pProduct="+ProductId+"&pModality="+valueCurrent+"&pPlanTarifario="+PlanTarifarioId+"&pSpecificationId="+"<%=hdnSpecification%>";      
	    parent.bottomFrame.location.replace(url);
	  //Fin CEM COR0303
   }
  
  function fxActionVenta(){
    try{

      var form = parent.mainFrame.frmdatos;

      //Sim EAGLE
      fxHideDivByHeaderId(2);
      //Renta
      fxHideDivByHeaderId(20);
      //Precio Insc.
      fxShowDivByHeaderId(18);
      //Precio Excepción.
      //INICIO: PRY-0864 | AMENDEZ
      <%if (!strflagvep.equals("1")){ %>
        fxShowDivByHeaderId(19);
      <%}else if (evaluateExceptionPriceVep==1){ %>
        fxShowDivByHeaderId(19);
      <%}else{%>
        fxHideDivByHeaderId(19);
      <%}%>
      //FIN: PRY-0864 | AMENDEZ
      //Cantidad
      fxShowDivByHeaderId(21);   
      //Fecha Programada de Devolución
      //fxShowDivByHeaderId(14);
      
      //parent.mainFrame.frmdatos.cmb_ItemDevolution.disabled = false; // TMOGROVEJO 23/07/2008 
      
      <%if(MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") 
        || MiUtil.getString(hdnSpecification).equals("2069")|| MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%>
      fxHideDivByHeaderId(85);
      <%}%>
      
      <%if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE ){%>
        fxHideDivByHeaderId(58);
      <%}%>   
      
        //EFLORES CDM+CDP PRY-0817 Modifica este metodo para poder mostrar el checkbox chkMantenerSIM solo para venta u alquiler


        <%if( MiUtil.getString(hdnSpecification).equals("2065")){%>

            if (( form.txt_ItemOriginalSolution.value == "Smart Postpago" || form.txt_ItemOriginalSolution.value =="Smart Prepago" ) && (form.cmb_ItemSolution.value=="15" || form.cmb_ItemSolution.value=="16")) //vasp
            {
                var chkMSIMExists = parent.mainFrame.frmdatos.chkMantenerSIM.checked;
                if(chkMSIMExists !=  null){
                    fxShowDivByHeaderId(151); //Mostramos el id del chkMantenerSIM
                }
            }

        <%}%>
      
         //INICIO DERAZO TDECONV003-2
        fxHideDivByHeaderId(165);
        //FIN DERAZO
      
     }catch(e){}
  }
  
  function fxActionAlquiler(){
      form = parent.mainFrame.frmdatos;    
      try{
      //Sim EAGLE
      fxHideDivByHeaderId(2);
      //Renta
      fxShowDivByHeaderId(20);
       if ( form.cmb_ItemSolution.value == "<%=Constante.KN_TELEFONIA_FIJA%>" )  //nbravo
       {
            fxHideDivByHeaderId(20);
       }
      //Precio Excepción.
      fxShowDivByHeaderId(19);
      //Cantidad
      fxShowDivByHeaderId(21);
      //Precio Cta Inscripcón
      fxShowDivByHeaderId(18);
      //Fecha Programada de Devolución
      //fxShowDivByHeaderId(14);
      
      //parent.mainFrame.frmdatos.cmb_ItemDevolution.value="S"; // TMOGROVEJO 23/07/2008         
      //parent.mainFrame.frmdatos.cmb_ItemPendDevolution.value="S"; // TMOGROVEJO 23/07/2008                 
      //parent.mainFrame.frmdatos.cmb_ItemDevolution.disabled=true; // TMOGROVEJO 23/07/2008      
      //fxOcultarTr("none",idDisplay13);//TMOGROVEJO 23/07/2008
      
      <%if(MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE)))|| MiUtil.getString(hdnSpecification).equals("2002") 
        || MiUtil.getString(hdnSpecification).equals("2069") || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%>
      fxHideDivByHeaderId(85);
      <%}%>

      <%if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE){%>
              fxHideDivByHeaderId(58);
      <%}%>   
      
          //EFLORES CDM+CDP PRY-0817 Modifica este metodo para poder mostrar el checkbox chkMantenerSIM solo para venta u alquiler - vasp


          <%if( MiUtil.getString(hdnSpecification).equals("2065")){%>

          if (( form.txt_ItemOriginalSolution.value == "Smart Postpago" || form.txt_ItemOriginalSolution.value =="Smart Prepago" ) && (form.cmb_ItemSolution.value=="15" || form.cmb_ItemSolution.value=="16")) //vasp
          {
              var chkMSIMExists = parent.mainFrame.frmdatos.chkMantenerSIM.checked;
              if(chkMSIMExists !=  null){
                  fxShowDivByHeaderId(151); //Mostramos el id del chkMantenerSIM
              }
          }

          <%}%>

      //INICIO DERAZO TDECONV003-2
      fxHideDivByHeaderId(165);
      //FIN DERAZO

     }catch(e){}
  }
  
  function fxActionPropio(){
    try{
      
      //Hide Check Marcar SIM - asp
      fxHideDivByHeaderId(151);
      //Sim EAGLE
      fxShowDivByHeaderId(2);
      //Renta
      fxHideDivByHeaderId(20);
      //Precio Cta Inscripcón
      fxShowDivByHeaderId(18);
      //Precio Excepción.
      fxHideDivByHeaderId(19);
      //Cantidad
      //fxHideDivByHeaderId(21);
      //Fecha Programada de Devolución
      //fxHideDivByHeaderId(14);
           
      //INICIO MOSTRAR RESPONSABLE DE DEVOLUCION Y AREA RESPONSABLE EN PRESTAMO Y PROPIO
       //fxHideDivByHeaderId(71);
        fxShowDivByHeaderId(14);
        fxShowDivByHeaderId(71);
        fxShowDivByHeaderId(70);
      //FIN MOSTRAR RESPONSABLE DE DEVOLUCIÓN Y AREA RESPONSABLE EN PRESTAMO Y PROPIO
      //parent.mainFrame.frmdatos.cmb_ItemDevolution.disabled=false; // TMOGROVEJO 23/07/2008
       
      <%if( MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") || MiUtil.getString(hdnSpecification).equals("2069")
        || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%>
      fxShowDivByHeaderId(85);
      <%}%>
      
      //INICIO COMENTO CONDICION PARA MOSTRAR FECHA DESACTIVACION Y RESPONSABLE DE AREA
      <%/*if( MiUtil.getString(hdnSpecification).equals("2035") ||  MiUtil.getString(hdnSpecification).equals("2034")  ){*/%>
         //INICIO MUESTRA FECHA DE DESACTIVACION
         //fxHideDivByHeaderId(14);
         // fxShowDivByHeaderId(70);
         //FIN MUESTRA FECHA DE DESACTIVACION
      <%/*}*/%>
      //FIN COMENTO CONDICION PARA MOSTRAR FECHA DESACTIVACION Y RESPONSABLE DE AREA

      <%if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE ){%>
              fxShowDivByHeaderId(58);
      <%}%>       

      //INICIO DERAZO TDECONV003-2
      <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA){%>
          try{
            if(parent.opener.frmdatos.chkFlagMigration != null && parent.opener.frmdatos.chkFlagMigration.checked) {
                fxShowDivByHeaderId(165);
            }
          }catch(e){}
      <%}%>
      //FIN DERAZO
    }catch(e){}
  }
  
  function fxActionAlquilerCliente(){
    try{
      
        //Hide Check Marcar SIM - asp
      fxHideDivByHeaderId(151);

      //Sim EAGLE
      fxShowDivByHeaderId(2);
      //Renta
      fxHideDivByHeaderId(20);
      //Precio Cta Inscripcón
      fxShowDivByHeaderId(18);
      //Precio Excepción.
      fxHideDivByHeaderId(19);
      //Cantidad
      //fxHideDivByHeaderId(21);
      //Fecha Programada de Devolución
    //  fxHideDivByHeaderId(14);
      //Responsable de Devolución
      fxHideDivByHeaderId(71);
      
      //parent.mainFrame.frmdatos.cmb_ItemDevolution.disabled=false; // TMOGROVEJO 23/07/2008
       
      <%if(MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") || MiUtil.getString(hdnSpecification).equals("2069")
      || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){ //Se agrega reposicion prepago tde - TDECONV034 %>
      fxShowDivByHeaderId(85);
      <%}%>
      
      <%if( MiUtil.getString(hdnSpecification).equals("2035") ||  MiUtil.getString(hdnSpecification).equals("2034")  ){%>
         fxHideDivByHeaderId(14);
         fxShowDivByHeaderId(70);
      <%}%>

      <%if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO || 
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE ){ //Se agrega reposicion prepago tde - TDECONV034 %>

              fxShowDivByHeaderId(58);
      <%}%>   
      
        //INICIO DERAZO TDECONV003-2
      fxHideDivByHeaderId(165);
      //FIN DERAZO
    }catch(e){}
  
  }
  
  function fxActionPrestamo(){
    try{
      //Hide Check Marcar SIM - asp
      fxHideDivByHeaderId(151);
      //Sim EAGLE
      fxHideDivByHeaderId(2);
      //Precio Cta Inscripcón
      fxShowDivByHeaderId(18);
      //Precio Excepción.
      fxHideDivByHeaderId(19);
      //Cantidad
      fxShowDivByHeaderId(21);
      //Renta
      fxHideDivByHeaderId(20);
      //Fecha Programada de Devolución
      fxShowDivByHeaderId(14);
      //Responsable de Devolución
      fxShowDivByHeaderId(71);
      //INICIO MUESTRA AREA RESPONSABLE PARA PRESTAMO Y PROPIO
      fxShowDivByHeaderId(70);
      //FIN MUESTRA AREA RESPONSABLE PARA PRESTAMO Y PROPIO
      
      <%if(MiUtil.getString(hdnSpecification).equals((String.valueOf(Constante.SPEC_PREPAGO_TDE))) || MiUtil.getString(hdnSpecification).equals("2002") || MiUtil.getString(hdnSpecification).equals("2069")
        || MiUtil.getString(hdnSpecification).equals(String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)) ){%> 
        
      fxHideDivByHeaderId(85);
      <%}%>
      
      //INICIO COMENTO CONDICION PARA MOSTRAR RESPONSABLE DE AREA
      <%/*if( MiUtil.getString(hdnSpecification).equals("2035") || MiUtil.getString(hdnSpecification).equals("2034")){*/%>
      <%/*if( MiUtil.getString(hdnSpecification).equals("2035") ){*/%> 
       //FIN MUESTRA AREA RESPONSABLE PARA PRESTAMO Y PROPIO
       //  fxShowDivByHeaderId(70);
      <%/*}*/%>
      //FIN COMENTO CONDICION PARA MOSTRAR RESPONSABLE DE AREA
      
       <%if( MiUtil.getString(hdnSpecification).equals("2037") ){%>
         fxShowDivByHeaderId(19);
      <%}%>

      <%if( MiUtil.parseInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO  || 
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_POSTPAGO_VENTA ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_NUEVA  ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_PREPAGO_TDE    ||
            MiUtil.parseInt(hdnSpecification) == Constante.SPEC_REPOSICION_PREPAGO_TDE){%>
              fxHideDivByHeaderId(58);
      <%}%>        
      
      //INICIO DERAZO TDECONV003-2
      fxHideDivByHeaderId(165);
      //FIN DERAZO

    }catch(e){}
  }
  
  function fxActionAsignacion(){
    /*try{
      //Sim EAGLE
      idDisplay2.style.display = '';
      //Renta
      idDisplay20.style.display = 'none';
      //Precio Cta Inscripcón
      idDisplay18.style.display = 'none';
      //Precio Excepción.
      idDisplay19.style.display = 'none';
      //Cantidad
      idDisplay21.style.display = 'none';
    }catch(e){}*/
  }
  
  /**
  Función que oculta el campo dado el
  HeaderId.
  */
  function fxHideDivByHeaderId(headerId){
    try{
      eval("idDisplay"+headerId+".style.display = 'none';");
    }catch(e){}
  }
  
  /**
  Función que muestra el campo dado el
  HeaderId.
  */
  function fxShowDivByHeaderId(headerId){
    try{
      eval("idDisplay"+headerId+".style.display = '';");
    }catch(e){}
  }
  
  /**
  Función que establece el valor de blanco
  en el nombre del objecto que se indica.
  */
  function formReset(){
    fxClearObjects("txt_ItemSIM_Eagle","TEXT");
    fxClearObjects("cmb_ItemProductLine","SELECT");
    fxClearObjects("cmb_ItemProducto","SELECT");    
    <%if (MiUtil.parseInt(hdnSpecification)!=Constante.SPEC_TRANSFERENCIA){%>    
    fxClearObjects("cmb_ItemPlanTarifario","SELECT");
    <%}%>
    fxClearObjects("cmb_ItemPromocion","SELECT");
    fxClearObjects("txt_ItemMoneda","SELECT");
    fxClearObjects("txt_ItemPromotionId","SELECT");
    fxClearObjects("txt_ItemPriceCtaInscrip","SELECT");
    fxClearObjects("txt_ItemPriceException","SELECT");
    fxClearObjects("txt_ItemEquipmentRent","SELECT");
    fxClearObjects("txt_ItemNewNumber","SELECT");
    fxClearObjects("txtItemImeiSim","TEXT");
    try{
      parent.mainFrame.idValidateImeSim.innerHTML = '';
    }catch(e){}

    //DERAZO TDECONV003-2 Limpiamos el campo al seleccionar la modalidad
    fxClearObjects("txt_ItemImeiFS","TEXT");
  }
  
  function fxClearObjects(objObject,typeObject){
    form = parent.mainFrame.frmdatos;
    try{
      if( typeObject == "TEXT" )
         eval("form."+objObject+".value=''");
      else
         eval("form."+objObject+".value=''");
    }catch(e){
     
    }
  
  }
  
</script>

<%
if( !type_window.equals("NEW") ){
  String[] paramNpobjitemvalue      = request.getParameterValues("b");
  String[] paramNpobjitemheaderid   = request.getParameterValues("a");
  int      indexHeader              = -1;
  for(int i=0;i<paramNpobjitemheaderid.length; i++)
    if( paramNpobjitemheaderid[i].equals("1") ) {
        indexHeader = i;
        break;
    }
    
  if( indexHeader != -1 ){%>
    <script>
      function fxLoadEditModalidad(){
         fxChangeObjectModality("<%=paramNpobjitemvalue[indexHeader]%>");
      }
    </script>
  <%}else{%>
     <script>
      function fxLoadEditModalidad(){}
    </script>
  <%}
 }
%>