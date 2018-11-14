<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.RepairService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.bean.EquipmentDamage"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%	  
        //Inicio TIENDA EXPRESS - Se requiere identificar la tienda del usuario logeado , asi como si es una TE   
        //18/06/2014 JRAMIREZ        
        int iIsTiendaExpress=0;     
        iIsTiendaExpress= Integer.valueOf(request.getParameter("iIsTiendaExpress")).intValue();         
        //Fin TIENDA EXPRESS
        
	RepairService objRepairService = new RepairService();
        HashMap hshDataMap1 = new HashMap();
        List<String> listAccesorry1=new ArrayList<String>(); 
        List<String> resultlistFails=new ArrayList<String>();
	String lRepairId = StringUtils.defaultString(request.getParameter("hdnRepairId"),"0");
	HashMap hshRepairReportMap = objRepairService.getDataLoanForReport(lRepairId);
        listAccesorry1=(List<String>)hshRepairReportMap.get("resultlistAccessory1");
        resultlistFails=(List<String>)hshRepairReportMap.get("resultlistFails");
	
	System.out.println("listAccesorry1.size(): "+listAccesorry1.size());
	String repairList_defectuoso=""+hshRepairReportMap.get("WN_NP_REPLISTID_1");
	String model_defectuoso=""+hshRepairReportMap.get("WV_MODEL_DEFECTUOSO");
	hshDataMap1 = objRepairService.getDamageList(repairList_defectuoso, model_defectuoso);
	List<EquipmentDamage> repairDamageList = (List<EquipmentDamage>)hshDataMap1.get("resultlist");
	String imagen1=""+hshDataMap1.get("imagen_equipo");
	String si_no_reemplazo= (hshRepairReportMap.get("WV_IMEI_REEMPLAZO")==null || 
	hshRepairReportMap.get("WV_IMEI_REEMPLAZO").toString().equals("")?"NO":"SI");
	
	Gson gson = new Gson();
	String repairDamageListBeansJson = gson.toJson(repairDamageList);
    //EPENA 23/07/2015
    GeneralService objGeneralService   = new GeneralService();
    ArrayList arrCondition = objGeneralService.getConditionsReport("REPORT_REPAIR_SIN_PRESTAMO");
    int tam=arrCondition.size();
%>
<html>
<head>
<title>Detalle de estado físico del equipo</title>
<link rel="stylesheet" href="../websales/Resource/css/salesweb.css"></link>
<link rel="stylesheet" href="../websales/Resource/css/damagesDialog.css"></link>

<script type="text/javascript" src="../websales/Resource/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="../websales/Resource/js/damagesDialog.js"></script>
<script type="text/javascript">
var damageTypes = [
	new Item("R", "Rayado"), 
	new Item("D", "Despintado"), 
	new Item("Q", "Quiñe"), 
	new Item("T", "Roto"), 
	new Item("S", "Sensor Líquido"), 
	new Item("J", "Rajado"), 
	new Item("L", "Derrame de LCD")
];
    var damages = new Array();
    var PATH="../websales/Resource/images/equipments/";
    var jsonObject1=<%=repairDamageListBeansJson%>;
    var image1 = PATH +'<%=imagen1%>';
    var codigo='<%=hshRepairReportMap.get("WV_REPAIRID_BOF")%>,<%=hshRepairReportMap.get("WV_IMEI_DEFECTUOSO")%>';
    var controllerRepair="<%=request.getContextPath()%>/repairServlet";
	
	$(document).ready(function() {
    	 getCodeBarsFromServer(controllerRepair);
         try{
            fillDamages(jsonObject1,damages);
         }catch(err){
             //console.debug(err);
         }
     });
