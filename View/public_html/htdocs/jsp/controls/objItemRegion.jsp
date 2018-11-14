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

    System.out.println("*********************************************objItemRegion.jsp*********************************************");
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
    System.out.println("******************************************************************************************");
    GeneralService objGeneralService = new GeneralService();
%>

<script language="javascript">
    var vctRegionItem = new Vector();

    function fxMakeRegionItem(objRegionItemid, objRegionItemName){
        this.objRegionItemid         = objRegionItemid;
        this.objRegionItemName       = objRegionItemName;
    }

    function fxChangeRegionEdit(objValue,option){
        var form = parent.mainFrame.frmdatos;
        try{
            form.cmb_ItemRegion.value = objValue;
            var productId = form.cmb_ItemProducto.value;
            if(option){
                fxGetEnabledProvinces(productId,objValue);
            }

        }catch(e){}
    }

    function fxGetEnabledProvinces(productId,regionId){
        var url_server = "<%=request.getContextPath()%>/itemServlet";
        var params = "hdnMethod=getEnabledProvinces&strProductId="+productId+"&strRegionId="+regionId;
        jQuery.ajax({
            url: url_server,
            data: params,
            type: "POST",
            dataType: 'json',
            success:function(data) {
                var formCurrentRegion = parent.mainFrame.frmdatos;

                if(!(typeof data.strMessage === "undefined")){
                    alert(data.strMessage);
                    formCurrentRegion.cmb_NpProvinceZone.value = "";
                    formCurrentRegion.cmb_NpDistrictZone.value = "";
                    parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_PROVINCE_ID%>.style.display = "none";
                    fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                }
                else{
                    var result = data.result;
                    if(result > 0){
                        parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_PROVINCE_ID%>.style.display = 'block';
                        fxShowDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                        parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_NpProvinceZone);
                        parent.mainFrame.DeleteSelectOptions(formCurrentRegion.cmb_NpDistrictZone);
                        var arrayRegions = data.listaRegiones;
                        if(arrayRegions != null){
                            for(i=0; i<arrayRegions.length; i++) {
                                parent.mainFrame.AddNewOption(formCurrentRegion.cmb_NpProvinceZone,arrayRegions[i].regionId,arrayRegions[i].regionName);
                            }
                        }
                        if(arrayRegions.length == 0){
                            formCurrentRegion.cmb_NpProvinceZone.value = "";
                            formCurrentRegion.cmb_NpDistrictZone.value = "";
                            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
                            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                        }
                    }
                    else{
                        formCurrentRegion.cmb_NpProvinceZone.value = "";
                        formCurrentRegion.cmb_NpDistrictZone.value = "";
                        parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_PROVINCE_ID%>.style.display = "none";
                        fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
                    }
                }
            },
            error:function(xhr, status, error) {
                var formCurrentRegion = parent.mainFrame.frmdatos;
                alert("Error interno al validar provincias: "+xhr.responseText);
                formCurrentRegion.cmb_NpProvinceZone.value = "";
                formCurrentRegion.cmb_NpDistrictZone.value = "";
                parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_PROVINCE_ID%>.style.display = "none";
                fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
            }
        });
    }
</script>

<select name="<%=nameHtmlItem%>" onChange="javascript:fxChangeRegionEdit(this.value,true);">
    <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
</select>

<%
    if( !type_window.equals("NEW") ){
        String strProductId    = "";
        String strRegionid     = "";

        String[] paramNpobjitemvalue      = request.getParameterValues("b");
        String[] paramNpobjitemheaderid   = request.getParameterValues("a");

        //INICIO: AMENDEZ | PRY-1049
        String strModality="";
        String strSolution     = "";
        String strProductLine     = "";
        //FIN: AMENDEZ | PRY-1049
        for(int i=0;i<paramNpobjitemheaderid.length; i++){
            //Product
            if(paramNpobjitemheaderid[i].equals("9"))
                strProductId = paramNpobjitemvalue[i];

            //Region
            if(paramNpobjitemheaderid[i].equals(Constante.CONTROL_ITEM_REGION_ID))//150
                strRegionid = paramNpobjitemvalue[i];

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
        }
%>

<script>
    function fxLoadEditRegion(){
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
        parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemRegion);
        parent.mainFrame.vctRegionItem.removeElementAll();
        <%
          HashMap objHashMap = objGeneralService.getEnabledRegions(strProductId);
          RegionBean objRegionBean;

          int result = 0;

          if(objHashMap == null){
            throw new Exception("Surgieron errores al cargar las regiones");
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
            parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = 'block';
        <%
            listaRegiones = (ArrayList<RegionBean>)objHashMap.get("listaRegiones");
            if (listaRegiones != null && listaRegiones.size() > 0){
                for(int i=0; i<listaRegiones.size();i++){
                    objRegionBean = (RegionBean)listaRegiones.get(i);
        %>
                    parent.mainFrame.vctRegionItem.addElement(new parent.mainFrame.fxMakeRegionItem('<%=objRegionBean.getRegionId()%>','<%=MiUtil.getMessageClean(objRegionBean.getRegionName())%>'));
                    parent.mainFrame.AddNewOption(formCurrent.cmb_ItemRegion,'<%=objRegionBean.getRegionId()%>','<%=MiUtil.getMessageClean(objRegionBean.getRegionName())%>');
        <%
                }
                if(!strRegionid.equals("")){
        %>
                  fxChangeRegionEdit(<%=strRegionid%>,false);
        <%      }
            }
          }
          else{
        %>
            formCurrent.cmb_ItemRegion.value = "";
            formCurrent.cmb_NpProvinceZone.value = "";
            formCurrent.cmb_NpDistrictZone.value = "";
            parent.mainFrame.idDisplay<%=Constante.CONTROL_ITEM_REGION_ID%>.style.display = 'none';
            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_PROVINCE_ID%>);
            fxHideDivByHeaderId(<%=Constante.CONTROL_ITEM_DISTRICT_ID%>);
        <%
          }
            }
        %>

    }

</script>
<%  }%>
