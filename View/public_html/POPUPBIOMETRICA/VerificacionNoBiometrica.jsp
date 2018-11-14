<%@page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.service.BiometricaService" %>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<%@page import="com.sun.org.apache.xml.internal.security.utils.Base64" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    System.out.println("INICIO SESSION - Verificacion  NO Biometrica");
    try{
        String strRutaContext=request.getContextPath();
        Constante constante= new Constante();
        BiometricaService biometricaService= new BiometricaService();
        MiUtil miUtil= new MiUtil();
        PersonInfoBean  personInfoBean;
        QuizBean   quizBean= new QuizBean();

        String  order="";
        String  authorizer="";
        String  documento="";
        String  origen="";
        String  motivo="";
        String  authorizerPass="";
        String  idsesion="";
        long    verificationId = 0;
        String  source="";
        String strlogin="";

        if(request.getParameter("datos")==null){
            order =request.getParameter("idorder");
            authorizer=request.getParameter("authorizer");
            documento=request.getParameter("documento");
            origen =request.getParameter("origen");
            motivo= request.getParameter("motivo");
            authorizerPass=request.getParameter("authorizerPass");
            idsesion=request.getParameter("idsesion");
            verificationId = Long.parseLong(request.getParameter("verificationId"));
        }else{
            String datos=request.getParameter("datos");
            String[] ArrayDatos =datos.split("_");
            order=ArrayDatos[0];
            authorizer= ArrayDatos[1];
            documento= ArrayDatos[3];
            origen= ArrayDatos[4];
            motivo= ArrayDatos[2];
            authorizerPass=MiUtil.getString(ArrayDatos[5]);
            idsesion= ArrayDatos[6];
            verificationId=Long.parseLong(MiUtil.getString(ArrayDatos[7]));
            source= ArrayDatos[8];
            strlogin= ArrayDatos[9];
        }

        System.out.println("order: "+order+" authorizer: "+authorizer+" documento: "+documento+" origen: "+origen+" motivo: "+motivo+" authorizerPass: "
                +authorizerPass+" idsesion: "+idsesion+", strlogin: "+strlogin);
        Integer   codQuestionnario=0;
        Integer   codverificationId=0;
        Integer   minAceptado=0;
        Integer   intentos=0;
        int qpreguntas=0;
        String  fechaEmision="";//ricardo.quispe pry-0438

        HashMap hashMap = biometricaService.getVerificationCustomer(verificationId);
        personInfoBean =(PersonInfoBean) hashMap.get("personInfoBean");
        System.out.println("verificationId: "+verificationId);
        System.out.println("personInfoBean: "+personInfoBean);
        codverificationId=personInfoBean.getVerificationid().intValue();
        minAceptado=personInfoBean.getMinAccepted().intValue();
        intentos=personInfoBean.getAttempts().intValue();
        fechaEmision=personInfoBean.getFechaEmision();//ricardo.quispe pry-0438


        if(personInfoBean.getLstQuizBean()!=null && personInfoBean.getLstQuizBean().size()>0){
            quizBean=personInfoBean.getLstQuizBean().get(0);
            codQuestionnario=quizBean.getIdQuestionary().intValue();
            qpreguntas=quizBean.getLstQuestion().size();
        }


        System.out.println("codQuestionnario"+codQuestionnario+"codverificationId"+codverificationId+" idsesion: "+idsesion);
        System.out.println("QuizBean"+quizBean.getIdQuestionary());

%>

<html>
<head>

    <title>Verificaci&oacute;n  No Biom&eacute;trica </title>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <script type="text/javascript" >

        function verificarNoBiometrica(){
            var vForm = document.frmNoBiometrica;
            var countQuestion=<%=qpreguntas %>;
            var contador=0;
            if(countQuestion>0){
                for(var i=1;i<=countQuestion;i++){
                    var option='rbOption_'+i;
                    var txtoption=$('input:radio[name='+option+']:checked').val();
                    if(txtoption>0){contador=contador;}else{contador=contador+1;}
                }

                if(contador>0){alert("-Responder todas las preguntas!");}
                else{
                    if(vForm.hdSource.value=="CRMVIA"){
                        servletMethod="validdaNoBiometricaAislada";
                    }else{
                        servletMethod="validdaNoBiometrica";
                    }
                    vForm.target = "bottomFrame";
                    vForm.action="<%=strRutaContext%>/biometricaservlet?myaction="+servletMethod;
                    vForm.submit();
                }
            }
        }

        function cancelOrder(){
            var vForm = document.frmNoBiometrica;
            if(vForm.hdSource.value=="CRMVIA"){
                //alert("Cierre este frame haciendo cliente arriba a la derecha");
                parent.close();
            }else{
                vForm.target = "bottomFrame";
                vForm.action="<%=strRutaContext%>/biometricaservlet?myaction=cancelNoBiometrico";
                vForm.submit();
            }
        }

        function closePopUpAnular(){
            if(vForm.hdSource.value!="CRMVIA")parent.opener.parent.mainFrame.redirectOrder('<%=order%>');
            parent.close();
        }


    </script>
</head>
<body>

<form name="frmNoBiometrica" method="post"   >
    <table align="center" width="100%" border="0">
        <tr width="100%">
            <td>
                <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                    <tr class="PortletHeaderColor">
                        <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                        <td class="PortletHeaderColor" align="left" valign="top">
                            <font class="PortletHeaderText">
                                Verificaci&oacute;n No Biom&eacute;trica
                            </font></td>
                        <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr width="100%">
            <td width="70%">
                <table align="center" width="100%" border="0">
                    <tr><td>
                        <input type="hidden" name="hdorder" value="<%=order%>">
                        <input type="hidden" name="hdauthorizer" value="<%=authorizer%>">
                        <input type="hidden" name="hdauthorizerPass" value="<%=authorizerPass%>">
                        <input type="hidden" name="hdminAceptado" value="<%=minAceptado%>">
                        <input type="hidden" name="hdquestionario" value="<%=codQuestionnario%>">
                        <input type="hidden" name="hdverification" value="<%=codverificationId%>">
                        <input type="hidden" name="hdmotivo" value="<%=motivo%>">
                        <input type="hidden" name="hddocumento" value="<%=documento%>">
                        <input type="hidden" name="hdorigen" value="<%=origen%>">
                        <input type="hidden" name="hdqpreguntas" value="<%=qpreguntas%>">
                        <input type="hidden" name="hdintentos" value="<%=intentos%>">
                        <input type="hidden" name="hdUserId" value="<%=idsesion%>">
                        <input type="hidden" name="hdSource" value="<%=source%>">
                        <input type="hidden"  id="hdnSessionLogin"    name= "hdnSessionLogin"  value="<%=strlogin%>" />

                    </td></tr>

                    <tr>
                        <td class="CellLabel" align="left" valign="top">
                            DNI:
                        </td>
                        <td>
                            <%=documento%>
                        </td>
                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" valign="top">
                            Fecha Emisi&oacute;n DNI:
                        </td>
                        <td>
                            <%=fechaEmision%>
                        </td>
                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" valign="top">
                            Motivo:
                        </td>
                        <td>
                            <%=motivo%>
                        </td>
                    </tr>
                    <tr id="<%=quizBean.getIdQuestionary()%>"  class="CellLabel" align="left" valign="top" >
                        <td colspan="2">
                            <table  border="0" cellpadding="0" cellspacing="0" id="table<%=quizBean.getIdQuestionary()%>" >

                                <%
                                    List<QuestionBean> lstQuestionBean =new ArrayList<QuestionBean>();
                                    lstQuestionBean = quizBean.getLstQuestion();

                                    int qiteracion=0;
                                    for(QuestionBean questionBean : lstQuestionBean){
                                        qiteracion=qiteracion+1;

                                        String qIdquestion=""+ quizBean.getIdQuestionary()+""+ questionBean.getIdquestion();
                                %>
                                <tr style="padding-top:10px"><td><%=qiteracion%>. <%=questionBean.getQuestion()%>  </td></tr>
                                <tr><input  type="hidden"  name="hdSucces_<%=qiteracion%>" id="hdSucces<%=qiteracion%>" value="<%=questionBean.getIdoptionSuccess()%>" /></tr>&nbsp;

                                <%
                                    System.out.print("Lista de respuestas");
                                    List<OptionBean> lstOptionBean =new ArrayList<OptionBean>();
                                    lstOptionBean = questionBean.getLstOption();
                                    System.out.print("tamaño: "+lstOptionBean.size());
                                    for(OptionBean optionBean : lstOptionBean){
                                        System.out.print("opciones: "+optionBean.getIdoption());
                                        System.out.print("opciones: "+optionBean.getOption());
                                %>
                                <tr>
                                    <td>
                                        <input type="radio" name="rbOption_<%=qiteracion%>"  value="<%=optionBean.getIdoption()%>"/>
                                        <% System.out.print("htmlOption"+optionBean.getOption()); %>

                                        <%=optionBean.getOption()%>
                                    </td>
                                </tr>
                                <% }%>

                                <% } %>
                                <tr style="height: 25px"><td></td></tr>

                                <tr >
                                    <td><input type="button" value="Verificar"  onclick="verificarNoBiometrica();"></td>
                                    <td><input type="button" value="Cancelar" onclick="cancelOrder();"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="30%">
                <table>
                    <tr>
                        <% if (personInfoBean.getPhoto()!=null){
                            String foto= Base64.encode(personInfoBean.getPhoto());
                        %>
                        <td class="CellContent"  style="text-align: center;"><img alt="" src="data:image/jpeg;base64,<%=foto%>"   style="height: 280px"/></td>
                        <%}else{%>
                        <td class="CellContent"  style="text-align: center;">FOTO</td>
                        <%}%>
                    </tr>
                </table>
            </td>
        </tr>
        <tr width="100%">
            <td>
                <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                    <tr class="PortletHeaderColor">
                        <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                        <td class="PortletHeaderColor" align="left" valign="top">
                            <font class="PortletHeaderText">

                            </font></td>
                        <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>


</form>
</body>
<script type="text/javascript">
</script>
</html>
<% }catch(Exception ex){
    ex.printStackTrace();
%>

<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>