</script>
</head>
<body onload="init();">
<table cellspacing="0" cellpadding="0" style="border-top: 1px solid black; border-right: 1px solid black; border-left: 1px solid black; " width="850">
	<tr>
		<td width="202"></td>
		<td width="200"></td>
		<td width="202"></td>
		<td width="200"></td>
	</tr>
	<tr>
		<td rowspan="3" colspan="2"> 
			<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/logo_nextel.gif" width="199" height="47" border="0">
		</td>
		<td align="left"> 
			<font size="2">TIENDA: &nbsp;<%=hshRepairReportMap.get("WV_RECEPCION")%></font> 
		</td>
		<td align="left">
			<font size="2">ORDEN DE SERVICIO: <%=hshRepairReportMap.get("WV_ORDERID")%></font>
		</td>
	</tr>
	<tr>
		<td align="left"><font size="2">ASESOR <% if(iIsTiendaExpress==0) {%> ENTEL<%}%> : <%=hshRepairReportMap.get("WV_CREATEDBY")%></font></td>
		<td align="left">
			<font size="2"> PROCESO: <%=hshRepairReportMap.get("WV_NPREPPROCESS")%></font>
		</td>
	</tr>
           
	<tr>
		<td align="left">
			<font size="2">FECHA DE RECEPCI&Oacute;N: <%=hshRepairReportMap.get("WV_NPCREATEDDATE")%></font>
		</td>
		<td align="left">
			<font size="2">HORA DE RECEPCI&Oacute;N: <%=hshRepairReportMap.get("WV_NPCREATEDHOUR")%></font>
		</td>
	</tr>
        
            <% if(iIsTiendaExpress==1) {%>  
            <tr>
                 <td align="left" colspan="2">	
                  <font size="2" style="font-weight:bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   TIENDA EXPRESS</font> <BR>
                  </td>
                  <td align="center" colspan="2">	
                            <table border="2" width="400" height="70" ><tr><td></td></tr> </table><BR>
                  </td>
            </tr>
          <%}%>
        

	<tr>
		<td align="center" colspan="2">			
			<div id="div_barcode1" style="width: 300px; height: 80px; padding: 1px 1px 1px 1px;text-align: center;"></div>                        
		</td>
                <td align="center" colspan="2">
			<div id="div_barcode2" style="width: 300px; height: 80px; padding: 1px 1px 1px 1px;text-align: center;"></div>                        
		</td>
	</tr>
