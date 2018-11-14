<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.bean.RegionBean" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>

<%

    System.out.println("*********************************************objItemDistrict.jsp*********************************************");
	String nameHtmlItem                     = (String)request.getParameter("nameObjectHtml");
    List<RegionBean> listaRegiones          = null;
    Hashtable hshtinputNewSection           = null;
    String hdnSpecification                 = ""
            //INICIO: AMENDEZ | PRY-1049
            ,strHdnCobertura=""
            //FIN: AMENDEZ | PRY-1049
            ;

    hshtinputNewSection  = (Hashtable)request.getAttribute("hshtInputNewSection");
    if (hshtinputNewSection!= null){
        hdnSpecification = (String)hshtinputNewSection.get("hdnSpecification");
        //INICIO: AMENDEZ | PRY-1049
        strHdnCobertura         =   (String)hshtinputNewSection.get("strHdnCobertura");
        System.out.println("strHdnCobertura  : "+strHdnCobertura);
        System.out.println("hdnSpecification : "+hdnSpecification);
        //FIN: AMENDEZ | PRY-1049
    }

    String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));

    GeneralService objGeneralService = new GeneralService();
%>

<script language="javascript">
    var vctDistrictItem = new Vector();

    function fxMakeDistrictItem(objDistrictItemid, objDistrictItemName){
        this.objDistrictItemid         = objDistrictItemid;
        this.objDistrictItemName       = objDistrictItemName;
    }

    function fxChangeDistrictEdit(objValue){
        var form = parent.mainFrame.frmdatos;
        try{
            form.cmb_NpDistrictZone.value = objValue;
        }catch(e){}
    }
</script>

<select name="<%=nameHtmlItem%>" >
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
</select>

<%
    if( !type_window.equals("NEW") ){
        String strProductId    = "";
        String strProvinceId     = "";
        String strDistrictId = "";

        String[] paramNpobjitemvalue      = request.getParameterValues("b");
        String[] paramNpobjitemheaderid   = request.getParameterValues("a");

        //INICIO: AMENDEZ | PRY-1049
        String strModality="";
        String strSolution     = "";
        String strProductLine     = "";
        //FIN: AMENDEZ | PRY-1049
        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            //INICIO: AMENDEZ | PRY-1049
            if( paramNpobjitemheaderid[i].equals("1") ) {
                strModality = paramNpobjitemvalue[i];
            }

            if( paramNpobjitemheaderid[i].equals("51") ) {
                strProductLine = paramNpobjitemvalue[i];
            }

            if( paramNpobjitemheaderid[i].equals("93") ) {
                strSolution = MiUtil.getString(paramNpobjitemvalue[i]);
            }
            //FIN: AMENDEZ | PRY-1049

            //Product
            if(paramNpobjitemheaderid[i].equals("9"))
                strProductId = paramNpobjitemvalue[i];

            //Region
            if(paramNpobjitemheaderid[i].equals(Constante.CONTROL_ITEM_PROVINCE_ID))//150
                strProvinceId = paramNpobjitemvalue[i];

            //Province
            if(paramNpobjitemheaderid[i].equals(Constante.CONTROL_ITEM_DISTRICT_ID))
                strDistrictId = paramNpobjitemvalue[i];
        }
%>

<script>
    function fxLoadEditDistrict(){
        <%
        //INICIO: AMENDEZ | PRY-1049
        NewOrderService objNewOrderService= new NewOrderService();
        int bafievaluation=objNewOrderService.validateConfigBafi2300(strModality,strSolution,strProductLine);
        int flagevaluation=0;

        if((strHdnCobertura.equals("0") || strHdnCobertura.equals("1")) && bafievaluation==1){
            flagevaluation=1;
        } else{
            flagevaluation=-1;
        }
        System.out.println("flagevaluation  : "+flagevaluation);
        if(flagevaluation == -1){
          System.out.println("Evaluacion para mostrar regiones ");
        //FIN: AMENDEZ | PRY-1049
        %>

        formCurrent = parent.mainFrame.frmdatos;
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_NpDistrictZone);
        parent.mainFrame.vctDistrictItem.removeElementAll();
        <%
          HashMap objHashMap = objGeneralService.getListDistrictBAFI(strProductId,strProvinceId);
          RegionBean objDistrictBean;

          int result = 0;

          if(objHashMap == null){
            throw new Exception("Surgieron errores al cargar las provincias");
          }
          else{
            if((String)objHashMap.get("strMessage")!= null){
              throw new Exception(MiUtil.getMessageClean((String)objHashMap.get("strMessage")));
            }
            else{
              result = ((Integer)objHashMap.get("result")).intValue();
            }
          }

          if(result > 0){
        %>
            fxShowDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
        <%
            listaRegiones = (ArrayList<RegionBean>)objHashMap.get("listaRegiones");
            if (listaRegiones != null && listaRegiones.size() > 0){
                for(int i=0; i<listaRegiones.size();i++){
                    objDistrictBean = (RegionBean)listaRegiones.get(i);
        %>
        parent.mainFrame.vctDistrictItem.addElement(new parent.mainFrame.fxMakeDistrictItem('<%=objDistrictBean.getRegionId()%>','<%=MiUtil.getMessageClean(objDistrictBean.getRegionName())%>'));
        parent.mainFrame.AddNewOption(formCurrent.cmb_NpDistrictZone,'<%=objDistrictBean.getRegionId()%>','<%=MiUtil.getMessageClean(objDistrictBean.getRegionName())%>');
        <%
                }
                if(!strDistrictId.equals("")){
        %>
                fxChangeDistrictEdit(<%=strDistrictId%>);
        <%      }
            }else{ %>
                formCurrent.cmb_NpDistrictZone.value = "";
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
            <% }
          }
          else{
        %>
        formCurrent.cmb_NpDistrictZone.value = "";
        parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_DISTRICT_ID%>.style.display = 'none';
        <%
          }

       }
        %>
    }

</script>
<%  }%>