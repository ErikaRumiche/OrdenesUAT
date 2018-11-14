<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%--@ page import="org.apache.commons.lang.ArrayUtils"--%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
        DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbCategoria);
		DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbSubCategoria);
        
    <%  ArrayList categoriaList = (ArrayList) request.getAttribute("cmbCategoryList");
        Iterator iterator = categoriaList.iterator();
        while(iterator.hasNext()) {
            DominioBean dominio = (DominioBean) iterator.next();
    %>
            fxAddNewOption(parent.mainFrame.document.frmdatos.cmbCategoria,'<%=dominio.getValor()%>','<%=dominio.getDescripcion()%>');
    <%  }
    %>
    
    function fxAddNewOption(TheCmb, Value, Description) {
        var option = new Option(Description, Value);
        var i = TheCmb.options.length;
        TheCmb.options[i] = option;
    }
    
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>
<%--
   -- PROCEDURE PL_DRAW_SALESOBJ_DEF_VALUES
   -- Purpose : Inserta en un formulario la dependencia: Zona - Coordinador - Supervisor - Consultor
   --           mostrando los valores por defecto que se pasa como parametros
   -- Developer            Date        Comment
   -- -----------------    ----------  ------------------------------------
   -- Fabio Alarcón        18/08/2003  Creación. Considera la nueva Entidad en Jerarquia de usuarios de ventas
   PROCEDURE PL_DRAW_SALESOBJ_DEF_VALUES (
      an_level             NUMBER,  -- "swlevel" -> valor de sesión (manipulable de acuerdo a requerimientos)
      av_code              VARCHAR2,-- "swcode" -> valor de sesión (manipulable de acuerdo a requerimientos)

      an_busunitid         NUMBER,  -- "swbusunitid" -> valor de sesión (manipulable de acuerdo a requerimientos)
      an_sellerid          NUMBER,  -- "ID de Vendedor" -> valor de sesión

      an_obj_zoneid        NUMBER,  -- "default ZoneID"        -> Si es 0 => no hay valor por defecto
      an_obj_coordinatorid NUMBER,  -- "default CoodinatorID"  -> Si es 0 => no hay valor por defecto
      an_obj_supervisorid  NUMBER,  -- "default SupervisorID"      -> Si es 0 => no hay valor por defecto
      an_obj_sellerid      NUMBER,  -- "default SellerID"      -> Si es 0 => no hay valor por defecto

      an_sellerid_flag     NUMBER,  -- "an_sellerid_flag" = 0 -> Valor de "an_sellerid" y "an_obj_sellerid" es "swpersonid"
                                    -- "an_sellerid_flag" = 1 -> Valor de "an_sellerid" y "an_obj_sellerid" es "swprovidergrpid"
                                    -- "wn_sellerid_flag" = -1 -> No considerar campo de Consultor
      av_frm_name          VARCHAR2 -- av_frm_name: Nombre de formulario
   )
   IS

      wn_zoneid         NUMBER;        -- ID de Zona ("swbusunitid" de Zona)
      wn_zone_name      VARCHAR2(100); -- Nombre de Zona
      wn_coordinatorid  NUMBER;        -- ID de Dealer ("swbusunitid" de Coordinador de Dealer, si es indirecto)
      wn_coord_name     VARCHAR2(100); -- Nombre de Coordinador de Dealer
      wn_supervisorid   NUMBER;        -- ID de Dealer ("swbusunitid" de Supervisor de Dealer, si es indirecto)
      wn_superv_name    VARCHAR2(100); -- Nombre de Supervisor de Dealer
      wn_sellerid       NUMBER;        -- ID de Dealer ("swpersonid" o "swprovidergrpid"), depende del valor de "an_seller_flag"
      wn_seller_name    VARCHAR2(100); -- Nombre de Consultor (vendedor)

      wc_list NP_TYPES_PKG.TYPCUR;
      wn_value NUMBER;
      wv_descr VARCHAR2(100);
      wv_error VARCHAR2(100);
   BEGIN
      wn_sellerid := an_sellerid;

      WEBSALES.NPSL_NEW_GENERAL_PKG.SP_GET_SALES_HIERARCHY_CODES (
      an_level, av_code, an_busunitid,
      wn_zoneid, wn_zone_name, wn_coordinatorid, wn_coord_name, wn_supervisorid, wn_superv_name, wn_sellerid, wn_seller_name, an_sellerid_flag
      );

      htp.p('
      <script language="JavaScript" src="/websales/Resource/BasicOperations.js"></script>
      <script language="javascript">
         <!--
         var Form = document.'||av_frm_name||';
         var wn_level = '||an_level||'; // Inicializamos variable para JavaScript
         var wv_code = "'||av_code||'"; // Inicializamos variable para JavaScript
         var wn_sellerid_flag = '||an_sellerid_flag||';

         function CleanZoneDependants() {
            if ( wn_sellerid_flag  >= 0 ) {
               DeleteSelectOptions(Form.v_consultor);
            }
            DeleteSelectOptions(Form.v_supervisor);
            DeleteSelectOptions(Form.v_coordinador);
         }
         function CleanCoordinatorDependants() {
            if ( wn_sellerid_flag  >= 0 ) {
               DeleteSelectOptions(Form.v_consultor);
            }
            DeleteSelectOptions(Form.v_supervisor);
         }

         function CleanSupervisorDependants() {
            if ( wn_sellerid_flag  >= 0 ) {
               DeleteSelectOptions(Form.v_consultor);
            }
         }

          function SetSalesObjDefaultValues() {
            var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_SET_SALESOBJ_DEFAULT_VALUES?"
               +"ans_busunitid='||an_busunitid||'"
               +"&an_user_level="+wn_level
               +"&av_user_code="+wv_code
               +"&an_user_zoneid='||wn_zoneid||'"
               +"&an_user_coordid='||wn_coordinatorid||'"
               +"&an_user_supervid='||wn_supervisorid||'"
               +"&an_user_sellerid='||wn_sellerid||'"
               +"&an_obj_zoneid='||an_obj_zoneid||'"
               +"&an_obj_coordid='||an_obj_coordinatorid||'"
               +"&an_obj_supervid='||an_obj_supervisorid||'"
               +"&an_obj_sellerid='||an_obj_sellerid||'"
               +"&an_sellerid_flag=" + wn_sellerid_flag
               +"&av_frm_name=" + Form.name
               +"&av_zone_field_name=" + Form.v_zona.name
               +"&av_coord_field_name=" + Form.v_coordinador.name
               +"&av_superv_field_name="  + Form.v_supervisor.name
               +"&av_seller_field_name=" + Form.v_consultor.name;

           parent.bottomFrame.location.replace(url);
         }

         function FillZoneDependants() {
            var an_zoneid = (Form.v_zona.length == null)?Form.v_zona.value:Form.v_zona.options[Form.v_zona.selectedIndex].value;
            CleanZoneDependants();
            if (an_zoneid != 0) {
               var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FILL_ZONE_DEPENDANTS?"
                  +"an_zoneid=" + an_zoneid
                  +"&wv_frm_name=" + Form.name
                  +"&wv_frm_coordinator_field_name="  + Form.v_coordinador.name
                  +"&wv_frm_seller_field_name=" + Form.v_consultor.name
                  +"&wn_sellerid_flag=" + wn_sellerid_flag;
               parent.bottomFrame.location.replace(url);
            }
         }

         function FillCoordinatorDependants() {
            var an_coordinatorid = (Form.v_coordinador.length == null)?Form.v_coordinador.value:Form.v_coordinador.options[Form.v_coordinador.selectedIndex].value;
            CleanCoordinatorDependants();
            if ( an_coordinatorid != 0 ) {
               var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FILL_COORDINATOR_DEPENDANTS?"
                  +"an_coordinatorid=" + an_coordinatorid
                  +"&wv_frm_name=" + Form.name
                  +"&wv_frm_supervisor_field_name=" + Form.v_supervisor.name
               parent.bottomFrame.location.replace(url);
            }
         }

         function FillSupervisorDependants() {
            var an_supervisorid = (Form.v_supervisor.length == null)?Form.v_supervisor.value:Form.v_supervisor.options[Form.v_supervisor.selectedIndex].value;
            CleanSupervisorDependants();
            if ( an_supervisorid != 0 && wn_sellerid_flag >= 0 ) {
               var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FILL_SUPERVISOR_DEPENDANTS?"
                  +"an_supervisorid="  + an_supervisorid
                  +"&wv_frm_name=" + Form.name
                  +"&wv_frm_seller_field_name=" + Form.v_consultor.name
                  +"&wn_sellerid_flag="+wn_sellerid_flag;
               parent.bottomFrame.location.replace(url);
            }
         }

         -->
      </script>

      <tr>
         <!-- Z O N A -->
         <!------------->

         <td align="left" valign="middle" class="CellLabel"><font color="#FF0000">*</font>Zona</td>
         <td align="left" valign="middle" class="CellContent">
         ');
         IF  wn_zoneid = 0 THEN
            htp.p('
            &nbsp;<select name="v_zona" onchange="javascript:FillZoneDependants();">
               <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            </select>');
         ELSE
            htp.p('&nbsp;'||wn_zone_name||'<input type="hidden" name="v_zona" value="'||wn_zoneid||'">');
         END IF;
         htp.p('
         </td>

         <!-- Coordinador -->
         <!------------------->
         <td align="left" valign="middle" class="CellLabel" >&nbsp;Coordinador</td>
         <!--td align="left" valign="middle" class="CellLabel" >&nbsp;Dealer</td-->
         <td align="left" valign="middle" class="CellContent">');
         --IF wn_coordinatorid = 0 AND av_code != 'D' THEN
         --IF wn_coordinatorid = 0 AND an_level <= 5 AND av_code != 'D' THEN
         IF wn_coordinatorid = 0 AND an_level <= 5 AND av_code != 'D' THEN
         --IF wn_coordinatorid = 0 AND an_level <= 5 AND av_code NOT IN ('D','DS') THEN
            htp.p('
            &nbsp;<select name="v_coordinador" onchange="javascript:FillCoordinatorDependants()">
               <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            </select>');
         ELSE
            htp.p('&nbsp;'||wn_coord_name||'<input type="hidden" name="v_coordinador" value="'||wn_coordinatorid||'">');
         END IF;
         htp.p('
         </td>
      </tr>
      <tr>
         <!-- Supervisor -->
         <!------------>
         <td align="left" valign="middle" class="CellLabel" >&nbsp;Supervisor</td>
         <!--td align="left" valign="middle" class="CellLabel" >&nbsp;Grupo</td-->
         <td align="left" valign="middle" class="CellContent">');
         IF wn_supervisorid = 0 AND an_level <= 6 AND av_code != 'D' THEN
            htp.p('
            &nbsp;<select name="v_supervisor" onchange="javascript:FillSupervisorDependants()">
               <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            </select>');
         ELSE
            htp.p('&nbsp;'||wn_superv_name||'<input type="hidden" name="v_supervisor" value="'||wn_supervisorid||'">');
         END IF;
             htp.p('
         </td>

         <!-- Consultor -->
         <!--------------->
         ');
         IF an_sellerid_flag >= 0 THEN
            htp.p('
            <td align="left" valign="middle" class="CellLabel">');
               IF an_level < 8 THEN
                  htp.p('<font color="red">*</font>Consultor/Ejecutivo');
               ELSE
                  htp.p('&nbsp;Consultor/Ejecutivo');
               END IF;
               htp.p('
            </td>
            <td align="left" valign="middle" class="CellContent">');
            IF wn_sellerid = 0 THEN
               htp.p('
               &nbsp;<select name="v_consultor" onchange="CheckSeller(this)">
                  <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
               </select>');
            ELSE
               htp.p('&nbsp;'||wn_seller_name||'<input type="hidden" name="v_consultor" value="'||wn_sellerid||'">');
            END IF;
            htp.p('
            </td>
            ');
         ELSE
            htp.p('
            <td align="left" valign="middle" class="CellLabel">&nbsp;</td>
            <td align="left" valign="middle" class="CellContent">&nbsp;<input type="hidden" name="v_consultor" value="-1"></td>
            ');
         END IF;
      htp.p('
      </tr>

      <script language="javascript">
         SetSalesObjDefaultValues();
         function CheckSeller(obj) {
         }
      </script>');

   EXCEPTION
      WHEN OTHERS THEN
         htp.p(sqlerrm);
   END PL_DRAW_SALESOBJ_DEF_VALUES;
--%>