</table>
<table cellspacing="0" cellpadding="0" style="border-bottom: 1px solid black; border-right: 1px solid black; border-left: 1px solid black;" width="850">
	<tr>
                <td align="left" colspan="5">
                        <table width="100%">
                                <tr>
                                        <td colspan="5" align="left" bgcolor="#ED750C" style="border: 1px solid black">
                                                <font size="2">DESCRIPCI&Oacute;N DEL CLIENTE</font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="4">
                                                <font size="2">RAZ&Oacute;N SOCIAL: <%=hshRepairReportMap.get("WV_NAMECOM")%></font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2">
			<font size="2">NOMBRE CONTACTO: <%=hshRepairReportMap.get("WV_CONTACTNAME")%></font>
		</td>
		<td colspan="2" width="50%">
			<font size="2">EMAIL CONTACTO: <%=hshRepairReportMap.get("WV_CONTACTEMAIL")%></font>
		</td>
	</tr>
                        </table>
                </td>
        </tr>
	<tr>
                <td align="left" colspan="5">
                        <table width="100%">
                                <tr>
                                        <td colspan="5" align="left" bgcolor="#ED750C" style="border: 1px solid black"><font size="2">DATOS DEL TERMINAL</font></td>
	</tr>
	<tr>
		<td width="35%" align="left">
                                                <font size="2">N° TELEF&Oacute;NICO: <%=hshRepairReportMap.get("WV_PHONE")%></font>
		</td>
		<td width="5%">&nbsp;</td>
		<td height="100%" width="30%" colspan="">
			<font size="2">N° SIM CARD: <%=hshRepairReportMap.get("WV_SIM")%></font>
		</td>
		<td height="100%" width="30%" colspan="">
                                                <font size="2">GARANT&Iacute;A DE FABRICA: <%=hshRepairReportMap.get("WV_WARRANTY_FACTORY")%></font>
		</td>
	
	</tr>
	<tr>
		<td width="35%" align="left">
			<font size="2">FABRICANTE: <%=hshRepairReportMap.get("WV_FABRICATOR")%></font>
		</td>
		<td width="5%">&nbsp;</td>
		<td height="100%" width="30%" colspan="">
			<font size="2">MODELO: <%=hshRepairReportMap.get("WV_MODEL_DEFECTUOSO")%></font>
		</td>
		<td height="100%" width="30%" colspan="">
			<font size="2">IMEI: <%=hshRepairReportMap.get("WV_IMEI_DEFECTUOSO")%></font>
		</td>	
	</tr>
                        </table>
                </td>
        </tr>

	<tr>
		<td align="center" colspan="5">
			<table width="100%">
				<tr>
					<td width="35%">
						<table width="100%" height="100%">
							<tr>
								<td  height="100%">
									<table width="100%" cellspacing="0" cellpadding="0"
										style="border: 1px solid black;" height="100%">
										<tr>
											<td bgcolor="#ED750C" align="center" colspan="2"
												style="border: 1px solid black;"><font size="2">
													ACCESORIOS </font></td>
										</tr>
										<%	
												if(listAccesorry1!=null && listAccesorry1.size()>0){
												int i=0;
												String altura="";
														for(String str:listAccesorry1){
																i++;
																%>
																<tr>
																		<td><font size="2"><%=str%></font></td>
																</tr>
																<%
																if(i==listAccesorry1.size()){				
																		%>
																		<tr>
																				<td height="100%"><font size="2">&nbsp;</font></td>
																		</tr>
																		<%
																}
														}
												}else{
														%>
														<tr>
																<td height="100%"><font size="2">&nbsp;</font></td>
														</tr>
														<%
												}
										%>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td width="5%">&nbsp;</td>
					<td width="60%" height="100%" colspan="2">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;" height="100%">
							<tr>
								<td bgcolor="#ED750C" align="center"><font size="2">DETALLE F&Iacute;SICO
										DEL EQUIPO</font></td>
							</tr>
							<tr>
								<td height="100%" align="center" >
                                                                <table align="center" class="RegionBorder" style="border-collapse: collapse" cellspacing="1" cellpadding="1" width="365">
                                                                    <tr>
                                                                            <td align="center" class="CellLabel" style="width: 94px">Frontal</td>				
                                                                            <td align="center" class="CellLabel" style="width: 94px">Posterior</td>				
                                                                            <td align="center" class="CellLabel" style="width: 42px">Izq.</td>				
                                                                            <td align="center" class="CellLabel" style="width: 42px">Der.</td>				
                                                                            <td align="center" class="CellLabel" style="width: 42px">Sup/Inf</td>				
                                                                            <td align="center" class="CellLabel">Detalle Daños</td>							
                                                                    </tr>
                                                                    <tr>
                                                                            <td colspan="5"><div id="divImage"></div></td>
                                                                            <td class="CellContent" width="27%"><div id="divCaption"></div></td>
                                                                    </tr>
                                                                </table>
                                                                </td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td colspan="5">
			<table width="100%">
				<tr>
					<td width="70%" valign="top">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;">
							<tr>
								<td bgcolor="#ED750C" align="center"><font size="2"> REPORTE DEL CLIENTE </font></td>
							</tr>
							<tr>
								<td><font size="2"><%=hshRepairReportMap.get("WV_DESCNEXTEL")%></font></td>
							</tr>
						</table>
					</td>
					<td width="30">&nbsp;</td>
					<td width="30%" valign="top">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;">
							<tr>
								<td bgcolor="#ED750C" height="100%" align="center"><font size="2">
										FALLAS DEL TERMINAL </font></td>
							</tr>
							<%	
                                                                if(resultlistFails!=null && resultlistFails.size()>0){
                                                                int i=0;
                                                                String altura="";
                                                                        for(String str:resultlistFails){
                                                                                i++;
                                                                                %>
                                                                                <tr>
                                                                                        <td><font size="2"><%=str%></font></td>
                                                                                </tr>
                                                                                <%
                                                                                }
                                                                }else{
                                                                        %>
                                                                        <tr>
                                                                                <td height="100%"><font size="2">&nbsp;</font></td>
                                                                        </tr>
                                                                        <%
                                                                }
                                                        %>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td colspan="5">
			<table width="100%">
				<tr>
					<td width="50%" valign="top">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;">
							<tr>
								<td bgcolor="#ED750C" align="center"><font size="2"> DIAGN&Oacute;STICO DE REPARACI&Oacute;N </font></td>
							</tr>
							<tr>
								<td><font size="2"><%=hshRepairReportMap.get("WV_ORDERDETAILDIAG")%></font></td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;">
							<tr>
								<td bgcolor="#ED750C" height="100%" align="center"><font size="2"> SOLUCI&Oacute;N </font></td>
							</tr>
							<tr>
								<td><font size="2"><%=hshRepairReportMap.get("WV_RCRESOLUTIONDET")%></font></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td align="center" colspan="5">
			<table width="100%">
				<tr>
					<td width="30%" colspan="2">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;" height="100%">
							<tr>
								<td bgcolor="#ED750C" align="left" colspan="2"
									style="border: 1px solid black;"><font size="2">
										REPARACI&Oacute;N EN GARANT&Iacute;A </font></td>
							</tr>
							<tr>
								<td colspan="2" height="100%">
								<font size="2">&nbsp;<%=hshRepairReportMap.get("WV_GARANTIA_REPARACION")%> </font></td>
							</tr>
						</table>
					</td>
					<td width="5%">&nbsp;</td>
					<td width="65%" height="100%">
						<table width="100%" cellspacing="0" cellpadding="0"
							style="border: 1px solid black;" height="100%">
							<tr>
								<td bgcolor="#ED750C" align="left" colspan="2"
									style="border: 1px solid black;"><font size="2">
										REEMPLAZO: <%=si_no_reemplazo%> </font></td>
							</tr>
							<tr>
								<td colspan="2" height="100%" align="center">
									<table width="100%"><tr>
									<td width="40%" align="left" ><font size="2">FAB: <%=hshRepairReportMap.get("WV_FAB_REEMPLAZO")%></font></td>
									<td width="30%" align="left"><font size="2">MODELO: <%=hshRepairReportMap.get("WV_MODEL_REEMPLAZO")%></font></td>
									<td width="30%" align="left"><font size="2">IMEI: <%=hshRepairReportMap.get("WV_IMEI_REEMPLAZO")%></font></td>
									</tr></table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td align="left" colspan="5">
			<table width="100%">
				<tr>
					<td bgcolor="#ED750C" colspan="2" cellspacing="0" cellpadding="0" style="border: 1px solid black;"><font size="2">
							CONFORMIDAD DEL SERVICIO </font></td>
				</tr>
				<tr>
					<td><font size="2">FIRMA: </font></td>
					<td><font size="2">ASESOR <% if(iIsTiendaExpress==0) {%> ENTEL<%}%> : <%=hshRepairReportMap.get("WV_CREATEDBY")%></font></td>
				</tr>
				<tr>
					<td><font size="2">NOMBRE Y APELLIDO: </font></td>
					<td><font size="2">FECHA DE ENTREGA: <%=hshRepairReportMap.get("WV_FECHA_ENTREGA")%></font></td>
				</tr>
				<tr>
					<td><font size="2">DNI: </font></td>
					<td><font size="2">HORA DE ENTREGA: <%=hshRepairReportMap.get("WV_HORA_ENTREGA")%></font></td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td align="left" colspan="5">
			<table width="100%">
				<tr>
					<td bgcolor="#ED750C" colspan="2" cellspacing="0" cellpadding="0" style="border: 1px solid black;"><font size="2">
							CONDICIONES </font></td>
				</tr>
				<tr>
					<td width="50%" cellspacing="0" cellpadding="0" style="border: 1px solid black; text-align:justify;"><font size="2">
                        <%int nroLinea=1, cont=0;
                            HashMap h;
                            String description,nptag1;
                            while(cont<tam){
                                h = (HashMap)arrCondition.get(cont);
                                description= (String)h.get("description");
                                nptag1= (String)h.get("nptag1");

                                if (description!= null && nptag1!= null){
                                    if (nptag1.equals("IZQ")){
                                        out.println(nroLinea + ".- "+description+".");%><br/><%
                                    nroLinea++;
                                }
                            }
                            cont++;
                        }
                        cont=0;
                    %>
                    </font></td>
					<td width="50%" cellspacing="0" cellpadding="0" style="border: 1px solid black; text-align:justify;"><font size="2">
                        <% cont=0;

                            while(cont<tam){
                                h = (HashMap)arrCondition.get(cont);
                                description= (String)h.get("description");
                                nptag1= (String)h.get("nptag1");

                                if (description!= null && nptag1!= null){
                                    if (nptag1.equals("DER")){
                                        out.println(nroLinea + ".- "+description+".");%><br/><%
                                    nroLinea++;
                                }
                            }
                            cont++;
                        }
                        cont=0;
                    %>
                    </font></td>
				</tr>
			</table>
		</td>
	</tr>

	<!-- SAR_0034-187448 FIN EALONSO-->
</table>
</body>
</html>